// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

public class ReportSelectToolBarPane extends BasicBeanPane
{
    public static abstract class EditToolBarPane extends BasicBeanPane
    {

        public abstract void editServerToolBarPane();

        public EditToolBarPane()
        {
        }
    }


    private UIRadioButton reportRadioButton;
    private UIRadioButton serverRadioButton;
    private UIButton serverEditButton;
    EditToolBarPane editToolBarPane;
    private ActionListener checkEnabledActionListener;

    public ReportSelectToolBarPane(EditToolBarPane edittoolbarpane)
    {
        reportRadioButton = new UIRadioButton(Inter.getLocText("I_Want_To_Set_Single"));
        serverRadioButton = new UIRadioButton(Inter.getLocText("Using_Server_Report_View_Settings"));
        serverEditButton = new UIButton(Inter.getLocText("Edit"));
        checkEnabledActionListener = new ActionListener() {

            final ReportSelectToolBarPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                checkEnabled();
            }

            
            {
                this$0 = ReportSelectToolBarPane.this;
                super();
            }
        }
;
        editToolBarPane = edittoolbarpane;
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createX_AXISBoxInnerContainer_S_Pane();
        add(jpanel, "North");
        ButtonGroup buttongroup = new ButtonGroup();
        buttongroup.add(reportRadioButton);
        buttongroup.add(serverRadioButton);
        jpanel.add(reportRadioButton);
        jpanel.add(serverRadioButton);
        reportRadioButton.addActionListener(checkEnabledActionListener);
        serverRadioButton.addActionListener(checkEnabledActionListener);
        jpanel.add(serverEditButton);
        serverEditButton.addActionListener(new ActionListener() {

            final ReportSelectToolBarPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                editToolBarPane.editServerToolBarPane();
            }

            
            {
                this$0 = ReportSelectToolBarPane.this;
                super();
            }
        }
);
        add(FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane(), "West");
        add(edittoolbarpane, "Center");
    }

    private void checkEnabled()
    {
        editToolBarPane.setEnabled(reportRadioButton.isSelected());
        serverEditButton.setEnabled(serverRadioButton.isSelected());
    }

    protected String title4PopupWindow()
    {
        return "select";
    }

    public void populateBean(Object obj)
    {
        reportRadioButton.setSelected(obj != null);
        serverRadioButton.setSelected(obj == null);
        if(obj != null)
            editToolBarPane.populateBean(obj);
        checkEnabled();
    }

    public Object updateBean()
    {
        if(reportRadioButton.isSelected())
            return editToolBarPane.updateBean();
        else
            return null;
    }

}
