// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.dscolumn;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icombobox.FunctionComboBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.core.group.*;
import java.awt.event.*;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.dscolumn:
//            ResultSetGroupPane

public class ResultSetGroupPopUpPane extends ResultSetGroupPane
{

    private UIRadioButton groupRadioButton;
    private UIButton advancedButton;
    private UIRadioButton listRadioButton;
    private UIRadioButton summaryRadioButton;
    private FunctionComboBox functionComboBox;
    private String InsertText;
    ActionListener checkEnabledActionListener = new ActionListener() {

        final ResultSetGroupPopUpPane this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            checkButtonEnabled();
        }

            
            {
                this$0 = ResultSetGroupPopUpPane.this;
                super();
            }
    }
;

    public ResultSetGroupPopUpPane()
    {
        this(true);
    }

    public ResultSetGroupPopUpPane(boolean flag)
    {
        InsertText = " ";
        initComponents(flag);
    }

    public void initComponents(boolean flag)
    {
        setLayout(FRGUIPaneFactory.create1ColumnGridLayout());
        groupRadioButton = new UIRadioButton(Inter.getLocText("BindColumn-Group(Merger_the_Items_Which_Have_The_Same_Value_in_Column)"));
        groupRadioButton.addActionListener(checkEnabledActionListener);
        groupComboBox.addItemListener(new ItemListener() {

            final ResultSetGroupPopUpPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                checkButtonEnabled();
            }

            
            {
                this$0 = ResultSetGroupPopUpPane.this;
                super();
            }
        }
);
        advancedButton = new UIButton(Inter.getLocText("Custom"));
        advancedButton.addActionListener(groupAdvancedListener);
        add(GUICoreUtils.createFlowPane(new JComponent[] {
            new UILabel(InsertText), groupRadioButton, groupComboBox, advancedButton
        }, 0));
        listRadioButton = new UIRadioButton(Inter.getLocText("BindColumn-Select(Regardless_of_Having_the_Same_Value,Display_all_Item_in_Column)"));
        listRadioButton.addActionListener(checkEnabledActionListener);
        add(GUICoreUtils.createFlowPane(new JComponent[] {
            new UILabel(InsertText), listRadioButton
        }, 0));
        summaryRadioButton = new UIRadioButton(Inter.getLocText("BindColumn-Summary(Including_SUM_,_AVERAGE_,_MAX_,_MIN_And_So_On)"), true);
        summaryRadioButton.addActionListener(checkEnabledActionListener);
        functionComboBox = new FunctionComboBox(GUICoreUtils.getFunctionArray());
        add(GUICoreUtils.createFlowPane(new JComponent[] {
            new UILabel(InsertText), summaryRadioButton, functionComboBox
        }, 0));
        ButtonGroup buttongroup = new ButtonGroup();
        buttongroup.add(groupRadioButton);
        if(flag)
            groupRadioButton.setSelected(true);
        else
            listRadioButton.setSelected(true);
        buttongroup.add(listRadioButton);
        buttongroup.add(summaryRadioButton);
        checkButtonEnabled();
    }

    public void populate(TemplateCellElement templatecellelement)
    {
        cellElement = templatecellelement;
        if(isNPE(templatecellelement))
            return;
        DSColumn dscolumn = (DSColumn)templatecellelement.getValue();
        recordGrouper = dscolumn.getGrouper();
        if((recordGrouper instanceof FunctionGrouper) && !((FunctionGrouper)recordGrouper).isCustom())
        {
            int i = ((FunctionGrouper)recordGrouper).getDivideMode();
            if(i == 0)
            {
                groupRadioButton.setSelected(true);
                groupComboBox.setSelectedIndex(0);
            } else
            if(i == 2)
            {
                groupRadioButton.setSelected(true);
                groupComboBox.setSelectedIndex(1);
            } else
            if(i == 1)
                listRadioButton.setSelected(true);
        } else
        if((recordGrouper instanceof FunctionGrouper) && ((FunctionGrouper)recordGrouper).isCustom())
        {
            groupRadioButton.setSelected(true);
            groupComboBox.setSelectedIndex(2);
        } else
        if(recordGrouper instanceof SummaryGrouper)
        {
            summaryRadioButton.setSelected(true);
            functionComboBox.setFunction(((SummaryGrouper)recordGrouper).getFunction());
        } else
        if(recordGrouper instanceof CustomGrouper)
        {
            groupRadioButton.setSelected(true);
            groupComboBox.setSelectedIndex(2);
        }
        checkButtonEnabled();
    }

    public void update()
    {
        if(isNPE(cellElement))
            return;
        DSColumn dscolumn = (DSColumn)cellElement.getValue();
        if(groupRadioButton.isSelected())
            recordGrouper = updateGroupCombox();
        else
        if(listRadioButton.isSelected())
        {
            FunctionGrouper functiongrouper = new FunctionGrouper();
            functiongrouper.setDivideMode(1);
            functiongrouper.setCustom(false);
            recordGrouper = functiongrouper;
        } else
        if(summaryRadioButton.isSelected())
        {
            SummaryGrouper summarygrouper = new SummaryGrouper();
            summarygrouper.setFunction(functionComboBox.getFunction());
            recordGrouper = summarygrouper;
        }
        dscolumn.setGrouper(recordGrouper);
    }

    private void checkButtonEnabled()
    {
        advancedButton.setEnabled(false);
        functionComboBox.setEnabled(false);
        groupComboBox.setEnabled(false);
        if(summaryRadioButton.isSelected())
            functionComboBox.setEnabled(true);
        if(groupRadioButton.isSelected())
        {
            groupComboBox.setEnabled(true);
            if(groupComboBox.getSelectedIndex() == 2)
                advancedButton.setEnabled(true);
        }
    }

    public boolean isSummaryRadioButtonSelected()
    {
        return summaryRadioButton.isSelected();
    }

    public void addListeners(ActionListener actionlistener, ActionListener actionlistener1, ActionListener actionlistener2)
    {
        summaryRadioButton.addActionListener(actionlistener);
        groupRadioButton.addActionListener(actionlistener1);
        listRadioButton.addActionListener(actionlistener1);
        advancedButton.addActionListener(actionlistener2);
    }

    public void setRecordGrouper(RecordGrouper recordgrouper)
    {
        recordGrouper = recordgrouper;
    }

}
