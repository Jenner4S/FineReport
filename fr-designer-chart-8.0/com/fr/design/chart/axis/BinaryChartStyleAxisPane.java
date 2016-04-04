// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.axis;

import com.fr.chart.chartattr.*;
import com.fr.design.mainframe.chart.gui.style.axis.*;

// Referenced classes of package com.fr.design.chart.axis:
//            ChartStyleAxisPane, AxisStyleObject

public class BinaryChartStyleAxisPane extends ChartStyleAxisPane
{

    public BinaryChartStyleAxisPane(Plot plot)
    {
        super(plot);
    }

    public final AxisStyleObject[] createAxisStyleObjects(Plot plot)
    {
        return (new AxisStyleObject[] {
            getXAxisPane(plot), getYAxisPane(plot)
        });
    }

    protected AxisStyleObject getXAxisPane(final Plot plot)
    {
        return new AxisStyleObject(CATE_AXIS, new ChartCategoryPane() {

            final Plot val$plot;
            final BinaryChartStyleAxisPane this$0;

            protected boolean isSupportLineStyle()
            {
                return plot.isSupportAxisLineStyle();
            }

            
            {
                this$0 = BinaryChartStyleAxisPane.this;
                plot = plot1;
                super();
            }
        }
);
    }

    protected AxisStyleObject getYAxisPane(final Plot plot)
    {
        Axis axis = plot.getyAxis();
        boolean flag = false;
        if(plot instanceof CategoryPlot)
            flag = ((CategoryPlot)plot).isStacked();
        ChartValuePane chartvaluepane = new ChartValuePane() {

            final Plot val$plot;
            final BinaryChartStyleAxisPane this$0;

            protected boolean isSupportLineStyle()
            {
                return plot.isSupportAxisLineStyle();
            }

            
            {
                this$0 = BinaryChartStyleAxisPane.this;
                plot = plot1;
                super();
            }
        }
;
        boolean flag1 = axis.isPercentage() && flag;
        Object obj = flag1 ? ((Object) (new ChartPercentValuePane())) : ((Object) (chartvaluepane));
        return new AxisStyleObject(VALUE_AXIS, ((com.fr.design.mainframe.chart.gui.style.axis.ChartAxisUsePane) (obj)));
    }
}
