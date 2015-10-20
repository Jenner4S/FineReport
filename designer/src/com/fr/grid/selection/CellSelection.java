package com.fr.grid.selection;

import com.fr.base.*;
import com.fr.cache.list.IntList;
import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.cell.*;
import com.fr.design.actions.cell.GlobalStyleMenuDef.GlobalStyleSelection;
import com.fr.design.actions.core.ActionUtils;
import com.fr.design.actions.edit.CopyAction;
import com.fr.design.actions.edit.CutAction;
import com.fr.design.actions.edit.HyperlinkAction;
import com.fr.design.actions.edit.PasteAction;
import com.fr.design.actions.utils.DeprecatedActionManager;
import com.fr.design.cell.clipboard.CellElementsClip;
import com.fr.design.cell.clipboard.ElementsTransferable;
import com.fr.design.designer.TargetComponent;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.imenu.UIMenu;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.ElementCasePane.Clear;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.KeySetUtils;
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

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * the cell selection (column��row)����ѡ��Ԫ�������Ͻǵ�λ�� �� ������ݽṹ����һ��Rectangle
 *
 * @editor zhou 2012-3-22����1:53:59
 */
public class CellSelection extends Selection {
    public static final int NORMAL = 0;
    public static final int CHOOSE_COLUMN = 1;
    public static final int CHOOSE_ROW = 2;

    private int column;
    private int row;
    private int columnSpan;
    private int rowSpan;
    private int selectedType = NORMAL;

    private Rectangle editRectangle = new Rectangle(0, 0, 1, 1);
    private List cellRectangleList = new ArrayList();

    public CellSelection() {
        this(0, 0, 1, 1);
        //this.cellRectangleList.add(new Rectangle(0, 0, 1, 1));
    }


    public CellSelection(int column, int row, int columnSpan, int rowSpan) {
        setBounds(column, row, columnSpan, rowSpan);
        this.cellRectangleList.add(new Rectangle(column, row, columnSpan, rowSpan));

    }

    public final void setBounds(int column, int row, int columnSpan, int rowSpan) {
        this.column = column;
        this.row = row;
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
        editRectangle.setBounds(column, row, columnSpan, rowSpan);
    }

    public void setLastRectangleBounds(int column, int row, int columnSpan, int rowSpan) {
        this.column = column;
        this.row = row;
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
        if (!cellRectangleList.isEmpty()) {
            ((Rectangle) cellRectangleList.get(cellRectangleList.size() - 1)).setBounds(column, row, columnSpan, rowSpan);
        }
    }

    public void setSelectedType(int chooseType) {
        this.selectedType = chooseType;
    }

    public int getSelectedType() {
        return selectedType;
    }

    /**
     * ����ѡ�е�����
     * @param cellRectangle ����
     */
    public void addCellRectangle(Rectangle cellRectangle) {
        int index = this.cellRectangleList.indexOf(cellRectangle);
        if (index != -1) {
            this.cellRectangleList.remove(index);
        }
        this.cellRectangleList.add(cellRectangle);
    }

    /**
     * Gets edit rectangle
     */
    public Rectangle getEditRectangle() {
        return this.editRectangle;
    }

    /**
     * Gets the only cell rectangle
     */
    public Rectangle getFirstCellRectangle() {
        //p:���ﲻ�жϳߴ磬ֱ�������״���type==CELL��ʱ��List����һ������0.
        return (Rectangle) this.cellRectangleList.get(0);
    }

    /**
     * Gets the last cell rectangle
     */
    public Rectangle getLastCellRectangle() {
        return (Rectangle) this.cellRectangleList.get(this.cellRectangleList.size() - 1);
    }


    /**
     * Gets the count of cell rectangle
     */
    public int getCellRectangleCount() {
        return this.cellRectangleList.size();
    }

    /**
     * Gets the cell rectangle at given position
     */
    public Rectangle getCellRectangle(int index) {
        return (Rectangle) this.cellRectangleList.get(index);
    }


    /**
     * ��������
     * @param i �����
     */
    public void clearCellRectangles(int i) {
        this.cellRectangleList.remove(i);
    }

