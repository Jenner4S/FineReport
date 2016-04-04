// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.server;

import com.fr.base.*;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.webattr.EditReportServerParameterPane;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;

public class ServerConfigManagerAction extends UpdateAction
{

    public static final MenuKeySet SERVER_CONFIG_MANAGER = new MenuKeySet() {

        public char getMnemonic()
        {
            return 'M';
        }

        public String getMenuName()
        {
            return Inter.getLocText("M_Server-Server_Config_Manager");
        }

        public KeyStroke getKeyStroke()
        {
            return null;
        }

    }
;

    public ServerConfigManagerAction()
    {
        setMenuKeySet(SERVER_CONFIG_MANAGER);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_web/edit.png"));
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        final ConfigManagerProvider configManager = ConfigManager.getProviderInstance();
        final EditReportServerParameterPane editReportServerParameterPane = new EditReportServerParameterPane() {

            final ConfigManagerProvider val$configManager;
            final ServerConfigManagerAction this$0;

            public void complete()
            {
                populate(configManager);
            }

            
            {
                this$0 = ServerConfigManagerAction.this;
                configManager = configmanagerprovider;
                super();
            }
        }
;
        BasicDialog basicdialog = editReportServerParameterPane.showWindow(DesignerContext.getDesignerFrame());
        basicdialog.addDialogActionListener(new DialogActionAdapter() {

            final EditReportServerParameterPane val$editReportServerParameterPane;
            final ConfigManagerProvider val$configManager;
            final ServerConfigManagerAction this$0;

            public void doOk()
            {
                editReportServerParameterPane.update(configManager);
                Env env = FRContext.getCurrentEnv();
                try
                {
                    env.writeResource(configManager);
                }
                catch(Exception exception)
                {
                    FRContext.getLogger().error(exception.getMessage(), exception);
                }
            }

            
            {
                this$0 = ServerConfigManagerAction.this;
                editReportServerParameterPane = editreportserverparameterpane;
                configManager = configmanagerprovider;
                super();
            }
        }
);
        basicdialog.setVisible(true);
    }

    public void update()
    {
        setEnabled(true);
    }

}
