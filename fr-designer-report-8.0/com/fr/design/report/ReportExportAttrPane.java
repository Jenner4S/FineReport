// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report;

import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;
import com.fr.io.attr.ReportExportAttr;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

// Referenced classes of package com.fr.design.report:
//            ExcelExportPane, PDFExportPane, WordExportPane

public class ReportExportAttrPane extends BasicPane
{

    private ExcelExportPane excelExportPane;
    private PDFExportPane pdfExportPane;
    private WordExportPane wordExportPane;

    public ReportExportAttrPane()
    {
        setLayout(new BoxLayout(this, 1));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        excelExportPane = new ExcelExportPane();
        add(excelExportPane);
        pdfExportPane = new PDFExportPane();
        add(pdfExportPane);
        wordExportPane = new WordExportPane();
        add(wordExportPane);
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("ReportD-Excel_Export");
    }

    public void populate(ReportExportAttr reportexportattr)
    {
        if(reportexportattr == null)
            reportexportattr = new ReportExportAttr();
        if(excelExportPane != null)
            excelExportPane.populate(reportexportattr.getExcelExportAttr());
        if(pdfExportPane != null)
            pdfExportPane.populate(reportexportattr.getPDFExportAttr());
        if(wordExportPane != null)
            wordExportPane.populate(reportexportattr.getWordExportAttr());
    }

    public ReportExportAttr update()
    {
        ReportExportAttr reportexportattr = new ReportExportAttr();
        if(excelExportPane != null)
            reportexportattr.setExcelExportAttr(excelExportPane.update());
        if(pdfExportPane != null)
            reportexportattr.setPDFExportAttr(pdfExportPane.update());
        if(wordExportPane != null)
            reportexportattr.setWordExportAttr(wordExportPane.update());
        return reportexportattr;
    }
}
