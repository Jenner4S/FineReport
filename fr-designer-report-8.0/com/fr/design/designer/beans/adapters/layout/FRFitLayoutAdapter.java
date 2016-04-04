// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.painters.FRFitLayoutPainter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.design.designer.creator.cardlayout.XWCardMainBorderLayout;
import com.fr.design.designer.creator.cardlayout.XWTabFitLayout;
import com.fr.design.designer.properties.FRFitLayoutConstraints;
import com.fr.design.designer.properties.FRFitLayoutPropertiesGroupModel;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.JForm;
import com.fr.design.utils.ComponentUtils;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.PaddingMargin;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WCardLayout;
import com.fr.form.ui.container.WFitLayout;
import com.fr.form.ui.container.WLayout;
import com.fr.general.ComparatorUtils;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

// Referenced classes of package com.fr.design.designer.beans.adapters.layout:
//            AbstractLayoutAdapter

public class FRFitLayoutAdapter extends AbstractLayoutAdapter
{

    public static final String WIDGETPANEICONPATH = "/com/fr/web/images/form/resources/layout_absolute.png";
    private static final double TOP_HALF = 0.25D;
    private static final double BOTTOM_HALF = 0.75D;
    private static final int DEFAULT_AREA_LENGTH = 5;
    private static final int BORDER_PROPORTION = 10;
    private static final int COMP_TOP = 1;
    private static final int COMP_BOTTOM = 2;
    private static final int COMP_LEFT = 3;
    private static final int COMP_RIGHT = 4;
    private static final int COMP_LEFT_TOP = 5;
    private static final int COMP_LEFT_BOTTOM = 6;
    private static final int COMP_RIGHT_TOP = 7;
    private static final int COMP_RIGHT_BOTTOM = 8;
    private static final int INDEX_ZERO = 0;
    private static final int DEPENDING_SCOPE = 3;
    private int trisectAreaDirect;
    private int crossPointAreaDirect;
    private java.util.List rightComps;
    private java.util.List leftComps;
    private java.util.List downComps;
    private java.util.List upComps;
    private boolean isFindRelatedComps;
    private boolean isCalculateChildPos;
    private int childPosition[];
    private HoverPainter painter;
    private int minWidth;
    private int minHeight;
    private int actualVal;
    private PaddingMargin margin;

    public FRFitLayoutAdapter(XLayoutContainer xlayoutcontainer)
    {
        super(xlayoutcontainer);
        trisectAreaDirect = 0;
        crossPointAreaDirect = 0;
        isFindRelatedComps = false;
        isCalculateChildPos = false;
        childPosition = null;
        minWidth = 0;
        minHeight = 0;
        actualVal = 0;
        painter = new FRFitLayoutPainter(xlayoutcontainer);
        initMinSize();
    }

    private void initMinSize()
    {
        XWFitLayout xwfitlayout = (XWFitLayout)container;
        minWidth = xwfitlayout.getActualMinWidth();
        minHeight = xwfitlayout.getActualMinHeight();
        actualVal = xwfitlayout.getAcualInterval();
        margin = xwfitlayout.toData().getMargin();
    }

    public HoverPainter getPainter()
    {
        return painter;
    }

    public GroupModel getLayoutProperties()
    {
        XWFitLayout xwfitlayout = (XWFitLayout)container;
        return new FRFitLayoutPropertiesGroupModel(xwfitlayout);
    }

    public void addComp(XCreator xcreator, int i, int j)
    {
        if(ComparatorUtils.equals(xcreator.getIconPath(), "/com/fr/web/images/form/resources/layout_absolute.png"))
            return;
        fix(xcreator, i, j);
        if(xcreator.shouldScaleCreator() || xcreator.hasTitleStyle())
            addParentCreator(xcreator);
        else
            container.add(xcreator, xcreator.toData().getWidgetName());
        XWFitLayout xwfitlayout = (XWFitLayout)container;
        xwfitlayout.updateBoundsWidget();
        updateCreatorBackBound();
    }

    private void updateCreatorBackBound()
    {
        int i = 0;
        for(int j = container.getComponentCount(); i < j; i++)
        {
            XCreator xcreator = (XCreator)container.getComponent(i);
            xcreator.updateChildBound(minHeight);
            xcreator.setBackupBound(xcreator.getBounds());
        }

    }

    private void addParentCreator(XCreator xcreator)
    {
        XLayoutContainer xlayoutcontainer = xcreator.initCreatorWrapper(minHeight);
        container.add(xlayoutcontainer, xcreator.toData().getWidgetName());
    }

    public boolean accept(XCreator xcreator, int i, int j)
    {
        isFindRelatedComps = false;
        Component component = container.getComponentAt(i, j);
        if(checkInterval(component))
            return false;
        matchEdge(i, j);
        int k = component.getHeight();
        int l = component.getWidth();
        int i1 = (int)((double)k * 0.25D) + component.getY();
        int j1 = (int)((double)k * 0.75D) + component.getY();
        if(isCrossPointArea(component, i, j))
            return canAcceptWhileCrossPoint(component, i, j);
        if(isTrisectionArea(component, i, j))
        {
            return canAcceptWhileTrisection(component, i, j);
        } else
        {
            boolean flag = l >= minWidth * 2 + actualVal;
            boolean flag1 = k >= minHeight * 2 + actualVal;
            return j <= i1 || j >= j1 ? flag1 : flag;
        }
    }

    private boolean checkInterval(Component component)
    {
        return container.getComponentCount() > 0 && component == container;
    }

    public boolean matchEdge(int i, int j)
    {
        if(intersectsEdge(i, j, container))
        {
            XLayoutContainer xlayoutcontainer = container.findNearestFit();
            container = xlayoutcontainer == null ? container : xlayoutcontainer;
            return true;
        } else
        {
            return false;
        }
    }

    public boolean intersectsEdge(int i, int j, XLayoutContainer xlayoutcontainer)
    {
        int k = xlayoutcontainer.getX();
        int l = xlayoutcontainer.getY();
        int i1 = xlayoutcontainer.getWidth();
        int j1 = xlayoutcontainer.getHeight();
        Rectangle rectangle = new Rectangle(i, j, 1, 1);
        Rectangle rectangle1 = new Rectangle(k, l, i1, 10);
        if(rectangle1.intersects(rectangle))
            return true;
        int k1 = (l + j1) - 10;
        Rectangle rectangle2 = new Rectangle(k, k1, i1, 10);
        if(rectangle2.intersects(rectangle))
            return true;
        int l1 = j1 - 20;
        int i2 = l + 10;
        Rectangle rectangle3 = new Rectangle(k, i2, 10, l1);
        if(rectangle3.intersects(rectangle))
        {
            return true;
        } else
        {
            int j2 = l + 10;
            int k2 = (k + i1) - 10;
            Rectangle rectangle4 = new Rectangle(k2, j2, 10, l1);
            return rectangle4.intersects(rectangle);
        }
    }

    private boolean canAcceptWhileCrossPoint(Component component, int i, int j)
    {
        int k = component.getX();
        int l = component.getY();
        int i1 = component.getHeight();
        int j1 = component.getWidth();
        Component component1 = container.getTopComp(k, l);
        Component component2 = container.getBottomComp(k, l, i1);
        Component component3 = container.getRightComp(k, l, j1);
        Component component5 = container.getLeftComp(k, l);
        int k1 = 0;
        int l1 = minHeight * 2;
        boolean flag = false;
        if(ComparatorUtils.equals(Integer.valueOf(crossPointAreaDirect), Integer.valueOf(5)))
        {
            boolean flag1 = component1 == null || component1.getX() != k;
            k1 = flag1 ? Math.min(i1, component5.getHeight()) : Math.min(j1, component1.getWidth());
            l1 = flag1 ? l1 : minWidth * 2;
        } else
        if(ComparatorUtils.equals(Integer.valueOf(crossPointAreaDirect), Integer.valueOf(8)))
        {
            component2 = container.getRightBottomComp(k, l, i1, j1);
            boolean flag2 = component2 == null || component2.getX() + component2.getWidth() != k + j1;
            component3 = container.getBottomRightComp(k, l, i1, j1);
            k1 = flag2 ? Math.min(i1, component3.getHeight()) : Math.min(j1, component2.getWidth());
            l1 = flag2 ? l1 : minWidth * 2;
        } else
        if(ComparatorUtils.equals(Integer.valueOf(crossPointAreaDirect), Integer.valueOf(6)))
        {
            component5 = container.getBottomLeftComp(k, l, i1);
            boolean flag3 = component5 == null || component5.getY() + component5.getHeight() != l + i1;
            k1 = flag3 ? Math.min(j1, component2.getWidth()) : Math.min(i1, component5.getHeight());
            l1 = flag3 ? minWidth * 2 : l1;
        } else
        if(ComparatorUtils.equals(Integer.valueOf(crossPointAreaDirect), Integer.valueOf(7)))
        {
            boolean flag4 = component3 == null || component3.getY() != l;
            component1 = container.getRightTopComp(k, l, j1);
            k1 = flag4 ? Math.min(j1, component1.getWidth()) : Math.min(i1, component3.getWidth());
            l1 = flag4 ? minWidth * 2 : l1;
        } else
        if(ComparatorUtils.equals(Integer.valueOf(crossPointAreaDirect), Integer.valueOf(1)))
            k1 = Math.min(component3.getHeight(), Math.min(i1, component5.getHeight()));
        else
        if(ComparatorUtils.equals(Integer.valueOf(crossPointAreaDirect), Integer.valueOf(2)))
        {
            Component component6 = container.getBottomLeftComp(k, l, i1);
            Component component4 = container.getBottomRightComp(k, l, i1, j1);
            k1 = Math.min(component4.getHeight(), Math.min(i1, component6.getHeight()));
        } else
        {
            if(ComparatorUtils.equals(Integer.valueOf(crossPointAreaDirect), Integer.valueOf(4)))
            {
                component1 = container.getRightTopComp(k, l, j1);
                component2 = container.getRightBottomComp(k, l, i1, j1);
            }
            k1 = Math.min(component1.getWidth(), Math.min(j1, component2.getWidth()));
            l1 = minWidth * 2;
        }
        return k1 >= l1 + actualVal;
    }

