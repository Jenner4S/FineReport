package com.fr.design.actions.cell;

import java.awt.event.ActionEvent;

import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.CellElementPropertyPane;

/**
 * ���е�CellAttributeTableAction����ָ��Ԫ�����Ա��,���˾��Զ���ת����Ԫ�����Ա�
 * 
 * @author zhou
 * @since 2012-5-23����4:19:48
 */
public abstract class CellAttributeTableAction extends UpdateAction {

	protected abstract String getID();

	@Override
	public void actionPerformed(ActionEvent e) {
		CellElementPropertyPane.getInstance().GoToPane(getID());
	}

}
