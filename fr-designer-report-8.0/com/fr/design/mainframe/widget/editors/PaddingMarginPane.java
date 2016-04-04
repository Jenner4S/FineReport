// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.PaddingMargin;
import com.fr.general.Inter;
import java.awt.*;
import javax.swing.JPanel;

public class PaddingMarginPane extends BasicPane
{

    private static final double MIN_VALUE = 0D;
    private UISpinner topSpinner;
    private UISpinner leftSpinner;
    private UISpinner bottomSpinner;
    private UISpinner rightSpinner;
    private JPanel contentPane;
    private LayoutManager coverLayout;

    public PaddingMarginPane()
    {
        coverLayout = new LayoutManager() {

            final PaddingMarginPane this$0;

            public void removeLayoutComponent(Component component)
            {
            }

            public Dimension preferredLayoutSize(Container container)
            {
                return BasicDialog.SMALL;
            }

            public Dimension minimumLayoutSize(Container container)
            {
                return null;
            }

            public void layoutContainer(Container container)
            {
                int i = container.getWidth();
                int j = container.getHeight();
                int k = contentPane.getPreferredSize().width;
                int l = contentPane.getPreferredSize().height;
                contentPane.setBounds((i - k) / 2, (j - l) / 2, k, l);
            }

            public void addLayoutComponent(String s, Component component)
            {
            }

            
            {
                this$0 = PaddingMarginPane.this;
                super();
            }
        }
;
        setLayout(coverLayout);
        contentPane = new JPanel(new GridLayout(2, 2, 4, 4));
        add(contentPane);
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Base_Top")).append(" ").toString()), "West");
        topSpinner = new UISpinner(0.0D, 2147483647D, 1.0D, 0.0D);
        jpanel.add(topSpinner, "Center");
        UILabel uilabel = new UILabel((new StringBuilder()).append(" ").append(Inter.getLocText("FR-Designer_Indent-Pixel")).append("  ").toString());
        jpanel.add(uilabel, "East");
        contentPane.add(jpanel);
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel1.add(new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Base_Left")).append(" ").toString()), "West");
        leftSpinner = new UISpinner(0.0D, 2147483647D, 1.0D, 0.0D);
        jpanel1.add(leftSpinner, "Center");
        UILabel uilabel1 = new UILabel((new StringBuilder()).append(" ").append(Inter.getLocText("FR-Designer_Indent-Pixel")).append("  ").toString());
        jpanel1.add(uilabel1, "East");
        contentPane.add(jpanel1);
        JPanel jpanel2 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel2.add(new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Base_Bottom")).append(" ").toString()), "West");
        bottomSpinner = new UISpinner(0.0D, 2147483647D, 1.0D, 0.0D);
        jpanel2.add(bottomSpinner, "Center");
        UILabel uilabel2 = new UILabel((new StringBuilder()).append(" ").append(Inter.getLocText("FR-Designer_Indent-Pixel")).append("  ").toString());
        jpanel2.add(uilabel2, "East");
        contentPane.add(jpanel2);
        JPanel jpanel3 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel3.add(new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Base_Right")).append(" ").toString()), "West");
        rightSpinner = new UISpinner(0.0D, 2147483647D, 1.0D, 0.0D);
        jpanel3.add(rightSpinner, "Center");
        UILabel uilabel3 = new UILabel((new StringBuilder()).append(" ").append(Inter.getLocText("FR-Designer_Indent-Pixel")).append("  ").toString());
        jpanel3.add(uilabel3, "East");
        contentPane.add(jpanel3);
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("FR-Base_Margin");
    }

    public void populate(PaddingMargin paddingmargin)
    {
        if(paddingmargin == null)
            paddingmargin = new PaddingMargin();
        topSpinner.setValue(paddingmargin.getTop());
        leftSpinner.setValue(paddingmargin.getLeft());
        bottomSpinner.setValue(paddingmargin.getBottom());
        rightSpinner.setValue(paddingmargin.getRight());
    }

    public PaddingMargin update()
    {
        PaddingMargin paddingmargin = new PaddingMargin();
        paddingmargin.setTop((int)topSpinner.getValue());
        paddingmargin.setLeft((int)leftSpinner.getValue());
        paddingmargin.setBottom((int)bottomSpinner.getValue());
        paddingmargin.setRight((int)rightSpinner.getValue());
        return paddingmargin;
    }

}
