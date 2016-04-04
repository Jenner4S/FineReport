package com.fr.plugin.chart.line;

import com.fr.chart.base.AttrBackground;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.plugin.chart.base.*;
import com.fr.plugin.chart.designer.other.condition.item.*;

import java.awt.*;

/**
 * Created by Mitisky on 15/11/5.
 */
public class VanChartLineConditionPane extends DataSeriesConditionPane {
    private static final long serialVersionUID = -7180705321732069806L;

    protected void initComponents() {
        super.initComponents();
        //���ȫ���������Ժ��ڵ�
        liteConditionPane.setPreferredSize(new Dimension(300, 400));
    }

    @Override
    protected void addBasicAction() {
        classPaneMap.put(AttrBackground.class, new VanChartSeriesColorConditionPane(this));
        classPaneMap.put(AttrLabel.class, new VanChartLabelConditionPane(this, class4Correspond()));
        classPaneMap.put(AttrTooltip.class, new VanChartTooltipConditionPane(this, class4Correspond()));
        classPaneMap.put(VanChartAttrTrendLine.class, new VanChartTrendLineConditionPane(this));
        classPaneMap.put(VanChartAttrMarker.class, new VanChartMarkerConditionPane(this));
        classPaneMap.put(VanChartAttrLine.class, new VanChartLineTypeConditionPane(this));
    }

    protected void addStyleAction() {

    }

    /**
     * ����ͼ��class����
     * @return class����
     */
    public Class<? extends Plot> class4Correspond() {
        return VanChartLinePlot.class;
    }
}
