// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.editor;

import com.fr.base.Style;
import com.fr.base.TextFormat;
import com.fr.report.ReportHelper;
import com.fr.report.cell.FloatElement;

// Referenced classes of package com.fr.design.cell.editor:
//            TextFloatEditor

public class GeneralFloatEditor extends TextFloatEditor
{

    public GeneralFloatEditor()
    {
    }

    public Object getFloatEditorValue()
        throws Exception
    {
        Object obj = super.getFloatEditorValue();
        FloatElement floatelement = getFloatElement();
        Style style = floatelement.getStyle();
        if(style != null && style.getFormat() != null && style.getFormat() == TextFormat.getInstance())
            return obj;
        else
            return ReportHelper.convertGeneralStringAccordingToExcel(obj);
    }
}
