package com.fr.plugin.chart.column;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrBackground;
import com.fr.chart.base.AttrBorder;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelAlphaPane;
import com.fr.design.chart.series.SeriesCondition.LabelBorderPane;
import com.fr.plugin.chart.base.*;
import com.fr.plugin.chart.designer.other.condition.item.*;

import java.awt.*;

/**
 * Created by Mitisky on 15/9/28.
 */
public class VanChartColumnConditionPane extends DataSeriesConditionPane {
    private static final long serialVersionUID = -7180705321732069806L;

    protected void initComponents() {
        super.initComponents();
        //添加全部条件属性后被遮挡
        liteConditionPane.setPreferredSize(new Dimension(300, 400));
    }

    @Override
    protected void addBasicAction() {
        classPaneMap.put(AttrBackground.class, new VanChartSeriesColorConditionPane(this));
        classPaneMap.put(AttrAlpha.class, new LabelAlphaPane(this));
        classPaneMap.put(AttrBorder.class, new LabelBorderPane(this));
        classPaneMap.put(AttrLabel.class, new VanChartLabelConditionPane(this, class4Correspond()));
        classPaneMap.put(AttrTooltip.class, new VanChartTooltipConditionPane(this, class4Correspond()));
        classPaneMap.put(AttrFloatColor.class, new VanChartFloatColorConditionPane(this));
        classPaneMap.put(VanChartAttrTrendLine.class, new VanChartTrendLineConditionPane(this));
        classPaneMap.put(AttrSeriesImageBackground.class, new VanChartSeriesImageBackgroundConditionPane(this));
    }

    protected void addStyleAction() {

    }

    /**
     * 返回图表class对象
     * @return class对象
     */
    public Class<? extends Plot> class4Correspond() {
        return VanChartColumnPlot.class;
    }
}
