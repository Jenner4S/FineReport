package com.fr.plugin.chart.gauge;

import com.fr.base.Utils;
import com.fr.base.chart.chartdata.ChartData;
import com.fr.chart.base.ChartFunctionProcessor;
import com.fr.chart.chartattr.ChartXMLUtils;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartdata.MeterChartData;
import com.fr.chart.chartdata.NormalChartData;
import com.fr.chart.chartglyph.*;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.general.data.MOD_COLUMN_ROW;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.axis.VanChartGaugeAxis;
import com.fr.plugin.chart.attr.plot.VanChartAxisPlot;
import com.fr.plugin.chart.attr.plot.VanChartLabelPositionPlot;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.AttrLabelDetail;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.base.AttrTooltipContent;
import com.fr.plugin.chart.glyph.axis.VanChartGaugeAxisGlyph;
import com.fr.script.Calculator;
import com.fr.stable.Constants;
import com.fr.stable.fun.FunctionProcessor;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 仪表盘
 */
public class VanChartGaugePlot extends VanChartPlot implements VanChartAxisPlot, VanChartLabelPositionPlot{
    private static final long serialVersionUID = 6197588366201673098L;
    private static final FRFont AXIS_LABEL_FONT = FRFont.getInstance("verdana", Font.PLAIN, 8, new Color(102,102,102));//多指针仪表盘坐标轴标签的默认样式

    private static final FRFont POINTER_CATE_LABEL_FONT = FRFont.getInstance("verdana", Font.PLAIN, 10, new Color(51,51,51));//多指针仪表盘分类标签的默认样式
    private static final FRFont POINTER_VALUE_LABEL_FONT = FRFont.getInstance("verdana", Font.PLAIN, 8, new Color(51,51,51));//多指针仪表盘值标签的默认样式

    private static final FRFont RING_PERCENT_LABEL_FONT = FRFont.getInstance("verdana", Font.BOLD, 18, Color.black);//自定义时，圆环仪表盘分类标签的默认样式
    private static final FRFont RING_VALUE_LABEL_FONT = FRFont.getInstance("verdana", Font.PLAIN, 9, new Color(119, 119, 119));//圆环仪表盘值标签的默认样式

    private static final FRFont SLOT_PERCENT_LABEL_FONT = FRFont.getInstance("verdana", Font.BOLD, 27, Color.black);//自定义时，圆环仪表盘分类标签的默认样式
    private static final FRFont SLOT_VALUE_LABEL_FONT = FRFont.getInstance("verdana", Font.PLAIN, 10, new Color(102, 102, 102));//百分比刻度槽仪表盘值标签的默认样式

    public static final FRFont THERMOMETER_PERCENT_LABEL_FONT = FRFont.getInstance("verdana", Font.BOLD, 14, new Color(51,51,51));//试管仪表盘百分比标签的默认样式
    private static final FRFont THERMOMETER_VALUE_LABEL_FONT = FRFont.getInstance("verdana", Font.PLAIN, 9, new Color(186,186,186));//试管仪表盘值标签的默认样式
    public static final FRFont THERMOMETER_VERTICAL_PERCENT_LABEL_FONT = FRFont.getInstance("verdana", Font.BOLD, 9, new Color(51,51,51));//垂直试管仪表盘百分比标签的默认样式

    private GaugeStyle gaugeStyle = GaugeStyle.POINTER;

    private GaugeDetailStyle gaugeDetailStyle;

    private VanChartGaugeAxis gaugeAxis = new VanChartGaugeAxis();

    public GaugeStyle getGaugeStyle() {
        return gaugeStyle;
    }

    public void setGaugeStyle(GaugeStyle gaugeStyle){
        this.gaugeStyle = gaugeStyle;
    }

    public void setGaugeDetailStyle(GaugeDetailStyle gaugeDetailStyle) {
        this.gaugeDetailStyle = gaugeDetailStyle;
    }

    public GaugeDetailStyle getGaugeDetailStyle() {
        return gaugeDetailStyle;
    }

    public void setGaugeAxis(VanChartGaugeAxis gaugeAxis) {
        this.gaugeAxis = gaugeAxis;
    }

