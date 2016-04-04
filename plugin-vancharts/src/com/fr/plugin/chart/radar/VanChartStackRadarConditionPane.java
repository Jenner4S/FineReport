package com.fr.plugin.chart.radar;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrBorder;
import com.fr.design.chart.series.SeriesCondition.LabelAlphaPane;
import com.fr.design.chart.series.SeriesCondition.LabelBorderPane;

/**
 * Created by Mitisky on 15/12/30.
 */
public class VanChartStackRadarConditionPane extends VanChartRadarConditionPane {

    private static final long serialVersionUID = -2477842288418899346L;

    protected void addRadarAction() {
        classPaneMap.put(AttrBorder.class, new LabelBorderPane(this));
        classPaneMap.put(AttrAlpha.class, new LabelAlphaPane(this));
    }
}
