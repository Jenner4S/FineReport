package com.fr.plugin.chart.gauge;

import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.glyph.VanChartDataPoint;

/**
 * Created by Mitisky on 15/12/9.
 */
public class VanChartGaugeDataPoint extends VanChartDataPoint {

    private static final long serialVersionUID = 8843096359779447187L;

    protected void addXYJSON(JSONObject js) throws JSONException {
        js.put("x", getSeriesName());
        js.put("y", isValueIsNull() ? "-" : getValue());
    }
}
