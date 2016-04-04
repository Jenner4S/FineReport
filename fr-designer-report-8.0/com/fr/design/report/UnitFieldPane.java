// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.stable.unit.*;
import java.awt.Dimension;
import javax.swing.*;

public class UnitFieldPane extends JPanel
{
    public static class UnitLabel extends UILabel
    {

        private int preferredHeight;

        public void setUnitType(int i)
        {
            if(i == 1)
                setText(Inter.getLocText("Unit_CM"));
            else
            if(i == 2)
                setText(Inter.getLocText("PageSetup-inches"));
            else
                setText(Inter.getLocText("PageSetup-mm"));
            Dimension dimension = new Dimension(getPreferredSize().width, preferredHeight);
            setMinimumSize(dimension);
            setMinimumSize(dimension);
            setSize(dimension);
            setPreferredSize(dimension);
        }

        public UnitLabel(int i, int j)
        {
            preferredHeight = j;
            setUnitType(i);
        }
    }


    private UIBasicSpinner valueSpinner;
    private JFormattedTextField textField;
    private UnitLabel unitLable;
    private int unitType;

    public UnitFieldPane(int i)
    {
        unitType = 0;
        setLayout(FRGUIPaneFactory.createBoxFlowLayout());
        unitType = i;
        valueSpinner = new UIBasicSpinner(new SpinnerNumberModel(0.0D, 0.0D, 1.7976931348623157E+308D, 1.0D));
        textField = ((javax.swing.JSpinner.DefaultEditor)valueSpinner.getEditor()).getTextField();
        textField.setColumns(4);
        add(valueSpinner);
        unitLable = new UnitLabel(i, valueSpinner.getPreferredSize().height);
        add(unitLable);
    }

    public JFormattedTextField getTextField()
    {
        return textField;
    }

    public void setUnitType(int i)
    {
        unitLable.setUnitType(i);
        unitType = i;
    }

    public UNIT getUnitValue()
    {
        if(unitType == 1)
            return new CM(((Number)valueSpinner.getValue()).floatValue());
        if(unitType == 2)
            return new INCH(((Number)valueSpinner.getValue()).floatValue());
        else
            return new MM(((Number)valueSpinner.getValue()).floatValue());
    }

    public void setUnitValue(UNIT unit)
    {
        if(unitType == 1)
            valueSpinner.setValue(new Float(unit.toCMValue4Scale2()));
        else
        if(unitType == 2)
            valueSpinner.setValue(new Float(unit.toINCHValue4Scale3()));
        else
            valueSpinner.setValue(new Float(unit.toMMValue4Scale2()));
    }
}
