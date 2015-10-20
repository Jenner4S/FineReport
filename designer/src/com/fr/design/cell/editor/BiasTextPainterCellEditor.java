/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.cell.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;

import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.Grid;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.painter.BiasTextPainter;
import com.fr.report.elementcase.TemplateElementCase;

/**
 * BiasTextPainter
 */
public class BiasTextPainterCellEditor extends AbstractCellEditor {

    private BiasTextPainterPane biasTextPainterPane = null;

    /**
     * Constructor.
     */
    public BiasTextPainterCellEditor(ElementCasePane<? extends TemplateElementCase> ePane) {
    	super(ePane);
    }

    /**
     * Return the value of the CellEditor.
     */
    @Override
    public Object getCellEditorValue() throws Exception {
        if (this.biasTextPainterPane == null) {
            return null;
        }

        return this.biasTextPainterPane.update();
    }

    /**
     * Sets an initial <code>cellElement</code> for the editor.  This will cause
     * the editor to <code>stopCellEditing</code> and lose any partially
     * edited value if the editor is editing when this method is called. <p>
     * <p/>
     * Returns the component that should be added to the client's
     * <code>Component</code> hierarchy.  Once installed in the client's
     * hierarchy this component will then be able to draw and receive
     * user input.
     *
     * @param grid        the <code>Grid</code> that is asking the
     *                    editor to edit; can be <code>null</code>
     * @param cellElement the value of the cell to be edited; it is
     *                    up to the specific editor to interpret
     *                    and draw the value.
     */
    @Override
    public Component getCellEditorComponent(Grid grid, TemplateCellElement cellElement, int resolution) {
        Object value = null;
        if (cellElement != null) {
            value = cellElement.getValue();
        }
        if (value == null || !(value instanceof BiasTextPainter)) {
            value = new BiasTextPainter("");
        }

        BiasTextPainter biasTextPainter = (BiasTextPainter) value;

        Window parentWindow = SwingUtilities.getWindowAncestor(grid);
        this.biasTextPainterPane = new BiasTextPainterPane();
        this.biasTextPainterPane.populate(biasTextPainter);
        return this.biasTextPainterPane.showSmallWindow(parentWindow, new DialogActionAdapter() {

            @Override
            public void doOk() {
                BiasTextPainterCellEditor.this.fireEditingStopped();
            }

            @Override
            public void doCancel() {
                BiasTextPainterCellEditor.this.fireEditingCanceled();
            }
        });
    }

    public static class BiasTextPainterPane extends BasicPane {
        private UITextArea formulaTextArea;
        private boolean isBackSlash;
        private UIRadioButton choice1;
        private UIRadioButton choice2;
        
        public BiasTextPainterPane() {
        	JPanel defaultPane = this;

            //center
            JPanel centerPane =FRGUIPaneFactory.createBorderLayout_S_Pane();
            defaultPane.add(centerPane, BorderLayout.CENTER);

            //text
            JPanel textPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
            centerPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            centerPane.add(textPane, BorderLayout.CENTER);

            UILabel formulaLabel = new UILabel(Inter.getLocText(new String[] {"BiasD-Input_Slope_Line_text_separated_by_'|'", "Example"}, new String[] {" ",  ": Season|Product"}) );
            textPane.add(formulaLabel, BorderLayout.NORTH);
            formulaLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 0));

            textPane.add(Box.createVerticalStrut(2), BorderLayout.SOUTH);

            formulaTextArea = new UITextArea();
            JScrollPane scrollPane = new JScrollPane(formulaTextArea);
            // ȥ��scollPane�ı߿򣬷����������߿��Գ�
            scrollPane.setBorder(null);
            textPane.add(scrollPane, BorderLayout.CENTER);


            JPanel choicePane = FRGUIPaneFactory.createX_AXISBoxInnerContainer_S_Pane();

            choice1 = new UIRadioButton((Inter.getLocText("BiasD-From-upper_left_to_lower_right")));
            choice2 = new UIRadioButton((Inter.getLocText("BiasD-From-lower_left_to_upper_right")));
            choice1.addActionListener(leftUp);
            choice2.addActionListener(leftDown);

            final ButtonGroup group = new ButtonGroup();
            group.add(choice1);
            group.add(choice2);

            choicePane.add(choice1);
//            choicePane.add(new UILabel("    "));
            choicePane.add(choice2);

            centerPane.add(choicePane, BorderLayout.SOUTH);
        }
        
        @Override
        protected String title4PopupWindow() {
        	return Inter.getLocText("BiasD-Slope_Line");
        }
        
        public void populate(BiasTextPainter biasTextPainter) {
            if (biasTextPainter == null) {
                return;
            }

            this.isBackSlash = biasTextPainter.isBackSlash();
            if (this.isBackSlash) {
                this.choice2.setSelected(true);
            } else {
                this.choice1.setSelected(true);
            }
            this.formulaTextArea.setText(biasTextPainter.getText());
        }

        /**
         * update
         */
        public BiasTextPainter update() {
            BiasTextPainter tmp = new BiasTextPainter(this.formulaTextArea.getText());
            tmp.setIsBackSlash(this.isBackSlash);
            return tmp;
        }
        ActionListener leftUp = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (ComparatorUtils.equals(e.getSource(), BiasTextPainterPane.this.choice1)) {
                	BiasTextPainterPane.this.isBackSlash = false;
                }
            }
        };
        ActionListener leftDown = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (ComparatorUtils.equals(e.getSource(), BiasTextPainterPane.this.choice2)) {
                	BiasTextPainterPane.this.isBackSlash = true;
                }
            }
        };
    }
}
