/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.IntrospectionException;

import javax.swing.JComponent;

import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.widget.editors.DictionaryEditor;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.design.mainframe.widget.renderer.DictionaryRenderer;
import com.fr.form.ui.ComboBox;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;

/**
 * @author richer
 * @since 6.5.3
 */
public class XComboBox extends XCustomWriteAbleRepeatEditor {
	LimpidButton btn;

    public XComboBox(ComboBox widget, Dimension initSize) {
        super(widget, initSize);
    }

    /**
     * �ؼ��������б�
     * @return �˿ؼ����õ������б�
     * @throws IntrospectionException �쳣
     */
    @Override
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        return (CRPropertyDescriptor[]) ArrayUtils.addAll(
            super.supportedDescriptor(),
            new CRPropertyDescriptor[]{
            	new CRPropertyDescriptor("widgetValue", this.data.getClass()).setI18NName(Inter.getLocText(new String[]{"Widget", "Value"})).setEditorClass(WidgetValueEditor.class),
                new CRPropertyDescriptor("dictionary", this.data.getClass()).setI18NName(Inter.getLocText("FR-Designer_DS-Dictionary")).setEditorClass(DictionaryEditor.class).setRendererClass(DictionaryRenderer.class)
            });
    }

    @Override
    protected JComponent initEditor() {
        if (editor == null) {
            editor = FRGUIPaneFactory.createBorderLayout_S_Pane();
            UITextField textField = new UITextField(5);
            textField.setOpaque(false);
            editor.add(textField, BorderLayout.CENTER);
			btn = new LimpidButton("", this.getIconPath(), toData().isVisible() ? FULL_OPACITY : HALF_OPACITY);
            btn.setPreferredSize(new Dimension(21, 21));
            btn.setOpaque(true);
            editor.add(btn, BorderLayout.EAST);
            editor.setBackground(Color.WHITE);
        }
        return editor;
    }

    @Override
    protected String getIconName() {
        return "combo_box_16.png";
    }

	protected void makeVisible(boolean visible) {
		btn.makeVisible(visible);
	}
	
	/**
	 * ��ȡ��ǰXCreator��һ����װ������
	 * 
	 * @param widgetName ��ǰ�����
	 * 
	 * @return ��װ�ĸ�����
	 * 
	 *
	 * @date 2014-11-25-����4:47:23
	 * 
	 */
	protected XLayoutContainer getCreatorWrapper(String widgetName){
		return new XWScaleLayout();
	}
	
	/**
	 * ����ǰ������ӵ���������
	 * 
	 * @param parentPanel ���������
	 * 
	 *
	 * @date 2014-11-25-����4:57:55
	 * 
	 */
	protected void addToWrapper(XLayoutContainer parentPanel, int width, int minHeight){			
		this.setSize(width, minHeight);
		parentPanel.add(this);
	}
	
	/**
	 * �˿ؼ�������Ӧ����Ҫ����ԭ���߶�
	 * 
	 * @return ���򷵻�true
	 */
	@Override
	public boolean shouldScaleCreator() {
		return true;
	}

}
