package com.fr.plugin.chart.designer.style.axis;

import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.general.ComparatorUtils;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.plot.VanChartAxisPlot;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;
import com.fr.plugin.chart.base.AttrSeriesStackAndAxis;
import com.fr.plugin.chart.base.VanChartConstants;
import com.fr.plugin.chart.column.VanChartColumnPlot;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.designer.style.axis.component.VanChartAxisButtonPane;
import com.fr.plugin.chart.designer.style.axis.gauge.VanChartAxisScrollPaneWithGauge;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;
import com.fr.plugin.chart.vanchart.VanChart;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 样式-坐标轴界面
 */
public class VanChartAxisPane extends BasicBeanPane<VanChart> {

    private static final long serialVersionUID = 3208082344409802817L;
    private VanChartAxisButtonPane axisButtonPane;
    private JPanel centerPane;
    private CardLayout cardLayout;
    private List<VanChartXYAxisPaneInterface> xAxisPaneList;
    private List<VanChartXYAxisPaneInterface> yAxisPaneList;

    private VanChartAxisPlot editingPlot;

    private VanChartStylePane parent;

    public VanChartAxisPane(VanChartStylePane parent){
        this.parent = parent;
        initComponents();
    }

    protected void initComponents() {
        xAxisPaneList = new ArrayList<VanChartXYAxisPaneInterface>();
        yAxisPaneList = new ArrayList<VanChartXYAxisPaneInterface>();

        this.setLayout(new BorderLayout());
        axisButtonPane = new VanChartAxisButtonPane(VanChartAxisPane.this);

        cardLayout = new CardLayout();
        centerPane = new JPanel(cardLayout);

        this.add(axisButtonPane, BorderLayout.NORTH);
        this.add(centerPane, BorderLayout.CENTER);
    }

    /**
     * 切换坐标轴
     * @param name 坐标轴名称
     */
    public void changeAxisSelected(String name) {
        cardLayout.show(centerPane, name);
    }

    /**
     * 添加X轴
     * @param name 坐标轴名称
     */
    public void addXAxis(String name) {
        List<VanChartAxis> xAxisList = editingPlot.getXAxisList();
        xAxisList.add(editingPlot.createXAxis(name, VanChartConstants.AXIS_BOTTOM));
        populate();
    }

    /**
     * 添加Y轴
     * @param name 坐标轴名称
     */
    public void addYAxis(String name) {
        List<VanChartAxis> yAxisList = editingPlot.getYAxisList();
        yAxisList.add(editingPlot.createYAxis(name, VanChartConstants.AXIS_LEFT));
        populate();
    }

    /**
     * 删除
     * @param name 坐标轴名称
     */
    public void removeAxis(String name) {
        List<VanChartAxis> xAxisList = editingPlot.getXAxisList();
        List<VanChartAxis> yAxisList = editingPlot.getYAxisList();
        for(int i = 0, len = xAxisList.size(); i < len; i++){
            VanChartAxis axis = xAxisList.get(i);
            if(ComparatorUtils.equals(name, axis.getAxisName())){
                xAxisList.remove(axis);
                populate();
                removeOthers(i, true);
                return;
            }
        }
        for(int i = 0, len = yAxisList.size(); i < len; i++){
            VanChartAxis axis = yAxisList.get(i);
            if(ComparatorUtils.equals(name, axis.getAxisName())){
                yAxisList.remove(axis);
                removeOthers(i, false);
                populate();
                return;
            }
        }
    }

    //删除此坐标轴相关堆积属性的设置
    private void removeOthers(int axisIndex, boolean isXAxis){
        //堆积和坐标轴
        ConditionCollection stackAndAxisCondition = editingPlot.getStackAndAxisCondition();
        if(stackAndAxisCondition == null){
            return;
        }
        for(int i = 0, len = stackAndAxisCondition.getConditionAttrSize(); i < len; i++){
            ConditionAttr conditionAttr = stackAndAxisCondition.getConditionAttr(i);
            AttrSeriesStackAndAxis stackAndAxis = (AttrSeriesStackAndAxis)conditionAttr.getExisted(AttrSeriesStackAndAxis.class);
            int index = isXAxis ? stackAndAxis.getXAxisIndex() : stackAndAxis.getYAxisIndex();
            if(index == axisIndex){
                stackAndAxisCondition.removeConditionAttr(conditionAttr);
            }
        }
    }

    @Override
    protected String title4PopupWindow() {
        return PaneTitleConstants.CHART_STYLE_AXIS_TITLE;
    }
    @Override
    public void populateBean(VanChart chart) {
        if(chart == null){
            return;
        }
        Plot plot = chart.getPlot();
        if(plot.isMeterPlot()){
            populateGaugePlotAxisPane((VanChartGaugePlot)plot);
        } else {
            editingPlot = (VanChartAxisPlot)plot;
            populate();
        }
    }

