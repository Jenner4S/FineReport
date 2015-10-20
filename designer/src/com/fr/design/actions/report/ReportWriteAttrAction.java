package com.fr.design.actions.report;

import com.fr.base.BaseUtils;
import com.fr.design.actions.ReportComponentAction;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.WorkSheetDesigner;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.webattr.ReportWriteAttrPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.report.worksheet.WorkSheet;

/**
 * ReportWriteAttrAction
 */
public class ReportWriteAttrAction extends ReportComponentAction<WorkSheetDesigner> {
    public ReportWriteAttrAction(WorkSheetDesigner t) {
        super(t);
        this.setMenuKeySet(KeySetUtils.REPORT_WRITE);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/reportWriteAttr.png"));
    }

    // ben:�����ж��Ƿ�update
    private boolean isChange;
    private boolean hasActionPerformed;

    /**
     * ִ�ж���
     *
     * @return �Ƿ�ִ�гɹ�
     */
    public boolean executeActionReturnUndoRecordNeeded() {
        hasActionPerformed = false;
        WorkSheetDesigner jws = getEditingComponent();
        if (jws == null) {
            return false;
        }
        final WorkSheet tplEC = jws.getTemplateReport();

        final ReportWriteAttrPane reportWritePane = new ReportWriteAttrPane(jws.getEditingElementCasePane()) {
            @Override
            public void complete() {
                populate(tplEC);
            }
        };

        BasicDialog dialog = reportWritePane.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
            @Override
            public void doOk() {
                isChange = true;
                tplEC.setReportWriteAttr(reportWritePane.update());
                if (hasActionPerformed) {
                    final JTemplate targetComponent = DesignerContext.getDesignerFrame().getSelectedJTemplate();
                    if (targetComponent != null) {
                        targetComponent.fireTargetModified();
                        targetComponent.requestFocus();
                    }
                }
            }

            @Override
            public void doCancel() {
                isChange = false;
            }
        });

        DesignerContext.setReportWritePane(dialog);
        dialog.setVisible(true);
        hasActionPerformed = true;
        return isChange;
    }
}