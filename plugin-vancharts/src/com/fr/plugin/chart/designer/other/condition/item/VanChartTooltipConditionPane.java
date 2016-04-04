package com.fr.plugin.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.designer.style.tooltip.VanChartPlotTooltipNoCheckPane;

import javax.swing.*;
import java.awt.*;

public class VanChartTooltipConditionPane extends ConditionAttrSingleConditionPane<DataSeriesCondition> {

    private static final long serialVersionUID = 7514028150764584873L;
    private VanChartPlotTooltipNoCheckPane tooltipContentsPane;
    private VanChartPlot plot = null;

    public VanChartTooltipConditionPane(ConditionAttributesPane conditionAttributesPane, Class plotClass) {
        this(conditionAttributesPane, true, plotClass);
    }

    public VanChartTooltipConditionPane(ConditionAttributesPane conditionAttributesPane, boolean isRemove, final Class plotClass) {
        super(conditionAttributesPane, isRemove);
        UILabel nameLabel = new UILabel(Inter.getLocText("Plugin-ChartF_Tooltip"));

        JPanel pane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        if (isRemove) {
            this.removeAll();
            this.setLayout(FRGUIPaneFactory.createBorderLayout());
            // 重新布局
            JPanel northPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            northPane.setPreferredSize(new Dimension(100, 30));
            this.add(northPane, BorderLayout.NORTH);

            northPane.add(cancel);
            northPane.add(nameLabel);

            pane.setBorder(BorderFactory.createEmptyBorder(6, 50, 0, 300));
        }

        try{
            plot = (VanChartPlot)plotClass.newInstance();
        } catch (IllegalAccessException e){
            FRLogger.getLogger().error("VanChartTooltipConditionPane IllegalAccessException");
        } catch (InstantiationException e){
            FRLogger.getLogger().error("VanChartTooltipConditionPane InstantiationException");
        }

        this.tooltipContentsPane = new VanChartPlotTooltipNoCheckPane(plot, null);

        pane.add(tooltipContentsPane);

        this.add(pane);
    }

    /**
     * 条件属性item的名称
     * @return item的名称
     */
    public String nameForPopupMenuItem() {
        return Inter.getLocText("Plugin-ChartF_Tooltip");
    }

    public void setDefault() {
        //下面这句话是给各个组件一个默认值
        AttrTooltip tooltip = plot == null ? new AttrTooltip() : plot.getDefaultAttrTooltip();
        this.tooltipContentsPane.populate(tooltip);
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof AttrTooltip) {
            this.tooltipContentsPane.populate((AttrTooltip) condition);
        }
    }

    public DataSeriesCondition update() {
        return this.tooltipContentsPane.update();
    }
}