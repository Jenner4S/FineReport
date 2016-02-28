package com.fr.plugin.chart.data;

import com.fr.chart.chartdata.NormalChartData;
import com.fr.chart.chartdata.NormalReportDataDefinition;

/**
 * Created by Mitisky on 16/1/19.
 *
 */
public class VanChartNormalReportDataDefinition extends NormalReportDataDefinition {

    private static final long serialVersionUID = -645098535257304519L;

    protected NormalChartData getNormalDataObject() {
        return new VanChartNormalChartData();
    }
}
