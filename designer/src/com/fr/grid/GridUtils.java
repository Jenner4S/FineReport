package com.fr.grid;

import com.fr.base.DynamicUnitList;
import com.fr.base.ScreenResolution;
import com.fr.design.cell.clipboard.CellElementsClip;
import com.fr.design.cell.clipboard.ElementsTransferable;
import com.fr.design.cell.clipboard.FloatElementsClip;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.ReportHelper;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellGUIAttr;
import com.fr.report.core.PaintUtils;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.stable.ReportConstants;
import com.fr.stable.ColumnRow;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.PT;
import com.fr.stable.unit.UNIT;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

/**
 * Some util method of GUI.
 */
public class GridUtils {

    private GridUtils() {
    }

    //peter:û��Drag
    public final static int DRAG_NONE = 0;
    //peter:Drag CellSelection�ı߿����ƶ�Ԫ��.
    public final static int DRAG_CELLSELECTION = 1;
    //peter:Drag CellSelection�����½���������Ԫ��
    public final static int DRAG_CELLSELECTION_BOTTOMRIGHT_CORNER = 2;
    public final static int DRAG_FLOAT = 3;

    //peter:�����⼸��������Drag�е�ʱ����.
    public final static int DRAG_CELL_SIZE = 1; //peter:drag��ʱ��ı���ӵĿ��.
    public final static int DRAG_SELECT_UNITS = 2; //peter:drag��ʱ��,ѡ�е�Ԫ��.

    /**
     * Is above float element.(the return may be null). <br>
     * The length of Object[] is 2, the first is FloatElement, the second is Cursor.<br>
     * The object[] is null
     */
    public static Object[] getAboveFloatElementCursor(ElementCasePane reportPane, double evtX, double evtY) {
        //peter: ��Ҫ���ص�Objects
        Object[] returnObject = null;
        ElementCase report = reportPane.getEditingElementCase();
        Selection sel = reportPane.getSelection();
        //peter:������е�����Ԫ��.
        Iterator flotIt = report.floatIterator();
        while (flotIt.hasNext()) {
            FloatElement tmpFloatElement = (FloatElement) flotIt.next();
            //peter:��������Ԫ�ص��ĸ������λ��.
            double[] floatArray = caculateFloatElementLocations(tmpFloatElement, ReportHelper.getColumnWidthList(report),
                    ReportHelper.getRowHeightList(report), reportPane.getGrid().getVerticalValue(), reportPane.getGrid().getHorizontalValue());

            int resolution = ScreenResolution.getScreenResolution();
            //peter:����Ԫ�صķ�Χ.
            Rectangle2D floatElementRect = new Rectangle2D.Double(floatArray[0], floatArray[1], tmpFloatElement.getWidth().toPixD(resolution), tmpFloatElement.getHeight().toPixD(resolution));
            //peter:���ǵ�ǰѡ�е�����Ԫ��,��֧�������ı��С�ĵ�.
            if (!(sel instanceof FloatSelection && ComparatorUtils.equals(tmpFloatElement.getName(), ((FloatSelection) sel).getSelectedFloatName()))) {
                if (floatElementRect.contains(evtX, evtY)) {
                    returnObject = new Object[]{tmpFloatElement, new Cursor(Cursor.MOVE_CURSOR)};
                }

                //peter:��Ҫ��������,�����ǰ�������ѡ�е�����Ԫ�ص����������ƶ��ĵ���,����.
                continue;
            }
            Cursor cursor = null;
            Rectangle2D[] cornerRect = getCornerRect(floatArray);
            //peter:����Ԫ�ض�Ӧ���������.
            int[] cursorType = {Cursor.NW_RESIZE_CURSOR, Cursor.N_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR, Cursor.E_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR,
                    Cursor.S_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR, Cursor.W_RESIZE_CURSOR};
            for (int c = 0; c < cornerRect.length; c++) {
                if (cornerRect[c].contains(evtX, evtY)) {
                    cursor = new Cursor(cursorType[c]);
                    break;
                }
            }
            //peter:������Ԫ�������ڲ�,���ǲ�������������,���ƶ������
            if (floatElementRect.contains(evtX, evtY) && cursor == null) {
                returnObject = new Object[]{tmpFloatElement, new Cursor(Cursor.MOVE_CURSOR)};
            }
            //peter:�ڵ�ǰѡ��Ԫ�ص���������,������ȼ���,ֱ�ӷ���.
            if (cursor != null) {// select
                return new Object[]{tmpFloatElement, cursor};
            }
        }
        return returnObject;
    }

