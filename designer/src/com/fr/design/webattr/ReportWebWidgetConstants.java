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
import com.fr.stable.bridge.BridgeMark;
import com.fr.stable.bridge.StableFactory;

public class ReportWebWidgetConstants {
    private ReportWebWidgetConstants() {
    }

    public static WidgetOption[] getPageToolBarInstance() {
        return new WidgetOption[]{FIRST, PREVIOUS, PAGENAVI, NEXT, LAST, SCALE, EMAIL, EXPORT, PDF, EXCELP, EXCELO, EXCELS, WORD, PRINT, FLASHPRINT, APPLETPRINT, PDFPRINT,
                SETPRINTEROFFSET, CUSTOM_BUTTON};
    }

    public static WidgetOption[] getViewToolBarInstance() {
        return new WidgetOption[]{PAGESETUP, EMAIL, EXPORT, PDF, EXCELP, EXCELO, EXCELS, WORD, PRINT, FLASHPRINT, APPLETPRINT, PDFPRINT, SETPRINTEROFFSET, PRINTPREVIEW, CUSTOM_BUTTON};
    }

    public static WidgetOption[] getPreviewToolBarInstance() {
        return new WidgetOption[]{FIRST, PREVIOUS, PAGENAVI, NEXT, LAST, SCALE, EXPORT, PDF, EXCELP, EXCELO, EXCELS, WORD, PRINT, FLASHPRINT, APPLETPRINT, PDFPRINT,
                SETPRINTEROFFSET, CUSTOM_BUTTON};
    }

    public static WidgetOption[] getWriteToolBarInstance() {
        return new WidgetOption[]{SUBMIT, VERIFY, EMAIL, EXPORT, PDF, EXCELP, EXCELO, EXCELS, WORD, PRINT, FLASHPRINT, APPLETPRINT, PDFPRINT, IMPORTEXCELDATA, SHOWCELLVALUE,
                APPENDCOLUMNROW, DELETECOLUMNROW, SETPRINTEROFFSET, WRITEOFFLINEHTML, CUSTOM_BUTTON};
    }

    public static WidgetOption[] getFormToolBarInstance() {
        return new WidgetOption[]{EMAIL, EXPORT, PDF, EXCELP, EXCELO, EXCELS, WORD, PRINT, FLASHPRINT, APPLETPRINT, PDFPRINT, SETPRINTEROFFSET, CUSTOM_BUTTON};
    }

