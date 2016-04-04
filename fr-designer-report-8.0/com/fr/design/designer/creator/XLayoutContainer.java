// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.base.FRContext;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.form.layout.FRLayoutManager;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.widget.editors.PaddingMarginEditor;
import com.fr.design.mainframe.widget.editors.WLayoutBorderStyleEditor;
import com.fr.design.mainframe.widget.renderer.LayoutBorderStyleRenderer;
import com.fr.design.mainframe.widget.renderer.PaddingMarginCellRenderer;
import com.fr.design.parameter.ParameterBridge;
import com.fr.form.ui.AbstractBorderStyleWidget;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WLayout;
import com.fr.general.Background;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.core.PropertyChangeAdapter;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.beans.IntrospectionException;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.designer.creator:
//            XBorderStyleWidgetCreator, CRPropertyDescriptor, XCreator, XWidgetCreator, 
//            XCreatorUtils

public abstract class XLayoutContainer extends XBorderStyleWidgetCreator
    implements ContainerListener, ParameterBridge
{

    public static int MIN_WIDTH = 36;
    public static int MIN_HEIGHT = 21;
    protected static final Dimension LARGEPREFERREDSIZE = new Dimension(200, 200);
    protected boolean isRefreshing;
    protected int default_Length;

    public XLayoutContainer(WLayout wlayout, Dimension dimension)
    {
        super(wlayout, dimension);
        default_Length = 5;
        addContainerListener(this);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("widgetName", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Form-Widget_Name")), (new CRPropertyDescriptor("borderStyle", data.getClass())).setEditorClass(com/fr/design/mainframe/widget/editors/WLayoutBorderStyleEditor).setRendererClass(com/fr/design/mainframe/widget/renderer/LayoutBorderStyleRenderer).setI18NName(Inter.getLocText("FR-Engine_Style")).putKeyValue("category", "Advanced").setPropertyChangeListener(new PropertyChangeAdapter() {

                final XLayoutContainer this$0;

                public void propertyChange()
                {
                    initStyle();
                }

            
            {
                this$0 = XLayoutContainer.this;
                super();
            }
            }
), (new CRPropertyDescriptor("margin", data.getClass())).setEditorClass(com/fr/design/mainframe/widget/editors/PaddingMarginEditor).setRendererClass(com/fr/design/mainframe/widget/renderer/PaddingMarginCellRenderer).setI18NName(Inter.getLocText("FR-Designer_Layout-Padding")).putKeyValue("category", "Advanced")
        });
    }

    public WLayout toData()
    {
        return (WLayout)data;
    }

    protected void initXCreatorProperties()
    {
        super.initXCreatorProperties();
        initBorderStyle();
        initLayoutManager();
        convert();
    }

    protected JComponent initEditor()
    {
        return this;
    }

    public XCreator replace(Widget widget, XCreator xcreator)
    {
        int i = getComponentZOrder(xcreator);
        if(i != -1)
        {
            toData().replace(widget, xcreator.toData());
            convert();
            XCreator xcreator1 = (XCreator)getComponent(i);
            xcreator1.setSize(xcreator.getSize());
            return xcreator1;
        } else
        {
            return null;
        }
    }

    public Dimension initEditorSize()
    {
        return LARGEPREFERREDSIZE;
    }

    protected abstract void initLayoutManager();

    public void convert()
    {
        isRefreshing = true;
        WLayout wlayout = toData();
        removeAll();
        addWidgetToSwingComponent(wlayout);
        isRefreshing = false;
    }

    protected void addWidgetToSwingComponent(WLayout wlayout)
    {
        for(int i = 0; i < wlayout.getWidgetCount(); i++)
        {
            Widget widget = wlayout.getWidget(i);
            if(widget != null)
            {
                XWidgetCreator xwidgetcreator = (XWidgetCreator)XCreatorUtils.createXCreator(widget, calculatePreferredSize(widget));
                add(xwidgetcreator, i);
            }
        }

    }

    public void componentAdded(ContainerEvent containerevent)
    {
        if(isRefreshing)
        {
            return;
        } else
        {
            XWidgetCreator xwidgetcreator = (XWidgetCreator)containerevent.getChild();
            WLayout wlayout = toData();
            Widget widget = xwidgetcreator.toData();
            wlayout.addWidget(widget);
            recalculateChildrenPreferredSize();
            return;
        }
    }

    public void componentRemoved(ContainerEvent containerevent)
    {
        if(isRefreshing)
        {
            return;
        } else
        {
            WLayout wlayout = toData();
            Widget widget = ((XWidgetCreator)containerevent.getChild()).toData();
            wlayout.removeWidget(widget);
            recalculateChildrenPreferredSize();
            return;
        }
    }

    protected Dimension calculatePreferredSize(Widget widget)
    {
        return new Dimension();
    }

    public void recalculateChildrenPreferredSize()
    {
        for(int i = 0; i < getComponentCount(); i++)
        {
            XCreator xcreator = getXCreator(i);
            Widget widget = xcreator.toData();
            Dimension dimension = calculatePreferredSize(widget);
            xcreator.setPreferredSize(dimension);
            xcreator.setMaximumSize(dimension);
        }

    }

    public int getXCreatorCount()
    {
        return getComponentCount();
    }

    public XCreator getXCreator(int i)
    {
        return (XCreator)getComponent(i);
    }

    public boolean canEnterIntoParaPane()
    {
        return false;
    }

    public boolean isComponentTreeLeaf()
    {
        return false;
    }

    public java.util.List getAllXCreatorNameList(XCreator xcreator, java.util.List list)
    {
        for(int i = 0; i < ((XLayoutContainer)xcreator).getXCreatorCount(); i++)
        {
            XCreator xcreator1 = ((XLayoutContainer)xcreator).getXCreator(i);
            xcreator1.getAllXCreatorNameList(xcreator1, list);
        }

        return list;
    }

    public boolean SearchQueryCreators(XCreator xcreator)
    {
        for(int i = 0; i < ((XLayoutContainer)xcreator).getXCreatorCount(); i++)
        {
            XCreator xcreator1 = ((XLayoutContainer)xcreator).getXCreator(i);
            if(xcreator1.SearchQueryCreators(xcreator1))
                return true;
        }

        return false;
    }

    public FRLayoutManager getFRLayout()
    {
        LayoutManager layoutmanager = getLayout();
        if(layoutmanager instanceof FRLayoutManager)
        {
            return (FRLayoutManager)layoutmanager;
        } else
        {
            FRContext.getLogger().error("FRLayoutManager isn't exsit!");
            return null;
        }
    }

    public abstract LayoutAdapter getLayoutAdapter();

    public int getIndexOfChild(Object obj)
    {
        int i = getComponentCount();
        for(int j = 0; j < i; j++)
        {
            Component component = getComponent(j);
            if(component == obj)
                return j;
        }

        return -1;
    }

    public Component getTopComp(int i, int j)
    {
        return getComponentAt(i, j - default_Length);
    }

    public Component getLeftComp(int i, int j)
    {
        return getComponentAt(i - default_Length, j);
    }

    public Component getRightComp(int i, int j, int k)
    {
        return getComponentAt(i + k + default_Length, j);
    }

    public Component getBottomComp(int i, int j, int k)
    {
        return getComponentAt(i, j + k + default_Length);
    }

    public Component getRightTopComp(int i, int j, int k)
    {
        return getComponentAt((i + k) - default_Length, j - default_Length);
    }

    public Component getBottomLeftComp(int i, int j, int k)
    {
        return getComponentAt(i - default_Length, (j + k) - default_Length);
    }

    public Component getBottomRightComp(int i, int j, int k, int l)
    {
        return getComponentAt(i + l + default_Length, (j + k) - default_Length);
    }

    public Component getRightBottomComp(int i, int j, int k, int l)
    {
        return getComponentAt((i + l) - default_Length, j + k + default_Length);
    }

    public boolean isDelayDisplayContent()
    {
        return false;
    }

    public boolean isDisplay()
    {
        return false;
    }

    public Background getDataBackground()
    {
        return toData().getBackground();
    }

    public int getDesignWidth()
    {
        return 0;
    }

    public int getPosition()
    {
        return 0;
    }

    public void stopAddingState(FormDesigner formdesigner)
    {
    }

    public XLayoutContainer findNearestFit()
    {
        XLayoutContainer xlayoutcontainer = getBackupParent();
        return xlayoutcontainer != null ? xlayoutcontainer.findNearestFit() : null;
    }

    public int[] getHors()
    {
        return ArrayUtils.EMPTY_INT_ARRAY;
    }

    public int[] getVeris()
    {
        return ArrayUtils.EMPTY_INT_ARRAY;
    }

    public void setDelayDisplayContent(boolean flag)
    {
    }

    public void setPosition(int i)
    {
    }

    public void setDisplay(boolean flag)
    {
    }

    public void setBackground(Background background)
    {
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
