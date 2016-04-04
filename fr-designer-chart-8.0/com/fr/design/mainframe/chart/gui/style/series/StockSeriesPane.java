// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.StockPlot;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartFillStylePane;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JSeparator;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            AbstractPlotSeriesPane

public class StockSeriesPane extends AbstractPlotSeriesPane
{

    private LineComboBox upLineBox;
    private ColorSelectBox upBorderColor;
    private ColorSelectBox upBackColor;
    private LineComboBox downLineBox;
    private ColorSelectBox downBorderColor;
    private ColorSelectBox downBackColor;

    public StockSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        super(chartstylepane, plot, false);
    }

    protected JPanel getContentInPlotType()
    {
        upLineBox = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
        upBorderColor = new ColorSelectBox(100);
        upBackColor = new ColorSelectBox(100);
        downLineBox = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
        downBorderColor = new ColorSelectBox(100);
        downBackColor = new ColorSelectBox(100);
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d, d, d, d, d, d
        };
        Component acomponent[][] = {
            {
                new BoldFontTextLabel(Inter.getLocText("UpBarBorderStyleAndColor")), null
            }, {
                new BoldFontTextLabel(Inter.getLocText(new String[] {
                    "Border", "Line-Style"
                })), upLineBox
            }, {
                new BoldFontTextLabel(Inter.getLocText("Border-Color")), upBorderColor
            }, {
                new BoldFontTextLabel(Inter.getLocText(new String[] {
                    "Background", "Color"
                })), upBackColor
            }, {
                new JSeparator(), null
            }, {
                new BoldFontTextLabel(Inter.getLocText("DownBarBorderStyleAndColor")), null
            }, {
                new BoldFontTextLabel(Inter.getLocText(new String[] {
                    "Border", "Line-Style"
                })), downLineBox
            }, {
                new BoldFontTextLabel(Inter.getLocText("Border-Color")), downBorderColor
            }, {
                new BoldFontTextLabel(Inter.getLocText(new String[] {
                    "Background", "Color"
                })), downBackColor
            }
        };
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        return jpanel;
    }

    protected ChartFillStylePane getFillStylePane()
    {
        return null;
    }

    public void populateBean(Plot plot)
    {
        super.populateBean(plot);
        if(plot instanceof StockPlot)
        {
            StockPlot stockplot = (StockPlot)plot;
            upLineBox.setSelectedLineStyle(stockplot.getUpBarBorderLineStyle());
            upBorderColor.setSelectObject(stockplot.getUpBarBorderLineBackground());
            upBackColor.setSelectObject(stockplot.getUpBarBackground());
            downLineBox.setSelectedLineStyle(stockplot.getDownBarBorderLineStyle());
            downBorderColor.setSelectObject(stockplot.getDownBarBorderLineBackground());
            downBackColor.setSelectObject(stockplot.getDownBarBackground());
        }
    }

    public void updateBean(Plot plot)
    {
        super.updateBean(plot);
        if(plot instanceof StockPlot)
        {
            StockPlot stockplot = (StockPlot)plot;
            stockplot.setUpBarBorderLineStyle(upLineBox.getSelectedLineStyle());
            stockplot.setUpBarBorderLineBackground(upBorderColor.getSelectObject());
            stockplot.setUpBarBackground(upBackColor.getSelectObject());
            stockplot.setDownBarBorderLineStyle(downLineBox.getSelectedLineStyle());
            stockplot.setDownBarBorderLineBackground(downBorderColor.getSelectObject());
            stockplot.setDownBarBackground(downBackColor.getSelectObject());
        }
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((Plot)obj);
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Plot)obj);
    }
}
