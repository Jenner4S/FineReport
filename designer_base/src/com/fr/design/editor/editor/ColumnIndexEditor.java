package com.fr.design.editor.editor;

import com.fr.design.gui.icombobox.IntComboBox;
import com.fr.general.Inter;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

/**
 * ����ű༭����������һ������������(0-value)
 *
 * @author zhou
 * @since 2012-6-1����2:24:04
 */
public class ColumnIndexEditor extends Editor<Object> {
    protected IntComboBox valueColumnIndexComboBox;

    public ColumnIndexEditor() {
        this(0);
    }

    /**
     * Ĭ�������ǡ�����š���Ҳ����ͨ���ڶ������캯���ı�
     *
     * @param value
     */
    public ColumnIndexEditor(int value) {
        this(value, Inter.getLocText("Datasource-Column_Index"));
    }

    public ColumnIndexEditor(int value, String name) {
        this.setLayout(new BorderLayout(0, 0));
        valueColumnIndexComboBox = new IntComboBox();
        for (int i = 1; i <= value; i++) {
            valueColumnIndexComboBox.addItem(new Integer(i));
        }

        if (value > 0) {
            valueColumnIndexComboBox.setSelectedInt(1);
        }
        this.add(valueColumnIndexComboBox, BorderLayout.CENTER);
        this.setName(name);
        valueColumnIndexComboBox.setBorder(null);

    }

    @Override
    public Integer getValue() {
        return valueColumnIndexComboBox.getSelectedInt();
    }

    @Override
    public void setValue(Object value) {
        valueColumnIndexComboBox.setSelectedInt(value == null ? 0 : Integer.parseInt(value.toString()));
    }

    public String getIconName() {
        return "ds_column_index";
    }

    /**
     * object�����Ƿ���Integer
     *
     * @param object �����������жϵĲ���
     * @return �����Ƿ���Index
     */
    public boolean accept(Object object) {
        return object instanceof Integer;
    }

    /**
     * ����һ��ItemListener
     *
     * @param l �������ӵ�Listener
     */
    public void addItemListener(ItemListener l) {
        valueColumnIndexComboBox.addItemListener(l);
    }

    /**
     * ����һ��ActionListener
     *
     * @param l �������ӵ�Listener
     */
    public void addActionListener(ActionListener l){
        valueColumnIndexComboBox.addActionListener(l);
    }

    /**
     * ����
     */
    public void reset() {
        valueColumnIndexComboBox.setSelectedIndex(-1);
    }

    /**
     * ���������
     */
    public void clearData() {
        valueColumnIndexComboBox.removeAllItems();
    }


    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        valueColumnIndexComboBox.setEnabled(enabled);
    }

}