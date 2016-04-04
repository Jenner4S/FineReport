// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget;

import com.fr.design.data.DataCreatorUI;
import javax.swing.JComponent;

public interface DataModify
{

    public abstract void populateBean(Object obj);

    public abstract Object updateBean();

    public abstract void checkValid()
        throws Exception;

    public abstract DataCreatorUI dataUI();

    public abstract JComponent toSwingComponent();
}
