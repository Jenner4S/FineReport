// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.ChartEnvManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.mainframe.actions.UpdateVersion;
import com.fr.design.mainframe.chart.UpdateOnLinePane;
import com.fr.design.mainframe.toolbar.ToolBarMenuDock;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.json.JSONObject;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

// Referenced classes of package com.fr.design.mainframe:
//            DesignerFrame, DesignerContext

public class DesignerFrame4Chart extends DesignerFrame
{

    public DesignerFrame4Chart(ToolBarMenuDock toolbarmenudock)
    {
        super(toolbarmenudock);
    }

    protected ArrayList getFrameListeners()
    {
        ArrayList arraylist = super.getFrameListeners();
        arraylist.add(0, new WindowAdapter() {

            final DesignerFrame4Chart this$0;

            public void windowOpened(WindowEvent windowevent)
            {
                super.windowOpened(windowevent);
                judgeFirstUseWhenStart();
            }

            
            {
                this$0 = DesignerFrame4Chart.this;
                super();
            }
        }
);
        return arraylist;
    }

    public void exit()
    {
        ChartEnvManager.getEnvManager().saveXMLFile();
        super.exit();
    }

    protected void laoyoutWestPane()
    {
    }

    protected void judgeFirstUseWhenStart()
    {
        boolean flag = ChartEnvManager.getEnvManager().isPushUpdateAuto() || ChartEnvManager.getEnvManager().isOverOneMonth();
        if(!StableUtils.checkDesignerActive(ChartEnvManager.getEnvManager().getActivationKey()) || flag)
        {
            ChartEnvManager.getEnvManager().setActivationKey("RXWY-A25421-K58F47757-7373");
            checkVersion();
            if(ChartEnvManager.getEnvManager().isOverOneMonth())
                ChartEnvManager.getEnvManager().resetCheckDate();
        }
    }

    private void checkVersion()
    {
        (new UpdateVersion() {

            final DesignerFrame4Chart this$0;

            protected void done()
            {
                try
                {
                    JSONObject jsonobject = (JSONObject)get();
                    String s = jsonobject.getString("version");
                    if(!ComparatorUtils.equals(ProductConstants.RELEASE_VERSION, s))
                    {
                        UpdateOnLinePane updateonlinepane = new UpdateOnLinePane(s);
                        BasicDialog basicdialog = updateonlinepane.showWindow4UpdateOnline(DesignerContext.getDesignerFrame());
                        updateonlinepane.setParentDialog(basicdialog);
                        basicdialog.setVisible(true);
                    }
                }
                catch(Exception exception)
                {
                    FRLogger.getLogger().error(exception.getMessage());
                }
            }

            
            {
                this$0 = DesignerFrame4Chart.this;
                super();
            }
        }
).execute();
    }
}
