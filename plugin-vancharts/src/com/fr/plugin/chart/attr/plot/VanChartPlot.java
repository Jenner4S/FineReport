package com.fr.plugin.chart.attr.plot;

import com.fr.base.chart.chartdata.ChartData;
import com.fr.chart.base.*;
import com.fr.chart.chartattr.Legend;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.*;
import com.fr.general.ComparatorUtils;
import com.fr.js.NameJavaScriptGroup;
import com.fr.plugin.chart.attr.VanChartLegend;
import com.fr.plugin.chart.base.AttrFloatColor;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.glyph.VanChartDataPoint;
import com.fr.plugin.chart.glyph.VanChartDataSeries;
import com.fr.plugin.chart.glyph.VanChartLegendGlyph;
import com.fr.plugin.chart.glyph.VanChartPlotGlyph;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLableReader;

/**
 * 新特性图表特有的属性
 */
public abstract class VanChartPlot extends Plot {

    private static final long serialVersionUID = 734260049200573474L;
    private boolean monitorRefresh = false;

    public VanChartPlot () {
        setLegend(new VanChartLegend());
    }

    public void setMonitorRefresh(boolean monitorRefresh) {
        this.monitorRefresh = monitorRefresh;
    }

    /**
     * 是否是监控刷新
     * @return 是否是监控刷新
     */
    public boolean isMonitorRefresh() {
        return this.monitorRefresh;
    }

    /**
     * 将图表属性设置到plot glyph中
     * @param plotGlyph  plot类型glyph
     * @param chartData 图表数据.
     */
    public void install4PlotGlyph(VanChartPlotGlyph plotGlyph, ChartData chartData) {
        super.install4PlotGlyph(plotGlyph, chartData);
        if(isSupportMonitorRefresh()){
            plotGlyph.setMonitorRefresh(isMonitorRefresh());
        }
    }

    /**
     * 创建图例
     * @param plotGlyph  绘图区
     * @return  图例Glyph
     */
    public VanChartLegendGlyph createLegendGlyph(PlotGlyph plotGlyph) {
        if (getLegend() == null) {
            return null;
        }
        return ((VanChartLegend)getLegend()).createLegendGlyph();
    }

    protected DataPoint createDataPoint() {
        return new VanChartDataPoint();
    }

    protected DataSeries createDataSeries(int seriesIndex) {
        return new VanChartDataSeries(seriesIndex);
    }

    /**
     * 新图表为了预览速度，在plotGlyph中生成标签
     * @param plotGlyph 图表
     */
    protected void createDataPointLabel(PlotGlyph plotGlyph) {
    }

    /**
     * 是否支持监控刷新
     * @return 默认不支持
     */
    public boolean isSupportMonitorRefresh() {
        return false;
    }

    protected void dealDataPointCustomCondition(VanChartDataPoint dataPoint, ConditionCollection conditionCollection){
        AttrLabel attrLabel = (AttrLabel)conditionCollection.getCustomDataSeriesCondition(AttrLabel.class, dataPoint);
        dataPoint.setLabel(attrLabel);

        AttrTooltip attrTooltip = (AttrTooltip)conditionCollection.getCustomDataSeriesCondition(AttrTooltip.class, dataPoint);
        dataPoint.setTooltip(attrTooltip);

        AttrAlpha attrAlpha = (AttrAlpha)conditionCollection.getCustomDataSeriesCondition(AttrAlpha.class, dataPoint);
        dataPoint.setAlpha(attrAlpha);

        AttrBorder attrBorder = (AttrBorder)conditionCollection.getCustomDataSeriesCondition(AttrBorder.class, dataPoint);
        dataPoint.setBorder(attrBorder);

        AttrBackground attrColor = (AttrBackground)conditionCollection.getCustomDataSeriesCondition(AttrBackground.class, dataPoint);
        dataPoint.setColor(attrColor);

        AttrFloatColor floatColor = (AttrFloatColor)conditionCollection.getCustomDataSeriesCondition(AttrFloatColor.class, dataPoint);
        dataPoint.setFloatColor(floatColor);
    }

    /**
     * 获取默认的数据点提示的配置
     * @return 获取默认的数据点提示的配置
     */
    public AttrTooltip getDefaultAttrTooltip() {
        AttrTooltip tooltip = new AttrTooltip();
        tooltip.getContent().setCategoryName(true);
        tooltip.getContent().setSeriesName(true);
        return tooltip;
    }

    public AttrLabel getDefaultAttrLabel() {
        return new AttrLabel();
    }

    protected void readPlotXML(XMLableReader reader){
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();

            if (ConditionCollection.XML_TAG.equals(tagName)) {
                setConditionCollection((ConditionCollection) reader.readXMLObject(new ConditionCollection()));
            } else if (VanChartLegend.XML_TAG.equals(tagName)) {
                setLegend((Legend)reader.readXMLObject(new VanChartLegend()));
            } else if (DataSheet.XML_TAG.equals(tagName)) {
                setDataSheet((DataSheet)reader.readXMLObject(new DataSheet()));
            } else if(tagName.equals("newHotTooltipStyle")){
                reader.readXMLObject(new AttrContents());
            } else if(NameJavaScriptGroup.XML_TAG.equals(tagName)) {
                setHotHyperLink((NameJavaScriptGroup) reader.readXMLObject(new NameJavaScriptGroup()));
            } else if ((tagName.equals("Attr"))) {
                readerAttr(reader);
            } else if(tagName.equals("newPlotFillStyle")) {
                reader.readXMLObject(new XMLReadable() {
                    public void readXML(XMLableReader reader) {
                        if (reader.isChildNode()) {
                            setPlotFillStyle((AttrFillStyle)reader.readXMLObject(new AttrFillStyle()));
                        }
                    }
                });
            }
//            else if(tagName.equals("VanChartPlotAttr")) {
//                monitorRefresh = reader.getAttrAsBoolean("monitorRefresh",false);
//            }
        }
    }

    protected void readerAttr(XMLableReader reader) {
        setAutoRefreshPerSecond(reader.getAttrAsInt("autoRefreshPerSecond", 0));
        setPlotStyle(reader.getAttrAsInt("plotStyle", 0));
    }
//
//    public void writeXML(XMLPrintWriter writer) {
//        super.writeXML(writer);
//
//        writer.startTAG("VanChartPlotAttr")
//                .attr("monitorRefresh", monitorRefresh)
//                .end();
//    }


    public boolean equals(Object ob) {
        return ob instanceof VanChartPlot && super.equals(ob)
                && ComparatorUtils.equals(((VanChartPlot) ob).isMonitorRefresh(), this.isMonitorRefresh())
                ;
    }

    public Object clone() throws CloneNotSupportedException {
        VanChartPlot newPlot = (VanChartPlot) super.clone();
        newPlot.setMonitorRefresh(this.isMonitorRefresh());
        return newPlot;
    }

}
