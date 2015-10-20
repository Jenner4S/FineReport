package com.fr.design.chart.axis;

import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.CategoryPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.style.axis.ChartAxisUsePane;
import com.fr.design.mainframe.chart.gui.style.axis.ChartCategoryPane;
import com.fr.design.mainframe.chart.gui.style.axis.ChartPercentValuePane;
import com.fr.design.mainframe.chart.gui.style.axis.ChartValuePane;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-12-6
 * Time   : ����8:57
 */
public class BinaryChartStyleAxisPane extends ChartStyleAxisPane {

    public BinaryChartStyleAxisPane(Plot plot) {
        super(plot);
    }

    /**
     * ������������ʽ���
     * @param plot ͼ��
     * @return ���
     */
    public final AxisStyleObject[] createAxisStyleObjects(Plot plot) {
        return new AxisStyleObject[]{
        		getXAxisPane(plot),
        		getYAxisPane(plot)
        };
    }

    protected AxisStyleObject getXAxisPane(final Plot plot) {
        //�Ƿ�֧������������ʽ
        return new AxisStyleObject(CATE_AXIS, new ChartCategoryPane(){
            protected boolean isSupportLineStyle(){
           		return plot.isSupportAxisLineStyle();
           	}
        });
    }

    protected AxisStyleObject getYAxisPane(final Plot plot) {
    	Axis axis = plot.getyAxis();
    	// �ѻ����ʱ
    	boolean isStack = false;
    	if(plot instanceof CategoryPlot) {
    		isStack = ((CategoryPlot)plot).isStacked();
    	}

        ChartValuePane valuePane = new ChartValuePane(){
            protected boolean isSupportLineStyle(){
           	   return plot.isSupportAxisLineStyle();
           	}
        };
    	boolean usePercentPane = axis.isPercentage() && isStack;
    	ChartAxisUsePane p = usePercentPane ? new ChartPercentValuePane() : valuePane;
    	
    	return new AxisStyleObject(VALUE_AXIS, p);
    }
}
