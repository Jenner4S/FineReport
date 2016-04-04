// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.design.form.layout.FRCardLayout;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.form.ui.container.WCardLayout;
import com.fr.general.Inter;
import java.awt.Dimension;
import javax.swing.table.*;

// Referenced classes of package com.fr.design.designer.properties:
//            CardDefaultShowRenderer, CardDefaultShowEditor

public class CardLayoutPropertiesGroupModel
    implements GroupModel
{

    private DefaultTableCellRenderer renderer;
    private PropertyCellEditor editor;
    private CardDefaultShowRenderer defaultShowRenderer;
    private CardDefaultShowEditor defaultShowEditor;
    private WCardLayout layout;
    private XWCardLayout xLayout;

    public CardLayoutPropertiesGroupModel(XWCardLayout xwcardlayout)
    {
        layout = xwcardlayout.toData();
        xLayout = xwcardlayout;
        renderer = new DefaultTableCellRenderer();
        editor = new PropertyCellEditor(new IntegerPropertyEditor());
        defaultShowRenderer = new CardDefaultShowRenderer(layout);
        defaultShowEditor = new CardDefaultShowEditor(layout);
    }

    public String getGroupName()
    {
        return Inter.getLocText("FR-Engine-Tab_Layout_Widget_Size");
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

        case 1: // '\001'
            return renderer;
        }
        return defaultShowRenderer;
    }

    public TableCellEditor getEditor(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            return editor;

        case 1: // '\001'
            return editor;
        }
        return defaultShowEditor;
    }

    public Object getValue(int i, int j)
    {
        if(j == 0)
        {
            switch(i)
            {
            case 0: // '\0'
                return Inter.getLocText("FR-Engine-Tab_Layout_Width");
            }
            return Inter.getLocText("FR-Engine-Tab_Layout_Height");
        }
        switch(i)
        {
        case 0: // '\0'
            return Integer.valueOf((int)xLayout.getSize().getHeight());
        }
        return Integer.valueOf((int)xLayout.getSize().getWidth());
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
            ((FRCardLayout)xLayout.getLayout()).setHgap(k);
            return true;

        case 1: // '\001'
            layout.setVgap(k);
            ((FRCardLayout)xLayout.getLayout()).setVgap(k);
            return true;

        case 2: // '\002'
            layout.setShowIndex(k);
            xLayout.showCard();
            return true;
        }
        return false;
    }

    public boolean isEditable(int i)
    {
        return false;
    }
}
