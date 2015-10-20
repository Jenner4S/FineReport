/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.file;

import com.fr.base.FRContext;
import com.fr.dav.LocalEnv;
import com.fr.design.gui.itree.filetree.TemplateFileTree;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.env.RemoteEnv;
import com.fr.file.FileNodeFILE;
import com.fr.file.filetree.FileNode;
import com.fr.file.filetree.IOFileNodeFilter;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.ProductConstants;
import com.fr.stable.CoreConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.project.ProjectConstants;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

public class TemplateTreePane extends JPanel implements FileOperations {

    public static TemplateTreePane getInstance() {
        return HOLDER.singleton;
    }

    private static class HOLDER {
        private static TemplateTreePane singleton = new TemplateTreePane();
    }

    private TemplateFileTree reportletsTree;
    private FileToolbarStateChangeListener toobarStateChangeListener;

    private TemplateTreePane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setPreferredSize(new Dimension(250, super.getPreferredSize().height));
        JPanel contentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(contentPane, BorderLayout.CENTER);

        reportletsTree = new TemplateFileTree();
        ToolTipManager.sharedInstance().registerComponent(reportletsTree);
        UIScrollPane scrollPane = new UIScrollPane(reportletsTree);
        scrollPane.setBorder(null);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        this.reportletsTree.addMouseListener(mouseFileTreeListener);// lx: add
        // mouse
        // listener
        this.reportletsTree.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    openSelectedReport();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    openSelectedReport();
                }
                if (toobarStateChangeListener != null) {
                    toobarStateChangeListener.stateChange();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    openSelectedReport();
                }
            }
        });
    }

    public TemplateFileTree getTemplateFileTree() {
        return this.reportletsTree;
    }

    /**
     * ˢ��
     */
    public void refreshDockingView() {
        reportletsTree.setFileNodeFilter(new IOFileNodeFilter(FRContext.getCurrentEnv().getSupportedTypes()));
        reportletsTree.refreshEnv(FRContext.getCurrentEnv());
    }

    /*
     * Tree.MouseAdapter
     */
    MouseListener mouseFileTreeListener = new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent evt) {
            if (SwingUtilities.isRightMouseButton(evt)) {
                return;
            } else if (evt.getClickCount() == 2) {
                openSelectedReport();
            }


        }

        public void mouseReleased(MouseEvent e) {
            if (toobarStateChangeListener != null) {
                toobarStateChangeListener.stateChange();
            }
        }
    };


    /**
     * ��ѡ�еı����ļ�
     */
    public void openSelectedReport() {
        String reportPath = reportletsTree.getSelectedTemplatePath();
        final String selectedFilePath = StableUtils.pathJoin(new String[]{ProjectConstants.REPORTLETS_NAME, reportPath});
        DesignerContext.getDesignerFrame().openTemplate(new FileNodeFILE(new FileNode(selectedFilePath, false)));
    }

    /**
     * ���ļ���
     */
    public void openContainerFolder() {
        FileNode fn = TemplateTreePane.this.reportletsTree.getSelectedFileNode();
        LocalEnv localEnv = (LocalEnv) FRContext.getCurrentEnv();
        String filePath = StableUtils.pathJoin(new String[]{localEnv.path, fn.getEnvPath()});
        filePath = filePath.substring(0, filePath.lastIndexOf(CoreConstants.SEPARATOR));
        try {
            Desktop.getDesktop().open(new File(filePath));
        } catch (Exception e) {
            localEnv.openContainerFolder(fn);
        }
    }

    /**
     * ˢ��
     */
    public void refresh() {
        reportletsTree.refresh();
        FRLogger.getLogger().log(Level.INFO, Inter.getLocText(new String[]{"File-tree", "Refresh_Successfully"}) + "!");
    }

    /**
     * ɾ���ļ�
     */
    public void deleteFile() {
        String[] reportPaths = reportletsTree.getSelectedTemplatePaths();
        if (reportPaths.length == 0) {
            return;
        }
        if (JOptionPane.showConfirmDialog(null, Inter.getLocText("Confirm-Delete-File")) != JOptionPane.OK_OPTION) {
            return;
        }
        for (String reportPath : reportPaths) {
            FileNodeFILE nodeFile = new FileNodeFILE(new FileNode(StableUtils.pathJoin(new String[]{ProjectConstants.REPORTLETS_NAME, reportPath}), false));

            if (nodeFile.isLocked()) {
                if (JOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("fileLocked_undeleted"),
                        Inter.getLocText("Error"), JOptionPane.YES_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION) {
                    refreshDockingView();
                }
                break;
            }
            if (nodeFile.exists()) {
                String path = StableUtils.pathJoin(new String[]{nodeFile.getEnvPath(), nodeFile.getPath()});
                FRContext.getCurrentEnv().deleteFile(nodeFile.getPath());
                deleteHistory(path.replaceAll("/", "\\\\"));
            } else {
                JOptionPane.showMessageDialog(this, Inter.getLocText("Warning-Template_Do_Not_Exsit"), ProductConstants.PRODUCT_NAME,
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
        reportletsTree.refresh();
    }


    private void deleteHistory(String fileName) {
        int index = HistoryTemplateListPane.getInstance().contains(fileName);
        int size = HistoryTemplateListPane.getInstance().getHistoryCount();
        if (index == -1) {
            return;
        }
        //����򿪹�����ɾ����ʵʱˢ�¶�tab���
        HistoryTemplateListPane.getInstance().getHistoryList().remove(index);
        int openfileCount = HistoryTemplateListPane.getInstance().getHistoryCount();
        if (openfileCount == 0) {
            DesignerContext.getDesignerFrame().addAndActivateJTemplate();
        }
        MutilTempalteTabPane.getInstance().repaint();
        if (size == index + 1 && index != 0) {
            //���ɾ���������һ��Tab����λ��ǰһ��
            MutilTempalteTabPane.getInstance().setSelectedIndex(index - 1);
        }
        JTemplate selectedfile = MutilTempalteTabPane.getInstance().getSelectedFile();
        if (!HistoryTemplateListPane.getInstance().isCurrentEditingFile(selectedfile.getFullPathName())) {
            //�����ʱ����ϵ�ʵʱˢ�µ�selectedIndex�õ��ĺ���ʷ�Ĳ�һ��
            DesignerContext.getDesignerFrame().activateJTemplate(selectedfile);
        }
        MutilTempalteTabPane.getInstance().repaint();
    }

    /**
     * �����ļ���
     */
    public void lockFile() {
        FileNode fn = reportletsTree.getSelectedFileNode();
        RemoteEnv remoteEnv = (RemoteEnv) FRContext.getCurrentEnv();
        if (fn == null) {
            return;
        }
        try {
            remoteEnv.getLock(new String[]{fn.getEnvPath()});
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
            JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), e.getMessage());
        }
        reportletsTree.refresh();
    }

    /**
     * �ļ�����
     */
    public void unLockFile() {
        FileNode fn = reportletsTree.getSelectedFileNode();
        if (fn == null) {
            return;
        }
        RemoteEnv remoteEnv = (RemoteEnv) FRContext.getCurrentEnv();
        try {
            remoteEnv.releaseLock(new String[]{fn.getEnvPath()});
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
            JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), e.getMessage());
        }
        reportletsTree.refresh();
    }

    public String getSelectedTemplatePath() {
        return reportletsTree.getSelectedTemplatePath();
    }

    public void setToobarStateChangeListener(FileToolbarStateChangeListener toobarStateChangeListener) {
        this.toobarStateChangeListener = toobarStateChangeListener;
    }

    /**
     * �ļ����Ƿ����
     *
     * @param newName ԭ��
     * @param oldName �µ��ļ���
     * @param suffix  ��׺��
     * @return �Ƿ����
     */
    public boolean isNameAlreadyExist(String newName, String oldName, String suffix) {
        boolean isNameAlreadyExist = false;

        TemplateFileTree tt = reportletsTree;
        DefaultMutableTreeNode gen = (DefaultMutableTreeNode) tt.getModel().getRoot();
        ArrayList<String> al = new ArrayList<String>();

        findFiles(gen, al);

        for (int i = 0; i < al.size(); i++) {
            if (ComparatorUtils.equals(al.get(i), newName + suffix)) {
                isNameAlreadyExist = true;
                break;
            }
        }

        if (ComparatorUtils.equals(newName, oldName)) {
            isNameAlreadyExist = false;
        }

        return isNameAlreadyExist;
    }


    private void findFiles(DefaultMutableTreeNode node, ArrayList<String> al) {
        String[] str = new String[node.getChildCount()];
        for (int j = 0; j < node.getChildCount(); j++) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(j);
            if (childNode.getChildCount() > 0) {
                findFiles(childNode, al);
            } else {
                str[j] = node.getChildAt(j).toString();
                if (str[j].contains(".")) {
                    al.add(str[j]);
                }
            }
        }
    }

}
