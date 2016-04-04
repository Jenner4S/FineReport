// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.web.ToolBarManager;
import com.fr.report.web.WebContent;
import com.fr.report.web.WebWrite;
import com.fr.web.attr.ReportWebAttr;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.webattr:
//            WebSettingPane, ReportWebWidgetConstants

public class WriteWebSettingPane extends WebSettingPane
{

    private UICheckBox colorBox;
    private UIColorButton colorButton;
    private UIRadioButton topRadioButton;
    private UIRadioButton bottomRadioButton;
    private UIRadioButton centerRadioButton;
    private UIRadioButton leftRadioButton;
    private UILabel rptShowLocationLabel;
    private UILabel sheetShowLocationLabel;
    private UICheckBox unloadCheck;
    private UICheckBox showWidgets;
    private UICheckBox isAutoStash;

    public WriteWebSettingPane()
    {
    }

    protected JPanel createOtherSetPane()
    {
        colorBox = new UICheckBox((new StringBuilder()).append(Inter.getLocText(new String[] {
            "Face_Write", "Current", "Edit", "Row", "Background", "Set"
        })).append(":").toString());
        colorBox.setSelected(true);
        colorButton = new UIColorButton(BaseUtils.readIcon("/com/fr/design/images/gui/color/background.png"));
        colorBox.addActionListener(new ActionListener() {

            final WriteWebSettingPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                colorButton.setEnabled(colorBox.isSelected());
            }

            
            {
                this$0 = WriteWebSettingPane.this;
                super();
            }
        }
);
        JPanel jpanel = GUICoreUtils.createFlowPane(new Component[] {
            colorBox, colorButton
        }, 0);
        topRadioButton = new UIRadioButton(Inter.getLocText("FR-Designer_Top"));
        bottomRadioButton = new UIRadioButton(Inter.getLocText("FR-Designer_Bottom"));
        sheetShowLocationLabel = new UILabel((new StringBuilder()).append("sheet").append(Inter.getLocText(new String[] {
            "Label", "Page_Number", "Display position"
        })).append(":").toString(), 2);
        ButtonGroup buttongroup = new ButtonGroup();
        bottomRadioButton.setSelected(true);
        buttongroup.add(topRadioButton);
        buttongroup.add(bottomRadioButton);
        JPanel jpanel1 = GUICoreUtils.createFlowPane(new Component[] {
            sheetShowLocationLabel, topRadioButton, bottomRadioButton
        }, 0);
        rptShowLocationLabel = new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Designer_Report_Show_Location")).append(":").toString(), 2);
        centerRadioButton = new UIRadioButton(Inter.getLocText(new String[] {
            "Center", "Display"
        }));
        leftRadioButton = new UIRadioButton(Inter.getLocText(new String[] {
            "Left", "Display"
        }));
        ButtonGroup buttongroup1 = new ButtonGroup();
        leftRadioButton.setSelected(true);
        buttongroup1.add(centerRadioButton);
        buttongroup1.add(leftRadioButton);
        JPanel jpanel2 = GUICoreUtils.createFlowPane(new Component[] {
            rptShowLocationLabel, centerRadioButton, leftRadioButton
        }, 0);
        unloadCheck = new UICheckBox(Inter.getLocText(new String[] {
            "Event-unloadcheck", "Tooltips"
        }));
        unloadCheck.setSelected(true);
        showWidgets = new UICheckBox(Inter.getLocText(new String[] {
            "Event-showWidgets"
        }));
        showWidgets.setSelected(false);
        isAutoStash = new UICheckBox(Inter.getLocText("FR-Designer-Write_Auto_Stash"));
        isAutoStash.setSelected(false);
        JPanel jpanel3 = GUICoreUtils.createFlowPane(new Component[] {
            unloadCheck, showWidgets, isAutoStash
        }, 0);
        JPanel jpanel4 = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(2);
        jpanel4.add(jpanel1);
        jpanel4.add(jpanel2);
        jpanel4.add(jpanel);
        jpanel4.add(jpanel3);
        return jpanel4;
    }

    protected void checkEnabled(boolean flag)
    {
        super.checkEnabled(flag);
        colorBox.setEnabled(flag);
        colorButton.setEnabled(flag);
        topRadioButton.setEnabled(flag);
        leftRadioButton.setEnabled(flag);
        centerRadioButton.setEnabled(flag);
        rptShowLocationLabel.setEnabled(flag);
        sheetShowLocationLabel.setEnabled(flag);
        bottomRadioButton.setEnabled(flag);
        unloadCheck.setEnabled(flag);
        showWidgets.setEnabled(flag);
        isAutoStash.setEnabled(flag);
    }

    protected void setDefault()
    {
        super.setDefault();
        colorBox.setSelected(false);
        bottomRadioButton.setSelected(true);
        leftRadioButton.setSelected(true);
        unloadCheck.setSelected(true);
        showWidgets.setSelected(false);
        isAutoStash.setSelected(false);
    }

    protected void populateSubWebSettingrBean(WebWrite webwrite)
    {
        if(webwrite == null)
            webwrite = new WebWrite();
        if(webwrite.getSelectedColor() != null)
        {
            colorBox.setSelected(true);
            colorButton.setColor(webwrite.getSelectedColor());
        } else
        {
            colorBox.setSelected(false);
        }
        if(webwrite.getSheetPosition() == 1)
            topRadioButton.setSelected(true);
        else
        if(webwrite.getSheetPosition() == 3)
            bottomRadioButton.setSelected(true);
        if(webwrite.isViewAtLeft())
            leftRadioButton.setSelected(true);
        else
            centerRadioButton.setSelected(true);
        unloadCheck.setSelected(webwrite.isUnloadCheck());
        showWidgets.setSelected(webwrite.isShowWidgets());
        isAutoStash.setSelected(webwrite.isAutoStash());
    }

    protected WebWrite updateSubWebSettingBean()
    {
        WebWrite webwrite = new WebWrite();
        if(colorBox.isSelected())
            webwrite.setSelectedColor(colorButton.getColor());
        else
            webwrite.setSelectedColor(null);
        if(topRadioButton.isSelected())
            webwrite.setSheetPosition(1);
        else
        if(bottomRadioButton.isSelected())
            webwrite.setSheetPosition(3);
        webwrite.setViewAtLeft(leftRadioButton.isSelected());
        webwrite.setUnloadCheck(unloadCheck.isSelected());
        webwrite.setShowWidgets(showWidgets.isSelected());
        webwrite.setAutoStash(isAutoStash.isSelected());
        return webwrite;
    }

    protected WidgetOption[] getToolBarInstance()
    {
        java.util.List list = Arrays.asList(ReportWebWidgetConstants.getWriteToolBarInstance());
        java.util.List list1 = Arrays.asList(ExtraDesignClassManager.getInstance().getWebWidgetOptions());
        ArrayList arraylist = new ArrayList();
        arraylist.addAll(list);
        arraylist.addAll(list1);
        return (WidgetOption[])arraylist.toArray(new WidgetOption[arraylist.size()]);
    }

    protected ToolBarManager getDefaultToolBarManager()
    {
        return ToolBarManager.createDefaultWriteToolBar();
    }

    protected WebWrite getWebContent(ReportWebAttr reportwebattr)
    {
        return reportwebattr != null ? reportwebattr.getWebWrite() : null;
    }

    protected String[] getEventNames()
    {
        return (new WebWrite()).supportedEvents();
    }

    protected void setWebContent(ReportWebAttr reportwebattr, WebWrite webwrite)
    {
        reportwebattr.setWebWrite(webwrite);
    }

    protected volatile void setWebContent(ReportWebAttr reportwebattr, WebContent webcontent)
    {
        setWebContent(reportwebattr, (WebWrite)webcontent);
    }

    protected volatile WebContent updateSubWebSettingBean()
    {
        return updateSubWebSettingBean();
    }

    protected volatile void populateSubWebSettingrBean(WebContent webcontent)
    {
        populateSubWebSettingrBean((WebWrite)webcontent);
    }

    protected volatile WebContent getWebContent(ReportWebAttr reportwebattr)
    {
        return getWebContent(reportwebattr);
    }


}
