package com.fr.design.widget;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.dialog.BasicPane;
import com.fr.form.ui.Button;
import com.fr.form.ui.*;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.design.widget.btn.ButtonConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

/**
 * CellEditorDef Pane.
 */
public class WidgetPane extends BasicPane implements ItemListener {

    private EditorTypeComboBox editorTypeComboBox;
    private CellWidgetCardPane cellEditorCardPane;
    private boolean shouldFireSelectedEvent;
    protected JPanel northPane;

    public WidgetPane() {
        this(null);
    }

    /**
     * Constructor
     */
    public WidgetPane(ElementCasePane pane) {
        this.initComponents(pane);
    }

    protected void initComponents(ElementCasePane pane) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        northPane = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
        this.add(northPane, BorderLayout.NORTH);

        northPane.add(new UILabel(Inter.getLocText("FR-Designer_Type") + ":"));
        editorTypeComboBox = new EditorTypeComboBox(pane != null);
        editorTypeComboBox.setPreferredSize(new Dimension(150, 30));
        editorTypeComboBox.setMaximumRowCount(16);
        northPane.add(editorTypeComboBox);
        editorTypeComboBox.addItemListener(this);

        cellEditorCardPane = new CellWidgetCardPane(pane);
        this.add(cellEditorCardPane, BorderLayout.CENTER);
    }

    /**
     * ״̬�ı�
     * @param e �¼�����
     */
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            if (e.getItem() instanceof Item && ((Item) e.getItem()).getValue() instanceof WidgetConfig) {
                populate(editorTypeComboBox.getCellWidget());
                return;
            }
            if (shouldFireSelectedEvent) {
                Widget selectedItem = editorTypeComboBox.getCellWidget();
                populateWidgetConfig(selectedItem);
            }
        }
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer_Widget");
    }

    public void populate(Widget widget) {
        if (widget == null) {
            widget = new TextEditor();
        }

        if (widget instanceof NameWidget) {
            String name = ((NameWidget) widget).getName();
            shouldFireSelectedEvent = false;
            editorTypeComboBox.setSelectedItem(new Item(name, name));
            shouldFireSelectedEvent = true;
            cellEditorCardPane.populate(widget);
            return;
        }

        Class clazz = widget.getClass();
        if (ArrayUtils.contains(ButtonConstants.CLASSES4BUTTON, clazz)) {
            clazz = Button.class;
        }
        shouldFireSelectedEvent = false;
        editorTypeComboBox.setSelectedItemByWidgetClass(clazz);
        shouldFireSelectedEvent = true;

        cellEditorCardPane.populate(widget);
    }

    public Widget update() {
        return cellEditorCardPane.update();
    }
    
    protected void populateWidgetConfig(Widget widget) {
    	cellEditorCardPane.populate(widget);
    }


    private static class EditorTypeComboBox extends UIComboBox {

        private Item item = new Item(Inter.getLocText("Widget-User_Defined"),
                Inter.getLocText("Widget-User_Defined"));

        public EditorTypeComboBox(boolean userDefined) {
            this.setEditable(false);
            this.setModel(new DefaultComboBoxModel(getWidgetsName(userDefined)));
            this.setRenderer(new UIComboBoxRenderer() {
                public Component getListCellRendererComponent(JList list,
                                                              Object value, int index, boolean isSelected,
                                                              boolean cellHasFocus) {
                    if (value == item) {
                        UILabel label = new UILabel(Inter
                                .getLocText("Widget-User_Defined")
                                + " ��������");
                        label.setEnabled(false);
                        return label;
                    }
                    return super.getListCellRendererComponent(list, value,
                            index, isSelected, cellHasFocus);
                }
            });
            this.setPreferredSize(new Dimension(100, 20));
        }

        public void setSelectedItemByWidgetClass(Class clazz) {
            WidgetOption[] options = getWidgetOptions();
            for (int i = 0, l = this.getModel().getSize(); i < l; i++) {
                Item item = (Item) this.getModel().getElementAt(i);
                if (item.getValue() instanceof Integer
                        && options[(Integer) item.getValue()].widgetClass() == clazz) {
                    this.setSelectedItem(item);
                }
            }
        }

        public void setSelectedItem(Object anObject) {
            if (anObject == item) {
                return;
            }
            super.setSelectedItem(anObject);
        }

        private Vector getWidgetsName(boolean userDefined) {

            WidgetOption[] reportWidgetInstance = getWidgetOptions();
            Vector<Item> items = new Vector<Item>();
            for (int i = 0, l = reportWidgetInstance.length; i < l; i++) {
                items.add(new Item(reportWidgetInstance[i].optionName(), i));
            }
            WidgetManagerProvider manager = WidgetManager.getProviderInstance();
            java.util.Iterator<String> nameIt = manager.getWidgetConfigNameIterator();
            if (userDefined && nameIt.hasNext()) {
                items.add(item);
                while (nameIt.hasNext()) {
                    String name = nameIt.next();
                    items.add(new Item(name, name));
                }
            }

            return items;
        }

        public Widget getCellWidget() {
            Item item = (Item) this.getSelectedItem();
            if (item.getValue() instanceof Integer) {
                return getWidgetOptions()[(Integer) item.getValue()].createWidget();
            } else if (item.getValue() instanceof String) {
                return new NameWidget((String) item.getValue());
            }
            return null;
        }

        private WidgetOption[] getWidgetOptions() {
           return (WidgetOption[])ArrayUtils.addAll(WidgetOption.getReportWidgetInstance(), ExtraDesignClassManager.getInstance().getCellWidgetOptions());
        }
    }

    /**
     * У��
     * @throws Exception �׳��쳣
     */
    public void checkValid() throws Exception {
        this.cellEditorCardPane.checkValid();
    }

    public static class Item {
        private String name;
        private Object value;


        public Item(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public Object getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        /**
         * ת�����ַ�����ʽ
         * @return �����ַ���
         */
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Item
                    && ComparatorUtils.equals(((Item) o).value, value)
                    && ComparatorUtils.equals(((Item) o).name, name);
        }
    }
}
