// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.general.Inter;
import com.fr.main.TemplateWorkBook;
import com.fr.main.impl.LinkWorkBookTemplate;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.cellattr.core.SubReport;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.StringUtils;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;

public class SubReportPane extends BasicPane
{

    private UITextField pathTextField;
    private FILE chooseFILE;
    private ReportletParameterViewPane kvPane;
    private UICheckBox extend;

    public SubReportPane()
    {
        chooseFILE = null;
        kvPane = null;
        initComponents();
    }

    protected void initComponents()
    {
        setLayout(FRGUIPaneFactory.createM_BorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText(new String[] {
            "Sub_Report", "Path"
        }), null));
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText("Location")).append(":").toString()), "West");
        jpanel.add(pathTextField = new UITextField(), "Center");
        pathTextField.setEditable(false);
        UIButton uibutton = new UIButton("...");
        jpanel.add(uibutton, "East");
        uibutton.setPreferredSize(new Dimension(20, 20));
        uibutton.setToolTipText(Inter.getLocText("Click_this_button"));
        uibutton.addActionListener(new ActionListener() {

            final SubReportPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                FILEChooserPane filechooserpane = FILEChooserPane.getInstance(true, false);
                int i = filechooserpane.showOpenDialog(SubReportPane.this);
                if(i == 0 || i == 2)
                {
                    chooseFILE = filechooserpane.getSelectedFILE();
                    if(chooseFILE != null && chooseFILE.exists())
                    {
                        pathTextField.setText((new StringBuilder()).append(chooseFILE.prefix()).append(chooseFILE.getPath()).toString());
                    } else
                    {
                        JOptionPane.showConfirmDialog(SubReportPane.this, Inter.getLocText("Sub_Report_Message1"), Inter.getLocText("Sub_Report_ToolTips"), 2, 2);
                        chooseFILE = null;
                        pathTextField.setText("");
                    }
                }
            }

            
            {
                this$0 = SubReportPane.this;
                super();
            }
        }
);
        add(jpanel, "North");
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        add(jpanel1, "Center");
        jpanel1.setLayout(FRGUIPaneFactory.createM_BorderLayout());
        jpanel1.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));
        kvPane = new ReportletParameterViewPane();
        jpanel1.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText(new String[] {
            "Set", "Delivery", "Parameter"
        }), null));
        JPanel jpanel2 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel2.add(kvPane);
        extend = new UICheckBox(Inter.getLocText("Hyperlink-Extends_Report_Parameters"));
        jpanel2.add(extend, "South");
        jpanel1.add(jpanel2, "Center");
        UITextArea uitextarea = new UITextArea(2, 1);
        jpanel1.add(uitextarea, "South");
        uitextarea.setText(Inter.getLocText("Sub_Report_Description"));
        uitextarea.setEditable(false);
        uitextarea.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("Attention"), null));
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "Insert", "Sub_Report"
        });
    }

    public void populate(ElementCase elementcase, CellElement cellelement)
    {
        Object obj = cellelement.getValue();
        if(obj != null && (obj instanceof SubReport))
        {
            TemplateWorkBook templateworkbook = ((SubReport)obj).getPackee();
            if(templateworkbook != null && (templateworkbook instanceof LinkWorkBookTemplate))
            {
                String s = ((LinkWorkBookTemplate)templateworkbook).getTemplatePath();
                if(StringUtils.isNotBlank(s))
                    pathTextField.setText((new StringBuilder()).append("env://reportlets").append(File.separator).append(s).toString());
            }
            com.fr.base.core.KV akv[] = ((SubReport)obj).getParameterKVS();
            kvPane.populate(akv);
            extend.setSelected(((SubReport)obj).isExtendOwnerParameters());
        }
    }

    public SubReport update()
    {
        LinkWorkBookTemplate linkworkbooktemplate = new LinkWorkBookTemplate();
        int i = "env://".length() + "reportlets".length() + 1;
        String s = pathTextField.getText().substring(i);
        linkworkbooktemplate.setTemplatePath(s);
        SubReport subreport = new SubReport(linkworkbooktemplate);
        subreport.setParameterKVS(kvPane.updateKV());
        subreport.setExtendOwnerParameters(extend.isSelected());
        return subreport;
    }

    protected boolean checkFILE()
    {
        return true;
    }



}
