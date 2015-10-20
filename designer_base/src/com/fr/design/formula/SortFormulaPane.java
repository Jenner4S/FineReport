package com.fr.design.formula;


import com.fr.base.Formula;
import com.fr.data.util.SortOrder;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.SortOrderComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class SortFormulaPane extends JPanel {
	protected static final String InsetText = " ";
	
	protected SortOrderComboBox sortOrderComboBox;
	protected UITextField sortFormulaTextField;
    // ���ε����Զ���ȽϹ��򡱺͡�ѡ�񡱰�ť��ֻ��ʾ��ʽ�����ı��͹�ʽ��ť
	protected UIButton sortFormulaTextFieldButton;

    public SortFormulaPane() {
    	this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        sortOrderComboBox = new SortOrderComboBox();
        sortOrderComboBox.setSortOrder(new SortOrder(SortOrder.ORIGINAL));
        sortOrderComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                checkSortButtonEnabled();
            }
        });

        sortFormulaTextField = new UITextField(16);
        //Lance:���һ��ʽ�༭����ť
        sortFormulaTextFieldButton = new UIButton("...");
        sortFormulaTextFieldButton.setToolTipText(Inter.getLocText("Formula") + "...");
        sortFormulaTextFieldButton.setPreferredSize(new Dimension(25, sortFormulaTextFieldButton.getPreferredSize().height));
        sortFormulaTextFieldButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                formulaAction();
            }
        });

        this.add(GUICoreUtils.createFlowPane(new JComponent[]{new UILabel(InsetText), //new UILabel(Inter.getLocText("Select_sort_order") + ":"),
                sortOrderComboBox, new UILabel(InsetText),
                new UILabel(Inter.getLocText("Formula") + ":="), sortFormulaTextField, //selectButton,
                sortFormulaTextFieldButton}, FlowLayout.LEFT));
    }
    
    public abstract void formulaAction();
    
    public void showFormulaDialog(String[] displayNames) {
    	String text = sortFormulaTextField.getText();
    	final UIFormula formulaPane = FormulaFactory.createFormulaPaneWhenReserveFormula();

        formulaPane.populate(new Formula(text), new CustomVariableResolver(displayNames, true));
        formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(SortFormulaPane.this),
            new DialogActionAdapter() {
                public void doOk() {
                    Formula fm = formulaPane.update();
                    if (fm.getContent().length() <= 1) {
                        sortFormulaTextField.setText("");
                    } else {
                        sortFormulaTextField.setText(fm.getContent().substring(1));
                    }
                }
            }).setVisible(true);
    }
    
    private void checkSortButtonEnabled() {
        if (this.sortOrderComboBox.getSortOrder().getOrder() == SortOrder.ORIGINAL) {
            sortFormulaTextField.setEnabled(false);
            sortFormulaTextFieldButton.setEnabled(false);
        } else {
            sortFormulaTextField.setEnabled(true);
            sortFormulaTextFieldButton.setEnabled(true);
        }
    }
}
