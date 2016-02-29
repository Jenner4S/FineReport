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
     * 刷新下拉列表和按钮
     * @param values 下拉列表里的值
     */
    public void refreshMenuAndAddMenuAction(List<UIMenuNameableCreator> values) {
        if (values == null || values.isEmpty()) {
            return;
        }
        this.values = values;
        popMenu.removeAll();
        // 如果只有一个弹出项，那么就不弹出，直接让add按钮替代
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
        //先屏蔽掉这个，之后还有别的
        String[] names = {Inter.getLocText("FR-Hyperlink_Chart_Float"), Inter.getLocText("FR-Hyperlink_Chart_Cell")};
        for (String name : names){
            if(!jTemplate.isJWorkBook() && ComparatorUtils.equals(itemName,name)){
                if(jTemplate.getEditingReportIndex() == BaseJForm.ELEMENTCASE_TAB &&  ComparatorUtils.equals(itemName, names[0])){
                    //表单报表块中图表悬浮元素超链，只屏蔽联动悬浮元素
                    return false;
                } else if(jTemplate.getEditingReportIndex() == BaseJForm.FORM_TAB) {
                    //表单图表超链屏蔽掉联动悬浮元素和联动单元格
                    return false;
                }
            }
        }
        String formName = Inter.getLocText("Hyperlink-Form_link");
        return !(jTemplate.isJWorkBook() && ComparatorUtils.equals(itemName, formName));
    }

    private String createUnrepeatedName(String prefix) {
        List<UIMenuNameableCreator> all = tablePane.updateBean();
        // richer:生成的名字从1开始. kunsnat: 添加属性从0开始.
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
         * 增加布局
         * @param name 名字
         * @param comp 组件
         */
        public void addLayoutComponent(String name, Component comp) {

        }

        /**
         * 删除组件
         * @param comp 组件
         */
        public void removeLayoutComponent(Component comp) {

        }

        /**
         * 获得组件的大小
         * @param parent 上层容器
         * @return 组件的大小
         */
        public Dimension preferredLayoutSize(Container parent) {
            int h = addButton.getPreferredSize().height + tablePane.getPreferredSize().height;
            return new Dimension(parent.getWidth(), h + 2);
        }

        /**
         * 最小的布局大小
         * @param parent 上层容器
         * @return 最小的大小
         */
        public Dimension minimumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }

        /**
         * 布局容器
         * @param parent 上层容器
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
     * 增加监听事件
     * @param l 监听的对象
     */
    public void addChangeListener(ChangeListener l) {
        this.listenerList.add(ChangeListener.class, l);
        this.tablePane.addChangeListener(l);
    }

    /**
     * 删除监听事件
     * @param l 需要删除的事件
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
     * 响应事件
     */
    public void fireTargetChanged() {
        this.validate();
        this.repaint();
        this.revalidate();
        fireChanged();
    }

    /**
     * 预定义的宽度和高度
     */
    public Dimension getPreferredSize() {
        Dimension dim = new Dimension();
        dim.width = super.getPreferredSize().width;
        dim.height = addButton.getPreferredSize().height + tablePane.getPreferredSize().height + 2;
        return dim;
    }

    /**
     * 更新并且重载 当前列表的值
     * @param list 更新的列表
     */
    public void populateBean(List list) {
        tablePane.populateBean(list);
    }

    /**
     * 返回 当前列表保存的值, 嵌套的一层, 主要对应creator中的obj
     */
    public List updateBean() {
        return tablePane.updateBean();
    }

    /**
     * 重置每个条目的名字
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
     * 测试例子界面
     * @param args 参数向量
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
     * 注册观察者监听事件
     * @param listener 观察者监听事件
     */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;

    }

    /**
     * 是否需要响应事件
     * @return 需要相应
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }
}