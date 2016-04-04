// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.mainframe.widget.editors.InChangeBooleanEditor;
import com.fr.form.ui.ComboCheckBox;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.Dimension;
import java.beans.IntrospectionException;

// Referenced classes of package com.fr.design.designer.creator:
//            XComboBox, CRPropertyDescriptor

public class XComboCheckBox extends XComboBox
{

    public XComboCheckBox(ComboCheckBox combocheckbox, Dimension dimension)
    {
        super(combocheckbox, dimension);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(super.supportedDescriptor(), ((ComboCheckBox)toData()).isReturnString() ? ((Object []) (new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("delimiter", data.getClass())).setI18NName(Inter.getLocText("Form-Delimiter")).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("returnString", data.getClass())).setI18NName(Inter.getLocText("Return-String")).setEditorClass(com/fr/design/mainframe/widget/editors/InChangeBooleanEditor).putKeyValue("category", "Return-Value"), (new CRPropertyDescriptor("startSymbol", data.getClass())).setI18NName(Inter.getLocText("ComboCheckBox-Start_Symbol")).putKeyValue("category", "Return-Value"), (new CRPropertyDescriptor("endSymbol", data.getClass())).setI18NName(Inter.getLocText("ComboCheckBox-End_Symbol")).putKeyValue("category", "Return-Value")
        })) : ((Object []) (new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("supportTag", data.getClass())).setI18NName(Inter.getLocText("Form-SupportTag")).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("delimiter", data.getClass())).setI18NName(Inter.getLocText("Form-Delimiter")).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("returnString", data.getClass())).setEditorClass(com/fr/design/mainframe/widget/editors/InChangeBooleanEditor).setI18NName(Inter.getLocText("Return-String")).putKeyValue("category", "Return-Value")
        })));
    }

    protected String getIconName()
    {
        return "combo_check_16.png";
    }
}
