// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report;

import com.fr.base.BaseUtils;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.border.UITitledBorder;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.*;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.report.stable.WorkSheetAttr;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ColumnRow;
import com.fr.stable.StringUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ReportColumnsPane extends BasicPane
{

    public static final int ROW = 0;
    public static final int COLUMN = 1;
    private int rowOrColumn;
    private boolean isRepeate;
    private static final String COLUMN_ROW_TEXTS[] = {
        Inter.getLocText("FR-Base_Rows"), Inter.getLocText("FR-Base_Columns")
    };
    private static final String SHOW_BLANK[] = {
        Inter.getLocText("FR-Designer_Show_Blank_Row"), Inter.getLocText("FR-Designer_Show_Blank_Column")
    };
    private static final String REPORT_COLUMN_RAPEAT[] = {
        Inter.getLocText("FR-Designer_ReportColumns-Repeat_Row"), Inter.getLocText("FR-Designer_ReportColumns-Repeat_Column")
    };
    private static final String FONT_NAME = "simsun";
    private static final int FONT_SIZE = 14;
    private UIButtonGroup onOffButtonGroup;
    private UIRadioButton rowButton;
    private UIRadioButton colButton;
    private UIRadioButton maxRadioButton;
    private UIBasicSpinner maxNumberSpinner;
    private UILabel maxUILabel;
    private UIRadioButton toXRadioButton;
    private UIBasicSpinner toXSpinner;
    private UILabel toUILabel;
    private UITextField repeatColDataTextField;
    private UILabel copyLabel;
    private UITextField copyTitleTextField;
    private UICheckBox showBlankCheckBox;
    private ActionListener onOffListener;
    private ActionListener rowChangeListener;
    private ActionListener colChangeListener;
    private ActionListener toXBtnListener;
    private ActionListener maxBtnListener;

    public ReportColumnsPane()
    {
        onOffListener = new ActionListener() {

            final ReportColumnsPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                isRepeate = onOffButtonGroup.getSelectedIndex() == 0;
                checkEnable();
            }

            
            {
                this$0 = ReportColumnsPane.this;
                super();
            }
        }
;
        rowChangeListener = new ActionListener() {

            final ReportColumnsPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(!rowButton.isSelected())
                {
                    rowButton.setSelected(true);
                } else
                {
                    colButton.setSelected(false);
                    rowOrColumn = 0;
                    emptyValueConvert();
                }
            }

            
            {
                this$0 = ReportColumnsPane.this;
                super();
            }
        }
;
        colChangeListener = new ActionListener() {

            final ReportColumnsPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(!colButton.isSelected())
                {
                    colButton.setSelected(true);
                } else
                {
                    rowButton.setSelected(false);
                    rowOrColumn = 1;
                    emptyValueConvert();
                }
            }

            
            {
                this$0 = ReportColumnsPane.this;
                super();
            }
        }
;
        toXBtnListener = new ActionListener() {

            final ReportColumnsPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(toXRadioButton.isSelected())
                {
                    toXSpinner.setEnabled(true);
                    maxNumberSpinner.setEnabled(false);
                    maxRadioButton.setSelected(false);
                } else
                {
                    toXRadioButton.setSelected(true);
                }
            }

            
            {
                this$0 = ReportColumnsPane.this;
                super();
            }
        }
;
        maxBtnListener = new ActionListener() {

            final ReportColumnsPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(maxRadioButton.isSelected())
                {
                    maxNumberSpinner.setEnabled(true);
                    toXRadioButton.setSelected(false);
                    toXSpinner.setEnabled(false);
                } else
                {
                    maxRadioButton.setSelected(true);
                }
            }

            
            {
                this$0 = ReportColumnsPane.this;
                super();
            }
        }
;
        rowOrColumn = 0;
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = new JPanel(new BorderLayout()) {

            final ReportColumnsPane this$0;

            public void paint(Graphics g)
            {
                super.paint(g);
                super.paintBorder(g);
            }

            
            {
                this$0 = ReportColumnsPane.this;
                super(layoutmanager);
            }
        }