    private boolean canAcceptWhileTrisection(Component component, int i, int j)
    {
        int k = component.getX();
        int l = component.getY();
        int i1 = component.getHeight();
        int j1 = component.getWidth();
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        if(ComparatorUtils.equals(Integer.valueOf(trisectAreaDirect), Integer.valueOf(1)))
        {
            int k1 = getUpMinHeightComp(l, i);
            int i2 = getDownMinHeightComp(component, j);
            return k1 != 0 ? k1 + i2 >= minHeight * 3 + actualVal : i2 >= minHeight * 2 + actualVal;
        }
        if(ComparatorUtils.equals(Integer.valueOf(trisectAreaDirect), Integer.valueOf(2)))
        {
            int l1 = getUpMinHeightComp(l + i1 + actualVal, i);
            int j2;
            if(l + i1 + 5 > container.getHeight() - margin.getBottom())
            {
                j2 = 0;
            } else
            {
                Component component1 = container.getBottomComp(i, l, i1);
                j2 = getDownMinHeightComp(component1, l + i1 + 5 + actualVal);
            }
            return j2 != 0 ? l1 + j2 >= minHeight * 3 + actualVal : l1 >= minHeight * 2 + actualVal;
        }
        if(ComparatorUtils.equals(Integer.valueOf(trisectAreaDirect), Integer.valueOf(3)))
        {
            int i3 = getMinRightWidth(k, 0, j);
            int k2;
            if(k - 5 < margin.getLeft())
            {
                k2 = 0;
            } else
            {
                Component component2 = container.getLeftComp(k, j);
                k2 = getMinLeftWidth(component2, k - 5 - actualVal);
            }
            return k2 != 0 ? k2 + i3 >= minWidth * 3 + actualVal : i3 >= minWidth * 2 + actualVal;
        }
        if(ComparatorUtils.equals(Integer.valueOf(trisectAreaDirect), Integer.valueOf(4)))
        {
            int l2 = getMinLeftWidth(component, i);
            int j3 = getMinRightWidth(k, j1, j);
            return j3 != 0 ? l2 + j3 >= minWidth * 3 + actualVal : l2 >= minWidth * 2 + actualVal;
        } else
        {
            return false;
        }
    }

    private int getUpMinHeightComp(int i, int j)
    {
        if(i == margin.getTop())
            return 0;
        int k = container.getWidth() - margin.getRight();
        int l = j;
        int i1 = i;
        boolean flag = false;
        if(isFindRelatedComps)
            upComps = new ArrayList();
        do
        {
            if(l >= k)
                break;
            Component component = container.getTopComp(l, i);
            if(component == null)
                break;
            int j1 = component.getHeight() + component.getY() + actualVal;
            if(j1 != i)
                break;
            if(component.getHeight() < i1)
                i1 = component.getHeight();
            l = component.getX() + component.getWidth() + 5 + actualVal;
            if(isFindRelatedComps)
                upComps.add(component);
        } while(true);
        if(container.getTopComp(j, i) == null)
            return 0;
        l = container.getTopComp(j, i).getX() - 5 - actualVal;
        do
        {
            if(l <= margin.getLeft())
                break;
            Component component1 = container.getTopComp(l, i);
            int k1 = component1.getHeight() + component1.getY() + actualVal;
            if(k1 != i)
                break;
            if(component1.getHeight() < i1)
                i1 = component1.getHeight();
            l = component1.getX() - 5 - actualVal;
            if(isFindRelatedComps)
                upComps.add(component1);
        } while(true);
        return i1;
    }

    private int getDownMinHeightComp(Component component, int i)
    {
        int j = component.getX();
        int k = component.getY();
        int l = component.getHeight();
        int i1 = container.getWidth() - margin.getRight();
        if(isFindRelatedComps)
            downComps = new ArrayList();
        int j1 = j + 5;
        do
        {
            if(j1 >= i1)
                break;
            Component component1 = container.getComponentAt(j1, i);
            if(component1.getY() != k)
                break;
            if(component1.getHeight() < l)
                l = component1.getHeight();
            j1 = component1.getX() + component1.getWidth() + 5 + actualVal;
            if(isFindRelatedComps)
                downComps.add(component1);
        } while(true);
        j1 = j - 5 - actualVal;
        do
        {
            if(j1 <= margin.getLeft())
                break;
            Component component2 = container.getComponentAt(j1, i);
            if(component2.getY() != k)
                break;
            if(component2.getHeight() < l)
                l = component2.getHeight();
            j1 = component2.getX() - 5 - actualVal;
            if(isFindRelatedComps)
                downComps.add(component2);
        } while(true);
        return l;
    }

    private int getMinRightWidth(int i, int j, int k)
    {
        int l = i + 5;
        l = j != 0 ? l + j + actualVal : l;
        if(l > container.getWidth() - margin.getRight())
            return 0;
        Component component = container.getComponentAt(l, k);
        int i1 = component.getWidth();
        int j1 = container.getHeight() - margin.getBottom();
        if(isFindRelatedComps)
            rightComps = new ArrayList();
        int k1 = component.getY() + 5;
        do
        {
            if(k1 >= j1)
                break;
            Component component1 = container.getComponentAt(l, k1);
            if(component1.getX() != component.getX())
                break;
            if(component1.getWidth() < i1)
                i1 = component1.getWidth();
            k1 = component1.getY() + component1.getHeight() + 5 + actualVal;
            if(isFindRelatedComps)
                rightComps.add(component1);
        } while(true);
        k1 = component.getY() - 5 - actualVal;
        do
        {
            if(k1 <= margin.getTop())
                break;
            Component component2 = container.getComponentAt(l, k1);
            if(component2.getX() != component.getX())
                break;
            if(component2.getWidth() < i1)
                i1 = component2.getWidth();
            k1 = component2.getY() - 5 - actualVal;
            if(isFindRelatedComps)
                rightComps.add(component2);
        } while(true);
        return i1;
    }

    private int getMinLeftWidth(Component component, int i)
    {
        int j = component.getWidth();
        int k = component.getX() + component.getWidth();
        int l = container.getHeight() - margin.getBottom();
        if(isFindRelatedComps)
            leftComps = new ArrayList();
        boolean flag = false;
        int k1 = component.getY() + 5;
        do
        {
            if(k1 >= l)
                break;
            Component component1 = container.getComponentAt(i, k1);
            int i1 = component1.getX() + component1.getWidth();
            if(i1 != k)
                break;
            if(component1.getWidth() < j)
                j = component1.getWidth();
            k1 = component1.getY() + component1.getHeight() + 5 + actualVal;
            if(isFindRelatedComps)
                leftComps.add(component1);
        } while(true);
        k1 = component.getY() - 5 - actualVal;
        do
        {
            if(k1 <= margin.getTop())
                break;
            Component component2 = container.getComponentAt(i, k1);
            int j1 = component2.getX() + component2.getWidth();
            if(j1 != k)
                break;
            if(component2.getWidth() < j)
                j = component2.getWidth();
            k1 = component2.getY() - 5 - actualVal;
            if(isFindRelatedComps)
                leftComps.add(component2);
        } while(true);
        return j;
    }