    /**
     * ������Ԫ��
     * @param column  ��
     * @param row  ��
     * @return ������������-1
     */
    public int containsCell(int column, int row) {
        for (int i = 0; i < this.cellRectangleList.size(); i++) {
            Rectangle tmpRectangle = (Rectangle) this.cellRectangleList.get(i);
            if (tmpRectangle.contains(column, row)) {
                return i;
            }
        }

        return -1;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public int getColumnSpan() {
        return columnSpan;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    /**
     * ת���ɾ���
     * @return ����
     */
    public Rectangle toRectangle() {
        return new Rectangle(column, row, columnSpan, rowSpan);
    }

    /**
     * �Ƿ�ѡ��һ����Ԫ��
     * @param ePane ����
     * @return ���򷵻�rue
     */
    public boolean isSelectedOneCell(ElementCasePane ePane) {
        if (getCellRectangleCount() > 1) {
            return false;
        }

        if (columnSpan == 1 && rowSpan == 1) {
            return true;
        }
        TemplateElementCase ec = ePane.getEditingElementCase();
        Iterator containedCellElementIterator = ec.intersect(column, row, columnSpan, rowSpan);
        while (containedCellElementIterator.hasNext()) {
            CellElement cellElement = (CellElement) containedCellElementIterator.next();
            if (cellElement.getColumnSpan() == columnSpan && cellElement.getRowSpan() == rowSpan) {
                return true;
            }
        }
        return false;

    }


    /**
     * ��Ϊ�ɴ����
     * @param transferable �������
     * @param ePane ����
     */
    public void asTransferable(ElementsTransferable transferable, ElementCasePane ePane) {
        java.util.List<TemplateCellElement> list = new java.util.ArrayList<TemplateCellElement>();

        TemplateElementCase ec = ePane.getEditingElementCase();
        Iterator cells = ec.intersect(column, row, columnSpan, rowSpan);
        while (cells.hasNext()) {
            TemplateCellElement cellElement = (TemplateCellElement) cells.next();
            list.add((TemplateCellElement) cellElement.deriveCellElement(cellElement.getColumn() - column, cellElement.getRow() - row));
        }

        transferable.addObject(new CellElementsClip(this.columnSpan, this.rowSpan, list.toArray(new TemplateCellElement[list.size()])));
    }

    /**
     * �����Ԫ��
     * @param ceClip ��Ԫ��
     * @param ePane ����
     * @return  �ɹ�����true
     */
    public boolean pasteCellElementsClip(CellElementsClip ceClip, ElementCasePane ePane) {
        TemplateElementCase ec = ePane.getEditingElementCase();
        CellSelection cs = ceClip.pasteAt(ec, column, row);
        if (cs != null) {
            ePane.setSelection(cs);
        }

        return true;
    }

    /**
     * ����ַ���
     * @param str �ַ���
     * @param ePane  ����
     * @return �ɹ�����true
     */
    public boolean pasteString(String str, ElementCasePane ePane) {
        // ��Ҫ��Ҫ����Excel���е�����.
        // Excel �ļ������ʽ
        // Excel �ļ������ʽ�ǳ��򵥡��������Ʊ���ָ�ͬһ���ϵ�Ԫ�أ�
        // ���û��з��ָ��С���������������һ�������ĺ�/�����ڵĵ�Ԫ��ʱ��Excel
        // ֻ�����ӱ�����ݱ�ǵ�һ�����ַ����У�������Ԫ��ֵ�ɸ��ַ����ڵ��Ʊ���ͻ��з��ָ���
        // �����ѡ�ĵ�Ԫ������ʱ��ô�죿�ܼ򵥣�Excel ������������ѡ���ݸ��Ƶ������塣
        // set value to current edit cell element.

        TemplateElementCase ec = ePane.getEditingElementCase();

        String[] allTextArray = StableUtils.splitString(str, '\n');
        for (int r = 0; r < allTextArray.length; r++) {
            String[] lineTextArray = StableUtils.splitString(allTextArray[r], '\t');
            for (int c = 0; c < lineTextArray.length; c++) {
                String textValue = lineTextArray[c];
                if (textValue.length() > 0 && textValue.charAt(0) == '=') {
                    ec.setCellValue(column + c, row + r, new Formula(textValue));
                } else {
                    Number number = Utils.string2Number(lineTextArray[c]);
                    if (number != null) {
                        ec.setCellValue(column + c, row + r, number);
                    } else {
                        // alex:����100,000,000������ֵ,����һ��ȡ�ɵĽ������
                        String newStr = Utils.replaceAllString(lineTextArray[c], ",", "");
                        number = Utils.string2Number(newStr);
                        if (number != null) {
                            ec.setCellValue(column + c, row + r, Utils.string2Number(newStr));
                        } else {
                            ec.setCellValue(column + c, row + r, lineTextArray[c]);
                        }
                    }
                }
            }
        }

        ePane.setSelection(new CellSelection(column, row, this.columnSpan, this.rowSpan));

        return true;
    }

    /**
     * �������
     * @param ob Ҫ����Ķ���
     * @param ePane ����
     * @return �ɹ�����true
     */
    public boolean pasteOtherType(Object ob, ElementCasePane ePane) {
        TemplateElementCase ec = ePane.getEditingElementCase();

        TemplateCellElement cellElement = ec.getTemplateCellElement(column, row);
        if (cellElement == null) {
            cellElement = new DefaultTemplateCellElement(column, row, ob);
            ec.addCellElement(cellElement, false);
        } else {
            cellElement.setValue(ob);
        }

        ePane.setSelection(new CellSelection(column, row, 1, 1));

        return true;
    }

    /**
     * �Ƿ��ܺϲ���Ԫ��
     * @param ePane ����
     * @return ���򷵻�true
     */
    public boolean canMergeCells(ElementCasePane ePane) {

        return !this.isSelectedOneCell(ePane);
    }

    /**
     * �ϲ���Ԫ��
     * @param ePane ����
     * @return �ɹ�����true
     */
    public boolean mergeCells(ElementCasePane ePane) {

        TemplateElementCase ec = ePane.getEditingElementCase();
        Iterator cells = ec.intersect(column, row, columnSpan, rowSpan);
        if (cells.hasNext() && cells.hasNext()) { // alex:���������ϵĸ��������������
            int returnValue = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(ePane), Inter.getLocText("Des-Merger_Cell"), Inter.getLocText("Utils-Merge_Cell"),
                    JOptionPane.OK_CANCEL_OPTION);
            if (returnValue != JOptionPane.OK_OPTION) {
                return false;
            }
        }

        ec.merge(row, row + rowSpan - 1, column, column + columnSpan - 1);

        return true;
    }

    /**
     * �Ƿ����ϲ���Ԫ��
     * @param ePane ����
     * @return ���򷵻�true
     */
    public boolean canUnMergeCells(ElementCasePane ePane) {
        TemplateElementCase ec = ePane.getEditingElementCase();

        Iterator containedCellElementIterator = ec.intersect(column, row, columnSpan, rowSpan);
        while (containedCellElementIterator.hasNext()) {
            CellElement cellElement = (CellElement) containedCellElementIterator.next();

            if (cellElement.getColumnSpan() > 1 || cellElement.getRowSpan() > 1) {
                return true;
            }
        }

        return false;
    }

    /**
     * �����ϲ���Ԫ��
     * @param ePane ����
     * @return �ɹ�����true
     */
    public boolean unMergeCells(ElementCasePane ePane) {
        TemplateElementCase ec = ePane.getEditingElementCase();

        Iterator containedCellElementIterator = ec.intersect(column, row, columnSpan, rowSpan);
        while (containedCellElementIterator.hasNext()) {
            TemplateCellElement cellElement = (TemplateCellElement) containedCellElementIterator.next();

            int columnSpan = cellElement.getColumnSpan();
            int rowSpan = cellElement.getRowSpan();
            ec.removeCellElement(cellElement);

            ec.addCellElement((TemplateCellElement) cellElement.deriveCellElement(cellElement.getColumn(), cellElement.getRow(), 1, 1));

            for (int kc = cellElement.getColumn(); kc < cellElement.getColumn() + columnSpan; kc++) {
                for (int kr = cellElement.getRow(); kr < cellElement.getRow() + rowSpan; kr++) {
                    if (kc == cellElement.getColumn() && kr == cellElement.getRow()) {
                        continue;
                    }

                    // ��������ǰ��Ԫ��
                    ec.addCellElement(new DefaultTemplateCellElement(kc, kr), false);
                }
            }
        }

        this.setBounds(column, row, 1, 1);

        return true;
    }

    /**
     * ���������˵�
     * @param ePane ����
     * @return �˵�
     */
    public UIPopupMenu createPopupMenu(ElementCasePane ePane) {
        UIPopupMenu popup = new UIPopupMenu();
        if (BaseUtils.isAuthorityEditing()) {
            popup.add(new CleanAuthorityAction(ePane).createMenuItem());
            return popup;
        }
        popup.add(DeprecatedActionManager.getCellMenu(ePane).createJMenu());
        popup.add(new EditCellAction(ePane).createMenuItem());
        // richer:add global style menu
        if (!ConfigManager.getProviderInstance().hasStyle()) {
            UIMenu styleMenu = new UIMenu(KeySetUtils.GLOBAL_STYLE.getMenuName());
            styleMenu.setIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/cell.png"));
            Iterator iterato = ConfigManager.getProviderInstance().getStyleNameIterator();
            while (iterato.hasNext()) {
                String name = (String) iterato.next();
                name = GlobalStyleMenuDef.judgeChina(name);
                NameStyle nameStyle = NameStyle.getInstance(name);
                UpdateAction.UseMenuItem useMenuItem = new GlobalStyleSelection(ePane, nameStyle).createUseMenuItem();
                useMenuItem.setNameStyle(nameStyle);
                styleMenu.add(useMenuItem);
            }
            styleMenu.addSeparator();
            styleMenu.add(new GlobalStyleMenuDef.CustomStyleAction(Inter.getLocText("FR-Designer_Custom")));
            popup.add(styleMenu);
        } else {
            popup.add(new StyleAction().createMenuItem());
        }
        JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if (jTemplate.isJWorkBook()){ //���б����༭���ε�  �ؼ�����
            popup.add(new CellWidgetAttrAction(ePane).createMenuItem());
        }
        popup.add(new CellExpandAttrAction().createMenuItem());
        popup.add(DeprecatedActionManager.getPresentMenu(ePane).createJMenu());
        popup.add(new ConditionAttributesAction(ePane).createMenuItem());
        popup.add(new CellAttributeAction().createMenuItem());
        popup.add(new HyperlinkAction(ePane).createMenuItem());
        // cut, copy and paste
        popup.addSeparator();
        popup.add(new CutAction(ePane).createMenuItem());
        popup.add(new CopyAction(ePane).createMenuItem());
        popup.add(new PasteAction(ePane).createMenuItem());

        popup.addSeparator();
        popup.add(DeprecatedActionManager.getInsertMenu(ePane));
        popup.add(DeprecatedActionManager.getDeleteMenu(ePane));
        popup.add(DeprecatedActionManager.getClearMenu(ePane));
        return popup;
    }

    /**
     * ���
     * @param type Ҫ���������
     * @param ePane  ����
     * @return �ɹ�����true
     */
    public boolean clear(Clear type, ElementCasePane ePane) {
        TemplateElementCase ec = ePane.getEditingElementCase();
        boolean isClear = true;
        int cellRectangleCount = getCellRectangleCount();
        for (int rect = 0; rect < cellRectangleCount; rect++) {
            isClear = clearCell(type, ec, rect);
        }
        return isClear;
    }

    private boolean clearCell(Clear type, TemplateElementCase ec, int rect) {
        List<CellElement> removeElementList = new ArrayList<CellElement>();
        Rectangle cellRectangle = getCellRectangle(rect);
        column = cellRectangle.x;
        row = cellRectangle.y;
        columnSpan = cellRectangle.width;
        rowSpan = cellRectangle.height;

        Iterator cells = ec.intersect(column, row, columnSpan, rowSpan);
        while (cells.hasNext()) {
            CellElement cellElement = (CellElement) cells.next();
            CellGUIAttr cellGUIAttr = cellElement.getCellGUIAttr();
            if (cellGUIAttr == null) {
                cellGUIAttr = CellGUIAttr.DEFAULT_CELLGUIATTR;
            }
            removeElementList.add(cellElement);
        }
        if (removeElementList.isEmpty()) {
            return false;
        }
        switch (type) {
            case ALL:
                for (int i = 0; i < removeElementList.size(); i++) {
                    CellElement element = removeElementList.get(i);
                    ec.removeCellElement((TemplateCellElement) element);
                }
                break;

            case FORMATS:
                for (int i = 0; i < removeElementList.size(); i++) {
                    CellElement element = removeElementList.get(i);
                    element.setStyle(null);
                }
                break;

            case CONTENTS:
                for (int i = 0; i < removeElementList.size(); i++) {
                    CellElement element = removeElementList.get(i);
                    element.setValue(null);
                }
                break;

            case WIDGETS:
                for (int i = 0; i < removeElementList.size(); i++) {
                    CellElement element = removeElementList.get(i);
                    ((TemplateCellElement) element).setWidget(null);
                }
                break;
        }
        return true;
    }


    @Override
    public int[] getSelectedColumns() {
        return IntList.range(column, column + columnSpan);

    }

    @Override
    public int[] getSelectedRows() {
        return IntList.range(row, row + rowSpan);
    }


    /**
     * �����ƶ�
     * @param ePane ����
     */
    public void moveLeft(ElementCasePane ePane) {
        if (column - 1 < 0) {
            return;
        }
        moveTo(ePane, column - 1, row);
    }

    /**
     * �����ƶ�
     * @param ePane ����
     */
    public void moveRight(ElementCasePane ePane) {
        moveTo(ePane, column + columnSpan, row);
    }

    /**
     * �����ƶ�
     * @param ePane ����
     */
    public void moveUp(ElementCasePane ePane) {
        if (row - 1 < 0) {
            return;
        }
        moveTo(ePane, column, row - 1);
    }

    /**
     * �����ƶ�
     * @param ePane ����
     */
    public void moveDown(ElementCasePane ePane) {
        moveTo(ePane, column, row + rowSpan);
    }

    private static void moveTo(ElementCasePane ePane, int column, int row) {
        if (GridUtils.canMove(ePane, column, row)) {
            GridUtils.doSelectCell(ePane, column, row);
            ePane.ensureColumnRowVisible(column, row);
        }
    }

    /**
     * ����ɾ������
     * @param ePane ����
     * @return �ɹ�����true
     */
    public boolean triggerDeleteAction(ElementCasePane ePane) {
        final TemplateElementCase ec = ePane.getEditingElementCase();
        final RowColumnPane rcPane = new RowColumnPane();
        rcPane.setTitle(Inter.getLocText("FR-Designer_Delete"));
        rcPane.showWindow(SwingUtilities.getWindowAncestor(ePane), new DialogActionAdapter() {

            @Override
            public void doOk() {
                if (rcPane.isEntireRow()) {
                    int[] rows = CellSelection.this.getSelectedRows();
                    for (int i = 0; i < rows.length; i++) {
                        ec.removeRow(rows[i] - i);
                    }
                } else {
                    int[] columns = CellSelection.this.getSelectedColumns();
                    for (int i = 0; i < columns.length; i++) {
                        ec.removeColumn(columns[i] - i);
                    }
                }
            }
        }).setVisible(true);

        return true;
    }

    /**
     * ��������
     * @param cr ����
     * @return ��������true
     */
    public boolean containsColumnRow(ColumnRow cr) {
        return new Rectangle(column, row, columnSpan, rowSpan).contains(cr.column, cr.row);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CellSelection)) {
            return false;
        }
        CellSelection cs = (CellSelection) obj;
        return this.getColumn() == cs.getColumn() && this.getRow() == cs.getRow() && this.getColumnSpan() == cs.getColumnSpan() && this.getRowSpan() == cs.getRowSpan();
    }

    @Override
    public CellSelection clone() {
        CellSelection cs = new CellSelection(column, row, columnSpan, rowSpan);

        if (this.editRectangle != null) {
            cs.editRectangle = (Rectangle) this.editRectangle.clone();
        }
        java.util.List newCellRectList = new java.util.ArrayList(this.cellRectangleList.size());
        cs.cellRectangleList = newCellRectList;
        for (int i = 0, len = this.cellRectangleList.size(); i < len; i++) {
            newCellRectList.add((Rectangle) ((Rectangle) this.cellRectangleList.get(i)).clone());
        }
        cs.selectedType = this.selectedType;

        return cs;
    }

    @Override
    public QuickEditor getQuickEditor(TargetComponent tc) {
        ElementCasePane ePane = (ElementCasePane) tc;
        TemplateElementCase tplEC = ePane.getEditingElementCase();
        TemplateCellElement cellElement = tplEC.getTemplateCellElement(column, row);
        Object value = null;
        boolean b = ePane.isSelectedOneCell();
        if (cellElement != null && b) {
            value = cellElement.getValue();
        }
        value = value == null ? "" : value;
        //֮ǰ�����˸�bigInteger,��kunsnat�ַ������˸�bigDecimal���������͵Ķ���stringEditor��û��Ҫ�Ǹ�����
        QuickEditor editor = ActionUtils.getCellEditor((value instanceof Number) ? (Number.class) : (value.getClass()));
        if (editor == null) {
            return null;
        }
        editor.populate(tc);
        return editor;
    }
}
