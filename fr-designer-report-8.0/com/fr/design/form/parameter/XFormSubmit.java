// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.form.parameter;

import com.fr.design.designer.creator.XButton;
import com.fr.design.designer.creator.XCreator;
import com.fr.form.parameter.FormSubmitButton;
import java.awt.Dimension;

public class XFormSubmit extends XButton
{

    public XFormSubmit(FormSubmitButton formsubmitbutton, Dimension dimension)
    {
        super(formsubmitbutton, dimension);
    }

    public boolean SearchQueryCreators(XCreator xcreator)
    {
        return true;
    }

    public boolean canEnterIntoAdaptPane()
    {
        return false;
    }

    protected String getIconName()
    {
        return "preview_16.png";
    }
}
