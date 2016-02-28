package com.fr.plugin.chart.attr;


import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.chart.chartattr.Title;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.plugin.chart.glyph.VanChartTitleGlyph;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.awt.*;

/**
 * Created by Mitisky on 15/8/17.
 */
public class VanChartTitle extends Title {

    private static final long serialVersionUID = -618243870663433046L;
    public static final String XML_TAG = "Title4VanChart";

    private boolean useHtml = false;

    private boolean floating = false;
    private double floatPercentX;
    private double floatPercentY;

    private boolean limitSize = false;
    private double maxHeight = 15;

    public VanChartTitle(){
        FRFont frFont = FRFont.getInstance("Microsoft YaHei", Font.PLAIN, 16, new Color(51, 51, 51));
        getTextAttr().setFRFont(frFont);
        setTextObject(Inter.getLocText("FR-Chart-Title_NewTitle"));
    }

    /**
     * 构造文本内容为textObject的标题对象
     * @param textObject 标题文本内容
     */
    public VanChartTitle(Object textObject) {
        super(textObject);
    }

    /**
     * 设置是否使用html解析文本
     * @param useHtml 是否使用html解析文本
     */
    public void setUseHtml(boolean useHtml) {
        this.useHtml = useHtml;
    }

    /**
     * 返回是否使用html解析文本
     * @return 是否使用html解析文本
     */
    public boolean isUseHtml() {
        return this.useHtml;
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
     * @param x 横向位置
     */
    public void setFloatPercentX(double x) {
        this.floatPercentX = x;
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
     * @param y 纵向位置
     */
    public void setFloatPercentY(double y) {
        this.floatPercentY = y;
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
     * 将标题属性转为标题图形
     * @return 标题图形
     */
    public VanChartTitleGlyph createGlyph() {
        String text4Glyph = Utils.objectToString(getTextObject());
        if(getTextObject() instanceof Formula) {
            Formula formula = (Formula)getTextObject();
            if(formula.getResult() != null) {
                text4Glyph = Utils.objectToString(formula.getResult());
            }
        }

        VanChartTitleGlyph vanChartTitleGlyph = new VanChartTitleGlyph(text4Glyph.trim(), getTextAttr());
        vanChartTitleGlyph.setGeneralInfo(this);
        vanChartTitleGlyph.setPosition(getPosition());
        vanChartTitleGlyph.setVisible(isTitleVisble());

        vanChartTitleGlyph.setUseHtml(isUseHtml());
        vanChartTitleGlyph.setFloating(isFloating());
        vanChartTitleGlyph.setFloatPercentX(getFloatPercentX());
        vanChartTitleGlyph.setFloatPercentY(getFloatPercentY());
        vanChartTitleGlyph.setMaxHeight(getMaxHeight());
        vanChartTitleGlyph.setLimitSize(isLimitSize());

        return vanChartTitleGlyph;
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
                setUseHtml(reader.getAttrAsBoolean("useHtml", false));
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
                .attr("useHtml", isUseHtml())
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
        return ob instanceof VanChartTitle && super.equals(ob)
                && ((VanChartTitle)ob).isUseHtml() == isUseHtml()
                && ((VanChartTitle)ob).isFloating() == isFloating()
                && ((VanChartTitle)ob).getFloatPercentX() == getFloatPercentX()
                && ((VanChartTitle)ob).getFloatPercentY() == getFloatPercentY()
                && ((VanChartTitle)ob).isLimitSize() == isLimitSize()
                && ((VanChartTitle)ob).getMaxHeight() == getMaxHeight();
    }

    public Object clone() throws CloneNotSupportedException {
        VanChartTitle vanChartTitle = (VanChartTitle)super.clone();

        vanChartTitle.setUseHtml(this.isUseHtml());
        vanChartTitle.setFloating(this.isFloating());
        vanChartTitle.setFloatPercentX(this.getFloatPercentX());
        vanChartTitle.setFloatPercentY(this.getFloatPercentY());
        vanChartTitle.setLimitSize(this.isLimitSize());
        vanChartTitle.setMaxHeight(this.getMaxHeight());

        return vanChartTitle;
    }
}
