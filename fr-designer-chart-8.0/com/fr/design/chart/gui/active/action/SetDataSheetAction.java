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

public class SetDataSheetAction extends ChartComponentAction
{

    private static final long serialVersionUID = 0xbde346797a2b3466L;

    public SetDataSheetAction(ChartComponent chartcomponent)
    {
        super(chartcomponent);
        setName(Inter.getLocText("Chart_Set_Data_Sheet"));
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        showDataSheetStylePane();
    }

    public void showDataSheetStylePane()
    {
        ChartEditPane.getInstance().GoToPane(new String[] {
            PaneTitleConstants.CHART_STYLE_TITLE, PaneTitleConstants.CHART_STYLE_DATA_TITLE
        });
    }
}
