/*
 * Copyright(c) 2001-2010, FineReport  Inc, All Rights Reserved.
 */
package com.fr.grid;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Hashtable;
import java.util.Iterator;

import com.fr.base.DynamicUnitList;
import com.fr.design.cell.editor.CellEditor;
import com.fr.design.cell.editor.FloatEditor;
import com.fr.design.cell.editor.FormulaCellEditor;
import com.fr.design.cell.editor.GeneralCellEditor;
import com.fr.design.cell.editor.GeneralFloatEditor;
import com.fr.design.cell.editor.TextCellEditor;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.grid.event.CellEditorEvent;
import com.fr.grid.event.CellEditorListener;
import com.fr.grid.event.FloatEditorEvent;
import com.fr.grid.event.FloatEditorListener;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.ReportHelper;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellGUIAttr;
import com.fr.report.cell.cellattr.CellImage;
import com.fr.report.cell.cellattr.core.RichText;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.StringUtils;

/**
 * Grid used to paint and edit grid.
 *
 * @editor zhou 2012-3-22����1:58:12
 */
// TODO ALEX_SEP �ܲ��ܰ�CellSelection��ΪGrid������,�Դ����ֿ�CellSelection & FloatSelection
public class Grid extends BaseGridComponent {
    /**
     * If editing, the <code>Component</code> that is handling the editing.
     */
    private static final int VERTICAL_EXTENT_INITIAL_VALUE = 50;
    private static final int HORIZONTAL_EXTENT_INITIAL_VALUE = 40;
    transient protected Component editorComponent;
    transient private Point2D editorComponentLoc;
    transient private TemplateCellElement editingCellElement;

    private boolean showGridLine = true;
    private Color gridLineColor = Color.lightGray; // line color.

    private boolean isShowPaginateLine = true;
    private Color paginationLineColor = Color.RED; // line color of paper

    private boolean isShowVerticalFrozenLine = true;
    private Color verticalFrozenLineColor = Color.black;

    private boolean isShowHorizontalFrozenLine = true;
    private Color horizontalFrozenLineColor = Color.black;

    private Color selectedBackground = UIConstants.SELECTED_BACKGROUND;
    private Color selectedBorderLineColor = UIConstants.SELECTED_BORDER_LINE_COLOR;
    private boolean editable = true; // ������ܿ��أ����Ƹ����Ƿ���Ա༭.

    private FloatElement drawingFloatElement = null;

    private int dragType = GridUtils.DRAG_NONE;// Drag�ı�־.
    // peter:Drag���ӵ�ʱ��,��Ҫ��ʾ�ı߿�,��Ϊnull��ʱ�򲻻�.
    private Rectangle dragRectangle = null;
    // ToolTip
    private Point tooltipLocation;

    /**
     * The object that overwrites the screen real estate occupied by the current
     * cell and allows the user to change its contents.
     */
    transient private CellEditor cellEditor;
    /**
     * The object that overwrites the screen real estate occupied by the current
     * float and allows the user to change its contents.
     */
    transient private FloatEditor floatEditor;
    /**
     * Identifies the column of the cell being edited.
     */
    transient private int editingColumn;
    /**
     * Identifies the row of the cell being edited.
     */
    transient private int editingRow;
    // A table of objects that display and edit the contents of a cell.
    transient private Hashtable<Class, CellEditor> defaultCellEditorsByClass;
    // A table of objects that display and edit the contents of a float.
    transient private Hashtable<Class, FloatEditor> defaultFloatEditorsByClass;
    // Vertical and Horizontal value.
    private int verticalValue = 0;
    private int verticalExtent = VERTICAL_EXTENT_INITIAL_VALUE;
    private int horizontalValue = 0;
    private int horizontalExtent = HORIZONTAL_EXTENT_INITIAL_VALUE;// marks:���ֵ��ԭ����10����20����Ϊ���ڵ��Զ��ǿ�����10�Ѿ�û�а취��������
    // denny: verticalBeginValue and horizantalBeginValue
    private int verticalBeginValue = 0;
    private int horizontalBeginValue = 0;

    private int resolution;
    // �ж�SmartJTablePane�Ƿ���ʾ����Ϊ��̬���߱�ʶ��
    private boolean notShowingTableSelectPane = true;

    public Grid(int resolution) {
        this.resolution = resolution;
        // �ܴ���processEvent�������Ƿ��component����listener
        enableEvents(AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);

        GridKeyAction.initGridInputActionMap(this);
        GridMouseAdapter gridMouseAdapter = new GridMouseAdapter(this);

        this.addMouseListener(gridMouseAdapter);
        this.addMouseMotionListener(gridMouseAdapter);
        this.addMouseWheelListener(gridMouseAdapter);

        this.addKeyListener(new GridKeyListener(this));

        // JDK1.4
        this.setFocusTraversalKeysEnabled(false);
        this.setOpaque(false);

        this.updateUI();
    }

    /**
	 * Ӧ�ý�������
	 * 
	 *
	 * @date 2014-12-21-����6:32:43
	 * 
	 */
    public void updateUI() {
        this.setUI(new GridUI(resolution));
    }

    /**
	 * �Ƿ���ʾ������
	 * 
	 * @return �Ƿ���ʾ������
	 *  
	 *
	 * @date 2014-12-21-����6:32:13
	 * 
	 */
    public boolean isShowGridLine() {
        return showGridLine;
    }

    /**
     * Sets whether to show grid line.
     *
     * @param isShowGridLine whether to show grid line.
     */
    public void setShowGridLine(boolean isShowGridLine) {
        this.showGridLine = isShowGridLine;

        this.getElementCasePane().repaint();
    }

