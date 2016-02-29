package com.fr.design.mainframe.chart.gui.type;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.fr.base.CoreDecimalFormat;
import com.fr.chart.base.AttrBorder;
import com.fr.chart.base.AttrContents;
import com.fr.chart.chartattr.*;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.charttypes.PieIndependentChart;
import com.fr.general.Inter;
import com.fr.stable.Constants;

/**
 * 饼图 属性表 选择类型 布局界面.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2012-12-25 下午06:55:33
 */
public class PiePlotPane extends AbstractChartTypePane{
	private static final long serialVersionUID = -601566194238908115L;

	private static final int PIE_CHART = 0;
	private static final int THREE_D_PIE_CHART = 1;
	
	@Override
	protected List<ChartImagePane> initDemoList() {
		List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
        String[] iconPaths = getTypeIconPath();
		ChartImagePane pane = new ChartImagePane(iconPaths[0], Inter.getLocText("I-PieStyle_Normal"));
		pane.isPressing = true;
		demoList.add(pane);
		demoList.add(new ChartImagePane(iconPaths[1],
				Inter.getLocText(new String[]{"FR-Chart-Chart_3D", "I-PieStyle_Normal"}), true));
		return demoList;
	}

	@Override
	protected List<ChartImagePane> initStyleList() {
		return initNormalStyleList();
	}


    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/PiePlot/type/0.png",
                "/com/fr/design/images/chart/PiePlot/type/1.png",
        };
    }

    @Override
    protected String[] getTypeLayoutPath() {
        return new String[]{"/com/fr/design/images/chart/PiePlot/layout/0.png",
                "/com/fr/design/images/chart/PiePlot/layout/1.png",
                "/com/fr/design/images/chart/PiePlot/layout/2.png",
                "/com/fr/design/images/chart/PiePlot/layout/3.png",
        };
    }


	/**
	 * 返回界面标题
     * @return 界面标题
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("I-PieStyle_Normal");
	}

    private void createPieCondition(Plot plot) {
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

	/**
	 * 更新界面内容
	 */
	public void populateBean(Chart chart) {
        super.populateBean(chart);
		for(ChartImagePane imagePane : typeDemo) {
			imagePane.isPressing = false;
		}
		Plot plot = chart.getPlot();
		if(plot instanceof Pie3DPlot) {
			typeDemo.get(THREE_D_PIE_CHART).isPressing = true;
            lastTypeIndex = THREE_D_PIE_CHART;
		} else {
			typeDemo.get(PIE_CHART).isPressing = true;
            lastTypeIndex = PIE_CHART;
		}

		checkDemosBackground();
	}

    protected Plot getSelectedClonedPlot(){
        Plot newPlot;
        if(typeDemo.get(PIE_CHART).isPressing) {
            newPlot = new PiePlot();
        } else {
            newPlot = new Pie3DPlot();
        }
        createPieCondition(newPlot);
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
     * 界面是否接受
     * @param ob 传入的对象
     * @return 是否是chart对象
     */
	public boolean accept(Object ob) {
		if(!super.accept(ob)) {
			return false;
		}
		Chart chart = (Chart)ob;
		Plot plot = chart.getPlot();
		return (plot.accept(PiePlot.class) || plot.accept(Pie3DPlot.class));
	}

    public Chart getDefaultChart() {
        return PieIndependentChart.pieChartTypes[0];
    }
}