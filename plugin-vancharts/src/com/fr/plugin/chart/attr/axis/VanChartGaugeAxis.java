package com.fr.plugin.chart.attr.axis;

import com.fr.base.chart.chartdata.ChartData;
import com.fr.chart.chartglyph.AxisGlyph;
import com.fr.general.ComparatorUtils;
import com.fr.plugin.chart.glyph.axis.VanChartGaugeAxisGlyph;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.awt.*;

/**
 * Created by Mitisky on 15/12/3.
 */
public class VanChartGaugeAxis extends VanChartValueAxis {

    private Color mainTickColor = new Color(186,186,186);
    private Color secTickColor = new Color(226,226,226);

    public Color getMainTickColor() {
        return mainTickColor;
    }

    public Color getSecTickColor() {
        return secTickColor;
    }

    public void setMainTickColor(Color mainTickColor) {
        this.mainTickColor = mainTickColor;
    }

    public void setSecTickColor(Color secTickColor) {
        this.secTickColor = secTickColor;
    }

    public VanChartGaugeAxisGlyph createAxisGlyph(ChartData chartData) {
        VanChartGaugeAxisGlyph axisGlyph = new VanChartGaugeAxisGlyph();
        initAxisGlyph(axisGlyph);

        return axisGlyph;
    }


    /**
     *初始化对应的坐标轴属性
     * @param axisGlyph 坐标轴绘图区
     */
    public void initAxisGlyph(AxisGlyph axisGlyph) {
        super.initAxisGlyph(axisGlyph);
        VanChartGaugeAxisGlyph vanChartAxisGlyph = (VanChartGaugeAxisGlyph)axisGlyph;
        vanChartAxisGlyph.setMainTickColor(getMainTickColor());
        vanChartAxisGlyph.setSecTickColor(getSecTickColor());
    }

    public boolean equals(Object ob) {
        return super.equals(ob) && ob instanceof VanChartGaugeAxis
                && ComparatorUtils.equals(((VanChartGaugeAxis) ob).getMainTickColor(), this.getMainTickColor())
                && ComparatorUtils.equals(((VanChartGaugeAxis) ob).getSecTickColor(), this.getSecTickColor())
                ;
    }

    public Object clone() throws CloneNotSupportedException {
        VanChartGaugeAxis newAxis = (VanChartGaugeAxis)super.clone();
        newAxis.setMainTickColor(this.getMainTickColor());
        newAxis.setSecTickColor(this.getSecTickColor());
        return newAxis;
    }

    public void readXML(XMLableReader reader) {
        super.readXML(reader);
        if (reader.isChildNode()) {
            String name = reader.getTagName();
            if (name.equals("VanChartGaugeAxisAttr")) {
                mainTickColor = reader.getAttrAsColor("mainTickColor", null);
                secTickColor = reader.getAttrAsColor("secTickColor", null);
            }
        }
    }

    public void writeXML(XMLPrintWriter writer) {
        super.writeXML(writer);

        writer.startTAG("VanChartGaugeAxisAttr");
        if(mainTickColor != null){
            writer.attr("mainTickColor", mainTickColor.getRGB());
        }
        if(secTickColor != null){
            writer.attr("secTickColor", secTickColor.getRGB());
        }
        writer.end();
    }

}
