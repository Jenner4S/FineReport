package com.hyp.plugin.workday;

import com.fr.json.JSONException;
import com.fr.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class PropertiesHelper {

    private static Map<String ,JSONObject> propsMap = new HashMap<String, JSONObject>();

    private final static String WORK_DAY_URL = "http://www.easybots.cn/api/holiday.php?d=";

    public static void setUrl(String url){
        if(url==null){
            propsMap.remove("URL");
        }
        try {
            propsMap.put("URL",JSONObject.create().put("value",url));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getConnectionPath(){
        if(propsMap.containsKey("URL")){
            return propsMap.get("URL").optString("value");
        }else{
            return WORK_DAY_URL;
        }
    }

    public static JSONObject readValue(String key) {
        if(propsMap.size()==0){
            readProperty();
        }
        return propsMap.get(key);
    }

    private static void readProperty(){
        readInFile();
        readOutFile();
    }

    private static void readInFile() {
        InputStream resourceAsStream = new PropertiesHelper().getClass().getResourceAsStream("/com/hyp/plugin/workday/local/value.properties");
        readProperties(resourceAsStream);
    }

    private static void readProperties(InputStream in){
        Properties props = new Properties();
        try {
            props.load(in);
            for(Object keyString:props.keySet()){
                String key = ""+keyString;
                String property = props.getProperty(key);
                try {
                    JSONObject jsonObject = propsMap.get(key);
                    if(jsonObject!=null){
                        jsonObject = jsonObject.join(new JSONObject(property));
                    }else {
                        jsonObject = new JSONObject(property);
                    }
                    propsMap.put(key,jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(key + "KEY：VALUE" + property);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void readOutFile() {
        String filepath = PropertiesHelper.class.getResource("/").getPath() + "value.properties";
        File file = new File(filepath);
        if (file.exists()) {
            try {
                InputStream in = new FileInputStream(file);
                readProperties(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static boolean writeValue(String key, JSONObject value) {
        propsMap.put(key,value);
        return saveFile();
    }

    private static boolean saveFile(){
        Properties props = new Properties();
        String filepath = PropertiesHelper.class.getResource("/").getPath() + "value.properties";
        try {
            File file = new File(filepath);
            if (!file.exists()) {
                file.createNewFile();
            }
            InputStream in = new FileInputStream(filepath);//new PropertiesHelper().getClass().getResourceAsStream("/com/hyp/plugin/workday/local/value.properties");;
            props.load(in);
            OutputStream os = new FileOutputStream(file/*new File(path)*/);
            for (String key:propsMap.keySet()){
                String value = propsMap.get(key).toString();
                props.setProperty(key, value);
            }
            props.store(os, null);
            os.flush();
            os.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
