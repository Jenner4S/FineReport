package com.fr.design.actions;

//ElementCaseActionӦ����GridSelectionChangeListener���ʹ�����Ԫ�غ͵�Ԫ���������ͱ������ˣ������ж���ЩElementCaseAction�Ƿ���Ա༭,��Ȼ��������Щ��������
//

import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;

public abstract class ElementCaseAction extends TemplateComponentAction<ElementCasePane> {
	protected ElementCaseAction(ElementCasePane t) {
		super(t);
		t.addSelectionChangeListener(new SelectionListener() {

			@Override
			public void selectionChanged(SelectionEvent e) {
				update();
				if (DesignerContext.getFormatState() != DesignerContext.FORMAT_STATE_NULL) {
					Selection selection = getEditingComponent().getSelection();
					if (selection instanceof CellSelection) {
						CellSelection cellselection = (CellSelection) selection;
						//��ʽ����
						getEditingComponent().setCellNeedTOFormat(cellselection);
					}
				}
			}
		});
	}

}
