package com.fr.plugin.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrAreaSeriesFillColorBackground;
import com.fr.plugin.chart.designer.component.VanChartAreaSeriesFillColorPane;

import javax.swing.*;
import java.awt.*;

/**
 * 面积图，填充色
 */
public class VanChartAreaFillColorConditionPane  extends ConditionAttrSingleConditionPane<DataSeriesCondition> {
    private static final long serialVersionUID = -4148284851967140012L;
    protected UILabel nameLabel;
    private VanChartAreaSeriesFillColorPane fillColorBackground;

    public VanChartAreaFillColorConditionPane(ConditionAttributesPane conditionAttributesPane) {
        this(conditionAttributesPane, true);
    }

    public VanChartAreaFillColorConditionPane(ConditionAttributesPane conditionAttributesPane, boolean isRemove) {
        super(conditionAttributesPane, isRemove);
        nameLabel = new UILabel(Inter.getLocText("Plugin-ChartF_FillColor"));
        fillColorBackground = new VanChartAreaSeriesFillColorPane();

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

        pane.add(fillColorBackground);

        this.add(pane);

    }

    /**
     * 条件属性item的名称
     * @return item的名称
     */
    public String nameForPopupMenuItem() {
        return Inter.getLocText("Plugin-ChartF_FillColor");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public void setDefault() {
        //下面这句话是给各组件一个默认值
        fillColorBackground.populate(new AttrAreaSeriesFillColorBackground());
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof AttrAreaSeriesFillColorBackground) {
            fillColorBackground.populate((AttrAreaSeriesFillColorBackground) condition);
        }
    }

    public DataSeriesCondition update() {
        return fillColorBackground.update();
    }
}

