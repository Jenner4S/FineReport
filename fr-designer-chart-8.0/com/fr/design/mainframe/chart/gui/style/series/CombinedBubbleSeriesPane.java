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
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            BubbleSeriesPane

public class CombinedBubbleSeriesPane extends BubbleSeriesPane
{

    private ChartFillStylePane fillColorPane;

    public CombinedBubbleSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        super(chartstylepane, plot);
    }

    protected JPanel getContentInPlotType()
    {
        initCom();
        fillColorPane = new ChartFillStylePane();
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
                new UILabel(Inter.getLocText("FR-Chart-Chart_BubbleChart")), null
            }, {
                fillColorPane, null
            }, {
                bubbleMean, null
            }, {
                new BoldFontTextLabel(Inter.getLocText("Chart_Change_Bubble_Size")), zoomTime
            }, {
                isMinus, null
            }
        };
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        setLayout(new BorderLayout());
        add(jpanel, "Center");
        return jpanel;
    }

    protected ChartFillStylePane getFillStylePane()
    {
        return null;
    }

    public void populateBean(Plot plot)
    {
        super.populateBean(plot);
        fillColorPane.populateBean(plot.getPlotFillStyle());
    }

    public void updateBean(Plot plot)
    {
        super.updateBean(plot);
        plot.setCombinedSize(Utils.string2Number(zoomTime.getText()).doubleValue());
        plot.setPlotFillStyle(fillColorPane.updateBean());
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
