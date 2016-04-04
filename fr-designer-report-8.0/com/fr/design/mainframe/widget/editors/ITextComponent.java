// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import java.awt.event.ActionListener;

public interface ITextComponent
{

    public abstract void setText(String s);

    public abstract String getText();

    public abstract void setEditable(boolean flag);

    public abstract void addActionListener(ActionListener actionlistener);

    public abstract void selectAll();

    public abstract void setValue(Object obj);
}
