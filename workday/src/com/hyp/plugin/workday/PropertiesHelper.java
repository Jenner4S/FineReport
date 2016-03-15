package com.hyp.plugin.workday;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import sun.swing.FilePane;


public class PropertiesHelper {
//	public static String getKeyValue(String key) {   
//        return props.getProperty(key);   
//    } 
//	private static String path = PropertiesHelper.class.getResource("/").getPath()+"com/hyp/plugin/workday/local/value.properties";
//	
//	static{
//		System.out.println("GGGGGGGGGG"+path);
//		File file = new File(path);  
//        if (!file.exists())
//			try {
//				file.createNewFile();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}  
//		
//	}
	public static String readValue( String key) {   
        Properties props = new Properties();   
        try {   //com/hyp/plugin/workday/local/
        	String filepath = PropertiesHelper.class.getResource("/").getPath()+"value.properties";
        //	
        	System.out.println("file::"+filepath);
        	System.out.println(PropertiesHelper.class.getClass().getClassLoader());
        	InputStream in;
        	File file = new File(filepath);
        	if(file.exists()&&!key.equals("2016")){
        		in = new  FileInputStream(file);
        	}else{
        		System.out.println("::::::::::::");
          		in = new PropertiesHelper().getClass().getResourceAsStream("/com/hyp/plugin/workday/local/value.properties");
              	
          	}
        	props.load(in);   
            String value = props.getProperty(key); 
            
            System.out.println(key +"KEY：VALUE"+ value);   
            return value;   
        } catch (Exception e) {   
            e.printStackTrace();   
            return null;   
        }   
	}
	public static boolean writeValue( String key,String value) {  
			Properties props = new Properties();   
        	String filepath = PropertiesHelper.class.getResource("/").getPath()+"value.properties";
//            String filename = filepath+"/value.properties";
        
        	//
        	try{
//        		InputStream in =new FileInputStream(filepath);//new PropertiesHelper().getClass().getResourceAsStream("/com/hyp/plugin/workday/local/value.properties");;
                File file = new File(filepath);
//                if()
                if(!file.exists()){
                	file.createNewFile();
                }
                InputStream in =new FileInputStream(filepath);//new PropertiesHelper().getClass().getResourceAsStream("/com/hyp/plugin/workday/local/value.properties");;
                
                props.load(in);    
            	OutputStream os = new FileOutputStream(file/*new File(path)*/);
    			props.setProperty(key, value);   
    			props.store(os, null);
    			os.flush();
    			os.close();
                System.out.println(key +"KEY ：VALUE"+ value);   
                return true; 
        		
        	}catch(Exception e){
        		return false;
        	}
             
    }   
	public static void main(String[] str){
		String path = "/C:/Users/hao01/git/finereport/advanced-development/WebReport/WEB-INF/classes/com/value.properties";
		 File file = new File(path);
         if(!file.exists()){
         	try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }
		
		
	}
}
