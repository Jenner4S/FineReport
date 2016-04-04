// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid.selection;

import com.fr.base.BaseUtils;
import com.fr.base.ConfigManager;
import com.fr.base.ConfigManagerProvider;
import com.fr.base.Formula;
import com.fr.base.NameStyle;
import com.fr.base.Utils;
import com.fr.cache.list.IntList;
import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.cell.CellAttributeAction;
import com.fr.design.actions.cell.CellExpandAttrAction;
import com.fr.design.actions.cell.CellWidgetAttrAction;
import com.fr.design.actions.cell.CleanAuthorityAction;
import com.fr.design.actions.cell.ConditionAttributesAction;
import com.fr.design.actions.cell.EditCellAction;
import com.fr.design.actions.cell.GlobalStyleMenuDef;
import com.fr.design.actions.cell.StyleAction;
import com.fr.design.actions.core.ActionUtils;
import com.fr.design.actions.edit.CopyAction;
import com.fr.design.actions.edit.CutAction;
import com.fr.design.actions.edit.HyperlinkAction;
import com.fr.design.actions.edit.PasteAction;
import com.fr.design.actions.utils.DeprecatedActionManager;
import com.fr.design.cell.clipboard.CellElementsClip;
import com.fr.design.cell.clipboard.ElementsTransferable;
import com.fr.design.designer.TargetComponent;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.imenu.UIMenu;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.report.RowColumnPane;
import com.fr.design.selection.QuickEditor;
import com.fr.general.Inter;
import com.fr.grid.GridUtils;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellGUIAttr;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.ColumnRow;
import com.fr.stable.StableUtils;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.grid.selection:
//            Selection

public class CellSelection extends Selection
{

    public static final int NORMAL = 0;
    public static final int CHOOSE_COLUMN = 1;
    public static final int CHOOSE_ROW = 2;
    private int column;
    private int row;
    private int columnSpan;
    private int rowSpan;
    private int selectedType;
    private Rectangle editRectangle;
    private java.util.List cellRectangleList;

    public CellSelection()
    {
        this(0, 0, 1, 1);
    }

    public CellSelection(int i, int j, int k, int l)
    {
        selectedType = 0;
        editRectangle = new Rectangle(0, 0, 1, 1);
        cellRectangleList = new ArrayList();
        setBounds(i, j, k, l);
        cellRectangleList.add(new Rectangle(i, j, k, l));
    }

    public final void setBounds(int i, int j, int k, int l)
    {
        column = i;
        row = j;
        columnSpan = k;
        rowSpan = l;
        editRectangle.setBounds(i, j, k, l);
    }

    public void setLastRectangleBounds(int i, int j, int k, int l)
    {
        column = i;
        row = j;
        columnSpan = k;
        rowSpan = l;
        if(!cellRectangleList.isEmpty())
            ((Rectangle)cellRectangleList.get(cellRectangleList.size() - 1)).setBounds(i, j, k, l);
    }

    public void setSelectedType(int i)
    {
        selectedType = i;
    }

    public int getSelectedType()
    {
        return selectedType;
    }

    public void addCellRectangle(Rectangle rectangle)
    {
        int i = cellRectangleList.indexOf(rectangle);
        if(i != -1)
            cellRectangleList.remove(i);
        cellRectangleList.add(rectangle);
    }

    public Rectangle getEditRectangle()
    {
        return editRectangle;
    }

    public Rectangle getFirstCellRectangle()
    {
        return (Rectangle)cellRectangleList.get(0);
    }

    public Rectangle getLastCellRectangle()
    {
        return (Rectangle)cellRectangleList.get(cellRectangleList.size() - 1);
    }

    public int getCellRectangleCount()
    {
        return cellRectangleList.size();
    }

    public Rectangle getCellRectangle(int i)
    {
        return (Rectangle)cellRectangleList.get(i);
    }

    public void clearCellRectangles(int i)
    {
        cellRectangleList.remove(i);
    }

    public int containsCell(int i, int j)
    {
        for(int k = 0; k < cellRectangleList.size(); k++)
        {
            Rectangle rectangle = (Rectangle)cellRectangleList.get(k);
            if(rectangle.contains(i, j))
                return k;
        }

        return -1;
    }

    public int getColumn()
    {
        return column;
    }

