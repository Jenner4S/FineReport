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
import com.fr.io.attr.PDFExportAttr;
import com.fr.stable.StringUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class PDFExportPane extends BasicPane
{

    private UICheckBox isNeedPassword;
    private UITextField passwordField;
    private JPanel passwordWritePane;

    public PDFExportPane()
    {
        initComponents();
    }

    protected void initComponents()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createTitledBorderPane((new StringBuilder()).append("PDF").append(Inter.getLocText("ReportD-Excel_Export")).toString());
        JPanel jpanel1 = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
        add(jpanel);
        jpanel.add(jpanel1);
        JPanel jpanel2 = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
        jpanel1.add(jpanel2);
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

            final PDFExportPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                passwordWritePane.setVisible(isNeedPassword.isSelected());
            }

            
            {
                this$0 = PDFExportPane.this;
                super();
            }
        }
);
    }

    protected String title4PopupWindow()
    {
        return "PDFExport";
    }

    public void populate(PDFExportAttr pdfexportattr)
    {
        if(pdfexportattr == null)
            return;
        if(StringUtils.isEmpty(pdfexportattr.getPassword()))
        {
            isNeedPassword.setSelected(false);
            passwordWritePane.setVisible(false);
        } else
        {
            isNeedPassword.setSelected(true);
            passwordField.setText(pdfexportattr.getPassword());
            passwordWritePane.setVisible(true);
        }
    }

    public PDFExportAttr update()
    {
        PDFExportAttr pdfexportattr = new PDFExportAttr();
        if(!isNeedPassword.isSelected())
            passwordField.setText(null);
        pdfexportattr.setPassword(passwordField.getText());
        return pdfexportattr;
    }


}
