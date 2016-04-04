// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class FRBorderLayoutConstraintsRenderer extends UILabel
    implements TableCellRenderer
{

    public FRBorderLayoutConstraintsRenderer()
    {
    }

    public Component getTableCellRendererComponent(JTable jtable, Object obj, boolean flag, boolean flag1, int i, int j)
    {
        if(obj != null)
            setText(Inter.getLocText((new StringBuilder()).append("BorderLayout-").append(obj).toString()));
        return this;
    }
}