    public VanChartGaugeAxis getGaugeAxis() {
        return gaugeAxis;
    }

    public VanChartGaugePlot(){
        this(GaugeStyle.POINTER);
    }

    public VanChartGaugePlot(GaugeStyle gaugeStyle){
        resetNullLegendAttr();
        this.gaugeStyle = gaugeStyle;
        this.gaugeDetailStyle = new GaugeDetailStyle(gaugeStyle);

        gaugeAxis.getTextAttr().setFRFont(AXIS_LABEL_FONT);
    }

    public boolean isMultiPointer() {
        switch (gaugeStyle){
            case POINTER:
                return true;
            case POINTER_SEMI:
                return true;
            default:
                return false;
        }
    }

    /**
     * 根据ChartData创建初始化PlotGlyph
     *
     * @param chartData 图表数据
     * @return 绘图区.
     */
    public PlotGlyph createPlotGlyph(ChartData chartData) {
        VanChartGaugePlotGlyph gaugePlotGlyph = new VanChartGaugePlotGlyph();

        install4PlotGlyph(gaugePlotGlyph, chartData);
        installAxisGlyph(gaugePlotGlyph, chartData);

        return gaugePlotGlyph;
    }

    protected DataPoint createDataPoint() {
        return new VanChartGaugeDataPoint();
    }

    /**
     * 初始化坐标轴属性
     * @param plotGlyph 绘图区
     * @param chartData 图表数据
     */
    public void installAxisGlyph(VanChartGaugePlotGlyph plotGlyph, ChartData chartData) {
        VanChartGaugeAxisGlyph gaugeAxisGlyph = gaugeAxis.createAxisGlyph(chartData);
        plotGlyph.setGaugeAxisGlyph(gaugeAxisGlyph);
    }


    /**
     * 将图表属性设置到plot glyph中
     * @param plotGlyph  plot类型glyph
     * @param chartData 图表数据.
     */
    public void install4PlotGlyph(VanChartGaugePlotGlyph plotGlyph, ChartData chartData) {
        super.install4PlotGlyph(plotGlyph, chartData);
        plotGlyph.setGaugeStyle(getGaugeStyle());
        plotGlyph.setGaugeDetailStyle(getGaugeDetailStyle());
        ConditionAttr attrList = getConditionCollection().getDefaultAttr();
        AttrLabel attrLabel = (AttrLabel)attrList.getExisted(AttrLabel.class);
        plotGlyph.setAttrLabel(attrLabel);
    }

    protected void addSeries2PlotGlyph(PlotGlyph plotGlyph, ChartData chartData) {
        if(chartData == null){
            return;
        }
        if(isMultiPointer() && chartData instanceof NormalChartData){
            super.addSeries2PlotGlyph(plotGlyph, chartData);
        } else if(chartData instanceof MeterChartData){
            MeterChartData cd = ChartXMLUtils.chartData4Meter(chartData);
            addSeriesByIndex(plotGlyph, cd);
        }
    }

    /**
     * 根据ChartData添加系列点到PlotGlyph
     */
    protected void addSeriesByIndex(PlotGlyph plotGlyph, MeterChartData chartData) {
        int namesCount = chartData.getCategoryLabelCount();

        DataSeries dataSeries = createDataSeries(0);
        plotGlyph.addSeries(dataSeries);
        for (int nameIndex = 0; nameIndex < namesCount; nameIndex++) {
            DataPoint dataPoint = createDataPoint();
            dataPoint.setCategoryIndex(nameIndex);
            dataPoint.setSeriesIndex(0);

            double value;
            Number number = chartData.getValue(nameIndex);
            if (number != null) {
                value = number.doubleValue();
            } else {
                dataPoint.setValueIsNull(true);
                value = 0;
            }
            dataPoint.setValue(value);

            if (nameIndex < namesCount) {
                dataPoint.setCategoryName(Utils.objectToString(chartData.getCategoryPresentLabel(nameIndex)));
                dataPoint.setCategoryOriginalName(Utils.objectToString(chartData.getCategoryOriginalLabel(nameIndex)));
            }

            dataSeries.addDataPoint(dataPoint);
        }
    }

