package com.fr.plugin.chart.pie;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrBackground;
import com.fr.chart.base.AttrBorder;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelAlphaPane;
import com.fr.design.chart.series.SeriesCondition.LabelBorderPane;
import com.fr.plugin.chart.PiePlot4VanChart;
import com.fr.plugin.chart.base.AttrFloatColor;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.designer.other.condition.item.VanChartFloatColorConditionPane;
import com.fr.plugin.chart.designer.other.condition.item.VanChartLabelConditionPane;
import com.fr.plugin.chart.designer.other.condition.item.VanChartSeriesColorConditionPane;
import com.fr.plugin.chart.designer.other.condition.item.VanChartTooltipConditionPane;

import java.awt.*;

/**
 * ��ͼ�������� ��ɫ(ϵ�еı���ɫ) ͸���� �߿� ��ǩ ���ݵ���ʾ ������ɫ��
 */
public class VanChartPieConditionPane extends DataSeriesConditionPane{

    private static final long serialVersionUID = -7180705321732069806L;

    protected void initComponents() {
        super.initComponents();
        //���ȫ���������Ժ��ڵ�
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
    }

    protected void addStyleAction() {

    }

    /**
     * ����ͼ��class����
     * @return class����
     */
    public Class<? extends Plot> class4Correspond() {
        return PiePlot4VanChart.class;
    }
}
