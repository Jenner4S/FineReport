// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.file.export;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.Inter;
import com.fr.io.exporter.*;
import com.fr.main.TemplateWorkBook;

// Referenced classes of package com.fr.design.actions.file.export:
//            AbstractExportAction

public class CSVExportAction extends AbstractExportAction
{

    public CSVExportAction(JWorkBook jworkbook)
    {
        super(jworkbook);
        setMenuKeySet(KeySetUtils.CSV_EXPORT);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/csv.png"));
    }

    protected Exporter getExporter()
    {
        com.fr.main.impl.WorkBook workbook = getTemplateWorkBook();
        if(hasLayerReport(workbook))
            return new LargeDataPageCSVExporter();
        else
            return new CSVExporter();
    }

    protected ChooseFileFilter getChooseFileFilter()
    {
        return new ChooseFileFilter(new String[] {
            "csv"
        }, Inter.getLocText("Export-CSV"));
    }

    protected String getDefaultExtension()
    {
        com.fr.main.impl.WorkBook workbook = getTemplateWorkBook();
        if(hasLayerReport(workbook))
            return "zip";
        else
            return "csv";
    }
}
