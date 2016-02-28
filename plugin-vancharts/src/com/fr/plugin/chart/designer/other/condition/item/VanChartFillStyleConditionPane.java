package com.fr.plugin.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.component.VanChartFillStylePane4Condition;

/**
 * 条件属性 配色
 */
public class VanChartFillStyleConditionPane extends ConditionAttrSingleConditionPane<DataSeriesCondition> {

    private static final long serialVersionUID = 2525375707601982258L;
    private UILabel nameLabel;
    private VanChartFillStylePane4Condition fillStylePane;

    public VanChartFillStyleConditionPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane, true);

        nameLabel = new UILabel(Inter.getLocText("plugin-ChartF_MatchColor"));
        fillStylePane = new VanChartFillStylePane4Condition();

        this.add(nameLabel);
        this.add(fillStylePane);
    }

    /**
     * 条件属性item的名称
     * @return item的名称
     */
    public String nameForPopupMenuItem() {
        return Inter.getLocText("plugin-ChartF_MatchColor");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public void populate(DataSeriesCondition condition) {

    }

    public DataSeriesCondition update() {
        return null;
    }

}
