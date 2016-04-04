package com.fr.plugin.chart.line;

import com.fr.base.background.ColorBackground;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.plugin.chart.glyph.VanChartDataPoint;
import com.fr.stable.web.Repository;

import java.awt.*;

/**
 * Created by Mitisky on 15/11/10.
 */
public class VanChartLineDataPoint extends VanChartDataPoint {
    private static final long serialVersionUID = -7198809115831695586L;
    private VanChartAttrMarker attrMarker;//标记点

    public void setAttrMarker(VanChartAttrMarker attrMarker) {
        this.attrMarker = attrMarker;
    }

    public VanChartAttrMarker getAttrMarker() {
        return this.attrMarker;
    }

    /**
     * 是否是柱子外(折线图部分内外，自动的样式都和系列色一样，即柱子外自动颜色)
     * @return 是否是柱子外
     */
    public boolean isOutside() {
        return true;
    }

    public Color getDataPointBackgroundColor() {
        if(color != null){//条件显示设置的系列色
            ColorBackground background = (ColorBackground)color.getSeriesBackground();
            return background.getColor();
        } else {//默认的配色
            return defaultColor;
        }
    }

    /**
     * 转为json数据
     *  @param repo 请求
     * @return 返回json
     * @throws com.fr.json.JSONException 抛错
     */
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        JSONObject js = super.toJSONObject(repo);
        if(attrMarker != null){
            js.put("marker", attrMarker.toJSONObject(repo));
        }
        return js;
    }
}
