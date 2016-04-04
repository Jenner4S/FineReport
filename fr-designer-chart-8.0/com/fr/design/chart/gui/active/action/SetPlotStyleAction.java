// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.gui.active.action;

import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.general.Inter;
import java.awt.event.ActionEvent;

// Referenced classes of package com.fr.design.chart.gui.active.action:
//            ChartComponentAction

public class SetPlotStyleAction extends ChartComponentAction
{

    private static final long serialVersionUID = 0x282a01b07a3de844L;

    public SetPlotStyleAction(ChartComponent chartcomponent)
    {
        super(chartcomponent);
        setName(Inter.getLocText(new String[] {
            "Set", "ChartF-Plot"
        }));
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        showPlotPane();
    }

    public void showPlotPane()
    {
        ChartEditPane.getInstance().GoToPane(new String[] {
            PaneTitleConstants.CHART_STYLE_TITLE, PaneTitleConstants.CHART_STYLE_AREA_TITLE, PaneTitleConstants.CHART_STYLE_AREA_PLOT_TITLE
        });
    }
}
