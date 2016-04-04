// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.base.AreaColor;
import com.fr.chart.chartattr.MeterPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.MapHotAreaColor;
import com.fr.chart.chartglyph.MeterInterval;
import com.fr.chart.chartglyph.MeterStyle;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartFillStylePane;
import com.fr.general.Inter;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JSeparator;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            AbstractPlotSeriesPane, ColorPickerPaneWithFormula, UIColorPickerPane

public class MeterSeriesPane extends AbstractPlotSeriesPane
{

    private UITextField unit;
    private UINumberDragPane angleMax;
    private UIComboBox order;
    private UIColorPickerPane colorPickerPane;

    public MeterSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        super(chartstylepane, plot, false);
    }

    protected JPanel getContentInPlotType()
    {
        unit = new UITextField();
        angleMax = new UINumberDragPane(0.0D, 360D);
        String as[] = {
            Inter.getLocText("Ge"), Inter.getLocText("Unit_Ten"), Inter.getLocText("Unit_Hundred"), Inter.getLocText("Unit_Thousand"), Inter.getLocText("Unit_Ten_Thousand")
        };
        order = new UIComboBox(as);
        colorPickerPane = createColorPickerPane();
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d, d
        };
        return TableLayoutHelper.createTableLayoutPane(createComponents(), ad1, ad);
    }

    protected UIColorPickerPane createColorPickerPane()
    {
        return new ColorPickerPaneWithFormula("meterString");
    }

    private Component[][] createComponents()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d
        };
        Component acomponent[][] = {
            {
                new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("Pointer-A-Tick-Order")).append(":").toString(), 2), order
            }
        };
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        Component acomponent1[][] = {
            {
                new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("Chart_Needle_Max_Range")).append(":").toString(), 2), angleMax
            }, {
                new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("Unit")).append(":").toString(), 2), unit
            }, {
                new JSeparator(), null
            }, {
                jpanel, null
            }, {
                colorPickerPane, null
            }
        };
        if(plot.isMeterPlot())
        {
            MeterPlot meterplot = (MeterPlot)plot;
            if(meterplot.getMeterStyle().getMeterType() == 0)
                return acomponent1;
            acomponent1 = (new Component[][] {
                new Component[] {
                    new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("Unit")).append(":").toString(), 2), unit
                }, new Component[] {
                    new JSeparator(), null
                }, new Component[] {
                    jpanel, null
                }, new Component[] {
                    colorPickerPane, null
                }
            });
        }
        return acomponent1;
    }

    protected ChartFillStylePane getFillStylePane()
    {
        return null;
    }

    public void populateBean(Plot plot)
    {
        if(plot.isMeterPlot())
        {
            MeterPlot meterplot = (MeterPlot)plot;
            MeterStyle meterstyle = meterplot.getMeterStyle();
            colorPickerPane.populateBean(meterstyle.getMapHotAreaColor());
            order.setSelectedIndex(meterstyle.getOrder());
            unit.setText(meterstyle.getUnits());
            angleMax.populateBean(new Double(meterstyle.getMaxArrowAngle()));
        }
    }

    public void updateBean(Plot plot)
    {
        if(plot.isMeterPlot())
        {
            MeterPlot meterplot = (MeterPlot)plot;
            MeterStyle meterstyle = meterplot.getMeterStyle();
            MapHotAreaColor maphotareacolor = meterstyle.getMapHotAreaColor();
            colorPickerPane.updateBean(maphotareacolor);
            meterstyle.setOrder(order.getSelectedIndex());
            meterstyle.setMaxArrowAngle(angleMax.updateBean().intValue());
            meterstyle.setDesignTyle(colorPickerPane.getDesignType());
            meterstyle.setUnits(unit.getText());
            if(meterstyle.getDesignType() == 1)
            {
                meterstyle.clearAllInterval();
                int i = maphotareacolor.getAreaNumber();
                java.util.List list = maphotareacolor.getAreaColorList();
                for(int j = i - 1; j >= 0; j--)
                {
                    AreaColor areacolor = (AreaColor)list.get(j);
                    MeterInterval meterinterval = new MeterInterval();
                    meterinterval.setBeginValue(areacolor.getMax());
                    meterinterval.setEndValue(areacolor.getMin());
                    meterinterval.setBackgroundColor(areacolor.getAreaColor());
                    meterstyle.addInterval(meterinterval);
                }

            }
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
