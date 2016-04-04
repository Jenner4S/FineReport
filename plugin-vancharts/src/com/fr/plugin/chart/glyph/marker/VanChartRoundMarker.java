package com.fr.plugin.chart.glyph.marker;

import com.fr.chart.chartglyph.RoundMarker;
import com.fr.plugin.chart.attr.VanChartAttrHelper;

import java.awt.*;
import java.awt.geom.Arc2D;

/**
 * Created by Mitisky on 16/1/14.
 */
public class VanChartRoundMarker extends RoundMarker implements VanChartMarkerInterface{
    private static final long serialVersionUID = -1395716073760825684L;

    public void paint(Graphics2D g2d, double centerX, double centerY, boolean drawBorder){
        double borderSize = size + GAP_SIZE;
        plotBackground = VanChartAttrHelper.getNotNullBackground(plotBackground);
        if(drawBorder){
            plotBackground.paint(g2d, new Arc2D.Double(centerX - borderSize, centerY - borderSize, 2 * borderSize, 2 * borderSize, 0, 360, Arc2D.OPEN));
        }
        this.background.paint(g2d, new Arc2D.Double(centerX - size,  centerY - size,2 * size, 2 * size, 0, 360, Arc2D.OPEN));
        borderSize = size - GAP_SIZE;
        this.plotBackground.paint(g2d, new Arc2D.Double(centerX - borderSize, centerY - borderSize, 2 * borderSize, 2 * borderSize, 0, 360, Arc2D.OPEN));
    }
}
