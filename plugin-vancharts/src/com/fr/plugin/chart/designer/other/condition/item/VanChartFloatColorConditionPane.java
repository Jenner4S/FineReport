package com.fr.plugin.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrFloatColor;

/**
 * �������� ������ɫ
 */
public class VanChartFloatColorConditionPane extends ConditionAttrSingleConditionPane<DataSeriesCondition>{

    private static final long serialVersionUID = 1804818835947067586L;

    protected UILabel nameLabel;
    private ColorSelectBox colorSelectionPane;
    private AttrFloatColor attrColor = new AttrFloatColor();

    public VanChartFloatColorConditionPane(ConditionAttributesPane conditionAttributesPane) {
        this(conditionAttributesPane, true);
    }

    public VanChartFloatColorConditionPane(ConditionAttributesPane conditionAttributesPane, boolean isRemove) {
        super(conditionAttributesPane, isRemove);
        nameLabel = new UILabel(Inter.getLocText("plugin-ChartF_FloatColor"));
        colorSelectionPane = new ColorSelectBox(80);

        if (isRemove) {
            this.add(nameLabel);
        }
        this.add(colorSelectionPane);

    }

    /**
     *  ��Ŀ����
     * @return ����
     */
    @Override
    public String nameForPopupMenuItem() {
        return Inter.getLocText("plugin-ChartF_FloatColor");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof AttrFloatColor) {
            attrColor = (AttrFloatColor) condition;
            this.colorSelectionPane.setSelectObject(this.attrColor.getSeriesColor());
        }
    }

    public DataSeriesCondition update() {
        attrColor.setSeriesColor(this.colorSelectionPane.getSelectObject());
        return attrColor;
    }
}
