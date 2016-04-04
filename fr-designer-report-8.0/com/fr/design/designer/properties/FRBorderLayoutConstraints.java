// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.mainframe.widget.editors.*;
import com.fr.form.ui.FreeButton;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.general.Inter;
import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.*;

// Referenced classes of package com.fr.design.designer.properties:
//            FRBorderLayoutConstraintsRenderer, FRBorderConstraintsEditor

public class FRBorderLayoutConstraints
    implements ConstraintsGroupModel
{

    private FRBorderLayoutConstraintsRenderer renderer0;
    private DefaultTableCellRenderer renderer;
    private FRBorderConstraintsEditor editor1;
    private PropertyCellEditor editor2;
    private PropertyCellEditor editor3;
    private Widget widget;
    private WBorderLayout layout;
    private XWBorderLayout container;

    public FRBorderLayoutConstraints(Container container1, Component component)
    {
        container = (XWBorderLayout)container1;
        layout = ((XWBorderLayout)container1).toData();
        widget = ((XWidgetCreator)component).toData();
        renderer0 = new FRBorderLayoutConstraintsRenderer();
        renderer = new DefaultTableCellRenderer();
        editor1 = new FRBorderConstraintsEditor(layout.getDirections());
        editor2 = new PropertyCellEditor(new StringEditor());
        editor3 = new PropertyCellEditor(new IntegerPropertyEditor());
    }

    public String getGroupName()
    {
        return Inter.getLocText("Layout_Constraints");
    }

    public int getRowCount()
    {
        Object obj = layout.getConstraints(widget);
        if(obj == null)
            return 0;
        else
            return "Center".equals(obj) ? 2 : 3;
    }

    public TableCellRenderer getRenderer(int i)
    {
        if(i == 0)
            return renderer0;
        else
            return renderer;
    }

    public TableCellEditor getEditor(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            return editor1;

        case 1: // '\001'
            return editor2;
        }
        return editor3;
    }

    public Object getValue(int i, int j)
    {
        if(j == 0)
        {
            switch(i)
            {
            case 0: // '\0'
                return Inter.getLocText("BorderLayout-Constraints");

            case 1: // '\001'
                return Inter.getLocText("Title");
            }
            return getSizeDisplayName();
        }
        switch(i)
        {
        case 0: // '\0'
            return getChildPositionDisplayName();

        case 1: // '\001'
            return getChildTitle();
        }
        return Integer.valueOf(getChildSize());
    }

    public boolean setValue(Object obj, int i, int j)
    {
        if(j == 1)
        {
            switch(i)
            {
            case 0: // '\0'
                return switchWidgets(obj);

            case 1: // '\001'
                return setChildTitle(obj);
            }
            return setChildSize(obj);
        } else
        {
            return true;
        }
    }

    public boolean isEditable(int i)
    {
        if(i == 2)
            return !(widget instanceof FreeButton) || !((FreeButton)widget).isCustomStyle();
        else
            return true;
    }

    private String getSizeDisplayName()
    {
        Object obj = layout.getConstraints(widget);
        if("North".equals(obj) || "South".equals(obj))
            return Inter.getLocText("Tree-Height");
        if("West".equals(obj) || "East".equals(obj))
            return Inter.getLocText("Tree-Width");
        else
            return "";
    }

    private String getChildPositionDisplayName()
    {
        Object obj = layout.getConstraints(widget);
        return obj.toString();
    }

    private boolean switchWidgets(Object obj)
    {
        Widget widget1 = layout.getLayoutWidget(obj);
        if(widget1 == null)
        {
            layout.removeWidget(widget);
            XWBorderLayout.add(layout, widget, obj);
        } else
        {
            Object obj1 = layout.getConstraints(widget);
            layout.removeWidget(widget);
            layout.removeWidget(widget1);
            XWBorderLayout.add(layout, widget, obj);
            XWBorderLayout.add(layout, widget1, obj1);
        }
        container.convert();
        return true;
    }

    private int getChildSize()
    {
        Object obj = layout.getConstraints(widget);
        try
        {
            Method method = layout.getClass().getDeclaredMethod((new StringBuilder()).append("get").append(obj).append("Size").toString(), new Class[0]);
            Object obj1 = method.invoke(layout, new Object[0]);
            if(obj1 instanceof Number)
                return ((Number)obj1).intValue();
        }
        catch(Exception exception)
        {
            Logger.getLogger(com/fr/design/designer/properties/FRBorderLayoutConstraints.getName()).log(Level.SEVERE, null, exception);
        }
        return 0;
    }

    private boolean setChildSize(Object obj)
    {
        int i = 0;
        if(obj != null)
            i = ((Number)obj).intValue();
        Object obj1 = layout.getConstraints(widget);
        try
        {
            Method method = layout.getClass().getDeclaredMethod((new StringBuilder()).append("set").append(obj1).append("Size").toString(), new Class[] {
                Integer.TYPE
            });
            method.invoke(layout, new Object[] {
                Integer.valueOf(i)
            });
            container.recalculateChildrenPreferredSize();
        }
        catch(Exception exception)
        {
            return false;
        }
        return true;
    }

    private String getChildTitle()
    {
        Object obj = layout.getConstraints(widget);
        try
        {
            Method method = layout.getClass().getDeclaredMethod((new StringBuilder()).append("get").append(obj).append("Title").toString(), new Class[0]);
            return (String)method.invoke(layout, new Object[0]);
        }
        catch(Exception exception)
        {
            Logger.getLogger(com/fr/design/designer/properties/FRBorderLayoutConstraints.getName()).log(Level.SEVERE, null, exception);
        }
        return "";
    }

    private boolean setChildTitle(Object obj)
    {
        Object obj1 = layout.getConstraints(widget);
        try
        {
            Method method = layout.getClass().getDeclaredMethod((new StringBuilder()).append("set").append(obj1).append("Title").toString(), new Class[] {
                java/lang/String
            });
            method.invoke(layout, new Object[] {
                obj
            });
        }
        catch(Exception exception)
        {
            return false;
        }
        return true;
    }
}
