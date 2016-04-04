// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRFitLayoutAdapter;
import com.fr.design.designer.creator.cardlayout.XWTabFitLayout;
import com.fr.design.form.layout.FRFitLayout;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.AbstractBorderStyleWidget;
import com.fr.form.ui.PaddingMargin;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WFitLayout;
import com.fr.form.ui.container.WLayout;
import com.fr.general.FRScreen;
import com.fr.stable.ArrayUtils;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ContainerEvent;
import java.util.ArrayList;
import java.util.Collections;

// Referenced classes of package com.fr.design.designer.creator:
//            XLayoutContainer, XCreator, XWidgetCreator, XCreatorUtils

public class XWFitLayout extends XLayoutContainer
{

    private static final long serialVersionUID = 0x7096d764e5902e50L;
    protected double containerPercent;
    private int needAddWidth;
    private int needAddHeight;
    private int minWidth;
    private int minHeight;
    private int backupGap;
    protected boolean hasCalGap;

    public XWFitLayout()
    {
        this(new WFitLayout(), new Dimension());
    }

    public XWFitLayout(WFitLayout wfitlayout, Dimension dimension)
    {
        super(wfitlayout, dimension);
        containerPercent = 1.0D;
        needAddWidth = 0;
        needAddHeight = 0;
        minWidth = WLayout.MIN_WIDTH;
        minHeight = WLayout.MIN_HEIGHT;
        backupGap = 0;
        hasCalGap = false;
        initPercent();
    }

