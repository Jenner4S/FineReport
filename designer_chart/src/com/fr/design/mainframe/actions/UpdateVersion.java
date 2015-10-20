package com.fr.design.mainframe.actions;

import com.fr.general.FRLogger;
import com.fr.json.JSONObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class UpdateVersion extends SwingWorker<JSONObject,Void> {

    private static final String VERSION_URL ="http://chart.finedevelop.com/update/update.json";
    private static final int TIME_OUT = 300;//5s
    public static final String VERSION = "version";

    public UpdateVersion(){

    }


    @Override
    protected JSONObject doInBackground() throws Exception {
        return getJsonContent();
    }

    public static JSONObject getJsonContent() throws Exception{
        String res = null;
        try {
            res = readVersionFromServer(TIME_OUT);
        } catch (IOException e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        return new JSONObject(res);
    }

    /**
     * �ӷ�������ȡ�汾
     */
    private static String readVersionFromServer(int timeOut) throws IOException {
        URL getUrl = new URL(VERSION_URL);
        // ����ƴ�յ�URL�������ӣ�URL.openConnection���������URL�����ͣ�
        // ���ز�ͬ��URLConnection����Ķ�������URL��һ��http�����ʵ�ʷ��ص���HttpURLConnection
        HttpURLConnection connection = (HttpURLConnection) getUrl
                .openConnection();
        connection.setReadTimeout(timeOut);
        // �������ӣ�����ʵ����get requestҪ����һ���connection.getInputStream()�����вŻ���������
        // ������
        connection.connect();
        // ȡ������������ʹ��Reader��ȡ
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf8"));//���ñ���,������������
        String lines;
        StringBuffer sb = new StringBuffer();
        while ((lines = reader.readLine()) != null) {
            sb.append(lines);
        }
        reader.close();
        // �Ͽ�����
        connection.disconnect();
        return sb.toString();
    }
}
