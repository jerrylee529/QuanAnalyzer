
import java.io.File;
import java.io.FileNotFoundException;   
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;    
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.SAXParser;   
import javax.xml.parsers.SAXParserFactory;   

import message.resp.Article;
import message.resp.NewsMessage;

import org.xml.sax.InputSource;



public class MessageUtil {
	// 历史数据的路径
	public static String Data_Path = "C:\\Stock\\Product\\";
	
	// 欢迎文件的路径
	public static String Welcome_Path = "C:\\Stock\\Product\\welcome.txt";

	// 帮助文件的路径
	public static String Help_Path = "C:\\Stock\\Product\\help.txt";

	// 服务的url
	public static String Service_URL = "http://115.159.104.225/quananalyzer/";
	
	// 网页输出的路径
	public static String Page_Url_Path = "c:\\apache-tomcat-8.0.24\\webapps\\quananalyzer\\";

	// 备注文件
	public static String Comments_File_Path = "C:\\Stock\\Product\\comments.txt";

	// 联系人
	public static String Contact_File_Path = "C:\\Stock\\Product\\contact.txt";

	// 策略解释文件
	public static String Strategy_File_Path = "C:\\Stock\\Product\\strategies.txt";
	
	// 微信消息键值定义
	public static final String ToUserName = "ToUserName";  
    public static final String FromUserName = "FromUserName";  
    public static final String CreateTime = "CreateTime";  
    public static final String MsgType = "MsgType";  
    public static final String Content = "Content";  
    public static final String MsgId = "MsgId";  
    public static final String PicUrl = "PicUrl"; 
    public static final String MediaId = "MediaId"; 
    public static final String Format = "Format"; 
    public static final String Recognition = "Recognition"; 
    public static final String ThumbMediaId = "ThumbMediaId"; 
    public static final String Location_X = "Location_X"; 
    public static final String Location_Y = "Location_Y"; 
    public static final String Scale = "Scale"; 
    public static final String Label = "Label"; 
    public static final String Title = "Title"; 
    public static final String Description = "Description"; 
    public static final String Url = "Url";
    
    // 微信事件键值定义
    public static final String Event = "Event"; 
    public static final String EventKey = "EventKey"; 
    public static final String Ticket = "Ticket"; 
    public static final String Latitude = "Latitude"; 
    public static final String Longitude = "Longitude"; 
    public static final String Precision = "Precision"; 
	
	// 微信请求消息类型
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";  
    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";  
    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";  
    public static final String REQ_MESSAGE_TYPE_LINK = "link";  
    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";
    public static final String REQ_MESSAGE_TYPE_VIDEO = "video";
    public static final String REQ_MESSAGE_TYPE_SHORTVIDEO = "shortvideo";
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";  
	
	// 微信回复消息类型
	public static final String RSP_MESSAGE_TYPE_TEXT = "text";  
	public static final String RSP_MESSAGE_TYPE_NEWS = "news";
	public static final String RSP_MESSAGE_TYPE_IMAGE = "image";  
    public static final String RSP_MESSAGE_TYPE_LOCATION = "location";  
    public static final String RSP_MESSAGE_TYPE_LINK = "link";  
    public static final String RSP_MESSAGE_TYPE_VOICE = "voice";
    public static final String RSP_MESSAGE_TYPE_VIDEO = "video";
    public static final String RSP_MESSAGE_TYPE_SHORTVIDEO = "shortvideo";
    public static final String RSP_MESSAGE_TYPE_EVENT = "event";  

    // 微信事件类型
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe"; 
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe"; 
    public static final String EVENT_TYPE_CLICK = "CLICK"; 
    
    private static StatResultDataset statResultDataset = null;

    static {
    	//再说一下加载方法:
    	InputStream in = MessageUtil.class.getClassLoader().getResourceAsStream("qa.properties");
    	Properties properties = new Properties();
    	try {
			properties.load(in);
			
			Data_Path = properties.getProperty("Data_Path");
			Welcome_Path = properties.getProperty("Welcome_Path");
			Help_Path = properties.getProperty("Help_Path");
			Service_URL = properties.getProperty("Service_URL");
			Page_Url_Path = properties.getProperty("Page_Url_Path");
			Comments_File_Path = properties.getProperty("Comments_File_Path");
			Contact_File_Path = properties.getProperty("Contact_File_Path");
			Strategy_File_Path = properties.getProperty("Strategy_File_Path");
			
			statResultDataset = new StatResultDataset(properties);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}    	
    }
    
