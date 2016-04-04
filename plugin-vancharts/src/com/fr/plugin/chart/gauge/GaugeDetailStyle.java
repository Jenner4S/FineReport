package com.fr.plugin.chart.gauge;

import com.fr.chart.base.AreaColor;
import com.fr.chart.chartglyph.MapHotAreaColor;
import com.fr.general.ComparatorUtils;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.script.Calculator;
import com.fr.stable.StableUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLable;
import com.fr.stable.xml.XMLableReader;

import java.awt.*;
import java.util.List;

/**
 * Created by Mitisky on 15/12/3.
 */
public class GaugeDetailStyle implements XMLable {
    private static final long serialVersionUID = -1375490941381976058L;
    public static final String XML_TAG = "GaugeDetailStyle";

    private static final Color HINGE = new Color(101,107,109);
    private static final Color HINGE_BACKGROUND = new Color(220,242,249);
    private static final Color NEEDLE = new Color(229,113,90);
    private static final Color PANE_BACKGROUND = new Color(252, 252, 252);
    private static final Color RING_PANE_BACKGROUND_COLOR = new Color(238,238,238);
    private static final Color RING_INNER_PANE_BACKGROUND = new Color(244,244,244);
    private static final Color SLOT_NEEDLE_COLOR = Color.WHITE;
    private static final Color SLOT_BACKGROUND = new Color(238,238,238);
    private static final Color THERMOMETER_NEEDLE_COLOR = Color.WHITE;
    private static final Color THERMOMETER_SLOT_BACKGROUND_COLOR = new Color(229,229,229);

    private boolean horizontalLayout = true;//横向布局
    private Color hingeColor;//枢纽颜色
    private Color hingeBackgroundColor;//枢纽背景颜色
    private Color needleColor;//指针颜色
    private Color paneBackgroundColor; //底盘背景颜色
    private Color slotBackgroundColor;//刻度槽颜色
    private boolean antiClockWise = true;//逆时针旋转
    private Color innerPaneBackgroundColor;//内底盘背景颜色

    private MapHotAreaColor hotAreaColor = new MapHotAreaColor();

    public void setAntiClockWise(boolean antiClockWise) {
        this.antiClockWise = antiClockWise;
    }

    /**
     * 是否是逆时针画
     * @return 是否是逆时针画
     */
    public boolean isAntiClockWise() {
        return antiClockWise;
    }

    public void setHingeBackgroundColor(Color hingeBackgroundColor) {
        this.hingeBackgroundColor = hingeBackgroundColor;
    }

    public Color getHingeBackgroundColor() {
        return hingeBackgroundColor;
    }

    public void setHingeColor(Color hingeColor) {
        this.hingeColor = hingeColor;
    }

    public Color getHingeColor() {
        return hingeColor;
    }

    public void setHorizontalLayout(boolean horizontalLayout) {
        this.horizontalLayout = horizontalLayout;
    }

    /**
     * 是否是横向画
     * @return 是否是横向画
     */
    public boolean isHorizontalLayout() {
        return horizontalLayout;
    }

    public void setHotAreaColor(MapHotAreaColor hotAreaColor) {
        this.hotAreaColor = hotAreaColor;
    }

    public MapHotAreaColor getHotAreaColor() {
        return hotAreaColor;
    }

    public void setInnerPaneBackgroundColor(Color innerPaneBackgroundColor) {
        this.innerPaneBackgroundColor = innerPaneBackgroundColor;
    }

    public Color getInnerPaneBackgroundColor() {
        return innerPaneBackgroundColor;
    }

    public void setNeedleColor(Color needleColor) {
        this.needleColor = needleColor;
    }

    public Color getNeedleColor() {
        return needleColor;
    }

    public void setPaneBackgroundColor(Color paneBackgroundColor) {
        this.paneBackgroundColor = paneBackgroundColor;
    }

    public Color getPaneBackgroundColor() {
        return paneBackgroundColor;
    }

    public void setSlotBackgroundColor(Color slotBackgroundColor) {
        this.slotBackgroundColor = slotBackgroundColor;
    }

    public Color getSlotBackgroundColor() {
        return slotBackgroundColor;
    }