    /**
     * Gets grid line color.
     *
     * @return grid line color.
     */
    public Color getGridLineColor() {
        return this.gridLineColor;
    }

    /**
     * Sets grid line color.
     *
     * @param gridLineColor the new color of line.
     */
    public void setGridLineColor(Color gridLineColor) {
        Color old = this.gridLineColor;
        this.gridLineColor = gridLineColor;

        this.firePropertyChange("girdLineColor", old, this.gridLineColor);
        this.getElementCasePane().repaint();
    }

    /**
	 * �Ƿ���ʾ��ҳ��
	 * 
	 * @return �Ƿ���ʾ��ҳ��
	 * 
	 *
	 * @date 2014-12-21-����6:31:45
	 * 
	 */
    public boolean isShowPaginateLine() {
        return isShowPaginateLine;
    }

    /**
     * Sets to show pagination line.
     */
    public void setShowPaginateLine(boolean showPaginateLine) {
        this.isShowPaginateLine = showPaginateLine;

        this.getElementCasePane().repaint();
    }

    /**
     * Gets pagination line color.
     */
    public Color getPaginationLineColor() {
        return this.paginationLineColor;
    }

    /**
     * Sets pagination line color.
     *
     * @param paginationLineColor the new color of pagination line.
     */
    public void setPaginationLineColor(Color paginationLineColor) {
        Color old = this.paginationLineColor;
        this.paginationLineColor = paginationLineColor;

        this.firePropertyChange("paginationLineColor", old, this.paginationLineColor);
        this.getElementCasePane().repaint();
    }


    /**
	 * �Ƿ���ʾ��ֱ������
	 * 
	 * @return �Ƿ���ʾ��ֱ������
	 * 
	 *
	 * @date 2014-12-21-����6:29:35
	 * 
	 */
    public boolean isShowVerticalFrozenLine() {
        return isShowVerticalFrozenLine;
    }

    /**
     * Sets to show vertical frozen line.
     */
    public void setShowVerticalFrozenLine(boolean showVerticalFrozenLine) {
        this.isShowVerticalFrozenLine = showVerticalFrozenLine;

        this.getElementCasePane().repaint();
    }

    /**
     * Gets vertical frozen line color.
     */
    public Color getVerticalFrozenLineColor() {
        return verticalFrozenLineColor;
    }

    /**
     * Sets vertical frozen line color.
     *
     * @param verticalFrozenLineColor the new color of vertical frozen line.
     */
    public void setVerticalFrozenLineColor(Color verticalFrozenLineColor) {
        Color old = this.verticalFrozenLineColor;
        this.verticalFrozenLineColor = verticalFrozenLineColor;

        this.firePropertyChange("verticalFrozenLineColor", old, this.verticalFrozenLineColor);
        this.getElementCasePane().repaint();
    }

    /**
	 * �Ƿ���ʾˮƽ������
	 * 
	 * @return �Ƿ���ʾˮƽ������
	 * 
	 *
	 * @date 2014-12-21-����6:29:35
	 * 
	 */
    public boolean isShowHorizontalFrozenLine() {
        return isShowHorizontalFrozenLine;
    }

    /**
     * Sets to show horizontal frozen line.
     */
    public void setShowHorizontalFrozenLine(boolean showHorizontalFrozenLine) {
        this.isShowHorizontalFrozenLine = showHorizontalFrozenLine;

        this.getElementCasePane().repaint();
    }

    /**
     * Gets horizontal frozen line color.
     */
    public Color getHorizontalFrozenLineColor() {
        return horizontalFrozenLineColor;
    }

    /**
     * Sets horizontal frozen line color.
     *
     * @param horizontalFrozenLineColor the new color of horizontal frozen line.
     */
    public void setHorizontalFrozenLineColor(Color horizontalFrozenLineColor) {
        Color old = this.horizontalFrozenLineColor;
        this.horizontalFrozenLineColor = horizontalFrozenLineColor;

        this.firePropertyChange("horizontalFrozenLineColor", old, this.horizontalFrozenLineColor);
        this.getElementCasePane().repaint();
    }

    /**
     * Gets the selected background.
     */
    public Color getSelectedBackground() {
        return this.selectedBackground;
    }

    /**
     * Sets the selected background.
     *
     * @param selectedBackground the new selected background.
     */
    public void setSelectedBackground(Color selectedBackground) {
        Color old = this.selectedBackground;
        this.selectedBackground = selectedBackground;

        this.firePropertyChange("selectedBackground", old, this.selectedBackground);
        this.getElementCasePane().repaint();
    }

    /**
     * Gets the selected border line color.
     */
    public Color getSelectedBorderLineColor() {
        return selectedBorderLineColor;
    }

    /**
     * Sets the selected border line color.
     *
     * @param selectedBorderLineColor the new color of selected border line.
     */
    public void setSelectedBorderLineColor(Color selectedBorderLineColor) {
        Color old = this.selectedBorderLineColor;
        this.selectedBorderLineColor = selectedBorderLineColor;

        this.firePropertyChange("selectedBorderLineColor", old, this.selectedBorderLineColor);
        this.getElementCasePane().repaint();
    }

    /**
	 * ����Ƿ���Ա��༭
	 * 
	 * @return ����Ƿ���Ա��༭
	 * 
	 *
	 * @date 2014-12-21-����6:29:09
	 * 
	 */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Sets whether to editable.
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * @return
     */
    public FloatElement getDrawingFloatElement() {
        return drawingFloatElement;
    }

    /**
     * @param drawingFloatElement
     */
    public void setDrawingFloatElement(FloatElement drawingFloatElement) {
        this.drawingFloatElement = drawingFloatElement;
    }

