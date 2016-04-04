package com.fr.plugin.chart.base;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.base.LineStyleInfo;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.web.Repository;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLableReader;

/**
 * Created by Mitisky on 15/10/19.
 */
public class VanChartAttrTrendLine extends DataSeriesCondition {
    public static final String XML_TAG = "AttrTrendLine";
    private LineStyleInfo lineStyleInfo = new LineStyleInfo();

    private String trendLineName;

    public void setTrendLineName(String trendLineName) {
        this.trendLineName = trendLineName;
    }

    public String getTrendLineName() {
        return trendLineName;
    }

    public void setLineStyleInfo(LineStyleInfo lineStyleInfo) {
        this.lineStyleInfo = lineStyleInfo;
    }

    public LineStyleInfo getLineStyleInfo() {
        return lineStyleInfo;
    }


    public void readXML(XMLableReader reader) {
        if(reader.isChildNode()) {
            String tagName = reader.getTagName();
            if(tagName.equals(LineStyleInfo.XML_TAG)) {
                reader.readXMLObject(new XMLReadable() {
                    public void readXML(XMLableReader reader) {
                        String tagName = reader.getTagName();
                        if(tagName.equals(LineStyleInfo.XML_TAG)) {
                            VanChartAttrTrendLine.this.setLineStyleInfo((LineStyleInfo) reader.readXMLObject(new LineStyleInfo()));
                        }
                    }
                });
            } else if (tagName.equals("Attr")) {
                this.setTrendLineName(reader.getAttrAsString("trendLineName", Inter.getLocText("Chart-Trend_Line")));
            }
        }

    }

    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG("TrendLine");

        writer.startTAG("Attr")
                .attr("trendLineName", trendLineName)
                .end();

        this.getLineStyleInfo().writeXML(writer);

        writer.end();
    }

    public boolean equals(Object ob) {
        return ob instanceof VanChartAttrTrendLine
                && ComparatorUtils.equals(((VanChartAttrTrendLine) ob).getTrendLineName(), this.getTrendLineName())
                && ComparatorUtils.equals(((VanChartAttrTrendLine) ob).getLineStyleInfo(), this.getLineStyleInfo())
                ;
    }

    /**
     * 转为json数据
     *  @param repo 请求
     * @return 返回json
     * @throws com.fr.json.JSONException 抛错
     */
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        JSONObject js = new JSONObject();
        js.put("type", "linear");
        if(StringUtils.isNotEmpty(trendLineName)){
            js.put("name", trendLineName);
        }
        if(lineStyleInfo.getAttrLineColor().getSeriesColor() != null){
            js.put("color", StableUtils.javaColorToCSSColor(lineStyleInfo.getAttrLineColor().getSeriesColor()));
        }
        VanChartAttrHelper.addDashLineStyleJSON(js, lineStyleInfo.getAttrLineStyle().getLineStyle());

        return js;
    }

    public String getConditionType() {
        return XML_TAG;
    }

}
