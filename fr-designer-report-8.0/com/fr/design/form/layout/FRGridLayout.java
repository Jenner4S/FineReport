// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.form.layout;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.ComparatorUtils;
import java.awt.*;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.form.layout:
//            FRLayoutManager

public class FRGridLayout extends GridLayout
    implements LayoutManager2, FRLayoutManager
{

    private Map map;

    public FRGridLayout()
    {
        this(1, 1, 0, 0);
    }

    public FRGridLayout(int i, int j)
    {
        this(i, j, 0, 0);
    }

    public FRGridLayout(int i, int j, int k, int l)
    {
        super(i, j, k, l);
        map = new HashMap();
    }

    public void removeLayoutComponent(Component component)
    {
        for(int i = 0; i < getRows(); i++)
        {
            for(int j = 0; j < getColumns(); j++)
            {
                Point point = new Point(j, i);
                if(ComparatorUtils.equals(component, map.get(point)))
                {
                    map.remove(point);
                    return;
                }
            }

        }

    }

    public void addLayoutComponent(Component component, Object obj)
    {
        if(obj == null)
        {
            for(int i = 0; i < getRows(); i++)
            {
                for(int j = 0; j < getColumns(); j++)
                {
                    Point point1 = new Point(j, i);
                    if(map.get(point1) == null)
                    {
                        map.put(point1, component);
                        return;
                    }
                }

            }

        } else
        {
            Point point = (Point)obj;
            if(point.x > getColumns() - 1 || point.y > getRows() - 1)
                throw new IllegalArgumentException("Component cannot be add at this point!");
            map.put(point, component);
        }
    }

    public Point getPoint(Component component)
    {
        for(Iterator iterator = map.keySet().iterator(); iterator.hasNext();)
        {
            Point point = (Point)iterator.next();
            if(ComparatorUtils.equals(map.get(point), component))
                return point;
        }

        return null;
    }

    public float getLayoutAlignmentX(Container container)
    {
        return 0.5F;
    }

    public float getLayoutAlignmentY(Container container)
    {
        return 0.5F;
    }

    public void invalidateLayout(Container container)
    {
    }

    public void layoutContainer(Container container)
    {
        synchronized(container.getTreeLock())
        {
            Insets insets = container.getInsets();
            int i = container.getWidth() - (insets.left + insets.right);
            int j = container.getHeight() - (insets.top + insets.bottom);
            i = (i - (getColumns() - 1) * getHgap()) / getColumns();
            j = (j - (getRows() - 1) * getVgap()) / getRows();
            Point point;
            int k;
            int l;
            for(Iterator iterator = map.keySet().iterator(); iterator.hasNext(); ((Component)map.get(point)).setBounds(l * (i + getHgap()) + insets.left, k * (j + getVgap()) + insets.top, i, j))
            {
                point = (Point)iterator.next();
                k = point.y;
                l = point.x;
            }

        }
    }

    public boolean isResizable()
    {
        return false;
    }

    public static void main(String args[])
    {
        JFrame jframe = new JFrame();
        JPanel jpanel = (JPanel)jframe.getContentPane();
        jpanel.setLayout(FRGUIPaneFactory.createBorderLayout());
        FRGridLayout frgridlayout = new FRGridLayout(3, 4, 10, 4);
        JPanel jpanel1 = new JPanel(frgridlayout);
        jpanel.add(jpanel1, "Center");
        jpanel1.add(new UIButton("1111"));
        jpanel1.add(new UIButton("111122"));
        jpanel1.add(new UITextField("1111222"), new Point(3, 2));
        jframe.setSize(400, 400);
        jframe.setVisible(true);
    }

    public Dimension maximumLayoutSize(Container container)
    {
        return new Dimension(0, 0);
    }
}