    private void populateGaugePlotAxisPane(VanChartGaugePlot gaugePlot){
        VanChartAxisScrollPaneWithGauge gaugeAxisPane = new VanChartAxisScrollPaneWithGauge();
        gaugeAxisPane.setGaugeStyle(gaugePlot.getGaugeStyle());
        gaugeAxisPane.populateBean(gaugePlot.getGaugeAxis());
        yAxisPaneList.add(gaugeAxisPane);
        this.removeAll();
        this.add(gaugeAxisPane, BorderLayout.CENTER);

        parent.initAllListeners();
    }

    private void populate(){
        if(editingPlot == null){
            return;
        }
        xAxisPaneList.clear();
        yAxisPaneList.clear();
        centerPane.removeAll();
        List<VanChartAxis> xAxisList = editingPlot.getXAxisList();
        List<VanChartAxis> yAxisList = editingPlot.getYAxisList();

        if(editingPlot instanceof VanChartColumnPlot && ((VanChartColumnPlot)editingPlot).isBar()){
            populateBarAxisList(xAxisList, yAxisList);
        } else {
            populateAxisList(xAxisList, yAxisList);
        }

        axisButtonPane.populateBean(editingPlot);

        parent.initAllListeners();//这句话不能挪出去，自定义的时候每次add后populate都得initListeners
    }

    private void populateAxisList(List<VanChartAxis> xAxisList, List<VanChartAxis> yAxisList){
        for(VanChartAxis axis : xAxisList){
            VanChartXYAxisPaneInterface axisPane = AxisPaneFactory.getXAxisScrollPane(editingPlot);
            if(axisPane == null){
                axisPane = new VanChartAxisScrollPaneWithTypeSelect();
            }
            axisPane.populate(axis, null);
            centerPane.add((JPanel)axisPane, axis.getAxisName());
            xAxisPaneList.add(axisPane);
        }
        for(VanChartAxis axis : yAxisList){
            VanChartXYAxisPaneInterface axisPane = AxisPaneFactory.getYAxisScrollPane(editingPlot);
            if(axisPane == null){
                axisPane = new VanChartAxisScrollPaneWithOutTypeSelect();
            }
            axisPane.populate(axis, null);
            centerPane.add((JPanel)axisPane, axis.getAxisName());
            yAxisPaneList.add(axisPane);
        }
    }

    private void populateBarAxisList(List<VanChartAxis> xAxisList, List<VanChartAxis> yAxisList){
        for(VanChartAxis axis : xAxisList){
            VanChartAxisScrollPaneWithOutTypeSelect axisPane = new VanChartAxisScrollPaneWithOutTypeSelect();
            //这边条形图的isXAxis是反的，传parent过去重新布局
            axisPane.populate(axis, parent);
            centerPane.add(axisPane, axis.getAxisName());
            xAxisPaneList.add(axisPane);
        }
        for(VanChartAxis axis : yAxisList){
            VanChartAxisScrollPaneWithTypeSelect axisPane = new VanChartAxisScrollPaneWithTypeSelect();
            axisPane.populate(axis, parent);
            centerPane.add(axisPane, axis.getAxisName());
            yAxisPaneList.add(axisPane);
        }
    }

    public void updateBean(VanChart chart){
        if(chart == null){
            return;
        }
        Plot plot = chart.getPlot();
        if(plot.isMeterPlot()){
            updateGaugePlotAxisPane((VanChartGaugePlot)plot);
        } else {
            update((VanChartRectanglePlot)plot);
        }
    }

    private void updateGaugePlotAxisPane(VanChartGaugePlot gaugePlot){
        if(yAxisPaneList.isEmpty()){
            return;
        }
        yAxisPaneList.get(0).update(gaugePlot.getGaugeAxis());
    }

    private void update(VanChartRectanglePlot rectanglePlot) {
        if(rectanglePlot == null){
            return;
        }
        List<VanChartAxis> xAxisList = rectanglePlot.getXAxisList();
        List<VanChartAxis> yAxisList = rectanglePlot.getYAxisList();
        List<VanChartAxis> xAxisNewList = new ArrayList<VanChartAxis>();
        List<VanChartAxis> yAxisNewList = new ArrayList<VanChartAxis>();

        for(int i = 0, len = xAxisPaneList.size(); i < len ;i++){
            xAxisNewList.add(xAxisPaneList.get(i).update(xAxisList.get(i)));
        }
        for(int i = 0, len = yAxisPaneList.size(); i < len ;i++){
            yAxisNewList.add(yAxisPaneList.get(i).update(yAxisList.get(i)));
        }
        rectanglePlot.setXAxisList(xAxisNewList);
        rectanglePlot.setYAxisList(yAxisNewList);
    }

    public VanChart updateBean() {
        return null;
    }
}
