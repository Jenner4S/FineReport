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
    //������֮��û���߼��ϵĴ�С��ϵ��
    private double minEval;//С�������Ĳ���ֵ
    private double maxEval;//���������Ĳ���ֵ
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
            return;//��������������
        }
        this.maxEval = maxEval;//û���õĻ�ȡ�������ݵ����ֵ����ֵ���ʼ�������Сֵʱȡ
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
     * תΪjson����
     *  @param repo ����
     * @param realColor ϵ�е�������ɫ�����ǵ�������ʾ����
     * @return ����json
     * @throws com.fr.json.JSONException �״�
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
