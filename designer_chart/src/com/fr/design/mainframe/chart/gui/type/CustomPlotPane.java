package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.CustomPlot;
import com.fr.chart.charttypes.CustomIndependentChart;
import com.fr.general.Inter;

import java.util.ArrayList;
import java.util.List;

/**
 * ���ͼ ���Ա� ͼ������ ����.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2012-12-21 ����06:17:40
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
	 * ���� ����
     * @return �������
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("ChartF-Comb_Chart");
	}

	/**
	 * ���½�������
	 */
	public void populateBean(Chart chart) {
		super.populateBean(chart);
		
		typeDemo.get(0).isPressing = true;
		
		checkDemosBackground();
	}
	
	/**
	 * �����������
	 */
	public void updateBean(Chart chart) {
        changePlotWithClone(chart, CustomIndependentChart.createCustomChart().getPlot());
	}

    /**
     * �����Ƿ����
     * @param ob ����Ķ���
     * @return �Ƿ���chart����
     */
	public boolean accept(Object ob) {
		return super.accept(ob) && ((Chart) ob).getPlot().accept(CustomPlot.class);
	}


    public Chart getDefaultChart() {
        return CustomIndependentChart.combChartTypes[0];
    }
}
