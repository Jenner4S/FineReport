package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.*;
import com.fr.chart.chartglyph.MeterStyle;
import com.fr.chart.charttypes.MeterIndependentChart;
import com.fr.general.FRLogger;
import com.fr.general.Inter;

import java.util.ArrayList;
import java.util.List;

/**
 * 仪表盘, 属性表 类型选择 界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-26 上午10:48:57
 */
public class MeterPlotPane extends AbstractChartTypePane {
	
	private static final int METER = 0;
    private static final int BLUE_METER =1;
    private static final int SIMPLE_METER = 2;


    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/MeterPlot/type/0.png",
                "/com/fr/design/images/chart/MeterPlot/type/1.png",
                "/com/fr/design/images/chart/MeterPlot/type/2.png",
        };
    }

    @Override
    protected String[] getTypeLayoutPath() {
        return new String[]{
        };
    }

    protected Plot getSelectedClonedPlot(){
        Chart[] meterChart = MeterIndependentChart.meterChartTypes;

        MeterPlot newPlot = null;
        if(typeDemo.get(METER).isPressing) {
            newPlot = (MeterPlot)meterChart[METER].getPlot();
        }
        else if(typeDemo.get(SIMPLE_METER).isPressing) {
            newPlot = (MeterPlot)meterChart[SIMPLE_METER].getPlot();
        }
        else if(typeDemo.get(BLUE_METER).isPressing) {
            newPlot = (MeterPlot)meterChart[BLUE_METER].getPlot();
        }

        Plot cloned = null;
        try {
            cloned = (Plot)newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In ColumnChart");
        }
        return cloned;
    }

	@Override
	protected List<ChartImagePane> initDemoList() {
		List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
        String[] iconPaths = getTypeIconPath();
		ChartImagePane pane = new ChartImagePane(iconPaths[0], Inter.getLocText(new String[]{"User-defined","ChartF-Meter"}), true);
		pane.isPressing = true;
		demoList.add(pane);
        demoList.add(new ChartImagePane(iconPaths[1], Inter.getLocText("FR-Chart-Type_Meter")+"1", true));
        demoList.add(new ChartImagePane(iconPaths[2], Inter.getLocText("FR-Chart-Type_Meter")+"2", true));
		return demoList;
	}

	@Override
	protected List<ChartImagePane> initStyleList() {
		return new ArrayList<ChartImagePane>();
	}

	/**
	 * 界面标题
     * @return 界面标题
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("FR-Chart-Type_Meter");
	}
	
	/**
	 * 保存界面属性
	 */
	public void updateBean(Chart chart) {
        if(needsResetChart(chart)){
            resetChart(chart);
        }
        chart.switchPlot(getSelectedClonedPlot());
	}

	/**
	 * 更新界面内容
	 */
	public void populateBean(Chart chart) {
      	MeterPlot meterPlot = (MeterPlot)chart.getPlot();
        MeterStyle meterStyle = meterPlot.getMeterStyle();

        if(meterStyle.getMeterType() == MeterStyle.METER_NORMAL){
            typeDemo.get(METER).isPressing = true;
            typeDemo.get(BLUE_METER).isPressing = false;
            typeDemo.get(SIMPLE_METER).isPressing = false;
        } else if(meterStyle.getMeterType() == MeterStyle.METER_BLUE) {
            typeDemo.get(METER).isPressing = false;
            typeDemo.get(BLUE_METER).isPressing = true;
            typeDemo.get(SIMPLE_METER).isPressing = false;
        } else {
            typeDemo.get(METER).isPressing = false;
            typeDemo.get(BLUE_METER).isPressing = false;
            typeDemo.get(SIMPLE_METER).isPressing = true;
        }
		checkDemosBackground();
	}

    /**
     * 界面是否接受
     * @param ob 传入的对象
     * @return 是否是chart对象
     */
	public boolean accept(Object ob) {
		return super.accept(ob) && ((Chart) ob).getPlot() instanceof MeterPlot;
	}


    public Chart getDefaultChart() {
        return MeterIndependentChart.meterChartTypes[0];
    }
}