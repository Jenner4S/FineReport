/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design;

import com.fr.base.BaseXMLUtils;
import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.base.Utils;
import com.fr.dav.LocalEnv;
import com.fr.design.constants.UIConstants;
import com.fr.env.RemoteEnv;
import com.fr.env.SignIn;
import com.fr.file.FILEFactory;
import com.fr.general.*;
import com.fr.stable.*;
import com.fr.stable.core.UUID;
import com.fr.stable.project.ProjectConstants;
import com.fr.stable.xml.*;

import javax.swing.*;
import javax.swing.SwingWorker.StateValue;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

/**
 * The manager of Designer GUI.
 */
public class DesignerEnvManager implements XMLReadable, XMLWriter {

    private static final int MAX_SHOW_NUM = 10;

    private static DesignerEnvManager designerEnvManager; // gui.
    private String activationKey = null;
    private String logLocation = null;
    private Rectangle windowBounds = null; // window bounds.
    private String DialogCurrentDirectory = null;
    private String CurrentDirectoryPrefix = null;
    private List<String> recentOpenedFilePathList = new ArrayList<String>();
    private boolean showPaintToolBar = true;
    private int maxNumberOrPreviewRow = 200;
    // name��Env�ļ�ֵ��
    private Map<String, Env> nameEnvMap = new ListMap();
    // marks: ��ǰ�������������
    private String curEnvName = null;
    private boolean showProjectPane = true;
    private boolean showDataPane = true;
    //p:���ǵ�ǰѡ������ݿ����ӵ�����,������½�����Դ��ʱ���õ�.
    private String recentSelectedConnection = null;
    // �����Ԥ��ʱ��Jetty�˿�
    public int jettyServerPort = 8075;
    // eason:
    private boolean supportUndo = true;
    private boolean supportDefaultParentCalculate = true;
    //samuel:֧���ַ����༭Ϊ��ʽ
    private boolean supportStringToFormula = true;
    private boolean defaultStringToFormula = false;
    private String autoCompleteShortcuts = UIConstants.DEFAULT_AUTO_COMPLETE;
    private boolean columnHeaderVisible = true;
    private boolean rowHeaderVisible = true;
    private boolean verticalScrollBarVisible = true;
    private boolean horizontalScrollBarVisible = true;
    private Color gridLineColor = Color.lightGray; // line color.
    private Color paginationLineColor = Color.black; // line color of paper
    private boolean supportCellEditorDef = false;
    private boolean isDragPermited = false;
    private Level level = Level.INFO;
    private int language;
    //2014-8-26Ĭ����ʾȫ��, ��Ϊ��ǰ�İ汾, ��Ȼ��false, ʵ��������ʾ���б�, ������Ҫ����
    private boolean useOracleSystemSpace = true;
    private boolean autoBackUp = true;
    private int undoLimit = 5;
    private short pageLengthUnit = Constants.UNIT_MM;
    private short reportLengthUnit = Constants.UNIT_MM;
    private boolean templateTreePaneExpanded = false;
    private String lastOpenFilePath = null;

    private int eastRegionToolPaneY = 300;
    private int eastRegionContainerWidth = 260;

    private int westRegionToolPaneY = 300;
    private int westRegionContainerWidth = 240;
    private String encryptionKey;
    private String jdkHome;
    //��ǰ������û�����̳�ǳ�
    private String bbsName;
    //��ǰ������û�����̳����
    private String bbsPassword;
	//��һ�ε�¼������ʱ��, Ϊ�˿���һ��ֻ��һ�δ���
    private String lastShowBBSTime;
    //��һ����Ѷ����ʱ�䣬 Ϊ�˿���һ��ֻ��һ��
    private String lastShowBBSNewsTime;
    //�������ΨһID, ����Զ����ƺ�������֤������
    private String uuid;
    //��¼��ǰ����������߼���״̬.
    private int activeKeyStatus = -1;
    private boolean joinProductImprove = true;
    
    
    public static final String CAS_CERTIFICATE_PATH = "certificatePath";
    
    public static final String CAS_CERTIFICATE_PASSWORD = "certificatePass";
    
    public static final String CAS_PARAS = "CASParas";
    
    //https���������֤��·��
    private String certificatePath = StringUtils.EMPTY;
    
    //https���������֤������
    private String certificatePass = StringUtils.EMPTY;
    
    //�Ƿ�����https����
    private boolean isHttps = false;
    
    private static List<SwingWorker> mapWorkerList = new ArrayList<SwingWorker>();

    /**
     * DesignerEnvManager.
     */
    public static DesignerEnvManager getEnvManager() {
        if (designerEnvManager == null) {
            designerEnvManager = new DesignerEnvManager();
            try {
                XMLTools.readFileXML(designerEnvManager, designerEnvManager.getDesignerEnvFile());
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }

            // james�����û��env���壬Ҫ����һ��Ĭ�ϵ�
            if (designerEnvManager.nameEnvMap.size() <= 0) {
                String installHome = StableUtils.getInstallHome();
                if (installHome != null) {
                    String name = Inter.getLocText("Default");
                    String envPath = StableUtils.pathJoin(new String[]{installHome, ProjectConstants.WEBAPP_NAME, ProjectConstants.WEBINF_NAME});
                    designerEnvManager.putEnv(name, LocalEnv.createEnv(envPath));
                    designerEnvManager.setCurEnvName(name);
                }
            }
        }

        GeneralContext.addEnvChangedListener(new EnvChangedListener() {
            @Override
            public void envChanged() {

                designerEnvManager.setCurrentDirectoryPrefix(FILEFactory.ENV_PREFIX);
                designerEnvManager.setDialogCurrentDirectory(ProjectConstants.REPORTLETS_NAME);
            }
        });

        return designerEnvManager;
    }

    /**
     * ������������ص�worker
     *
     * @param worker ��ص�worker
     */
    public static void addWorkers(SwingWorker worker) {
        mapWorkerList.add(worker);
    }

    /**
     * ɾ�����������ص�worker.
     *
     * @param worker ��ص�worker
     */
    public static void removeWorkers(SwingWorker worker) {
        if (mapWorkerList.contains(worker)) {
            mapWorkerList.remove(worker);
        }
    }