    // 读取xml信息到map
	public static Map<String, String> parseXML(String strXML) throws Exception {
		
		Map<String, String> ret = null;
		
		try {   
            SAXParserFactory saxfac = SAXParserFactory.newInstance();
            
            SAXParser saxparser = saxfac.newSAXParser();   
            
            WeiXinMsgHandler tsax = new WeiXinMsgHandler();   
            
            saxparser.parse(new InputSource(new StringReader(strXML)), tsax);   
            
            ret = tsax.GetList();
        } catch (Exception e) {   
            e.printStackTrace();   
        }
		
		return ret;
	}
	
	// 将map内容写入xml信息
	public static String mapToXML(Map<String, String> map) throws Exception
	{
		StringBuffer str = new StringBuffer();  
        str.append("<xml>");  
        str.append("<ToUserName><![CDATA[" + map.get(MessageUtil.ToUserName) + "]]></ToUserName>");  
        str.append("<FromUserName><![CDATA[" + map.get(MessageUtil.FromUserName) + "]]></FromUserName>");  
        str.append("<CreateTime>" + map.get(MessageUtil.CreateTime) + "</CreateTime>");  
        str.append("<MsgType><![CDATA[" + map.get(MessageUtil.MsgType) + "]]></MsgType>");  
        str.append("<Content><![CDATA[" + map.get(MessageUtil.Content) + "]]></Content>");  
        str.append("</xml>"); 
        
        System.out.println(str.toString());  
		
		return str.toString();
	}
	
