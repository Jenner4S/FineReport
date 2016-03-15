package com.plugin.workday.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fr.general.Inter;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;

public class WorkDayFunction {
	public static String isWorkDay(Object[] ob) throws JSONException, Exception{
		if(ob.length!=1){
			return "Check your parameter!";
		}
		String day = ob[0].toString();
		DateHelper.isValidDate(day, "yyyyMMdd");
		String year = day.substring(0,4);
		String yearjson = PropertiesHelper.readValue(year);
		JSONObject json = new JSONObject(yearjson);
		if(json.has(day)){
			return "false";
		}
		return "true";
	}
	
	public static String workDay(Object[] ob) throws Exception{
//		JSONObject res = new JSONObject();
		String res = "";
		for(Object o :ob){
			String day = o.toString();
			DateHelper.isValidDate(day, "yyyyMMdd");
			String year = day.substring(0,4);
			String yearjson = PropertiesHelper.readValue(year);
			JSONObject json = new JSONObject(yearjson);
			if(json.has(day)){
				int intday = json.getInt(day);
				if(intday==1){
					res+=",\""+day+"\":\""+Inter.getLocText("Plugin-WorkDay_restday")+"\"";
//					res.put(day,  Inter.getLocText("Plugin-WorkDay_restday"));
				}else /*if(intday == 2)*/{
					res+=",\""+day+"\":\""+Inter.getLocText("Plugin-WorkDay_holiday")+"\"";
//					res.put(day,  Inter.getLocText("Plugin-WorkDay_holiday"));
				}
			}else {
				res+=",\""+day+"\":\""+Inter.getLocText("Plugin-WorkDay_Workday")+"\"";
//				res.put(day,  Inter.getLocText("Plugin-WorkDay_Workday"));
			}
		}
		return "{"+res.substring(1)+"}";
	}
	
	public static String circleWorkDay(Object[] ob) throws Exception{
		
		if(ob.length!=2){
			return "Check your parameter!";
		}
			String startdaystr =  ob[0].toString();
			DateHelper.isValidDate(startdaystr, "yyyyMMdd");
			int circle = Integer.parseInt( ob[1].toString());
			
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			String nowstr = df.format(new Date());
			
			int alldaynum = DateHelper.getDateNum(startdaystr, nowstr);
			int workdaynum = 0;
			String[] days = DateHelper.getDays(startdaystr, alldaynum);
			String beginyear = startdaystr.substring(0,4);
			String endyear =  nowstr.substring(0, 4);
			int b = Integer.parseInt(beginyear);
			int e = Integer.parseInt(endyear);
			JSONObject json = new JSONObject();
			for(;b<=e;){
				try {
					json.join(new JSONObject(PropertiesHelper.readValue(""+b)));
				} catch (JSONException e1) {
							// TODO Auto-generated catch block
					throw new Exception("Calendar database has problems");
				}
						b++;
			}
			for(String day :days){
				if(!json.has(day)){
					workdaynum++;
				}
			}
			System.out.println(alldaynum);
			System.out.println(workdaynum+"day");
			if(workdaynum%circle==0&&!json.has(nowstr)){
				return "true";
			}else{
				return "false";
			}
	}
		
	public static String updateWorkDay(Object[] ob) throws Exception{
		if(ob.length!=1){
			return "Check your parameter!";
		}
		String year = ob[0].toString();
		DateHelper.isValidDate(year, "yyyy");
		String beginday = year+"0100";
		String endday = year+"1231";
		int num = 10;
		int alldaynum = DateHelper.getDateNum(beginday, endday);
		int inum = alldaynum/num;
		JSONObject json=new JSONObject();
		for(int i=0;i<inum;i++){
			DateHelper.getDays(beginday, num);
			String daysstring = DateHelper.daysString;
			try {
				json.join( DateHelper.getWorkday(GetHttp.httpGet(daysstring)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			beginday = DateHelper.endDayString;
		}
		if(alldaynum%num!=0){
			DateHelper.getDays(beginday, alldaynum%num);
			String daysstring = DateHelper.daysString;
			try {
				json.join( DateHelper.getWorkday(GetHttp.httpGet(daysstring)));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(json);
		PropertiesHelper.writeValue(year, json.toString());
		return "Update "+year+" Success";
	}
	
	
	
	public static void main(String[] str){
		
		try {
//			updateWorkDay(new Object[]{"2014"});
//			String result = isWorkDay(new Object[]{"20160501"});
//			String res = workDay(new Object[]{"20150501","20160203"});
			String cir = circleWorkDay(new Object[]{"20160201","3"});
			System.out.println(cir);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());;
		}
	}
}
