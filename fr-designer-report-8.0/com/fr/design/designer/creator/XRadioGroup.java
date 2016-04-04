// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.widget.editors.*;
import com.fr.design.mainframe.widget.renderer.DictionaryRenderer;
import com.fr.form.ui.RadioGroup;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.Dimension;
import java.beans.IntrospectionException;
import javax.swing.*;

// Referenced classes of package com.fr.design.designer.creator:
//            XFieldEditor, CRPropertyDescriptor

public class XRadioGroup extends XFieldEditor
{

    public XRadioGroup(RadioGroup radiogroup, Dimension dimension)
    {
        super(radiogroup, dimension);
    }

    public RadioGroup toData()
    {
        return (RadioGroup)data;
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(super.supportedDescriptor(), getCRPropertyDescriptor());
    }

    private CRPropertyDescriptor[] getCRPropertyDescriptor()
        throws IntrospectionException
    {
        CRPropertyDescriptor acrpropertydescriptor[] = {
            (new CRPropertyDescriptor("widgetValue", data.getClass())).setI18NName(Inter.getLocText(new String[] {
                "Widget", "Value"
            })).setEditorClass(com/fr/design/mainframe/widget/editors/WidgetValueEditor), (new CRPropertyDescriptor("dictionary", data.getClass())).setI18NName(Inter.getLocText("DS-Dictionary")).setEditorClass(com/fr/design/mainframe/widget/editors/DictionaryEditor).setRendererClass(com/fr/design/mainframe/widget/renderer/DictionaryRenderer), (new CRPropertyDescriptor("adaptive", data.getClass())).setI18NName(Inter.getLocText("Adaptive")).putKeyValue("category", "Advanced").setEditorClass(com/fr/design/mainframe/widget/editors/InChangeBooleanEditor)
        };
        if(!toData().isAdaptive())
            acrpropertydescriptor = (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.add(acrpropertydescriptor, (new CRPropertyDescriptor("columnsInRow", data.getClass())).setI18NName(Inter.getLocText("Button-Group-Display-Columns")).putKeyValue("category", "Advanced"));
        return acrpropertydescriptor;
    }

    protected JComponent initEditor()
    {
        if(editor == null)
        {
            editor = FRGUIPaneFactory.createBorderLayout_S_Pane();
            editor.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            ButtonGroup buttongroup = new ButtonGroup();
            JRadioButton jradiobutton = new JRadioButton();
            jradiobutton.setSelected(true);
            JRadioButton jradiobutton1 = new JRadioButton();
            buttongroup.add(jradiobutton);
            buttongroup.add(jradiobutton1);
            editor.add(jradiobutton, "West");
            editor.add(jradiobutton1, "East");
        }
        return editor;
    }

    protected String getIconName()
    {
        return "button_group_16.png";
    }

    public volatile Widget toData()
    {
        return toData();
    }
}
