// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWVerticalBoxLayout;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WVerticalBoxLayout;
import com.fr.general.Inter;
import java.awt.Component;

// Referenced classes of package com.fr.design.designer.properties:
//            HVLayoutConstraints, LayoutConstraintsEditor

public class VerticalLayoutConstraints extends HVLayoutConstraints
{

    WVerticalBoxLayout layout;

    public VerticalLayoutConstraints(XLayoutContainer xlayoutcontainer, Component component)
    {
        super(xlayoutcontainer, component);
        layout = ((XWVerticalBoxLayout)xlayoutcontainer).toData();
        editor1 = new LayoutConstraintsEditor(layout);
    }

    public Object getValue(int i, int j)
    {
        if(j == 0)
        {
            switch(i)
            {
            case 0: // '\0'
                return Inter.getLocText("Layout-Index");
            }
            return Inter.getLocText("Tree-Height");
        }
        switch(i)
        {
        case 0: // '\0'
            return Integer.valueOf(layout.getWidgetIndex(widget) + 1);
        }
        return Integer.valueOf(layout.getHeightAtWidget(widget));
    }

    public boolean setValue(Object obj, int i, int j)
    {
        if(j == 1)
        {
            XWVerticalBoxLayout xwverticalboxlayout = (XWVerticalBoxLayout)parent;
            switch(i)
            {
            case 0: // '\0'
                layout.setWidgetIndex(widget, obj != null ? ((Number)obj).intValue() - 1 : 0);
                xwverticalboxlayout.convert();
                return true;
            }
            layout.setHeightAtWidget(widget, obj != null ? ((Number)obj).intValue() : 0);
            xwverticalboxlayout.recalculateChildrenPreferredSize();
            return true;
        } else
        {
            return true;
        }
    }
}
