package com.fr.plugin.chart.attr;

import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.base.AttrAreaSeriesFillColorBackground;
import com.fr.stable.StableUtils;
import com.fr.stable.web.Repository;

import java.awt.*;

/**
 * Created by Mitisky on 15/11/11.
 */
public class AttrBand {
    private boolean hasMinEval;
    private boolean hasMaxEval;
    //这两个之间没有逻辑上的大小关系。
    private double minEval;//小于条件的参照值
    private double maxEval;//大于条件的参照值
    private Color color;
    private AttrAreaSeriesFillColorBackground fillColorBackground;

    public AttrBand(boolean hasMinEval, boolean hasMaxEval, double minEval, double maxEval){
        this.hasMinEval = hasMinEval;
        this.hasMaxEval = hasMaxEval;
        this.maxEval = maxEval;
        this.minEval = minEval;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setFillColorBackground(AttrAreaSeriesFillColorBackground fillColorBackground) {
        this.fillColorBackground = fillColorBackground;
    }

    public AttrAreaSeriesFillColorBackground getFillColorBackground() {
        return fillColorBackground;
    }

    public void setMaxEval(double maxEval) {
        if(hasMaxEval){
            return;//条件属性设置了
        }
        this.maxEval = maxEval;//没设置的话取所有数据的最大值，在值轴初始化最大最小值时取
    }

    public void setMinEval(double minEval) {
        if(hasMinEval){
            return;
        }
        this.minEval = minEval;
    }

    public double getMinEval() {
        return minEval;
    }

    public double getMaxEval() {
        return maxEval;
    }

    public Color getColor() {
        return color;
    }

    /**
     * 转为json数据
     *  @param repo 请求
     * @param realColor 系列的最终颜色，考虑到条件显示设置
     * @return 返回json
     * @throws com.fr.json.JSONException 抛错
     */
    public JSONObject toJSONObject(Repository repo, Color realColor) throws JSONException {
        JSONObject js = new JSONObject();
        js.put("from", maxEval);
        js.put("to", minEval);
        if(color != null){
            js.put("color", StableUtils.javaColorToCSSColor(color));
        }
        if(fillColorBackground != null){
            fillColorBackground.addToJSONObject(js, realColor);
        }
        js.put("axis", "y");
        return js;
    }
}
