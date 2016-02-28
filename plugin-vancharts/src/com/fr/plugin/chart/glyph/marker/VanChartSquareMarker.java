package com.fr.plugin.chart.glyph.marker;

import com.fr.chart.chartglyph.SquareMarker;
import com.fr.plugin.chart.attr.VanChartAttrHelper;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by Mitisky on 16/1/14.
 */
public class VanChartSquareMarker extends SquareMarker implements VanChartMarkerInterface{
    private static final long serialVersionUID = 5711707168948356744L;

    public void paint(Graphics2D g2d, double centerX, double centerY, boolean drawBorder){
        double borderSize = size + GAP_SIZE;
        plotBackground = VanChartAttrHelper.getNotNullBackground(plotBackground);
        if(drawBorder){
            plotBackground.paint(g2d, new Rectangle2D.Double(centerX - borderSize, centerY - borderSize, 2 * borderSize, 2 * borderSize));
        }
        this.background.paint(g2d, new Rectangle2D.Double(centerX - size, centerY - size, 2 * size, 2 * size));
        borderSize = size - GAP_SIZE;
        plotBackground.paint(g2d,new Rectangle2D.Double(centerX - borderSize, centerY - borderSize, 2 * borderSize, 2 * borderSize));
    }
}
