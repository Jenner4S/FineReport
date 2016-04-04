// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.report;

import com.fr.base.BaseUtils;
import com.fr.design.actions.ReportComponentAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.WorkSheetDesigner;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.report.freeze.RepeatAndFreezeSettingPane;
import com.fr.page.ReportSettingsProvider;
import com.fr.report.worksheet.WorkSheet;
import javax.swing.SwingUtilities;

public class ReportPageAttrAction extends ReportComponentAction
{

    private boolean returnValue;

    public ReportPageAttrAction(WorkSheetDesigner worksheetdesigner)
    {
        super(worksheetdesigner);
        setMenuKeySet(KeySetUtils.REPORT_PAGE);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/pageSetup.png"));
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        WorkSheetDesigner worksheetdesigner = (WorkSheetDesigner)getEditingComponent();
        if(worksheetdesigner == null)
        {
            return false;
        } else
        {
            final WorkSheet tplECReport = (WorkSheet)worksheetdesigner.getTemplateReport();
            final RepeatAndFreezeSettingPane attrPane = new RepeatAndFreezeSettingPane();
            attrPane.populate(tplECReport.getReportPageAttr());
            attrPane.populateWriteFrozenColumnRow(tplECReport.getReportSettings().getWriteFrozenColumnRow());
            BasicDialog basicdialog = attrPane.showWindow(SwingUtilities.getWindowAncestor(worksheetdesigner));
            basicdialog.addDialogActionListener(new DialogActionAdapter() {

                final WorkSheet val$tplECReport;
                final RepeatAndFreezeSettingPane val$attrPane;
                final ReportPageAttrAction this$0;

                public void doOk()
                {
                    tplECReport.setReportPageAttr(attrPane.update());
                    tplECReport.getReportSettings().setWriteFrozenColumnRow(attrPane.updateWriteFrozenColumnRow());
                    returnValue = true;
                }

            
            {
                this$0 = ReportPageAttrAction.this;
                tplECReport = worksheet;
                attrPane = repeatandfreezesettingpane;
                super();
            }
            }
);
            basicdialog.setVisible(true);
            return returnValue;
        }
    }

}
