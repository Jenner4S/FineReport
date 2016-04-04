// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.base.Utils;
import com.fr.chart.chartattr.PiePlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartBeautyPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            AbstractPlotSeriesPane

public class Pie2DSeriesPane extends AbstractPlotSeriesPane
{

    private static final long serialVersionUID = 0x26ddfb25569516fcL;
    private static final int NUM = 100;
    protected ChartBeautyPane stylePane;
    protected UICheckBox isSecondPlot;
    protected UIButtonGroup secondPlotType;
    protected UIBasicSpinner smallPercent;

    public Pie2DSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        super(chartstylepane, plot, false);
    }

    protected void initCom()
    {
        stylePane = new ChartBeautyPane();
        isSecondPlot = new UICheckBox(Inter.getLocText("Chart_Second_Plot"));
        String as[] = {
            Inter.getLocText("PieStyle"), Inter.getLocText("BarStyle")
        };
        Integer ainteger[] = {
            Integer.valueOf(0), Integer.valueOf(1)
        };
        secondPlotType = new UIButtonGroup(as, ainteger);
        smallPercent = new UIBasicSpinner(new SpinnerNumberModel(5, 0, 100, 1));
        secondPlotType.setSelectedIndex(0);
        isSecondPlot.addActionListener(new ActionListener() {

            final Pie2DSeriesPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                checkBoxUse();
            }

            
            {
                this$0 = Pie2DSeriesPane.this;
                super();
            }
        }
);
    }

    protected JPanel getContentInPlotType()
    {
        initCom();
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
                stylePane, null
            }, {
                new JSeparator(), null
            }, {
                isSecondPlot, null
            }, {
                new UILabel((new StringBuilder()).append("    ").append(Inter.getLocText(new String[] {
                    "Chart_Second_Plot", "Content"
                })).toString()), secondPlotType
            }, {
                new UILabel((new StringBuilder()).append("    ").append(Inter.getLocText(new String[] {
                    "ConditionB-is_less_than", "StyleFormat-Percent"
                })).toString()), smallPercent
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    private void checkBoxUse()
    {
        boolean flag = isSecondPlot.isSelected();
        GUICoreUtils.setEnabled(secondPlotType, flag);
        GUICoreUtils.setEnabled(smallPercent, flag);
    }

    public void populateBean(Plot plot)
    {
        super.populateBean(plot);
        if(plot instanceof PiePlot)
        {
            PiePlot pieplot = (PiePlot)plot;
            stylePane.populateBean(Integer.valueOf(pieplot.getPlotStyle()));
            isSecondPlot.setSelected(false);
            if(pieplot.getSubType() == 2)
            {
                isSecondPlot.setSelected(true);
                secondPlotType.setSelectedIndex(0);
            } else
            if(pieplot.getSubType() == 3)
            {
                isSecondPlot.setSelected(true);
                secondPlotType.setSelectedIndex(1);
            }
            if(isSecondPlot.isSelected())
                smallPercent.setValue(Double.valueOf(pieplot.getSmallPercent() * 100D));
        }
        checkBoxUse();
    }

    public void updateBean(Plot plot)
    {
        super.updateBean(plot);
        if(plot instanceof PiePlot)
        {
            PiePlot pieplot = (PiePlot)plot;
            pieplot.setPlotStyle(stylePane.updateBean().intValue());
            if(isSecondPlot.isSelected())
            {
                if(secondPlotType.getSelectedIndex() == 0)
                    pieplot.setSubType(2);
                else
                if(secondPlotType.getSelectedIndex() == 1)
                    pieplot.setSubType(3);
                Number number = Utils.objectToNumber(smallPercent.getValue(), true);
                if(number != null)
                    pieplot.setSmallPercent(number.doubleValue() / 100D);
            } else
            {
                pieplot.setSubType(1);
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
