package com.fr.solution.plugin.chart.echarts.base;

import com.fr.chart.fun.impl.AbstractIndependentChartProvider;

/**
 * Created by richie on 16/1/29.
 */
public abstract class AbstractIndependentEChartsProvider extends AbstractIndependentChartProvider {

    @Override
    public String[] getRequiredJS() {
        return new String[]{
                "/com/fr/solution/plugin/chart/echarts/web/echarts.bridge.js"
        };
    }
}
