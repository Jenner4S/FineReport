package com.plugin.workday.tool;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import com.fr.json.JSONException;
import com.fr.json.JSONObject;

public class DateHelper {
	public static String endDayString="";
	public static String daysString;
	public static int getDateNum(String begin,String end) throws Exception{
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		Long c = null;
		try {
			c = sf.parse(end).getTime()-sf.parse(begin).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long d = c/1000/60/60/24;
		System.out.println(d+"day");
		if(d<0){
			throw new Exception("Wrong Date!");
		}
		return (int)d;
	}
	public static String[] getDays(String startday,int daynum){
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		DecimalFormat df = new DecimalFormat("00");
		String[] days = new String[daynum];
		daysString="";
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
			e.printStackTrace();
		}
		daysString = daysString.substring(1);
		return days;
	}
	public static void main(String[] s ){
		getDays("20160203", 2);
		System.out.println(endDayString);
	}
	
	
	public static JSONObject getWorkday(String result) {
		JSONObject out = new JSONObject();
		// TODO Auto-generated method stub
		try {
			JSONObject res = new JSONObject(result);
			Iterator it = res.keys();
			for(;it.hasNext();){
				String key = (String) it.next();
				int value = res.getInt(key);
				if(value != 0){
					out.put(key, value);
				}
			}
			System.out.println(out);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return out;
	}
	public static boolean isValidDate(String date,String formatstr) throws Exception{
		try {
			
			SimpleDateFormat format=new SimpleDateFormat(formatstr);
			format.setLenient(false);
			format.parse(date);
			
		} catch (Exception ex){
			throw new Exception("Check the date "+date+" and try again!");
		}
		
		return true;
	}
}
