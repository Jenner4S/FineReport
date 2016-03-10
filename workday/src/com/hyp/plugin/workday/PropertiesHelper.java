package com.hyp.plugin.workday;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.junit.Test;

public class PropertiesHelper {
//	public static String getKeyValue(String key) {   
//        return props.getProperty(key);   
//    } 
	private static String path = PropertiesHelper.class.getResource("/").getPath()+"com/hyp/plugin/workday/local/value.properties";
	static{
		File file = new File(path);  
        if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		
	}
	public static String readValue( String key) {   
        Properties props = new Properties();   
        try {   
        	System.out.println(path);
        	
        	InputStream in = new  FileInputStream(path);//PropertiesHelper.class.getResourceAsStream(filePath);
            props.load(in);   
            String value = props.getProperty(key); 
            System.out.println(key +"键的值是："+ value);   
            return value;   
        } catch (Exception e) {   
            e.printStackTrace();   
            return null;   
        }   
	}
	public static boolean writeValue( String key,String value) {  
		Properties props = new Properties();   
        try {   
        	InputStream in = new  FileInputStream(path);//PropertiesHelper.class.getResourceAsStream(filePath);
            props.load(in);    
        	OutputStream os = new FileOutputStream(path/*new File(path)*/);
			props.setProperty(key, value);   
			props.store(os, null);
			os.flush();
			os.close();
            System.out.println(key +"键的shuru值是："+ value);   
            return true;   
        } catch (Exception e) {   
            e.printStackTrace();   
            return false;   
        }   
    }   
//	public static void main(String[] str){
////		String path = PropertiesHelper.class.getResource("/").getPath()+"com/hyp/plugin/workday.local/value.properties";
//		Properties props = new Properties();   
//		try {
//        	InputStream in =PropertiesHelper.class.getResourceAsStream(filePath);
//            props.load(in);  	
//			OutputStream os = new FileOutputStream(path,true/*new File(path)*/);
//			System.out.println("da");
//			
//			props.setProperty("aaa", "bb");   
//            // 以适合使用 load 方法加载到 Properties 表中的格式，   
//            // 将此 Properties 表中的属性列表（键和元素对）写入输出流   
//            try {
//				props.store(os, null);
////				os.flush();
//				os.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}  
//			
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(path);
//		writeValue("aaa", "a");
//	}
}
