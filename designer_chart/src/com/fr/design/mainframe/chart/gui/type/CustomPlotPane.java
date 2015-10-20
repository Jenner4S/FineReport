package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.CustomPlot;
import com.fr.chart.charttypes.CustomIndependentChart;
import com.fr.general.Inter;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合图 属性表 图表类型 界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-21 下午06:17:40
 */
public class CustomPlotPane extends AbstractChartTypePane {
	
	@Override
	protected List<ChartImagePane> initDemoList() {
		List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
		ChartImagePane pane = new ChartImagePane(getTypeIconPath()[0], Inter.getLocText("ChartF-Comb_Chart"), true);
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
        return new String[]{"/com/fr/design/images/chart/CustomPlot/type/0.png",
        };
    }

    @Override
    protected String[] getTypeLayoutPath() {
        return new String[]{"/com/fr/design/images/chart/CustomPlot/layout/0.png",
                "/com/fr/design/images/chart/CustomPlot/layout/1.png",
                "/com/fr/design/images/chart/CustomPlot/layout/2.png",
                "/com/fr/design/images/chart/CustomPlot/layout/3.png",};
    }

	/**
	 * 界面 标题
     * @return 界面标题
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("ChartF-Comb_Chart");
	}

	/**
	 * 更新界面内容
	 */
	public void populateBean(Chart chart) {
		super.populateBean(chart);
		
		typeDemo.get(0).isPressing = true;
		
		checkDemosBackground();
	}
	
	/**
	 * 保存界面属性
	 */
	public void updateBean(Chart chart) {
        changePlotWithClone(chart, CustomIndependentChart.createCustomChart().getPlot());
	}

    /**
     * 界面是否接受
     * @param ob 传入的对象
     * @return 是否是chart对象
     */
	public boolean accept(Object ob) {
		return super.accept(ob) && ((Chart) ob).getPlot().accept(CustomPlot.class);
	}


    public Chart getDefaultChart() {
        return CustomIndependentChart.combChartTypes[0];
    }
}
