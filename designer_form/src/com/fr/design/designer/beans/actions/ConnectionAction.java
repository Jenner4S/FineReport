package com.fr.design.designer.beans.actions;

import java.awt.event.ActionEvent;

import com.fr.base.BaseUtils;
import com.fr.design.actions.ToggleButtonUpdateAction;
import com.fr.design.actions.UpdateAction;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.mainframe.FormDesigner;
import com.fr.general.Inter;
import com.fr.design.utils.gui.GUICoreUtils;

//marro : �����߰�ť��Ŀǰ�ò����������������š�2012-3-26
public class ConnectionAction extends UpdateAction implements ToggleButtonUpdateAction {
	private FormDesigner fd;

	public ConnectionAction(FormDesigner fd) {
		this.fd = fd;
		this.setName(Inter.getLocText("Connectionline"));
		this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/toolbarbtn/connector.png"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		UIToggleButton toggleButton = this.createToolBarComponent();
		fd.setDrawLineMode(toggleButton.isSelected());
	}

	@Override
	public UIToggleButton createToolBarComponent() {
		return GUICoreUtils.createToolBarComponent(this);
	}

}
