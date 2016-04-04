// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.file.export;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.io.exporter.*;
import com.fr.main.TemplateWorkBook;
import com.fr.report.core.ReportUtils;

// Referenced classes of package com.fr.design.actions.file.export:
//            AbstractExcelExportAction

public class PageExcelExportAction extends AbstractExcelExportAction
{

    public PageExcelExportAction(JWorkBook jworkbook)
    {
        super(jworkbook);
        setMenuKeySet(KeySetUtils.PAGE_EXCEL_EXPORT);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/excel.png"));
    }

    protected Exporter getExporter()
    {
        com.fr.main.impl.WorkBook workbook = getTemplateWorkBook();
        if(hasLayerReport(workbook))
            return new LargeDataPageExcelExporter(ReportUtils.getPaperSettingListFromWorkBook(workbook), true);
        else
            return new PageExcelExporter(ReportUtils.getPaperSettingListFromWorkBook(workbook));
    }
}
