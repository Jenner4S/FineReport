// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.FRContext;
import com.fr.data.core.db.DBUtils;
import com.fr.data.core.db.dialect.DialectFactory;
import com.fr.data.core.db.dml.Delete;
import com.fr.data.core.db.dml.Select;
import com.fr.data.core.db.dml.Table;
import com.fr.data.impl.JDBCDatabaseConnection;
import com.fr.design.DesignerEnvManager;
import com.fr.general.ComparatorUtils;
import com.fr.general.DateUtils;
import com.fr.general.DefaultValues;
import com.fr.general.DesUtils;
import com.fr.general.FRLogger;
import com.fr.general.GeneralUtils;
import com.fr.general.IOUtils;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.record.DBRecordManager;
import com.fr.stable.ArrayUtils;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLTools;
import com.fr.stable.xml.XMLWriter;
import com.fr.stable.xml.XMLableReader;
import com.fr.third.javax.xml.stream.XMLStreamException;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONObject;

public class InformationCollector
    implements XMLReadable, XMLWriter
{
    private class StartStopTime
        implements XMLReadable, XMLWriter
    {

        private String startDate;
        private String stopDate;
        final InformationCollector this$0;

        public String getStartDate()
        {
            return startDate;
        }

        public void setStartDate(String s)
        {
            startDate = s;
        }

        public String getStopDate()
        {
            return stopDate;
        }

        public void setStopDate(String s)
        {
            stopDate = s;
        }

        public void writeXML(XMLPrintWriter xmlprintwriter)
        {
            xmlprintwriter.startTAG("StartStop");
            if(StringUtils.isNotEmpty(startDate))
                xmlprintwriter.attr("start", startDate);
            if(StringUtils.isNotEmpty(stopDate))
                xmlprintwriter.attr("stop", stopDate);
            xmlprintwriter.end();
        }

        public void readXML(XMLableReader xmlablereader)
        {
            startDate = xmlablereader.getAttrAsString("start", "");
            stopDate = xmlablereader.getAttrAsString("stop", "");
        }

        private StartStopTime()
        {
            this$0 = InformationCollector.this;
            super();
        }

    }


    private static final long DELTA = 0xf731400L;
    private static final long SEND_DELAY = 30000L;
    private static final String FILE_NAME = "fr.info";
    private static final String XML_START_STOP_LIST = "StartStopList";
    private static final String XML_START_STOP = "StartStop";
    private static final String XML_LAST_TIME = "LastTime";
    private static final String ATTR_START = "start";
    private static final String ATTR_STOP = "stop";
    private static final String XML_JAR = "JarInfo";
    private static final String XML_VERSION = "Version";
    private static final String XML_USERNAME = "Username";
    private static final String XML_UUID = "UUID";
    private static final String XML_KEY = "ActiveKey";
    private static final String XML_OS = "OS";
    public static final String FUNCTIONS_INFO = "http://feedback.finedevelop.com:3000/monitor/function/record";
    public static final String USER_INFO = "http://feedback.finedevelop.com:3000/monitor/userinfo/record";
    public static final String TABLE_NAME = "fr_functionrecord";
    public static final String FUNC_COLUMNNAME = "func";
    private static InformationCollector collector;
    private List startStop;
    private String lastTime;
    private StartStopTime current;

    public InformationCollector()
    {
        startStop = new ArrayList();
        current = new StartStopTime();
    }

    public static InformationCollector getInstance()
    {
        if(collector == null)
        {
            collector = new InformationCollector();
            readEncodeXMLFile(collector, collector.getInfoFile());
        }
        return collector;
    }

    private static void readEncodeXMLFile(XMLReadable xmlreadable, File file)
    {
        if(file == null || !file.exists())
            return;
        String s = "UTF-8";
        try
        {
            String s1 = getDecodeFileContent(file);
            ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(s1.getBytes(s));
            InputStreamReader inputstreamreader = new InputStreamReader(bytearrayinputstream, s);
            XMLableReader xmlablereader = XMLableReader.createXMLableReader(inputstreamreader);
            if(xmlablereader != null)
                xmlablereader.readXMLObject(xmlreadable);
            bytearrayinputstream.close();
        }
        catch(FileNotFoundException filenotfoundexception)
        {
            FRContext.getLogger().error(filenotfoundexception.getMessage());
        }
        catch(IOException ioexception)
        {
            FRContext.getLogger().error(ioexception.getMessage());
        }
        catch(XMLStreamException xmlstreamexception)
        {
            FRContext.getLogger().error(xmlstreamexception.getMessage());
        }
    }

    private static String getDecodeFileContent(File file)
        throws FileNotFoundException, UnsupportedEncodingException
    {
        FileInputStream fileinputstream = new FileInputStream(file);
        String s = IOUtils.inputStream2String(fileinputstream);
        return DesUtils.getDecString(s);
    }

    private long getLastTimeMillis()
    {
        if(StringUtils.isEmpty(lastTime))
            return 0L;
        try
        {
            return DateUtils.string2Date(lastTime, true).getTime();
        }
        catch(Exception exception)
        {
            return -1L;
        }
    }

    private byte[] getJSONContentAsByte()
    {
        JSONObject jsonobject = new JSONObject();
        JSONArray jsonarray = new JSONArray();
        for(int i = 0; i < startStop.size(); i++)
        {
            JSONObject jsonobject1 = new JSONObject();
            jsonobject1.put("start", ((StartStopTime)startStop.get(i)).getStartDate());
            jsonobject1.put("stop", ((StartStopTime)startStop.get(i)).getStopDate());
            jsonarray.put(jsonobject1);
        }

        DesignerEnvManager designerenvmanager = DesignerEnvManager.getEnvManager();
        jsonobject.put("StartStop", jsonarray);
        jsonobject.put("UUID", designerenvmanager.getUUID());
        jsonobject.put("JarInfo", GeneralUtils.readBuildNO());
        jsonobject.put("Version", ProductConstants.RELEASE_VERSION);
        jsonobject.put("Username", designerenvmanager.getBBSName());
        jsonobject.put("ActiveKey", designerenvmanager.getActivationKey());
        jsonobject.put("OS", System.getProperty("os.name"));
        try
        {
            return jsonobject.toString().getBytes("UTF-8");
        }
        catch(UnsupportedEncodingException unsupportedencodingexception)
        {
            FRContext.getLogger().error(unsupportedencodingexception.getMessage());
        }
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }

    private void sendUserInfo()
    {
        long l = (new Date()).getTime();
        long l1 = getLastTimeMillis();
        if(l - l1 <= 0xf731400L)
            return;
        byte abyte0[] = getJSONContentAsByte();
        HttpClient httpclient = new HttpClient("http://feedback.finedevelop.com:3000/monitor/userinfo/record");
        httpclient.setContent(abyte0);
        if(!httpclient.isServerAlive())
            return;
        String s = httpclient.getResponseText();
        boolean flag = ComparatorUtils.equals((new JSONObject(s)).get("status"), "success");
        if(flag)
            reset();
    }

    private void sendFunctionsInfo()
    {
        byte abyte0[];
        Connection connection;
        Table table;
        long l = (new Date()).getTime();
        long l1 = getLastTimeMillis();
        if(l - l1 <= 0xf731400L)
            return;
        abyte0 = ArrayUtils.EMPTY_BYTE_ARRAY;
        connection = null;
        table = new Table("fr_functionrecord");
        ResultSet resultset;
        connection = DBRecordManager.getDB().createConnection();
        resultset = selectAllFromLogDB(connection, table);
        if(resultset == null)
        {
            DBUtils.closeConnection(connection);
            return;
        }
        abyte0 = getFunctionsContentAsByte(resultset);
        DBUtils.closeConnection(connection);
        break MISSING_BLOCK_LABEL_122;
        Exception exception;
        exception;
        FRContext.getLogger().error(exception.getMessage());
        DBUtils.closeConnection(connection);
        break MISSING_BLOCK_LABEL_122;
        Exception exception1;
        exception1;
        DBUtils.closeConnection(connection);
        throw exception1;
        HttpClient httpclient = new HttpClient("http://feedback.finedevelop.com:3000/monitor/function/record");
        httpclient.setContent(abyte0);
        httpclient.setTimeout(5000);
        if(!httpclient.isServerAlive())
            return;
        String s = httpclient.getResponseText();
        boolean flag = ComparatorUtils.equals((new JSONObject(s)).get("status"), "success");
        if(flag)
        {
            deleteLogDB(connection, table);
            lastTime = dateToString();
        }
        return;
    }

    private void deleteLogDB(Connection connection, Table table)
    {
        connection = DBRecordManager.getDB().createConnection();
        Delete delete = new Delete(table);
        delete.execute(connection);
        DBUtils.closeConnection(connection);
        break MISSING_BLOCK_LABEL_56;
        Exception exception;
        exception;
        FRContext.getLogger().error(exception.getMessage());
        DBUtils.closeConnection(connection);
        break MISSING_BLOCK_LABEL_56;
        Exception exception1;
        exception1;
        DBUtils.closeConnection(connection);
        throw exception1;
    }

    private byte[] getFunctionsContentAsByte(ResultSet resultset)
        throws JSONException
    {
        com.fr.json.JSONObject jsonobject = new com.fr.json.JSONObject();
        HashMap hashmap = new HashMap();
        try
        {
            while(resultset.next()) 
            {
                com.fr.json.JSONObject jsonobject1 = new com.fr.json.JSONObject(resultset.getString("func"));
                Map map = jsonobject1.toMap();
                Iterator iterator1 = map.keySet().iterator();
                while(iterator1.hasNext()) 
                {
                    Object obj1 = iterator1.next();
                    if(hashmap.containsKey(obj1))
                    {
                        int i = Integer.parseInt(hashmap.get(obj1).toString());
                        int j = Integer.parseInt(map.get(obj1).toString());
                        hashmap.put(obj1, Integer.valueOf(i + j));
                    } else
                    {
                        hashmap.put(obj1, map.get(obj1));
                    }
                }
            }
            resultset.close();
        }
        catch(SQLException sqlexception) { }
        JSONArray jsonarray = new JSONArray();
        com.fr.json.JSONObject jsonobject2;
        for(Iterator iterator = hashmap.keySet().iterator(); iterator.hasNext(); jsonarray.put(jsonobject2))
        {
            Object obj = iterator.next();
            jsonobject2 = new com.fr.json.JSONObject();
            jsonobject2.put("point", obj);
            jsonobject2.put("times", hashmap.get(obj));
        }

        DesignerEnvManager designerenvmanager = DesignerEnvManager.getEnvManager();
        jsonobject.put("username", designerenvmanager.getBBSName());
        jsonobject.put("uuid", designerenvmanager.getUUID());
        jsonobject.put("functions", jsonarray);
        try
        {
            return jsonobject.toString().getBytes("UTF-8");
        }
        catch(UnsupportedEncodingException unsupportedencodingexception)
        {
            FRContext.getLogger().error(unsupportedencodingexception.getMessage());
        }
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }

    private ResultSet selectAllFromLogDB(Connection connection, Table table)
    {
        Select select = new Select(table, DialectFactory.generateDialect(connection));
        PreparedStatement preparedstatement;
        try
        {
            preparedstatement = select.createPreparedStatement(connection);
        }
        catch(SQLException sqlexception)
        {
            return null;
        }
        ResultSet resultset;
        try
        {
            resultset = preparedstatement.executeQuery();
        }
        catch(SQLException sqlexception1)
        {
            try
            {
                preparedstatement.close();
            }
            catch(SQLException sqlexception2) { }
            return null;
        }
        return resultset;
    }

    public void collectStartTime()
    {
        current.setStartDate(dateToString());
        sendUserInfoInOtherThread();
    }

    private void sendUserInfoInOtherThread()
    {
        if(!DesignerEnvManager.getEnvManager().isJoinProductImprove() || !FRContext.isChineseEnv())
        {
            return;
        } else
        {
            Thread thread = new Thread(new Runnable() {

                final InformationCollector this$0;

                public void run()
                {
                    try
                    {
                        Thread.sleep(30000L);
                    }
                    catch(InterruptedException interruptedexception)
                    {
                        FRContext.getLogger().error(interruptedexception.getMessage());
                    }
                    sendFunctionsInfo();
                    sendUserInfo();
                }

            
            {
                this$0 = InformationCollector.this;
                super();
            }
            }
);
            thread.start();
            return;
        }
    }

    public void collectStopTime()
    {
        current.setStopDate(dateToString());
    }

    private String dateToString()
    {
        DateFormat dateformat = FRContext.getDefaultValues().getDateTimeFormat();
        return dateformat.format(new Date());
    }

    private void reset()
    {
        startStop.clear();
        lastTime = dateToString();
    }

    private File getInfoFile()
    {
        return new File(StableUtils.pathJoin(new String[] {
            ProductConstants.getEnvHome(), "fr.info"
        }));
    }

    public void saveXMLFile()
    {
        File file = getInfoFile();
        try
        {
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            XMLTools.writeOutputStreamXML(this, bytearrayoutputstream);
            bytearrayoutputstream.flush();
            bytearrayoutputstream.close();
            String s = new String(bytearrayoutputstream.toByteArray(), "UTF-8");
            String s1 = DesUtils.getEncString(s);
            writeEncodeContentToFile(s1, file);
        }
        catch(Exception exception)
        {
            FRContext.getLogger().error(exception.getMessage());
        }
    }

    private static void writeEncodeContentToFile(String s, File file)
    {
        BufferedWriter bufferedwriter = null;
        FileOutputStream fileoutputstream = new FileOutputStream(file);
        OutputStreamWriter outputstreamwriter = new OutputStreamWriter(fileoutputstream, "UTF-8");
        bufferedwriter = new BufferedWriter(outputstreamwriter);
        bufferedwriter.write(s);
        if(bufferedwriter != null)
            try
            {
                bufferedwriter.close();
            }
            catch(IOException ioexception) { }
        break MISSING_BLOCK_LABEL_97;
        Exception exception;
        exception;
        FRContext.getLogger().error(exception.getMessage());
        if(bufferedwriter != null)
            try
            {
                bufferedwriter.close();
            }
            catch(IOException ioexception1) { }
        break MISSING_BLOCK_LABEL_97;
        Exception exception1;
        exception1;
        if(bufferedwriter != null)
            try
            {
                bufferedwriter.close();
            }
            catch(IOException ioexception2) { }
        throw exception1;
    }

    public void writeXML(XMLPrintWriter xmlprintwriter)
    {
        startStop.add(current);
        xmlprintwriter.startTAG("Info");
        writeStartStopList(xmlprintwriter);
        writeTag("LastTime", lastTime, xmlprintwriter);
        xmlprintwriter.end();
    }

    private void writeStartStopList(XMLPrintWriter xmlprintwriter)
    {
        xmlprintwriter.startTAG("StartStopList");
        for(int i = 0; i < startStop.size(); i++)
            ((StartStopTime)startStop.get(i)).writeXML(xmlprintwriter);

        xmlprintwriter.end();
    }

    private void writeTag(String s, String s1, XMLPrintWriter xmlprintwriter)
    {
        if(StringUtils.isEmpty(s1))
        {
            return;
        } else
        {
            xmlprintwriter.startTAG(s);
            xmlprintwriter.textNode(s1);
            xmlprintwriter.end();
            return;
        }
    }

    public void readXML(XMLableReader xmlablereader)
    {
        if(xmlablereader.isChildNode())
        {
            String s = xmlablereader.getTagName();
            if("StartStopList".equals(s))
                readStartStopList(xmlablereader);
            else
            if("LastTime".equals(s))
                readLastTime(xmlablereader);
        }
    }

    private void readLastTime(XMLableReader xmlablereader)
    {
        String s;
        if(StringUtils.isNotBlank(s = xmlablereader.getElementValue()))
            lastTime = s;
    }

    private void readStartStopList(XMLableReader xmlablereader)
    {
        startStop.clear();
        xmlablereader.readXMLObject(new XMLReadable() {

            final InformationCollector this$0;

            public void readXML(XMLableReader xmlablereader1)
            {
                if("StartStop".equals(xmlablereader1.getTagName()))
                {
                    StartStopTime startstoptime = new StartStopTime();
                    xmlablereader1.readXMLObject(startstoptime);
                    startStop.add(startstoptime);
                }
            }

            
            {
                this$0 = InformationCollector.this;
                super();
            }
        }
);
    }



}
