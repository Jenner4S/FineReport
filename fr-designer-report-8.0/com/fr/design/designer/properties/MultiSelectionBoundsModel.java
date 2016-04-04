// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelection;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.general.Inter;
import java.awt.Rectangle;
import javax.swing.table.*;

public class MultiSelectionBoundsModel
    implements ConstraintsGroupModel
{

    private DefaultTableCellRenderer renderer;
    private PropertyCellEditor editor;
    private FormDesigner designer;

    public MultiSelectionBoundsModel(FormDesigner formdesigner)
    {
        designer = formdesigner;
        renderer = new DefaultTableCellRenderer();
        editor = new PropertyCellEditor(new IntegerPropertyEditor());
    }

    public TableCellEditor getEditor(int i)
    {
        return editor;
    }

    public String getGroupName()
    {
        return Inter.getLocText("Form-Component_Bounds");
    }

    public TableCellRenderer getRenderer(int i)
    {
        return renderer;
    }

    public int getRowCount()
    {
        return 4;
    }

    public Object getValue(int i, int j)
    {
        if(j == 0)
        {
            switch(i)
            {
            case 0: // '\0'
                return Inter.getLocText("X-Coordinate");

            case 1: // '\001'
                return Inter.getLocText("Y-Coordinate");

            case 2: // '\002'
                return Inter.getLocText("Tree-Width");
            }
            return Inter.getLocText("Tree-Height");
        }
        Rectangle rectangle = designer.getSelectionModel().getSelection().getSelctionBounds();
        switch(i)
        {
        case 0: // '\0'
            return Integer.valueOf(rectangle.x);

        case 1: // '\001'
            return Integer.valueOf(rectangle.y);

        case 2: // '\002'
            return Integer.valueOf(rectangle.width);
        }
        return Integer.valueOf(rectangle.height);
    }

    public boolean isEditable(int i)
    {
        return true;
    }

    public boolean setValue(Object obj, int i, int j)
    {
        if(j == 1)
        {
            FormSelection formselection = designer.getSelectionModel().getSelection();
            int k = obj != null ? ((Number)obj).intValue() : 0;
            Rectangle rectangle = formselection.getSelctionBounds();
            switch(i)
            {
            default:
                break;

            case 0: // '\0'
                if(rectangle.x == k)
                    return false;
                rectangle.x = k;
                break;

            case 1: // '\001'
                if(rectangle.y == k)
                    return false;
                rectangle.y = k;
                break;

            case 2: // '\002'
                if(rectangle.width == k)
                    return false;
                rectangle.width = k;
                break;

            case 3: // '\003'
                if(rectangle.height == k)
                    return false;
                rectangle.height = k;
                break;
            }
            formselection.backupBounds();
            formselection.setSelectionBounds(rectangle, designer);
            formselection.fixCreator(designer);
            return true;
        } else
        {
            return false;
        }
    }
}
