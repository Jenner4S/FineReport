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
//            AbstractGridHeaderMouseHandler, Grid, GridUtils, GridHeader, 
//            GridRow

public class GridRowMouseHandler extends AbstractGridHeaderMouseHandler
{

    public GridRowMouseHandler(GridRow gridrow)
    {
        super(gridrow);
    }

    protected void resetSelectionByRightButton(ColumnRow columnrow, Selection selection, ElementCasePane elementcasepane)
    {
        int ai[] = selection.getSelectedRows();
        if(ai.length == 0 || columnrow.getRow() < ai[0] || columnrow.getRow() > ai[ai.length - 1])
            resetGridSelectionBySelect(columnrow.getRow(), elementcasepane);
    }

    protected int doChooseFrom()
    {
        return 2;
    }

    protected int getScrollValue(ElementCasePane elementcasepane)
    {
        return elementcasepane.getGrid().getVerticalValue();
    }

    protected int getScrollExtent(ElementCasePane elementcasepane)
    {
        return elementcasepane.getGrid().getVerticalExtent();
    }

    protected int getBeginValue(ElementCasePane elementcasepane)
    {
        return elementcasepane.getGrid().getVerticalBeginValue();
    }

    protected Rectangle resetSelectedBoundsByShift(Rectangle rectangle, ColumnRow columnrow, ElementCasePane elementcasepane)
    {
        int i = rectangle.y;
        if(columnrow.getRow() >= rectangle.y)
            columnrow = ColumnRow.valueOf(columnrow.getColumn(), columnrow.getRow() + 1);
        else
            i++;
        int j = GridUtils.getAdjustLastColumnRowOfReportPane(elementcasepane).getColumn();
        return new Rectangle(0, Math.min(i, columnrow.getRow()), j, Math.max(rectangle.height, Math.abs(i - columnrow.getRow())));
    }

    protected int[] getGridSelectionIndices(CellSelection cellselection)
    {
        return cellselection.getSelectedRows();
    }

    protected int getColumnOrRowByGridHeader(ColumnRow columnrow)
    {
        return columnrow.getRow();
    }

    protected void resetGridSelectionBySelect(int i, ElementCasePane elementcasepane)
    {
        int j = GridUtils.getAdjustLastColumnRowOfReportPane(elementcasepane).getColumn();
        CellSelection cellselection = new CellSelection(0, i, j, 1);
        cellselection.setSelectedType(2);
        elementcasepane.setSelection(cellselection);
    }

    protected boolean isOnSeparatorLineIncludeZero(MouseEvent mouseevent, double d, double d1)
    {
        return d1 <= 1.0D && (double)mouseevent.getY() >= d + 2D && (double)mouseevent.getY() <= d + 5D;
    }

    protected boolean between(MouseEvent mouseevent, double d, double d1)
    {
        return (double)mouseevent.getY() > d && (double)mouseevent.getY() <= d1;
    }

    protected DynamicUnitList getSizeList(ElementCase elementcase)
    {
        return ReportHelper.getRowHeightList(elementcase);
    }

    protected String methodName()
    {
        return "setRowHeight";
    }

    protected boolean isOnNormalSeparatorLine(MouseEvent mouseevent, double d)
    {
        return (double)mouseevent.getY() >= d - 2D && (double)mouseevent.getY() <= d + 2D;
    }

    protected int evtOffset(MouseEvent mouseevent, int i)
    {
        return mouseevent.getY() - i;
    }

    protected String getSelectedHeaderTooltip(int i)
    {
        return (new StringBuilder()).append(i).append("R").toString();
    }

    protected Point getTipLocationByMouseEvent(MouseEvent mouseevent, GridHeader gridheader, Dimension dimension)
    {
        Point point = new Point(0, mouseevent.getY());
        SwingUtilities.convertPointToScreen(point, gridheader);
        point.x = point.x + gridheader.getSize().width + 2;
        point.y = point.y - dimension.height / 2;
        return point;
    }

    protected String nameOfMoveCursorGIF()
    {
        return "cursor_vmove";
    }

    protected String nameOfSelectCursorGIF()
    {
        return "cursor_vselect";
    }

    protected String nameOfSplitCursorGIF()
    {
        return "cursor_vsplit";
    }

    protected UIPopupMenu createPopupMenu(ElementCasePane elementcasepane, MouseEvent mouseevent, int i)
    {
        return elementcasepane.createRowPopupMenu(mouseevent, i);
    }

    protected void resetGridSelectionByDrag(CellSelection cellselection, ElementCasePane elementcasepane, int i, int j)
    {
        int k = GridUtils.getAdjustLastColumnRowOfReportPane(elementcasepane).getColumn();
        cellselection.setLastRectangleBounds(0, Math.min(j, i), k, Math.abs(i - j) + 1);
    }
}
