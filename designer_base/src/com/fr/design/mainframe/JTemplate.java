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
 * ������ƺͱ���Ƶı༭����(������༭��IO�ļ�)
 */
public abstract class JTemplate<T extends IOFile, U extends BaseUndoState<?>> extends TargetComponent<T> implements ToolBarMenuDockPlus, JTemplateProvider {
    // TODO ALEX_SEP editingFILE�������һ��Ҫ��?�����Ҫ����,��û�п��ܱ�֤��Ϊnull
    private static final int PREFIX_NUM = 3000;
    private FILE editingFILE = null;
    // alex:��ʼ״̬Ϊsaved,�����������½�ģ��,���Ǵ�ģ��,���δ���κβ���ֱ�ӹر�,����ʾ����
    private boolean saved = true;
    private boolean authoritySaved = true;
    private UndoManager undoMananger;
    private UndoManager authorityUndoManager;
    protected U undoState;
    protected U authorityUndoState = null;
    private static short currentIndex = 0;// �˱������ڶ���½�ģ��ʱ�������ֲ��ظ�
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
     * ��ʼ��Ȩ��ϸ���ȳ���״̬
     */
    public void iniAuthorityUndoState() {
        this.authorityUndoState = createUndoState();
    }

    /**
     * ������ȡ����ʽˢ
     */
    public void doConditionCancelFormat() {
        return;
    }

    public int getMenuState() {
        return DesignState.WORK_SHEET;
    }

    /**
     * ȡ����ʽ
     */
    public void cancelFormat() {
        return;
    }

    //��Ϊ�����tab��0��ʼ�����Ա�Ĭ��Ϊ-1��
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
     * ȥ��ѡ��
     */
    public abstract void removeTemplateSelection();


    public void setSheetCovered(boolean isCovered) {

    }

    /**
     * ��Ȩ�ޱ༭��״̬�£��л����½ǽ�ɫ���Ľ�ɫʱ���ж϶�Ӧ�Ķ�sheet�ǲ�����Ҫcorver
     *
     * @param roles ��ɫ
     */
    public void judgeSheetAuthority(String roles) {

    }

    /**
     * ˢ������
     */
    public abstract void refreshContainer();

    /**
     * ȥ���������ѡ��
     */
    public abstract void removeParameterPaneSelection();

    protected abstract DesignModelAdapter<T, ?> createDesignModel();

    /**
     * �����˵���Preview
     *
     * @return �˵�
     */
    public abstract UIMenuItem[] createMenuItem4Preview();

    /**
     * @return
     */
    public DesignModelAdapter<T, ?> getModel() {
        return designModel;
    }

    /**
     * ���¼����С
     */
    public void doResize() {

    }

    /**
     * �Ƿ񱣴���
     *
     * @return ���򷵻�true
     */
    public boolean isSaved() {
        return BaseUtils.isAuthorityEditing() ? this.authoritySaved : this.saved;
    }

    /**
     * �Ƿ񶼱�����
     *
     * @return ���򷵻�true
     */
    public boolean isALLSaved() {
        return this.saved && this.authoritySaved;
    }


    /**
     * �Ƿ���Ȩ�ޱ༭ʱ��������
     *
     * @return ���򷵻�true
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
     * ���Ȩ��ϸ���ȳ���
     */
    public void cleanAuthorityUndo() {
        authorityUndoManager = null;
        authorityUndoState = null;
        authoritySaved = true;
    }


    /**
     * ���Գ���
     *
     * @return ���򷵻�true
     */
    public boolean canUndo() {
        return this.getUndoManager().canUndo();
    }

    /**
     * ��������
     *
     * @return ���򷵻�true
     */
    public boolean canRedo() {
        return this.getUndoManager().canRedo();
    }

    /**
     * ����
     */
    public void undo() {
        this.getUndoManager().undo();
        fireSuperTargetModified();
    }

    /**
     * ����
     */
    public void redo() {
        this.getUndoManager().redo();

        fireSuperTargetModified();
    }