    /**
     * 创建x轴
     * @param axisName 轴名称
     * @param position 位置
     * @return 轴
     */
    public VanChartAxis createXAxis(String axisName, int position){
        return new VanChartAxis();
    }

    /**
     * 创建y轴
     * @param axisName 轴名称
     * @param position 位置
     * @return 轴
     */
    public VanChartAxis createYAxis(String axisName, int position){
        return new VanChartAxis();
    }

    public List<VanChartAxis> getYAxisList(){
        return new ArrayList<VanChartAxis>();
    }

    public List<VanChartAxis> getXAxisList(){
        return new ArrayList<VanChartAxis>();
    }

    public ConditionCollection getStackAndAxisCondition(){
        return new ConditionCollection();
    }

    /**
     * 自定义图表
     * @return 自定义图表
     */
    public boolean isCustomChart(){
        return false;
    }

    public String[] getLabelLocationNameArray() {
        switch (gaugeStyle){
            case THERMOMETER:
                if(gaugeDetailStyle.isHorizontalLayout()){
                    return new String[] {Inter.getLocText("Plugin-ChartF_AlertLeft"), Inter.getLocText("Plugin-ChartF_AlertRight")};
                } else {
                    return new String[] {Inter.getLocText("Plugin-ChartF_AxisTop"), Inter.getLocText("Plugin-ChartF_AxisBottom")};
                }
            default:
                return new String[] {Inter.getLocText("Plugin-ChartF_AxisTop"), Inter.getLocText("Plugin-ChartF_AxisBottom")};
        }

    }

    public Integer[] getLabelLocationValueArray() {
        switch (gaugeStyle) {
            case THERMOMETER:
                if(gaugeDetailStyle.isHorizontalLayout()){
                    return new Integer[] {Constants.LEFT, Constants.RIGHT};
                } else {
                    return new Integer[] {Constants.TOP, Constants.BOTTOM};
                }
            default:
                return new Integer[] {Constants.TOP, Constants.BOTTOM};
        }
    }

    /**
     * 获取默认的数据点提示的配置
     * @return 获取默认的数据点提示的配置
     */
    public AttrTooltip getDefaultAttrTooltip() {
        AttrTooltip tooltip = new AttrTooltip();
        tooltip.setEnable(false);
        tooltip.setShowMutiSeries(true);
        AttrTooltipContent content = tooltip.getContent();
        content.setCategoryName(true);
        content.setSeriesName(false);
        content.setValue(true);
        content.setPercentValue(false);
        return tooltip;
    }

    public AttrLabel getDefaultAttrLabel() {
        AttrLabel attrLabel = new AttrLabel();
        attrLabel.setEnable(true);
        AttrLabelDetail detail = attrLabel.getAttrLabelDetail();
        AttrLabelDetail valueDetail = new AttrLabelDetail();
        attrLabel.setGaugeValueLabelDetail(valueDetail);
        AttrTooltipContent content = detail.getContent();
        AttrTooltipContent valueContent = valueDetail.getContent();
        content.setAllSelectFalse();
        valueContent.setAllSelectFalse();

        switch (gaugeStyle){
            case POINTER:
                setMultiDefaultLabel(content, detail, valueContent, valueDetail);
                break;
            case POINTER_SEMI:
                setMultiDefaultLabel(content, detail, valueContent, valueDetail);
                break;
            case RING:
                setRingDefaultLabel(content, detail, valueContent, valueDetail);
                break;
            case SLOT:
                setSlotDefaultLabel(content, detail, valueContent, valueDetail);
                break;
            case THERMOMETER:
                setThermometerDefaultLabel(content, detail, valueContent, valueDetail);
                break;
        }
        return attrLabel;
    }

    private void setMultiDefaultLabel(AttrTooltipContent content, AttrLabelDetail detail, AttrTooltipContent valueContent, AttrLabelDetail valueDetail) {
        content.setCategoryName(true);
        detail.setPosition(Constants.BOTTOM);
        detail.setCustom(true);
        detail.getTextAttr().setFRFont(POINTER_CATE_LABEL_FONT);
        valueContent.setValue(true);
        valueDetail.setCustom(true);
        valueDetail.getTextAttr().setFRFont(POINTER_VALUE_LABEL_FONT);
        valueDetail.setBackgroundColor(new Color(245,245,247));
    }