    // ��ѯ
    public static final WidgetOption SEARCH = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[]{"Query", "Form-Button"}),
            BaseUtils.readIcon("/com/fr/web/images/form/resources/preview_16.png"), StableFactory.getMarkedClass(BridgeMark.SUBMIT_BUTTON, Widget.class));

    // �ύ��ť
    public static final WidgetOption SUBMIT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Utils-Submit"), BaseUtils.readIcon("/com/fr/web/images/save.png"),
            Submit.class);

    // flash��ӡ��ť
    public static final WidgetOption FLASHPRINT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Utils-Print[Client]"),
            BaseUtils.readIcon("/com/fr/web/images/flashPrint.png"), FlashPrint.class);

    // appletprint
    public static final WidgetOption APPLETPRINT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Applet_Print"),
            BaseUtils.readIcon("/com/fr/web/images/appletPrint.png"), AppletPrint.class);

    // PDF����
    public static final WidgetOption PDF = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("ReportServerP-PDF"), BaseUtils.readIcon("/com/fr/web/images/pdf.png"),
            PDF.class);

    // PDF���������linux������������ĵ����������
    public static final WidgetOption PDF2 = WidgetOptionFactory.createByWidgetClass(
            Inter.getLocText(new String[]{"ReportServerP-PDF", "ReportServerP-PDF2-INFO"}, new String[]{"(", ")"}), BaseUtils.readIcon("/com/fr/web/images/pdf.png"), PDF2.class);

    // �ͻ���PDF��ӡ
    public static final WidgetOption PDFPRINT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Utils-Print[Client]"),
            BaseUtils.readIcon("/com/fr/web/images/pdfPrint.png"), PDFPrint.class);

    // �������˴�ӡ
    public static final WidgetOption SERVERPRINT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("ReportServerP-Print[Server]"),
            BaseUtils.readIcon("/com/fr/web/images/serverPrint.png"), ServerPrint.class);
    // �ʼ�����
    public static final WidgetOption EMAIL = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Email"), BaseUtils.readIcon("/com/fr/web/images/email.png"), Email.class);
    public static final WidgetOption PRINTPREVIEW = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("PrintP-Print_Preview"),
            BaseUtils.readIcon("/com/fr/web/images/preview.png"), PrintPreview.class);

    public static final WidgetOption EDIT = WidgetOptionFactory.createByWidgetClass("Edit", Edit.class);

    // ������Excel ��ҳ����
    public static final WidgetOption EXCELP = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[]{"Excel", "Export-Excel-Page"}, new String[]{"(", ")"}),
            BaseUtils.readIcon("/com/fr/web/images/excel.png"), Excel.class);
    // ������Excel ԭ������
    public static final WidgetOption EXCELO = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[]{"Excel", "Export-Excel-Simple"}, new String[]{"(", ")"}),
            BaseUtils.readIcon("/com/fr/web/images/excel.png"), ExcelO.class);
    // ������Excel ��ҳ��Sheet����
    public static final WidgetOption EXCELS = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[]{"Excel", "Export-Excel-PageToSheet"}, new String[]{"(", ")"}),
            BaseUtils.readIcon("/com/fr/web/images/excel.png"), ExcelS.class);

    // ������Word
    public static final WidgetOption WORD = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Word"), BaseUtils.readIcon("/com/fr/web/images/word.png"), Word.class);
    // ҳ������
    public static final WidgetOption PAGESETUP = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("PageSetup-Page_Setup"), BaseUtils.readIcon("/com/fr/web/images/pageSetup.png"), PageSetup.class);
    // ����
    public static final WidgetOption EXPORT = WidgetOptionFactory
            .createByWidgetClass(Inter.getLocText("Export"), BaseUtils.readIcon("/com/fr/web/images/export.png"), Export.class);

    // ��ǰҳ/��ҳ��
    public static final WidgetOption PAGENAVI = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[]{"HJS-Current_Page", "HF-Number_of_Page"}, new String[]{"/", ""}),
            BaseUtils.readIcon("/com/fr/web/images/pageNumber.png"), PageNavi.class);
    // ��ҳ
    public static final WidgetOption FIRST = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("ReportServerP-First"), BaseUtils.readIcon("/com/fr/web/images/first.png"),
            First.class);
    // ĩҳ
    public static final WidgetOption LAST = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("ReportServerP-Last"), BaseUtils.readIcon("/com/fr/web/images/last.png"),
            Last.class);
    // ǰһҳ
    public static final WidgetOption PREVIOUS = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("ReportServerP-Previous"),
            BaseUtils.readIcon("/com/fr/web/images/previous.png"), Previous.class);
    // ��һҳ
    public static final WidgetOption NEXT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("ReportServerP-Next"), BaseUtils.readIcon("/com/fr/web/images/next.png"),
            Next.class);
    public static final WidgetOption SCALE = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Enlarge_Or_Reduce"), BaseUtils.readIcon("/com/fr/web/images/scale.png"),
            Scale.class);

    public static final WidgetOption PRINT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Print"), BaseUtils.readIcon("/com/fr/web/images/print.png"), Print.class);
    public static final WidgetOption APPENDCOLUMNROW = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Utils-Insert_Record"),
            BaseUtils.readIcon("/com/fr/web/images/appendRow.png"), AppendColumnRow.class);
    public static final WidgetOption DELETECOLUMNROW = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Utils-Delete_Record"),
            BaseUtils.readIcon("/com/fr/web/images/deleteRow.png"), DeleteColumnRow.class);
    public static final WidgetOption VERIFY = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Verify-Data_Verify"), BaseUtils.readIcon("/com/fr/web/images/verify.gif"),
            Verify.class);
    public static final WidgetOption SUBMITFORCIBLY = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Utils-Submit_Forcibly"),
            BaseUtils.readIcon("/com/fr/web/images/save2.png"), SubmitForcibly.class);

    // show cell value
    public static final WidgetOption SHOWCELLVALUE = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Utils-Show_Cell_Value"),
            BaseUtils.readIcon("/com/fr/web/images/showValue.png"), ShowCellValue.class);

    // import excel data
    public static final WidgetOption IMPORTEXCELDATA = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Utils-Import_Excel_Data"),
            BaseUtils.readIcon("/com/fr/web/images/excel.png"), ImportExcelData.class);

    // ��ӡ��ƫ������
    public static final WidgetOption SETPRINTEROFFSET = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("SetPrinterOffset"), BaseUtils.readIcon("/com/fr/web/images/pianyi.png"), SetPrinterOffset.class);

    public static final WidgetOption CUSTOM_BUTTON = WidgetOptionFactory
            .createByWidgetClass(Inter.getLocText(new String[]{"Custom", "Form-Button"}), CustomToolBarButton.class);

    // �������html����
    public static final WidgetOption WRITEOFFLINEHTML = WidgetOptionFactory
            .createByWidgetClass(Inter.getLocText("Export-Offline-Html"), BaseUtils.readIcon("/com/fr/web/images/writeOffline.png"), WriteOfflineHTML.class);

}
