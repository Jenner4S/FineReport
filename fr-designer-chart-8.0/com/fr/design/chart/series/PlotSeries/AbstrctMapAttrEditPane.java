// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.PlotSeries;

import com.fr.chart.base.MapSvgAttr;

public interface AbstrctMapAttrEditPane
{

    public abstract void populateMapAttr(MapSvgAttr mapsvgattr);

    public abstract MapSvgAttr updateCurrentAttr();
}
