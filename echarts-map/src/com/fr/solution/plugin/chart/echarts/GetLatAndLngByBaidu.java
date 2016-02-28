package com.fr.solution.plugin.chart.echarts;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.fr.json.JSONArray;
import com.fr.json.JSONException;

public class GetLatAndLngByBaidu { 
	
	/** 
	* @param addr 
	* 查询的地址 
	* @return 
	* @throws IOException 
	*/ 
	public static JSONArray getCoordinate(String addr)  { 
		String lng = null;//经度
		String lat = null;//纬度
		String address = null; 
		try { 
			address = java.net.URLEncoder.encode(addr, "UTF-8"); 
		}catch (UnsupportedEncodingException e1) { 
			e1.printStackTrace(); 
		} 
		String key = "f247cdb592eb43ebac6ccd27f796e2d2"; 
		String url = String .format("http://api.map.baidu.com/geocoder?address=%s&output=json&key=%s", address, key); 
		URL myURL = null; 
		URLConnection httpsConn = null; 
		try { 
			myURL = new URL(url); 
		} catch (MalformedURLException e) { 
			e.printStackTrace(); 
		} 
		InputStreamReader insr = null;
		BufferedReader br = null;
		try { 
			httpsConn = (URLConnection) myURL.openConnection();// 不使用代理 
			if (httpsConn != null) { 
				insr = new InputStreamReader( httpsConn.getInputStream(), "UTF-8"); 
				br = new BufferedReader(insr); 
				String data = null; 
				int count = 1;
				while((data= br.readLine())!=null){ 
					if(count==5){
						lng = (String)data.subSequence(data.indexOf(":")+1, data.indexOf(","));//经度
						count++;
					}else if(count==6){
						lat = data.substring(data.indexOf(":")+1);//纬度
						count++;
					}else{
						count++;
					}
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return JSONArray.create().put(lng).put(lat);//{lng,lat}; 
	} 
	public static void main(String[] args) throws IOException, JSONException {
		JSONArray o = GetLatAndLngByBaidu.getCoordinate("成都市天府四街");
		System.out.println(o.get(0));//经度
		System.out.println(o.get(1));//纬度
	}

}