// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.GraphHelper;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itooltip.MultiLineToolTip;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.container.*;
import com.fr.general.Inter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

public class WLayoutSelectionPane extends BasicPane
{
    private class KindPane extends JPanel
        implements MouseListener
    {

        private boolean isMouseOver;
        private boolean isMousePressed;
        private WLayout layout;
        final WLayoutSelectionPane this$0;

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            if(isMouseOver || isMousePressed)
            {
                Dimension dimension = getSize();
                GraphHelper.paintImage(g, dimension.width, dimension.height, WLayoutSelectionPane.hoverImage, 1, 2, 0, 105, 89);
            }
        }

        public JToolTip createToolTip()
        {
            MultiLineToolTip multilinetooltip = new MultiLineToolTip();
            multilinetooltip.setComponent(this);
            multilinetooltip.setOpaque(false);
            return multilinetooltip;
        }

        public Insets getInsets()
        {
            return new Insets(10, 10, 10, 10);
        }

        public void mouseClicked(MouseEvent mouseevent)
        {
            if(currentKindPane != null)
            {
                currentKindPane.isMousePressed = false;
                currentKindPane.repaint();
            }
            currentKindPane = this;
            currentKindPane.isMousePressed = true;
            wLayout = layout;
            if(mouseevent.getClickCount() >= 2)
            {
                java.awt.Container container;
                for(container = getParent(); container != null && !(container instanceof BasicDialog); container = container.getParent());
                if(container != null)
                    ((BasicDialog)container).doOK();
            }
        }

        public void mousePressed(MouseEvent mouseevent)
        {
        }

        public void mouseReleased(MouseEvent mouseevent)
        {
        }

        public void mouseEntered(MouseEvent mouseevent)
        {
            isMouseOver = true;
            repaint();
        }

        public void mouseExited(MouseEvent mouseevent)
        {
            isMouseOver = false;
            repaint();
        }

