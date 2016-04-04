// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.module;

import com.fr.chart.base.ChartInternationalNameContentBean;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.actions.core.ActionUtils;
import com.fr.design.chart.ChartDialog;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.gui.ChartWidgetOption;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.mainframe.*;
import com.fr.form.ui.ChartEditor;
import com.fr.general.*;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.plugin.PluginReadListener;

// Referenced classes of package com.fr.design.module:
//            DesignModule, ChartHyperlinkGroup, ChartPreStyleAction, DesignModuleFactory

public class ChartDesignerModule extends DesignModule
{

    public ChartDesignerModule()
    {
    }

    public void start()
    {
        super.start();
        dealBeforeRegister();
        register();
        registerFloatEditor();
    }

    protected void dealBeforeRegister()
    {
        StableFactory.registerMarkedClass("ExtraChartDesignClassManagerProvider", com/fr/design/ChartTypeInterfaceManager);
    }

    private void register()
    {
        DesignModuleFactory.registerHyperlinkGroupType(new ChartHyperlinkGroup());
        GeneralContext.addPluginReadListener(new PluginReadListener() {

            final ChartDesignerModule this$0;

            public void success()
            {
                DesignModuleFactory.registerExtraWidgetOptions(options4Show());
            }

            
            {
                this$0 = ChartDesignerModule.this;
                super();
            }
        }
);
        DesignModuleFactory.registerChartEditorClass(com/fr/form/ui/ChartEditor);
        DesignModuleFactory.registerChartComponentClass(com/fr/design/chart/gui/ChartComponent);
        DesignModuleFactory.registerChartDialogClass(com/fr/design/chart/ChartDialog);
        DesignModuleFactory.registerChartAndWidgetPropertyPane(com/fr/design/mainframe/ChartAndWidgetPropertyPane);
        DesignModuleFactory.registerChartPropertyPaneClass(com/fr/design/mainframe/ChartPropertyPane);
        ActionUtils.registerChartPreStyleAction(new ChartPreStyleAction());
    }

    protected void registerFloatEditor()
    {
        ActionUtils.registerChartCollection(com/fr/chart/chartattr/ChartCollection);
    }

    public App[] apps4TemplateOpener()
    {
        return new App[0];
    }

    protected WidgetOption[] options4Show()
    {
        ChartInternationalNameContentBean achartinternationalnamecontentbean[] = ChartTypeManager.getInstance().getAllChartBaseNames();
        ChartWidgetOption achartwidgetoption[] = new ChartWidgetOption[achartinternationalnamecontentbean.length];
        for(int i = 0; i < achartinternationalnamecontentbean.length; i++)
        {
            String s = achartinternationalnamecontentbean[i].getPlotID();
            com.fr.chart.chartattr.Chart achart[] = ChartTypeManager.getInstance().getChartTypes(s);
            String s1 = ChartTypeInterfaceManager.getInstance().getIconPath(s);
            javax.swing.Icon icon = IOUtils.readIcon(s1);
            achartwidgetoption[i] = new ChartWidgetOption(Inter.getLocText(achartinternationalnamecontentbean[i].getName()), icon, com/fr/form/ui/ChartEditor, achart[0]);
        }

        return achartwidgetoption;
    }

    public String getInterNationalName()
    {
        return Inter.getLocText("FR-Chart-Design_ChartModule");
    }
}
