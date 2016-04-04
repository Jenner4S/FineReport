// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid;

import com.fr.base.DynamicUnitList;
import com.fr.design.cell.editor.*;
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
import com.fr.page.ReportSettingsProvider;
import com.fr.report.ReportHelper;
import com.fr.report.cell.*;
import com.fr.report.cell.cellattr.CellGUIAttr;
import com.fr.report.cell.cellattr.CellImage;
import com.fr.report.cell.cellattr.core.RichText;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.StringUtils;
import com.fr.stable.unit.FU;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.JScrollBar;

// Referenced classes of package com.fr.grid:
//            BaseGridComponent, GridMouseAdapter, GridKeyListener, GridUI, 
//            GridKeyAction, GridUtils

public class Grid extends BaseGridComponent
{

    private static final int VERTICAL_EXTENT_INITIAL_VALUE = 50;
    private static final int HORIZONTAL_EXTENT_INITIAL_VALUE = 40;
    protected transient Component editorComponent;
    private transient Point2D editorComponentLoc;
    private transient TemplateCellElement editingCellElement;
    private boolean showGridLine;
    private Color gridLineColor;
    private boolean isShowPaginateLine;
    private Color paginationLineColor;
    private boolean isShowVerticalFrozenLine;
    private Color verticalFrozenLineColor;
    private boolean isShowHorizontalFrozenLine;
    private Color horizontalFrozenLineColor;
    private Color selectedBackground;
    private Color selectedBorderLineColor;
    private boolean editable;
    private FloatElement drawingFloatElement;
    private int dragType;
    private Rectangle dragRectangle;
    private Point tooltipLocation;
    private transient CellEditor cellEditor;
    private transient FloatEditor floatEditor;
    private transient int editingColumn;
    private transient int editingRow;
    private transient Hashtable defaultCellEditorsByClass;
    private transient Hashtable defaultFloatEditorsByClass;
    private int verticalValue;
    private int verticalExtent;
    private int horizontalValue;
    private int horizontalExtent;
    private int verticalBeginValue;
    private int horizontalBeginValue;
    private int resolution;
    private boolean notShowingTableSelectPane;
    private CellEditorListener innerCellEditorListener;
    private FloatEditorListener innerFloatEditorListener;

    public Grid(int i)
    {
        showGridLine = true;
        gridLineColor = Color.lightGray;
        isShowPaginateLine = true;
        paginationLineColor = Color.RED;
        isShowVerticalFrozenLine = true;
        verticalFrozenLineColor = Color.black;
        isShowHorizontalFrozenLine = true;
        horizontalFrozenLineColor = Color.black;
        selectedBackground = UIConstants.SELECTED_BACKGROUND;
        selectedBorderLineColor = UIConstants.SELECTED_BORDER_LINE_COLOR;
        editable = true;
        drawingFloatElement = null;
        dragType = 0;
        dragRectangle = null;
        verticalValue = 0;
        verticalExtent = 50;
        horizontalValue = 0;
        horizontalExtent = 40;
        verticalBeginValue = 0;
        horizontalBeginValue = 0;
        notShowingTableSelectPane = true;
        innerCellEditorListener = new CellEditorListener() {

            final Grid this$0;

            public void editingStopped(CellEditorEvent celleditorevent)
            {
                stopCellEditingInner(false);
            }

            public void editingCanceled(CellEditorEvent celleditorevent)
            {
                cancelEditing();
            }

            
            {
                this$0 = Grid.this;
                super();
            }
        }
;
        innerFloatEditorListener = new FloatEditorListener() {

            final Grid this$0;

            public void editingStopped(FloatEditorEvent floateditorevent)
            {
                stopFloatEditingInner(false);
            }

            public void editingCanceled(FloatEditorEvent floateditorevent)
            {
                cancelEditing();
            }

            
            {
                this$0 = Grid.this;
                super();
            }
        }
;
        resolution = i;
        enableEvents(56L);
        GridKeyAction.initGridInputActionMap(this);
        GridMouseAdapter gridmouseadapter = new GridMouseAdapter(this);
        addMouseListener(gridmouseadapter);
        addMouseMotionListener(gridmouseadapter);
        addMouseWheelListener(gridmouseadapter);
        addKeyListener(new GridKeyListener(this));
        setFocusTraversalKeysEnabled(false);
        setOpaque(false);
        updateUI();
    }

