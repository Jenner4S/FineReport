// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.general.Inter;
import java.awt.Rectangle;
import javax.swing.JOptionPane;
import javax.swing.table.*;

public class BoundsGroupModel
    implements ConstraintsGroupModel
{

    private static final int MINHEIGHT = 21;
    private static final int FOUR = 4;
    private DefaultTableCellRenderer renderer;
    private PropertyCellEditor editor;
    private XCreator component;
    private XWAbsoluteLayout parent;

    public BoundsGroupModel(XWAbsoluteLayout xwabsolutelayout, XCreator xcreator)
    {
        parent = xwabsolutelayout;
        component = xcreator;
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
                return Inter.getLocText("FR-Designer_X_Coordinate");

            case 1: // '\001'
                return Inter.getLocText("FR-Designer_Y_Coordinate");

            case 2: // '\002'
                return Inter.getLocText("FR-Designer_Widget_Width");
            }
            return Inter.getLocText("FR-Designer_Widget_Height");
        }
        switch(i)
        {
        case 0: // '\0'
            return Integer.valueOf(component.getX());

        case 1: // '\001'
            return Integer.valueOf(component.getY());

        case 2: // '\002'
            return Integer.valueOf(component.getWidth());
        }
        return Integer.valueOf(component.getHeight());
    }

    public boolean setValue(Object obj, int i, int j)
    {
        if(j == 1)
        {
            int k = obj != null ? ((Number)obj).intValue() : 0;
            Rectangle rectangle = new Rectangle(component.getBounds());
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
                if(k < 21)
                {
                    JOptionPane.showMessageDialog(null, (new StringBuilder()).append(Inter.getLocText("FR-Designer_Min_Height")).append("21").toString());
                    k = component.getHeight();
                }
                if(rectangle.height == k)
                    return false;
                rectangle.height = k;
                break;
            }
            WAbsoluteLayout wabsolutelayout = parent.toData();
            wabsolutelayout.setBounds(component.toData(), rectangle);
            component.setBounds(rectangle);
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
