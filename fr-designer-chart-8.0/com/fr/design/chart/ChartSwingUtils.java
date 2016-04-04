// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart;

import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import java.awt.event.*;
import javax.swing.SwingUtilities;

public class ChartSwingUtils
{
    public static interface OKListener
    {

        public abstract void action();
    }


    public ChartSwingUtils()
    {
    }

    public static void addListener(UICheckBox uicheckbox, UITextField uitextfield)
    {
        uitextfield.addMouseListener(new MouseAdapter(uicheckbox, uitextfield) {

            final UICheckBox val$box;
            final UITextField val$textField;

            public void mousePressed(MouseEvent mouseevent)
            {
                if(box.isSelected())
                    ChartSwingUtils.showFormulaPane(textField, null);
            }

            
            {
                box = uicheckbox;
                textField = uitextfield;
                super();
            }
        }
);
        uitextfield.addKeyListener(new KeyAdapter(uicheckbox, uitextfield) {

            final UICheckBox val$box;
            final UITextField val$textField;

            public void keyTyped(KeyEvent keyevent)
            {
                if(box.isSelected())
                {
                    keyevent.consume();
                    ChartSwingUtils.showFormulaPane(textField, null);
                }
            }

            
            {
                box = uicheckbox;
                textField = uitextfield;
                super();
            }
        }
);
    }

    public static UITextField createFormulaUITextField(OKListener oklistener)
    {
        UITextField uitextfield = new UITextField();
        uitextfield.addMouseListener(new MouseAdapter(uitextfield, oklistener) {

            final UITextField val$textField;
            final OKListener val$l;

            public void mousePressed(MouseEvent mouseevent)
            {
                ChartSwingUtils.showFormulaPane(textField, l);
            }

            
            {
                textField = uitextfield;
                l = oklistener;
                super();
            }
        }
);
        uitextfield.addKeyListener(new KeyAdapter(uitextfield, oklistener) {

            final UITextField val$textField;
            final OKListener val$l;

            public void keyTyped(KeyEvent keyevent)
            {
                keyevent.consume();
                ChartSwingUtils.showFormulaPane(textField, l);
            }

            
            {
                textField = uitextfield;
                l = oklistener;
                super();
            }
        }
);
        return uitextfield;
    }

    private static void showFormulaPane(UITextField uitextfield, OKListener oklistener)
    {
        UIFormula uiformula = FormulaFactory.createFormulaPane();
        uiformula.populate(new Formula(uitextfield.getText()));
        uiformula.showLargeWindow(SwingUtilities.getWindowAncestor(FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane()), new DialogActionAdapter(uiformula, uitextfield, oklistener) {

            final UIFormula val$formulaPane;
            final UITextField val$jTextField;
            final OKListener val$l;

            public void doOk()
            {
                Formula formula = formulaPane.update();
                jTextField.setText(Utils.objectToString(formula));
                if(l != null)
                    l.action();
            }

            
            {
                formulaPane = uiformula;
                jTextField = uitextfield;
                l = oklistener;
                super();
            }
        }
).setVisible(true);
    }

}