    public GaugeDetailStyle(GaugeStyle gaugeStyle){
        switch (gaugeStyle){
            case THERMOMETER:
                this.needleColor = THERMOMETER_NEEDLE_COLOR;
                this.slotBackgroundColor = THERMOMETER_SLOT_BACKGROUND_COLOR;
                break;
            case SLOT:
                this.needleColor = SLOT_NEEDLE_COLOR;
                this.slotBackgroundColor = SLOT_BACKGROUND;
                break;
            case RING:
                this.paneBackgroundColor = RING_PANE_BACKGROUND_COLOR;
                this.innerPaneBackgroundColor = RING_INNER_PANE_BACKGROUND;
                break;
            default:
                this.hingeColor = HINGE;
                this.hingeBackgroundColor = HINGE_BACKGROUND;
                this.needleColor = NEEDLE;
                this.paneBackgroundColor = PANE_BACKGROUND;
                break;
        }
    }

    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);

        writer.startTAG("GaugeDetailStyleAttr")
                .attr("horizontalLayout", horizontalLayout);
        if(hingeColor != null){
            writer.attr("hingeColor", hingeColor.getRGB());
        }
        if(hingeBackgroundColor != null){
            writer.attr("hingeBackgroundColor", hingeBackgroundColor.getRGB());
        }
        if(needleColor != null){
            writer.attr("needleColor", needleColor.getRGB());
        }
        if(paneBackgroundColor != null){
            writer.attr("paneBackgroundColor", paneBackgroundColor.getRGB());
        }
        if(slotBackgroundColor != null){
            writer.attr("slotBackgroundColor", slotBackgroundColor.getRGB());
        }
        if(innerPaneBackgroundColor != null){
            writer.attr("innerPaneBackgroundColor", innerPaneBackgroundColor.getRGB());
        }
        writer.attr("antiClockWise", antiClockWise);
        writer.end();

        if (hotAreaColor != null) {
            hotAreaColor.writeXML(writer);
        }
      
        writer.end();
    }

   

    public void readXML(XMLableReader reader) {
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();
            if ("GaugeDetailStyleAttr".equals(tagName)) {
                horizontalLayout = reader.getAttrAsBoolean("horizontalLayout", true);
                hingeColor = reader.getAttrAsColor("hingeColor", null);
                hingeBackgroundColor = reader.getAttrAsColor("hingeBackgroundColor", null);
                needleColor = reader.getAttrAsColor("needleColor", null);
                paneBackgroundColor = reader.getAttrAsColor("paneBackgroundColor", null);
                slotBackgroundColor = reader.getAttrAsColor("slotBackgroundColor", null);
                antiClockWise = reader.getAttrAsBoolean("antiClockWise", true);
                innerPaneBackgroundColor = reader.getAttrAsColor("innerPaneBackgroundColor", null);
            }else if (ComparatorUtils.equals(tagName, MapHotAreaColor.XML_TAG)) {
                reader.readXMLObject(new XMLReadable() {
                    public void readXML(XMLableReader reader) {
                        GaugeDetailStyle.this.hotAreaColor = (MapHotAreaColor) reader.readXMLObject(new MapHotAreaColor());
                    }
                });
            }
        }
    }

    public Object clone() throws CloneNotSupportedException {
        GaugeDetailStyle cloned = (GaugeDetailStyle) super.clone();
        cloned.setAntiClockWise(this.isAntiClockWise());
        cloned.setHingeBackgroundColor(this.getHingeBackgroundColor());
        cloned.setHingeColor(this.getHingeColor());
        cloned.setHorizontalLayout(this.isHorizontalLayout());
        cloned.setHotAreaColor((MapHotAreaColor)this.getHotAreaColor().clone());
        cloned.setInnerPaneBackgroundColor(this.getInnerPaneBackgroundColor());
        cloned.setNeedleColor(this.getNeedleColor());
        cloned.setSlotBackgroundColor(this.getSlotBackgroundColor());
        cloned.setPaneBackgroundColor(this.getPaneBackgroundColor());
        return cloned;
    }


    public boolean equals(Object ob) {
        return ob instanceof GaugeDetailStyle
                && ComparatorUtils.equals(((GaugeDetailStyle)ob).isAntiClockWise(), this.isAntiClockWise())
                && ComparatorUtils.equals(((GaugeDetailStyle)ob).getHingeBackgroundColor(), this.getHingeBackgroundColor())
                && ComparatorUtils.equals(((GaugeDetailStyle)ob).getHingeColor(), this.getHingeColor())
                && ComparatorUtils.equals(((GaugeDetailStyle)ob).isHorizontalLayout(), this.isHorizontalLayout())
                && ComparatorUtils.equals(((GaugeDetailStyle)ob).getHotAreaColor(), this.getHotAreaColor())
                && ComparatorUtils.equals(((GaugeDetailStyle)ob).getInnerPaneBackgroundColor(), this.getInnerPaneBackgroundColor())
                && ComparatorUtils.equals(((GaugeDetailStyle)ob).getNeedleColor(), this.getNeedleColor())
                && ComparatorUtils.equals(((GaugeDetailStyle)ob).getSlotBackgroundColor(), this.getSlotBackgroundColor())
                && ComparatorUtils.equals(((GaugeDetailStyle)ob).getPaneBackgroundColor(), this.getPaneBackgroundColor())
                ;
    }

    /**
     * SE中处理公式
     * @param calculator  计算器
     */
    public void dealFormula(Calculator calculator) {
        this.hotAreaColor.dealFormula(calculator);
    }

    public JSONArray getBandsArray() throws JSONException{
        JSONArray bands = new JSONArray();
        List colorList = hotAreaColor.getAreaColorList();
        for(int i = 0, size = colorList.size(); i < size; i++) {
            AreaColor areaColor = (AreaColor)colorList.get(i);
            JSONObject band = new JSONObject();
            band.put("from", areaColor.getMinNum());
            band.put("to", areaColor.getMaxNum());
            band.put("color", StableUtils.javaColor2JSColorWithAlpha(areaColor.getAreaColor()));
            bands.put(band);
        }
        return bands;
    }

    public void addDetailStyleJSON(JSONObject js, GaugeStyle gaugeStyle) throws JSONException {
        switch (gaugeStyle) {
            case RING:
                addClockWiseJSON(js);
                addPaneBackgroundColorJSON(js);
                addInnerPaneBackgroundColorJSON(js);
                break;
            case SLOT:
                addNeedleColorJSON(js);
                addSlotBackgroundColorJSON(js);
                break;
            case THERMOMETER:
                addNeedleColorJSON(js);
                addSlotBackgroundColorJSON(js);
                //写死，试管布局方向与总体布局相反
                js.put("thermometerLayout", isHorizontalLayout() ? "vertical" : "horizontal");
                break;
            default:
                addHingeColorJSON(js);
                addHingeBackgroundColorJSON(js);
                addNeedleColorJSON(js);
                addPaneBackgroundColorJSON(js);
                break;
        }
    }

    private void addClockWiseJSON(JSONObject js) throws JSONException {
        js.put("clockwise", !antiClockWise);
    }

    private void addPaneBackgroundColorJSON(JSONObject js) throws JSONException {
        js.put("paneBackgroundColor", VanChartAttrHelper.getStringColor(paneBackgroundColor));
    }

    private void addInnerPaneBackgroundColorJSON(JSONObject js) throws JSONException {
        js.put("innerPaneBackgroundColor", VanChartAttrHelper.getStringColor(innerPaneBackgroundColor));
    }

    private void addNeedleColorJSON(JSONObject js) throws JSONException {
        js.put("needle", VanChartAttrHelper.getStringColor(needleColor));
    }

    private void addSlotBackgroundColorJSON(JSONObject js) throws JSONException {
        js.put("slotBackgroundColor", VanChartAttrHelper.getStringColor(slotBackgroundColor));
    }

    private void addHingeColorJSON(JSONObject js) throws JSONException {
        js.put("hinge", VanChartAttrHelper.getStringColor(hingeColor));
    }

    private void addHingeBackgroundColorJSON(JSONObject js) throws JSONException {
        js.put("hingeBackgroundColor", VanChartAttrHelper.getStringColor(hingeBackgroundColor));
    }
}
