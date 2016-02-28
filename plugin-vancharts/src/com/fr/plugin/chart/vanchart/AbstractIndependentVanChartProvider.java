package com.fr.plugin.chart.vanchart;

import com.fr.base.ChartPreStyleManagerProvider;
import com.fr.base.ChartPreStyleServerManager;
import com.fr.base.Utils;
import com.fr.chart.base.*;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.fun.impl.AbstractIndependentChartProvider;
import com.fr.data.DataConstants;
import com.fr.data.condition.CommonCondition;
import com.fr.data.condition.ListCondition;
import com.fr.data.core.Compare;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;
import com.fr.plugin.chart.base.AttrSeriesStackAndAxis;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.stable.Constants;

import java.awt.*;

/**
 * 图表新特性创建默认图表都要继承这个类
 */
public abstract class AbstractIndependentVanChartProvider extends AbstractIndependentChartProvider {

    public String getWrapperName(){
        return "VanChartWrapper";
    }

    public String[] getRequiredJS(){
        return getVanChartJS();
    }

    protected static String[] getVanChartJS(){
        return new String[]{
                "/com/fr/web/core/js/d3.js",
                "/com/fr/web/core/js/vancharts-all.js",
                "/com/fr/web/core/js/VanChartWrapper.js"
        };
    }

    /**
     * 配色：新特性、风格：渐变
     * @param plot 图表
     */
    protected static void createDefaultPlotStyleAttr(Plot plot){
        plot.setPlotStyle(ChartConstants.STYLE_SHADE);

        ChartPreStyleManagerProvider manager = ChartPreStyleServerManager.getProviderInstance();
        Object preStyle = manager.getPreStyle(Inter.getLocText("Plugin-ChartF_New"));
        if(preStyle != null){
            AttrFillStyle attrFillStyle = ((ChartPreStyle) preStyle).getAttrFillStyle();
            attrFillStyle.setFillStyleName(Utils.objectToString(Inter.getLocText("Plugin-ChartF_New")));
            plot.setPlotFillStyle(attrFillStyle);
        }
    }

    //数据点提示
    protected static void createDefaultTooltipCondition(ConditionAttr defaultAttr, VanChartPlot plot) {
        AttrTooltip attrTooltip = (AttrTooltip) defaultAttr.getExisted(AttrTooltip.class);
        if(attrTooltip == null){
            defaultAttr.addDataSeriesCondition(plot.getDefaultAttrTooltip());
        }
    }

    //自定义图表时，堆积和坐标轴 & y轴2
    protected static void createDefaultStackAndAxisCondition(VanChartRectanglePlot plot) {
        plot.getYAxisList().add(VanChartAttrHelper.createDefaultY2Axis());

        ConditionCollection conditionCollection = new ConditionCollection();
        ConditionAttr conditionAttr = new ConditionAttr();
        conditionAttr.setName(Inter.getLocText("Plugin-ChartF_StackAndSeries") + "1");

        ListCondition condition2 = new ListCondition();
        condition2.addJoinCondition(DataConstants.AND, new CommonCondition(ChartConstants.SERIES_INDEX, Compare.EQUALS, new Integer(2)));
        conditionAttr.setCondition(condition2);

        conditionCollection.addConditionAttr(conditionAttr);
        AttrSeriesStackAndAxis seriesStackAndAxis = new AttrSeriesStackAndAxis();
        conditionAttr.addDataSeriesCondition(seriesStackAndAxis);
        seriesStackAndAxis.setXAxisIndex(0);
        seriesStackAndAxis.setYAxisIndex(1);

        plot.setStackAndAxisCondition(conditionCollection);
    }

    //系列边框
    protected static void createDefaultSeriesBorderCondition(ConditionAttr defaultAttr) {
        AttrBorder attrBorder = (AttrBorder) defaultAttr.getExisted(AttrBorder.class);
        if(attrBorder == null){
            attrBorder = new AttrBorder(Color.white, Constants.LINE_THIN);
            defaultAttr.addDataSeriesCondition(attrBorder);
        }
    }

    //不透明度
    protected static void createDefaultSeriesAlphaCondition(ConditionAttr defaultAttr) {
        AttrAlpha attrAlpha = (AttrAlpha) defaultAttr.getExisted(AttrAlpha.class);
        if(attrAlpha == null) {
            attrAlpha = new AttrAlpha();
            defaultAttr.addDataSeriesCondition(attrAlpha);
        }
    }

    //百分比堆积图时，将y轴（条形图是x轴）设置为百分比，格式为#.##%
    protected static void setDefaultAxisPercentAndFormat(VanChartAxis axis){
        if(axis.getFormat() == null){
            axis.setFormat(VanChartAttrHelper.PERCENT_FORMAT);
        }
        axis.setPercentage(true);
    }

}
