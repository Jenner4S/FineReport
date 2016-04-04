// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.form.ui.container.WFitLayout;
import com.fr.general.Inter;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JOptionPane;
import javax.swing.table.*;

public class FRFitLayoutConstraints
    implements ConstraintsGroupModel
{

    private static final int MINHEIGHT;
    private static final int ROWNUM = 2;
    private DefaultTableCellRenderer renderer;
    private PropertyCellEditor editor;
    private XCreator xCreator;
    private XWFitLayout parent;

    public FRFitLayoutConstraints(XWFitLayout xwfitlayout, XCreator xcreator)
    {
        parent = xwfitlayout;
        xCreator = xcreator;
        renderer = new DefaultTableCellRenderer();
        editor = new PropertyCellEditor(new IntegerPropertyEditor());
    }

    public String getGroupName()
    {
        return Inter.getLocText("Widget-Size");
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
        {
            switch(i)
            {
            case 0: // '\0'
                return Inter.getLocText("Tree-Width");
            }
            return Inter.getLocText("Tree-Height");
        }
        switch(i)
        {
        case 0: // '\0'
            return Integer.valueOf(xCreator.getWidth());
        }
        return Integer.valueOf(xCreator.getHeight());
    }

    public boolean setValue(Object obj, int i, int j)
    {
        if(j == 1)
        {
            int k = obj != null ? ((Number)obj).intValue() : 0;
            Rectangle rectangle = new Rectangle(xCreator.getBounds());
            switch(i)
            {
            default:
                break;

            case 0: // '\0'
                if(rectangle.width == k)
                    return false;
                rectangle.width = k;
                break;

            case 1: // '\001'
                if(k < MINHEIGHT)
                {
                    JOptionPane.showMessageDialog(null, (new StringBuilder()).append(Inter.getLocText("Min-Height")).append(Integer.toString(MINHEIGHT)).toString());
                    k = xCreator.getHeight();
                }
                if(rectangle.height == k)
                    return false;
                rectangle.height = k;
                break;
            }
            WFitLayout wfitlayout = parent.toData();
            wfitlayout.setBounds(xCreator.toData(), rectangle);
            xCreator.setBounds(rectangle);
            return true;
        } else
        {
            return false;
        }
    }

    public boolean isEditable(int i)
    {
        return false;
    }

    static 
    {
        MINHEIGHT = XCreator.SMALL_PREFERRED_SIZE.height;
    }
}
