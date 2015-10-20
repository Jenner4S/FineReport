package com.fr.design.actions.cell;

import com.fr.design.actions.CellSelectionAction;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.GridUtils;
import com.fr.grid.selection.CellSelection;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.core.SheetUtils;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.stable.ReportConstants;

// TODO ALEX_SEP �������AbstractCellAction��ʲô��ϵ?
public abstract class AbstractCellElementAction extends CellSelectionAction {
	protected AbstractCellElementAction(ElementCasePane t) {
		super(t);
	}
	
	@Override
	protected boolean executeActionReturnUndoRecordNeededWithCellSelection(CellSelection cs) {
		final ElementCasePane ePane = this.getEditingComponent();
		final TemplateElementCase tplEC = ePane.getEditingElementCase();
		TemplateCellElement editCellElement = tplEC.getTemplateCellElement(cs.getColumn(), cs.getRow());
		if (editCellElement == null) {
			editCellElement = new DefaultTemplateCellElement(cs.getColumn(), cs.getRow());
			tplEC.addCellElement(editCellElement);
		}
		if (tplEC != null) {
			SheetUtils.calculateDefaultParent(tplEC);
		}
		final CellSelection finalCS = cs;
		final BasicPane bp = populateBasicPane(editCellElement);
		BasicDialog dialog = bp.showWindow(DesignerContext.getDesignerFrame());
		dialog.addDialogActionListener(new DialogActionAdapter() {
			@Override
			public void doOk() {
				// ��Ҫ���к��е�������Ԫ�ء�
				for (int j = 0; j < finalCS.getRowSpan(); j++) {
					for (int i = 0; i < finalCS.getColumnSpan(); i++) {
						int column = i + finalCS.getColumn();
						int row = j + finalCS.getRow();
						TemplateCellElement editCellElement = tplEC.getTemplateCellElement(column, row);
						if (editCellElement == null) {
							editCellElement = new DefaultTemplateCellElement(column, row);
							tplEC.addCellElement(editCellElement);
						}
						// alex:������һ�仰�ᵼ�¿��п��еĸ��ӱ����update
						if (editCellElement.getColumn() != column || editCellElement.getRow() != row) {
							continue;
						}
						updateBasicPane(bp, editCellElement);
						// update cell attributes
						if (isNeedShinkToFit()) {
							// shink to fit.(���value��String)
							Object editElementValue = editCellElement.getValue();
							if (editElementValue != null && (editElementValue instanceof String || editElementValue instanceof Number)) {
								// TODO ALEX_SEP ��ʱ��FIT_DEFAULT���,��ȡreportsetting���������,��ΪҲ��֪����Ӧ�÷���report���滹��elementcase����
								GridUtils.shrinkToFit(ReportConstants.AUTO_SHRINK_TO_FIT_DEFAULT, tplEC, editCellElement);
							}
						}
					}
				}
				ePane.fireTargetModified();
			}
		});
        //�ؼ����ü�סdlg���ύ���������ӵ�Ԫ������show����
        DesignerContext.setReportWritePane(dialog);
		dialog.setVisible(true);
		return false;
	}

	/**
	 * ��ʼ���Ի���
	 * 
	 * @param cellElement
	 * @return
	 */
	protected abstract BasicPane populateBasicPane(TemplateCellElement cellElement);

	/**
	 * ���¶Ի���֮�󣬸ı�ֵ
	 * 
	 * @param cellElement
	 * @return
	 */
	protected abstract void updateBasicPane(BasicPane basicPane, TemplateCellElement cellElement);

	/**
	 * if isNeedShinkToFit��please override this method
	 * 
	 * @return isNeedShinkToFit
	 */
	protected boolean isNeedShinkToFit() {
		return false;
	}
}
