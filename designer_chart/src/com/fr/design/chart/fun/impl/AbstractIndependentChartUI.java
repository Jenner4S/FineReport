package com.fr.design.chart.fun.impl;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.fun.IndependentChartUIProvider;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.general.ComparatorUtils;


/**
 * Created by eason on 15/4/23.
 */
public abstract class AbstractIndependentChartUI implements IndependentChartUIProvider {
    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener){
        return new AbstractChartAttrPane[0];
    }

    /**
     * �Ƿ�ʹ��Ĭ�ϵĽ��棬Ϊ�˱�����������л�
     * @return �Ƿ�ʹ��Ĭ�ϵĽ���
     */
    public boolean isUseDefaultPane(){
        return true;
    }

    public ConditionAttributesPane getPlotConditionPane(){
        return null;
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(){
        return null;
    }

    public boolean equals(Object obj) {
        return obj != null && ComparatorUtils.equals(obj.getClass(), this.getClass());
    }
}
