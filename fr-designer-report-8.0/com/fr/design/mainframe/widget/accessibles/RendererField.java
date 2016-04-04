// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.mainframe.widget.editors.ITextComponent;
import com.fr.design.mainframe.widget.renderer.GenericCellRenderer;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import javax.swing.JComponent;

public class RendererField extends JComponent
    implements ITextComponent
{

    private GenericCellRenderer renderer;

    public RendererField(GenericCellRenderer genericcellrenderer)
    {
        setLayout(new BorderLayout());
        renderer = genericcellrenderer;
        add(genericcellrenderer, "Center");
    }

    public void addActionListener(ActionListener actionlistener)
    {
    }

    public String getText()
    {
        return null;
    }

    public void selectAll()
    {
    }

    public void setEditable(boolean flag)
    {
        setEnabled(flag);
    }

    public void setText(String s)
    {
    }

    public void setValue(Object obj)
    {
        renderer.setValue(obj);
        repaint();
    }
}
