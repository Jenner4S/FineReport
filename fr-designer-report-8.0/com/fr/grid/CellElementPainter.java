// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid;

import com.fr.base.Style;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.core.PaintUtils;
import com.fr.report.elementcase.ElementCase;
import java.awt.Graphics2D;

public class CellElementPainter
{

    public CellElementPainter()
    {
    }

    public void paintBackground(Graphics2D graphics2d, ElementCase elementcase, CellElement cellelement, int i, int j)
    {
        Style.paintBackground(graphics2d, cellelement.getStyle(), i, j);
    }

    public void paintContent(Graphics2D graphics2d, ElementCase elementcase, TemplateCellElement templatecellelement, int i, int j, int k)
    {
        PaintUtils.paintGridCellContent(graphics2d, templatecellelement, i, j, k);
    }

    public void paintBorder(Graphics2D graphics2d, ElementCase elementcase, CellElement cellelement, double d, double d1)
    {
        Style.paintBorder(graphics2d, cellelement.getStyle(), d, d1);
    }
}
