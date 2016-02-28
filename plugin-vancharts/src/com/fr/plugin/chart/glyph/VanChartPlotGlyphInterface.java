package com.fr.plugin.chart.glyph;

import java.awt.geom.Rectangle2D;

/**
 * 所有新的图表类型
 */
public interface VanChartPlotGlyphInterface {

    /**
     *  对绘图区中的坐标轴部分进行布局,传过去图表边界计算限制区域
     * @param chartOriginalBounds 原始图表边界（计算限制区域时用到）
     * @param resolution 分辨率
     */
    public void layoutAxisGlyph(Rectangle2D chartOriginalBounds, int resolution);

    /**
     * 是否是监控刷新
     * @return 是否是监控刷新
     */
    public boolean isMonitorRefresh();
}
