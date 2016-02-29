/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.server;

import com.fr.base.BaseUtils;
import com.fr.base.ConfigManager;
import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.DesignModelAdapter;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.parameter.ParameterManagerPane;
import com.fr.general.Inter;
import com.fr.base.ConfigManagerProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * Parameter dialog
 */
public class GlobalParameterAction extends UpdateAction {
    public GlobalParameterAction() {
        this.setMenuKeySet(GLOBAL_PARAMETER);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/p.png"));
    }

    /**
     * 动作
     *
     * @param e 事件
     */
    public void actionPerformed(ActionEvent e) {
        final DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
        final ParameterManagerPane parameterManagerPane = new ParameterManagerPane();

        final BasicDialog parameterManagerDialog = parameterManagerPane.showWindow(designerFrame);

        //marks:读取服务器配置信息
        final ConfigManagerProvider configManager = ConfigManager.getProviderInstance();

        parameterManagerPane.populate(configManager);
        parameterManagerDialog.addDialogActionListener(new DialogActionAdapter() {
            public void doOk() {
                //apply new parameter list.
                parameterManagerPane.update(configManager);
                //marks:保存数据
                Env currentEnv = FRContext.getCurrentEnv();
                try {
                    currentEnv.writeResource(configManager);
                } catch (Exception ex) {
                    FRContext.getLogger().error(ex.getMessage(), ex);
                }
                DesignModelAdapter<?, ?> model = DesignModelAdapter.getCurrentModelAdapter();
                if (model != null) {
                    model.parameterChanged();
                }
                parameterManagerDialog.setDoOKSucceed(!parameterManagerPane.isContainsRename());
            }
        });
        parameterManagerDialog.setModal(true);
        parameterManagerDialog.setVisible(true);
    }

    public void update() {
        this.setEnabled(true);
    }

    public static final MenuKeySet GLOBAL_PARAMETER = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'G';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Server-Global_Parameters");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}