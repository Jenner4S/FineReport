// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.base.Utils;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.RangePlot;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            AbstractPlotSeriesPane

public class RangeSeriesPane extends AbstractPlotSeriesPane
{

    private UIBasicSpinner seriesWidthSpinner;

    public RangeSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        super(chartstylepane, plot, false);
    }

    public void populateBean(Plot plot)
    {
        super.populateBean(plot);
        seriesWidthSpinner.setValue(Double.valueOf(((RangePlot)plot).getSeriesWidth()));
    }

    public void updateBean(Plot plot)
    {
        super.updateBean(plot);
        ((RangePlot)plot).setSeriesWidth(Double.valueOf(Utils.objectToString(seriesWidthSpinner.getValue())).doubleValue());
    }

    protected JPanel getContentInPlotType()
    {
        seriesWidthSpinner = new UIBasicSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        JPanel jpanel = new JPanel(new BorderLayout());
        jpanel.add(new BoldFontTextLabel(Inter.getLocText(new String[] {
            "ChartF-Series", "Tree-Width"
        })), "West");
        jpanel.add(seriesWidthSpinner, "Center");
        return jpanel;
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
