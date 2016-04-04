// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.MultilineLabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.io.attr.WordExportAttr;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

public class WordExportPane extends BasicPane
{

    private UICheckBox isExportAsTable;
    private JPanel southPane;

    public WordExportPane()
    {
        initComponents();
    }

    protected void initComponents()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createTitledBorderPane((new StringBuilder()).append("Word").append(Inter.getLocText("ReportD-Excel_Export")).toString());
        add(jpanel);
        JPanel jpanel1 = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
        jpanel.add(jpanel1);
        JPanel jpanel2 = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
        isExportAsTable = new UICheckBox(Inter.getLocText("is_need_word_adjust"), false);
        jpanel2.add(isExportAsTable);
        southPane = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
        JPanel jpanel3 = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Attention"));
        JPanel jpanel4 = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
        MultilineLabel multilinelabel = new MultilineLabel();
        multilinelabel.setPreferredSize(new Dimension(250, 100));
        multilinelabel.setText(Inter.getLocText("alert_word"));
        multilinelabel.setForeground(Color.RED);
        jpanel4.add(multilinelabel);
        southPane.add(jpanel3);
        jpanel3.add(jpanel4);
        jpanel1.add(jpanel2);
        jpanel1.add(southPane);
    }

    protected String title4PopupWindow()
    {
        return "WordExport";
    }

    public void populate(WordExportAttr wordexportattr)
    {
        if(wordexportattr == null)
            return;
        if(wordexportattr.isExportAsTable())
            isExportAsTable.setSelected(true);
    }

    public WordExportAttr update()
    {
        WordExportAttr wordexportattr = new WordExportAttr();
        wordexportattr.setExportAsTable(isExportAsTable.isSelected());
        return wordexportattr;
    }
}
