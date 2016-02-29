/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.edit.order;

import com.fr.base.BaseUtils;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.ElementCase;

/**
 * Send FloatElement backward.
 */
public class SendFloatElementBackwardAction extends AbstractFloatElementOrderAction {
    /**
     * Constructor
     */
	public SendFloatElementBackwardAction(ElementCasePane t) {
		super(t);
		
        this.setName(Inter.getLocText("M_Edit-Send_Backward"));
        this.setMnemonic('B');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/sendBackward.png"));
    }

	@Override
	public void orderWithSelectedFloatElement(ElementCase report,
			FloatElement floatElement) {
		report.sendFloatElementBackward(floatElement);
		
	}
}