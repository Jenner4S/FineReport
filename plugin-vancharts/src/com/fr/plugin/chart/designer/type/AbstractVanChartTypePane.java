package com.fr.plugin.chart.designer.type;

import com.fr.chart.base.AttrFillStyle;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Legend;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.chartglyph.DataSheet;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.general.Background;
import com.fr.general.FRLogger;
import com.fr.js.NameJavaScriptGroup;
import com.fr.plugin.chart.attr.VanChartZoom;
import com.fr.plugin.chart.vanchart.VanChart;

public abstract class AbstractVanChartTypePane extends AbstractChartTypePane{
    private static final long serialVersionUID = 7743244512351499265L;

    /**
     * 保存界面属性
     */
    public void updateBean(Chart chart) {
        checkTypeChange();
        Plot oldPlot = chart.getPlot();
        Plot newPlot = getSelectedClonedPlot();
        boolean samePlot = accept(chart);
        if(typeChanged && samePlot){
            //同一中图表切换不同类型
            cloneOldPlot2New(oldPlot, newPlot);
            chart.setPlot(newPlot);
        } else if(!samePlot){
            //不同的图表类型切换
            chart.setPlot(newPlot);
            if(newPlot.isSupportZoomDirection() && !newPlot.isSupportZoomCategoryAxis()){
                ((VanChart)chart).setVanChartZoom(new VanChartZoom());
            }
        }
    }

    public boolean accept(Object ob) {
        return ob instanceof VanChart;
    }

    protected void checkTypeChange(){
        for(int i = 0; i < typeDemo.size(); i++){
            if(typeDemo.get(i).isPressing && i != lastTypeIndex){
                typeChanged = true;
                lastTypeIndex = i;
                break;
            }
            typeChanged = false;
        }
    }

    /**
     * 同一个图表， 类型之间切换
     */
    protected void cloneOldPlot2New(Plot oldPlot, Plot newPlot) {
        try {
            if (oldPlot.getLegend() != null) {
                newPlot.setLegend((Legend) oldPlot.getLegend().clone());
            }
            cloneOldConditionCollection(oldPlot, newPlot);
            if (oldPlot.getHotHyperLink() != null) {
                newPlot.setHotHyperLink((NameJavaScriptGroup)oldPlot.getHotHyperLink().clone());
            }
            if (oldPlot.getPlotFillStyle() != null) {
                newPlot.setPlotFillStyle((AttrFillStyle)oldPlot.getPlotFillStyle().clone());
            }
            newPlot.setPlotStyle(oldPlot.getPlotStyle());
            if (oldPlot.getDataSheet() != null) {
                newPlot.setDataSheet((DataSheet)oldPlot.getDataSheet().clone());
            }

            if (oldPlot.getBackground() != null) {
                newPlot.setBackground((Background)oldPlot.getBackground().clone());
            }
            if (oldPlot.getBorderColor() != null) {
                newPlot.setBorderColor(oldPlot.getBorderColor());
            }
            newPlot.setBorderStyle(oldPlot.getBorderStyle());
            newPlot.setRoundRadius(oldPlot.getRoundRadius());
            newPlot.setAlpha(oldPlot.getAlpha());
            newPlot.setShadow(oldPlot.isShadow());

        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error in change plot");
        }
    }

    protected void cloneOldConditionCollection(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException{
        if (oldPlot.getConditionCollection() != null) {
            newPlot.setConditionCollection((ConditionCollection)oldPlot.getConditionCollection().clone());
        }
    }
}
