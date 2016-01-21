/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.data.datapane.TableDataNameObjectCreator;
import com.fr.design.data.tabledata.wrapper.TableDataFactory;
import com.fr.design.fun.*;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.core.WidgetOptionFactory;
import com.fr.design.menu.ShortCut;
import com.fr.design.widget.Appearance;
import com.fr.file.XMLFileManager;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.GeneralContext;
import com.fr.general.GeneralUtils;
import com.fr.design.fun.ToolbarItemProvider;
import com.fr.stable.EnvChangedListener;
import com.fr.stable.StringUtils;
import com.fr.stable.plugin.ExtraDesignClassManagerProvider;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.util.*;

/**
 * @author : richie
 * @since : 8.0
 * 用于设计器扩展的管理类
 */
public class ExtraDesignClassManager extends XMLFileManager implements ExtraDesignClassManagerProvider {

    private static final String XML_TAG = "ExtraDesignClassManager";
    private static final String TEMPLATE_TREE_TAG = "TemplateTreeShortCut";

    private static ExtraDesignClassManager classManager;

    public synchronized static ExtraDesignClassManager getInstance() {
        if (classManager == null) {
            classManager = new ExtraDesignClassManager();
            classManager.readXMLFile();
            // 初始化的时候，默认尝试加载的插件
            // richie:先屏蔽掉更新菜单，因为没有设计比较丑
            //classManager.addMenuHandler("com.fr.update.UpdateMenuHandler");
        }

        return classManager;
    }

    static {
        GeneralContext.addEnvChangedListener(new EnvChangedListener() {
            public void envChanged() {
                ExtraDesignClassManager.envChanged();
            }
        });
    }


    private synchronized static void envChanged() {
        classManager = null;
    }

    private List<TableDataNameObjectCreator> reportTDCreators;
    private List<TableDataNameObjectCreator> serverTDCreators;

    private List<WidgetOption> parameterWidgetOptions;
    private Map<Class<? extends Widget>, Class<?>> parameterWidgetOptionsMap;
    private List<WidgetOption> webWidgetOptions;

    private List<WidgetOption> formWidgetOptions;
    private List<WidgetOption> formWidgetContainerOptions;
    private Map<Class<? extends Widget>, Class<?>> formWidgetOptionsMap;

    private List<WidgetOption> cellWidgetOptions;
    private Map<Class<? extends Widget>, Appearance> cellWidgetOptionMap;
    private List<NameObjectCreator> connectionCreators;
    private Set<PreviewProvider> previewProviders;

    private Set<HighlightProvider> highlightProviders;

    private TableDataCreatorProvider tableDataCreatorProvider;

    private List<MenuHandler> menuHandlers;

    private UIFormulaProcessor uiFormulaProcessor;

    private List<PresentKindProvider> presentKindProviders;

    private List<ExportToolBarProvider> exportToolBarProviders;

    private Set<ShortCut> templateTreeShortCutProviders;

    private List<SubmitProvider> submitProviders;

    private List<GlobalListenerProvider> globalListenerProviders;

    private List<JavaScriptActionProvider> javaScriptActionProviders;

    private TitlePlaceProcessor titlePlaceProcessor;

    private FormElementCaseEditorProcessor formElementCaseEditorProcessor;

    private IndentationUnitProcessor indentationUnitProcessor;

    private CellAttributeProvider cellAttributeProvider;

    private Set<HyperlinkProvider> hyperlinkGroupProviders;

    public HyperlinkProvider[] getHyperlinkProvider() {
        if (hyperlinkGroupProviders == null) {
            return new HyperlinkProvider[0];
        }
        return hyperlinkGroupProviders.toArray(new HyperlinkProvider[hyperlinkGroupProviders.size()]);
    }

    public void addHyperlinkProvider(String className) {
        if (StringUtils.isNotEmpty(className)) {
            if (hyperlinkGroupProviders == null) {
                hyperlinkGroupProviders = new HashSet<HyperlinkProvider>();
            }
            try {
                Class clazz = GeneralUtils.classForName(className);
                HyperlinkProvider provider = (HyperlinkProvider) clazz.newInstance();
                hyperlinkGroupProviders.add(provider);
            } catch (Exception e) {
                FRLogger.getLogger().error(e.getMessage(), e);
            }
        }
    }

