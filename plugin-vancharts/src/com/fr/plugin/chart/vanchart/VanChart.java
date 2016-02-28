package com.fr.plugin.chart.vanchart;

import com.fr.base.FRContext;
import com.fr.base.background.ImageBackground;
import com.fr.base.chart.BaseChartGlyph;
import com.fr.base.chart.chartdata.ChartData;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartXMLCompatibleUtils;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.chart.chartglyph.ChartGlyph;
import com.fr.chart.chartglyph.GeneralInfo;
import com.fr.chart.chartglyph.PlotGlyph;
import com.fr.general.Background;
import com.fr.general.ComparatorUtils;
import com.fr.general.data.MOD_COLUMN_ROW;
import com.fr.general.xml.GeneralXMLTools;
import com.fr.plugin.chart.attr.VanChartTitle;
import com.fr.plugin.chart.attr.VanChartTools;
import com.fr.plugin.chart.attr.VanChartZoom;
import com.fr.plugin.chart.data.VanChartMoreNameCDDefinition;
import com.fr.plugin.chart.data.VanChartNormalReportDataDefinition;
import com.fr.plugin.chart.data.VanChartOneValueCDDefinition;
import com.fr.plugin.chart.glyph.VanChartDataSheetGlyph;
import com.fr.plugin.chart.glyph.VanChartLegendGlyph;
import com.fr.plugin.chart.glyph.VanChartTitleGlyph;
import com.fr.script.Calculator;
import com.fr.stable.Constants;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLableReader;

import java.awt.*;

/**
 * Created by Mitisky on 15/8/14.
 */
public class VanChart extends Chart {
    private static final long serialVersionUID = 8606596488760033141L;

    private VanChartTools vanChartTools = new VanChartTools();
    private VanChartZoom vanChartZoom = new VanChartZoom();
    
    
    public VanChart() {
        setTitle(new VanChartTitle());
        this.setBorderColor(new Color(238, 238, 238));
        this.setBorderStyle(Constants.LINE_NONE);
        setWrapperName("VanChartWrapper");
    }

    public VanChart(Plot plot) {
        setPlot(plot);
        setTitle(new VanChartTitle());
        this.setBorderColor(new Color(238, 238, 238));
        this.setBorderStyle(Constants.LINE_NONE);
        setWrapperName("VanChartWrapper");
    }

    public void setVanChartTools(VanChartTools vanChartTools) {
        this.vanChartTools = vanChartTools;
    }

    public VanChartTools getVanChartTools() {
        return vanChartTools;
    }

    public void setVanChartZoom(VanChartZoom vanChartZoom) {
        this.vanChartZoom = vanChartZoom;
    }

    public VanChartZoom getVanChartZoom() {
        return vanChartZoom;
    }

    /**
     * ����ChartGlyph�����������(TitleGlyph)����ͼ��(PlotGlyph)��ͼ��(LegendGlyph)����
     * @param chartData ����ChartGlyph���õ�ͼ������
     * @return ChartGlyph  ����ͼ��Glyph
     */
    public BaseChartGlyph createGlyph(ChartData chartData) {
        VanChartGlyph chartGlyph = new VanChartGlyph();

        chartGlyph.setGeneralInfo(this);
        chartGlyph.setVanChartTools(vanChartTools);
        if(getPlot().isSupportZoomDirection()){
            chartGlyph.setVanChartZoom(vanChartZoom);
        }
        chartGlyph.setJSDraw(isJSDraw());

        createTitleGlyph4ChartGlyph(chartGlyph);
        PlotGlyph plotGlyph = createPlotGlyph4ChartGlyph(chartData, chartGlyph);
        createLegendGlyph(chartGlyph, plotGlyph);
        createDataSheetGlyph(chartGlyph, plotGlyph);

        chartGlyph.setWrapperName(this.getWrapperName());
        chartGlyph.setRequiredJS(this.getRequiredJS());

        return chartGlyph;
    }

    private void createTitleGlyph4ChartGlyph(ChartGlyph chartGlyph) {
        if (getTitle() == null) {
            setTitle(new VanChartTitle());
        }
        try {
            VanChartTitleGlyph titleGlyph = (VanChartTitleGlyph)getTitle().createGlyph();
            chartGlyph.setTitleGlyph(titleGlyph);
        } catch (Exception e) {
            chartGlyph.setTitleGlyph(new VanChartTitleGlyph());
            String message = "Error happens at Chart Title." + "\nerror message is " + e.getMessage();
            FRContext.getLogger().error(message, e);
        }
    }

    private PlotGlyph createPlotGlyph4ChartGlyph(ChartData chartData, ChartGlyph chartGlyph) {
        PlotGlyph plotGlyph = null;
        try {
            if(getPlot() != null){
                plotGlyph = getPlot().createPlotGlyph(chartData);
                chartGlyph.setPlotGlyph(plotGlyph);
            }
        } catch (Exception e) {
            // �״���Ϣ
            chartGlyph.setPlotGlyph(null);
            String message = "Error happens at Chart Plot." + "\nerror message is " + e.getMessage();
            FRContext.getLogger().error(message, e);
        }
        return plotGlyph;
    }

    private void createLegendGlyph(ChartGlyph chartGlyph, PlotGlyph plotGlyph) {
        if(getPlot() != null) {
            VanChartLegendGlyph legendGlyph = (VanChartLegendGlyph)getPlot().createLegendGlyph(plotGlyph);
            chartGlyph.setLegendGlyph(legendGlyph);
        }
    }

