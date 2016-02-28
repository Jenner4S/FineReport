package com.fr.plugin.chart.base;

import com.fr.base.background.ColorBackground;
import com.fr.base.background.GradientBackground;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartglyph.GeneralInfo;
import com.fr.general.Background;
import com.fr.general.ComparatorUtils;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.stable.StableUtils;
import com.fr.stable.web.Repository;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

/**
 * 数据点提示
 */
public class AttrTooltip extends DataSeriesCondition {

    public static final String XML_TAG = "AttrTooltip";
    private static final long serialVersionUID = 3276836768506510374L;
    private static final int DEFAULT_PADDING = 5;

    private boolean enable = true;

    private AttrTooltipContent content = new AttrTooltipContent();

    private boolean isCustom = false;
    private TextAttr textAttr = new TextAttr();

    private GeneralInfo generalInfo = VanChartAttrHelper.createDefaultTooltipBackground();

    //显示所有系列，堆积图用到
    private boolean isShowMutiSeries = false;
    private boolean followMouse = false;

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * 使用数据点提示
     * @return 使用数据点提示
     */
    public boolean isEnable() {
        return enable;
    }

    public void setContent(AttrTooltipContent content) {
        this.content = content;
    }

    public void setCustom(boolean isCustom) {
        this.isCustom = isCustom;
    }

    public void setTextAttr(TextAttr textAttr) {
        this.textAttr = textAttr;
    }

    public void setGeneralInfo(GeneralInfo generalInfo) {
        this.generalInfo = generalInfo;
    }

    public void setFollowMouse(boolean followMouse) {
        this.followMouse = followMouse;
    }

    public void setShowMutiSeries(boolean isShowMutiSeries) {
        this.isShowMutiSeries = isShowMutiSeries;
    }

    public TextAttr getTextAttr() {
        return textAttr;
    }

    /**
     * 自定义
     * @return 自定义
     */
    public boolean isCustom() {
        return isCustom;
    }

    public AttrTooltipContent getContent() {
        return content;
    }

    public GeneralInfo getGeneralInfo() {
        return generalInfo;
    }

    /**
     * 显示多系列
     * @return 显示多系列
     */
    public boolean isShowMutiSeries() {
        return isShowMutiSeries;
    }

    /**
     * 跟随鼠标
     * @return 跟随鼠标
     */
    public boolean isFollowMouse() {
        return followMouse;
    }

    public void readXML(XMLableReader reader) {
        if(reader.isChildNode()) {
            String tagName = reader.getTagName();
            if(tagName.equals("Attr")) {
                this.enable = reader.getAttrAsBoolean("enable", true);
                this.isCustom = reader.getAttrAsBoolean("isCustom", true);
                this.followMouse = reader.getAttrAsBoolean("followMouse",false);
                this.isShowMutiSeries = reader.getAttrAsBoolean("showMutiSeries",false);
            } else if (TextAttr.XML_TAG.equals(tagName)) {
                this.setTextAttr((TextAttr)reader.readXMLObject(new TextAttr()));
            } else if (AttrTooltipContent.XML_TAG.equals(tagName)) {
                this.setContent((AttrTooltipContent)reader.readXMLObject(new AttrTooltipContent()));
            } else if (GeneralInfo.XML_TAG.equals(tagName)) {
                this.setGeneralInfo((GeneralInfo)reader.readXMLObject(new GeneralInfo()));
            }
        }
    }

    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);

        writer.startTAG("Attr")
                .attr("enable", enable)
                .attr("followMouse", followMouse)
                .attr("showMutiSeries",isShowMutiSeries)
                .attr("isCustom", isCustom)
        ;
        writer.end();

        if (this.textAttr != null) {
            this.textAttr.writeXML(writer);
        }

        this.content.writeXML(writer);
        this.generalInfo.writeXML(writer);

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
        js.put("enabled", enable && content.hasLabelContent());
        js.put("animation", true);
        content.toJSONObject(js);
        js.put("follow", followMouse);
        js.put("shared", isShowMutiSeries);
        if(isCustom){
            js.put("style", VanChartAttrHelper.getCSSFontJSONWithFont(textAttr.getFRFont()));
        }

        Background background = generalInfo.getBackground();
        if(background != null && ComparatorUtils.equals(background.getBackgroundType(), "ColorBackground")){
            js.put("backgroundColor", VanChartAttrHelper.getRGBAColorWithColorAndAlpha(((ColorBackground) background).getColor(), generalInfo.getAlpha()));
        } else if(background != null && ComparatorUtils.equals(background.getBackgroundType(), "GradientBackground")){
            js.put("backgroundColor", VanChartAttrHelper.getGradientBackgroundJSON((GradientBackground) background, generalInfo.getAlpha()));
        }

        js.put("shadow", generalInfo.isShadow());

        if(generalInfo.getBorderColor() != null){
            js.put("borderColor", StableUtils.javaColorToCSSColor(generalInfo.getBorderColor()));
        } else {
            js.put("borderColor", VanChartAttrHelper.TRANSPARENT_COLOR);
        }
        js.put("borderWidth", VanChartAttrHelper.getAxisLineStyle(generalInfo.getBorderStyle()));
        js.put("borderRadius", generalInfo.getRoundRadius());
        js.put("padding", DEFAULT_PADDING);

        return js;
    }

    public boolean equals(Object ob) {
        return ob instanceof AttrTooltip
                && ComparatorUtils.equals(((AttrTooltip) ob).isEnable(), this.isEnable())
                && ComparatorUtils.equals(((AttrTooltip) ob).getContent(), this.getContent())
                && ComparatorUtils.equals(((AttrTooltip) ob).isFollowMouse(), this.isFollowMouse())
                && ComparatorUtils.equals(((AttrTooltip) ob).isShowMutiSeries(), this.isShowMutiSeries())
                && ComparatorUtils.equals(((AttrTooltip) ob).getGeneralInfo(), this.getGeneralInfo())
                && ComparatorUtils.equals(((AttrTooltip) ob).isCustom(), this.isCustom())
                && ComparatorUtils.equals(((AttrTooltip) ob).getTextAttr(), this.getTextAttr())
                ;
    }

    public String getConditionType() {
        return XML_TAG;
    }

}
