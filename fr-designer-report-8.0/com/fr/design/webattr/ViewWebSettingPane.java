// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.web.ToolBarManager;
import com.fr.report.web.WebContent;
import com.fr.report.web.WebView;
import com.fr.web.attr.ReportWebAttr;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.webattr:
//            WebSettingPane, ReportWebWidgetConstants

public class ViewWebSettingPane extends WebSettingPane
{

    private UICheckBox sortCheckBox;
    private UICheckBox conditionFilterBox;
    private UICheckBox listFilterBox;

    public ViewWebSettingPane()
    {
    }

    protected JPanel createOtherSetPane()
    {
        sortCheckBox = new UICheckBox(Inter.getLocText("FR-Engine-Sort_Sort"));
        conditionFilterBox = new UICheckBox(Inter.getLocText("FR-Engine-Selection_Filter"));
        listFilterBox = new UICheckBox(Inter.getLocText("FR-Engine-List_Filter"));
        sortCheckBox.setSelected(true);
        conditionFilterBox.setSelected(true);
        listFilterBox.setSelected(true);
        return GUICoreUtils.createFlowPane(new Component[] {
            new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Engine_ViewPreview")).append(":").toString()), sortCheckBox, conditionFilterBox, listFilterBox
        }, 0, 6);
    }

    protected void populateSubWebSettingrBean(WebView webview)
    {
        if(webview == null)
            webview = new WebView();
        listFilterBox.setSelected(webview.isListFuncCheck());
        conditionFilterBox.setSelected(webview.isConditionFuncCheck());
        sortCheckBox.setSelected(webview.isSortFuncCheck());
    }

    protected WebView updateSubWebSettingBean()
    {
        WebView webview = new WebView();
        webview.setIsListFuncCheck(listFilterBox.isSelected());
        webview.setIsConditionFuncCheck(conditionFilterBox.isSelected());
        webview.setIsSortFuncCheck(sortCheckBox.isSelected());
        return webview;
    }

    protected WidgetOption[] getToolBarInstance()
    {
        java.util.List list = Arrays.asList(ReportWebWidgetConstants.getViewToolBarInstance());
        java.util.List list1 = Arrays.asList(ExtraDesignClassManager.getInstance().getWebWidgetOptions());
        ArrayList arraylist = new ArrayList();
        arraylist.addAll(list);
        arraylist.addAll(list1);
        return (WidgetOption[])arraylist.toArray(new WidgetOption[arraylist.size()]);
    }

    protected ToolBarManager getDefaultToolBarManager()
    {
        return ToolBarManager.createDefaultViewToolBar();
    }

    protected WebView getWebContent(ReportWebAttr reportwebattr)
    {
        return reportwebattr != null ? reportwebattr.getWebView() : null;
    }

    protected String[] getEventNames()
    {
        return (new WebView()).supportedEvents();
    }

    protected void setWebContent(ReportWebAttr reportwebattr, WebView webview)
    {
        reportwebattr.setWebView(webview);
    }

    protected void checkEnabled(boolean flag)
    {
        super.checkEnabled(flag);
        sortCheckBox.setEnabled(flag);
        conditionFilterBox.setEnabled(flag);
        listFilterBox.setEnabled(flag);
    }

    protected void setDefault()
    {
        super.setDefault();
        sortCheckBox.setSelected(true);
        conditionFilterBox.setSelected(true);
        listFilterBox.setSelected(true);
    }

    protected volatile void setWebContent(ReportWebAttr reportwebattr, WebContent webcontent)
    {
        setWebContent(reportwebattr, (WebView)webcontent);
    }

    protected volatile WebContent updateSubWebSettingBean()
    {
        return updateSubWebSettingBean();
    }

    protected volatile void populateSubWebSettingrBean(WebContent webcontent)
    {
        populateSubWebSettingrBean((WebView)webcontent);
    }

    protected volatile WebContent getWebContent(ReportWebAttr reportwebattr)
    {
        return getWebContent(reportwebattr);
    }
}
