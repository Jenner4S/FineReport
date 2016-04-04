// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.axis;

import com.fr.base.Formula;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.chartattr.Axis;
import com.fr.design.chart.ChartSwingUtils;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class MinMaxValuePane extends JPanel
{

    private static final long serialVersionUID = 0x2e893258ae9d2b38L;
    protected UICheckBox maxCheckBox;
    protected UITextField maxValueField;
    protected UICheckBox minCheckBox;
    protected UITextField minValueField;
    protected UICheckBox isCustomMainUnitBox;
    protected UITextField mainUnitField;
    protected UICheckBox isCustomSecUnitBox;
    protected UITextField secUnitField;

    public MinMaxValuePane()
    {
        minCheckBox = new UICheckBox(Inter.getLocText(new String[] {
            "Custom", "Min_Value"
        }));
        minValueField = new UITextField(6);
        maxCheckBox = new UICheckBox(Inter.getLocText(new String[] {
            "Custom", "Max_Value"
        }));
        maxValueField = new UITextField(6);
        isCustomMainUnitBox = new UICheckBox(Inter.getLocText("FR-Chart_MainGraduationUnit"));
        mainUnitField = new UITextField(6);
        isCustomSecUnitBox = new UICheckBox(Inter.getLocText("FR-Chart_SecondGraduationUnit"));
        secUnitField = new UITextField(6);
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d
        };
        Component acomponent[][] = getPanelComponents();
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        setLayout(new BorderLayout());
        add(jpanel, "Center");
        for(int i = 0; i < acomponent.length; i++)
        {
            ((UICheckBox)acomponent[i][0]).addActionListener(new ActionListener() {

                final MinMaxValuePane this$0;

                public void actionPerformed(ActionEvent actionevent)
                {
                    checkBoxUse();
                }

            
            {
                this$0 = MinMaxValuePane.this;
                super();
            }
            }
);
            ChartSwingUtils.addListener((UICheckBox)acomponent[i][0], (UITextField)acomponent[i][1]);
        }

    }

    protected Component[][] getPanelComponents()
    {
        return (new Component[][] {
            new Component[] {
                minCheckBox, minValueField
            }, new Component[] {
                maxCheckBox, maxValueField
            }, new Component[] {
                isCustomMainUnitBox, mainUnitField
            }, new Component[] {
                isCustomSecUnitBox, secUnitField
            }
        });
    }

    private void checkBoxUse()
    {
        minValueField.setEnabled(minCheckBox.isSelected());
        maxValueField.setEnabled(maxCheckBox.isSelected());
        mainUnitField.setEnabled(isCustomMainUnitBox.isSelected());
        secUnitField.setEnabled(isCustomSecUnitBox.isSelected());
    }

    public void setPaneEditable(boolean flag)
    {
        minCheckBox.setEnabled(flag);
        maxCheckBox.setEnabled(flag);
        minValueField.setEnabled(flag);
        maxValueField.setEnabled(flag);
        mainUnitField.setEnabled(flag);
        secUnitField.setEnabled(flag);
        isCustomMainUnitBox.setEnabled(flag);
        isCustomSecUnitBox.setEnabled(flag);
        checkBoxUse();
    }

    public void populate(Axis axis)
    {
        if(axis == null)
            return;
        if(axis.isCustomMinValue())
        {
            minCheckBox.setSelected(true);
            if(axis.getMinValue() != null)
                minValueField.setText(axis.getMinValue().toString());
        }
        if(axis.isCustomMaxValue())
        {
            maxCheckBox.setSelected(true);
            if(axis.getMaxValue() != null)
                maxValueField.setText(axis.getMaxValue().toString());
        }
        if(axis.isCustomMainUnit())
        {
            isCustomMainUnitBox.setSelected(true);
            if(axis.getMainUnit() != null)
                mainUnitField.setText(axis.getMainUnit().toString());
        }
        if(axis.isCustomSecUnit())
        {
            isCustomSecUnitBox.setSelected(true);
            if(axis.getSecUnit() != null)
                secUnitField.setText(axis.getSecUnit().toString());
        }
        checkBoxUse();
    }

    public void update(Axis axis)
    {
        if(axis == null)
            return;
        if(minCheckBox.isSelected())
        {
            axis.setCustomMinValue(StringUtils.isNotEmpty(minValueField.getText()));
            axis.setMinValue(new Formula(minValueField.getText()));
        } else
        {
            axis.setCustomMinValue(false);
        }
        if(maxCheckBox.isSelected())
        {
            axis.setCustomMaxValue(StringUtils.isNotEmpty(maxValueField.getText()));
            axis.setMaxValue(new Formula(maxValueField.getText()));
        } else
        {
            axis.setCustomMaxValue(false);
        }
        updateUnit(axis);
    }

    private void updateUnit(Axis axis)
    {
        if(isCustomMainUnitBox.isSelected())
        {
            String s = mainUnitField.getText();
            if(StringUtils.isEmpty(s))
            {
                axis.setCustomMainUnit(false);
                axis.setMainUnit(null);
            } else
            {
                axis.setCustomMainUnit(true);
                Formula formula = new Formula(s);
                Number number = ChartBaseUtils.formula2Number(formula);
                if(number != null && number.doubleValue() < 0.0D)
                    axis.setMainUnit(new Formula("10"));
                else
                    axis.setMainUnit(formula);
            }
        } else
        {
            axis.setCustomMainUnit(false);
        }
        if(isCustomSecUnitBox.isSelected())
        {
            String s1 = secUnitField.getText();
            if(StringUtils.isEmpty(s1))
            {
                axis.setCustomSecUnit(false);
                axis.setSecUnit(null);
            } else
            {
                axis.setCustomSecUnit(true);
                Formula formula1 = new Formula(s1);
                Number number1 = ChartBaseUtils.formula2Number(formula1);
                if(number1 != null && number1.doubleValue() < 0.0D)
                    axis.setSecUnit(new Formula("10"));
                else
                    axis.setSecUnit(formula1);
            }
        } else
        {
            axis.setCustomSecUnit(false);
        }
    }

}
