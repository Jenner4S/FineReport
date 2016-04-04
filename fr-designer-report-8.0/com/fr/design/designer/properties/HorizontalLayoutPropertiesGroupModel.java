// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.XWHorizontalBoxLayout;
import com.fr.design.form.layout.FRHorizontalLayout;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.form.ui.container.WHorizontalBoxLayout;
import com.fr.general.Inter;
import javax.swing.table.*;

// Referenced classes of package com.fr.design.designer.properties:
//            HorizontalAlignmentRenderer, HorizontalAlignmentEditor

public class HorizontalLayoutPropertiesGroupModel
    implements GroupModel
{

    private DefaultTableCellRenderer renderer;
    private PropertyCellEditor editor;
    private HorizontalAlignmentRenderer alignmentRenderer;
    private HorizontalAlignmentEditor alignmentEditor;
    private WHorizontalBoxLayout layout;
    private XWHorizontalBoxLayout wLayout;

    public HorizontalLayoutPropertiesGroupModel(XWHorizontalBoxLayout xwhorizontalboxlayout)
    {
        wLayout = xwhorizontalboxlayout;
        layout = xwhorizontalboxlayout.toData();
        renderer = new DefaultTableCellRenderer();
        editor = new PropertyCellEditor(new IntegerPropertyEditor());
        alignmentRenderer = new HorizontalAlignmentRenderer();
        alignmentEditor = new HorizontalAlignmentEditor();
    }

    public String getGroupName()
    {
        return Inter.getLocText("Layout-HBox");
    }

    public int getRowCount()
    {
        return 3;
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
        return alignmentRenderer;
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
        return alignmentEditor;
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
            return Inter.getLocText("Alignment");
        }
        switch(i)
        {
        case 0: // '\0'
            return Integer.valueOf(layout.getHgap());

        case 1: // '\001'
            return Integer.valueOf(layout.getVgap());
        }
        return Integer.valueOf(layout.getAlignment());
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
            ((FRHorizontalLayout)wLayout.getLayout()).setHgap(k);
            return true;

        case 1: // '\001'
            layout.setVgap(k);
            ((FRHorizontalLayout)wLayout.getLayout()).setVgap(k);
            return true;

        case 2: // '\002'
            layout.setAlignment(k);
            ((FRHorizontalLayout)wLayout.getLayout()).setAlignment(k);
            return true;
        }
        return false;
    }

    public boolean isEditable(int i)
    {
        return true;
    }
}
