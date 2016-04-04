// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.AttrContents;
import com.fr.design.chart.ChartPlotFactory;
import com.fr.design.chart.series.SeriesCondition.dlp.DataLabelPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.layout.FRGUIPaneFactory;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class DataLabelContentsPane extends BasicPane
{

    private static final long serialVersionUID = 0x91df49931bd5cc73L;
    private DataLabelPane dataLabelPane;

    public DataLabelContentsPane(Class class1)
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        add(jpanel, "North");
        jpanel.add(createLabelPane(class1), "North");
    }

    protected String title4PopupWindow()
    {
        return "Label";
    }

    private JPanel createLabelPane(Class class1)
    {
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        jpanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 10, 10));
        jpanel.add(dataLabelPane = ChartPlotFactory.createDataLabelPane4Plot(class1), "Center");
        return jpanel;
    }

    public void checkGuidBox()
    {
        dataLabelPane.checkGuidBox();
    }

    public void populate(AttrContents attrcontents)
    {
        dataLabelPane.populate(attrcontents);
    }

    public void update(AttrContents attrcontents)
    {
        dataLabelPane.update(attrcontents);
    }
}
