// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid;

import com.fr.base.*;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.*;
import com.fr.design.present.CellWriteAttrPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.poly.creator.ECBlockPane;
import com.fr.report.ReportHelper;
import com.fr.report.cell.*;
import com.fr.report.cell.cellattr.CellGUIAttr;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.*;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.OLDPIX;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

// Referenced classes of package com.fr.grid:
//            Grid, GridUtils, IntelliElements

public class GridMouseAdapter
    implements MouseListener, MouseWheelListener, MouseMotionListener
{

    private static final int WIDGET_WIDTH = 13;
    private static final int TIME_DELAY = 100;
    private static final int TOOLTIP_X = 30;
    private static final int TOOLTIP_X_Y_FIX = 4;
    private static final double COPY_CROSS_INNER_DISTANCE = 1.5D;
    private static final double COPY_CROSS_OUTER_DISTANCE = 2.5D;
    private static int DRAG_REFRESH_TIME = 10;
    private Grid grid;
    private int oldEvtX;
    private int oldEvtY;
    private int oldLocationX;
    private int oldLocationY;
    private long lastMouseMoveTime;
    private Map floatNamePointMap;
    private ColumnRow tempOldSelectedCell;
    private int ECBlockGap;

    protected GridMouseAdapter(Grid grid1)
    {
        oldEvtX = 0;
        oldEvtY = 0;
        lastMouseMoveTime = 0L;
        ECBlockGap = 40;
        grid = grid1;
    }

    public void mousePressed(MouseEvent mouseevent)
    {
        if(!grid.isEnabled())
            return;
        oldEvtX = mouseevent.getX();
        oldEvtY = mouseevent.getY();
        grid.stopEditing();
        if(!grid.hasFocus() && grid.isRequestFocusEnabled())
            grid.requestFocus();
        if(grid.getDrawingFloatElement() != null)
        {
            doWithDrawingFloatElement();
        } else
        {
            if(SwingUtilities.isRightMouseButton(mouseevent))
                doWithRightButtonPressed();
            else
                doWithLeftButtonPressed(mouseevent);
            ElementCasePane elementcasepane = grid.getElementCasePane();
            if(!mouseevent.isShiftDown() && (elementcasepane.getSelection() instanceof CellSelection))
                tempOldSelectedCell = GridUtils.getAdjustEventColumnRow(elementcasepane, oldEvtX, oldEvtY);
        }
    }

    private void doWithDrawingFloatElement()
    {
        ElementCasePane elementcasepane = grid.getElementCasePane();
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        DynamicUnitList dynamicunitlist = ReportHelper.getColumnWidthList(templateelementcase);
        DynamicUnitList dynamicunitlist1 = ReportHelper.getRowHeightList(templateelementcase);
        int i = grid.getHorizontalValue();
        int j = grid.getVerticalValue();
        int k = ScreenResolution.getScreenResolution();
        FU fu = FU.valueOfPix(oldEvtX, k);
        FU fu1 = FU.valueOfPix(oldEvtY, k);
        FU fu2 = FU.getInstance(fu.toFU() + dynamicunitlist.getRangeValue(0, i).toFU());
        FU fu3 = FU.getInstance(fu1.toFU() + dynamicunitlist1.getRangeValue(0, j).toFU());
        grid.getDrawingFloatElement().setLeftDistance(fu2);
        grid.getDrawingFloatElement().setTopDistance(fu3);
        templateelementcase.addFloatElement(grid.getDrawingFloatElement());
        elementcasepane.setSelection(new FloatSelection(grid.getDrawingFloatElement().getName()));
    }

    private void doWithRightButtonPressed()
    {
        ElementCasePane elementcasepane = grid.getElementCasePane();
        Object aobj[] = GridUtils.getAboveFloatElementCursor(elementcasepane, oldEvtX, oldEvtY);
        if(!ArrayUtils.isEmpty(aobj))
        {
            FloatElement floatelement = (FloatElement)aobj[0];
            elementcasepane.setSelection(new FloatSelection(floatelement.getName()));
        } else
        {
            ColumnRow columnrow = GridUtils.getAdjustEventColumnRow(elementcasepane, oldEvtX, oldEvtY);
            if(!elementcasepane.getSelection().containsColumnRow(columnrow))
                GridUtils.doSelectCell(elementcasepane, columnrow.getColumn(), columnrow.getRow());
        }
        elementcasepane.repaint();
        JPopupMenu jpopupmenu = elementcasepane.createPopupMenu();
        if(jpopupmenu != null)
            GUICoreUtils.showPopupMenu(jpopupmenu, grid, oldEvtX - 1, oldEvtY - 1);
    }

    private void doWithLeftButtonPressed(MouseEvent mouseevent)
    {
        if(BaseUtils.isAuthorityEditing())
            grid.setEditable(false);
        ElementCasePane elementcasepane = grid.getElementCasePane();
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        boolean flag = mouseevent.isShiftDown();
        boolean flag1 = mouseevent.isControlDown();
        int i = mouseevent.getClickCount();
        grid.setDragType(isMoveCellSelection(oldEvtX, oldEvtY));
        if(i >= 2)
            grid.setDragType(0);
        if(grid.getDragType() != 0)
        {
            Selection selection = elementcasepane.getSelection();
            if(selection instanceof CellSelection)
            {
                if(grid.getDragRectangle() == null)
                    grid.setDragRectangle(new Rectangle());
                CellSelection cellselection = ((CellSelection)selection).clone();
                grid.getDragRectangle().setBounds(cellselection.toRectangle());
                return;
            }
        }
        doOneClickSelection(oldEvtX, oldEvtY, flag, flag1);
        ColumnRow columnrow = GridUtils.getEventColumnRow(elementcasepane, oldEvtX, oldEvtY);
        TemplateCellElement templatecellelement = templateelementcase.getTemplateCellElement(columnrow.getColumn(), columnrow.getRow());
        if(i >= 2 && !BaseUtils.isAuthorityEditing())
            grid.startEditing();
        if(i == 1 && templatecellelement != null && templatecellelement.getWidget() != null && !BaseUtils.isAuthorityEditing())
            showWidetWindow(templatecellelement, templateelementcase);
        elementcasepane.repaint();
    }

    private void showWidetWindow(TemplateCellElement templatecellelement, TemplateElementCase templateelementcase)
    {
        int i = ScreenResolution.getScreenResolution();
        DynamicUnitList dynamicunitlist = ReportHelper.getColumnWidthList(templateelementcase);
        DynamicUnitList dynamicunitlist1 = ReportHelper.getRowHeightList(templateelementcase);
        double d = (double)oldEvtX - dynamicunitlist.getRangeValue(grid.getHorizontalValue(), templatecellelement.getColumn()).toPixD(i);
        double d1 = (double)oldEvtY - dynamicunitlist1.getRangeValue(grid.getVerticalValue(), templatecellelement.getRow()).toPixD(i);
        double d2 = dynamicunitlist.getRangeValue(templatecellelement.getColumn(), templatecellelement.getColumn() + templatecellelement.getColumnSpan()).toPixD(i);
        double d3 = dynamicunitlist1.getRangeValue(templatecellelement.getRow(), templatecellelement.getRow() + templatecellelement.getRowSpan()).toPixD(i);
        if(fitSizeToShow(d2, d3, d, d1))
            CellWriteAttrPane.showWidgetWindow(grid.getElementCasePane());
    }

    private boolean fitSizeToShow(double d, double d1, double d2, double d3)
    {
        return d - d2 > 0.0D && d1 - d3 > 0.0D && d - d2 < 13D && d1 - d3 < 13D;
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
        if(!grid.isEnabled() || !grid.isEditable())
            return;
        boolean flag = false;
        ElementCasePane elementcasepane = grid.getElementCasePane();
        Selection selection = elementcasepane.getSelection();
        if(grid.getDrawingFloatElement() != null)
        {
            if(grid.getDrawingFloatElement().getWidth().equal_zero() && grid.getDrawingFloatElement().getHeight().equal_zero())
            {
                grid.getDrawingFloatElement().setWidth(new OLDPIX(100F));
                grid.getDrawingFloatElement().setHeight(new OLDPIX(100F));
            }
            grid.setDrawingFloatElement(null);
        } else
        if(selection instanceof FloatSelection)
            grid.setCursor(Cursor.getDefaultCursor());
        if(grid.getDragType() == 1)
        {
            if(selection instanceof CellSelection)
            {
                grid.getElementCasePane().cut();
                if(outOfBounds(mouseevent, elementcasepane))
                    GridUtils.doSelectCell(elementcasepane, grid.getDragRectangle().x, grid.getDragRectangle().y);
                else
                    mousePressed(mouseevent);
                grid.getElementCasePane().paste();
                flag = true;
            }
        } else
        if(grid.getDragType() == 2)
        {
            if(selection instanceof CellSelection)
            {
                CellSelection cellselection = (CellSelection)selection;
                IntelliElements.iterating(elementcasepane, cellselection.toRectangle(), grid.getDragRectangle());
                if(grid.getDragRectangle() != null)
                    elementcasepane.setSelection(new CellSelection(grid.getDragRectangle().x, grid.getDragRectangle().y, grid.getDragRectangle().width, grid.getDragRectangle().height));
                flag = true;
            }
        } else
        if(grid.getDragType() == 3)
            flag = true;
        grid.setDragType(0);
        grid.setDragRectangle(null);
        if(flag)
        {
            elementcasepane.setSupportDefaultParentCalculate(true);
            elementcasepane.fireTargetModified();
            elementcasepane.setSupportDefaultParentCalculate(false);
        }
        doWithFormatBrush(elementcasepane);
        elementcasepane.repaint();
    }

    private void doWithFormatBrush(ElementCasePane elementcasepane)
    {
        if(DesignerContext.getFormatState() == 0)
            return;
        if(elementcasepane.getCellNeedTOFormat() != null)
        {
            elementcasepane.getFormatBrushAction().updateFormatBrush(DesignerContext.getReferencedStyle(), elementcasepane.getCellNeedTOFormat(), elementcasepane);
            elementcasepane.fireTargetModified();
        }
        if(DesignerContext.getFormatState() == 1)
            elementcasepane.cancelFormatBrush();
        if(DesignerContext.getFormatState() == 2)
            elementcasepane.getFormatBrush().setSelected(true);
    }

    private boolean outOfBounds(MouseEvent mouseevent, ElementCasePane elementcasepane)
    {
        return mouseevent.getY() > elementcasepane.getHeight() || mouseevent.getY() < 0 || mouseevent.getX() > elementcasepane.getWidth() || mouseevent.getX() < 0;
    }

    public void mouseMoved(MouseEvent mouseevent)
    {
        ElementCasePane elementcasepane = grid.getElementCasePane();
        boolean flag = !grid.isEnabled() || !grid.isEditable();
        if(flag || grid.isEditing())
        {
            if(grid.IsNotShowingTableSelectPane())
            {
                grid.setCursor(UIConstants.CELL_DEFAULT_CURSOR);
                return;
            }
            if(DesignerContext.getFormatState() != 0)
                grid.setCursor(UIConstants.FORMAT_BRUSH_CURSOR);
            else
                grid.setCursor(GUICoreUtils.createCustomCursor(BaseUtils.readImage("com/fr/design/images/buttonicon/select.png"), new Point(0, 0), "select", grid));
            return;
        }
        long l = System.currentTimeMillis();
        if(l - lastMouseMoveTime <= 100L)
        {
            return;
        } else
        {
            lastMouseMoveTime = l;
            mouseMoveOnGrid(mouseevent.getX(), mouseevent.getY());
            return;
        }
    }

    public void mouseDragged(MouseEvent mouseevent)
    {
        if(!grid.isEnabled())
            return;
        boolean flag = mouseevent.isControlDown();
        long l = System.currentTimeMillis();
        if(l - lastMouseMoveTime <= (long)DRAG_REFRESH_TIME)
            return;
        lastMouseMoveTime = l;
        if(SwingUtilities.isRightMouseButton(mouseevent))
        {
            return;
        } else
        {
            doWithMouseDragged(mouseevent.getX(), mouseevent.getY(), flag);
            return;
        }
    }

    private void doWithMouseDragged(int i, int j, boolean flag)
    {
        ElementCasePane elementcasepane = grid.getElementCasePane();
        if(elementcasepane.mustInVisibleRange())
        {
            Grid grid1 = elementcasepane.getGrid();
            if(i > grid1.getWidth() - 2 || j > grid1.getHeight() - 2)
                return;
        }
        Selection selection = elementcasepane.getSelection();
        if((selection instanceof FloatSelection) && !BaseUtils.isAuthorityEditing())
        {
            doWithFloatElementDragged(i, j, (FloatSelection)selection);
            grid.setDragType(3);
        } else
        if(grid.getDragType() == 2 && !BaseUtils.isAuthorityEditing())
            doWithCellElementDragged(i, j, (CellSelection)selection);
        else
        if(grid.getDragType() == 1 && !BaseUtils.isAuthorityEditing())
        {
            ColumnRow columnrow = GridUtils.getAdjustEventColumnRow(elementcasepane, i, j);
            if(columnrow.getColumn() != grid.getDragRectangle().x || columnrow.getRow() != grid.getDragRectangle().y)
            {
                grid.getDragRectangle().x = columnrow.getColumn();
                grid.getDragRectangle().y = columnrow.getRow();
            }
        } else
        {
            doShiftSelectCell(i, j);
        }
        grid.getElementCasePane().repaint();
    }

    private void doWithFloatElementDragged(int i, int j, FloatSelection floatselection)
    {
        TemplateElementCase templateelementcase = grid.getElementCasePane().getEditingElementCase();
        int k = ScreenResolution.getScreenResolution();
        String s = floatselection.getSelectedFloatName();
        FloatElement floatelement = templateelementcase.getFloatElement(s);
        int l = grid.getCursor().getType();
        if(l == 6 || l == 7 || l == 5 || l == 4)
        {
            DynamicUnitList dynamicunitlist = ReportHelper.getColumnWidthList(templateelementcase);
            DynamicUnitList dynamicunitlist4 = ReportHelper.getRowHeightList(templateelementcase);
            FU fu2 = FU.valueOfPix(Math.min(oldEvtX, i), k);
            FU fu5 = FU.valueOfPix(Math.min(oldEvtY, j), k);
            FU fu6 = fu2.add(dynamicunitlist.getRangeValue(0, grid.getHorizontalValue()));
            FU fu7 = fu5.add(dynamicunitlist4.getRangeValue(0, grid.getVerticalValue()));
            floatelement.setLeftDistance(fu6);
            floatelement.setTopDistance(fu7);
            floatelement.setWidth(FU.valueOfPix(Math.max(oldEvtX, i), k).subtract(fu2));
            floatelement.setHeight(FU.valueOfPix(Math.max(oldEvtY, j), k).subtract(fu5));
        } else
        if(l == 9 || l == 8)
        {
            DynamicUnitList dynamicunitlist1 = ReportHelper.getRowHeightList(templateelementcase);
            FU fu = FU.valueOfPix(Math.min(oldEvtY, j), k);
            FU fu3 = fu.add(dynamicunitlist1.getRangeValue(0, grid.getVerticalValue()));
            floatelement.setTopDistance(fu3);
            floatelement.setHeight(FU.valueOfPix(Math.max(oldEvtY, j), k).subtract(fu));
        } else
        if(l == 10 || l == 11)
        {
            DynamicUnitList dynamicunitlist2 = ReportHelper.getColumnWidthList(templateelementcase);
            FU fu1 = FU.valueOfPix(Math.min(oldEvtX, i), k);
            FU fu4 = fu1.add(dynamicunitlist2.getRangeValue(0, grid.getHorizontalValue()));
            floatelement.setLeftDistance(fu4);
            floatelement.setWidth(FU.valueOfPix(Math.max(oldEvtX, i), k).subtract(fu1));
        } else
        if(l == 13)
        {
            DynamicUnitList dynamicunitlist3 = ReportHelper.getColumnWidthList(templateelementcase);
            DynamicUnitList dynamicunitlist5 = ReportHelper.getRowHeightList(templateelementcase);
            int i1 = grid.getHorizontalValue();
            int j1 = grid.getVerticalValue();
            String s1 = floatselection.getSelectedFloatName();
            FloatElement floatelement1 = templateelementcase.getFloatElement(s1);
            Point point = (Point)floatNamePointMap.get(s1);
            int k1 = point.x + Math.max(oldLocationX + (i - oldEvtX), 0);
            int l1 = point.y + Math.max(oldLocationY + (j - oldEvtY), 0);
            FU fu8 = FU.valueOfPix(k1, k);
            FU fu9 = fu8.add(dynamicunitlist3.getRangeValue(0, i1));
            FU fu10 = FU.valueOfPix(l1, k);
            FU fu11 = fu10.add(dynamicunitlist5.getRangeValue(0, j1));
            floatelement1.setLeftDistance(fu9);
            floatelement1.setTopDistance(fu11);
        }
    }

    private void doWithCellElementDragged(int i, int j, CellSelection cellselection)
    {
        ElementCasePane elementcasepane = grid.getElementCasePane();
        Rectangle rectangle = cellselection.toRectangle();
        ColumnRow columnrow = GridUtils.getAdjustEventColumnRow(elementcasepane, i, j);
        if(rectangle.contains(columnrow.getColumn(), columnrow.getRow()))
        {
            grid.getDragRectangle().setBounds(rectangle);
        } else
        {
            int k = i - oldEvtX;
            int l = j - oldEvtY;
            if(Math.abs(l) > Math.abs(k))
            {
                grid.getDragRectangle().x = rectangle.x;
                grid.getDragRectangle().width = rectangle.width;
                if(l >= 0)
                {
                    if((elementcasepane instanceof ECBlockPane) && j > elementcasepane.getBounds().height - ECBlockGap)
                        return;
                    grid.getDragRectangle().y = rectangle.y;
                    grid.getDragRectangle().height = (columnrow.getRow() - rectangle.y) + 1;
                } else
                if(columnrow.getRow() >= rectangle.y && columnrow.getRow() < rectangle.y + rectangle.height)
                {
                    grid.getDragRectangle().y = rectangle.y;
                    grid.getDragRectangle().height = rectangle.height;
                } else
                {
                    grid.getDragRectangle().y = rectangle.y;
                    grid.getDragRectangle().height = (rectangle.y - columnrow.getRow()) + rectangle.height;
                }
            } else
            {
                grid.getDragRectangle().y = rectangle.y;
                grid.getDragRectangle().height = rectangle.height;
                if(k >= 0)
                {
                    if((elementcasepane instanceof ECBlockPane) && i > elementcasepane.getBounds().width - ECBlockGap)
                        return;
                    grid.getDragRectangle().x = rectangle.x;
                    grid.getDragRectangle().width = (columnrow.getColumn() - rectangle.x) + 1;
                } else
                if(columnrow.getColumn() >= rectangle.x && columnrow.getColumn() < rectangle.x + rectangle.width)
                {
                    grid.getDragRectangle().x = rectangle.x;
                    grid.getDragRectangle().width = rectangle.width;
                } else
                {
                    grid.getDragRectangle().x = columnrow.getColumn();
                    grid.getDragRectangle().width = (rectangle.x - columnrow.getColumn()) + rectangle.width;
                }
            }
        }
        elementcasepane.ensureColumnRowVisible(columnrow.getColumn() + 1, columnrow.getRow() + 1);
    }

    private void doShiftSelectCell(double d, double d1)
    {
        ElementCasePane elementcasepane = grid.getElementCasePane();
        Selection selection = elementcasepane.getSelection();
        if(selection instanceof FloatSelection)
            return;
        ColumnRow columnrow = GridUtils.getAdjustEventColumnRow(elementcasepane, d, d1);
        int i = columnrow.getColumn();
        int j = columnrow.getRow();
        CellSelection cellselection = ((CellSelection)selection).clone();
        int k = tempOldSelectedCell.getColumn();
        int l = tempOldSelectedCell.getRow();
        int i1 = i < k ? i : k;
        int j1 = j < l ? j : l;
        int k1 = Math.abs(i - k) + 1;
        int l1 = Math.abs(j - l) + 1;
        Rectangle rectangle = new Rectangle(i1, j1, k1, l1);
        Rectangle rectangle1 = grid.caculateIntersectsUnion(elementcasepane.getEditingElementCase(), rectangle);
        cellselection.setBounds(rectangle1.x, rectangle1.y, rectangle1.width, rectangle1.height);
        cellselection.clearCellRectangles(cellselection.getCellRectangleCount() - 1);
        cellselection.addCellRectangle(rectangle1);
        elementcasepane.setSelection(cellselection);
        if(!elementcasepane.mustInVisibleRange())
            elementcasepane.ensureColumnRowVisible(i, j);
    }

    private void doControlSelectCell(double d, double d1)
    {
        ElementCasePane elementcasepane = grid.getElementCasePane();
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        Selection selection = elementcasepane.getSelection();
        if(selection instanceof FloatSelection)
            return;
        ColumnRow columnrow = GridUtils.getAdjustEventColumnRow(elementcasepane, d, d1);
        CellSelection cellselection = ((CellSelection)selection).clone();
        cellselection.setSelectedType(((CellSelection)selection).getSelectedType());
        CellElement cellelement = templateelementcase.getCellElement(columnrow.getColumn(), columnrow.getRow());
        if(cellelement == null)
        {
            cellselection.setBounds(columnrow.getColumn(), columnrow.getRow(), 1, 1);
            int i = cellselection.containsCell(columnrow.getColumn(), columnrow.getRow());
            if(i == -1)
                cellselection.addCellRectangle(new Rectangle(columnrow.getColumn(), columnrow.getRow(), 1, 1));
            else
                cellselection.clearCellRectangles(i);
        } else
        {
            cellselection.setBounds(cellelement.getColumn(), cellelement.getRow(), cellelement.getColumnSpan(), cellelement.getRowSpan());
            cellselection.addCellRectangle(new Rectangle(cellelement.getColumn(), cellelement.getRow(), cellelement.getColumnSpan(), cellelement.getRowSpan()));
        }
        elementcasepane.setSelection(cellselection);
        if(!elementcasepane.mustInVisibleRange())
            elementcasepane.ensureColumnRowVisible(columnrow.getColumn(), columnrow.getRow());
    }

    private void mouseMoveOnGrid(int i, int j)
    {
        grid.setToolTipText(null);
        if(grid.getDrawingFloatElement() != null)
        {
            grid.setCursor(UIConstants.DRAW_CURSOR);
        } else
        {
            Object aobj[] = GridUtils.getAboveFloatElementCursor(grid.getElementCasePane(), i, j);
            if(!ArrayUtils.isEmpty(aobj))
                grid.setCursor((Cursor)aobj[1]);
            else
                doMouseMoveOnCells(i, j);
        }
    }

    private void doMouseMoveOnCells(int i, int j)
    {
        ElementCasePane elementcasepane = grid.getElementCasePane();
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        if(DesignerContext.getFormatState() != 0)
            grid.setCursor(UIConstants.FORMAT_BRUSH_CURSOR);
        else
            grid.setCursor(UIConstants.CELL_DEFAULT_CURSOR);
        ColumnRow columnrow = GridUtils.getEventColumnRow(elementcasepane, i, j);
        TemplateCellElement templatecellelement = templateelementcase.getTemplateCellElement(columnrow.getColumn(), columnrow.getRow());
        if(templatecellelement != null)
            setCursorAndToolTips(templatecellelement, templateelementcase);
        int k = isMoveCellSelection(i, j);
        if(k == 1)
            grid.setCursor(new Cursor(13));
        else
        if(k == 2)
            grid.setCursor(new Cursor(1));
    }

    private void setCursorAndToolTips(TemplateCellElement templatecellelement, TemplateElementCase templateelementcase)
    {
        int i = ScreenResolution.getScreenResolution();
        DynamicUnitList dynamicunitlist = ReportHelper.getColumnWidthList(templateelementcase);
        DynamicUnitList dynamicunitlist1 = ReportHelper.getRowHeightList(templateelementcase);
        CellGUIAttr cellguiattr = templatecellelement.getCellGUIAttr();
        if(cellguiattr == null)
            cellguiattr = CellGUIAttr.DEFAULT_CELLGUIATTR;
        grid.setToolTipText(cellguiattr.getTooltipText());
        double d = dynamicunitlist.getRangeValue(grid.getHorizontalValue(), templatecellelement.getColumn()).toPixD(i) + 4D;
        double d1 = dynamicunitlist1.getRangeValue(grid.getVerticalValue(), templatecellelement.getRow() + templatecellelement.getRowSpan()).toPixD(i) + 4D;
        if(StringUtils.isNotBlank(grid.getToolTipText()))
            grid.setTooltipLocation(d + 30D, d1);
    }

    private int isMoveCellSelection(double d, double d1)
    {
        ElementCasePane elementcasepane = grid.getElementCasePane();
        Selection selection = elementcasepane.getSelection();
        if(!(selection instanceof CellSelection))
            return 0;
        if((selection instanceof CellSelection) && ((CellSelection)selection).getCellRectangleCount() != 1)
            return 0;
        CellSelection cellselection = (CellSelection)selection;
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        DynamicUnitList dynamicunitlist = ReportHelper.getColumnWidthList(templateelementcase);
        DynamicUnitList dynamicunitlist1 = ReportHelper.getRowHeightList(templateelementcase);
        int i = ScreenResolution.getScreenResolution();
        double d2 = dynamicunitlist.getRangeValue(grid.getHorizontalValue(), cellselection.getColumn()).toPixD(i);
        double d3 = dynamicunitlist.getRangeValue(grid.getHorizontalValue(), cellselection.getColumn() + cellselection.getColumnSpan()).toPixD(i);
        double d4 = dynamicunitlist1.getRangeValue(grid.getVerticalValue(), cellselection.getRow()).toPixD(i);
        double d5 = dynamicunitlist1.getRangeValue(grid.getVerticalValue(), cellselection.getRow() + cellselection.getRowSpan()).toPixD(i);
        if(fitCellSelectionBottomRight(d, d1, d3, d5))
            return 2;
        double d6 = 1.0D;
        if(fitCellSelection(d, d2, d3, d6))
        {
            if(d1 >= d4 - d6 && d1 <= d5 + d6)
                return 1;
        } else
        if(fitCellSelection(d1, d4, d5, d6) && d >= d2 - d6 && d <= d3 + d6)
            return 1;
        return 0;
    }

    private boolean fitCellSelection(double d, double d1, double d2, double d3)
    {
        return d >= d1 - d3 && d <= d1 + d3 || d >= d2 - d3 && d <= d2 + d3;
    }

    private boolean fitCellSelectionBottomRight(double d, double d1, double d2, double d3)
    {
        return d > d2 - 1.5D && d < d2 + 2.5D && d1 > d3 - 1.5D && d3 < d3 + 2.5D;
    }

    private void doOneClickSelection(int i, int j, boolean flag, boolean flag1)
    {
        ElementCasePane elementcasepane = grid.getElementCasePane();
        Object aobj[] = GridUtils.getAboveFloatElementCursor(elementcasepane, i, j);
        if(!ArrayUtils.isEmpty(aobj))
            doSelectFloatElement(aobj, i, j);
        else
        if(flag)
            doShiftSelectCell(i, j);
        else
        if(flag1)
        {
            doControlSelectCell(i, j);
        } else
        {
            ColumnRow columnrow = GridUtils.getEventColumnRow(elementcasepane, i, j);
            int k = elementcasepane.ensureColumnRowVisible(columnrow.getColumn(), columnrow.getRow());
            if(k == 0)
                GridUtils.doSelectCell(elementcasepane, columnrow.getColumn(), columnrow.getRow());
            else
            if(k == 2)
                GridUtils.doSelectCell(elementcasepane, columnrow.getColumn(), columnrow.getRow() - 1);
            else
            if(k == 1)
                GridUtils.doSelectCell(elementcasepane, columnrow.getColumn() - 1, columnrow.getRow());
            else
                GridUtils.doSelectCell(elementcasepane, columnrow.getColumn() - 1, columnrow.getRow() - 1);
            return;
        }
    }

    private void doSelectFloatElement(Object aobj[], int i, int j)
    {
        ElementCasePane elementcasepane = grid.getElementCasePane();
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        FloatElement floatelement = (FloatElement)aobj[0];
        String s = floatelement.getName();
        elementcasepane.setSelection(new FloatSelection(s));
        double ad[] = GridUtils.caculateFloatElementLocations(floatelement, ReportHelper.getColumnWidthList(templateelementcase), ReportHelper.getRowHeightList(templateelementcase), elementcasepane.getGrid().getVerticalValue(), elementcasepane.getGrid().getHorizontalValue());
        int k = ((Cursor)aobj[1]).getType();
        if(k == 13)
        {
            oldEvtX = i;
            oldEvtY = j;
            FloatElement floatelement1 = templateelementcase.getFloatElement(s);
            int l = ScreenResolution.getScreenResolution();
            int i1 = grid.getVerticalValue();
            int j1 = grid.getHorizontalValue();
            DynamicUnitList dynamicunitlist = ReportHelper.getColumnWidthList(templateelementcase);
            DynamicUnitList dynamicunitlist1 = ReportHelper.getRowHeightList(templateelementcase);
            oldLocationX = FU.getInstance(floatelement1.getLeftDistance().toFU() - dynamicunitlist.getRangeValue(0, j1).toFU()).toPixI(l);
            oldLocationY = FU.getInstance(floatelement1.getTopDistance().toFU() - dynamicunitlist1.getRangeValue(0, i1).toFU()).toPixI(l);
            if(floatNamePointMap == null)
                floatNamePointMap = new HashMap();
            floatNamePointMap.clear();
            FloatElement floatelement2 = templateelementcase.getFloatElement(s);
            int k1 = FU.getInstance(floatelement2.getLeftDistance().toFU() - dynamicunitlist.getRangeValue(0, j1).toFU()).toPixI(l) - oldLocationX;
            int l1 = FU.getInstance(floatelement2.getTopDistance().toFU() - dynamicunitlist1.getRangeValue(0, i1).toFU()).toPixI(l) - oldLocationY;
            floatNamePointMap.put(s, new Point(k1, l1));
        } else
        if(k == 6)
            setOld_X_AndOld_Y(ad[2], ad[3]);
        else
        if(k == 7)
            setOld_X_AndOld_Y(ad[0], ad[3]);
        else
        if(k == 5)
            setOld_X_AndOld_Y(ad[0], ad[1]);
        else
        if(k == 4)
            setOld_X_AndOld_Y(ad[2], ad[1]);
        else
        if(k == 8)
            setOld_X_AndOld_Y(ad[0], ad[3]);
        else
        if(k == 9)
            setOld_X_AndOld_Y(ad[0], ad[1]);
        else
        if(k == 10)
            setOld_X_AndOld_Y(ad[2], ad[1]);
        else
        if(k == 11)
            setOld_X_AndOld_Y(ad[0], ad[1]);
    }

    private void setOld_X_AndOld_Y(double d, double d1)
    {
        oldEvtX = (int)d;
        oldEvtY = (int)d1;
    }

    public void mouseWheelMoved(MouseWheelEvent mousewheelevent)
    {
        ElementCasePane elementcasepane = grid.getElementCasePane();
        if(elementcasepane.isHorizontalScrollBarVisible())
            elementcasepane.getVerticalScrollBar().setValue(elementcasepane.getVerticalScrollBar().getValue() + mousewheelevent.getWheelRotation() * 3);
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

}