        public KindPane(String s, WLayout wlayout)
        {
            this$0 = WLayoutSelectionPane.this;
            super();
            layout = wlayout;
            setLayout(new BorderLayout(20, 20));
            add(new UILabel(BaseUtils.readIcon(s)));
            addMouseListener(this);
        }
    }

    private class AbsoluteLayoutPane extends BasicPane
    {

        final WLayoutSelectionPane this$0;

        protected String title4PopupWindow()
        {
            return "AbsoluteLayout";
        }

        public AbsoluteLayoutPane()
        {
            this$0 = WLayoutSelectionPane.this;
            super();
            setLayout(new GridLayout(1, 4, 5, 5));
            KindPane kindpane = new KindPane("/com/fr/web/images/form/layout_absolute_big.png", new WAbsoluteLayout());
            add(kindpane);
            add(new UILabel());
            add(new UILabel());
            add(new UILabel());
        }
    }

    private class CardLayoutPane extends BasicPane
    {

        final WLayoutSelectionPane this$0;

        protected String title4PopupWindow()
        {
            return "CardLayout";
        }

        public CardLayoutPane()
        {
            this$0 = WLayoutSelectionPane.this;
            super();
            setLayout(new GridLayout(1, 4, 5, 5));
            KindPane kindpane = new KindPane("/com/fr/web/images/form/layout_absolute_big.png", new WCardLayout());
            kindpane.setToolTipText(Inter.getLocText("WLayout-Card-ToolTip"));
            add(kindpane);
            add(new UILabel());
            add(new UILabel());
            add(new UILabel());
        }
    }

    private class GridLayoutPane extends BasicPane
    {

        final WLayoutSelectionPane this$0;

        protected String title4PopupWindow()
        {
            return "GridLayout";
        }

        public GridLayoutPane()
        {
            this$0 = WLayoutSelectionPane.this;
            super();
            setLayout(new GridLayout(1, 4, 5, 5));
            KindPane kindpane = new KindPane("/com/fr/web/images/form/layout_grid_2x2.png", new WGridLayout(2, 2, 0, 0));
            kindpane.setToolTipText(Inter.getLocText(new String[] {
                "Two_Rows_Of_Two_Grid", "Layout_Container"
            }));
            KindPane kindpane1 = new KindPane("/com/fr/web/images/form/layout_grid_2x3.png", new WGridLayout(2, 3, 0, 0));
            kindpane1.setToolTipText(Inter.getLocText(new String[] {
                "Two_Rows_Of_Three_Grid", "Layout_Container"
            }));
            KindPane kindpane2 = new KindPane("/com/fr/web/images/form/layout_grid_3x2.png", new WGridLayout(3, 2, 0, 0));
            kindpane2.setToolTipText(Inter.getLocText(new String[] {
                "Three_Rows_Of_Two_Grid", "Layout_Container"
            }));
            KindPane kindpane3 = new KindPane("/com/fr/web/images/form/layout_grid_3x3.png", new WGridLayout(3, 3, 0, 0));
            kindpane3.setToolTipText(Inter.getLocText(new String[] {
                "Three_Rows_Of_Three_Grid", "Layout_Container"
            }));
            add(kindpane);
            add(kindpane1);
            add(kindpane2);
            add(kindpane3);
        }
    }

    private class BorderLayoutPane extends BasicPane
    {

        final WLayoutSelectionPane this$0;

        protected String title4PopupWindow()
        {
            return "BorderLayout";
        }

        public BorderLayoutPane()
        {
            this$0 = WLayoutSelectionPane.this;
            super();
            setLayout(new GridLayout(1, 4, 5, 5));
            KindPane kindpane = new KindPane("/com/fr/web/images/form/layout_border_nc.png", new WBorderLayout(0, 0, new String[] {
                "North", "Center"
            }));
            kindpane.setToolTipText(Inter.getLocText("WLayout-Border-LayoutContainer"));
            KindPane kindpane1 = new KindPane("/com/fr/web/images/form/layout_border_ncw.png", new WBorderLayout(0, 0, new String[] {
                "West", "North", "Center"
            }));
            kindpane1.setToolTipText(Inter.getLocText("WLayout-Border-ThreeContainer"));
            KindPane kindpane2 = new KindPane("/com/fr/web/images/form/layout_border_all.png", new WBorderLayout(0, 0));
            kindpane2.setToolTipText(Inter.getLocText("WLayout-Border-ToolTips"));
            add(kindpane);
            add(kindpane1);
            add(kindpane2);
            add(new UILabel());
        }
    }


    private static Image hoverImage = BaseUtils.readImage("com/fr/design/images/form/hover.png");
    private WLayout wLayout;
    private KindPane currentKindPane;

    public WLayoutSelectionPane()
    {
        wLayout = new WBorderLayout();
        setBorder(BorderFactory.createTitledBorder((new StringBuilder()).append(Inter.getLocText("Form-Please_Select_A_Kind_Of_Form_Container")).append(":").toString()));
        setLayout(FRGUIPaneFactory.createBorderLayout());
        Component acomponent[][] = {
            {
                createTypeLabel(Inter.getLocText("BorderLayout")), new BorderLayoutPane()
            }, {
                createTypeLabel(Inter.getLocText("GridLayout")), new GridLayoutPane()
            }, {
                createTypeLabel(Inter.getLocText("CardLayout")), new CardLayoutPane()
            }, {
                createTypeLabel(Inter.getLocText("Form-NullLayout")), new AbsoluteLayoutPane()
            }
        };
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d, d, d, d
        };
        double ad1[] = {
            d, d1
        };
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad, ad1);
        JScrollPane jscrollpane = new JScrollPane(jpanel);
        add(jscrollpane, "Center");
    }

    public WLayout update()
    {
        return wLayout;
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("Widget-Form_Widget_Container");
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(600, 400);
    }

    private UILabel createTypeLabel(String s)
    {
        UILabel uilabel = new UILabel((new StringBuilder()).append(" ").append(s).append(":").toString());
        Font font = new Font("SimSun", 1, 13);
        uilabel.setFont(font);
        return uilabel;
    }

    public static void main(String args[])
    {
        JFrame jframe = new JFrame();
        JPanel jpanel = (JPanel)jframe.getContentPane();
        jpanel.setLayout(FRGUIPaneFactory.createBorderLayout());
        WLayoutSelectionPane wlayoutselectionpane = new WLayoutSelectionPane();
        jpanel.add(wlayoutselectionpane, "Center");
        jframe.setSize(300, 200);
        jframe.setVisible(true);
    }





}
