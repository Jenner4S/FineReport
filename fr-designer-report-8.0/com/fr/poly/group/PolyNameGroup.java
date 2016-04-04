// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly.group;

import com.fr.design.beans.GroupModel;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.design.mainframe.widget.editors.StringEditor;
import com.fr.general.Inter;
import com.fr.report.poly.TemplateBlock;
import javax.swing.table.*;

public class PolyNameGroup
    implements GroupModel
{

    private TemplateBlock block;
    private DefaultTableCellRenderer defaultTableCellRenderer;
    private PropertyCellEditor defaultCellEditor;

    public PolyNameGroup(TemplateBlock templateblock)
    {
        block = templateblock;
        defaultCellEditor = new PropertyCellEditor(new StringEditor());
        defaultTableCellRenderer = new DefaultTableCellRenderer();
    }

    public String getGroupName()
    {
        return Inter.getLocText("Form-Basic_Properties");
    }

    public int getRowCount()
    {
        return 1;
    }

    public TableCellRenderer getRenderer(int i)
    {
        return defaultTableCellRenderer;
    }

    public TableCellEditor getEditor(int i)
    {
        return defaultCellEditor;
    }

    public Object getValue(int i, int j)
    {
        if(j == 0)
            return Inter.getLocText("Poly_Name");
        else
            return block.getBlockName();
    }

    public boolean setValue(Object obj, int i, int j)
    {
        if(j == 1)
        {
            block.setBlockName((String)obj);
            return true;
        } else
        {
            return false;
        }
    }

    public boolean isEditable(int i)
    {
        return true;
    }
}
