// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.report;

import com.fr.base.BaseUtils;
import com.fr.design.actions.ReportComponentAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.WorkSheetDesigner;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.report.LayerReportPane;
import com.fr.report.worksheet.WorkSheet;

public class ReportEngineAttrAction extends ReportComponentAction
{

    private boolean isChange;

    public ReportEngineAttrAction(WorkSheetDesigner worksheetdesigner)
    {
        super(worksheetdesigner);
        setMenuKeySet(KeySetUtils.REPORT_ENGINE);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/reportEngineAttr.png"));
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        WorkSheetDesigner worksheetdesigner = (WorkSheetDesigner)getEditingComponent();
        if(worksheetdesigner == null)
        {
            return false;
        } else
        {
            final WorkSheet tplEC = (WorkSheet)worksheetdesigner.getTemplateReport();
            final LayerReportPane layerReportPane = new LayerReportPane(tplEC);
            layerReportPane.populateBean(tplEC.getLayerReportAttr());
            BasicDialog basicdialog = layerReportPane.showWindow(DesignerContext.getDesignerFrame());
            isChange = false;
            basicdialog.addDialogActionListener(new DialogActionAdapter() {

                final WorkSheet val$tplEC;
                final LayerReportPane val$layerReportPane;
                final ReportEngineAttrAction this$0;

                public void doOk()
                {
                    isChange = true;
                    tplEC.setLayerReportAttr(layerReportPane.updateBean());
                }

            
            {
                this$0 = ReportEngineAttrAction.this;
                tplEC = worksheet;
                layerReportPane = layerreportpane;
                super();
            }
            }
);
            basicdialog.setVisible(true);
            return isChange;
        }
    }

}
