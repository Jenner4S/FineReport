// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.area;

import com.fr.chart.chartattr.*;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JSeparator;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.area:
//            ChartAxisAreaPane

public class RadarAxisAreaPane extends ChartAxisAreaPane
{

    private ColorSelectBox horizontalColorPane;
    private UICheckBox isHorizontalGridLine;
    private ColorSelectBox gridColorPane;

    public RadarAxisAreaPane()
    {
        horizontalColorPane = new ColorSelectBox(100);
        isHorizontalGridLine = new UICheckBox(Inter.getLocText("Chart_Main_Grid"));
        gridColorPane = new ColorSelectBox(100);
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d, d
        };
        Component acomponent[][] = {
            {
                new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("Chart_Interval_Back")).append(":").toString()), horizontalColorPane
            }, {
                new JSeparator(), null
            }, {
                new BoldFontTextLabel(Inter.getLocText("Chart_Main_Grid")), null
            }, {
                new BoldFontTextLabel(Inter.getLocText("Color")), gridColorPane
            }
        };
        javax.swing.JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        setLayout(new BorderLayout());
        add(jpanel, "Center");
    }

    public void populateBean(Plot plot)
    {
        RadarPlot radarplot = (RadarPlot)plot;
        isHorizontalGridLine.setSelected(radarplot.getxAxis().getMainGridStyle() != 0);
        gridColorPane.setSelectObject(radarplot.getxAxis().getMainGridColor());
        horizontalColorPane.setSelectObject(radarplot.getIntervalColor());
    }

    public void updateBean(Plot plot)
    {
        RadarPlot radarplot = (RadarPlot)plot;
        if(isHorizontalGridLine.isSelected())
        {
            radarplot.getxAxis().setMainGridStyle(1);
            radarplot.getxAxis().setMainGridColor(gridColorPane.getSelectObject());
        } else
        {
            radarplot.getxAxis().setMainGridStyle(0);
        }
        radarplot.setIntervalColor(horizontalColorPane.getSelectObject());
    }
}
