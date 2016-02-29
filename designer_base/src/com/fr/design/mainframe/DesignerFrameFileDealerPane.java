package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.DesignModelAdapter;
import com.fr.design.DesignerEnvManager;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.data.tabledata.ResponseDataSourceChange;
import com.fr.design.file.*;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenu.UIMenuHighLight;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.gui.itree.filetree.TemplateFileTree;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.file.FILE;
import com.fr.file.FileNodeFILE;
import com.fr.file.filetree.FileNode;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.project.ProjectConstants;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DesignerFrameFileDealerPane extends JPanel implements FileToolbarStateChangeListener, ResponseDataSourceChange {
    private static final String FILE = "file";
    private static DesignerFrameFileDealerPane THIS;

    private CardLayout card;
    private JPanel cardPane;

    private FileOperations selectedOperation;
    private UIToolbar toolBar;

    private OpenReportAction openReportAction = new OpenReportAction();
    private RefreshTreeAction refreshTreeAction = new RefreshTreeAction();
    private OpenFolderAction openFolderAction = new OpenFolderAction();
    private RenameAction renameAction = new RenameAction();
    private DelFileAction delFileAction = new DelFileAction();

    /**
     * 刷新
     */
    public void refresh() {
        selectedOperation.refresh();
    }

    public static final DesignerFrameFileDealerPane getInstance() {
        if (THIS == null) {
            THIS = new DesignerFrameFileDealerPane();
        }
        return THIS;
    }

    private DesignerFrameFileDealerPane() {
        setLayout(new BorderLayout());
        toolBar = ToolBarDef.createJToolBar();
        toolBar.setBorder(BorderFactory.createEmptyBorder(3, 0, 4, 0));
        JPanel tooBarPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        tooBarPane.add(toolBar, BorderLayout.CENTER);
        tooBarPane.add(new UIMenuHighLight(), BorderLayout.SOUTH);

        add(tooBarPane, BorderLayout.NORTH);
        cardPane = new JPanel(card = new CardLayout());
        cardPane.add(TemplateTreePane.getInstance(), FILE);

        selectedOperation = TemplateTreePane.getInstance();
        card.show(cardPane, FILE);

        TemplateTreePane.getInstance().setToobarStateChangeListener(this);

        add(cardPane, BorderLayout.CENTER);
        stateChange();
    }


    public final void setCurrentEditingTemplate(JTemplate<?, ?> jt) {
        DesignModelAdapter.setCurrentModelAdapter(jt == null ? null : jt.getModel());
        fireDSChanged();
        TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter());
        HistoryTemplateListPane.getInstance().setCurrentEditingTemplate(jt);
        //处理自动新建的模板
        MutilTempalteTabPane.getInstance().doWithtemTemplate();
        if (BaseUtils.isAuthorityEditing()) {
            RolesAlreadyEditedPane.getInstance().refreshDockingView();
        }

        jt.setComposite();
        jt.refreshToolArea();
        jt.fireJTemplateOpened();
        jt.requestFocus();
        jt.revert();

        FRContext.getLogger().info("\"" + jt.getEditingFILE().getName() + "\"" + Inter.getLocText("LOG-Has_Been_Openned") + "!");
    }

    /**
     * 刷新菜单
     */
    public void refreshDockingView() {
        ToolBarDef toolbarDef = new ToolBarDef();
        toolbarDef.addShortCut(openReportAction, refreshTreeAction);
        if (FRContext.getCurrentEnv().isSupportLocalFileOperate()) {
            toolbarDef.addShortCut(openFolderAction, renameAction);
        }
        toolbarDef.addShortCut(delFileAction);
        ShortCut[] extraShortCuts = ExtraDesignClassManager.getInstance().getTemplateTreeShortCutProviders();
        for (ShortCut shortCut : extraShortCuts){
            toolbarDef.addShortCut(shortCut);
        }

        toolbarDef.updateToolBar(toolBar);
        refreshActions();
    }


    private void refreshActions() {
        openReportAction.setEnabled(false);
        refreshTreeAction.setEnabled(true);
        openFolderAction.setEnabled(false);
        renameAction.setEnabled(false);
        delFileAction.setEnabled(false);
        this.repaint();
    }

    /**
     * 响应数据集改变
     */
    public void fireDSChanged() {
        fireDSChanged(new HashMap<String, String>());
    }

    /**
     *  响应数据集改变
     * @param map     改变名字的数据集
     */
    public void fireDSChanged(Map<String, String> map) {
        DesignTableDataManager.fireDSChanged(map);
    }


    /*
     * Open Report Action
     */
    private class OpenReportAction extends UpdateAction {

        public OpenReportAction() {
            this.setName(KeySetUtils.OPEN_TEMPLATE.getMenuKeySetName());
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/open.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            selectedOperation.openSelectedReport();
        }

    }

    private class OpenFolderAction extends UpdateAction {

        public OpenFolderAction() {
            this.setName(Inter.getLocText("FR-Designer_Show_in_Containing_Folder"));
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/view_folder.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            selectedOperation.openContainerFolder();
        }
    }

    /*
     * 刷新ReportletsTree
     */
    private class RefreshTreeAction extends UpdateAction {

        public RefreshTreeAction() {
            this.setName(Inter.getLocText("FR-Designer_Refresh"));
            this.setSmallIcon(UIConstants.REFRESH_ICON);
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            selectedOperation.refresh();
            stateChange();
        }
    }

    /*
     * 重命名文件
     */
    private class RenameAction extends UpdateAction {

        public RenameAction() {
            this.setName(Inter.getLocText("FR-Designer_Rename"));
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/data/source/rename.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            new RenameDialog();
            MutilTempalteTabPane.getInstance().repaint();
        }

    }

    /*
     * 删除指定文件
     */
    private class DelFileAction extends UpdateAction {

        public DelFileAction() {
            this.setName(Inter.getLocText("FR-Designer_Remove"));
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/data/source/delete.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            selectedOperation.deleteFile();
        }
    }

    /*
     * 加锁
     */
    private class GetLockAction extends UpdateAction {

        public GetLockAction() {
            this.setName(Inter.getLocText("FR-Designer_Get_Lock"));
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/lock.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            selectedOperation.lockFile();
        }
    }

    /*
     * 解锁
     */
    private class ReleaseLockAction extends UpdateAction {

        public ReleaseLockAction() {
            this.setName(Inter.getLocText("FR-Designer_Release_Lock"));
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/unlock.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            selectedOperation.unLockFile();
        }
    }

    /**
     * 按钮状态改变
     */
    @Override
    public void stateChange() {
        //当前环境为远程环境时
        if (FRContext.getCurrentEnv() != null) {
            if (!FRContext.getCurrentEnv().isSupportLocalFileOperate()) {
                if (selectedOperation.getSelectedTemplatePath() != null) {
                    openReportAction.setEnabled(true);
                } else {
                    openReportAction.setEnabled(false);
                }
                FileNode node = TemplateTreePane.getInstance().getTemplateFileTree().getSelectedFileNode();
                if (selectedOperation.getSelectedTemplatePath() != null){
                    if (node.getLock() != null && !ComparatorUtils.equals(node.getUserID(),node.getLock())){
                        delFileAction.setEnabled(false);
                    } else {
                        delFileAction.setEnabled(true);
                    }
                } else {
                    delFileAction.setEnabled(false);
                }
            } else {
                //当前环境为本地环境时
                if (selectedOperation.getSelectedTemplatePath() != null) {
                    openReportAction.setEnabled(true);
                    renameAction.setEnabled(true);
                    delFileAction.setEnabled(true);
                } else {
                    openReportAction.setEnabled(false);
                    renameAction.setEnabled(false);
                    delFileAction.setEnabled(false);
                }
                openFolderAction.setEnabled(containsFolderNums() + seletedTemplateNums() != 0);
            }
            refreshTreeAction.setEnabled(true);
        }
        if (containsFolderNums() > 0 && (containsFolderNums() + seletedTemplateNums() > 1)) {
            refreshActions();
        } else if (containsFolderNums() == 0 && seletedTemplateNums() > 1) {
            openReportAction.setEnabled(false);
            refreshTreeAction.setEnabled(true);
            openFolderAction.setEnabled(false);
            renameAction.setEnabled(false);
            delFileAction.setEnabled(true);
        }

    }

    /**
     * 是否包含文件夹
     *
     * @return
     */

    private int containsFolderNums() {
        TemplateFileTree fileTree = TemplateTreePane.getInstance().getTemplateFileTree();
        if (fileTree.getSelectionPaths() == null) {
            return 0;
        }

        //选择的包含文件和文件夹的数目
        if (fileTree.getSelectionPaths().length == 0) {
            return 0;
        }
        //所有的num减去模板的num，得到文件夹的num
        return fileTree.getSelectionPaths().length - fileTree.getSelectedTemplatePaths().length;
    }

    /**
     * 是否选择了多个模板
     *
     * @return
     */
    private int seletedTemplateNums() {
        TemplateFileTree fileTree = TemplateTreePane.getInstance().getTemplateFileTree();
        if (fileTree.getSelectionPaths() == null) {
            return 0;
        }

        return fileTree.getSelectedTemplatePaths().length;
    }


    // js: 重命名对话框，模仿Eclipse的重命名，支持快捷键F2，Enter，ESC
    private class RenameDialog {

        private UITextField jt;
        private String userInput;
        private String oldName;
        private UILabel hintsLabel;
        private UIButton confirmButton;
        private JDialog jd;
        private String suffix;

        public RenameDialog() {
            final String reportPath = selectedOperation.getSelectedTemplatePath();
            if (reportPath == null) {
                return;
            }

            final FileNodeFILE nodeFile = new FileNodeFILE(new FileNode(StableUtils.pathJoin(new String[]{ProjectConstants.REPORTLETS_NAME, reportPath}), false));
            final String path = StableUtils.pathJoin(new String[]{nodeFile.getEnvPath(), nodeFile.getPath()});
            oldName = nodeFile.getName();
            suffix = oldName.substring(oldName.lastIndexOf(CoreConstants.DOT), oldName.length());
            oldName = oldName.replaceAll(suffix, "");

            jd = new JDialog();
            jd.setLayout(null);
            jd.setModal(true);
            UILabel newNameLable = new UILabel(Inter.getLocText("FR-Designer_Enter-New-FileName"));
            newNameLable.setBounds(20, 10, 130, 30);
            jt = new UITextField(oldName);
            jt.getDocument().addDocumentListener(getdoDocumentListener());
            jt.selectAll();
            jt.setBounds(130, 15, 150, 20);
            jd.add(newNameLable);
            jd.add(jt);
            addUITextFieldListener(nodeFile, path);

            hintsLabel = new UILabel();
            hintsLabel.setBounds(20, 50, 250, 30);
            hintsLabel.setForeground(Color.RED);
            hintsLabel.setVisible(false);

            confirmButton = new UIButton(Inter.getLocText("FR-Designer_Confirm"));
            confirmButton.setBounds(180, 90, 60, 25);
            confirmButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    confirmClose(nodeFile, path);
                }
            });

            UIButton cancelButton = new UIButton(Inter.getLocText("FR-Designer_Cancel"));
            cancelButton.setBounds(250, 90, 60, 25);
            cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jd.dispose();
                }
            });

            jd.add(cancelButton);
            jd.add(confirmButton);
            jd.add(hintsLabel);
            jd.setSize(340, 180);
            jd.setTitle(Inter.getLocText("FR-Designer_Rename"));
            jd.setResizable(false);
            jd.setAlwaysOnTop(true);
            jd.setIconImage(BaseUtils.readImage("/com/fr/base/images/oem/logo.png"));
            jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            GUICoreUtils.centerWindow(jd);
            jd.setVisible(true);
        }

        public void confirmClose(FileNodeFILE nodeFile, String path) {
            userInput = userInput == null ? oldName : userInput;
            String oldPath = path.replaceAll("/", "\\\\");
            String newPath = path.replace(nodeFile.getName(), userInput + suffix);
            renameTemplateInMemory(nodeFile, userInput + suffix, oldName + suffix);
            DesignerEnvManager.getEnvManager().replaceRecentOpenedFilePath(oldPath, newPath.replaceAll("/", "\\\\"));
            File newFile = new File(newPath);
            new File(path).renameTo(newFile);
            selectedOperation.refresh();
            DesignerContext.getDesignerFrame().setTitle();
            jd.dispose();
        }

        private void renameTemplateInMemory(FILE tplFile, String newName, String oldName) {
            JTemplate<?, ?> dPane = getSpecialTemplateByFILE(tplFile);
            if (dPane == null) {
                return;
            }
            FILE renameFile = dPane.getEditingFILE();
            renameFile.setPath(renameFile.getPath().replace(oldName, newName));
        }

        // 增加enter以及esc快捷键的支持
        public void addUITextFieldListener(final FileNodeFILE nodeFile, final String path) {

            jt.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        jd.dispose();
                    }
                }
            });

            jt.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (confirmButton.isEnabled()) {
                            confirmClose(nodeFile, path);
                        }
                    }
                }
            });

        }

        // UITextField的输入监听
        public DocumentListener getdoDocumentListener() {
            DocumentListener dl = new DocumentListener() {

                public void changedUpdate(DocumentEvent e) {
                    isNameAlreadyExist();
                }

                public void insertUpdate(DocumentEvent e) {
                    isNameAlreadyExist();
                }

                public void removeUpdate(DocumentEvent e) {
                    isNameAlreadyExist();
                }
            };

            return dl;
        }

        private void isNameAlreadyExist() {
            userInput = jt.getText().trim();
            if (selectedOperation.isNameAlreadyExist(userInput, oldName, suffix)) {
                jt.selectAll();
                // 如果文件名已存在，则灰掉确认按钮
                hintsLabel.setText(Inter.getLocText(new String[]{"Utils-File_name", "Already_exists"}, new String[]{userInput}));
                hintsLabel.setVisible(true);
                confirmButton.setEnabled(false);
            } else {
                hintsLabel.setVisible(false);
                confirmButton.setEnabled(true);
            }
        }
    }

    /**
     * @param tplFile
     * @return 内存中的template重命名一下
     */
    private JTemplate<?, ?> getSpecialTemplateByFILE(FILE tplFile) {
        HistoryTemplateListPane historyHandle = HistoryTemplateListPane.getInstance();
        if (ComparatorUtils.equals(historyHandle.getCurrentEditingTemplate().getEditingFILE(), tplFile)) {
            return historyHandle.getCurrentEditingTemplate();
        }
        for (int i = 0; i < historyHandle.getHistoryCount(); i++) {
            if (ComparatorUtils.equals(historyHandle.get(i).getEditingFILE(), tplFile)) {
                return historyHandle.get(i);
            }
        }
        return null;
    }

}