;
        jpanel.setPreferredSize(new Dimension(549, 59));
        jpanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(11, 23, 6, 23), new UIRoundedBorder(new Color(204, 204, 204), 1, 10)));
        String as[] = {
            Inter.getLocText("FR-Base_TurnOn"), Inter.getLocText("FR-Base_TurnOff")
        };
        onOffButtonGroup = new UIButtonGroup(as) {

            final ReportColumnsPane this$0;

            protected void initButton(UIToggleButton uitogglebutton)
            {
                uitogglebutton.setSize(new Dimension(60, 20));
                uitogglebutton.setPreferredSize(new Dimension(60, 20));
                super.initButton(uitogglebutton);
            }

            
            {
                this$0 = ReportColumnsPane.this;
                super(as);
            }
        }
;
        onOffButtonGroup.addActionListener(onOffListener);
        UILabel uilabel = new UILabel(Inter.getLocText("FR-Designer_ReportColumns-Columns"));
        uilabel.setFont(FRFont.getInstance("simsun", 0, 14F));
        uilabel.setHorizontalAlignment(0);
        uilabel.setPreferredSize(new Dimension(100, 20));
        jpanel.add(uilabel, "West");
        JPanel jpanel1 = new JPanel(new FlowLayout(1, 23, 11));
        jpanel1.add(onOffButtonGroup);
        jpanel.add(jpanel1, "East");
        add(jpanel, "North");
        add(createRowColumnPane(), "Center");
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("ReportColumns-Report_Columns");
    }

    private void checkEnable()
    {
        if(isRepeate)
        {
            rowButton.setSelected(!colButton.isSelected());
            maxRadioButton.setSelected(!toXRadioButton.isSelected());
        }
        rowButton.setEnabled(isRepeate);
        colButton.setEnabled(isRepeate);
        maxNumberSpinner.setEnabled(isRepeate && maxRadioButton.isSelected());
        toXSpinner.setEnabled(isRepeate && toXRadioButton.isSelected());
        maxRadioButton.setEnabled(isRepeate);
        toXRadioButton.setEnabled(isRepeate);
        repeatColDataTextField.setEnabled(isRepeate);
        copyTitleTextField.setEnabled(isRepeate);
        showBlankCheckBox.setEnabled(isRepeate);
        setAllLableEnabled(this, isRepeate);
    }

    private void setAllLableEnabled(Container container, boolean flag)
    {
        int i = 0;
        for(int j = container.getComponentCount(); i < j; i++)
        {
            Component component = container.getComponent(i);
            if(component instanceof UILabel)
            {
                component.setEnabled(flag);
                continue;
            }
            if(component instanceof Container)
                setAllLableEnabled((Container)component, flag);
        }

    }

    private void colOrRowConvert()
    {
        maxUILabel.setText(COLUMN_ROW_TEXTS[rowOrColumn]);
        toUILabel.setText(COLUMN_ROW_TEXTS[1 - rowOrColumn]);
        showBlankCheckBox.setText(SHOW_BLANK[rowOrColumn]);
        copyLabel.setText((new StringBuilder()).append(REPORT_COLUMN_RAPEAT[rowOrColumn]).append(":").toString());
    }

    private JPanel createRowColumnPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.setBorder(BorderFactory.createEmptyBorder(0, 21, 0, 21));
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel jpanel2 = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText(new String[] {
            "ReportColumns-Columns", "Style"
        }));
        jpanel2.setPreferredSize(new Dimension(549, 216));
        jpanel2.add(createSamplePane());
        jpanel2.add(createRowMaxOrSetPane());
        jpanel1.add(createRowPane(), "North");
        JPanel jpanel3 = new JPanel(new FlowLayout(2, 0, 0));
        showBlankCheckBox = new UICheckBox(SHOW_BLANK[rowOrColumn]);
        jpanel3.add(showBlankCheckBox);
        jpanel1.add(jpanel3, "Center");
        jpanel.add(jpanel2, "North");
        jpanel.add(jpanel1, "Center");
        return jpanel;
    }

    private JPanel createSamplePane()
    {
        JPanel jpanel = new JPanel(new GridLayout(1, 2));
        jpanel.setPreferredSize(new Dimension(524, 130));
        JPanel jpanel1 = new JPanel();
        UILabel uilabel = new UILabel(BaseUtils.readIcon("/com/fr/design/images/reportcolumns/row.png"));
        uilabel.setBorder(BorderFactory.createEmptyBorder(5, 45, 0, 49));
        jpanel1.add(uilabel);
        rowButton = new UIRadioButton(Inter.getLocText("ReportColumns-Columns_horizontally"));
        rowButton.addActionListener(rowChangeListener);
        jpanel1.add(rowButton);
        jpanel.add(jpanel1);
        JPanel jpanel2 = new JPanel();
        UILabel uilabel1 = new UILabel(BaseUtils.readIcon("/com/fr/design/images/reportcolumns/col.png"));
        uilabel1.setBorder(BorderFactory.createEmptyBorder(5, 49, 0, 49));
        jpanel2.add(uilabel1);
        colButton = new UIRadioButton(Inter.getLocText("ReportColumns-Columns_vertically"));
        colButton.addActionListener(colChangeListener);
        jpanel2.add(colButton);
        jpanel.add(jpanel2);
        return jpanel;
    }

    private void emptyValueConvert()
    {
        maxNumberSpinner.setValue(Integer.valueOf(0));
        toXSpinner.setValue(Integer.valueOf(0));
        colOrRowConvert();
    }

    private JPanel createRowMaxOrSetPane()
    {
        JPanel jpanel = new JPanel();
        jpanel.setBorder(BorderFactory.createEmptyBorder(8, 5, 0, 0));
        jpanel.setLayout(new FlowLayout(0, 25, 2));
        maxRadioButton = new UIRadioButton(Inter.getLocText("ReportColumns-Columns_after"));
        maxNumberSpinner = new UIBasicSpinner(new SpinnerNumberModel(0, 0, 0x7fffffff, 1));
        GUICoreUtils.setColumnForSpinner(maxNumberSpinner, 6);
        maxRadioButton.addActionListener(maxBtnListener);
        maxUILabel = new UILabel(COLUMN_ROW_TEXTS[rowOrColumn]);
        JPanel jpanel1 = GUICoreUtils.createFlowPane(new JComponent[] {
            maxRadioButton, maxNumberSpinner, maxUILabel, new UILabel(Inter.getLocText("FR-Designer_ReportColumns-Columns"))
        }, 1);
        jpanel.add(jpanel1);
        toXRadioButton = new UIRadioButton(Inter.getLocText("ReportColumns-Columns_to"));
        toXRadioButton.addActionListener(toXBtnListener);
        toXSpinner = new UIBasicSpinner(new SpinnerNumberModel(0, 0, 0x7fffffff, 1));
        GUICoreUtils.setColumnForSpinner(toXSpinner, 6);
        toUILabel = new UILabel(COLUMN_ROW_TEXTS[1 - rowOrColumn]);
        JPanel jpanel2 = GUICoreUtils.createFlowPane(new JComponent[] {
            toXRadioButton, toXSpinner, toUILabel
        }, 1);
        jpanel.add(jpanel2);
        return jpanel;
    }

    private JPanel createRowPane()
    {
        JPanel jpanel = new JPanel();
        UITitledBorder uititledborder = UITitledBorder.createBorderWithTitle(Inter.getLocText(new String[] {
            "ReportColumns-Columns", "Filed"
        }));
        jpanel.setBorder(uititledborder);
        jpanel.setLayout(new FlowLayout(0, 5, 13));
        jpanel.setPreferredSize(new Dimension(500, 80));
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
            "ReportColumns-Columns", "Data"
        })).append(":").toString()));
        repeatColDataTextField = new UITextField();
        repeatColDataTextField.setPreferredSize(new Dimension(107, 24));
        jpanel.add(repeatColDataTextField);
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Base_Format")).append(": A2:D5 ").toString()));
        copyLabel = new UILabel((new StringBuilder()).append(REPORT_COLUMN_RAPEAT[rowOrColumn]).append(":").toString());
        jpanel.add(copyLabel);
        copyTitleTextField = new UITextField();
        copyTitleTextField.setPreferredSize(new Dimension(107, 24));
        jpanel.add(copyTitleTextField);
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Base_Format")).append(": 1,2-3,5,18").toString()));
        return jpanel;
    }

    public void populate(WorkSheetAttr worksheetattr, int i, int j)
    {
        isRepeate = true;
        switch(worksheetattr.getDirection())
        {
        case 0: // '\0'
            populateTopBottom(worksheetattr);
            setValueText(worksheetattr, i, j, repeatColDataTextField, copyTitleTextField, showBlankCheckBox);
            break;

        case 1: // '\001'
            populateLeftRight(worksheetattr);
            setValueText(worksheetattr, i, j, repeatColDataTextField, copyTitleTextField, showBlankCheckBox);
            break;

        default:
            onOffButtonGroup.setSelectedIndex(1);
            isRepeate = false;
            break;
        }
        colOrRowConvert();
        checkEnable();
    }

    private void populateLeftRight(WorkSheetAttr worksheetattr)
    {
        rowOrColumn = 1;
        onOffButtonGroup.setSelectedIndex(0);
        colButton.setSelected(true);
        rowButton.setSelected(false);
        maxNumberSpinner.setEnabled(false);
        toXSpinner.setEnabled(false);
        if(worksheetattr.getCount() > -1 && worksheetattr.getCount() < 0x7fffffff)
        {
            toXRadioButton.setSelected(true);
            toXSpinner.setValue(new Integer(worksheetattr.getCount()));
            toXSpinner.setEnabled(true);
        } else
        if(worksheetattr.getMaxCount() > -1 && worksheetattr.getMaxCount() < 0x7fffffff)
        {
            maxRadioButton.setSelected(true);
            maxNumberSpinner.setValue(new Integer(worksheetattr.getMaxCount()));
            maxNumberSpinner.setEnabled(true);
        }
    }

    private void populateTopBottom(WorkSheetAttr worksheetattr)
    {
        onOffButtonGroup.setSelectedIndex(0);
        rowButton.setSelected(true);
        colButton.setSelected(false);
        rowOrColumn = 0;
        maxNumberSpinner.setEnabled(false);
        toXSpinner.setEnabled(false);
        if(worksheetattr.getCount() > -1 && worksheetattr.getCount() < 0x7fffffff)
        {
            toXRadioButton.setSelected(true);
            toXSpinner.setValue(new Integer(worksheetattr.getCount()));
            toXSpinner.setEnabled(true);
        } else
        if(worksheetattr.getMaxCount() > -1 && worksheetattr.getMaxCount() < 0x7fffffff)
        {
            maxRadioButton.setSelected(true);
            maxNumberSpinner.setValue(new Integer(worksheetattr.getMaxCount()));
            maxNumberSpinner.setEnabled(true);
        }
    }

    public void populate(WorkSheet worksheet)
    {
        if(worksheet == null)
            return;
        WorkSheetAttr worksheetattr = worksheet.getWorkSheetAttr();
        if(worksheetattr == null)
            worksheetattr = new WorkSheetAttr();
        int i = worksheet.getRowCount();
        int j = worksheet.getColumnCount();
        populate(worksheetattr, i, j);
    }

    private void setValueText(WorkSheetAttr worksheetattr, int i, int j, UITextField uitextfield, UITextField uitextfield1, UICheckBox uicheckbox)
    {
        int l = -1;
        int j1 = -1;
        if(worksheetattr.getStartIndex() == -1 && worksheetattr.getEndIndex() == -1)
            return;
        int k;
        int i1;
        if(worksheetattr.getDirection() == 0)
        {
            k = worksheetattr.getStartIndex();
            l = worksheetattr.getEndIndex() != -1 ? worksheetattr.getEndIndex() : i - 1;
            i1 = worksheetattr.getOppoStartIndex() != -1 ? worksheetattr.getOppoStartIndex() : 0;
            j1 = worksheetattr.getOppoEndIndex() != -1 ? worksheetattr.getOppoEndIndex() : j - 1;
        } else
        {
            k = worksheetattr.getOppoStartIndex() != -1 ? worksheetattr.getOppoStartIndex() : 0;
            l = worksheetattr.getOppoEndIndex() != -1 ? worksheetattr.getOppoEndIndex() : i - 1;
            i1 = worksheetattr.getStartIndex();
            j1 = worksheetattr.getEndIndex() != -1 ? worksheetattr.getEndIndex() : j - 1;
        }
        uitextfield.setText((new StringBuilder()).append(ColumnRow.valueOf(i1, k).toString()).append(":").append(ColumnRow.valueOf(j1, l).toString()).toString());
        uitextfield1.setText(worksheetattr.getIndexsToCopy());
        uicheckbox.setSelected(worksheetattr.isShowBlank());
    }

    private void updateRow(WorkSheetAttr worksheetattr)
    {
        worksheetattr.setDirection((byte)0);
        if(maxRadioButton.isSelected())
        {
            int i = ((Integer)(Integer)maxNumberSpinner.getValue()).intValue();
            if(i > 0 && i < 0x7fffffff)
                worksheetattr.setMaxCount(i);
        } else
        {
            int j = ((Integer)toXSpinner.getValue()).intValue();
            if(j > 0 && j < 0x7fffffff)
                worksheetattr.setCount(j);
        }
    }

    private void updateCol(WorkSheetAttr worksheetattr)
    {
        worksheetattr.setDirection((byte)1);
        if(maxRadioButton.isSelected())
        {
            double d = ((Integer)(Integer)maxNumberSpinner.getValue()).intValue();
            if(d > 0.0D && d < 2147483647D)
                worksheetattr.setMaxCount((int)d);
        } else
        {
            double d1 = ((Integer)toXSpinner.getValue()).intValue();
            if(d1 > 0.0D && d1 < 2147483647D)
                worksheetattr.setCount((int)d1);
        }
    }

    public void update(WorkSheetAttr worksheetattr)
    {
        if(!isRepeate)
        {
            worksheetattr.setDirection((byte)2);
            return;
        }
        switch(rowOrColumn)
        {
        case 0: // '\0'
            updateRow(worksheetattr);
            divide(worksheetattr, repeatColDataTextField, copyTitleTextField, showBlankCheckBox);
            break;

        case 1: // '\001'
            updateCol(worksheetattr);
            divide(worksheetattr, repeatColDataTextField, copyTitleTextField, showBlankCheckBox);
            break;

        default:
            worksheetattr.setDirection((byte)2);
            break;
        }
    }

    public void update(WorkSheet worksheet)
    {
        if(worksheet == null)
        {
            return;
        } else
        {
            WorkSheetAttr worksheetattr = new WorkSheetAttr();
            worksheet.setWorkSheetAttr(worksheetattr);
            update(worksheetattr);
            return;
        }
    }

    public void divide(WorkSheetAttr worksheetattr, UITextField uitextfield, UITextField uitextfield1, UICheckBox uicheckbox)
    {
        String as[] = uitextfield.getText().split(":");
        if(as.length != 2)
            return;
        ColumnRow columnrow = ColumnRow.valueOf(as[0]);
        ColumnRow columnrow1 = ColumnRow.valueOf(as[1]);
        int i;
        int j;
        int k;
        int l;
        if(worksheetattr.getDirection() == 0)
        {
            i = columnrow.getRow();
            k = columnrow1.getRow();
            j = columnrow.getColumn();
            l = columnrow1.getColumn();
        } else
        {
            i = columnrow.getColumn();
            k = columnrow1.getColumn();
            j = columnrow.getRow();
            l = columnrow1.getRow();
        }
        worksheetattr.setStartIndex(i);
        worksheetattr.setOppoStartIndex(j);
        worksheetattr.setEndIndex(k);
        worksheetattr.setOppoEndIndex(l);
        worksheetattr.setIndexsToCopy(uitextfield1.getText());
        worksheetattr.setShowBlank(uicheckbox.isSelected());
    }

    public void checkValid()
        throws Exception
    {
        String s = repeatColDataTextField.getText().trim();
        if(StringUtils.isEmpty(s))
            return;
        boolean flag = true;
        if(!s.matches("[a-zA-Z]+[0-9]+[:][a-zA-Z]+[0-9]+"))
        {
            flag = false;
        } else
        {
            String as[] = s.split(":");
            ColumnRow columnrow = ColumnRow.valueOf(as[0]);
            ColumnRow columnrow1 = ColumnRow.valueOf(as[1]);
            flag = columnrow.getRow() <= columnrow1.getRow() && columnrow.getColumn() <= columnrow1.getColumn();
        }
        if(!flag)
        {
            repeatColDataTextField.setText("");
            throw new Exception(Inter.getLocText(new String[] {
                "Tooltips", "ReportColumns-Columns", "Filed", "Format", "Error"
            }, new String[] {
                ": ", "", "", "", "!"
            }));
        } else
        {
            return;
        }
    }












}
