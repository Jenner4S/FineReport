package com.fr.design.mainframe.chart.gui.type;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

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


public class GisMapPlotPane extends AbstractChartTypePane{
	
	private static final long serialVersionUID = 2595221900000305396L;
	
	private static final int GISMAP = 0;
	
	private UITextField keyInput;
	
	public GisMapPlotPane(){
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
        Component[][] components = null;

		styleList = initStyleList();
		
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
	protected List<ChartImagePane> initDemoList() {
		List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
		ChartImagePane pane = new ChartImagePane(getTypeIconPath()[0], "gis"+Inter.getLocText("FR-Chart-Map_Map"), true);
		pane.isPressing = true;
		demoList.add(pane);
		return demoList;
	}

	/**
	 * ����
	 */
	protected List<ChartImagePane> initStyleList() {
		List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
		String baiduMap = Inter.getLocText("FR-Chart-Type_BaiduMap");
		String googleMap = Inter.getLocText("FR-Chart-Map_GoogleMap");
		String[] layoutPaths = getTypeLayoutPath();
		ChartImagePane pane = new ChartImagePane(layoutPaths[0], baiduMap);
		pane.isPressing = true;
		demoList.add(pane);
		demoList.add(new ChartImagePane(layoutPaths[1], googleMap));
		return demoList;
	}

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/GisMapPlot/type/0.png",
        };
    }

    @Override
    protected String[] getTypeLayoutPath() {
        return new String[]{"/com/fr/design/images/chart/GisMapPlot/layout/0.png",
                "/com/fr/design/images/chart/GisMapPlot/layout/1.png",
        };
    }

	/**
	 * �����������
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
	 * ���½�������
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
     * �����Ƿ����
     * @param ob ����
     * @return  true��ʾ����
     */
	public boolean accept(Object ob) {
		if(!super.accept(ob)) {
			return false;
		}
		Chart chart = (Chart)ob;
		Plot plot = chart.getPlot();
		return (plot instanceof GisMapPlot);
	}


    /**
     * �������
     * @return ����
     */
	public String title4PopupWindow() {
		return "gis"+Inter.getLocText("FR-Chart-Map_Map");
	}

    /**
     * �Ƿ���������
     * @return û��������
     */
	public boolean isHaveAxis() {
    	return false;
    }


    public Chart getDefaultChart() {
        return GisMapIndependentChart.gisChartTypes[0];
    }
}
