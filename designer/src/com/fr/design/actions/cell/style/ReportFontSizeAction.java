/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.cell.style;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JComponent;

import com.fr.base.Style;
import com.fr.base.Utils;
import com.fr.base.core.StyleUtils;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.general.Inter;

/**
 * Font size.
 */
public class ReportFontSizeAction extends AbstractStyleAction {
	
	private static final int MAX_FONT_SIZE = 100;
	
	public ReportFontSizeAction(ElementCasePane t) {
		super(t);
		
        this.setName(Inter.getLocText("FRFont-Size"));
    }

	/**
	 * Ӧ��ѡ�е���ʽ
	 * 
	 * @param style ֮ǰ����ʽ
	 * @param defStyle ѡ�е���ʽ
	 * 
	 * @return ���ĺ����ʽ
	 * 
	 * @date 2015-1-22-����4:54:00
	 * 
	 */
	public Style executeStyle(Style style, Style defStyle) {
        Object object = this.getValue(UIComboBox.class.getName());
        if (object != null && object instanceof UIComboBox) {
        	Object value = ((UIComboBox) object).getSelectedItem();
        	float selectedFontSize;
        	selectedFontSize = Utils.round5(Float.parseFloat(value.toString()));

            if (style.getFRFont().getSize() == defStyle.getFRFont().getSize()) {
            	style = StyleUtils.setReportFontSize(style, defStyle.getFRFont().getSize());
            }
            if (selectedFontSize == style.getFRFont().getSize()) {
                return style;
            }
            style = StyleUtils.setReportFontSize(style, selectedFontSize);
        }

        return style;
    }

    public void setFontSize(float size) {
        Object object = this.getValue(UIComboBox.class.getName());
        if (object != null && object instanceof UIComboBox) {
            UIComboBox comboBox = (UIComboBox) object;
            //�Ⱥ���ǰ��Font Size�Ƚ�.
            if (ComparatorUtils.equals(comboBox.getSelectedItem(), size)) {
                return;
            }

            //������Font Size
            comboBox.removeActionListener(this);
            comboBox.setSelectedItem(size + "");
            comboBox.addActionListener(this);
        }
    }

    /**
	 * �������������
	 * 
	 * @return ���
	 * 
	 * @date 2015-1-22-����4:53:29
	 * 
	 */
	public JComponent createToolBarComponent() {
        Object object = this.getValue(UIComboBox.class.getName());
        if (object == null || !(object instanceof UIComboBox)) {
            Vector<Integer> integerList = new Vector<Integer>();
            for (int i = 1; i < MAX_FONT_SIZE; i++) {
                integerList.add(i);
            }

            UIComboBox itemComponent = new UIComboBox(integerList);
            this.putValue(UIComboBox.class.getName(), itemComponent);
            itemComponent.setMinimumSize(new Dimension(50, 20));
            itemComponent.setPreferredSize(new Dimension(50, 20));
            itemComponent.setEnabled(this.isEnabled());
            itemComponent.addActionListener(this);
            //���������С�ɱ༭
            itemComponent.setEditable(true);

            return itemComponent;
        }

        return (JComponent) object;
    }

    /**
     * ������ʽ
     * 
     * @param style ��ʽ
     * 
     */
    @Override
	public void updateStyle(Style style) {
        if (style == null) {
            return;
        }
        FRFont frFont = style.getFRFont();
        if (frFont == null) {
            return;
        }
        setFontSize(Utils.round5(frFont.getSize2D()));
    }
}
