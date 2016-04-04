package com.fr.plugin.chart.attr.plot;

import com.fr.base.chart.chartdata.ChartData;
import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrBackground;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.chartglyph.DataSheetGlyph;
import com.fr.chart.chartglyph.PlotGlyph;
import com.fr.general.ComparatorUtils;
import com.fr.general.data.MOD_COLUMN_ROW;
import com.fr.general.xml.GeneralXMLTools;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.attr.VanChartPlotType;
import com.fr.plugin.chart.attr.axis.AxisTickLineType;
import com.fr.plugin.chart.attr.axis.AxisType;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.axis.VanChartValueAxis;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.AttrSeriesStackAndAxis;
import com.fr.plugin.chart.base.VanChartAttrTrendLine;
import com.fr.plugin.chart.base.VanChartConstants;
import com.fr.plugin.chart.glyph.VanChartDataPoint;
import com.fr.plugin.chart.glyph.VanChartDataSeries;
import com.fr.plugin.chart.glyph.VanChartRectanglePlotGlyph;
import com.fr.plugin.chart.glyph.axis.VanChartBaseAxisGlyph;
import com.fr.script.Calculator;
import com.fr.stable.Constants;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLableReader;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 矩形绘图区抽象类
 */
public abstract class VanChartRectanglePlot extends VanChartPlot implements VanChartAxisPlot{
    private static final long serialVersionUID = -464988260022618096L;

    protected List<VanChartAxis> xAxisList;
    protected List<VanChartAxis> yAxisList;

    protected VanChartPlotType vanChartPlotType = VanChartPlotType.NORMAL;
    private boolean isDefaultIntervalBackground = true;

    private ConditionCollection stackAndAxisCondition = new ConditionCollection();

    public List<VanChartAxis> getYAxisList() {
        return yAxisList;
    }

    public List<VanChartAxis> getXAxisList() {
        return xAxisList;
    }

    public void setXAxisList(List<VanChartAxis> xAxisList) {
        this.xAxisList = xAxisList;
    }

    public void setYAxisList(List<VanChartAxis> yAxisList) {
        this.yAxisList = yAxisList;
    }

    public void setVanChartPlotType(VanChartPlotType vanChartPlotType) {
        this.vanChartPlotType = vanChartPlotType;
    }

    public VanChartPlotType getVanChartPlotType() {
        return vanChartPlotType;
    }

    public void setIsDefaultIntervalBackground(boolean isDefaultIntervalBackground) {
        this.isDefaultIntervalBackground = isDefaultIntervalBackground;
    }

    /**
     * 默认间隔背景
     * @return 默认间隔背景
     */
    public boolean isDefaultIntervalBackground() {
        return isDefaultIntervalBackground;
    }

    public void setStackAndAxisCondition(ConditionCollection stackAndAxisCondition) {
        this.stackAndAxisCondition = stackAndAxisCondition;
    }

    public ConditionCollection getStackAndAxisCondition() {
        return stackAndAxisCondition;
    }

    public VanChartRectanglePlot() {
        this(VanChartPlotType.NORMAL);
    }

    public VanChartRectanglePlot(VanChartPlotType vanChartPlotType){
        super();
        this.vanChartPlotType = vanChartPlotType;
        initXYAxisList();
        setDataSheetDefaultFormat();
    }

    protected void initXYAxisList() {
        xAxisList = VanChartAttrHelper.createDefaultXAxisList();
        yAxisList = VanChartAttrHelper.createDefaultYAxisList();
    }

    protected void setDataSheetDefaultFormat() {
        if(getDataSheet() != null){
            getDataSheet().setFormat(VanChartAttrHelper.VALUE_FORMAT);
        }
    }

    /**
     * 创建x轴
     * @param axisName 轴名称
     * @param position 位置
     * @return 轴
     */
    public VanChartAxis createXAxis(String axisName, int position){
        return new VanChartAxis(axisName, position);
    }

    /**
     * 创建y轴
     * @param axisName 轴名称
     * @param position 位置
     * @return 轴
     */
    public VanChartAxis createYAxis(String axisName, int position){
        VanChartAxis axis = new VanChartValueAxis(axisName, position);
        axis.setAxisStyle(Constants.LINE_NONE);
        axis.setMainTickLine(AxisTickLineType.TICK_LINE_NONE);
        return axis;
    }

    public boolean isNormalChart() {
        return ComparatorUtils.equals(vanChartPlotType, VanChartPlotType.NORMAL);
    }

    /**
     * 自定义图表
     * @return 自定义图表
     */
    public boolean isCustomChart() {
        return ComparatorUtils.equals(vanChartPlotType, VanChartPlotType.CUSTOM);
    }

