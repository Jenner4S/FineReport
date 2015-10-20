package com.fr.design.mainframe.chart.gui.type;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.fr.base.CoreDecimalFormat;
import com.fr.chart.base.AttrBorder;
import com.fr.chart.base.AttrContents;
import com.fr.chart.chartattr.*;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.charttypes.DonutIndependentChart;
import com.fr.general.Inter;
import com.fr.stable.Constants;

/**
 * 圆环图的类型界面
 * @author eason
 *
 */
public class DonutPlotPane extends AbstractChartTypePane{
	private static final long serialVersionUID = -7084314809934346710L;
	private static final int DONUT_CHART = 0; //2d圆环图
	private static final int THREE_D_DONUT_CHART = 1; //3D圆环图
	
	protected List<ChartImagePane> initDemoList() {
		List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
		ChartImagePane pane = new ChartImagePane(getTypeIconPath()[0], Inter.getLocText("FR-Chart-Type_Donut"));
		pane.isPressing = true;
		demoList.add(pane);
		demoList.add(new ChartImagePane(getTypeIconPath()[1],
				Inter.getLocText(new String[]{"FR-Chart-Chart_3D", "ChartF-Donut"}), true));
		return demoList;
	}

	protected List<ChartImagePane> initStyleList() {
		return initNormalStyleList();
	}

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/DonutPlot/type/0.png",
                "/com/fr/design/images/chart/DonutPlot/type/1.png",
        };
    }

    @Override
    protected String[] getTypeLayoutPath() {
        return new String[]{"/com/fr/design/images/chart/DonutPlot/layout/0.png",
                "/com/fr/design/images/chart/DonutPlot/layout/1.png",
                "/com/fr/design/images/chart/DonutPlot/layout/2.png",
                "/com/fr/design/images/chart/DonutPlot/layout/3.png",
        };
    }


    /**
     * 界面是否接受
     * @param ob 对象
     * @return  true表示接受
     */
    public boolean accept(Object ob) {
        if(!super.accept(ob)) {
            return false;
        }
        Chart chart = (Chart)ob;
        Plot plot = chart.getPlot();
        return (plot instanceof DonutPlot);
    }

    /**
     * 更新界面内容
     */
    public void populateBean(Chart chart) {
        super.populateBean(chart);
        for(ChartImagePane imagePane : typeDemo) {
            imagePane.isPressing = false;
        }
        Plot plot = chart.getPlot();
        if(plot instanceof Donut3DPlot) {
            typeDemo.get(THREE_D_DONUT_CHART).isPressing = true;
            lastTypeIndex = THREE_D_DONUT_CHART;
        } else {
            typeDemo.get(DONUT_CHART).isPressing = true;
            lastTypeIndex = DONUT_CHART;
        }

        checkDemosBackground();
    }

    protected Plot getSelectedClonedPlot(){
        Plot newPlot;
        if(typeDemo.get(DONUT_CHART).isPressing) {
            newPlot = new Donut2DPlot();
        } else {
            newPlot = new Donut3DPlot();
        }
        createDonutCondition(newPlot);
        return newPlot;
    }

    /**
     * 保存界面属性
     */
    public void updateBean(Chart chart) {
        chart.switchPlot(getSelectedClonedPlot());
        super.updateBean(chart);
    }

    /**
     *界面标题
     * @return 标题
     */
    public String title4PopupWindow() {
		return Inter.getLocText("FR-Chart-Type_Donut");
	}

    /**
     * 是否有坐标轴
     * @return 没有坐标轴
     */
    public boolean isHaveAxis() {
        return false;
    }

    private void createDonutCondition(Plot plot) {
        ConditionCollection collection = plot.getConditionCollection();
        AttrBorder attrBorder = (AttrBorder) collection.getDefaultAttr().getExisted(AttrBorder.class);
        if (attrBorder == null) {
            attrBorder = new AttrBorder();
            collection.getDefaultAttr().addDataSeriesCondition(attrBorder);
        }
        attrBorder.setBorderColor(Color.WHITE);
        attrBorder.setBorderStyle(Constants.LINE_THIN);

        AttrContents attrContents = new AttrContents("${VALUE}${PERCENT}");
        plot.setHotTooltipStyle(attrContents);
        attrContents.setFormat(new CoreDecimalFormat(new DecimalFormat(), "#.##"));
        attrContents.setPercentFormat(new CoreDecimalFormat(new DecimalFormat(), "#.##%"));
    }


    public Chart getDefaultChart() {
        return DonutIndependentChart.donutChartTypes[0];
    }
}
