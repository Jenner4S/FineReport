package com.fr.plugin.chart.glyph;

import com.fr.chart.chartglyph.FoldLine;
import com.fr.chart.chartglyph.ShapeGlyph;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

/**
 * 其实画的还是一根整线，只是裁剪了区域
 * 面积图的话还可能多一片区域
 */
public class VanChartBandGlyph extends FoldLine {
    private static final long serialVersionUID = -8935706955950367108L;
    private Rectangle2D clipBounds;
    private ShapeGlyph areaGlyph;

    public VanChartBandGlyph(GeneralPath generalPath, Rectangle2D clipBounds) {
        super(generalPath);
        this.clipBounds = clipBounds;
    }

    public void setAreaGlyph(ShapeGlyph areaGlyph) {
        this.areaGlyph = areaGlyph;
    }

    public Rectangle2D getClipBounds() {
        return this.clipBounds;
    }

    public void setClipBounds(Rectangle2D clipBounds) {
        this.clipBounds = clipBounds;
    }

    public ShapeGlyph getAreaGlyph() {
        return areaGlyph;
    }

    public void draw(Graphics g, int resolution) {
        Shape oldClip = g.getClip();
        g.clipRect((int) clipBounds.getX(), (int) clipBounds.getY(), (int) clipBounds.getWidth(), (int) clipBounds.getHeight());
        if(this.areaGlyph != null){
            this.areaGlyph.draw(g, resolution);
        }
        super.draw(g, resolution);
        g.setClip(oldClip);
    }
}
