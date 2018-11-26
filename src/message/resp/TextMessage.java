package message.resp;


/**
 * �ı���Ϣ
 * 
 * @author liufeng
 * @date 2013-09-11
 */
public class TextMessage extends BaseMessage {
	// �ظ�����Ϣ����
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
	
	protected void fillMsgContent(StringBuffer msg) {
		msg.append("<Content><![CDATA[" + Content + "]]></Content>");
	}
}
