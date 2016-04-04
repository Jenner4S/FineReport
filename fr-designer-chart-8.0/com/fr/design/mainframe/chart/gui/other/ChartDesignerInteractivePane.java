// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.other;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.mainframe.chart.gui.ChartOtherPane;
import com.fr.general.Inter;
import com.fr.js.JavaScriptImpl;
import com.fr.js.WebHyperlink;
import com.fr.third.org.hsqldb.lib.HashMap;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.other:
//            ChartInteractivePane

public class ChartDesignerInteractivePane extends ChartInteractivePane
{

    public ChartDesignerInteractivePane(ChartOtherPane chartotherpane)
    {
        super(chartotherpane);
    }

    protected List refreshList(HashMap hashmap)
    {
        ArrayList arraylist = new ArrayList();
        arraylist.add(new UIMenuNameableCreator(Inter.getLocText("Hyperlink-Web_link"), new WebHyperlink(), getUseMap(hashmap, com/fr/js/WebHyperlink)));
        arraylist.add(new UIMenuNameableCreator("JavaScript", new JavaScriptImpl(), getUseMap(hashmap, com/fr/js/JavaScriptImpl)));
        arraylist.add(new UIMenuNameableCreator(Inter.getLocText("RelatedChart"), null, null));
        return arraylist;
    }

    protected void populateAutoRefresh(Chart chart)
    {
        super.populateAutoRefresh(chart);
        if(chart.getFilterDefinition() != null)
        {
            TopDefinition topdefinition = (TopDefinition)chart.getFilterDefinition();
            isAutoRefresh.setEnabled(topdefinition.isSupportAutoRefresh());
            if(!isAutoRefresh.isEnabled())
                isAutoRefresh.setSelected(false);
            autoRefreshTime.setEnabled(topdefinition.isSupportAutoRefresh());
        }
    }
}