    /**
     * @return
     */
    public int getVerticalValue() {
        return verticalValue;
    }

    /**
     * @param verticalValue
     */
    public void setVerticalValue(int verticalValue) {
        this.verticalValue = verticalValue;
    }

    /**
     * @return
     */
    public int getVerticalExtent() {
        return verticalExtent;
    }

    /**
     * @param verticalExtent
     */
    public void setVerticalExtent(int verticalExtent) {
        this.verticalExtent = verticalExtent;
    }

    /**
     * // denny: add the get and set method of verticalBeginValue
     *
     * @return
     */
    public int getVerticalBeginValue() {
        return verticalBeginValue;
    }

    /**
     * @param verticalBeginValue
     */
    public void setVerticalBeinValue(int verticalBeginValue) {
        this.verticalBeginValue = verticalBeginValue;
    }

    /**
     * @return
     */
    public int getHorizontalExtent() {
        return horizontalExtent;
    }

    /**
     * @param horizontalExtent
     */
    public void setHorizontalExtent(int horizontalExtent) {
        this.horizontalExtent = horizontalExtent;
    }

    /**
     * @return
     */
    public int getHorizontalValue() {
        return horizontalValue;
    }

    /**
     * @param horizontalValue
     */
    public void setHorizontalValue(int horizontalValue) {
        this.horizontalValue = horizontalValue;
    }

    /**
     * denny: add the get and set method of horizontalBeginValue
     *
     * @return
     */
    public int getHorizontalBeginValue() {
        return this.horizontalBeginValue;
    }

    /**
     * @param horizontalBeginValue
     */
    public void setHorizontalBeginValue(int horizontalBeginValue) {
        this.horizontalBeginValue = horizontalBeginValue;
    }

    // /////////////editor begin

    /**
	 * �Ƿ��ڱ༭״̬
	 * 
	 * @return �Ƿ��ڱ༭״̬
	 * 
	 *
	 * @date 2014-12-21-����6:28:45
	 * 
	 */
    public boolean isEditing() {
        return this.editorComponent != null;
    }

    /**
	 * ��ǰ�༭�����Ƿ�Ϊ��Ԫ��
	 * 
	 * @return ��ǰ�༭�����Ƿ�Ϊ��Ԫ��
	 * 
	 *
	 * @date 2014-12-21-����6:28:18
	 * 
	 */
    public boolean isCellEditing() {
        return this.isEditing() && cellEditor != null && notShowingTableSelectPane;
    }

    /**
     * @param f
     */
    public void setNotShowingTableSelectPane(boolean f) {
        this.notShowingTableSelectPane = f;
    }

    /**
	 * �Ƿ�������ѡ��Ԫ��׶�
	 * 
	 * @return �Ƿ�������ѡ��Ԫ��׶�
	 * 
	 *
	 * @date 2014-12-21-����6:27:36
	 * 
	 */
    public boolean IsNotShowingTableSelectPane() {
        return this.notShowingTableSelectPane;
    }

    /**
	 * ��ǰ�Ƿ��ڱ༭����Ԫ��
	 * 
	 * @return �Ƿ��ڱ༭����Ԫ��
	 * 
	 *
	 * @date 2014-12-21-����6:26:46
	 * 
	 */
    public boolean isFloatEditing() {
        return this.isEditing() && floatEditor != null;
    }

    /**
     * Returns an appropriate editor for the cell specified by
     * <code>column</code> and <code>row</code>.
     *
     * @param column the column of the cell to edit, where 0 is the first column;
     * @param row    the row of the cell to edit, where 0 is the first row
     * @return the editor for this cell; if <code>null</code> return the default
     *         editor for this type of cell
     * @see com.fr.design.cell.editor.CellEditor
     */
    public CellEditor getCellEditor(int column, int row) {
        ElementCasePane reportPane = this.getElementCasePane();
        ElementCase report = reportPane.getEditingElementCase();
        CellElement cellElement = report.getCellElement(column, row);

        // ��ö���.
        Class objClass = Object.class;// Ĭ�϶�����Object.
        if (cellElement != null && cellElement.getValue() != null) {
            objClass = cellElement.getValue().getClass();
        }

        return this.getDefaultCellEditor(objClass);
    }

    /**
     * Gets the component that is handling the editing session. If nothing is
     * being edited, returns null.
     *
     * @return Component handling editing session
     */
    public Component getEditorComponent() {
        return this.editorComponent;
    }

    /**
     * Gets the index of the column that contains the cell currently being
     * edited. If nothing is being edited, returns -1.
     *
     * @return the index of the column that contains the cell currently being
     *         edited; returns -1 if nothing being edited
     */
    public int getEditingColumn() {
        return editingColumn;
    }

    /**
     * Sets the <code>editingColumn</code> variable.
     *
     * @param editingColumn the column of the cell to be edited
     */
    public void setEditingColumn(int editingColumn) {
        this.editingColumn = editingColumn;
    }

    /**
     * Gets the index of the row that contains the cell currently being edited.
     * If nothing is being edited, returns -1.
     *
     * @return the index of the row that contains the cell currently being
     *         edited; returns -1 if nothing being edited
     */
    public int getEditingRow() {
        return editingRow;
    }

    /**
     * Sets the <code>editingRow</code> variable.
     *
     * @param editingColumn the row of the cell to be edited
     */
    public void setEditingRow(int editingColumn) {
        this.editingRow = editingColumn;
    }

    /**
     * Gets the cell editor.
     *
     * @return the <code>CellEditor</code> that does the editing
     */
    public CellEditor getCellEditor() {
        return cellEditor;
    }

