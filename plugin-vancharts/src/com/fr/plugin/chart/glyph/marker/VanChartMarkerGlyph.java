package com.fr.plugin.chart.glyph.marker;

import com.fr.chart.chartglyph.MarkerGlyph;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by Mitisky on 16/1/15.
 */
public class VanChartMarkerGlyph extends MarkerGlyph {
    private static final long serialVersionUID = 2981491064740982663L;

    public void draw(Graphics g, int resolution) {
        Graphics2D g2d = (Graphics2D)g;

        if (this.getMarker() == null) {
            return;
        }

        Paint oldPaint = g2d.getPaint();
        Composite oldComposite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.getAlpha()));

        if(this.getMarker() instanceof VanChartMarkerInterface){
            VanChartMarkerInterface marker = (VanChartMarkerInterface)this.getMarker();
            Rectangle2D rect = getShape().getBounds2D();
            marker.paint(g2d, rect.getCenterX(), rect.getCenterY(), isNeedBorderBG());
        }

        g2d.setPaint(oldPaint);
        g2d.setComposite(oldComposite);
    }
}
