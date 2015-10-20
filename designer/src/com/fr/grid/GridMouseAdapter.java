package com.fr.grid;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.fr.base.BaseUtils;
import com.fr.base.DynamicUnitList;
import com.fr.base.ScreenResolution;
import com.fr.design.constants.UIConstants;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.present.CellWriteAttrPane;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.poly.creator.ECBlockPane;
import com.fr.report.ReportHelper;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellGUIAttr;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.ArrayUtils;
import com.fr.stable.ColumnRow;
import com.fr.stable.StringUtils;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.OLDPIX;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * the MouseListener of the Grid
 *
 * @editor zhou 2012-3-22����1:53:59
 */
public class GridMouseAdapter implements MouseListener, MouseWheelListener, MouseMotionListener {
	private static final int WIDGET_WIDTH = 13;
	private static final int TIME_DELAY = 100;
	private static final int TOOLTIP_X = 30;
	private static final int TOOLTIP_X_Y_FIX = 4;
	private static final double COPY_CROSS_INNER_DISTANCE = 1.5;
	private static final double COPY_CROSS_OUTER_DISTANCE = 2.5;
	/**
	 * ��קʱ��ˢ��ʱ����
	 */
	private static int DRAG_REFRESH_TIME = 10;
	/**
	 * ��Ӧ�ı��-Grid
	 */
	private Grid grid;
	/**
	 * the Point(x,y) where the mouse pressed
	 */
	private int oldEvtX = 0;
	private int oldEvtY = 0;
	// the old location, used for Move float element.
	private int oldLocationX;
	private int oldLocationY;
	private long lastMouseMoveTime = 0; // ����MouseMoveʱ��.
	// �����������Ԫ�ص�oldLocation����
	private Map<String, Point> floatNamePointMap;
	/**
	 * august:��ΪCellSelection����û�м�¼�ı����ˣ�����Ҫ�и��������水סshift����λ��֮ǰ������λ��
	 * �û�����һֱ��סshift�����ţ����԰�סshift��֮ǰ�����λ���Ǳ����е�.
	 */
	private ColumnRow tempOldSelectedCell;

	private int ECBlockGap = 40;

	protected GridMouseAdapter(Grid grid) {
		this.grid = grid;
	}

	/**
	 * @param evt
	 */
	public void mousePressed(MouseEvent evt) {
		if (!grid.isEnabled()) {
			return;
		}
		oldEvtX = evt.getX();
		oldEvtY = evt.getY();
		grid.stopEditing();

		if (!grid.hasFocus() && grid.isRequestFocusEnabled()) {
			grid.requestFocus();
		}

		if (grid.getDrawingFloatElement() != null) {
			doWithDrawingFloatElement();
		} else {
			if (SwingUtilities.isRightMouseButton(evt)) {
				doWithRightButtonPressed();
			} else {
				doWithLeftButtonPressed(evt);
			}
			// �û�û�а�סShift��ʱ��tempOldSelectedCell��һֱ�仯�ġ����һֱ��סshift���ǲ����
			ElementCasePane ePane = grid.getElementCasePane();
			if (!evt.isShiftDown() && ePane.getSelection() instanceof CellSelection) {
				tempOldSelectedCell = GridUtils.getAdjustEventColumnRow(ePane, oldEvtX, oldEvtY);
			}
		}

	}

	/**
	 * ������Ԫ��(ֻ���ı��͹�ʽ)��ӵ��������λ��
	 */
	private void doWithDrawingFloatElement() {
		ElementCasePane reportPane = grid.getElementCasePane();
		TemplateElementCase report = reportPane.getEditingElementCase();
		DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
		DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);

		int horizentalScrollValue = grid.getHorizontalValue();
		int verticalScrollValue = grid.getVerticalValue();
		int resolution = ScreenResolution.getScreenResolution();
		FU evtX_fu = FU.valueOfPix(this.oldEvtX, resolution);
		FU evtY_fu = FU.valueOfPix(this.oldEvtY, resolution);

		FU leftDistance = FU.getInstance(evtX_fu.toFU() + columnWidthList.getRangeValue(0, horizentalScrollValue).toFU());
		FU topDistance = FU.getInstance(evtY_fu.toFU() + rowHeightList.getRangeValue(0, verticalScrollValue).toFU());

		grid.getDrawingFloatElement().setLeftDistance(leftDistance);
		grid.getDrawingFloatElement().setTopDistance(topDistance);

