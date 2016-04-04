// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.XWGridLayout;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.form.ui.container.WGridLayout;
import com.fr.general.Inter;
import java.awt.Container;
import javax.swing.table.*;

public class FRGridLayoutPropertiesGroupModel
    implements GroupModel
{

    private DefaultTableCellRenderer renderer;
    private PropertyCellEditor editor;
    private WGridLayout layout;

    public FRGridLayoutPropertiesGroupModel(Container container)
    {
        layout = ((XWGridLayout)container).toData();
        renderer = new DefaultTableCellRenderer();
        editor = new PropertyCellEditor(new IntegerPropertyEditor());
    }

    public String getGroupName()
    {
        return Inter.getLocText("GridLayout");
    }

    public int getRowCount()
    {
        return 4;
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
        {
            switch(i)
            {
            case 0: // '\0'
                return Inter.getLocText("Hgap");

            case 1: // '\001'
                return Inter.getLocText("Vgap");

            case 2: // '\002'
                return Inter.getLocText("Edit-Row_Count");

            case 3: // '\003'
                return Inter.getLocText("Edit-Column_Count");
            }
            return null;
        }
        switch(i)
        {
        case 0: // '\0'
            return Integer.valueOf(layout.getHgap());

        case 1: // '\001'
            return Integer.valueOf(layout.getVgap());

        case 2: // '\002'
            return Integer.valueOf(layout.getRows());

        case 3: // '\003'
            return Integer.valueOf(layout.getColumns());
        }
        return null;
    }

    public boolean setValue(Object obj, int i, int j)
    {
        if(j == 0)
            return false;
        int k = 0;
        if(obj != null)
            k = ((Number)obj).intValue();
        switch(i)
        {
        case 0: // '\0'
            layout.setHgap(k);
            return true;

        case 1: // '\001'
            layout.setVgap(k);
            return true;

        case 2: // '\002'
            layout.setRows(k);
            return true;

        case 3: // '\003'
            layout.setColumns(k);
            return true;
        }
        return false;
    }

    public boolean isEditable(int i)
    {
        return true;
    }
}
