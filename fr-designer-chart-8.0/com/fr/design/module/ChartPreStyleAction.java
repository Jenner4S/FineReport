// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.module;

import com.fr.base.*;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;

// Referenced classes of package com.fr.design.module:
//            ChartPreStyleManagerPane

public class ChartPreStyleAction extends UpdateAction
{

    public static final MenuKeySet CHART_DEFAULT_STYLE = new MenuKeySet() {

        public char getMnemonic()
        {
            return 'C';
        }

        public String getMenuName()
        {
            return Inter.getLocText("FR-Menu-Server_Chart_PreStyle");
        }

        public KeyStroke getKeyStroke()
        {
            return null;
        }

    }
;

    public ChartPreStyleAction()
    {
        setMenuKeySet(CHART_DEFAULT_STYLE);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("com/fr/design/images/chart/ChartType.png"));
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        com.fr.design.mainframe.DesignerFrame designerframe = DesignerContext.getDesignerFrame();
        final ChartPreStyleManagerPane pane = new ChartPreStyleManagerPane();
        BasicDialog basicdialog = pane.showWindow(designerframe);
        basicdialog.addDialogActionListener(new DialogActionAdapter() {

            final ChartPreStyleManagerPane val$pane;
            final ChartPreStyleAction this$0;

            public void doOk()
            {
                pane.updateBean();
                ChartPreStyleManagerProvider chartprestylemanagerprovider = ChartPreStyleServerManager.getProviderInstance();
                chartprestylemanagerprovider.setStyleEditing(null);
            }

            public void doCancel()
            {
                ChartPreStyleManagerProvider chartprestylemanagerprovider = ChartPreStyleServerManager.getProviderInstance();
                chartprestylemanagerprovider.setStyleEditing(null);
            }

            
            {
                this$0 = ChartPreStyleAction.this;
                pane = chartprestylemanagerpane;
                super();
            }
        }
);
        pane.populateBean();
        basicdialog.setVisible(true);
    }

}
