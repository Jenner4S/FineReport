// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui;

import com.fr.chart.chartattr.*;
import com.fr.design.chart.report.GisMapDataPane4Chart;
import com.fr.design.chart.report.MapDataPane4Chart;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.data.ImportSetChartDataPane;
import com.fr.general.FRLogger;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui:
//            ChartDataPane

public class ChartDesignerDataPane extends ChartDataPane
{

    private AttributeChangeListener listener;

    public ChartDesignerDataPane(AttributeChangeListener attributechangelistener)
    {
        super(attributechangelistener);
        addMouseListener(new MouseAdapter() {

            final ChartDesignerDataPane this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                FRLogger.getLogger().info("SD");
            }

            
            {
                this$0 = ChartDesignerDataPane.this;
                super();
            }
        }
);
        listener = attributechangelistener;
    }

    protected JPanel createContentPane()
    {
        contentsPane = new ImportSetChartDataPane(listener, this);
        return contentsPane;
    }

    protected void repeatLayout(ChartCollection chartcollection)
    {
        if(contentsPane != null)
            remove(contentsPane);
        setLayout(new BorderLayout(0, 0));
        if(chartcollection.getSelectedChart().getPlot() instanceof MapPlot)
            contentsPane = new MapDataPane4Chart(listener, this);
        else
        if(chartcollection.getSelectedChart().getPlot() instanceof GisMapPlot)
            contentsPane = new GisMapDataPane4Chart(listener, this);
        else
            contentsPane = new ImportSetChartDataPane(listener, this);
    }

    public boolean isNeedPresentPaneWhenFilterData()
    {
        return true;
    }
}
