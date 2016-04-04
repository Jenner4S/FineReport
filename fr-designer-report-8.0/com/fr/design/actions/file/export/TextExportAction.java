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
import com.fr.io.exporter.Exporter;
import com.fr.io.exporter.TextExporter;

// Referenced classes of package com.fr.design.actions.file.export:
//            AbstractExportAction

public class TextExportAction extends AbstractExportAction
{

    public TextExportAction(JWorkBook jworkbook)
    {
        super(jworkbook);
        setMenuKeySet(KeySetUtils.TEXT_EXPORT);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/text.png"));
    }

    protected Exporter getExporter()
    {
        return new TextExporter();
    }

    protected ChooseFileFilter getChooseFileFilter()
    {
        return new ChooseFileFilter(new String[] {
            "txt"
        }, Inter.getLocText("Export-Text"));
    }

    protected String getDefaultExtension()
    {
        return "txt";
    }
}
