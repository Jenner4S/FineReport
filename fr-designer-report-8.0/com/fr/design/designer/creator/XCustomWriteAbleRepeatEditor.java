// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.form.ui.WriteAbleRepeatEditor;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.Dimension;
import java.beans.IntrospectionException;

// Referenced classes of package com.fr.design.designer.creator:
//            XWriteAbleRepeatEditor, CRPropertyDescriptor

public abstract class XCustomWriteAbleRepeatEditor extends XWriteAbleRepeatEditor
{

    public XCustomWriteAbleRepeatEditor(WriteAbleRepeatEditor writeablerepeateditor, Dimension dimension)
    {
        super(writeablerepeateditor, dimension);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(super.supportedDescriptor(), new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("customData", data.getClass())).setI18NName(Inter.getLocText("Form-Allow_CustomData"))
        });
    }
}
