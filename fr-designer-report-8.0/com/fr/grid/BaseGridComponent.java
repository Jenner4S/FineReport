// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid;

import com.fr.design.mainframe.ElementCasePane;
import javax.swing.JComponent;

public abstract class BaseGridComponent extends JComponent
{

    private ElementCasePane reportPane;

    public BaseGridComponent()
    {
        this(null);
    }

    public BaseGridComponent(ElementCasePane elementcasepane)
    {
        setElementCasePane(elementcasepane);
    }

    public void setElementCasePane(ElementCasePane elementcasepane)
    {
        reportPane = elementcasepane;
    }

    public ElementCasePane getElementCasePane()
    {
        return reportPane;
    }
}
