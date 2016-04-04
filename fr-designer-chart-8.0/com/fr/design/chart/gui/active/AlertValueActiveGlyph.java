// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.gui.active;

import com.fr.base.chart.Glyph;
import com.fr.chart.chartglyph.ChartAlertValueGlyph;
import com.fr.chart.chartglyph.NumberAxisGlyph;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.gui.active.action.SetAnalysisLineStyleAction;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

// Referenced classes of package com.fr.design.chart.gui.active:
//            ActiveGlyph

public class AlertValueActiveGlyph extends ActiveGlyph
{

    ChartAlertValueGlyph alertValueGlyph;

    public AlertValueActiveGlyph(ChartComponent chartcomponent, ChartAlertValueGlyph chartalertvalueglyph, Glyph glyph)
    {
        super(chartcomponent, glyph);
        alertValueGlyph = chartalertvalueglyph;
    }

    public Point2D offset4Paint()
    {
        Rectangle2D rectangle2d = alertValueGlyph.getValueAxisGlyph().getBounds();
        return new java.awt.geom.Point2D.Double(parentGlyph.getShape().getBounds().getX() + rectangle2d.getX(), parentGlyph.getShape().getBounds().getY() + rectangle2d.getY());
    }

    public Glyph getGlyph()
    {
        return alertValueGlyph;
    }

    public void goRightPane()
    {
        (new SetAnalysisLineStyleAction(chartComponent)).showAnalysisLineStylePane();
    }
}
