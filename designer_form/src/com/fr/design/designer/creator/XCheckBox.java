/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.Dimension;
import java.beans.IntrospectionException;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import com.fr.design.form.util.XCreatorConstants;
import com.fr.stable.core.PropertyChangeAdapter;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.form.ui.CheckBox;
import com.fr.form.ui.WidgetValue;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;

/**
 * @author richer
 * @since 6.5.3
 */
public class XCheckBox extends XWidgetCreator {
	
    public XCheckBox(CheckBox widget, Dimension initSize) {
        super(widget, initSize);
    }

    @Override
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
		return (CRPropertyDescriptor[]) ArrayUtils.addAll(super.supportedDescriptor(), new CRPropertyDescriptor[] {
				new CRPropertyDescriptor("text", this.data.getClass()).setI18NName(Inter.getLocText("Text"))
						.setPropertyChangeListener(new PropertyChangeAdapter() {

							@Override
							public void propertyChange() {
								((UICheckBox) editor).setText(((CheckBox) data).getText());
							}
						}),
				new CRPropertyDescriptor("widgetValue", this.data.getClass()).setI18NName(
						Inter.getLocText(new String[]{"Widget", "Value"})).setEditorClass(WidgetValueEditor.class)
						.setPropertyChangeListener(new PropertyChangeAdapter() {

							@Override
							public void propertyChange() {
								WidgetValue value = ((CheckBox) data).getWidgetValue();
								if (value != null && value.getValue() instanceof Boolean) {
									((UICheckBox) editor).setSelected((Boolean) value.getValue());
								}
							}
						}),
				new CRPropertyDescriptor("fontSize", this.data.getClass(), "getFontSize", "setFontSize")
						.setI18NName(Inter.getLocText(new String[]{"FRFont", "FRFont-Size"}))
						.putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced")
		});
	}

    @Override
    protected JComponent initEditor() {
        if (editor == null) {
            editor = new UICheckBox();
			editor.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        }
        return editor;
    }

    @Override
    protected void initXCreatorProperties() {
		super.initXCreatorProperties();
		UICheckBox jCheckBox = (UICheckBox) editor;
		CheckBox check = (CheckBox) data;
		jCheckBox.setText(check.getText());
		if (check.getWidgetValue() != null && check.getWidgetValue().getValue() instanceof Boolean) {
			jCheckBox.setSelected((Boolean) check.getWidgetValue().getValue());
		}
	}

    @Override
    protected String getIconName() {
        return "check_box_16.png";
    }
}