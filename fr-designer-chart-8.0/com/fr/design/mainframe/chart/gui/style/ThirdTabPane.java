// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.gui.ibutton.UIHeadGroup;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.border.Border;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style:
//            AbstractChartTabPane

public abstract class ThirdTabPane extends BasicBeanPane
{
    protected static class NamePane
    {

        private String name;
        private AbstractChartTabPane pane;

        public String getName()
        {
            return name;
        }

        public void setName(String s)
        {
            name = s;
        }

        public AbstractChartTabPane getPane()
        {
            return pane;
        }

        public void setPane(AbstractChartTabPane abstractcharttabpane)
        {
            pane = abstractcharttabpane;
        }



        public NamePane(String s, AbstractChartTabPane abstractcharttabpane)
        {
            name = s;
            pane = abstractcharttabpane;
        }
    }


    private static final long serialVersionUID = 0x1fe64ce367b49c9eL;
    protected UIHeadGroup tabPane;
    protected String nameArray[];
    public JPanel centerPane;
    public CardLayout cardLayout;
    public java.util.List paneList;
    private Border myBorder;

    protected abstract java.util.List initPaneList(Plot plot, AbstractAttrNoScrollPane abstractattrnoscrollpane);

    public ThirdTabPane(Plot plot, AbstractAttrNoScrollPane abstractattrnoscrollpane)
    {
        myBorder = new Border() {

            final ThirdTabPane this$0;

            public void paintBorder(Component component, Graphics g, int i, int j, int k, int l)
            {
                Graphics2D graphics2d = (Graphics2D)g;
                graphics2d.setColor(UIConstants.LINE_COLOR);
                graphics2d.drawLine(0, 0, 0, l);
                graphics2d.drawLine(tabPane.getPreferredSize().width - 1, 0, k - 2, 0);
                graphics2d.drawLine(0, l - 1, k - 2, l - 1);
            }

            public boolean isBorderOpaque()
            {
                return false;
            }

            public Insets getBorderInsets(Component component)
            {
                return new Insets(2, 2, 2, 2);
            }

            
            {
                this$0 = ThirdTabPane.this;
                super();
            }
        }
;
        paneList = initPaneList(plot, abstractattrnoscrollpane);
        initAllPane();
    }

    protected void initAllPane()
    {
        cardLayout = new CardLayout();
        centerPane = new JPanel(cardLayout);
        nameArray = new String[paneList.size()];
        for(int i = 0; i < paneList.size(); i++)
        {
            NamePane namepane = (NamePane)paneList.get(i);
            nameArray[i] = namepane.name;
            centerPane.add(namepane.pane, nameArray[i]);
        }

        if(!paneList.isEmpty())
        {
            tabPane = new UIHeadGroup(nameArray) {

                final ThirdTabPane this$0;

                public void tabChanged(int j)
                {
                    cardLayout.show(centerPane, nameArray[j]);
                }

            
            {
                this$0 = ThirdTabPane.this;
                super(as);
            }
            }
;
            centerPane.setBorder(myBorder);
        }
        initLayout();
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        if(!paneList.isEmpty())
        {
            Graphics2D graphics2d = (Graphics2D)g;
            graphics2d.setColor(UIConstants.LINE_COLOR);
            graphics2d.drawLine(getWidth() - 2, tabPane.getPreferredSize().height, getWidth() - 2, getHeight() - 1);
        }
    }

    protected void initLayout()
    {
        setLayout(new BorderLayout());
        if(!paneList.isEmpty())
        {
            JPanel jpanel = new JPanel(new FlowLayout(3, 0, 0));
            jpanel.add(tabPane);
            add(jpanel, "North");
        }
        add(centerPane, "Center");
    }

    protected int getContentPaneWidth()
    {
        return centerPane.getPreferredSize().width;
    }

    public Object updateBean()
    {
        return null;
    }
}
