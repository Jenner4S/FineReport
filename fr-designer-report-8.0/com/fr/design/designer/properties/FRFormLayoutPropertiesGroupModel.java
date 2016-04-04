// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.beans.GroupModel;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.general.Inter;
import java.awt.Component;
import javax.swing.table.*;

public class FRFormLayoutPropertiesGroupModel
    implements GroupModel
{

    private DefaultTableCellRenderer renderer;
    private PropertyCellEditor editor;

    public FRFormLayoutPropertiesGroupModel(Component component)
    {
        renderer = new DefaultTableCellRenderer();
        editor = new PropertyCellEditor(new IntegerPropertyEditor());
    }

    public String getGroupName()
    {
        return Inter.getLocText("Form-Component_Bounds");
    }

    public int getRowCount()
    {
        return 4;
    }

    public TableCellRenderer getRenderer(int i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TableCellEditor getEditor(int i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getValue(int i, int j)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean setValue(Object obj, int i, int j)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isEditable(int i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
