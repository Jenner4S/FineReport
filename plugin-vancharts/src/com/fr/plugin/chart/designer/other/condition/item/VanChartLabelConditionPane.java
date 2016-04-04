package com.fr.plugin.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.Plot;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.designer.style.label.VanChartPlotLabelNoCheckPane;

import javax.swing.*;
import java.awt.*;

public class VanChartLabelConditionPane extends ConditionAttrSingleConditionPane<DataSeriesCondition> {

    private static final long serialVersionUID = 1338868748575437659L;
    private UILabel nameLabel;
    private VanChartPlotLabelNoCheckPane dataLabelContentsPane;

    private AttrLabel attrContents = new AttrLabel();

    public VanChartLabelConditionPane(ConditionAttributesPane conditionAttributesPane, Class plotClass) {
        this(conditionAttributesPane, true, plotClass);
    }

    public VanChartLabelConditionPane(ConditionAttributesPane conditionAttributesPane, boolean isRemove, final Class plotClass) {
        super(conditionAttributesPane, isRemove);
        nameLabel = new UILabel(Inter.getLocText("FR-Chart-Chart_Label"));

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

        Plot plot = null;
        try{
            plot = (Plot)plotClass.newInstance();
        } catch (IllegalAccessException e){
            FRLogger.getLogger().error("VanChartLabelConditionPane IllegalAccessException");
        } catch (InstantiationException e){
            FRLogger.getLogger().error("VanChartLabelConditionPane InstantiationException");
        }
        this.dataLabelContentsPane = new VanChartPlotLabelNoCheckPane(plot,null);
        pane.add(dataLabelContentsPane);

        this.add(pane);
    }

    /**
     * 条件属性item的名称
     * @return item的名称
     */
    public String nameForPopupMenuItem() {
        return Inter.getLocText("FR-Chart-Chart_Label");
    }

    public void setDefault() {
        //下面这句话是给各组件一个默认值
        this.dataLabelContentsPane.populate(new AttrLabel());
    }


    public void populate(DataSeriesCondition condition) {
        if (condition instanceof AttrLabel) {
            attrContents = (AttrLabel) condition;
            this.dataLabelContentsPane.populate(attrContents);
        }
    }

    public DataSeriesCondition update() {
        return this.dataLabelContentsPane.update();
    }
}