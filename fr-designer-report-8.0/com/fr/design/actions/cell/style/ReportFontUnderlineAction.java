// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.cell.style;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.base.core.StyleUtils;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.FRFont;
import com.fr.general.Inter;

// Referenced classes of package com.fr.design.actions.cell.style:
//            ReportFontBoldAction

public class ReportFontUnderlineAction extends ReportFontBoldAction
{

    public ReportFontUnderlineAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setName(Inter.getLocText("FRFont-Underline"));
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/underline.png"));
    }

    protected void setSelectedFont(Style style)
    {
        this.style = StyleUtils.setReportFontUnderline(style, true);
    }

    protected void setUnselectedFont(Style style)
    {
        this.style = StyleUtils.setReportFontUnderline(style, false);
    }

    protected boolean isStyle(FRFont frfont)
    {
        return frfont.getUnderline() != 0;
    }
}
