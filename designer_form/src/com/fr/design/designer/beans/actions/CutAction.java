package com.fr.design.designer.beans.actions;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.fr.base.BaseUtils;
import com.fr.general.Inter;
import com.fr.design.mainframe.FormDesigner;

public class CutAction extends FormEditAction {

	public CutAction(FormDesigner t) {
		super(t);
		this.setName(Inter.getLocText("M_Edit-Cut"));
		this.setMnemonic('T');
		this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/cut.png"));
		this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
	}

	@Override
	public boolean executeActionReturnUndoRecordNeeded() {
		FormDesigner editPane = getEditingComponent();
		if (editPane == null) {
			return false;
		}
		return editPane.cut();
	}

}
