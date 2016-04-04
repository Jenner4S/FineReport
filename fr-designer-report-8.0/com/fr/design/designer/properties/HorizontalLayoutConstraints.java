// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWHorizontalBoxLayout;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WHorizontalBoxLayout;
import com.fr.general.Inter;
import java.awt.Component;

// Referenced classes of package com.fr.design.designer.properties:
//            HVLayoutConstraints, LayoutConstraintsEditor

public class HorizontalLayoutConstraints extends HVLayoutConstraints
{

    WHorizontalBoxLayout layout;

    public HorizontalLayoutConstraints(XLayoutContainer xlayoutcontainer, Component component)
    {
        super(xlayoutcontainer, component);
        layout = ((XWHorizontalBoxLayout)xlayoutcontainer).toData();
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
            return Inter.getLocText("Tree-Width");
        }
        switch(i)
        {
        case 0: // '\0'
            return Integer.valueOf(layout.getWidgetIndex(widget) + 1);
        }
        return Integer.valueOf(layout.getWidthAtWidget(widget));
    }

    public boolean setValue(Object obj, int i, int j)
    {
        if(j == 1)
        {
            XWHorizontalBoxLayout xwhorizontalboxlayout = (XWHorizontalBoxLayout)parent;
            switch(i)
            {
            case 0: // '\0'
                layout.setWidgetIndex(widget, obj != null ? ((Number)obj).intValue() - 1 : 0);
                xwhorizontalboxlayout.convert();
                return true;
            }
            layout.setWidthAtWidget(widget, obj != null ? ((Number)obj).intValue() : 0);
            xwhorizontalboxlayout.recalculateChildrenPreferredSize();
            return true;
        } else
        {
            return true;
        }
    }
}
