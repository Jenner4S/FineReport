// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.base.*;
import com.fr.design.gui.frpane.LoadingBasicPane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.web.attr.ReportWebAttr;
import java.awt.Component;
import java.io.File;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.webattr:
//            PageToolBarPane, WriteToolBarPane, ViewToolBarPane, WebCssPane, 
//            WebJsPane, ErrorTemplatePane

public class EditReportServerParameterPane extends LoadingBasicPane
{

    private UITextField configFileTextField;
    private UITabbedPane tabbedPane;
    private PageToolBarPane pagePane;
    private ViewToolBarPane viewPane;
    private WriteToolBarPane writePane;
    private ReportWebAttr webAttr;
    private WebCssPane cssPane;
    private WebJsPane jsPane;
    private ErrorTemplatePane errorTemplatePane;

    public EditReportServerParameterPane()
    {
    }

    protected void initComponents(JPanel jpanel)
    {
        JPanel jpanel1 = jpanel;
        jpanel1.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel2 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel1.add(jpanel2, "North");
        jpanel2.add(GUICoreUtils.createFlowPane(new Component[] {
            new UILabel((new StringBuilder()).append(Inter.getLocText("ReportServerP-Report_server_parameter")).append(":").toString()), configFileTextField = new UITextField(40)
        }, 0), "West");
        configFileTextField.setEditable(false);
        tabbedPane = new UITabbedPane();
        jpanel1.add(tabbedPane, "Center");
        tabbedPane.addTab(Inter.getLocText("WEB-Pagination_Setting"), pagePane = new PageToolBarPane());
        tabbedPane.addTab(Inter.getLocText("WEB-Write_Setting"), writePane = new WriteToolBarPane());
        tabbedPane.addTab(Inter.getLocText("M-Data_Analysis_Settings"), viewPane = new ViewToolBarPane());
        tabbedPane.addTab(Inter.getLocText("ReportServerP-Import_Css"), cssPane = new WebCssPane());
        tabbedPane.addTab(Inter.getLocText("ReportServerP-Import_JavaScript"), jsPane = new WebJsPane());
        tabbedPane.addTab(Inter.getLocText("FR-Designer_ErrorHandlerTemplate"), errorTemplatePane = new ErrorTemplatePane());
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("ReportServerP-Report_server_parameter");
    }

    public void populate(ConfigManagerProvider configmanagerprovider)
    {
        configFileTextField.setText((new StringBuilder()).append(FRContext.getCurrentEnv().getPath()).append(File.separator).append("resources").append(File.separator).append(ConfigManager.getProviderInstance().fileName()).toString());
        webAttr = (ReportWebAttr)ConfigManager.getProviderInstance().getGlobalAttribute(com/fr/web/attr/ReportWebAttr);
        if(webAttr != null)
        {
            pagePane.populateBean(webAttr.getWebPage());
            viewPane.populateBean(webAttr.getWebView());
            writePane.populateBean(webAttr.getWebWrite());
            cssPane.populate(webAttr);
            jsPane.populate(webAttr);
        }
        errorTemplatePane.populateBean(configmanagerprovider.getErrorTemplate());
    }

    public void update(ConfigManagerProvider configmanagerprovider)
    {
        ReportWebAttr reportwebattr = (ReportWebAttr)ConfigManager.getProviderInstance().getGlobalAttribute(com/fr/web/attr/ReportWebAttr);
        if(reportwebattr == null)
        {
            reportwebattr = new ReportWebAttr();
            configmanagerprovider.putGlobalAttribute(com/fr/web/attr/ReportWebAttr, reportwebattr);
        }
        reportwebattr.setWebPage(pagePane.updateBean());
        reportwebattr.setWebView(viewPane.updateBean());
        reportwebattr.setWebWrite(writePane.updateBean());
        cssPane.update(reportwebattr);
        jsPane.update(reportwebattr);
        configmanagerprovider.setErrorTemplate(errorTemplatePane.updateBean());
    }
}
