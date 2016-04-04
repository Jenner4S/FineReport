package com.fr.plugin.chart.base;

import com.fr.base.BaseXMLUtils;
import com.fr.base.background.ColorBackground;
import com.fr.base.background.ImageBackground;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartglyph.Marker;
import com.fr.general.ComparatorUtils;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.MarkerType;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.stable.StableUtils;
import com.fr.stable.web.Repository;
import com.fr.stable.xml.XMLConstants;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

/**
 * Created by Mitisky on 15/11/9.
 */
public class VanChartAttrMarker extends DataSeriesCondition {

    public static final String XML_TAG = "VanAttrMarker";

    private boolean isCommon = true;

    private MarkerType markerType = MarkerType.MARKER_NULL;
    private ColorBackground colorBackground;
    private double radius = 4.5;

    private ImageBackground imageBackground;
    private double width = 30;
    private double height = 30;

    public void setColorBackground(ColorBackground colorBackground) {
        this.colorBackground = colorBackground;
    }

    public void setCommon(boolean isCommon) {
        this.isCommon = isCommon;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setImageBackground(ImageBackground imageBackground) {
        this.imageBackground = imageBackground;
    }

    public void setMarkerType(MarkerType markerType) {
        this.markerType = markerType;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public ImageBackground getImageBackground() {
        return imageBackground;
    }

    public double getWidth() {
        return width;
    }

    public double getRadius() {
        return radius;
    }

    public double getHeight() {
        return height;
    }

    public ColorBackground getColorBackground() {
        return colorBackground;
    }

    /**
     * 是否是常规
     * @return 是否是常规
     */
    public boolean isCommon() {
        return isCommon;
    }

    public MarkerType getMarkerType() {
        return markerType;
    }

    public void readXML(XMLableReader reader) {
        if(reader.isChildNode()) {
            String tagName = reader.getTagName();
            if (tagName.equals("Attr")) {
                if(reader.getAttrAsColor("color", null) != null){
                    this.setColorBackground(ColorBackground.getInstance(reader.getAttrAsColor("color", null)));
                }
                this.setMarkerType(MarkerType.parse(reader.getAttrAsString("markerType", Marker.NULL_M)));
                this.setRadius(reader.getAttrAsDouble("radius", 4.5));
                this.setCommon(reader.getAttrAsBoolean("isCommon", true));
                this.setWidth(reader.getAttrAsDouble("width", 4.5));
                this.setHeight(reader.getAttrAsDouble("height", 4.5));
            } else if(tagName.equals(XMLConstants.Background_TAG)) {
                this.setImageBackground((ImageBackground) BaseXMLUtils.readBackground(reader));
            }
        }

    }

    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);

        writer.startTAG("Attr")
                .attr("isCommon", isCommon)
                .attr("markerType", markerType.getType())
                .attr("radius", radius)
                .attr("width", width)
                .attr("height", height);
        if(colorBackground != null && colorBackground.getColor() != null){
            writer.attr("color", colorBackground.getColor().getRGB());
        }
        writer.end();

        BaseXMLUtils.writeBackground(writer, this.imageBackground);

        writer.end();
    }

    public boolean equals(Object ob) {
        return ob instanceof VanChartAttrMarker
                && ComparatorUtils.equals(((VanChartAttrMarker) ob).isCommon(), this.isCommon())
                && ComparatorUtils.equals(((VanChartAttrMarker) ob).getMarkerType(), this.getMarkerType())
                && ComparatorUtils.equals(((VanChartAttrMarker) ob).getColorBackground(), this.getColorBackground())
                && ComparatorUtils.equals(((VanChartAttrMarker) ob).getRadius(), this.getRadius())
                && ComparatorUtils.equals(((VanChartAttrMarker) ob).getImageBackground(), this.getImageBackground())
                && ComparatorUtils.equals(((VanChartAttrMarker) ob).getHeight(), this.getHeight())
                && ComparatorUtils.equals(((VanChartAttrMarker) ob).getWidth(), this.getWidth())
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
        js.put("enabled", true);
        if(isCommon()){
            js.put("radius", radius);
            if(colorBackground != null){
                js.put("fillColor", StableUtils.javaColorToCSSColor(colorBackground.getColor()));
            }
            js.put("symbol", VanChartAttrHelper.getMarkerType(markerType));
        } else if(imageBackground != null){
            js.put("symbol", VanChartAttrHelper.getImageBackground(imageBackground, repo));
            js.put("width", width);
            js.put("height", height);
        } else {
            js.put("enabled", false);
        }

        return js;
    }

    public String getConditionType() {
        return XML_TAG;
    }
}
