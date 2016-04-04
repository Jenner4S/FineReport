// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.form.ui.FreeButton;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import java.awt.Component;
import javax.swing.table.*;

// Referenced classes of package com.fr.design.designer.properties:
//            LayoutConstraintsEditor

public abstract class HVLayoutConstraints
    implements ConstraintsGroupModel
{

    protected DefaultTableCellRenderer renderer;
    protected LayoutConstraintsEditor editor1;
    protected PropertyCellEditor editor2;
    protected Widget widget;
    protected XLayoutContainer parent;

    public HVLayoutConstraints(XLayoutContainer xlayoutcontainer, Component component)
    {
        parent = xlayoutcontainer;
        widget = ((XWidgetCreator)component).toData();
        renderer = new DefaultTableCellRenderer();
        editor2 = new PropertyCellEditor(new IntegerPropertyEditor());
    }

    public String getGroupName()
    {
        return Inter.getLocText("Layout_Constraints");
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
        if(i == 0)
            return editor1;
        else
            return editor2;
    }

    public boolean isEditable(int i)
    {
        if(i == 1)
            return !(widget instanceof FreeButton) || !((FreeButton)widget).isCustomStyle();
        else
            return true;
    }
}