    /**
     * ģ�����
     */
    public void fireTargetModified() {
        U newState = createUndoState();
        if (newState == null) {
            return;
        }
        //������ڲ�ͬ��ģʽ�²�����
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
     * �������˳�Ȩ�ޱ༭��ʱ�򣬽����в�������Ȩ�ޱ༭��Ч����Ϊһ������������������undoManager��
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
     * ֹͣ�༭, �жϱ������� *
     */
    public void stopEditing() {
    }

    ;


    /**
     * �õ����ڱ༭��FILE
     *
     * @return
     */
    public FILE getEditingFILE() {
        return this.editingFILE;
    }


    /**
     * richer:�����ļ��ĺ�׺��
     *
     * @return ���غ�׺��
     */
    public abstract String suffix();

    /**
     * �Ƿ񱣴�
     *
     * @return ����ģ��
     */
    public boolean saveTemplate() {
        return this.saveTemplate(true);
    }

    /**
     * ����
     *
     * @return ����ɹ�����true
     */
    public boolean saveTemplate2Env() {
        return this.saveTemplate(false);
    }

    /**
     * ���
     *
     * @return ����ɹ�����true
     */
    public boolean saveAsTemplate() {
        return this.saveAsTemplate(true);
    }

    /**
     * ���
     *
     * @return ����ɹ�����true
     */
    public boolean saveAsTemplate2Env() {
        return this.saveAsTemplate(false);
    }

    /**
     * WebԤ����ʱ����Ҫ���ص������������л��������·��(C��D�̵�) isShowLoc = false
     *
     * @param isShowLoc �Ƿ񱾵�
     * @return ����ɹ�����true
     */
    public boolean saveTemplate(boolean isShowLoc) {
        FILE editingFILE = this.getEditingFILE();
        // carl:editingFILEû�У���Ȼ������,��Ȼ�������������
        if (editingFILE == null) {
            return false;
        }
        //���һ�������Ƿ�ɹ�
        try {
            if (FRContext.getCurrentEnv() != null && !FRContext.getCurrentEnv().testServerConnectionWithOutShowMessagePane()) {
                //���Ӳ��ɹ�����ʾ
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(),
                        Inter.getLocText(new String[]{"server_disconnected", "template_unsaved"}, new String[]{",", "!"})
                        , Inter.getLocText("FR-Designer_Error"), JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }


        // ���һ��editingFILE�ǲ����Ѵ��ڵ��ļ�,�������������saveAs
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
        // alex:�����SaveAs�Ļ���Ҫ���û���ѡ��·����
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
        //���������
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
	 * ��ģ�����Ϊ���Է����ȥ�Ļ������������ݼ�ģ��
	 * 
	 * @return �Ƿ����ɹ�
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
                //�������������������
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
        // ���ڻ�ȡ���ģ����ļ�����������Ѱ���"WorkBook1.cpt, WorkBook12.cpt, WorkBook177.cpt"
        // ��ô�½����ļ�����������Ϊ"WorkBook178.cpt",��ȡ�����+1
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
     * �ļ���4���˵�
     *
     * @return ���ز˵�
     */
    public ShortCut[] shortcut4FileMenu() {
        if (BaseUtils.isAuthorityEditing()) {
            return new ShortCut[]{new SaveTemplateAction(this), new UndoAction(this), new RedoAction(this)};
        } else {
            return new ShortCut[]{new SaveTemplateAction(this), new SaveAsTemplateAction(this), new UndoAction(this), new RedoAction(this)};
        }

    }

    /**
     * Ŀ��˵�
     *
     * @return �˵�
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
     * ģ��˵�
     *
     * @return ���ز˵�
     */
    public abstract ShortCut[] shortcut4TemplateMenu();

    /**
     * Ȩ��ϸ����ģ��˵�
     *
     * @return �˵�
     */
    public abstract ShortCut[] shortCuts4Authority();

    // /////////////////////////////JTemplateActionListener//////////////////////////////////

    /**
     * ����ģ��Listener
     *
     * @param l ģ��Listener
     */
    public void addJTemplateActionListener(JTemplateActionListener l) {
        this.listenerList.add(JTemplateActionListener.class, l);
    }

    /**
     * �Ƴ�ģ��Listener
     *
     * @param l ģ��Listener
     */
    public void removeJTemplateActionListener(JTemplateActionListener l) {
        this.listenerList.remove(JTemplateActionListener.class, l);
    }

    /**
     * ����ģ��ر�
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
     * ����ģ�屣��
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
     * ����ģ���
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
     * ģ���л�ʱ���ָ�ԭ����״̬
     */
    public void revert() {

    }
    
    private int getVersionCompare(String versionString){
        if (StringUtils.isBlank(versionString)) {
            return 0;
        }
        //8.0.0���Դ�8.0.1��ģ��.
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
     * �ж��Ƿ����°������
     * @return    �Ƿ���true
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
     * �Ƿ��ǾͰ汾�����
     *
     * @return �Ǿͷ���true
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
     * ˢ�¹�������
     */
    public void refreshToolArea() {

    }


    /**
     * �Ƿ��ǹ�����
     *
     * @return ���򷵻�true
     */
    public abstract boolean isJWorkBook();
    
    /**
     * ���ص�ǰ֧�ֵĳ�������pane
     * @return �������ӽ���
     */
    public HyperlinkGroupPane getHyperLinkPane() {
    	return new HyperlinkGroupPane();
    }

    /**
     * �Ƿ���ͼ��
     *
     * @return Ĭ�ϲ���
     */
    public boolean isChartBook(){
        return false;
    }

    public abstract void setAuthorityMode(boolean isUpMode);

    /**
     * �Ƿ��ǲ�������ģʽ
     *
     * @return ����
     */
    public boolean isUpMode() {
        return false;
    }

    /**
     * ����Ԥ����ʽ
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
     * �õ�Ԥ���Ĵ�ͼ��
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
     * ���������
     */
    public void requestGridFocus() {

    }
    
    /**
	 * ��������sql�ύ��pane
	 * 
	 * @return ����sql�ύ��pane
	 * 
	 *
	 * @date 2014-10-14-����7:39:27
	 */
    public DBManipulationPane createDBManipulationPane(){
    	return new DBManipulationPane();
    }
    
    /**
     * �����ؼ��¼�������sql�ύ��pane
     * 
     * @return ����sql�ύ��pane
     * 
     *
     * @date 2014-10-14-����7:39:27
     */
    public DBManipulationPane createDBManipulationPaneInWidget(){
    	return new DBManipulationInWidgetEventPane();
    }

    /**
     * ȡСͼ�꣬��Ҫ���ڶ�TAB��ǩ��
     * @return ͼ��
     */
    public abstract Icon getIcon();

    /**
     * �����˵���
     * @return �˵���
     */
    public ShortCut[] shortcut4ExportMenu() {
        return new ShortCut[0];
    }

    /**
     * ����JS����
     */
    public void copyJS(){}

    /**
     * ϵ�з��Ķ�
     */
    public void styleChange(){

    }
    
    /**
	 * ��������ģ��İ�ť, Ŀǰֻ��jworkbookʵ����
	 * 
	 * @return ����ģ�尴ť
	 * 
	 */
    public UIButton[] createShareButton(){
    	return new UIButton[0]; 
    }

    /**
     * ��
     * @param provider Ԥ��ģʽ
     */
    public void previewMenuActionPerformed(PreviewProvider provider) {

    }

    /**
     * ֧�ֵ�Ԥ��ģʽ
     * @return Ԥ��ģʽ
     */
    public PreviewProvider[] supportPreview() {
        return new PreviewProvider[0];
    }

    /**
     * Ԥ��ģʽת��
     * @param typeCode ����
     * @return Ԥ��ģʽ
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
