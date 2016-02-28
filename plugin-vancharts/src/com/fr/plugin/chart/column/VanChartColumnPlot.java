package com.fr.plugin.chart.column;

import com.fr.base.chart.chartdata.ChartData;
import com.fr.chart.base.ChartFunctionProcessor;
import com.fr.chart.base.ChartUtils;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartdata.NormalChartData;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.chartglyph.DataPoint;
import com.fr.chart.chartglyph.PlotGlyph;
import com.fr.general.ComparatorUtils;
import com.fr.general.DateUtils;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.attr.VanChartPlotType;
import com.fr.plugin.chart.attr.axis.AxisTickLineType;
import com.fr.plugin.chart.attr.axis.AxisType;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.axis.VanChartValueAxis;
import com.fr.plugin.chart.attr.plot.VanChartLabelPositionPlot;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;
import com.fr.plugin.chart.base.AttrSeriesImageBackground;
import com.fr.plugin.chart.base.AttrSeriesStackAndAxis;
import com.fr.plugin.chart.glyph.VanChartDataPoint;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;
import com.fr.stable.fun.FunctionProcessor;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.util.List;

/**
 * Created by Mitisky on 15/9/24.
 */
public class VanChartColumnPlot extends VanChartRectanglePlot implements VanChartLabelPositionPlot{

    private static final long serialVersionUID = 2328721012110296113L;

    private boolean isBar = false;

    private double seriesOverlapPercent = 20;
    private double categoryIntervalPercent = 20;
    private boolean fixedWidth = false;//是否固定宽度
    private int columnWidth = 0;//固定宽度大小
    private boolean filledWithImage = false;//是否填充图片

    public void setBar(boolean isBar) {
        this.isBar = isBar;
    }

    /**
     * 是否是条形图
     * @return 是否
     */
    public boolean isBar() {
        return isBar;
    }

    public void setSeriesOverlapPercent(double overlapPercent) {
        this.seriesOverlapPercent = overlapPercent;
    }

    public double getSeriesOverlapPercent() {
        return this.seriesOverlapPercent;
    }

    public void setCategoryIntervalPercent(double catePercent) {
        this.categoryIntervalPercent = catePercent;
    }

    public double getCategoryIntervalPercent() {
        return this.categoryIntervalPercent;
    }

    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    public void setFilledWithImage(boolean filledWithImage) {
        this.filledWithImage = filledWithImage;
    }

    public void setFixedWidth(boolean fixedWidth) {
        this.fixedWidth = fixedWidth;
    }

    /**
     * 是否填充图片
     * @return 返回是否填充图片
     */
    public boolean isFilledWithImage() {
        return filledWithImage;
    }

