package com.fr.plugin.chart.glyph.marker;

import com.fr.chart.chartglyph.DiamondFilledMarker;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.gauge.glyph.GaugeGlyphHelper;

import java.awt.*;

/**
 * Created by Mitisky on 16/1/15.
 */
public class VanChartDiamondFilledMarker extends DiamondFilledMarker implements VanChartMarkerInterface{
    private static final long serialVersionUID = -337546556806171879L;

    public void paint(Graphics2D g2d, double centerX, double centerY, boolean drawBorder){
        if(drawBorder){
            plotBackground = VanChartAttrHelper.getNotNullBackground(plotBackground);
            this.plotBackground.paint(g2d, GaugeGlyphHelper.getDiamondPath(centerX, centerY, size + GAP_SIZE));
        }
        this.background.paint(g2d, GaugeGlyphHelper.getDiamondPath(centerX, centerY, size));
    }
}
