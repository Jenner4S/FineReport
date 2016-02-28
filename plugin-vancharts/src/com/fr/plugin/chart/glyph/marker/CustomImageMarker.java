package com.fr.plugin.chart.glyph.marker;

import com.fr.chart.chartglyph.Marker;
import com.fr.plugin.chart.attr.VanChartAttrHelper;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by Mitisky on 15/11/11.
 */
public class CustomImageMarker extends Marker implements VanChartMarkerInterface{
    private static final long serialVersionUID = -1895809052881779349L;

    private double width;
    private double height;

    public CustomImageMarker(double width, double height) {
        super();
        this.width = width;
        this.height = height;
    }

    /**
     * 画出标记
     * @param g2d 用于绘制的上下文
     * @param x    x坐标
     * @param y    y坐标
     */
    public void paintMarker(Graphics2D g2d, double x, double y) {
        paint(g2d, x, y, true);
    }

    public void paint(Graphics2D g2d, double centerX, double centerY, boolean drawBorder){
        if(drawBorder){
            double borderWidth = width + GAP_SIZE;
            double borderHeight = height + GAP_SIZE;
            plotBackground = VanChartAttrHelper.getNotNullBackground(plotBackground);
            plotBackground.paint(g2d,new Rectangle2D.Double(centerX - 0.5 * borderWidth, centerY - 0.5 * borderHeight, borderWidth, borderHeight));
        }
        this.background.paint(g2d, new Rectangle2D.Double(centerX - 0.5 * width, centerY - 0.5 * height, width, height));
    }
    /**
     * 用于js的初始化, 返回JSON类型
     */
    public String getMarkerType() {
        return "CustomImageMarker";
    }
}
