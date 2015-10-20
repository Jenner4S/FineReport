package com.fr.design.actions.server;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.utils.DesignUtils;
import com.fr.general.Inter;
import com.fr.general.web.ParameterConsts;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PlatformManagerAction extends UpdateAction {
	public PlatformManagerAction() {
        this.setMenuKeySet(PLATEFORM_MANAGER);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
		this.setSmallIcon(BaseUtils.readIcon("/com/fr/web/images/platform/platform_16_16.png"));
	}

    /**
     * ����
     * @param evt �¼�
     */
	public void actionPerformed(ActionEvent evt) {
		DesignUtils.visitEnvServerByParameters(
				new String[] {ParameterConsts.OP}, 
				new String[] {"fr_platform"}
		);
	}

	public static final MenuKeySet PLATEFORM_MANAGER = new MenuKeySet() {
		@Override
		public char getMnemonic() {
			return 'P';
		}

		@Override
		public String getMenuName() {
			return Inter.getLocText("M_Server-Platform_Manager");
		}

		@Override
		public KeyStroke getKeyStroke() {
			return null;
		}
	};
}
