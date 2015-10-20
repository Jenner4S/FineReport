package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.LinePlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.LineIndependentChart;
import com.fr.general.FRLogger;
import com.fr.general.Inter;

import java.util.ArrayList;
import java.util.List;

public class LinePlotPane extends AbstractChartTypePane{

	private static final int LINE_CHART = 0;
	
	@Override
	protected List<ChartImagePane> initDemoList() {
		List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
		
		ChartImagePane pane = new ChartImagePane(getTypeIconPath()[0], Inter.getLocText("I-LineStyle_Line"));
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
        return new String[]{"/com/fr/design/images/chart/LinePlot/type/0.png",
                "/com/fr/design/images/chart/LinePlot/type/1.png",
                "/com/fr/design/images/chart/LinePlot/type/2.png",
        };
    }

    @Override
    protected String[] getTypeLayoutPath() {
        return new String[]{"/com/fr/design/images/chart/LinePlot/layout/0.png",
                "/com/fr/design/images/chart/LinePlot/layout/1.png",
                "/com/fr/design/images/chart/LinePlot/layout/2.png",
                "/com/fr/design/images/chart/LinePlot/layout/3.png",
        };
    }
	/**
	 * �������ߵ�����ѡ�����
	 */
	public void populateBean(Chart chart) {
		super.populateBean(chart);
		typeDemo.get(LINE_CHART).isPressing = true;
		
		checkDemosBackground();
	}

	/**
	 * �����������: ���ߵ�ѡ�����ͽ���
	 */
	public void updateBean(Chart chart) {
        if(needsResetChart(chart)){
            resetChart(chart);
        }

		Chart[] cs = LineIndependentChart.lineChartTypes;
		LinePlot plot;
		if (cs.length > 0) {
			try {
				plot = (LinePlot)cs[0].getPlot().clone();
			} catch (Exception e) {
				plot = new LinePlot();
			}
		} else {
			plot = new LinePlot();
		}
		
		try {
			chart.switchPlot((Plot)plot.clone());
		} catch (CloneNotSupportedException e) {
			FRLogger.getLogger().error("Error In LineChart");
			chart.switchPlot(new LinePlot());
		}
	}

	/**
	 * �������
     * @return �������
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("I-LineStyle_Line");
	}

    /**
     * �����Ƿ����
     * @param ob ����Ķ���
     * @return �Ƿ���chart����
     */
	public boolean accept(Object ob) {
		return super.accept(ob) && ((Chart) ob).getPlot().accept(LinePlot.class);
	}

    public Chart getDefaultChart() {
        return LineIndependentChart.lineChartTypes[0];
    }

}
