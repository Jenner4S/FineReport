package com.fr.plugin.chart.base;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.general.ComparatorUtils;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.LineStyle;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.stable.Constants;
import com.fr.stable.web.Repository;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

/**
 * ���ߡ��ߵ���ش洢
 */
public class VanChartAttrLine extends DataSeriesCondition {
    public static final String XML_TAG = "VanAttrLine";

    private int lineWidth = Constants.LINE_MEDIUM;//2px
    private LineStyle lineStyle = LineStyle.NORMAL;
    private boolean isNullValueBreak = true;

    public void setLineStyle(LineStyle lineStyle) {
        this.lineStyle = lineStyle;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setNullValueBreak(boolean isNullValueBreak) {
        this.isNullValueBreak = isNullValueBreak;
    }

    public LineStyle getLineStyle() {
        return lineStyle;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    /**
     * �Ƿ��ֵ�Ͽ�
     * @return �Ƿ��ֵ�Ͽ�
     */
    public boolean isNullValueBreak() {
        return isNullValueBreak;
    }

    public void readXML(XMLableReader reader) {
        if(reader.isChildNode()) {
            String tagName = reader.getTagName();
            if (tagName.equals("Attr")) {
                this.setLineWidth(reader.getAttrAsInt("lineWidth", Constants.LINE_THICK));
                this.setLineStyle(LineStyle.parse(reader.getAttrAsInt("lineStyle", 0)));
                this.setNullValueBreak(reader.getAttrAsBoolean("nullValueBreak", true));
            }
        }

    }

    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);

        writer.startTAG("Attr")
                .attr("lineWidth", lineWidth)
                .attr("lineStyle", lineStyle.ordinal())
                .attr("nullValueBreak", isNullValueBreak)
                .end();

        writer.end();
    }

    public boolean equals(Object ob) {
        return ob instanceof VanChartAttrLine
                && ComparatorUtils.equals(((VanChartAttrLine) ob).getLineWidth(), this.getLineWidth())
                && ComparatorUtils.equals(((VanChartAttrLine) ob).getLineStyle(), this.getLineStyle())
                && ComparatorUtils.equals(((VanChartAttrLine) ob).isNullValueBreak(), this.isNullValueBreak())
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
        addToJSONObject(js);
        return js;
    }

    /**
     * ���뵽json
     * @param js json����
     * @throws JSONException �״�
     */
    public void addToJSONObject(JSONObject js) throws JSONException {
        switch (lineStyle){
            case CURVE:
                js.put("curve", true);
                break;
            case STEP:
                js.put("step", true);
                break;
            default:
                js.put("curve", false);
                js.put("step", false);
                break;
        }
        js.put("connectNulls", !isNullValueBreak());
        js.put("lineWidth", VanChartAttrHelper.getAxisLineStyle(getLineWidth()));
    }

    public String getConditionType() {
        return XML_TAG;
    }
}
