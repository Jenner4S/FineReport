// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid;

import com.fr.base.*;
import com.fr.design.DesignerEnvManager;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.ColumnRow;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.UNIT;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

// Referenced classes of package com.fr.grid:
//            GridHeader, Grid, GridUtils

public abstract class AbstractGridHeaderMouseHandler extends MouseInputAdapter
{
    private abstract class ScrollAction
    {

        final AbstractGridHeaderMouseHandler this$0;

        public abstract boolean run(MouseEvent mouseevent, int i, double d, double d1, int j, 
                int k, ElementCase elementcase, DynamicUnitList dynamicunitlist);

        private ScrollAction()
        {
            this$0 = AbstractGridHeaderMouseHandler.this;
            super();
        }

    }


    protected static final int SEPARATOR_GAP = 5;
    private GridHeader gHeader;
    private int dragType;
    private boolean isMultiSelectDragPermited;
    private int startMultiSelectIndex;
    private int endMultiSelectIndex;
    private boolean isDragPermited;
    private int dragIndex;
    private JToolTip tip;
    private JWindow tipWindow;
    private ScrollAction DRAG_ACTION;
    private ScrollAction PRESS_ACTION;

    public AbstractGridHeaderMouseHandler(GridHeader gridheader)
    {
        dragType = 0;
        isMultiSelectDragPermited = false;
        startMultiSelectIndex = 0;
        endMultiSelectIndex = 0;
        isDragPermited = false;
        dragIndex = 0;
        tip = null;
        tipWindow = null;
        DRAG_ACTION = new ScrollAction() {

            final AbstractGridHeaderMouseHandler this$0;

            public boolean run(MouseEvent mouseevent, int i, double d, double d1, int j, 
                    int k, ElementCase elementcase, DynamicUnitList dynamicunitlist)
            {
                int l = ScreenResolution.getScreenResolution();
                Method method = null;
                try
                {
                    method = com/fr/report/elementcase/ElementCase.getMethod(methodName(), new Class[] {
                        Integer.TYPE, com/fr/stable/unit/UNIT
                    });
                }
                catch(Exception exception)
                {
                    FRContext.getLogger().error(exception.getMessage(), exception);
                }
                if(between(mouseevent, d, d1))
                {
                    if(i >= dragIndex)
                    {
                        try
                        {
                            method.invoke(elementcase, new Object[] {
                                Integer.valueOf(dragIndex), FU.valueOfPix(evtOffset(mouseevent, k), l)
                            });
                        }
                        catch(Exception exception1)
                        {
                            FRContext.getLogger().error(exception1.getMessage(), exception1);
                        }
                    } else
                    {
                        try
                        {
                            method.invoke(elementcase, new Object[] {
                                Integer.valueOf(i), FU.valueOfPix(evtOffset(mouseevent, (int)d), l)
                            });
                        }
                        catch(Exception exception2)
                        {
                            FRContext.getLogger().error(exception2.getMessage(), exception2);
                        }
                        for(int i1 = dragIndex - 1; i1 > i; i1--)
                            try
                            {
                                method.invoke(elementcase, new Object[] {
                                    Integer.valueOf(i1), UNIT.ZERO
                                });
                            }
                            catch(Exception exception3)
                            {
                                FRContext.getLogger().error(exception3.getMessage(), exception3);
                            }

                    }
                    return true;
                } else
                {
                    return false;
                }
            }

            
            {
                this$0 = AbstractGridHeaderMouseHandler.this;
                super();
            }
        }
;
        PRESS_ACTION = new ScrollAction() {

            final AbstractGridHeaderMouseHandler this$0;

            public boolean run(MouseEvent mouseevent, int i, double d, double d1, int j, 
                    int k, ElementCase elementcase, DynamicUnitList dynamicunitlist)
            {
                int l = ScreenResolution.getScreenResolution();
                if(isOnSeparatorLineIncludeZero(mouseevent, d1, j) || isOnNormalSeparatorLine(mouseevent, d1))
                {
                    dragType = 1;
                    isDragPermited = true;
                    dragIndex = i;
                    showToolTip(mouseevent, createToolTipString(dynamicunitlist.get(dragIndex).toPixD(l), dynamicunitlist.getRangeValue(0, dragIndex + 1).toPixD(l)));
                    return true;
                }
                if(between(mouseevent, d, d1))
                {
                    dragType = 2;
                    isMultiSelectDragPermited = true;
                    startMultiSelectIndex = i;
                    showToolTip(mouseevent, getSelectedHeaderTooltip(1));
                    return true;
                } else
                {
                    return false;
                }
            }

            
            {
                this$0 = AbstractGridHeaderMouseHandler.this;
                super();
            }
        }
;
        gHeader = gridheader;
    }

