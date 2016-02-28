package com.fr.plugin.chart.glyph.axis;

import com.fr.chart.base.TextAttr;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.stable.StableUtils;
import com.fr.stable.web.Repository;

import java.awt.*;

/**
 * 仪表盘的坐标轴。
 */
public class VanChartGaugeAxisGlyph extends VanChartValueAxisGlyph{
    private static final long serialVersionUID = -4903122510730862962L;
    private Color mainTickColor = new Color(186,186,186);
    private Color secTickColor = new Color(226,226,226);

    public Color getMainTickColor() {
        return mainTickColor;
    }

    public Color getSecTickColor() {
        return secTickColor;
    }

    public void setMainTickColor(Color mainTickColor) {
        this.mainTickColor = mainTickColor;
    }

    public void setSecTickColor(Color secTickColor) {
        this.secTickColor = secTickColor;
    }

    /**
     * 转为json数据
     *
     * @param repo 请求
     * @return 返回json
     * @throws com.fr.json.JSONException 抛错
     */
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        JSONObject js = new JSONObject();

        js.put("tickColor", StableUtils.javaColorToCSSColor(mainTickColor));
        js.put("minorTickColor", StableUtils.javaColorToCSSColor(secTickColor));

        addAxisLabelJSON(js, repo);
        addMinMaxValue(js, repo);
        addValueFormat(js, repo);

        return js;
    }

    protected void addAxisLabelJSON(JSONObject js, Repository repo) throws JSONException{
        TextAttr textAttr = getTextAttr();

        js.put("showLabel", isShowAxisLabel());
        js.put("labelStyle", VanChartAttrHelper.getCSSFontJSONWithFont(textAttr.getFRFont()));
    }
}
