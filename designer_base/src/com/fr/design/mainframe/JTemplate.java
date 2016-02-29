package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.ConfigManager;
import com.fr.base.FRContext;
import com.fr.base.Parameter;
import com.fr.base.io.IOFile;
import com.fr.design.DesignModelAdapter;
import com.fr.design.DesignState;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.TableDataSourceAction;
import com.fr.design.actions.edit.RedoAction;
import com.fr.design.actions.edit.UndoAction;
import com.fr.design.actions.file.SaveAsTemplateAction;
import com.fr.design.actions.file.SaveTemplateAction;
import com.fr.design.constants.UIConstants;
import com.fr.design.designer.TargetComponent;
import com.fr.design.dialog.InformationWarnPane;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.file.TemplateTreePane;
import com.fr.design.fun.MenuHandler;
import com.fr.design.fun.PreviewProvider;
import com.fr.design.gui.frpane.HyperlinkGroupPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.gui.itree.filetree.TemplateFileTree;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.NameSeparator;
import com.fr.design.menu.ShortCut;
import com.fr.design.preview.PagePreview;
import com.fr.design.write.submit.DBManipulationInWidgetEventPane;
import com.fr.design.write.submit.DBManipulationPane;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.file.FileNodeFILE;
import com.fr.file.MemFILE;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.ProductConstants;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * 报表设计和表单设计的编辑区域(设计器编辑的IO文件)
 */
public abstract class JTemplate<T extends IOFile, U extends BaseUndoState<?>> extends TargetComponent<T> implements ToolBarMenuDockPlus, JTemplateProvider {
    // TODO ALEX_SEP editingFILE这个属性一定要吗?如果非要不可,有没有可能保证不为null
    private static final int PREFIX_NUM = 3000;
    private FILE editingFILE = null;
    // alex:初始状态为saved,这样不管是新建模板,还是打开模板,如果未做任何操作直接关闭,不提示保存
    private boolean saved = true;
    private boolean authoritySaved = true;
    private UndoManager undoMananger;
    private UndoManager authorityUndoManager;
    protected U undoState;
    protected U authorityUndoState = null;
    private static short currentIndex = 0;// 此变量用于多次新建模板时，让名字不重复
    private DesignModelAdapter<T, ?> designModel;
    private PreviewProvider previewType;

    public JTemplate(T t, String defaultFileName) {
        this(t, new MemFILE(newTemplateNameByIndex(defaultFileName)));
    }

