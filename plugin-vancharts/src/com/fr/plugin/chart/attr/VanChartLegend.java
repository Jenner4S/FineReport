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
     * �����Ƿ��Զ�������λ��
     * @param floating �����Ƿ��Զ�������λ��
     */
    public void setFloating(boolean floating) {
        this.floating = floating;
    }

    /**
     * �����Ƿ��Զ�������λ��
     * @return �����Ƿ��Զ�������λ��
     */
    public boolean isFloating() {
        return floating;
    }

    /**
     * ��������λ�� �������ϽǺ���λ��
     * @param floatPercentX ����λ��
     */
    public void setFloatPercentX(double floatPercentX) {
        this.floatPercentX = floatPercentX;
    }

    /**
     * ��������λ�� �������ϽǺ���λ��
     * @return ����λ��
     */
    public double getFloatPercentX() {
        return floatPercentX;
    }

    /**
     * ��������λ�� �������Ͻ�����λ��
     * @param floatPercentY ����λ��
     */
    public void setFloatPercentY(double floatPercentY) {
        this.floatPercentY = floatPercentY;
    }

    /**
     * ��������λ�� �������Ͻ�����λ��
     * @return ����λ��
     */
    public double getFloatPercentY() {
        return floatPercentY;
    }

    /**
     * �����Ƿ����������С
     * @param limitSize �Ƿ����������С
     */
    public void setLimitSize(boolean limitSize) {
        this.limitSize = limitSize;
    }

    /**
     * �����Ƿ����������С
     * @return �����Ƿ����������С
     */
    public boolean isLimitSize() {
        return limitSize;
    }

    /**
     * �����������ռ��
     * @param maxHeight �������ռ��
     */
    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
    }

    /**
     * �����������ռ��
     * @return �����������ռ��
     */
    public double getMaxHeight() {
        return maxHeight;
    }


    /**
     * ���ɻ�ͼ����Glyph
     * @return ��ͼ����Glyph
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
     * ��ȡXML����
     * @param reader XML��ȡ��
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
     *  дXML
     * @param writer XML�������
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
     * �ȽϺ�Object�Ƿ����
     * @param ob �� �ڱȽϵ�Object
     * @return һ��booleanֵ
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
