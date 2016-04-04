// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly.group;

import com.fr.base.ScreenResolution;
import com.fr.design.beans.GroupModel;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.general.Inter;
import com.fr.report.poly.*;
import com.fr.stable.unit.UnitRectangle;
import java.awt.Rectangle;
import javax.swing.table.*;

public class PolyBoundsGroup
    implements GroupModel
{

    private static final int DEFAULT_ROW_COUNT = 4;
    private int resolution;
    private DefaultTableCellRenderer renderer;
    private TemplateBlock block;
    private PropertyCellEditor editor;
    private PolyWorkSheet worksheet;

    public PolyBoundsGroup(TemplateBlock templateblock, PolyWorkSheet polyworksheet)
    {
        resolution = ScreenResolution.getScreenResolution();
        block = templateblock;
        worksheet = polyworksheet;
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
        UnitRectangle unitrectangle = block.getBounds();
        Rectangle rectangle = unitrectangle.toRectangle(resolution);
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

    public boolean setValue(Object obj, int i, int j)
    {
        if(j == 1)
        {
            int k = obj != null ? ((Number)obj).intValue() : 0;
            UnitRectangle unitrectangle = block.getBounds();
            Rectangle rectangle = unitrectangle.toRectangle(resolution);
            switch(i)
            {
            case 0: // '\0'
                rectangle.x = k;
                break;

            case 1: // '\001'
                rectangle.y = k;
                break;

            case 2: // '\002'
                rectangle.width = k;
                break;

            case 3: // '\003'
                rectangle.height = k;
                break;
            }
            UnitRectangle unitrectangle1 = new UnitRectangle(new Rectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height), resolution);
            block.setBounds(unitrectangle1, worksheet);
            if(block instanceof AbstractPolyECBlock)
                ((AbstractPolyECBlock)block).reCalculateBlockSize();
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
