// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.server;

import com.fr.base.*;
import com.fr.design.DesignModelAdapter;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.*;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.webattr.WidgetManagerPane;
import com.fr.form.ui.WidgetManager;
import com.fr.form.ui.WidgetManagerProvider;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;

public class WidgetManagerAction extends UpdateAction
{

    public static final MenuKeySet WIDGET_MANAGER = new MenuKeySet() {

        public char getMnemonic()
        {
            return 'W';
        }

        public String getMenuName()
        {
            return Inter.getLocText("ServerM-Widget_Manager");
        }

        public KeyStroke getKeyStroke()
        {
            return null;
        }

    }
;

    public WidgetManagerAction()
    {
        setMenuKeySet(WIDGET_MANAGER);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/modified.png"));
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        final DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
        final WidgetManagerProvider widgetManager = WidgetManager.getProviderInstance();
        final WidgetManagerPane widgetManagerPane = new WidgetManagerPane() {

            final WidgetManagerProvider val$widgetManager;
            final WidgetManagerAction this$0;

            public void complete()
            {
                populate(widgetManager);
            }

            
            {
                this$0 = WidgetManagerAction.this;
                widgetManager = widgetmanagerprovider;
                super();
            }
        }
;
        BasicDialog basicdialog = widgetManagerPane.showLargeWindow(designerFrame, new DialogActionAdapter() {

            final WidgetManagerPane val$widgetManagerPane;
            final WidgetManagerProvider val$widgetManager;
            final DesignerFrame val$designerFrame;
            final WidgetManagerAction this$0;

            public void doOk()
            {
                widgetManagerPane.update(widgetManager);
                Env env = FRContext.getCurrentEnv();
                try
                {
                    env.writeResource(widgetManager);
                }
                catch(Exception exception)
                {
                    FRContext.getLogger().error(exception.getMessage(), exception);
                }
                DesignModelAdapter designmodeladapter = DesignModelAdapter.getCurrentModelAdapter();
                if(designmodeladapter != null)
                    designmodeladapter.widgetConfigChanged();
                designerFrame.getSelectedJTemplate().refreshToolArea();
            }

            
            {
                this$0 = WidgetManagerAction.this;
                widgetManagerPane = widgetmanagerpane;
                widgetManager = widgetmanagerprovider;
                designerFrame = designerframe;
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
