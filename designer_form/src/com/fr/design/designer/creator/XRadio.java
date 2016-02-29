/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.Dimension;
import java.beans.IntrospectionException;

import javax.swing.JComponent;
import javax.swing.JRadioButton;

import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.form.ui.Radio;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;

/**
 * @author richer
 * @since 6.5.3
 */
public class XRadio extends XWidgetCreator {

    public XRadio(Radio widget, Dimension initSize) {
        super(widget, initSize);
    }

    @Override
    public Radio toData() {
        return (Radio)data;
    }

    @Override
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
		return (CRPropertyDescriptor[]) ArrayUtils.addAll(super.supportedDescriptor(), new CRPropertyDescriptor[] {
				new CRPropertyDescriptor("text", this.data.getClass()).setI18NName(Inter.getLocText("Text")),
				new CRPropertyDescriptor("widgetValue", this.data.getClass()).setI18NName(
						Inter.getLocText(new String[]{"Widget", "Value"})).setEditorClass(WidgetValueEditor.class)});
	}

    @Override
    protected JComponent initEditor() {
        if (editor == null) {
            editor = new JRadioButton();
        }
        return editor;
    }

    @Override
    protected void initXCreatorProperties() {
        super.initXCreatorProperties();
        JRadioButton jRadio = (JRadioButton) editor;
        Radio radio = (Radio) data;
        jRadio.setText(radio.getText());
        if(radio.getWidgetValue() != null && radio.getWidgetValue().getValue() instanceof Boolean) {
        	jRadio.setSelected((Boolean) radio.getWidgetValue().getValue());
        }
    }

    @Override
    protected String getIconName() {
        return "radio_button_16.png";
    }
}