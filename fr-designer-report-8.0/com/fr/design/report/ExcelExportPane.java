// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.io.attr.ExcelExportAttr;
import com.fr.stable.StringUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class ExcelExportPane extends BasicPane
{

    private UICheckBox isExportHidedRow;
    private UICheckBox isExportHidenColumn;
    private UICheckBox isNeedPassword;
    private UITextField passwordField;
    private UICheckBox protectedWord;
    private UITextField protectedField;
    private JPanel passwordWritePane;
    private JPanel wordPane;

    public ExcelExportPane()
    {
        initComponents();
    }

    protected void initComponents()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createTitledBorderPane((new StringBuilder()).append("Excel").append(Inter.getLocText("ReportD-Excel_Export")).toString());
        JPanel jpanel1 = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
        add(jpanel);
        jpanel.add(jpanel1);
        JPanel jpanel2 = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
        jpanel1.add(jpanel2);
        isExportHidedRow = new UICheckBox(Inter.getLocText("ReportD-Export_Hided_Row"));
        isExportHidedRow.setSelected(false);
        jpanel2.add(isExportHidedRow);
        isExportHidenColumn = new UICheckBox(Inter.getLocText("ReportD-Export_Hided_Column"));
        isExportHidenColumn.setSelected(false);
        jpanel2.add(isExportHidenColumn);
        JPanel jpanel3 = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
        isNeedPassword = new UICheckBox(Inter.getLocText("IS_Need_Password"), false);
        jpanel3.add(isNeedPassword);
        jpanel1.add(jpanel3);
        passwordWritePane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel3.add(passwordWritePane);
        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
            "File", "Password"
        })).append(":").toString());
        passwordWritePane.add(uilabel);
        passwordField = new UITextField(11);
        passwordWritePane.add(passwordField);
        isNeedPassword.addActionListener(new ActionListener() {

            final ExcelExportPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(isNeedPassword.isSelected())
                    passwordWritePane.setVisible(true);
                else
                    passwordWritePane.setVisible(false);
            }

            
            {
                this$0 = ExcelExportPane.this;
                super();
            }
        }
);
        JPanel jpanel4 = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
        protectedWord = new UICheckBox(Inter.getLocText(new String[] {
            "Protected", "Password"
        }));
        wordPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        wordPane.add(new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
            "Sheet", "Password"
        })).append(":").toString()));
        protectedField = new UITextField(11);
        wordPane.add(protectedField);
        jpanel4.add(protectedWord);
        jpanel4.add(wordPane);
        jpanel1.add(jpanel4);
        protectedWord.addActionListener(new ActionListener() {

            final ExcelExportPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(protectedWord.isSelected())
                {
                    wordPane.setVisible(true);
                } else
                {
                    wordPane.setVisible(false);
                    protectedField.setText(null);
                }
            }

            
            {
                this$0 = ExcelExportPane.this;
                super();
            }
        }
);
    }

    protected String title4PopupWindow()
    {
        return "ExcelExport";
    }

    public void populate(ExcelExportAttr excelexportattr)
    {
        if(excelexportattr == null)
            return;
        isExportHidedRow.setSelected(excelexportattr.isExportHidedRow());
        isExportHidenColumn.setSelected(excelexportattr.isExportHidedColumn());
        if(StringUtils.isEmpty(excelexportattr.getPassword()))
        {
            isNeedPassword.setSelected(false);
            passwordWritePane.setVisible(false);
        } else
        {
            isNeedPassword.setSelected(true);
            passwordField.setText(excelexportattr.getPassword());
            passwordWritePane.setVisible(true);
        }
        if(StringUtils.isEmpty(excelexportattr.getProtectedWord()))
        {
            protectedWord.setSelected(false);
            wordPane.setVisible(false);
        } else
        {
            protectedWord.setSelected(true);
            wordPane.setVisible(true);
            protectedField.setText(excelexportattr.getProtectedWord());
        }
    }

    public ExcelExportAttr update()
    {
        ExcelExportAttr excelexportattr = new ExcelExportAttr();
        excelexportattr.setExportHidedColumn(isExportHidenColumn.isSelected());
        excelexportattr.setExportHidedRow(isExportHidedRow.isSelected());
        if(!isNeedPassword.isSelected())
            passwordField.setText(null);
        excelexportattr.setPassword(passwordField.getText());
        excelexportattr.setProtectedWord(protectedField.getText());
        return excelexportattr;
    }





}
