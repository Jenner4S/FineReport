package com.fr.design.gui.frpane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;

import com.fr.base.BaseUtils;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.UIDefaultTableCellEditor;
import com.fr.design.gui.itable.UITable;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.general.Inter;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * @author jerry
 */
public class UICorrelationPane extends JPanel implements UIObserver {

    private static int size = 20;
    protected UIButton addButton;
    protected UITable tablePane;
    protected int columnCount;
    private UIObserverListener uiObserverListener;
    private HeaderPane header;

    /**
     * @param �����������֣�length��������
     */
    public UICorrelationPane(String... names) {
        columnCount = names.length;
        initComponents();
        header = new HeaderPane(names);
        initLayout();
        iniListener();
    }

    /**
     * @param column ����
     *               û�б�������CorrelationPane
     */
    public UICorrelationPane(int column) {
        columnCount = column;
        initComponents();
        initLayout();
        iniListener();
    }

    /**
     * ����Ҫʹ����Ӱ�ť, Ŀǰֻ��ͼ��: �ɼ�ͼ, ����ͼ��Ԫ�����ݽ����õ�.
     */
    public void noAddUse() {
        addButton.setEnabled(false);
        addButton.setVisible(false);
    }

    protected boolean isDeletable(){
        return true;
    }

    /**
     * ��Ӱ�ť �� �Ƿ����
     * @param use �Ƿ����
     */
    public void checkBoxUse(boolean use) {
        addButton.setEnabled(use);
    }

    public void populateBean(List<Object[]> values) {
        tablePane.populateBean(values);
        this.validate();
        this.repaint();
        this.revalidate();
    }

    public List<Object[]> updateBean() {
        List<Object[]> list = tablePane.updateBean();

        if (tablePane.isEditing()) {
            TableCellEditor editor = tablePane.getCellEditor();
            if (editor != null) {
                Object value = editor.getCellEditorValue();
                int row = tablePane.getEditingRow();
                int col = tablePane.getEditingColumn();
                if (list.size() > row) {
                    Object[] objs = list.get(row);
                    if (objs != null && objs.length > col) {
                        objs[col] = value;
                    }
                }
            }
        }

        return list;
    }


    /**
     *���һ��
     * @param line ��
     */
    public void addLine(Object[] line) {
        tablePane.addLine(line);
    }

    /**
     * ɾ��ĳ������
     * @param rowIndex �к�
     */
    public void removeLine(int rowIndex) {
        tablePane.removeLine(rowIndex);
    }

