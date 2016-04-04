// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report.freeze;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

public abstract class FreezeAndRepeatPane extends BasicBeanPane
{

    protected JComponent start;
    protected JComponent end;
    protected UILabel connectionLabel;
    protected boolean isEnalbed;

    public FreezeAndRepeatPane()
    {
    }

    protected void initComponent()
    {
        Dimension dimension = new Dimension(43, 21);
        if(start instanceof UISpinner)
            start.setPreferredSize(dimension);
        if(end instanceof UISpinner)
            end.setPreferredSize(dimension);
        setLayout(FRGUIPaneFactory.createBoxFlowLayout());
        add(start);
        connectionLabel = new UILabel(getLabeshow());
        add(connectionLabel);
        add(end);
    }

    public abstract String getLabeshow();

    public void setEnabled(boolean flag)
    {
        isEnalbed = flag;
        if(start instanceof UISpinner)
            start.setEnabled(flag);
        if(end instanceof UISpinner)
            end.setEnabled(flag);
    }

    public boolean isEnabled()
    {
        return isEnalbed;
    }

    public void addListener(ChangeListener changelistener)
    {
        if(end instanceof UISpinner)
            ((UISpinner)end).addChangeListener(changelistener);
    }
}
