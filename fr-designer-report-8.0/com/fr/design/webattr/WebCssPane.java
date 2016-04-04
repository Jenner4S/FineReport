// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.EditingStringListPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.DesignerContext;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import com.fr.web.attr.ReportWebAttr;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class WebCssPane extends BasicPane
{

    private UITextField localText;
    UIButton chooseFile;
    private EditingStringListPane centerPane;
    private ActionListener chooseFileListener;

    public WebCssPane()
    {
        chooseFileListener = new ActionListener() {

            final WebCssPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                FILEChooserPane filechooserpane = FILEChooserPane.getInstance(false, false, true, new ChooseFileFilter("css", (new StringBuilder()).append("css").append(Inter.getLocText("File")).toString()));
                if(filechooserpane.showOpenDialog(DesignerContext.getDesignerFrame()) == 0)
                {
                    FILE file = filechooserpane.getSelectedFILE();
                    if(file == null)
                        return;
                    String s = file.getName();
                    String s1 = s.substring(s.lastIndexOf(".") + 1);
                    if(!"css".equalsIgnoreCase(s1))
                        return;
                    localText.setText(file.getPath().substring(1));
                    centerPane.setAddEnabled(true);
                }
                filechooserpane.removeFILEFilter(new ChooseFileFilter("js"));
            }

            
            {
                this$0 = WebCssPane.this;
                super();
            }
        }
;
        setLayout(new BorderLayout(0, 20));
        setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 0));
        JPanel jpanel = new JPanel(new BorderLayout(0, 5));
        JPanel jpanel1 = new JPanel(new FlowLayout(0, 8, 0));
        localText = new UITextField();
        localText.setPreferredSize(new Dimension(450, 20));
        localText.setEditable(false);
        chooseFile = new UIButton(Inter.getLocText("Selection"));
        chooseFile.setPreferredSize(new Dimension(75, 23));
        chooseFile.addActionListener(chooseFileListener);
        jpanel1.add(new UILabel((new StringBuilder()).append(Inter.getLocText("Disk_File")).append(":").toString()), 0);
        jpanel1.add(localText, 1);
        jpanel1.add(chooseFile, 2);
        jpanel.add(jpanel1, "North");
        UILabel uilabel = new UILabel(Inter.getLocText("CSS_warning"));
        uilabel.setForeground(new Color(207, 42, 39));
        jpanel.add(uilabel, "Center");
        add(jpanel, "North");
        centerPane = new EditingStringListPane() {

            final WebCssPane this$0;

            protected void selectedChanged(String s)
            {
                localText.setText(s);
                checkEnableState();
            }

            protected String getAddOrEditString()
            {
                return localText.getText();
            }

            
            {
                this$0 = WebCssPane.this;
                super();
            }
        }
;
        add(centerPane, "Center");
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("ReportServerP-Import_Css");
    }

    public void populate(ReportWebAttr reportwebattr)
    {
        if(reportwebattr == null)
        {
            centerPane.populateBean(new ArrayList());
            return;
        }
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < reportwebattr.getCSSImportCount(); i++)
            if(StringUtils.isNotBlank(reportwebattr.getCSSImport(i)))
                arraylist.add(reportwebattr.getCSSImport(i));

        centerPane.populateBean(arraylist);
    }

    public void update(ReportWebAttr reportwebattr)
    {
        java.util.List list = centerPane.updateBean();
        reportwebattr.clearCSSImportList();
        for(int i = 0; i < list.size(); i++)
        {
            String s = (String)list.get(i);
            reportwebattr.addCSSImport(s);
        }

    }


}
