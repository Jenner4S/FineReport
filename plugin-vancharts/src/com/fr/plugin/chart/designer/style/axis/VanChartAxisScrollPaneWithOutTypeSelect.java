package com.fr.plugin.chart.designer.style.axis;

import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;

/**
 * ������ѡ��ֵ�������ڻ����ı���Ĭ������ֵ�ᡣ
 */
public class VanChartAxisScrollPaneWithOutTypeSelect extends BasicScrollPane<VanChartAxis> implements VanChartXYAxisPaneInterface{
    private static final long serialVersionUID = 7700110757493668325L;
    protected VanChartBaseAxisPane axisPane;

    protected JPanel createContentPane(){
        initAxisPane();
        return axisPane;
    }

    protected void initAxisPane() {
        axisPane = new VanChartValueAxisPane(false);
    }

    @Override
    protected String title4PopupWindow() {
        return PaneTitleConstants.CHART_STYLE_AXIS_TITLE;
    }
    @Override
    public void populateBean(VanChartAxis axis) {
        populate(axis, null);
    }

    public VanChartAxis update(VanChartAxis axis){
        if(axis != null){
            axisPane.updateBean(axis);
        }
        return axis;
    }

    public void populate(VanChartAxis axis, VanChartStylePane parent){
        if(axis == null){
            return;
        }
        axisPane.setParent(parent);
        axisPane.populateBean(axis);
    }

    public VanChartAxis updateBean(String axisName, int position){
        return axisPane.updateBean(axisName, position);
    }
}