    /**
     * Gets the float editor.
     *
     * @return the <code>FloatEditor</code> that does the editing
     */
    public FloatEditor getFloatEditor() {
        return this.floatEditor;
    }

    /**
     * Sets the <code>cellEditor</code> variable.
     *
     * @param anEditor the CellEditor that does the editing
     */
    public void setCellEditor(CellEditor anEditor) {
        CellEditor old = this.cellEditor;
        this.cellEditor = anEditor;

        firePropertyChange("CellEditor", old, this.cellEditor);
    }

    /**
     * Sets the <code>FloatEditor</code> variable.
     *
     * @param anEditor the FloatEditor that does the editing
     */
    public void setFloatEditor(FloatEditor anEditor) {
        FloatEditor old = this.floatEditor;
        this.floatEditor = anEditor;

        firePropertyChange("FloatEditor", old, this.floatEditor);
    }

    /**
     * Gets the cell editor to be edit Object class.
     *
     * @return the default cell editor to be used for Object Class
     * @see #setDefaultCellEditor
     * @see com.fr.report.cell.CellElement#getValue
     */
    public CellEditor getDefaultCellEditor() {
        return this.getDefaultCellEditor(Object.class);
    }

    /**
     * Gets the float editor to be edit Object class.
     *
     * @return the default float editor to be used for Object Class
     * @see #setDefaultFloatEditor
     * @see com.fr.report.cell.FloatElement#getValue
     */
    public FloatEditor getDefaultFloatEditor() {
        return this.getDefaultFloatEditor(Object.class);
    }

    /**
     * Sets the cell editor to be edit the Object class.
     *
     * @param editor default cell editor to be used for Object Class
     * @see #getDefaultCellEditor
     */
    public void setDefaultCellEditor(CellEditor editor) {
        this.setDefaultCellEditor(Object.class, editor);
    }

    /**
     * Sets the float editor to be edit the Object class.
     *
     * @param editor default float editor to be used for Object Class
     * @see #getDefaultFloatEditor
     */
    public void setDefaultFloatEditor(FloatEditor editor) {
        this.setDefaultFloatEditor(Object.class, editor);
    }

    /**
     * Gets the editor to be edit the class. The <code>Grid</code> installs
     * entries for <code>Object</code>, <code>Number</code>,
     * <code>Boolean</code>,and all values that supported by CellElement.
     *
     * @param objectClass return the default cell editor for this Class
     * @return the default cell editor to be used for this Class
     * @see #setDefaultCellEditor
     * @see com.fr.report.cell.CellElement#getValue
     */
    public CellEditor getDefaultCellEditor(Class objectClass) {
        if (objectClass == null) {
            objectClass = Object.class;
        }

        CellEditor editor = this.prepareDefaultCellEditorsByClass().get(objectClass);
        if (editor != null) {
            return editor;
        } else {
            return getDefaultCellEditor(objectClass.getSuperclass());
        }
    }

    /**
     * Gets the float editor to be edit the class. The <code>Grid</code>
     * installs entries for <code>Object</code>, <code>Number</code>,
     * <code>Boolean</code>,and all values that supported by FloatElement.
     *
     * @param objectClass return the default float editor for this Class
     * @return the default cell editor to be used for this Class
     * @see #setDefaultFloatEditor
     * @see com.fr.report.cell.FloatElement#getValue
     */
    public FloatEditor getDefaultFloatEditor(Class objectClass) {
        if (objectClass == null) {
            objectClass = Object.class;
        }

        FloatEditor editor = this.prepareDefaultFloatEditorsByClass().get(objectClass);
        if (editor != null) {
            return editor;
        } else {
            return getDefaultFloatEditor(objectClass.getSuperclass());
        }
    }

    /**
     * Sets the editor to be edit the class. The <code>Grid</code> installs
     * entries for <code>Object</code>, <code>Number</code>,
     * <code>Boolean</code>,and all values that supported by CellElement.
     *
     * @param objectClass set the default cell editor for this Class
     * @param editor      default cell editor to be used for this Class
     * @see #getDefaultCellEditor
     * @see com.fr.report.cell.CellElement#getValue
     */
    public void setDefaultCellEditor(Class objectClass, CellEditor editor) {
        if (editor != null) {
            this.prepareDefaultCellEditorsByClass().put(objectClass, editor);
        } else {
            this.prepareDefaultCellEditorsByClass().remove(objectClass);
        }
    }

    /**
     * Sets the float editor to be edit the class. The <code>Grid</code>
     * installs entries for <code>Object</code>, <code>Number</code>,
     * <code>Boolean</code>,and all values that supported by CellElement.
     *
     * @param objectClass set the default float editor for this Class
     * @param floatEditor default float editor to be used for this Class
     * @see #getDefaultFloatEditor
     * @see com.fr.report.cell.FloatElement#getValue
     */
    public void setDefaultFloatEditor(Class objectClass, FloatEditor floatEditor) {
        if (floatEditor != null) {
            this.prepareDefaultFloatEditorsByClass().put(objectClass, floatEditor);
        } else {
            this.prepareDefaultFloatEditorsByClass().remove(objectClass);
        }
    }

    /**
	 * ��ʼ��Ԫ��༭
	 * 
	 *
	 * @date 2014-12-21-����6:25:17
	 * 
	 */
    public void startEditing() {
        this.startEditing(false);
    }