    /**
     * @return ��Ӱ�ť���¼��ӿ�
     */
    protected ActionListener getAddButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tablePane.addBlankLine();
                fireTargetChanged();
            }
        };
    }

    protected void initLayout() {
        this.setLayout(new Layout());
        this.add(addButton);
        if (header != null) {
            this.add(header);
        }
        this.add(tablePane);
    }

    protected void initComponents() {
        tablePane = initUITable();
        initAddButton();
    }

    private void iniListener() {
        if (shouldResponseChangeListener()) {
            this.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (uiObserverListener == null) {
                        return;
                    }
                    uiObserverListener.doChange();
                }
            });
        }
    }

    /**
     * ֹͣ��Ԫ��༭
     */
    public void stopCellEditing() {
        if (tablePane.getDefaultEditor(UITable.class) != null) {
            tablePane.getDefaultEditor(UITable.class).stopCellEditing();
        }
    }

    /**
     * ֹͣ���༭
     * @param e �¼�
     */
    public void stopPaneEditing(ChangeEvent e) {
        fireChanged();
    }

    /**
     * ����table�༭��
     * @return �༭��
     */
    public UITableEditor createUITableEditor() {
        return new UIDefaultTableCellEditor(new UITextField());
    }

    protected UITable initUITable() {
        return new UITable(columnCount) {

            public UITableEditor createTableEditor() {
                return UICorrelationPane.this.createUITableEditor();
            }

            public void tableCellEditingStopped(ChangeEvent e) {
                UICorrelationPane.this.stopPaneEditing(e);
            }

        };
    }

    protected void initAddButton() {
        addButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png")) {
            public boolean shouldResponseChangeListener() {
                return false;
            }
        };
        addButton.setBorderType(UIButton.OTHER_BORDER);
        addButton.setOtherBorder(UIConstants.BS, UIConstants.LINE_COLOR);
        addButton.addActionListener(getAddButtonListener());
    }

    /**
     * ֪ͨ�仯
     */
    public void fireTargetChanged() {
        this.validate();
        this.repaint();
        this.revalidate();
        fireChanged();
    }

    /**
   	 * ������Ǽ�һ���۲��߼����¼�
   	 *
   	 * @param listener �۲��߼����¼�
   	 */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
    }

    /**
   	 * ����Ƿ���Ҫ��Ӧ��ӵĹ۲����¼�
   	 *
   	 * @return �����Ҫ��Ӧ�۲����¼��򷵻�true�����򷵻�false
   	 */
    public boolean shouldResponseChangeListener() {
        return true;
    }

    public class Layout implements LayoutManager {

        /**
         * �������
         * @param name �����
         * @param comp ���
         */
        public void addLayoutComponent(String name, Component comp) {

        }

        /**
         *�Ƴ����
         * @param comp ���
         */
        public void removeLayoutComponent(Component comp) {

        }

        /**
         *��Ѵ�С
         * @param parent ������
         * @return ��С
         */
        public Dimension preferredLayoutSize(Container parent) {
            int h = addButton.getPreferredSize().height + tablePane.getPreferredSize().height;
            if (header != null) {
                h = header.getPreferredSize().height;
            }
            return new Dimension(parent.getWidth(), h + 2);
        }

        /**
         * ��С���ִ�С
         * @param parent ������
         * @return ��С
         */
        public Dimension minimumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }

        /**
         * ��������
         * @param parent ������
         */
        public void layoutContainer(Container parent) {
            int width = parent.getWidth();
            int y = 0;
            if (header != null) {
                header.setBounds(0, y, width - (isDeletable() ?size:0), header.getPreferredSize().height);
                y = y + header.getPreferredSize().height;
            }
            tablePane.setBounds(0, y, width+(isDeletable() ?0:size), tablePane.getPreferredSize().height);
            y += tablePane.getPreferredSize().height + 2;
            addButton.setBounds(0, y, width, addButton.getPreferredSize().height);
        }
    }

    private class HeaderPane extends JPanel {

        public HeaderPane(String[] names) {
            UILabel[] labels = new UILabel[names.length];
            this.setLayout(new GridLayout(0, names.length));
            for (int i = 0; i < names.length; i++) {
                labels[i] = new UILabel(names[i], UILabel.CENTER) {
                    @Override
                    public void paint(Graphics g) {
                        super.paint(g);
                        int width = getWidth();
                        int height = getHeight();
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setColor(UIConstants.LINE_COLOR);
                        g2d.drawLine(4, height - 1, width - 4, height - 1);
                    }

                };
                this.add(labels[i]);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(super.getPreferredSize().width - (isDeletable() ?size:0), size);
        }

    }

    /**
     * ���Ӽ���
     * @param l ����
     */
    public void addChangeListener(ChangeListener l) {
        this.listenerList.add(ChangeListener.class, l);
    }

    /**
     * �Ƴ�����
     * @param l ����
     */
    public void removeChangeListener(ChangeListener l) {
        this.listenerList.remove(ChangeListener.class, l);
    }

    // august: Process the listeners last to first
    protected void fireChanged() {
        Object[] listeners = listenerList.getListenerList();

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                ((ChangeListener) listeners[i + 1]).stateChanged(new ChangeEvent(this));
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension dim = new Dimension();
        dim.width = super.getPreferredSize().width;
        dim.height = addButton.getPreferredSize().height + tablePane.getPreferredSize().height + 2;
        if (header != null) {
            dim.height += header.getPreferredSize().height;
        }
        return dim;
    }

    public UITable getTable() {
        return tablePane;
    }


    /**
     * ������
     * @param args ����
     */
    public static void main(String... args) {
        JFrame jf = new JFrame("test");
        final String[] columnNames = {Inter.getLocText("Actual_Value"), Inter.getLocText("Display_Value")};
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel content = (JPanel) jf.getContentPane();
        content.setLayout(new BorderLayout());
        UICorrelationPane pane = new UICorrelationPane(columnNames);
        content.add(pane, BorderLayout.CENTER);
        List<Object[]> values = new ArrayList<Object[]>();
        values.add(new String[]{"askdjf1", "skdjfasjdf1"});
        values.add(new String[]{"askdjf2", "skdjfasjdf2"});
        values.add(new String[]{"askdjf3", "skdjfasjdf3"});
        values.add(new String[]{"askdjf4", "skdjfasjdf4"});
        values.add(new String[]{"askdjf5", "skdjfasjdf5"});
        values.add(new String[]{"askdjf6", "skdjfasjdf6"});
        values.add(new String[]{"askdjf7", "skdjfasjdf7"});
        values.add(new String[]{"askdjf8", "skdjfasjdf8"});
        pane.populateBean(values);
        GUICoreUtils.centerWindow(jf);
        jf.setSize(400, 400);
        jf.setVisible(true);
    }
}
