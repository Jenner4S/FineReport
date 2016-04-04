// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.report.web.ToolBarManager;
import com.fr.report.web.WebContent;
import com.fr.report.web.WebPage;
import com.fr.web.attr.ReportWebAttr;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.webattr:
//            WebSettingPane, ReportWebWidgetConstants

public class PageWebSettingPane extends WebSettingPane
{

    private UIRadioButton centerRadioButton;
    private UIRadioButton leftRadioButton;
    private UICheckBox isShowAsImageBox;
    private UICheckBox isAutoScaleBox;
    private UICheckBox isTDHeavyBox;

    public PageWebSettingPane()
    {
    }

    protected JPanel createOtherSetPane()
    {
        centerRadioButton = new UIRadioButton(Inter.getLocText(new String[] {
            "Center", "Display"
        }));
        leftRadioButton = new UIRadioButton(Inter.getLocText(new String[] {
            "Left", "Display"
        }));
        ButtonGroup buttongroup = new ButtonGroup();
        leftRadioButton.setSelected(true);
        buttongroup.add(centerRadioButton);
        buttongroup.add(leftRadioButton);
        JPanel jpanel = new JPanel(FRGUIPaneFactory.createBoxFlowLayout());
        jpanel.add(centerRadioButton);
        jpanel.add(leftRadioButton);
        isShowAsImageBox = new UICheckBox(Inter.getLocText("Is_Paint_Page"));
        isAutoScaleBox = new UICheckBox(Inter.getLocText("IS_Auto_Scale"));
        isTDHeavyBox = new UICheckBox(Inter.getLocText("IS_TD_HEAVY_EXPORT"), false);
        double d = -2D;
        double ad[] = {
            d, d, d
        };
        double ad1[] = {
            d, d, d, d
        };
        Component acomponent[][] = {
            {
                new UILabel((new StringBuilder()).append(Inter.getLocText("Report_Show_Location")).append(":").toString(), 4), jpanel, null
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("PageSetup-Page")).append(":").toString(), 4), isShowAsImageBox, isAutoScaleBox
            }, {
                null, isTDHeavyBox, null
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    protected void checkEnabled(boolean flag)
    {
        super.checkEnabled(flag);
        centerRadioButton.setEnabled(flag);
        leftRadioButton.setEnabled(flag);
        isShowAsImageBox.setEnabled(flag);
        isAutoScaleBox.setEnabled(flag);
        isTDHeavyBox.setEnabled(flag);
    }

    protected void setDefault()
    {
        super.setDefault();
        leftRadioButton.setSelected(true);
        isShowAsImageBox.setSelected(false);
        isAutoScaleBox.setSelected(false);
        isTDHeavyBox.setSelected(false);
    }

    protected void populateSubWebSettingrBean(WebPage webpage)
    {
        if(webpage == null)
            webpage = new WebPage();
        if(webpage.isViewAtCenter())
            centerRadioButton.setSelected(true);
        else
            leftRadioButton.setSelected(true);
        isShowAsImageBox.setSelected(webpage.isShowAsImage());
        isAutoScaleBox.setSelected(webpage.isAutoScaleWhenEmbeddedInIframe());
        isTDHeavyBox.setSelected(webpage.isTDHeavy());
    }

    protected WebPage updateSubWebSettingBean()
    {
        WebPage webpage = new WebPage();
        webpage.setViewAtCenter(centerRadioButton.isSelected());
        webpage.setShowAsImage(isShowAsImageBox.isSelected());
        webpage.setAutoScaleWhenEmbeddedInIframe(isAutoScaleBox.isSelected());
        webpage.setTDHeavy(isTDHeavyBox.isSelected());
        return webpage;
    }

    protected ToolBarManager getDefaultToolBarManager()
    {
        return ToolBarManager.createDefaultToolBar();
    }

    protected WidgetOption[] getToolBarInstance()
    {
        java.util.List list = Arrays.asList(ReportWebWidgetConstants.getPageToolBarInstance());
        java.util.List list1 = Arrays.asList(ExtraDesignClassManager.getInstance().getWebWidgetOptions());
        ArrayList arraylist = new ArrayList();
        arraylist.addAll(list);
        arraylist.addAll(list1);
        return (WidgetOption[])arraylist.toArray(new WidgetOption[arraylist.size()]);
    }

    protected WebPage getWebContent(ReportWebAttr reportwebattr)
    {
        return reportwebattr != null ? reportwebattr.getWebPage() : null;
    }

    protected String[] getEventNames()
    {
        return (new WebPage()).supportedEvents();
    }

    protected void setWebContent(ReportWebAttr reportwebattr, WebPage webpage)
    {
        reportwebattr.setWebPage(webpage);
    }

    protected volatile void setWebContent(ReportWebAttr reportwebattr, WebContent webcontent)
    {
        setWebContent(reportwebattr, (WebPage)webcontent);
    }

    protected volatile WebContent updateSubWebSettingBean()
    {
        return updateSubWebSettingBean();
    }

    protected volatile void populateSubWebSettingrBean(WebContent webcontent)
    {
        populateSubWebSettingrBean((WebPage)webcontent);
    }

    protected volatile WebContent getWebContent(ReportWebAttr reportwebattr)
    {
        return getWebContent(reportwebattr);
    }
}
