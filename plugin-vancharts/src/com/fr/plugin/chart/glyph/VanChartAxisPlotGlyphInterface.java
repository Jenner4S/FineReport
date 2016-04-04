package com.fr.plugin.chart.glyph;

import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.stable.web.Repository;

/**
 * Created by Mitisky on 15/12/9.
 */
public interface VanChartAxisPlotGlyphInterface {
    /**
     * X坐标轴写入js
     * @param js json对象
     * @param repo 请求
     * @throws com.fr.json.JSONException 抛错
     */
    public void addXAxisJSON(JSONObject js, Repository repo) throws JSONException;
    /**
     * Y坐标轴写入js
     * @param js json对象
     * @param repo 请求
     * @throws JSONException 抛错
     */
    public void addYAxisJSON(JSONObject js, Repository repo) throws JSONException;
}
