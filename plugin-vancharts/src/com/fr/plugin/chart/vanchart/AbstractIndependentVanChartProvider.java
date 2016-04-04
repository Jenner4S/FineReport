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
 * ͼ�������Դ���Ĭ��ͼ��Ҫ�̳������
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
     * ��ɫ�������ԡ���񣺽���
     * @param plot ͼ��
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

    //���ݵ���ʾ
    protected static void createDefaultTooltipCondition(ConditionAttr defaultAttr, VanChartPlot plot) {
        AttrTooltip attrTooltip = (AttrTooltip) defaultAttr.getExisted(AttrTooltip.class);
        if(attrTooltip == null){
            defaultAttr.addDataSeriesCondition(plot.getDefaultAttrTooltip());
        }
    }

    //�Զ���ͼ��ʱ���ѻ��������� & y��2
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

    //ϵ�б߿�
    protected static void createDefaultSeriesBorderCondition(ConditionAttr defaultAttr) {
        AttrBorder attrBorder = (AttrBorder) defaultAttr.getExisted(AttrBorder.class);
        if(attrBorder == null){
            attrBorder = new AttrBorder(Color.white, Constants.LINE_THIN);
            defaultAttr.addDataSeriesCondition(attrBorder);
        }
    }

    //��͸����
    protected static void createDefaultSeriesAlphaCondition(ConditionAttr defaultAttr) {
        AttrAlpha attrAlpha = (AttrAlpha) defaultAttr.getExisted(AttrAlpha.class);
        if(attrAlpha == null) {
            attrAlpha = new AttrAlpha();
            defaultAttr.addDataSeriesCondition(attrAlpha);
        }
    }

    //�ٷֱȶѻ�ͼʱ����y�ᣨ����ͼ��x�ᣩ����Ϊ�ٷֱȣ���ʽΪ#.##%
    protected static void setDefaultAxisPercentAndFormat(VanChartAxis axis){
        if(axis.getFormat() == null){
            axis.setFormat(VanChartAttrHelper.PERCENT_FORMAT);
        }
        axis.setPercentage(true);
    }

}