    /**
     * 堆积图表
     * @return 堆积图表
     */
    public boolean isStackChart() {
        return ComparatorUtils.equals(vanChartPlotType, VanChartPlotType.STACK);
    }

    /**
     * 百分比堆积图表
     * @return 百分比堆积图表
     */
    public boolean isPercentStackChart() {
        return ComparatorUtils.equals(vanChartPlotType, VanChartPlotType.STACK_BY_PERCENT);
    }

    public VanChartAxis getDefaultXAxis() {
        return xAxisList.get(0);
    }

    public VanChartAxis getDefaultYAxis() {
        return yAxisList.get(0);
    }

    /**
     * 初始化坐标轴属性
     * @param plotGlyph 绘图区
     * @param chartData 图表数据
     */
    public void installAxisGlyph(VanChartRectanglePlotGlyph plotGlyph, ChartData chartData) {
        for(VanChartAxis axis : xAxisList){
            VanChartBaseAxisGlyph axisGlyph = axis.createAxisGlyph(chartData);
            plotGlyph.addXAxisGlyph(axisGlyph);
        }
        for(VanChartAxis axis : yAxisList){
            VanChartBaseAxisGlyph axisGlyph = axis.createAxisGlyph(chartData);
            plotGlyph.addYAxisGlyph(axisGlyph);
        }
    }

    /**
     * 创建数据表对应的Glyph
     * @param plotGlyph  绘图区
     *                   @return  数据表Glyph
     */
    public DataSheetGlyph createDataSheetGlyph(PlotGlyph plotGlyph) {
        boolean bottomAndCateAxis = getDefaultXAxis().getPosition() == VanChartConstants.AXIS_BOTTOM
                && ComparatorUtils.equals(getDefaultXAxis().getAxisType(), AxisType.AXIS_CATEGORY);
        if (getDataSheet() != null && getDataSheet().isVisible() && bottomAndCateAxis) {
            return plotGlyph.createDataSheetGlyph(getDataSheet(), null);
        } else if(getDataSheet() != null){
            getDataSheet().setVisible(false);
        }
        return null;
    }

    protected void addSeriesByIndex(int from, int to, PlotGlyph plotGlyph, ChartData cd) {
        super.addSeriesByIndex(from, to, plotGlyph, cd);
        ConditionCollection conditionCollection = getConditionCollection();
        Color[] colors = ChartBaseUtils.createFillColorArray(getPlotFillStyle(), plotGlyph.getSeriesSize());
        ConditionCollection seriesStackAndAxis = getStackAndAxisCondition();
        clearAllAxisPercent();

        for(int seriesIndex = 0, seriesLen = plotGlyph.getSeriesSize()  ; seriesIndex < seriesLen ; seriesIndex++) {
            VanChartDataSeries dataSeries = (VanChartDataSeries)plotGlyph.getSeries(seriesIndex);
            dealDataSeriesCustomCondition(dataSeries,conditionCollection, seriesStackAndAxis);
            for(int categoryIndex = 0, cateLen = plotGlyph.getCategoryCount(); categoryIndex < cateLen; categoryIndex++) {
                VanChartDataPoint dataPoint = (VanChartDataPoint)dataSeries.getDataPoint(categoryIndex);
                dealDataPointCustomCondition(dataPoint, conditionCollection);
                dataPoint.setDefaultAttrLabel((AttrLabel)conditionCollection.getDefaultAttr().getExisted(AttrLabel.class));
                dataPoint.setDefaultColor((Color) ChartBaseUtils.getObject(seriesIndex, colors));
                setSeriesColor(dataPoint, dataSeries);
            }
        }

    }

    protected void setSeriesColor(VanChartDataPoint dataPoint, VanChartDataSeries dataSeries){

    }

    private void clearAllAxisPercent() {
        if(isPercentStackChart()){
            return;
        }
        for(VanChartAxis axis : getValueAxisList()){
            axis.setPercentage(false);
        }
    }

    protected List<VanChartAxis> getValueAxisList(){
        return this.yAxisList;
    }

