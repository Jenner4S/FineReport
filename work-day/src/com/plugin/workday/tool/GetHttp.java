package com.plugin.workday.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class GetHttp {
	
	public static String httpGet(String str) throws Exception {
		//http://9.tbip.sinaapp.com/?apiserviceid=1116&d=20130101,20160203
		String url = "http://9.tbip.sinaapp.com/?apiserviceid=1116&d="+ str; 
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
				while((data= br.readLine())!=null){ 
					if(data!="")
					result = data;
				} 
				
			} 
		} catch (IOException e) { 
			throw new Exception("Check you internet!");
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
	public static void main(String[] str){
		String string="";
		try {
			string = GetHttp.httpGet("20160504,20150302");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(string);
	}

}
