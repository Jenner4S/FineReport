/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.editor.editor;

import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: wikky
 * Date: 14-3-21
 * Time: ����9:12
 * To change this template use File | Settings | File Templates.
 */
public class XMLANameEditor extends ColumnIndexEditor{
    private String[] XMLANames;

    /**
     * ��ά���ݼ����˽����ά�ȺͶ���ֵ����Editor
     */
    public XMLANameEditor() {
        this(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    /**
     * ��ά���ݼ����˽����ά�ȺͶ���ֵ����Editor
     * @param columnNames ����ֵ
     */
    public XMLANameEditor(String[] columnNames) {
        this(columnNames, Inter.getLocText("Measure"));
    }

    /**
     * ��ά���ݼ����˽����ά�ȺͶ���ֵ����Editor
     * @param columnNames ά�Ȼ����ֵ
     * @param name ��ʾ����
     */
    public XMLANameEditor(final String[] columnNames, String name) {
        super(columnNames.length, name);
        this.XMLANames = columnNames;
        valueColumnIndexComboBox.setRenderer(new UIComboBoxRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value == null) {
                    this.setText("");
                } else {
                    this.setText(columnNames[((Integer) value).intValue() - 1]);
                    this.setToolTipText(columnNames[((Integer) value).intValue() - 1]);
                }

                return this;
            }
        });
    }

    /**
     *��XMLANames��ֵ
     * @param value Ҫ�����ֵ
     */
    @Override
    public void setValue(Object value) {
        for (int i = 0; i < XMLANames.length; i++) {
            if (XMLANames[i].equalsIgnoreCase(String.valueOf(value))) {
                super.setValue(i + 1);
                return;
            }
        }

        super.reset();
    }

    /**
     * �жϲ����Ƿ���String
     * @param object �����������жϵĲ���
     * @return �����Ƿ���String
     */
    @Override
    public boolean accept(Object object) {
        return object instanceof String;
    }

    /**
     * ��ȡά�Ȼ����ֵ
     * @return ����ά�Ȼ����ֵ
     */
    public String getColumnName() {
        int index = ((Integer) this.getValue()).intValue() - 1;
        return getColumnNameAtIndex(index);
    }

    /**
     * ��ȡά�Ȼ����ֵ
     * @param index ��ѡ������
     * @return ������Ŷ�Ӧ��ά�Ȼ����ֵ
     */
    public String getColumnNameAtIndex(int index) {
        return index >= 0 && XMLANames.length > index ? XMLANames[index] : StringUtils.EMPTY;
    }

    /**
     * ��ȡͼ����
     * @return
     */
    public String getIconName() {
        return "cube";
    }

}
