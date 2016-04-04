// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.file.export;

import com.fr.base.ExcelUtils;
import com.fr.design.mainframe.JWorkBook;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.Inter;
import com.fr.main.TemplateWorkBook;

// Referenced classes of package com.fr.design.actions.file.export:
//            AbstractExportAction

public abstract class AbstractExcelExportAction extends AbstractExportAction
{

    protected AbstractExcelExportAction(JWorkBook jworkbook)
    {
        super(jworkbook);
    }

    protected ChooseFileFilter getChooseFileFilter()
    {
        com.fr.main.impl.WorkBook workbook = getTemplateWorkBook();
        if(hasLayerReport(workbook))
            return new ChooseFileFilter(new String[] {
                "zip"
            }, "ZIP");
        else
            return new ChooseFileFilter(new String[] {
                "xls", "xlsx"
            }, Inter.getLocText("Export-Excel"));
    }

    protected String getDefaultExtension()
    {
        com.fr.main.impl.WorkBook workbook = getTemplateWorkBook();
        if(hasLayerReport(workbook))
            return "zip";
        else
            return ExcelUtils.checkPOIJarExist() ? "xlsx" : "xls";
    }
}
