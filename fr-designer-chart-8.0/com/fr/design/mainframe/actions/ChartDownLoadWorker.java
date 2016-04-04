// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.actions;

import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.file.SaveSomeTemplatePane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.json.JSONObject;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import javax.swing.SwingWorker;

// Referenced classes of package com.fr.design.mainframe.actions:
//            UpdateVersion

public class ChartDownLoadWorker extends SwingWorker
{

    private static final String FILE_PATH = "http://chart.finedevelop.com/update/";
    private static final String VERSION = "version";
    private static final String TEMP = "_temp";
    private static final int BYTE = 0x25800;
    private static final int FILE_BYTE = 1024;
    private HashMap files;

    public ChartDownLoadWorker()
    {
        files = new HashMap();
    }

    private void loadFilesPaths()
        throws Exception
    {
        files.clear();
        String s = StableUtils.getInstallHome();
        JSONObject jsonobject = UpdateVersion.getJsonContent();
        if(jsonobject == null)
            return;
        Iterator iterator = jsonobject.keys();
        do
        {
            if(!iterator.hasNext())
                break;
            String s1 = (String)iterator.next();
            if(!ComparatorUtils.equals(s1, "version"))
            {
                String s2 = (String)jsonobject.get(s1);
                String s3 = (new StringBuilder()).append(s).append(s2.substring(2)).toString();
                files.put(s1, s3);
            }
        } while(true);
        files.isEmpty();
    }

    protected Void doInBackground()
        throws Exception
    {
        try
        {
            loadFilesPaths();
            Set set = files.keySet();
            Iterator iterator = set.iterator();
            int i;
            URLConnection urlconnection;
            for(i = 0; iterator.hasNext(); i += urlconnection.getContentLength())
            {
                String s = (String)iterator.next();
                String s1 = (new StringBuilder()).append("http://chart.finedevelop.com/update/").append(s).toString();
                URL url = new URL(s1);
                urlconnection = url.openConnection();
            }

            int j = 0;
            for(Iterator iterator1 = set.iterator(); iterator1.hasNext();)
            {
                String s2 = (String)iterator1.next();
                String s3 = (new StringBuilder()).append("http://chart.finedevelop.com/update/").append(s2).toString();
                URL url1 = new URL(s3);
                InputStream inputstream = url1.openStream();
                String s4 = (String)files.get(s2);
                int k = s4.lastIndexOf(".");
                String s5 = (new StringBuilder()).append(s4.substring(0, k)).append("_temp").append(s4.substring(k)).toString();
                FileOutputStream fileoutputstream = new FileOutputStream(s5);
                byte abyte0[] = new byte[0x25800];
                int l = 0;
                while((l = inputstream.read(abyte0)) > 0) 
                {
                    fileoutputstream.write(abyte0, 0, l);
                    abyte0 = new byte[0x25800];
                    j += l;
                    publish(new Double[] {
                        Double.valueOf((double)j / (double)i)
                    });
                }
            }

        }
        catch(Exception exception)
        {
            throw new Exception((new StringBuilder()).append("Update Failed !").append(exception.getMessage()).toString());
        }
        return null;
    }

    protected void replaceFiles()
    {
        try
        {
            Set set = files.keySet();
            FileInputStream fileinputstream;
            for(Iterator iterator = set.iterator(); iterator.hasNext(); fileinputstream.close())
            {
                String s = (String)iterator.next();
                String s1 = (String)files.get(s);
                int i = s1.lastIndexOf(".");
                String s2 = (new StringBuilder()).append(s1.substring(0, i)).append("_temp").append(s1.substring(i)).toString();
                fileinputstream = new FileInputStream(s2);
                FileOutputStream fileoutputstream = new FileOutputStream(s1);
                byte abyte0[] = new byte[1024];
                for(int j = 0; (j = fileinputstream.read(abyte0)) > 0;)
                {
                    fileoutputstream.write(abyte0, 0, j);
                    abyte0 = new byte[1024];
                }

                fileoutputstream.flush();
                fileoutputstream.close();
            }

        }
        catch(Exception exception)
        {
            FRLogger.getLogger().error(exception.getMessage());
        }
    }

    public void done()
    {
        SaveSomeTemplatePane savesometemplatepane = new SaveSomeTemplatePane(true);
        if(HistoryTemplateListPane.getInstance().getHistoryCount() == 1)
        {
            int i = savesometemplatepane.saveLastOneTemplate();
            if(i != 2)
                restartChartDesigner();
        } else
        if(savesometemplatepane.showSavePane())
            restartChartDesigner();
    }

    private void restartChartDesigner()
    {
        String s = StableUtils.getInstallHome();
        if(StringUtils.isEmpty(s) || ComparatorUtils.equals(".", s))
        {
            DesignerContext.getDesignerFrame().exit();
            return;
        }
        try
        {
            String s1 = (new StringBuilder()).append(s).append(File.separator).append("bin").append(File.separator).append("restart.bat").toString();
            ProcessBuilder processbuilder = new ProcessBuilder(new String[] {
                s1, s
            });
            processbuilder.start();
            DesignerContext.getDesignerFrame().exit();
        }
        catch(Exception exception)
        {
            FRLogger.getLogger().error(exception.getMessage());
        }
    }

    protected volatile Object doInBackground()
        throws Exception
    {
        return doInBackground();
    }
}
