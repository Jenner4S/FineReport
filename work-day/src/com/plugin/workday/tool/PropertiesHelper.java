package com.plugin.workday.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;



public class PropertiesHelper {
	public static String readValue( String key) throws Exception {   
        Properties props = new Properties();   
        try {   
        	String filepath = PropertiesHelper.class.getResource("/").getPath()+"value.properties";
        	InputStream in;
        	File file = new File(filepath);
        	if(file.exists()&&!key.equals("2016")){
        		in = new  FileInputStream(file);
        	}else{
          		in = new PropertiesHelper().getClass().getResourceAsStream("/com/plugin/workday/local/value.properties");
          	}
        	props.load(in);   
            String value = props.getProperty(key); 
            System.out.println(key +"KEY：VALUE"+ value);  
            if(value!=null){
                return value;   
            }else{
            	throw new Exception("Please updateworkday for "+key +" year");
            }
        } catch (FileNotFoundException e) {   
            throw new Exception("FileNotFoundException!");
        }   catch (IOException e) {
			// TODO: handle exception
        	throw new Exception("IOException!");
		}
	}
	public static boolean writeValue( String key,String value) throws Exception {  
			Properties props = new Properties();   
        	String filepath = PropertiesHelper.class.getResource("/").getPath()+"value.properties";
        	try{
                File file = new File(filepath);
                if(!file.exists()){
                	file.createNewFile();
                }
                InputStream in =new FileInputStream(file);
                props.load(in);
            	OutputStream os = new FileOutputStream(file/*new File(path)*/);
    			props.setProperty(key, value);   
    			props.store(os, null);
    			os.flush();
    			os.close();
                System.out.println(key +"KEY ：VALUE"+ value);   
                return true; 
        		
        	}catch(Exception e){
        		throw new Exception("Properties Write Fail!");
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
