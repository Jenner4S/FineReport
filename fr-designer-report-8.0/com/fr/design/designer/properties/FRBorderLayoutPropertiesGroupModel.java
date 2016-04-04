// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.design.designer.properties.items.Item;
import com.fr.design.form.layout.FRBorderLayout;
import com.fr.design.mainframe.widget.editors.*;
import com.fr.design.mainframe.widget.renderer.PropertyCellRenderer;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.general.Inter;
import javax.swing.table.*;

public class FRBorderLayoutPropertiesGroupModel
    implements GroupModel
{

    private DefaultTableCellRenderer renderer;
    private PropertyCellEditor gapEditor;
    private PropertyCellEditor directionEditor;
    private PropertyCellRenderer directionRenderer;
    private WBorderLayout layout;
    private XWBorderLayout xbl;

    public FRBorderLayoutPropertiesGroupModel(XWBorderLayout xwborderlayout)
    {
        xbl = xwborderlayout;
        layout = xwborderlayout.toData();
        renderer = new DefaultTableCellRenderer();
        gapEditor = new PropertyCellEditor(new IntegerPropertyEditor());
        BorderLayoutDirectionEditor borderlayoutdirectioneditor = new BorderLayoutDirectionEditor();
        directionEditor = new PropertyCellEditor(borderlayoutdirectioneditor);
        directionRenderer = new PropertyCellRenderer(borderlayoutdirectioneditor);
    }

    public String getGroupName()
    {
        return Inter.getLocText("BorderLayout");
    }

    public int getRowCount()
    {
        return 3;
    }

    public TableCellRenderer getRenderer(int i)
    {
        if(i == 2)
            return directionRenderer;
        else
            return renderer;
    }

    public TableCellEditor getEditor(int i)
    {
        if(i == 2)
            return directionEditor;
        else
            return gapEditor;
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
            }
            return Inter.getLocText(new String[] {
                "Form-Layout", "Style"
            });
        }
        switch(i)
        {
        case 0: // '\0'
            return Integer.valueOf(layout.getHgap());

        case 1: // '\001'
            return Integer.valueOf(layout.getVgap());
        }
        return layout.getDirections();
    }

    public boolean setValue(Object obj, int i, int j)
    {
        if(j == 0)
            return false;
        if(i == 0 || i == 1)
        {
            int k = 0;
            if(obj != null)
                k = ((Number)obj).intValue();
            switch(i)
            {
            case 0: // '\0'
                layout.setHgap(k);
                ((FRBorderLayout)xbl.getLayout()).setHgap(k);
                return true;

            case 1: // '\001'
                layout.setVgap(k);
                ((FRBorderLayout)xbl.getLayout()).setVgap(k);
                return true;
            }
        } else
        if(i == 2)
        {
            String as[] = null;
            if(obj instanceof Object[])
            {
                as = new String[((Object[])(Object[])obj).length];
                int l = 0;
                for(int i1 = as.length; l < i1; l++)
                    as[l] = (String)((Item)((Object[])(Object[])obj)[l]).getValue();

            }
            layout.setDirections(as);
            xbl.convert();
            return true;
        }
        return false;
    }

    public boolean isEditable(int i)
    {
        return true;
    }
}
