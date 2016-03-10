package com.hyp.plugin.workday;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.activation.DataHandler;

import org.omg.CORBA.OBJ_ADAPTER;

import com.fr.general.Inter;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.report.core.A.o;

public class GetWorkDay { 
	public static String updateWorkDay(Object[] ob){
		for(Object o :ob)
		{
			String year = o.toString();
//			DecimalFormat df = new DecimalFormat("0000");
//			df.format(Integer.parseInt(year));
			int num = 50;
			String beginday = year+"0100";
			String endday = year+"1231";
			int alldaynum = DateHelper.getDateNum(beginday, endday);
			int inum = alldaynum/num;
			JSONObject json=new JSONObject();
			for(int i=0;i<inum;i++){
				DateHelper.getDays(beginday, num);
				String daysstring = DateHelper.daysString;
				try {
					json.join( DateHelper.getWorkday(httpGet(daysstring)));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				beginday = DateHelper.endDayString;
				
			}
			if(alldaynum%num!=0){
				DateHelper.getDays(beginday, num);
				String daysstring = DateHelper.daysString;
				try {
					json.join( DateHelper.getWorkday(httpGet(daysstring)));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				beginday = DateHelper.endDayString;
			}
			System.out.println(json.toString());
			PropertiesHelper.writeValue(year, json.toString());
			
		}
		return "Success";
	}
	
	
	public static boolean circleWordDay(Object[] ob){
		if(ob.length==2){
			int num=50;
			String startdaystr =  ob[0].toString();
//			int startdayint = Integer.parseInt( startdaystr);
			int circle = Integer.parseInt( ob[1].toString());
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			String nowstr = df.format(new Date());
			int alldaynum = DateHelper.getDateNum(startdaystr, nowstr);
			int workdaynum = 0;
			String data = httpGet(nowstr);
			int inum = alldaynum/num;
			for(int i=0;i<inum;i++){
				String[] days = DateHelper.getDays(startdaystr, num);
				
				if(data.equals("{}")){
					String beginyear = startdaystr.substring(0,4);
					startdaystr = DateHelper.endDayString;
					String endyear =  startdaystr.substring(0, 4);
					int b = Integer.parseInt(beginyear);
					int e = Integer.parseInt(endyear);
					JSONObject json = new JSONObject();
					for(;b<=e;){
						try {
							json.join(new JSONObject(PropertiesHelper.readValue(""+b)));
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							System.out.println("GETWORKDAY");
							e1.printStackTrace();
						}
						b++;
					}
					System.out.println("::::::"+json);
					workdaynum += DateHelper.getWorkdayNum(json.toString(),days);
				}else{
					startdaystr = DateHelper.endDayString;
					String daysstring = DateHelper.daysString;
					workdaynum += DateHelper.getWorkdayNum(httpGet(daysstring),days);
				}
				
			}
			if(alldaynum%num!=0){
				String[] days = DateHelper.getDays(startdaystr, alldaynum%num);
				if(data.equals("{}")){
					String beginyear = startdaystr.substring(0,4);
					startdaystr = DateHelper.endDayString;
					String endyear =  startdaystr.substring(0, 4);
					int b = Integer.parseInt(beginyear);
					int e = Integer.parseInt(endyear);
					JSONObject json = new JSONObject();
					for(;b<=e;){
						try {
							json.join(new JSONObject(PropertiesHelper.readValue(""+b)));
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							System.out.println("GETWORKDAY");
							e1.printStackTrace();
						}
						b++;
					}
					System.out.println("::::::"+json);
					workdaynum += DateHelper.getWorkdayNum(json.toString(),days);
				}else{
					startdaystr = DateHelper.endDayString;
					String daysstring = DateHelper.daysString;
					workdaynum += DateHelper.getWorkdayNum(httpGet(daysstring),days);
				}
			}
			int todayisworkday=0;
			if(data.equals("{}")){
				String json = PropertiesHelper.readValue(nowstr.substring(0,4));
				todayisworkday = DateHelper.getWorkdayNum(json,new String[]{nowstr});
			}else{
				todayisworkday = DateHelper.getWorkdayNum(httpGet(nowstr),new String[]{nowstr});
			}
			
			System.out.println(workdaynum+"   "+todayisworkday);
			if(workdaynum%circle==0&&todayisworkday==1){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isWorkDay(Object[] ob){
		if(ob.length==1){
			String str = ob[0].toString();
			String result = httpGet(str);
			try {
				JSONObject json = new JSONObject(result);
				if(json.has(str)){
					int i = new JSONObject(result).getInt(str);
					if(i==0){
						return true;
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return false;
	}
	public static JSONObject getIsWorkDay(Object[] ob)  { 
		String str="";
		String[] days = new String[ob.length];
		int i=0;
		for(Object o :ob){
			days[i++]=o.toString();
		}
		for(String day:days){
			str+=","+day;
		}
		str = str.substring(1);
		String result = httpGet(str);
		JSONObject res=new JSONObject();
		try {
			JSONObject out = new JSONObject(result);
			for(String day:days){
				if(out.has(day)){
					int intday = out.getInt(day);
					if(intday==0){
						res.put(day,  Inter.getLocText("Plugin-WorkDay_Workday"));
					}else if(intday == 1){
						res.put(day,  Inter.getLocText("Plugin-WorkDay_restday"));
					}else if(intday==2){
						res.put(day,  Inter.getLocText("Plugin-WorkDay_holiday"));
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res;
	}
	private static String httpGet(String str) {
		String url = "http://www.easybots.cn/api/holiday.php?d="+ str; 
		URL myURL = null; 
		URLConnection httpsConn = null; 
		try { 
			myURL = new URL(url); 
		} catch (MalformedURLException e) { 
			e.printStackTrace(); 
		} 
		InputStreamReader insr = null;
		BufferedReader br = null;
		String result="{}";
		try { 
			httpsConn = (URLConnection) myURL.openConnection();
			if (httpsConn != null) { 
				insr = new InputStreamReader( httpsConn.getInputStream(), "UTF-8"); 
				br = new BufferedReader(insr); 
				String data = null; 
				int count = 1;
				while((data= br.readLine())!=null){ 
					if(data!="")
					result = data;
				} 
				
			} 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} finally {
			if(insr!=null){
				try {
					insr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	} 
	public static void main(String[] args) throws IOException, JSONException {
		String o = GetWorkDay.updateWorkDay(new Object[]{"2012","2015"});
//		circleWordDay(new Object[]{"20150302","3"});
//		System.out.println(o);
	}

}