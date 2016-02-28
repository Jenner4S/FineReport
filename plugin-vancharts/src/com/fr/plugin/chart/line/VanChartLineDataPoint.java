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
    private VanChartAttrMarker attrMarker;//��ǵ�

    public void setAttrMarker(VanChartAttrMarker attrMarker) {
        this.attrMarker = attrMarker;
    }

    public VanChartAttrMarker getAttrMarker() {
        return this.attrMarker;
    }

    /**
     * �Ƿ���������(����ͼ�������⣬�Զ�����ʽ����ϵ��ɫһ�������������Զ���ɫ)
     * @return �Ƿ���������
     */
    public boolean isOutside() {
        return true;
    }

    public Color getDataPointBackgroundColor() {
        if(color != null){//������ʾ���õ�ϵ��ɫ
            ColorBackground background = (ColorBackground)color.getSeriesBackground();
            return background.getColor();
        } else {//Ĭ�ϵ���ɫ
            return defaultColor;
        }
    }

    /**
     * תΪjson����
     *  @param repo ����
     * @return ����json
     * @throws com.fr.json.JSONException �״�
     */
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        JSONObject js = super.toJSONObject(repo);
        if(attrMarker != null){
            js.put("marker", attrMarker.toJSONObject(repo));
        }
        return js;
    }
}
