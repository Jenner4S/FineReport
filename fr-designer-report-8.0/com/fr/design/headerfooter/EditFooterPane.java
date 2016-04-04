// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.headerfooter;

import com.fr.general.Inter;
import com.fr.report.stable.ReportSettings;
import com.fr.stable.unit.UNIT;

// Referenced classes of package com.fr.design.headerfooter:
//            HeaderFooterPane

public class EditFooterPane extends HeaderFooterPane
{

    public EditFooterPane()
    {
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("HF-Edit_Footer");
    }

    public void populate(ReportSettings reportsettings)
    {
        super.populate(reportsettings, false);
    }

    public UNIT updateReportSettings()
    {
        return super.updateReportSettings();
    }
}
