package com.fr.plugin.chart.radar;

import com.fr.plugin.chart.line.VanChartLineDataPoint;

/**
 * Created by Mitisky on 16/1/7.
 */
public class VanChartRadarDataPoint extends VanChartLineDataPoint {
    private static final long serialVersionUID = 5757737246599950108L;

    private boolean columnType;

    public VanChartRadarDataPoint(boolean columnType){
        super();
        this.columnType = columnType;
    }

    /**
     * 是否是柱子外(折线图部分内外，自动的样式都和系列色一样，即柱子外自动颜色)
     * @return 是否是柱子外
     */
    public boolean isOutside() {
        return !columnType;
    }

}