    public GlobalListenerProvider[] getGlobalListenerProvider() {
        if (globalListenerProviders == null) {
            return new GlobalListenerProvider[0];
        }
        return globalListenerProviders.toArray(new GlobalListenerProvider[globalListenerProviders.size()]);
    }

    /**
     * 获取javaScriptPane
     *
     * @return javaScriptPane集合
     */
    public List<JavaScriptActionProvider> getJavaScriptActionProvider() {
        return javaScriptActionProviders;
    }

    /**
     * 添加一个javaScriptPane
     *
     * @param className 类名
     */
    public void addJavaScriptActionProvider(String className) {
        if (StringUtils.isNotBlank(className)) {
            if (javaScriptActionProviders == null) {
                javaScriptActionProviders = new ArrayList<JavaScriptActionProvider>();
            }
            try {
                Class clazz = GeneralUtils.classForName(className);
                JavaScriptActionProvider provider = (JavaScriptActionProvider) clazz.newInstance();
                if (!javaScriptActionProviders.contains(provider)) {
                    javaScriptActionProviders.add(provider);
                }
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
    }

    /**
     * 添加全局监听
     *
     * @param className 包名
     */
    public void addGlobalListenerProvider(String className) {
        if (StringUtils.isNotBlank(className)) {
            if (globalListenerProviders == null) {
                globalListenerProviders = new ArrayList<GlobalListenerProvider>();
            }
            try {
                Class clazz = GeneralUtils.classForName(className);
                GlobalListenerProvider provider = (GlobalListenerProvider) clazz.newInstance();
                if (!globalListenerProviders.contains(provider)) {
                    globalListenerProviders.add(provider);
                }
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
    }

    public TableDataCreatorProvider getTableDataCreatorProvider() {
        return tableDataCreatorProvider;
    }

    public void setTableDataCreatorProvider(String className) {
        if (StringUtils.isNotBlank(className)) {
            try {
                Class clazz = Class.forName(className);
                tableDataCreatorProvider = (TableDataCreatorProvider) clazz.newInstance();
            } catch (Exception e) {
                FRLogger.getLogger().error(e.getMessage(), e);
            }
        }
    }

    public SubmitProvider[] getSubmitProviders() {
        if (submitProviders == null) {
            return new SubmitProvider[0];
        } else {
            return submitProviders.toArray(new SubmitProvider[submitProviders.size()]);
        }
    }

    /**
     * 添加提交接口
     *
     * @param className 包全名
     */
    public void addSubmitProvider(String className) {
        if (StringUtils.isNotBlank(className)) {
            if (submitProviders == null) {
                submitProviders = new ArrayList<SubmitProvider>();
            }
            try {
                Class clazz = GeneralUtils.classForName(className);
                SubmitProvider provider = (SubmitProvider) clazz.newInstance();
                if (!submitProviders.contains(provider)) {
                    submitProviders.add(provider);
                }
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage());
            }
        }
    }

    public TableDataNameObjectCreator[] getReportTableDataCreators() {
        if (reportTDCreators == null) {
            return new TableDataNameObjectCreator[0];
        } else {
            return reportTDCreators.toArray(new TableDataNameObjectCreator[reportTDCreators.size()]);
        }
    }

    /**
     * 添加reportTDCreators
     *
     * @param className 类名
     */
    public void addTableDataNameObjectCreator(String className) {
        if (StringUtils.isNotBlank(className)) {
            try {
                Class clazz = Class.forName(className);
                if (reportTDCreators == null) {
                    reportTDCreators = new ArrayList<TableDataNameObjectCreator>();
                }
                TableDataDefineProvider provider = (TableDataDefineProvider) clazz.newInstance();
                TableDataNameObjectCreator creator = new TableDataNameObjectCreator(
                        provider.nameForTableData(),
                        provider.prefixForTableData(),
                        provider.iconPathForTableData(),
                        provider.classForTableData(),
                        provider.classForInitTableData(),
                        provider.appearanceForTableData()
                );
                TableDataFactory.register(provider.classForTableData(), creator);
                if (!reportTDCreators.contains(creator)) {
                    reportTDCreators.add(creator);
                }
            } catch (Exception e) {
                FRLogger.getLogger().error("class not found:" + e.getMessage(), e);
            }
        }
    }

    /**
     * 添加serverTDCreators
     *
     * @return 类名
     */
    public TableDataNameObjectCreator[] getServerTableDataCreators() {
        if (serverTDCreators == null) {
            return new TableDataNameObjectCreator[0];
        } else {
            return serverTDCreators.toArray(new TableDataNameObjectCreator[serverTDCreators.size()]);
        }
    }


    /**
     * 添加serverTDCreators
     *
     * @param className 类名
     */
    public void addServerTableDataNameObjectCreator(String className) {
        if (StringUtils.isNotBlank(className)) {
            try {
                Class clazz = Class.forName(className);
                if (serverTDCreators == null) {
                    serverTDCreators = new ArrayList<TableDataNameObjectCreator>();
                }
                TableDataDefineProvider provider = (TableDataDefineProvider) clazz.newInstance();
                TableDataNameObjectCreator creator = new TableDataNameObjectCreator(
                        provider.nameForTableData(),
                        provider.prefixForTableData(),
                        provider.iconPathForTableData(),
                        provider.classForTableData(),
                        provider.classForInitTableData(),
                        provider.appearanceForTableData()
                );
                TableDataFactory.register(provider.classForTableData(), creator);
                if (!serverTDCreators.contains(creator)) {
                    serverTDCreators.add(creator);
                }
            } catch (Exception e) {
                FRLogger.getLogger().error("class not found:" + e.getMessage());
            }
        }
    }

    public Map<Class<? extends Widget>, Class<?>> getParameterWidgetOptionsMap() {
        if (parameterWidgetOptionsMap == null) {
            return new HashMap<Class<? extends Widget>, Class<?>>();
        } else {
            return parameterWidgetOptionsMap;
        }
    }

    public WidgetOption[] getParameterWidgetOptions() {
        if (parameterWidgetOptions == null) {
            return new WidgetOption[0];
        } else {
            return parameterWidgetOptions.toArray(new WidgetOption[parameterWidgetOptions.size()]);
        }
    }

    /**
     * 添加parameterWidgetOptionsMap
     *
     * @param className 类名
     */
    public void addParameterWidgetOption(String className) {
        if (StringUtils.isNotBlank(className)) {
            try {
                Class clazz = Class.forName(className);
                if (parameterWidgetOptions == null) {
                    parameterWidgetOptions = new ArrayList<WidgetOption>();
                }
                if (parameterWidgetOptionsMap == null) {
                    parameterWidgetOptionsMap = new HashMap<Class<? extends Widget>, Class<?>>();
                }
                ParameterWidgetOptionProvider provider = (ParameterWidgetOptionProvider) clazz.newInstance();
                WidgetOption option = WidgetOptionFactory.createByWidgetClass(
                        provider.nameForWidget(),
                        BaseUtils.readIcon(provider.iconPathForWidget()),
                        provider.classForWidget()
                );
                parameterWidgetOptionsMap.put(provider.classForWidget(), provider.appearanceForWidget());
                parameterWidgetOptions.add(option);
            } catch (Exception e) {
                FRLogger.getLogger().error("class not found:" + e.getMessage());
            }
        }
    }

    /**
     * 添加 webWidgetOptions
     *
     * @param className 类名
     * @return 返回 webWidgetOptions
     */
    public void addWebWidgetOption(String className) {
        if (StringUtils.isNotBlank(className)) {
            try {
                Class clazz = Class.forName(className);
                if (webWidgetOptions == null) {
                    webWidgetOptions = new ArrayList<WidgetOption>();
                }
                ToolbarItemProvider provider = (ToolbarItemProvider) clazz.newInstance();
                WidgetOption option = WidgetOptionFactory.createByWidgetClass(
                        provider.nameForWidget(),
                        BaseUtils.readIcon(provider.iconPathForWidget()),
                        provider.classForWidget()
                );
                if (!webWidgetOptions.contains(option)) {
                    webWidgetOptions.add(option);
                }
            } catch (Exception e) {
                FRLogger.getLogger().error("class not found:" + e.getMessage());
            }
        }
    }

    public Map<Class<? extends Widget>, Class<?>> getFormWidgetOptionsMap() {
        if (formWidgetOptionsMap == null) {
            return new HashMap<Class<? extends Widget>, Class<?>>();
        } else {
            return formWidgetOptionsMap;
        }
    }

    public WidgetOption[] getFormWidgetOptions() {
        if (formWidgetOptions == null) {
            return new WidgetOption[0];
        } else {
            return formWidgetOptions.toArray(new WidgetOption[formWidgetOptions.size()]);
        }
    }

    public WidgetOption[] getWebWidgetOptions() {
        if (webWidgetOptions == null) {
            return new WidgetOption[0];
        } else {
            return webWidgetOptions.toArray(new WidgetOption[webWidgetOptions.size()]);
        }
    }

    public WidgetOption[] getFormWidgetContainerOptions() {
        if (formWidgetContainerOptions == null) {
            return new WidgetOption[0];
        } else {
            return formWidgetContainerOptions.toArray(new WidgetOption[formWidgetContainerOptions.size()]);
        }
    }

    /**
     * 添加 formWidgetContainerOptions
     *
     * @param className 类名
     */
    public void addFormWidgetOption(String className) {
        if (StringUtils.isNotBlank(className)) {
            try {
                Class clazz = Class.forName(className);
                if (formWidgetOptions == null) {
                    formWidgetOptions = new ArrayList<WidgetOption>();
                }
                if (formWidgetContainerOptions == null) {
                    formWidgetContainerOptions = new ArrayList<WidgetOption>();
                }
                if (formWidgetOptionsMap == null) {
                    formWidgetOptionsMap = new HashMap<Class<? extends Widget>, Class<?>>();
                }
                FormWidgetOptionProvider provider = (FormWidgetOptionProvider) clazz.newInstance();
                WidgetOption option = WidgetOptionFactory.createByWidgetClass(
                        provider.nameForWidget(),
                        BaseUtils.readIcon(provider.iconPathForWidget()),
                        provider.classForWidget()
                );
                formWidgetOptionsMap.put(provider.classForWidget(), provider.appearanceForWidget());
                if (provider.isContainer()) {
                    formWidgetContainerOptions.add(option);
                } else {
                    formWidgetOptions.add(option);
                }
            } catch (Exception e) {
                FRLogger.getLogger().error("class not found:" + e.getMessage());
            }
        }
    }

    public Map<Class<? extends Widget>, Appearance> getCellWidgetOptionsMap() {
        if (cellWidgetOptionMap == null) {
            return new HashMap<Class<? extends Widget>, Appearance>();
        } else {
            return cellWidgetOptionMap;
        }
    }

    public WidgetOption[] getCellWidgetOptions() {
        if (cellWidgetOptions == null) {
            return new WidgetOption[0];
        } else {
            return cellWidgetOptions.toArray(new WidgetOption[cellWidgetOptions.size()]);
        }
    }

    /**
     * 添加cellWidgetOptionMap
     *
     * @param className 类名
     */
    public void addCellWidgetOption(String className) {
        if (StringUtils.isNotBlank(className)) {
            try {
                Class clazz = Class.forName(className);
                if (cellWidgetOptions == null) {
                    cellWidgetOptions = new ArrayList<WidgetOption>();
                }
                if (cellWidgetOptionMap == null) {
                    cellWidgetOptionMap = new HashMap<Class<? extends Widget>, Appearance>();
                }
                CellWidgetOptionProvider provider = (CellWidgetOptionProvider) clazz.newInstance();
                WidgetOption option = WidgetOptionFactory.createByWidgetClass(
                        provider.nameForWidget(),
                        BaseUtils.readIcon(provider.iconPathForWidget()),
                        provider.classForWidget()
                );
                if (cellWidgetOptions.contains(option)) {
                    return;
                }
                cellWidgetOptionMap.put(provider.classForWidget(), new Appearance(provider.appearanceForWidget(), Appearance.P_MARK + cellWidgetOptionMap.size()));
                cellWidgetOptions.add(option);
            } catch (Exception e) {
                FRLogger.getLogger().error("class not found:" + e.getMessage());
            }
        }
    }


    /**
     * 添加 connectionCreators
     *
     * @param className 类名
     */
    public void addConnection(String className) {
        if (StringUtils.isNotBlank(className)) {
            try {
                Class clazz = Class.forName(className);
                if (connectionCreators == null) {
                    connectionCreators = new ArrayList<NameObjectCreator>();
                }
                ConnectionProvider provider = (ConnectionProvider) clazz.newInstance();
                NameObjectCreator creator = new NameObjectCreator(
                        provider.nameForConnection(),
                        provider.iconPathForConnection(),
                        provider.classForConnection(),
                        provider.appearanceForConnection()
                );
                if (!connectionCreators.contains(creator)) {
                    connectionCreators.add(creator);
                }
            } catch (Exception e) {
                FRLogger.getLogger().error("class not found:" + e.getMessage());
            }
        }
    }

    public NameObjectCreator[] getConnections() {
        if (connectionCreators == null) {
            return new NameObjectCreator[0];
        } else {
            return connectionCreators.toArray(new NameObjectCreator[connectionCreators.size()]);
        }
    }

    public PreviewProvider[] getPreviewProviders() {
        if (previewProviders == null) {
            return new PreviewProvider[0];
        }
        return previewProviders.toArray(new PreviewProvider[previewProviders.size()]);
    }

    /**
     * 添加previewProviders
     *
     * @param className 类名
     */
    public void addPreviewProvider(String className) {
        if (StringUtils.isNotBlank(className)) {
            try {
                Class clazz = GeneralUtils.classForName(className);
                if (previewProviders == null) {
                    previewProviders = new HashSet<PreviewProvider>();
                }
                PreviewProvider provider = (PreviewProvider) clazz.newInstance();
                if (!previewProviders.contains(provider)) {
                    previewProviders.add(provider);
                }
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
    }

    public HighlightProvider[] getHighlightProviders() {
        if (highlightProviders == null) {
            return new HighlightProvider[0];
        }
        return highlightProviders.toArray(new HighlightProvider[highlightProviders.size()]);
    }

    /**
     * 添加 highlightProviders
     *
     * @param className 类名
     */
    public void addTemplateTreeShortCutProvider(String className) {
        if (StringUtils.isNotBlank(className)) {
            if (templateTreeShortCutProviders == null) {
                templateTreeShortCutProviders = new HashSet<ShortCut>();
            }
            try {
                Class clazz = GeneralUtils.classForName(className);
                ShortCut provider = (ShortCut) clazz.newInstance();
                templateTreeShortCutProviders.add(provider);
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
    }

    public ShortCut[] getTemplateTreeShortCutProviders() {
        if (templateTreeShortCutProviders == null) {
            return new ShortCut[0];
        }
        return templateTreeShortCutProviders.toArray(new ShortCut[templateTreeShortCutProviders.size()]);
    }

    /**
     * 添加 highlightProviders
     *
     * @param className 类名
     */
    public void addConditionProvider(String className) {
        if (StringUtils.isNotBlank(className)) {
            if (highlightProviders == null) {
                highlightProviders = new HashSet<HighlightProvider>();
            }
            try {
                Class clazz = GeneralUtils.classForName(className);
                HighlightProvider provider = (HighlightProvider) clazz.newInstance();
                highlightProviders.add(provider);
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
    }

    public Feedback getFeedback() {
        try {
            Class clazz = GeneralUtils.classForName("com.fr.design.feedback.CurrentFeedback");
            if (clazz != null) {
                return (Feedback) clazz.newInstance();
            }
        } catch (Exception e) {
            FRLogger.getLogger().info("no feed back support");
        }
        return Feedback.EMPTY;
    }

    public MenuHandler[] getMenuHandlers(String category) {
        if (menuHandlers == null) {
            return new MenuHandler[0];
        }
        List<MenuHandler> handlers = new ArrayList<MenuHandler>();
        for (MenuHandler handler : menuHandlers) {
            if (ComparatorUtils.equals(category, handler.category())) {
                handlers.add(handler);
            }
        }
        return handlers.toArray(new MenuHandler[handlers.size()]);
    }

    /**
     * 添加menuHandlers
     *
     * @param className 类名
     */
    public void addMenuHandler(String className) {
        if (StringUtils.isNotBlank(className)) {
            if (menuHandlers == null) {
                menuHandlers = new ArrayList<MenuHandler>();
            }
            try {
                Class clazz = GeneralUtils.classForName(className);
                MenuHandler handler = (MenuHandler) clazz.newInstance();
                if (!menuHandlers.contains(handler)) {
                    menuHandlers.add(handler);
                }
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
    }

    public UIFormulaProcessor getUIFormulaProcessor() {
        return uiFormulaProcessor;
    }

    public void setUIFormulaProcessor(String className) {
        if (StringUtils.isNotBlank(className)) {
            try {
                Class clazz = GeneralUtils.classForName(className);
                uiFormulaProcessor = (UIFormulaProcessor) clazz.newInstance();
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
    }

    public PresentKindProvider[] getPresentKindProviders() {
        if (presentKindProviders == null) {
            return new PresentKindProvider[0];
        }
        return presentKindProviders.toArray(new PresentKindProvider[presentKindProviders.size()]);
    }

    /**
     * 添加presentKindProviders
     *
     * @param className 类名
     */
    public void addPresentKindProvider(String className) {
        if (StringUtils.isNotBlank(className)) {
            if (presentKindProviders == null) {
                presentKindProviders = new ArrayList<PresentKindProvider>();
            }
            try {
                Class clazz = GeneralUtils.classForName(className);
                PresentKindProvider provider = (PresentKindProvider) clazz.newInstance();
                presentKindProviders.add(provider);
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
    }

    public ExportToolBarProvider[] getExportToolBarProviders() {
        if (exportToolBarProviders == null) {
            return new ExportToolBarProvider[0];
        }
        return exportToolBarProviders.toArray(new ExportToolBarProvider[exportToolBarProviders.size()]);
    }

    /**
     * 添加exportToolBarProviders
     *
     * @param className 类名
     */
    public void addExportToolBarProvider(String className) {
        if (StringUtils.isNotBlank(className)) {
            if (exportToolBarProviders == null) {
                exportToolBarProviders = new ArrayList<ExportToolBarProvider>();
            }
            try {
                Class clazz = GeneralUtils.classForName(className);
                ExportToolBarProvider provider = (ExportToolBarProvider) clazz.newInstance();
                if (!exportToolBarProviders.contains(provider)) {
                    exportToolBarProviders.add(provider);
                }
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
    }

    public TitlePlaceProcessor getTitlePlaceProcessor() {
        return titlePlaceProcessor;
    }

    public void setTitlePlaceProcessor(String className) {
        if (StringUtils.isNotBlank(className)) {
            try {
                Class clazz = GeneralUtils.classForName(className);
                titlePlaceProcessor = (TitlePlaceProcessor) clazz.newInstance();
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
    }

    public FormElementCaseEditorProcessor getPropertyTableEditor() {
        return formElementCaseEditorProcessor;
    }

    public void setPropertyTableEditor(String className) {
        if (StringUtils.isNotBlank(className)) {
            try {
                Class clazz = GeneralUtils.classForName(className);
                formElementCaseEditorProcessor = (FormElementCaseEditorProcessor) clazz.newInstance();
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
    }

    public IndentationUnitProcessor getIndentationUnitEditor() {
        return indentationUnitProcessor;
    }

    public void setIndentationUnitEditor(String className) {
        if (StringUtils.isNotBlank(className)) {
            try {
                Class clazz = GeneralUtils.classForName(className);
                indentationUnitProcessor = (IndentationUnitProcessor) clazz.newInstance();
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
    }

    public CellAttributeProvider getCelllAttributeProvider(){
        return cellAttributeProvider;
    }
	
    public void setCellAttributeProvider(String className) {
        if (StringUtils.isNotBlank(className)) {
            try {
                Class clazz = GeneralUtils.classForName(className);
                cellAttributeProvider = (CellAttributeProvider) clazz.newInstance();
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
    }


    /**
     * 文件名
     *
     * @return 文件名
     */
    @Override
    public String fileName() {
        return "designer.xml";
    }

    /**
     * 读xml
     *
     * @param reader xml对象
     */
    public void readXML(XMLableReader reader) {
        readXML(reader, null);
    }

    /**
     * 读xml
     *
     * @param reader                   xml对象
     * @param extraDesignInterfaceList 接口列表
     */
    @Override
    public void readXML(XMLableReader reader, List<String> extraDesignInterfaceList) {
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();
            if (extraDesignInterfaceList != null) {
                extraDesignInterfaceList.add(tagName);
            }
            if (tagName.equals(TableDataCreatorProvider.XML_TAG)) {
                setTableDataCreatorProvider(reader.getAttrAsString("class", ""));
            } else if (tagName.equals(TableDataDefineProvider.XML_TAG)) {
                addTableDataNameObjectCreator(reader.getAttrAsString("class", ""));
            } else if (tagName.equals(ServerTableDataDefineProvider.XML_TAG)) {
                addServerTableDataNameObjectCreator(reader.getAttrAsString("class", ""));
            } else if (tagName.equals(ParameterWidgetOptionProvider.XML_TAG)) {
                addParameterWidgetOption(reader.getAttrAsString("class", ""));
            } else if (tagName.equals(FormWidgetOptionProvider.XML_TAG)) {
                addFormWidgetOption(reader.getAttrAsString("class", ""));
            } else if (tagName.equals(ToolbarItemProvider.XML_TAG)) {
                addWebWidgetOption(reader.getAttrAsString("class", ""));
            } else if (tagName.equals(ExportToolBarProvider.XML_TAG)) {
                addExportToolBarProvider(reader.getAttrAsString("class", ""));
            } else if (tagName.equals(CellWidgetOptionProvider.XML_TAG)) {
                addCellWidgetOption(reader.getAttrAsString("class", ""));
            } else if (tagName.equals(ConnectionProvider.XML_TAG)) {
                addConnection(reader.getAttrAsString("class", ""));
            } else if (tagName.equals(PreviewProvider.MARK_STRING)) {
                addPreviewProvider(reader.getAttrAsString("class", ""));
            } else if (tagName.equals(HighlightProvider.MARK_STRING)) {
                addConditionProvider(reader.getAttrAsString("class", ""));
            } else if (tagName.equals(MenuHandler.MARK_STRING)) {
                addMenuHandler(reader.getAttrAsString("class", ""));
            } else if (tagName.equals(UIFormulaProcessor.MARK_STRING)) {
                setUIFormulaProcessor(reader.getAttrAsString("class", ""));
            } else if (tagName.equals(PresentKindProvider.MARK_STRING)) {
                addPresentKindProvider(reader.getAttrAsString("class", ""));
            } else if (tagName.equals(TEMPLATE_TREE_TAG)) {
                addTemplateTreeShortCutProvider(reader.getAttrAsString("class", ""));
            } else if (tagName.equals(SubmitProvider.MARK_STRING)) {
                addSubmitProvider(reader.getAttrAsString("class", ""));
            } else if (tagName.equals(GlobalListenerProvider.XML_TAG)) {
                addGlobalListenerProvider(reader.getAttrAsString("class", ""));
            } else if (tagName.equals(JavaScriptActionProvider.XML_TAG)) {
                addJavaScriptActionProvider(reader.getAttrAsString("class", ""));
            } else if (tagName.equals(TitlePlaceProcessor.MARK_STRING)) {
                setTitlePlaceProcessor(reader.getAttrAsString("class", ""));
            } else if (tagName.equals(FormElementCaseEditorProcessor.MARK_STRING)) {
                setPropertyTableEditor(reader.getAttrAsString("class", ""));
            } else if (tagName.equals(IndentationUnitProcessor.MARK_STRING)) {
                setIndentationUnitEditor(reader.getAttrAsString("class",""));
            } else if (tagName.equals(CellAttributeProvider.MARK_STRING)) {
                setCellAttributeProvider(reader.getAttrAsString("class",""));
            } else if (tagName.equals(HyperlinkProvider.XML_TAG)) {
                addHyperlinkProvider(reader.getAttrAsString("class",""));
            }
        }
    }

    /**
     * 写xml
     *
     * @param writer xml对象
     */
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);
        writer.end();
    }
}
