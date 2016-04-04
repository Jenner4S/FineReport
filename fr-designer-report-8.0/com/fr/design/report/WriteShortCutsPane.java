// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report;

import com.fr.base.*;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class WriteShortCutsPane extends JPanel
{

    private String nextColString;
    private String nextRowString;
    private UILabel nextColHK;
    private UILabel nextRowHK;
    private UILabel preCol;
    private UILabel preRow;

    public WriteShortCutsPane()
    {
        nextColString = "Tab";
        nextRowString = "Enter";
        setLayout(null);
        add(getFeatureNamePane());
        add(getHintsPane());
        if(!ConfigManager.getProviderInstance().isWriteShortCuts())
        {
            nextColString = "Enter";
            nextRowString = "Tab";
            switchColRow();
        }
    }

    public JPanel getFeatureNamePane()
    {
        JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel.setBounds(20, 20, 600, 140);
        jpanel.setLayout(null);
        jpanel.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("FR-Designer_Shortcut_Set")));
        UILabel uilabel = new UILabel(Inter.getLocText("FR-Designer_Feature_Name"), 0);
        uilabel.setBounds(40, 30, 80, 50);
        UILabel uilabel1 = new UILabel(Inter.getLocText("FR-Designer_Cursor_to_next_column"), 0);
        uilabel1.setBounds(140, 30, 180, 50);
        UILabel uilabel2 = new UILabel(Inter.getLocText("FR-Designer_Cursor_to_next_row"), 0);
        uilabel2.setBounds(390, 30, 180, 50);
        UILabel uilabel3 = new UILabel(Inter.getLocText("FR-Designer_Current_keys"), 0);
        uilabel3.setBounds(40, 80, 80, 50);
        nextColHK = new UILabel(nextColString, 0);
        nextColHK.setBounds(140, 80, 180, 50);
        UIButton uibutton = new UIButton(BaseUtils.readIcon("com/fr/design/images/buttonicon/switchShortCuts.png"));
        uibutton.addActionListener(getListener());
        uibutton.setToolTipText(Inter.getLocText("FR-Designer_Exchange_key"));
        uibutton.setBounds(337, 90, 36, 29);
        nextRowHK = new UILabel(nextRowString, 0);
        nextRowHK.setBounds(390, 80, 180, 50);
        jpanel.add(uilabel);
        jpanel.add(uilabel1);
        jpanel.add(uilabel2);
        jpanel.add(uilabel3);
        jpanel.add(nextColHK);
        jpanel.add(uibutton);
        jpanel.add(nextRowHK);
        return jpanel;
    }

    public JPanel getHintsPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel.setBounds(20, 170, 600, 150);
        jpanel.setLayout(null);
        jpanel.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("FR-Designer_Tooltips")));
        UILabel uilabel = new UILabel(Inter.getLocText("FR-Designer_System_default"), 0);
        uilabel.setBounds(38, 30, 84, 50);
        UILabel uilabel1 = new UILabel(Inter.getLocText("FR-Designer_Cursor_to_previous_column"), 0);
        uilabel1.setBounds(140, 30, 190, 50);
        UILabel uilabel2 = new UILabel(Inter.getLocText("FR-Designer_Cursor_to_previous_row"), 0);
        uilabel2.setBounds(140, 80, 190, 50);
        preCol = new UILabel((new StringBuilder()).append("Shift+").append(nextColString).toString(), 0);
        preCol.setBounds(370, 30, 100, 50);
        preRow = new UILabel((new StringBuilder()).append("Shift+").append(nextRowString).toString(), 0);
        preRow.setBounds(370, 80, 100, 50);
        jpanel.add(uilabel);
        jpanel.add(uilabel1);
        jpanel.add(uilabel2);
        jpanel.add(preCol);
        jpanel.add(preRow);
        return jpanel;
    }

    public ActionListener getListener()
    {
        ActionListener actionlistener = new ActionListener() {

            final WriteShortCutsPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                String s = nextColString;
                nextColString = nextRowString;
                nextRowString = s;
                switchColRow();
                ConfigManager.getProviderInstance().setWriteShortCuts(ComparatorUtils.equals(nextColString, "Tab"));
                try
                {
                    FRContext.getCurrentEnv().writeResource(ConfigManager.getProviderInstance());
                }
                catch(Exception exception)
                {
                    FRContext.getLogger().error(exception.getMessage());
                }
            }

            
            {
                this$0 = WriteShortCutsPane.this;
                super();
            }
        }
;
        return actionlistener;
    }

    private void switchColRow()
    {
        nextColHK.setText(nextColString);
        nextRowHK.setText(nextRowString);
        preCol.setText((new StringBuilder()).append("Shift+").append(nextColString).toString());
        preRow.setText((new StringBuilder()).append("Shift+").append(nextRowString).toString());
    }





}