    public boolean isTrisectionArea(Component component, int i, int j)
    {
        XCreator xcreator = (XCreator)component;
        if(container.getComponentCount() <= 1)
            return false;
        int k = component.getWidth();
        int l = component.getHeight();
        int i1 = component.getX();
        int j1 = component.getY();
        int k1 = Math.max(k / 10, 5);
        int l1 = Math.max(l / 10, 5);
        if(j < j1 + l1)
            trisectAreaDirect = 1;
        else
        if(j > (j1 + l) - l1)
            trisectAreaDirect = 2;
        else
        if(i < i1 + k1)
            trisectAreaDirect = 3;
        else
        if(i > (i1 + k) - k1)
            trisectAreaDirect = 4;
        if(!xcreator.getTargetChildrenList().isEmpty())
            return false;
        else
            return !ComparatorUtils.equals(Integer.valueOf(trisectAreaDirect), Integer.valueOf(0));
    }

    public boolean isCrossPointArea(Component component, int i, int j)
    {
        if(component == null || container.getComponentCount() <= 2)
            return false;
        int k = component.getX();
        int l = component.getY();
        int i1 = component.getWidth();
        int j1 = component.getHeight();
        int k1 = Math.max(i1 / 10, 5);
        int l1 = Math.max(j1 / 10, 5);
        int i2 = k + i1;
        int j2 = l + j1;
        int k2 = k + k1;
        int l2 = l + l1;
        int i3 = container.getWidth() - margin.getRight();
        int j3 = container.getHeight() - margin.getBottom();
        if(i < k2 && j < l2)
            crossPointAreaDirect = l <= margin.getTop() && k <= margin.getLeft() ? 0 : 5;
        else
        if(j < l2 && i > i2 - k1)
            crossPointAreaDirect = l <= margin.getTop() && i2 >= i3 ? 0 : 7;
        else
        if(i < k2 && j > j2 - l1)
            crossPointAreaDirect = k <= margin.getLeft() && j2 >= j3 ? 0 : 6;
        else
        if(i > i2 - k1 && j > j2 - l1)
            crossPointAreaDirect = j2 >= j3 && i2 >= i3 ? 0 : 8;
        else
            isMiddlePosition(component, i, j, k1, l1);
        XCreator xcreator = (XCreator)component;
        if(!xcreator.getTargetChildrenList().isEmpty())
            return false;
        else
            return crossPointAreaDirect != 0;
    }

    private void isMiddlePosition(Component component, int i, int j, int k, int l)
    {
        int i1 = component.getX();
        int j1 = component.getY();
        int k1 = component.getWidth();
        int l1 = component.getHeight();
        boolean flag = false;
        if(i > (i1 + k1 / 2) - k && i < i1 + k1 / 2 + k)
        {
            Component component1 = container.getLeftComp(i1, j1);
            Component component5 = container.getRightComp(i1, j1, k1);
            if(j < j1 + l)
            {
                flag = component1 != null && component5 != null && component1.getY() == j1 && component5.getY() == j1;
                crossPointAreaDirect = flag ? 1 : 0;
            } else
            if(j > (j1 + l1) - l)
            {
                Component component2 = container.getBottomLeftComp(i1, j1, l1);
                Component component6 = container.getBottomRightComp(i1, j1, l1, k1);
                if(component2 != null && component6 != null)
                    flag = component2.getY() + component2.getHeight() == j1 + l1 && component6.getY() + component6.getHeight() == j1 + l1;
                crossPointAreaDirect = flag ? 2 : 0;
            }
        } else
        if(j > (j1 + l1 / 2) - l && j < j1 + l1 / 2 + l)
        {
            Component component3 = container.getTopComp(i1, j1);
            Component component7 = container.getBottomComp(i1, j1, l1);
            if(i < i1 + k)
            {
                flag = component3 != null && component7 != null && component3.getX() == i1 && component7.getX() == i1;
                crossPointAreaDirect = flag ? 3 : 0;
            } else
            if(i > (i1 + k1) - k)
            {
                Component component4 = container.getRightTopComp(i1, j1, k1);
                Component component8 = container.getRightBottomComp(i1, j1, l1, k1);
                if(component4 != null && component8 != null)
                    flag = component4.getX() + component4.getWidth() == i1 + k1 && component8.getX() + component8.getWidth() == i1 + k1;
                crossPointAreaDirect = flag ? 4 : 0;
            }
        }
    }

    private void initCompsList()
    {
        rightComps = new ArrayList();
        leftComps = new ArrayList();
        upComps = new ArrayList();
        downComps = new ArrayList();
    }

    private void clearCompsList()
    {
        rightComps = null;
        leftComps = null;
        upComps = null;
        downComps = null;
    }

    private Rectangle adjustBackupBound(Rectangle rectangle, XWCardMainBorderLayout xwcardmainborderlayout)
    {
        JForm jform = (JForm)(JForm)HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if(jform.getFormDesign().getParaComponent() != null)
            rectangle.y -= jform.getFormDesign().getParaHeight();
        Rectangle rectangle1 = xwcardmainborderlayout.getBounds();
        rectangle.x -= rectangle1.x;
        rectangle.y -= rectangle1.y;
        XWCardLayout xwcardlayout = xwcardmainborderlayout.getCardPart();
        LayoutBorderStyle layoutborderstyle = xwcardlayout.toData().getBorderStyle();
        if(ComparatorUtils.equals(Integer.valueOf(layoutborderstyle.getType()), Integer.valueOf(1)))
            rectangle.y -= 36;
        return rectangle;
    }

    public void fix(XCreator xcreator)
    {
        Rectangle rectangle = xcreator.getBackupBound();
        rectangle.x -= container.getX();
        rectangle.y -= container.getY();
        int i = xcreator.getX();
        int j = xcreator.getY();
        int ai[] = container.getHors();
        int ai1[] = container.getVeris();
        XLayoutContainer xlayoutcontainer = container.getOuterLayout();
        if(!ComparatorUtils.equals(xlayoutcontainer, container.getBackupParent()))
        {
            XWCardMainBorderLayout xwcardmainborderlayout = (XWCardMainBorderLayout)xlayoutcontainer;
            rectangle = adjustBackupBound(rectangle, xwcardmainborderlayout);
        }
        int k = xcreator.getWidth();
        int l = xcreator.getHeight();
        initCompsList();
        xcreator.setBounds(rectangle);
        int i1 = 0;
        if(i != rectangle.x)
            dealLeft(rectangle, i, ai, i1, xcreator);
        else
        if(k != rectangle.width)
            dealRight(rectangle, i, k, ai, i1, xcreator);
        else
        if(j != rectangle.y)
            dealTop(rectangle, j, ai1, i1, xcreator);
        else
        if(l != rectangle.height)
            dealButtom(rectangle, j, l, ai1, i1, xcreator);
        clearCompsList();
        XWFitLayout xwfitlayout = (XWFitLayout)container;
        xwfitlayout.updateBoundsWidget();
        updateCreatorBackBound();
    }

    private void dealLeft(Rectangle rectangle, int i, int ai[], int j, XCreator xcreator)
    {
        if(rectangle.x == margin.getLeft())
        {
            return;
        } else
        {
            i = adjustCoordinateByDependingLine(i, ai);
            j = i - rectangle.x;
            dealDirectionAtLeft(rectangle, j, xcreator);
            return;
        }
    }

    private void dealRight(Rectangle rectangle, int i, int j, int ai[], int k, XCreator xcreator)
    {
        if(rectangle.width + rectangle.x == container.getWidth() - margin.getRight())
        {
            return;
        } else
        {
            j = adjustDiffByDependingLine(i, ai, j);
            k = j - rectangle.width;
            dealDirectionAtRight(rectangle, k, xcreator);
            return;
        }
    }

    private void dealTop(Rectangle rectangle, int i, int ai[], int j, XCreator xcreator)
    {
        if(rectangle.y == margin.getTop())
        {
            return;
        } else
        {
            i = adjustCoordinateByDependingLine(i, ai);
            j = i - rectangle.y;
            dealDirectionAtTop(rectangle, j, xcreator);
            return;
        }
    }

    private void dealButtom(Rectangle rectangle, int i, int j, int ai[], int k, XCreator xcreator)
    {
        if(rectangle.y + rectangle.height == container.getHeight() - margin.getBottom())
        {
            return;
        } else
        {
            j = adjustDiffByDependingLine(i, ai, j);
            k = j - rectangle.height;
            dealDirectionABottom(rectangle, k, xcreator);
            return;
        }
    }

    private int adjustCoordinateByDependingLine(int i, int ai[])
    {
        int j = 0;
        do
        {
            if(j >= ai.length)
                break;
            if(i != ai[j] && i > ai[j] - 3 && i < ai[j] + 3)
            {
                i = ai[j];
                break;
            }
            j++;
        } while(true);
        return i;
    }

