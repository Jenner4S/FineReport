// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.main.impl.WorkBook;
import com.fr.report.poly.PolyWorkSheet;

// Referenced classes of package com.fr.design.mainframe:
//            JWorkBook, PolySheetNameTabPane, ReportComponentComposite, SheetNameTabPane

public class JPolyWorkBook extends JWorkBook
{

    private static final String DEFAULT_NAME = "Poly";

    public JPolyWorkBook()
    {
        super(new WorkBook(new PolyWorkSheet()), "Poly");
        populateReportParameterAttr();
    }

    public SheetNameTabPane createSheetNameTabPane(ReportComponentComposite reportcomponentcomposite)
    {
        return new PolySheetNameTabPane(reportcomponentcomposite);
    }
}
