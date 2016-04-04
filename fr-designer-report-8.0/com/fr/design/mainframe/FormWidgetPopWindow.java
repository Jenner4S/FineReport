// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.constants.UIConstants;
import com.fr.design.gui.core.WidgetOption;
import com.fr.stable.OperatingSystem;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JWindow;

// Referenced classes of package com.fr.design.mainframe:
//            ToolBarButton

public class FormWidgetPopWindow extends JWindow
{
    private static class EditorLayout
        implements LayoutManager
    {

        int top;
        int left;
        int right;
        int bottom;
        int hgap;
        int vgap;
        int maxLine;

        public void addLayoutComponent(String s, Component component)
        {
        }

        public void layoutContainer(Container container)
        {
            synchronized(container.getTreeLock())
            {
                Insets insets = container.getInsets();
                int i = container.getComponentCount();
                for(int j = 0; j < i; j++)
                {
                    Component component = container.getComponent(j);
                    if(component.isVisible())
                    {
                        Dimension dimension = component.getPreferredSize();
                        component.setBounds(insets.left + left + (j % maxLine) * (hgap + dimension.width), top + insets.top + (j / maxLine) * (vgap + dimension.height), dimension.width, dimension.height);
                    }
                }

            }
        }

        public Dimension minimumLayoutSize(Container container)
        {
            return new Dimension(0, 0);
        }

        public Dimension preferredLayoutSize(Container container)
        {
            Insets insets = container.getInsets();
            int i = container.getComponentCount();
            return new Dimension(maxLine * 28 + insets.left + insets.right + right + left, (i / maxLine + 1) * 24 + insets.top + insets.bottom + top + bottom);
        }

        public void removeLayoutComponent(Component component)
        {
        }

        private EditorLayout()
        {
            top = 4;
            left = 4;
            right = 4;
            bottom = 4;
            hgap = 5;
            vgap = 4;
            maxLine = 8;
        }

    }

    private class EditorChoosePane extends JPanel
    {

        final FormWidgetPopWindow this$0;

        public void paintComponent(Graphics g)
        {
            Rectangle rectangle = getBounds();
            g.setColor(UIConstants.NORMAL_BACKGROUND);
            g.fillRoundRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, 0, 0);
            g.setColor(UIConstants.LINE_COLOR);
            g.drawRoundRect(rectangle.x, rectangle.y, rectangle.width - 1, rectangle.height - 1, 0, 0);
        }

        protected void initComponents()
        {
            WidgetOption awidgetoption[] = options;
            int i = awidgetoption.length;
            for(int j = 0; j < i; j++)
            {
                WidgetOption widgetoption = awidgetoption[j];
                ToolBarButton toolbarbutton = new ToolBarButton(widgetoption);
                add(toolbarbutton);
            }

        }

        public EditorChoosePane()
        {
            this$0 = FormWidgetPopWindow.this;
            super();
            setLayout(new EditorLayout());
            initComponents();
        }
    }


    private WidgetOption options[];
    private EditorChoosePane pane;
    private AWTEventListener awt;

    public FormWidgetPopWindow()
    {
        awt = new AWTEventListener() {

            final FormWidgetPopWindow this$0;

            public void eventDispatched(AWTEvent awtevent)
            {
                if(awtevent instanceof MouseEvent)
                {
                    MouseEvent mouseevent = (MouseEvent)awtevent;
                    if(mouseevent.getClickCount() > 0)
                    {
                        Point point = new Point((int)mouseevent.getLocationOnScreen().getX(), (int)mouseevent.getLocationOnScreen().getY());
                        if(OperatingSystem.isWindows())
                        {
                            if(!contains(point))
                                setVisible(false);
                        } else
                        if(OperatingSystem.isMacOS())
                        {
                            Dimension dimension = getSize();
                            Point point1 = getLocation();
                            Rectangle rectangle = new Rectangle(point1, dimension);
                            if(!rectangle.contains(point))
                                setVisible(false);
                        }
                    }
                }
            }

            
            {
                this$0 = FormWidgetPopWindow.this;
                super();
            }
        }
;
    }

    private void initComp()
    {
        if(pane != null)
            remove(pane);
        pane = new EditorChoosePane();
        getContentPane().add(pane);
        setSize(pane.getPreferredSize());
    }

    public void showToolTip(int i, int j, WidgetOption awidgetoption[])
    {
        Toolkit.getDefaultToolkit().addAWTEventListener(awt, 16L);
        setLocation(i, j);
        options = awidgetoption;
        initComp();
        setVisible(true);
        doLayout();
    }

}
