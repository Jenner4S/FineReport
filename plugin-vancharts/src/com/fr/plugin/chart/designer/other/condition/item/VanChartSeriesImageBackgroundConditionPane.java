package com.fr.plugin.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.backgroundpane.ImageBackgroundPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrSeriesImageBackground;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mitisky on 15/10/20.
 */
public class VanChartSeriesImageBackgroundConditionPane extends ConditionAttrSingleConditionPane<DataSeriesCondition> {

    private static final long serialVersionUID = 1804818835947067586L;
    protected UILabel nameLabel;
    private ImageBackgroundPane imageBackgroundPane;
    private AttrSeriesImageBackground attrBackground = new AttrSeriesImageBackground();

    public VanChartSeriesImageBackgroundConditionPane(ConditionAttributesPane conditionAttributesPane) {
        this(conditionAttributesPane, true);
    }

    public VanChartSeriesImageBackgroundConditionPane(ConditionAttributesPane conditionAttributesPane, boolean isRemove) {
        super(conditionAttributesPane, isRemove);
        nameLabel = new UILabel(Inter.getLocText("Plugin-ChartF_FilledWithImage"));
        imageBackgroundPane = new ImageBackgroundPane(false);

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

        pane.add(imageBackgroundPane);

        this.add(pane);

    }

    /**
     * 条件属性item的名称
     * @return item的名称
     */
    public String nameForPopupMenuItem() {
        return Inter.getLocText("Plugin-ChartF_FilledWithImage");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof AttrSeriesImageBackground) {
            attrBackground = (AttrSeriesImageBackground) condition;
            this.imageBackgroundPane.populateBean(attrBackground.getSeriesBackground());
        }
    }

    public DataSeriesCondition update() {
        com.fr.general.Background imageBackground = imageBackgroundPane.updateBean();
        attrBackground.setSeriesBackground(imageBackground);
        return attrBackground;
    }
}