    /**
	 * ��ʼ��Ԫ��༭
	 * 
	 * @param byKeyEvent �Ƿ�Ϊ���̴���
	 * 
	 *
	 * @date 2014-12-21-����6:25:17
	 * 
	 */
    protected void startEditing(boolean byKeyEvent) {
        ElementCasePane reportPane = this.getElementCasePane();
        ElementCase report = reportPane.getEditingElementCase();

        Selection s = reportPane.getSelection();
        if (s instanceof FloatSelection) {
            FloatElement selectedFloatElement = report.getFloatElement(((FloatSelection) s).getSelectedFloatName());
            this.stopEditing();// ��Ҫ��ֹͣ.

            Object value = selectedFloatElement.getValue();
            if (value == null) {
                this.floatEditor = this.getDefaultFloatEditor();
            } else {
                this.floatEditor = this.getDefaultFloatEditor(value.getClass());
            }

            if (this.floatEditor == null) {
                // peter:���editorComponent.
                this.editorComponent = null;
                return;
            }

            this.editorComponent = this.floatEditor.getFloatEditorComponent(this, selectedFloatElement, resolution);
            if (this.editorComponent == null) {
                removeEditor();
                return;
            }
            floatEditor.addFloatEditorListener(innerFloatEditorListener);

            this.setFloatEditor(floatEditor);

            if (this.editorComponent instanceof Window) {
                this.editorComponent.setVisible(true);
            } else {
                this.ajustEditorComponentBounds();
                this.add(this.editorComponent);
                this.validate();
                this.editorComponent.requestFocus();
                this.repaint(10);
            }
        } else {// james��
            // Edit CellElement.
            CellSelection cs = (CellSelection) s;
            startCellEditingAt_DEC(cs.getColumn(), cs.getRow(), null, byKeyEvent);
        }
    }

    /**
	 * ��ʼ��Ԫ��༭
	 * 
	 * @param column ��
	 * @param row ��
	 * @param cellTypeClass ��Ԫ������
	 * @param byKeyEvent �Ƿ�Ϊ���̴���
	 * 
	 * @return �༭�Ƿ�ɹ�
	 * 
	 *
	 * @date 2014-12-21-����6:25:17
	 * 
	 */
    public boolean startCellEditingAt_DEC(int column, int row, Class cellTypeClass, boolean byKeyEvent) {
        if (this.isEditing()) {
            this.stopEditing();// ��Ҫ��ֹͣ���ڽ��еı༭.
        }
        if (!this.isEditable()) {// �ж��ܿ��أ��Ƿ���Ա༭.
            return false;
        }
        if (row < 0 || column < 0) {
            return false;
        }

        ElementCasePane reportPane = this.getElementCasePane();
        TemplateElementCase report = reportPane.getEditingElementCase();
        editingCellElement = report.getTemplateCellElement(column, row);
        this.cellEditor = cellTypeClass == null ? this.getCellEditor(column, row) : this.getDefaultCellEditor(cellTypeClass);
        if (this.cellEditor == null) {
            this.editorComponent = null;
            return false;
        }
        // ���뱣֤editingCellElement����null��
        if (editingCellElement == null) {
            editingCellElement = new DefaultTemplateCellElement(column, row);
        }
        editorComponent = getCellEditingComp();
        if (editorComponent == null) {
            return false;
        }
        this.setEditingColumn(column);
        this.setEditingRow(row);
        if (editorComponent instanceof Window) {
            editorComponent.setVisible(true);
        } else {
            // ����Ǵ�KeyEvent�����Ļ���������ı��������֣���Ҫ�������.
            if (byKeyEvent && editorComponent instanceof UITextField) {
                ((UITextField) editorComponent).setText(StringUtils.EMPTY);
            }
            startInnerEditing(column, row);

        }
        return false;
    }

    private Component getCellEditingComp() {
        // marks:����ط����editor
        Component eComp = this.cellEditor.getCellEditorComponent(this, editingCellElement, resolution);

        if (eComp == null) {
            removeEditor();
        } else {
            this.editorComponentLoc = this.cellEditor.getLocationOnCellElement();
            cellEditor.addCellEditorListener(innerCellEditorListener);
            this.setCellEditor(cellEditor);
        }
        return eComp;

    }

    /**
     * ���ڵ�Ԫ������༭
     */
    private void startInnerEditing(int column, int row) {
        this.editingRow = this.editingCellElement.getRow();
        this.editingColumn = this.editingCellElement.getColumn();

        this.ajustEditorComponentBounds();
        this.add(this.editorComponent);
        this.getElementCasePane().ensureColumnRowVisible(column, row);
        this.validate();
        // ��Ҫ���»��ƽ���
        this.repaint(10);
        this.editorComponent.requestFocus();
    }

    /**
	 * ֹͣ�༭״̬
	 * 
	 *
	 * @date 2014-12-21-����6:24:54
	 * 
	 */
    public void stopEditing() {
        // �����ж����������͵ı༭.
        if (this.isCellEditing()) {
            this.stopCellEditingInner(true);
        }
        if (this.isFloatEditing()) {
            this.stopFloatEditingInner(true);
        }
    }

