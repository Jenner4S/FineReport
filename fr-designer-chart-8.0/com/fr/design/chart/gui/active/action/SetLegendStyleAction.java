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

public class SetLegendStyleAction extends ChartComponentAction
{

    private static final long serialVersionUID = 0x2d25a786c99eba6eL;

    public SetLegendStyleAction(ChartComponent chartcomponent)
    {
        super(chartcomponent);
        setName(Inter.getLocText("Set_Legend_Sytle"));
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        showLegendStylePane();
    }

    public void showLegendStylePane()
    {
        ChartEditPane.getInstance().GoToPane(new String[] {
            PaneTitleConstants.CHART_STYLE_TITLE, PaneTitleConstants.CHART_STYLE_LEGNED_TITLE
        });
    }
}
