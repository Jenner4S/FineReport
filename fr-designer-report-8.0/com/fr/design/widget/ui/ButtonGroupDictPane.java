// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.ButtonGroup;
import com.fr.general.Inter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;

public class ButtonGroupDictPane extends JPanel
{

    private UIBasicSpinner columnSpinner;
    private UICheckBox adaptiveCheckbox;
    private UILabel columnLabel;

    public ButtonGroupDictPane()
    {
        initComponents();
    }

    public void initComponents()
    {
        setLayout(FRGUIPaneFactory.createLabelFlowLayout());
        adaptiveCheckbox = new UICheckBox(Inter.getLocText("Adaptive"), true);
        adaptiveCheckbox.addActionListener(new ActionListener() {

            final ButtonGroupDictPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                columnSpinner.setVisible(!adaptiveCheckbox.isSelected());
                columnLabel.setVisible(!adaptiveCheckbox.isSelected());
            }

            
            {
                this$0 = ButtonGroupDictPane.this;
                super();
            }
        }
);
        add(adaptiveCheckbox);
        columnLabel = new UILabel((new StringBuilder()).append(Inter.getLocText("Button-Group-Display-Columns")).append(":").toString());
        add(columnLabel);
        columnSpinner = new UIBasicSpinner(new SpinnerNumberModel(0, 0, 0x7fffffff, 1));
        add(columnSpinner);
    }

    public void populate(ButtonGroup buttongroup)
    {
        adaptiveCheckbox.setSelected(buttongroup.isAdaptive());
        columnSpinner.setVisible(!adaptiveCheckbox.isSelected());
        columnLabel.setVisible(!adaptiveCheckbox.isSelected());
        columnSpinner.setValue(Integer.valueOf(buttongroup.getColumnsInRow()));
    }

    public void update(ButtonGroup buttongroup)
    {
        buttongroup.setAdaptive(adaptiveCheckbox.isSelected());
        buttongroup.setColumnsInRow(((Integer)(Integer)columnSpinner.getValue()).intValue());
    }



}
