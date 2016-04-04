// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.widget.DataModify;
import javax.swing.JComponent;

public abstract class AbstractDataModify extends BasicBeanPane
    implements DataModify
{

    public AbstractDataModify()
    {
    }

    public DataCreatorUI dataUI()
    {
        return null;
    }

    public JComponent toSwingComponent()
    {
        return this;
    }
}
