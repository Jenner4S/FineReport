// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.quickeditor.cellquick;

import com.fr.design.actions.insert.cell.RichTextCellAction;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;
import com.fr.quickeditor.CellQuickEditor;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

public class CellRichTextEditor extends CellQuickEditor
{

    private UIButton subReportButton;
    private static CellRichTextEditor THIS;

    public static final CellRichTextEditor getInstance()
    {
        if(THIS == null)
            THIS = new CellRichTextEditor();
        return THIS;
    }

    private CellRichTextEditor()
    {
    }

    public JComponent createCenterBody()
    {
        subReportButton = new UIButton();
        subReportButton.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        subReportButton.setMargin(null);
        subReportButton.setOpaque(false);
        return subReportButton;
    }

    protected void refreshDetails()
    {
        RichTextCellAction richtextcellaction = new RichTextCellAction((ElementCasePane)tc);
        richtextcellaction.setName(Inter.getLocText("FR-Designer_RichTextEditor"));
        subReportButton.setAction(richtextcellaction);
    }
}