    private int adjustDiffByDependingLine(int i, int ai[], int j)
    {
        int k = 0;
        do
        {
            if(k >= ai.length)
                break;
            if(i + j > ai[k] - 3 && i + j < ai[k] + 3)
            {
                j = ai[k] - i;
                break;
            }
            k++;
        } while(true);
        return j;
    }

    private void dealDirectionAtLeft(Rectangle rectangle, int i, Component component)
    {
        rightComps.add(component);
        Object obj = null;
        int j = rectangle.x - 5 - actualVal;
        int k = rectangle.x + 5;
        Component component3 = container.getLeftComp(rectangle.x, rectangle.y);
        leftComps.add(component3);
        int l = rectangle.y;
        int i1 = component3.getY();
        int k1 = margin.getTop();
        int l1 = container.getHeight() - margin.getBottom();
        while(l >= k1 && i1 >= k1 && l != i1) 
            if(l > i1)
            {
                Component component1 = container.getTopComp(k, l);
                l = component1.getY();
                rightComps.add(component1);
            } else
            {
                Component component4 = container.getTopComp(j, i1);
                i1 = component4.getY();
                leftComps.add(component4);
            }
        l = rectangle.y + rectangle.height;
        for(int j1 = ((Component)leftComps.get(0)).getY() + ((Component)leftComps.get(0)).getHeight(); l <= l1 && j1 <= l1 && l != j1;)
            if(l > j1)
            {
                Component component5 = container.getComponentAt(j, j1 + 5 + actualVal);
                j1 = component5.getY() + component5.getHeight();
                leftComps.add(component5);
            } else
            {
                Component component2 = container.getComponentAt(k, l + 5 + actualVal);
                l = component2.getY() + component2.getHeight();
                rightComps.add(component2);
            }

        dealHorDirection(rectangle.x, i);
    }

    private void dealDirectionAtRight(Rectangle rectangle, int i, Component component)
    {
        leftComps.add(component);
        Object obj = null;
        int j = (rectangle.x + rectangle.width) - 5;
        int k = rectangle.x + rectangle.width + 5 + actualVal;
        Component component3 = container.getRightComp(rectangle.x, rectangle.y, rectangle.width);
        rightComps.add(component3);
        int l = rectangle.y;
        int i1 = component3.getY();
        int k1 = margin.getTop();
        int l1 = container.getHeight() - margin.getBottom();
        while(i1 >= k1 && l >= k1 && i1 != l) 
            if(i1 > l)
            {
                Component component4 = container.getTopComp(k, i1);
                i1 = component4.getY();
                rightComps.add(component4);
            } else
            {
                Component component1 = container.getTopComp(j, l);
                l = component1.getY();
                leftComps.add(component1);
            }
        l = rectangle.y + rectangle.height;
        for(int j1 = ((Component)rightComps.get(0)).getY() + ((Component)rightComps.get(0)).getHeight(); j1 <= l1 && l <= l1 && j1 != l;)
            if(j1 > l)
            {
                Component component2 = container.getComponentAt(j, l + 5 + actualVal);
                l = component2.getY() + component2.getHeight();
                leftComps.add(component2);
            } else
            {
                Component component5 = container.getComponentAt(k, j1 + 5 + actualVal);
                j1 = component5.getY() + component5.getHeight();
                rightComps.add(component5);
            }

        dealHorDirection(rectangle.x + rectangle.width + actualVal, i);
    }

    private void dealHorDirection(int i, int j)
    {
        if(j > 0)
            j = Math.min(getMinWidth(rightComps) - minWidth, j);
        else
            j = Math.max(j, minWidth - getMinWidth(leftComps));
        if(CalculateLefttRelatComponent(j))
            CalculateRightRelatComponent(i + j, -j);
    }

    private void dealDirectionAtTop(Rectangle rectangle, int i, Component component)
    {
        downComps.add(component);
        int j = rectangle.y - 5 - actualVal;
        int k = rectangle.y + 5;
        Component component1 = container.getTopComp(rectangle.x, rectangle.y);
        upComps.add(component1);
        Object obj = null;
        int l = margin.getLeft();
        int i1 = container.getWidth() - margin.getRight();
        int j1 = component1.getX();
        for(int k1 = rectangle.x; j1 >= l && k1 >= l && j1 != k1;)
            if(j1 < k1)
            {
                Component component4 = container.getLeftComp(k1, k);
                k1 = component4.getX();
                downComps.add(component4);
            } else
            {
                Component component2 = container.getLeftComp(j1, j);
                j1 = component2.getX();
                upComps.add(component2);
            }

        j1 = ((Component)upComps.get(0)).getX() + ((Component)upComps.get(0)).getWidth();
        for(int l1 = rectangle.x + rectangle.width; j1 <= i1 && l1 <= i1 && j1 != l1;)
            if(j1 < l1)
            {
                Component component3 = container.getComponentAt(j1 + 5 + actualVal, j);
                j1 = component3.getX() + component3.getWidth();
                upComps.add(component3);
            } else
            {
                Component component5 = container.getComponentAt(l1 + 5 + actualVal, k);
                l1 = component5.getX() + component5.getWidth();
                downComps.add(component5);
            }

        dealVertiDirection(rectangle.y, i);
    }

    private void dealDirectionABottom(Rectangle rectangle, int i, Component component)
    {
        upComps.add(component);
        Object obj = null;
        Component component3 = container.getBottomComp(rectangle.x, rectangle.y, rectangle.height);
        int j = rectangle.y + rectangle.height + 5 + actualVal;
        int k = (rectangle.y + rectangle.height) - 5;
        downComps.add(component3);
        int l = component3.getX();
        int i1 = rectangle.x;
        int k1 = margin.getLeft();
        int l1 = container.getWidth() - margin.getRight();
        while(i1 >= k1 && l >= k1 && i1 != l) 
            if(i1 < l)
            {
                Component component4 = container.getLeftComp(l, j);
                l = component4.getX();
                downComps.add(component4);
            } else
            {
                Component component1 = container.getLeftComp(i1, k);
                i1 = component1.getX();
                upComps.add(component1);
            }
        l = ((Component)downComps.get(0)).getX() + ((Component)downComps.get(0)).getWidth();
        for(int j1 = rectangle.x + rectangle.width; j1 <= l1 && l <= l1 && j1 != l;)
            if(j1 < l)
            {
                Component component2 = container.getComponentAt(j1 + 5 + actualVal, k);
                j1 = component2.getX() + component2.getWidth();
                upComps.add(component2);
            } else
            {
                Component component5 = container.getComponentAt(l + 5 + actualVal, j);
                l = component5.getX() + component5.getWidth();
                downComps.add(component5);
            }

        dealVertiDirection(rectangle.y + rectangle.height + actualVal, i);
    }

    private void dealVertiDirection(int i, int j)
    {
        if(j > 0)
            j = Math.min(getMinHeight(downComps) - minHeight, j);
        else
            j = Math.max(j, minHeight - getMinHeight(upComps));
        if(CalculateUpRelatComponent(j))
            CalculateDownRelatComponent(i + j, -j);
    }

    public void fix(XCreator xcreator, int i, int j)
    {
        Component component = container.getComponentAt(i, j);
        if(container.getComponentCount() == 0)
        {
            xcreator.setLocation(0, 0);
            xcreator.setSize(component.getWidth(), component.getHeight());
        } else
        {
            if(isCrossPointArea(component, i, j))
            {
                fixCrossPointArea(component, xcreator, i, j);
                return;
            }
            if(isTrisectionArea(component, i, j))
            {
                fixTrisect(component, xcreator, i, j);
                return;
            }
            fixHalve(component, xcreator, i, j);
        }
    }

    private void fixHalve(Component component, XCreator xcreator, int i, int j)
    {
        XCreator xcreator1 = (XCreator)component;
        if(!xcreator1.getTargetChildrenList().isEmpty())
        {
            fixHalveOfTab(xcreator1, xcreator, i, j);
            return;
        }
        int k = component.getWidth();
        int l = component.getHeight();
        int i1 = component.getX();
        int j1 = component.getY();
        Dimension dimension = new Dimension();
        boolean flag = (double)(j - j1) <= (double)l * 0.25D;
        boolean flag1 = (double)(j - j1) >= (double)l * 0.75D;
        boolean flag2 = i - i1 < k / 2;
        int k1 = i1;
        int l1 = j1;
        int i2 = k;
        int j2 = l;
        if(flag)
        {
            dimension.width = k;
            dimension.height = l / 2 - actualVal / 2;
            l1 = j1 + dimension.height + actualVal;
            j2 = l - dimension.height - actualVal;
        } else
        if(flag1)
        {
            dimension.height = l / 2 - actualVal / 2;
            dimension.width = k;
            j2 = l - dimension.height - actualVal;
            j1 = j1 + j2 + actualVal;
        } else
        if(flag2)
        {
            dimension.width = k / 2 - actualVal / 2;
            dimension.height = l;
            k1 = i1 + dimension.width + actualVal;
            i2 = k - dimension.width - actualVal;
        } else
        {
            i2 = k / 2 - actualVal / 2;
            i1 = i1 + i2 + actualVal;
            dimension.width = k - i2 - actualVal;
            dimension.height = l;
        }
        if(isCalculateChildPos)
        {
            childPosition = (new int[] {
                i1, j1, dimension.width, dimension.height
            });
        } else
        {
            component.setLocation(k1, l1);
            component.setSize(i2, j2);
            xcreator.setLocation(i1, j1);
            xcreator.setSize(dimension);
        }
    }

