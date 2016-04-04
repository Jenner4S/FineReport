// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.smartaction;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.Grid;
import java.awt.Container;
import java.awt.Dialog;

// Referenced classes of package com.fr.design.cell.smartaction:
//            SmartJTablePaneAction, SmartJTablePane

public abstract class AbstractSmartJTablePaneAction
    implements SmartJTablePaneAction
{

    protected Container dialog;
    private SmartJTablePane smartJTablePane;

    public AbstractSmartJTablePaneAction(SmartJTablePane smartjtablepane, Container container)
    {
        smartJTablePane = smartjtablepane;
        dialog = container;
    }

    public void setSmartJTablePane(SmartJTablePane smartjtablepane)
    {
        smartJTablePane = smartjtablepane;
    }

    public void doDialogExit(int i)
    {
        smartJTablePane.actionReportPane.getGrid().setNotShowingTableSelectPane(true);
        smartJTablePane.actionReportPane.setEditable(smartJTablePane.old_editable);
        smartJTablePane.actionReportPane.removeSelectionChangeListener(smartJTablePane.gridSelectionChangeL);
        smartJTablePane.actionReportPane.repaint();
        if(i == 0)
            doOk();
        showDialog();
    }

    public void showDialog()
    {
        for(; dialog != null && !(dialog instanceof Dialog); dialog = dialog.getParent());
        if(dialog != null)
        {
            dialog.repaint();
            dialog.setVisible(true);
        }
    }

    public abstract void doOk();
}
