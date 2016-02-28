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

    private RadarType radarType = RadarType.CIRCLE;//�״�ͼ����Բ�λ��Ƕ���Σ�polygon��ʾ����Σ�circle��ʾԲ�� ��Ĭ��circle

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
     * �Զ���ͼ��
     * @return �Զ���ͼ��
     */
    public boolean isCustomChart() {
        return false;
    }

    /**
     * ����ChartData���ɶ�Ӧ��Area3DPlotGlyph.
     * @param chartData ͼ����ص�����
     * @return ��ͼ������
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
     * ��ͼ���������õ�plot glyph��
     * @param plotGlyph  plot����glyph
     * @param chartData ͼ������.
     */
    public void install4PlotGlyph(VanChartRadarPlotGlyph plotGlyph, ChartData chartData) {
        super.install4PlotGlyph(plotGlyph, chartData);
        plotGlyph.setRadarType(radarType);
        plotGlyph.setColumnType(isStackChart());
    }

    /**
     * ��ʼ������������
     * @param plotGlyph ��ͼ��
     * @param chartData ͼ������
     */
    public void installAxisGlyph(VanChartRadarPlotGlyph plotGlyph, ChartData chartData) {
        VanChartRadarAxisGlyph radarAxisGlyph = new VanChartRadarAxisGlyph(chartData, getDefaultXAxis(),
                (VanChartValueAxis)getDefaultYAxis(), getRadarType());
        plotGlyph.setRadarAxisGlyph(radarAxisGlyph);
        // ��߰��״�ͼ��������ŵ�rectangle��list��ԭ���ǣ�
        // rectangle����ö������Ҫ��ȡ�����������Ϣ�����Ǵ�list����ȡ��Ӧ��������
        plotGlyph.addXAxisGlyph(radarAxisGlyph.getDataSeriesCateAxisGlyph());
        plotGlyph.addYAxisGlyph(radarAxisGlyph.getDataSeriesValueAxisGlyph());
    }

    //����ǻ���ǩ��ʱ���õ��ģ���ǩ��ʱ��ȡϵ��ɫ��
    protected void setSeriesColor(VanChartDataPoint dataPoint, VanChartDataSeries dataSeries){
        if(isNormalChart()){//�����״�ͼ�����ݵ��colorû�����壬ϵ�е�color��������
            dataPoint.setColor(dataSeries.getColor());
        }
    }

    //����series��bands����
    protected void dealDataSeriesBands(VanChartDataSeries dataSeries, ConditionCollection conditionCollection){
    }

    private static final String[] CATE_NAME = {"speed", "judgment", "calculation", "precision", "observation", "memory"};
    private static final Object[] SERIES_NAME = {"michael", "Hepburn"};
    private static final Object[][] VALUE = {{"40", "45", "35", "55", "35", "45"}, {"35", "40", "35", "45", "33", "37"}};
    private static final NormalChartData RADAR_DATA = new NormalChartData(CATE_NAME, SERIES_NAME, VALUE);


    /**
     * ���ͼ��ʱ, ȡ�õ�Ĭ��ͼ������
     * @return Ĭ������
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
     * �Ƿ�֧�������� ����false Ĭ�ϲ�֧��
     * @return  �Ƿ�֧��������.
     */
    public boolean isSupportTrendLine() {
        return false;
    }

    /**
     * �Ƿ�֧�����ݱ�.
     * @return  Ĭ�ϲ�֧�����ݱ�.
     */
    public boolean isSupportDataSheet() {
        return false;
    }

    /**
     * �Ƿ�֧�ַ����������. Ĭ�ϲ�֧�� (����֧��: ��ά����, ����, ��ά���, ���ͼ, �ɼ�ͼ)
     * @return  Ĭ�ϲ�֧������������.
     */
    public boolean isSupportZoomCategoryAxis() {
        return false;
    }

    /**
     * �Ƿ�֧�����ŷ�������
     * @return Ĭ�ϲ�֧��
     */
    public boolean isSupportZoomDirection() {
        return false;
    }

    /**
     *  �Ƿ�֧��Ĭ�ϱ߿�
     *  @return ���ز�֧�ֱ߿�
     *  */
    public boolean isSupportBorder() {
        return false;
    }

    /**
     * ��ѡ�����ͽ���ʱ �Ƚϴ��µ�Plot����.
     * @param newPlot  ��plot
     *                 @return  �ж��Ƿ����.
     */
    public boolean matchPlotType(Plot newPlot) {
        return newPlot instanceof VanChartRadarPlot;
    }

    @Override
    public String getPlotID() {
        return "VanChartRadarPlot";
    }

    /**
     * ����Plot����������,
     *
     * @return ��������.
     */
    public String getPlotName() {
        return Inter.getLocText("Plugin-ChartF_NewRadar");
    }

    /**
     * �ж�ͼ�������Ƿ���obClass
     * @param obClass �������
     * @return �Ƿ���obClass����
     */
    public boolean accept(Class<? extends Plot> obClass) {
        return ComparatorUtils.equals(obClass, VanChartRadarPlot.class);
    }

    public FunctionProcessor getFunctionToRecord() {
        return ChartFunctionProcessor.RADAR_VAN_CHART;
    }
}
