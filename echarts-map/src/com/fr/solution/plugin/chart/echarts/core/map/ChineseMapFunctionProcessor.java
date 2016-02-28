package com.fr.solution.plugin.chart.echarts.core.map;

import com.fr.general.Inter;
import com.fr.stable.fun.FunctionHelper;
import com.fr.stable.fun.FunctionProcessor;
import com.fr.stable.fun.impl.AbstractFunctionProcessor;

/**
 * Created by richie on 16/1/29.
 */
public class ChineseMapFunctionProcessor extends AbstractFunctionProcessor {

    public static FunctionProcessor MAP = new ChineseMapFunctionProcessor();

    @Override
    public int getId() {
        return FunctionHelper.generateFunctionID("com.fr.solution.plugin.chart.echarts.map");
    }

    @Override
    public String toString() {
        return Inter.getLocText("Plugin-ECharts_Chinese_Map");
    }
}