    public int getRow()
    {
        return row;
    }

    public int getColumnSpan()
    {
        return columnSpan;
    }

    public int getRowSpan()
    {
        return rowSpan;
    }

    public Rectangle toRectangle()
    {
        return new Rectangle(column, row, columnSpan, rowSpan);
    }

    public boolean isSelectedOneCell(ElementCasePane elementcasepane)
    {
        if(getCellRectangleCount() > 1)
            return false;
        if(columnSpan == 1 && rowSpan == 1)
            return true;
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        for(Iterator iterator = templateelementcase.intersect(column, row, columnSpan, rowSpan); iterator.hasNext();)
        {
            CellElement cellelement = (CellElement)iterator.next();
            if(cellelement.getColumnSpan() == columnSpan && cellelement.getRowSpan() == rowSpan)
                return true;
        }

        return false;
    }

    public void asTransferable(ElementsTransferable elementstransferable, ElementCasePane elementcasepane)
    {
        ArrayList arraylist = new ArrayList();
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        TemplateCellElement templatecellelement;
        for(Iterator iterator = templateelementcase.intersect(column, row, columnSpan, rowSpan); iterator.hasNext(); arraylist.add((TemplateCellElement)templatecellelement.deriveCellElement(templatecellelement.getColumn() - column, templatecellelement.getRow() - row)))
            templatecellelement = (TemplateCellElement)iterator.next();

        elementstransferable.addObject(new CellElementsClip(columnSpan, rowSpan, (TemplateCellElement[])arraylist.toArray(new TemplateCellElement[arraylist.size()])));
    }

    public boolean pasteCellElementsClip(CellElementsClip cellelementsclip, ElementCasePane elementcasepane)
    {
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        CellSelection cellselection = cellelementsclip.pasteAt(templateelementcase, column, row);
        if(cellselection != null)
            elementcasepane.setSelection(cellselection);
        return true;
    }

    public boolean pasteString(String s, ElementCasePane elementcasepane)
    {
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        String as[] = StableUtils.splitString(s, '\n');
        for(int i = 0; i < as.length; i++)
        {
            String as1[] = StableUtils.splitString(as[i], '\t');
            for(int j = 0; j < as1.length; j++)
            {
                String s1 = as1[j];
                if(s1.length() > 0 && s1.charAt(0) == '=')
                {
                    templateelementcase.setCellValue(column + j, row + i, new Formula(s1));
                    continue;
                }
                Number number = Utils.string2Number(as1[j]);
                if(number != null)
                {
                    templateelementcase.setCellValue(column + j, row + i, number);
                    continue;
                }
                String s2 = Utils.replaceAllString(as1[j], ",", "");
                number = Utils.string2Number(s2);
                if(number != null)
                    templateelementcase.setCellValue(column + j, row + i, Utils.string2Number(s2));
                else
                    templateelementcase.setCellValue(column + j, row + i, as1[j]);
            }

        }

        elementcasepane.setSelection(new CellSelection(column, row, columnSpan, rowSpan));
        return true;
    }

    public boolean pasteOtherType(Object obj, ElementCasePane elementcasepane)
    {
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        Object obj1 = templateelementcase.getTemplateCellElement(column, row);
        if(obj1 == null)
        {
            obj1 = new DefaultTemplateCellElement(column, row, obj);
            templateelementcase.addCellElement(((TemplateCellElement) (obj1)), false);
        } else
        {
            ((TemplateCellElement) (obj1)).setValue(obj);
        }
        elementcasepane.setSelection(new CellSelection(column, row, 1, 1));
        return true;
    }

    public boolean canMergeCells(ElementCasePane elementcasepane)
    {
        return !isSelectedOneCell(elementcasepane);
    }

