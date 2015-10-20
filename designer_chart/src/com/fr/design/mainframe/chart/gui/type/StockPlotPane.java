package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.StockPlot;
import com.fr.chart.charttypes.StockIndependentChart;
import com.fr.general.Inter;

import java.util.ArrayList;
import java.util.List;

/**
 * 股价图 属性表 选择类型 布局 界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-26 上午10:52:36
 */
public class StockPlotPane extends AbstractChartTypePane {
	
	private static final int STOCK = 0;


    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/StockPlot/type/0.png",
        };
    }

    @Override
    protected String[] getTypeLayoutPath() {
        return new String[]{
        };
    }

	@Override
	protected List<ChartImagePane> initDemoList() {
		List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
		ChartImagePane pane = new ChartImagePane(getTypeIconPath()[0], Inter.getLocText("FR-Chart-Stock_Chart"), true);
		pane.isPressing = true;
		demoList.add(pane);
		return demoList;
	}

	@Override
	protected List<ChartImagePane> initStyleList() {
		return new ArrayList<ChartImagePane>();
	}

	/**
	 * 界面标题
     * @return 界面标题
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("FR-Chart-Stock_Chart");
	}

	/**
	 * 保存界面属性
	 */
	public void updateBean(Chart chart) {
        if(needsResetChart(chart)){
            resetChart(chart);
        }
        changePlotWithClone(chart, StockIndependentChart.createStockChart().getPlot());
	}

	/**
	 * 更新界面内容
	 */
	public void populateBean(Chart chart) {
		typeDemo.get(STOCK).isPressing = true; 
		
		checkDemosBackground();
	}

    /**
     * 界面是否接受
     * @param ob 传入的对象
     * @return 是否是chart对象
     */
	public boolean accept(Object ob) {
		return super.accept(ob) && ((Chart) ob).getPlot() instanceof StockPlot;
	}


    public Chart getDefaultChart() {
        return StockIndependentChart.stockChartTypes[0];
    }
}
