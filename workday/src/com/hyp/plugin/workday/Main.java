package com.hyp.plugin.workday;

public class Main {
	private static String strs="";

	public static void main(String[] str){
		String[] days = new String[]{"sss","sssbb"};
//		System.out.println(srt);
//		String str="";
		for(String day:days){
			strs+=","+day;
		}
		strs = strs.substring(1);
		System.out.println(strs);
		
	}

}
