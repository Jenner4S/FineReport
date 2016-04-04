package com.fr.plugin.chart.designer.style.series;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrBorder;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.UICorrelationComboBoxPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.series.AbstractPlotSeriesPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;
import com.fr.plugin.chart.base.*;
import com.fr.plugin.chart.column.VanChartCustomStackAndAxisConditionPane;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.*;
import com.fr.plugin.chart.designer.component.border.VanChartBorderPane;

import javax.swing.*;
import java.util.ArrayList;

/**
 * 图表样式-系列抽象界面
 */

public abstract class VanChartAbstractPlotSeriesPane extends AbstractPlotSeriesPane {

    private static final long serialVersionUID = -3909265296019479690L;

    private VanChartBeautyPane stylePane;//风格

    private VanChartTrendLinePane trendLinePane;//趋势线

    private VanChartLineTypePane lineTypePane;//线

    private VanChartMarkerPane markerPane;//标记点类型

    private VanChartAreaSeriesFillColorPane areaSeriesFillColorPane;//填充颜色

    private VanChartBorderPane borderPane;//边框

    private UINumberDragPane transparent;//不透明度

    //堆积和坐标轴
    private UICorrelationComboBoxPane stackAndAxisPane;
    private JPanel stackAndAxisWholePane;
    protected JSeparator jSeparator;
    protected JPanel contentPane;

    public VanChartAbstractPlotSeriesPane(ChartStylePane parent, Plot plot){
        super(parent, plot);
    }

    //风格
    protected VanChartBeautyPane createStylePane() {
        stylePane = new VanChartBeautyPane();
        return stylePane;
    }

    //趋势线
    protected JPanel createTrendLinePane() {
        trendLinePane = new VanChartTrendLinePane();
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Chart-Trend_Line"), trendLinePane);
    }

    //线
    protected JPanel createLineTypePane() {
        lineTypePane = getLineTypePane();
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_Line"), lineTypePane);
    }

    protected VanChartLineTypePane getLineTypePane() {
        return new VanChartLineTypePane();
    }

    //标记点类型
    protected JPanel createMarkerPane() {
        markerPane = new VanChartMarkerPane();
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_Marker"), markerPane);
    }

    //填充颜色
    protected JPanel createAreaFillColorPane() {
        areaSeriesFillColorPane = new VanChartAreaSeriesFillColorPane();
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_FillColor"), areaSeriesFillColorPane);
    }

    //边框（默认没有圆角）
    protected VanChartBorderPane createBorderPane() {
        borderPane = createDiffBorderPane();
        return borderPane;
    }

    protected VanChartBorderPane createDiffBorderPane() {
        return new VanChartBorderPane();
    }


    //不透明度
    protected JPanel createAlphaPane() {
        transparent = new UINumberDragPane(0, 100);
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_Alpha"), transparent);
    }


    //堆积和坐标轴设置(自定义柱形图等用到)
    protected JPanel createStackedAndAxisPane() {
        stackAndAxisPane = new UICorrelationComboBoxPane();
        stackAndAxisWholePane = TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_StackAndSeries"), stackAndAxisPane);
        return stackAndAxisWholePane;
    }

    //界面上删除堆积和坐标轴设置
    protected void removeStackWholePane() {
        contentPane.remove(stackAndAxisWholePane);
        contentPane.remove(jSeparator);
        contentPane.repaint();
    }

    /**
     * 更新Plot的属性到系列界面
     */
    public void populateBean(Plot plot) {
        if(plot == null) {
            return;
        }
        super.populateBean(plot);//配色

        if(stylePane != null){//风格
            stylePane.populateBean(plot.getPlotStyle());
        }

        if(stackAndAxisPane != null && plot instanceof VanChartRectanglePlot){//堆积和坐标轴
            VanChartRectanglePlot rectanglePlot = (VanChartRectanglePlot)plot;
            if(rectanglePlot.isCustomChart()){
                populateStackAndAxis(rectanglePlot);
            } else {
                removeStackWholePane();
            }
        }

        populateCondition(plot.getConditionCollection().getDefaultAttr());
    }

