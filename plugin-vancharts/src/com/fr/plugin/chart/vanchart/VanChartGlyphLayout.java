package com.fr.plugin.chart.vanchart;

import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.chartglyph.ChartGlyphLayout;
import com.fr.chart.chartglyph.LegendGlyph;
import com.fr.chart.chartglyph.PlotGlyph;
import com.fr.plugin.chart.glyph.VanChartDataSheetGlyph;
import com.fr.plugin.chart.glyph.VanChartDataSheetPlotGlyphInterface;
import com.fr.plugin.chart.glyph.VanChartPlotGlyph;
import com.fr.plugin.chart.glyph.VanChartPlotGlyphInterface;

import java.awt.geom.Rectangle2D;

/**
 * Created by Mitisky on 15/10/21.
 */
public class VanChartGlyphLayout {
    private static final double WIDTH_GAP = 8;
    private static final double HEIGHT_GAP = 8;
    private static final double GAP_BETWEEN_AXIS = 10;
    /**
     * 图表ChartGlyph的整个布局 边界
     * 整个图表的布局顺序：标题、图例、。
     * @param chartGlyph 用于布局的对象
     * @param resolution 分辨率参数
     */
    public static void doLayout(VanChartGlyph chartGlyph, int resolution) {
        if(chartGlyph == null || chartGlyph.getBounds() == null)  {
            return;
        }

        //原始图表边界。这个是用来计算标题、图例、坐标轴的最大占比的
        Rectangle2D chartOriginalBounds = chartGlyph.getBounds();
        //这个不断根据标题、图例、坐标轴等裁剪
        Rectangle2D leftBounds = chartGlyph.getBounds();

        ChartGlyphLayout.preGap4ChartBorder(chartGlyph, leftBounds);
        ChartGlyphLayout.preGap4ChartInPadding(leftBounds);

        //布局标题并裁减图表区
        ChartGlyphLayout.titleGlyphDoLayout(leftBounds, chartGlyph.getTitleGlyph(), resolution);

        //布局图例并裁减图表区
        legendDoLayoutWithLeftBounds(chartOriginalBounds, leftBounds, chartGlyph, resolution);

        //布局绘图区（包括坐标轴、数据表和系列图形）
        plotDoLayout(chartOriginalBounds, leftBounds, chartGlyph, resolution);
    }

    /**
     * 布局数据表，同时调整绘图区和坐标轴的边界信息
     * @param chartOriginalBounds 图表区的初始边界
     * @param plotOriginalBounds 绘图区布局坐标轴和数据表之前的初始边界
     * @param chartGlyph 图表绘图对象
     * @param resolution 像素
     */
    private static void dataSheetDoLayoutWithLeftBounds(Rectangle2D chartOriginalBounds, Rectangle2D plotOriginalBounds,
                                                        VanChartGlyph chartGlyph, VanChartDataSheetPlotGlyphInterface plotGlyph, int resolution){

        VanChartDataSheetGlyph dataSheetGlyph = (VanChartDataSheetGlyph)chartGlyph.getDataSheetGlyph();
        if(dataSheetGlyph == null){
            return;
        }

        //数据表布局第一步：将画数据表需要的属性补充完整。
        //构造的时候只是给了数据表配置的属性，这边将相关的图例、坐标轴、数据点等补充完整。
        dataSheetGlyph.initDataSheet(chartGlyph.getLegendGlyph(), plotGlyph);

        //数据表布局第二步：计算系列名的宽度（数据表中的图例的宽度）
        dataSheetGlyph.calculateSeriesWidth(chartOriginalBounds, resolution);

        //根据数据表左侧系列名的宽度调整绘图区以及坐标轴（x轴unitLength等）
        double leftWidth = dataSheetGlyph.getLeftWidth();
        double leftGap = plotGlyph.getBounds().getX() - plotOriginalBounds.getX();
        if(leftWidth > leftGap){
            double gap = leftWidth - leftGap;
            plotGlyph.setBounds(new Rectangle2D.Double(
                    plotGlyph.getBounds().getX() + gap,
                    plotGlyph.getBounds().getY(),
                    plotGlyph.getBounds().getWidth() - gap,
                    plotGlyph.getBounds().getHeight()));
            plotGlyph.adjustAxisGlyphWithLeftGap(gap);
        }

        //数据表布局第三步：根据x坐标轴的最终的unitLength计算分类标签的高度、系列的高度等。
        dataSheetGlyph.calculateOtherWidthAndHeight(resolution);

        //根据数据表整体高度调整绘图区以及坐标轴（y轴unitLength等）
        double bottomHeight = dataSheetGlyph.getBottomHeight();
        plotGlyph.setBounds(new Rectangle2D.Double(
                plotGlyph.getBounds().getX(),
                plotGlyph.getBounds().getY(),
                plotGlyph.getBounds().getWidth(),
                plotGlyph.getBounds().getHeight() - bottomHeight));
        plotGlyph.adjustAxisGlyphWithBottomGap(bottomHeight);

        //相对于整个图表区的位置
        double y = plotOriginalBounds.getMaxY() - bottomHeight;
        if(plotGlyph.getBottomXAxisCount() == 1){
            y -= GAP_BETWEEN_AXIS;
        }

        //数据表布局第四步：确定边界（相对于图表区位置）
        dataSheetGlyph.setBounds(new Rectangle2D.Double(plotGlyph.getBounds().getX() - leftWidth,
                 y, dataSheetGlyph.getWholeWidth(), bottomHeight));
    }

