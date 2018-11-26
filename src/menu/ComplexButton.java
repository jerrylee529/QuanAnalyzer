package menu;

public class ComplexButton extends Button {
	private Button[] sub_buttons;
	
	public Button[] getSub_buttons() {
		return  sub_buttons;
	}
	
	public void setSub_buttons(Button[] value) {
		sub_buttons = value;
	}
}
