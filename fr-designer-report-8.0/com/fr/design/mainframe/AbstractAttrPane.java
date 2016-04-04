// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.gui.iscrollbar.UIScrollBar;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe:
//            CellElementPropertyPane

public abstract class AbstractAttrPane extends AbstractAttrNoScrollPane
{
    protected class BarLayout
        implements LayoutManager
    {

        final AbstractAttrPane this$0;

        public void addLayoutComponent(String s, Component component)
        {
        }

        public void removeLayoutComponent(Component component)
        {
        }

        public Dimension preferredLayoutSize(Container container)
        {
            return AbstractAttrPane.this.this$0.getPreferredSize();
        }

        public Dimension minimumLayoutSize(Container container)
        {
            return AbstractAttrPane.this.this$0.getMinimumSize();
        }

        public void layoutContainer(Container container)
        {
            maxHeight = CellElementPropertyPane.getInstance().getHeight() - 50;
            if(100 - scrollBar.getVisibleAmount() == 0)
            {
                beginY = 0;
            } else
            {
                int i = AbstractAttrPane.this.this$0.getPreferredSize().height;
                int k = scrollBar.getValue();
                beginY = (k * (i - maxHeight)) / (100 - scrollBar.getVisibleAmount());
            }
            int j = container.getWidth();
            int l = container.getHeight();
            if(AbstractAttrPane.this.this$0.getPreferredSize().height > maxHeight)
            {
                AbstractAttrPane.this.this$0.setBounds(0, -beginY, j - scrollBar.getWidth() - 4, l + beginY);
                scrollBar.setBounds(j - scrollBar.getWidth() - 1, 0, scrollBar.getWidth(), l);
            } else
            {
                AbstractAttrPane.this.this$0.setBounds(0, 0, j - 8 - 4, l);
            }
            AbstractAttrPane.this.this$0.validate();
        }

        protected BarLayout()
        {
            this$0 = AbstractAttrPane.this;
            super();
        }
    }


    private static final int MAXVALUE = 100;
    private static final int TITLE_HEIGHT = 50;
    private static final int MOUSE_WHEEL_SPEED = 5;
    private static final int CONTENTPANE_WIDTH_GAP = 4;
    private static final int SCROLLBAR_WIDTH = 8;
    private int maxHeight;
    private int beginY;
    private UIScrollBar scrollBar;

    public AbstractAttrPane()
    {
        maxHeight = 280;
        beginY = 0;
        setLayout(new BarLayout());
        scrollBar = new UIScrollBar(1) {

            final AbstractAttrPane this$0;

            public int getVisibleAmount()
            {
                int i = AbstractAttrPane.this.this$0.getPreferredSize().height;
                int j = (100 * maxHeight) / i;
                setVisibleAmount(j);
                return j;
            }

            public int getMaximum()
            {
                return 100;
            }

            
            {
                this$0 = AbstractAttrPane.this;
                super(i);
            }
        }
;
        add(scrollBar);
        add(leftContentPane);
        scrollBar.addAdjustmentListener(new AdjustmentListener() {

            final AbstractAttrPane this$0;

            public void adjustmentValueChanged(AdjustmentEvent adjustmentevent)
            {
                adjustValues();
            }

            
            {
                this$0 = AbstractAttrPane.this;
                super();
            }
        }
);
        addMouseWheelListener(new MouseWheelListener() {

            final AbstractAttrPane this$0;

            public void mouseWheelMoved(MouseWheelEvent mousewheelevent)
            {
                int i = scrollBar.getValue();
                i += 5 * mousewheelevent.getWheelRotation();
                scrollBar.setValue(i);
                adjustValues();
            }

            
            {
                this$0 = AbstractAttrPane.this;
                super();
            }
        }
);
    }















}
