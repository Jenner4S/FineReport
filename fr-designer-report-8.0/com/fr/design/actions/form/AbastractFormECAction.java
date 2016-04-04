// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.form;

import com.fr.design.actions.TemplateComponentAction;
import com.fr.design.mainframe.form.FormElementCasePaneDelegate;

public class AbastractFormECAction extends TemplateComponentAction
{

    protected AbastractFormECAction(FormElementCasePaneDelegate formelementcasepanedelegate)
    {
        super(formelementcasepanedelegate);
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        return false;
    }
}