    /**
     * Stop editing. ���༭����Ϊ�����ťʧȥ����󣬲���ҪRequest Focus.
     */
    private void stopFloatEditingInner(boolean isRequestFocus) {
        if (!this.isFloatEditing()) {
            return;
        }

        if (floatEditor == null) {
            if (this.editorComponent != null) {
                this.remove(this.editorComponent);
            }
            return;
        }

        Object newValue = null;
        try {
            newValue = floatEditor.getFloatEditorValue();
        } catch (Exception exp) { // ��׽������Ϣ.
        }
        if (newValue == null) {// If return null, do nothing.
            removeEditor();
            return;
        }

        ElementCasePane reportPane = this.getElementCasePane();
        Selection selection = reportPane.getSelection();
        if (selection instanceof FloatSelection) {// kunsnat: �����ж�, ������ɾ������Ԫ��ʱ ����ֹͣ�༭����.
            FloatSelection fs = (FloatSelection) reportPane.getSelection();

            FloatElement selectedFloatElement = reportPane.getEditingElementCase().getFloatElement(fs.getSelectedFloatName());
            Object oldValue = selectedFloatElement.getValue();
            if (!ComparatorUtils.equals_exactly(oldValue, newValue)) {
                if (newValue instanceof CellImage) {
                    CellImage cellImage = (CellImage) newValue;
                    newValue = cellImage.getImage();
                    if (cellImage.getStyle() != null) {
                        selectedFloatElement.setStyle(cellImage.getStyle());
                    }
                }
                selectedFloatElement.setValue(newValue);
                reportPane.fireTargetModified();
                //����仰��Ϊ���ڱ༭������Ԫ�ع�ʽ��ʱ�򣬵��ȷ�������Ͻ���������ˢ��
                reportPane.getCurrentEditor();
            }
        }

        removeEditor();
        if (isRequestFocus && !this.hasFocus()) {
            this.requestFocus();
        }
    }

    /**
     * Stop editing. ���༭����Ϊ�����ťʧȥ����󣬲���ҪRequest Focus.
     */
    private void stopCellEditingInner(boolean isRequestFocus) {
        if ((!this.isCellEditing())) {
            return;
        }
        if (cellEditor == null) {
            if (this.editorComponent != null) {
                this.remove(this.editorComponent);
            }
            return;
        }

        ElementCasePane reportPane = this.getElementCasePane();
        TemplateElementCase tplEC = reportPane.getEditingElementCase();

        Object newValue = null;
        Object oldValue = null;
        // CellAdapter
        this.editingCellElement = tplEC.getTemplateCellElement(editingColumn, editingRow);
        try {
            newValue = cellEditor.getCellEditorValue();
        } catch (Exception exp) { // ��׽������Ϣ.
        }

        if (cellEditor instanceof TextCellEditor) {
            oldValue = ((TextCellEditor) cellEditor).getOldValue();
        }
        if (isValueEmpty(newValue)) {
            reportPane.clearContents();
            if (cellEditor instanceof FormulaCellEditor || !isValueEmpty(oldValue)) {
                reportPane.fireTargetModified();
            }
            removeEditor();
            return;
        }
        // ���뱣֤editingCellElement����null��
        if (editingCellElement == null) {
            editingCellElement = new DefaultTemplateCellElement(editingColumn, editingRow);
            tplEC.addCellElement(editingCellElement);
        }
        if (setValue4EditingElement(newValue)) {
            shrinkToFit(tplEC);
            reportPane.fireTargetModified();
        }
        removeEditor();

        if (isRequestFocus && !this.hasFocus()) {
            this.requestFocus();
        }
    }

    private boolean isValueEmpty(Object newValue) {
        return (newValue == null || ComparatorUtils.equals(newValue, StringUtils.EMPTY));

    }

    /**
     * ����ֵ����editingCellElement
     *
     * @param newValue
     * @return true if the value changed
     */
    private boolean setValue4EditingElement(Object newValue) {
        if (newValue instanceof TemplateCellElement) {
            TemplateCellElement cellElement = (TemplateCellElement) newValue;
            editingCellElement.setValue(cellElement.getValue());
            editingCellElement.setCellExpandAttr(cellElement.getCellExpandAttr());
            return true;
        } else if (newValue instanceof CellImage) {
            CellImage cellImage = (CellImage) newValue;
            newValue = cellImage.getImage();
            boolean styleChange = false;
            if (!ComparatorUtils.equals_exactly(cellImage.getStyle(), editingCellElement.getStyle())) {
                editingCellElement.setStyle(cellImage.getStyle());
                styleChange = true;
            }
            Object oldValue = this.editingCellElement.getValue();
            boolean imageChange = false;
            if (!ComparatorUtils.equals_exactly(oldValue, newValue)) {
                editingCellElement.setValue(newValue);
                imageChange = true;
            }
            if (styleChange || imageChange) {
                return true;
            }
    	} else {
    		if(newValue instanceof RichText){
        		setShowAsHtml(this.editingCellElement);
    		}
    		
            Object oldValue = this.editingCellElement.getValue();
            if (!ComparatorUtils.equals_exactly(oldValue, newValue)) {
                editingCellElement.setValue(newValue);
                return true;
            }
        }
        return false;
    }
    
	private void setShowAsHtml(CellElement cellElement){
		CellGUIAttr guiAttr = cellElement.getCellGUIAttr();
		if(guiAttr == null){
			guiAttr = new CellGUIAttr();
			cellElement.setCellGUIAttr(guiAttr);
		}
		
		guiAttr.setShowAsHTML(true);
	}

    /**
     * ����Ԫ��������ݹ���ʱ���Զ�������Ԫ��
     *
     * @param tplEC
     */
    private void shrinkToFit(TemplateElementCase tplEC) {
        if (editingCellElement == null) {
        	return;
        }
        
        Object editElementValue = editingCellElement.getValue();
        if (valueNeedFit(editElementValue)) {
        	int mode = this.getElementCasePane().getReportSettings().getShrinkToFitMode();
            GridUtils.shrinkToFit(mode, tplEC, editingCellElement);
        }
    }
    
    //�Ƿ���Ҫ���������Զ�����, Ŀǰֻ���ַ���, ����, ���ı���Ҫ
    private boolean valueNeedFit(Object value){
    	if(value == null){
    		return false;
    	}
    	
    	return value instanceof String ||
    			value instanceof Number ||
    			value instanceof RichText;
    }

