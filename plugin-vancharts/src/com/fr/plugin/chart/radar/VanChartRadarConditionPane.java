package com.fr.plugin.chart.radar;

import com.fr.chart.base.AttrBackground;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.plugin.chart.base.*;
import com.fr.plugin.chart.designer.other.condition.item.*;

import java.awt.*;

/**
 * Created by Mitisky on 15/12/29.
 */
public class VanChartRadarConditionPane extends DataSeriesConditionPane{
    private static final long serialVersionUID = 5696786807262339122L;

    protected void initComponents() {
        super.initComponents();
        //���ȫ���������Ժ��ڵ�
        liteConditionPane.setPreferredSize(new Dimension(300, 400));
    }

    protected void addRadarAction() {
        classPaneMap.put(VanChartAttrMarker.class, new VanChartMarkerConditionPane(this));
        classPaneMap.put(VanChartAttrLine.class, new VanChartLineWidthConditionPane(this));
        classPaneMap.put(AttrAreaSeriesFillColorBackground.class, new VanChartAreaFillColorConditionPane(this));
    }

    @Override
    protected void addBasicAction() {
        classPaneMap.put(AttrBackground.class, new VanChartSeriesColorConditionPane(this));
        classPaneMap.put(AttrLabel.class, new VanChartLabelConditionPane(this, class4Correspond()));
        classPaneMap.put(AttrTooltip.class, new VanChartTooltipConditionPane(this, class4Correspond()));
        addRadarAction();
    }

    protected void addStyleAction() {

    }

    /**
     * ����ͼ��class����
     * @return class����
     */
    public Class<? extends Plot> class4Correspond() {
        return VanChartRadarPlot.class;
    }
}
