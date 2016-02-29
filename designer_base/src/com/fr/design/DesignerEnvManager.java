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
    // name和Env的键值对
    private Map<String, Env> nameEnvMap = new ListMap();
    // marks: 当前报表服务器名字
    private String curEnvName = null;
    private boolean showProjectPane = true;
    private boolean showDataPane = true;
    //p:这是当前选择的数据库连接的名字,这个在新建数据源的时候用到.
    private String recentSelectedConnection = null;
    // 设计器预览时的Jetty端口
    public int jettyServerPort = 8075;
    // eason:
    private boolean supportUndo = true;
    private boolean supportDefaultParentCalculate = true;
    //samuel:支持字符串编辑为公式
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
    //2014-8-26默认显示全部, 因为以前的版本, 虽然是false, 实际上是显示所有表, 因此这边要兼容
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
    //当前设计器用户的论坛昵称
    private String bbsName;
    //当前设计器用户的论坛密码
    private String bbsPassword;
	//上一次登录弹窗的时间, 为了控制一天只弹一次窗口
    private String lastShowBBSTime;
    //上一次资讯弹窗时间， 为了控制一天只弹一次
    private String lastShowBBSNewsTime;
    //设计器的唯一ID, 用于远程设计和在线验证激活码
    private String uuid;
    //记录当前激活码的在线激活状态.
    private int activeKeyStatus = -1;
    private boolean joinProductImprove = true;
    
    
    public static final String CAS_CERTIFICATE_PATH = "certificatePath";
    
    public static final String CAS_CERTIFICATE_PASSWORD = "certificatePass";
    
    public static final String CAS_PARAS = "CASParas";
    
    //https链接所需的证书路径
    private String certificatePath = StringUtils.EMPTY;
    
    //https链接所需的证书密码
    private String certificatePass = StringUtils.EMPTY;
    
    //是否启用https连接
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

            // james：如果没有env定义，要设置一个默认的
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
     * 添加设计器中相关的worker
     *
     * @param worker 相关的worker
     */
    public static void addWorkers(SwingWorker worker) {
        mapWorkerList.add(worker);
    }

    /**
     * 删除设计器中相关的worker.
     *
     * @param worker 相关的worker
     */
    public static void removeWorkers(SwingWorker worker) {
        if (mapWorkerList.contains(worker)) {
            mapWorkerList.remove(worker);
        }
    }

    /**
     * 做完设计器中现存的地图worker
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
     * richer:载入log设置
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

        // 写文件的LogLocation
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
        // james:FineReportEnv.xml文件没有必要做兼容，里面保存的主要是界面布局以及设计器激活的信息
        // 对于这些内容，除了激活码以外，其他的兼容毫无意义。对于激活码，也可以不兼容，正好知道用户是否进行了升级呢
        // 对于自己升级的用户，正好知道有升级的用户呢。对于我们帮助升级的，给用户一个激活码就是啦
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
                // marks:兼容DesignerEnv6-1.xml
                FileReader fileReader = new FileReader(oldEnvFile);
                Utils.copyCharTo(fileReader, fileWriter);
                fileReader.close();
            } else {
                // marks:生成一个新的xml文件
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
     * 是否启用了https
     * @return 同上
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
     * 返回上次打开的模板文件
     */
    public String getLastOpenFile() {
        return lastOpenFilePath;
    }

    /**
     * 设置记录 上次打开的模板文件
     */
    public void setLastOpenFile(String lastOpenFilePath) {
        this.lastOpenFilePath = lastOpenFilePath;
    }


    /**
     * 得到西面板的上下子面板的高度区分
     *
     * @return
     */
    public int getLastWestRegionToolPaneY() {
        return this.westRegionToolPaneY;
    }

    /**
     * 得到上次关闭设计器时的西边面板的宽度
     *
     * @return
     */
    public int getLastWestRegionContainerWidth() {
        return this.westRegionContainerWidth;
    }

    /**
     * 设置西面板的上下子面板的高度区分
     *
     * @param toolPaneY
     */
    public void setLastWestRegionToolPaneY(int toolPaneY) {
        this.westRegionToolPaneY = toolPaneY;
    }

    /**
     * 设置上次关闭设计器时的西边面板的宽度
     *
     * @param westRegionContainerWidth
     */
    public void setLastWestRegionContainerWidth(int westRegionContainerWidth) {
        this.westRegionContainerWidth = westRegionContainerWidth;
    }


    /**
     * 得到上次关闭设计器前东部面板的上下子面板的高度区分
     *
     * @return
     */
    public int getLastEastRegionToolPaneY() {
        return this.eastRegionToolPaneY;
    }

    /**
     * 得到上次关闭设计器前东部面板的宽度
     *
     * @return
     */
    public int getLastEastRegionContainerWidth() {
        return this.eastRegionContainerWidth;
    }

    /**
     * 设置上次关闭设计器前东部面板的上下子面板的高度区分
     *
     * @param toolPaneY
     */
    public void setLastEastRegionToolPaneY(int toolPaneY) {
        this.eastRegionToolPaneY = toolPaneY;
    }

    /**
     * 设置上次关闭设计器前东部面板的宽度
     *
     * @param eastRegionContainerWidth
     */
    public void setLastEastRegionContainerWidth(int eastRegionContainerWidth) {
        this.eastRegionContainerWidth = eastRegionContainerWidth;
    }


    /**
     * 判断当前环境是否为默认
     *
     * @return 是默认则返回true
     */
    public boolean isCurrentEnvDefault() {
        Env currentEnv = this.getEnv(curEnvName);
        String defaultEnvPath = StableUtils.pathJoin(new String[]{StableUtils.getInstallHome(), ProjectConstants.WEBAPP_NAME, ProjectConstants.WEBINF_NAME});
        return ComparatorUtils.equals(new File(defaultEnvPath).getPath(), currentEnv.getPath());
    }

    /**
     * 返回默认环境
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
     * 返回默认环境名称
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
     * 设置当前环境为默认
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
     * 模板Tree是否展开
     *
     * @return 展开则返回true
     */
    public boolean isTemplateTreePaneExpanded() {
        return templateTreePaneExpanded;
    }

    /**
     * 设置模板Tree是否展开
     */
    public void setTemplateTreePaneExpanded(boolean templateTreePaneExpanded) {
        this.templateTreePaneExpanded = templateTreePaneExpanded;
    }

    /**
     * 知否自动备份
     *
     * @return 是则返回true
     */
    public boolean isAutoBackUp() {
        return autoBackUp;
    }

    /**
     * 设置是否自动备份
     */
    public void setAutoBackUp(boolean autoBackUp) {
        this.autoBackUp = autoBackUp;
    }

    /**
     * 返回页面长度单位
     */
    public short getPageLengthUnit() {
        return pageLengthUnit;
    }

    /**
     * 设置页面长度单位
     */
    public void setPageLengthUnit(short pageLengthUnit) {
        this.pageLengthUnit = pageLengthUnit;
    }

    /**
     * 返回报表长度单位
     */
    public short getReportLengthUnit() {
        return reportLengthUnit;
    }

    /**
     * 设置报表长度单位
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
	 * 保存设计器的配置文件, 该文件不在env的resource目录下
	 * 而是在Consts.getEnvHome() + File.separator + Consts.APP_NAME
	 * 
	 *
	 * @date 2014-9-29-上午11:04:23
	 *
	 */
    public void saveXMLFile() {
    	File xmlFile = this.getDesignerEnvFile();
		if (xmlFile == null) {
			return;
		}
		if (!xmlFile.getParentFile().exists()) {//建立目录.
			StableUtils.mkdirs(xmlFile.getParentFile());
		}
    	
		String tempName = xmlFile.getName() + ProjectConstants.TEMP_SUFFIX;
		File tempFile = new File(xmlFile.getParentFile(), tempName);
		
		writeTempFile(tempFile);
		IOUtils.renameTo(tempFile, xmlFile);
    }

    /**
     * 设置是否使用磁盘空间
     */
    public void setOracleSystemSpace(boolean displayOracleSystem) {
        this.useOracleSystemSpace = displayOracleSystem;
    }
    
    /**
	 * 是否加入产品改良
	 * 
	 * @return 是否加入产品改良
	 * 
	 */
	public boolean isJoinProductImprove() {
		return joinProductImprove;
	}

	/**
	 * 设置加入产品改良
	 * 
	 */
	public void setJoinProductImprove(boolean joinProductImprove) {
		this.joinProductImprove = joinProductImprove;
	}

	/**
     * 是否磁盘空间参数
     *
     * @return 是则返回true
     */
    public boolean isOracleSystemSpace() {
        return this.useOracleSystemSpace;
    }

    /**
     * 返回语言类型
     */
    public int getLanguage() {
        return this.language;
    }

    /**
     * 设置语言参数
     */
    public void setLanguage(int i) {
        this.language = i;
    }

    /**
     * 返回环境名称迭代器
     */
    public Iterator<String> getEnvNameIterator() {
        return this.nameEnvMap.keySet().iterator();
    }

    /**
     * 根据名称返回环境
     */
    public Env getEnv(String name) {
        return this.nameEnvMap.get(name);
    }

    /**
     * 记录名称 和对应的环境
     *
     * @param name 名称
     * @param env  对应的环境
     */
    public void putEnv(String name, Env env) {
        this.nameEnvMap.put(name, env);
    }

    /**
     * 删除名称对应的环境
     *
     * @param name 环境的名字
     */
    public void removeEnv(String name) {
        this.nameEnvMap.remove(name);
    }

    /**
     * 清除全部环境
     */
    public void clearAllEnv() {
        this.nameEnvMap.clear();
    }

    /**
     * 返回界面的大小范围.
     */
    public Rectangle getWindowBounds() {
        return this.windowBounds;
    }

    /**
     * 设置界面的大小范围
     */
    public void setWindowBounds(Rectangle windowBounds) {
        this.windowBounds = windowBounds;
    }


    /**
     * 返回当前环境的名称.
     */
    public String getCurEnvName() {
        return this.curEnvName;
    }

    /**
     * 设置当前环境的名称
     */
    public void setCurEnvName(String envName) {
        this.curEnvName = envName;
    }

    /**
     * 返回Jetty服务器的端口号
     */
    public int getJettyServerPort() {
        return this.jettyServerPort;
    }

    /**
     * 设置Jetty服务器的端口号
     */
    public void setJettyServerPort(int jettyServerPort) {
        if (jettyServerPort <= 0) {
            return;
        }
        this.jettyServerPort = jettyServerPort;
    }

    /**
     * 返回对话框当前路径
     */
    public String getDialogCurrentDirectory() {
        return DialogCurrentDirectory;
    }

    /**
     * 设置当前对话框路径
     */
    public void setDialogCurrentDirectory(String dialogCurrentDirectory) {
        this.DialogCurrentDirectory = dialogCurrentDirectory;
    }

    /**
     * 返回当前路径前缀
     */
    public String getCurrentDirectoryPrefix() {
        return CurrentDirectoryPrefix;
    }

    /**
     * 设置当前路径前缀
     */
    public void setCurrentDirectoryPrefix(String prefix) {
        this.CurrentDirectoryPrefix = prefix;
    }


    /**
     * 返回最近打开的文件路径列表
     */
    public List<String> getRecentOpenedFilePathList() {
        return this.recentOpenedFilePathList;
    }

    /**
     * 添加最近打开的文件路径
     *
     * @param filePath 文件路径
     */
    public void addRecentOpenedFilePath(String filePath) {
        // 先删除.
        if (this.recentOpenedFilePathList.contains(filePath)) {
            this.recentOpenedFilePathList.remove(filePath);
        }

        this.recentOpenedFilePathList.add(0, filePath);
        checkRecentOpenedFileNum();
    }

    /**
     * 替换近期打开的文件路径
     *
     * @param oldPath 旧的路径
     * @param newPath 新的路径
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
     * 移除最近打开的文件路径
     *
     * @param filePath 文件路径
     */
    public void removeRecentOpenedFilePath(String filePath) {
        if (this.recentOpenedFilePathList.contains(filePath)) {
            this.recentOpenedFilePathList.remove(filePath);
        }
    }


    /**
     * 是否展示toolbar
     *
     * @return 是则返回true
     */
    public boolean isShowPaintToolBar() {
        return showPaintToolBar;
    }

    /**
     * 设置是否展示toolbar
     */
    public void setShowPaintToolBar(boolean showPaintToolBar) {
        this.showPaintToolBar = showPaintToolBar;
    }

    /**
     * 是否支持撤销
     *
     * @return 是则返回true
     */
    public boolean isSupportUndo() {
        return supportUndo;
    }

    /**
     * 设置是否支持撤销
     */
    public void setSupportUndo(boolean supportUndo) {
        this.supportUndo = supportUndo;
    }

    /**
     * 是否支持默认父类计算
     *
     * @return 是则返回true
     */
    public boolean isSupportDefaultParentCalculate() {
        return supportDefaultParentCalculate;
    }

    /**
     * 设置是否支持默认父类计算
     */
    public void setSupportDefaultParentCalculate(boolean supportDefaultParentCalculate) {
        this.supportDefaultParentCalculate = supportDefaultParentCalculate;
    }

    /**
     * 设置是否支持字符串转为公式
     *
     * @return 支持则返回true
     */
    public boolean isSupportStringToFormula() {
        return supportStringToFormula;
    }

    /**
     * 设置是否支持字符串转为公式
     */
    public void setSupportStringToFormula(boolean supportStringToFormula) {
        this.supportStringToFormula = supportStringToFormula;
    }

    /**
     * 是否默认字符串转为公式
     *
     * @return 是则返回true
     */
    public boolean isDefaultStringToFormula() {
        return defaultStringToFormula;
    }

    /**
     * 设置是否支持字符串转为公式
     */
    public void setDefaultStringToFormula(boolean defaultStringToFormula) {
        this.defaultStringToFormula = defaultStringToFormula;
    }

    /**
     * 获取快捷键名称
     */
    public String getAutoCompleteShortcuts() {
        return autoCompleteShortcuts;
    }

    /**
     * 设置快捷键名称
     */
    public void setAutoCompleteShortcuts(String autoCompleteShortcuts) {
        this.autoCompleteShortcuts = autoCompleteShortcuts;
    }


    /**
     * 列表头是否可见
     *
     * @return 是则返回true
     */
    public boolean isColumnHeaderVisible() {
        return columnHeaderVisible;
    }

    /**
     * 设置列表头是否可见
     */
    public void setColumnHeaderVisible(boolean columnHeaderVisible) {
        this.columnHeaderVisible = columnHeaderVisible;
    }

    /**
     * 行表头是否可见
     *
     * @return 是则返回true
     */
    public boolean isRowHeaderVisible() {
        return rowHeaderVisible;
    }

    /**
     * 设置行表头是否可见
     */
    public void setRowHeaderVisible(boolean rowHeaderVisible) {
        this.rowHeaderVisible = rowHeaderVisible;
    }

    /**
     * 垂直滚动条是否可见
     *
     * @return 是则返回true
     */
    public boolean isVerticalScrollBarVisible() {
        return verticalScrollBarVisible;
    }

    /**
     * 设置垂直滚动条可见
     */
    public void setVerticalScrollBarVisible(boolean verticalScrollBarVisible) {
        this.verticalScrollBarVisible = verticalScrollBarVisible;
    }

    /**
     * 水平滚动条是否可见
     *
     * @return 是则返回true
     */
    public boolean isHorizontalScrollBarVisible() {
        return horizontalScrollBarVisible;
    }

    /**
     * 设置水平滚动条是否可见
     */
    public void setHorizontalScrollBarVisible(boolean horizontalScrollBarVisible) {
        this.horizontalScrollBarVisible = horizontalScrollBarVisible;
    }

    /**
     * 返回网格线的颜色
     */
    public Color getGridLineColor() {
        return gridLineColor;
    }

    /**
     * 设置网格线的颜色
     */
    public void setGridLineColor(Color gridLineColor) {
        this.gridLineColor = gridLineColor;
    }

    /**
     * 返回页面的线颜色
     */
    public Color getPaginationLineColor() {
        return paginationLineColor;
    }

    /**
     * 设置页面的线颜色
     */
    public void setPaginationLineColor(Color paginationLineColor) {
        this.paginationLineColor = paginationLineColor;

    }

    /**
     * 是否支持单元格编辑器
     *
     * @return 是则返回true
     */
    public boolean isSupportCellEditorDef() {
        return supportCellEditorDef;
    }

    /**
     * 设置是否支持单元格编辑器
     */
    public void setSupportCellEditorDef(boolean supportCellEditorDef) {
        this.supportCellEditorDef = supportCellEditorDef;
    }

    /**
     * 是否允许拖拽
     *
     * @return 是则返回true
     */
    public boolean isDragPermited() {
        return isDragPermited;
    }

    /**
     * 设置是否允许拖拽
     */
    public void setDragPermited(boolean isDragPermited) {
        this.isDragPermited = isDragPermited;
    }

    /**
     * 返回最大预览的行数
     */
    public int getMaxNumberOrPreviewRow() {
        return maxNumberOrPreviewRow;
    }

    /**
     * 设置最大预览的行数
     */
    public void setMaxNumberOrPreviewRow(int maxNumberOrPreviewRow) {
        this.maxNumberOrPreviewRow = maxNumberOrPreviewRow;
    }

    /**
     * 是否展示工程界面
     *
     * @return 是则返回true
     */
    public boolean isShowProjectPane() {
        return showProjectPane;
    }

    /**
     * 设置是否显示工程界面.
     */
    public void setShowProjectPane(boolean showProjectPane) {
        this.showProjectPane = showProjectPane;
    }

    /**
     * 是否展示数据界面
     *
     * @return 是则返回true
     */
    public boolean isShowDataPane() {
        return showDataPane;
    }

    /**
     * 设置是否显示数据界面
     */
    public void setShowDataPane(boolean showDataPane) {
        this.showDataPane = showDataPane;
    }

    /**
     * 返回最近选择的链接
     */
    public String getRecentSelectedConnection() {
        return recentSelectedConnection;
    }

    /**
     * 设置最近选择的链接
     */
    public void setRecentSelectedConnection(String recentlySelectedConnectionName) {
        this.recentSelectedConnection = recentlySelectedConnectionName;
    }

    /**
     * 返回激活码
     */
    public String getActivationKey() {
        return activationKey;
    }

    /**
     * 设置激活码
     */
    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    /**
     * 返回Log的位置
     */
    public String getLogLocation() {
        return logLocation;
    }

    /**
     * 设置Log的位置
     */
    public void setLogLocation(String logsLocation) {
        this.logLocation = logsLocation;
    }

    /**
     * 返回日志的等级
     */
    public Level getLogLevel() {
        return this.level;
    }

    /**
     * 设置log的等级
     */
    public void setLogLevel(Level level) {
        this.level = level;
    }

    /**
     * 设置撤销的限制次数
     */
    public void setUndoLimit(int undoLimit) {
        this.undoLimit = undoLimit;
    }

    /**
     * 返回撤销的限制次数
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
            if (name.equals("XMLVersion")) {// 兼容09.12.30前把XMLVersion写在根目录下的第一个标签中
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
        	// bug33217,705是好的，不知道711里因为什么把这段代码注释了，现打开
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
        // peter:读取webinfLocation
        if ((tmpVal = reader.getAttrAsString("webinfLocation", null)) != null) {
            // marks:兼容6.1的
            // marks:设置默认的目录.
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
        // marks:设置默认的webInf名字
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
                        // marks:获取名字
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

        // xml读完之后,看一下nameEnvMap是不是长度为0
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
                    // alex:以前一直是写ResentOpenedFilePath
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

	//写入uuid
    private void writeUUID(XMLPrintWriter writer){
    	writer.startTAG("uuid");
    	writer.textNode(getUUID());
    	writer.end();
    }

    //读取uuid
	private void readUUID(XMLableReader reader){
		String tmpVal;
		if (StringUtils.isNotBlank(tmpVal = reader.getElementValue())) {
			this.uuid = tmpVal;
		}
	}
	
	//写入激活状态
	private void writeActiveStatus(XMLPrintWriter writer){
		if (this.activeKeyStatus == 0){
			writer.startTAG("status");
			writer.textNode(this.activeKeyStatus + "");
			writer.end();
		}
	}
	
	//读取激活状态
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
    
    //写论坛相关的两个属性
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
        // eason: ReportPaneAttributions报表支持的 一些功能和Grid的GUI属性等等
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
         * 写Env为xml
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
         * 从xml读Env
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