package com.fr.plugin.chart.designer.other;

import com.fr.chart.chartattr.Plot;
import com.fr.chart.web.ChartHyperPoplink;
import com.fr.chart.web.ChartHyperRelateCellLink;
import com.fr.chart.web.ChartHyperRelateFloatLink;
import com.fr.design.chart.javascript.ChartEmailPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperPoplinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperRelateCellLinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperRelateFloatLinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.FormHyperlinkPane;
import com.fr.design.hyperlink.ReportletHyperlinkPane;
import com.fr.design.hyperlink.WebHyperlinkPane;
import com.fr.design.javascript.JavaScriptImplPane;
import com.fr.design.javascript.ParameterJavaScriptPane;
import com.fr.js.*;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;

import java.util.HashMap;


/**
 * 根据plot返回不同的超链的map
 */

public class HyperlinkMapFactory {
    private static final String NORMAL = "$CHART";
    private static final String GAUGE = "$CHART_METER";

    private static HashMap<String, String> plotTypeMap = new HashMap<String, String>();

    static {
        plotTypeMap.put(VanChartGaugePlot.class.getName(), GAUGE);
    }

    public static HashMap getHyperlinkMap(Plot plot) {
        String plotType = plotTypeMap.get(plot.getClass().getName());
        if(plotType == null){
            plotType = NORMAL;
        }
        HashMap<Class, Class> map = new HashMap<Class, Class>();

        map.put(ReportletHyperlink.class, getClassWithPrefix(ReportletHyperlinkPane.class, plotType));
        map.put(EmailJavaScript.class, ChartEmailPane.class);
        map.put(WebHyperlink.class, getClassWithPrefix(WebHyperlinkPane.class, plotType));
        map.put(ParameterJavaScript.class, getClassWithPrefix(ParameterJavaScriptPane.class, plotType));

        map.put(JavaScriptImpl.class, getClassWithPrefix(JavaScriptImplPane.class, plotType));
        map.put(ChartHyperPoplink.class, getClassWithPrefix(ChartHyperPoplinkPane.class, plotType));
        map.put(ChartHyperRelateCellLink.class, getClassWithPrefix(ChartHyperRelateCellLinkPane.class, plotType));
        map.put(ChartHyperRelateFloatLink.class, getClassWithPrefix(ChartHyperRelateFloatLinkPane.class, plotType));

        map.put(FormHyperlinkProvider.class, getClassWithPrefix(FormHyperlinkPane.class, plotType));
        return map;
    }

    private static Class getClassWithPrefix(Class superClass, String plotType){
        String wholeClassString = superClass.getName() + plotType;
        try{
            return Class.forName(wholeClassString);
        } catch (Exception e) {
            return superClass;
        }
    }

}
