// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart;

import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.chart.gui.ChartDesignerDataPane;
import com.fr.design.mainframe.chart.gui.ChartDesignerOtherPane;
import com.fr.design.mainframe.chart.gui.StylePane4Chart;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart:
//            ChartEditPane

public class ChartDesignEditPane extends ChartEditPane
{

    private static ChartDesignEditPane instance;
    private boolean isFromToolBar;

    public static synchronized ChartEditPane getInstance()
    {
        if(instance == null)
            instance = new ChartDesignEditPane();
        return instance;
    }

    public ChartDesignEditPane()
    {
        isFromToolBar = false;
        paneList = new ArrayList();
        dataPane4SupportCell = new ChartDesignerDataPane(listener);
        paneList.add(dataPane4SupportCell);
        paneList.add(new StylePane4Chart(listener, false));
        paneList.add(new ChartDesignerOtherPane());
        createTabsPane();
    }

    protected void dealWithStyleChange()
    {
        if(!isFromToolBar)
            HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().styleChange();
    }

    public void styleChange(boolean flag)
    {
        isFromToolBar = flag;
    }
}