    /**
     * 只布局绘图区
     * @param chartOriginalBounds 图表原始边界
     * @param chartBounds 裁剪到这里是绘图区的初始边界
     * @param chartGlyph 图表绘图对象
     * @param resolution 分辨率
     */
    private static void plotDoLayout(Rectangle2D chartOriginalBounds, Rectangle2D chartBounds, VanChartGlyph chartGlyph, int resolution) {
        PlotGlyph plotGlyph = chartGlyph.getPlotGlyph();
        if(plotGlyph == null) {
            return;
        }
        //先把没有裁剪掉坐标轴和数据表的绘图区边界赋值给绘图区
        //之后布局数据表和坐标轴再进行裁剪
        plotGlyph.setBounds(chartBounds);

        //布局坐标轴并裁减
        if(plotGlyph instanceof VanChartPlotGlyphInterface){
            VanChartPlotGlyphInterface plotGlyphInterface = (VanChartPlotGlyphInterface)plotGlyph;
            plotGlyphInterface.layoutAxisGlyph(chartOriginalBounds, resolution);
        }

        //布局数据表
        if(plotGlyph instanceof VanChartDataSheetPlotGlyphInterface){
            dataSheetDoLayoutWithLeftBounds(chartOriginalBounds, chartBounds, chartGlyph, (VanChartDataSheetPlotGlyphInterface)plotGlyph, resolution);
        }

        //绘图区的边界确定了之后， 最后布局系列
        plotGlyph.layoutDataSeriesGlyph(resolution);

        if(plotGlyph.getRoundRadius() > 0){
            plotGlyph.setBounds(ChartBaseUtils.rectangle2RoundRectangle(plotGlyph.getBounds(), plotGlyph.getRoundRadius()));
        } else {
            plotGlyph.setBounds(ChartBaseUtils.rectangle2RoundRectangle(plotGlyph.getBounds(), plotGlyph.isRoundBorder()));
        }
    }

    /**
     * 布局图例
     * @param chartOriginalBounds 图表原始边界
     * @param chartBounds 图表剩余边界
     * @param chartGlyph 图表
     * @param resolution 分辨率
     */
    private static void legendDoLayoutWithLeftBounds(Rectangle2D chartOriginalBounds, Rectangle2D chartBounds, VanChartGlyph chartGlyph, int resolution) {
        LegendGlyph legendGlyph = chartGlyph.getLegendGlyph();
        if(legendGlyph == null || !legendGlyph.isVisible()) {
            return;
        }

        //布局之前先根据系列生成图例元素
        VanChartPlotGlyph plotGlyph = (VanChartPlotGlyph)chartGlyph.getPlotGlyph();
        legendGlyph.setItems(plotGlyph.createLegendItems());

        legendGlyph.layoutLegendAndRefreshChartBounds(chartOriginalBounds, chartBounds, WIDTH_GAP, HEIGHT_GAP, resolution);
    }

}
