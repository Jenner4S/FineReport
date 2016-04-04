// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.dialog.JWizardPanel;

public abstract class ChartWizardPane extends JWizardPanel
{

    public ChartWizardPane()
    {
    }

    public abstract void update(ChartCollection chartcollection);

    public abstract void populate(ChartCollection chartcollection);
}