    private void fixHalveOfTab(XCreator xcreator, XCreator xcreator1, int i, int j)
    {
        int k = xcreator.getWidth();
        int l = xcreator.getHeight();
        int i1 = xcreator.getX();
        int j1 = xcreator.getY();
        Dimension dimension = new Dimension();
        int k1 = getPositionOfFix(xcreator, i, j);
        int l1 = i1;
        int i2 = j1;
        int j2 = k;
        int k2 = l;
        switch(k1)
        {
        case 1: // '\001'
            dimension.width = k;
            dimension.height = l / 2;
            i2 = j1 + dimension.height;
            k2 = l - dimension.height;
            break;

        case 2: // '\002'
            dimension.height = l / 2;
            dimension.width = k;
            k2 = l - dimension.height;
            j1 += k2;
            break;

        case 3: // '\003'
            dimension.width = k / 2;
            dimension.height = l;
            l1 = i1 + dimension.width;
            j2 = k - dimension.width;
            break;

        default:
            j2 = k / 2;
            i1 += j2;
            dimension.width = k - j2;
            dimension.height = l;
            break;
        }
        if(isCalculateChildPos)
        {
            childPosition = (new int[] {
                i1, j1, dimension.width, dimension.height
            });
        } else
        {
            xcreator.setLocation(l1, i2);
            xcreator.setSize(j2, k2);
            xcreator.recalculateChildWidth(j2);
            xcreator.recalculateChildHeight(k2);
            xcreator1.setLocation(i1, j1);
            xcreator1.setSize(dimension);
        }
    }

    private int getPositionOfFix(XCreator xcreator, int i, int j)
    {
        byte byte0 = 0;
        XWCardLayout xwcardlayout = ((XWCardMainBorderLayout)xcreator).getCardPart();
        XLayoutContainer xlayoutcontainer = (XLayoutContainer)xwcardlayout.getComponent(0);
        Rectangle rectangle = ComponentUtils.getRelativeBounds(xlayoutcontainer);
        int k = i - rectangle.x;
        int l = (j - rectangle.y) + 36;
        int i1 = xlayoutcontainer.getX();
        int j1 = xlayoutcontainer.getY();
        int k1 = xlayoutcontainer.getWidth();
        int l1 = xlayoutcontainer.getHeight();
        Rectangle rectangle1 = new Rectangle(k, l, 1, 1);
        Rectangle rectangle2 = new Rectangle(i1, j1, k1, 10);
        if(rectangle2.intersects(rectangle1))
            byte0 = 1;
        int i2 = (j1 + l1) - 10;
        Rectangle rectangle3 = new Rectangle(i1, i2, k1, 10);
        if(rectangle3.intersects(rectangle1))
            byte0 = 2;
        int j2 = l1 - 20;
        int k2 = j1 + 10;
        Rectangle rectangle4 = new Rectangle(i1, k2, 10, j2);
        if(rectangle4.intersects(rectangle1))
            byte0 = 3;
        return byte0;
    }

    private void fixCrossPointArea(Component component, XCreator xcreator, int i, int j)
    {
        initCompsList();
        switch(crossPointAreaDirect)
        {
        case 5: // '\005'
            dealCrossPointAtLeftTop(component, xcreator);
            break;

        case 8: // '\b'
            dealCrossPointAtRightBottom(component, xcreator);
            break;

        case 6: // '\006'
            dealCrossPointAtLeftBottom(component, xcreator);
            break;

        case 7: // '\007'
            dealCrossPointAtRightTop(component, xcreator);
            break;

        case 1: // '\001'
            dealCrossPointAtTop(component, xcreator);
            break;

        case 2: // '\002'
            dealCrossPointAtBottom(component, xcreator);
            break;

        case 3: // '\003'
            dealCrossPointAtLeft(component, xcreator);
            break;

        case 4: // '\004'
            dealCrossPointAtRight(component, xcreator);
            break;
        }
        crossPointAreaDirect = 0;
        clearCompsList();
    }

    private void dealCrossPointAtLeftTop(Component component, XCreator xcreator)
    {
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        int k1 = component.getX();
        int l1 = component.getY();
        int i2 = component.getHeight();
        int j2 = component.getWidth();
        Component component1 = container.getTopComp(k1, l1);
        Component component2 = container.getLeftComp(k1, l1);
        if(component1 == null || component1.getX() != k1)
        {
            int i = i2 >= component2.getHeight() ? component2.getHeight() : i2;
            downComps.add(component2);
            downComps.add(component);
            int k2 = i / 2;
            int k = component2.getWidth() + j2 + actualVal;
            int i1 = k2 - actualVal / 2;
            if(isCalculateChildPos)
            {
                childPosition = (new int[] {
                    component2.getX(), component2.getY(), k, i1
                });
            } else
            {
                xcreator.setLocation(component2.getX(), component2.getY());
                xcreator.setSize(k, i1);
                calculateBottomComps(k2);
            }
        } else
        {
            rightComps.add(component);
            rightComps.add(component1);
            int j = j2 >= component1.getWidth() ? component1.getWidth() : j2;
            int l2 = j / 2;
            int l = l2 - actualVal / 2;
            int j1 = component.getHeight() + component1.getHeight() + actualVal;
            if(isCalculateChildPos)
            {
                childPosition = (new int[] {
                    component1.getX(), component1.getY(), l, j1
                });
            } else
            {
                xcreator.setLocation(component1.getX(), component1.getY());
                xcreator.setSize(l, j1);
                calculateRightComps(l2);
            }
        }
    }

    private void dealCrossPointAtRightBottom(Component component, XCreator xcreator)
    {
        boolean flag = false;
        boolean flag1 = false;
        int k = component.getHeight();
        int l = component.getWidth();
        int i1 = component.getX();
        int j1 = component.getY();
        Component component1 = container.getRightBottomComp(i1, j1, k, l);
        Component component2 = container.getBottomRightComp(i1, j1, k, l);
        if(component1 == null || component1.getX() + component1.getWidth() != i1 + l)
        {
            int i = k >= component2.getHeight() ? component2.getHeight() : k;
            upComps.add(component);
            upComps.add(component2);
            int k1 = i / 2;
            calculateTopComps(k1, xcreator, k1);
        } else
        {
            leftComps.add(component);
            leftComps.add(component1);
            int j = l >= component1.getWidth() ? component1.getWidth() : l;
            int l1 = j / 2;
            calculateLeftComps(l1, xcreator, l1);
        }
    }

    private void dealCrossPointAtLeftBottom(Component component, XCreator xcreator)
    {
        boolean flag = false;
        boolean flag1 = false;
        int k = component.getX();
        int l = component.getY();
        int i1 = component.getHeight();
        int j1 = component.getWidth();
        Component component1 = container.getBottomComp(k, l, i1);
        Component component2 = container.getBottomLeftComp(k, l, i1);
        if(component2 == null || component2.getY() + component2.getHeight() != l + i1)
        {
            rightComps.add(component);
            rightComps.add(component1);
            int j = j1 >= component1.getWidth() ? component1.getWidth() : j1;
            int k1 = j / 2;
            int i2 = k1 - actualVal / 2;
            int j2 = component.getHeight() + component1.getHeight() + actualVal;
            if(isCalculateChildPos)
            {
                childPosition = (new int[] {
                    component.getX(), component.getY(), i2, j2
                });
            } else
            {
                xcreator.setLocation(component.getX(), component.getY());
                xcreator.setSize(i2, j2);
                calculateRightComps(k1);
            }
        } else
        {
            upComps.add(component);
            upComps.add(component2);
            int i = i1 >= component2.getHeight() ? component2.getHeight() : i1;
            int l1 = i / 2;
            calculateTopComps(l1, xcreator, l1);
        }
    }

