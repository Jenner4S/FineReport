package com.hyp.plugin.workday;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.omg.CORBA.OBJ_ADAPTER;

import com.fr.general.Inter;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;

public class GetWorkDay { 
	public static boolean isWorkDay(Object[] ob){
		if(ob.length==1){
			String str = ob[0].toString();
			String result = httpGet(str);
			System.out.println("aaaa"+result);
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
		System.out.println(url);
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
			httpsConn = (URLConnection) myURL.openConnection();// ��ʹ�ô��� 
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
					System.out.println("GETLATANDLNG");
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
		JSONObject o = GetWorkDay.getIsWorkDay(new Object[]{"20120111","20160302","20150212"});
		System.out.println(o);
	}

}