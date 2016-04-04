// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.painters;

import com.fr.design.designer.beans.adapters.layout.FRFitLayoutAdapter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.general.ComparatorUtils;
import java.awt.*;

// Referenced classes of package com.fr.design.designer.beans.painters:
//            AbstractPainter

public class FRFitLayoutPainter extends AbstractPainter
{

    private static final int BORDER_PROPORTION = 10;
    private static final int X = 0;
    private static final int Y = 1;
    private static final int WIDTH = 2;
    private static final int HEIGHT = 3;
    private static final Color DEPEND_LINE_COLOR = new Color(200, 200, 200);
    private static final int DEPEND_LINE_SOCOPE = 3;

    public FRFitLayoutPainter(XLayoutContainer xlayoutcontainer)
    {
        super(xlayoutcontainer);
    }

    public void paint(Graphics g, int i, int j)
    {
        if(hotspot_bounds == null && creator != null && container != null)
        {
            drawDependingLine(g);
            return;
        }
        super.paint(g, i, j);
        int k = hotspot.x - hotspot_bounds.x;
        int l = hotspot.y - hotspot_bounds.y;
        FRFitLayoutAdapter frfitlayoutadapter = (FRFitLayoutAdapter)container.getLayoutAdapter();
        Component component = container.getComponentAt(k, l);
        if(component == null)
            return;
        int ai[] = {
            hotspot_bounds.x, hotspot_bounds.y, hotspot_bounds.width, hotspot_bounds.height
        };
        boolean flag = frfitlayoutadapter.accept(creator, k, l);
        Color color = XCreatorConstants.FIT_LAYOUT_HOTSPOT_COLOR;
        if(flag)
        {
            l = l != container.getHeight() ? l : l - 1;
            k = k != container.getWidth() ? k : k - 1;
            ai = frfitlayoutadapter.getChildPosition(component, creator, k, l);
        } else
        {
            color = XCreatorConstants.LAYOUT_FORBIDDEN_COLOR;
            Rectangle rectangle = component.getBounds();
            ai = component != container ? (new int[] {
                rectangle.x, rectangle.y, rectangle.width, rectangle.height
            }) : (new int[] {
                k, l, 0, 0
            });
        }
        if(!ComparatorUtils.equals(container.getBackupParent(), container.getOuterLayout()) && frfitlayoutadapter.intersectsEdge(k, l, container))
        {
            dealHotspotOfTab(ai, container, k, l, color, g, flag);
            return;
        }
        ai[0] += hotspot_bounds.x;
        ai[1] += hotspot_bounds.y;
        drawRegionBackground(g, ai[0], ai[1], ai[2], ai[3], color, flag);
        if(flag)
            paintCrossPoint(component, g, k, l);
    }

    private void dealHotspotOfTab(int ai[], XLayoutContainer xlayoutcontainer, int i, int j, Color color, Graphics g, boolean flag)
    {
        int k = xlayoutcontainer.getX();
        int l = xlayoutcontainer.getY();
        int i1 = xlayoutcontainer.getWidth();
        int j1 = xlayoutcontainer.getHeight();
        Rectangle rectangle = new Rectangle(i, j, 1, 1);
        Rectangle rectangle1 = new Rectangle(k, l, i1, 10);
        if(rectangle1.intersects(rectangle))
        {
            hotspot_bounds.y -= 36;
            ai[2] = xlayoutcontainer.getWidth();
            ai[3] = (xlayoutcontainer.getHeight() + 36) / 2;
        }
        int k1 = (l + j1) - 10;
        Rectangle rectangle2 = new Rectangle(k, k1, i1, 10);
        if(rectangle2.intersects(rectangle))
        {
            hotspot_bounds.y -= 18;
            ai[2] = xlayoutcontainer.getWidth();
            ai[3] = (xlayoutcontainer.getHeight() + 36) / 2;
            flag = false;
        }
        int l1 = j1 - 20;
        int i2 = l + 10;
        Rectangle rectangle3 = new Rectangle(k, i2, 10, l1);
        if(rectangle3.intersects(rectangle))
        {
            hotspot_bounds.y -= 36;
            ai[2] = xlayoutcontainer.getWidth() / 2;
            ai[3] = xlayoutcontainer.getHeight() + 36;
        }
        int j2 = l + 10;
        int k2 = (k + i1) - 10;
        Rectangle rectangle4 = new Rectangle(k2, j2, 10, l1);
        if(rectangle4.intersects(rectangle))
        {
            hotspot_bounds.y -= 36;
            hotspot_bounds.x += xlayoutcontainer.getWidth() / 2;
            ai[2] = xlayoutcontainer.getWidth() / 2;
            ai[3] = xlayoutcontainer.getHeight() + 36;
        }
        ai[0] += hotspot_bounds.x;
        ai[1] += hotspot_bounds.y;
        drawRegionBackground(g, ai[0], ai[1], ai[2], ai[3], color, flag);
    }

