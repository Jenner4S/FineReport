package com.fr.plugin.chart.glyph.marker;

import com.fr.chart.chartglyph.TriangleMarker;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.gauge.glyph.GaugeGlyphHelper;

import java.awt.*;

/**
 * Created by Mitisky on 16/1/15.
 */
public class VanChartTriangleMarker extends TriangleMarker implements VanChartMarkerInterface{
    private static final long serialVersionUID = 724617390466487601L;

    public void paint(Graphics2D g2d, double centerX, double centerY, boolean drawBorder){
        plotBackground = VanChartAttrHelper.getNotNullBackground(plotBackground);
        if(drawBorder){
            this.plotBackground.paint(g2d, GaugeGlyphHelper.getTrianglePath(centerX, centerY, size + GAP_SIZE));
        }
        this.background.paint(g2d,GaugeGlyphHelper.getTrianglePath(centerX, centerY, size));
        this.plotBackground.paint(g2d,GaugeGlyphHelper.getTrianglePath(centerX, centerY, size - GAP_SIZE));
    }
}
