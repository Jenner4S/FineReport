package com.fr.design.gui.frpane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.BaseUtils;
import com.fr.design.constants.UIConstants;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.design.gui.imenutable.UIMenuTable;
import com.fr.design.hyperlink.ReportletHyperlinkPane;
import com.fr.design.hyperlink.WebHyperlinkPane;
import com.fr.design.javascript.EmailPane;
import com.fr.design.mainframe.BaseJForm;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.DialogActionListener;
import com.fr.design.dialog.UIDialog;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.js.AbstractJavaScript;
import com.fr.js.EmailJavaScript;
import com.fr.js.ReportletHyperlink;
import com.fr.js.WebHyperlink;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import com.fr.design.utils.gui.GUICoreUtils;

public class UICorrelationComboBoxPane extends JPanel implements UIObserver {
    private JPopupMenu popMenu;
    private UIMenuTable tablePane;
    private UIButton addButton;
    private List<UIMenuNameableCreator> values;
    private UIObserverListener uiObserverListener;

    public UICorrelationComboBoxPane() {
        this(null);
        iniListener();
    }

    public UICorrelationComboBoxPane(List<UIMenuNameableCreator> values) {
        initComponents();
        initLayout();
        refreshMenuAndAddMenuAction(values);
        iniListener();
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
     * ˢ�������б�Ͱ�ť
     * @param values �����б����ֵ
     */
    public void refreshMenuAndAddMenuAction(List<UIMenuNameableCreator> values) {
        if (values == null || values.isEmpty()) {
            return;
        }
        this.values = values;
        popMenu.removeAll();
        // ���ֻ��һ���������ô�Ͳ�������ֱ����add��ť���
        if (values.size() > 1) {
            for (UIMenuNameableCreator value : values) {
                final String itemName = value.getName();
                if(!whetherAdd(itemName)){
                    continue;
                }
                UIMenuItem item = new UIMenuItem(itemName);
                final UIMenuNameableCreator creator = value;
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        UIMenuNameableCreator ui = creator.clone();
                        ui.setName(createUnrepeatedName(itemName));
                        tablePane.addLine(ui);

                        final Object obj = ui.getObj();
                        final BasicBeanPane pane = ui.getPane();
                        UIDialog dialog = pane.showUnsizedWindow(SwingUtilities.getWindowAncestor(new JPanel()), new DialogActionAdapter() {
                            public void doOk() {
                                pane.updateBean(obj);
                                fireTargetChanged();
                            }

                            public void doCancel() {
                                int row = tablePane.getRowCount();
                                tablePane.removeLine(row - 1);
                                fireTargetChanged();
                            }
                        });
                        dialog.setSize(500, 500);
                        GUICoreUtils.centerWindow(dialog);
                        dialog.setVisible(true);
                    }
                });
                popMenu.add(item);
            }
        }
        initAddButtonListener();
    }

    private boolean whetherAdd(String itemName){
        JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        //�����ε������֮���б��
        String[] names = {Inter.getLocText("FR-Hyperlink_Chart_Float"), Inter.getLocText("FR-Hyperlink_Chart_Cell")};
        for (String name : names){
            if(!jTemplate.isJWorkBook() && ComparatorUtils.equals(itemName,name)){
                if(jTemplate.getEditingReportIndex() == BaseJForm.ELEMENTCASE_TAB &&  ComparatorUtils.equals(itemName, names[0])){
                    //���������ͼ������Ԫ�س�����ֻ������������Ԫ��
                    return false;
                } else if(jTemplate.getEditingReportIndex() == BaseJForm.FORM_TAB) {
                    //��ͼ�������ε���������Ԫ�غ�������Ԫ��
                    return false;
                }
            }
        }
        String formName = Inter.getLocText("Hyperlink-Form_link");
        return !(jTemplate.isJWorkBook() && ComparatorUtils.equals(itemName, formName));
    }

    private String createUnrepeatedName(String prefix) {
        List<UIMenuNameableCreator> all = tablePane.updateBean();
        // richer:���ɵ����ִ�1��ʼ. kunsnat: ������Դ�0��ʼ.
        int count = all.size() + 1;
        while (true) {
            String name_test = prefix + count;
            boolean repeated = false;
            for (int i = 0, len = all.size(); i < len; i++) {
                UIMenuNameableCreator nameable = all.get(i);
                if (ComparatorUtils.equals(nameable.getName(), name_test)) {
                    repeated = true;
                    break;
                }
            }

            if (!repeated) {
                return name_test;
            }
            count++;
        }
    }

    private ActionListener addAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (values.size() == 1) {
                UIMenuNameableCreator current = values.get(0);

                UIMenuNameableCreator ui = current.clone();
                ui.setName(createUnrepeatedName(current.getName()));
                tablePane.addLine(ui);
                fireTargetChanged();

                final Object obj = ui.getObj();
                final BasicBeanPane pane = ui.getPane();

                pane.populateBean(obj);

                UIDialog dialog = pane.showUnsizedWindow(SwingUtilities.getWindowAncestor(new JPanel()), new DialogActionListener() {
                    @Override
                    public void doOk() {
                        pane.updateBean(obj);
                        fireTargetChanged();
                    }

                    @Override
                    public void doCancel() {
                        int row = tablePane.getRowCount();
                        tablePane.removeLine(row - 1);
                        fireTargetChanged();
                    }
                });
                dialog.setSize(500, 500);
                dialog.setVisible(true);
            } else {
                popMenu.show(UICorrelationComboBoxPane.this, addButton.getX() + 1, addButton.getY() + addButton.getHeight());
            }
        }
    };

    protected ActionListener addAction(final List<UIMenuNameableCreator> values) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (values.size() == 1) {
                    UIMenuNameableCreator current = values.get(0);
                    current.setName(createUnrepeatedName(current.getName()));
                    tablePane.addLine(current);
                    fireTargetChanged();
                } else {
                    popMenu.show(UICorrelationComboBoxPane.this, addButton.getX() + 1, addButton.getY() + addButton.getHeight());
                }
            }
        };
    }

    protected void initComponents() {
        tablePane = new UIMenuTable();
        popMenu = new JPopupMenu() {
            @Override
            public Dimension getPreferredSize() {
                Dimension dimension = new Dimension();
                dimension.height = super.getPreferredSize().height;
                dimension.width = addButton.getWidth() - 2;
                return dimension;
            }
        };
        initAddButton();
    }

    protected void initAddButton() {
        addButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png"));
        addButton.setBorderType(UIButton.OTHER_BORDER);
        addButton.setOtherBorder(UIConstants.BS, UIConstants.LINE_COLOR);
    }

    private void initAddButtonListener() {
        ActionListener[] listeners = addButton.getListeners(ActionListener.class);
        if (!ArrayUtils.contains(listeners, addAction)) {
            addButton.addActionListener(addAction);
        }
    }

    protected void initLayout() {
        this.setLayout(new Layout());
        this.add(addButton);
        this.add(tablePane);
    }

    public class Layout implements LayoutManager {

        /**
         * ���Ӳ���
         * @param name ����
         * @param comp ���
         */
        public void addLayoutComponent(String name, Component comp) {

        }

        /**
         * ɾ�����
         * @param comp ���
         */
        public void removeLayoutComponent(Component comp) {

        }

        /**
         * �������Ĵ�С
         * @param parent �ϲ�����
         * @return ����Ĵ�С
         */
        public Dimension preferredLayoutSize(Container parent) {
            int h = addButton.getPreferredSize().height + tablePane.getPreferredSize().height;
            return new Dimension(parent.getWidth(), h + 2);
        }

        /**
         * ��С�Ĳ��ִ�С
         * @param parent �ϲ�����
         * @return ��С�Ĵ�С
         */
        public Dimension minimumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }

        /**
         * ��������
         * @param parent �ϲ�����
         */
        public void layoutContainer(Container parent) {
            int width = parent.getWidth();
            int y = 0;
            tablePane.setBounds(0, y, width, tablePane.getPreferredSize().height);
            y += tablePane.getPreferredSize().height + 2;
            addButton.setBounds(0, y, width, addButton.getPreferredSize().height);
        }
    }

    /**
     * ���Ӽ����¼�
     * @param l �����Ķ���
     */
    public void addChangeListener(ChangeListener l) {
        this.listenerList.add(ChangeListener.class, l);
        this.tablePane.addChangeListener(l);
    }

    /**
     * ɾ�������¼�
     * @param l ��Ҫɾ�����¼�
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

        this.tablePane.fireTargetChanged();
    }

    /**
     * ��Ӧ�¼�
     */
    public void fireTargetChanged() {
        this.validate();
        this.repaint();
        this.revalidate();
        fireChanged();
    }

    /**
     * Ԥ����Ŀ�Ⱥ͸߶�
     */
    public Dimension getPreferredSize() {
        Dimension dim = new Dimension();
        dim.width = super.getPreferredSize().width;
        dim.height = addButton.getPreferredSize().height + tablePane.getPreferredSize().height + 2;
        return dim;
    }

    /**
     * ���²������� ��ǰ�б��ֵ
     * @param list ���µ��б�
     */
    public void populateBean(List list) {
        tablePane.populateBean(list);
    }

    /**
     * ���� ��ǰ�б����ֵ, Ƕ�׵�һ��, ��Ҫ��Ӧcreator�е�obj
     */
    public List updateBean() {
        return tablePane.updateBean();
    }

    /**
     * ����ÿ����Ŀ������
     */
    public void resetItemName(){
        for(int i = 0; i < tablePane.getRowCount(); i++){
            UIMenuNameableCreator line = tablePane.getLine(i);
            Object obj = line.getObj();
            if(obj instanceof AbstractJavaScript){
                AbstractJavaScript script = (AbstractJavaScript)obj;
                String itemName = script.getItemName();
                if(!StringUtils.isBlank(itemName)){
                    line.setName(itemName);
                }
            }
        }
    }

    /**
     * �������ӽ���
     * @param args ��������
     */
    public static void main(String... args) {
        JFrame jf = new JFrame("test");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel content = (JPanel) jf.getContentPane();
        content.setLayout(new BorderLayout());
        List<UIMenuNameableCreator> data = new ArrayList<UIMenuNameableCreator>();
        UIMenuNameableCreator reportlet = new UIMenuNameableCreator(Inter.getLocText("FR-Hyperlink_Reportlet"),
                new ReportletHyperlink(), true ? ReportletHyperlinkPane.CHART.class : ReportletHyperlinkPane.class);

        UIMenuNameableCreator email = new UIMenuNameableCreator(Inter.getLocText("FR-Designer_Email"),
                new EmailJavaScript(), EmailPane.class);

        UIMenuNameableCreator web = new UIMenuNameableCreator(Inter.getLocText("Hyperlink-Web_link"),
                new WebHyperlink(), true ? WebHyperlinkPane.CHART.class : WebHyperlinkPane.class);
        data.add(reportlet);
        data.add(email);
        data.add(web);
        UICorrelationComboBoxPane pane = new UICorrelationComboBoxPane(data);
        content.add(pane, BorderLayout.CENTER);
        GUICoreUtils.centerWindow(jf);
        jf.setSize(400, 400);
        jf.setVisible(true);
    }

    /**
     * ע��۲��߼����¼�
     * @param listener �۲��߼����¼�
     */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;

    }

    /**
     * �Ƿ���Ҫ��Ӧ�¼�
     * @return ��Ҫ��Ӧ
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }
}
