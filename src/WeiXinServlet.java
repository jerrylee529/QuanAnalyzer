
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  
import java.io.Writer;  
import java.util.ArrayList;  
import java.util.Collections;  
import java.util.Date;  
import java.util.HashMap;
import java.util.List; 
import java.util.Map;
import java.io.IOException;
import java.io.PrintWriter;  

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class WeiXinServlet
 */
@WebServlet("/WeiXinServlet")
public class WeiXinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	  
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WeiXinServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init(ServletConfig config) throws ServletException {
        //new Thread(new AccessTokenThread()).start();

    	/*
    	String str = null;
    	String content = "300204,D";
    	String[] segments = content.split(",");
		if (segments.length == 1) { // 股票策略运行结果
			str = MessageUtil.getResult(segments[0], "");
		} else if (segments.length == 2 && segments[1].equalsIgnoreCase("D")) { // 买卖详细信息
			str = MessageUtil.getTradeInfo(segments[0]);
		} else {
			str = "welcome";
		}
		
		System.out.println(str);
		*/
		//System.out.println(MessageUtil.Data_Path);

    	//System.out.println(MessageUtil.getComments());
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {  
            // 微信加密签名  
            String signature = request.getParameter("signature");  
            // 时间戳  
            String timestamp = request.getParameter("timestamp");  
            // 随机数  
            String nonce = request.getParameter("nonce");  
            // 随机字符串  
            String echostr = request.getParameter("echostr");  
  
            PrintWriter out = response.getWriter();  
            
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败  
            if (SecurityUtil.checkSignature(signature, timestamp, nonce)) {  
                out.print(echostr);  
            }
            else
            {
            	out.print("test error");
            }
            
            out.close(); 
            
            out = null;  
  
        } catch (Exception e) {  
        }  
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// log.info("收到POST请求："+(new Date()));
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		// 调用核心业务类接收消息、处理消息
		String respMessage = CoreService.processRequest(request);

		// 响应消息
		PrintWriter out = response.getWriter();
		out.print(respMessage);
		out.close(); 
		
		// TODO:写微信平台推送过来的各种信息的处理逻辑
	}

}
