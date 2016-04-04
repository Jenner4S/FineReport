// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.actions;

import com.fr.design.actions.TemplateComponentAction;
import com.fr.design.mainframe.FormDesigner;

public abstract class FormEditAction extends TemplateComponentAction
{

    protected FormEditAction(FormDesigner formdesigner)
    {
        super(formdesigner);
    }

    public void update()
    {
        setEnabled(true);
    }
}
