// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.beans.GroupModel;
import com.fr.design.mainframe.widget.editors.DoubleEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.form.ui.container.WSplitLayout;
import com.fr.general.Inter;
import javax.swing.table.*;

public class VerticalSplitProperties
    implements GroupModel
{

    private DefaultTableCellRenderer renderer;
    private PropertyCellEditor editor;
    private WSplitLayout layout;

    public VerticalSplitProperties(WSplitLayout wsplitlayout)
    {
        layout = wsplitlayout;
        renderer = new DefaultTableCellRenderer();
        editor = new PropertyCellEditor(new DoubleEditor());
    }

    public String getGroupName()
    {
        return Inter.getLocText("Vertical-Split_Layout");
    }

    public int getRowCount()
    {
        return 3;
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
                return Inter.getLocText("Ratio");

            case 1: // '\001'
                return Inter.getLocText("Hgap");

            case 2: // '\002'
                return Inter.getLocText("Vgap");
            }
        else
            switch(i)
            {
            case 0: // '\0'
                return Double.valueOf(layout.getRatio());

            case 1: // '\001'
                return Integer.valueOf(layout.getHgap());

            case 2: // '\002'
                return Integer.valueOf(layout.getVgap());
            }
        return null;
    }

    public boolean setValue(Object obj, int i, int j)
    {
        if(j == 0)
            return false;
        switch(i)
        {
        case 0: // '\0'
            double d = 0.5D;
            if(obj != null)
                d = ((Number)obj).doubleValue();
            layout.setRatio(d);
            return true;

        case 1: // '\001'
            int k = 0;
            if(obj != null)
                k = ((Number)obj).intValue();
            layout.setHgap(k);
            return true;

        case 2: // '\002'
            int l = 0;
            if(obj != null)
                l = ((Number)obj).intValue();
            layout.setVgap(l);
            return true;
        }
        return false;
    }

    public boolean isEditable(int i)
    {
        return true;
    }
}
