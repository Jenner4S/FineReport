package com.fr.solution.plugin.chart.echarts;

import com.fr.stable.fun.impl.AbstractLocaleFinder;

/**
 * Created by richie on 16/1/29.
 */
public class EChartsLocaleFinder extends AbstractLocaleFinder {
    @Override
    public String find() {
        return "com/fr/solution/plugin/chart/echarts/locale/echarts";
    }
}
