package com.fr.design.mainframe.chart.gui.type;

import java.util.ArrayList;
import java.util.List;

import com.fr.chart.chartattr.BarPlot;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.ColumnIndependentChart;
import com.fr.general.FRLogger;
import com.fr.general.Inter;

/**
 * 柱形图 属性表 选择类型 布局界面.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2012-12-25 下午06:57:14
 */
public class ColumnPlotPane extends AbstractBarPane{
	private static final long serialVersionUID = 7070966970039838314L;

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/BarPlot/type/0.png",
                "/com/fr/design/images/chart/BarPlot/type/1.png",
                "/com/fr/design/images/chart/BarPlot/type/2.png",
                "/com/fr/design/images/chart/BarPlot/type/3.png",
                "/com/fr/design/images/chart/BarPlot/type/4.png",
                "/com/fr/design/images/chart/BarPlot/type/5.png",
                "/com/fr/design/images/chart/BarPlot/type/6.png",
        };
    }

    @Override
    protected String[] getTypeLayoutPath() {
        return new String[]{"/com/fr/design/images/chart/BarPlot/layout/0.png",
                "/com/fr/design/images/chart/BarPlot/layout/1.png",
                "/com/fr/design/images/chart/BarPlot/layout/2.png",
                "/com/fr/design/images/chart/BarPlot/layout/3.png",};
    }

	protected List<ChartImagePane> initDemoList() {
		List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
        String[] iconPaths = getTypeIconPath();
		ChartImagePane pane = new ChartImagePane(iconPaths[COLOMN_CHART], Inter.getLocText("FR-Chart-Type_Column"));
		pane.isPressing = true;
		demoList.add(pane);
        demoList.add(new ChartImagePane(iconPaths[STACK_COLOMN_CHART], Inter.getLocText(new String[]{"FR-Chart-Type_Stacked","FR-Chart-Type_Column"})));
        demoList.add(new ChartImagePane(iconPaths[PERCENT_STACK_COLOMN_CHART],
                Inter.getLocText(new String[]{"FR-Chart-Use_Percent","FR-Chart-Type_Stacked","FR-Chart-Type_Column"})));
        demoList.add(new ChartImagePane(iconPaths[THREE_D_COLOMN_CHART], Inter.getLocText(new String[]{"FR-Chart-Chart_3D","FR-Chart-Type_Column"}), true));

        demoList.add(new ChartImagePane(iconPaths[THREE_D_COLOMN_HORIZON_DRAW_CHART],Inter.getLocText(new String[]{"FR-Chart-Chart_3D","FR-Chart-Type_Column","FR-Chart-Direction_Horizontal"},new String[]{"","(",")"})));
        demoList.add(new ChartImagePane(iconPaths[THREE_D_STACK_COLOMN_CHART],
                Inter.getLocText(new String[]{"FR-Chart-Chart_3D","FR-Chart-Type_Stacked","FR-Chart-Type_Column"})));
        demoList.add(new ChartImagePane(iconPaths[THREE_D_PERCENT_STACK_COLOMN_CHART],
                Inter.getLocText(new String[]{"FR-Chart-Chart_3D","FR-Chart-Use_Percent","FR-Chart-Type_Stacked","FR-Chart-Type_Column"}), true));

		return demoList;
	}

	@Override
	protected List<ChartImagePane> initStyleList() {
		return initNormalStyleList();
	}

	/**
	 * 界面标题
     * @return 界面标题
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("FR-Chart-Type_Column");
	}

    protected Plot getSelectedClonedPlot(){
        Chart[] barChart = ColumnIndependentChart.columnChartTypes;
        BarPlot newPlot = null;
        if(typeDemo.get(COLOMN_CHART).isPressing) {
            newPlot = (BarPlot)barChart[COLOMN_CHART].getPlot();
        }
        else if(typeDemo.get(STACK_COLOMN_CHART).isPressing) {
            newPlot = (BarPlot)barChart[STACK_COLOMN_CHART].getPlot();
        }
        else if(typeDemo.get(PERCENT_STACK_COLOMN_CHART).isPressing) {
            newPlot = (BarPlot)barChart[PERCENT_STACK_COLOMN_CHART].getPlot();
        }
        else if(typeDemo.get(THREE_D_COLOMN_CHART).isPressing) {
            newPlot = (BarPlot)barChart[THREE_D_COLOMN_CHART].getPlot();
        }
        else if(typeDemo.get(THREE_D_COLOMN_HORIZON_DRAW_CHART).isPressing) {
            newPlot = (BarPlot)barChart[THREE_D_COLOMN_HORIZON_DRAW_CHART].getPlot();
        }
        else if(typeDemo.get(THREE_D_STACK_COLOMN_CHART).isPressing) {
            newPlot = (BarPlot)barChart[THREE_D_STACK_COLOMN_CHART].getPlot();
        }
        else if(typeDemo.get(THREE_D_PERCENT_STACK_COLOMN_CHART).isPressing) {
            newPlot = (BarPlot)barChart[THREE_D_PERCENT_STACK_COLOMN_CHART].getPlot();
        }

        Plot cloned = null;
        try {
            cloned = (Plot)newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In ColumnChart");
        }
        return cloned;
    }

	/**
	 * 保存界面属性
	 */
	public void updateBean(Chart chart) {
	    chart.switchPlot(getSelectedClonedPlot());
		super.updateBean(chart);
	}

	/**
	 * 判断界面是否进入
     * @param ob 传入的对象
     * @return 对象是否是chart
	 */
	public boolean accept(Object ob) {
		if(!super.accept(ob)) {
			return false;
		}
		Chart chart = (Chart) ob;
		Plot plot = chart.getPlot();
		return plot.accept(BarPlot.class) && !((BarPlot)plot).isHorizontal();
	}

    public Chart getDefaultChart() {
        return ColumnIndependentChart.columnChartTypes[0];
    }
}