    private void paintCrossPoint(Component component, Graphics g, int i, int j)
    {
        if(component == container)
            return;
        Color color = XCreatorConstants.FIT_LAYOUT_POINT_COLOR;
        int k = component.getX();
        int l = component.getY();
        int i1 = component.getHeight();
        int j1 = component.getWidth();
        int k1 = j1 / 10;
        int l1 = i1 / 10;
        int i2 = Math.min(10, Math.min(k1, l1));
        Component component1 = container.getTopComp(k, l);
        Component component2 = container.getBottomComp(k, l, i1);
        Component component3 = container.getRightComp(k, l, j1);
        Component component4 = container.getLeftComp(k, l);
        boolean flag = component1 != null && component1 != container;
        boolean flag1 = component4 != null && component4 != container;
        boolean flag2 = component2 != null && component2 != container;
        boolean flag3 = component3 != null && component3 != container;
        if(flag || flag1)
            drawRegionBackground(g, k + hotspot_bounds.x, l + hotspot_bounds.y, i2, i2, color, true);
        if(flag2 || flag1)
            drawRegionBackground(g, k + hotspot_bounds.x, ((l + i1) - i2) + hotspot_bounds.y, i2, i2, color, true);
        if(flag || flag3)
            drawRegionBackground(g, ((k + j1) - i2) + hotspot_bounds.x, l + hotspot_bounds.y, i2, i2, color, true);
        if(flag2 || flag3)
            drawRegionBackground(g, ((k + j1) - i2) + hotspot_bounds.x, ((l + i1) - i2) + hotspot_bounds.y, i2, i2, color, true);
        if(flag1 && flag3)
        {
            if(component4.getY() == l && component3.getY() == l)
                drawRegionBackground(g, ((k + j1 / 2) - k1) + hotspot_bounds.x, l + hotspot_bounds.y, k1 * 2, i2, color, true);
            component4 = container.getBottomLeftComp(k, l, i1);
            component3 = container.getBottomRightComp(k, l, i1, j1);
            if(component4.getY() + component4.getHeight() == l + i1 && component3.getY() + component3.getHeight() == l + i1)
                drawRegionBackground(g, ((k + j1 / 2) - k1) + hotspot_bounds.x, ((l + i1) - i2) + hotspot_bounds.y, k1 * 2, i2, color, true);
        }
        if(flag && flag2)
        {
            if(component1.getX() == k && component2.getX() == k)
                drawRegionBackground(g, k + hotspot_bounds.x, ((l + i1 / 2) - l1) + hotspot_bounds.y, i2, l1 * 2, color, true);
            component1 = container.getRightTopComp(k, l, j1);
            component2 = container.getRightBottomComp(k, l, i1, j1);
            if(component1.getX() + component1.getWidth() == k + j1 && component2.getX() + component2.getWidth() == k + j1)
                drawRegionBackground(g, ((k + j1) - i2) + hotspot_bounds.x, ((l + i1 / 2) - l1) + hotspot_bounds.y, i2, l1 * 2, color, true);
        }
    }

    private void drawDependingLine(Graphics g)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        Stroke stroke = graphics2d.getStroke();
        int i = creator.getX();
        int j = creator.getY();
        double d = hotspot.getX();
        double d1 = hotspot.getY();
        int ai[] = container.getHors();
        int ai1[] = container.getVeris();
        int k = 0;
        int l = 0;
        k = getDependLinePos(k, ai, i, d);
        l = getDependLinePos(l, ai1, j, d1);
        graphics2d.setStroke(stroke);
        graphics2d.setColor(DEPEND_LINE_COLOR);
        if(k != 0)
            graphics2d.drawRect(k, 0, 0, container.getHeight());
        if(l != 0)
            graphics2d.drawRect(0, l, container.getWidth(), 0);
    }

    private int getDependLinePos(int i, int ai[], int j, double d)
    {
        int k = 0;
        do
        {
            if(k >= ai.length)
                break;
            if(ai[k] != j && d > (double)(ai[k] - 3) && d < (double)(ai[k] + 3))
            {
                i = ai[k];
                break;
            }
            k++;
        } while(true);
        return i;
    }

}
