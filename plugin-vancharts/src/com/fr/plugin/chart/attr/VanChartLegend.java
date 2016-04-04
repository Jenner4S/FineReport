package com.fr.plugin.chart.attr;

import com.fr.chart.chartattr.Legend;
import com.fr.general.FRFont;
import com.fr.plugin.chart.glyph.VanChartLegendGlyph;
import com.fr.stable.Constants;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.awt.*;

/**
 * Created by Mitisky on 15/8/18.
 */
public class VanChartLegend extends Legend {

    private static final long serialVersionUID = -338653804642389849L;

    public static final String XML_TAG = "Legend4VanChart";

    private boolean floating = false;
    private double floatPercentX;
    private double floatPercentY;

    private boolean limitSize = false;
    private double maxHeight = 15;

    public VanChartLegend() {
        setFRFont(FRFont.getInstance("Microsoft YaHei", Font.PLAIN, 11, new Color(102, 102, 102)));

        this.setBorderColor(new Color(204, 204, 204));
        this.setBorderStyle(Constants.LINE_NONE);
    }

    /**
     * 设置是否自定义悬浮位置
     * @param floating 设置是否自定义悬浮位置
     */
    public void setFloating(boolean floating) {
        this.floating = floating;
    }

    /**
     * 返回是否自定义悬浮位置
     * @return 返回是否自定义悬浮位置
     */
    public boolean isFloating() {
        return floating;
    }

    /**
     * 设置悬浮位置 距离左上角横向位置
     * @param floatPercentX 横向位置
     */
    public void setFloatPercentX(double floatPercentX) {
        this.floatPercentX = floatPercentX;
    }

    /**
     * 返回悬浮位置 距离左上角横向位置
     * @return 横向位置
     */
    public double getFloatPercentX() {
        return floatPercentX;
    }

    /**
     * 设置悬浮位置 距离左上角纵向位置
     * @param floatPercentY 纵向位置
     */
    public void setFloatPercentY(double floatPercentY) {
        this.floatPercentY = floatPercentY;
    }

    /**
     * 返回悬浮位置 距离左上角纵向位置
     * @return 纵向位置
     */
    public double getFloatPercentY() {
        return floatPercentY;
    }

    /**
     * 设置是否限制区域大小
     * @param limitSize 是否限制区域大小
     */
    public void setLimitSize(boolean limitSize) {
        this.limitSize = limitSize;
    }

    /**
     * 返回是否限制区域大小
     * @return 返回是否限制区域大小
     */
    public boolean isLimitSize() {
        return limitSize;
    }

    /**
     * 设置区域最大占比
     * @param maxHeight 区域最大占比
     */
    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
    }

    /**
     * 返回区域最大占比
     * @return 返回区域最大占比
     */
    public double getMaxHeight() {
        return maxHeight;
    }


    /**
     * 生成画图例的Glyph
     * @return 画图例的Glyph
     */
    public VanChartLegendGlyph createLegendGlyph() {
        VanChartLegendGlyph resultLegendGlyph = new VanChartLegendGlyph();

        resultLegendGlyph.setFont(getFRFont());
        resultLegendGlyph.setGeneralInfo(this);
        resultLegendGlyph.setPosition(getPosition());
        resultLegendGlyph.setVisible(isLegendVisible());

        resultLegendGlyph.setFloating(isFloating());
        resultLegendGlyph.setFloatPercentX(getFloatPercentX());
        resultLegendGlyph.setFloatPercentY(getFloatPercentY());
        resultLegendGlyph.setMaxHeight(getMaxHeight());
        resultLegendGlyph.setLimitSize(isLimitSize());

        return resultLegendGlyph;
    }
    
    /**
     * 读取XML属性
     * @param reader XML读取器
     */
    public void readXML(XMLableReader reader) {
        super.readXML(reader);

        if (reader.isChildNode()) {
            String name = reader.getTagName();
            if ("Attr4VanChart".equals(name)) {
                setFloating(reader.getAttrAsBoolean("floating", false));
                setFloatPercentX(reader.getAttrAsInt("x", 0));
                setFloatPercentY(reader.getAttrAsInt("y", 0));
                setLimitSize(reader.getAttrAsBoolean("limitSize", false));
                setMaxHeight(reader.getAttrAsFloat("maxHeight", 0));
            }
        }
    }

    /**
     *  写XML
     * @param writer XML属性输出
     */
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);

        super.writeXML(writer);

        writer.startTAG("Attr4VanChart")
                .attr("floating", isFloating())
                .attr("x", getFloatPercentX())
                .attr("y", getFloatPercentY())
                .attr("limitSize", isLimitSize())
                .attr("maxHeight", getMaxHeight())
                .end();

        writer.end();
    }

    /**
     * 比较和Object是否相等
     * @param ob 用 于比较的Object
     * @return 一个boolean值
     */
    public boolean equals(Object ob) {
        return ob instanceof VanChartLegend && super.equals(ob)
                && ((VanChartLegend)ob).isFloating() == isFloating()
                && ((VanChartLegend)ob).getFloatPercentX() == getFloatPercentX()
                && ((VanChartLegend)ob).getFloatPercentY() == getFloatPercentY()
                && ((VanChartLegend)ob).isLimitSize() == isLimitSize()
                && ((VanChartLegend)ob).getMaxHeight() == getMaxHeight();
    }

    public Object clone() throws CloneNotSupportedException {
        VanChartLegend newLegend = (VanChartLegend)super.clone();

        newLegend.setFloating(this.isFloating());
        newLegend.setFloatPercentX(this.getFloatPercentX());
        newLegend.setFloatPercentY(this.getFloatPercentY());
        newLegend.setLimitSize(this.isLimitSize());
        newLegend.setMaxHeight(this.getMaxHeight());

        return newLegend;
    }
}
