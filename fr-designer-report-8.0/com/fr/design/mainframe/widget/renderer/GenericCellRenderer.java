// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.renderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

public abstract class GenericCellRenderer extends JComponent
    implements TableCellRenderer
{

    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    public GenericCellRenderer()
    {
    }

    public Component getTableCellRendererComponent(JTable jtable, Object obj, boolean flag, boolean flag1, int i, int j)
    {
        Component component = getRendererComponent();
        Color color = null;
        Color color1 = null;
        javax.swing.JTable.DropLocation droplocation = jtable.getDropLocation();
        if(droplocation != null && !droplocation.isInsertRow() && !droplocation.isInsertColumn() && droplocation.getRow() == i && droplocation.getColumn() == j)
        {
            color = UIManager.getColor("Table.dropCellForeground");
            color1 = UIManager.getColor("Table.dropCellBackground");
            flag = true;
        }
        if(flag)
        {
            component.setForeground(color != null ? color : jtable.getSelectionForeground());
            component.setBackground(color1 != null ? color1 : jtable.getSelectionBackground());
        } else
        {
            component.setForeground(jtable.getForeground());
            component.setBackground(jtable.getBackground());
        }
        component.setFont(jtable.getFont());
        if(flag1)
        {
            Border border = null;
            if(flag)
                border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
            if(border == null)
                border = UIManager.getBorder("Table.focusCellHighlightBorder");
            ((JComponent)component).setBorder(border);
            if(!flag && jtable.isCellEditable(i, j))
            {
                Color color2 = UIManager.getColor("Table.focusCellForeground");
                if(color2 != null)
                    component.setForeground(color2);
                color2 = UIManager.getColor("Table.focusCellBackground");
                if(color2 != null)
                    component.setBackground(color2);
            }
        }
        ((JComponent)component).setBorder(getNoFocusBorder());
        setValue(obj);
        return component;
    }

    private static Border getNoFocusBorder()
    {
        if(System.getSecurityManager() != null)
            return SAFE_NO_FOCUS_BORDER;
        else
            return noFocusBorder;
    }

    public Component getRendererComponent()
    {
        return this;
    }

    public abstract void setValue(Object obj);

}
