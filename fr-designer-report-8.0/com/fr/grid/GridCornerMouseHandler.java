// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid;

import com.fr.design.constants.UIConstants;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.stable.ColumnRow;
import java.awt.event.MouseEvent;
import javax.swing.JScrollBar;
import javax.swing.event.MouseInputAdapter;

// Referenced classes of package com.fr.grid:
//            GridColumnMouseHandler, GridRowMouseHandler, GridCorner, GridUtils, 
//            GridColumn, GridRow

public class GridCornerMouseHandler extends MouseInputAdapter
{

    GridCorner gridCorner;

    public GridCornerMouseHandler(GridCorner gridcorner)
    {
        gridCorner = gridcorner;
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
        doSelectAll();
    }

    private void doSelectAll()
    {
        ElementCasePane elementcasepane = gridCorner.getElementCasePane();
        ColumnRow columnrow = GridUtils.getAdjustLastColumnRowOfReportPane(elementcasepane);
        elementcasepane.setSelection(new CellSelection(0, 0, columnrow.getColumn(), columnrow.getRow()));
        GridColumn gridcolumn = elementcasepane.getGridColumn();
        GridColumnMouseHandler gridcolumnmousehandler = new GridColumnMouseHandler(gridcolumn);
        gridcolumnmousehandler.setStartMultiSelectIndex(0);
        gridcolumnmousehandler.setEndMultiSelectIndex(columnrow.getColumn());
        gridcolumn.addMouseListener(gridcolumnmousehandler);
        gridcolumn.addMouseMotionListener(gridcolumnmousehandler);
        GridRow gridrow = elementcasepane.getGridRow();
        GridRowMouseHandler gridrowmousehandler = new GridRowMouseHandler(gridrow);
        gridrowmousehandler.setStartMultiSelectIndex(0);
        gridrowmousehandler.setEndMultiSelectIndex(columnrow.getRow());
        gridrow.addMouseListener(gridrowmousehandler);
        gridrow.addMouseMotionListener(gridrowmousehandler);
        elementcasepane.getHorizontalScrollBar().setValue(elementcasepane.getHorizontalScrollBar().getValue());
        elementcasepane.getVerticalScrollBar().setValue(elementcasepane.getVerticalScrollBar().getValue());
    }

    public void mouseMoved(MouseEvent mouseevent)
    {
        gridCorner.setCursor(UIConstants.CELL_DEFAULT_CURSOR);
    }
}
