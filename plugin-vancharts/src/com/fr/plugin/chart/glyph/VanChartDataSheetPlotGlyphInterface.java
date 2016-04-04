package com.fr.plugin.chart.glyph;

import com.fr.chart.chartglyph.DataSeries;
import com.fr.chart.chartglyph.LegendItem;
import com.fr.plugin.chart.glyph.axis.VanChartBaseAxisGlyph;

import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

/**
 * Created by Mitisky on 16/1/21.
 * 含有数据表的图形对象
 */
public interface VanChartDataSheetPlotGlyphInterface {

    /**
     * 布局数据表的时候，坐标轴左侧对绘图区的裁剪部分不够数据表使用，需要对绘图区左侧继续裁剪,
     * 因为是相对位置，所以只需要右侧的y坐标轴往左移就好了，x坐标轴需要计算unitLength等。
     * @param leftGap 绘图区左侧还需要裁剪leftGap
     */
    public void adjustAxisGlyphWithLeftGap(double leftGap);

    /**
     * 布局数据表的时候，需要对绘图区下方继续裁剪，
     * 下方x坐标轴也要进行平移等计算，y坐标轴需要计算unitLength等。
     * @param bottomGap 绘图区下方还需要裁剪rightGap
     */
    public void adjustAxisGlyphWithBottomGap(double bottomGap);

    /**
     * 返回矩形Glyph的边界
     */
    public Rectangle2D getBounds();

    /**
     * 设置矩形Glyph的边界
     * @param bounds 矩形边界
     */
    public void setBounds(RectangularShape bounds);


    public VanChartBaseAxisGlyph getDefaultXAxisGlyph();

    public int getBottomXAxisCount();

    public LegendItem[] createLegendItems();

    /**
     * 已经添加的系列的数量
     *
     * @return 系列的数量
     */
    public int getSeriesSize();

    /**
     * 指定位置上的系列, 如果不存在, 则返回一个空的系列.
     *
     * @param index 要返回的系列的索引
     * @return 系列
     */
    public DataSeries getSeries(int index);
}
