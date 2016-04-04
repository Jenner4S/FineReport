// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.form.ui.*;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.Dimension;
import java.beans.IntrospectionException;
import javax.swing.JComponent;
import javax.swing.JRadioButton;

// Referenced classes of package com.fr.design.designer.creator:
//            XWidgetCreator, CRPropertyDescriptor

public class XRadio extends XWidgetCreator
{

    public XRadio(Radio radio, Dimension dimension)
    {
        super(radio, dimension);
    }

    public Radio toData()
    {
        return (Radio)data;
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(super.supportedDescriptor(), new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("text", data.getClass())).setI18NName(Inter.getLocText("Text")), (new CRPropertyDescriptor("widgetValue", data.getClass())).setI18NName(Inter.getLocText(new String[] {
                "Widget", "Value"
            })).setEditorClass(com/fr/design/mainframe/widget/editors/WidgetValueEditor)
        });
    }

    protected JComponent initEditor()
    {
        if(editor == null)
            editor = new JRadioButton();
        return editor;
    }

    protected void initXCreatorProperties()
    {
        super.initXCreatorProperties();
        JRadioButton jradiobutton = (JRadioButton)editor;
        Radio radio = (Radio)data;
        jradiobutton.setText(radio.getText());
        if(radio.getWidgetValue() != null && (radio.getWidgetValue().getValue() instanceof Boolean))
            jradiobutton.setSelected(((Boolean)radio.getWidgetValue().getValue()).booleanValue());
    }

    protected String getIconName()
    {
        return "radio_button_16.png";
    }

    public volatile Widget toData()
    {
        return toData();
    }
}
