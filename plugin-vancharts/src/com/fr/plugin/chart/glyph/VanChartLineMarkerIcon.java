package com.fr.plugin.chart.glyph;

import com.fr.base.GraphHelper;
import com.fr.chart.chartglyph.LineMarkerIcon;
import com.fr.stable.Constants;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Created by Mitisky on 15/11/11.
 */
public class VanChartLineMarkerIcon extends LineMarkerIcon {

    public void paint(Graphics g, int iconSize) {
        Graphics2D g2d = (Graphics2D)g;

        Paint oldPaint = g2d.getPaint();

        Composite oldComposite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha()));

        boolean isLineNotNone= this.getLineStyle() != Constants.LINE_NONE && getBackground() != null;

        if (this.getMarker() != null && isLineNotNone) {
            Stroke oldStroke = g2d.getStroke();

            g2d.setStroke(GraphHelper.getStroke(getLineStyle()));

            getBackground().drawWithGradientLine(g2d, new Line2D.Double(0, iconSize / 2.0, iconSize, iconSize / 2.0));

            g2d.setStroke(oldStroke);
        }

        if (this.getMarker() != null) {
            this.getMarker().paint(g2d, iconSize / 2.0, iconSize / 2.0);
        }

        g2d.setComposite(oldComposite);
        g2d.setPaint(oldPaint);
    }
}
