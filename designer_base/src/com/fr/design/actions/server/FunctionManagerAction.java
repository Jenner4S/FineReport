/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.server;

import com.fr.base.BaseUtils;
import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FunctionManagerPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.menu.MenuKeySet;
import com.fr.file.FunctionManager;
import com.fr.file.FunctionManagerProvider;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * FunctionManager.
 */
public class FunctionManagerAction extends UpdateAction {
    public FunctionManagerAction() {
        this.setMenuKeySet(FUNCTION_MANAGER);
        this.setName(getMenuKeySet().getMenuKeySetName()+"...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_web/function.png"));
    }

    /**
     * ����
     * @param evt �¼�
     */
    public void actionPerformed(ActionEvent evt) {
    	final FunctionManagerPane functionManagerPane = new FunctionManagerPane();
        BasicDialog functionManagerDialog =
        	functionManagerPane.showWindow(
        			DesignerContext.getDesignerFrame(),null);
        final FunctionManagerProvider functionManager = FunctionManager.getProviderInstance();
        functionManagerDialog.addDialogActionListener(new DialogActionAdapter() {
			public void doOk() {
				functionManagerPane.update(functionManager);
				Env currentEnv = FRContext.getCurrentEnv();
				try {
					currentEnv.writeResource(functionManager);
				} catch (Exception e) {
					FRContext.getLogger().error(e.getMessage(), e);
				}
			}                
        });
        functionManagerPane.populate(functionManager);
        functionManagerDialog.setVisible(true);
    }
    
    public void update() {
		this.setEnabled(true);
	}

    public static final MenuKeySet FUNCTION_MANAGER = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'F';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Server-Function_Manager");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}