    public void updateUI()
    {
        setUI(new GridUI(resolution));
    }

    public boolean isShowGridLine()
    {
        return showGridLine;
    }

    public void setShowGridLine(boolean flag)
    {
        showGridLine = flag;
        getElementCasePane().repaint();
    }

    public Color getGridLineColor()
    {
        return gridLineColor;
    }

    public void setGridLineColor(Color color)
    {
        Color color1 = gridLineColor;
        gridLineColor = color;
        firePropertyChange("girdLineColor", color1, gridLineColor);
        getElementCasePane().repaint();
    }

    public boolean isShowPaginateLine()
    {
        return isShowPaginateLine;
    }

    public void setShowPaginateLine(boolean flag)
    {
        isShowPaginateLine = flag;
        getElementCasePane().repaint();
    }

    public Color getPaginationLineColor()
    {
        return paginationLineColor;
    }

    public void setPaginationLineColor(Color color)
    {
        Color color1 = paginationLineColor;
        paginationLineColor = color;
        firePropertyChange("paginationLineColor", color1, paginationLineColor);
        getElementCasePane().repaint();
    }

    public boolean isShowVerticalFrozenLine()
    {
        return isShowVerticalFrozenLine;
    }

    public void setShowVerticalFrozenLine(boolean flag)
    {
        isShowVerticalFrozenLine = flag;
        getElementCasePane().repaint();
    }

    public Color getVerticalFrozenLineColor()
    {
        return verticalFrozenLineColor;
    }

    public void setVerticalFrozenLineColor(Color color)
    {
        Color color1 = verticalFrozenLineColor;
        verticalFrozenLineColor = color;
        firePropertyChange("verticalFrozenLineColor", color1, verticalFrozenLineColor);
        getElementCasePane().repaint();
    }

    public boolean isShowHorizontalFrozenLine()
    {
        return isShowHorizontalFrozenLine;
    }

    public void setShowHorizontalFrozenLine(boolean flag)
    {
        isShowHorizontalFrozenLine = flag;
        getElementCasePane().repaint();
    }

    public Color getHorizontalFrozenLineColor()
    {
        return horizontalFrozenLineColor;
    }

    public void setHorizontalFrozenLineColor(Color color)
    {
        Color color1 = horizontalFrozenLineColor;
        horizontalFrozenLineColor = color;
        firePropertyChange("horizontalFrozenLineColor", color1, horizontalFrozenLineColor);
        getElementCasePane().repaint();
    }

    public Color getSelectedBackground()
    {
        return selectedBackground;
    }

    public void setSelectedBackground(Color color)
    {
        Color color1 = selectedBackground;
        selectedBackground = color;
        firePropertyChange("selectedBackground", color1, selectedBackground);
        getElementCasePane().repaint();
    }

    public Color getSelectedBorderLineColor()
    {
        return selectedBorderLineColor;
    }

    public void setSelectedBorderLineColor(Color color)
    {
        Color color1 = selectedBorderLineColor;
        selectedBorderLineColor = color;
        firePropertyChange("selectedBorderLineColor", color1, selectedBorderLineColor);
        getElementCasePane().repaint();
    }

    public boolean isEditable()
    {
        return editable;
    }

    public void setEditable(boolean flag)
    {
        editable = flag;
    }

    public FloatElement getDrawingFloatElement()
    {
        return drawingFloatElement;
    }

    public void setDrawingFloatElement(FloatElement floatelement)
    {
        drawingFloatElement = floatelement;
    }

    public int getVerticalValue()
    {
        return verticalValue;
    }

    public void setVerticalValue(int i)
    {
        verticalValue = i;
    }

