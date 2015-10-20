package com.fr.design.actions.edit.order;

import com.fr.design.actions.FloatSelectionAction;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.FloatSelection;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.ElementCase;

/**
 * FloatElement order.
 */
public abstract class AbstractFloatElementOrderAction extends FloatSelectionAction {
	protected AbstractFloatElementOrderAction(ElementCasePane t) {
		super(t);
	}
	
    @Override
    public boolean executeActionReturnUndoRecordNeededWithFloatSelection(FloatSelection fs) {
        ElementCasePane ePane = getEditingComponent();

        ElementCase report = ePane.getEditingElementCase();

        //p:������һ��ѡ�е�����Ԫ��.
        FloatElement selectedFloatElement = report.getFloatElement(fs.getSelectedFloatName());
        orderWithSelectedFloatElement(report, selectedFloatElement);

        return true;
    }

    public abstract void orderWithSelectedFloatElement(ElementCase report, FloatElement floatElement);
}
