package com.fr.plugin.chart.radar;

import com.fr.base.chart.chartdata.ChartData;
import com.fr.chart.base.ChartFunctionProcessor;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartdata.NormalChartData;
import com.fr.chart.chartglyph.*;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.plugin.chart.area.VanChartAreaPlot;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.attr.VanChartPlotType;
import com.fr.plugin.chart.attr.axis.VanChartValueAxis;
import com.fr.plugin.chart.glyph.VanChartDataPoint;
import com.fr.plugin.chart.glyph.VanChartDataSeries;
import com.fr.plugin.chart.glyph.axis.VanChartRadarAxisGlyph;
import com.fr.stable.fun.FunctionProcessor;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.awt.*;

/**
 * Created by Mitisky on 15/12/28.
 */
public class VanChartRadarPlot extends VanChartAreaPlot{

    private static final long serialVersionUID = -3224547323399182733L;

    private RadarType radarType = RadarType.CIRCLE;//雷达图采用圆形还是多边形，polygon表示多边形，circle表示圆形 。默认circle

    public void setRadarType(RadarType radarType) {
        this.radarType = radarType;
    }

    public RadarType getRadarType() {
        return radarType;
    }

    public VanChartRadarPlot() {
        super();
    }
    public VanChartRadarPlot(VanChartPlotType vanChartPlotType){
        super(vanChartPlotType);
    }

    protected void initXYAxisList() {
        xAxisList = VanChartAttrHelper.createRadarXAxisList();
        yAxisList = VanChartAttrHelper.createRadarYAxisList();
    }

    /**
     * 自定义图表
     * @return 自定义图表
     */
    public boolean isCustomChart() {
        return false;
    }

    /**
     * 根据ChartData生成对应的Area3DPlotGlyph.
     * @param chartData 图表相关的数据
     * @return 绘图区对象
     */
    public PlotGlyph createPlotGlyph(ChartData chartData) {
        VanChartRadarPlotGlyph plotGlyph =  new VanChartRadarPlotGlyph();
        install4PlotGlyph(plotGlyph, chartData);
        installAxisGlyph(plotGlyph, chartData);
        return plotGlyph;
    }

    protected DataPoint createDataPoint() {
        return new VanChartRadarDataPoint(isStackChart());
    }

    /**
     * 将图表属性设置到plot glyph中
     * @param plotGlyph  plot类型glyph
     * @param chartData 图表数据.
     */
    public void install4PlotGlyph(VanChartRadarPlotGlyph plotGlyph, ChartData chartData) {
        super.install4PlotGlyph(plotGlyph, chartData);
        plotGlyph.setRadarType(radarType);
        plotGlyph.setColumnType(isStackChart());
    }

    /**
     * 初始化坐标轴属性
     * @param plotGlyph 绘图区
     * @param chartData 图表数据
     */
    public void installAxisGlyph(VanChartRadarPlotGlyph plotGlyph, ChartData chartData) {
        VanChartRadarAxisGlyph radarAxisGlyph = new VanChartRadarAxisGlyph(chartData, getDefaultXAxis(),
                (VanChartValueAxis)getDefaultYAxis(), getRadarType());
        plotGlyph.setRadarAxisGlyph(radarAxisGlyph);
        // 这边把雷达图的坐标轴放到rectangle的list的原因是：
        // rectangle里面好多计算需要获取坐标轴相关信息，都是从list里面取相应的坐标轴
        plotGlyph.addXAxisGlyph(radarAxisGlyph.getDataSeriesCateAxisGlyph());
        plotGlyph.addYAxisGlyph(radarAxisGlyph.getDataSeriesValueAxisGlyph());
    }

    //这个是画标签的时候用到的（标签的时候取系列色）
    protected void setSeriesColor(VanChartDataPoint dataPoint, VanChartDataSeries dataSeries){
        if(isNormalChart()){//常规雷达图的数据点的color没有意义，系列的color才有意义
            dataPoint.setColor(dataSeries.getColor());
        }
    }

    //处理series的bands属性
    protected void dealDataSeriesBands(VanChartDataSeries dataSeries, ConditionCollection conditionCollection){
    }

    private static final String[] CATE_NAME = {"speed", "judgment", "calculation", "precision", "observation", "memory"};
    private static final Object[] SERIES_NAME = {"michael", "Hepburn"};
    private static final Object[][] VALUE = {{"40", "45", "35", "55", "35", "45"}, {"35", "40", "35", "45", "33", "37"}};
    private static final NormalChartData RADAR_DATA = new NormalChartData(CATE_NAME, SERIES_NAME, VALUE);


    /**
     * 设计图表时, 取得的默认图表数据
     * @return 默认数据
     */
    public ChartData defaultChartData() {
        return RADAR_DATA;
    }

    public boolean equals(Object ob) {
        return ob instanceof VanChartRadarPlot && super.equals(ob)
                && ComparatorUtils.equals(((VanChartRadarPlot) ob).getRadarType(), this.getRadarType())
                ;
    }

    public Object clone() throws CloneNotSupportedException {
        VanChartRadarPlot newPlot = (VanChartRadarPlot) super.clone();
        newPlot.setRadarType(this.getRadarType());
        return newPlot;
    }

    protected void readPlotXML(XMLableReader reader){
        super.readPlotXML(reader);
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();

            if(tagName.equals("VanChartRadarPlotAttr")) {
                radarType = RadarType.parse(reader.getAttrAsString("radarType", RadarType.CIRCLE.getType()));
            }
        }
    }

    public void writeXML(XMLPrintWriter writer) {
        super.writeXML(writer);

        writer.startTAG("VanChartRadarPlotAttr")
                .attr("radarType", radarType.getType())
                .end();
    }

    /**
     * 是否支持趋势线 返回false 默认不支持
     * @return  是否支持趋势线.
     */
    public boolean isSupportTrendLine() {
        return false;
    }

    /**
     * 是否支持数据表.
     * @return  默认不支持数据表.
     */
    public boolean isSupportDataSheet() {
        return false;
    }

    /**
     * 是否支持分类轴的缩放. 默认不支持 (以下支持: 二维柱形, 折线, 二维面积, 组合图, 股价图)
     * @return  默认不支持坐标轴缩放.
     */
    public boolean isSupportZoomCategoryAxis() {
        return false;
    }

    /**
     * 是否支持缩放方向设置
     * @return 默认不支持
     */
    public boolean isSupportZoomDirection() {
        return false;
    }

    /**
     *  是否支持默认边框
     *  @return 返回不支持边框
     *  */
    public boolean isSupportBorder() {
        return false;
    }

    /**
     * 在选择类型界面时 比较大致的Plot类型.
     * @param newPlot  新plot
     *                 @return  判断是否符合.
     */
    public boolean matchPlotType(Plot newPlot) {
        return newPlot instanceof VanChartRadarPlot;
    }

    @Override
    public String getPlotID() {
        return "VanChartRadarPlot";
    }

    /**
     * 返回Plot的类型名称,
     *
     * @return 类型名称.
     */
    public String getPlotName() {
        return Inter.getLocText("Plugin-ChartF_NewRadar");
    }

    /**
     * 判断图表类型是否是obClass
     * @param obClass 传入对象
     * @return 是否是obClass对象
     */
    public boolean accept(Class<? extends Plot> obClass) {
        return ComparatorUtils.equals(obClass, VanChartRadarPlot.class);
    }

    public FunctionProcessor getFunctionToRecord() {
        return ChartFunctionProcessor.RADAR_VAN_CHART;
    }
}
