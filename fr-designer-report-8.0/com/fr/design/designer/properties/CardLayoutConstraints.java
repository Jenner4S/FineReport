// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WCardLayout;
import com.fr.general.Inter;
import java.awt.Component;
import javax.swing.table.*;

// Referenced classes of package com.fr.design.designer.properties:
//            FRCardConstraintsEditor

public class CardLayoutConstraints
    implements ConstraintsGroupModel
{

    private DefaultTableCellRenderer renderer;
    private FRCardConstraintsEditor editor1;
    private Widget widget;
    private WCardLayout layout;
    private XWCardLayout container;

    public CardLayoutConstraints(XWCardLayout xwcardlayout, Component component)
    {
        layout = xwcardlayout.toData();
        container = xwcardlayout;
        widget = ((XWidgetCreator)component).toData();
        editor1 = new FRCardConstraintsEditor(layout);
        renderer = new DefaultTableCellRenderer();
    }

    public String getGroupName()
    {
        return Inter.getLocText("FR-Designer_Layout_Constraints");
    }

    public int getRowCount()
    {
        return 1;
    }

    public TableCellRenderer getRenderer(int i)
    {
        return renderer;
    }

    public TableCellEditor getEditor(int i)
    {
        return editor1;
    }

    public Object getValue(int i, int j)
    {
        if(j == 0)
            return Inter.getLocText("FR-Designer_Layout-Index");
        else
            return Integer.valueOf(layout.getWidgetIndex(widget) + 1);
    }

    public boolean setValue(Object obj, int i, int j)
    {
        if(j == 1)
        {
            layout.setWidgetIndex(widget, obj != null ? ((Number)obj).intValue() - 1 : 0);
            container.convert();
            return true;
        } else
        {
            return true;
        }
    }

    public boolean isEditable(int i)
    {
        return true;
    }
}
