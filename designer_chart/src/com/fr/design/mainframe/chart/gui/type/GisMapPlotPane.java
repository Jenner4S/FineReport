package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.GisMapPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.GisMapIndependentChart;
import com.fr.design.chart.series.PlotStyle.ChartSelectDemoPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.FRLogger;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;


public class GisMapPlotPane extends AbstractChartTypePane{
	
	private static final long serialVersionUID = 2595221900000305396L;
	
	private static final int GISMAP = 0;
	
	private UITextField keyInput;
	
	public GisMapPlotPane(){
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
        Component[][] components = null;

		styleList = createStyleList();
		
		checkDemosBackground();

		JPanel layoutPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(4);
		if(styleList != null && !styleList.isEmpty()) {
			for(int i = 0; i < styleList.size(); i ++) {
				ChartImagePane tmp = styleList.get(i);
				layoutPane.add(tmp);
				tmp.setDemoGroup(styleList.toArray(new ChartSelectDemoPane[styleList.size()]));
			}
		}
		
		keyInput = new UITextField();

        double[] columnSize = { f };
		double[] rowSize = { p,p,p,p};

		if(styleList != null && !styleList.isEmpty()) {
			components = new Component[][]{
					new Component[]{layoutPane},
					new Component[]{new UILabel(Inter.getLocText("FR-Chart-Waring_Please_Input_The_Key"))},
					new Component[]{keyInput},
			};
		}
		
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
	
	}

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/GisMapPlot/type/0.png",
        };
    }

	@Override
	protected String[] getTypeTipName() {
		return new String[]{
				"gis"+Inter.getLocText("FR-Chart-Map_Map")
		};
	}

	@Override
	protected String getPlotTypeID() {
		return ChartConstants.GIS_CHAER;
	}

    protected String[] getTypeLayoutPath() {
        return new String[]{"/com/fr/design/images/chart/GisMapPlot/layout/0.png",
                "/com/fr/design/images/chart/GisMapPlot/layout/1.png",
        };
    }

	@Override
	protected String[] getTypeLayoutTipName() {
		return new String[]{
				Inter.getLocText("FR-Chart-Type_BaiduMap"),
				Inter.getLocText("FR-Chart-Map_GoogleMap")
		};
	}

	/**
	 * 保存界面属性
	 */
	public void updateBean(Chart chart) {
        if(needsResetChart(chart)){
            resetChart(chart);
        }

		Chart[] cs = GisMapIndependentChart.gisChartTypes;
		GisMapPlot plot;
		if (cs.length > 0) {
			try {
				plot = (GisMapPlot)cs[0].getPlot().clone();
			} catch (Exception e) {
				plot = new GisMapPlot();
			}
		} else {
			plot = new GisMapPlot();
		}
		
		try {
			chart.switchPlot((Plot)plot.clone());
		} catch (CloneNotSupportedException e) {
			FRLogger.getLogger().error("Error In LineChart");
			chart.switchPlot(new GisMapPlot());
		}
		
		plot = (GisMapPlot) chart.getPlot();
		boolean index = plot.isGisType();
		if(styleList.get(BAIDU).isPressing){
			plot.setGisType(true);
		}else{
			plot.setGisType(false);
		}
		
		if(index != plot.isGisType()){
			if(plot.isGisType()){
				this.keyInput.setText(plot.getBaiduKey());
			}else{
				this.keyInput.setText(plot.getGoogleKey());
			}
		}else{
			String key = this.keyInput.getText().trim();
			if(plot.isGisType() && key != plot.getBaiduKey()){
				plot.setBaiduKey(key);
			}else if(!plot.isGisType() && key != plot.getGoogleKey()){
				plot.setGoogleKey(key);
			}
		}
	}

	/**
	 * 更新界面内容
	 */
	public void populateBean(Chart chart) {
		typeDemo.get(0).isPressing = true; 
		GisMapPlot plot = (GisMapPlot) chart.getPlot();

		if(plot.isGisType()){
			styleList.get(BAIDU).isPressing = true;
            styleList.get(GOOGLE).isPressing = false;
			keyInput.setText(plot.getBaiduKey());
			
		}else{
            styleList.get(GOOGLE).isPressing = true;
            styleList.get(BAIDU).isPressing  =false;
			keyInput.setText(plot.getGoogleKey());
		}
        styleList.get(GOOGLE).checkBackground();
        styleList.get(BAIDU).checkBackground();
	}

    /**
     * 界面标题
     * @return 标题
     */
	public String title4PopupWindow() {
		return "gis"+Inter.getLocText("FR-Chart-Map_Map");
	}

    /**
     * 是否有坐标轴
     * @return 没有坐标轴
     */
	public boolean isHaveAxis() {
    	return false;
    }


    public Chart getDefaultChart() {
        return GisMapIndependentChart.gisChartTypes[0];
    }
}