    private void createDataSheetGlyph(ChartGlyph chartGlyph, PlotGlyph plotGlyph) {
        try {
            if(getPlot() != null){
                VanChartDataSheetGlyph dataSheetGlyph = (VanChartDataSheetGlyph)getPlot().createDataSheetGlyph(plotGlyph);
                chartGlyph.setDataSheetGlyph(dataSheetGlyph);
            }
        } catch (Exception e) {
            String message = "Error happens at Chart Legend or DataSheet." + "\nerror message is " + e.getMessage();
            FRContext.getLogger().error(message, e);
        }
    }


    @Override
    protected void readChartXML(XMLableReader reader) {
        if (reader.isChildNode()) {
            String tmpNodeName = reader.getTagName();

            if (tmpNodeName.equals(VanChartTitle.XML_TAG)) {
                setTitle(new VanChartTitle());
                reader.readXMLObject(getTitle());
            } else if (tmpNodeName.equals(Plot.XML_TAG)) {
                setPlot((Plot) GeneralXMLTools.readXMLable(reader));
            } else if (tmpNodeName.equals("ChartAttr")) {
                this.setJSDraw(reader.getAttrAsBoolean("isJSDraw", true));
                this.setStyleGlobal(reader.getAttrAsBoolean("isStyleGlobal",false));
            } else if(ComparatorUtils.equals(tmpNodeName, "ChartDefinition")) {
                reader.readXMLObject(new XMLReadable() {
                    public void readXML(XMLableReader reader) {
                        setFilterDefinition(readDefinition(reader));
                    }
                });
            } else if(tmpNodeName.equals(VanChartTools.XML_TAG)){
                setVanChartTools((VanChartTools) reader.readXMLObject(new VanChartTools()));
                changeImageLayout(this);
                changeImageLayout(this.getPlot());
            } else if(tmpNodeName.equals(VanChartZoom.XML_TAG)) {
                setVanChartZoom((VanChartZoom)reader.readXMLObject(new VanChartZoom()));
            }
        }
    }

    /**
     * ���ڶ�ȡ��ͬ��FilterDefinition.
     * @param reader XML��ȡ��
     * @return TopDefinition ��ȡ��Top���ݶ���
     */
    public static TopDefinition readDefinition(XMLableReader reader) {
        TopDefinition filterDefinition;
        String tmpNodeName = reader.getTagName();
        if (VanChartOneValueCDDefinition.XML_TAG.equals(tmpNodeName)) {
            filterDefinition = new VanChartOneValueCDDefinition();
        } else if (VanChartMoreNameCDDefinition.XML_TAG.equals(tmpNodeName)) {
            filterDefinition = new VanChartMoreNameCDDefinition();
        } else if (VanChartNormalReportDataDefinition.XML_TAG.equals(tmpNodeName)) {
            filterDefinition = new VanChartNormalReportDataDefinition();
        }else {
            return ChartXMLCompatibleUtils.readDefinition(reader);
        }

        reader.readXMLObject(filterDefinition);
        return filterDefinition;
    }

    //֮ǰͼƬ�������в���ѡ��ģ�ǰ̨�Ǳ�ȫ������������죬���Ժ�̨���ȥ���˲���ѡ��ͳһ�����졣
    private void changeImageLayout(GeneralInfo generalInfo) {
        if(generalInfo == null){
            return;
        }
        Background background = generalInfo.getBackground();
        if(background instanceof ImageBackground){
            ((ImageBackground) background).setLayout(Constants.IMAGE_EXTEND);
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        super.writeXML(writer);
        this.vanChartTools.writeXML(writer);
        if(this.vanChartZoom != null){
            this.vanChartZoom.writeXML(writer);
        }
    }

    /**
     * ��¡
     * @return ���ؿ�¡�����Object
     */
    public Object clone() throws CloneNotSupportedException {
        VanChart newChart = (VanChart)super.clone();

        newChart.setVanChartZoom((VanChartZoom)this.getVanChartZoom().clone());
        newChart.setVanChartTools((VanChartTools)this.getVanChartTools().clone());

        return newChart;
    }

    /**
     * SE �д���ʽ���
     * @param calculator SE�еĹ�ʽ������
     */
    public void dealFormula(Calculator calculator) {
        super.dealFormula(calculator);
        if(vanChartZoom != null){
            vanChartZoom.dealFormula(calculator);
        }
    }

    /**
     * Ԥ�ȼ���ۺ�ͼ�� ��乫ʽ˳��.
     * @param list ��¼���˳���б�
     * @param calculator ������
     */
    public void buidExecuteSequenceList(java.util.List list, Calculator calculator) {
        super.buidExecuteSequenceList(list, calculator);
        if(vanChartZoom != null){
            vanChartZoom.buidExecuteSequenceList(list, calculator);
        }
    }

    /**
     * ����ɾ������ʱ ͼ���еĹ�ʽ ����
     * @param mod ִ�й�ʽ�����Ķ���
     */
    public void modFormulaString(MOD_COLUMN_ROW mod) {
        super.modFormulaString(mod);
        if(vanChartZoom != null){
            vanChartZoom.modFormulaString(mod);
        }
    }

    /**
     * �ȽϺ�Object�Ƿ����
     * @param ob �� �ڱȽϵ�Object
     * @return һ��booleanֵ
     */
    public boolean equals(Object ob) {
        return ob instanceof VanChart && super.equals(ob)
                && ComparatorUtils.equals(((VanChart) ob).getVanChartTools(), getVanChartTools())
                && ComparatorUtils.equals(((VanChart) ob).getVanChartZoom(), getVanChartZoom());
    }

    /**
     * �ж�ͼ�������Ƿ���obClass
     * @param obClass �������
     * @return �Ƿ���obClass����
     */
    public boolean accept(Class<? extends Chart> obClass){
        return ComparatorUtils.equals(VanChart.class, obClass);
    }
}