    public int getVerticalExtent()
    {
        return verticalExtent;
    }

    public void setVerticalExtent(int i)
    {
        verticalExtent = i;
    }

    public int getVerticalBeginValue()
    {
        return verticalBeginValue;
    }

    public void setVerticalBeinValue(int i)
    {
        verticalBeginValue = i;
    }

    public int getHorizontalExtent()
    {
        return horizontalExtent;
    }

    public void setHorizontalExtent(int i)
    {
        horizontalExtent = i;
    }

    public int getHorizontalValue()
    {
        return horizontalValue;
    }

    public void setHorizontalValue(int i)
    {
        horizontalValue = i;
    }

    public int getHorizontalBeginValue()
    {
        return horizontalBeginValue;
    }

    public void setHorizontalBeginValue(int i)
    {
        horizontalBeginValue = i;
    }

    public boolean isEditing()
    {
        return editorComponent != null;
    }

    public boolean isCellEditing()
    {
        return isEditing() && cellEditor != null && notShowingTableSelectPane;
    }

    public void setNotShowingTableSelectPane(boolean flag)
    {
        notShowingTableSelectPane = flag;
    }

    public boolean IsNotShowingTableSelectPane()
    {
        return notShowingTableSelectPane;
    }

    public boolean isFloatEditing()
    {
        return isEditing() && floatEditor != null;
    }

    public CellEditor getCellEditor(int i, int j)
    {
        ElementCasePane elementcasepane = getElementCasePane();
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        CellElement cellelement = templateelementcase.getCellElement(i, j);
        Object obj = java/lang/Object;
        if(cellelement != null && cellelement.getValue() != null)
            obj = cellelement.getValue().getClass();
        return getDefaultCellEditor(((Class) (obj)));
    }

    public Component getEditorComponent()
    {
        return editorComponent;
    }

    public int getEditingColumn()
    {
        return editingColumn;
    }

    public void setEditingColumn(int i)
    {
        editingColumn = i;
    }

    public int getEditingRow()
    {
        return editingRow;
    }

    public void setEditingRow(int i)
    {
        editingRow = i;
    }

    public CellEditor getCellEditor()
    {
        return cellEditor;
    }

    public FloatEditor getFloatEditor()
    {
        return floatEditor;
    }

    public void setCellEditor(CellEditor celleditor)
    {
        CellEditor celleditor1 = cellEditor;
        cellEditor = celleditor;
        firePropertyChange("CellEditor", celleditor1, cellEditor);
    }

    public void setFloatEditor(FloatEditor floateditor)
    {
        FloatEditor floateditor1 = floatEditor;
        floatEditor = floateditor;
        firePropertyChange("FloatEditor", floateditor1, floatEditor);
    }

    public CellEditor getDefaultCellEditor()
    {
        return getDefaultCellEditor(java/lang/Object);
    }

    public FloatEditor getDefaultFloatEditor()
    {
        return getDefaultFloatEditor(java/lang/Object);
    }

    public void setDefaultCellEditor(CellEditor celleditor)
    {
        setDefaultCellEditor(java/lang/Object, celleditor);
    }

    public void setDefaultFloatEditor(FloatEditor floateditor)
    {
        setDefaultFloatEditor(java/lang/Object, floateditor);
    }

    public CellEditor getDefaultCellEditor(Class class1)
    {
        if(class1 == null)
            class1 = java/lang/Object;
        CellEditor celleditor = (CellEditor)prepareDefaultCellEditorsByClass().get(class1);
        if(celleditor != null)
            return celleditor;
        else
            return getDefaultCellEditor(class1.getSuperclass());
    }

    public FloatEditor getDefaultFloatEditor(Class class1)
    {
        if(class1 == null)
            class1 = java/lang/Object;
        FloatEditor floateditor = (FloatEditor)prepareDefaultFloatEditorsByClass().get(class1);
        if(floateditor != null)
            return floateditor;
        else
            return getDefaultFloatEditor(class1.getSuperclass());
    }

