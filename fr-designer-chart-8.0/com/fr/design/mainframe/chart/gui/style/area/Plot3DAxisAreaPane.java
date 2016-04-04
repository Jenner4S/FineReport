// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.area;

import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.area:
//            ChartAxisAreaPane

public class Plot3DAxisAreaPane extends ChartAxisAreaPane
{

    private UICheckBox gridLine;
    private ColorSelectBox gridColorPane;

    public Plot3DAxisAreaPane()
    {
        gridLine = new UICheckBox(Inter.getLocText("ChartF-Grid_Line"));
        gridColorPane = new ColorSelectBox(100);
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d
        };
        Component acomponent[][] = {
            {
                gridLine, null
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
        gridLine.setSelected(plot.getyAxis().getMainGridStyle() != 0);
        gridColorPane.setSelectObject(plot.getyAxis().getMainGridColor());
    }

    public void updateBean(Plot plot)
    {
        if(gridLine.isSelected())
        {
            plot.getyAxis().setMainGridStyle(1);
            plot.getyAxis().setMainGridColor(gridColorPane.getSelectObject());
        } else
        {
            plot.getyAxis().setMainGridStyle(0);
        }
    }
}