    private void dealCrossPointAtRightTop(Component component, XCreator xcreator)
    {
        boolean flag = false;
        boolean flag1 = false;
        int k = component.getX();
        int l = component.getY();
        int i1 = component.getHeight();
        int j1 = component.getWidth();
        Component component1 = container.getRightTopComp(k, l, j1);
        Component component2 = container.getRightComp(k, l, j1);
        if(component2 == null || component2.getY() != l)
        {
            leftComps.add(component);
            leftComps.add(component1);
            int j = j1 >= component1.getWidth() ? component1.getWidth() : j1;
            int k1 = j / 2;
            calculateLeftComps(k1, xcreator, k1);
        } else
        {
            int i = i1 >= component2.getHeight() ? component2.getHeight() : i1;
            downComps.add(component);
            downComps.add(component2);
            int l1 = i / 2;
            int i2 = j1 + component2.getWidth() + actualVal;
            int j2 = l1 - actualVal / 2;
            if(isCalculateChildPos)
            {
                childPosition = (new int[] {
                    component.getX(), component.getY(), i2, j2
                });
            } else
            {
                xcreator.setLocation(component.getX(), component.getY());
                xcreator.setSize(i2, j2);
                calculateBottomComps(l1);
            }
        }
    }

    private void dealCrossPointAtTop(Component component, XCreator xcreator)
    {
        int i = 0;
        int j = component.getX();
        int k = component.getY();
        int l = component.getHeight();
        int i1 = component.getWidth();
        Component component1 = container.getLeftComp(j, k);
        Component component2 = container.getRightComp(j, k, i1);
        i = Math.min(component2.getHeight(), Math.min(l, component1.getHeight()));
        downComps.add(component1);
        downComps.add(component);
        downComps.add(component2);
        int j1 = i / 2;
        int k1 = i1 + component1.getWidth() + component2.getWidth() + actualVal * 2;
        int l1 = j1 - actualVal / 2;
        if(isCalculateChildPos)
        {
            childPosition = (new int[] {
                component1.getX(), component1.getY(), k1, l1
            });
        } else
        {
            xcreator.setLocation(component1.getX(), component1.getY());
            xcreator.setSize(k1, l1);
            calculateBottomComps(j1);
        }
    }

    private void dealCrossPointAtBottom(Component component, XCreator xcreator)
    {
        int i = 0;
        int j = component.getX();
        int k = component.getY();
        int l = component.getHeight();
        int i1 = component.getWidth();
        Component component1 = container.getBottomLeftComp(j, k, l);
        Component component2 = container.getBottomRightComp(j, k, l, i1);
        i = Math.min(component2.getHeight(), Math.min(l, component1.getHeight()));
        upComps.add(component1);
        upComps.add(component);
        upComps.add(component2);
        int j1 = i / 2;
        calculateTopComps(j1, xcreator, j1);
    }

    private void dealCrossPointAtRight(Component component, XCreator xcreator)
    {
        int i = 0;
        int j = component.getX();
        int k = component.getY();
        int l = component.getHeight();
        int i1 = component.getWidth();
        Component component1 = container.getRightTopComp(j, k, i1);
        Component component2 = container.getRightBottomComp(j, k, l, i1);
        i = Math.min(component1.getWidth(), Math.min(i1, component2.getWidth()));
        leftComps.add(component1);
        leftComps.add(component);
        leftComps.add(component2);
        int j1 = i / 2;
        calculateLeftComps(j1, xcreator, j1);
    }

    private void dealCrossPointAtLeft(Component component, XCreator xcreator)
    {
        int i = 0;
        int j = component.getX();
        int k = component.getY();
        int l = component.getHeight();
        int i1 = component.getWidth();
        Component component1 = container.getTopComp(j, k);
        Component component2 = container.getBottomComp(j, k, l);
        i = Math.min(component1.getWidth(), Math.min(i1, component2.getWidth()));
        rightComps.add(component1);
        rightComps.add(component);
        rightComps.add(component2);
        int j1 = i / 2;
        int k1 = j1 - actualVal / 2;
        int l1 = component1.getHeight() + component.getHeight() + component2.getHeight() + actualVal * 2;
        if(isCalculateChildPos)
        {
            childPosition = (new int[] {
                component1.getX(), component1.getY(), k1, l1
            });
        } else
        {
            xcreator.setLocation(component1.getX(), component1.getY());
            xcreator.setSize(k1, l1);
            calculateRightComps(j1);
        }
    }

    private void fixTrisect(Component component, XCreator xcreator, int i, int j)
    {
        boolean flag = false;
        int i1 = 0;
        int j1 = 0;
        boolean flag1 = false;
        int i2 = component.getX();
        int j2 = component.getY();
        int k2 = component.getHeight();
        int l2 = component.getWidth();
        isFindRelatedComps = true;
        if(ComparatorUtils.equals(Integer.valueOf(trisectAreaDirect), Integer.valueOf(1)))
        {
            int k = getUpMinHeightComp(j2, i);
            i1 = getDownMinHeightComp(component, j);
            dealTrisectAtTop(xcreator, k, i1);
        } else
        if(ComparatorUtils.equals(Integer.valueOf(trisectAreaDirect), Integer.valueOf(2)))
        {
            int l = getUpMinHeightComp(j2 + k2 + actualVal, i);
            if(j2 + k2 + 5 < container.getHeight() - margin.getBottom())
            {
                Component component1 = container.getBottomComp(i, j2, k2);
                i1 = getDownMinHeightComp(component1, j2 + k2 + 5 + actualVal);
            }
            dealTrisectAtTop(xcreator, l, i1);
        } else
        if(ComparatorUtils.equals(Integer.valueOf(trisectAreaDirect), Integer.valueOf(4)))
        {
            int k1 = getMinRightWidth(i2, l2, j);
            j1 = getMinLeftWidth(component, i);
            dealTrisectAtRight(xcreator, j1, k1);
        } else
        if(ComparatorUtils.equals(Integer.valueOf(trisectAreaDirect), Integer.valueOf(3)))
        {
            int l1 = getMinRightWidth(i2, 0, j);
            if(i2 - 5 > margin.getLeft())
            {
                Component component2 = container.getLeftComp(i2, j);
                j1 = getMinLeftWidth(component2, i2 - 5);
            }
            dealTrisectAtRight(xcreator, j1, l1);
        }
        crossPointAreaDirect = 0;
        clearCompsList();
    }

    private void dealTrisectAtTop(XCreator xcreator, int i, int j)
    {
        int k = ((i + j) - actualVal) / 3;
        int l = 0;
        int i1 = 0;
        if(j == 0)
        {
            l = 0;
            i1 = i / 2;
            calculateTopComps(i1, xcreator, i1);
            return;
        }
        if(i == 0)
        {
            l = j / 2;
            int j1 = container.getWidth() - margin.getLeft() - margin.getRight();
            if(!isCalculateChildPos)
            {
                calculateBottomComps(l);
                xcreator.setLocation(margin.getLeft(), margin.getRight());
                xcreator.setSize(j1, l - actualVal / 2);
            } else
            {
                childPosition = (new int[] {
                    margin.getLeft(), margin.getRight(), j1, l - actualVal / 2
                });
            }
            return;
        }
        if(i >= j)
        {
            j -= actualVal / 2;
            if((j * 2) / 3 < minHeight)
                l = j - minHeight;
            else
                l = j / 3;
            i1 = k - l;
        } else
        {
            i -= actualVal / 2;
            if((i * 2) / 3 < minHeight)
                i1 = i - minHeight;
            else
                i1 = i / 3;
            l = k - i1;
        }
        if(!isCalculateChildPos)
            calculateBottomComps(l);
        k += actualVal / 2;
        calculateTopComps(i1, xcreator, k);
    }

    private void dealTrisectAtRight(XCreator xcreator, int i, int j)
    {
        int k = ((i + j) - actualVal) / 3;
        int l = 0;
        int i1 = 0;
        if(i == 0)
        {
            i1 = j / 2;
            int j1 = container.getHeight() - margin.getBottom() - margin.getTop();
            if(!isCalculateChildPos)
            {
                calculateRightComps(i1);
                xcreator.setLocation(margin.getLeft(), margin.getRight());
                xcreator.setSize(i1 - actualVal / 2, j1);
            } else
            {
                childPosition = (new int[] {
                    margin.getLeft(), margin.getRight(), i1 - actualVal / 2, j1
                });
            }
            return;
        }
        if(j == 0)
        {
            l = i / 2;
            calculateLeftComps(l, xcreator, l);
            return;
        }
        if(j >= i)
        {
            i -= actualVal / 2;
            if((i * 2) / 3 < minWidth)
                l = i - minWidth;
            else
                l = i / 3;
            i1 = k - l;
        } else
        {
            j -= actualVal / 2;
            if((j * 2) / 3 < minWidth)
                i1 = j - minWidth;
            else
                i1 = j / 3;
            l = k - i1;
        }
        if(!isCalculateChildPos)
            calculateRightComps(i1);
        k += actualVal / 2;
        calculateLeftComps(l, xcreator, k);
    }