    public JTemplate(T t, FILE file) {
        super(t);
        this.previewType = parserPreviewProvider(t.getPreviewType());
        this.editingFILE = file;
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder());
        this.add(createCenterPane(), BorderLayout.CENTER);
        this.undoState = createUndoState();
        designModel = createDesignModel();
    }

    public U getUndoState() {
        return undoState;
    }

    /**
     * 初始化权限细粒度撤销状态
     */
    public void iniAuthorityUndoState() {
        this.authorityUndoState = createUndoState();
    }

    /**
     * 有条件取消格式刷
     */
    public void doConditionCancelFormat() {
        return;
    }

    public int getMenuState() {
        return DesignState.WORK_SHEET;
    }

    /**
     * 取消格式
     */
    public void cancelFormat() {
        return;
    }

    //因为报表的tab从0开始，所以表单默认为-1吧
    public int getEditingReportIndex() {
        return -1;
    }

    public String getFullPathName() {
        String editingFileName = getEditingFILE().getPath();
        if (editingFileName.startsWith(ProjectConstants.REPORTLETS_NAME)) {
            editingFileName = ((FileNodeFILE) getEditingFILE()).getEnvPath() + File.separator + editingFileName;
        }
        return editingFileName.replaceAll("/", "\\\\");
    }

    protected abstract JComponent createCenterPane();

    /**
     * 去除选择
     */
    public abstract void removeTemplateSelection();


    public void setSheetCovered(boolean isCovered) {

    }

    /**
     * 在权限编辑的状态下，切换左下角角色树的角色时，判断对应的额sheet是不是需要corver
     *
     * @param roles 角色
     */
    public void judgeSheetAuthority(String roles) {

    }

    /**
     * 刷新容器
     */
    public abstract void refreshContainer();

    /**
     * 去除参数面板选择
     */
    public abstract void removeParameterPaneSelection();

    protected abstract DesignModelAdapter<T, ?> createDesignModel();

    /**
     * 创建菜单项Preview
     *
     * @return 菜单
     */
    public abstract UIMenuItem[] createMenuItem4Preview();

    /**
     * @return
     */
    public DesignModelAdapter<T, ?> getModel() {
        return designModel;
    }

    /**
     * 重新计算大小
     */
    public void doResize() {

    }

    /**
     * 是否保存了
     *
     * @return 是则返回true
     */
    public boolean isSaved() {
        return BaseUtils.isAuthorityEditing() ? this.authoritySaved : this.saved;
    }

    /**
     * 是否都保存了
     *
     * @return 是则返回true
     */
    public boolean isALLSaved() {
        return this.saved && this.authoritySaved;
    }


    /**
     * 是否在权限编辑时做过操作
     *
     * @return 是则返回true
     */
    public boolean isDoSomethingInAuthority() {
        return authorityUndoManager != null && authorityUndoManager.canUndo();
    }

    public void setSaved(boolean isSaved) {
        if (BaseUtils.isAuthorityEditing()) {
            authoritySaved = isSaved;
        } else {
            saved = isSaved;
        }
    }

    /**
     * @return
     */
    public UndoManager getUndoManager() {
        if (BaseUtils.isAuthorityEditing()) {
            if (this.authorityUndoManager == null) {
                this.authorityUndoManager = new UndoManager();
                int limit = DesignerEnvManager.getEnvManager().getUndoLimit();
                limit = (limit <= 0) ? -1 : limit;

                this.authorityUndoManager.setLimit(limit);
            }
            return authorityUndoManager;
        }
        if (this.undoMananger == null) {
            this.undoMananger = new UndoManager();
            int limit = DesignerEnvManager.getEnvManager().getUndoLimit();
            limit = (limit <= 0) ? -1 : limit;

            this.undoMananger.setLimit(limit);
        }
        return this.undoMananger;
    }

    /**
     * 清除权限细粒度撤销
     */
    public void cleanAuthorityUndo() {
        authorityUndoManager = null;
        authorityUndoState = null;
        authoritySaved = true;
    }


    /**
     * 可以撤销
     *
     * @return 是则返回true
     */
    public boolean canUndo() {
        return this.getUndoManager().canUndo();
    }

    /**
     * 可以重做
     *
     * @return 是则返回true
     */
    public boolean canRedo() {
        return this.getUndoManager().canRedo();
    }

    /**
     * 撤销
     */
    public void undo() {
        this.getUndoManager().undo();
        fireSuperTargetModified();
    }

    /**
     * 重做
     */
    public void redo() {
        this.getUndoManager().redo();

        fireSuperTargetModified();
    }

    /**
     * 模板更新
     */
    public void fireTargetModified() {
        U newState = createUndoState();
        if (newState == null) {
            return;
        }
        //如果是在不同的模式下产生的
        if (BaseUtils.isAuthorityEditing()) {
            this.getUndoManager().addEdit(new UndoStateEdit(authorityUndoState, newState));
            authorityUndoState = newState;
        } else {
            this.getUndoManager().addEdit(new UndoStateEdit(undoState, newState));
            undoState = newState;
        }
        fireSuperTargetModified();
    }

    /**
     * 用于在退出权限编辑的时候，将所有操作的有权限编辑的效果作为一个动作放入正常报表undoManager中
     */
    public void fireAuthorityStateToNomal() {
        U newState = createUndoState();
        if (newState == null) {
            return;
        }
        newState.setAuthorityType(BaseUndoState.AUTHORITY_STATE);
        this.getUndoManager().addEdit(new UndoStateEdit(undoState, newState));
        undoState = newState;
        fireSuperTargetModified();
    }

    protected boolean accept(Object o){
    	return true;
    }

    private void fireSuperTargetModified() {
        if (BaseUtils.isAuthorityEditing()) {
            this.authoritySaved = false;
        } else {
            this.saved = false;
        }
        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().setSaved(false);
        super.fireTargetModified();
    }

    protected abstract U createUndoState();

    protected abstract void applyUndoState(U u);

    /**
     * 停止编辑, 判断保存属性 *
     */
    public void stopEditing() {
    }

    ;


    /**
     * 得到正在编辑的FILE
     *
     * @return
     */
    public FILE getEditingFILE() {
        return this.editingFILE;
    }


    /**
     * richer:保存文件的后缀名
     *
     * @return 返回后缀名
     */
    public abstract String suffix();

    /**
     * 是否保存
     *
     * @return 保存模板
     */
    public boolean saveTemplate() {
        return this.saveTemplate(true);
    }

    /**
     * 保存
     *
     * @return 保存成功返回true
     */
    public boolean saveTemplate2Env() {
        return this.saveTemplate(false);
    }

    /**
     * 另存
     *
     * @return 保存成功返回true
     */
    public boolean saveAsTemplate() {
        return this.saveAsTemplate(true);
    }

    /**
     * 另存
     *
     * @return 保存成功返回true
     */
    public boolean saveAsTemplate2Env() {
        return this.saveAsTemplate(false);
    }

    /**
     * Web预览的时候需要隐藏掉除“报表运行环境”外的路径(C盘D盘等) isShowLoc = false
     *
     * @param isShowLoc 是否本地
     * @return 保存成功返回true
     */
    public boolean saveTemplate(boolean isShowLoc) {
        FILE editingFILE = this.getEditingFILE();
        // carl:editingFILE没有，当然不存了,虽然不会有这种情况
        if (editingFILE == null) {
            return false;
        }
        //检查一下连接是否成功
        try {
            if (FRContext.getCurrentEnv() != null && !FRContext.getCurrentEnv().testServerConnectionWithOutShowMessagePane()) {
                //连接不成功，提示
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(),
                        Inter.getLocText(new String[]{"server_disconnected", "template_unsaved"}, new String[]{",", "!"})
                        , Inter.getLocText("FR-Designer_Error"), JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }


        // 检查一下editingFILE是不是已存在的文件,如果不存在则用saveAs
        if (!editingFILE.exists()) {
            return saveAsTemplate(isShowLoc);
        }
        if (!FRContext.getCurrentEnv().hasFileFolderAllow(this.getEditingFILE().getPath()) ) {
            JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("FR-Designer_No-Privilege") + "!", Inter.getLocText("FR-Designer_Message"), JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return this.saveFile();
    }
    
    private boolean isCancelOperation(int operation){
    	return operation == FILEChooserPane.CANCEL_OPTION ||
    	       operation == FILEChooserPane.JOPTIONPANE_CANCEL_OPTION;
    }
    
    private boolean isOkOperation(int operation){
    	return operation == FILEChooserPane.JOPTIONPANE_OK_OPTION ||
    		   operation == FILEChooserPane.OK_OPTION;
    }

    private boolean saveAsTemplate(boolean isShowLoc) {
        FILE editingFILE = this.getEditingFILE();
        if (editingFILE == null) {
            return false;
        }
        String oldName = this.getFullPathName();
        // alex:如果是SaveAs的话需要让用户来选择路径了
        FILEChooserPane fileChooser = getFILEChooserPane(isShowLoc);
        fileChooser.setFileNameTextField(editingFILE.getName(), this.suffix());
        int chooseResult = fileChooser.showSaveDialog(DesignerContext.getDesignerFrame(), this.suffix());

        if (isCancelOperation(chooseResult)) {
            fileChooser = null;
            return false;
        } 
        
        if (isOkOperation(chooseResult)) {
            if (!FRContext.getCurrentEnv().hasFileFolderAllow(fileChooser.getSelectedFILE().getPath()) ) {
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("FR-Designer_No-Privilege") + "!", Inter.getLocText("FR-Designer_Message"), JOptionPane.WARNING_MESSAGE);
                return false;
            }
            editingFILE = fileChooser.getSelectedFILE();
            mkNewFile(editingFILE);
            fileChooser = null;
        }
        
        return saveNewFile(editingFILE, oldName);
    }
    
    protected boolean saveNewFile(FILE editingFILE, String oldName){
        this.editingFILE = editingFILE;

        boolean result = this.saveFile();
        if (result) {
            DesignerFrameFileDealerPane.getInstance().refresh();
        }
        //更换最近打开
        DesignerEnvManager.getEnvManager().replaceRecentOpenedFilePath(oldName, this.getFullPathName());
        return result;
    }
    
    protected void mkNewFile(FILE file){
        try {
        	file.mkfile();
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
    }
    
    /**
	 * 将模板另存为可以分享出去的混淆后内置数据集模板
	 * 
	 * @return 是否另存成功
	 * 
	 */
    public boolean saveShareFile(){
    	return true;
    }

    protected FILEChooserPane getFILEChooserPane(boolean isShowLoc){
        return new FILEChooserPane(true, isShowLoc);
    }

    protected boolean saveFile() {
        FILE editingFILE = this.getEditingFILE();

        if (editingFILE == null || editingFILE instanceof MemFILE) {
            return false;
        }
        try {
            if (!this.getTarget().export(editingFILE.asOutputStream())) {
                return false;
            }

            if (BaseUtils.isAuthorityEditing()) {
                //触发保存服务器工具栏
                try {
                    FRContext.getCurrentEnv().writeResource(ConfigManager.getProviderInstance());
                } catch (Exception e1) {
                    FRContext.getLogger().error(e1.getMessage());
                }
            }

        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
            JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        this.editingFILE = editingFILE;
        this.saved = true;
        this.authoritySaved = true;
        DesignerContext.getDesignerFrame().setTitle();

        this.fireJTemplateSaved();
        return true;
    }

    private static String newTemplateNameByIndex(String prefix) {
        // 用于获取左侧模板的文件名，如左侧已包含"WorkBook1.cpt, WorkBook12.cpt, WorkBook177.cpt"
        // 那么新建的文件名将被命名为"WorkBook178.cpt",即取最大数+1
        TemplateFileTree tt = TemplateTreePane.getInstance().getTemplateFileTree();
        DefaultMutableTreeNode gen = (DefaultMutableTreeNode) tt.getModel().getRoot();
        String[] str = new String[gen.getChildCount()];
        ArrayList<String> al = new ArrayList<String>();
        for (int j = 0; j < gen.getChildCount(); j++) {
            str[j] = gen.getChildAt(j).toString();
            if (str[j].contains(prefix) && str[j].contains(".")) {
                for (int i = 0; i < PREFIX_NUM; i++) {
                    if (ComparatorUtils.equals(str[j].split("[.]")[0], (prefix + i))) {
                        al.add(str[j]);
                    }

                }
            }
        }

        int[] reportNum = new int[al.size()];
        for (int i = 0; i < al.size(); i++) {
            Pattern pattern = Pattern.compile("[" + prefix + ".]+");
            String[] strs = pattern.split(al.get(i).toString());
            reportNum[i] = Integer.parseInt(strs[1]);
        }

        Arrays.sort(reportNum);
        int idx = reportNum.length > 0 ? reportNum[reportNum.length - 1] + 1 : 1;
        idx = idx + currentIndex;
        currentIndex++;
        return prefix + idx;
    }

    // /////////////////////////////toolbarMenuDock//////////////////////////////////

    /**
     * 文件的4个菜单
     *
     * @return 返回菜单
     */
    public ShortCut[] shortcut4FileMenu() {
        if (BaseUtils.isAuthorityEditing()) {
            return new ShortCut[]{new SaveTemplateAction(this), new UndoAction(this), new RedoAction(this)};
        } else {
            return new ShortCut[]{new SaveTemplateAction(this), new SaveAsTemplateAction(this), new UndoAction(this), new RedoAction(this)};
        }

    }

    /**
     * 目标菜单
     *
     * @return 菜单
     */
    public MenuDef[] menus4Target() {
        MenuDef tplMenu = new MenuDef(Inter.getLocText("FR-Designer_M-Template"), 'T');
        tplMenu.setAnchor(MenuHandler.TEMPLATE);
        if (!BaseUtils.isAuthorityEditing()) {
            tplMenu.addShortCut(new NameSeparator(Inter.getLocText("FR-Designer_WorkBook")));
            tplMenu.addShortCut(new TableDataSourceAction(this));
            tplMenu.addShortCut(shortcut4TemplateMenu());
        }
        tplMenu.addShortCut(shortCuts4Authority());

        return new MenuDef[]{tplMenu};
    }

    /**
     * 模板菜单
     *
     * @return 返回菜单
     */
    public abstract ShortCut[] shortcut4TemplateMenu();

    /**
     * 权限细粒度模板菜单
     *
     * @return 菜单
     */
    public abstract ShortCut[] shortCuts4Authority();

    // /////////////////////////////JTemplateActionListener//////////////////////////////////

    /**
     * 增加模板Listener
     *
     * @param l 模板Listener
     */
    public void addJTemplateActionListener(JTemplateActionListener l) {
        this.listenerList.add(JTemplateActionListener.class, l);
    }

    /**
     * 移除模板Listener
     *
     * @param l 模板Listener
     */
    public void removeJTemplateActionListener(JTemplateActionListener l) {
        this.listenerList.remove(JTemplateActionListener.class, l);
    }

    /**
     * 触发模板关闭
     */
    public void fireJTemplateClosed() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == JTemplateActionListener.class) {
                ((JTemplateActionListener) listeners[i + 1]).templateClosed(this);
            }
        }

        this.repaint(30);
    }

    /**
     * 触发模板保存
     */
    public void fireJTemplateSaved() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == JTemplateActionListener.class) {
                ((JTemplateActionListener) listeners[i + 1]).templateSaved(this);
            }
        }

        this.repaint(30);
    }

    /**
     * 触发模板打开
     */
    public void fireJTemplateOpened() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == JTemplateActionListener.class) {
                ((JTemplateActionListener) listeners[i + 1]).templateOpened(this);
            }
        }

        this.repaint(30);
    }

    /**
     * 模板切换时，恢复原来的状态
     */
    public void revert() {

    }
    
    private int getVersionCompare(String versionString){
        if (StringUtils.isBlank(versionString)) {
            return 0;
        }
        //8.0.0可以打开8.0.1的模板.
        int len = ProductConstants.DESIGNER_VERSION.length() - 1;
        return ComparatorUtils.compare(versionString.substring(0, len), ProductConstants.DESIGNER_VERSION.substring(0, len));

    }
    
    private int getVersionCompareHBB(String versionString){
        if (StringUtils.isBlank(versionString)) {
            return 0;
        }
        return ComparatorUtils.compare(versionString, "HBB");

    }
    
    private boolean isHigherThanCurrent(String versionString) {
        return getVersionCompare(versionString) > 0;
    }
    
    private boolean isLowerThanCurrent(String versionString) {
    	return getVersionCompare(versionString) < 0;
    }
    
    private boolean isLowerThanHBB(String versionString) {
    	return getVersionCompareHBB(versionString) < 0;
    }

    /**
     * 判断是否是新版设计器
     * @return    是返回true
     */
    public boolean isNewDesigner() {
        String xmlDesignerVersion = getTarget().getXMLDesignerVersion();
        if (isLowerThanHBB(xmlDesignerVersion)) {
        	String info = Inter.getLocText("FR-Designer_open-new-form-tip");
            String moreInfo = Inter.getLocText("FR-Designer_Server-version-tip-moreInfo");
            new InformationWarnPane(info, moreInfo, Inter.getLocText("FR-Designer_Tooltips")).show();
            return true;
        }
        return false;
    }

    /**
     * 是否是就版本设计器
     *
     * @return 是就返回true
     */
    public boolean isOldDesigner() {
        String xmlDesignerVersion = getTarget().getXMLDesignerVersion();
        if (isHigherThanCurrent(xmlDesignerVersion)) {
            String[] message = new String[]{"Server-version-info", "Above"};
            String[] sign = new String[]{StringUtils.parseVersion(xmlDesignerVersion)};
            String infor = Inter.getLocText(message, sign);
            String moreInfo = Inter.getLocText("FR-Designer_Server-version-tip-moreInfo");
            new InformationWarnPane(infor, moreInfo, Inter.getLocText("FR-Designer_Tooltips")).show();
            return true;
        }
        return false;
    }

    /**
     *
     */
    public void setComposite() {

    }

    /**
     * @return
     */
    public PreviewProvider getPreviewType() {
        return previewType;
    }

    /**
     * 刷新工具区域
     */
    public void refreshToolArea() {

    }


    /**
     * 是否是工作薄
     *
     * @return 是则返回true
     */
    public abstract boolean isJWorkBook();
    
    /**
     * 返回当前支持的超链界面pane
     * @return 超链连接界面
     */
    public HyperlinkGroupPane getHyperLinkPane() {
    	return new HyperlinkGroupPane();
    }

    /**
     * 是否是图表
     *
     * @return 默认不是
     */
    public boolean isChartBook(){
        return false;
    }

    public abstract void setAuthorityMode(boolean isUpMode);

    /**
     * 是否是参数面板的模式
     *
     * @return 不是
     */
    public boolean isUpMode() {
        return false;
    }

    /**
     * 设置预览方式
     *
     * @param previewType
     */
    public void setPreviewType(PreviewProvider previewType) {
        if (this.previewType == previewType) {
            return;
        }
        this.previewType = previewType;
        getTarget().setPreviewType(previewType.previewTypeCode());
    }

    /**
     * 得到预览的大图标
     *
     * @return
     */
    public Icon getPreviewLargeIcon() {
        return UIConstants.RUN_BIG_ICON;
    }

    public Parameter[] getParameters() {
        return new Parameter[0];
    }

    /**
     * 请求表单焦点
     */
    public void requestGridFocus() {

    }
    
    /**
	 * 创建内置sql提交的pane
	 * 
	 * @return 内置sql提交的pane
	 * 
	 *
	 * @date 2014-10-14-下午7:39:27
	 */
    public DBManipulationPane createDBManipulationPane(){
    	return new DBManipulationPane();
    }
    
    /**
     * 创建控件事件里内置sql提交的pane
     * 
     * @return 内置sql提交的pane
     * 
     *
     * @date 2014-10-14-下午7:39:27
     */
    public DBManipulationPane createDBManipulationPaneInWidget(){
    	return new DBManipulationInWidgetEventPane();
    }

    /**
     * 取小图标，主要用于多TAB标签栏
     * @return 图表
     */
    public abstract Icon getIcon();

    /**
     * 导出菜单项
     * @return 菜单项
     */
    public ShortCut[] shortcut4ExportMenu() {
        return new ShortCut[0];
    }

    /**
     * 复制JS代码
     */
    public void copyJS(){}

    /**
     * 系列风格改动
     */
    public void styleChange(){

    }
    
    /**
	 * 创建分享模板的按钮, 目前只有jworkbook实现了
	 * 
	 * @return 分享模板按钮
	 * 
	 */
    public UIButton[] createShareButton(){
    	return new UIButton[0]; 
    }

    /**
     * 略
     * @param provider 预览模式
     */
    public void previewMenuActionPerformed(PreviewProvider provider) {

    }

    /**
     * 支持的预览模式
     * @return 预览模式
     */
    public PreviewProvider[] supportPreview() {
        return new PreviewProvider[0];
    }

    /**
     * 预览模式转换
     * @param typeCode 类型
     * @return 预览模式
     */
    public PreviewProvider parserPreviewProvider(int typeCode) {
        PreviewProvider pp = null;
        PreviewProvider[] previewProviders = supportPreview();
        for (PreviewProvider p : previewProviders) {
            if (p.previewTypeCode() == typeCode) {
                pp = p;
            }
        }
        if (pp == null) {
            return new PagePreview();
        }
        return pp;
    }
}