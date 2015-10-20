package com.fr.design;

import com.fr.base.FRContext;
import com.fr.base.Utils;
import com.fr.general.ComparatorUtils;
import com.fr.general.DateUtils;
import com.fr.general.IOUtils;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.stable.xml.*;

import java.io.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class ChartEnvManager  implements XMLReadable, XMLWriter {
    public static final String ACTIVE_KEY = "RXWY-A25421-K58F47757-7373";
    private static final int ONE_MONTH_SECOND = 30*24*60*60;//30�죬����Ϊ��λ
    private static final int MS =1000;

    boolean isPushUpdateAuto = true; //�Ƿ��Զ����͸���

    private String activationKey = null;

    private static ChartEnvManager chartEnvManager;

    private Date lastCheckDate;

    private long checkTimeSpan =ONE_MONTH_SECOND;

    /**
     * DesignerEnvManager.
     */
    public static ChartEnvManager getEnvManager() {
        if(chartEnvManager == null){
            chartEnvManager = new ChartEnvManager();
            try {
                XMLTools.readFileXML(chartEnvManager, chartEnvManager.getDesignerEnvFile());
            }catch (Exception exp){
                FRContext.getLogger().error(exp.getMessage(), exp);
            }
        }
        return chartEnvManager;
    }

    private static File envFile = new File(ProductConstants.getEnvHome() + File.separator + ProductConstants.APP_NAME + "ChartEnv.xml");

    private File getEnvFile() {
        return envFile;
    }


    private File getDesignerEnvFile() {
        File envFile = getEnvFile();
        if (!envFile.exists()) {
            createEnvFile(envFile);
        }

        return envFile;
    }


    private void createEnvFile(File envFile) {
        try {
            FileWriter fileWriter = new FileWriter(envFile);
            StringReader stringReader = new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Env></Env>");
            Utils.copyCharTo(stringReader, fileWriter);
            stringReader.close();
            fileWriter.close();
        } catch (IOException e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
    }

    /**
     * ���ؼ�����
     */
    public String getActivationKey() {
        return activationKey;
    }

    /**
     * ���ü�����
     */
    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public void setPushUpdateAuto(boolean isPushUpdateAuto){
        this.isPushUpdateAuto = isPushUpdateAuto;
        if(!this.isPushUpdateAuto){
            lastCheckDate = new Date();
        }
    }

    /**
     * �Ƿ��������Զ�����ͼ����������߸���
     * @return ���򷵻�true
     */
    public boolean isPushUpdateAuto(){
        return isPushUpdateAuto;
    }

    /**
     *�����ò��Զ��������߸��µ�����£�ÿ30���Զ����һ��
     * @return �Ƿ���Ҫ���
     */
    public boolean isOverOneMonth(){
        return !isPushUpdateAuto && ((new Date().getTime()-lastCheckDate.getTime())/MS>=checkTimeSpan);
    }

    /***
     * �����������¼�������
     */
    public void resetCheckDate(){
        this.lastCheckDate = new Date();
    }

    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isChildNode()) {
            String name = reader.getTagName();
            if(ComparatorUtils.equals(name,"ChartAttributes")){
                activationKey = reader.getAttrAsString("activationKey",null);
                isPushUpdateAuto = reader.getAttrAsBoolean("isPushUpdateAuto",true);
                checkTimeSpan = reader.getAttrAsLong("checkTimeSpan",ONE_MONTH_SECOND);
                String date = reader.getAttrAsString("lastCheckDate", null);
                if(!StringUtils.isEmpty(date)){
                    lastCheckDate = DateUtils.string2Date(date,true);
                } else {
                    lastCheckDate = new Date();
                }
            }
        }

    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG("ChartDesigner");
        writer.startTAG("ChartAttributes").attr("activationKey",activationKey)
                .attr("isPushUpdateAuto",isPushUpdateAuto)
                .attr("checkTimeSpan",checkTimeSpan)
                .attr("lastCheckDate", DateUtils.getDate2LStr(lastCheckDate))
                .end();
        writer.end();
    }

    /**
   	 * ����������������ļ�, ���ļ�����env��resourceĿ¼��
   	 * ������Consts.getEnvHome() + File.separator + Consts.APP_NAME
   	 *
   	 *
   	 * @date 2014-9-29-����11:04:23
   	 */
       public void saveXMLFile() {
           File xmlFile = this.getDesignerEnvFile();
   		   if (xmlFile == null) {
   			  return;
   		   }
   		   if (!xmlFile.getParentFile().exists()) {//����Ŀ¼.
   			  StableUtils.mkdirs(xmlFile.getParentFile());
   		   }

   		   String tempName = xmlFile.getName() + ProjectConstants.TEMP_SUFFIX;
           File tempFile = new File(xmlFile.getParentFile(), tempName);

   		   writeTempFile(tempFile);
   		   IOUtils.renameTo(tempFile, xmlFile);
       }

    private void writeTempFile(File tempFile){
    	try{
			OutputStream fout = new FileOutputStream(tempFile);
			XMLTools.writeOutputStreamXML(this, fout);
			fout.flush();
			fout.close();
    	}catch (Exception e) {
    		FRContext.getLogger().error(e.getMessage());
		}
    }
}
