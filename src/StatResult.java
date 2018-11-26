import java.util.ArrayList;
import java.util.List;

public class StatResult {
	// 股票代码
	private String code;

	// 股票名称
	private String name;

	// 总收益率
	private String total_roi;

	// 最大收益率
	private String max_roi;

	// 最大回撤率
	private String max_loss;

	// 正收益次数
	private String num_of_pos_gain;

	// 买入次数
	private String buy_times;

	// 卖出次数
	private String sell_times;

	// 平均获利周期
	private String average_of_pos_gain_period;

	// 策略的平均正收益
	private String average_of_pos_gain;

	// 策略正收益的比例
	private String percent_of_pos_gain;

	// 上次买入日期
	private String last_buy_date;

	// 当前的状态
	private String current_status;

	// 所有的买入点
	private String buy_dates;

	// 所有的卖出点
	private String sell_dates;
	
	// 交易信息
	private List<TradeInfo> trade_infos = new ArrayList<TradeInfo>();
	
	public String getCode() {
		return code;
	}

	public void setCode(String value) {
		code = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		name = value;
	}

	public String getTotal_roi() {
		return total_roi;
	}

	public void setTotal_rio(String value) {
		total_roi = value;
	}

	public String getNum_of_pos_gain() {
		return num_of_pos_gain;
	}

	public void setNum_of_pos_gain(String value) {
		num_of_pos_gain = value;
	}

	public String getMax_roi() {
		return max_roi;
	}

	public void setMax_roi(String value) {
		max_roi = value;
	}

	public String getMax_loss() {
		return max_loss;
	}

	public void setMax_loss(String value) {
		max_loss = value;
	}

	public String getSell_times() {
		return sell_times;
	}

	public void setSell_times(String value) {
		sell_times = value;
	}

	public String getBuy_times() {
		return buy_times;
	}

	public void setBuy_times(String value) {
		buy_times = value;
	}

	public String getLast_buy_date() {
		return last_buy_date;
	}

	public void setLast_buy_date(String value) {
		last_buy_date = value;
	}

	public String getAverage_of_pos_gain_period() {
		return average_of_pos_gain_period;
	}

	public void setAverage_of_pos_gain_period(String value) {
		average_of_pos_gain_period = value;
	}

	public String getAverage_of_pos_gain() {
		return average_of_pos_gain;
	}

	public void setAverage_of_pos_gain(String value) {
		average_of_pos_gain = value;
	}

	public String getPercent_of_pos_gain() {
		return percent_of_pos_gain;
	}

	public void setPercent_of_pos_gain(String value) {
		percent_of_pos_gain = value;
	}

	public String getCurrent_status() {
		return current_status;
	}

	public void setCurrent_status(String value) {
		current_status = value;
	}

	public String getBuy_dates() {
		return buy_dates;
	}

	public void setBuy_dates(String value) {
		buy_dates = value;
	}

	public String getSell_dates() {
		return sell_dates;
	}

	public void setSell_dates(String value) {
		sell_dates = value;
	}
	
	public List<TradeInfo> getTradeInfos() {
		return trade_infos;
	}
	
	public void setTradeInfos(List<TradeInfo> value) {
		
		for(TradeInfo tradeinfo:value)
		{
			TradeInfo info = new TradeInfo();
			info.setBuy_date(tradeinfo.getBuy_date());
			info.setBuy_price(tradeinfo.getBuy_price());
			info.setPeriod(tradeinfo.getPeriod());
			info.setRoi(tradeinfo.getRoi());
			info.setSell_date(tradeinfo.getSell_date());
			info.setSell_price(tradeinfo.getSell_price());
			
			trade_infos.add(info);
		}
	}
}
