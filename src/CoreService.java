
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * 核心服务类
 * 
 * @author Jerry Lee
 * @date 2015-08-13
 */
public class CoreService {
	
	private static String welcomeMsg = null;
	 	
   	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
		
		String respMessage = null;
		
 		try {
			// 默认返回的文本消息内容
			String respContent = "请求处理异常，请稍候尝试！";
			
			// 读取xml字符串
			StringBuffer sb = new StringBuffer();  
	        InputStream is = request.getInputStream();  
	        InputStreamReader isr = new InputStreamReader(is, "UTF-8");  
	        BufferedReader br = new BufferedReader(isr);  
	        String s = "";  
	        while ((s = br.readLine()) != null) {  
	            sb.append(s);  
	        }  

	        String xml = sb.toString(); //次即为接收到微信端发送过来的xml数据
	        
			System.out.println("request:" + xml);

			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXML(xml);

			if(requestMap == null)
			{
				return respContent;
			}
			
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get(MessageUtil.FromUserName);
			
			// 公众帐号
			String toUserName = requestMap.get(MessageUtil.ToUserName);
			
			// 消息类型
			String msgType = requestMap.get(MessageUtil.MsgType);
			
			// 获取欢迎信息
			if (welcomeMsg == null) {
				welcomeMsg = MessageUtil.getWelcomeMessage();
			}
			
			// 文本信息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				String content = requestMap.get(MessageUtil.Content);

				if (content.equalsIgnoreCase("0")) {
					respContent = MessageUtil.getHelpMessage();	
				} else if (content.equalsIgnoreCase("1")) {
					respContent = MessageUtil.getBuyTop5();
				} else if (content.equalsIgnoreCase("2")) {
					respContent = MessageUtil.getHoldTop5();
				} else if (content.equalsIgnoreCase("3")) {
					respContent = MessageUtil.getSellTop5();
				} else if (content.equalsIgnoreCase("4")) {
					respContent = MessageUtil.getPB_top5();
				} else if (content.equalsIgnoreCase("5")) {
					respContent = MessageUtil.getPE_top5();
				} else if (content.equalsIgnoreCase("6")) {
					respContent = MessageUtil.getROE_top5();
				} else if (content.equalsIgnoreCase("7")) {
					respContent = MessageUtil.getPEG_top5();
				} else if (content.equalsIgnoreCase("8")) {
					respContent = MessageUtil.getBelow_MA_60();
				} else if (content.equalsIgnoreCase("9")) {
					respContent = MessageUtil.getBeyond_MA_20();
				} else if (content.equalsIgnoreCase("10")) {
					respContent = MessageUtil.getBeyond_MA_60();
				} else if (content.equalsIgnoreCase("11")) {
					respContent = MessageUtil.getComments();
				} else if (content.equalsIgnoreCase("12")) {
					respContent = MessageUtil.getContact();
				} else {
					String[] segments = content.split(",");
					
					if (segments.length == 1) { // 股票策略运行结果
						//respContent = MessageUtil.getResult(segments[0], "");
						return MessageUtil.getComplexTradeInfo(requestMap, segments[0]);
					} else if (segments.length == 2 && segments[1].equalsIgnoreCase("D")) { // 买卖详细信息
						respContent = MessageUtil.getTradeInfo(segments[0]);
					} else if (segments.length == 2 && segments[1].equalsIgnoreCase("C")) { // 买卖详细信息
						return MessageUtil.getComplexTradeInfo(requestMap, segments[0]);
					} else {
						respContent = welcomeMsg;
					}
				}
			} else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)){ // 事件信息
				// 事件类型
				String eventType = requestMap.get(MessageUtil.Event);
				
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					respContent = welcomeMsg;
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO 暂不做处理
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					respContent = MessageUtil.getEventRsp(requestMap.get(MessageUtil.EventKey));
				} else {
					respContent = welcomeMsg;
				}
			} else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
 				respContent = welcomeMsg;
 			} else {
 				respContent = welcomeMsg;
 			}

			// 回复文本消息
			Map<String, String> map = new HashMap<String, String>();
			map.put(MessageUtil.ToUserName, fromUserName);
			map.put(MessageUtil.FromUserName, toUserName);
			map.put(MessageUtil.CreateTime, new Long(new Date().getTime()).toString());
			map.put(MessageUtil.MsgType, MessageUtil.RSP_MESSAGE_TYPE_TEXT);
			map.put(MessageUtil.Content, respContent);
			
			respMessage = MessageUtil.mapToXML(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return respMessage;
	}
}