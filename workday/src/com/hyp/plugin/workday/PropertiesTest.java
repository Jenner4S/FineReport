package com.hyp.plugin.workday;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class PropertiesTest {
	static File file = new File("test.properties");
	
	public static void main(String[] args) {
		try {
			createFile();
			readFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 写文件 
	 * @throws IOException
	 */
	public static void createFile() throws IOException{
		
		if(file.exists())
			file.delete();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("1", "2");
		map.put("3", "4");
		map.put("34", "12");
		map.put("5", "6");
		
		
		FileOutputStream out =new FileOutputStream(file);
		for(Map.Entry<String, String> maps : map.entrySet()){
			out.write(maps.getKey().getBytes());
			out.write("=".getBytes());
			out.write(maps.getValue().getBytes());
			out.write("\n".getBytes());
		}
	}
	
	/**
	 * 读文件 
	 * @param file
	 * @throws IOException
	 */
	public static void readFile(File file) throws IOException{
		if(!file.exists())
			return;
		Properties props = new Properties();
		InputStream inStream = new FileInputStream(file);
		props.load(inStream);
		for(Map.Entry<Object, Object> maps : props.entrySet()){
			System.out.println(maps.getKey().toString()+":"+maps.getValue().toString());
		}
	}
}

