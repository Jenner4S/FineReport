package com.hyp.plugin.workday;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.hyp.plugin.workday.utils.DateHelper;

import com.fr.general.Inter;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;

public class WorkDayFactory {
    private static String error;
    public static String updateWorkDay(Object[] ob) {
        for(Object o :ob){
            if(!"{\"20170303\":\"0\"}".equals(httpGet("20170303"))){
                return "Check your Internet !";
            }
            final String year = ""+o;
            new Thread(){
                @Override
                public void run(){
                    updateYear(year);
                }
            }.start();
            return "Success";
        }
        return "Check your Parameter!";
    }

    private static String updateYear(String year){
        int intYear = Integer.parseInt(year);
        String startDay = ""+(intYear-1) + "1231";
        String endDay = year +"1231";
        String beginDay = startDay;
        String dayString="";
        int size = 30;
        int i = 0;
        JSONObject res = JSONObject.create();
        while (!beginDay.equals(endDay)){
            beginDay = DateHelper.getNextDay(beginDay);
            if(i==0){
                dayString = "";
                dayString = beginDay;
            }else{
                dayString += ","+beginDay;
            }
            if(i==size){
                String result = httpGet(dayString);
                if(result==null){
                    return "Check Internet!";
                }
                try {
                    res.join(new JSONObject(result));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                i=0;
            }else{
                i++;
            }
        }
        if(dayString!=""){
            String result = httpGet(dayString);
            if(result==null){
                return "Check Internet!";
            }
            try {
                res.join(new JSONObject(result));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            i=0;
        }
        PropertiesHelper.writeValue(year,res);
        return "success";
    }

    public static Object circleWorkDay(Object[] ob) {
        if (ob.length == 2) {
//            if(httpGet())

            String startDay = ob[0].toString();
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMdd");
            String toDay = dataFormat.format(new Date());
            String nextDay = startDay;
            int sum =0;
            if(toDay.compareTo(startDay)<0){
                return "startDay Beyond Today ！";
            }
            while (!nextDay.equals(toDay)){
                String nextDay1 = DateHelper.getNextDay(nextDay);
                int dayType = getDayType(nextDay1);
                if(dayType==0){
                    sum++;
                }
                nextDay = nextDay1;
            }
            int circle = Integer.parseInt(ob[1].toString());
            if (sum % circle == 0 && getDayType(toDay) == 0) {
                return true;
            }else{
                return false;
            }
        }
        return "check your parameter !";
    }

    private static int getDayType(String day) {
        String year = day.substring(0, 4);
        JSONObject jsonObject = PropertiesHelper.readValue(year);
        if (jsonObject != null && jsonObject.has(day)) {//缓存文件
            int type = jsonObject.optInt(day);
            return type;
        } else {
            String json = httpGet(day);
            if (json != null) {
                try {
                    JSONObject result = new JSONObject(json);
                    if (result.has(day)) {
                        return result.optInt(day);
                    } else {
                        error = "Check the UTL : " + PropertiesHelper.getConnectionPath() + "  is useful";
                        return -1;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    error = "Check the UTL : " + PropertiesHelper.getConnectionPath() + "  is useful";
                    return -1;
                }
            } else {
                error = "Check your connection Or updateWorkDays";
                return -1;
            }
        }
    }

    public static Object isWorkDay(Object[] ob) {
        if (ob.length == 1) {
            String day = ob[0].toString();
            int dayType = getDayType(day);
            switch (dayType) {
                case 0:
                    return true;
                case -1:
                    return error;
                default:
                    return false;
            }
        }
        return "check your parameter !";
    }

    public static String getIsWorkDay(Object[] ob) {

        JSONObject jsonObject = JSONObject.create();
        for (Object o : ob) {
            String day = "" + o;
            int dayType = getDayType(day);
            try {
                switch (dayType) {
                    case 0:
                        jsonObject.put(day, Inter.getLocText("Plugin-WorkDay_Workday"));
                    case 1:
                        jsonObject.put(day, Inter.getLocText("Plugin-WorkDay_restday"));
                    case -1:
                        jsonObject.put(day, error);
                    case 2:
                        jsonObject.put(day, Inter.getLocText("Plugin-WorkDay_holiday"));
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return jsonObject.toString();
    }

    public static String httpGet(String str) {
        String url = PropertiesHelper.getConnectionPath() + str;
        InputStreamReader inputStream = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL myURL = new URL(url);
            URLConnection httpsConn = (URLConnection) myURL.openConnection();
            if (httpsConn != null) {
                inputStream = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");
                br = new BufferedReader(inputStream);
                String data;
                if(result==null){
                    result = "";
                }
                while ((data = br.readLine()) != null) {
                    if(data!=null){
                        result += data;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}