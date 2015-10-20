/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.server;

import com.fr.base.BaseUtils;
import com.fr.base.ConfigManager;
import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.webattr.EditReportServerParameterPane;
import com.fr.general.Inter;
import com.fr.base.ConfigManagerProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Server Config Manager
 */
public class ServerConfigManagerAction extends UpdateAction {
    public ServerConfigManagerAction() {
        this.setMenuKeySet(SERVER_CONFIG_MANAGER);
        this.setName(getMenuKeySet().getMenuKeySetName()+ "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_web/edit.png"));
    }

    /**
     * ����
     * @param e �¼�
     */
    public void actionPerformed(ActionEvent e) {
        final ConfigManagerProvider configManager = ConfigManager.getProviderInstance();
        final EditReportServerParameterPane editReportServerParameterPane = new EditReportServerParameterPane() {
            @Override
			public void complete() {
                populate(configManager);
            }
        };

        final BasicDialog editReportServerParameterDialog = editReportServerParameterPane.showWindow(
        	DesignerContext.getDesignerFrame()
        );

        editReportServerParameterDialog.addDialogActionListener(new DialogActionAdapter() {
            @Override
			public void doOk() {
                editReportServerParameterPane.update(configManager);
                Env currentEnv = FRContext.getCurrentEnv();
                try {
                    currentEnv.writeResource(configManager);
                } catch (Exception ex) {
                    FRContext.getLogger().error(ex.getMessage(), ex);
                }
            }
        });
        editReportServerParameterDialog.setVisible(true);
    }

    @Override
	public void update() {
        this.setEnabled(true);
    }

    public static final MenuKeySet SERVER_CONFIG_MANAGER = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'M';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Server-Server_Config_Manager");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}