    public void setDefaultCellEditor(Class class1, CellEditor celleditor)
    {
        if(celleditor != null)
            prepareDefaultCellEditorsByClass().put(class1, celleditor);
        else
            prepareDefaultCellEditorsByClass().remove(class1);
    }

    public void setDefaultFloatEditor(Class class1, FloatEditor floateditor)
    {
        if(floateditor != null)
            prepareDefaultFloatEditorsByClass().put(class1, floateditor);
        else
            prepareDefaultFloatEditorsByClass().remove(class1);
    }

    public void startEditing()
    {
        startEditing(false);
    }

    protected void startEditing(boolean flag)
    {
        ElementCasePane elementcasepane = getElementCasePane();
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        Selection selection = elementcasepane.getSelection();
        if(selection instanceof FloatSelection)
        {
            FloatElement floatelement = templateelementcase.getFloatElement(((FloatSelection)selection).getSelectedFloatName());
            stopEditing();
            Object obj = floatelement.getValue();
            if(obj == null)
                floatEditor = getDefaultFloatEditor();
            else
                floatEditor = getDefaultFloatEditor(obj.getClass());
            if(floatEditor == null)
            {
                editorComponent = null;
                return;
            }
            editorComponent = floatEditor.getFloatEditorComponent(this, floatelement, resolution);
            if(editorComponent == null)
            {
                removeEditor();
                return;
            }
            floatEditor.addFloatEditorListener(innerFloatEditorListener);
            setFloatEditor(floatEditor);
            if(editorComponent instanceof Window)
            {
                editorComponent.setVisible(true);
            } else
            {
                ajustEditorComponentBounds();
                add(editorComponent);
                validate();
                editorComponent.requestFocus();
                repaint(10L);
            }
        } else
        {
            CellSelection cellselection = (CellSelection)selection;
            startCellEditingAt_DEC(cellselection.getColumn(), cellselection.getRow(), null, flag);
        }
    }

    public boolean startCellEditingAt_DEC(int i, int j, Class class1, boolean flag)
    {
        if(isEditing())
            stopEditing();
        if(!isEditable())
            return false;
        if(j < 0 || i < 0)
            return false;
        ElementCasePane elementcasepane = getElementCasePane();
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        editingCellElement = templateelementcase.getTemplateCellElement(i, j);
        cellEditor = class1 != null ? getDefaultCellEditor(class1) : getCellEditor(i, j);
        if(cellEditor == null)
        {
            editorComponent = null;
            return false;
        }
        if(editingCellElement == null)
            editingCellElement = new DefaultTemplateCellElement(i, j);
        editorComponent = getCellEditingComp();
        if(editorComponent == null)
            return false;
        setEditingColumn(i);
        setEditingRow(j);
        if(editorComponent instanceof Window)
        {
            editorComponent.setVisible(true);
        } else
        {
            if(flag && (editorComponent instanceof UITextField))
                ((UITextField)editorComponent).setText("");
            startInnerEditing(i, j);
        }
        return false;
    }

    private Component getCellEditingComp()
    {
        Component component = cellEditor.getCellEditorComponent(this, editingCellElement, resolution);
        if(component == null)
        {
            removeEditor();
        } else
        {
            editorComponentLoc = cellEditor.getLocationOnCellElement();
            cellEditor.addCellEditorListener(innerCellEditorListener);
            setCellEditor(cellEditor);
        }
        return component;
    }

    private void startInnerEditing(int i, int j)
    {
        editingRow = editingCellElement.getRow();
        editingColumn = editingCellElement.getColumn();
        ajustEditorComponentBounds();
        add(editorComponent);
        getElementCasePane().ensureColumnRowVisible(i, j);
        validate();
        repaint(10L);
        editorComponent.requestFocus();
    }

    public void stopEditing()
    {
        if(isCellEditing())
            stopCellEditingInner(true);
        if(isFloatEditing())
            stopFloatEditingInner(true);
    }

