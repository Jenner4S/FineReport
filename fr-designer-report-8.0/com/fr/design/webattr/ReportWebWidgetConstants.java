// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.core.WidgetOptionFactory;
import com.fr.form.ui.CustomToolBarButton;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import com.fr.report.web.button.*;
import com.fr.report.web.button.page.*;
import com.fr.report.web.button.write.*;
import com.fr.stable.bridge.StableFactory;

public class ReportWebWidgetConstants
{

    public static final WidgetOption SEARCH = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[] {
        "Query", "Form-Button"
    }), BaseUtils.readIcon("/com/fr/web/images/form/resources/preview_16.png"), StableFactory.getMarkedClass("SubmitButton", com/fr/form/ui/Widget));
    public static final WidgetOption SUBMIT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_Utils-Submit"), BaseUtils.readIcon("/com/fr/web/images/save.png"), com/fr/report/web/button/write/Submit);
    public static final WidgetOption FLASHPRINT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_Utils-Print[Client]"), BaseUtils.readIcon("/com/fr/web/images/flashPrint.png"), com/fr/report/web/button/FlashPrint);
    public static final WidgetOption APPLETPRINT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_Applet_Print"), BaseUtils.readIcon("/com/fr/web/images/appletPrint.png"), com/fr/report/web/button/AppletPrint);
    public static final WidgetOption PDF = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_ReportServerP-PDF"), BaseUtils.readIcon("/com/fr/web/images/pdf.png"), com/fr/report/web/button/PDF);
    public static final WidgetOption PDF2 = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[] {
        "ReportServerP-PDF", "ReportServerP-PDF2-INFO"
    }, new String[] {
        "(", ")"
    }), BaseUtils.readIcon("/com/fr/web/images/pdf.png"), com/fr/report/web/button/PDF2);
    public static final WidgetOption PDFPRINT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_Utils-Print[Client]"), BaseUtils.readIcon("/com/fr/web/images/pdfPrint.png"), com/fr/report/web/button/PDFPrint);
    public static final WidgetOption SERVERPRINT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_ReportServerP-Print[Server]"), BaseUtils.readIcon("/com/fr/web/images/serverPrint.png"), com/fr/report/web/button/ServerPrint);
    public static final WidgetOption EMAIL = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_Email"), BaseUtils.readIcon("/com/fr/web/images/email.png"), com/fr/report/web/button/Email);
    public static final WidgetOption PRINTPREVIEW = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("PrintP-Print_Preview"), BaseUtils.readIcon("/com/fr/web/images/preview.png"), com/fr/report/web/button/PrintPreview);
    public static final WidgetOption EDIT = WidgetOptionFactory.createByWidgetClass("Edit", com/fr/report/web/button/Edit);
    public static final WidgetOption EXCELP = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[] {
        "Excel", "Export-Excel-Page"
    }, new String[] {
        "(", ")"
    }), BaseUtils.readIcon("/com/fr/web/images/excel.png"), com/fr/report/web/button/ExcelP);
    public static final WidgetOption EXCELO = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[] {
        "Excel", "Export-Excel-Simple"
    }, new String[] {
        "(", ")"
    }), BaseUtils.readIcon("/com/fr/web/images/excel.png"), com/fr/report/web/button/ExcelO);
    public static final WidgetOption EXCELS = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[] {
        "Excel", "Export-Excel-PageToSheet"
    }, new String[] {
        "(", ")"
    }), BaseUtils.readIcon("/com/fr/web/images/excel.png"), com/fr/report/web/button/ExcelS);
    public static final WidgetOption WORD = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_Word"), BaseUtils.readIcon("/com/fr/web/images/word.png"), com/fr/report/web/button/Word);
    public static final WidgetOption PAGESETUP = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("PageSetup-Page_Setup"), BaseUtils.readIcon("/com/fr/web/images/pageSetup.png"), com/fr/report/web/button/PageSetup);
    public static final WidgetOption EXPORT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_Export"), BaseUtils.readIcon("/com/fr/web/images/export.png"), com/fr/report/web/button/Export);
    public static final WidgetOption PAGENAVI = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[] {
        "HJS-Current_Page", "HF-Number_of_Page"
    }, new String[] {
        "/", ""
    }), BaseUtils.readIcon("/com/fr/web/images/pageNumber.png"), com/fr/report/web/button/page/PageNavi);
    public static final WidgetOption FIRST = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_ReportServerP-First"), BaseUtils.readIcon("/com/fr/web/images/first.png"), com/fr/report/web/button/page/First);
    public static final WidgetOption LAST = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_ReportServerP-Last"), BaseUtils.readIcon("/com/fr/web/images/last.png"), com/fr/report/web/button/page/Last);
    public static final WidgetOption PREVIOUS = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_ReportServerP-Previous"), BaseUtils.readIcon("/com/fr/web/images/previous.png"), com/fr/report/web/button/page/Previous);
    public static final WidgetOption NEXT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_ReportServerP-Next"), BaseUtils.readIcon("/com/fr/web/images/next.png"), com/fr/report/web/button/page/Next);
    public static final WidgetOption SCALE = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_Enlarge_Or_Reduce"), BaseUtils.readIcon("/com/fr/web/images/scale.png"), com/fr/report/web/button/Scale);
    public static final WidgetOption PRINT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_Print"), BaseUtils.readIcon("/com/fr/web/images/print.png"), com/fr/report/web/button/Print);
    public static final WidgetOption APPENDCOLUMNROW = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Utils-Insert_Record"), BaseUtils.readIcon("/com/fr/web/images/appendRow.png"), com/fr/report/web/button/write/AppendColumnRow);
    public static final WidgetOption DELETECOLUMNROW = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Utils-Delete_Record"), BaseUtils.readIcon("/com/fr/web/images/deleteRow.png"), com/fr/report/web/button/write/DeleteColumnRow);
    public static final WidgetOption VERIFY = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Verify-Data_Verify"), BaseUtils.readIcon("/com/fr/web/images/verify.gif"), com/fr/report/web/button/write/Verify);
    public static final WidgetOption SUBMITFORCIBLY = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Utils-Submit_Forcibly"), BaseUtils.readIcon("/com/fr/web/images/save2.png"), com/fr/report/web/button/write/SubmitForcibly);
    public static final WidgetOption SHOWCELLVALUE = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Utils-Show_Cell_Value"), BaseUtils.readIcon("/com/fr/web/images/showValue.png"), com/fr/report/web/button/write/ShowCellValue);
    public static final WidgetOption IMPORTEXCELDATA = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Utils-Import_Excel_Data"), BaseUtils.readIcon("/com/fr/web/images/excel.png"), com/fr/report/web/button/write/ImportExcelData);
    public static final WidgetOption SETPRINTEROFFSET = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_SetPrinterOffset"), BaseUtils.readIcon("/com/fr/web/images/pianyi.png"), com/fr/report/web/button/page/SetPrinterOffset);
    public static final WidgetOption CUSTOM_BUTTON = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[] {
        "Custom", "Form-Button"
    }), com/fr/form/ui/CustomToolBarButton);
    public static final WidgetOption WRITEOFFLINEHTML = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_Export-Offline-Html"), BaseUtils.readIcon("/com/fr/web/images/writeOffline.png"), com/fr/report/web/button/write/WriteOfflineHTML);
    public static final WidgetOption WRITESTASH = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine-Write_Stash"), BaseUtils.readIcon("/com/fr/web/images/edit/stash.png"), com/fr/report/web/button/write/StashButton);
    public static final WidgetOption WRITESTASHCLEAR = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine-Write_Clear"), BaseUtils.readIcon("/com/fr/web/images/edit/clearstash.png"), com/fr/report/web/button/write/ClearStashedButton);

    private ReportWebWidgetConstants()
    {
    }

    public static WidgetOption[] getPageToolBarInstance()
    {
        return (new WidgetOption[] {
            FIRST, PREVIOUS, PAGENAVI, NEXT, LAST, SCALE, EMAIL, EXPORT, PDF, EXCELP, 
            EXCELO, EXCELS, WORD, PRINT, FLASHPRINT, APPLETPRINT, PDFPRINT, SETPRINTEROFFSET, CUSTOM_BUTTON
        });
    }

    public static WidgetOption[] getViewToolBarInstance()
    {
        return (new WidgetOption[] {
            PAGESETUP, EMAIL, EXPORT, PDF, EXCELP, EXCELO, EXCELS, WORD, PRINT, FLASHPRINT, 
            APPLETPRINT, PDFPRINT, SETPRINTEROFFSET, PRINTPREVIEW, CUSTOM_BUTTON
        });
    }

    public static WidgetOption[] getPreviewToolBarInstance()
    {
        return (new WidgetOption[] {
            FIRST, PREVIOUS, PAGENAVI, NEXT, LAST, SCALE, EXPORT, PDF, EXCELP, EXCELO, 
            EXCELS, WORD, PRINT, FLASHPRINT, APPLETPRINT, PDFPRINT, SETPRINTEROFFSET, CUSTOM_BUTTON
        });
    }

    public static WidgetOption[] getWriteToolBarInstance()
    {
        return (new WidgetOption[] {
            SUBMIT, VERIFY, EMAIL, EXPORT, PDF, EXCELP, EXCELO, EXCELS, WORD, PRINT, 
            FLASHPRINT, APPLETPRINT, PDFPRINT, IMPORTEXCELDATA, SHOWCELLVALUE, APPENDCOLUMNROW, DELETECOLUMNROW, SETPRINTEROFFSET, WRITEOFFLINEHTML, CUSTOM_BUTTON, 
            WRITESTASH, WRITESTASHCLEAR
        });
    }

    public static WidgetOption[] getFormToolBarInstance()
    {
        return (new WidgetOption[] {
            EMAIL, EXPORT, PDF, EXCELP, EXCELO, EXCELS, WORD, PRINT, FLASHPRINT, APPLETPRINT, 
            PDFPRINT, SETPRINTEROFFSET, CUSTOM_BUTTON
        });
    }

}
