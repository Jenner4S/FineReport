// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design;

import com.fr.base.FRContext;
import com.fr.base.Utils;
import com.fr.general.*;
import com.fr.stable.*;
import com.fr.stable.xml.*;
import java.io.*;
import java.util.Date;

public class ChartEnvManager
    implements XMLReadable, XMLWriter
{

    public static final String ACTIVE_KEY = "RXWY-A25421-K58F47757-7373";
    private static final int ONE_MONTH_SECOND = 0x278d00;
    private static final int MS = 1000;
    boolean isPushUpdateAuto;
    private String activationKey;
    private static ChartEnvManager chartEnvManager;
    private Date lastCheckDate;
    private long checkTimeSpan;
    private static File envFile;

    public ChartEnvManager()
    {
        isPushUpdateAuto = true;
        activationKey = null;
        checkTimeSpan = 0x278d00L;
    }

    public static ChartEnvManager getEnvManager()
    {
        if(chartEnvManager == null)
        {
            chartEnvManager = new ChartEnvManager();
            try
            {
                XMLTools.readFileXML(chartEnvManager, chartEnvManager.getDesignerEnvFile());
            }
            catch(Exception exception)
            {
                FRContext.getLogger().error(exception.getMessage(), exception);
            }
        }
        return chartEnvManager;
    }

    private File getEnvFile()
    {
        return envFile;
    }

    private File getDesignerEnvFile()
    {
        File file = getEnvFile();
        if(!file.exists())
            createEnvFile(file);
        return file;
    }

    private void createEnvFile(File file)
    {
        try
        {
            FileWriter filewriter = new FileWriter(file);
            StringReader stringreader = new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Env></Env>");
            Utils.copyCharTo(stringreader, filewriter);
            stringreader.close();
            filewriter.close();
        }
        catch(IOException ioexception)
        {
            FRContext.getLogger().error(ioexception.getMessage(), ioexception);
        }
    }

    public String getActivationKey()
    {
        return activationKey;
    }

    public void setActivationKey(String s)
    {
        activationKey = s;
    }

    public void setPushUpdateAuto(boolean flag)
    {
        isPushUpdateAuto = flag;
        if(!isPushUpdateAuto)
            lastCheckDate = new Date();
    }

    public boolean isPushUpdateAuto()
    {
        return isPushUpdateAuto;
    }

    public boolean isOverOneMonth()
    {
        return !isPushUpdateAuto && ((new Date()).getTime() - lastCheckDate.getTime()) / 1000L >= checkTimeSpan;
    }

    public void resetCheckDate()
    {
        lastCheckDate = new Date();
    }

    public void readXML(XMLableReader xmlablereader)
    {
        if(xmlablereader.isChildNode())
        {
            String s = xmlablereader.getTagName();
            if(ComparatorUtils.equals(s, "ChartAttributes"))
            {
                activationKey = xmlablereader.getAttrAsString("activationKey", null);
                isPushUpdateAuto = xmlablereader.getAttrAsBoolean("isPushUpdateAuto", true);
                checkTimeSpan = xmlablereader.getAttrAsLong("checkTimeSpan", 0x278d00L);
                String s1 = xmlablereader.getAttrAsString("lastCheckDate", null);
                if(!StringUtils.isEmpty(s1))
                    lastCheckDate = DateUtils.string2Date(s1, true);
                else
                    lastCheckDate = new Date();
            }
        }
    }

    public void writeXML(XMLPrintWriter xmlprintwriter)
    {
        xmlprintwriter.startTAG("ChartDesigner");
        xmlprintwriter.startTAG("ChartAttributes").attr("activationKey", activationKey).attr("isPushUpdateAuto", isPushUpdateAuto).attr("checkTimeSpan", checkTimeSpan).attr("lastCheckDate", DateUtils.getDate2LStr(lastCheckDate)).end();
        xmlprintwriter.end();
    }

    public void saveXMLFile()
    {
        File file = getDesignerEnvFile();
        if(file == null)
            return;
        if(!file.getParentFile().exists())
            StableUtils.mkdirs(file.getParentFile());
        String s = (new StringBuilder()).append(file.getName()).append(".tmp").toString();
        File file1 = new File(file.getParentFile(), s);
        writeTempFile(file1);
        IOUtils.renameTo(file1, file);
    }

    private void writeTempFile(File file)
    {
        try
        {
            FileOutputStream fileoutputstream = new FileOutputStream(file);
            XMLTools.writeOutputStreamXML(this, fileoutputstream);
            fileoutputstream.flush();
            fileoutputstream.close();
        }
        catch(Exception exception)
        {
            FRContext.getLogger().error(exception.getMessage());
        }
    }

    static 
    {
        envFile = new File((new StringBuilder()).append(ProductConstants.getEnvHome()).append(File.separator).append(ProductConstants.APP_NAME).append("ChartEnv.xml").toString());
    }
}
