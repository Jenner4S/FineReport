// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.mainframe.widget.editors.*;
import com.fr.form.ui.container.WFitLayout;
import com.fr.general.Inter;
import javax.swing.table.*;

// Referenced classes of package com.fr.design.designer.properties:
//            FitStateRenderer

public class FRFitLayoutPropertiesGroupModel
    implements GroupModel
{

    private PropertyCellEditor editor;
    private DefaultTableCellRenderer renderer;
    private FitLayoutDirectionEditor stateEditor;
    private FitStateRenderer stateRenderer;
    private WFitLayout layout;
    private XWFitLayout xfl;

    public FRFitLayoutPropertiesGroupModel(XWFitLayout xwfitlayout)
    {
        xfl = xwfitlayout;
        layout = xwfitlayout.toData();
        renderer = new DefaultTableCellRenderer();
        editor = new PropertyCellEditor(new IntegerPropertyEditor());
        stateEditor = new FitLayoutDirectionEditor();
        stateRenderer = new FitStateRenderer();
    }

    public String getGroupName()
    {
        return Inter.getLocText("FR-Designer-Layout_Adaptive_Layout");
    }

    public int getRowCount()
    {
        return 2;
    }

    public TableCellRenderer getRenderer(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            return renderer;
        }
        return stateRenderer;
    }

    public TableCellEditor getEditor(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            return editor;
        }
        return stateEditor;
    }

    public Object getValue(int i, int j)
    {
        if(j == 0)
        {
            switch(i)
            {
            case 0: // '\0'
                return Inter.getLocText("FR-Designer_Component_Interval");
            }
            return Inter.getLocText("FR-Designer_Component_Scale");
        }
        switch(i)
        {
        case 0: // '\0'
            return Integer.valueOf(layout.getCompInterval());
        }
        return Integer.valueOf(layout.getCompState());
    }

    public boolean setValue(Object obj, int i, int j)
    {
        int k = ((Integer)obj).intValue();
        if(j == 0 || k < 0)
            return false;
        if(i == 0 && xfl.canAddInterval(k))
        {
            setLayoutGap(k);
            return true;
        }
        if(i == 1)
        {
            layout.setCompState(k);
            return true;
        } else
        {
            return false;
        }
    }

    private void setLayoutGap(int i)
    {
        int j = layout.getCompInterval();
        if(i != j)
        {
            xfl.moveContainerMargin();
            xfl.moveCompInterval(xfl.getAcualInterval());
            layout.setCompInterval(i);
            xfl.addCompInterval(xfl.getAcualInterval());
        }
    }

    public boolean isEditable(int i)
    {
        return true;
    }
}
