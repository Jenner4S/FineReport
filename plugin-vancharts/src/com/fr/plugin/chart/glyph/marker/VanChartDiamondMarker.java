package com.fr.plugin.chart.glyph.marker;

import com.fr.chart.chartglyph.DiamondMarker;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.gauge.glyph.GaugeGlyphHelper;

import java.awt.*;

/**
 * Created by Mitisky on 16/1/15.
 */
public class VanChartDiamondMarker extends DiamondMarker implements VanChartMarkerInterface{
    private static final long serialVersionUID = 5747176764138320058L;

    public void paint(Graphics2D g2d, double centerX, double centerY, boolean drawBorder){
        plotBackground = VanChartAttrHelper.getNotNullBackground(plotBackground);
        if(drawBorder){
            this.plotBackground.paint(g2d, GaugeGlyphHelper.getDiamondPath(centerX, centerY, size + GAP_SIZE));
        }
        this.background.paint(g2d, GaugeGlyphHelper.getDiamondPath(centerX, centerY, size));
        this.plotBackground.paint(g2d,GaugeGlyphHelper.getDiamondPath(centerX, centerY, size - GAP_SIZE));
    }
}