    /**
     * 保存 系列界面的属性到Plot
     */
    public void updateBean(Plot plot) {
        if(plot == null) {
            return;
        }
        super.updateBean(plot);//配色

        if(stylePane != null){//风格
            plot.setPlotStyle(stylePane.updateBean());
        }

        if(stackAndAxisPane != null && plot instanceof VanChartRectanglePlot){//堆积和坐标轴
            VanChartRectanglePlot rectanglePlot = (VanChartRectanglePlot)plot;
            if(rectanglePlot.isCustomChart()){
                updateStackAndAxis(rectanglePlot);
            }
        }

        updateCondition(plot.getConditionCollection().getDefaultAttr());
    }

    private void populateStackAndAxis(VanChartRectanglePlot plot) {
        java.util.List<VanChartAxis> xAxisList = plot.getXAxisList();
        java.util.List<VanChartAxis> yAxisList = plot.getYAxisList();
        String[] axisXNames = VanChartAttrHelper.getAxisNames(xAxisList);
        String[] axisYNames = VanChartAttrHelper.getAxisNames(yAxisList);

        java.util.List<UIMenuNameableCreator> menuList = new ArrayList<UIMenuNameableCreator>();
        ConditionAttr demo = new ConditionAttr();
        AttrSeriesStackAndAxis seriesStackAndAxis = new AttrSeriesStackAndAxis();
        seriesStackAndAxis.setXAxisNamesArray(axisXNames);
        seriesStackAndAxis.setYAxisNameArray(axisYNames);
        demo.addDataSeriesCondition(seriesStackAndAxis);
        menuList.add(new VanChartUIMenuNameableCreator(Inter.getLocText("Plugin-ChartF_StackAndSeries"), demo, getStackAndAxisPaneClass()));
        stackAndAxisPane.refreshMenuAndAddMenuAction(menuList);

        java.util.List<UIMenuNameableCreator> list = new ArrayList<UIMenuNameableCreator>();

        ConditionCollection stackAndAxisCondition = plot.getStackAndAxisCondition();

        for(int i = 0, len = stackAndAxisCondition.getConditionAttrSize(); i < len; i++){
            ConditionAttr conditionAttr = stackAndAxisCondition.getConditionAttr(i);
            AttrSeriesStackAndAxis stackAndAxis = (AttrSeriesStackAndAxis)conditionAttr.getExisted(AttrSeriesStackAndAxis.class);
            stackAndAxis.setXAxisNamesArray(axisXNames);
            stackAndAxis.setYAxisNameArray(axisYNames);
            list.add(new VanChartUIMenuNameableCreator(conditionAttr.getName(), conditionAttr, getStackAndAxisPaneClass()));
        }

        stackAndAxisPane.populateBean(list);
        stackAndAxisPane.doLayout();
    }

    protected Class<? extends BasicBeanPane> getStackAndAxisPaneClass() {
        return VanChartCustomStackAndAxisConditionPane.class;
    }

    private void updateStackAndAxis(VanChartRectanglePlot plot){
        ConditionCollection stackAndAxisCondition = plot.getStackAndAxisCondition();
        stackAndAxisCondition.clearConditionAttr();

        java.util.List<UIMenuNameableCreator> list = stackAndAxisPane.updateBean();
        for(UIMenuNameableCreator creator : list){
            ConditionAttr conditionAttr = (ConditionAttr)creator.getObj();
            conditionAttr.setName(creator.getName());
            AttrSeriesStackAndAxis seriesStackAndAxis = (AttrSeriesStackAndAxis)conditionAttr.getExisted(AttrSeriesStackAndAxis.class);
            seriesStackAndAxis.setStackID(creator.getName());
            stackAndAxisCondition.addConditionAttr(conditionAttr);
        }
    }

