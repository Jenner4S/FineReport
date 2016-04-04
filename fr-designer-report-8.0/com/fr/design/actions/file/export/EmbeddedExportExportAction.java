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
import com.fr.io.exporter.EmbeddedTableDataExporter;
import com.fr.io.exporter.Exporter;

// Referenced classes of package com.fr.design.actions.file.export:
//            AbstractExportAction

public class EmbeddedExportExportAction extends AbstractExportAction
{

    public EmbeddedExportExportAction(JWorkBook jworkbook)
    {
        super(jworkbook);
        setMenuKeySet(KeySetUtils.EMBEDDED_EXPORT);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/oem/logo.png"));
    }

    protected Exporter getExporter()
    {
        return new EmbeddedTableDataExporter();
    }

    protected ChooseFileFilter getChooseFileFilter()
    {
        return new ChooseFileFilter(new String[] {
            "cpt"
        }, Inter.getLocText("Export-Template(embedded_data)"));
    }

    protected String getDefaultExtension()
    {
        return "cpt";
    }
}