    private void initPercent()
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        double d = FRScreen.getByDimension(dimension).getValue().doubleValue();
        if(d != 100D)
            setContainerPercent(d / 100D);
    }

    public LayoutAdapter getLayoutAdapter()
    {
        return new FRFitLayoutAdapter(this);
    }

    protected void initLayoutManager()
    {
        setLayout(new FRFitLayout());
    }

    protected String getIconName()
    {
        return "layout_absolute.png";
    }

    public int getNeedAddWidth()
    {
        return needAddWidth;
    }

    public void setNeedAddWidth(int i)
    {
        needAddWidth = i;
    }

    public int getNeedAddHeight()
    {
        return needAddHeight;
    }

    public void setNeedAddHeight(int i)
    {
        needAddHeight = i;
    }

    private void updateCreatorsBackupBound()
    {
        int i = 0;
        for(int j = getComponentCount(); i < j; i++)
        {
            Component component = getComponent(i);
            XCreator xcreator = (XCreator)component;
            xcreator.setBackupBound(component.getBounds());
        }

    }

    public void adjustCreatorsWhileSlide(double d)
    {
        int i = getComponentCount();
        if(i == 0)
        {
            Dimension dimension = new Dimension(getSize());
            dimension.width += (double)dimension.width * d;
            dimension.height += (double)dimension.height * d;
            setSize(dimension);
            return;
        }
        if(hasCalGap)
        {
            moveContainerMargin();
            moveCompInterval(backupGap);
            LayoutUtils.layoutContainer(this);
        }
        int j = 0;
        int k = 0;
        int ai[] = getHors(false);
        int ai1[] = getVeris(false);
        PaddingMargin paddingmargin = toData().getMargin();
        for(int l = 0; l < i; l++)
        {
            XCreator xcreator = getXCreator(l);
            Rectangle rectangle = modifyCreatorPoint(xcreator.getBounds(), d, ai, ai1);
            if(rectangle.x == paddingmargin.getLeft())
                k += rectangle.height;
            if(rectangle.y == paddingmargin.getTop())
                j += rectangle.width;
            xcreator.setBounds(rectangle);
            xcreator.updateChildBound(getActualMinHeight());
        }

        setSize(j + paddingmargin.getLeft() + paddingmargin.getRight(), k + paddingmargin.getTop() + paddingmargin.getBottom());
        updateCreatorsBackupBound();
        if(!hasCalGap)
        {
            moveContainerMargin();
            addCompInterval(getAcualInterval());
        }
        LayoutUtils.layoutContainer(this);
    }

    private Rectangle modifyCreatorPoint(Rectangle rectangle, double d, int ai[], int ai1[])
    {
        int i = 0;
        int j = 0;
        PaddingMargin paddingmargin = toData().getMargin();
        Rectangle rectangle1 = new Rectangle(rectangle);
        if(rectangle.x > paddingmargin.getLeft())
        {
            int k = 1;
            int i1 = ai.length;
            do
            {
                if(k >= i1)
                    break;
                rectangle.x += (double)(ai[k] - ai[k - 1]) * d;
                if(rectangle1.x == ai[k])
                {
                    i = k;
                    break;
                }
                k++;
            } while(true);
        }
        int l = i;
        int j1 = ai.length;
        do
        {
            if(l >= j1 - 1)
                break;
            rectangle.width += (double)(ai[l + 1] - ai[l]) * d;
            if(ai[l + 1] - ai[i] == rectangle1.width)
                break;
            l++;
        } while(true);
        if(rectangle.y > paddingmargin.getTop())
        {
            l = 1;
            j1 = ai1.length;
            do
            {
                if(l >= j1)
                    break;
                rectangle.y += (double)(ai1[l] - ai1[l - 1]) * d;
                if(rectangle1.y == ai1[l])
                {
                    j = l;
                    break;
                }
                l++;
            } while(true);
        }
        l = j;
        j1 = ai1.length;
        do
        {
            if(l >= j1 - 1)
                break;
            rectangle.height += (double)(ai1[l + 1] - ai1[l]) * d;
            if(ai1[l + 1] - ai1[j] == rectangle1.height)
                break;
            l++;
        } while(true);
        return rectangle;
    }

    public int[] getHors()
    {
        return getHors(false);
    }

    public int[] getVeris()
    {
        return getVeris(false);
    }

    public int[] getHors(boolean flag)
    {
        double d = flag ? containerPercent : 1.0D;
        ArrayList arraylist = new ArrayList();
        PaddingMargin paddingmargin = flag ? new PaddingMargin(0, 0, 0, 0) : toData().getMargin();
        arraylist.add(Integer.valueOf(paddingmargin.getLeft()));
        int i = getWidth() - paddingmargin.getLeft() - paddingmargin.getRight();
        int j = (int)((double)i / d);
        arraylist.add(Integer.valueOf(j));
        int k = 0;
        for(int l = getComponentCount(); k < l; k++)
        {
            int i1 = getComponent(k).getX();
            int j1 = (int)((double)i1 / d);
            if(!arraylist.contains(Integer.valueOf(j1)))
                arraylist.add(Integer.valueOf(j1));
        }

        Collections.sort(arraylist);
        return ArrayUtils.toPrimitive((Integer[])arraylist.toArray(new Integer[] {
            Integer.valueOf(arraylist.size())
        }));
    }

    public int[] getVeris(boolean flag)
    {
        double d = flag ? containerPercent : 1.0D;
        ArrayList arraylist = new ArrayList();
        PaddingMargin paddingmargin = flag ? new PaddingMargin(0, 0, 0, 0) : toData().getMargin();
        arraylist.add(Integer.valueOf(paddingmargin.getTop()));
        int i = getHeight() - paddingmargin.getTop() - paddingmargin.getBottom();
        int j = (int)((double)i / d);
        arraylist.add(Integer.valueOf(j));
        int k = 0;
        for(int l = getComponentCount(); k < l; k++)
        {
            int i1 = getComponent(k).getY();
            int j1 = (int)((double)i1 / d);
            if(!arraylist.contains(Integer.valueOf(j1)))
                arraylist.add(Integer.valueOf(j1));
        }

        Collections.sort(arraylist);
        return ArrayUtils.toPrimitive((Integer[])arraylist.toArray(new Integer[] {
            Integer.valueOf(arraylist.size())
        }));
    }

    public boolean canReduce(double d)
    {
        boolean flag = true;
        if(d < 0.0D && hasCalGap)
            flag = canReduceSize(d);
        return flag;
    }

    public void adjustCreatorsWidth(double d)
    {
        if(getComponentCount() == 0)
        {
            toData().setContainerWidth(getWidth());
            return;
        }
        updateWidgetBackupBounds();
        int i = toData().getCompInterval();
        if(i > 0 && hasCalGap)
            moveCompInterval(getAcualInterval());
        layoutWidthResize(d);
        if(d < 0.0D && needAddWidth > 0)
        {
            setSize(getWidth() + needAddWidth, getHeight());
            modifyEdgemostCreator(true);
        }
        addCompInterval(getAcualInterval());
        toData().setContainerWidth(getWidth());
        updateWidgetBackupBounds();
        LayoutUtils.layoutContainer(this);
    }

    protected void layoutWidthResize(double d)
    {
        int ai[] = toData().getHorComps();
        int i = ai.length;
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        for(int i1 = 0; i1 < i - 1; i1++)
        {
            int j = ai[i1];
            int l = ai[i1 + 1];
            int k = (int)((double)(l - j) * d);
            if(l - j < MIN_WIDTH - k)
                k = (MIN_WIDTH + j) - l;
            caculateWidth(j, k);
        }

    }

    private void caculateWidth(int i, int j)
    {
        java.util.List list = getCompsAtX(i);
        if(list.isEmpty())
            return;
        int k = 0;
        for(int l = list.size(); k < l; k++)
        {
            XCreator xcreator = (XCreator)list.get(k);
            com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = toData().getBoundsWidget(xcreator.toData());
            Rectangle rectangle = boundswidget.getBounds();
            Rectangle rectangle1 = boundswidget.getBackupBounds();
            if(rectangle1.x < i)
            {
                if(!notHasRightCreator(rectangle1))
                {
                    xcreator.setSize(rectangle.width + j, rectangle.height);
                    toData().setBounds(xcreator.toData(), xcreator.getBounds());
                }
            } else
            {
                calculateCreatorWidth(xcreator, rectangle, j, i);
            }
        }

    }

    private void calculateCreatorWidth(XCreator xcreator, Rectangle rectangle, int i, int j)
    {
        if(j == 0)
        {
            int k = notHasRightCreator(rectangle) ? getWidth() : rectangle.width + i;
            xcreator.setBounds(0, rectangle.y, k, rectangle.height);
            xcreator.recalculateChildWidth(k);
        } else
        {
            XCreator xcreator1 = getCreatorAt(rectangle.x - 1, rectangle.y);
            int l = getPosX(xcreator1);
            int i1 = notHasRightCreator(rectangle) ? getWidth() - l : rectangle.width + i;
            int j1 = i1;
            if(i1 < MIN_WIDTH)
                j1 = MIN_WIDTH;
            xcreator.setBounds(l, rectangle.y, j1, rectangle.height);
            if(i1 < MIN_WIDTH)
                needAddWidth = Math.max(needAddWidth, MIN_WIDTH - i1);
        }
        toData().setBounds(xcreator.toData(), xcreator.getBounds());
    }

    private boolean notHasRightCreator(Rectangle rectangle)
    {
        return rectangle.x + rectangle.width == getBackupBound().width;
    }

    private boolean notHasBottomCreator(Rectangle rectangle)
    {
        return rectangle.y + rectangle.height == getBackupBound().height;
    }

    protected void modifyEdgemostCreator(boolean flag)
    {
        int i = 0;
        for(int j = getComponentCount(); i < j; i++)
        {
            XCreator xcreator = (XCreator)getComponent(i);
            com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = toData().getBoundsWidget(xcreator.toData());
            Rectangle rectangle = boundswidget.getBackupBounds();
            if(flag && notHasRightCreator(rectangle))
            {
                xcreator.setSize(xcreator.getWidth() + needAddWidth, xcreator.getHeight());
                continue;
            }
            if(!flag && notHasBottomCreator(rectangle))
                xcreator.setSize(xcreator.getWidth(), xcreator.getHeight() + needAddHeight);
        }

    }

    public void adjustCreatorsHeight(double d)
    {
        if(getComponentCount() == 0)
        {
            toData().setContainerHeight(getHeight());
            return;
        }
        updateWidgetBackupBounds();
        int i = toData().getCompInterval();
        if(i > 0 && hasCalGap)
            moveCompInterval(getAcualInterval());
        layoutHeightResize(d);
        if(d < 0.0D && needAddHeight > 0)
        {
            setSize(getWidth(), getHeight() + needAddHeight);
            modifyEdgemostCreator(false);
        }
        addCompInterval(getAcualInterval());
        toData().setContainerHeight(getHeight());
        updateWidgetBackupBounds();
        LayoutUtils.layoutContainer(this);
    }

    protected void layoutHeightResize(double d)
    {
        int ai[] = toData().getVertiComps();
        int i = ai.length;
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        for(int i1 = 0; i1 < i - 1; i1++)
        {
            int j = ai[i1];
            int l = ai[i1 + 1];
            int k = (int)((double)(l - j) * d);
            if(l - j < MIN_HEIGHT - k)
                k = (MIN_HEIGHT + j) - l;
            calculateHeight(j, k);
        }

    }

    private void calculateHeight(int i, int j)
    {
        java.util.List list = getCompsAtY(i);
        if(list.isEmpty())
            return;
        int k = 0;
        for(int l = list.size(); k < l; k++)
        {
            XCreator xcreator = (XCreator)list.get(k);
            com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = toData().getBoundsWidget(xcreator.toData());
            Rectangle rectangle = boundswidget.getBounds();
            Rectangle rectangle1 = boundswidget.getBackupBounds();
            if(rectangle1.y < i)
            {
                if(!notHasBottomCreator(rectangle1))
                {
                    xcreator.setSize(rectangle.width, rectangle.height + j);
                    toData().setBounds(xcreator.toData(), xcreator.getBounds());
                }
            } else
            {
                calculateCreatorHeight(xcreator, rectangle, j, i);
            }
        }

    }

    private void calculateCreatorHeight(XCreator xcreator, Rectangle rectangle, int i, int j)
    {
        if(j == 0)
        {
            int k = notHasBottomCreator(rectangle) ? getHeight() : rectangle.height + i;
            xcreator.setBounds(rectangle.x, 0, rectangle.width, k);
        } else
        {
            XCreator xcreator1 = getCreatorAt(rectangle.x, rectangle.y - 1);
            int l = getPosY(xcreator1);
            int i1 = notHasBottomCreator(rectangle) ? getHeight() - l : rectangle.height + i;
            xcreator.setBounds(rectangle.x, l, rectangle.width, i1);
            if(i1 < MIN_HEIGHT)
                needAddHeight = Math.max(needAddHeight, MIN_HEIGHT - i1);
        }
        toData().setBounds(xcreator.toData(), xcreator.getBounds());
    }

    private java.util.List getCompsAtX(int i)
    {
        ArrayList arraylist = new ArrayList();
        int j = toData().getWidgetCount();
        for(int k = 0; k < j; k++)
        {
            Component component = getComponent(k);
            XCreator xcreator = (XCreator)component;
            com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = toData().getBoundsWidget(xcreator.toData());
            Rectangle rectangle = boundswidget.getBackupBounds();
            boolean flag = rectangle.x < i && i < rectangle.x + rectangle.width;
            if(flag || rectangle.x == i)
                arraylist.add(component);
        }

        return arraylist;
    }

    private java.util.List getCompsAtY(int i)
    {
        ArrayList arraylist = new ArrayList();
        int j = 0;
        for(int k = getComponentCount(); j < k; j++)
        {
            Component component = getComponent(j);
            XCreator xcreator = (XCreator)component;
            com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = toData().getBoundsWidget(xcreator.toData());
            Rectangle rectangle = boundswidget.getBackupBounds();
            boolean flag = rectangle.y < i && i < rectangle.y + rectangle.height;
            if(flag || rectangle.y == i)
                arraylist.add(component);
        }

        return arraylist;
    }

    private int getPosX(XCreator xcreator)
    {
        if(xcreator == null)
            return 0;
        else
            return xcreator.getX() + xcreator.getWidth();
    }

    private int getPosY(XCreator xcreator)
    {
        if(xcreator == null)
            return 0;
        else
            return xcreator.getY() + xcreator.getHeight();
    }

    public XCreator getCreatorAt(int i, int j)
    {
        int k = 0;
        for(int l = getComponentCount(); k < l; k++)
        {
            XCreator xcreator = (XCreator)getComponent(k);
            com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = toData().getBoundsWidget(xcreator.toData());
            Rectangle rectangle = boundswidget.getBackupBounds();
            boolean flag = rectangle.x <= i && i < rectangle.x + rectangle.width && rectangle.y <= j && j < rectangle.y + rectangle.height;
            if(flag)
                return xcreator;
        }

        return null;
    }

    protected void updateWidgetBackupBounds()
    {
        int i = 0;
        for(int j = getComponentCount(); i < j; i++)
        {
            Component component = getComponent(i);
            XCreator xcreator = (XCreator)component;
            com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = toData().getBoundsWidget(xcreator.toData());
            boundswidget.setBackupBounds(boundswidget.getBounds());
        }

    }

    public int getMinHeight(java.util.List list)
    {
        if(list.isEmpty())
            return 0;
        int i = getWidth();
        int j = 0;
        for(int k = list.size(); j < k; j++)
            i = i <= ((Component)list.get(j)).getHeight() ? i : ((Component)list.get(j)).getHeight();

        return i;
    }

    public Dimension initEditorSize()
    {
        return new Dimension(0, 0);
    }

    public String createDefaultName()
    {
        return "fit";
    }

    public WFitLayout toData()
    {
        return (WFitLayout)data;
    }

    public XCreator replace(Widget widget, XCreator xcreator)
    {
        int i = getComponentZOrder(xcreator);
        if(i != -1)
        {
            toData().replace(new com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget(widget, xcreator.getBounds()), new com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget(xcreator.toData(), xcreator.getBounds()));
            convert();
            return (XCreator)getComponent(i);
        } else
        {
            return null;
        }
    }

    public Dimension getMinimumSize()
    {
        return toData().getMinDesignSize();
    }

    public void convert()
    {
        isRefreshing = true;
        WFitLayout wfitlayout = toData();
        removeAll();
        int i = 0;
        for(int j = wfitlayout.getWidgetCount(); i < j; i++)
        {
            com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = (com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget)wfitlayout.getWidget(i);
            if(boundswidget != null)
            {
                Rectangle rectangle = boundswidget.getBounds();
                boundswidget.setBackupBounds(rectangle);
                XWidgetCreator xwidgetcreator = (XWidgetCreator)XCreatorUtils.createXCreator(boundswidget.getWidget());
                xwidgetcreator.setBounds(rectangle);
                add(xwidgetcreator, boundswidget.getWidget().getWidgetName(), true);
                xwidgetcreator.setBackupParent(this);
            }
        }

        isRefreshing = false;
    }

    public void componentAdded(ContainerEvent containerevent)
    {
        if(isRefreshing)
        {
            return;
        } else
        {
            LayoutUtils.layoutContainer(this);
            WFitLayout wfitlayout = toData();
            XWidgetCreator xwidgetcreator = (XWidgetCreator)containerevent.getChild();
            Rectangle rectangle = xwidgetcreator.getBounds();
            wfitlayout.addWidget(new com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget(xwidgetcreator.toData(), rectangle));
            xwidgetcreator.setBackupParent(this);
            return;
        }
    }

    private Rectangle dealWidgetBound(Rectangle rectangle)
    {
        if(containerPercent == 1.0D)
        {
            return rectangle;
        } else
        {
            rectangle.x = (int)((double)rectangle.x / containerPercent);
            rectangle.y = (int)((double)rectangle.y / containerPercent);
            rectangle.width = (int)((double)rectangle.width / containerPercent);
            rectangle.height = (int)((double)rectangle.height / containerPercent);
            return rectangle;
        }
    }

    public void updateBoundsWidget()
    {
        WFitLayout wfitlayout = toData();
        if(getComponentCount() == 0)
            return;
        moveContainerMargin();
        moveCompInterval(getAcualInterval());
        int ai[] = getHors(true);
        int ai1[] = getVeris(true);
        int i = 0;
        int j = 0;
        int k = 0;
        for(int l = getComponentCount(); k < l; k++)
        {
            XCreator xcreator = (XCreator)getComponent(k);
            com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = wfitlayout.getBoundsWidget(xcreator.toData());
            Rectangle rectangle = dealWidgetBound(xcreator.getBounds());
            Rectangle rectangle1 = recalculateWidgetBounds(rectangle, ai, ai1);
            boundswidget.setBounds(rectangle1);
            xcreator.toData().updateChildBounds(rectangle1);
            if(rectangle1.x == 0)
                j += rectangle1.height;
            if(rectangle1.y == 0)
                i += rectangle1.width;
            ArrayList arraylist = xcreator.getTargetChildrenList();
            if(arraylist.isEmpty())
                continue;
            for(int i1 = 0; i1 < arraylist.size(); i1++)
            {
                XWTabFitLayout xwtabfitlayout = (XWTabFitLayout)arraylist.get(i1);
                xwtabfitlayout.updateBoundsWidget();
            }

        }

        wfitlayout.setContainerHeight(j);
        wfitlayout.setContainerWidth(i);
        addCompInterval(getAcualInterval());
    }

    private Rectangle recalculateWidgetBounds(Rectangle rectangle, int ai[], int ai1[])
    {
        int i = 0;
        int j = 0;
        Rectangle rectangle1 = new Rectangle();
        if(rectangle.x > 0)
        {
            int k = 1;
            int i1 = ai.length;
            do
            {
                if(k >= i1)
                    break;
                rectangle1.x += ai[k] - ai[k - 1];
                if(rectangle.x == ai[k])
                {
                    i = k;
                    break;
                }
                k++;
            } while(true);
        }
        int l = i;
        int j1 = ai.length;
        do
        {
            if(l >= j1 - 1)
                break;
            rectangle1.width += ai[l + 1] - ai[l];
            if(ai[l + 1] - ai[i] >= rectangle.width)
                break;
            l++;
        } while(true);
        if(rectangle.y > 0)
        {
            l = 1;
            j1 = ai1.length;
            do
            {
                if(l >= j1)
                    break;
                rectangle1.y += ai1[l] - ai1[l - 1];
                if(rectangle.y == ai1[l])
                {
                    j = l;
                    break;
                }
                l++;
            } while(true);
        }
        l = j;
        j1 = ai1.length;
        do
        {
            if(l >= j1 - 1)
                break;
            rectangle1.height += ai1[l + 1] - ai1[l];
            if(ai1[l + 1] - ai1[j] >= rectangle.height)
                break;
            l++;
        } while(true);
        return rectangle1;
    }

    public void componentRemoved(ContainerEvent containerevent)
    {
        if(isRefreshing)
        {
            return;
        } else
        {
            WFitLayout wfitlayout = toData();
            XWidgetCreator xwidgetcreator = (XWidgetCreator)containerevent.getChild();
            Widget widget = xwidgetcreator.toData();
            com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = wfitlayout.getBoundsWidget(widget);
            wfitlayout.removeWidget(boundswidget);
            updateBoundsWidget();
            return;
        }
    }

    public void add(Component component, Object obj)
    {
        if(component == null)
        {
            return;
        } else
        {
            super.add(component, obj);
            XCreator xcreator = (XCreator)component;
            dealDirections(xcreator, false);
            return;
        }
    }

    private void add(Component component, Object obj, boolean flag)
    {
        super.add(component, obj);
        XCreator xcreator = (XCreator)component;
        dealDirections(xcreator, flag);
    }

    private void dealDirections(XCreator xcreator, boolean flag)
    {
        if(xcreator == null)
            return;
        int i = flag ? toData().getContainerWidth() : getWidth();
        int j = flag ? toData().getContainerHeight() : getHeight();
        PaddingMargin paddingmargin = flag ? new PaddingMargin(0, 0, 0, 0) : toData().getMargin();
        for(int k = 0; k < getXCreatorCount(); k++)
        {
            XCreator xcreator1 = getXCreator(k);
            int l = xcreator1.getX();
            int i1 = xcreator1.getY();
            int j1 = xcreator1.getWidth();
            int k1 = xcreator1.getHeight();
            ArrayList arraylist = new ArrayList();
            if(l > paddingmargin.getLeft())
                arraylist.add(Integer.valueOf(3));
            if(l + j1 < i - paddingmargin.getRight())
                arraylist.add(Integer.valueOf(4));
            if(i1 > paddingmargin.getTop())
                arraylist.add(Integer.valueOf(1));
            if(i1 + k1 < j - paddingmargin.getBottom())
                arraylist.add(Integer.valueOf(2));
            if(arraylist.isEmpty())
                xcreator1.setDirections(null);
            else
                xcreator1.setDirections(ArrayUtils.toPrimitive((Integer[])arraylist.toArray(new Integer[arraylist.size()])));
        }

    }

    public void addCompInterval(int i)
    {
        if(i == 0)
            return;
        int j = i / 2;
        int k = 0;
        for(int l = getComponentCount(); k < l; k++)
        {
            Component component = getComponent(k);
            Rectangle rectangle = component.getBounds();
            Rectangle rectangle1 = new Rectangle(rectangle);
            if(rectangle.x > 0)
            {
                rectangle1.x += j;
                rectangle1.width -= j;
            }
            if(rectangle.width + rectangle.x < getWidth())
                rectangle1.width -= j;
            if(rectangle.y > 0)
            {
                rectangle1.y += j;
                rectangle1.height -= j;
            }
            if(rectangle.height + rectangle.y < getHeight())
                rectangle1.height -= j;
            component.setBounds(rectangle1);
        }

        hasCalGap = true;
    }

    public void moveCompInterval(int i)
    {
        if(i == 0)
            return;
        int j = i / 2;
        int k = 0;
        for(int l = getComponentCount(); k < l; k++)
        {
            Component component = getComponent(k);
            Rectangle rectangle = component.getBounds();
            Rectangle rectangle1 = new Rectangle(rectangle);
            if(rectangle.x > 0)
            {
                rectangle1.x -= j;
                rectangle1.width += j;
            }
            if(rectangle.width + rectangle.x < getWidth())
                rectangle1.width += j;
            if(rectangle.y > 0)
            {
                rectangle1.y -= j;
                rectangle1.height += j;
            }
            if(rectangle.height + rectangle.y < getHeight())
                rectangle1.height += j;
            component.setBounds(rectangle1);
        }

        hasCalGap = false;
    }

    public boolean canAddInterval(int i)
    {
        int j = i / 2;
        int k = 0;
        for(int l = getComponentCount(); k < l; k++)
        {
            Component component = getComponent(k);
            Rectangle rectangle = component.getBounds();
            Dimension dimension = new Dimension(getWidth(), getHeight());
            Rectangle rectangle1 = dealBound(rectangle, dimension, j, 0.0D);
            if(rectangle1.width < minWidth || rectangle1.height < minHeight)
                return false;
        }

        return true;
    }

    private Rectangle dealBound(Rectangle rectangle, Dimension dimension, int i, double d)
    {
        Rectangle rectangle1 = reSetBound(rectangle, d);
        if(rectangle.x > 0)
            rectangle1.width -= i;
        if(rectangle.width + rectangle.x < dimension.width)
            rectangle1.width -= i;
        if(rectangle.y > 0)
            rectangle1.height -= i;
        if(rectangle.height + rectangle.y < dimension.height)
            rectangle1.height -= i;
        return new Rectangle(rectangle1);
    }

    private boolean canReduceSize(double d)
    {
        int i = toData().getCompInterval() / 2;
        int j = 0;
        for(int k = getComponentCount(); j < k; j++)
        {
            XCreator xcreator = (XCreator)getComponent(j);
            com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = toData().getBoundsWidget(xcreator.toData());
            Rectangle rectangle = boundswidget.getBounds();
            Dimension dimension = new Dimension(getBackupBound().width, getBackupBound().height);
            Rectangle rectangle1 = dealBound(rectangle, dimension, i, d);
            if(rectangle1.width < MIN_WIDTH || rectangle1.height < MIN_HEIGHT)
                return false;
        }

        return true;
    }

    private Rectangle reSetBound(Rectangle rectangle, double d)
    {
        Rectangle rectangle1 = new Rectangle(rectangle);
        rectangle1.x += (double)rectangle1.x * d;
        rectangle1.y += (double)rectangle1.y * d;
        rectangle1.width += (double)rectangle1.width * d;
        rectangle1.height += (double)rectangle1.height * d;
        return new Rectangle(rectangle1);
    }

    public void moveContainerMargin()
    {
        PaddingMargin paddingmargin = toData().getMargin();
        int i = getComponentCount();
        int j = getWidth() - paddingmargin.getRight();
        int k = getHeight() - paddingmargin.getBottom();
        for(int l = 0; l < i; l++)
        {
            Component component = getComponent(l);
            Rectangle rectangle = component.getBounds();
            if(rectangle.x == paddingmargin.getLeft())
            {
                rectangle.x = 0;
                rectangle.width += paddingmargin.getLeft();
            }
            if(rectangle.y == paddingmargin.getTop())
            {
                rectangle.y = 0;
                rectangle.height += paddingmargin.getTop();
            }
            if(rectangle.x + rectangle.width == j)
                rectangle.width += paddingmargin.getRight();
            if(rectangle.y + rectangle.height == k)
                rectangle.height += paddingmargin.getBottom();
            component.setBounds(rectangle);
        }

    }

    public Component getTopComp(int i, int j)
    {
        int k = getAcualInterval();
        return getComponentAt(i, j - default_Length - k);
    }

    public Component getLeftComp(int i, int j)
    {
        int k = getAcualInterval();
        return getComponentAt(i - default_Length - k, j);
    }

    public Component getRightComp(int i, int j, int k)
    {
        int l = getAcualInterval();
        return getComponentAt(i + k + default_Length + l, j);
    }

    public Component getBottomComp(int i, int j, int k)
    {
        int l = getAcualInterval();
        return getComponentAt(i, j + k + default_Length + l);
    }

    public Component getRightTopComp(int i, int j, int k)
    {
        int l = getAcualInterval();
        return getComponentAt((i + k) - default_Length, j - default_Length - l);
    }

    public Component getBottomLeftComp(int i, int j, int k)
    {
        int l = getAcualInterval();
        return getComponentAt(i - default_Length - l, (j + k) - default_Length);
    }

    public Component getBottomRightComp(int i, int j, int k, int l)
    {
        int i1 = getAcualInterval();
        return getComponentAt(i + l + default_Length + i1, (j + k) - default_Length);
    }

    public Component getRightBottomComp(int i, int j, int k, int l)
    {
        int i1 = getAcualInterval();
        return getComponentAt((i + l) - default_Length, j + k + default_Length + i1);
    }

    public double getContainerPercent()
    {
        return containerPercent;
    }

    public void setContainerPercent(double d)
    {
        containerPercent = d;
        minWidth = (int)((double)MIN_WIDTH * d);
        minHeight = (int)((double)MIN_HEIGHT * d);
    }

    public boolean isSupportDrag()
    {
        return false;
    }

    public int getActualMinWidth()
    {
        return minWidth;
    }

    public int getActualMinHeight()
    {
        return minHeight;
    }

    public int getAcualInterval()
    {
        int i = (int)((double)toData().getCompInterval() * containerPercent);
        int j = i / 2;
        return j * 2;
    }

    public boolean isHasCalGap()
    {
        return hasCalGap;
    }

    public void setHasCalGap(boolean flag)
    {
        hasCalGap = flag;
    }

    public void setBackupGap(double d)
    {
        int i = (int)((double)toData().getCompInterval() * d);
        int j = i / 2;
        backupGap = j * 2;
    }

    public XLayoutContainer findNearestFit()
    {
        return this;
    }

    public volatile WLayout toData()
    {
        return toData();
    }

    public volatile AbstractBorderStyleWidget toData()
    {
        return toData();
    }

    public volatile Widget toData()
    {
        return toData();
    }
}
