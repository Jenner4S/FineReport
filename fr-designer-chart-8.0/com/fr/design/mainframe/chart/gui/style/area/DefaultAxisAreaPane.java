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
import java.awt.*;
import javax.swing.JPanel;
import javax.swing.JSeparator;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.area:
//            ChartAxisAreaPane

public class DefaultAxisAreaPane extends ChartAxisAreaPane
{

    private JPanel backgroundPane;
    private ColorSelectBox horizontalColorPane;
    private ColorSelectBox verticalColorPane;
    private JPanel gridlinePane;
    private UICheckBox isVerticleGridLine;
    private UICheckBox isHorizontalGridLine;
    private ColorSelectBox gridColorPane;

    public DefaultAxisAreaPane()
    {
        initBackgroundColorPane();
        initGridlinePane();
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d, d, d
        };
        Component acomponent[][] = {
            {
                backgroundPane
            }, {
                new JSeparator()
            }, {
                gridlinePane
            }
        };
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        setLayout(new BorderLayout());
        add(jpanel, "Center");
    }

    private void initBackgroundColorPane()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d
        };
        double ad1[] = {
            d, d1
        };
        horizontalColorPane = new ColorSelectBox(100);
        verticalColorPane = new ColorSelectBox(100);
        Component acomponent[][] = {
            {
                new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("Utils-Left_to_Right")).append(":").toString()), horizontalColorPane
            }, {
                new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("Utils-Top_to_Bottom")).append(":").toString()), verticalColorPane
            }
        };
        backgroundPane = TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
            "Chart_Interval_Back"
        }, acomponent, ad, ad1);
    }

    private void initGridlinePane()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d
        };
        double ad1[] = {
            d, d1
        };
        isVerticleGridLine = new UICheckBox(Inter.getLocText("Utils-Left_to_Right"));
        isHorizontalGridLine = new UICheckBox(Inter.getLocText("Utils-Top_to_Bottom"));
        gridColorPane = new ColorSelectBox(100);
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane(new Component[][] {
            new Component[] {
                new BoldFontTextLabel(Inter.getLocText("Color")), gridColorPane
            }
        }, ad, ad1);
        Component acomponent[][] = {
            {
                isVerticleGridLine, isHorizontalGridLine
            }, {
                jpanel, null
            }
        };
        gridlinePane = TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
            "ChartF-Grid_Line"
        }, acomponent, ad, ad1);
    }

    public void populateBean(Plot plot)
    {
        isVerticleGridLine.setSelected(plot.getyAxis().getMainGridStyle() != 0);
        isHorizontalGridLine.setSelected(plot.getxAxis().getMainGridStyle() != 0);
        gridColorPane.setSelectObject(isVerticleGridLine.isSelected() ? plot.getyAxis().getMainGridColor() : plot.getxAxis().getMainGridColor());
        horizontalColorPane.setSelectObject(((RectanglePlot)plot).getHorizontalIntervalBackgroundColor());
        verticalColorPane.setSelectObject(((RectanglePlot)plot).getVerticalIntervalBackgroundColor());
    }

    public void updateBean(Plot plot)
    {
        if(isVerticleGridLine.isSelected())
        {
            plot.getyAxis().setMainGridStyle(1);
            plot.getyAxis().setMainGridColor(gridColorPane.getSelectObject());
        } else
        {
            plot.getyAxis().setMainGridStyle(0);
        }
        if(isHorizontalGridLine.isSelected())
        {
            plot.getxAxis().setMainGridStyle(1);
            plot.getxAxis().setMainGridColor(gridColorPane.getSelectObject());
        } else
        {
            plot.getxAxis().setMainGridStyle(0);
        }
        ((RectanglePlot)plot).setHorizontalIntervalBackgroundColor(horizontalColorPane.getSelectObject());
        ((RectanglePlot)plot).setVerticalIntervalBackgroundColor(verticalColorPane.getSelectObject());
    }
}
