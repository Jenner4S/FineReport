/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.edit;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.fr.base.BaseUtils;
import com.fr.design.actions.TemplateComponentAction;
import com.fr.design.designer.TargetComponent;
import com.fr.general.Inter;

/**
 * Cut.
 */
public class CutAction extends TemplateComponentAction {
    /**
     * Constructor
     */
	public CutAction(TargetComponent t) {
    	super(t);
    	
        this.setName(Inter.getLocText("M_Edit-Cut"));
        this.setMnemonic('T');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/cut.png"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        TargetComponent editPane = getEditingComponent();
        if (editPane == null) {
            return false;
        }
        return editPane.cut();
    }
}