    private void stopFloatEditingInner(boolean flag)
    {
        if(!isFloatEditing())
            return;
        if(floatEditor == null)
        {
            if(editorComponent != null)
                remove(editorComponent);
            return;
        }
        Object obj = null;
        try
        {
            obj = floatEditor.getFloatEditorValue();
        }
        catch(Exception exception) { }
        if(obj == null)
        {
            removeEditor();
            return;
        }
        ElementCasePane elementcasepane = getElementCasePane();
        Selection selection = elementcasepane.getSelection();
        if(selection instanceof FloatSelection)
        {
            FloatSelection floatselection = (FloatSelection)elementcasepane.getSelection();
            FloatElement floatelement = elementcasepane.getEditingElementCase().getFloatElement(floatselection.getSelectedFloatName());
            Object obj1 = floatelement.getValue();
            if(!ComparatorUtils.equals_exactly(obj1, obj))
            {
                if(obj instanceof CellImage)
                {
                    CellImage cellimage = (CellImage)obj;
                    obj = cellimage.getImage();
                    if(cellimage.getStyle() != null)
                        floatelement.setStyle(cellimage.getStyle());
                }
                floatelement.setValue(obj);
                elementcasepane.fireTargetModified();
                elementcasepane.getCurrentEditor();
            }
        }
        removeEditor();
        if(flag && !hasFocus())
            requestFocus();
    }

    private void stopCellEditingInner(boolean flag)
    {
        if(!isCellEditing())
            return;
        if(cellEditor == null)
        {
            if(editorComponent != null)
                remove(editorComponent);
            return;
        }
        ElementCasePane elementcasepane = getElementCasePane();
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        Object obj = null;
        String s = null;
        editingCellElement = templateelementcase.getTemplateCellElement(editingColumn, editingRow);
        try
        {
            obj = cellEditor.getCellEditorValue();
        }
        catch(Exception exception) { }
        if(cellEditor instanceof TextCellEditor)
            s = ((TextCellEditor)cellEditor).getOldValue();
        if(isValueEmpty(obj))
        {
            elementcasepane.clearContents();
            if((cellEditor instanceof FormulaCellEditor) || !isValueEmpty(s))
                elementcasepane.fireTargetModified();
            removeEditor();
            return;
        }
        if(editingCellElement == null)
        {
            editingCellElement = new DefaultTemplateCellElement(editingColumn, editingRow);
            templateelementcase.addCellElement(editingCellElement);
        }
        if(setValue4EditingElement(obj))
        {
            shrinkToFit(templateelementcase);
            elementcasepane.fireTargetModified();
        }
        removeEditor();
        if(flag && !hasFocus())
            requestFocus();
    }

    private boolean isValueEmpty(Object obj)
    {
        return obj == null || ComparatorUtils.equals(obj, "");
    }

    private boolean setValue4EditingElement(Object obj)
    {
        if(obj instanceof TemplateCellElement)
        {
            TemplateCellElement templatecellelement = (TemplateCellElement)obj;
            editingCellElement.setValue(templatecellelement.getValue());
            editingCellElement.setCellExpandAttr(templatecellelement.getCellExpandAttr());
            return true;
        }
        if(obj instanceof CellImage)
        {
            CellImage cellimage = (CellImage)obj;
            obj = cellimage.getImage();
            boolean flag = false;
            if(!ComparatorUtils.equals_exactly(cellimage.getStyle(), editingCellElement.getStyle()))
            {
                editingCellElement.setStyle(cellimage.getStyle());
                flag = true;
            }
            Object obj2 = editingCellElement.getValue();
            boolean flag1 = false;
            if(!ComparatorUtils.equals_exactly(obj2, obj))
            {
                editingCellElement.setValue(obj);
                flag1 = true;
            }
            if(flag || flag1)
                return true;
        } else
        {
            if(obj instanceof RichText)
                setShowAsHtml(editingCellElement);
            Object obj1 = editingCellElement.getValue();
            if(!ComparatorUtils.equals_exactly(obj1, obj))
            {
                editingCellElement.setValue(obj);
                return true;
            }
        }
        return false;
    }

