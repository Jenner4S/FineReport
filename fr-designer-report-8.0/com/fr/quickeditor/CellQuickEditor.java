// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.quickeditor;

import com.fr.base.BaseUtils;
import com.fr.design.actions.utils.DeprecatedActionManager;
import com.fr.design.designer.TargetComponent;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuDef;
import com.fr.design.selection.QuickEditor;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.ColumnRow;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.*;
import javax.swing.*;

public abstract class CellQuickEditor extends QuickEditor
{

    protected UITextField columnRowTextField;
    protected UIButton cellElementEditButton;
    protected TemplateCellElement cellElement;

    public CellQuickEditor()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d
        };
        Component acomponent[][] = {
            {
                new UILabel((new StringBuilder()).append("  ").append(Inter.getLocText("Cell")).toString()), columnRowTextField = initColumnRowTextField()
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("HF-Insert_Content")).append(" ").toString()), cellElementEditButton = initCellElementEditButton()
            }, {
                createCenterBody(), null
            }
        };
        javax.swing.JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(jpanel, "Center");
    }

    protected UIButton initCellElementEditButton()
    {
        final UIButton cellElementEditButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png"));
        cellElementEditButton.addMouseListener(new MouseAdapter() {

            final UIButton val$cellElementEditButton;
            final CellQuickEditor this$0;

            public void mousePressed(MouseEvent mouseevent)
            {
                GUICoreUtils.showPopMenuWithParentWidth(DeprecatedActionManager.getCellMenu((ElementCasePane)CellQuickEditor.this.this$0).createJPopupMenu(), cellElementEditButton, 0, cellElementEditButton.getY() - 6);
            }

            
            {
                this$0 = CellQuickEditor.this;
                cellElementEditButton = uibutton;
                super();
            }
        }
);
        return cellElementEditButton;
    }

    protected UITextField initColumnRowTextField()
    {
        final UITextField columnRowTextField = new UITextField(4);
        columnRowTextField.addActionListener(new ActionListener() {

            final UITextField val$columnRowTextField;
            final CellQuickEditor this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                ColumnRow columnrow = ColumnRow.valueOf(columnRowTextField.getText());
                if(!ColumnRow.validate(columnrow))
                {
                    Object aobj[] = {
                        Inter.getLocText("OK")
                    };
                    JOptionPane.showOptionDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("Please_Input_Letters+Numbers(A1,AA1,A11....)"), Inter.getLocText("Warning"), -1, 2, null, aobj, aobj[0]);
                    ((ElementCasePane)CellQuickEditor.this.this$0).setSelection(((ElementCasePane)CellQuickEditor.this.this$0).getSelection());
                    return;
                } else
                {
                    JScrollBar jscrollbar = ((ElementCasePane)CellQuickEditor.this.this$0).getVerticalScrollBar();
                    JScrollBar jscrollbar1 = ((ElementCasePane)CellQuickEditor.this.this$0).getHorizontalScrollBar();
                    int i = columnrow.getColumn();
                    int j = columnrow.getRow();
                    jscrollbar.setMaximum(j);
                    jscrollbar.setValue(j >= 21 ? j - 20 : jscrollbar.getValue());
                    jscrollbar1.setMaximum(i);
                    jscrollbar1.setValue(i >= 13 ? i - 12 : jscrollbar1.getValue());
                    ((ElementCasePane)CellQuickEditor.this.this$0).setSelection(new CellSelection(i, j, 1, 1));
                    ((ElementCasePane)CellQuickEditor.this.this$0).requestFocus();
                    return;
                }
            }

            
            {
                this$0 = CellQuickEditor.this;
                columnRowTextField = uitextfield;
                super();
            }
        }
);
        return columnRowTextField;
    }

    public abstract JComponent createCenterBody();

    protected void refresh()
    {
        CellSelection cellselection = (CellSelection)((ElementCasePane)tc).getSelection();
        ColumnRow columnrow = ColumnRow.valueOf(cellselection.getColumn(), cellselection.getRow());
        columnRowTextField.setText(columnrow.toString());
        cellElement = ((ElementCasePane)tc).getEditingElementCase().getTemplateCellElement(cellselection.getColumn(), cellselection.getRow());
        refreshDetails();
    }

    protected abstract void refreshDetails();







}
