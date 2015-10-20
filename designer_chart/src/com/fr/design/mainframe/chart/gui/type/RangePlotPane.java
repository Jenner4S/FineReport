package com.fr.design.mainframe.chart.gui.type;

import java.util.ArrayList;
import java.util.List;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.RangePlot;
import com.fr.chart.charttypes.RangeIndependentChart;
import com.fr.general.Inter;

/**
 * ȫ��ͼ ���Ա� ѡ������ ���� ����.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version ����ʱ�䣺2012-12-26 ����10:43:50
 */
public class RangePlotPane extends AbstractChartTypePane{
	private static final long serialVersionUID = -601566194238908115L;

	private static final int RANGE = 0;
	
	@Override
	protected List<ChartImagePane> initDemoList() {
		List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
		ChartImagePane pane = new ChartImagePane(getTypeIconPath()[0], Inter.getLocText("ChartF-Range_Chart"), true);
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
        return new String[]{"/com/fr/design/images/chart/RangePlot/type/0.png",
        };
    }

    @Override
    protected String[] getTypeLayoutPath() {
        return new String[]{
        };
    }

	/**
	 * �������
     * @return �������
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("ChartF-Range_Chart");
	}

	/**
	 * �����������
	 */
	public void updateBean(Chart chart) {
        if(needsResetChart(chart)){
            resetChart(chart);
        }

		RangePlot plot = new RangePlot();
		chart.switchPlot(plot);
	}

	/**
	 * ���½�������
	 */
	public void populateBean(Chart chart) {
		typeDemo.get(RANGE).isPressing = true;
		
		checkDemosBackground();
	}

    /**
     * �жϽ����Ƿ�ΪChart ����
     * @param ob �����Ƿ�Ϊchart
     * @return �Ƿ���chart����
     */
	public boolean accept(Object ob) {
		if(!super.accept(ob)) {
			return false;
		}
		Chart chart = (Chart)ob;
		Plot plot = chart.getPlot();
		return (plot instanceof RangePlot);
	}


    public Chart getDefaultChart() {
        return RangeIndependentChart.rangeChartTypes[0];
    }
}
