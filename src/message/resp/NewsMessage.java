package message.resp;

import java.util.List;

/**
 * �ı���Ϣ
 * 
 * @author liufeng
 * @date 2013-09-11
 */
public class NewsMessage extends BaseMessage {
	// ͼ����Ϣ��������Ϊ10������
	private int ArticleCount;
	// ����ͼ����Ϣ��Ϣ��Ĭ�ϵ�һ��itemΪ��ͼ
	private List<Article> Articles;

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<Article> getArticles() {
		return Articles;
	}

	public void setArticles(List<Article> articles) {
		Articles = articles;
	}
	
	protected void fillMsgContent(StringBuffer msg) {
		msg.append("<ArticleCount>" + ArticleCount + "</ArticleCount>");
		msg.append("<Articles>");
		
		for(Article article:Articles) {
			msg.append("<item>");
			msg.append("<Title><![CDATA[" + article.getTitle() + "]]></Title>");
			msg.append("<Description><![CDATA[" + article.getDescription() + "]]></Description>");
			msg.append("<PicUrl><![CDATA[" + article.getPicUrl() + "]]></PicUrl>");
			msg.append("<Url><![CDATA[" + article.getUrl() + "]]></Url>");
			msg.append("</item>");
		}
		
		msg.append("</Articles>");
	}
}
