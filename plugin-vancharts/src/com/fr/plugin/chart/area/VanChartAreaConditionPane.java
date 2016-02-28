package com.fr.plugin.chart.area;

import com.fr.chart.base.AttrBackground;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.plugin.chart.base.*;
import com.fr.plugin.chart.designer.other.condition.item.*;

import java.awt.*;

/**
 * Created by Mitisky on 15/11/18.
 */
public class VanChartAreaConditionPane extends DataSeriesConditionPane {
    private static final long serialVersionUID = -7180705321732069806L;

    protected void initComponents() {
        super.initComponents();
        //添加全部条件属性后被遮挡
        liteConditionPane.setPreferredSize(new Dimension(300, 400));
    }

    @Override
    protected void addBasicAction() {
        classPaneMap.put(AttrBackground.class, new VanChartSeriesColorConditionPane(this));
        classPaneMap.put(AttrLabel.class, new VanChartLabelConditionPane(this, class4Correspond()));
        classPaneMap.put(AttrTooltip.class, new VanChartTooltipConditionPane(this, class4Correspond()));
        classPaneMap.put(VanChartAttrTrendLine.class, new VanChartTrendLineConditionPane(this));
        classPaneMap.put(VanChartAttrMarker.class, new VanChartMarkerConditionPane(this));
        classPaneMap.put(AttrAreaSeriesFillColorBackground.class, new VanChartAreaFillColorConditionPane(this));
        classPaneMap.put(VanChartAttrLine.class, new VanChartLineTypeConditionPane(this));
    }

    protected void addStyleAction() {

    }

    /**
     * 返回图表class对象
     * @return class对象
     */
    public Class<? extends Plot> class4Correspond() {
        return VanChartAreaPlot.class;
    }
}
