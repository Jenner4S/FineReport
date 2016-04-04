// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.*;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.*;
import com.fr.design.actions.cell.BorderAction;
import com.fr.design.actions.cell.CleanAuthorityAction;
import com.fr.design.actions.cell.style.*;
import com.fr.design.actions.columnrow.*;
import com.fr.design.actions.core.ActionUtils;
import com.fr.design.actions.edit.*;
import com.fr.design.actions.edit.merge.MergeCellAction;
import com.fr.design.actions.edit.merge.UnmergeCellAction;
import com.fr.design.cell.bar.DynamicScrollBar;
import com.fr.design.cell.clipboard.*;
import com.fr.design.cell.editor.*;
import com.fr.design.constants.UIConstants;
import com.fr.design.designer.EditingState;
import com.fr.design.designer.TargetComponent;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.cell.QuickEditorRegion;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.*;
import com.fr.design.selection.*;
import com.fr.general.*;
import com.fr.grid.*;
import com.fr.grid.dnd.ElementCasePaneDropTarget;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.page.PageAttributeGetter;
import com.fr.page.ReportPageAttrProvider;
import com.fr.poly.creator.PolyElementCasePane;
import com.fr.report.ReportHelper;
import com.fr.report.cell.*;
import com.fr.report.cell.cellattr.core.RichText;
import com.fr.report.cell.cellattr.core.SubReport;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import com.fr.report.cell.painter.BiasTextPainter;
import com.fr.report.core.SheetUtils;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.ArrayUtils;
import com.fr.stable.ColumnRow;
import com.fr.stable.unit.FU;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.EventListenerList;

// Referenced classes of package com.fr.design.mainframe:
//            RGridLayout, FormatBrushAction, ElementCasePaneAuthorityEditPane, JWorkBook, 
//            DesignerContext, EastRegionContainerPane, CellElementPropertyPane, DesignerFrame, 
//            JTemplate, AuthorityEditPane

public abstract class ElementCasePane extends TargetComponent
    implements Selectedable, PageAttributeGetter
{
    private class ElementCaseEditingState
        implements EditingState
    {

        protected Selection selection;
        protected int verticalValue;
        protected int horizontalValue;
        final ElementCasePane this$0;

        public void revert()
        {
            try
            {
                ElementCasePane.this.selection = selection.clone();
                fireSelectionChangeListener();
            }
            catch(CloneNotSupportedException clonenotsupportedexception)
            {
                throw new RuntimeException(clonenotsupportedexception);
            }
            getVerticalScrollBar().setValue(verticalValue);
            getHorizontalScrollBar().setValue(horizontalValue);
            repaint();
        }

        protected ElementCaseEditingState(Selection selection1, int i, int j)
        {
            this$0 = ElementCasePane.this;
            super();
            verticalValue = 0;
            horizontalValue = 0;
            try
            {
                selection = selection1.clone();
            }
            catch(CloneNotSupportedException clonenotsupportedexception)
            {
                throw new RuntimeException(clonenotsupportedexception);
            }
            verticalValue = i;
            horizontalValue = j;
        }
    }

    public static final class Clear extends Enum
    {

        public static final Clear ALL;
        public static final Clear FORMATS;
        public static final Clear CONTENTS;
        public static final Clear WIDGETS;
        private static final Clear $VALUES[];

        public static Clear[] values()
        {
            return (Clear[])$VALUES.clone();
        }

        public static Clear valueOf(String s)
        {
            return (Clear)Enum.valueOf(com/fr/design/mainframe/ElementCasePane$Clear, s);
        }

        static 
        {
            ALL = new Clear("ALL", 0);
            FORMATS = new Clear("FORMATS", 1);
            CONTENTS = new Clear("CONTENTS", 2);
            WIDGETS = new Clear("WIDGETS", 3);
            $VALUES = (new Clear[] {
                ALL, FORMATS, CONTENTS, WIDGETS
            });
        }

        private Clear(String s, int i)
        {
            super(s, i);
        }
    }


    public static final int NO_OVER = 0;
    public static final int HORIZONTAL_OVER = 1;
    public static final int VERTICAL_OVER = 2;
    private Selection selection;
    private boolean supportDefaultParentCalculate;
    private Grid grid;
    private GridRow gridRow;
    private GridColumn gridColumn;
    private GridCorner gridCorner;
    private JScrollBar verScrollBar;
    private JScrollBar horScrollBar;
    private boolean columnHeaderVisible;
    private boolean rowHeaderVisible;
    private boolean verticalScrollBarVisible;
    private boolean horizontalScrollBarVisible;
    private int resolution;
    protected UIButton formatBrush;
    private CellSelection formatReferencedCell;
    private CellSelection cellNeedTOFormat;
    private FormatBrushAction formatBrushAction;
    ActionListener keyListener;
    ActionListener escKey;

    public ElementCasePane(TemplateElementCase templateelementcase)
    {
        super(templateelementcase);
        selection = new CellSelection(-1, -1, -1, -1);
        supportDefaultParentCalculate = false;
        columnHeaderVisible = true;
        rowHeaderVisible = true;
        verticalScrollBarVisible = true;
        horizontalScrollBarVisible = true;
        formatBrush = null;
        formatReferencedCell = null;
        cellNeedTOFormat = null;
        keyListener = new ActionListener() {

            final ElementCasePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(!formatBrush.isSelected())
                {
                    DesignerContext.setFormatState(1);
                    DesignerContext.setReferencedElementCasePane(ElementCasePane.this);
                    DesignerContext.setReferencedIndex(((JWorkBook)HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()).getEditingReportIndex());
                    formatBrush.setSelected(true);
                    formatBrushAction.executeActionReturnUndoRecordNeeded();
                } else
                {
                    cancelFormatBrush();
                }
            }

            
            {
                this$0 = ElementCasePane.this;
                super();
            }
        }
;
        escKey = new ActionListener() {

            final ElementCasePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                cancelFormatBrush();
            }

            
            {
                this$0 = ElementCasePane.this;
                super();
            }
        }