    public void setStartMultiSelectIndex(int i)
    {
        startMultiSelectIndex = i;
    }

    public void setEndMultiSelectIndex(int i)
    {
        endMultiSelectIndex = i;
    }

    protected abstract DynamicUnitList getSizeList(ElementCase elementcase);

    protected abstract String methodName();

    protected abstract int getScrollValue(ElementCasePane elementcasepane);

    protected abstract int getScrollExtent(ElementCasePane elementcasepane);

    protected abstract int getBeginValue(ElementCasePane elementcasepane);

    protected abstract String getSelectedHeaderTooltip(int i);

    protected abstract boolean isOnSeparatorLineIncludeZero(MouseEvent mouseevent, double d, double d1);

    protected abstract boolean between(MouseEvent mouseevent, double d, double d1);

    protected abstract boolean isOnNormalSeparatorLine(MouseEvent mouseevent, double d);

    private void iterateScrollBar(ElementCasePane elementcasepane, MouseEvent mouseevent, ScrollAction scrollaction)
    {
        com.fr.report.elementcase.TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        DynamicUnitList dynamicunitlist = getSizeList(templateelementcase);
        int i = getScrollValue(elementcasepane);
        int j = getScrollExtent(elementcasepane);
        int k = i + j + 1;
        int l = getBeginValue(elementcasepane);
        double d = 0.0D;
        double d2 = 0.0D;
        double d3 = 0.0D;
        int i1 = ScreenResolution.getScreenResolution();
        int j1 = l;
        do
        {
            if(j1 >= k)
                break;
            if(j1 == 0)
                j1 = i;
            d += d2;
            d2 = dynamicunitlist.get(j1).toPixD(i1);
            double d1 = d + Math.max(1.0D, d2);
            if(j1 == dragIndex)
                d3 = d + 1.0D;
            if(scrollaction.run(mouseevent, j1, d, d1, (int)d2, (int)d3, templateelementcase, dynamicunitlist))
                break;
            j1++;
        } while(true);
    }

    protected abstract UIPopupMenu createPopupMenu(ElementCasePane elementcasepane, MouseEvent mouseevent, int i);

    public void mousePressed(MouseEvent mouseevent)
    {
        if(!gHeader.isEnabled())
            return;
        ElementCasePane elementcasepane = gHeader.getElementCasePane();
        elementcasepane.getGrid().stopEditing();
        ColumnRow columnrow = GridUtils.getAdjustEventColumnRow(elementcasepane, mouseevent.getX(), mouseevent.getY());
        iterateScrollBar(elementcasepane, mouseevent, PRESS_ACTION);
        if(SwingUtilities.isRightMouseButton(mouseevent))
        {
            Selection selection = elementcasepane.getSelection();
            resetSelectionByRightButton(columnrow, selection, elementcasepane);
            UIPopupMenu uipopupmenu = createPopupMenu(elementcasepane, mouseevent, Math.max(dragIndex, Math.max(startMultiSelectIndex, endMultiSelectIndex)));
            if(uipopupmenu != null)
                GUICoreUtils.showPopupMenu(uipopupmenu, gHeader, mouseevent.getX() + 1, mouseevent.getY() + 1);
        } else
        if(dragType == 2)
            if(mouseevent.isShiftDown())
            {
                doShiftSelectHeader(elementcasepane, mouseevent.getX(), mouseevent.getY());
            } else
            {
                endMultiSelectIndex = startMultiSelectIndex;
                resetGridSelectionBySelect(getColumnOrRowByGridHeader(columnrow), elementcasepane);
            }
        elementcasepane.repaint();
    }

    protected abstract void resetSelectionByRightButton(ColumnRow columnrow, Selection selection, ElementCasePane elementcasepane);

    protected abstract int doChooseFrom();

