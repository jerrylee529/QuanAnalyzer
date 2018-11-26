import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class StatResultDataset {
	private final int CONSTANT_COLOMNS = 16;
	private String Strategy_Result_File_Path = "C:\\Stock\\product\\strategyresult.srf";
	private String Buy_Top5_File_Path = "C:\\Stock\\product\\buy_top5.srf";
	private String Hold_Top5_File_Path = "C:\\Stock\\product\\hold_top5.srf";
	private String Sell_Top5_File_Path = "C:\\Stock\\product\\sell_top5.srf";
	private String Result_File_Folder_Path = "C:\\Stock\\product";
	
	private Map<String, StatResult> map_statresult = new LinkedHashMap<String, StatResult>();	
	
	public StatResultDataset(Properties properties) {
		Strategy_Result_File_Path = properties.getProperty("Strategy_Result_File_Path");
		Buy_Top5_File_Path = properties.getProperty("Buy_Top5_File_Path");
		Sell_Top5_File_Path = properties.getProperty("Sell_Top5_File_Path");
		Hold_Top5_File_Path = properties.getProperty("Hold_Top5_File_Path");
		Result_File_Folder_Path = properties.getProperty("Result_File_Folder_Path");		
	}
	
    /**
     * 
     * @param code: 股票代码
     * @param strategy: 策略类型
     * @return 返回給用户的信息
     */
	public StatResult getStatResult(String code, String strategy) {
		if (map_statresult.size() == 0) {
			List<StatResult> list = new ArrayList<StatResult>();
     			
			load(Strategy_Result_File_Path, list);
			
			for(int i = 0; i < list.size(); i++)  
	        {  
				StatResult sr = list.get(i);
				
				map_statresult.put(sr.getCode(), sr);
	        }  
		}
								        
        return map_statresult.get(code);
	}
	
		
	/**
	 * 
	 * @param report
	 * @return
	 */
	public List<ReportInfo> getReportData(String reportPrefix) {
		List<ReportInfo> top5 = new ArrayList<ReportInfo>();
		
		String filePath = Result_File_Folder_Path + "\\" + getLatestReportFile(Result_File_Folder_Path, reportPrefix);
		
		System.out.println(filePath);
		
		loadReport(filePath, top5);

		return top5;
	}
	
	
	/**
	 * 
	 * @param report
	 * @return
	 */
	public String getLatestReportFile(String path, String reportPrefix) {
		List<File> files = Arrays.asList(new File(path).listFiles());
		Collections.sort(files, new Comparator<File>(){
		    @Override
		    public int compare(File o1, File o2) {
		    	 if (o1.lastModified() < o2.lastModified()) {
                     return 1;
                 } else if (o1.lastModified() == o2.lastModified()) {
                     return 0;
                 } else {
                     return -1;
                 }
		   }
		});
		 
		for(File f : files) {
		    if (f.getName().startsWith(reportPrefix)) {
		    	return f.getName();
		    }
		}
		
		return "";
	}
	
    /**
     * 	
     * @return
     */
	public List<StatResult> getBuy_top5() {
		List<StatResult> list = new ArrayList<StatResult>();
		
		load(Buy_Top5_File_Path, list);
				
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<StatResult> getHold_top5() {
		List<StatResult> list = new ArrayList<StatResult>();
		
		load(Hold_Top5_File_Path, list);
				
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<StatResult> getSell_top5() {
		List<StatResult> list = new ArrayList<StatResult>();
		
		load(Sell_Top5_File_Path, list);
				
		return list;
	}
	
    /**
     * 
     * @param filename 策略结果文件路径
     * @param map 策略结果列表
     */
	private void load(String filename, List<StatResult> list) {
		File file = new File(filename);
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));

			String line = null;
			try {
				while((line = br.readLine())!=null){ //循环读取行
					//System.out.print(line);
					//System.out.println();
										
				    String[] segments = line.split("\t"); //按tab分割
				    
				    if (segments.length == CONSTANT_COLOMNS) {
				    	StatResult sr = new StatResult();
				    	sr.setCode(segments[0]);
				    	sr.setName(segments[1]);
				    	sr.setTotal_rio(segments[2]);
				    	sr.setMax_roi(segments[3]);
				    	sr.setMax_loss(segments[4]);
				    	sr.setNum_of_pos_gain(segments[5]);
				    	sr.setBuy_times(segments[6]);
				    	sr.setSell_times(segments[7]);
				    	sr.setAverage_of_pos_gain_period(segments[8]);
				    	sr.setAverage_of_pos_gain(segments[9]);
				    	sr.setPercent_of_pos_gain(segments[10]);
				    	sr.setLast_buy_date(segments[11]);
				    	sr.setCurrent_status(segments[12]);
				    	sr.setBuy_dates(segments[13]);
				    	sr.setSell_dates(segments[14]);
				    	
				    	String strLine = segments[15];
				    	
				    	String[] tradeinfos = strLine.split(",");
				    	
				    	List<TradeInfo> array = new ArrayList<TradeInfo>();
				    	
				    	for(String tradeinfo:tradeinfos) {

					    	String[] fields = tradeinfo.split(":");
				    		
					    	if (fields.length >= 6) {
				    			TradeInfo ti = new TradeInfo();
				    			ti.setBuy_date(fields[0]);
				    			ti.setBuy_price(fields[1]);
				    			ti.setSell_date(fields[2]);
				    			ti.setSell_price(fields[3]);
				    			ti.setPeriod(fields[4]);
				    			ti.setRoi(fields[5]);
				    			
				    			array.add(ti);
				    		}
				    	}
				    	
				    	
				    	//map.put(sr.getCode(), sr);
				    	sr.setTradeInfos(array);
				    	
				    	list.add(sr);
				    }
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @param filename
	 * @param list
	 */
	private void loadReport(String filename, List<ReportInfo> list) {
		File file = new File(filename);
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));

			String line = null;
			
			try {
				br.readLine(); // 去掉文件头
				
				while((line = br.readLine())!=null){ //循环读取行
				    String[] segments = line.split(","); //按tab分割
				    
				    if (segments.length >= 22) {
				    	ReportInfo info = new ReportInfo();
				    	info.setCode(segments[0]);
				    	info.setName(segments[1]);
				    	info.setPB(Double.parseDouble(segments[21]));
				    	info.setPE(Double.parseDouble(segments[20]));
				    	info.setPEG(Double.parseDouble(segments[22]));
				    	info.setROE(Double.parseDouble(segments[5]));
				    	list.add(info);
				    }
				    else {
				    	ReportInfo info = new ReportInfo();
				    	info.setCode(segments[0]);
				    	info.setName(segments[1]);
				    	list.add(info);
				    }
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