    private void setShowAsHtml(CellElement cellelement)
    {
        CellGUIAttr cellguiattr = cellelement.getCellGUIAttr();
        if(cellguiattr == null)
        {
            cellguiattr = new CellGUIAttr();
            cellelement.setCellGUIAttr(cellguiattr);
        }
        cellguiattr.setShowAsHTML(true);
    }

    private void shrinkToFit(TemplateElementCase templateelementcase)
    {
        if(editingCellElement == null)
            return;
        Object obj = editingCellElement.getValue();
        if(valueNeedFit(obj))
        {
            int i = getElementCasePane().getReportSettings().getShrinkToFitMode();
            GridUtils.shrinkToFit(i, templateelementcase, editingCellElement);
        }
    }

    private boolean valueNeedFit(Object obj)
    {
        if(obj == null)
            return false;
        else
            return (obj instanceof String) || (obj instanceof Number) || (obj instanceof RichText);
    }

    public void cancelEditing()
    {
        if(isEditing())
        {
            removeEditor();
            requestFocus();
        }
    }

    public void removeEditor()
    {
        if(isCellEditing())
            removeCellEditor();
        else
            removeFloatEditor();
    }

    public void removeCellEditor()
    {
        CellEditor celleditor = getCellEditor();
        if(celleditor == null)
            return;
        if(editorComponent != null)
            if(editorComponent instanceof Window)
            {
                editorComponent.setVisible(false);
                ((Window)editorComponent).dispose();
            } else
            {
                remove(editorComponent);
                validate();
            }
        celleditor.removeCellEditorListener(innerCellEditorListener);
        setCellEditor(null);
        setEditingColumn(-1);
        setEditingRow(-1);
        editorComponent = null;
        editingCellElement = null;
        getElementCasePane().repaint();
    }

    public void removeFloatEditor()
    {
        FloatEditor floateditor = getFloatEditor();
        if(floateditor != null)
        {
            if(editorComponent != null)
                if(editorComponent instanceof Window)
                {
                    editorComponent.setVisible(false);
                    ((Window)editorComponent).dispose();
                } else
                {
                    remove(editorComponent);
                    validate();
                }
            floateditor.removeFloatEditorListener(innerFloatEditorListener);
        }
        setFloatEditor(null);
        editorComponent = null;
        getElementCasePane().repaint();
    }

    private Hashtable prepareDefaultCellEditorsByClass()
    {
        if(defaultCellEditorsByClass == null)
        {
            defaultCellEditorsByClass = new Hashtable();
            defaultCellEditorsByClass.put(java/lang/Object, new GeneralCellEditor());
        }
        return defaultCellEditorsByClass;
    }

    private Hashtable prepareDefaultFloatEditorsByClass()
    {
        if(defaultFloatEditorsByClass == null)
        {
            defaultFloatEditorsByClass = new Hashtable();
            defaultFloatEditorsByClass.put(java/lang/Object, new GeneralFloatEditor());
        }
        return defaultFloatEditorsByClass;
    }

    public void doMousePress(double d, double d1)
    {
        dispatchEvent(new MouseEvent(this, 501, System.currentTimeMillis(), 0, (int)d, (int)d1, 1, false));
    }

    public Rectangle caculateIntersectsUnion(ElementCase elementcase, Rectangle rectangle)
    {
        Rectangle rectangle1 = new Rectangle(rectangle);
        Iterator iterator = elementcase.intersect(rectangle1.x, rectangle1.y, rectangle1.width, rectangle1.height);
        do
        {
            if(!iterator.hasNext())
                break;
            CellElement cellelement = (CellElement)iterator.next();
            Rectangle rectangle2 = new Rectangle(cellelement.getColumn(), cellelement.getRow(), cellelement.getColumnSpan(), cellelement.getRowSpan());
            if(rectangle1.intersects(rectangle2) && !rectangle1.contains(rectangle2))
                rectangle1 = rectangle1.union(rectangle2);
        } while(true);
        if(!GUICoreUtils.isTheSameRect(rectangle1, rectangle))
            return caculateIntersectsUnion(elementcase, rectangle1);
        else
            return rectangle1;
    }

