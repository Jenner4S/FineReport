// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.gui.active;

import com.fr.base.chart.Glyph;
import com.fr.chart.chartglyph.DataSeries;
import com.fr.chart.chartglyph.PlotGlyph;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

// Referenced classes of package com.fr.design.chart.gui.active:
//            ActiveGlyph

public class DataSeriesActiveGlyph extends ActiveGlyph
{

    private DataSeries series;

    public DataSeriesActiveGlyph(ChartComponent chartcomponent, DataSeries dataseries, Glyph glyph)
    {
        super(chartcomponent, glyph);
        series = dataseries;
    }

    public Glyph getGlyph()
    {
        return series;
    }

    public void goRightPane()
    {
        if(chartComponent.getEditingChart() == null)
        {
            return;
        } else
        {
            ChartEditPane.getInstance().GoToPane(new String[] {
                PaneTitleConstants.CHART_STYLE_TITLE, PaneTitleConstants.CHART_STYLE_SERIES_TITLE
            });
            return;
        }
    }

    public void drawAllGlyph(Graphics2D graphics2d, int i)
    {
        Point2D point2d = offset4Paint();
        graphics2d.translate(point2d.getX(), point2d.getY());
        if(parentGlyph != null && (parentGlyph instanceof PlotGlyph))
        {
            PlotGlyph plotglyph = (PlotGlyph)parentGlyph;
            plotglyph.drawShape4Series(graphics2d, i);
        }
        graphics2d.translate(-point2d.getX(), -point2d.getY());
    }
}
