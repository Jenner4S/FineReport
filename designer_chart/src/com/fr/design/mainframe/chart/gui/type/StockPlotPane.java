package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.StockPlot;
import com.fr.chart.charttypes.StockIndependentChart;
import com.fr.general.Inter;

import java.util.ArrayList;
import java.util.List;

/**
 * �ɼ�ͼ ���Ա� ѡ������ ���� ����.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2012-12-26 ����10:52:36
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
	 * �������
     * @return �������
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("FR-Chart-Stock_Chart");
	}

	/**
	 * �����������
	 */
	public void updateBean(Chart chart) {
        if(needsResetChart(chart)){
            resetChart(chart);
        }
        changePlotWithClone(chart, StockIndependentChart.createStockChart().getPlot());
	}

	/**
	 * ���½�������
	 */
	public void populateBean(Chart chart) {
		typeDemo.get(STOCK).isPressing = true; 
		
		checkDemosBackground();
	}

    /**
     * �����Ƿ����
     * @param ob ����Ķ���
     * @return �Ƿ���chart����
     */
	public boolean accept(Object ob) {
		return super.accept(ob) && ((Chart) ob).getPlot() instanceof StockPlot;
	}


    public Chart getDefaultChart() {
        return StockIndependentChart.stockChartTypes[0];
    }
}
