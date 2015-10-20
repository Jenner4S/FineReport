/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.editor.editor;

import com.fr.base.Formula;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * CellEditor used to edit Formula object.
 *
 * @editor zhou
 * @since 2012-3-29����6:27:27
 */
public class FormulaEditor extends Editor<Formula> {
    private Formula formula = new Formula(StringUtils.EMPTY);
    private UITextField currentTextField;
    private ShowPaneListener listerner = new ShowPaneListener();

    /**
     * Constructor.
     */
    public FormulaEditor() {
        this("");
    }

    public FormulaEditor(String name) {
        this(name, null);
    }

    public FormulaEditor(String name, Formula formula) {
        if (formula != null) {
            this.formula = formula;
        }
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        JPanel editPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        currentTextField = new UITextField(28);
        currentTextField.setText(this.formula.getContent());

        editPane.add(currentTextField, BorderLayout.CENTER);
        currentTextField.setEditable(false);
        currentTextField.addMouseListener(listerner);
        this.add(editPane, BorderLayout.CENTER);
        this.setName(name);
    }

    private class ShowPaneListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if (currentTextField.isEnabled()) {
                showFormulaPane();
            }
        }
    }

    public void setColumns(int i) {
        this.currentTextField.setColumns(i);
    }


    /**
     * ѡ��ʱ������ʽ�༭��
     */
    public void selected() {
        showFormulaPane();
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        currentTextField.setEnabled(enabled);
    }


    protected void showFormulaPane() {
        final UIFormula formulaPane = FormulaFactory.createFormulaPane();
        formulaPane.populate(formula);
        formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(FormulaEditor.this), new DialogActionAdapter() {

            @Override
            public void doOk() {
                formula = formulaPane.update();
                setValue(formula);
                fireStateChanged();
            }
        }).setVisible(true);
    }

    /**
     * Return the value of the CellEditor.
     */
    @Override
    public Formula getValue() {
        if (formula != null && "=".equals(formula.getContent())) {
            return null;
        }
        return formula;
    }

    /**
     * Set the value to the CellEditor.
     */
    @Override
    public void setValue(Formula value) {
        if (value == null) {
            value = new Formula(StringUtils.EMPTY);
        }
        this.formula = value;
        currentTextField.setText(value.toString());
    }

    /**
     * �����ı�����
     *
     * @param l ������
     */
    public void addDocumentListener(DocumentListener l) {
        currentTextField.getDocument().addDocumentListener(l);
    }

    public String getIconName() {
        return "type_formula";
    }

    /**
     * object�Ƿ��ǹ�˾���Ͷ���
     *
     * @param object ���жϵĶ���
     * @return �ǹ�ʽ�����򷵻�true
     */
    public boolean accept(Object object) {
        return object instanceof Formula;
    }

    /**
     * ����
     */
    public void reset() {
        currentTextField.setText("=");
        formula = new Formula(StringUtils.EMPTY);
    }

    /**
     * �������
     */
    public void clearData() {
        reset();
    }

    /**
     * �Ƿ����
     *
     * @param flag Ϊtrue�������
     */
    public void enableEditor(boolean flag) {
        this.setEnabled(flag);
        this.currentTextField.setEnabled(flag);
        if (flag == false) {
            this.currentTextField.removeMouseListener(listerner);
        } else {
            int listenerSize = this.currentTextField.getMouseListeners().length;
            for (int i = 0; i < listenerSize; i++) {
                this.currentTextField.removeMouseListener(listerner);
            }
            this.currentTextField.addMouseListener(listerner);
        }
    }
}
