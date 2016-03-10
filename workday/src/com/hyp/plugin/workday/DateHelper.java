package com.hyp.plugin.workday;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

import com.fr.json.JSONException;
import com.fr.json.JSONObject;

public class DateHelper {
//	public static String daysString="";
	public static String endDayString="";
public static String daysString;
	public static int getDateNum(String begin,String end){
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		Long c = (long) 0.0;
		try {
			c = sf.parse(end).getTime()-sf.parse(begin).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long d = c/1000/60/60/24;
		System.out.println(d+"day");
		return (int) Math.abs(d);
	}
	public static String[] getDays(String startday,int daynum){
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		DecimalFormat df = new DecimalFormat("00");
		String[] days = new String[daynum];
		Calendar cl = Calendar.getInstance();
		try {
			cl.setTimeInMillis(sf.parse(startday).getTime());
			int date = cl.get(Calendar.DATE);
			cl.set(Calendar.DATE, date+1);	
			int year=0;
			int month=0;
			for(int i=0;i<daynum;i++){
				
				year = cl.get(Calendar.YEAR);
				month = cl.get(Calendar.MONTH);
				date = cl.get(Calendar.DATE);
				String da = ""+year+df.format(month+1)+df.format(date);
				days[i]=da;
				daysString += "," +da;
				cl.set(Calendar.DATE, date+1);	
			}
			endDayString = ""+year+df.format(month+1)+df.format(date);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		daysString = daysString.substring(1);
		return days;
	}
	public static void main(String[] s ){
		getDays("20160203", 2);
//		System.out.println(daysString);
		System.out.println(endDayString);
	}
	
	
	public static int getWorkdayNum(String result,String[] days) {
		int daynum = 0;
		try {
			JSONObject out = new JSONObject(result);
			System.out.println(out);
//			 Iterator it = out.keys();
//				for(;it.hasNext();){
//					String key=(String)it.next();
//					int value = out.getInt(key);
//					if(value==0){
//						daynum++;
//					}
//					
//				}
			
			System.out.println(out);
			for(String day:days){
				if(out.has(day)){
					int intday = out.getInt(day);
					if(intday==0){
						daynum ++;
					}
				}
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return daynum;
	}
	public static JSONObject getWorkday(String result) {
		JSONObject json = new JSONObject();
		// TODO Auto-generated method stub
		try {
			JSONObject out = new JSONObject(result);
			System.out.println(out);
			 Iterator it = out.keys();
			for(;it.hasNext();){
				String key=(String)it.next();
				int value = out.getInt(key);
				if(value==0){
					json.put(key, value);
				}
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json;
	}
}
