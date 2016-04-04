package com.fr.plugin.chart.base;

import com.fr.chart.base.ChartUtils;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.base.TextAttr;
import com.fr.general.ComparatorUtils;
import com.fr.general.xml.GeneralXMLTools;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.stable.Constants;
import com.fr.stable.web.Repository;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

/**
 * 标签
 */
public class AttrLabel extends DataSeriesCondition {
    public static final String XML_TAG = "AttrLabel";
    private static final long serialVersionUID = 3276836768506510374L;

    private boolean enable = false;

    private AttrLabelDetail attrLabelDetail = new AttrLabelDetail();//正常的标签（仪表盘的分类或者百分比标签）

    private AttrLabelDetail gaugeValueLabelDetail;//仪表盘的值标签

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * 使用标签
     * @return 使用标签
     */
    public boolean isEnable() {
        return enable;
    }

    public void setGaugeValueLabelDetail(AttrLabelDetail gaugeValueLabelDetail) {
        this.gaugeValueLabelDetail = gaugeValueLabelDetail;
    }

    public AttrLabelDetail getGaugeValueLabelDetail() {
        return gaugeValueLabelDetail;
    }

    public AttrLabelDetail getAttrLabelDetail() {
        return attrLabelDetail;
    }


    public TextAttr getTextAttr() {
        return attrLabelDetail.getTextAttr();
    }

    public int getPosition() {
        return attrLabelDetail.getPosition();
    }

    /**
     * 牵引线
     * @return 牵引线
     */
    public boolean isShowGuidLine() {
        return attrLabelDetail.isShowGuidLine();
    }

    /**
     * 自定义
     * @return 自定义
     */
    public boolean isCustom() {
        return attrLabelDetail.isCustom();
    }

    public AttrTooltipContent getContent() {
        return attrLabelDetail.getContent();
    }

    public void readXML(XMLableReader reader) {
        if(reader.isChildNode()) {
            String tagName = reader.getTagName();
            if(tagName.equals("labelAttr")) {
                this.enable = reader.getAttrAsBoolean("enable", false);
            } else if (tagName.equals("labelDetail")){
                attrLabelDetail = (AttrLabelDetail) GeneralXMLTools.readXMLable(reader);
            } else if(tagName.equals("gaugeValueLabel")){
                gaugeValueLabelDetail = (AttrLabelDetail) GeneralXMLTools.readXMLable(reader);
            } else if(tagName.equals("Attr")) { // 后面这三个是兼容之前的
                this.enable = reader.getAttrAsBoolean("enable", false);
                attrLabelDetail.setShowGuidLine(reader.getAttrAsBoolean("showLine", false));
                attrLabelDetail.setPosition(reader.getAttrAsInt("position", Constants.TOP));
                attrLabelDetail.setCustom(reader.getAttrAsBoolean("isCustom", true));
            } else if (TextAttr.XML_TAG.equals(tagName)) {
                attrLabelDetail.setTextAttr((TextAttr) reader.readXMLObject(new TextAttr()));
            } else if (AttrTooltipContent.XML_TAG.equals(tagName)){
                attrLabelDetail.setContent((AttrTooltipContent) reader.readXMLObject(new AttrTooltipContent()));
            }
        }
    }

    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);

        writer.startTAG("labelAttr")
                .attr("enable", this.enable)
                .end();

        if(attrLabelDetail != null){
            GeneralXMLTools.writeXMLable(writer, this.attrLabelDetail, "labelDetail");
        }

        if(gaugeValueLabelDetail != null){
            GeneralXMLTools.writeXMLable(writer, this.gaugeValueLabelDetail, "gaugeValueLabel");
        }

        writer.end();
    }
    /**
     * 转为json数据
     *  @param repo 请求
     * @return 返回json
     * @throws com.fr.json.JSONException 抛错
     */
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        JSONObject js = new JSONObject();
        js.put("enabled", enable && getContent().hasLabelContent());
        getContent().toJSONObject(js);
        js.put("align", ChartUtils.getPositionString(getPosition()));
        if(isCustom()){
            js.put("style", VanChartAttrHelper.getCSSFontJSONWithFont(getTextAttr().getFRFont()));
        }
        if(getPosition() == Constants.OUTSIDE){
            js.put("connectorWidth", isShowGuidLine() ? 1 : 0);
        }
        return js;
    }

    public JSONObject toGaugeValueLabelJSONObject() throws JSONException {
        JSONObject js = new JSONObject();

        js.put("enabled", enable && getGaugeValueLabelDetail().getContent().hasLabelContent());
        getGaugeValueLabelDetail().getContent().toJSONObject(js);
        js.put("align", ChartUtils.getPositionString(getGaugeValueLabelDetail().getPosition()));
        if(getGaugeValueLabelDetail().isCustom()){
            js.put("style", VanChartAttrHelper.getCSSFontJSONWithFont(getGaugeValueLabelDetail().getTextAttr().getFRFont()));
        }

        js.put("backgroundColor", VanChartAttrHelper.getStringColor(getGaugeValueLabelDetail().getBackgroundColor()));
        return js;
    }

    public boolean equals(Object ob) {
        return ob instanceof AttrLabel
                && ComparatorUtils.equals(((AttrLabel) ob).isEnable(), this.isEnable())
                && ComparatorUtils.equals(((AttrLabel) ob).getAttrLabelDetail(), this.getAttrLabelDetail())
                && ComparatorUtils.equals(((AttrLabel) ob).getGaugeValueLabelDetail(), this.getGaugeValueLabelDetail())
                ;
    }

    public String getConditionType() {
        return XML_TAG;
    }

}