    public boolean mergeCells(ElementCasePane elementcasepane)
    {
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        Iterator iterator = templateelementcase.intersect(column, row, columnSpan, rowSpan);
        if(iterator.hasNext() && iterator.hasNext())
        {
            int i = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(elementcasepane), Inter.getLocText("Des-Merger_Cell"), Inter.getLocText("Utils-Merge_Cell"), 2);
            if(i != 0)
                return false;
        }
        templateelementcase.merge(row, (row + rowSpan) - 1, column, (column + columnSpan) - 1);
        return true;
    }

    public boolean canUnMergeCells(ElementCasePane elementcasepane)
    {
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        for(Iterator iterator = templateelementcase.intersect(column, row, columnSpan, rowSpan); iterator.hasNext();)
        {
            CellElement cellelement = (CellElement)iterator.next();
            if(cellelement.getColumnSpan() > 1 || cellelement.getRowSpan() > 1)
                return true;
        }

        return false;
    }

    public boolean unMergeCells(ElementCasePane elementcasepane)
    {
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        for(Iterator iterator = templateelementcase.intersect(column, row, columnSpan, rowSpan); iterator.hasNext();)
        {
            TemplateCellElement templatecellelement = (TemplateCellElement)iterator.next();
            int i = templatecellelement.getColumnSpan();
            int j = templatecellelement.getRowSpan();
            templateelementcase.removeCellElement(templatecellelement);
            templateelementcase.addCellElement((TemplateCellElement)templatecellelement.deriveCellElement(templatecellelement.getColumn(), templatecellelement.getRow(), 1, 1));
            int k = templatecellelement.getColumn();
            while(k < templatecellelement.getColumn() + i) 
            {
                for(int l = templatecellelement.getRow(); l < templatecellelement.getRow() + j; l++)
                    if(k != templatecellelement.getColumn() || l != templatecellelement.getRow())
                        templateelementcase.addCellElement(new DefaultTemplateCellElement(k, l), false);

                k++;
            }
        }

        setBounds(column, row, 1, 1);
        return true;
    }

    public UIPopupMenu createPopupMenu(ElementCasePane elementcasepane)
    {
        UIPopupMenu uipopupmenu = new UIPopupMenu();
        if(BaseUtils.isAuthorityEditing())
        {
            uipopupmenu.add((new CleanAuthorityAction(elementcasepane)).createMenuItem());
            return uipopupmenu;
        }
        uipopupmenu.add(DeprecatedActionManager.getCellMenu(elementcasepane).createJMenu());
        uipopupmenu.add((new EditCellAction(elementcasepane)).createMenuItem());
        if(!ConfigManager.getProviderInstance().hasStyle())
        {
            UIMenu uimenu = new UIMenu(KeySetUtils.GLOBAL_STYLE.getMenuName());
            uimenu.setIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/cell.png"));
            com.fr.design.actions.UpdateAction.UseMenuItem usemenuitem;
            for(Iterator iterator = ConfigManager.getProviderInstance().getStyleNameIterator(); iterator.hasNext(); uimenu.add(usemenuitem))
            {
                String s = (String)iterator.next();
                s = GlobalStyleMenuDef.judgeChina(s);
                NameStyle namestyle = NameStyle.getInstance(s);
                usemenuitem = (new com.fr.design.actions.cell.GlobalStyleMenuDef.GlobalStyleSelection(elementcasepane, namestyle)).createUseMenuItem();
                usemenuitem.setNameStyle(namestyle);
            }

            uimenu.addSeparator();
            uimenu.add(new com.fr.design.actions.cell.GlobalStyleMenuDef.CustomStyleAction(Inter.getLocText("FR-Designer_Custom")));
            uipopupmenu.add(uimenu);
        } else
        {
            uipopupmenu.add((new StyleAction()).createMenuItem());
        }
        JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if(jtemplate.isJWorkBook())
            uipopupmenu.add((new CellWidgetAttrAction(elementcasepane)).createMenuItem());
        uipopupmenu.add((new CellExpandAttrAction()).createMenuItem());
        uipopupmenu.add(DeprecatedActionManager.getPresentMenu(elementcasepane).createJMenu());
        uipopupmenu.add((new ConditionAttributesAction(elementcasepane)).createMenuItem());
        uipopupmenu.add((new CellAttributeAction()).createMenuItem());
        uipopupmenu.add((new HyperlinkAction(elementcasepane)).createMenuItem());
        uipopupmenu.addSeparator();
        uipopupmenu.add((new CutAction(elementcasepane)).createMenuItem());
        uipopupmenu.add((new CopyAction(elementcasepane)).createMenuItem());
        uipopupmenu.add((new PasteAction(elementcasepane)).createMenuItem());
        uipopupmenu.addSeparator();
        uipopupmenu.add(DeprecatedActionManager.getInsertMenu(elementcasepane));
        uipopupmenu.add(DeprecatedActionManager.getDeleteMenu(elementcasepane));
        uipopupmenu.add(DeprecatedActionManager.getClearMenu(elementcasepane));
        return uipopupmenu;
    }

    public boolean clear(com.fr.design.mainframe.ElementCasePane.Clear clear1, ElementCasePane elementcasepane)
    {
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        boolean flag = true;
        int i = getCellRectangleCount();
        for(int j = 0; j < i; j++)
            flag = clearCell(clear1, templateelementcase, j);

        return flag;
    }

    private boolean clearCell(com.fr.design.mainframe.ElementCasePane.Clear clear1, TemplateElementCase templateelementcase, int i)
    {
        ArrayList arraylist = new ArrayList();
        Rectangle rectangle = getCellRectangle(i);
        column = rectangle.x;
        row = rectangle.y;
        columnSpan = rectangle.width;
        rowSpan = rectangle.height;
        CellElement cellelement;
        for(Iterator iterator = templateelementcase.intersect(column, row, columnSpan, rowSpan); iterator.hasNext(); arraylist.add(cellelement))
        {
            cellelement = (CellElement)iterator.next();
            CellGUIAttr cellguiattr = cellelement.getCellGUIAttr();
            if(cellguiattr == null)
                cellguiattr = CellGUIAttr.DEFAULT_CELLGUIATTR;
        }

        if(arraylist.isEmpty())
            return false;
        static class _cls2
        {

            static final int $SwitchMap$com$fr$design$mainframe$ElementCasePane$Clear[];

            static 
            {
                $SwitchMap$com$fr$design$mainframe$ElementCasePane$Clear = new int[com.fr.design.mainframe.ElementCasePane.Clear.values().length];
                try
                {
                    $SwitchMap$com$fr$design$mainframe$ElementCasePane$Clear[com.fr.design.mainframe.ElementCasePane.Clear.ALL.ordinal()] = 1;
                }
                catch(NoSuchFieldError nosuchfielderror) { }
                try
                {
                    $SwitchMap$com$fr$design$mainframe$ElementCasePane$Clear[com.fr.design.mainframe.ElementCasePane.Clear.FORMATS.ordinal()] = 2;
                }
                catch(NoSuchFieldError nosuchfielderror1) { }
                try
                {
                    $SwitchMap$com$fr$design$mainframe$ElementCasePane$Clear[com.fr.design.mainframe.ElementCasePane.Clear.CONTENTS.ordinal()] = 3;
                }
                catch(NoSuchFieldError nosuchfielderror2) { }
                try
                {
                    $SwitchMap$com$fr$design$mainframe$ElementCasePane$Clear[com.fr.design.mainframe.ElementCasePane.Clear.WIDGETS.ordinal()] = 4;
                }
                catch(NoSuchFieldError nosuchfielderror3) { }
            }
        }

        switch(_cls2..SwitchMap.com.fr.design.mainframe.ElementCasePane.Clear[clear1.ordinal()])
        {
        default:
            break;

        case 1: // '\001'
            for(int j = 0; j < arraylist.size(); j++)
            {
                CellElement cellelement1 = (CellElement)arraylist.get(j);
                templateelementcase.removeCellElement((TemplateCellElement)cellelement1);
            }

            break;

        case 2: // '\002'
            for(int k = 0; k < arraylist.size(); k++)
            {
                CellElement cellelement2 = (CellElement)arraylist.get(k);
                cellelement2.setStyle(null);
            }

            break;

        case 3: // '\003'
            for(int l = 0; l < arraylist.size(); l++)
            {
                CellElement cellelement3 = (CellElement)arraylist.get(l);
                cellelement3.setValue(null);
            }

            break;

        case 4: // '\004'
            for(int i1 = 0; i1 < arraylist.size(); i1++)
            {
                CellElement cellelement4 = (CellElement)arraylist.get(i1);
                ((TemplateCellElement)cellelement4).setWidget(null);
            }

            break;
        }
        return true;
    }

    public int[] getSelectedColumns()
    {
        return IntList.range(column, column + columnSpan);
    }

    public int[] getSelectedRows()
    {
        return IntList.range(row, row + rowSpan);
    }

    public void moveLeft(ElementCasePane elementcasepane)
    {
        if(column - 1 < 0)
        {
            return;
        } else
        {
            moveTo(elementcasepane, column - 1, row);
            return;
        }
    }

    public void moveRight(ElementCasePane elementcasepane)
    {
        moveTo(elementcasepane, column + columnSpan, row);
    }

    public void moveUp(ElementCasePane elementcasepane)
    {
        if(row - 1 < 0)
        {
            return;
        } else
        {
            moveTo(elementcasepane, column, row - 1);
            return;
        }
    }

    public void moveDown(ElementCasePane elementcasepane)
    {
        moveTo(elementcasepane, column, row + rowSpan);
    }

    private static void moveTo(ElementCasePane elementcasepane, int i, int j)
    {
        if(GridUtils.canMove(elementcasepane, i, j))
        {
            GridUtils.doSelectCell(elementcasepane, i, j);
            elementcasepane.ensureColumnRowVisible(i, j);
        }
    }

    public boolean triggerDeleteAction(ElementCasePane elementcasepane)
    {
        final TemplateElementCase ec = elementcasepane.getEditingElementCase();
        final RowColumnPane rcPane = new RowColumnPane();
        rcPane.setTitle(Inter.getLocText("FR-Designer_Delete"));
        rcPane.showWindow(SwingUtilities.getWindowAncestor(elementcasepane), new DialogActionAdapter() {

            final RowColumnPane val$rcPane;
            final TemplateElementCase val$ec;
            final CellSelection this$0;

            public void doOk()
            {
                if(rcPane.isEntireRow())
                {
                    int ai[] = getSelectedRows();
                    for(int i = 0; i < ai.length; i++)
                        ec.removeRow(ai[i] - i);

                } else
                {
                    int ai1[] = getSelectedColumns();
                    for(int j = 0; j < ai1.length; j++)
                        ec.removeColumn(ai1[j] - j);

                }
            }

            
            {
                this$0 = CellSelection.this;
                rcPane = rowcolumnpane;
                ec = templateelementcase;
                super();
            }
        }
).setVisible(true);
        return true;
    }

    public boolean containsColumnRow(ColumnRow columnrow)
    {
        return (new Rectangle(column, row, columnSpan, rowSpan)).contains(columnrow.column, columnrow.row);
    }

    public boolean equals(Object obj)
    {
        if(!(obj instanceof CellSelection))
        {
            return false;
        } else
        {
            CellSelection cellselection = (CellSelection)obj;
            return getColumn() == cellselection.getColumn() && getRow() == cellselection.getRow() && getColumnSpan() == cellselection.getColumnSpan() && getRowSpan() == cellselection.getRowSpan();
        }
    }

    public CellSelection clone()
    {
        CellSelection cellselection = new CellSelection(column, row, columnSpan, rowSpan);
        if(editRectangle != null)
            cellselection.editRectangle = (Rectangle)editRectangle.clone();
        ArrayList arraylist = new ArrayList(cellRectangleList.size());
        cellselection.cellRectangleList = arraylist;
        int i = 0;
        for(int j = cellRectangleList.size(); i < j; i++)
            arraylist.add((Rectangle)((Rectangle)cellRectangleList.get(i)).clone());

        cellselection.selectedType = selectedType;
        return cellselection;
    }

    public QuickEditor getQuickEditor(TargetComponent targetcomponent)
    {
        ElementCasePane elementcasepane = (ElementCasePane)targetcomponent;
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        TemplateCellElement templatecellelement = templateelementcase.getTemplateCellElement(column, row);
        Object obj = null;
        boolean flag = elementcasepane.isSelectedOneCell();
        if(templatecellelement != null && flag)
            obj = templatecellelement.getValue();
        obj = obj != null ? obj : "";
        QuickEditor quickeditor = ActionUtils.getCellEditor(((Class) ((obj instanceof Number) ? java/lang/Number : obj.getClass())));
        if(quickeditor == null)
        {
            return null;
        } else
        {
            quickeditor.populate(targetcomponent);
            return quickeditor;
        }
    }

    public volatile Selection clone()
        throws CloneNotSupportedException
    {
        return clone();
    }

    public volatile JPopupMenu createPopupMenu(ElementCasePane elementcasepane)
    {
        return createPopupMenu(elementcasepane);
    }

    public volatile Object clone()
        throws CloneNotSupportedException
    {
        return clone();
    }
}