    private void doShiftSelectHeader(ElementCasePane elementcasepane, double d, double d1)
    {
        ColumnRow columnrow = GridUtils.getAdjustEventColumnRow(elementcasepane, d, d1);
        int i = columnrow.getColumn();
        int j = columnrow.getRow();
        CellSelection cellselection = ((CellSelection)elementcasepane.getSelection()).clone();
        int k = cellselection.getColumn();
        int l = cellselection.getRow();
        int i1 = 0;
        int j1 = 0;
        int k1 = 0;
        int l1 = 0;
        if(i == k)
        {
            i1 = i;
            j1 = Math.min(j, l);
            k1 = cellselection.getColumnSpan();
            l1 = Math.abs(j - l) + 1;
        } else
        if(j == l)
        {
            j1 = j;
            i1 = Math.min(i, k);
            k1 = Math.abs(i - k) + 1;
            l1 = cellselection.getRowSpan();
        }
        Rectangle rectangle = new Rectangle(i1, j1, k1, l1);
        cellselection.setBounds(i1, j1, k1, l1);
        cellselection.clearCellRectangles(cellselection.getCellRectangleCount() - 1);
        cellselection.addCellRectangle(rectangle);
        cellselection.setSelectedType(doChooseFrom());
        elementcasepane.setSelection(cellselection);
        elementcasepane.ensureColumnRowVisible(i, j);
    }

    protected abstract Rectangle resetSelectedBoundsByShift(Rectangle rectangle, ColumnRow columnrow, ElementCasePane elementcasepane);

    protected abstract void resetGridSelectionBySelect(int i, ElementCasePane elementcasepane);

    private String createToolTipString(double d, double d1)
    {
        short word0 = DesignerEnvManager.getEnvManager().getReportLengthUnit();
        int i = ScreenResolution.getScreenResolution();
        FU fu = FU.valueOfPix((int)d, i);
        FU fu1 = FU.valueOfPix((int)d1, i);
        String s;
        double d2;
        double d3;
        if(word0 == 3)
        {
            d2 = fu.toPTValue4Scale2();
            d3 = fu1.toPTValue4Scale2();
            s = Inter.getLocText("Unit_PT");
        } else
        if(word0 == 1)
        {
            d2 = fu.toCMValue4Scale2();
            d3 = fu1.toCMValue4Scale2();
            s = Inter.getLocText("Unit_CM");
        } else
        if(word0 == 2)
        {
            d2 = fu.toINCHValue4Scale3();
            d3 = fu1.toINCHValue4Scale3();
            s = Inter.getLocText("Unit_INCH");
        } else
        {
            d2 = fu.toMMValue4Scale2();
            d3 = fu1.toMMValue4Scale2();
            s = Inter.getLocText("Unit_MM");
        }
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(Utils.convertNumberStringToString(new Float(d2))).append('/').append(Utils.convertNumberStringToString(new Float(d3))).append(s).append('(').append(Utils.doubleToString(d)).append('/').append(Utils.doubleToString(d1)).append(Inter.getLocText("px")).append(')');
        return stringbuilder.toString();
    }

    private void showToolTip(MouseEvent mouseevent, String s)
    {
        if(tipWindow == null)
        {
            tipWindow = new JWindow();
            tip = gHeader.createToolTip();
            tipWindow.getContentPane().add(tip, "Center");
        }
        tip.setTipText(s);
        Point point = getTipLocationByMouseEvent(mouseevent, gHeader, tip.getPreferredSize());
        tipWindow.setLocation(point.x, point.y);
        tipWindow.pack();
        tipWindow.setVisible(true);
    }

    protected abstract Point getTipLocationByMouseEvent(MouseEvent mouseevent, GridHeader gridheader, Dimension dimension);

    private void hideToolTip()
    {
        if(tipWindow != null)
            tipWindow.setVisible(false);
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
        if(!gHeader.isEnabled())
            return;
        ElementCasePane elementcasepane = gHeader.getElementCasePane();
        elementcasepane.getGrid().stopEditing();
        isMultiSelectDragPermited = false;
        isDragPermited = false;
        hideToolTip();
        if(dragType == 1)
        {
            JScrollBar jscrollbar = elementcasepane.getVerticalScrollBar();
            if(jscrollbar != null)
                jscrollbar.setValue(jscrollbar.getValue());
            JScrollBar jscrollbar1 = elementcasepane.getHorizontalScrollBar();
            if(jscrollbar1 != null)
                jscrollbar1.setValue(jscrollbar1.getValue());
            elementcasepane.fireTargetModified();
        }
        dragType = 0;
        dragIndex = 0;
    }

    protected abstract int[] getGridSelectionIndices(CellSelection cellselection);

