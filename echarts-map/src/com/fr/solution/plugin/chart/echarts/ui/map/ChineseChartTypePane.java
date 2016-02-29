package com.fr.solution.plugin.chart.echarts.ui.map;

import java.util.ArrayList;
import java.util.List;

import com.fr.chart.base.AttrFillStyle;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Legend;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.chartglyph.DataSheet;
import com.fr.design.mainframe.chart.gui.type.ChartImagePane;
import com.fr.design.mainframe.chart.gui.type.UserDefinedChartTypePane;
import com.fr.general.Background;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.js.NameJavaScriptGroup;
import com.fr.plugin.chart.PiePlot4VanChart;
import com.fr.solution.plugin.chart.echarts.ChineseMap;
import com.fr.solution.plugin.chart.echarts.base.ChartMapType;
import com.fr.solution.plugin.chart.echarts.base.NewChart;
import com.fr.solution.plugin.chart.echarts.core.map.ChineseMapPlot;

/**
 * Created by richie on 16/1/29.
 */
public class ChineseChartTypePane extends UserDefinedChartTypePane {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1452755307696221957L;

	@Override
    protected List<ChartImagePane> initDemoList() {
        List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
        String[] iconPaths = getTypeIconPath();
        ChartImagePane pane = new ChartImagePane(iconPaths[0], Inter.getLocText("Plugin-ECharts_Chinese_Map"));
        pane.isPressing = true;
        demoList.add(pane);
        demoList.add(new ChartImagePane(iconPaths[1], Inter.getLocText("Plugin-ECharts_World_Map"), true));
        demoList.add(new ChartImagePane(iconPaths[2], Inter.getLocText("Plugin-ECharts_Guide_Map"), true));
        return demoList;
    }
	@Override
    protected List<ChartImagePane> initStyleList() {
        return new ArrayList<ChartImagePane>();
    }
	 @Override
	    protected String[] getTypeLayoutPath() {
	        return new String[]{
	        };
	    }
	  /**
     * 更新界面内容
     */
    public void populateBean(Chart chart) {
        for(ChartImagePane imagePane : typeDemo) {
            imagePane.isPressing = false;
        }
        Plot plot = chart.getPlot();
        if(plot instanceof ChineseMapPlot) {
            lastTypeIndex = ((ChineseMapPlot)plot).getType().ordinal();
            typeDemo.get(lastTypeIndex).isPressing = true;
        }
        checkDemosBackground();
    }
	
	
	
    /**
     * 弹出框的标题
     *
     * @return 弹出框的标题
     */
    public String title4PopupWindow() {
        return Inter.getLocText("Plugin-ECharts_Chinese_Map");
        
    }

    public void updateBean(Chart chart) {
    	super.updateBean(chart);
    	  checkTypeChange();
          for(int i = 0; i < typeDemo.size(); i++){
              if(typeDemo.get(i).isPressing && i != lastTypeIndex){
                  NewChart chart2 = ChineseMap.charts[i];
//                  ChartMapType type = ( (NewChart)chart2).getMaptype();
//                  ( (NewChart)chart).setMaptype(type);
                  chart.setPlot(chart2.getPlot());
                  break;
              }
          }
    }
    
    
    /**
     * 界面是否接受
     * @param ob 对象是否为chart
     * @return 界面是否接受对象
     */
    public boolean accept(Object ob) {
        return (ob instanceof Chart)&&((Chart) ob).getPlot().accept(ChineseMapPlot.class);
    }

    @Override
    public Chart getDefaultChart() {
        return ChineseMap.charts[0];
    }

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{
                "/com/fr/solution/plugin/chart/echarts/images/map/map_type1.png",
                "/com/fr/solution/plugin/chart/echarts/images/map/map_type2.png",//世界地图，
                "/com/fr/solution/plugin/chart/echarts/images/map/map_type3.png"
                
        };
    }
}
