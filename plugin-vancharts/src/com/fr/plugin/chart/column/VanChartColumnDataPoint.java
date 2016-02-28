package com.fr.plugin.chart.column;

import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.base.AttrSeriesImageBackground;
import com.fr.plugin.chart.glyph.VanChartDataPoint;
import com.fr.stable.web.Repository;

/**
 * Created by Mitisky on 15/10/20.
 */
public class VanChartColumnDataPoint extends VanChartDataPoint {
    private static final long serialVersionUID = 138608473678926589L;
    private boolean isBar;
    private AttrSeriesImageBackground imageBackground;


    public void setBar(boolean isBar) {
        this.isBar = isBar;
    }

    public void setImageBackground(AttrSeriesImageBackground imageBackground) {
        this.imageBackground = imageBackground;
    }

    public AttrSeriesImageBackground getImageBackground() {
        return this.imageBackground;
    }

    /**
     * 转为json数据
     *  @param repo 请求
     * @return 返回json
     * @throws com.fr.json.JSONException 抛错
     */
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        JSONObject js = super.toJSONObject(repo);

        if(imageBackground != null){
            VanChartAttrHelper.addImageBackgroundAndWidthAndHeight(js, imageBackground, repo);
        }

        return js;
    }

    protected void addXYJSON(JSONObject js) throws JSONException {
        if(isBar){
            js.put("x", isValueIsNull() ? "-" : getValue());
            js.put("y", getCategoryName());
        } else {
            super.addXYJSON(js);
        }
    }

}
