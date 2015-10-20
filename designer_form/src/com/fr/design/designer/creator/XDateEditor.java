/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.IntrospectionException;
import java.util.Date;

import javax.swing.JComponent;

import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.widget.editors.DateFormatEditor;
import com.fr.design.mainframe.widget.editors.DateRangeEditor;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.design.mainframe.widget.renderer.DateCellRenderer;
import com.fr.form.ui.DateEditor;
import com.fr.form.ui.WidgetValue;
import com.fr.general.DateUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeAdapter;

/**
 * @author richer
 * @since 6.5.3
 */
public class XDateEditor extends XDirectWriteEditor {
	
	private UITextField textField;
	private LimpidButton btn;

    public XDateEditor(DateEditor widget, Dimension initSize) {
        super(widget, initSize);
    }
    
    /**
     * �ؼ��������б�
     * @return �˿ؼ����õ������б�
     * @throws IntrospectionException �쳣
     */
    @Override
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
		return (CRPropertyDescriptor[]) ArrayUtils.addAll(super.supportedDescriptor(),
				new CRPropertyDescriptor[] {
						new CRPropertyDescriptor("widgetValue", this.data.getClass()).setI18NName(
								Inter.getLocText(new String[]{"Widget", "Value"})).setEditorClass(
								WidgetValueEditor.class).setPropertyChangeListener(new PropertyChangeAdapter() {
									
									@Override
									public void propertyChange() {
										initFieldText();
									}
								}),
						new CRPropertyDescriptor("formatText", this.data.getClass()).setI18NName(
								Inter.getLocText("FR-Engine_Format")).setEditorClass(formatClass()).setRendererClass(
								DateCellRenderer.class).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
						new CRPropertyDescriptor("startDate", this.data.getClass()).setI18NName(
								Inter.getLocText("FR-Designer_Start-Date")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
								"Advanced").setEditorClass(DateRangeEditor.class),
						new CRPropertyDescriptor("endDate", this.data.getClass()).setI18NName(
								Inter.getLocText("FR-Designer_End-Date")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
								"Advanced").setEditorClass(DateRangeEditor.class),
						new CRPropertyDescriptor("returnDate", this.data.getClass()).setI18NName(
								Inter.getLocText("FR-Designer_Return-Date")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
								"Return-Value"),
						new CRPropertyDescriptor("waterMark", this.data.getClass()).setI18NName(
								Inter.getLocText("FR-Designer_WaterMark")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
								"Advanced")
				});
	}
    
    protected Class formatClass() {
    	return DateFormatEditor.class;
    }
    
	private void initFieldText() {
		DateEditor dateEditor = (DateEditor) data;
		if (dateEditor.getWidgetValue() != null) {
			WidgetValue widgetValue = dateEditor.getWidgetValue();
			//�ؼ�ֵ.toString
			String valueStr = widgetValue.toString();
			//�ؼ�ֵ
			Object value = widgetValue.getValue();
			//��ʽ
			String format = dateEditor.getFormatText();
			
			if(value instanceof Date){
				valueStr = DateUtils.getDate2Str(format, (Date) value);
			} 
			
			//���ڿؼ�Ĭ��ֵ
			if(StringUtils.isEmpty(valueStr)){
				valueStr = DateUtils.getDate2Str(format, new Date());
				dateEditor.setWidgetValue(new WidgetValue(new Date()));
			}
			
			textField.setText(valueStr);
		}
	}

	@Override
	protected void initXCreatorProperties() {
		super.initXCreatorProperties();
		initFieldText();
	}

    @Override
    protected JComponent initEditor() {
        if (editor == null) {
            editor = FRGUIPaneFactory.createBorderLayout_S_Pane();
            editor.add(textField = new UITextField(5), BorderLayout.CENTER);
			btn = new LimpidButton("", this.getIconPath(), toData().isVisible() ? FULL_OPACITY : HALF_OPACITY);
            btn.setPreferredSize(new Dimension(21, 21));
            editor.add(btn, BorderLayout.EAST);
            textField.setOpaque(false);
            editor.setBackground(Color.WHITE);
        }
        return editor;
    }

    @Override
    protected String getIconName() {
        return "date_16.png";
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
