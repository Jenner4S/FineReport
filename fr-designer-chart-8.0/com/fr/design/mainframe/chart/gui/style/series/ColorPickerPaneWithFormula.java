// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.itextfield.UITextField;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            UIColorPickerPane

public class ColorPickerPaneWithFormula extends UIColorPickerPane
{

    public ColorPickerPaneWithFormula()
    {
    }

    public ColorPickerPaneWithFormula(String s)
    {
        super(s);
    }

    protected ArrayList getTextFieldList()
    {
        return new ArrayList();
    }

    protected void setTextValue4Index(int i, String s)
    {
        ((TinyFormulaPane)textFieldList.get(i)).getUITextField().setText(s);
    }

    protected JComponent getNewTextFieldComponent(int i, String s)
    {
        TinyFormulaPane tinyformulapane = new TinyFormulaPane();
        tinyformulapane.setBounds(0, i * 2 * 20, 120, 20);
        tinyformulapane.getUITextField().setText(s);
        return tinyformulapane;
    }

    protected String getValue4Index(int i)
    {
        return ((TinyFormulaPane)textFieldList.get(i)).getUITextField().getText();
    }

    protected void setBackgroundUIColor(int i, Color color)
    {
        ((TinyFormulaPane)textFieldList.get(i)).getUITextField().setBackgroundUIColor(color);
    }
}