    /**
     * ������������ִ�ĵ�ͼworker
     */
    public static void doEndMapSaveWorkersIndesign() {
        for (int i = 0; i < mapWorkerList.size(); i++) {
            SwingWorker worker = mapWorkerList.get(i);
            if (worker.getState() != StateValue.DONE) {
                worker.execute();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    FRLogger.getLogger().error("Map Save Error");
                }
            }
        }
    }

    /**
     * richer:����log����
     */
    public static void loadLogSetting() {
        DesignerEnvManager designerEnvManager = DesignerEnvManager.getEnvManager();
        Level logLevel = designerEnvManager.getLogLevel();
        if (logLevel != null) {
            FRContext.getLogger().setLogLevel(logLevel, true);
        }
        if (StringUtils.isNotEmpty(designerEnvManager.getJdkHome())) {
            System.setProperty("java.home", designerEnvManager.getJdkHome());
        }

        // д�ļ���LogLocation
        String logLocation = DesignerEnvManager.getEnvManager().getLogLocation();
        if (logLocation != null) {
            try {
                Calendar calender = GregorianCalendar.getInstance();
                calender.setTimeInMillis(System.currentTimeMillis());
                String today = calender.get(Calendar.YEAR) + "-" + (calender.get(Calendar.MONTH) + 1) + "-" + calender.get(Calendar.DAY_OF_MONTH);

                String fileName = StableUtils.pathJoin(new String[]{
                        logLocation, "fr_" + today + "_%g.log"
                });
                if (!new java.io.File(fileName).exists()) {
                    StableUtils.makesureFileExist(new java.io.File(fileName));
                }
                Handler handler = new FileHandler(fileName, true);
                handler.setFormatter(new SimpleFormatter());
                FRContext.getLogger().addLogHandler(handler);
            } catch (SecurityException e) {
                FRContext.getLogger().error(e.getMessage(), e);
            } catch (IOException e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
    }

    private File getDesignerEnvFile() {
        File envFile = getEnvFile();
        // james:FineReportEnv.xml�ļ�û�б�Ҫ�����ݣ����汣�����Ҫ�ǽ��沼���Լ�������������Ϣ
        // ������Щ���ݣ����˼��������⣬�����ļ��ݺ������塣���ڼ����룬Ҳ���Բ����ݣ�����֪���û��Ƿ������������
        // �����Լ��������û�������֪�����������û��ء��������ǰ��������ģ����û�һ�������������
        if (!envFile.exists()) {
            createEnvFile(envFile);
        }

        return envFile;
    }

    private void createEnvFile(File envFile) {
        try {
            FileWriter fileWriter = new FileWriter(envFile);
            File oldEnvFile = new File(ProductConstants.getEnvHome() + File.separator + ProductConstants.APP_NAME + "6-1" + "Env.xml");
            if (oldEnvFile.exists()) {
                // marks:����DesignerEnv6-1.xml
                FileReader fileReader = new FileReader(oldEnvFile);
                Utils.copyCharTo(fileReader, fileWriter);
                fileReader.close();
            } else {
                // marks:����һ���µ�xml�ļ�
                StringReader stringReader = new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Env></Env>");
                Utils.copyCharTo(stringReader, fileWriter);
                stringReader.close();
            }
            fileWriter.close();
        } catch (IOException e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
    }

    public static void setEnvFile(File envFile) {
        DesignerEnvManager.envFile = envFile;
    }

    private static File envFile = null;

    private File getEnvFile() {
        if (envFile == null) {
            envFile = new File(ProductConstants.getEnvHome() + File.separator + ProductConstants.APP_NAME + "Env.xml");
        }
        return envFile;
    }
    
    /**
     * �Ƿ�������https
     * @return ͬ��
     */
    public boolean isHttps() {
		return isHttps;
	}


	public void setHttps(boolean isHttps) {
		this.isHttps = isHttps;
	}


	public String getCertificatePath() {
		return certificatePath;
	}


	public void setCertificatePath(String certificatePath) {
		this.certificatePath = certificatePath;
	}


	public String getCertificatePass() {
		return certificatePass;
	}
	
	public void setCertificatePass(String certificatePass){
		this.certificatePass = certificatePass;
	}

    /**
     * �����ϴδ򿪵�ģ���ļ�
     */
    public String getLastOpenFile() {
        return lastOpenFilePath;
    }

    /**
     * ���ü�¼ �ϴδ򿪵�ģ���ļ�
     */
    public void setLastOpenFile(String lastOpenFilePath) {
        this.lastOpenFilePath = lastOpenFilePath;
    }


    /**
     * �õ����������������ĸ߶�����
     *
     * @return
     */
    public int getLastWestRegionToolPaneY() {
        return this.westRegionToolPaneY;
    }

    /**
     * �õ��ϴιر������ʱ���������Ŀ��
     *
     * @return
     */
    public int getLastWestRegionContainerWidth() {
        return this.westRegionContainerWidth;
    }

    /**
     * �������������������ĸ߶�����
     *
     * @param toolPaneY
     */
    public void setLastWestRegionToolPaneY(int toolPaneY) {
        this.westRegionToolPaneY = toolPaneY;
    }

    /**
     * �����ϴιر������ʱ���������Ŀ��
     *
     * @param westRegionContainerWidth
     */
    public void setLastWestRegionContainerWidth(int westRegionContainerWidth) {
        this.westRegionContainerWidth = westRegionContainerWidth;
    }


    /**
     * �õ��ϴιر������ǰ�����������������ĸ߶�����
     *
     * @return
     */
    public int getLastEastRegionToolPaneY() {
        return this.eastRegionToolPaneY;
    }

    /**
     * �õ��ϴιر������ǰ�������Ŀ��
     *
     * @return
     */
    public int getLastEastRegionContainerWidth() {
        return this.eastRegionContainerWidth;
    }

    /**
     * �����ϴιر������ǰ�����������������ĸ߶�����
     *
     * @param toolPaneY
     */
    public void setLastEastRegionToolPaneY(int toolPaneY) {
        this.eastRegionToolPaneY = toolPaneY;
    }

    /**
     * �����ϴιر������ǰ�������Ŀ��
     *
     * @param eastRegionContainerWidth
     */
    public void setLastEastRegionContainerWidth(int eastRegionContainerWidth) {
        this.eastRegionContainerWidth = eastRegionContainerWidth;
    }


    /**
     * �жϵ�ǰ�����Ƿ�ΪĬ��
     *
     * @return ��Ĭ���򷵻�true
     */
    public boolean isCurrentEnvDefault() {
        Env currentEnv = this.getEnv(curEnvName);
        String defaultEnvPath = StableUtils.pathJoin(new String[]{StableUtils.getInstallHome(), ProjectConstants.WEBAPP_NAME, ProjectConstants.WEBINF_NAME});
        return ComparatorUtils.equals(new File(defaultEnvPath).getPath(), currentEnv.getPath());
    }

    /**
     * ����Ĭ�ϻ���
     */
    public Env getDefaultEnv() {
        String installHome = StableUtils.getInstallHome();
        String defaultenvPath = StableUtils.pathJoin(new String[]{installHome, ProjectConstants.WEBAPP_NAME, ProjectConstants.WEBINF_NAME});
        defaultenvPath = new File(defaultenvPath).getPath();
        if (nameEnvMap.size() >= 0) {
            Iterator<Entry<String, Env>> entryIt = nameEnvMap.entrySet().iterator();
            while (entryIt.hasNext()) {
                Entry<String, Env> entry = entryIt.next();
                Env env = entry.getValue();
                if (ComparatorUtils.equals(defaultenvPath, env.getPath())) {
                    return env;
                }
            }
        }
        Env newDefaultEnv = LocalEnv.createEnv(defaultenvPath);
        this.putEnv(Inter.getLocText(new String[]{"Default", "Utils-Report_Runtime_Env"}), newDefaultEnv);
        return newDefaultEnv;
    }

    /**
     * ����Ĭ�ϻ�������
     */
    public String getDefaultEnvName() {
        String installHome = StableUtils.getInstallHome();
        String defaultenvPath = StableUtils.pathJoin(new String[]{installHome, ProjectConstants.WEBAPP_NAME, ProjectConstants.WEBINF_NAME});
        defaultenvPath = new File(defaultenvPath).getPath();
        if (nameEnvMap.size() >= 0) {
            Iterator<Entry<String, Env>> entryIt = nameEnvMap.entrySet().iterator();
            while (entryIt.hasNext()) {
                Entry<String, Env> entry = entryIt.next();
                Env env = entry.getValue();
                if (ComparatorUtils.equals(defaultenvPath, env.getPath())) {
                    return entry.getKey();
                }
            }
        }
        return Inter.getLocText(new String[]{"Default", "Utils-Report_Runtime_Env"});
    }

    /**
     * ���õ�ǰ����ΪĬ��
     */
    public void setCurrentEnv2Default() {
        if (isCurrentEnvDefault()) {
            return;
        }
        try {
            SignIn.signIn(getDefaultEnv());
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
    }

    /**
     * ģ��Tree�Ƿ�չ��
     *
     * @return չ���򷵻�true
     */
    public boolean isTemplateTreePaneExpanded() {
        return templateTreePaneExpanded;
    }

    /**
     * ����ģ��Tree�Ƿ�չ��
     */
    public void setTemplateTreePaneExpanded(boolean templateTreePaneExpanded) {
        this.templateTreePaneExpanded = templateTreePaneExpanded;
    }

    /**
     * ֪���Զ�����
     *
     * @return ���򷵻�true
     */
    public boolean isAutoBackUp() {
        return autoBackUp;
    }

    /**
     * �����Ƿ��Զ�����
     */
    public void setAutoBackUp(boolean autoBackUp) {
        this.autoBackUp = autoBackUp;
    }

    /**
     * ����ҳ�泤�ȵ�λ
     */
    public short getPageLengthUnit() {
        return pageLengthUnit;
    }

    /**
     * ����ҳ�泤�ȵ�λ
     */
    public void setPageLengthUnit(short pageLengthUnit) {
        this.pageLengthUnit = pageLengthUnit;
    }

    /**
     * ���ر����ȵ�λ
     */
    public short getReportLengthUnit() {
        return reportLengthUnit;
    }

    /**
     * ���ñ����ȵ�λ
     */
    public void setReportLengthUnit(short reportLengthUnit) {
        this.reportLengthUnit = reportLengthUnit;
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

	/**
	 * ����������������ļ�, ���ļ�����env��resourceĿ¼��
	 * ������Consts.getEnvHome() + File.separator + Consts.APP_NAME
	 * 
	 *
	 * @date 2014-9-29-����11:04:23
	 *
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

    /**
     * �����Ƿ�ʹ�ô��̿ռ�
     */
    public void setOracleSystemSpace(boolean displayOracleSystem) {
        this.useOracleSystemSpace = displayOracleSystem;
    }
    
    /**
	 * �Ƿ�����Ʒ����
	 * 
	 * @return �Ƿ�����Ʒ����
	 * 
	 */
	public boolean isJoinProductImprove() {
		return joinProductImprove;
	}

	/**
	 * ���ü����Ʒ����
	 * 
	 */
	public void setJoinProductImprove(boolean joinProductImprove) {
		this.joinProductImprove = joinProductImprove;
	}

	/**
     * �Ƿ���̿ռ����
     *
     * @return ���򷵻�true
     */
    public boolean isOracleSystemSpace() {
        return this.useOracleSystemSpace;
    }

    /**
     * ������������
     */
    public int getLanguage() {
        return this.language;
    }

    /**
     * �������Բ���
     */
    public void setLanguage(int i) {
        this.language = i;
    }

    /**
     * ���ػ������Ƶ�����
     */
    public Iterator<String> getEnvNameIterator() {
        return this.nameEnvMap.keySet().iterator();
    }

    /**
     * �������Ʒ��ػ���
     */
    public Env getEnv(String name) {
        return this.nameEnvMap.get(name);
    }

    /**
     * ��¼���� �Ͷ�Ӧ�Ļ���
     *
     * @param name ����
     * @param env  ��Ӧ�Ļ���
     */
    public void putEnv(String name, Env env) {
        this.nameEnvMap.put(name, env);
    }

    /**
     * ɾ�����ƶ�Ӧ�Ļ���
     *
     * @param name ����������
     */
    public void removeEnv(String name) {
        this.nameEnvMap.remove(name);
    }

    /**
     * ���ȫ������
     */
    public void clearAllEnv() {
        this.nameEnvMap.clear();
    }

    /**
     * ���ؽ���Ĵ�С��Χ.
     */
    public Rectangle getWindowBounds() {
        return this.windowBounds;
    }

    /**
     * ���ý���Ĵ�С��Χ
     */
    public void setWindowBounds(Rectangle windowBounds) {
        this.windowBounds = windowBounds;
    }


    /**
     * ���ص�ǰ����������.
     */
    public String getCurEnvName() {
        return this.curEnvName;
    }

    /**
     * ���õ�ǰ����������
     */
    public void setCurEnvName(String envName) {
        this.curEnvName = envName;
    }

    /**
     * ����Jetty�������Ķ˿ں�
     */
    public int getJettyServerPort() {
        return this.jettyServerPort;
    }

    /**
     * ����Jetty�������Ķ˿ں�
     */
    public void setJettyServerPort(int jettyServerPort) {
        if (jettyServerPort <= 0) {
            return;
        }
        this.jettyServerPort = jettyServerPort;
    }

    /**
     * ���ضԻ���ǰ·��
     */
    public String getDialogCurrentDirectory() {
        return DialogCurrentDirectory;
    }

    /**
     * ���õ�ǰ�Ի���·��
     */
    public void setDialogCurrentDirectory(String dialogCurrentDirectory) {
        this.DialogCurrentDirectory = dialogCurrentDirectory;
    }

    /**
     * ���ص�ǰ·��ǰ׺
     */
    public String getCurrentDirectoryPrefix() {
        return CurrentDirectoryPrefix;
    }

    /**
     * ���õ�ǰ·��ǰ׺
     */
    public void setCurrentDirectoryPrefix(String prefix) {
        this.CurrentDirectoryPrefix = prefix;
    }


    /**
     * ��������򿪵��ļ�·���б�
     */
    public List<String> getRecentOpenedFilePathList() {
        return this.recentOpenedFilePathList;
    }

    /**
     * �������򿪵��ļ�·��
     *
     * @param filePath �ļ�·��
     */
    public void addRecentOpenedFilePath(String filePath) {
        // ��ɾ��.
        if (this.recentOpenedFilePathList.contains(filePath)) {
            this.recentOpenedFilePathList.remove(filePath);
        }

        this.recentOpenedFilePathList.add(0, filePath);
        checkRecentOpenedFileNum();
    }

    /**
     * �滻���ڴ򿪵��ļ�·��
     *
     * @param oldPath �ɵ�·��
     * @param newPath �µ�·��
     */
    public void replaceRecentOpenedFilePath(String oldPath, String newPath) {
        if (this.recentOpenedFilePathList.contains(oldPath)) {
            int index = recentOpenedFilePathList.indexOf(oldPath);
            this.recentOpenedFilePathList.remove(oldPath);
            this.recentOpenedFilePathList.add(index, newPath);
        }
    }

    private void checkRecentOpenedFileNum() {
        if (this.recentOpenedFilePathList == null) {
            return;
        }
        while (this.recentOpenedFilePathList.size() > MAX_SHOW_NUM) {
            this.recentOpenedFilePathList.remove(this.recentOpenedFilePathList.size() - 1);
        }
    }

    /**
     * �Ƴ�����򿪵��ļ�·��
     *
     * @param filePath �ļ�·��
     */
    public void removeRecentOpenedFilePath(String filePath) {
        if (this.recentOpenedFilePathList.contains(filePath)) {
            this.recentOpenedFilePathList.remove(filePath);
        }
    }


    /**
     * �Ƿ�չʾtoolbar
     *
     * @return ���򷵻�true
     */
    public boolean isShowPaintToolBar() {
        return showPaintToolBar;
    }

    /**
     * �����Ƿ�չʾtoolbar
     */
    public void setShowPaintToolBar(boolean showPaintToolBar) {
        this.showPaintToolBar = showPaintToolBar;
    }

    /**
     * �Ƿ�֧�ֳ���
     *
     * @return ���򷵻�true
     */
    public boolean isSupportUndo() {
        return supportUndo;
    }

    /**
     * �����Ƿ�֧�ֳ���
     */
    public void setSupportUndo(boolean supportUndo) {
        this.supportUndo = supportUndo;
    }

    /**
     * �Ƿ�֧��Ĭ�ϸ������
     *
     * @return ���򷵻�true
     */
    public boolean isSupportDefaultParentCalculate() {
        return supportDefaultParentCalculate;
    }

    /**
     * �����Ƿ�֧��Ĭ�ϸ������
     */
    public void setSupportDefaultParentCalculate(boolean supportDefaultParentCalculate) {
        this.supportDefaultParentCalculate = supportDefaultParentCalculate;
    }

    /**
     * �����Ƿ�֧���ַ���תΪ��ʽ
     *
     * @return ֧���򷵻�true
     */
    public boolean isSupportStringToFormula() {
        return supportStringToFormula;
    }

    /**
     * �����Ƿ�֧���ַ���תΪ��ʽ
     */
    public void setSupportStringToFormula(boolean supportStringToFormula) {
        this.supportStringToFormula = supportStringToFormula;
    }

    /**
     * �Ƿ�Ĭ���ַ���תΪ��ʽ
     *
     * @return ���򷵻�true
     */
    public boolean isDefaultStringToFormula() {
        return defaultStringToFormula;
    }

    /**
     * �����Ƿ�֧���ַ���תΪ��ʽ
     */
    public void setDefaultStringToFormula(boolean defaultStringToFormula) {
        this.defaultStringToFormula = defaultStringToFormula;
    }

    /**
     * ��ȡ��ݼ�����
     */
    public String getAutoCompleteShortcuts() {
        return autoCompleteShortcuts;
    }

    /**
     * ���ÿ�ݼ�����
     */
    public void setAutoCompleteShortcuts(String autoCompleteShortcuts) {
        this.autoCompleteShortcuts = autoCompleteShortcuts;
    }


    /**
     * �б�ͷ�Ƿ�ɼ�
     *
     * @return ���򷵻�true
     */
    public boolean isColumnHeaderVisible() {
        return columnHeaderVisible;
    }

    /**
     * �����б�ͷ�Ƿ�ɼ�
     */
    public void setColumnHeaderVisible(boolean columnHeaderVisible) {
        this.columnHeaderVisible = columnHeaderVisible;
    }

    /**
     * �б�ͷ�Ƿ�ɼ�
     *
     * @return ���򷵻�true
     */
    public boolean isRowHeaderVisible() {
        return rowHeaderVisible;
    }

    /**
     * �����б�ͷ�Ƿ�ɼ�
     */
    public void setRowHeaderVisible(boolean rowHeaderVisible) {
        this.rowHeaderVisible = rowHeaderVisible;
    }

    /**
     * ��ֱ�������Ƿ�ɼ�
     *
     * @return ���򷵻�true
     */
    public boolean isVerticalScrollBarVisible() {
        return verticalScrollBarVisible;
    }

    /**
     * ���ô�ֱ�������ɼ�
     */
    public void setVerticalScrollBarVisible(boolean verticalScrollBarVisible) {
        this.verticalScrollBarVisible = verticalScrollBarVisible;
    }

    /**
     * ˮƽ�������Ƿ�ɼ�
     *
     * @return ���򷵻�true
     */
    public boolean isHorizontalScrollBarVisible() {
        return horizontalScrollBarVisible;
    }

    /**
     * ����ˮƽ�������Ƿ�ɼ�
     */
    public void setHorizontalScrollBarVisible(boolean horizontalScrollBarVisible) {
        this.horizontalScrollBarVisible = horizontalScrollBarVisible;
    }

    /**
     * ���������ߵ���ɫ
     */
    public Color getGridLineColor() {
        return gridLineColor;
    }

    /**
     * ���������ߵ���ɫ
     */
    public void setGridLineColor(Color gridLineColor) {
        this.gridLineColor = gridLineColor;
    }

    /**
     * ����ҳ�������ɫ
     */
    public Color getPaginationLineColor() {
        return paginationLineColor;
    }

    /**
     * ����ҳ�������ɫ
     */
    public void setPaginationLineColor(Color paginationLineColor) {
        this.paginationLineColor = paginationLineColor;

    }

    /**
     * �Ƿ�֧�ֵ�Ԫ��༭��
     *
     * @return ���򷵻�true
     */
    public boolean isSupportCellEditorDef() {
        return supportCellEditorDef;
    }

    /**
     * �����Ƿ�֧�ֵ�Ԫ��༭��
     */
    public void setSupportCellEditorDef(boolean supportCellEditorDef) {
        this.supportCellEditorDef = supportCellEditorDef;
    }

    /**
     * �Ƿ�������ק
     *
     * @return ���򷵻�true
     */
    public boolean isDragPermited() {
        return isDragPermited;
    }

    /**
     * �����Ƿ�������ק
     */
    public void setDragPermited(boolean isDragPermited) {
        this.isDragPermited = isDragPermited;
    }

    /**
     * �������Ԥ��������
     */
    public int getMaxNumberOrPreviewRow() {
        return maxNumberOrPreviewRow;
    }

    /**
     * �������Ԥ��������
     */
    public void setMaxNumberOrPreviewRow(int maxNumberOrPreviewRow) {
        this.maxNumberOrPreviewRow = maxNumberOrPreviewRow;
    }

    /**
     * �Ƿ�չʾ���̽���
     *
     * @return ���򷵻�true
     */
    public boolean isShowProjectPane() {
        return showProjectPane;
    }

    /**
     * �����Ƿ���ʾ���̽���.
     */
    public void setShowProjectPane(boolean showProjectPane) {
        this.showProjectPane = showProjectPane;
    }

    /**
     * �Ƿ�չʾ���ݽ���
     *
     * @return ���򷵻�true
     */
    public boolean isShowDataPane() {
        return showDataPane;
    }

    /**
     * �����Ƿ���ʾ���ݽ���
     */
    public void setShowDataPane(boolean showDataPane) {
        this.showDataPane = showDataPane;
    }

    /**
     * �������ѡ�������
     */
    public String getRecentSelectedConnection() {
        return recentSelectedConnection;
    }

    /**
     * �������ѡ�������
     */
    public void setRecentSelectedConnection(String recentlySelectedConnectionName) {
        this.recentSelectedConnection = recentlySelectedConnectionName;
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

    /**
     * ����Log��λ��
     */
    public String getLogLocation() {
        return logLocation;
    }

    /**
     * ����Log��λ��
     */
    public void setLogLocation(String logsLocation) {
        this.logLocation = logsLocation;
    }

    /**
     * ������־�ĵȼ�
     */
    public Level getLogLevel() {
        return this.level;
    }

    /**
     * ����log�ĵȼ�
     */
    public void setLogLevel(Level level) {
        this.level = level;
    }

    /**
     * ���ó��������ƴ���
     */
    public void setUndoLimit(int undoLimit) {
        this.undoLimit = undoLimit;
    }

    /**
     * ���س��������ƴ���
     */
    public int getUndoLimit() {
        return undoLimit;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getJdkHome() {
        return this.jdkHome;
    }

    public void setJdkHome(String home) {
        this.jdkHome = home;
    }

    public String getBBSName() {
		return bbsName;
	}

	public void setBBSName(String bbsName) {
		this.bbsName = bbsName;
	}
	
    public String getBBSPassword() {
		return bbsPassword;
	}

	public void setBBSPassword(String bbsPassword) {
		this.bbsPassword = bbsPassword;
	}
	
	public String getLastShowBBSTime() {
		return lastShowBBSTime;
	}

	public void setLastShowBBSTime(String lastShowBBSTime) {
		this.lastShowBBSTime = lastShowBBSTime;
	}
	
	public String getLastShowBBSNewsTime() {
		return lastShowBBSNewsTime;
	}

	public void setLastShowBBSNewsTime(String lastShowBBSNewsTime) {
		this.lastShowBBSNewsTime = lastShowBBSNewsTime;
	}

	private void readXMLVersion(XMLableReader reader){
		String tmpVal;
        if ((tmpVal = reader.getElementValue()) != null) {
            reader.setXmlVersionByString(tmpVal);
        }
	}
	
	private void readActiveKey(XMLableReader reader){
		String tmpVal;
        if ((tmpVal = reader.getElementValue()) != null) {
            this.setActivationKey(tmpVal);
        }
	}
	
	private void readLogLocation(XMLableReader reader){
		String tmpVal;
        if ((tmpVal = reader.getElementValue()) != null) {
            this.setLogLocation(tmpVal);
        }
	}
	
	private void readLanguage(XMLableReader reader){
		String tmpVal;
        if ((tmpVal = reader.getElementValue()) != null) {
            this.setLanguage(Integer.parseInt(tmpVal));
        }
	}
	
	private void readJettyPort(XMLableReader reader){
		String tmpVal;
        if ((tmpVal = reader.getElementValue()) != null) {
            this.setJettyServerPort(Integer.parseInt(tmpVal));
        }
	}
	
	private void readPageLengthUnit(XMLableReader reader){
		String tmpVal;
        if (StringUtils.isNotBlank(tmpVal = reader.getElementValue())) {
            this.pageLengthUnit = Short.parseShort(tmpVal);
        }
	}
	
	private void readReportLengthUnit(XMLableReader reader){
		String tmpVal;
        if (StringUtils.isNotBlank(tmpVal = reader.getElementValue())) {
            this.reportLengthUnit = Short.parseShort(tmpVal);
        }
	}
	
	private void readLastOpenFile(XMLableReader reader){
		String tmpVal;
        if (StringUtils.isNotBlank(tmpVal = reader.getElementValue())) {
            this.lastOpenFilePath = tmpVal;
        }
	}
	
	private void readEncrytionKey(XMLableReader reader){
		String tmpVal;
        if (StringUtils.isNotBlank(tmpVal = reader.getElementValue())) {
            this.encryptionKey = tmpVal;
        }
	}
	
	private void readBBSName(XMLableReader reader){
		String tmpVal;
        if (StringUtils.isNotBlank(tmpVal = reader.getElementValue())) {
            this.bbsName = tmpVal;
        }
	}
	
	private void readBBSPassword(XMLableReader reader){
		String tmpVal;
        if (StringUtils.isNotBlank(tmpVal = reader.getElementValue())) {
            this.bbsPassword = CodeUtils.passwordDecode(tmpVal);
        }
	}
	
	private void readLastBBSTime(XMLableReader reader){
		String tmpVal;
		if (StringUtils.isNotBlank(tmpVal = reader.getElementValue())) {
			this.lastShowBBSTime = tmpVal;
		}
	}
	
	private void readLastBBSNewsTime(XMLableReader reader){
		String tmpVal;
		if (StringUtils.isNotBlank(tmpVal = reader.getElementValue())) {
			this.lastShowBBSNewsTime = tmpVal;
		}
	}

	/**
     * Read XML.<br>
     * The method will be invoked when read data from XML file.<br>
     * May override the method to read the data that you saved.
     *
     * @param reader the reader.
     */
    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isChildNode()) {
            String name = reader.getTagName();
            if (name.equals("XMLVersion")) {// ����09.12.30ǰ��XMLVersionд�ڸ�Ŀ¼�µĵ�һ����ǩ��
            	readXMLVersion(reader);
            } else if (name.equals("Attributes")) {
                this.readAttributes(reader);
            } else if (name.equals("ReportPaneAttributions")) {
                this.readReportPaneAttributions(reader);
            } else if ("RecentOpenedFilePathList".equals(name) || "ResentOpenedFilePathList".equals(name)) {
                this.readRecentOpenFileList(reader);
            } else if ("Envs".equals(name) || name.equals("ReportServerMap")) {
                this.readCurEnv(reader);
            } else if (name.equals("ActivationKey")) {
            	readActiveKey(reader);
            } else if ("LogLocation".equals(name)) {
            	readLogLocation(reader);
            } else if ("LogLevel".equals(name)) {
                this.readLogLevel(reader);
            } else if ("Language".equals(name)) {
            	readLanguage(reader);
            } else if ("JettyServerPort".equals(name)) {
            	readJettyPort(reader);
            } else if ("PLengthUnit".equals(name)) {
            	readPageLengthUnit(reader);
            } else if ("RLengthUnit".equals(name)) {
            	readReportLengthUnit(reader);
            } else if ("LastOpenFilePath".equals(name)) {
            	readLastOpenFile(reader);
            } else if ("EncryptionKey".equals(name)) {
            	readEncrytionKey(reader);
            } else if ("jdkHome".equals(name)) {
                this.jdkHome = reader.getElementValue();
            } else if ("bbsName".equals(name)){
            	readBBSName(reader);
            } else if ("bbsPassword".equals(name)){
            	readBBSPassword(reader);
            } else if ("lastBBSTime".equals(name)){
            	readLastBBSTime(reader);
            } else if ("lastBBSNewsTime".equals(name)){
            	readLastBBSNewsTime(reader);
            }else if ("uuid".equals(name)){
            	readUUID(reader);
            } else if ("status".equals(name)){
            	readActiveStatus(reader);
            } else if (ComparatorUtils.equals(CAS_PARAS,name)){
            	readHttpsParas(reader);
            }else {
                readLayout(reader, name);
            }
        }
    }
    
    private void readHttpsParas(XMLableReader reader){
    	String tempVal;
    	if((tempVal = reader.getAttrAsString(CAS_CERTIFICATE_PATH, null)) != null){
    		this.setCertificatePath(tempVal);
    	}
    	if((tempVal = reader.getAttrAsString(CAS_CERTIFICATE_PASSWORD, null)) != null){
    		this.setCertificatePass(tempVal);
    	}
    	
    	this.setHttps(reader.getAttrAsBoolean("enable", false));
    }


    private void readLayout(XMLableReader reader, String name) {
        if ("LastEastRegionLayout".equals(name)) {
            this.readLastEastRegionLayout(reader);
        } else if ("LastWestRegionLayout".equals(name)) {
            this.readLastWestRegionLayout(reader);
        }
    }

    private void readLastEastRegionLayout(XMLableReader reader) {
        String tmpVal;
        if ((tmpVal = reader.getAttrAsString("toolPaneY", null)) != null) {
            this.setLastEastRegionToolPaneY(Integer.parseInt(tmpVal));
        }
        if ((tmpVal = reader.getAttrAsString("containerWidth", null)) != null) {
        	// bug33217,705�Ǻõģ���֪��711����Ϊʲô����δ���ע���ˣ��ִ�
            this.setLastEastRegionContainerWidth(Integer.parseInt(tmpVal));
        }
    }

    private void readLastWestRegionLayout(XMLableReader reader) {
        String tmpVal;
        if ((tmpVal = reader.getAttrAsString("toolPaneY", null)) != null) {
            this.setLastWestRegionToolPaneY(Integer.parseInt(tmpVal));
        }
        if ((tmpVal = reader.getAttrAsString("containerWidth", null)) != null) {
            this.setLastWestRegionContainerWidth(Integer.parseInt(tmpVal));
        }
    }


    private void readAttributes(XMLableReader reader) {
        String tmpVal;
        if ((tmpVal = reader.getAttrAsString("windowBounds", null)) != null) {
            this.setWindowBounds(BaseXMLUtils.readRectangle(tmpVal));
        }
        if ((tmpVal = reader.getAttrAsString("DialogCurrentDirectory", null)) != null) {
            this.setDialogCurrentDirectory(tmpVal);
        }
        if ((tmpVal = reader.getAttrAsString("CurrentDirectoryPrefix", null)) != null) {
            this.setCurrentDirectoryPrefix(tmpVal);
        }
        this.setShowPaintToolBar(reader.getAttrAsBoolean("showPaintToolBar", true));
        this.setMaxNumberOrPreviewRow(reader.getAttrAsInt("maxNumberOrPreviewRow", 200));

        this.setOracleSystemSpace(reader.getAttrAsBoolean("useOracleSystemSpace", true));
        this.setJoinProductImprove(reader.getAttrAsBoolean("joinProductImprove", true));
        this.setAutoBackUp(reader.getAttrAsBoolean("autoBackUp", true));
        this.setTemplateTreePaneExpanded(reader.getAttrAsBoolean("templateTreePaneExpanded", false));
        // peter:��ȡwebinfLocation
        if ((tmpVal = reader.getAttrAsString("webinfLocation", null)) != null) {
            // marks:����6.1��
            // marks:����Ĭ�ϵ�Ŀ¼.
            Env reportServer = LocalEnv.createEnv(tmpVal);

            String curReportServerName = Inter.getLocText("Server-Embedded_Server");
            this.putEnv(curReportServerName, reportServer);
            this.setCurEnvName(curReportServerName);
        }

        this.setShowProjectPane(reader.getAttrAsBoolean("showProjectPane", true));
        this.setShowDataPane(reader.getAttrAsBoolean("showDataPane", true));

        if ((tmpVal = reader.getAttrAsString("recentSelectedConnection", null)) != null) {
            this.setRecentSelectedConnection(tmpVal);
        }
    }

    private void readReportPaneAttributions(XMLableReader reader) {
        String tmpVal;
        this.setSupportUndo(reader.getAttrAsBoolean("supportUndo", true));
        this.setSupportDefaultParentCalculate(reader.getAttrAsBoolean("supportDefaultParentCalculate", true));
        this.setSupportStringToFormula(reader.getAttrAsBoolean("supportStringToFormula", true));
        this.setColumnHeaderVisible(reader.getAttrAsBoolean("columnHeaderVisible", true));
        this.setAutoCompleteShortcuts(reader.getAttrAsString("autoCompleteShortcuts", UIConstants.DEFAULT_AUTO_COMPLETE));
        this.setRowHeaderVisible(reader.getAttrAsBoolean("rowHeaderVisible", true));
        this.setVerticalScrollBarVisible(reader.getAttrAsBoolean("verticalScrollBarVisible", true));
        this.setHorizontalScrollBarVisible(reader.getAttrAsBoolean("horizontalScrollBarVisible", true));
        this.setSupportCellEditorDef(reader.getAttrAsBoolean("supportCellEditorDef", false));
        this.setDragPermited(reader.getAttrAsBoolean("isDragPermited", false));
        this.setUndoLimit(reader.getAttrAsInt("undoLimit", 5));
        this.setDefaultStringToFormula(reader.getAttrAsBoolean("defaultStringToFormula", false));
        if ((tmpVal = reader.getAttrAsString("gridLineColor", null)) != null) {
            this.setGridLineColor(new Color(Integer.parseInt(tmpVal)));
        }
        if ((tmpVal = reader.getAttrAsString("paginationLineColor", null)) != null) {
            this.setPaginationLineColor(new Color(Integer.parseInt(tmpVal)));
        }
    }

    private void readCurEnv(XMLableReader reader) {
        String tmpVal;
        // marks:����Ĭ�ϵ�webInf����
        if ((tmpVal = reader.getAttrAsString("currentEnv", null)) != null) {
            this.setCurEnvName(tmpVal);
        }

        reader.readXMLObject(new XMLReadable() {

            @Override
            public void readXML(XMLableReader reader) {
                if (reader.isAttr()) {
                    DesignerEnvManager.this.clearAllEnv();
                }

                if (reader.isChildNode()) {
                    if (reader.getTagName().equals("Env")) { // description.
                        // marks:��ȡ����
                        String reportServerName = reader.getAttrAsString("name", null);

                        Env env = readEnv(reader);
                        if (env == null) {
                            return;
                        }

                        DesignerEnvManager.this.putEnv(reportServerName, env);
                    }
                }
            }
        });

        // xml����֮��,��һ��nameEnvMap�ǲ��ǳ���Ϊ0
        if (nameEnvMap.isEmpty() && StableUtils.getInstallHome() != null) {
            String install_home = StableUtils.getInstallHome();
            if (install_home != null && new java.io.File(install_home).exists()) {
                nameEnvMap.put("Default", LocalEnv.createEnv(StableUtils.pathJoin(new String[]{
                        install_home, ProjectConstants.WEBAPP_NAME, ProjectConstants.WEBINF_NAME
                })));

                curEnvName = "Default";
            }
        }
    }

    private void readRecentOpenFileList(XMLableReader reader) {
        reader.readXMLObject(new XMLReadable() {

            @Override
            public void readXML(XMLableReader reader) {
                if (reader.isAttr()) {
                    DesignerEnvManager.this.recentOpenedFilePathList.clear();
                }

                if (reader.isChildNode()) {
                    String tmpVal;
                    String name = reader.getTagName();
                    // alex:��ǰһֱ��дResentOpenedFilePath
                    if ("ResentOpenedFilePath".equals(name) || "Path".equals(name)) { // description.
                        if ((tmpVal = reader.getElementValue()) != null) {
                            DesignerEnvManager.this.recentOpenedFilePathList.add(tmpVal);
                        }
                    }
                }
            }
        });
        checkRecentOpenedFileNum();
    }


    private void readLogLevel(XMLableReader reader) {
		String level;
		if ((level = reader.getElementValue()) != null) {
			this.setLogLevel(FRLevel.getByName(level).getLevel());
		}
	}


    /**
     * Write XML.<br>
     * The method will be invoked when save data to XML file.<br>
     * May override the method to save your own data.
     *
     * @param writer the PrintWriter.
     */
    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG("Designer");

        writeAttrues(writer);
        writeReportPaneAttributions(writer);
        writeRecentOpenFileAndEnvList(writer);
        writeSomeAttr(writer);
        writeLastEastRegionLayout(writer);
        writeLastWestRegionLayout(writer);
        writeUUID(writer);
        writeActiveStatus(writer);
        writeHttpsParas(writer);
        writer.end();
    }
	
	public String getUUID() {
		return StringUtils.isEmpty(uuid) ? UUID.randomUUID().toString() : uuid;
	}
	
	public int getActiveKeyStatus() {
		return activeKeyStatus;
	}

	public void setActiveKeyStatus(int activeKeyStatus) {
		this.activeKeyStatus = activeKeyStatus;
	}

	//д��uuid
    private void writeUUID(XMLPrintWriter writer){
    	writer.startTAG("uuid");
    	writer.textNode(getUUID());
    	writer.end();
    }

    //��ȡuuid
	private void readUUID(XMLableReader reader){
		String tmpVal;
		if (StringUtils.isNotBlank(tmpVal = reader.getElementValue())) {
			this.uuid = tmpVal;
		}
	}
	
	//д�뼤��״̬
	private void writeActiveStatus(XMLPrintWriter writer){
		if (this.activeKeyStatus == 0){
			writer.startTAG("status");
			writer.textNode(this.activeKeyStatus + "");
			writer.end();
		}
	}
	
	//��ȡ����״̬
	private void readActiveStatus(XMLableReader reader){
		String tmpVal;
		if (StringUtils.isNotBlank(tmpVal = reader.getElementValue())) {
			this.activeKeyStatus = Integer.parseInt(tmpVal);
		}
	}

    private void writeRecentOpenFileAndEnvList(XMLPrintWriter writer) {
        checkRecentOpenedFileNum();
        writer.startTAG("RecentOpenedFilePathList");
        int resentOpenedFilePathCount = Math.min(12, this.recentOpenedFilePathList.size());
        for (int i = 0; i < resentOpenedFilePathCount; i++) {
            writer.startTAG("Path").textNode(recentOpenedFilePathList.get(i)).end();
        }
        writer.end();

        writer.startTAG("Envs");
        if (this.curEnvName != null) {
            writer.attr("currentEnv", this.curEnvName);
        }
        Iterator<String> nameIt = this.getEnvNameIterator();
        while (nameIt.hasNext()) {
            String envName = nameIt.next();
            Env env = this.getEnv(envName);

            writeEnv(writer, envName, env);
        }

        writer.end();
    }

    private void writeAttrues(XMLPrintWriter writer) {
        writer.startTAG("Attributes");
        if (this.getWindowBounds() != null) {
            writer.attr("windowBounds", BaseXMLUtils.getRectangleText(this.getWindowBounds()));
        }
        // Open directory.
        if (this.getDialogCurrentDirectory() != null) {
            writer.attr("DialogCurrentDirectory", this.getDialogCurrentDirectory());
        }
        if (this.getCurrentDirectoryPrefix() != null) {
            writer.attr("CurrentDirectoryPrefix", this.getCurrentDirectoryPrefix());
        }
        writer.attr("showPaintToolBar", this.isShowPaintToolBar())
                .attr("maxNumberOrPreviewRow", this.getMaxNumberOrPreviewRow())
                .attr("showProjectPane", this.isShowProjectPane())
                .attr("showDataPane", this.isShowDataPane());
        if (StringUtils.isNotBlank(this.getRecentSelectedConnection())) {
            writer.attr("recentSelectedConnection", this.getRecentSelectedConnection());
        }
        if (!this.isOracleSystemSpace()) {
            writer.attr("useOracleSystemSpace", this.isOracleSystemSpace());
        }
        if (!this.isJoinProductImprove()){
        	writer.attr("joinProductImprove", this.isJoinProductImprove());
        }
        if (!this.isAutoBackUp()) {
            writer.attr("autoBackUp", this.isAutoBackUp());
        }
        if (this.isTemplateTreePaneExpanded()) {
            writer.attr("templateTreePaneExpanded", this.isTemplateTreePaneExpanded());
        }
        writer.end();
    }


    private void writeLastEastRegionLayout(XMLPrintWriter writer) {
        writer.startTAG("LastEastRegionLayout");
        if (this.getLastEastRegionToolPaneY() >= 0) {
            writer.attr("toolPaneY ", this.getLastEastRegionToolPaneY());
        }
        if (this.getLastEastRegionContainerWidth() >= 0) {
            writer.attr("containerWidth", this.getLastEastRegionContainerWidth());
        }
        writer.end();
    }

    private void writeLastWestRegionLayout(XMLPrintWriter writer) {
        writer.startTAG("LastWestRegionLayout");
        if (this.getLastWestRegionToolPaneY() >= 0) {
            writer.attr("toolPaneY ", this.getLastWestRegionToolPaneY());
        }
        if (this.getLastWestRegionContainerWidth() >= 0) {
            writer.attr("containerWidth", this.getLastWestRegionContainerWidth());
        }
        writer.end();
    }


    private void writeSomeAttr(XMLPrintWriter writer) {
        if (this.activationKey != null) {
            writer.startTAG("ActivationKey");
            writer.textNode(this.getActivationKey());
            writer.end();
        }

        if (this.encryptionKey != null && (!this.encryptionKey.equals(""))) {
            writer.startTAG("EncryptionKey");
            writer.textNode(this.getEncryptionKey());
            writer.end();
        }

        if (this.logLocation != null && this.logLocation.length() > 0) {
            writer.startTAG("LogLocation");
            writer.textNode(this.logLocation);
            writer.end();
        }

        if (this.level != null) {
            writer.startTAG("LogLevel");
			writer.textNode(FRLevel.getByLevel(this.level).getName());
            writer.end();
        }
        if (StringUtils.isNotBlank(jdkHome)) {
            writer.startTAG("jdkHome");
            writer.textNode(jdkHome);
            writer.end();
        }
        
        writeBBSRelated(writer);

        writer.startTAG("PLengthUnit");
        writer.textNode("" + this.pageLengthUnit);
        writer.end();

        writer.startTAG("RLengthUnit");
        writer.textNode("" + this.reportLengthUnit);
        writer.end();

        writer.startTAG("LastOpenFilePath");
        writer.textNode("" + this.lastOpenFilePath);
        writer.end();

        writer.startTAG("Language").textNode(String.valueOf(this.language)).end()
                .startTAG("JettyServerPort").textNode(String.valueOf(this.jettyServerPort)).end();
    }
    
    //д��̳��ص���������
    private void writeBBSRelated(XMLPrintWriter writer){
        if (StringUtils.isNotBlank(bbsName)) {
        	writer.startTAG("bbsName");
        	writer.textNode(bbsName);
        	writer.end();
        }
        
        if (StringUtils.isNotBlank(bbsPassword)){
        	writer.startTAG("bbsPassword");
        	writer.textNode(CodeUtils.passwordEncode(bbsPassword));
        	writer.end();
        }
        
        if (StringUtils.isNotEmpty(this.lastShowBBSTime)){
        	writer.startTAG("lastBBSTime");
        	writer.textNode(lastShowBBSTime);
        	writer.end();
        }
        
        if (StringUtils.isNotEmpty(this.lastShowBBSNewsTime)){
        	writer.startTAG("lastBBSNewsTime");
        	writer.textNode(lastShowBBSNewsTime);
        	writer.end();
        }
    }
    
    private void writeHttpsParas(XMLPrintWriter writer){
		writer.startTAG(CAS_PARAS);
		if(StringUtils.isNotBlank(certificatePath)){
			writer.attr(CAS_CERTIFICATE_PATH, certificatePath);
		}
		if(StringUtils.isNotBlank(certificatePass)){
			writer.attr(CAS_CERTIFICATE_PASSWORD, certificatePass);
		}
		if(isHttps){
			writer.attr("enable", true);
		}
		writer.end();
    }

    private void writeReportPaneAttributions(XMLPrintWriter writer) {
        // eason: ReportPaneAttributions����֧�ֵ� һЩ���ܺ�Grid��GUI���Եȵ�
        writer.startTAG("ReportPaneAttributions")
                .attr("supportUndo", this.isSupportUndo())
                .attr("supportDefaultParentCalculate", this.isSupportDefaultParentCalculate())
                .attr("supportStringToFormula", this.isSupportStringToFormula())
                .attr("defaultStringToFormula", this.isDefaultStringToFormula())
                .attr("columnHeaderVisible", this.isColumnHeaderVisible())
                .attr("autoCompleteShortcuts", this.getAutoCompleteShortcuts())
                .attr("rowHeaderVisible", this.isRowHeaderVisible())
                .attr("verticalScrollBarVisible", this.isVerticalScrollBarVisible())
                .attr("horizontalScrollBarVisible", this.isHorizontalScrollBarVisible())
                .attr("supportCellEditorDef", this.isSupportCellEditorDef())
                .attr("isDragPermited", this.isDragPermited())
                .attr("gridLineColor", this.getGridLineColor().getRGB())
                .attr("paginationLineColor", this.getPaginationLineColor().getRGB())
                .attr("undoLimit", this.getUndoLimit())
                .end();
    }

    /*
         * дEnvΪxml
         */
    private static void writeEnv(XMLPrintWriter writer, String name, Env env) {
        if (env == null) {
            return;
        }

        writer.startTAG("Env").attr("class", env.getClass().getName()).attr("name", name);

        env.writeXML(writer);

        writer.end();
    }

    /*
         * ��xml��Env
         */
    private static Env readEnv(XMLableReader reader) {
        Env env = null;

        String tmpVal; //temp value
        if ((tmpVal = reader.getAttrAsString("class", null)) != null) {
            if (tmpVal.endsWith(".LocalEnv")) {
                env = LocalEnv.createEnv();
            } else if (tmpVal.endsWith(".RemoteEnv")) {
                env = new RemoteEnv();
            }
        }

        if (env == null) {
            return env;
        }

        reader.readXMLObject(env);

        return env;
    }
}
