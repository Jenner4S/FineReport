// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.gui.ilable.UILabel;
import com.fr.form.ui.Widget;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ContainerListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// Referenced classes of package com.fr.design.designer.creator:
//            XCreator

public class FormCardPane extends JComponent
    implements SwingConstants
{
    class FormCardLayout
        implements LayoutManager
    {

        final FormCardPane this$0;

        public void addLayoutComponent(String s, Component component)
        {
        }

        public void layoutContainer(Container container)
        {
            Insets insets = container.getInsets();
            int i = container.getWidth() - insets.left - insets.right;
            int j = container.getHeight() - insets.top - insets.bottom;
            if(showTab)
            {
                if(tabPlace == 1 || tabPlace == 3)
                {
                    int k = getTabPaneHeight();
                    tabPane.setSize(i, k);
                    cardPane.setSize(i, j - k);
                    if(tabPlace == 1)
                    {
                        tabPane.setLocation(insets.left, insets.top);
                        cardPane.setLocation(insets.left, insets.top + k);
                    } else
                    {
                        cardPane.setLocation(insets.left, insets.top);
                        tabPane.setLocation(insets.left, (insets.top + j) - k);
                    }
                } else
                if(tabPlace == 2 || tabPlace == 4)
                {
                    int l = getTabPaneWidth();
                    tabPane.setSize(l, j);
                    cardPane.setSize(i - l, j);
                    if(tabPlace == 2)
                    {
                        tabPane.setLocation(insets.left, insets.top);
                        cardPane.setLocation(insets.left + l, insets.top);
                    } else
                    {
                        cardPane.setLocation(insets.left, insets.top);
                        tabPane.setLocation((insets.left + i) - l, insets.top);
                    }
                }
            } else
            {
                tabPane.setVisible(false);
                cardPane.setSize(container.getWidth(), container.getHeight());
                cardPane.setLocation(0, 0);
            }
        }

        public Dimension minimumLayoutSize(Container container)
        {
            return null;
        }

        public Dimension preferredLayoutSize(Container container)
        {
            return null;
        }

        public void removeLayoutComponent(Component component)
        {
        }

        FormCardLayout()
        {
            this$0 = FormCardPane.this;
            super();
        }
    }


    private boolean showTab;
    private int tabPlace;
    private JPanel tabPane;
    private JPanel cardPane;
    private CardLayout layout;
    private java.util.List tabComponent;
    private int showIndex;

    public FormCardPane(boolean flag, int i)
    {
        tabComponent = new ArrayList();
        showTab = flag;
        tabPlace = i;
        initTabComponent();
        initCardComponent();
        setLayout(new FormCardLayout());
        add(tabPane);
        add(cardPane);
    }

    public FormCardPane(ContainerListener containerlistener)
    {
        this(true, 1);
        cardPane.addContainerListener(containerlistener);
    }

    private void initTabComponent()
    {
        tabPane = new JPanel();
        tabPane.setOpaque(true);
        tabPane.setBorder(BorderFactory.createLineBorder(Color.pink));
        tabPane.setBackground(Color.white);
    }

    private void initCardComponent()
    {
        cardPane = new JPanel();
        cardPane.setBorder(BorderFactory.createLineBorder(Color.orange));
        layout = new CardLayout();
        cardPane.setLayout(layout);
    }

    public void setSelectedIndex(int i)
    {
        setSelectedComponent(getComponentAt(i));
    }

    public int getTabCount()
    {
        return cardPane.getComponentCount();
    }

    public void add(Component component, Object obj)
    {
        add(component, obj, -1);
    }

    public void removeAll()
    {
        cardPane.removeAll();
        tabComponent.clear();
    }

    private void reSetTabComponent()
    {
        if(tabPlace == 1 || tabPlace == 3)
        {
            int i = 0;
            for(Iterator iterator = tabComponent.iterator(); iterator.hasNext();)
            {
                Component component = (Component)iterator.next();
                i += component.getWidth();
            }

            showTabComponent(i > tabPane.getWidth());
        } else
        if(tabPlace == 2 || tabPlace == 4)
        {
            int j = 0;
            for(Iterator iterator1 = tabComponent.iterator(); iterator1.hasNext();)
            {
                Component component1 = (Component)iterator1.next();
                j += component1.getHeight();
            }

            showTabComponent(j > tabPane.getHeight());
        }
    }

    private void showTabComponent(boolean flag)
    {
        tabPane.removeAll();
        if(showIndex < 0 || showIndex >= tabComponent.size())
            showIndex = 0;
        if(tabPlace == 1 || tabPlace == 3)
            tabPane.setLayout(new FlowLayout(0));
        else
        if(tabPlace == 2 || tabPlace == 4)
            tabPane.setLayout(new BoxLayout(tabPane, 1));
        else
            return;
        Component component;
        for(Iterator iterator = tabComponent.iterator(); iterator.hasNext(); tabPane.add(component))
            component = (Component)iterator.next();

        tabPane.repaint();
    }

    public void add(Component component, Object obj, int i)
    {
        if(!(component instanceof XCreator))
            return;
        Object obj1 = new JPanel();
        if(obj instanceof String)
            obj1 = new UILabel((String)obj);
        else
        if(obj instanceof Icon)
            obj1 = new UILabel((Icon)obj);
        else
            return;
        ((JComponent) (obj1)).setOpaque(true);
        ((JComponent) (obj1)).setBorder(BorderFactory.createLineBorder(Color.red));
        cardPane.add(component, ((XCreator)component).toData().getWidgetName(), i);
        if(i == -1)
            tabComponent.add(obj1);
        else
            tabComponent.add(i, obj1);
        reSetTabComponent();
    }

    public void setTabPlacement(int i)
    {
        tabPlace = i;
    }

    public Component getComponentAt(int i)
    {
        return cardPane.getComponent(i);
    }

    public void setSelectedComponent(Component component)
    {
        if(component instanceof XCreator)
        {
            int i = cardPane.getComponentZOrder(component);
            if(i == -1 || i == showIndex)
                return;
            ((Component)tabComponent.get(showIndex)).setBackground(null);
            showIndex = i;
            ((Component)tabComponent.get(showIndex)).setBackground(Color.orange);
            layout.show(cardPane, ((XCreator)component).toData().getWidgetName());
        }
    }

    public int getTabPaneHeight()
    {
        return 22;
    }

    public int getTabPaneWidth()
    {
        return 40;
    }

    public void addCreatorListener(ContainerListener containerlistener)
    {
    }

    public void setShowTab(boolean flag)
    {
        showTab = flag;
    }




}
