package com.fr.plugin.chart.glyph;

import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.stable.web.Repository;

/**
 * Created by Mitisky on 15/12/9.
 */
public interface VanChartAxisPlotGlyphInterface {
    /**
     * X������д��js
     * @param js json����
     * @param repo ����
     * @throws com.fr.json.JSONException �״�
     */
    public void addXAxisJSON(JSONObject js, Repository repo) throws JSONException;
    /**
     * Y������д��js
     * @param js json����
     * @param repo ����
     * @throws JSONException �״�
     */
    public void addYAxisJSON(JSONObject js, Repository repo) throws JSONException;
}
