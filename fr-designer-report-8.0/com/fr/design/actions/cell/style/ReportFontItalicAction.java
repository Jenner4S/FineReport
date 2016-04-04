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

public class ReportFontItalicAction extends ReportFontBoldAction
{

    public ReportFontItalicAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setName(Inter.getLocText("FRFont-italic"));
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/italic.png"));
    }

    protected void setSelectedFont(Style style)
    {
        this.style = StyleUtils.italicReportFont(style);
    }

    protected void setUnselectedFont(Style style)
    {
        this.style = StyleUtils.unItalicReportFont(style);
    }

    protected boolean isStyle(FRFont frfont)
    {
        return frfont.isItalic();
    }
}