;
        enableEvents(0x20000L);
        initComponents();
        new ElementCasePaneDropTarget(this);
        setFocusTraversalKeysEnabled(false);
    }

    public void cancelFormat()
    {
    }

    protected void initComponents()
    {
        setLayout(new RGridLayout());
        resolution = ScreenResolution.getScreenResolution();
        initGridComponent();
        grid.setElementCasePane(this);
        gridColumn.setElementCasePane(this);
        gridRow.setElementCasePane(this);
        gridCorner.setElementCasePane(this);
        add("GridCorner", gridCorner);
        add("GridColumn", gridColumn);
        add("GridRow", gridRow);
        add("Grid", grid);
        verScrollBar = new DynamicScrollBar(1, this, resolution);
        horScrollBar = new DynamicScrollBar(0, this, resolution);
        add("VerticalBar", verScrollBar);
        initInputActionMap();
        setMinimumSize(new Dimension(0, 0));
        initDefaultEditors();
        initFormatBrush();
    }

    public int getMenuState()
    {
        return 0;
    }

    protected void initFormatBrush()
    {
        formatBrushAction = new FormatBrushAction(this);
        formatBrush = (UIButton)formatBrushAction.createToolBarComponent();
        formatBrush.setSelected(DesignerContext.getFormatState() != 0);
        formatBrush.removeActionListener(formatBrushAction);
        formatBrush.addMouseListener(new MouseAdapter() {

            final ElementCasePane this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                if(mouseevent.getClickCount() == 1)
                {
                    if(!formatBrush.isSelected())
                    {
                        DesignerContext.setFormatState(1);
                        DesignerContext.setReferencedElementCasePane(ElementCasePane.this);
                        DesignerContext.setReferencedIndex(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().getEditingReportIndex());
                        formatBrush.setSelected(true);
                        formatBrushAction.executeActionReturnUndoRecordNeeded();
                    } else
                    {
                        cancelFormatBrush();
                    }
                } else
                if(mouseevent.getClickCount() == 2)
                {
                    if(!formatBrush.isSelected())
                        formatBrush.setSelected(true);
                    DesignerContext.setFormatState(2);
                    DesignerContext.setReferencedElementCasePane(ElementCasePane.this);
                    DesignerContext.setReferencedIndex(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().getEditingReportIndex());
                    formatBrush.setSelected(true);
                    formatBrushAction.executeActionReturnUndoRecordNeeded();
                }
            }

            
            {
                this$0 = ElementCasePane.this;
                super();
            }
        }
);
        formatBrush.registerKeyboardAction(keyListener, KeyStroke.getKeyStroke(66, 2), 2);
        formatBrush.registerKeyboardAction(escKey, KeyStroke.getKeyStroke(27, 0), 2);
    }

    public void cancelFormatBrush()
    {
        setFormatState(0);
        formatBrush.setSelected(false);
        grid.setCursor(UIConstants.CELL_DEFAULT_CURSOR);
        if(DesignerContext.getReferencedElementCasePane() == null)
        {
            return;
        } else
        {
            ((ElementCasePane)DesignerContext.getReferencedElementCasePane()).getGrid().setNotShowingTableSelectPane(true);
            ((ElementCasePane)DesignerContext.getReferencedElementCasePane()).getGrid().setCursor(UIConstants.CELL_DEFAULT_CURSOR);
            DesignerContext.setReferencedElementCasePane(null);
            DesignerContext.setReferencedIndex(0);
            repaint();
            return;
        }
    }

    public UIButton getFormatBrush()
    {
        return formatBrush;
    }

    public void setFormatState(int i)
    {
        DesignerContext.setFormatState(i);
        if(i == 0)
            cellNeedTOFormat = null;
    }

    public JPanel getEastUpPane()
    {
        return new JPanel();
    }

    public JPanel getEastDownPane()
    {
        return new JPanel();
    }

    public FormatBrushAction getFormatBrushAction()
    {
        return formatBrushAction;
    }

    protected void initGridComponent()
    {
        if(grid == null)
            grid = new Grid(resolution);
        if(gridColumn == null)
            gridColumn = new GridColumn();
        if(gridRow == null)
            gridRow = new GridRow();
        if(gridCorner == null)
            gridCorner = new GridCorner();
    }

    public boolean mustInVisibleRange()
    {
        return true;
    }

    private void initDefaultEditors()
    {
        Grid grid1 = getGrid();
        grid1.setDefaultCellEditor(com/fr/report/cell/cellattr/core/group/DSColumn, new DSColumnCellEditor(this));
        grid1.setDefaultCellEditor(com/fr/base/Formula, new FormulaCellEditor(this));
        grid1.setDefaultCellEditor(com/fr/report/cell/cellattr/core/RichText, new RichTextCellEditor(this));
        grid1.setDefaultCellEditor(com/fr/report/cell/painter/BiasTextPainter, new BiasTextPainterCellEditor(this));
        grid1.setDefaultCellEditor(java/awt/Image, new ImageCellEditor(this));
        grid1.setDefaultCellEditor(com/fr/report/cell/cellattr/core/SubReport, new SubReportCellEditor(this));
        Class class1 = ActionUtils.getChartCollectionClass();
        if(class1 != null)
        {
            grid1.setDefaultCellEditor(class1, new ChartCellEditor(this));
            grid1.setDefaultFloatEditor(class1, new ChartFloatEditor());
        }
        grid1.setDefaultFloatEditor(com/fr/base/Formula, new FormulaFloatEditor());
        grid1.setDefaultFloatEditor(java/awt/Image, new ImageFloatEditor());
        DesignerEnvManager designerenvmanager = DesignerEnvManager.getEnvManager();
        grid1.setGridLineColor(designerenvmanager.getGridLineColor());
        grid1.setPaginationLineColor(designerenvmanager.getPaginationLineColor());
    }

    public final TemplateElementCase getEditingElementCase()
    {
        return (TemplateElementCase)getTarget();
    }

    public Grid getGrid()
    {
        return grid;
    }

    public GridColumn getGridColumn()
    {
        return gridColumn;
    }

    public GridRow getGridRow()
    {
        return gridRow;
    }

    public GridCorner getGridCorner()
    {
        return gridCorner;
    }

    public JScrollBar getVerticalScrollBar()
    {
        return verScrollBar;
    }

    public JScrollBar getHorizontalScrollBar()
    {
        return horScrollBar;
    }

    public boolean isSupportDefaultParentCalculate()
    {
        return supportDefaultParentCalculate;
    }

    public void setSupportDefaultParentCalculate(boolean flag)
    {
        supportDefaultParentCalculate = flag;
    }

    public ElementsTransferable transferSelection()
    {
        ElementsTransferable elementstransferable = new ElementsTransferable();
        selection.asTransferable(elementstransferable, this);
        return elementstransferable;
    }

    public QuickEditor getCurrentEditor()
    {
        return selection.getQuickEditor(this);
    }

    public void setSelection(Selection selection1)
    {
        if(!ComparatorUtils.equals(selection, selection1) || !ComparatorUtils.equals(EastRegionContainerPane.getInstance().getDownPane(), CellElementPropertyPane.getInstance()))
        {
            selection = selection1;
            fireSelectionChanged();
        }
    }

    public void setOldSelecton(Selection selection1)
    {
        selection = selection1;
    }

    public void setFormatReferencedCell(CellSelection cellselection)
    {
        formatReferencedCell = cellselection;
        getOldStyles(formatReferencedCell);
    }

    private void getOldStyles(CellSelection cellselection)
    {
        Style astyle[][] = new Style[formatReferencedCell.getColumnSpan()][formatReferencedCell.getRowSpan()];
        int i = cellselection.getCellRectangleCount();
        TemplateElementCase templateelementcase = getEditingElementCase();
        for(int j = 0; j < i; j++)
        {
            Rectangle rectangle = cellselection.getCellRectangle(j);
            for(int k = 0; k < rectangle.height; k++)
            {
                for(int l = 0; l < rectangle.width; l++)
                {
                    int i1 = l + rectangle.x;
                    int j1 = k + rectangle.y;
                    Object obj = templateelementcase.getTemplateCellElement(i1, j1);
                    if(obj == null)
                    {
                        obj = new DefaultTemplateCellElement(i1, j1);
                        templateelementcase.addCellElement(((TemplateCellElement) (obj)));
                    }
                    Style style = ((TemplateCellElement) (obj)).getStyle();
                    if(style == null)
                    {
                        Style _tmp = style;
                        style = Style.DEFAULT_STYLE;
                    }
                    astyle[l][k] = style;
                }

            }

        }

        DesignerContext.setReferencedStyle(astyle);
    }

    public CellSelection getFormatReferencedCell()
    {
        return formatReferencedCell;
    }

    public Selection getSelection()
    {
        return selection;
    }

    public boolean isSelectedOneCell()
    {
        return selection.isSelectedOneCell(this);
    }

    public void stopEditing()
    {
        getGrid().stopEditing();
    }

    public boolean cut()
    {
        copy();
        return clearAll();
    }

    public void copy()
    {
        ElementsTransferable elementstransferable = transferSelection();
        Object obj = elementstransferable.getFirstObject();
        if(obj instanceof CellElementsClip)
            elementstransferable.addObject(((CellElementsClip)obj).compateExcelPaste());
        Clipboard clipboard = DesignerContext.getClipboard(getGrid());
        clipboard.setContents(elementstransferable, elementstransferable);
    }

    public boolean paste()
    {
        if(!isEditable())
            return false;
        Object obj = getClipObject();
        if(!DesignerContext.getDesignerFrame().getSelectedJTemplate().accept(obj))
            return false;
        if(obj instanceof FloatElementsClip)
            return selection.pasteFloatElementClip((FloatElementsClip)obj, this);
        if(obj instanceof CellElementsClip)
            return selection.pasteCellElementsClip((CellElementsClip)obj, this);
        if(obj instanceof String)
            return selection.pasteString((String)obj, this);
        else
            return selection.pasteOtherType(obj, this);
    }

    private Object getClipObject()
    {
        Clipboard clipboard = DesignerContext.getClipboard(getGrid());
        Transferable transferable = clipboard.getContents(this);
        if(transferable == null)
            return null;
        Object obj;
        try
        {
            obj = ElementsTransferable.getElementNotStringTransderData(transferable);
        }
        catch(Exception exception)
        {
            try
            {
                obj = transferable.getTransferData(DataFlavor.stringFlavor);
            }
            catch(Exception exception1)
            {
                FRContext.getLogger().error(exception1.getMessage(), exception1);
                return null;
            }
        }
        return obj;
    }

    public int ensureColumnRowVisible(int i, int j)
    {
        Grid grid1 = getGrid();
        int k = grid1.getVerticalValue() + grid1.getVerticalExtent() + 1;
        int l = grid1.getHorizontalValue() + grid1.getHorizontalExtent() + 1;
        int i1 = 0;
        if(grid1.getHorizontalValue() > i)
            getHorizontalScrollBar().setValue(i);
        else
        if(l < i + 1)
            if(this instanceof PolyElementCasePane)
                i1++;
            else
                getHorizontalScrollBar().setValue((i - grid1.getHorizontalExtent()) + 1);
        if(grid1.getVerticalValue() > j)
            getVerticalScrollBar().setValue(j);
        else
        if(k <= j + 1)
            if(this instanceof PolyElementCasePane)
                i1 += 2;
            else
                getVerticalScrollBar().setValue((j - grid1.getVerticalExtent()) + 1);
        return i1;
    }

    public boolean clearAll()
    {
        boolean flag = selection.clear(Clear.ALL, this);
        fireSelectionChanged();
        return flag;
    }

    public boolean clearFormats()
    {
        boolean flag = selection.clear(Clear.FORMATS, this);
        fireSelectionChanged();
        return flag;
    }

    public boolean clearContents()
    {
        if(BaseUtils.isAuthorityEditing())
        {
            return false;
        } else
        {
            boolean flag = selection.clear(Clear.CONTENTS, this);
            fireSelectionChanged();
            return flag;
        }
    }

    public boolean clearWidget()
    {
        boolean flag = selection.clear(Clear.WIDGETS, this);
        fireSelectionChanged();
        return flag;
    }

    public boolean canMergeCell()
    {
        return selection.canMergeCells(this);
    }

    public boolean mergeCell()
    {
        boolean flag = selection.mergeCells(this);
        fireSelectionChanged();
        return flag;
    }

    public boolean canUnMergeCell()
    {
        return selection.canUnMergeCells(this);
    }

    public boolean unMergeCell()
    {
        boolean flag = selection.unMergeCells(this);
        fireSelectionChanged();
        return flag;
    }

    private void fireSelectionChanged()
    {
        fireSelectionChangeListener();
        repaint(15L);
    }

    public void addSelectionChangeListener(SelectionListener selectionlistener)
    {
        listenerList.add(com/fr/design/selection/SelectionListener, selectionlistener);
    }

    public void removeSelectionChangeListener(SelectionListener selectionlistener)
    {
        listenerList.remove(com/fr/design/selection/SelectionListener, selectionlistener);
    }

    public void fireSelectionChangeListener()
    {
        Object aobj[] = listenerList.getListenerList();
        for(int i = aobj.length - 2; i >= 0; i -= 2)
            if(aobj[i] == com/fr/design/selection/SelectionListener)
                ((SelectionListener)aobj[i + 1]).selectionChanged(new SelectionEvent(this));

    }

    public void fireTargetModified()
    {
        TemplateElementCase templateelementcase = getEditingElementCase();
        if(isSupportDefaultParentCalculate())
        {
            SheetUtils.calculateDefaultParent(templateelementcase);
            setSupportDefaultParentCalculate(false);
        }
        super.fireTargetModified();
    }

    protected void initInputActionMap()
    {
        InputMap inputmap = getInputMap(1);
        ActionMap actionmap = getActionMap();
        inputmap.clear();
        actionmap.clear();
        inputmap.put(KeyStroke.getKeyStroke(88, 2), "cut");
        actionmap.put("cut", new AbstractAction() {

            final ElementCasePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(cut())
                    fireTargetModified();
            }

            
            {
                this$0 = ElementCasePane.this;
                super();
            }
        }
);
        inputmap.put(KeyStroke.getKeyStroke(67, 2), "copy");
        actionmap.put("copy", new AbstractAction() {

            final ElementCasePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                copy();
            }

            
            {
                this$0 = ElementCasePane.this;
                super();
            }
        }
);
        inputmap.put(KeyStroke.getKeyStroke(86, 2), "paste");
        actionmap.put("paste", new AbstractAction() {

            final ElementCasePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(paste())
                {
                    QuickEditorRegion.getInstance().populate(getCurrentEditor());
                    fireTargetModified();
                    QuickEditorRegion.getInstance().populate(getCurrentEditor());
                }
            }

            
            {
                this$0 = ElementCasePane.this;
                super();
            }
        }
);
        inputmap.put(KeyStroke.getKeyStroke(127, 0), "delete_content");
        actionmap.put("delete_content", new AbstractAction() {

            final ElementCasePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(clearContents())
                    fireTargetModified();
            }

            
            {
                this$0 = ElementCasePane.this;
                super();
            }
        }
);
        inputmap.put(KeyStroke.getKeyStroke(127, 2), "delete_all");
        actionmap.put("delete_all", new AbstractAction() {

            final ElementCasePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(clearAll())
                    fireTargetModified();
            }

            
            {
                this$0 = ElementCasePane.this;
                super();
            }
        }
);
    }

    public boolean isColumnHeaderVisible()
    {
        return columnHeaderVisible;
    }

    public void setColumnHeaderVisible(boolean flag)
    {
        columnHeaderVisible = flag;
        resizeAndRepaint();
    }

    public boolean isRowHeaderVisible()
    {
        return rowHeaderVisible;
    }

    public void setRowHeaderVisible(boolean flag)
    {
        rowHeaderVisible = flag;
        resizeAndRepaint();
    }

    public boolean isVerticalScrollBarVisible()
    {
        return verticalScrollBarVisible;
    }

    public void setVerticalScrollBarVisible(boolean flag)
    {
        verticalScrollBarVisible = flag;
    }

    public boolean isHorizontalScrollBarVisible()
    {
        return horizontalScrollBarVisible;
    }

    public void setHorizontalScrollBarVisible(boolean flag)
    {
        horizontalScrollBarVisible = flag;
    }

    public boolean isEditable()
    {
        return grid.isEditable();
    }

    public void setEditable(boolean flag)
    {
        grid.setEditable(flag);
    }

    public JPopupMenu createPopupMenu()
    {
        return selection.createPopupMenu(this);
    }

    public UIPopupMenu createRowPopupMenu(MouseEvent mouseevent, int i)
    {
        UIPopupMenu uipopupmenu = new UIPopupMenu();
        if(BaseUtils.isAuthorityEditing())
        {
            uipopupmenu.add((new CleanAuthorityAction(this)).createMenuItem());
            return uipopupmenu;
        }
        InsertRowAction insertrowaction = new InsertRowAction(this, Inter.getLocText("Utils-Insert_Row"));
        DeleteRowAction deleterowaction = new DeleteRowAction(this, Inter.getLocText("Utils-Delete_Row"));
        RowHeightAction rowheightaction = new RowHeightAction(this, i);
        RowHideAction rowhideaction = new RowHideAction(this, i);
        ResetRowHideAction resetrowhideaction = new ResetRowHideAction(this, i);
        uipopupmenu.add(insertrowaction.createMenuItem());
        uipopupmenu.add(deleterowaction.createMenuItem());
        uipopupmenu.addSeparator();
        uipopupmenu.add(rowheightaction.createMenuItem());
        if(supportRepeatedHeaderFooter())
        {
            CellSelection cellselection = (CellSelection)getSelection();
            addRowMenu(uipopupmenu, mouseevent, cellselection.getRow(), (cellselection.getRow() + cellselection.getRowSpan()) - 1);
        }
        uipopupmenu.add(rowhideaction.createMenuItem());
        uipopupmenu.add(resetrowhideaction.createMenuItem());
        return uipopupmenu;
    }

    private void addRowMenu(JPopupMenu jpopupmenu, MouseEvent mouseevent, int i, int j)
    {
        HeadRowAction headrowaction = new HeadRowAction(this);
        FootRowAction footrowaction = new FootRowAction(this);
        TemplateElementCase templateelementcase = getEditingElementCase();
        boolean flag = false;
        ColumnRow columnrow = GridUtils.getAdjustEventColumnRow(this, mouseevent.getX(), mouseevent.getY());
        ReportPageAttrProvider reportpageattrprovider = templateelementcase.getReportPageAttr();
        TemplateElementCase templateelementcase1 = getEditingElementCase();
        if(reportpageattrprovider != null)
        {
            jpopupmenu.addSeparator();
            jpopupmenu.add(headrowaction.createMenuItem());
            jpopupmenu.add(footrowaction.createMenuItem());
            int k = templateelementcase1.getReportPageAttr().getRepeatHeaderRowFrom();
            int l = templateelementcase1.getReportPageAttr().getRepeatHeaderRowTo();
            int i1 = templateelementcase1.getReportPageAttr().getRepeatFooterRowFrom();
            int j1 = templateelementcase1.getReportPageAttr().getRepeatFooterRowTo();
            if(columnrow.getRow() >= i && columnrow.getRow() <= j)
            {
                flag = isCancel(templateelementcase1, i, j, k, l, i1, j1);
            } else
            {
                int k1 = columnrow.getRow();
                if(templateelementcase1.getReportPageAttr() != null && (k1 == k || k1 == l || k1 == i1 || k1 == j1))
                    flag = true;
            }
            if(flag)
            {
                CancelRowAction cancelrowaction = new CancelRowAction(this);
                jpopupmenu.add(cancelrowaction.createMenuItem());
            }
        }
    }

    private boolean isCancel(ElementCase elementcase, int i, int j, int k, int l, int i1, int j1)
    {
        boolean flag = false;
        if(elementcase.getReportPageAttr() != null)
        {
            if(k == i && l == j)
                flag = true;
            if(i1 == i && j1 == j)
                flag = true;
        }
        return flag;
    }

    public UIPopupMenu createColumnPopupMenu(MouseEvent mouseevent, int i)
    {
        UIPopupMenu uipopupmenu = new UIPopupMenu();
        if(BaseUtils.isAuthorityEditing())
        {
            uipopupmenu.add((new CleanAuthorityAction(this)).createMenuItem());
            return uipopupmenu;
        }
        InsertColumnAction insertcolumnaction = new InsertColumnAction(this, Inter.getLocText("Utils-Insert_Column"));
        DeleteColumnAction deletecolumnaction = new DeleteColumnAction(this, Inter.getLocText("Utils-Delete_Column"));
        ColumnWidthAction columnwidthaction = new ColumnWidthAction(this, i);
        ColumnHideAction columnhideaction = new ColumnHideAction(this, i);
        ResetColumnHideAction resetcolumnhideaction = new ResetColumnHideAction(this, i);
        uipopupmenu.add(insertcolumnaction.createMenuItem());
        uipopupmenu.add(deletecolumnaction.createMenuItem());
        uipopupmenu.addSeparator();
        uipopupmenu.add(columnwidthaction.createMenuItem());
        if(supportRepeatedHeaderFooter())
        {
            CellSelection cellselection = (CellSelection)getSelection();
            addColumnMenu(uipopupmenu, mouseevent, cellselection.getColumn(), (cellselection.getColumn() + cellselection.getColumnSpan()) - 1);
        }
        uipopupmenu.add(columnhideaction.createMenuItem());
        uipopupmenu.add(resetcolumnhideaction.createMenuItem());
        return uipopupmenu;
    }

    private void addColumnMenu(UIPopupMenu uipopupmenu, MouseEvent mouseevent, int i, int j)
    {
        HeadColumnAction headcolumnaction = new HeadColumnAction(this);
        FootColumnAction footcolumnaction = new FootColumnAction(this);
        ColumnRow columnrow = GridUtils.getAdjustEventColumnRow(this, mouseevent.getX(), mouseevent.getY());
        TemplateElementCase templateelementcase = getEditingElementCase();
        boolean flag = false;
        ReportPageAttrProvider reportpageattrprovider = templateelementcase.getReportPageAttr();
        if(reportpageattrprovider != null)
        {
            uipopupmenu.addSeparator();
            uipopupmenu.add(headcolumnaction.createMenuItem());
            uipopupmenu.add(footcolumnaction.createMenuItem());
            int k = reportpageattrprovider.getRepeatHeaderColumnFrom();
            int l = reportpageattrprovider.getRepeatHeaderColumnTo();
            int i1 = reportpageattrprovider.getRepeatFooterColumnFrom();
            int j1 = reportpageattrprovider.getRepeatFooterColumnTo();
            int k1 = columnrow.getColumn();
            if(k1 >= i && k1 <= j)
                flag = isCancel(templateelementcase, i, j, k, l, i1, j1);
            else
            if(templateelementcase.getReportPageAttr() != null && (k1 == k || k1 == l || k1 == i1 || k1 == j1))
                flag = true;
            if(flag)
            {
                CancelColumnAction cancelcolumnaction = new CancelColumnAction(this);
                uipopupmenu.add(cancelcolumnaction.createMenuItem());
            }
        }
    }

    protected abstract boolean supportRepeatedHeaderFooter();

    public void requestFocus()
    {
        super.requestFocus();
        getGrid().requestFocus();
    }

    protected void resizeAndRepaint()
    {
        revalidate();
        repaint();
    }

    public ShortCut[] shortcut4TemplateMenu()
    {
        return new ShortCut[0];
    }

    public ShortCut[] shortCuts4Authority()
    {
        return (new ShortCut[] {
            new NameSeparator(Inter.getLocText(new String[] {
                "DashBoard-Potence", "Edit"
            })), BaseUtils.isAuthorityEditing() ? new ExitAuthorityEditAction(this) : new AllowAuthorityEditAction(this)
        });
    }

    public MenuDef[] menus4Target()
    {
        return new MenuDef[0];
    }

    public ToolBarDef[] toolbars4Target()
    {
        return (new ToolBarDef[] {
            createFontToolBar(), createAlignmentToolBar(), createStyleToolBar(), createCellToolBar(), createInsertToolBar()
        });
    }

    public JComponent[] toolBarButton4Form()
    {
        formatBrush.setSelected(DesignerContext.getFormatState() != 0);
        return (new JComponent[] {
            (new CutAction(this)).createToolBarComponent(), (new CopyAction(this)).createToolBarComponent(), (new PasteAction(this)).createToolBarComponent(), formatBrush
        });
    }

    protected ToolBarDef createFontToolBar()
    {
        return ShortCut.asToolBarDef(new ShortCut[] {
            new ReportFontNameAction(this), new ReportFontSizeAction(this), new ReportFontBoldAction(this), new ReportFontItalicAction(this), new ReportFontUnderlineAction(this)
        });
    }

    protected ToolBarDef createAlignmentToolBar()
    {
        return ShortCut.asToolBarDef(new ShortCut[] {
            new AlignmentAction(this, new Icon[] {
                BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_left_normal.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_center_normal.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_right_normal.png")
            }, new Integer[] {
                Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(4)
            })
        });
    }

    protected ToolBarDef createStyleToolBar()
    {
        return ShortCut.asToolBarDef(new ShortCut[] {
            new BorderAction(this), new StyleBackgroundAction(this), new ReportFontForegroundAction(this)
        });
    }

    protected ToolBarDef createCellToolBar()
    {
        return ShortCut.asToolBarDef(new ShortCut[] {
            new MergeCellAction(this), new UnmergeCellAction(this)
        });
    }

    protected ToolBarDef createInsertToolBar()
    {
        MenuDef menudef = new MenuDef();
        menudef.setName(KeySetUtils.INSERT_FLOAT.getMenuKeySetName());
        menudef.setTooltip(Inter.getLocText("T_Insert-Float"));
        menudef.setIconPath("/com/fr/design/images/m_insert/floatPop.png");
        com.fr.design.actions.UpdateAction aupdateaction[] = ActionUtils.createFloatInsertAction(com/fr/design/mainframe/ElementCasePane, this);
        for(int i = 0; i < aupdateaction.length; i++)
            menudef.addShortCut(new ShortCut[] {
                aupdateaction[i]
            });

        com.fr.design.actions.UpdateAction aupdateaction1[] = ActionUtils.createCellInsertAction(com/fr/design/mainframe/ElementCasePane, this);
        ShortCut ashortcut[] = new ShortCut[aupdateaction1.length];
        System.arraycopy(aupdateaction1, 0, ashortcut, 0, aupdateaction1.length);
        return ShortCut.asToolBarDef((ShortCut[])(ShortCut[])ArrayUtils.add(ashortcut, menudef));
    }

    public AuthorityEditPane createAuthorityEditPane()
    {
        return new ElementCasePaneAuthorityEditPane(this);
    }

    public ToolBarMenuDockPlus getToolBarMenuDockPlus()
    {
        return new JWorkBook();
    }

    public EditingState createEditingState()
    {
        return new ElementCaseEditingState(selection, verScrollBar.getValue(), horScrollBar.getValue());
    }

    public void setCellNeedTOFormat(CellSelection cellselection)
    {
        cellNeedTOFormat = cellselection;
    }

    public CellSelection getCellNeedTOFormat()
    {
        return cellNeedTOFormat;
    }

    public void addFloatElementToCenterOfElementPane(FloatElement floatelement)
    {
        TemplateElementCase templateelementcase = getEditingElementCase();
        long l = floatelement.getWidth().toFU();
        long l1 = floatelement.getHeight().toFU();
        int i = getGrid().getHorizontalValue();
        int j = getGrid().getHorizontalExtent();
        int k = getGrid().getVerticalValue();
        int i1 = getGrid().getVerticalExtent();
        DynamicUnitList dynamicunitlist = ReportHelper.getColumnWidthList(templateelementcase);
        DynamicUnitList dynamicunitlist1 = ReportHelper.getRowHeightList(templateelementcase);
        long l2 = dynamicunitlist.getRangeValue(i, i + j).toFU() - l;
        long l3 = dynamicunitlist1.getRangeValue(k, k + i1).toFU() - l1;
        int j1 = i;
        long l4 = 0L;
        int k1 = k;
        long l5 = 0L;
        if(l2 > 0L)
        {
            int i2 = 0;
            do
            {
                i2 = (int)((long)i2 + dynamicunitlist.get(j1).toFU());
                if((long)i2 > l2 / 2L)
                {
                    l4 = (long)i2 - l2 / 2L - dynamicunitlist.get(j1).toFU();
                    break;
                }
                j1++;
            } while(true);
        }
        if(l3 > 0L)
        {
            int j2 = 0;
            do
            {
                j2 = (int)((long)j2 + dynamicunitlist1.get(k1).toFU());
                if((long)j2 > l3 / 2L)
                {
                    l5 = (long)j2 - l3 / 2L - dynamicunitlist1.get(k1).toFU();
                    break;
                }
                k1++;
            } while(true);
        }
        floatelement.setLeftDistance(FU.getInstance(Math.max(l4, 0L)));
        floatelement.setTopDistance(FU.getInstance(Math.max(l5, 0L)));
        floatelement.setWidth(FU.getInstance(l));
        floatelement.setHeight(FU.getInstance(l1));
        templateelementcase.addFloatElement(floatelement);
    }

    public volatile void setSelection(SelectableElement selectableelement)
    {
        setSelection((Selection)selectableelement);
    }

    public volatile SelectableElement getSelection()
    {
        return getSelection();
    }


}
