// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.base.Utils;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartFillStylePane;
import com.fr.general.Inter;
import java.awt.Component;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            Pie2DSeriesPane

public class CombinedPie2DSeriesPane extends Pie2DSeriesPane
{

    private ChartFillStylePane fillColorPane;
    private UITextField zoomTime;

    public CombinedPie2DSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        super(chartstylepane, plot);
    }

    protected JPanel getContentInPlotType()
    {
        initCom();
        fillColorPane = new ChartFillStylePane();
        zoomTime = new UITextField();
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d
        };
        Component acomponent[][] = {
            {
                new UILabel(Inter.getLocText("FR-Chart-Type_Pie")), null
            }, {
                fillColorPane, null
            }, {
                stylePane, null
            }, {
                new BoldFontTextLabel(Inter.getLocText("Chart-Change_Pie_Size")), zoomTime
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    protected ChartFillStylePane getFillStylePane()
    {
        return null;
    }

    public void updateBean(Plot plot)
    {
        super.updateBean(plot);
        plot.setCombinedSize(Utils.string2Number(zoomTime.getText()).doubleValue());
        plot.setPlotFillStyle(fillColorPane.updateBean());
    }

    public void populateBean(Plot plot)
    {
        super.populateBean(plot);
        zoomTime.setText((new StringBuilder()).append("").append(plot.getCombinedSize()).toString());
        fillColorPane.populateBean(plot.getPlotFillStyle());
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
