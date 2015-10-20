package com.fr.design.mainframe.chart.gui.type;

import java.util.ArrayList;
import java.util.List;

import com.fr.chart.chartattr.BubblePlot;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.BubbleIndependentChart;
import com.fr.general.Inter;

/**
 * 气泡图 属性表 选择类型 布局界面.
 * 
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-25 下午06:56:47
 */
public class BubblePlotPane extends AbstractChartTypePane {
	private static final long serialVersionUID = -601566194238908115L;

	private static final int BUBBLE_CHART = 0;

	@Override
	protected List<ChartImagePane> initDemoList() {
		List<ChartImagePane> demoList = new ArrayList<ChartImagePane>();
		ChartImagePane pane = new ChartImagePane(getTypeIconPath()[0], Inter.getLocText("FR-Chart-Chart_BubbleChart"), true);
		pane.isPressing = true;
		demoList.add(pane);
		return demoList;
	}

	@Override
	protected List<ChartImagePane> initStyleList() {
		return new ArrayList<ChartImagePane>();
	}

	@Override
	protected String[] getTypeIconPath() {
		return new String[]{"/com/fr/design/images/chart/BubblePlot/type/0.png",};
	}

	@Override
	protected String[] getTypeLayoutPath() {
		return new String[]{"/com/fr/design/images/chart/BubblePlot/layout/0.png",
                "/com/fr/design/images/chart/BubblePlot/layout/1.png",
                "/com/fr/design/images/chart/BubblePlot/layout/2.png",
                "/com/fr/design/images/chart/BubblePlot/layout/3.png",};
	}

	/**
	 * 界面标题
     * @return 界面标题
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("FR-Chart-Chart_BubbleChart");
	}

	/**
	 * 保存界面属性
	 */
	public void updateBean(Chart chart) {
        if(needsResetChart(chart)){
            resetChart(chart);
        }

		BubblePlot plot = new BubblePlot();
		chart.switchPlot(plot);
	}

	/**
	 * 更新界面内容
	 */
	public void populateBean(Chart chart) {
		typeDemo.get(0).isPressing = true;
		
		checkDemosBackground();
	}

	/**
	 * 判断界面是否进入
     * @param ob 传入的对象
     * @return 是否对象是chart
	 */
	public boolean accept(Object ob) {
		if (!super.accept(ob)) {
			return false;
		}
		Chart chart = (Chart) ob;
		Plot plot = chart.getPlot();
		return (plot instanceof BubblePlot);
	}


    public Chart getDefaultChart() {
        return BubbleIndependentChart.bubbleChartTypes[0];
    }
}