    //peter:����Ԫ�ص��������ƶ���С���ε�.
    //marks:��ѡ�������Ԫ�ض���һ��ʱ������Ӧ�ò���������Ԫ�صĹ��
    private static Rectangle2D[] getCornerRect(double[] floatArray) {
        double floatX1 = floatArray[0];
        double floatY1 = floatArray[1];
        double floatX2 = floatArray[2];
        double floatY2 = floatArray[3];
        Rectangle2D[] cornerRect = {new Rectangle2D.Double(floatX1 - 3, floatY1 - 3, 6, 6),
                new Rectangle2D.Double((floatX1 + floatX2) / 2 - 3, floatY1 - 3, 6, 6), new Rectangle2D.Double(floatX2 - 3, floatY1 - 3, 6, 6),
                new Rectangle2D.Double(floatX2 - 3, (floatY1 + floatY2) / 2 - 3, 6, 6), new Rectangle2D.Double(floatX2 - 3, floatY2 - 3, 6, 6),
                new Rectangle2D.Double((floatX1 + floatX2) / 2 - 3, floatY2 - 3, 6, 6), new Rectangle2D.Double(floatX1 - 3, floatY2 - 3, 6, 6),
                new Rectangle2D.Double(floatX1 - 3, (floatY1 + floatY2) / 2 - 3, 6, 6)};
        return cornerRect;
    }


    /**
     * Gets float element locations. Returns[] {flaotX1, floatY1, floatX2, floatY2}.
     */
    public static double[] caculateFloatElementLocations(FloatElement floatElement, DynamicUnitList columnWidthList, DynamicUnitList rowHeightList,
                                                         int verticalValue, int horizentalValue) {
        int resolution = ScreenResolution.getScreenResolution();


        double floatX = columnWidthList.getRangeValue(horizentalValue, 0).toPixD(resolution) + floatElement.getLeftDistance().toPixD(resolution);
        double floatY = rowHeightList.getRangeValue(verticalValue, 0).toPixD(resolution) + floatElement.getTopDistance().toPixD(resolution);

        double floatX2 = floatX + floatElement.getWidth().toPixD(resolution);
        double floatY2 = floatY + floatElement.getHeight().toPixD(resolution);

        return new double[]{floatX, floatY, floatX2, floatY2};
    }

