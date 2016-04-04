// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.quickeditor.cellquick;

import com.fr.design.actions.insert.cell.SubReportCellAction;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;
import com.fr.quickeditor.CellQuickEditor;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

public class CellSubReportEditor extends CellQuickEditor
{

    private UIButton subReportButton;
    private static CellSubReportEditor THIS;

    public static final CellSubReportEditor getInstance()
    {
        if(THIS == null)
            THIS = new CellSubReportEditor();
        return THIS;
    }

    private CellSubReportEditor()
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
        SubReportCellAction subreportcellaction = new SubReportCellAction((ElementCasePane)tc);
        subreportcellaction.setName(Inter.getLocText(new String[] {
            "Edit", "Sub_Report"
        }));
        subReportButton.setAction(subreportcellaction);
    }
}
