// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.actions;

import com.fr.general.FRLogger;
import com.fr.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.SwingWorker;

public class UpdateVersion extends SwingWorker
{

    private static final String VERSION_URL = "http://chart.finedevelop.com/update/update.json";
    private static final int TIME_OUT = 300;
    public static final String VERSION = "version";

    public UpdateVersion()
    {
    }

    protected JSONObject doInBackground()
        throws Exception
    {
        return getJsonContent();
    }

    public static JSONObject getJsonContent()
        throws Exception
    {
        String s = null;
        try
        {
            s = readVersionFromServer(300);
        }
        catch(IOException ioexception)
        {
            FRLogger.getLogger().error(ioexception.getMessage());
        }
        return new JSONObject(s);
    }

    private static String readVersionFromServer(int i)
        throws IOException
    {
        URL url = new URL("http://chart.finedevelop.com/update/update.json");
        HttpURLConnection httpurlconnection = (HttpURLConnection)url.openConnection();
        httpurlconnection.setReadTimeout(i);
        httpurlconnection.connect();
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream(), "utf8"));
        StringBuffer stringbuffer = new StringBuffer();
        String s;
        while((s = bufferedreader.readLine()) != null) 
            stringbuffer.append(s);
        bufferedreader.close();
        httpurlconnection.disconnect();
        return stringbuffer.toString();
    }

    protected volatile Object doInBackground()
        throws Exception
    {
        return doInBackground();
    }
}