    private void calculateBottomComps(int i)
    {
        i += actualVal / 2;
        int j = 0;
        for(int k = downComps.size(); j < k; j++)
        {
            Component component = (Component)downComps.get(j);
            component.setLocation(component.getX(), component.getY() + i);
            int l = component.getHeight() - i;
            component.setSize(component.getWidth(), l);
            XCreator xcreator = (XCreator)component;
            xcreator.recalculateChildHeight(l);
        }

    }

    private void calculateTopComps(int i, XCreator xcreator, int j)
    {
        i += actualVal / 2;
        int k = (upComps.size() - 1) * actualVal;
        int l = container.getWidth() - margin.getLeft() - margin.getRight();
        int i1 = 0;
        if(upComps.size() > 0)
            i1 = (((Component)upComps.get(0)).getY() + ((Component)upComps.get(0)).getHeight()) - i;
        int j1 = 0;
        for(int k1 = upComps.size(); j1 < k1; j1++)
        {
            Component component = (Component)upComps.get(j1);
            k += component.getWidth();
            if(component.getX() < l)
                l = component.getX();
            if(!isCalculateChildPos)
            {
                int l1 = component.getHeight() - i;
                component.setSize(component.getWidth(), l1);
                XCreator xcreator1 = (XCreator)component;
                xcreator1.recalculateChildHeight(l1);
            }
        }

        i1 += actualVal;
        j -= actualVal / 2;
        if(isCalculateChildPos)
        {
            childPosition = (new int[] {
                l, i1, k, j
            });
        } else
        {
            xcreator.setLocation(l, i1);
            xcreator.setSize(k, j);
        }
    }

    private void calculateLeftComps(int i, XCreator xcreator, int j)
    {
        i += actualVal / 2;
        if(leftComps.isEmpty())
            return;
        int k = (leftComps.size() - 1) * actualVal;
        int l = 0;
        if(leftComps.size() > 0)
            l = (((Component)leftComps.get(0)).getX() + ((Component)leftComps.get(0)).getWidth()) - i;
        int i1 = container.getHeight() - margin.getBottom();
        int j1 = 0;
        for(int k1 = leftComps.size(); j1 < k1; j1++)
        {
            Component component = (Component)leftComps.get(j1);
            k += component.getHeight();
            if(component.getY() < i1)
                i1 = component.getY();
            if(!isCalculateChildPos)
            {
                int l1 = component.getWidth() - i;
                component.setSize(l1, component.getHeight());
                XCreator xcreator1 = (XCreator)component;
                xcreator1.recalculateChildWidth(l1);
            }
        }

        l += actualVal;
        j -= actualVal / 2;
        if(isCalculateChildPos)
        {
            childPosition = (new int[] {
                l, i1, j, k
            });
        } else
        {
            xcreator.setLocation(l, i1);
            xcreator.setSize(j, k);
        }
    }

    private void calculateRightComps(int i)
    {
        i += actualVal / 2;
        int j = 0;
        for(int k = rightComps.size(); j < k; j++)
        {
            Component component = (Component)rightComps.get(j);
            component.setLocation(component.getX() + i, component.getY());
            int l = component.getWidth() - i;
            component.setSize(l, component.getHeight());
            XCreator xcreator = (XCreator)component;
            xcreator.recalculateChildWidth(l);
        }

    }

    protected void delete(XCreator xcreator, int i, int j)
    {
        int k = xcreator.getX();
        int l = xcreator.getY();
        recalculateChildrenSize(k, l, i, j);
        updateCreatorBackBound();
    }

    public void recalculateChildrenSize(int i, int j, int k, int l)
    {
        if(container.getComponentCount() == 0)
            return;
        initCompsList();
        int i1 = k;
        int j1 = l;
        calculateRelatedComponent(i, j, i1, j1);
        if(!rightComps.isEmpty() && getAllHeight(rightComps) == j1)
            CalculateRightRelatComponent(i, i1 + actualVal);
        else
        if(!leftComps.isEmpty() && getAllHeight(leftComps) == j1)
            CalculateLefttRelatComponent(i1 + actualVal);
        else
        if(!downComps.isEmpty() && getAllWidth(downComps) == i1)
            CalculateDownRelatComponent(j, j1 + actualVal);
        else
        if(!upComps.isEmpty() && getAllWidth(upComps) == i1)
            CalculateUpRelatComponent(j1 + actualVal);
        else
            calculateNoRelatedComponent(i, j, i1, j1);
        clearCompsList();
    }

    private void calculateNoRelatedComponent(int i, int j, int k, int l)
    {
        if(container.getComponentCount() <= 1)
            return;
        Component component = container.getRightComp(i, j, k);
        if(component == null)
            return;
        int i1 = component.getY();
        clearCompsList();
        initCompsList();
        Rectangle rectangle = new Rectangle(i, j, k, l);
        if(i1 != j)
            calculateNoRelatedWhileRightTop(rectangle, component);
        else
            calculateNoRelatedWhileRightBott(rectangle, component);
    }