    protected void dealDataSeriesCustomCondition(VanChartDataSeries dataSeries, ConditionCollection conditionCollection, ConditionCollection stackAndAxisCondition){
        dealDataSeriesStackAndAxisCondition(dataSeries, stackAndAxisCondition);
        VanChartAttrTrendLine attrTrendLine = (VanChartAttrTrendLine)conditionCollection.getCustomDataSeriesCondition(VanChartAttrTrendLine.class, dataSeries);
        dataSeries.setAttrTrendLine(attrTrendLine);

        //因为前台的图例取得系列色，所以系列这个层级的条件属性也要传过去
        AttrBackground attrColor = (AttrBackground)conditionCollection.getCustomDataSeriesCondition(AttrBackground.class, dataSeries);
        dataSeries.setColor(attrColor);//折线图条件属性配色只对系列有意义

        //前台图例要用
        AttrAlpha attrAlpha = (AttrAlpha)conditionCollection.getCustomDataSeriesCondition(AttrAlpha.class, dataSeries);
        dataSeries.setAlpha(attrAlpha);
    }


    protected void dealDataSeriesStackAndAxisCondition(VanChartDataSeries dataSeries, ConditionCollection stackAndAxisCondition){
        if(isCustomChart()){
            AttrSeriesStackAndAxis attrSeriesStackAndAxis = (AttrSeriesStackAndAxis)stackAndAxisCondition.getCustomDataSeriesCondition(AttrSeriesStackAndAxis.class, dataSeries);
            dataSeries.setAttrSeriesStackAndAxis(attrSeriesStackAndAxis);
            if(attrSeriesStackAndAxis != null && attrSeriesStackAndAxis.isPercentStacked()){
                VanChartAxis axis = getDefaultValueAxis(attrSeriesStackAndAxis);
                axis.setPercentage(true);
            }
        } else if(isStackChart()){
            AttrSeriesStackAndAxis attrSeriesStackAndAxis = new AttrSeriesStackAndAxis();
            dataSeries.setAttrSeriesStackAndAxis(attrSeriesStackAndAxis);
        } else if(isPercentStackChart()){
            AttrSeriesStackAndAxis attrSeriesStackAndAxis = new AttrSeriesStackAndAxis();
            attrSeriesStackAndAxis.setPercentStacked(true);
            dataSeries.setAttrSeriesStackAndAxis(attrSeriesStackAndAxis);
            VanChartAxis axis = getDefaultValueAxis();
            axis.setPercentage(true);
        }
    }

    protected VanChartAxis getDefaultValueAxis(){
        return getDefaultYAxis();
    }

    protected VanChartAxis getDefaultValueAxis(AttrSeriesStackAndAxis attrSeriesStackAndAxis){
        return getYAxisList().get(attrSeriesStackAndAxis.getYAxisIndex());
    }

    public boolean equals(Object ob) {
        return ob instanceof VanChartRectanglePlot && super.equals(ob)
                && ComparatorUtils.equals(((VanChartRectanglePlot) ob).getVanChartPlotType(), this.getVanChartPlotType())
                && ComparatorUtils.equals(((VanChartRectanglePlot) ob).isDefaultIntervalBackground(), this.isDefaultIntervalBackground())
                && ComparatorUtils.equals(((VanChartRectanglePlot) ob).getYAxisList(), this.getYAxisList())
                && ComparatorUtils.equals(((VanChartRectanglePlot) ob).getXAxisList(), this.getXAxisList())
                && ComparatorUtils.equals(((VanChartRectanglePlot) ob).getStackAndAxisCondition(), this.getStackAndAxisCondition())
                ;
    }

    public Object clone() throws CloneNotSupportedException {
        VanChartRectanglePlot newPlot = (VanChartRectanglePlot) super.clone();
        newPlot.setVanChartPlotType(this.getVanChartPlotType());
        newPlot.setIsDefaultIntervalBackground(this.isDefaultIntervalBackground());
        newPlot.xAxisList = new ArrayList<VanChartAxis>();
        for(VanChartAxis axis : this.xAxisList){
            newPlot.xAxisList.add((VanChartAxis)axis.clone());
        }
        newPlot.yAxisList = new ArrayList<VanChartAxis>();
        for(VanChartAxis axis : this.yAxisList){
            newPlot.yAxisList.add((VanChartAxis)axis.clone());
        }
        if(this.getStackAndAxisCondition() != null){
            newPlot.setStackAndAxisCondition((ConditionCollection)this.getStackAndAxisCondition().clone());
        }

        return newPlot;
    }

