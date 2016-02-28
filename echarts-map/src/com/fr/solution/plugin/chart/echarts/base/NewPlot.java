package com.fr.solution.plugin.chart.echarts.base;

import com.fr.base.chart.chartdata.ChartData;
import com.fr.chart.chartattr.Plot;
import com.fr.general.ComparatorUtils;

/**
 * Created by richie on 16/2/2.
 */
public abstract class NewPlot extends Plot {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2730862489936465038L;
	public NewTitleGlyph title;
    public boolean accept(Class<? extends Plot> obClass) {
        return ComparatorUtils.equals(NewPlot.class, obClass);
    }
    
    public abstract NewSeriesGlyph createChartSeriesGlyph(ChartData chartData) ;

    public abstract NewTitleGlyph createChartTitleGlyph(ChartData chartData) ;/*{
    	
        return new NewTitleGlyph("iPhone", "iPhone");
    }*/

    public abstract NewLegendGlyph createChartLegendGlyph(ChartData chartData); /*{
        return new NewLegendGlyph();
    }*/

}
