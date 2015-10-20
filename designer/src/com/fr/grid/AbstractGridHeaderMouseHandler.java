package com.fr.grid;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;

import javax.swing.JScrollBar;
import javax.swing.JToolTip;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import com.fr.base.BaseUtils;
import com.fr.base.DynamicUnitList;
import com.fr.base.FRContext;
import com.fr.base.ScreenResolution;
import com.fr.base.Utils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.ColumnRow;
import com.fr.stable.Constants;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.UNIT;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * @editor zhou
 * @since 2012-3-23����11:54:14
 */
public abstract class AbstractGridHeaderMouseHandler extends MouseInputAdapter {
    protected static final int SEPARATOR_GAP = 5;

    private GridHeader gHeader;

    private int dragType = GridUtils.DRAG_NONE;
    //james �Ƿ�Ϊѡ�����е�drag
    private boolean isMultiSelectDragPermited = false;
    private int startMultiSelectIndex = 0;
    private int endMultiSelectIndex = 0;

    // james
    private boolean isDragPermited = false;
    private int dragIndex = 0;
    private JToolTip tip = null;
    private JWindow tipWindow = null;

    public AbstractGridHeaderMouseHandler(GridHeader gHeader) {
        this.gHeader = gHeader;
    }

    public void setStartMultiSelectIndex(int index) {
        this.startMultiSelectIndex = index;
    }

    public void setEndMultiSelectIndex(int index) {
        this.endMultiSelectIndex = index;
    }

    protected abstract DynamicUnitList getSizeList(ElementCase elCase);

    protected abstract String methodName();

    protected abstract int getScrollValue(ElementCasePane casePane);

    protected abstract int getScrollExtent(ElementCasePane casePane);

    protected abstract int getBeginValue(ElementCasePane casePane);

    protected abstract String getSelectedHeaderTooltip(int selectedSize);

    /**
     * Checks whether is on zero separator line.
     */
    protected abstract boolean isOnSeparatorLineIncludeZero(MouseEvent evt, double tmpSize2, double tmpIncreaseSize);

    protected abstract boolean between(MouseEvent evt, double from, double to);

    /**
     * Checks whether is on normal separator line.
     */
    protected abstract boolean isOnNormalSeparatorLine(MouseEvent evt, double tmpSize2);

    private abstract class ScrollAction {
        public abstract boolean run(MouseEvent evt, int index, double tmpSize1, double tmpSize2, int tmpIncreaseSize, int oldEndValueSize, ElementCase report, DynamicUnitList sizeList);
    }

