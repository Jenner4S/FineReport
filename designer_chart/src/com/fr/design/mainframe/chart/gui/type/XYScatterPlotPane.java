package com.fr.design.mainframe.chart.gui.type;

import java.util.ArrayList;
import java.util.List;

import com.fr.chart.base.AttrLineStyle;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.XYScatterPlot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.charttypes.XYScatterIndependentChart;
import com.fr.general.Inter;
import com.fr.stable.Constants;

/**
 * ɢ��ͼ ���Ա� ѡ������ ���ֽ���. 
* @author kunsnat E-mail:kunsnat@gmail.com
* @version ����ʱ�䣺2012-12-25 ����08:33:55
 */
public class XYScatterPlotPane extends AbstractChartTypePane{
	private static final long serialVersionUID = -601566194238908115L;

	private static final int XYSCATTER_CHART = 0;
	
	@Override
	protected List<ChartImagePane> initDemoList() {
		List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
		ChartImagePane pane = new ChartImagePane(getTypeIconPath()[0], Inter.getLocText("I-xyScatterStyle_Marker"), true);
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
        return new String[]{"/com/fr/design/images/chart/XYScatterPlot/type/0.png",
        };
    }

    @Override
    protected String[] getTypeLayoutPath() {
        return new String[]{"/com/fr/design/images/chart/XYScatterPlot/layout/0.png",
                "/com/fr/design/images/chart/XYScatterPlot/layout/1.png",
                "/com/fr/design/images/chart/XYScatterPlot/layout/2.png",
                "/com/fr/design/images/chart/XYScatterPlot/layout/3.png",
        };
    }

	/**
	 * �������
     * @return �������
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("I-xyScatterStyle_Marker");
	}

	/**
	 * ����������� 
	 */
	public void updateBean(Chart chart) {

        if(needsResetChart(chart)){
            resetChart(chart);
        }

		XYScatterPlot plot = new XYScatterPlot();
		chart.switchPlot(plot);
		
		ConditionAttr conditionAttr = plot.getConditionCollection().getDefaultAttr();
		 AttrLineStyle lineStyle = (AttrLineStyle) conditionAttr.getConditionInType(AttrLineStyle.XML_TAG);
		 if (lineStyle != null) {
			 conditionAttr.remove(lineStyle);
		 }
		 conditionAttr.addDataSeriesCondition(new AttrLineStyle(Constants.LINE_NONE));
	}
	
	/**
	 * ���½�������
	 */
	public void populateBean(Chart chart) {
		typeDemo.get(XYSCATTER_CHART).isPressing = true;
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
		return (plot instanceof XYScatterPlot);
	}


    public Chart getDefaultChart() {
        return XYScatterIndependentChart.XYScatterChartTypes[0];
    }
}
