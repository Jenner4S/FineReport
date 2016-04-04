// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.menu.MenuDef;
import java.awt.Graphics2D;
import javax.swing.Icon;

// Referenced classes of package com.fr.design.mainframe:
//            SheetNameTabPane, ReportComponentComposite

public class PolySheetNameTabPane extends SheetNameTabPane
{

    public PolySheetNameTabPane(ReportComponentComposite reportcomponentcomposite)
    {
        super(reportcomponentcomposite);
    }

    protected void paintAddButton(Graphics2D graphics2d)
    {
        ADD_POLY_SHEET.paintIcon(this, graphics2d, iconLocation, 3);
    }

    protected void firstInsertActionPerformed()
    {
        (new SheetNameTabPane.PolyReportInsertAction(this)).actionPerformed(null);
    }

    protected void addInsertGridShortCut(MenuDef menudef)
    {
    }
}