    /**
     * Gets column and row which located on (evtX, evtY)
     * peter:��������ظ����ǵ�Frozen�����,û���κ�BUG,�������û�п������ֲ�������������.
     *
     * @param reportPane ��ǰ��ReportPane
     * @param evtX       event x
     * @param evtY       event y
     * @return the event located column and row.
     */
    public static ColumnRow getEventColumnRow(ElementCasePane reportPane, double evtX, double evtY) {
        ElementCase report = reportPane.getEditingElementCase();

        // Width and height list.
        DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);
        DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);

        int verticalValue = reportPane.getGrid().getVerticalValue();
        int horizentalValue = reportPane.getGrid().getHorizontalValue();

        // denny: get verticalBeginValue and horizontalBeginValue;
        int verticalBeginValue = reportPane.getGrid().getVerticalBeginValue();
        int horizontalBeginValue = reportPane.getGrid().getHorizontalBeginValue();
        return ColumnRow.valueOf(
                cc_selected_column_or_row(evtX, horizontalBeginValue, horizentalValue, columnWidthList),
                cc_selected_column_or_row(evtY, verticalBeginValue, verticalValue, rowHeightList)
        );
    }

    private static int cc_selected_column_or_row(double mouseEvtPosition, int beginValue, int value, DynamicUnitList sizeList) {
        double tmpIntIndex = 0;
        int selectedCellIndex = 0;
        int resolution = ScreenResolution.getScreenResolution();
        if (mouseEvtPosition < 0) {
            selectedCellIndex = value;
            for (; true; selectedCellIndex--) {
                if (tmpIntIndex < mouseEvtPosition) {
                    break;
                }
                tmpIntIndex -= sizeList.get(selectedCellIndex).toPixD(resolution);

            }
        } else {
            boolean isInnerFrozen = false;
            for (int i = beginValue; i < 0; i++) {
                tmpIntIndex += sizeList.get(i).toPixD(resolution);

                if (tmpIntIndex > mouseEvtPosition) {
                    selectedCellIndex = i;
                    isInnerFrozen = true;
                    break;
                }
            }

            if (!isInnerFrozen) {
                selectedCellIndex = value;
                for (; true; selectedCellIndex++) {
                    tmpIntIndex += sizeList.get(selectedCellIndex).toPixD(resolution);
                    if (tmpIntIndex > mouseEvtPosition) {
                        break;
                    }
                }
            }
        }

        return selectedCellIndex;
    }

    /**
     * Gets column and row which located on (evtX, evtY)
     * peter:��������ǵ�������Column,Row,����С��0, ���ܴ������ֵ,���������ֿ����˲�������������.
     * һ�㾭�����������, ��Ӧ��getEventColumnRow(...)����������.
     *
     * @param reportPane ��ǰ��ReportPane
     * @param evtX       event x
     * @param evtY       event y
     * @return the event located column and row.
     */
    public static ColumnRow getAdjustEventColumnRow(ElementCasePane reportPane, double evtX, double evtY) {
        ColumnRow selectedCellPoint = GridUtils.getEventColumnRow(reportPane, evtX, evtY);

        int col = Math.max(selectedCellPoint.getColumn(), 0);
        int row = Math.max(selectedCellPoint.getRow(), 0);


        return ColumnRow.valueOf(col, row);
    }

    /**
     * �Ƿ�ɽ���ǰ��Ԫ���Ϊ�ɼ��ĸ���
     */
    public static boolean canMove(ElementCasePane reportPane, int cellColumn, int cellRow) {
        if (reportPane.mustInVisibleRange()) {
            Grid grid = reportPane.getGrid();
            int verticalEndValue = grid.getVerticalValue() + grid.getVerticalExtent() - 1;
            int horizontalEndValue = grid.getHorizontalValue() + grid.getHorizontalExtent() - 1;
            if (cellColumn > horizontalEndValue) {
                return false;
            }
            if (cellRow > verticalEndValue) {
                return false;
            }
        }
        return true;
    }

    /**
     * ѡ��һ��Cell, ֧��Merge.
     */
    public static void doSelectCell(ElementCasePane reportPane, int cellColumn, int cellRow) {
        ElementCase report = reportPane.getEditingElementCase();


        CellElement cellElement = report.getCellElement(cellColumn, cellRow);
        if (cellElement == null) {
            reportPane.setSelection(new CellSelection(cellColumn, cellRow, 1, 1));
        } else {
            reportPane.setSelection(new CellSelection(cellElement.getColumn(), cellElement.getRow(), cellElement.getColumnSpan(), cellElement.getRowSpan()));
        }
    }

    /**
     * peter: ��ReportPaneѡ�е��������ElementsCopy
     */
    public static ElementsTransferable caculateElementsTransferable(ElementCasePane reportPane) {
        ElementsTransferable elementsTransferable = new ElementsTransferable();

        //p:��õ�ǰ��Report����.
        ElementCase report = reportPane.getEditingElementCase();

        Selection sel = reportPane.getSelection();
        //p:���ж�����Ԫ��.
        if (sel instanceof FloatSelection) {
            FloatSelection fs = (FloatSelection) sel;
            //p:��Ҫ����floatElementsClip.
            FloatElementsClip floatElementsClip = new FloatElementsClip(report.getFloatElement(fs.getSelectedFloatName()));

            elementsTransferable.addObject(floatElementsClip);
        } else {
            CellSelection cs = (CellSelection) sel;
            java.util.List<TemplateCellElement> elList = new java.util.ArrayList<TemplateCellElement>();
            //p:��������ཻ��CellElement.
            Rectangle selectionBounds = new Rectangle(cs.getColumn(), cs.getRow(), cs.getColumnSpan(), cs.getRowSpan());
            Iterator cells = report.intersect(cs.getColumn(), cs.getRow(), cs.getColumnSpan(), cs.getRowSpan());
            while (cells.hasNext()) {
                TemplateCellElement cellElement = (TemplateCellElement) cells.next();
                Rectangle tmpCellBound = new Rectangle(cellElement.getColumn(), cellElement.getRow(), cellElement.getColumnSpan(), cellElement.getRowSpan());
                //peter,��Ȼ��߰���
                if (GUICoreUtils.isTheSameRect(selectionBounds, tmpCellBound) || selectionBounds.contains(tmpCellBound)) {
                    //peter:��ӵ�CellElementsClip
                    elList.add((TemplateCellElement) cellElement.deriveCellElement(cellElement.getColumn() - cs.getColumn(), cellElement.getRow() - cs.getRow()));
                }
            }

            elementsTransferable.addObject(new CellElementsClip(
                    cs.getColumnSpan(), cs.getRowSpan(), elList.toArray(new TemplateCellElement[elList.size()])
            ));
        }

        return elementsTransferable;
    }

    /**
     * james: Gets adjust last columrow of reportpane especially used in whole row/column selected
     * the area before last columnrow should contain all the cellelement of the reportpane
     */
    public static ColumnRow getAdjustLastColumnRowOfReportPane(ElementCasePane reportPane) {
        ElementCase report = reportPane.getEditingElementCase();

        //james��ȫѡ�����һ�������ݵĸ���
        return ColumnRow.valueOf(Math.max(1, report.getColumnCount()), Math.max(1, report.getRowCount()));
    }

    /**
     * ����ɼ��������/����
     */
    public static int getExtentValue(int start, DynamicUnitList sizeList, double visibleSize, int dpi) {
        double sumSize = 0;
        int maxIndex = Integer.MAX_VALUE;
        for (int i = start; i <= maxIndex; i++) {
            sumSize += sizeList.get(i).toPixD(dpi);

            if (sumSize > visibleSize) {
                start = i;

                // check zero value.
                for (int j = i; true; j++) {
                    if (sizeList.get(j).equal_zero()) {
                        start = j;
                    } else {
                        break;
                    }
                }
                break;
            }
        }
        return start;
    }

    public static void shrinkToFit(int reportShrinkMode, TemplateElementCase tplEC, TemplateCellElement editCellElement) {
        DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(tplEC);
        DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(tplEC);

        CellGUIAttr cellGUIAttr = editCellElement.getCellGUIAttr();
        if (cellGUIAttr == null) {
            cellGUIAttr = new CellGUIAttr();
        }

        // carl:�����û������������и߻����п�
        if (cellGUIAttr.getAdjustMode() == ReportConstants.AUTO_SHRINK_TO_FIT_HEIGHT
                || (cellGUIAttr.getAdjustMode() == ReportConstants.AUTO_SHRINK_TO_FIT_DEFAULT
                && reportShrinkMode == ReportConstants.AUTO_SHRINK_TO_FIT_HEIGHT)) {
            fitHetght(editCellElement, columnWidthList, rowHeightList);
        } else if (cellGUIAttr.getAdjustMode() == ReportConstants.AUTO_SHRINK_TO_FIT_WIDTH
                || (cellGUIAttr.getAdjustMode() == ReportConstants.AUTO_SHRINK_TO_FIT_DEFAULT
                && reportShrinkMode == ReportConstants.AUTO_SHRINK_TO_FIT_WIDTH)) {
            fitWidth(editCellElement, columnWidthList, rowHeightList);
        }
    }

    private static void fitHetght(TemplateCellElement editCellElement, DynamicUnitList columnWidthList, DynamicUnitList rowHeightList) {
        int editElementcolumn = editCellElement.getColumn();
        UNIT preferredHeight = PaintUtils.analyzeCellElementPreferredHeight(
                editCellElement,
                columnWidthList.getRangeValue(editElementcolumn, editElementcolumn + editCellElement.getColumnSpan()));
        if (editCellElement.getRowSpan() == 1) {
            rowHeightList.set(editCellElement.getRow(),
                    UNIT.max(preferredHeight, rowHeightList.get(editCellElement.getRow())));
        } else {
            int lastRowIndex = editCellElement.getRow() + editCellElement.getRowSpan() - 1;
            // kurt ����Ԫ��ʱ���ӵĸ߶�
            long extraHeight = preferredHeight.toFU() - rowHeightList.getRangeValue(editCellElement.getRow(), lastRowIndex + 1).toFU();
            if (extraHeight > 0) {
                // kurt ƽ�ָ���Щ��
                for (int m = editCellElement.getRow(); m <= lastRowIndex; m++) {
                    rowHeightList.set(m, FU.getInstance(rowHeightList.get(m).toFU() + extraHeight
                            / editCellElement.getRowSpan()));
                }
            }
        }
    }

    private static void fitWidth(TemplateCellElement editCellElement, DynamicUnitList columnWidthList, DynamicUnitList rowHeightList) {
        UNIT preferredWidth = PaintUtils.getPreferredWidth(editCellElement, PT.valueOfFU(rowHeightList.getRangeValue(editCellElement.getRow(),
                editCellElement.getRow() + editCellElement.getRowSpan()).toFU()));
        // carl�����ŵ����и���Ū
        if (editCellElement.getColumnSpan() == 1) {
            columnWidthList.set(editCellElement.getColumn(), UNIT.max(preferredWidth,
                    columnWidthList.get(editCellElement.getColumn())));
        } else {
            int lastColumnIndex = editCellElement.getColumn() + editCellElement.getColumnSpan() - 1;
            long extraWidth = preferredWidth.toFU() - columnWidthList.getRangeValue(editCellElement.getColumn(), lastColumnIndex + 1).toFU();
            if (extraWidth > 0) {
                for (int m = editCellElement.getColumn(); m <= lastColumnIndex; m++) {
                    columnWidthList.set(m, FU.getInstance(columnWidthList.get(m).toFU() + extraWidth
                            / editCellElement.getColumnSpan() + 1));
                }
            }
        }
    }

}
