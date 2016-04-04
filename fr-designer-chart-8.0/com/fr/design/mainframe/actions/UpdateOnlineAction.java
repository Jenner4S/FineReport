// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.actions;

import com.fr.design.ChartEnvManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.chart.UpdateOnLinePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.json.JSONObject;
import com.fr.stable.ProductConstants;
import com.fr.stable.StringUtils;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;

// Referenced classes of package com.fr.design.mainframe.actions:
//            UpdateVersion

public class UpdateOnlineAction extends UpdateAction
{

    public UpdateOnlineAction()
    {
        setMenuKeySet(getKeySet());
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
    }

    private MenuKeySet getKeySet()
    {
        return new MenuKeySet() {

            final UpdateOnlineAction this$0;

            public char getMnemonic()
            {
                return 'U';
            }

            public String getMenuName()
            {
                return Inter.getLocText("FR-Chart-Help_UpdateOnline");
            }

            public KeyStroke getKeyStroke()
            {
                return null;
            }

            
            {
                this$0 = UpdateOnlineAction.this;
                super();
            }
        }
;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        (new UpdateVersion() {

            final UpdateOnlineAction this$0;

            protected void done()
            {
                try
                {
                    ChartEnvManager.getEnvManager().resetCheckDate();
                    JSONObject jsonobject = (JSONObject)get();
                    String s = jsonobject.getString("version");
                    UpdateOnLinePane updateonlinepane = new UpdateOnLinePane(StringUtils.isEmpty(s) ? ProductConstants.RELEASE_VERSION : s);
                    BasicDialog basicdialog = updateonlinepane.showWindow4UpdateOnline(DesignerContext.getDesignerFrame());
                    updateonlinepane.setParentDialog(basicdialog);
                    basicdialog.setVisible(true);
                }
                catch(Exception exception)
                {
                    FRLogger.getLogger().error(exception.getMessage());
                }
            }

            
            {
                this$0 = UpdateOnlineAction.this;
                super();
            }
        }
).execute();
    }
}
