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
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;

// Referenced classes of package com.fr.design.actions.server:
//            StyleManagerPane

public class StyleListAction extends UpdateAction
{

    public static final MenuKeySet PREDEFINED_STYLES = new MenuKeySet() {

        public char getMnemonic()
        {
            return 'K';
        }

        public String getMenuName()
        {
            return Inter.getLocText("ServerM-Predefined_Styles");
        }

        public KeyStroke getKeyStroke()
        {
            return null;
        }

    }
;

    public StyleListAction()
    {
        setMenuKeySet(PREDEFINED_STYLES);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_web/style.png"));
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        com.fr.design.mainframe.DesignerFrame designerframe = DesignerContext.getDesignerFrame();
        final StyleManagerPane styleListPane = new StyleManagerPane();
        BasicDialog basicdialog = styleListPane.showWindow(designerframe);
        basicdialog.addDialogActionListener(new DialogActionAdapter() {

            final StyleManagerPane val$styleListPane;
            final StyleListAction this$0;

            public void doOk()
            {
                styleListPane.update(ConfigManager.getProviderInstance());
                Env env = FRContext.getCurrentEnv();
                try
                {
                    env.writeResource(ConfigManager.getProviderInstance());
                }
                catch(Exception exception)
                {
                    FRContext.getLogger().error(exception.getMessage(), exception);
                }
            }

            
            {
                this$0 = StyleListAction.this;
                styleListPane = stylemanagerpane;
                super();
            }
        }
);
        styleListPane.populate(ConfigManager.getProviderInstance());
        basicdialog.setVisible(true);
    }

    public void update()
    {
        setEnabled(true);
    }

}
