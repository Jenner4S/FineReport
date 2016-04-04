// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.design.beans.BasicBeanPane;
import com.fr.report.web.Location;
import com.fr.report.web.ToolBarManager;
import com.fr.stable.ArrayUtils;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.webattr:
//            ToolBarPane

public abstract class WidgetToolBarPane extends BasicBeanPane
{

    protected ToolBarPane northToolBar;
    protected ToolBarPane southToolBar;
    protected ToolBarManager defaultToolBar;

    public WidgetToolBarPane()
    {
    }

    public void setDefaultToolBar(ToolBarManager toolbarmanager)
    {
        defaultToolBar = toolbarmanager;
    }

    public void populateBean(ToolBarManager atoolbarmanager[])
    {
        if(ArrayUtils.isEmpty(atoolbarmanager))
            return;
        for(int i = 0; i < atoolbarmanager.length; i++)
        {
            Location location = atoolbarmanager[i].getToolBarLocation();
            if(!(location instanceof com.fr.report.web.Location.Embed))
                continue;
            if(((com.fr.report.web.Location.Embed)location).getPosition() == 1)
            {
                northToolBar.populateBean(atoolbarmanager[i].getToolBar());
                continue;
            }
            if(((com.fr.report.web.Location.Embed)location).getPosition() == 3)
                southToolBar.populateBean(atoolbarmanager[i].getToolBar());
        }

    }

    public ToolBarManager[] updateBean()
    {
        ArrayList arraylist = new ArrayList();
        if(!northToolBar.isEmpty())
        {
            ToolBarManager toolbarmanager = new ToolBarManager();
            toolbarmanager.setToolBar(northToolBar.updateBean());
            toolbarmanager.setToolBarLocation(Location.createTopEmbedLocation());
            arraylist.add(toolbarmanager);
        }
        if(!southToolBar.isEmpty())
        {
            ToolBarManager toolbarmanager1 = new ToolBarManager();
            toolbarmanager1.setToolBar(southToolBar.updateBean());
            toolbarmanager1.setToolBarLocation(Location.createBottomEmbedLocation());
            arraylist.add(toolbarmanager1);
        }
        return (ToolBarManager[])arraylist.toArray(new ToolBarManager[arraylist.size()]);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((ToolBarManager[])obj);
    }
}
