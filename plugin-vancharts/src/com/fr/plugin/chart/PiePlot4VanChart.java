package com.fr.plugin.chart;

import com.fr.base.chart.chartdata.ChartData;
import com.fr.chart.base.AttrBorder;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.base.ChartFunctionProcessor;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartdata.NormalChartData;
import com.fr.chart.chartglyph.*;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.VanChartLegend;
import com.fr.plugin.chart.attr.plot.VanChartLabelPositionPlot;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.base.ChartRoseType;
import com.fr.plugin.chart.glyph.VanChartDataSeries;
import com.fr.plugin.chart.pie.VanChartPieDataPoint;
import com.fr.plugin.chart.pie.VanChartPiePlotGlyph;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;
import com.fr.stable.fun.FunctionProcessor;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLableReader;

import java.awt.*;

/**
 * 新饼图
 */
public class PiePlot4VanChart extends VanChartPlot implements VanChartLabelPositionPlot{
    private static final long serialVersionUID = 4333286862213569516L;

    public static final double START_ANGLE = 0;
    public static final double END_ANGLE = 360;

    private ChartRoseType roseType = ChartRoseType.PIE;

    private double innerRadiusPercent = 0;// 内径占比

    private double startAngle = START_ANGLE;// 起始角度

    private double endAngle = END_ANGLE;// 结束角度

    private boolean supportRotation = false;// 是否支持旋转

    private boolean useDefaultNullData = false;

    public void setEndAngle(double endAngle) {
        this.endAngle = endAngle;
    }

    public void setInnerRadiusPercent(double innerRadiusPercent) {
        this.innerRadiusPercent = innerRadiusPercent;
    }

    public void setRoseType(ChartRoseType roseType) {
        this.roseType = roseType;
    }

    public void setStartAngle(double startAngle) {
        this.startAngle = startAngle;
    }

    public ChartRoseType getRoseType() {
        return roseType;
    }

    public double getStartAngle() {
        return startAngle;
    }

    public double getInnerRadiusPercent() {
        return innerRadiusPercent;
    }

    public double getEndAngle() {
        return endAngle;
    }

    public void setSupportRotation(boolean supportRotation) {
        this.supportRotation = supportRotation;
    }

    /**
     *  返回是否支持旋转操作
     * @return 返回是否支持旋转操作
     */
    public boolean isSupportRotation() {
        return supportRotation;
    }

    public PiePlot4VanChart(){
        setLegend(new VanChartLegend());
        this.setBorderColor(new Color(238, 238, 238));
        this.setBorderStyle(Constants.LINE_NONE);
    }


    /**
     * 返回Plot的类型名称,
     * @return 类型名称.
     */
    public String getPlotName() {
        return Inter.getLocText("Plugin-ChartF_NewPie");
    }

    /**
     * 创建相关PlotGlyph
     * @param chartData 图表相关的数据
     * @return 返回plotGlyph
     */
    public PlotGlyph createPlotGlyph(ChartData chartData) {
        VanChartPiePlotGlyph plotGlyph = new VanChartPiePlotGlyph();

        install4PlotGlyph(plotGlyph, chartData);

        return plotGlyph;
    }

    /**
     * 创建空值数据.
     * @return  返回图表数据.
     */
    public ChartData createNullChartData() {
        useDefaultNullData = true;
        return super.createNullChartData();
    }

    /**
     * 将图表属性设置到plot glyph中
     * @param plotGlyph  plot类型glyph
     * @param chartData 图表数据.
     */
    public void install4PlotGlyph(VanChartPiePlotGlyph plotGlyph, ChartData chartData) {
        super.install4PlotGlyph(plotGlyph, chartData);
        plotGlyph.setRoseType(getRoseType());
        plotGlyph.setStartAngle(getStartAngle());
        plotGlyph.setEndAngle(getEndAngle());
        plotGlyph.setInnerRadiusPercent(getInnerRadiusPercent());
        plotGlyph.setSupportRotation(isSupportRotation());
        plotGlyph.setUseDefaultNullData(this.useDefaultNullData);
    }

    protected void addSeriesByIndex(int from, int to, PlotGlyph plotGlyph, ChartData cd) {
        super.addSeriesByIndex(from, to, plotGlyph, cd);
        ConditionCollection conditionCollection = getConditionCollection();
        Color[] colors = ChartBaseUtils.createFillColorArray(getPlotFillStyle(), plotGlyph.getSeriesSize());

        for(int seriesIndex = 0, seriesLen = plotGlyph.getSeriesSize()  ; seriesIndex < seriesLen ; seriesIndex++) {
            DataSeries dataSeries = plotGlyph.getSeries(seriesIndex);
            for(int categoryIndex = 0, cateLen = plotGlyph.getCategoryCount(); categoryIndex < cateLen; categoryIndex++) {
                VanChartPieDataPoint dataPoint = (VanChartPieDataPoint)dataSeries.getDataPoint(categoryIndex);
                dealDataPointCustomCondition(dataPoint, conditionCollection);
                dataPoint.setDefaultAttrLabel((AttrLabel)conditionCollection.getDefaultAttr().getExisted(AttrLabel.class));
                dataPoint.setDefaultColor((Color) ChartBaseUtils.getObject(seriesIndex, colors));
            }
        }
    }

    protected DataPoint createDataPoint() {
        return new VanChartPieDataPoint();
    }

    protected DataSeries createDataSeries(int seriesIndex) {
        return new VanChartDataSeries(seriesIndex);
    }