    private ScrollAction DRAG_ACTION = new ScrollAction() {
        @Override
        public boolean run(MouseEvent evt, int index, double tmpSize1, double tmpSize2, int tmpIncreaseSize, int oldEndValueSize, ElementCase report, DynamicUnitList sizeList) {

            int resolution = ScreenResolution.getScreenResolution();
            // richer:�����ô����ԭ���ǵ������и��п����Ҫ֪ͨ�ۺϿ�ı�߽�
            Method method = null;
            try {
                method = ElementCase.class.getMethod(methodName(), int.class, UNIT.class);
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
            if (between(evt, tmpSize1, tmpSize2)) {
                if (index >= dragIndex) {
                    try {
                        method.invoke(report, dragIndex, FU.valueOfPix(evtOffset(evt, oldEndValueSize), resolution));
                    } catch (Exception e) {
                        FRContext.getLogger().error(e.getMessage(), e);
                    }
                    //sizeList.set(dragIndex, FU.valueOfPix(evtOffset(evt, oldEndValueSize), resolution));
                } else {
                    try {
                        method.invoke(report, index, FU.valueOfPix(evtOffset(evt, (int) tmpSize1), resolution));
                    } catch (Exception e) {
                        FRContext.getLogger().error(e.getMessage(), e);
                    }
                    //sizeList.set(index, FU.valueOfPix(evtOffset(evt, (int)tmpSize1), resolution));
                    // from all to do.
                    for (int h = (dragIndex - 1); h > index; h--) {
                        try {
                            method.invoke(report, h, UNIT.ZERO);
                        } catch (Exception e) {
                            FRContext.getLogger().error(e.getMessage(), e);
                        }
                        //sizeList.set(h, UNIT.ZERO);
                    }
                }

                return true;
            }

            return false;
        }
    };

    private ScrollAction PRESS_ACTION = new ScrollAction() {
        @Override
        public boolean run(MouseEvent evt, int index, double tmpSize1, double tmpSize2, int tmpIncreaseSize, int oldEndValueSize, ElementCase report, DynamicUnitList sizeList) {
            int resolution = ScreenResolution.getScreenResolution();

            if (isOnSeparatorLineIncludeZero(evt, tmpSize2, tmpIncreaseSize) || isOnNormalSeparatorLine(evt, tmpSize2)) {
                dragType = GridUtils.DRAG_CELL_SIZE;
                isDragPermited = true;
                dragIndex = index;
                showToolTip(evt, createToolTipString(sizeList.get(dragIndex).toPixD(resolution), sizeList.getRangeValue(0, dragIndex + 1).toPixD(resolution)));
                return true;
            }
            if (between(evt, tmpSize1, tmpSize2)) {
                dragType = GridUtils.DRAG_SELECT_UNITS;
                isMultiSelectDragPermited = true;
                startMultiSelectIndex = index;
                showToolTip(evt, getSelectedHeaderTooltip(1));
                return true;
            }
            return false;
        }
    };

    private void iterateScrollBar(ElementCasePane ePane, MouseEvent evt, ScrollAction action) {
        ElementCase report = ePane.getEditingElementCase();
        DynamicUnitList sizeList = getSizeList(report);

        int scrollValue = getScrollValue(ePane);
        int scrollExtent = getScrollExtent(ePane);
        int endValue = scrollValue + scrollExtent + 1;

        int beginValue = getBeginValue(ePane);

        double tmpSize1 = 0;
        double tmpSize2;
        double tmpIncreaseSize = 0;
        double oldEndValueSize = 0;

        int resolution = ScreenResolution.getScreenResolution();

        for (int index = beginValue; index < endValue; index++) { // denny:
            // beginValue
            if (index == 0) {
                index = scrollValue;
            }

            tmpSize1 += tmpIncreaseSize;
            tmpIncreaseSize = sizeList.get(index).toPixD(resolution);
            tmpSize2 = tmpSize1 + Math.max(1, tmpIncreaseSize);

            if (index == dragIndex) {
                oldEndValueSize = tmpSize1 + 1;
            }

            if (action.run(evt, index, tmpSize1, tmpSize2, (int) tmpIncreaseSize, (int) oldEndValueSize, report, sizeList)) {
                break;
            }
        }
    }

    protected abstract UIPopupMenu createPopupMenu(ElementCasePane ePane, MouseEvent evt, int index);


    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    @Override
    public void mousePressed(MouseEvent evt) {
        if (!this.gHeader.isEnabled()) {
            return;
        }

        ElementCasePane ePane = this.gHeader.getElementCasePane();
        ePane.getGrid().stopEditing();// james ֹͣ��ǰ�����б༭

        // peter:ѡ�и���λ��.
        ColumnRow selectedCellPoint = GridUtils.getAdjustEventColumnRow(ePane, evt.getX(), evt.getY());

        iterateScrollBar(ePane, evt, PRESS_ACTION);

        // popu menu
        if (SwingUtilities.isRightMouseButton(evt)) {
            Selection cs = ePane.getSelection();
            resetSelectionByRightButton(selectedCellPoint, cs, ePane);

            UIPopupMenu popupMenu = createPopupMenu(ePane, evt, Math.max(dragIndex, Math.max(this.startMultiSelectIndex, this.endMultiSelectIndex)));
            if (popupMenu != null) {
                GUICoreUtils.showPopupMenu(popupMenu, gHeader, evt.getX() + 1, evt.getY() + 1);
            }
        } else {
            if (dragType == GridUtils.DRAG_SELECT_UNITS) {
                if (evt.isShiftDown()) {// shift selection.
                    this.doShiftSelectHeader(ePane, evt.getX(), evt.getY());
                } else {
                    this.endMultiSelectIndex = this.startMultiSelectIndex;
                    resetGridSelectionBySelect(getColumnOrRowByGridHeader(selectedCellPoint), ePane);
                }
            }
        }
        // p:������һ���߳����滰,�����ӳٻ�.
        ePane.repaint();
    }

    protected abstract void resetSelectionByRightButton(ColumnRow mouseSelectedColumnRow, Selection cs, ElementCasePane ePane);


    protected abstract int doChooseFrom();

    /**
     * ѡ�ж��л��߶���
     *
     * @param ePane
     * @param evtX
     * @param evtY
     */
    private void doShiftSelectHeader(ElementCasePane ePane, double evtX, double evtY) {
        ColumnRow selectedCellPoint = GridUtils.getAdjustEventColumnRow(ePane, evtX, evtY);
        int selectedCellPointX = selectedCellPoint.getColumn();// column.
        int selectedCellPointY = selectedCellPoint.getRow();// row.
        CellSelection cs = ((CellSelection) ePane.getSelection()).clone();
        int tempOldSelectedCellX = cs.getColumn();
        int tempOldSelectedCellY = cs.getRow();
        int column = 0;
        int row = 0;
        int columnSpan = 0;
        int rowSpan = 0;
        //august ͬ�к�ͬ��
        if (selectedCellPointX == tempOldSelectedCellX) {
            column = selectedCellPointX;
            row = Math.min(selectedCellPointY, tempOldSelectedCellY);
            columnSpan = cs.getColumnSpan();
            rowSpan = Math.abs(selectedCellPointY - tempOldSelectedCellY) + 1;
        } else if (selectedCellPointY == tempOldSelectedCellY) {
            row = selectedCellPointY;
            column = Math.min(selectedCellPointX, tempOldSelectedCellX);
            columnSpan = Math.abs(selectedCellPointX - tempOldSelectedCellX) + 1;
            rowSpan = cs.getRowSpan();
        }

        Rectangle newrectangle = new Rectangle(column, row, columnSpan, rowSpan);
        cs.setBounds(column, row, columnSpan, rowSpan);
        cs.clearCellRectangles(cs.getCellRectangleCount() - 1);
        cs.addCellRectangle(newrectangle);
        cs.setSelectedType(doChooseFrom());
        ePane.setSelection(cs);
        ePane.ensureColumnRowVisible(selectedCellPointX, selectedCellPointY);
    }

    protected abstract Rectangle resetSelectedBoundsByShift(Rectangle editRectangle, ColumnRow selectedCellPoint, ElementCasePane ePane);


    protected abstract void resetGridSelectionBySelect(int index, ElementCasePane ePane);

    private String createToolTipString(double doubleValue, double totalDoubleValue) {
        int unitType = DesignerEnvManager.getEnvManager().getReportLengthUnit();
        int resolution = ScreenResolution.getScreenResolution();
        FU ulen = FU.valueOfPix((int) doubleValue, resolution);
        FU tulen = FU.valueOfPix((int) totalDoubleValue, resolution);
        String unit;
        double len, tlen;
        if (unitType == Constants.UNIT_PT) {
            len = ulen.toPTValue4Scale2();
            tlen = tulen.toPTValue4Scale2();
            unit = Inter.getLocText("Unit_PT");
        } else if (unitType == Constants.UNIT_CM) {
            len = ulen.toCMValue4Scale2();
            tlen = tulen.toCMValue4Scale2();
            unit = Inter.getLocText("Unit_CM");
        } else if (unitType == Constants.UNIT_INCH) {
            len = ulen.toINCHValue4Scale3();
            tlen = tulen.toINCHValue4Scale3();
            unit = Inter.getLocText("Unit_INCH");
        } else {
            len = ulen.toMMValue4Scale2();
            tlen = tulen.toMMValue4Scale2();
            unit = Inter.getLocText("Unit_MM");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(Utils.convertNumberStringToString(new Float(len)))
                .append('/').append(Utils.convertNumberStringToString(new Float(tlen)))
                .append(unit).append('(')
                .append(Utils.doubleToString(doubleValue)).append('/')
                .append(Utils.doubleToString(totalDoubleValue))
                .append(Inter.getLocText("px"))
                .append(')');
        return sb.toString();
    }

    private void showToolTip(MouseEvent evt, String text) {
        if (tipWindow == null) {
            tipWindow = new JWindow();

            tip = this.gHeader.createToolTip();
            tipWindow.getContentPane().add(tip, BorderLayout.CENTER);
        }
        tip.setTipText(text);

        Point tipLocation = getTipLocationByMouseEvent(evt, gHeader, tip.getPreferredSize());

        tipWindow.setLocation(tipLocation.x, tipLocation.y);
        tipWindow.pack();
        tipWindow.setVisible(true);
    }

    protected abstract Point getTipLocationByMouseEvent(MouseEvent evt, GridHeader gHeader, Dimension tipPreferredSize);

    private void hideToolTip() {
        if (tipWindow != null) {
            tipWindow.setVisible(false);
        }
    }

    @Override
    public void mouseReleased(MouseEvent evt) {
        if (!this.gHeader.isEnabled()) {
            return;
        }

        ElementCasePane ePane = gHeader.getElementCasePane();

        ePane.getGrid().stopEditing();// james ֹͣ��ǰ�����б༭
        this.isMultiSelectDragPermited = false;
        this.isDragPermited = false;
        this.hideToolTip();// hide tooltip first.
        if (dragType == GridUtils.DRAG_CELL_SIZE) {// read changed column width.
            // ��Ҫrevalidate scrollbar.
            JScrollBar vBar = ePane.getVerticalScrollBar();
            if (vBar != null) {
                vBar.setValue(vBar.getValue());
            }
            JScrollBar hBar = ePane.getHorizontalScrollBar();
            if (hBar != null) {
                hBar.setValue(hBar.getValue());
            }
            ePane.fireTargetModified();
        }

        dragType = GridUtils.DRAG_NONE;
        this.dragIndex = 0;

    }

    protected abstract int[] getGridSelectionIndices(CellSelection cs);

    @Override
    public void mouseDragged(MouseEvent evt) {
        if (!gHeader.isEnabled() || this.dragType == GridUtils.DRAG_NONE || SwingUtilities.isRightMouseButton(evt)) {
            return;
        }

        ElementCasePane ePane = gHeader.getElementCasePane();
        ElementCase report = ePane.getEditingElementCase();
        if (ePane.getSelection() instanceof FloatSelection) {
            return;
        }
        CellSelection cs = ((CellSelection) ePane.getSelection()).clone();
        ePane.getGrid().stopEditing();// james ֹͣ��ǰ�����б༭

        if (this.dragType == GridUtils.DRAG_SELECT_UNITS) {// james ����ѡ��ʱ
            if (!this.isMultiSelectDragPermited) {
                return;
            }
            ColumnRow selectedCellPoint = GridUtils.getAdjustEventColumnRow(ePane, evt.getX(), evt.getY());
            endMultiSelectIndex = getColumnOrRowByGridHeader(selectedCellPoint);
            resetGridSelectionByDrag(cs, ePane, startMultiSelectIndex, endMultiSelectIndex);
            cs.setSelectedType(doChooseFrom());
            ePane.setSelection(cs);
            if (!ePane.mustInVisibleRange()) {
                ePane.ensureColumnRowVisible(selectedCellPoint.getColumn(), selectedCellPoint.getRow());
            }
            this.setToolTipText2(this.getSelectedHeaderTooltip(Math.abs(startMultiSelectIndex - endMultiSelectIndex) + 1));
        } else if (dragType == GridUtils.DRAG_CELL_SIZE) {
            //Ȩ�ޱ༭״̬�����Ըı����еĿ��
            if (BaseUtils.isAuthorityEditing()) {
                return;
            }
            if (!(isDragPermited)) {
                return;
            }
            iterateScrollBar(ePane, evt, DRAG_ACTION);

            DynamicUnitList sizeList = getSizeList(report);
            int resolution = ScreenResolution.getScreenResolution();
            this.setToolTipText2(this.createToolTipString(sizeList.get(dragIndex).toPixD(resolution), sizeList.getRangeValue(0, dragIndex + 1).toPixD(resolution)));
        }

        ePane.repaint();
    }

    protected abstract void resetGridSelectionByDrag(
            CellSelection gridSelection, ElementCasePane reportPane,
            int startMultiSelectIndex, int endMultiSelectIndex
    );

    protected abstract int evtOffset(MouseEvent evt, int offset);

    protected abstract int getColumnOrRowByGridHeader(ColumnRow selectedCellPoint);


    private void setToolTipText2(String text) {
        if (tip == null) {
            return;
        }

        tip.setTipText(text);
        tip.setSize(tip.getPreferredSize());
        tipWindow.pack();
        tipWindow.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent evt) {
        if (!gHeader.isEnabled()) {
            return;
        }

        ElementCasePane ePane = gHeader.getElementCasePane();
        ElementCase report = ePane.getEditingElementCase();

        gHeader.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(BaseUtils.readImage(
                "/com/fr/base/images/cell/cursor/" + nameOfSelectCursorGIF() + ".gif"
        ), new Point(16, 16), nameOfSelectCursorGIF()));

        DynamicUnitList sizeList = getSizeList(report);
        int scrollValue = getScrollValue(ePane);
        int scrollExtent = getScrollExtent(ePane);
        int endValue = scrollValue + scrollExtent + 1;
        int beginValue = getBeginValue(ePane);

        // draw column
        double tmpSize1 = 0;
        double tmpSize2;
        double tmpIncreaseSize = 0;
        int resolution = ScreenResolution.getScreenResolution();

        for (int i = beginValue; i < endValue; i++) {
            if (i == 0) {
                i = scrollValue;
            }
            // ajust width.
            tmpSize1 += tmpIncreaseSize;
            tmpIncreaseSize = sizeList.get(i).toPixD(resolution);
            tmpSize2 = tmpIncreaseSize <= 0 ? tmpSize1 + 1 : tmpSize1 + tmpIncreaseSize;

            if (BaseUtils.isAuthorityEditing()) {
                break;
            }

            if (this.isOnSeparatorLineIncludeZero(evt, tmpSize2, tmpIncreaseSize)) {
                gHeader.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(BaseUtils.readImage(
                        "/com/fr/base/images/cell/cursor/" + nameOfSplitCursorGIF() + ".gif"
                ), new Point(16, 16), nameOfSplitCursorGIF()));
                break;
            } else if (this.isOnNormalSeparatorLine(evt, tmpSize2)) {
                gHeader.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(BaseUtils.readImage(
                        "/com/fr/base/images/cell/cursor/" + nameOfMoveCursorGIF() + ".gif"
                ), new Point(16, 16), nameOfMoveCursorGIF()));
                break;
            }
        }
    }

    protected abstract String nameOfSelectCursorGIF();

    protected abstract String nameOfSplitCursorGIF();

    protected abstract String nameOfMoveCursorGIF();
}
