// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.quickeditor.cellquick;

import com.fr.base.*;
import com.fr.design.designer.TargetComponent;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.Grid;
import com.fr.grid.selection.CellSelection;
import com.fr.quickeditor.CellQuickEditor;
import com.fr.report.ReportHelper;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.ColumnRow;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class CellStringQuickEditor extends CellQuickEditor
{

    private static CellStringQuickEditor THIS;
    private UITextField stringTextField;
    private boolean isEditing;
    private boolean reserveInResult;
    private boolean reserveOnWriteOrAnaly;
    DocumentListener documentListener;

    public static final CellStringQuickEditor getInstance()
    {
        if(THIS == null)
            THIS = new CellStringQuickEditor();
        return THIS;
    }

    private CellStringQuickEditor()
    {
        isEditing = false;
        reserveInResult = false;
        reserveOnWriteOrAnaly = true;
        documentListener = new DocumentListener() {

            final CellStringQuickEditor this$0;

            public void insertUpdate(DocumentEvent documentevent)
            {
                changeReportPaneCell(stringTextField.getText().trim());
            }

            public void removeUpdate(DocumentEvent documentevent)
            {
                changeReportPaneCell(stringTextField.getText().trim());
            }

            public void changedUpdate(DocumentEvent documentevent)
            {
                changeReportPaneCell(stringTextField.getText().trim());
            }

            
            {
                this$0 = CellStringQuickEditor.this;
                super();
            }
        }
;
    }

    public JComponent createCenterBody()
    {
        stringTextField = new UITextField();
        stringTextField.addKeyListener(new KeyAdapter() {

            final CellStringQuickEditor this$0;

            public void keyReleased(KeyEvent keyevent)
            {
                if(CellStringQuickEditor.this.KeyAdapter != null)
                    ((ElementCasePane)CellStringQuickEditor.this.KeyAdapter).getGrid().dispatchEvent(keyevent);
            }

            
            {
                this$0 = CellStringQuickEditor.this;
                super();
            }
        }
);
        return stringTextField;
    }

    protected void changeReportPaneCell(String s)
    {
        isEditing = true;
        CellSelection cellselection = (CellSelection)((ElementCasePane)tc).getSelection();
        ColumnRow columnrow = ColumnRow.valueOf(cellselection.getColumn(), cellselection.getRow());
        columnRowTextField.setText(columnrow.toString());
        cellElement = ((ElementCasePane)tc).getEditingElementCase().getTemplateCellElement(cellselection.getColumn(), cellselection.getRow());
        if(cellElement == null)
        {
            CellSelection cellselection1 = (CellSelection)((ElementCasePane)tc).getSelection();
            cellElement = new DefaultTemplateCellElement(cellselection1.getColumn(), cellselection1.getRow());
            ((ElementCasePane)tc).getEditingElementCase().addCellElement(cellElement, false);
        }
        if(s != null && s.length() > 0 && s.charAt(0) == '=')
        {
            Formula formula = new Formula(s);
            formula.setReserveInResult(reserveInResult);
            formula.setReserveOnWriteOrAnaly(reserveOnWriteOrAnaly);
            cellElement.setValue(formula);
        } else
        {
            Style style = cellElement.getStyle();
            if(cellElement != null && style != null && style.getFormat() != null && style.getFormat() == TextFormat.getInstance())
                cellElement.setValue(s);
            else
                cellElement.setValue(ReportHelper.convertGeneralStringAccordingToExcel(s));
        }
        fireTargetModified();
        stringTextField.requestFocus();
        isEditing = false;
    }

    protected void refreshDetails()
    {
        String s = null;
        if(cellElement == null)
        {
            s = "";
        } else
        {
            Object obj = cellElement.getValue();
            if(obj == null)
                s = "";
            else
            if(obj instanceof Formula)
            {
                Formula formula = (Formula)obj;
                s = formula.getContent();
                reserveInResult = formula.isReserveInResult();
                reserveOnWriteOrAnaly = formula.isReserveOnWriteOrAnaly();
            } else
            {
                s = obj.toString();
            }
        }
        showText(s);
        stringTextField.setEditable(((ElementCasePane)tc).isSelectedOneCell());
    }

    public void showText(String s)
    {
        if(isEditing)
        {
            return;
        } else
        {
            stringTextField.getDocument().removeDocumentListener(documentListener);
            stringTextField.setText(s);
            stringTextField.getDocument().addDocumentListener(documentListener);
            return;
        }
    }



}