		report.addFloatElement(grid.getDrawingFloatElement());
		reportPane.setSelection(new FloatSelection(grid.getDrawingFloatElement().getName()));
	}

	/**
	 * �����һ��¼��������Ҽ��˵�.
	 */
	private void doWithRightButtonPressed() {
		ElementCasePane reportPane = grid.getElementCasePane();
		Object[] tmpFloatElementCursor = GridUtils.getAboveFloatElementCursor(reportPane, this.oldEvtX, this.oldEvtY);
		if (!ArrayUtils.isEmpty(tmpFloatElementCursor)) {
			FloatElement selectedFloatElement = (FloatElement) tmpFloatElementCursor[0];
			reportPane.setSelection(new FloatSelection(selectedFloatElement.getName()));
		} else {
			ColumnRow selectedCellPoint = GridUtils.getAdjustEventColumnRow(reportPane, this.oldEvtX, this.oldEvtY);
			if (!reportPane.getSelection().containsColumnRow(selectedCellPoint)) {
				GridUtils.doSelectCell(reportPane, selectedCellPoint.getColumn(), selectedCellPoint.getRow());
			}
		}
		reportPane.repaint();
		JPopupMenu cellPopupMenu = reportPane.createPopupMenu();
		if (cellPopupMenu != null) {
			GUICoreUtils.showPopupMenu(cellPopupMenu, this.grid, this.oldEvtX - 1, this.oldEvtY - 1);
		}
	}

	/**
	 * ��������¼�
	 */
	private void doWithLeftButtonPressed(MouseEvent evt) {
		if(BaseUtils.isAuthorityEditing()){
			grid.setEditable(false);
		}

		ElementCasePane reportPane = grid.getElementCasePane();
		TemplateElementCase report = reportPane.getEditingElementCase();
		boolean isShiftDown = evt.isShiftDown();
		boolean isControlDown = evt.isControlDown();
		int clickCount = evt.getClickCount();
		// peter:��Ҫ�ж��Ƿ��ڿ��ƶ�CellSelection������
		grid.setDragType(isMoveCellSelection(this.oldEvtX, this.oldEvtY));
		if (clickCount >= 2) {
			grid.setDragType(GridUtils.DRAG_NONE);
		}
		if (grid.getDragType() != GridUtils.DRAG_NONE) {// Drag�ı�־.
			Selection selection = reportPane.getSelection();
			if (selection instanceof CellSelection) {
				// peter:����DragRecatagle�ı�־.
				if (grid.getDragRectangle() == null) {
					grid.setDragRectangle(new Rectangle());
				}
				CellSelection cs = ((CellSelection) selection).clone();
				grid.getDragRectangle().setBounds(cs.toRectangle());
				return;
			}
		}
		// peter:ѡ��GridSelection,֧��Shift
		doOneClickSelection(this.oldEvtX, this.oldEvtY, isShiftDown, isControlDown);
		// �õ�������ڵ�column and row
		ColumnRow columnRow = GridUtils.getEventColumnRow(reportPane, this.oldEvtX, this.oldEvtY);
		TemplateCellElement cellElement = report.getTemplateCellElement(columnRow.getColumn(), columnRow.getRow());
		if (clickCount >= 2 && !BaseUtils.isAuthorityEditing()) {
			grid.startEditing();
		}
		if (clickCount == 1 && cellElement != null && cellElement.getWidget() != null && !BaseUtils.isAuthorityEditing()) {
			showWidetWindow(cellElement, report);
		}
		reportPane.repaint();
	}

	/**
	 * ��ʾ�ؼ��༭����
	 *
	 * @param cellElement
	 * @param report
	 */

	private void showWidetWindow(TemplateCellElement cellElement, TemplateElementCase report) {
		int resolution = ScreenResolution.getScreenResolution();
		DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
		DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);
		double fixed_pos_x = this.oldEvtX - columnWidthList.getRangeValue(grid.getHorizontalValue(), cellElement.getColumn()).toPixD(resolution);
		double fixed_pos_y = this.oldEvtY - rowHeightList.getRangeValue(grid.getVerticalValue(), cellElement.getRow()).toPixD(resolution);
		double cell_width = columnWidthList.getRangeValue(cellElement.getColumn(), cellElement.getColumn() + cellElement.getColumnSpan()).toPixD(resolution);
		double cell_height = rowHeightList.getRangeValue(cellElement.getRow(), cellElement.getRow() + cellElement.getRowSpan()).toPixD(resolution);
		if (fitSizeToShow(cell_width, cell_height, fixed_pos_x, fixed_pos_y)) {
			CellWriteAttrPane.showWidgetWindow(grid.getElementCasePane());
		}
	}

	private boolean fitSizeToShow(double cell_width, double cell_height, double fixed_pos_x, double fixed_pos_y) {
		return cell_width - fixed_pos_x > 0 && cell_height - fixed_pos_y > 0
				&& cell_width - fixed_pos_x < WIDGET_WIDTH && cell_height - fixed_pos_y < WIDGET_WIDTH;
	}

	/**
	 * @param evt
	 */
	public void mouseReleased(MouseEvent evt) {
		if (!grid.isEnabled() || !grid.isEditable()) {
			return;
		}
		boolean isDataChanged = false;
		ElementCasePane reportPane = grid.getElementCasePane();
		Selection selection = reportPane.getSelection();
		if (grid.getDrawingFloatElement() != null) {
			if (grid.getDrawingFloatElement().getWidth().equal_zero() && grid.getDrawingFloatElement().getHeight().equal_zero()) {
				grid.getDrawingFloatElement().setWidth(new OLDPIX(100));
				grid.getDrawingFloatElement().setHeight(new OLDPIX(100));
			}
			grid.setDrawingFloatElement(null);
		} else if (selection instanceof FloatSelection) {
			grid.setCursor(Cursor.getDefaultCursor());
		}
		if (grid.getDragType() == GridUtils.DRAG_CELLSELECTION) {
			if (selection instanceof CellSelection) {
				grid.getElementCasePane().cut();
				// mouse release��ʱ��Ҫ�ж����Ƿ���reportPane��Χ��
				if (outOfBounds(evt, reportPane)) {
					GridUtils.doSelectCell(reportPane, grid.getDragRectangle().x, grid.getDragRectangle().y);
				} else {
					mousePressed(evt);
				}
				grid.getElementCasePane().paste();
				isDataChanged = true;
			}
		} else if (grid.getDragType() == GridUtils.DRAG_CELLSELECTION_BOTTOMRIGHT_CORNER) {
			if (selection instanceof CellSelection) {
				CellSelection cs = (CellSelection) selection;
				// august��������ק��չ��Ԫ��ֵ
				IntelliElements.iterating(reportPane, cs.toRectangle(), grid.getDragRectangle());
				if (grid.getDragRectangle() != null) {
					reportPane.setSelection(new CellSelection(grid.getDragRectangle().x, grid.getDragRectangle().y, grid.getDragRectangle().width, grid.getDragRectangle().height));
				}
				isDataChanged = true;
			}
		} else if (grid.getDragType() == GridUtils.DRAG_FLOAT) {
			isDataChanged = true;
		}
		grid.setDragType(GridUtils.DRAG_NONE);
		grid.setDragRectangle(null);
		if (isDataChanged) {
            reportPane.setSupportDefaultParentCalculate(true);
			reportPane.fireTargetModified();
            reportPane.setSupportDefaultParentCalculate(false);
		}
		doWithFormatBrush(reportPane);
		reportPane.repaint();
	}

	private void doWithFormatBrush(ElementCasePane reportPane) {
		if (DesignerContext.getFormatState() == DesignerContext.FORMAT_STATE_NULL) {
			return;
		}

		if (reportPane.getCellNeedTOFormat() != null) {
			reportPane.getFormatBrushAction().updateFormatBrush(DesignerContext.getReferencedStyle(), reportPane.getCellNeedTOFormat(), reportPane);
			reportPane.fireTargetModified();

		}
		if (DesignerContext.getFormatState() == DesignerContext.FORMAT_STATE_ONCE) {
			reportPane.cancelFormatBrush();
		}
		if (DesignerContext.getFormatState() == DesignerContext.FORMAT_STATE_MORE) {
			reportPane.getFormatBrush().setSelected(true);
		}
	}

	private boolean outOfBounds(MouseEvent evt, ElementCasePane reportPane) {
		return evt.getY() > reportPane.getHeight() || evt.getY() < 0 || evt.getX() > reportPane.getWidth() || evt.getX() < 0;
	}

	/**
	 * @param evt
	 */
	public void mouseMoved(final MouseEvent evt) {
		ElementCasePane reportPane = grid.getElementCasePane();
		boolean isGridForSelection = !grid.isEnabled() || !grid.isEditable();
		if (isGridForSelection || grid.isEditing()) {
			if (grid.IsNotShowingTableSelectPane()) {
				grid.setCursor(UIConstants.CELL_DEFAULT_CURSOR);
				return;
			}
			if (DesignerContext.getFormatState() != DesignerContext.FORMAT_STATE_NULL) {
				grid.setCursor(UIConstants.FORMAT_BRUSH_CURSOR);
			} else {
				grid.setCursor(GUICoreUtils.createCustomCursor(BaseUtils.readImage("com/fr/design/images/buttonicon/select.png"),
						new Point(0, 0), "select", grid));
			}

			return;
		}
		// peter:ͣ��һ��ʱ��.
		long systemCurrentTime = System.currentTimeMillis();
		if (systemCurrentTime - lastMouseMoveTime <= TIME_DELAY) {
			return;
		}
		lastMouseMoveTime = systemCurrentTime;// ��¼���һ�ε�ʱ��.
		mouseMoveOnGrid(evt.getX(), evt.getY());
	}

	/**
	 * @param evt
	 */
	public void mouseDragged(MouseEvent evt) {
		if (!grid.isEnabled()) {
			return;
		}

		boolean isControlDown = evt.isControlDown();

		long systemCurrentTime = System.currentTimeMillis();
		if (systemCurrentTime - lastMouseMoveTime <= DRAG_REFRESH_TIME) {// alex:Drag
			return;
		} else {
			lastMouseMoveTime = systemCurrentTime;
		}

		// right mouse cannot Drag..
		if (SwingUtilities.isRightMouseButton(evt)) {
			return;
		}

		doWithMouseDragged(evt.getX(), evt.getY(), isControlDown);
	}

	private void doWithMouseDragged(int evtX, int evtY, boolean isControlDown) {
		ElementCasePane reportPane = grid.getElementCasePane();

		if (reportPane.mustInVisibleRange()) {
			Grid grid = reportPane.getGrid();
			if (evtX > grid.getWidth() - 2 || evtY > grid.getHeight() - 2) {
				return;
			}
		}
		Selection selection = reportPane.getSelection();

		if (selection instanceof FloatSelection && !BaseUtils.isAuthorityEditing()) {
			doWithFloatElementDragged(evtX, evtY, (FloatSelection) selection);
			grid.setDragType(GridUtils.DRAG_FLOAT);
		} else if (grid.getDragType() == GridUtils.DRAG_CELLSELECTION_BOTTOMRIGHT_CORNER && !BaseUtils.isAuthorityEditing()) {
			doWithCellElementDragged(evtX, evtY, (CellSelection) selection);
		} else if (grid.getDragType() == GridUtils.DRAG_CELLSELECTION && !BaseUtils.isAuthorityEditing()) {
			// peter:��õ�������Selected Column Row.
			ColumnRow selectedCellPoint = GridUtils.getAdjustEventColumnRow(reportPane, evtX, evtY);
			if (selectedCellPoint.getColumn() != grid.getDragRectangle().x || selectedCellPoint.getRow() != grid.getDragRectangle().y) {
				grid.getDragRectangle().x = selectedCellPoint.getColumn();
				grid.getDragRectangle().y = selectedCellPoint.getRow();
			}
		} else {// august: ��קѡ�ж����Ԫ��
			doShiftSelectCell(evtX, evtY);
		}
		grid.getElementCasePane().repaint();
	}

	/**
	 * ��ק����Ԫ��
	 *
	 * @param evtX
	 * @param evtY
	 * @param fs
	 */

	private void doWithFloatElementDragged(int evtX, int evtY, FloatSelection fs) {
		ElementCase report = grid.getElementCasePane().getEditingElementCase();
		int resolution = ScreenResolution.getScreenResolution();
		String floatName = fs.getSelectedFloatName();
		FloatElement floatElement = report.getFloatElement(floatName);
		int cursorType = grid.getCursor().getType();

		if (cursorType == Cursor.NW_RESIZE_CURSOR || cursorType == Cursor.NE_RESIZE_CURSOR || cursorType == Cursor.SE_RESIZE_CURSOR || cursorType == Cursor.SW_RESIZE_CURSOR) {
			DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
			DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);
			FU floatX1_fu = FU.valueOfPix(Math.min(oldEvtX, evtX), resolution);
			FU floatY1_fu = FU.valueOfPix(Math.min(oldEvtY, evtY), resolution);
			FU leftDistance = floatX1_fu.add(columnWidthList.getRangeValue(0, grid.getHorizontalValue()));
			FU topDistance = floatY1_fu.add(rowHeightList.getRangeValue(0, grid.getVerticalValue()));
			floatElement.setLeftDistance(leftDistance);
			floatElement.setTopDistance(topDistance);
			floatElement.setWidth(FU.valueOfPix(Math.max(oldEvtX, evtX), resolution).subtract(floatX1_fu));
			floatElement.setHeight(FU.valueOfPix(Math.max(oldEvtY, evtY), resolution).subtract(floatY1_fu));
		} else if (cursorType == Cursor.S_RESIZE_CURSOR || cursorType == Cursor.N_RESIZE_CURSOR) {
			DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);
			FU floatY1_fu = FU.valueOfPix(Math.min(oldEvtY, evtY), resolution);
			FU topDistance = floatY1_fu.add(rowHeightList.getRangeValue(0, grid.getVerticalValue()));
			floatElement.setTopDistance(topDistance);
			floatElement.setHeight(FU.valueOfPix(Math.max(oldEvtY, evtY), resolution).subtract(floatY1_fu));
		} else if (cursorType == Cursor.W_RESIZE_CURSOR || cursorType == Cursor.E_RESIZE_CURSOR) {
			DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
			FU floatX1_fu = FU.valueOfPix(Math.min(oldEvtX, evtX), resolution);
			FU leftDistance = floatX1_fu.add(columnWidthList.getRangeValue(0, grid.getHorizontalValue()));
			floatElement.setLeftDistance(leftDistance);
			floatElement.setWidth(FU.valueOfPix(Math.max(oldEvtX, evtX), resolution).subtract(floatX1_fu));
		} else if (cursorType == Cursor.MOVE_CURSOR) {
			DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
			DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);
			int horizentalValue = grid.getHorizontalValue();
			int verticalValue = grid.getVerticalValue();
			String floatElementName = fs.getSelectedFloatName();
			FloatElement tempFolatElement = report.getFloatElement(floatElementName);
			Point tempFolatElementPoint = floatNamePointMap.get(floatElementName);
			int floatX1ForTempFloatElement = tempFolatElementPoint.x + Math.max(oldLocationX + (evtX - oldEvtX), 0);
			int floatY1ForTempFloatElement = tempFolatElementPoint.y + Math.max(oldLocationY + (evtY - oldEvtY), 0);
			FU floatX1ForTempFloatElement_fu = FU.valueOfPix(floatX1ForTempFloatElement, resolution);
			FU leftDistance = floatX1ForTempFloatElement_fu.add(columnWidthList.getRangeValue(0, horizentalValue));
			FU floatY1ForTempFloatElement_fu = FU.valueOfPix(floatY1ForTempFloatElement, resolution);
			FU topDistance = floatY1ForTempFloatElement_fu.add(rowHeightList.getRangeValue(0, verticalValue));
			tempFolatElement.setLeftDistance(leftDistance);
			tempFolatElement.setTopDistance(topDistance);
		}

	}

	/**
	 * ��ק��Ԫ��
	 *
	 * @param evtX
	 * @param evtY
	 * @param cs
	 */

	private void doWithCellElementDragged(int evtX, int evtY, CellSelection cs) {
		ElementCasePane reportPane = grid.getElementCasePane();
		java.awt.Rectangle cellRectangle = cs.toRectangle();

		ColumnRow selectedCellPoint = GridUtils.getAdjustEventColumnRow(reportPane, evtX, evtY);
		if (cellRectangle.contains(selectedCellPoint.getColumn(), selectedCellPoint.getRow())) {
			grid.getDragRectangle().setBounds(cellRectangle);
		} else {
			int xDistance = evtX - this.oldEvtX;
			int yDistance = evtY - this.oldEvtY;
			if (Math.abs(yDistance) > Math.abs(xDistance)) {
				grid.getDragRectangle().x = cellRectangle.x;
				grid.getDragRectangle().width = cellRectangle.width;
				if (yDistance >= 0) {
					// �ۺϱ���Ҫ����ק��ʱ��Ҫ�ڱ�����ڲ����� ��������������
					if (reportPane instanceof ECBlockPane && evtY > reportPane.getBounds().height - ECBlockGap) {
						return;
					}
					grid.getDragRectangle().y = cellRectangle.y;
					grid.getDragRectangle().height = selectedCellPoint.getRow() - cellRectangle.y + 1;
				} else {
					if (selectedCellPoint.getRow() >= cellRectangle.y && selectedCellPoint.getRow() < cellRectangle.y + cellRectangle.height) {
						grid.getDragRectangle().y = cellRectangle.y;
						grid.getDragRectangle().height = cellRectangle.height;
					} else {
						grid.getDragRectangle().y = cellRectangle.y;
						grid.getDragRectangle().height = cellRectangle.y - selectedCellPoint.getRow() + cellRectangle.height;
					}
				}
			} else {
				grid.getDragRectangle().y = cellRectangle.y;
				grid.getDragRectangle().height = cellRectangle.height;
				if (xDistance >= 0) {
					if (reportPane instanceof ECBlockPane && evtX > reportPane.getBounds().width - ECBlockGap) {
						return;
					}
					grid.getDragRectangle().x = cellRectangle.x;
					grid.getDragRectangle().width = selectedCellPoint.getColumn() - cellRectangle.x + 1;
				} else {
					if (selectedCellPoint.getColumn() >= cellRectangle.x && selectedCellPoint.getColumn() < cellRectangle.x + cellRectangle.width) {
						grid.getDragRectangle().x = cellRectangle.x;
						grid.getDragRectangle().width = cellRectangle.width;
					} else {
						grid.getDragRectangle().x = selectedCellPoint.getColumn();
						grid.getDragRectangle().width = cellRectangle.x - selectedCellPoint.getColumn() + cellRectangle.width;
					}
				}
			}
		}
		reportPane.ensureColumnRowVisible(selectedCellPoint.getColumn() + 1, selectedCellPoint.getRow() + 1);
	}

	private void doShiftSelectCell(double evtX, double evtY) {
		ElementCasePane reportPane = grid.getElementCasePane();
		Selection s = reportPane.getSelection();
		if (s instanceof FloatSelection) {
			return;
		}
		ColumnRow selectedCellPoint = GridUtils.getAdjustEventColumnRow(reportPane, evtX, evtY);
		int selectedCellPointX = selectedCellPoint.getColumn();
		int selectedCellPointY = selectedCellPoint.getRow();
		CellSelection gridSelection = ((CellSelection) s).clone();
		//����ѡ��Ԫ��
		int tempOldSelectedCellX = tempOldSelectedCell.getColumn();
		int tempOldSelectedCellY = tempOldSelectedCell.getRow();
//		int tempOldSelectedCellX = gridSelection.getEditRectangle().x;
//		int tempOldSelectedCellY = gridSelection.getEditRectangle().y;

		int column = selectedCellPointX >= tempOldSelectedCellX ? tempOldSelectedCellX : selectedCellPointX;
		int row = selectedCellPointY >= tempOldSelectedCellY ? tempOldSelectedCellY : selectedCellPointY;
		int columnSpan = Math.abs(selectedCellPointX - tempOldSelectedCellX) + 1;
		int rowSpan = Math.abs(selectedCellPointY - tempOldSelectedCellY) + 1;
		Rectangle oldrectangle = new Rectangle(column, row, columnSpan, rowSpan);
		// ajust them to got the correct selected bounds.
		Rectangle newrectangle = grid.caculateIntersectsUnion(reportPane.getEditingElementCase(), oldrectangle);
		gridSelection.setBounds(newrectangle.x, newrectangle.y, newrectangle.width, newrectangle.height);
		gridSelection.clearCellRectangles(gridSelection.getCellRectangleCount() - 1);
		gridSelection.addCellRectangle(newrectangle);
		reportPane.setSelection(gridSelection);
		if (!reportPane.mustInVisibleRange()) {
			reportPane.ensureColumnRowVisible(selectedCellPointX, selectedCellPointY);
		}
	}


	private void doControlSelectCell(double evtX, double evtY) {
		ElementCasePane reportPane = grid.getElementCasePane();
		ElementCase report = reportPane.getEditingElementCase();
		//��һ��ѡ�еĵ�Ԫ��
		Selection s = reportPane.getSelection();
		if (s instanceof FloatSelection) {
			return;
		}

		ColumnRow selectedCellPoint = GridUtils.getAdjustEventColumnRow(reportPane, evtX, evtY);
		//������������ֱ��ǿ��ʹ���Լ�����Ԫ��ѡ��仯
		CellSelection gridSelection = ((CellSelection) s).clone();
		gridSelection.setSelectedType(((CellSelection) s).getSelectedType());
		CellElement cellElement = report.getCellElement(selectedCellPoint.getColumn(), selectedCellPoint.getRow());
		if (cellElement == null) {
			gridSelection.setBounds(selectedCellPoint.getColumn(), selectedCellPoint.getRow(), 1, 1);
			int point = gridSelection.containsCell(selectedCellPoint.getColumn(), selectedCellPoint.getRow());
			if (point == -1) {
				gridSelection.addCellRectangle(new Rectangle(selectedCellPoint.getColumn(), selectedCellPoint.getRow(), 1, 1));
			} else {
				gridSelection.clearCellRectangles(point);
			}

		} else {
			gridSelection.setBounds(cellElement.getColumn(), cellElement.getRow(), cellElement.getColumnSpan(), cellElement.getRowSpan());
			gridSelection.addCellRectangle(new Rectangle(cellElement.getColumn(), cellElement.getRow(), cellElement.getColumnSpan(), cellElement.getRowSpan()));


		}

		reportPane.setSelection(gridSelection);

		if (!reportPane.mustInVisibleRange()) {
			reportPane.ensureColumnRowVisible(selectedCellPoint.getColumn(), selectedCellPoint.getRow());
		}


	}


	/**
	 * �����Grid�����ƶ�.
	 */
	private void mouseMoveOnGrid(int evtX, int evtY) {
		grid.setToolTipText(null);
		if (grid.getDrawingFloatElement() != null) {
			grid.setCursor(UIConstants.DRAW_CURSOR); // august���Ƿ��ǽ�Ҫ������Ԫ�أ������Ǹ��ʵ���״
		} else {
			Object[] floatElementCursor = GridUtils.getAboveFloatElementCursor(grid.getElementCasePane(), evtX, evtY);
			if (!ArrayUtils.isEmpty(floatElementCursor)) {// ���������Ԫ�����ƶ�
				grid.setCursor((Cursor) floatElementCursor[1]);
			} else {// ����ڵ�Ԫ�����ƶ�
				doMouseMoveOnCells(evtX, evtY);
			}
		}
	}

	/**
	 * ����ڵ�Ԫ�����ƶ�
	 *
	 * @param evtX
	 * @param evtY
	 */
	private void doMouseMoveOnCells(int evtX, int evtY) {
		ElementCasePane reportPane = grid.getElementCasePane();
		TemplateElementCase report = reportPane.getEditingElementCase();
		//����Ǹ�ʽˢ״̬
		if (DesignerContext.getFormatState() != DesignerContext.FORMAT_STATE_NULL) {
			grid.setCursor(UIConstants.FORMAT_BRUSH_CURSOR);
		} else {
			grid.setCursor(UIConstants.CELL_DEFAULT_CURSOR);
		}
		ColumnRow selectedCellColumnRow = GridUtils.getEventColumnRow(reportPane, evtX, evtY);
		TemplateCellElement curCellElement = report.getTemplateCellElement(selectedCellColumnRow.getColumn(), selectedCellColumnRow.getRow());

		if (curCellElement != null) {
			setCursorAndToolTips(curCellElement, report);
		}

		int dragType = isMoveCellSelection(evtX, evtY);
		if (dragType == GridUtils.DRAG_CELLSELECTION) {// �ж��Ƿ��ƶ�ѡ�е�����.
			grid.setCursor(new Cursor(Cursor.MOVE_CURSOR));
		} // peter:�ж��Ƿ����ƶ��Ľ���.
		else if (dragType == GridUtils.DRAG_CELLSELECTION_BOTTOMRIGHT_CORNER) {
			grid.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}

	}

	/**
	 * ֻ����CellGUIAttr�����tooltips��ʾ�ˣ�ԭ�ȵ���ʾ�������ԡ���̬���ؼ���������
	 *
	 * @param curCellElement
	 * @param report
	 */
	private void setCursorAndToolTips(TemplateCellElement curCellElement, TemplateElementCase report) {
		int resolution = ScreenResolution.getScreenResolution();
		// �������Grid����ʾλ��.
		DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
		DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);

		CellGUIAttr cellGUIAttr = curCellElement.getCellGUIAttr();
		if (cellGUIAttr == null) {
			cellGUIAttr = CellGUIAttr.DEFAULT_CELLGUIATTR;
		}
		grid.setToolTipText(cellGUIAttr.getTooltipText());
		double tooltipX = columnWidthList.getRangeValue(grid.getHorizontalValue(), curCellElement.getColumn()).toPixD(resolution) + TOOLTIP_X_Y_FIX;
		double tooltipY = rowHeightList.getRangeValue(grid.getVerticalValue(), curCellElement.getRow() + curCellElement.getRowSpan()).toPixD(resolution) + TOOLTIP_X_Y_FIX;

		// peter:��ʾtooltip
		if (StringUtils.isNotBlank(grid.getToolTipText())) {
			grid.setTooltipLocation(tooltipX + TOOLTIP_X, tooltipY);
		}
	}

	/**
	 * �Ƿ��ƶ�CellSelection
	 */
	private int isMoveCellSelection(double evtX, double evtY) {
		ElementCasePane reportPane = grid.getElementCasePane();

		// p:�ж��Ƿ���ѡ������ı߿򣬿����ƶ�CellSelelctionѡ������
		Selection selection = reportPane.getSelection();
		if (!(selection instanceof CellSelection)) {
			return GridUtils.DRAG_NONE;
		}

		if ((selection instanceof CellSelection)
				&& ((CellSelection) selection).getCellRectangleCount() != 1) {// p:û��ѡ��Cell.
			return GridUtils.DRAG_NONE;
		}

		CellSelection cs = (CellSelection) selection;

		ElementCase report = reportPane.getEditingElementCase();

		// peter:�������Grid����ʾλ��.
		DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
		DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);

		int resolution = ScreenResolution.getScreenResolution();

		double leftColDistance = columnWidthList.getRangeValue(grid.getHorizontalValue(), cs.getColumn()).toPixD(resolution);
		double rightColDistance = columnWidthList.getRangeValue(grid.getHorizontalValue(), cs.getColumn() + cs.getColumnSpan()).toPixD(resolution);
		double topRowDistance = rowHeightList.getRangeValue(grid.getVerticalValue(), cs.getRow()).toPixD(resolution);
		double bottomRowDistance = rowHeightList.getRangeValue(grid.getVerticalValue(), cs.getRow() + cs.getRowSpan()).toPixD(resolution);

		// �����ж��Ƿ��ڿ��Ը��Ƶ����½���.
		if (fitCellSelectionBottomRight(evtX, evtY, rightColDistance, bottomRowDistance)) {
			return GridUtils.DRAG_CELLSELECTION_BOTTOMRIGHT_CORNER;
		}

		// ���distֵ��Сһ��,�������û���ʹ��drag and drop ���༭����֧��
		double dist = 1.0;
		if (fitCellSelection(evtX, leftColDistance, rightColDistance, dist)) {
			if (evtY >= (topRowDistance - dist) && evtY <= (bottomRowDistance + dist)) {
				return GridUtils.DRAG_CELLSELECTION;
			}
		} else if (fitCellSelection(evtY, topRowDistance, bottomRowDistance, dist)) {
			if (evtX >= (leftColDistance - dist) && evtX <= (rightColDistance + dist)) {
				return GridUtils.DRAG_CELLSELECTION;
			}
		}

		return GridUtils.DRAG_NONE;
	}

	private boolean fitCellSelection(double evt, double d1, double d2, double dist) {
		return (evt >= (d1 - dist) && evt <= (d1 + dist))
				|| (evt >= (d2 - dist) && evt <= (d2 + dist));
	}

	private boolean fitCellSelectionBottomRight(double evtX, double evtY, double rightColDistance, double bottomRowDistance) {
		return evtX > rightColDistance - COPY_CROSS_INNER_DISTANCE && evtX < rightColDistance + COPY_CROSS_OUTER_DISTANCE
				&& evtY > bottomRowDistance - COPY_CROSS_INNER_DISTANCE && bottomRowDistance < bottomRowDistance + COPY_CROSS_OUTER_DISTANCE;
	}

	/**
	 * Do one click selection
	 */
	private void doOneClickSelection(int evtX, int evtY, boolean isShiftDown, boolean isControlDown) {
		ElementCasePane reportPane = grid.getElementCasePane();
		// check float elements.
		Object[] tmpFloatElementCursor = GridUtils.getAboveFloatElementCursor(reportPane, evtX, evtY);
		if (!ArrayUtils.isEmpty(tmpFloatElementCursor)) {// p:ѡ��������Ԫ��.
			doSelectFloatElement(tmpFloatElementCursor, evtX, evtY);
		} else if (isShiftDown) {
			doShiftSelectCell(evtX, evtY);
		} else if (isControlDown) {
			doControlSelectCell(evtX, evtY);
		} else {
			ColumnRow selectedCellPoint = GridUtils.getEventColumnRow(reportPane, evtX, evtY);
			int type = reportPane.ensureColumnRowVisible(selectedCellPoint.getColumn(), selectedCellPoint.getRow());
			if (type == ElementCasePane.NO_OVER) {
				GridUtils.doSelectCell(reportPane, selectedCellPoint.getColumn(), selectedCellPoint.getRow());
			} else if (type == ElementCasePane.VERTICAL_OVER) {
				//�ۺϱ����ѡ���±߽��ʱ����ʱ�������ƣ���ֹ������
				GridUtils.doSelectCell(reportPane, selectedCellPoint.getColumn(), selectedCellPoint.getRow() - 1);
			} else if (type == ElementCasePane.HORIZONTAL_OVER) {
				//�ۺϱ����ѡ���ұ߽��ʱ����ʱ�������ƣ���ֹ������
				GridUtils.doSelectCell(reportPane, selectedCellPoint.getColumn() - 1, selectedCellPoint.getRow());
			} else {
				GridUtils.doSelectCell(reportPane, selectedCellPoint.getColumn() - 1, selectedCellPoint.getRow() - 1);
			}

			return;
		}

	}

	/**
	 * ѡ������Ԫ��
	 *
	 * @param tmpFloatElementCursor
	 * @param evtX
	 * @param evtY
	 */

	private void doSelectFloatElement(Object[] tmpFloatElementCursor, int evtX, int evtY) {
		ElementCasePane reportPane = grid.getElementCasePane();
		ElementCase report = reportPane.getEditingElementCase();
		FloatElement floatElement = (FloatElement) tmpFloatElementCursor[0];
		String floatName = floatElement.getName();
		reportPane.setSelection(new FloatSelection(floatName));
		double[] floatArray = GridUtils.caculateFloatElementLocations(floatElement, ReportHelper.getColumnWidthList(report), ReportHelper.getRowHeightList(report), reportPane
				.getGrid().getVerticalValue(), reportPane.getGrid().getHorizontalValue());

		int cursorType = ((Cursor) tmpFloatElementCursor[1]).getType();
		if (cursorType == Cursor.MOVE_CURSOR) {
			this.oldEvtX = evtX;
			this.oldEvtY = evtY;
			FloatElement el = report.getFloatElement(floatName);
			int resolution = ScreenResolution.getScreenResolution();
			int verticalValue = grid.getVerticalValue();
			int horizentalValue = grid.getHorizontalValue();
			DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
			DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);
			this.oldLocationX = FU.getInstance(el.getLeftDistance().toFU() - columnWidthList.getRangeValue(0, horizentalValue).toFU()).toPixI(resolution);
			this.oldLocationY = FU.getInstance(el.getTopDistance().toFU() - rowHeightList.getRangeValue(0, verticalValue).toFU()).toPixI(resolution);
			if (floatNamePointMap == null) {
				floatNamePointMap = new HashMap<String, Point>();
			}
			floatNamePointMap.clear();
			FloatElement tempFolatElement = report.getFloatElement(floatName);
			int floatX1ForTempFloatElement = FU.getInstance(tempFolatElement.getLeftDistance().toFU() - columnWidthList.getRangeValue(0, horizentalValue).toFU())
					.toPixI(resolution) - oldLocationX;
			int floatY1ForTempFloatElement = FU.getInstance(tempFolatElement.getTopDistance().toFU() - rowHeightList.getRangeValue(0, verticalValue).toFU()).toPixI(resolution)
					- oldLocationY;
			floatNamePointMap.put(floatName, new Point(floatX1ForTempFloatElement, floatY1ForTempFloatElement));
		} else if (cursorType == Cursor.NW_RESIZE_CURSOR) {
			setOld_X_AndOld_Y(floatArray[2], floatArray[3]);
		} else if (cursorType == Cursor.NE_RESIZE_CURSOR) {
			setOld_X_AndOld_Y(floatArray[0], floatArray[3]);
		} else if (cursorType == Cursor.SE_RESIZE_CURSOR) {
			setOld_X_AndOld_Y(floatArray[0], floatArray[1]);
		} else if (cursorType == Cursor.SW_RESIZE_CURSOR) {
			setOld_X_AndOld_Y(floatArray[2], floatArray[1]);
		} else if (cursorType == Cursor.N_RESIZE_CURSOR) {
			setOld_X_AndOld_Y(floatArray[0], floatArray[3]);
		} else if (cursorType == Cursor.S_RESIZE_CURSOR) {
			setOld_X_AndOld_Y(floatArray[0], floatArray[1]);
		} else if (cursorType == Cursor.W_RESIZE_CURSOR) {
			setOld_X_AndOld_Y(floatArray[2], floatArray[1]);
		} else if (cursorType == Cursor.E_RESIZE_CURSOR) {
			setOld_X_AndOld_Y(floatArray[0], floatArray[1]);
		}
	}

	private void setOld_X_AndOld_Y(double x, double y) {
		this.oldEvtX = (int) x;
		this.oldEvtY = (int) y;
	}

	/**
	 * @param e
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		ElementCasePane reportPane = grid.getElementCasePane();
		if (reportPane.isHorizontalScrollBarVisible()) {
			reportPane.getVerticalScrollBar().setValue(reportPane.getVerticalScrollBar().getValue() + e.getWheelRotation() * 3);
		}
	}

	/**
	 * @param e
	 */
	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * @param e
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * @param e
	 */
	public void mouseExited(MouseEvent e) {
	}
}
