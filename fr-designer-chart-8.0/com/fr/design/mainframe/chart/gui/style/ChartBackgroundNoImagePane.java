// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style;

import com.fr.design.gui.style.GradientPane;
import com.fr.design.mainframe.backgroundpane.ColorBackgroundPane;
import com.fr.design.mainframe.backgroundpane.NullBackgroundPane;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style:
//            ChartBackgroundPane

public class ChartBackgroundNoImagePane extends ChartBackgroundPane
{

    public ChartBackgroundNoImagePane()
    {
    }

    protected void initList()
    {
        paneList.add(new NullBackgroundPane());
        paneList.add(new ColorBackgroundPane());
        paneList.add(new GradientPane(150));
    }
}