    protected void readPlotXML(XMLableReader reader){
        super.readPlotXML(reader);
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();

            if(tagName.equals("VanChartRectanglePlotAttr")) {
                vanChartPlotType = VanChartPlotType.parse(reader.getAttrAsString("vanChartPlotType", VanChartPlotType.NORMAL.getType()));
                isDefaultIntervalBackground = reader.getAttrAsBoolean("isDefaultIntervalBackground",true);
            } else if(tagName.equals("XAxisList")){
                xAxisList.clear();
                reader.readXMLObject(new XMLReadable() {
                    public void readXML(XMLableReader reader) {
                        if (reader.isChildNode()) {
                            if (reader.getTagName().equals("VanChartAxis")) {
                                xAxisList.add((VanChartAxis)GeneralXMLTools.readXMLable(reader));
                            }
                        }
                    }
                });
            } else if(tagName.equals("YAxisList")){
                yAxisList.clear();
                reader.readXMLObject(new XMLReadable() {
                    public void readXML(XMLableReader reader) {
                            if (reader.isChildNode()) {
                                if (reader.getTagName().equals("VanChartAxis")) {
                                    yAxisList.add((VanChartAxis)GeneralXMLTools.readXMLable(reader));
                                }
                            }
                        }

                });
            } else if(tagName.equals("stackAndAxisCondition")){
                reader.readXMLObject(new XMLReadable() {
                    @Override
                    public void readXML(XMLableReader reader) {
                        if (reader.isChildNode() && reader.getTagName().equals(ConditionCollection.XML_TAG)) {
                            setStackAndAxisCondition((ConditionCollection) reader.readXMLObject(new ConditionCollection()));
                        }
                    }
                });
            }
        }
    }

    public void writeXML(XMLPrintWriter writer) {
        super.writeXML(writer);

        writer.startTAG("VanChartRectanglePlotAttr")
                .attr("vanChartPlotType", vanChartPlotType.getType())
                .attr("isDefaultIntervalBackground", isDefaultIntervalBackground)
                .end();

        writer.startTAG("XAxisList");
        for (VanChartAxis axis : xAxisList) {
            GeneralXMLTools.writeXMLable(writer, axis, "VanChartAxis");
        }
        writer.end();

        writer.startTAG("YAxisList");
        for(VanChartAxis axis : yAxisList){
            GeneralXMLTools.writeXMLable(writer, axis, "VanChartAxis");
        }
        writer.end();

        if(stackAndAxisCondition != null){
            writer.startTAG("stackAndAxisCondition");
            stackAndAxisCondition.writeXML(writer);
            writer.end();
        }
    }

    /**
     * SE中处理公式
     * @param calculator  计算器
     */
    public void dealFormula(Calculator calculator) {
        super.dealFormula(calculator);

        for(VanChartAxis axis : xAxisList){
            axis.dealFormula(calculator);
        }

        for(VanChartAxis axis : yAxisList){
            axis.dealFormula(calculator);
        }
    }

    /**
     *  预先计算聚合图表 表间公式顺序.
     * @param list  表间变动list
     * @param calculator   公式计算器
     */
    public void buidExecuteSequenceList(List list, Calculator calculator) {
        super.buidExecuteSequenceList(list, calculator);

        for(VanChartAxis axis : xAxisList){
            axis.buidExecuteSequenceList(list, calculator);
        }

        for(VanChartAxis axis : yAxisList){
            axis.buidExecuteSequenceList(list, calculator);
        }
    }

    /**
     * 报表插入行列时 公式联动
     * @param mod  行列变动
     */
    public void modFormulaString(MOD_COLUMN_ROW mod) {
        super.modFormulaString(mod);

        for(VanChartAxis axis : xAxisList){
            axis.modFormulaString(mod);
        }

        for(VanChartAxis axis : yAxisList){
            axis.modFormulaString(mod);
        }
    }


    /**
     * 返回true, 支持间隔背景
     * @return  返回支持间隔背景
     */
    public boolean isSupportIntervalBackground() {
        return true;
    }

    /**
     *  是否支持默认边框
     *  @return 返回不支持边框
     *  */
    public boolean isSupportBorder() {
        return true;
    }

    /**
     * 是否支持趋势线 返回false 默认不支持
     * @return  是否支持趋势线.
     */
    public boolean isSupportTrendLine() {
        return true;
    }

    /**
     * 数据点提示是否支持显示多系列值
     * @return 默认
     */
    public boolean isSupportTooltipSeriesType(){
        return true;
    }

    /**
     * 是否支持数据表.
     * @return  默认不支持数据表.
     */
    public boolean isSupportDataSheet() {
        return true;
    }

    /**
     * 是否支持分类轴的缩放. 默认不支持。即缩放控件。
     * @return  默认不支持坐标轴缩放.
     */
    public boolean isSupportZoomCategoryAxis() {
        return true;
    }

    /**
     * 是否支持缩放方向设置
     * @return 默认不支持
     */
    public boolean isSupportZoomDirection() {
        return true;
    }

    /**
     * 是否支持监控刷新
     * @return 默认不支持
     */
    public boolean isSupportMonitorRefresh() {
        return true;
    }

}