    /**
	 * ȡ���༭״̬
	 * 
	 *
	 * @date 2014-12-21-����6:24:34
	 * 
	 */
    public void cancelEditing() {
        if (this.isEditing()) {
            removeEditor();
            this.requestFocus();
        }
    }

    /**
	 * �Ƴ�ѡ�����
	 * 
	 *
	 * @date 2014-12-21-����6:24:16
	 * 
	 */
    public void removeEditor() {
        if (this.isCellEditing()) {
            this.removeCellEditor();
        } else {
            this.removeFloatEditor();
        }
    }

    /**
	 * �Ƴ���Ԫ�����
	 * 
	 *
	 * @date 2014-12-21-����6:24:00
	 * 
	 */
    public void removeCellEditor() {
        CellEditor cellEditor = getCellEditor();
        if (cellEditor == null) {
            return;
        }

        if (this.editorComponent != null) {
            if (this.editorComponent instanceof Window) {
                editorComponent.setVisible(false);
                ((Window) editorComponent).dispose();
            } else {
                this.remove(this.editorComponent);
                this.validate();
            }
        }

        cellEditor.removeCellEditorListener(innerCellEditorListener);

        setCellEditor(null);
        setEditingColumn(-1);
        setEditingRow(-1);
        this.editorComponent = null;
        this.editingCellElement = null;

        this.getElementCasePane().repaint();
    }

    /**
	 * �Ƴ�����Ԫ�����
	 * 
	 *
	 * @date 2014-12-21-����6:23:38
	 * 
	 */
    public void removeFloatEditor() {
        FloatEditor floatEditor = getFloatEditor();
        if (floatEditor != null) {
            if (this.editorComponent != null) {
                if (this.editorComponent instanceof Window) {
                    editorComponent.setVisible(false);
                    ((Window) editorComponent).dispose();
                } else {
                    this.remove(this.editorComponent);
                    this.validate();
                }
            }

            floatEditor.removeFloatEditorListener(innerFloatEditorListener);
        }

        setFloatEditor(null);
        this.editorComponent = null;

        this.getElementCasePane().repaint();
    }

    /**
     * �������������ʼ��defaultEditorsByClass, ��Ϊ�˼ӿ�Grid����ĳ�ʼ���ٶ�.
     * ���û��༭��ʱ��,�Ž��Ǹ�CellEditor�ĳ�ʼ��.
     */
    private Hashtable<Class, CellEditor> prepareDefaultCellEditorsByClass() {
        if (this.defaultCellEditorsByClass == null) {
            this.defaultCellEditorsByClass = new Hashtable<Class, CellEditor>();
            defaultCellEditorsByClass.put(Object.class, new GeneralCellEditor());
        }

        return this.defaultCellEditorsByClass;
    }

    /**
     * �������������ʼ��defaultFloatEditorsByClass, ��Ϊ�˼ӿ�Grid����ĳ�ʼ���ٶ�.
     * ���û��༭��ʱ��,�Ž��Ǹ�FloatEditor�ĳ�ʼ��.
     */
    private Hashtable<Class, FloatEditor> prepareDefaultFloatEditorsByClass() {
        if (this.defaultFloatEditorsByClass == null) {
            this.defaultFloatEditorsByClass = new Hashtable<Class, FloatEditor>();
            defaultFloatEditorsByClass.put(Object.class, new GeneralFloatEditor());
        }

        return this.defaultFloatEditorsByClass;
    }

    // /////////////editor end

    /**
	 * ������¼�
	 * 
	 * @param evtX x����
	 * @param evtY y����
	 * 
	 *
	 * @date 2014-12-21-����6:22:56
	 * 
	 */
    public void doMousePress(double evtX, double evtY) {
        dispatchEvent(new MouseEvent(this, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, (int) evtX, (int) evtY, 1, false));
    }

    /**
     * ����oldRectangle,��ΪCellElement�ĺϲ����ɶ�������.
	 * 
	 * @param report ��ǰ���ӱ���
	 * @param oldRectangle ֮ǰ��ѡ������
	 * 
	 * @return ���������
	 * 
	 *
	 * @date 2014-12-21-����6:22:21
	 * 
	 */
    public Rectangle caculateIntersectsUnion(ElementCase report, Rectangle oldRectangle) {
        Rectangle newRectangle = new Rectangle(oldRectangle);

        Iterator cells = report.intersect(newRectangle.x, newRectangle.y, newRectangle.width, newRectangle.height);
        while (cells.hasNext()) {
            CellElement cellElement = (CellElement) cells.next();

            Rectangle tmpCellElementRect = new Rectangle(cellElement.getColumn(), cellElement.getRow(), cellElement.getColumnSpan(), cellElement.getRowSpan());
            if (newRectangle.intersects(tmpCellElementRect) && !newRectangle.contains(tmpCellElementRect)) {
                newRectangle = newRectangle.union(tmpCellElementRect);
            }
        }

        // ���ʮ����Ҫ������ѭ��һ��.
        if (!GUICoreUtils.isTheSameRect(newRectangle, oldRectangle)) {
            return this.caculateIntersectsUnion(report, newRectangle);
        }

        return newRectangle;
    }

    /**
     * @param event
     * @return
     */
    public Point getToolTipLocation(MouseEvent event) {
        if (StringUtils.isEmpty(this.getToolTipText())) {
            return null;
        }
        if (tooltipLocation == null) {
            tooltipLocation = new Point();
        }

        return this.tooltipLocation;
    }

