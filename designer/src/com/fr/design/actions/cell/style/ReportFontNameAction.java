/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.cell.style;

import java.awt.Dimension;

import javax.swing.ComboBoxModel;
import javax.swing.JComponent;

import com.fr.base.Style;
import com.fr.base.Utils;
import com.fr.base.core.StyleUtils;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;

/**
 * Font name..
 */
public class ReportFontNameAction extends AbstractStyleAction {
	public ReportFontNameAction(ElementCasePane t) {
		super(t);
		
        this.setName(Inter.getLocText("FRFont-Family"));
    }

    @Override
	public Style executeStyle(Style style, Style defStyle) {
        Object object = this.getValue(UIComboBox.class.getName());
        if (object != null && object instanceof UIComboBox) {
            String selectedFontName = (String) ((UIComboBox) object).getSelectedItem();
            //������if ������˼�ǣ������������cellStyle.getFRFont() ����Ϊ��Ҳ��ԭ���ı�ѡ�����ͬ���򲻸ı䡣
            // �����ж��Ƿ�Ϊ��,����ճ���
            //Ȼ�����ǵ����;�ת��ΪdelCellStyle��ͬ��Ȼ���ٽ�֮�ж��Ƿ���ѡ�����ͬ���粻ͬ��ת��Ϊ��ͬ��
            if (ComparatorUtils.equals(style.getFRFont().getName(), defStyle.getFRFont().getName())) {
            	style = StyleUtils.setReportFontName(style, defStyle.getFRFont().getName());
            }
            if (ComparatorUtils.equals(selectedFontName, style.getFRFont().getName())) {
                return style;
            }
            style = StyleUtils.setReportFontName(style, selectedFontName);
        }

        return style;
    }

    public void setFontName(String fontName) {
        Object object = this.getValue(UIComboBox.class.getName());
        if (object != null && object instanceof UIComboBox) {
            UIComboBox comboBox = (UIComboBox) object;
            ComboBoxModel comboBoxModel = comboBox.getModel();
            for (int i = 0; i < comboBoxModel.getSize(); i++) {
                Object item = comboBoxModel.getElementAt(i);

                if (ComparatorUtils.equals(item, fontName)) {
                    //������Font Name
                    comboBox.removeActionListener(this);
                    comboBox.setSelectedIndex(i);
                    comboBox.addActionListener(this);
                    break;
                }
            }
        }
    }

    @Override
	public JComponent createToolBarComponent() {
        Object object = this.getValue(UIComboBox.class.getName());
        if (object == null || !(object instanceof UIComboBox)) {
            UIComboBox itemComponent = new UIComboBox(Utils.getAvailableFontFamilyNames4Report());
            this.putValue(UIComboBox.class.getName(), itemComponent);
            //������ѿ��.
            itemComponent.setPreferredSize(new Dimension(
                    Math.min(140, itemComponent.getPreferredSize().width),
                    itemComponent.getPreferredSize().height));

            itemComponent.setEnabled(this.isEnabled());
            itemComponent.addActionListener(this);

            return itemComponent;
        }

        return (UIComboBox) object;
    }

    /**
     * Update Style.
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

        this.setFontName(frFont.getFamily());
    }
}
