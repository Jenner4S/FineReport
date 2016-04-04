// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid;

import com.fr.base.DynamicUnitList;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.ReportHelper;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.ColumnRow;
import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.grid:
//            AbstractGridHeaderMouseHandler, GridUtils, Grid, GridColumn, 
//            GridHeader

public class GridColumnMouseHandler extends AbstractGridHeaderMouseHandler
{

    public GridColumnMouseHandler(GridColumn gridcolumn)
    {
        super(gridcolumn);
    }

    protected void resetSelectionByRightButton(ColumnRow columnrow, Selection selection, ElementCasePane elementcasepane)
    {
        int ai[] = selection.getSelectedColumns();
        if(ai.length == 0 || columnrow.getColumn() < ai[0] || columnrow.getColumn() > ai[ai.length - 1])
            resetGridSelectionBySelect(columnrow.getColumn(), elementcasepane);
    }

    protected int doChooseFrom()
    {
        return 1;
    }

    protected Rectangle resetSelectedBoundsByShift(Rectangle rectangle, ColumnRow columnrow, ElementCasePane elementcasepane)
    {
        int i = rectangle.x;
        if(columnrow.getColumn() >= rectangle.x)
            columnrow = ColumnRow.valueOf(columnrow.getColumn() + 1, columnrow.getRow());
        else
            i++;
        int j = GridUtils.getAdjustLastColumnRowOfReportPane(elementcasepane).getRow();
        return new Rectangle(Math.min(i, columnrow.getColumn()), 0, Math.max(rectangle.width, Math.abs(i - columnrow.getColumn())), j);
    }

    protected int[] getGridSelectionIndices(CellSelection cellselection)
    {
        return cellselection.getSelectedColumns();
    }

    protected int getScrollValue(ElementCasePane elementcasepane)
    {
        return elementcasepane.getGrid().getHorizontalValue();
    }

    protected int getScrollExtent(ElementCasePane elementcasepane)
    {
        return elementcasepane.getGrid().getHorizontalExtent();
    }

    protected int getBeginValue(ElementCasePane elementcasepane)
    {
        return elementcasepane.getGrid().getHorizontalBeginValue();
    }

    protected int getColumnOrRowByGridHeader(ColumnRow columnrow)
    {
        return columnrow.getColumn();
    }

    protected boolean isOnSeparatorLineIncludeZero(MouseEvent mouseevent, double d, double d1)
    {
        return d1 <= 1.0D && (double)mouseevent.getX() >= d + 2D && (double)mouseevent.getX() <= d + 5D;
    }

    protected boolean between(MouseEvent mouseevent, double d, double d1)
    {
        return (double)mouseevent.getX() > d && (double)mouseevent.getX() <= d1;
    }

    protected boolean isOnNormalSeparatorLine(MouseEvent mouseevent, double d)
    {
        return (double)mouseevent.getX() >= d - 2D && (double)mouseevent.getX() <= d + 2D;
    }

    protected int evtOffset(MouseEvent mouseevent, int i)
    {
        return mouseevent.getX() - i;
    }

    protected DynamicUnitList getSizeList(ElementCase elementcase)
    {
        return ReportHelper.getColumnWidthList(elementcase);
    }

    protected String methodName()
    {
        return "setColumnWidth";
    }

    protected String getSelectedHeaderTooltip(int i)
    {
        return (new StringBuilder()).append(i).append("C").toString();
    }

    protected Point getTipLocationByMouseEvent(MouseEvent mouseevent, GridHeader gridheader, Dimension dimension)
    {
        Point point = new Point(mouseevent.getX(), 0);
        SwingUtilities.convertPointToScreen(point, gridheader);
        Dimension dimension1 = Toolkit.getDefaultToolkit().getScreenSize();
        point.x = Math.max(0, Math.min(point.x - dimension.width / 2, dimension1.width - dimension.width));
        point.y = point.y - dimension.height - 2;
        return point;
    }

    protected void resetGridSelectionBySelect(int i, ElementCasePane elementcasepane)
    {
        int j = GridUtils.getAdjustLastColumnRowOfReportPane(elementcasepane).getRow();
        CellSelection cellselection = new CellSelection(i, 0, 1, j);
        cellselection.setSelectedType(1);
        elementcasepane.setSelection(cellselection);
    }

    protected String nameOfMoveCursorGIF()
    {
        return "cursor_hmove";
    }

    protected String nameOfSelectCursorGIF()
    {
        return "cursor_hselect";
    }

    protected String nameOfSplitCursorGIF()
    {
        return "cursor_hsplit";
    }

    protected UIPopupMenu createPopupMenu(ElementCasePane elementcasepane, MouseEvent mouseevent, int i)
    {
        return elementcasepane.createColumnPopupMenu(mouseevent, i);
    }

    protected void resetGridSelectionByDrag(CellSelection cellselection, ElementCasePane elementcasepane, int i, int j)
    {
        int k = GridUtils.getAdjustLastColumnRowOfReportPane(elementcasepane).getRow();
        cellselection.setLastRectangleBounds(Math.min(j, i), 0, Math.abs(i - j) + 1, k);
    }
}
