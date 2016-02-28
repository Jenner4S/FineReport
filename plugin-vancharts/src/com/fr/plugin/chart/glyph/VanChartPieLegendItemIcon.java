package com.fr.plugin.chart.glyph;

import com.fr.chart.chartglyph.LineMarkerIcon;
import com.fr.plugin.chart.gauge.glyph.GaugeGlyphHelper;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

/**
 * Created by Mitisky on 16/1/12.
 */
public class VanChartPieLegendItemIcon extends LineMarkerIcon {

    private static final long serialVersionUID = 109108275050824515L;
    private static final double START_ANGLE = 30;
    private static final double EXTENT = 120;
    private static final double GAP = 0.5 - Math.sqrt(3)/6.0;
    private static final double START_ANGLE_HAS_INNER = 45;
    private static final double EXTENT_HAS_INNER = 90;
    private static final double OUTER_RADIUS_PERCENT = 2.0/3.0;
    private static final double INNER_RADIUS_PERCENT = 1.0/3.0;
    private double innerRadiusPercent;

    public VanChartPieLegendItemIcon(double innerRadiusPercent){
        this.innerRadiusPercent = innerRadiusPercent;
    }

    public void paint(Graphics g, int iconSize) {
        Graphics2D g2d = (Graphics2D)g;

        Paint oldPaint = g2d.getPaint();

        Composite oldComposite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha()));

        Shape shape;
        if(innerRadiusPercent > 0){
            shape = GaugeGlyphHelper.getArcPath(new Point2D.Double(iconSize/2.0, iconSize),
                    iconSize * INNER_RADIUS_PERCENT, iconSize * OUTER_RADIUS_PERCENT, START_ANGLE_HAS_INNER, EXTENT_HAS_INNER);
        } else {
            shape = new Arc2D.Double(0, iconSize * GAP, iconSize, iconSize, START_ANGLE, EXTENT, Arc2D.PIE);
        }

        getBackground().paint(g2d, shape);

        g2d.setComposite(oldComposite);
        g2d.setPaint(oldPaint);
    }
}
