// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.XWVerticalBoxLayout;
import com.fr.design.form.layout.FRVerticalLayout;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.form.ui.container.WVerticalBoxLayout;
import com.fr.general.Inter;
import javax.swing.table.*;

public class VerticalBoxProperties
    implements GroupModel
{

    private DefaultTableCellRenderer renderer;
    private PropertyCellEditor editor;
    private WVerticalBoxLayout layout;
    private XWVerticalBoxLayout wLayout;

    public VerticalBoxProperties(XWVerticalBoxLayout xwverticalboxlayout)
    {
        wLayout = xwverticalboxlayout;
        layout = xwverticalboxlayout.toData();
        renderer = new DefaultTableCellRenderer();
        editor = new PropertyCellEditor(new IntegerPropertyEditor());
    }

    public String getGroupName()
    {
        return Inter.getLocText("VerticalBoxLayout");
    }

    public int getRowCount()
    {
        return 2;
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
                return Inter.getLocText("Hgap");

            case 1: // '\001'
                return Inter.getLocText("Vgap");
            }
        else
            switch(i)
            {
            case 0: // '\0'
                return Integer.valueOf(layout.getHgap());

            case 1: // '\001'
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
            int k = 0;
            if(obj != null)
                k = ((Number)obj).intValue();
            layout.setHgap(k);
            ((FRVerticalLayout)wLayout.getLayout()).setHgap(k);
            return true;

        case 1: // '\001'
            int l = 0;
            if(obj != null)
                l = ((Number)obj).intValue();
            layout.setVgap(l);
            ((FRVerticalLayout)wLayout.getLayout()).setVgap(l);
            return true;
        }
        return false;
    }

    public boolean isEditable(int i)
    {
        return true;
    }
}
