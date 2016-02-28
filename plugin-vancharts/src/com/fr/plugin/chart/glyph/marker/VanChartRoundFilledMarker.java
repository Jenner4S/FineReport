package com.fr.plugin.chart.glyph.marker;

import com.fr.chart.chartglyph.RoundFilledMarker;
import com.fr.plugin.chart.attr.VanChartAttrHelper;

import java.awt.*;
import java.awt.geom.Arc2D;

/**
 * Created by Mitisky on 16/1/14.
 */
public class VanChartRoundFilledMarker extends RoundFilledMarker implements VanChartMarkerInterface{
    private static final long serialVersionUID = -6469486895894830901L;

    public void paint(Graphics2D g2d, double centerX, double centerY, boolean drawBorder){
        if(drawBorder){
            double borderSize = size + GAP_SIZE;
            plotBackground = VanChartAttrHelper.getNotNullBackground(plotBackground);
            plotBackground.paint(g2d, new Arc2D.Double(centerX - borderSize, centerY - borderSize, 2 * borderSize, 2 * borderSize, 0, 360, Arc2D.OPEN));
        }
        this.background.paint(g2d, new Arc2D.Double(centerX - size, centerY - size, 2 * size, 2 * size, 0, 360, Arc2D.OPEN));
    }
}
