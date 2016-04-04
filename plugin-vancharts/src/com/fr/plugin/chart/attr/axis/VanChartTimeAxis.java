package com.fr.plugin.chart.attr.axis;

import com.fr.base.chart.chartdata.ChartData;
import com.fr.chart.chartglyph.AxisGlyph;
import com.fr.general.ComparatorUtils;
import com.fr.plugin.chart.glyph.axis.VanChartTimeAxisGlyph;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

/**
 * 时间坐标轴
 */
public class VanChartTimeAxis extends VanChartAxis {

    public static final String XML_TAG = "VanChartTimeAxis";

    private static final long serialVersionUID = -2579204035140918432L;

    /**
     * 主要刻度的日期类型
     */
    private TimeType mainType = TimeType.TIME_SECOND;
    /**
     * 次要刻度的日期类型
     */
    private TimeType secondType = TimeType.TIME_SECOND;

    /**
     * 设置主要日期类型
     */
    public void setMainType(TimeType mainType) {
        this.mainType = mainType;
    }

    /**
     * 返回主要日期类型
     */
    public TimeType getMainType() {
        return mainType;
    }

    /**
     * 设置次要日期类型
     */
    public void setSecondType(TimeType secondType) {
        this.secondType = secondType;
    }

    /**
     * 返回次要日期类型
     */
    public TimeType getSecondType() {
        return secondType;
    }

    public AxisType getAxisType() {
        return AxisType.AXIS_TIME;
    }

    public VanChartTimeAxis(){
        super();
    }

    public VanChartTimeAxis(String axisName, int position){
        super(axisName, position);
    }

    /**
     *根据ChartData 生成对应的AxisGlyph
     * @param chartData 数据
     * @return 坐标轴绘图区
     */
    public VanChartTimeAxisGlyph createAxisGlyph(ChartData chartData) {
        VanChartTimeAxisGlyph axisGlyph = new VanChartTimeAxisGlyph();
        initAxisGlyph(axisGlyph);

        return axisGlyph;
    }

    /**
     *初始化对应的坐标轴属性
     * @param axisGlyph 坐标轴绘图区
     */
    public void initAxisGlyph(AxisGlyph axisGlyph) {
        super.initAxisGlyph(axisGlyph);
        VanChartTimeAxisGlyph vanChartAxisGlyph = (VanChartTimeAxisGlyph)axisGlyph;
        if(isCustomMainUnit()){
            vanChartAxisGlyph.setMainType(getMainType());
        }
        if(isCustomSecUnit()){
            vanChartAxisGlyph.setSecondType(getSecondType());
        }
    }

    public boolean equals(Object ob) {
        return super.equals(ob) && ob instanceof VanChartTimeAxis
                && ComparatorUtils.equals(((VanChartTimeAxis) ob).getMainType(), this.getMainType())
                && ComparatorUtils.equals(((VanChartTimeAxis) ob).getSecondType(), this.getSecondType());
    }

    public Object clone() throws CloneNotSupportedException {
        VanChartTimeAxis newAxis = (VanChartTimeAxis)super.clone();
        newAxis.setMainType(this.getMainType());
        newAxis.setSecondType(this.getSecondType());
        return newAxis;
    }

    public void readXML(XMLableReader reader) {
        super.readXML(reader);
        if (reader.isChildNode()) {
            String name = reader.getTagName();
            if (name.equals("VanChartTimeAxisAttr")) {
                mainType = TimeType.parseInt(reader.getAttrAsInt("mainType", 0));
                secondType = TimeType.parseInt(reader.getAttrAsInt("secondType", 0));
            }
        }
    }
    public void writeXML(XMLPrintWriter writer) {
        super.writeXML(writer);

        writer.startTAG("VanChartTimeAxisAttr")
                .attr("mainType", mainType.getTimeType())
                .attr("secondType", secondType.getTimeType())
                .end();
    }
}