    private void calculateNoRelatedWhileRightTop(Rectangle rectangle, Component component)
    {
        if(component == null)
            return;
        int i = component.getY();
        int j = component.getHeight();
        int k = component.getWidth();
        int l = rectangle.y - i - actualVal;
        if(l < minHeight)
        {
            dealDirectionAtTop(component.getBounds(), l + actualVal, component);
            if(component.getY() != rectangle.y)
            {
                clearCompsList();
                initCompsList();
                dealDirectionAtTop(component.getBounds(), rectangle.y - component.getY() - minHeight - actualVal, component);
                i = component.getY();
                int i1 = component.getX();
                component.setBounds(i1, i, k, minHeight);
                recalculateChildrenSize(i1, rectangle.y, k, j - l - actualVal);
                recalculateChildrenSize(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                return;
            }
        } else
        if(j + i == rectangle.y + rectangle.height)
        {
            component.setSize(k, l);
            rectangle.width += k;
            rectangle.width += actualVal;
        } else
        {
            recalculateChildrenSize(rectangle.x, i + j + actualVal, rectangle.width, (rectangle.height + rectangle.y) - j - i - actualVal);
            recalculateChildrenSize(rectangle.x, rectangle.y, rectangle.width, (i + j) - rectangle.y);
            return;
        }
        recalculateChildrenSize(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    private void calculateNoRelatedWhileRightBott(Rectangle rectangle, Component component)
    {
        component = container.getBottomRightComp(rectangle.x, rectangle.y, rectangle.height, rectangle.width);
        int i = component.getY();
        int j = component.getHeight();
        int l = component.getWidth();
        int i1 = (i + j) - rectangle.y - rectangle.height - actualVal;
        if(i1 < minHeight)
        {
            dealDirectionABottom(component.getBounds(), -i1 - actualVal, component);
            if(component.getHeight() + i != rectangle.y + rectangle.height)
            {
                clearCompsList();
                initCompsList();
                i1 = (i + component.getHeight()) - rectangle.y - rectangle.height - actualVal;
                dealDirectionABottom(component.getBounds(), minHeight - i1, component);
                int k = component.getHeight();
                int j1 = component.getX();
                component.setBounds(j1, rectangle.y + rectangle.height + actualVal, l, minHeight);
                recalculateChildrenSize(j1, i, l, k - minHeight - actualVal);
                recalculateChildrenSize(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                return;
            }
        } else
        if(i == rectangle.y)
        {
            component.setBounds(component.getX(), rectangle.y + rectangle.height + actualVal, l, i1);
            rectangle.width += l;
            rectangle.width += actualVal;
        } else
        {
            recalculateChildrenSize(rectangle.x, rectangle.y, rectangle.width, i - rectangle.y - actualVal);
            recalculateChildrenSize(rectangle.x, i, rectangle.width, (rectangle.height - i) + rectangle.y);
            return;
        }
        recalculateChildrenSize(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    private int getMinWidth(java.util.List list)
    {
        if(list.isEmpty())
            return 0;
        int i = container.getWidth() - margin.getLeft() - margin.getRight();
        int j = 0;
        for(int k = list.size(); j < k; j++)
            i = i <= ((Component)list.get(j)).getWidth() ? i : ((Component)list.get(j)).getWidth();

        return i;
    }

    private int getMinHeight(java.util.List list)
    {
        if(list.isEmpty())
            return 0;
        int i = container.getHeight() - margin.getTop() - margin.getBottom();
        int j = 0;
        for(int k = list.size(); j < k; j++)
            i = i <= ((Component)list.get(j)).getHeight() ? i : ((Component)list.get(j)).getHeight();

        return i;
    }

    private int getAllHeight(java.util.List list)
    {
        int i = 0;
        if(list.isEmpty())
            return i;
        int j = list.size();
        for(int k = 0; k < j; k++)
            i += ((Component)list.get(k)).getHeight();

        i += (j - 1) * actualVal;
        return i;
    }

    private int getAllWidth(java.util.List list)
    {
        int i = 0;
        if(list.isEmpty())
            return i;
        int j = list.size();
        for(int k = 0; k < j; k++)
            i += ((Component)list.get(k)).getWidth();

        i += (j - 1) * actualVal;
        return i;
    }

    protected void calculateRelatedComponent(int i, int j, int k, int l)
    {
        int i1 = container.getComponentCount();
        for(int j1 = 0; j1 < i1; j1++)
        {
            Component component = container.getComponent(j1);
            int k1 = component.getX();
            int l1 = component.getY();
            int i2 = component.getWidth();
            int j2 = component.getHeight();
            int k2 = l1 - j;
            int l2 = k1 - i;
            boolean flag = k2 >= 0 && l >= j2 + k2;
            boolean flag1 = l2 >= 0 && k >= i2 + l2;
            if(flag && i + k + actualVal == k1)
            {
                rightComps.add(component);
                continue;
            }
            if(flag && i == k1 + i2 + actualVal)
            {
                leftComps.add(component);
                continue;
            }
            if(flag1 && j + l + actualVal == l1)
            {
                downComps.add(component);
                continue;
            }
            if(flag1 && j == l1 + j2 + actualVal)
                upComps.add(component);
        }

    }

    private int getCompsMinWidth(java.util.List list)
    {
        return getMaxCompsNum(list, true) * WLayout.MIN_WIDTH;
    }

    private int getCompsMinHeight(java.util.List list)
    {
        for(int i = 0; i < list.size(); i++)
        {
            XCreator xcreator = (XCreator)list.get(i);
            ArrayList arraylist = xcreator.getTargetChildrenList();
            if(!arraylist.isEmpty())
                return getMaxCompsNum(list, false) * WLayout.MIN_HEIGHT + 36;
        }

        return WLayout.MIN_HEIGHT;
    }

    private int getMaxCompsNum(java.util.List list, boolean flag)
    {
        int i = 1;
        int j = 0;
        for(int k = list.size(); j < k; j++)
        {
            XCreator xcreator = (XCreator)list.get(j);
            ArrayList arraylist = xcreator.getTargetChildrenList();
            int l = arraylist.size();
            if(l <= 0)
                continue;
            for(int i1 = 0; i1 < l; i1++)
            {
                XWTabFitLayout xwtabfitlayout = (XWTabFitLayout)arraylist.get(i1);
                int ai[] = flag ? xwtabfitlayout.getHors(true) : xwtabfitlayout.getVeris(true);
                int j1 = ai.length - 1;
                i = Math.max(j1, i);
            }

        }

        return i;
    }

    private void adjustCompsSize(XCreator xcreator, int i, boolean flag)
    {
        ArrayList arraylist = xcreator.getTargetChildrenList();
        int j = arraylist.size();
        if(j > 0)
        {
            for(int k = 0; k < j; k++)
            {
                XWTabFitLayout xwtabfitlayout = (XWTabFitLayout)arraylist.get(k);
                xwtabfitlayout.setBackupBound(xwtabfitlayout.getBounds());
                int l = flag ? xwtabfitlayout.getWidth() : xwtabfitlayout.getHeight();
                double d = (double)i / (double)l;
                if(d < 0.0D && !xwtabfitlayout.canReduce(d))
                    return;
                setAdjustedSize(xwtabfitlayout, i, flag);
                for(int i1 = 0; i1 < xwtabfitlayout.getComponentCount(); i1++)
                {
                    XCreator xcreator1 = xwtabfitlayout.getXCreator(i1);
                    com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = xwtabfitlayout.toData().getBoundsWidget(xcreator1.toData());
                    boundswidget.setBounds(xwtabfitlayout.getComponent(i1).getBounds());
                }

                adjustCreatorsSize(d, xwtabfitlayout, flag);
            }

        }
    }

    private void setAdjustedHeight(XWTabFitLayout xwtabfitlayout, int i)
    {
        xwtabfitlayout.setSize(xwtabfitlayout.getWidth(), xwtabfitlayout.getHeight() + i);
    }

    private void setAdjustedSize(XWTabFitLayout xwtabfitlayout, int i, boolean flag)
    {
        if(i < 0)
            xwtabfitlayout.setReferDim(new Dimension(xwtabfitlayout.getWidth(), xwtabfitlayout.getHeight()));
        if(flag)
        {
            xwtabfitlayout.setSize(xwtabfitlayout.getWidth() + i, xwtabfitlayout.getHeight());
            return;
        } else
        {
            setAdjustedHeight(xwtabfitlayout, i);
            return;
        }
    }

    private void adjustCreatorsSize(double d, XWTabFitLayout xwtabfitlayout, boolean flag)
    {
        if(flag)
        {
            xwtabfitlayout.adjustCreatorsWidth(d);
            return;
        } else
        {
            xwtabfitlayout.adjustCreatorsHeight(d);
            return;
        }
    }

    protected void CalculateRightRelatComponent(int i, int j)
    {
        int k = rightComps.size();
        for(int l = 0; l < k; l++)
        {
            XCreator xcreator = (XCreator)rightComps.get(l);
            adjustCompsSize(xcreator, j, true);
            int i1 = xcreator.getY();
            int j1 = xcreator.getWidth();
            int k1 = xcreator.getHeight();
            xcreator.setLocation(i, i1);
            xcreator.setSize(j1 + j, k1);
        }

    }

    private boolean isBeyondAdjustWidthScope(int i)
    {
        boolean flag = false;
        flag = i >= 0 ? isBeyondWidthScope(i, rightComps) : isBeyondWidthScope(i, leftComps);
        return flag;
    }

    private boolean isBeyondWidthScope(int i, java.util.List list)
    {
        int j = getCompsMinWidth(list);
        for(int k = 0; k < list.size(); k++)
        {
            XCreator xcreator = (XCreator)list.get(k);
            if(Math.abs(i) > xcreator.getWidth() - j)
                return true;
        }

        return false;
    }

    protected boolean CalculateLefttRelatComponent(int i)
    {
        if(isBeyondAdjustWidthScope(i))
            return false;
        int j = leftComps.size();
        for(int k = 0; k < j; k++)
        {
            XCreator xcreator = (XCreator)leftComps.get(k);
            adjustCompsSize(xcreator, i, true);
            int l = xcreator.getWidth();
            int i1 = xcreator.getHeight();
            xcreator.setSize(l + i, i1);
        }

        return true;
    }

    protected void CalculateDownRelatComponent(int i, int j)
    {
        int k = downComps.size();
        for(int l = 0; l < k; l++)
        {
            XCreator xcreator = (XCreator)downComps.get(l);
            adjustCompsSize(xcreator, j, false);
            int i1 = xcreator.getX();
            int j1 = xcreator.getWidth();
            int k1 = xcreator.getHeight();
            xcreator.setLocation(i1, i);
            xcreator.setSize(j1, k1 + j);
        }

    }

    private boolean isBeyondAdjustHeightScope(int i)
    {
        boolean flag = false;
        flag = i >= 0 ? isBeyondHeightScope(i, downComps) : isBeyondHeightScope(i, upComps);
        return flag;
    }

    private boolean isBeyondHeightScope(int i, java.util.List list)
    {
        int j = getCompsMinHeight(list);
        for(int k = 0; k < list.size(); k++)
        {
            XCreator xcreator = (XCreator)list.get(k);
            if(Math.abs(i) > xcreator.getHeight() - j)
                return true;
        }

        return false;
    }

    protected boolean CalculateUpRelatComponent(int i)
    {
        if(isBeyondAdjustHeightScope(i))
            return false;
        int j = upComps.size();
        for(int k = 0; k < j; k++)
        {
            XCreator xcreator = (XCreator)upComps.get(k);
            adjustCompsSize(xcreator, i, false);
            int l = xcreator.getWidth();
            int i1 = xcreator.getHeight();
            xcreator.setSize(l, i1 + i);
        }

        return true;
    }

    public int[] getChildPosition(Component component, XCreator xcreator, int i, int j)
    {
        if(component == container)
            return (new int[] {
                0, 0, container.getWidth(), container.getHeight()
            });
        isCalculateChildPos = true;
        if(isCrossPointArea(component, i, j))
            fixCrossPointArea(component, xcreator, i, j);
        else
        if(isTrisectionArea(component, i, j))
            fixTrisect(component, xcreator, i, j);
        else
            fixHalve(component, xcreator, i, j);
        if(childPosition == null)
            childPosition = (new int[] {
                0, 0, 0, 0
            });
        isCalculateChildPos = false;
        return childPosition;
    }

    public ConstraintsGroupModel getLayoutConstraints(XCreator xcreator)
    {
        return new FRFitLayoutConstraints((XWFitLayout)container, xcreator);
    }
}
