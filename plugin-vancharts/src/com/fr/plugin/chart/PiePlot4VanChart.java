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
 * �±�ͼ
 */
public class PiePlot4VanChart extends VanChartPlot implements VanChartLabelPositionPlot{
    private static final long serialVersionUID = 4333286862213569516L;

    public static final double START_ANGLE = 0;
    public static final double END_ANGLE = 360;

    private ChartRoseType roseType = ChartRoseType.PIE;

    private double innerRadiusPercent = 0;// �ھ�ռ��

    private double startAngle = START_ANGLE;// ��ʼ�Ƕ�

    private double endAngle = END_ANGLE;// �����Ƕ�

    private boolean supportRotation = false;// �Ƿ�֧����ת

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
     *  �����Ƿ�֧����ת����
     * @return �����Ƿ�֧����ת����
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
     * ����Plot����������,
     * @return ��������.
     */
    public String getPlotName() {
        return Inter.getLocText("Plugin-ChartF_NewPie");
    }

    /**
     * �������PlotGlyph
     * @param chartData ͼ����ص�����
     * @return ����plotGlyph
     */
    public PlotGlyph createPlotGlyph(ChartData chartData) {
        VanChartPiePlotGlyph plotGlyph = new VanChartPiePlotGlyph();

        install4PlotGlyph(plotGlyph, chartData);

        return plotGlyph;
    }

    /**
     * ������ֵ����.
     * @return  ����ͼ������.
     */
    public ChartData createNullChartData() {
        useDefaultNullData = true;
        return super.createNullChartData();
    }

    /**
     * ��ͼ���������õ�plot glyph��
     * @param plotGlyph  plot����glyph
     * @param chartData ͼ������.
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
     * Ĭ������.
     * @return ��������
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
                            //���ݣ���߱߿�֮ǰ���Ǵ������������е�
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
     * �Ƚ� �л����ͽ����е� ����Plot����
     * @param newPlot  ��plot
     * @return  �ж��Ƿ�Ϊ��ͼ
     */
    public boolean matchPlotType(Plot newPlot) {
        return newPlot instanceof PiePlot4VanChart;
    }

    /**
     * �ж�ͼ�������Ƿ���obClass
     * @param obClass �������
     * @return �Ƿ���obClass����
     */
    public boolean accept(Class<? extends Plot> obClass){
        return ComparatorUtils.equals(PiePlot4VanChart.class, obClass);
    }

    /**
     * �Ƿ�֧�ַ�������
     * @return false Ĭ�ϲ�֧�ַ���
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
     * �Ƿ�֧��ǣ����
     * @return ����true.
     */
    public boolean isSupportLeadLine() {
        return true;
    }

    /**
     *  û��������
     *  @return  û��������.
     */
    public boolean isHaveAxis() {
        return false;
    }

    /**
     * ��ȡĬ�ϵ����ݵ���ʾ������
     * @return ��ȡĬ�ϵ����ݵ���ʾ������
     */
    public AttrTooltip getDefaultAttrTooltip() {
        AttrTooltip tooltip = new AttrTooltip();
        tooltip.getContent().setSeriesName(true);
        return tooltip;
    }
}
