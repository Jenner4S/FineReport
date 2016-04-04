// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.design.gui.icombobox.*;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.CheckBoxGroup;
import com.fr.form.ui.ComboCheckBox;
import com.fr.general.Inter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class CheckBoxDictPane extends JPanel
{

    private DictionaryComboBox delimiterComboBox;
    private UIComboBox returnTypeComboBox;
    private DictionaryComboBox startComboBox;
    private DictionaryComboBox endComboBox;
    private JPanel delimiterPane;
    private JPanel startPane;
    private JPanel endPane;

    public CheckBoxDictPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText("Widget-Date_Selector_Return_Type")).append(":").toString()), "West");
        returnTypeComboBox = new UIComboBox(new String[] {
            Inter.getLocText("Widget-Array"), Inter.getLocText("String")
        });
        jpanel.add(returnTypeComboBox, "Center");
        add(jpanel);
        delimiterPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText("Form-Delimiter")).append(":").toString());
        delimiterPane.add(uilabel, "West");
        delimiterPane.add(delimiterComboBox = new DictionaryComboBox(DictionaryConstants.delimiters, DictionaryConstants.delimiterDisplays), "Center");
        delimiterComboBox.setEditable(true);
        add(delimiterPane);
        startPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        startPane.add(new UILabel((new StringBuilder()).append(Inter.getLocText("ComboCheckBox-Start_Symbol")).append(":").toString()), "West");
        startPane.add(startComboBox = new DictionaryComboBox(DictionaryConstants.symbols, DictionaryConstants.symbolDisplays), "Center");
        startComboBox.setEditable(true);
        add(startPane);
        endPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        endPane.add(new UILabel((new StringBuilder()).append(Inter.getLocText("ComboCheckBox-End_Symbol")).append(":").toString()), "West");
        endPane.add(endComboBox = new DictionaryComboBox(DictionaryConstants.symbols, DictionaryConstants.symbolDisplays), "Center");
        endComboBox.setEditable(true);
        add(endPane);
        returnTypeComboBox.addActionListener(new ActionListener() {

            final CheckBoxDictPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                checkVisible();
            }

            
            {
                this$0 = CheckBoxDictPane.this;
                super();
            }
        }
);
    }

    private void checkVisible()
    {
        delimiterPane.setVisible(returnTypeComboBox.getSelectedIndex() == 1);
        startPane.setVisible(returnTypeComboBox.getSelectedIndex() == 1);
        endPane.setVisible(returnTypeComboBox.getSelectedIndex() == 1);
    }

    public void populate(ComboCheckBox combocheckbox)
    {
        delimiterComboBox.setSelectedItem(combocheckbox.getDelimiter());
        returnTypeComboBox.setSelectedIndex(combocheckbox.isReturnString() ? 1 : 0);
        startComboBox.setSelectedItem(combocheckbox.getStartSymbol());
        endComboBox.setSelectedItem(combocheckbox.getEndSymbol());
        checkVisible();
    }

    public void update(ComboCheckBox combocheckbox)
    {
        combocheckbox.setDelimiter((String)delimiterComboBox.getSelectedItem());
        combocheckbox.setReturnString(returnTypeComboBox.getSelectedIndex() != 0);
        combocheckbox.setStartSymbol((String)startComboBox.getSelectedItem());
        combocheckbox.setEndSymbol((String)endComboBox.getSelectedItem());
    }

    public void populate(CheckBoxGroup checkboxgroup)
    {
        delimiterComboBox.setSelectedItem(checkboxgroup.getDelimiter());
        returnTypeComboBox.setSelectedIndex(checkboxgroup.isReturnString() ? 1 : 0);
        startComboBox.setSelectedItem(checkboxgroup.getStartSymbol());
        endComboBox.setSelectedItem(checkboxgroup.getEndSymbol());
        checkVisible();
    }

    public void update(CheckBoxGroup checkboxgroup)
    {
        checkboxgroup.setDelimiter((String)delimiterComboBox.getSelectedItem());
        checkboxgroup.setReturnString(returnTypeComboBox.getSelectedIndex() != 0);
        checkboxgroup.setStartSymbol((String)startComboBox.getSelectedItem());
        checkboxgroup.setEndSymbol((String)endComboBox.getSelectedItem());
    }

}
