package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.GanttPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.GanttIndependentChart;
import com.fr.general.Inter;

import java.util.ArrayList;
import java.util.List;

/**
 * ����ͼ ���Ա� ѡ������ ���ֽ���.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version ����ʱ�䣺2012-12-26 ����10:38:18
 */
public class GanttPlotPane extends AbstractChartTypePane{
	private static final long serialVersionUID = -601566194238908115L;

	private static final int GANTT = 0;
	
	@Override
	protected List<ChartImagePane> initDemoList() {
		List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
		ChartImagePane pane = new ChartImagePane(getTypeIconPath()[0], Inter.getLocText("FR-Chart-Gantt_Chart"), true);
		pane.isPressing = true;
		demoList.add(pane);
		return demoList;
	}

	/**
	 * û�в���
	 */
	protected List<ChartImagePane> initStyleList() {
		return new ArrayList<ChartImagePane>();
	}


    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/GanttPlot/type/0.png",
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
		return Inter.getLocText("FR-Chart-Gantt_Chart");
	}

	/**
	 * �����������
	 */
	public void updateBean(Chart chart) {
        if(chart.getPlot() != null && chart.getPlot().getPlotStyle() != ChartConstants.STYLE_NONE){
            resetChart(chart);
        }
        changePlotWithClone(chart, GanttIndependentChart.createGanttChart().getPlot());
	}

	/**
	 * ���½�������
	 */
	public void populateBean(Chart chart) {
		typeDemo.get(0).isPressing = true; 
		
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
		return (plot instanceof GanttPlot);
	}


    public Chart getDefaultChart() {
        return GanttIndependentChart.ganttChartTypes[0];
    }
}
