// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.design.gui.frpane.LoadingBasicPane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.report.VerifierListPane;
import com.fr.design.report.WriteShortCutsPane;
import com.fr.design.write.submit.SubmiterListPane;
import com.fr.general.Inter;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.bridge.StableFactory;
import com.fr.write.ReportWriteAttrProvider;
import javax.swing.JPanel;

public class ReportWriteAttrPane extends LoadingBasicPane
{

    private SubmiterListPane submiterListPane;
    private VerifierListPane verifierListPane;
    private WriteShortCutsPane writeShortCutsPane;
    private ElementCasePane ePane;

    public ReportWriteAttrPane(ElementCasePane elementcasepane)
    {
        ePane = elementcasepane;
    }

    protected void initComponents(JPanel jpanel)
    {
        jpanel.setLayout(FRGUIPaneFactory.createBorderLayout());
        UITabbedPane uitabbedpane = new UITabbedPane(1, 1);
        jpanel.add(uitabbedpane, "Center");
        if(submiterListPane == null)
            submiterListPane = new SubmiterListPane(ePane);
        if(verifierListPane == null)
            verifierListPane = new VerifierListPane(ePane);
        writeShortCutsPane = new WriteShortCutsPane();
        uitabbedpane.addTab(Inter.getLocText("FR-Utils_Submit"), submiterListPane);
        uitabbedpane.addTab(Inter.getLocText("Verify-Data_Verify"), verifierListPane);
        uitabbedpane.addTab(Inter.getLocText("Writer-ShortCuts_Setting"), writeShortCutsPane);
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("ReportD-Report_Write_Attributes");
    }

    public void populate(WorkSheet worksheet)
    {
        if(worksheet == null)
            return;
        ReportWriteAttrProvider reportwriteattrprovider = worksheet.getReportWriteAttr();
        if(reportwriteattrprovider == null)
            reportwriteattrprovider = (ReportWriteAttrProvider)StableFactory.getMarkedInstanceObjectFromClass("ReportWriteAttr", com/fr/write/ReportWriteAttrProvider);
        submiterListPane.populate(reportwriteattrprovider);
        verifierListPane.populate(reportwriteattrprovider);
    }

    public ReportWriteAttrProvider update()
    {
        ReportWriteAttrProvider reportwriteattrprovider = (ReportWriteAttrProvider)StableFactory.getMarkedInstanceObjectFromClass("ReportWriteAttr", com/fr/write/ReportWriteAttrProvider);
        submiterListPane.updateReportWriteAttr(reportwriteattrprovider);
        verifierListPane.updateReportWriteAttr(reportwriteattrprovider);
        return reportwriteattrprovider;
    }

    public void checkValid()
        throws Exception
    {
        submiterListPane.checkValid();
        verifierListPane.checkValid();
    }

    private int getInvalidIndex()
    {
        int i = 0;
        try
        {
            submiterListPane.checkValid();
            i++;
            verifierListPane.checkValid();
        }
        catch(Exception exception)
        {
            return i;
        }
        return -1;
    }
}