    /**
     * 默认数据.
     * @return 返回数据
     */
    public ChartData defaultChartData() {
        if(ComparatorUtils.equals(getRoseType(), ChartRoseType.PIE_SAME_ARC)){
            return SAME_ARC_DATA;
        } else if(ComparatorUtils.equals(getRoseType(), ChartRoseType.PIE_DIFFERENT_ARC)){
            return DIFFERENT_ARC_DATA;
        }
        return PIE_DATA;
    }

    private static final String[] PIE_CATE = {
            "Pie1"
    };

    private static final String[] PIE_SERIES = {
            "PS1", "PS2", "PS3", "PS4", "PS5", "PS6"
    };

    private static final String[][] PIE_VALUE = {
            {"45"},
            {"26"},
            {"13"},
            {"9"},
            {"6"},
            {"1"}
    };

    private static final String[][] SAME_VALUE = {
            {"50"},
            {"40"},
            {"32"},
            {"25"},
            {"20"},
            {"16"}
    };

    private static final String[][] DIFFERENT_VALUE = {
            {"50"},
            {"40"},
            {"32"},
            {"25"},
            {"20"},
            {"16"}
    };

    private static final NormalChartData PIE_DATA = new NormalChartData(PIE_CATE, PIE_SERIES, PIE_VALUE);
    private static final NormalChartData SAME_ARC_DATA = new NormalChartData(PIE_CATE, PIE_SERIES, SAME_VALUE);
    private static final NormalChartData DIFFERENT_ARC_DATA = new NormalChartData(PIE_CATE, PIE_SERIES, DIFFERENT_VALUE);

    public boolean equals(Object ob) {
        return ob instanceof PiePlot4VanChart && super.equals(ob)
                && ((PiePlot4VanChart) ob).getRoseType() == getRoseType()
                && ((PiePlot4VanChart) ob).getInnerRadiusPercent() == getInnerRadiusPercent()
                && ((PiePlot4VanChart) ob).getStartAngle() == getStartAngle()
                && ((PiePlot4VanChart) ob).getEndAngle() == getEndAngle()
                && ((PiePlot4VanChart) ob).isSupportRotation() == isSupportRotation()
                ;
    }

    protected void readPlotXML(XMLableReader reader){
        super.readPlotXML(reader);
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();

            if(tagName.equals("PieAttr4VanChart")) {
                setRoseType(ChartRoseType.parse(reader.getAttrAsString("roseType", StringUtils.EMPTY)));
                setStartAngle(reader.getAttrAsDouble("startAngle", 0));
                setEndAngle(reader.getAttrAsDouble("endAngle", 0));
                setInnerRadiusPercent(reader.getAttrAsDouble("innerRadius", 0));
                setSupportRotation(reader.getAttrAsBoolean("supportRotation", false));
                reader.readXMLObject(new XMLReadable() {
                    public void readXML(XMLableReader reader) {
                        if (reader.isChildNode()) {
                            //兼容：这边边框之前不是存在条件属性中的
                            AttrBorder attrBorder = (AttrBorder) reader.readXMLObject(new AttrBorder());
                            if(getConditionCollection() != null && getConditionCollection().getDefaultAttr() != null){
                                if(attrBorder != null){
                                    ConditionAttr defaultAttr = getConditionCollection().getDefaultAttr();
                                    if(defaultAttr.getExisted(AttrBorder.class) != null){
                                        defaultAttr.remove(AttrBorder.class);
                                    }
                                    defaultAttr.addDataSeriesCondition(attrBorder);
                                }
                            }
                        }
                    }
                });
            }
        }
    }


    public void writeXML(XMLPrintWriter writer) {
        super.writeXML(writer);

        writer.startTAG("PieAttr4VanChart")
                .attr("roseType", getRoseType().getRoseType())
                .attr("startAngle", getStartAngle())
                .attr("endAngle", getEndAngle())
                .attr("innerRadius", getInnerRadiusPercent())
                .attr("supportRotation", isSupportRotation());

        writer.end();
    }

    public String[] getLabelLocationNameArray() {
        return new String[] {Inter.getLocText("Chart-In_Pie"), Inter.getLocText("Chart-Out_Pie")};
    }

    public Integer[] getLabelLocationValueArray() {
        return  new Integer[] {Constants.INSIDE, Constants.OUTSIDE};
    }

    /**
     * 比较 切换类型界面中的 大致Plot类型
     * @param newPlot  新plot
     * @return  判断是否为饼图
     */
    public boolean matchPlotType(Plot newPlot) {
        return newPlot instanceof PiePlot4VanChart;
    }

    /**
     * 判断图表类型是否是obClass
     * @param obClass 传入对象
     * @return 是否是obClass对象
     */
    public boolean accept(Class<? extends Plot> obClass){
        return ComparatorUtils.equals(PiePlot4VanChart.class, obClass);
    }

    /**
     * 是否支持分类数据
     * @return false 默认不支持分类
     */
    public boolean isSupportCate() {
        return true;
    }

    public String getPlotID(){
        return "VanChartPiePlot";
    }

    public FunctionProcessor getFunctionToRecord() {
        return ChartFunctionProcessor.PIE_VAN_CHARTS;
    }

    /**
     * 是否支持牵引线
     * @return 返回true.
     */
    public boolean isSupportLeadLine() {
        return true;
    }

    /**
     *  没有坐标轴
     *  @return  没有坐标轴.
     */
    public boolean isHaveAxis() {
        return false;
    }

    /**
     * 获取默认的数据点提示的配置
     * @return 获取默认的数据点提示的配置
     */
    public AttrTooltip getDefaultAttrTooltip() {
        AttrTooltip tooltip = new AttrTooltip();
        tooltip.getContent().setSeriesName(true);
        return tooltip;
    }
}