    /**
     * 是否固定宽度
     * @return 返回是否固定宽度
     */
    public boolean isFixedWidth() {
        return fixedWidth;
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    public VanChartColumnPlot(){
        this(VanChartPlotType.NORMAL);
    }

    public VanChartColumnPlot(VanChartPlotType vanChartPlotType){
        this(false, vanChartPlotType);
    }

    public VanChartColumnPlot(boolean isBar, VanChartPlotType vanChartPlotType){
        super(vanChartPlotType);
        this.isBar = isBar;
        if(isBar){
            xAxisList = VanChartAttrHelper.createDefaultBarXAxisList();
            yAxisList = VanChartAttrHelper.createDefaultBarYAxisList();
        }
    }

    /**
     * 创建x轴
     * @param axisName 轴名称
     * @param position 位置
     * @return 轴
     */
    public VanChartAxis createXAxis(String axisName, int position){
        VanChartAxis axis = isBar() ? new VanChartValueAxis(axisName, position) : new VanChartAxis(axisName, position);
        if(isBar()){
            axis.setAxisStyle(Constants.LINE_NONE);
            axis.setMainTickLine(AxisTickLineType.TICK_LINE_NONE);
        }
        return axis;
    }

    /**
     * 创建y轴
     * @param axisName 轴名称
     * @param position 位置
     * @return 轴
     */
    public VanChartAxis createYAxis(String axisName, int position){
        VanChartAxis axis = isBar() ? new VanChartAxis(axisName, position) : new VanChartValueAxis(axisName, position);
        if(!isBar()){
            axis.setAxisStyle(Constants.LINE_NONE);
            axis.setMainTickLine(AxisTickLineType.TICK_LINE_NONE);
        }
        return axis;
    }

    /**
     * 创建相关PlotGlyph
     * @param chartData 图表相关的数据
     * @return 返回plotGlyph
     */
    public PlotGlyph createPlotGlyph(ChartData chartData) {
        VanChartColumnPlotGlyph plotGlyph = new VanChartColumnPlotGlyph();

        install4PlotGlyph(plotGlyph, chartData);
        installAxisGlyph(plotGlyph, chartData);

        return plotGlyph;
    }

    /**
     * 将图表属性设置到plot glyph中
     * @param plotGlyph  plot类型glyph
     * @param chartData 图表数据.
     */
    public void install4PlotGlyph(VanChartColumnPlotGlyph plotGlyph, ChartData chartData) {
        super.install4PlotGlyph(plotGlyph, chartData);
        plotGlyph.setSeriesOverlapPercent(getSeriesOverlapPercent());
        plotGlyph.setCategoryIntervalPercent(getCategoryIntervalPercent());
        plotGlyph.setFixedWidth(isFixedWidth());
        plotGlyph.setColumnWidth(getColumnWidth());
        plotGlyph.setFilledWithImage(isFilledWithImage());
        plotGlyph.setBar(isBar());
    }

    protected List<VanChartAxis> getValueAxisList(){
        return isBar ? this.xAxisList : this.yAxisList;
    }

    protected VanChartAxis getDefaultValueAxis(){
        return isBar ? getDefaultXAxis() : getDefaultYAxis();
    }

    protected VanChartAxis getDefaultValueAxis(AttrSeriesStackAndAxis attrSeriesStackAndAxis){
        return isBar ? getXAxisList().get(attrSeriesStackAndAxis.getXAxisIndex()) : getYAxisList().get(attrSeriesStackAndAxis.getYAxisIndex());
    }

    protected void dealDataPointCustomCondition(VanChartDataPoint dataPoint, ConditionCollection conditionCollection){
        super.dealDataPointCustomCondition(dataPoint, conditionCollection);

        AttrSeriesImageBackground imageBackground = (AttrSeriesImageBackground)conditionCollection.getCustomDataSeriesCondition(AttrSeriesImageBackground.class, dataPoint);
        ((VanChartColumnDataPoint)dataPoint).setImageBackground(imageBackground);

        ((VanChartColumnDataPoint)dataPoint).setBar(isBar);
    }

    protected DataPoint createDataPoint() {
        return new VanChartColumnDataPoint();
    }

    private static final Object[] NORMAL_SERIES_NAME =  {
            Inter.getLocText("FR-Chart-Data_Series") + "1",
            Inter.getLocText("FR-Chart-Data_Series") + "2",
            Inter.getLocText("FR-Chart-Data_Series") + "3"
    };

    public static final Object[][] NORMAL_VALUES = {
            {"40", "50", "30"},
            {"35", "25", "15"},
            {"25", "45", "55"},
    };

    private static final Object[] VALUE_SERIES_NAME =  {
            Inter.getLocText("FR-Chart-Data_Series") + "1",
    };

    public static final Object[][] VALUE_TIME_VALUES = {
            {"40", "50", "30"},
    };

    /*
 * 基本图表的默认横轴日期数据
 */
    public static final Object[] AXIS_DATE = {
            DateUtils.createDate(2001, 01, 03),
            DateUtils.createDate(2001, 01, 01),
            DateUtils.createDate(2001, 01, 07),
    };


    public static final Object[] AXIS_VALUE = {
            "10",
            "20",
            "30",
    };

    public static final Object[] AXIS_CATEGORY = ChartUtils.CATEGORY_STRING;

    /**
     * 返回柱形图默认的图表数据
     * @return  返回默认数据。
     */
    public ChartData defaultChartData() {
        VanChartAxis axis = isBar() ? getDefaultYAxis() : getDefaultXAxis();
        if(ComparatorUtils.equals(axis.getAxisType(), AxisType.AXIS_CATEGORY)){
            return new NormalChartData(AXIS_CATEGORY, NORMAL_SERIES_NAME, NORMAL_VALUES);
        } else if(ComparatorUtils.equals(axis.getAxisType(), AxisType.AXIS_VALUE)) {
            return new NormalChartData(AXIS_VALUE, VALUE_SERIES_NAME, VALUE_TIME_VALUES);
        } else {
            return new NormalChartData(AXIS_DATE, VALUE_SERIES_NAME, VALUE_TIME_VALUES);
        }

    }

    public boolean equals(Object ob) {
        return ob instanceof VanChartColumnPlot && super.equals(ob)
                &&  ComparatorUtils.equals(((VanChartColumnPlot) ob).isBar(), this.isBar())
                && ComparatorUtils.equals(((VanChartColumnPlot) ob).getCategoryIntervalPercent(), this.getCategoryIntervalPercent())
                && ComparatorUtils.equals(((VanChartColumnPlot) ob).getSeriesOverlapPercent(), this.getSeriesOverlapPercent())
                && ComparatorUtils.equals(((VanChartColumnPlot) ob).isFixedWidth(), this.isFixedWidth())
                && ComparatorUtils.equals(((VanChartColumnPlot) ob).getColumnWidth(), this.getColumnWidth())
                && ComparatorUtils.equals(((VanChartColumnPlot) ob).isFilledWithImage(), this.isFilledWithImage())
                ;
    }

    protected void readPlotXML(XMLableReader reader){
        super.readPlotXML(reader);

        if (reader.isChildNode() && reader.getTagName().equals("VanChartColumnPlotAttr")) {
            isBar = reader.getAttrAsBoolean("isBar", false);
            seriesOverlapPercent = reader.getAttrAsDouble("seriesOverlapPercent", 0);
            categoryIntervalPercent = reader.getAttrAsDouble("categoryIntervalPercent", 0);
            fixedWidth = reader.getAttrAsBoolean("fixedWidth", false);
            columnWidth = reader.getAttrAsInt("columnWidth", 0);
            filledWithImage = reader.getAttrAsBoolean("filledWithImage", false);

            String columnType = reader.getAttrAsString("columnType", "old");
            if(!ComparatorUtils.equals(columnType, "old")){
                this.vanChartPlotType = VanChartPlotType.parse(columnType.toLowerCase().replace("column", StringUtils.EMPTY));
            }
        }
    }

    public void writeXML(XMLPrintWriter writer) {
        super.writeXML(writer);

        writer.startTAG("VanChartColumnPlotAttr")
                .attr("seriesOverlapPercent", seriesOverlapPercent)
                .attr("categoryIntervalPercent", categoryIntervalPercent)
                .attr("fixedWidth", fixedWidth)
                .attr("columnWidth", columnWidth)
                .attr("filledWithImage", filledWithImage)
                .attr("isBar",isBar);


        writer.end();
    }

    public String[] getLabelLocationNameArray() {
        return new String[] {Inter.getLocText("Plugin-ChartF_Inside"), Inter.getLocText("Plugin-ChartF_Outside"), Inter.getLocText("Plugin-ChartF_Center")};
    }

    public Integer[] getLabelLocationValueArray() {
        return new Integer[] {Constants.INSIDE, Constants.OUTSIDE, Constants.CENTER};
    }


    /**
     * 返回Plot的类型名称,
     *
     * @return 类型名称.
     */
    public String getPlotName() {
        return Inter.getLocText(isBar() ? "Plugin-ChartF_NewBar" : "Plugin-ChartF_NewColumn");
    }

    public String getPlotID(){
        return isBar() ? "VanChartBarPlot" : "VanChartColumnPlot";
    }

    /**
     * 是否支持数据表.
     * @return  默认不支持数据表.
     */
    public boolean isSupportDataSheet() {
        return !isBar;
    }

    /**
     * 是否支持分类轴的缩放. 默认不支持 (以下支持: 二维柱形, 折线, 二维面积, 组合图, 股价图)
     * @return  默认不支持坐标轴缩放.
     */
    public boolean isSupportZoomCategoryAxis() {
        return !isBar();
    }

    /**
     * 比较 切换类型界面中的 大致Plot类型
     *
     * @param newPlot 新plot
     * @return 两者是否相符
     */
    public boolean matchPlotType(Plot newPlot) {
        return newPlot instanceof VanChartColumnPlot;
    }

    /**
     * 判断图表类型是否是obClass
     * @param obClass 传入对象
     * @return 是否是obClass对象
     */
    public boolean accept(Class<? extends Plot> obClass){
        return ComparatorUtils.equals(VanChartColumnPlot.class, obClass);
    }


    public Object clone() throws CloneNotSupportedException {
        VanChartColumnPlot newPlot = (VanChartColumnPlot) super.clone();
        newPlot.setBar(this.isBar());
        return newPlot;
    }

    public FunctionProcessor getFunctionToRecord() {
        return isBar ? ChartFunctionProcessor.BAR_VAN_CHART : ChartFunctionProcessor.COLUMN_VAN_CHART;
    }
}