    private void populateCondition(ConditionAttr defaultAttr){
        if(trendLinePane != null){//趋势线
            VanChartAttrTrendLine attrTrendLine =(VanChartAttrTrendLine)defaultAttr.getExisted(VanChartAttrTrendLine.class);
            trendLinePane.populate(attrTrendLine);
        }
        if(lineTypePane != null){//线-线型、控制断开等
            VanChartAttrLine attrLine = (VanChartAttrLine)defaultAttr.getExisted(VanChartAttrLine.class);
            lineTypePane.populate(attrLine);
        }
        if(markerPane != null){//标记点
            VanChartAttrMarker attrMarker = (VanChartAttrMarker)defaultAttr.getExisted(VanChartAttrMarker.class);
            markerPane.populate(attrMarker);
        }
        if(areaSeriesFillColorPane != null){//填充颜色
            AttrAreaSeriesFillColorBackground seriesFillColorBackground = (AttrAreaSeriesFillColorBackground)defaultAttr.getExisted(AttrAreaSeriesFillColorBackground.class);
            areaSeriesFillColorPane.populate(seriesFillColorBackground);
        }
        if(borderPane != null){//边框
            AttrBorder attrBorder = (AttrBorder)defaultAttr.getExisted(AttrBorder.class);
            if(attrBorder != null){
                borderPane.populate(attrBorder);
            }
        }
        if(transparent != null){//不透明度
            AttrAlpha attrAlpha = (AttrAlpha)defaultAttr.getExisted(AttrAlpha.class);
            if(attrAlpha != null){
                transparent.populateBean(attrAlpha.getAlpha() * VanChartAttrHelper.PERCENT);
            } else {
                //初始值为100
                transparent.populateBean(VanChartAttrHelper.PERCENT);
            }
        }
    }

    private void updateCondition(ConditionAttr defaultAttr){
        if(trendLinePane != null){
            VanChartAttrTrendLine newTrendLine = trendLinePane.update();
            VanChartAttrTrendLine attrTrendLine =(VanChartAttrTrendLine)defaultAttr.getExisted(VanChartAttrTrendLine.class);
            defaultAttr.remove(attrTrendLine);
            defaultAttr.addDataSeriesCondition(newTrendLine);
        }
        if(lineTypePane != null){
            VanChartAttrLine attrLine = (VanChartAttrLine)defaultAttr.getExisted(VanChartAttrLine.class);
            defaultAttr.remove(attrLine);
            defaultAttr.addDataSeriesCondition(lineTypePane.update());
        }
        if(markerPane != null){
            VanChartAttrMarker newMarker = markerPane.update();
            VanChartAttrMarker attrMarker = (VanChartAttrMarker)defaultAttr.getExisted(VanChartAttrMarker.class);
            defaultAttr.remove(attrMarker);
            defaultAttr.addDataSeriesCondition(newMarker);
        }
        if(areaSeriesFillColorPane != null){
            AttrAreaSeriesFillColorBackground newFillColorBackground = areaSeriesFillColorPane.update();
            AttrAreaSeriesFillColorBackground oldFillColorBackground = (AttrAreaSeriesFillColorBackground)defaultAttr.getExisted(AttrAreaSeriesFillColorBackground.class);
            if(oldFillColorBackground != null){
                defaultAttr.remove(oldFillColorBackground);
            }
            defaultAttr.addDataSeriesCondition(newFillColorBackground);
        }
        if(borderPane != null){
            AttrBorder attrBorder = (AttrBorder)defaultAttr.getExisted(AttrBorder.class);
            if(attrBorder == null){
                attrBorder = new AttrBorder();
                defaultAttr.addDataSeriesCondition(attrBorder);
            }
            borderPane.update(attrBorder);
        }
        if(transparent != null){
            AttrAlpha attrAlpha = (AttrAlpha)defaultAttr.getExisted(AttrAlpha.class);
            if(attrAlpha == null){
                attrAlpha = new AttrAlpha();
                defaultAttr.addDataSeriesCondition(attrAlpha);
            }
            attrAlpha.setAlpha((float)(transparent.updateBean()/VanChartAttrHelper.PERCENT));
        }
    }
}

