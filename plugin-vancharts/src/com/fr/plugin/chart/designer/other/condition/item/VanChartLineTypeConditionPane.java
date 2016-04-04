package com.fr.plugin.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.VanChartAttrLine;
import com.fr.plugin.chart.designer.component.VanChartLineTypePane;

import javax.swing.*;
import java.awt.*;

/**
 * 折线图，线型相关条件属性
 */
public class VanChartLineTypeConditionPane extends ConditionAttrSingleConditionPane<DataSeriesCondition>{
    private static final long serialVersionUID = 1924676751313839477L;
    private UILabel nameLabel;
    private VanChartLineTypePane lineTypePane;
    private VanChartAttrLine attrLine = new VanChartAttrLine();

    public VanChartLineTypeConditionPane(ConditionAttributesPane conditionAttributesPane) {
        this(conditionAttributesPane, true);
    }

    public VanChartLineTypeConditionPane(ConditionAttributesPane conditionAttributesPane, boolean isRemove) {
        super(conditionAttributesPane, isRemove);
        nameLabel = new UILabel(Inter.getLocText("Plugin-ChartF_LineStyle"));
        lineTypePane = createLinePane();

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

        pane.add(lineTypePane);

        this.add(pane);

    }

    protected VanChartLineTypePane createLinePane() {
        return new VanChartLineTypePane();
    }

    /**
     * 条件属性item的名称
     * @return item的名称
     */
    public String nameForPopupMenuItem() {
        return Inter.getLocText("Plugin-ChartF_LineStyle");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public void setDefault() {
        lineTypePane.populate(new VanChartAttrLine());
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof VanChartAttrLine) {
            attrLine = (VanChartAttrLine) condition;
            lineTypePane.populate(attrLine);
        }
    }

    public DataSeriesCondition update() {
        return lineTypePane.update();
    }
}
