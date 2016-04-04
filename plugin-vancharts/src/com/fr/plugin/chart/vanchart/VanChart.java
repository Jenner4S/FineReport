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
     * 生成ChartGlyph，包括其标题(TitleGlyph)、绘图区(PlotGlyph)和图例(LegendGlyph)属性
     * @param chartData 生成ChartGlyph所用的图表数据
     * @return ChartGlyph  返回图表Glyph
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
            // 抛错信息
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
     * 用于读取不同的FilterDefinition.
     * @param reader XML读取器
     * @return TopDefinition 读取的Top数据定义
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

    //之前图片背景是有布局选择的，前台那边全部处理成了拉伸，所以后台这边去掉了布局选择，统一成拉伸。
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
     * 克隆
     * @return 返回克隆后的新Object
     */
    public Object clone() throws CloneNotSupportedException {
        VanChart newChart = (VanChart)super.clone();

        newChart.setVanChartZoom((VanChartZoom)this.getVanChartZoom().clone());
        newChart.setVanChartTools((VanChartTools)this.getVanChartTools().clone());

        return newChart;
    }

    /**
     * SE 中处理公式结果
     * @param calculator SE中的公式处理器
     */
    public void dealFormula(Calculator calculator) {
        super.dealFormula(calculator);
        if(vanChartZoom != null){
            vanChartZoom.dealFormula(calculator);
        }
    }

    /**
     * 预先计算聚合图表 表间公式顺序.
     * @param list 记录表间顺序列表
     * @param calculator 计算器
     */
    public void buidExecuteSequenceList(java.util.List list, Calculator calculator) {
        super.buidExecuteSequenceList(list, calculator);
        if(vanChartZoom != null){
            vanChartZoom.buidExecuteSequenceList(list, calculator);
        }
    }

    /**
     * 插入删除行列时 图表中的公式 联动
     * @param mod 执行公式联动的对象
     */
    public void modFormulaString(MOD_COLUMN_ROW mod) {
        super.modFormulaString(mod);
        if(vanChartZoom != null){
            vanChartZoom.modFormulaString(mod);
        }
    }

    /**
     * 比较和Object是否相等
     * @param ob 用 于比较的Object
     * @return 一个boolean值
     */
    public boolean equals(Object ob) {
        return ob instanceof VanChart && super.equals(ob)
                && ComparatorUtils.equals(((VanChart) ob).getVanChartTools(), getVanChartTools())
                && ComparatorUtils.equals(((VanChart) ob).getVanChartZoom(), getVanChartZoom());
    }

    /**
     * 判断图表类型是否是obClass
     * @param obClass 传入对象
     * @return 是否是obClass对象
     */
    public boolean accept(Class<? extends Chart> obClass){
        return ComparatorUtils.equals(VanChart.class, obClass);
    }
}
