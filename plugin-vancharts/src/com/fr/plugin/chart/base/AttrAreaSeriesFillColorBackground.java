package com.fr.plugin.chart.base;

import com.fr.base.background.ColorBackground;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.general.ComparatorUtils;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.stable.StableUtils;
import com.fr.stable.web.Repository;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.awt.*;

/**
 * ���ͼ��ϵ�е������ɫ
 */
public class AttrAreaSeriesFillColorBackground extends DataSeriesCondition {

    public static final String XML_TAG = "AttrAreaSeriesFillColorBackground";
    private static final long serialVersionUID = -2908359581313365154L;

    private ColorBackground colorBackground;//ѡ��ϵ��ɫ��ʱ��colorΪnull
    private double alpha = 0.15;

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setColorBackground(ColorBackground colorBackground) {
        this.colorBackground = colorBackground;
    }

    public ColorBackground getColorBackground() {
        return colorBackground;
    }

    public void readXML(XMLableReader reader) {
        if(reader.isChildNode()) {
            String tagName = reader.getTagName();
            if (tagName.equals("Attr")) {
                if(reader.getAttrAsColor("color", null) != null){
                    this.setColorBackground(ColorBackground.getInstance(reader.getAttrAsColor("color", null)));
                }
                alpha = reader.getAttrAsDouble("alpha", 1);
            }
        }

    }

    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);

        writer.startTAG("Attr")
                .attr("alpha", alpha);
        if(colorBackground != null && colorBackground.getColor() != null){
            writer.attr("color", colorBackground.getColor().getRGB());
        }
        writer.end();

        writer.end();
    }

    public boolean equals(Object ob) {
        return ob instanceof AttrAreaSeriesFillColorBackground
                && ComparatorUtils.equals(((AttrAreaSeriesFillColorBackground)ob).getColorBackground(), this.getColorBackground())
                && ComparatorUtils.equals(((AttrAreaSeriesFillColorBackground)ob).getAlpha(), this.getAlpha())
                ;
    }

    /**
     * תΪjson����
     *  @param repo ����
     * @return ����json
     * @throws com.fr.json.JSONException �״�
     */
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        JSONObject js = new JSONObject();
        addToJSONObject(js, null);
        return js;
    }

    /**
     * ����json
     * @param js json����
     * @param seriesColor ϵ�е�������ɫ�����ǵ�������ʾ����
     * @throws JSONException �״�
     */
    public void addToJSONObject(JSONObject js, Color seriesColor) throws JSONException {
        if(colorBackground != null){
            js.put("fillColor", StableUtils.javaColorToCSSColor(colorBackground.getColor()));
        } else if(seriesColor != null){
            js.put("fillColor", StableUtils.javaColorToCSSColor(seriesColor));
        } else {
            js.put("fillColor", true);
        }
        js.put("fillColorOpacity", getAlpha());
    }

    public String getConditionType() {
        return XML_TAG;
    }
}
