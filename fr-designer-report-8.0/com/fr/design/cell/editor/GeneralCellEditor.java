// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.editor;

import com.fr.base.Style;
import com.fr.base.TextFormat;
import com.fr.grid.Grid;
import com.fr.report.ReportHelper;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.TemplateCellElement;
import java.awt.Component;

// Referenced classes of package com.fr.design.cell.editor:
//            TextCellEditor

public class GeneralCellEditor extends TextCellEditor
{

    private CellElement cellElement;

    public GeneralCellEditor()
    {
    }

    public Object getCellEditorValue()
        throws Exception
    {
        Object obj = super.getCellEditorValue();
        Style style = cellElement.getStyle();
        if(style != null && (style.getFormat() instanceof TextFormat))
            return obj;
        else
            return ReportHelper.convertGeneralStringAccordingToExcel(obj);
    }

    public Component getCellEditorComponent(Grid grid, TemplateCellElement templatecellelement, int i)
    {
        cellElement = templatecellelement;
        return super.getCellEditorComponent(grid, templatecellelement, i);
    }
}
