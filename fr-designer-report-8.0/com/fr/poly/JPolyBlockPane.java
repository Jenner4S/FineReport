// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly;

import com.fr.page.ReportSettingsProvider;
import com.fr.poly.creator.PolyElementCasePane;
import com.fr.report.poly.PolyECBlock;
import com.fr.report.stable.ReportSettings;

public class JPolyBlockPane extends PolyElementCasePane
{

    public JPolyBlockPane(PolyECBlock polyecblock)
    {
        super(polyecblock);
        setHorizontalScrollBarVisible(false);
        setVerticalScrollBarVisible(false);
        setColumnHeaderVisible(false);
        setRowHeaderVisible(false);
        setSelection(null);
    }

    public ReportSettingsProvider getReportSettings()
    {
        return new ReportSettings();
    }
}