    private void setRingDefaultLabel(AttrTooltipContent content, AttrLabelDetail detail, AttrTooltipContent valueContent, AttrLabelDetail valueDetail) {
        content.setPercentValue(true);
        detail.getTextAttr().setFRFont(RING_PERCENT_LABEL_FONT);
        valueContent.setCategoryName(true);
        valueContent.setValue(true);
        valueDetail.setCustom(true);
        valueDetail.getTextAttr().setFRFont(RING_VALUE_LABEL_FONT);
    }

    private void setSlotDefaultLabel(AttrTooltipContent content, AttrLabelDetail detail, AttrTooltipContent valueContent, AttrLabelDetail valueDetail) {
        content.setPercentValue(true);
        detail.getTextAttr().setFRFont(SLOT_PERCENT_LABEL_FONT);
        valueContent.setCategoryName(true);
        valueContent.setValue(true);
        valueDetail.setCustom(true);
        valueDetail.getTextAttr().setFRFont(SLOT_VALUE_LABEL_FONT);
    }

    private void setThermometerDefaultLabel(AttrTooltipContent content, AttrLabelDetail detail, AttrTooltipContent valueContent, AttrLabelDetail valueDetail) {
        detail.setCustom(true);
        valueDetail.setCustom(true);
        content.setPercentValue(true);
        valueContent.setCategoryName(true);
        valueContent.setValue(true);
        valueDetail.getTextAttr().setFRFont(THERMOMETER_VALUE_LABEL_FONT);
        if(gaugeDetailStyle.isHorizontalLayout()){
            detail.setPosition(Constants.LEFT);
            valueDetail.setPosition(Constants.LEFT);
            detail.getTextAttr().setFRFont(THERMOMETER_VERTICAL_PERCENT_LABEL_FONT);
        } else {
            detail.setPosition(Constants.BOTTOM);
            valueDetail.setPosition(Constants.BOTTOM);
            detail.getTextAttr().setFRFont(THERMOMETER_PERCENT_LABEL_FONT);
        }
    }

    /**
     * 创建空值数据.
     * @return  返回图表数据.
     */
    public ChartData createNullChartData() {
        return null;
    }

    /**
     * 默认仪表盘数据.
     *
     * @return 返回图表数据.
     */
    public ChartData defaultChartData() {
        switch (gaugeStyle){
            case RING:
                return SINGLE_DATA;
            case SLOT:
                return SINGLE_DATA;
            case THERMOMETER:
                return THERMOMETER_DATA;
            default:
                return NORMAL_DATA;
        }
    }

    private static final MeterChartData SINGLE_DATA = new MeterChartData(
            new String[]{"android"},
            new String[]{"250"}
    );

    private static final MeterChartData THERMOMETER_DATA = new MeterChartData(
            new String[]{"android", "ios"},
            new String[]{"250", "400"}
    );

    private static final String[] NORMAL_CATE = new String[]{
            "China"
    };

    private static final String[] NORMAL_SERIES = new String[]{
            "ios", "android"
    };

    private static final String[][] NORMAL_VALUE = new String[][]{
            {"250"},
            {"400"}
    };

    private static final NormalChartData NORMAL_DATA = new NormalChartData(NORMAL_CATE, NORMAL_SERIES, NORMAL_VALUE);


