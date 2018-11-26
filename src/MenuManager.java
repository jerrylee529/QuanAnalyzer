

import menu.Button;
import menu.ClickButton;
import menu.ComplexButton;
import menu.Menu;


public class MenuManager {
	/**
	 * 定义菜单结构
	 * 
	 * @return
	 */
	private static Menu getMenu() {
		ClickButton btn11 = new ClickButton();
		btn11.setName("买入个股");
		btn11.setType("click");
		btn11.setKey("stock_buy");

		ClickButton btn12 = new ClickButton();
		btn12.setName("持有个股");
		btn12.setType("click");
		btn12.setKey("stock_hold");

		ClickButton btn13 = new ClickButton();
		btn13.setName("术语解析");
		btn13.setType("click");
		btn13.setKey("comments");

		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName("查询");
		mainBtn1.setSub_buttons(new Button[] { btn11, btn12, btn13 });

		ClickButton mainBtn2 = new ClickButton();
		mainBtn2.setName("策略定制");
		mainBtn2.setType("click");
		mainBtn2.setKey("contact");

		Menu menu = new Menu();
		menu.setButton(new Button[] { mainBtn1, mainBtn2 });

		return menu;
	}
	
	/*
	 * 初始化菜单
	 * 
	 */
	public static void Init() {
	    String appid = "wx9ac14d5576687eee";
	    
	    String appkey = "6b325d0ce852df7b067bf06a6518d168";

		// 调用接口获取凭证
		AccessToken token = CommonUtil.getToken(appid, appkey);

		if (null != token) {
			System.out.print("Access token: " + token.getAccess_token());
			
			// 创建菜单
			boolean result = MenuUtil.createMenu(getMenu(), token.getAccess_token());

			// 判断菜单创建结果
			if (result)
				System.out.print("菜单创建成功！");
			else
				System.out.print("菜单创建失败！");
		}
	}
}