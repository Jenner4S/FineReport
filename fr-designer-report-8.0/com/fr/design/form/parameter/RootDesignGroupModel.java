// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.form.parameter;

import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.form.ui.container.WParameterLayout;
import com.fr.general.Inter;
import java.awt.Component;
import javax.swing.table.*;

public class RootDesignGroupModel
    implements ConstraintsGroupModel
{

    private DefaultTableCellRenderer renderer;
    private PropertyCellEditor editor;
    private XWParameterLayout root;

    public RootDesignGroupModel(XWParameterLayout xwparameterlayout)
    {
        root = xwparameterlayout;
        renderer = new DefaultTableCellRenderer();
        editor = new PropertyCellEditor(new IntegerPropertyEditor());
        if(xwparameterlayout.toData().getDesignWidth() == 0)
            xwparameterlayout.toData().setDesignWidth(xwparameterlayout.getWidth());
    }

    public String getGroupName()
    {
        return Inter.getLocText("Form-Design_Size");
    }

    public int getRowCount()
    {
        return 1;
    }

    public TableCellRenderer getRenderer(int i)
    {
        return renderer;
    }

    public TableCellEditor getEditor(int i)
    {
        return editor;
    }

    public Object getValue(int i, int j)
    {
        if(j == 0)
            switch(i)
            {
            case 0: // '\0'
                return Inter.getLocText("Form-Desin_Width");
            }
        else
            switch(i)
            {
            case 0: // '\0'
                return Integer.valueOf(root.toData().getDesignWidth());
            }
        return null;
    }

    public boolean setValue(Object obj, int i, int j)
    {
        if(j == 1)
        {
            int k = obj != null ? ((Number)obj).intValue() : 0;
            switch(i)
            {
            case 0: // '\0'
                if(isCompsOutOfDesignerWidth(k))
                {
                    return false;
                } else
                {
                    root.toData().setDesignWidth(k);
                    return true;
                }
            }
            return true;
        } else
        {
            return false;
        }
    }

    private boolean isCompsOutOfDesignerWidth(int i)
    {
        for(int j = 0; j < root.getComponentCount(); j++)
        {
            Component component = root.getComponent(j);
            if(component.getX() + component.getWidth() > i)
                return true;
        }

        return false;
    }

    public boolean isEditable(int i)
    {
        return true;
    }
}
