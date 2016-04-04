// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.form.ui.DirectWriteEditor;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.Dimension;
import java.beans.IntrospectionException;

// Referenced classes of package com.fr.design.designer.creator:
//            XFieldEditor, CRPropertyDescriptor

public abstract class XDirectWriteEditor extends XFieldEditor
{

    public XDirectWriteEditor(DirectWriteEditor directwriteeditor, Dimension dimension)
    {
        super(directwriteeditor, dimension);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(super.supportedDescriptor(), new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("directEdit", data.getClass())).setI18NName(Inter.getLocText("Form-Allow_Edit"))
        });
    }
}