    public void mouseDragged(MouseEvent mouseevent)
    {
        if(!gHeader.isEnabled() || dragType == 0 || SwingUtilities.isRightMouseButton(mouseevent))
            return;
        ElementCasePane elementcasepane = gHeader.getElementCasePane();
        com.fr.report.elementcase.TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        if(elementcasepane.getSelection() instanceof FloatSelection)
            return;
        CellSelection cellselection = ((CellSelection)elementcasepane.getSelection()).clone();
        elementcasepane.getGrid().stopEditing();
        if(dragType == 2)
        {
            if(!isMultiSelectDragPermited)
                return;
            ColumnRow columnrow = GridUtils.getAdjustEventColumnRow(elementcasepane, mouseevent.getX(), mouseevent.getY());
            endMultiSelectIndex = getColumnOrRowByGridHeader(columnrow);
            resetGridSelectionByDrag(cellselection, elementcasepane, startMultiSelectIndex, endMultiSelectIndex);
            cellselection.setSelectedType(doChooseFrom());
            elementcasepane.setSelection(cellselection);
            if(!elementcasepane.mustInVisibleRange())
                elementcasepane.ensureColumnRowVisible(columnrow.getColumn(), columnrow.getRow());
            setToolTipText2(getSelectedHeaderTooltip(Math.abs(startMultiSelectIndex - endMultiSelectIndex) + 1));
        } else
        if(dragType == 1)
        {
            if(BaseUtils.isAuthorityEditing())
                return;
            if(!isDragPermited)
                return;
            iterateScrollBar(elementcasepane, mouseevent, DRAG_ACTION);
            DynamicUnitList dynamicunitlist = getSizeList(templateelementcase);
            int i = ScreenResolution.getScreenResolution();
            setToolTipText2(createToolTipString(dynamicunitlist.get(dragIndex).toPixD(i), dynamicunitlist.getRangeValue(0, dragIndex + 1).toPixD(i)));
        }
        elementcasepane.repaint();
    }

    protected abstract void resetGridSelectionByDrag(CellSelection cellselection, ElementCasePane elementcasepane, int i, int j);

    protected abstract int evtOffset(MouseEvent mouseevent, int i);

    protected abstract int getColumnOrRowByGridHeader(ColumnRow columnrow);

    private void setToolTipText2(String s)
    {
        if(tip == null)
        {
            return;
        } else
        {
            tip.setTipText(s);
            tip.setSize(tip.getPreferredSize());
            tipWindow.pack();
            tipWindow.repaint();
            return;
        }
    }

    public void mouseMoved(MouseEvent mouseevent)
    {
        if(!gHeader.isEnabled())
            return;
        ElementCasePane elementcasepane = gHeader.getElementCasePane();
        com.fr.report.elementcase.TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        gHeader.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(BaseUtils.readImage((new StringBuilder()).append("/com/fr/base/images/cell/cursor/").append(nameOfSelectCursorGIF()).append(".gif").toString()), new Point(16, 16), nameOfSelectCursorGIF()));
        DynamicUnitList dynamicunitlist = getSizeList(templateelementcase);
        int i = getScrollValue(elementcasepane);
        int j = getScrollExtent(elementcasepane);
        int k = i + j + 1;
        int l = getBeginValue(elementcasepane);
        double d = 0.0D;
        double d2 = 0.0D;
        int i1 = ScreenResolution.getScreenResolution();
        int j1 = l;
        do
        {
            if(j1 >= k)
                break;
            if(j1 == 0)
                j1 = i;
            d += d2;
            d2 = dynamicunitlist.get(j1).toPixD(i1);
            double d1 = d2 > 0.0D ? d + d2 : d + 1.0D;
            if(BaseUtils.isAuthorityEditing())
                break;
            if(isOnSeparatorLineIncludeZero(mouseevent, d1, d2))
            {
                gHeader.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(BaseUtils.readImage((new StringBuilder()).append("/com/fr/base/images/cell/cursor/").append(nameOfSplitCursorGIF()).append(".gif").toString()), new Point(16, 16), nameOfSplitCursorGIF()));
                break;
            }
            if(isOnNormalSeparatorLine(mouseevent, d1))
            {
                gHeader.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(BaseUtils.readImage((new StringBuilder()).append("/com/fr/base/images/cell/cursor/").append(nameOfMoveCursorGIF()).append(".gif").toString()), new Point(16, 16), nameOfMoveCursorGIF()));
                break;
            }
            j1++;
        } while(true);
    }

    protected abstract String nameOfSelectCursorGIF();

    protected abstract String nameOfSplitCursorGIF();

    protected abstract String nameOfMoveCursorGIF();








}
