import java.util.HashMap;
import java.util.Map;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


class WeiXinMsgHandler extends DefaultHandler {

	private StringBuffer buf;
	
	//存储正在解析的元素的数据
	private Map<String,String> map = new HashMap<String, String>();

	public WeiXinMsgHandler() {
		super();
	}

	public Map<String, String> GetList()
	{
		return map;
	}
	
	public void startDocument() throws SAXException {
		buf = new StringBuffer();
		System.out.println("*******开始解析XML*******");
	}

	public void endDocument() throws SAXException {
		System.out.println("*******XML解析结束*******");
	}

	public void endElement(String namespaceURI, String localName,
			String fullName) throws SAXException {
		System.out.println("节点=" + fullName + "\tvalue=" + buf + " 长度="
				+ buf.length());
		
		System.out.println();
		
		if(fullName != "xml")
		{
			map.put(fullName, buf.toString());
		}
		
		buf.delete(0, buf.length());
	}

	public void characters(char[] chars, int start, int length)
			throws SAXException {
		// 将元素内容累加到StringBuffer中
		buf.append(chars, start, length);
	}
}