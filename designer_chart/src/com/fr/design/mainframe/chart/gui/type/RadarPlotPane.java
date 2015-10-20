package com.fr.design.mainframe.chart.gui.type;

import java.util.ArrayList;
import java.util.List;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.RadarPlot;
import com.fr.chart.charttypes.RadarIndependentChart;
import com.fr.general.Inter;

/**
 * �״�ͼ ���Ա� ѡ������ ���ֽ���.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version ����ʱ�䣺2012-12-26 ����09:55:01
 */
public class RadarPlotPane extends AbstractChartTypePane{
	private static final long serialVersionUID = -601566194238908115L;

	private static final int RADAR = 0;
	
	@Override
	protected List<ChartImagePane> initDemoList() {
		List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
		ChartImagePane pane = new ChartImagePane(getTypeIconPath()[0], Inter.getLocText("FR-Chart-Radar_Chart"), true);
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
        return new String[]{"/com/fr/design/images/chart/RadarPlot/type/0.png",
        };
    }

    @Override
    protected String[] getTypeLayoutPath() {
        return new String[]{
        };
    }

	/**
	 * ���浯������
     * @return �������
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("FR-Chart-Radar_Chart");
	}

	/**
	 * �����������
	 */
	public void updateBean(Chart chart) {
        if(needsResetChart(chart)){
            resetChart(chart);
        }
		chart.switchPlot(RadarIndependentChart.createRadarChart().getPlot());
	}

	/**
	 * ���½�������
	 */
	public void populateBean(Chart chart) {
		typeDemo.get(RADAR).isPressing = true;
		
		checkDemosBackground();
	}

    /**
     * �����Ƿ����
     * @param ob ����Ķ���
     * @return �Ƿ���chart����
     */
	public boolean accept(Object ob) {
		if(!super.accept(ob)) {
			return false;
		}
		Chart chart = (Chart)ob;
		Plot plot = chart.getPlot();
		return (plot instanceof RadarPlot);
	}


    public Chart getDefaultChart() {
        return RadarIndependentChart.radarChartTypes[0];
    }
}
