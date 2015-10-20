package com.fr.design.file;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.logging.Level;

import javax.swing.*;

import com.fr.design.constants.UIConstants;
import com.fr.design.DesignerEnvManager;
import com.fr.design.gui.ilable.UILabel;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.fr.base.FRContext;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.dav.LocalEnv;
import com.fr.design.DesignModelAdapter;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilist.UIList;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.file.filetree.FileNode;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.stable.project.ProjectConstants;
import com.fr.design.utils.gui.GUIPaintUtils;

public class HistoryTemplateListPane extends JPanel implements FileOperations {
    private static final int LIST_BORDER = 4;
    private List<JTemplate<?, ?>> historyList;
    private JTemplate<?, ?> editingTemplate;
    private FileToolbarStateChangeListener toobarStateChangeListener;

    private static HistoryTemplateListPane THIS;

    private UIList list;

    public static final HistoryTemplateListPane getInstance() {
        if (THIS == null) {
            THIS = new HistoryTemplateListPane();
        }
        return THIS;
    }

    public HistoryTemplateListPane() {
        setLayout(new BorderLayout());
        historyList = new ArrayList<JTemplate<?, ?>>();
        list = new UIList(new HistoryListDataMode()) {
            public int locationToIndex(Point location) {
                int rowCount = getModel().getSize();
                int height = getPreferredSize().height - 2 * LIST_BORDER;
                int rowHeight = height / rowCount;
                int index = (location.y - LIST_BORDER) / rowHeight;
                if (location.y < LIST_BORDER || index > rowCount - 1) {
                    return -1;
                } else {
                    return index;
                }
            }
        };
        ToolTipManager.sharedInstance().registerComponent(list);
        list.setBackground(UIConstants.NORMAL_BACKGROUND);
        list.setCellRenderer(new HistoryListCellRender());
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() < 2) {
                    return;
                }
                openSelectedReport();
            }
        });


        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (toobarStateChangeListener != null) {
                    toobarStateChangeListener.stateChange();
                }

            }
        });
        list.setBorder(BorderFactory.createEmptyBorder(LIST_BORDER, LIST_BORDER, LIST_BORDER, LIST_BORDER));
        UIScrollPane scrollPane = new UIScrollPane(list);
        scrollPane.setBorder(null);

        this.add(scrollPane, BorderLayout.CENTER);

    }

    /**
     *�ر�ѡ����ļ�
     * @param selected ѡ���
     */
    public void closeSelectedReport(JTemplate<?, ?> selected) {
        if (contains(selected) == -1) {
            return;
        }
        selected.fireJTemplateClosed();
        selected.stopEditing();
        try {
            historyList.remove(contains(selected));
            selected.getEditingFILE().closeTemplate();
            FRLogger.getLogger().log(Level.INFO, Inter.getLocText(new String[]{"Template", "alraedy_close"}, new String[]{selected.getEditingFILE().getName(), "."}));
            MutilTempalteTabPane.getInstance().refreshOpenedTemplate(historyList);
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }

    }

    public JTemplate<?, ?> getCurrentEditingTemplate() {
        return this.editingTemplate;
    }

    public void setCurrentEditingTemplate(JTemplate<?, ?> jt) {
        this.editingTemplate = jt;
        //�����ǰ��ʷ�����û��

        if (contains(jt) == -1) {
            addHistory();
        }
        MutilTempalteTabPane.getInstance().refreshOpenedTemplate(historyList);
        //����tab��Ϊ��ǰѡ�е���һ��
        if (editingTemplate != null) {
            MutilTempalteTabPane.getInstance().setSelectedIndex(contains(jt));
        }

    }

    /**
     * �����ʷ��¼
     */
    public void addHistory() {
        if (editingTemplate == null) {
            return;
        }
        DesignerEnvManager.getEnvManager().addRecentOpenedFilePath(editingTemplate.getFullPathName());
        ((HistoryListDataMode) list.getModel()).add(editingTemplate);
    }


    public List<JTemplate<?, ?>> getHistoryList() {
        return historyList;
    }


    /**
     * �����ʷ��¼
     */
    public void removeAllHistory() {
        historyList.clear();
        this.editingTemplate = null;
    }

    public int getHistoryCount() {
        return list.getModel().getSize();
    }


    public UIList getList() {
        return list;
    }


    public JTemplate<?, ?> get(int index) {
        return (JTemplate<?, ?>) list.getModel().getElementAt(index);
    }


    public JTemplate<?, ?> getTemplate(int index) {
        return historyList.get(index);
    }

    /**
     * ��ȡģ���index
     *
     * @param jt ģ��
     * @return λ��
     */
    public int contains(JTemplate<?, ?> jt) {
        for (int i = 0; i < historyList.size(); i++) {
            if (ComparatorUtils.equals(historyList.get(i).getEditingFILE(), jt.getEditingFILE())) {
                return i;
            }
        }
        return -1;
    }


    /**
     * �ж��Ƿ�򿪹���ģ��
     * @param filename �ļ���
     * @return �ļ�λ��
     */
    public int contains(String filename) {
        for (int i = 0; i < historyList.size(); i++) {
            String historyPath = historyList.get(i).getFullPathName();
            if (ComparatorUtils.equals(historyPath, filename)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * �Ƿ��ǵ�ǰ�༭���ļ�
     * @param filename �ļ���
     * @return ���򷵻�TRUE
     */
    public boolean isCurrentEditingFile(String filename) {
        String editingFileName = editingTemplate.getFullPathName();
        return ComparatorUtils.equals(filename, editingFileName);
    }


    private class HistoryListCellRender extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, final boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            JTemplate<?, ?> jt = (JTemplate<?, ?>) value;
            UILabel nameLabel = new UILabel(jt.getEditingFILE().getName());
            final int nameWidth = nameLabel.getPreferredSize().width;
            UILabel uiLabel = new UILabel() {
                public void paint(Graphics g) {
                    GUIPaintUtils.fillPaint((Graphics2D) g, 18, 0, nameWidth + 2, getHeight(), true, Constants.NULL, isSelected ? UIConstants.FLESH_BLUE : UIConstants.NORMAL_BACKGROUND, UIConstants.ARC);
                    super.paint(g);
                }

            };
            uiLabel.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));
            uiLabel.setIcon(jt.getIcon());
            uiLabel.setText(jt.getEditingFILE().getName());
            return uiLabel;
        }

    }


    private class HistoryListDataMode extends AbstractListModel {

        @Override
        public int getSize() {
            return historyList.size();
        }

        @Override
        public JTemplate<?, ?> getElementAt(int index) {
            if (index > getSize() - 1 || index < 0) {
                return null;
            }
            Collections.reverse(historyList);
            JTemplate<?, ?> select = historyList.get(index);
            Collections.reverse(historyList);
            return select;
        }

        public void remove(int index) {
            boolean outofindex = index >= historyList.size() || index < 0;
            if (historyList.isEmpty() || outofindex) {
                return;
            }
            historyList.remove(index);
        }

        public void add(JTemplate<?, ?> jt) {
            historyList.add(jt);
            refresh();
        }
    }

    /**
     * ˢ��
     */
    public void refresh() {
        list.removeAll();
        list.setModel(new HistoryListDataMode());
        list.setSelectedIndex(list.getSelectedIndex());
    }

    /**
     * ��ѡ����ļ�
     */
    public void openSelectedReport() {
        DesignerContext.getDesignerFrame().addAndActivateJTemplate((JTemplate<?, ?>) list.getSelectedValue());
        TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter());
        refresh();
    }

    /**
     * ���ļ������ļ���
     */
    public void openContainerFolder() {
        FileNode fileNode = new FileNode(((JTemplate<?, ?>) list.getSelectedValue()).getEditingFILE().getPath(), false);
        LocalEnv localEnv = (LocalEnv) FRContext.getCurrentEnv();
        localEnv.openContainerFolder(fileNode);
    }

    /**
     * ɾ���ļ�
     */
    public void deleteFile() {
// TODO Auto-generated method stub

    }

    /***
     * ���ļ�
     */
    public void lockFile() {
// TODO Auto-generated method stub

    }

    /**
     * ����
     */
    public void unLockFile() {
// TODO Auto-generated method stub

    }

    /**
     * ·��
     * @return ·��
     */
    public String getSelectedTemplatePath() {
        if (list.getSelectedIndex() < 0 || list.getSelectedIndex() > list.getModel().getSize() - 1) {
            return null;
        }
        ;
        String path = ((HistoryListDataMode) list.getModel()).getElementAt(list.getSelectedIndex()).getEditingFILE().getPath();
        if (path.startsWith(ProjectConstants.REPORTLETS_NAME)) {
            return path.substring(ProjectConstants.REPORTLETS_NAME.length());
        }
        return path;
    }

    public void setToobarStateChangeListener(FileToolbarStateChangeListener toobarStateChangeListener) {
        this.toobarStateChangeListener = toobarStateChangeListener;
    }

    /**
     * �ļ��Ƿ����
     *
     * @param newName �ļ���
     * @param oldName ԭ��
     * @param suffix  ��׺��
     * @return �ļ��Ƿ����
     */
    public boolean isNameAlreadyExist(String newName, String oldName, String suffix) {
        boolean isNameAreadyExist = false;
        for (int i = 0; i < getHistoryCount(); i++) {
            JTemplate<?, ?> jt = ((HistoryListDataMode) list.getModel()).getElementAt(i);
            if (ComparatorUtils.equals(jt.getEditingFILE().getName(), newName + suffix)) {
                isNameAreadyExist = true;
                break;
            }
        }
        if (ComparatorUtils.equals(newName, oldName)) {
            isNameAreadyExist = false;
        }

        return isNameAreadyExist;
    }
}