    public Point getToolTipLocation(MouseEvent mouseevent)
    {
        if(StringUtils.isEmpty(getToolTipText()))
            return null;
        if(tooltipLocation == null)
            tooltipLocation = new Point();
        return tooltipLocation;
    }

    public void ajustEditorComponentBounds()
    {
        if(editorComponent == null || (editorComponent instanceof Window))
            return;
        ElementCasePane elementcasepane = getElementCasePane();
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        DynamicUnitList dynamicunitlist = ReportHelper.getRowHeightList(templateelementcase);
        DynamicUnitList dynamicunitlist1 = ReportHelper.getColumnWidthList(templateelementcase);
        Selection selection = elementcasepane.getSelection();
        double d;
        double d1;
        double d2;
        double d3;
        if(selection instanceof FloatSelection)
        {
            FloatElement floatelement = templateelementcase.getFloatElement(((FloatSelection)selection).getSelectedFloatName());
            int j = elementcasepane.getHorizontalScrollBar().getValue();
            int l = elementcasepane.getVerticalScrollBar().getValue();
            d = floatelement.getLeftDistance().toPixD(resolution) + dynamicunitlist1.getRangeValue(j, 0).toPixD(resolution);
            d1 = floatelement.getTopDistance().toPixD(resolution) + dynamicunitlist.getRangeValue(l, 0).toPixD(resolution);
            d2 = floatelement.getWidth().toPixD(resolution);
            d3 = floatelement.getHeight().toPixD(resolution);
        } else
        {
            d = dynamicunitlist1.getRangeValue(horizontalBeginValue, editingColumn).toPixD(resolution);
            d1 = dynamicunitlist.getRangeValue(verticalBeginValue, editingRow).toPixD(resolution);
            int i = editingCellElement == null ? 1 : editingCellElement.getColumnSpan();
            int k = editingCellElement == null ? 1 : editingCellElement.getRowSpan();
            d2 = dynamicunitlist1.getRangeValue(editingColumn, editingColumn + i).toPixD(resolution) - 1.0D;
            d3 = dynamicunitlist.getRangeValue(editingRow, editingRow + k).toPixD(resolution) - 1.0D;
        }
        applayRect(d, d1, d2, d3);
    }

    private void applayRect(double d, double d1, double d2, double d3)
    {
        if(editorComponentLoc == null)
            editorComponent.setLocation((int)(d + 1.0D), (int)(d1 + 1.0D));
        else
            editorComponent.setLocation((int)(d + editorComponentLoc.getX()), (int)(d1 + editorComponentLoc.getY()));
        if(editorComponent instanceof UITextField)
        {
            Dimension dimension = editorComponent.getPreferredSize();
            if(editorComponentLoc == null)
                editorComponent.setSize((int)Math.max(d2, dimension.getWidth() + 1.0D), (int)d3);
            else
                editorComponent.setSize((int)(Math.max(d2, dimension.getWidth() + 1.0D) - editorComponentLoc.getX()), (int)(d3 - editorComponentLoc.getY()));
        } else
        if(editorComponentLoc == null)
            editorComponent.setSize((int)d2, (int)d3);
        else
            editorComponent.setSize((int)(d2 - editorComponentLoc.getX()), (int)(d3 - editorComponentLoc.getY()));
    }

    public void setDragType(int i)
    {
        dragType = i;
    }

    public int getDragType()
    {
        return dragType;
    }

    public void setDragRectangle(Rectangle rectangle)
    {
        dragRectangle = rectangle;
    }

    public Rectangle getDragRectangle()
    {
        return dragRectangle;
    }

    public void setTooltipLocation(double d, double d1)
    {
        if(tooltipLocation == null)
            tooltipLocation = new Point();
        tooltipLocation.setLocation(d, d1);
    }


}
