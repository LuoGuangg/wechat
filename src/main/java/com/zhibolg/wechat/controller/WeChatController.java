package com.zhibolg.wechat.controller;

import java.util.Arrays;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.test.web.client.RequestMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thoughtworks.xstream.XStream;
import com.zhibolg.wechat.util.Sha1;

@Controller
@RequestMapping(value = "wechat")
public class WeChatController {
	
			@RequestMapping(value = "index", method = RequestMethod.GET)
			@ResponseBody
			public String index(HttpServletRequest req) {
				System.out.println("准备");
				String token = "lgwsy5201314";
				// 根据api说明，获取上述四个参数
				String signature = req.getParameter("signature");
				String timestamp = req.getParameter("timestamp");
				String nonce = req.getParameter("nonce");
				String echostr = req.getParameter("echostr");
				
				String[] parms = new String[] { token, timestamp, nonce };// 将需要字典序排列的字符串放到数组中
				Arrays.sort(parms);// 按照api要求进行字典序排序【百度：什么是字典序排序】
		
				// 第二步:将三个参数字符串拼接成一个字符串进行sha1加密【百度：java sha1加密】
				// 拼接字符串
				String parmsString = "";// 注意，此处不能=null。
				for (int i = 0; i < parms.length; i++) {
				  parmsString += parms[i];
				}
				// sha1加密
				String mParms = null;// 加密后的结果
		
				 //该地方是sha1加密的实现，不再贴代码    
		
				mParms = Sha1.encode(parmsString);// 加密结果
		
				/*
				 * api要求： 若确认此次GET请求来自微信服务器，请原样返回echostr参数内容， 则接入生效， 成为开发者成功，否则接入失败。
				 */
				// 第三步： 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信接入成功。
				System.out.println(mParms + "---->" + signature);
				if (mParms.equals(signature)) {
				  // System.out.println(TAG + ":" + mParms + "---->" + signature);
				  return echostr;
				} else {
				  // 接入失败,不用回写
				   System.out.println( "接入失败");
				}  
				return "error";
			}
			
			@RequestMapping(value = "index", method = RequestMethod.POST)
			@ResponseBody
			public String message(HttpServletRequest req) {
				System.out.println("微信发来消息");
				 // 处理接收消息    
		        ServletInputStream in = req.getInputStream();    
		        // 将POST流转换为XStream对象    
		        XStream xs = SerializeXmlUtil.createXstream();    
		        xs.processAnnotations(InputMessage.class);    
		        xs.processAnnotations(OutputMessage.class);    
		        // 将指定节点下的xml节点数据映射为对象    
		        xs.alias("xml", InputMessage.class);    
		        // 将流转换为字符串    
		        StringBuilder xmlMsg = new StringBuilder();    
		        byte[] b = new byte[4096];    
		        for (int n; (n = in.read(b)) != -1;) {    
		            xmlMsg.append(new String(b, 0, n, "UTF-8"));    
		        }    
		        // 将xml内容转换为InputMessage对象    
		        InputMessage inputMsg = (InputMessage) xs.fromXML(xmlMsg.toString());    
		    
		        String servername = inputMsg.getToUserName();// 服务端    
		        String custermname = inputMsg.getFromUserName();// 客户端    
		        long createTime = inputMsg.getCreateTime();// 接收时间    
		        Long returnTime = Calendar.getInstance().getTimeInMillis() / 1000;// 返回时间    
		        
		        
				return "error";
			}
}