    protected void readPlotXML(XMLableReader reader){
        super.readPlotXML(reader);
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();

            if(tagName.equals("VanChartGaugePlotAttr")) {
                this.gaugeStyle = GaugeStyle.parse(reader.getAttrAsString("gaugeStyle", GaugeStyle.POINTER.getStyle()));
            } else if(tagName.equals(GaugeDetailStyle.XML_TAG)){
                this.gaugeDetailStyle = (GaugeDetailStyle) reader.readXMLObject(new GaugeDetailStyle(this.gaugeStyle));
            } else if(tagName.equals("gaugeAxis")){
                this.gaugeAxis = (VanChartGaugeAxis)reader.readXMLObject(new VanChartGaugeAxis());
            }
        }
    }

    public void writeXML(XMLPrintWriter writer) {
        super.writeXML(writer);

        writer.startTAG("VanChartGaugePlotAttr")
                .attr("gaugeStyle", getGaugeStyle().getStyle())
                .end();

        if(gaugeDetailStyle != null){
            gaugeDetailStyle.writeXML(writer);
        }

        if(gaugeAxis != null){
            writer.startTAG("gaugeAxis");
            gaugeAxis.writeXML(writer);
            writer.end();
        }
    }



    public boolean equals(Object ob) {
        return ob instanceof VanChartGaugePlot && super.equals(ob)
                && ComparatorUtils.equals(((VanChartGaugePlot) ob).getGaugeStyle(), this.getGaugeStyle())
                && ComparatorUtils.equals(((VanChartGaugePlot) ob).getGaugeDetailStyle(), this.getGaugeDetailStyle())
                && ComparatorUtils.equals(((VanChartGaugePlot) ob).getGaugeAxis(), this.getGaugeAxis())
                ;
    }

    public Object clone() throws CloneNotSupportedException {
        VanChartGaugePlot newPlot = (VanChartGaugePlot) super.clone();
        newPlot.setGaugeStyle(this.getGaugeStyle());
        newPlot.setGaugeDetailStyle((GaugeDetailStyle)this.getGaugeDetailStyle().clone());
        newPlot.setGaugeAxis((VanChartGaugeAxis)this.getGaugeAxis().clone());
        return newPlot;
    }

    /**
     * SE中处理公式
     * @param calculator  计算器
     */
    public void dealFormula(Calculator calculator) {
        super.dealFormula(calculator);
        this.gaugeDetailStyle.dealFormula(calculator);
        this.gaugeAxis.dealFormula(calculator);
    }

    /**
     *  预先计算聚合图表 表间公式顺序.
     * @param list  表间变动list
     * @param calculator   公式计算器
     */
    public void buidExecuteSequenceList(List list, Calculator calculator) {
        super.buidExecuteSequenceList(list, calculator);
        this.gaugeAxis.buidExecuteSequenceList(list, calculator);
    }

    /**
     * 报表插入行列时 公式联动
     * @param mod  行列变动
     */
    public void modFormulaString(MOD_COLUMN_ROW mod) {
        super.modFormulaString(mod);
        this.gaugeAxis.modFormulaString(mod);
    }

    /**
     * 仪表盘不支持条件显示
     * @return  默认支持.
     */
    public boolean isSupportDataSeriesCondition() {
        return false;
    }

    /**
     * 是否支持图例
     * @return 支持则返回true
     */
    public boolean isSupportLegend(){
        return false;
    }

    /**
     * 是否支持绘图区背景
     * @return 默认
     */
    public boolean isSupportPlotBackground(){
        return false;
    }

    /**
     * 数据点提示是否支持显示多系列值
     * @return 默认
     */
    public boolean isSupportTooltipSeriesType(){
        return true;
    }

    /**
     * 是否是仪表盘
     *
     * @return 是则返回true
     */
    public boolean isMeterPlot() {
        return true;
    }

    /**
     * 在选择类型界面时 比较大致的Plot类型.
     * @param newPlot  新plot
     *                 @return  判断是否符合.
     */
    public boolean matchPlotType(Plot newPlot) {
        return newPlot instanceof VanChartGaugePlot;
    }

    @Override
    public String getPlotID() {
        return "VanChartGaugePlot";
    }

    /**
     * 返回Plot的类型名称,
     *
     * @return 类型名称.
     */
    public String getPlotName() {
        return Inter.getLocText("Plugin-ChartF_NewGauge");
    }

    /**
     * 判断图表类型是否是obClass
     * @param obClass 传入对象
     * @return 是否是obClass对象
     */
    public boolean accept(Class<? extends Plot> obClass) {
        return ComparatorUtils.equals(obClass, VanChartGaugePlot.class);
    }

    @Override
    public FunctionProcessor getFunctionToRecord() {
        return ChartFunctionProcessor.METER_VAN_CHART;
    }

}
