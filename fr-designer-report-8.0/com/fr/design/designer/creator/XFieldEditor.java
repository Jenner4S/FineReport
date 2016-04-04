// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.mainframe.widget.editors.InChangeBooleanEditor;
import com.fr.form.ui.FieldEditor;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.IntrospectionException;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

// Referenced classes of package com.fr.design.designer.creator:
//            XWidgetCreator, CRPropertyDescriptor

public abstract class XFieldEditor extends XWidgetCreator
{

    protected static final Border FIELDBORDER = BorderFactory.createLineBorder(new Color(128, 152, 186), 1);

    public XFieldEditor(FieldEditor fieldeditor, Dimension dimension)
    {
        super(fieldeditor, dimension);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(super.supportedDescriptor(), getCRPropertyDescriptor());
    }

    private CRPropertyDescriptor[] getCRPropertyDescriptor()
        throws IntrospectionException
    {
        CRPropertyDescriptor crpropertydescriptor = (new CRPropertyDescriptor("allowBlank", data.getClass())).setI18NName(Inter.getLocText("Allow_Blank")).setEditorClass(com/fr/design/mainframe/widget/editors/InChangeBooleanEditor).putKeyValue("category", "Advanced");
        CRPropertyDescriptor crpropertydescriptor1 = (new CRPropertyDescriptor("errorMessage", data.getClass())).setI18NName(Inter.getLocText("Verify-Message")).putKeyValue("category", "Advanced");
        CRPropertyDescriptor crpropertydescriptor2 = (new CRPropertyDescriptor("fontSize", data.getClass(), "getFontSize", "setFontSize")).setI18NName(Inter.getLocText(new String[] {
            "FRFont", "FRFont-Size"
        })).putKeyValue("category", "Advanced");
        return ((FieldEditor)toData()).isAllowBlank() ? (new CRPropertyDescriptor[] {
            crpropertydescriptor, crpropertydescriptor2
        }) : (new CRPropertyDescriptor[] {
            crpropertydescriptor, crpropertydescriptor1, crpropertydescriptor2
        });
    }

}
