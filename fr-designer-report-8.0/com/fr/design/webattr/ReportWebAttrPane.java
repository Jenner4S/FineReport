// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.design.gui.frpane.LoadingBasicPane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.background.BackgroundPane;
import com.fr.general.Inter;
import com.fr.web.attr.ReportWebAttr;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.webattr:
//            CommonPane, ReportServerPrinterPane, PageWebSettingPane, WriteWebSettingPane, 
//            ViewWebSettingPane, WebCssPane, WebJsPane

public class ReportWebAttrPane extends LoadingBasicPane
{

    private ReportWebAttr reportWebAttr;
    private UITabbedPane tabbedPane;
    private CommonPane commonPane;
    private ReportServerPrinterPane serverPrintPane;
    private PageWebSettingPane pageWeb;
    private WriteWebSettingPane writeWeb;
    private ViewWebSettingPane viewWeb;
    private BackgroundPane backgroundPane;
    protected WebCssPane cssPane;
    protected WebJsPane jsPane;

    public ReportWebAttrPane()
    {
    }

    protected void initComponents(JPanel jpanel)
    {
        JPanel jpanel1 = jpanel;
        jpanel1.setLayout(FRGUIPaneFactory.createBorderLayout());
        tabbedPane = new UITabbedPane();
        jpanel1.add(tabbedPane, "Center");
        tabbedPane.addTab(Inter.getLocText("ReportServerP-Basic"), commonPane = new CommonPane());
        tabbedPane.addTab(Inter.getLocText("ReportServerP-Printers(Server)"), serverPrintPane = new ReportServerPrinterPane());
        tabbedPane.add(Inter.getLocText("WEB-Pagination_Setting"), pageWeb = new PageWebSettingPane());
        tabbedPane.add(Inter.getLocText("WEB-Write_Setting"), writeWeb = new WriteWebSettingPane());
        tabbedPane.add(Inter.getLocText("M-Data_Analysis_Settings"), viewWeb = new ViewWebSettingPane());
        tabbedPane.addTab(Inter.getLocText("ReportServerP-Browser_Background"), backgroundPane = new BackgroundPane(true));
        tabbedPane.addTab(Inter.getLocText("ReportServerP-Import_Css"), cssPane = new WebCssPane());
        tabbedPane.addTab(Inter.getLocText("ReportServerP-Import_JavaScript"), jsPane = new WebJsPane());
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("ReportD-Report_Web_Attributes");
    }

    public void populate(ReportWebAttr reportwebattr)
    {
        if(reportwebattr == null)
            reportwebattr = new ReportWebAttr();
        reportWebAttr = reportwebattr;
        commonPane.populate(reportwebattr);
        serverPrintPane.populate(reportwebattr.getPrinter());
        pageWeb.populateBean(reportwebattr);
        viewWeb.populateBean(reportwebattr);
        writeWeb.populateBean(reportwebattr);
        backgroundPane.populate(reportwebattr.getBackground());
        cssPane.populate(reportwebattr);
        jsPane.populate(reportwebattr);
    }

    public ReportWebAttr update()
    {
        reportWebAttr.setPrinter(serverPrintPane.update());
        pageWeb.update(reportWebAttr);
        writeWeb.update(reportWebAttr);
        viewWeb.update(reportWebAttr);
        reportWebAttr.setBackground(backgroundPane.update());
        cssPane.update(reportWebAttr);
        jsPane.update(reportWebAttr);
        commonPane.update(reportWebAttr);
        return reportWebAttr;
    }
}
