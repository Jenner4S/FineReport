package com.fr.design.mainframe.chart.gui.type;


import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.BarIndependentChart;
import com.fr.general.FRLogger;
import com.fr.general.Inter;

import java.util.ArrayList;
import java.util.List;

/**
 * 条形图 属性表 选择类型 布局界面.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2012-12-25 下午06:55:58
 */
public class BarPlotPane extends AbstractBarPane{
	private static final long serialVersionUID = 503770883396447032L;
	
	@Override
	protected String[] getTypeIconPath() {
		return new String[]{"/com/fr/design/images/chart/ColumnPlot/type/0.png",
                "/com/fr/design/images/chart/ColumnPlot/type/1.png",
                "/com/fr/design/images/chart/ColumnPlot/type/2.png",
                "/com/fr/design/images/chart/ColumnPlot/type/3.png",
                "/com/fr/design/images/chart/ColumnPlot/type/4.png",
                "/com/fr/design/images/chart/ColumnPlot/type/5.png",
                "/com/fr/design/images/chart/ColumnPlot/type/6.png",
        };
	}

	@Override
	protected String[] getTypeLayoutPath() {
		return new String[]{"/com/fr/design/images/chart/ColumnPlot/layout/0.png",
                "/com/fr/design/images/chart/ColumnPlot/layout/1.png",
                "/com/fr/design/images/chart/ColumnPlot/layout/2.png",
                "/com/fr/design/images/chart/ColumnPlot/layout/3.png",
        };
	}
	
	protected List<ChartImagePane> initDemoList() {
		List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
        String[] iconPaths = getTypeIconPath();
		ChartImagePane pane = new ChartImagePane(iconPaths[COLOMN_CHART], Inter.getLocText("FR-Chart-Type_Bar"));
		pane.isPressing = true;
		demoList.add(pane);
        demoList.add(new ChartImagePane(iconPaths[STACK_COLOMN_CHART], Inter.getLocText(new String[]{"FR-Chart-Type_Stacked","FR-Chart-Type_Bar"})));
        demoList.add(new ChartImagePane(iconPaths[PERCENT_STACK_COLOMN_CHART], Inter.getLocText(new String[]{"FR-Chart-Use_Percent","FR-Chart-Type_Stacked","FR-Chart-Type_Bar"})));

        demoList.add(new ChartImagePane(iconPaths[THREE_D_COLOMN_CHART], Inter.getLocText(new String[]{"FR-Chart-Chart_3D","FR-Chart-Type_Bar"}), true));

        demoList.add(new ChartImagePane(iconPaths[THREE_D_COLOMN_HORIZON_DRAW_CHART], Inter.getLocText(new String[]{"FR-Chart-Chart_3D","FR-Chart-Type_Bar","FR-Chart-Direction_Horizontal"},new String[]{"","(",")"})));

        demoList.add(new ChartImagePane(iconPaths[THREE_D_STACK_COLOMN_CHART],
                Inter.getLocText(new String[]{"FR-Chart-Chart_3D","FR-Chart-Type_Stacked","FR-Chart-Type_Bar"})));
        demoList.add(new ChartImagePane(iconPaths[THREE_D_PERCENT_STACK_COLOMN_CHART],
                Inter.getLocText(new String[]{"FR-Chart-Chart_3D","FR-Chart-Use_Percent","FR-Chart-Type_Stacked","FR-Chart-Type_Bar"}), true));
		return demoList;
	}

	@Override
	protected List <ChartImagePane> initStyleList() {
		return initNormalStyleList();
	}
	
	/**
	 * 界面标题
     * @return 界面标题
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("FR-Chart-Type_Bar");
	}

    protected Plot getSelectedClonedPlot(){
        Chart[] barChart = BarIndependentChart.barChartTypes;

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

        // check 是否类型发生变化,  如果不是, 改变风格的话, 就直接用原plot进行改变.

        // 另外 目前风格 populate 不是按照名称来的.  TODO
	}

	/**
	 * 判断界面进入
     * @param ob 传入的对象
     * @return 是否接受
	 */
	public boolean accept(Object ob) {
		if(!(ob instanceof Chart)) {
			return false;
		}
		Chart chart = (Chart)ob;
		Plot plot = chart.getPlot();
		return plot instanceof BarPlot && ((BarPlot)plot).isHorizontal();
	}

    public Chart getDefaultChart() {
        return BarIndependentChart.barChartTypes[0];
    }
}