	// 从文件中读取信息
	private static String readMessageFromFile(String filename) {
		String str = null;
		
		File file = new File(filename);
		
   		try {
			FileReader reader = new FileReader(file);
            int fileLen = (int)file.length();
            char[] chars = new char[fileLen];
            reader.read(chars);
            str = String.valueOf(chars);
            reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
   		return str;
	}
	
	// 获得欢迎信息
	public static String getWelcomeMessage() {
		return readMessageFromFile(Welcome_Path);
	}
	
	// 获得帮助信息
	public static String getHelpMessage() {
		return readMessageFromFile(Help_Path);
	}

	/*
	 *  获取策略分析结果
	 */
	public static String getResult(String code, String strategy) {
		StatResult sr = statResultDataset.getStatResult(code, strategy);
		
		if (sr == null) {
			return "对不起，无法找到相关股票的策略分析结果";
		}
		
		StringBuffer str = new StringBuffer();  
        str.append("股票代码: " + sr.getCode() + "\r\n");  
        str.append("股票名称: " + sr.getName() + "\r\n");  
        str.append("策略总收益率: " + sr.getTotal_roi() + "%\r\n");  
        str.append("最大收益率: " + sr.getMax_roi() + "%\r\n");  
        str.append("最大回撤: " + sr.getMax_loss() + "%\r\n");  
        str.append("正收益的比例: " + sr.getPercent_of_pos_gain() + "%\r\n");  
        str.append("正收益的平均周期: " + sr.getAverage_of_pos_gain_period() + "天\r\n");  
        str.append("正收益率的平均值: " + sr.getAverage_of_pos_gain() + "%\r\n");  
        str.append("上一次买入日期: " + sr.getLast_buy_date() + "\r\n");  
        str.append("所有买入日期: " + sr.getBuy_dates() + "\r\n");  
        str.append("所有卖出日期: " + sr.getSell_dates() + "\r\n");  
        
        String status;
        
        if (sr.getCurrent_status().equalsIgnoreCase("0")) {
        	status = "买入";
        } else if (sr.getCurrent_status().equalsIgnoreCase("1")) {
        	status = "卖出";
        } else if (sr.getCurrent_status().equalsIgnoreCase("2")) {
        	status = "持有";
        }
        else {
        	status = "空仓";
        }
        
        str.append("当前状态: " + status);  
        
        return str.toString();
	}

	/*
	 *  获取交易信息
	 */
	public static String getTradeInfo(String code) {
		StatResult sr = statResultDataset.getStatResult(code, "");
		
		if (sr == null) {
			return "对不起，无法找到相关股票的策略分析结果";
		}

		StringBuffer str = new StringBuffer();  

        str.append("股票代码: " + sr.getCode() + "\r\n");  
        str.append("股票名称: " + sr.getName() + "\r\n");  
		
		List<TradeInfo> list = sr.getTradeInfos();
		
		int i = 1;
		for (TradeInfo tradeinfo : list) {
			str.append("第" + i + "次:\r\n");
			str.append("买入日期: " + tradeinfo.getBuy_date() + " 买入价格: " + tradeinfo.getBuy_price() + "\r\n");
			str.append("卖出日期: " + tradeinfo.getSell_date() + " 卖出价格: " + tradeinfo.getSell_price() + "\r\n");
			str.append("收益率: " + tradeinfo.getRoi() + "% 持有周期: " + tradeinfo.getPeriod() + "天\r\n");
			i++;
		}
        
        return str.toString();
	}
	
	/*
	 * 获取交易详细信息
	 */
	public static void generateTradeInfo(String code, List<Article> articles) {
		StatResult sr = statResultDataset.getStatResult(code, "");
		
		if (sr == null) {
			return;
		}
		
		String strUrl = Service_URL + code + ".htm";
				
		Article article = new Article();

		article.setTitle(sr.getCode() + " " + sr.getName());
		article.setDescription(getResult(code, ""));
		article.setPicUrl("");
		article.setUrl(strUrl);
			
		articles.add(article);
	}
	
	/*
	 * 生成交易信息页面
	 */
	public static void generateTradeInfoPage(String code) {
		StatResult sr = statResultDataset.getStatResult(code, "");
		
		if (sr == null) {
			return;
		}
		
		List<TradeInfo> list = sr.getTradeInfos();
		
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf8\"/></head>");
		sb.append("<body>");
		sb.append("<h1 align=center>" + getCurrentDate() + " " + sr.getCode() + " " + sr.getName() + "</h1>");
		sb.append("<table width=98% border=1 cellpadding=0 cellspacing=2>");
		sb.append("<tr>");
		sb.append("<th align=center>序号<th align=center>买入日期<th align=center>买入价格<th align=center>卖出日期<th align=center>卖出价格<th align=center>收益率%<th align=center>持有周期(天)");
		sb.append("</tr>");
		
		int i = 1;
		for (TradeInfo tradeinfo : list) {
			sb.append("<tr>");
			sb.append("<td align=center>" + i + "</td>");
			sb.append("<td align=center>" + tradeinfo.getBuy_date() + "</td>");
			sb.append("<td align=center>" + tradeinfo.getBuy_price() + "</td>");
			sb.append("<td align=center>" + tradeinfo.getSell_date() + "</td>");
			sb.append("<td align=center>" + tradeinfo.getSell_price() + "</td>");
			sb.append("<td align=center>" + tradeinfo.getRoi() + "</td>");
			sb.append("<td align=center>" + tradeinfo.getPeriod() + "</td>");
			sb.append("</tr>");
			i++;
		}
		
		sb.append("</table>");
		sb.append("</body></html>");
				
		System.out.println(sb.toString());
		
		String filename = Page_Url_Path + code + ".htm";
		File file = new File(filename);
		
   		try {
   			/*
			FileWriter writer = new FileWriter(file);
			writer.write(sb.toString());
			writer.close();
			*/
   			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
            out.write(sb.toString());
            out.flush();
            out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 *  获取术语注释
	 */
	public static String getComments() {
		return readMessageFromFile(Comments_File_Path);
	}

	/*
	 *  获取联系方式
	 */
	public static String getContact() {
		return readMessageFromFile(Contact_File_Path);
	}
	
	/*
	 *  获取策略列表
	 */
	public static String getStrategies() {
		return readMessageFromFile(Strategy_File_Path);
	}
	
	/*
	 *  获取买入前5位的股票
	 */
	public static String getBuyTop5() {
		List<StatResult> list = statResultDataset.getBuy_top5();
		
		if (list.size() == 0) {
			return "当前暂时没有买入股票";
		}
		
		StringBuffer str = new StringBuffer();  
		str.append("数据日期:" + getCurrentDate() + "\r\n");
        str.append("买入股票:" + "\r\n");
        
		int i = 1;
		for (int j = 0; j < list.size(); j++) {
			StatResult sr = list.get(j);
	        str.append(i + ". " + sr.getCode() + " " + sr.getName() + "\r\n");  
	        i++;

	        if (i > 5) {
	        	break;
	        }
       }
		
		return str.toString();
	}
	
	/*
	 *  获取持仓前5位的股票
	 */
	public static String getHoldTop5() {
		List<StatResult> list = statResultDataset.getHold_top5();
		
		if (list.size() == 0) {
			return "当前暂时没有持有股票";
		}
		
		StringBuffer str = new StringBuffer();  
		str.append("数据日期:" + getCurrentDate() + "\r\n");
		str.append("持有股票:" + "\r\n");

		int i = 1;
		for (int j = 0; j < list.size(); j++) {
			StatResult sr = list.get(j);
	        str.append(i + ". " + sr.getCode() + " " + sr.getName() + "\r\n");  
	        i++;

	        if (i > 5) {
	        	break;
	        }
       }
                
        return str.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getSellTop5() {
		List<StatResult> list = statResultDataset.getSell_top5();
		
		if (list.size() == 0) {
			return "当前暂时没有卖出股票";
		}
		
		StringBuffer str = new StringBuffer();  
		str.append("数据日期:" + getCurrentDate() + "\r\n");
		str.append("持有股票:" + "\r\n");

		int i = 1;
		for (int j = 0; j < list.size(); j++) {
			StatResult sr = list.get(j);
	        str.append(i + ". " + sr.getCode() + " " + sr.getName() + "\r\n");  
	        i++;

	        if (i > 5) {
	        	break;
	        }
       }
                
        return str.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getPE_top5() {
		List<ReportInfo> list = statResultDataset.getReportData("thebestpe_report");
		
		if (list.size() == 0) {
			return "当前暂时没有相关数据 ";
		}
		
		StringBuffer str = new StringBuffer();  
		str.append("日期:" + getCurrentDate() + "\r\n");
		str.append("PE排名:" + "\r\n");

		int i = 1;
		for(ReportInfo info : list){
	        str.append(i + ". " + info.getCode() + " " + info.getName() + " " + String.format("%.2f", info.getPE()) + "\r\n");  
	        
	        i++;

	        if (i > 5) {
	        	break;
	        }
		}
		
		return str.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getROE_top5() {
		List<ReportInfo> list = statResultDataset.getReportData("thebestroe_report");
		
		if (list.size() == 0) {
			return "当前暂时没有相关数据 ";
		}
		
		StringBuffer str = new StringBuffer();  
		str.append("日期:" + getCurrentDate() + "\r\n");
		str.append("ROE排名:" + "\r\n");

		int i = 1;
		for(ReportInfo info : list){
	        str.append(i + ". " + info.getCode() + " " + info.getName() + " " + String.format("%.2f", info.getROE()) + "\r\n");  
			
	        i++;

	        if (i > 5) {
	        	break;
	        }
		}
		
		return str.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getPB_top5() {
		List<ReportInfo> list = statResultDataset.getReportData("thebestpb_report");
		
		if (list.size() == 0) {
			return "当前暂时没有相关数据 ";
		}
		
		StringBuffer str = new StringBuffer();  
		str.append("日期:" + getCurrentDate() + "\r\n");
		str.append("PB排名:" + "\r\n");

		int i = 1;
		for(ReportInfo info : list){
	        str.append(i + ". " + info.getCode() + " " + info.getName() + " " + String.format("%.2f", info.getPB()) + "\r\n");  
	        
	        i++;

	        if (i > 5) {
	        	break;
	        }
		}
		
		return str.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getPEG_top5() {
		List<ReportInfo> list = statResultDataset.getReportData("thebestpeg_report");
		
		if (list.size() == 0) {
			return "当前暂时没有相关数据 ";
		}
		
		StringBuffer str = new StringBuffer();  
		str.append("日期:" + getCurrentDate() + "\r\n");
		str.append("PEG排名:" + "\r\n");

		int i = 1;
		for(ReportInfo info : list){
	        str.append(i + ". " + info.getCode() + " " + info.getName() + " " + String.format("%.2f", info.getPEG()) + "\r\n");  
	        
	        i++;

	        if (i > 5) {
	        	break;
	        }
		}
		
		return str.toString();
		
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getBelow_MA_60() {
		List<ReportInfo> list = statResultDataset.getReportData("belowma_60_20");
		
		if (list.size() == 0) {
			return "当前暂时没有相关数据 ";
		}
		
		StringBuffer str = new StringBuffer();  
		str.append("日期:" + getCurrentDate() + "\r\n");
		str.append("低于60日线(20%以上):" + "\r\n");

		int i = 1;
		for(ReportInfo info : list){
	        str.append(i + ". " + info.getCode() + " " + info.getName() + "\r\n");  
	        
	        i++;

	        if (i > 5) {
	        	break;
	        }
		}
		
		return str.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getBeyond_MA_60() {
		List<ReportInfo> list = statResultDataset.getReportData("mafilter_60");
		
		if (list.size() == 0) {
			return "当前暂时没有相关数据 ";
		}
		
		StringBuffer str = new StringBuffer();  
		str.append("日期:" + getCurrentDate() + "\r\n");
		str.append("突破60日线:" + "\r\n");

		int i = 1;
		for(ReportInfo info : list){
	        str.append(i + ". " + info.getCode() + " " + info.getName() + "\r\n");  
	        
	        i++;

	        if (i > 5) {
	        	break;
	        }
		}
		
		return str.toString();
		
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getBeyond_MA_20() {
		
		List<ReportInfo> list = statResultDataset.getReportData("mafilter_20");
		
		if (list.size() == 0) {
			return "当前暂时没有相关数据 ";
		}
		
		StringBuffer str = new StringBuffer();  
		str.append("日期:" + getCurrentDate() + "\r\n");
		str.append("突破20日线:" + "\r\n");

		int i = 1;
		for(ReportInfo info : list){
	        str.append(i + ". " + info.getCode() + " " + info.getName() + "\r\n");  
			
	        i++;

	        if (i > 5) {
	        	break;
	        }
		}
		
		return str.toString();		
	}
	
	/*
	 * 获取事件处理结果
	 * 
	 */
	public static String getEventRsp(String eventKey) {
		String str = null;
		
		if (eventKey.equals("stock_buy")) {
			str = getBuyTop5();
		} else if (eventKey.equals("stock_hold")) {
			str = getHoldTop5();
		} else if (eventKey.equals("comments")) {
			str = getComments();
		} else if (eventKey.equals("contact")) {
			str = getContact();
		} else {
			str = getWelcomeMessage();
		}
		
		return str;
	}
	
	/*
	 * 生成图文信息, 策略结果和交易明细
	 */
	public static String getComplexTradeInfo(Map<String, String> mapReq, String code) {
		List<Article> articles = new ArrayList<Article>();
		
		// 生成交易信息
		generateTradeInfo(code, articles);
		
		// 生成交易信息明细页面
		generateTradeInfoPage(code);
		
		NewsMessage news = new NewsMessage();
		news.setToUserName(mapReq.get(FromUserName));
		news.setFromUserName(mapReq.get(ToUserName));
		news.setCreateTime(new Date().getTime());
		news.setMsgType(MessageUtil.RSP_MESSAGE_TYPE_NEWS);
		news.setArticleCount(articles.size());
		news.setArticles(articles);
		
		StringBuffer sbMsg = new StringBuffer();
		news.generateMessage(sbMsg);
		
		return sbMsg.toString();
	}
	
	/*
	 * 获取当前日期
	 */
	private static String getCurrentDate() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateNowStr = sdf.format(d);
		System.out.println("格式化后的日期：" + dateNowStr);
		
		return dateNowStr;
	}
}
