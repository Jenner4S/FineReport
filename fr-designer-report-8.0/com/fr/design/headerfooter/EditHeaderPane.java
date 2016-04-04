// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.headerfooter;

import com.fr.general.Inter;
import com.fr.page.ReportSettingsProvider;
import com.fr.stable.unit.UNIT;

// Referenced classes of package com.fr.design.headerfooter:
//            HeaderFooterPane

public class EditHeaderPane extends HeaderFooterPane
{

    public EditHeaderPane()
    {
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("HF-Edit_Header");
    }

    public void populate(ReportSettingsProvider reportsettingsprovider)
    {
        super.populate(reportsettingsprovider, true);
    }

    public UNIT updateReportSettings()
    {
        return super.updateReportSettings();
    }
}
