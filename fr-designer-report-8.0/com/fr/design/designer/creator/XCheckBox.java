// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.form.ui.CheckBox;
import com.fr.form.ui.WidgetValue;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.core.PropertyChangeAdapter;
import java.awt.Dimension;
import java.beans.IntrospectionException;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.designer.creator:
//            XWidgetCreator, CRPropertyDescriptor

public class XCheckBox extends XWidgetCreator
{

    public XCheckBox(CheckBox checkbox, Dimension dimension)
    {
        super(checkbox, dimension);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(super.supportedDescriptor(), new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("text", data.getClass())).setI18NName(Inter.getLocText("Text")).setPropertyChangeListener(new PropertyChangeAdapter() {

                final XCheckBox this$0;

                public void propertyChange()
                {
                    ((UICheckBox)editor).setText(((CheckBox)data).getText());
                }

            
            {
                this$0 = XCheckBox.this;
                super();
            }
            }
), (new CRPropertyDescriptor("widgetValue", data.getClass())).setI18NName(Inter.getLocText(new String[] {
                "Widget", "Value"
            })).setEditorClass(com/fr/design/mainframe/widget/editors/WidgetValueEditor).setPropertyChangeListener(new PropertyChangeAdapter() {

                final XCheckBox this$0;

                public void propertyChange()
                {
                    WidgetValue widgetvalue = ((CheckBox)data).getWidgetValue();
                    if(widgetvalue != null && (widgetvalue.getValue() instanceof Boolean))
                        ((UICheckBox)editor).setSelected(((Boolean)widgetvalue.getValue()).booleanValue());
                }

            
            {
                this$0 = XCheckBox.this;
                super();
            }
            }
), (new CRPropertyDescriptor("fontSize", data.getClass(), "getFontSize", "setFontSize")).setI18NName(Inter.getLocText(new String[] {
                "FRFont", "FRFont-Size"
            })).putKeyValue("category", "Advanced")
        });
    }

    protected JComponent initEditor()
    {
        if(editor == null)
        {
            editor = new UICheckBox();
            editor.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        }
        return editor;
    }

    protected void initXCreatorProperties()
    {
        super.initXCreatorProperties();
        UICheckBox uicheckbox = (UICheckBox)editor;
        CheckBox checkbox = (CheckBox)data;
        uicheckbox.setText(checkbox.getText());
        if(checkbox.getWidgetValue() != null && (checkbox.getWidgetValue().getValue() instanceof Boolean))
            uicheckbox.setSelected(((Boolean)checkbox.getWidgetValue().getValue()).booleanValue());
    }

    protected String getIconName()
    {
        return "check_box_16.png";
    }
}
