package com.fr.plugin.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.VanChartAttrTrendLine;
import com.fr.plugin.chart.designer.component.VanChartTrendLinePane;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mitisky on 15/10/19.
 */
public class VanChartTrendLineConditionPane extends ConditionAttrSingleConditionPane<DataSeriesCondition>{
    private UILabel nameLabel;
    private VanChartTrendLinePane trendLinePane;
    private VanChartAttrTrendLine attrTrendLine = new VanChartAttrTrendLine();

    public VanChartTrendLineConditionPane(ConditionAttributesPane conditionAttributesPane) {
        this(conditionAttributesPane, true);
    }

    public VanChartTrendLineConditionPane(ConditionAttributesPane conditionAttributesPane, boolean isRemove) {
        super(conditionAttributesPane, isRemove);
        nameLabel = new UILabel(Inter.getLocText("Chart-Trend_Line"));
        trendLinePane = new VanChartTrendLinePane();

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

        pane.add(trendLinePane);

        this.add(pane);

    }

    /**
     * 条件属性item的名称
     * @return item的名称
     */
    public String nameForPopupMenuItem() {
        return Inter.getLocText("Chart-Trend_Line");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof VanChartAttrTrendLine) {
            attrTrendLine = (VanChartAttrTrendLine) condition;
            this.trendLinePane.populate(attrTrendLine);
        }
    }

    public DataSeriesCondition update() {
        return this.trendLinePane.update();
    }



}
