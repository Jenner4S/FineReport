// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.widget.editors.*;
import com.fr.design.mainframe.widget.renderer.DictionaryRenderer;
import com.fr.form.ui.CheckBoxGroup;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.Dimension;
import java.beans.IntrospectionException;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.designer.creator:
//            XFieldEditor, CRPropertyDescriptor

public class XCheckBoxGroup extends XFieldEditor
{

    public XCheckBoxGroup(CheckBoxGroup checkboxgroup, Dimension dimension)
    {
        super(checkboxgroup, dimension);
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
            })).setEditorClass(com/fr/design/mainframe/widget/editors/WidgetValueEditor), (new CRPropertyDescriptor("dictionary", data.getClass())).setI18NName(Inter.getLocText("DS-Dictionary")).setEditorClass(com/fr/design/mainframe/widget/editors/DictionaryEditor).setRendererClass(com/fr/design/mainframe/widget/renderer/DictionaryRenderer), (new CRPropertyDescriptor("adaptive", data.getClass())).setI18NName(Inter.getLocText("Adaptive")).putKeyValue("category", "Advanced").setEditorClass(com/fr/design/mainframe/widget/editors/InChangeBooleanEditor), (new CRPropertyDescriptor("chooseAll", data.getClass())).setI18NName(Inter.getLocText(new String[] {
                "Provide", "Choose_All"
            })).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("returnString", data.getClass())).setI18NName(Inter.getLocText("Return-String")).setEditorClass(com/fr/design/mainframe/widget/editors/InChangeBooleanEditor).putKeyValue("category", "Return-Value")
        };
        if(((CheckBoxGroup)toData()).isReturnString())
            acrpropertydescriptor = (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(acrpropertydescriptor, new CRPropertyDescriptor[] {
                (new CRPropertyDescriptor("delimiter", data.getClass())).setI18NName(Inter.getLocText("Form-Delimiter")).putKeyValue("category", "Return-Value"), (new CRPropertyDescriptor("startSymbol", data.getClass())).setI18NName(Inter.getLocText("ComboCheckBox-Start_Symbol")).putKeyValue("category", "Return-Value"), (new CRPropertyDescriptor("endSymbol", data.getClass())).setI18NName(Inter.getLocText("ComboCheckBox-End_Symbol")).putKeyValue("category", "Return-Value")
            });
        if(!((CheckBoxGroup)toData()).isAdaptive())
            acrpropertydescriptor = (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.add(acrpropertydescriptor, (new CRPropertyDescriptor("columnsInRow", data.getClass())).setI18NName(Inter.getLocText("Button-Group-Display-Columns")).putKeyValue("category", "Advanced"));
        return acrpropertydescriptor;
    }

    protected JComponent initEditor()
    {
        if(editor == null)
        {
            editor = FRGUIPaneFactory.createBorderLayout_S_Pane();
            editor.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            UICheckBox uicheckbox = new UICheckBox();
            editor.add(uicheckbox, "West");
            UICheckBox uicheckbox1 = new UICheckBox();
            editor.add(uicheckbox1, "East");
        }
        return editor;
    }

    protected String getIconName()
    {
        return "checkbox_group_16.png";
    }
}