    /**
     * peter:���µ���editorComponent�Ŀ�Ⱥ͸߶�,�����ǽ����б仯�͵���.
     */
    public void ajustEditorComponentBounds() {
        // û�б༭�����߶��ڵ�����Window����Ҫ����Bound.
        if (this.editorComponent == null || this.editorComponent instanceof Window) {
            return;
        }
        ElementCasePane reportPane = this.getElementCasePane();
        ElementCase report = reportPane.getEditingElementCase();
        // four anchor values.
        DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);
        DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);

        // ����X, Y, width, height
        double x, y, width, height;
        Selection s = reportPane.getSelection();

        if (s instanceof FloatSelection) {
            FloatElement selectedFloatElement = report.getFloatElement(((FloatSelection) s).getSelectedFloatName());
            int h = reportPane.getHorizontalScrollBar().getValue();
            int v = reportPane.getVerticalScrollBar().getValue();
            x = selectedFloatElement.getLeftDistance().toPixD(this.resolution) + columnWidthList.getRangeValue(h, 0).toPixD(this.resolution);
            y = selectedFloatElement.getTopDistance().toPixD(this.resolution) + rowHeightList.getRangeValue(v, 0).toPixD(this.resolution);
            width = selectedFloatElement.getWidth().toPixD(this.resolution);
            height = selectedFloatElement.getHeight().toPixD(this.resolution);
        } else {
            x = columnWidthList.getRangeValue(this.horizontalBeginValue, this.editingColumn).toPixD(this.resolution);
            y = rowHeightList.getRangeValue(this.verticalBeginValue, this.editingRow).toPixD(this.resolution);
            int columnSpan = this.editingCellElement != null ? this.editingCellElement.getColumnSpan() : 1;
            int rowSpan = this.editingCellElement != null ? this.editingCellElement.getRowSpan() : 1;
            width = columnWidthList.getRangeValue(this.editingColumn, this.editingColumn + columnSpan).toPixD(this.resolution) - 1;
            height = rowHeightList.getRangeValue(this.editingRow, this.editingRow + rowSpan).toPixD(this.resolution) - 1;
        }
        applayRect(x, y, width, height);
    }

    private void applayRect(double x, double y, double width, double height) {
        // peter:��Ҫ���Loc������editorComponent��location.
        if (this.editorComponentLoc == null) {
            this.editorComponent.setLocation((int) (x + 1), (int) (y + 1));
        } else {
            this.editorComponent.setLocation((int) (x + this.editorComponentLoc.getX()), (int) (y + this.editorComponentLoc.getY()));
        }
        // ר�Ŵ���TextField, TextField�ĳ�����Ҫ�����ı����ܳ������仯.
        if (this.editorComponent instanceof UITextField) {
            Dimension textPrefSize = this.editorComponent.getPreferredSize();
            // peter:�����ı�������Ҫ���� 4 �����أ�������ʾ��������.
            // peter:��Ҫ���Loc������editorComponent��location.
            if (this.editorComponentLoc == null) {
                this.editorComponent.setSize((int) Math.max(width, textPrefSize.getWidth() + 1), (int) height);
            } else {
                this.editorComponent.setSize((int) (Math.max(width, textPrefSize.getWidth() + 1) - this.editorComponentLoc.getX()), (int) (height - this.editorComponentLoc.getY()));
            }
        } else {
            if (this.editorComponentLoc == null) {
                this.editorComponent.setSize((int) width, (int) height);
            } else {
                this.editorComponent.setSize((int) (width - this.editorComponentLoc.getX()), (int) (height - this.editorComponentLoc.getY()));
            }
        }
    }

    // CellEditorListener, ����ʼ�༭��ʱ��,���Listener�ᱻ��������.ֹͣ�༭�����listener�ᱻɾ��.
    private CellEditorListener innerCellEditorListener = new CellEditorListener() {

        /**
         * This tells the listeners the editor has stopped editing
         */
        public void editingStopped(CellEditorEvent evt) {
            Grid.this.stopCellEditingInner(false);
            // if(Grid.this.)
        }

        /**
         * This tells the listeners the editor has canceled editing
         */
        public void editingCanceled(CellEditorEvent evt) {
            Grid.this.cancelEditing();
        }
    };
    // FloatEditorListener, ����ʼ�༭��ʱ��,���Listener�ᱻ��������.ֹͣ�༭�����listener�ᱻɾ��.
    private FloatEditorListener innerFloatEditorListener = new FloatEditorListener() {

        /**
         * This tells the listeners the editor has stopped editing
         */
        public void editingStopped(FloatEditorEvent evt) {
            Grid.this.stopFloatEditingInner(false);
        }

        /**
         * This tells the listeners the editor has canceled editing
         */
        public void editingCanceled(FloatEditorEvent evt) {
            Grid.this.cancelEditing();
        }
    };

    /**
     * @param dragType
     */
    public void setDragType(int dragType) {
        this.dragType = dragType;
    }

    /**
     * @return
     */
    public int getDragType() {
        return dragType;
    }

    /**
     * @param dragRectangle
     */
    public void setDragRectangle(Rectangle dragRectangle) {
        this.dragRectangle = dragRectangle;
    }

    /**
     * @return
     */
    public Rectangle getDragRectangle() {
        return dragRectangle;
    }

    /**
     * @param x
     * @param y
     */
    public void setTooltipLocation(double x, double y) {
        if (tooltipLocation == null) {
            tooltipLocation = new Point();
        }
        this.tooltipLocation.setLocation(x, y);
    }

//	@Override
//	public void requestFocus() {
//		super.requestFocus();
//	}
}
