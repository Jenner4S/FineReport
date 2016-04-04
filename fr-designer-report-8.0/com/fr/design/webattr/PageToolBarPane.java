// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.base.ConfigManager;
import com.fr.base.ConfigManagerProvider;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.event.Listener;
import com.fr.general.Inter;
import com.fr.report.web.Location;
import com.fr.report.web.ToolBarManager;
import com.fr.report.web.WebContent;
import com.fr.report.web.WebPage;
import com.fr.web.attr.ReportWebAttr;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.webattr:
//            AbstractEditToolBarPane, EventPane, ReportWebWidgetConstants

public class PageToolBarPane extends AbstractEditToolBarPane
{

    private UIRadioButton centerRadioButton;
    private UIRadioButton leftRadioButton;
    private UICheckBox isUseToolBarCheckBox;
    private UICheckBox isShowAsImageBox;
    private UICheckBox isAutoScaleBox;
    private UICheckBox isTDHeavyBox;
    private EventPane eventPane;
    private UILabel showLocationLabel;
    private UILabel showListenersLabel;
    private UIButton editToolBarButton;

    public PageToolBarPane()
    {
        centerRadioButton = new UIRadioButton(Inter.getLocText(new String[] {
            "Center", "Display"
        }));
        leftRadioButton = new UIRadioButton(Inter.getLocText(new String[] {
            "Left", "Display"
        }));
        isUseToolBarCheckBox = new UICheckBox(Inter.getLocText("FR-Designer_Use_ToolBar"));
        showLocationLabel = new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Designer_Report_Show_Location")).append(":").toString());
        showListenersLabel = new UILabel((new StringBuilder()).append(Inter.getLocText("Form-Editing_Listeners")).append(":").toString());
        editToolBarButton = new UIButton(Inter.getLocText("FR-Designer_Edit"));
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        add(jpanel, "Center");
        JPanel jpanel1 = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
        jpanel.add(jpanel1, "North");
        ButtonGroup buttongroup = new ButtonGroup();
        leftRadioButton.setSelected(true);
        buttongroup.add(centerRadioButton);
        buttongroup.add(leftRadioButton);
        jpanel1.add(GUICoreUtils.createFlowPane(new Component[] {
            showLocationLabel, new UILabel(" "), centerRadioButton, leftRadioButton
        }, 0));
        isShowAsImageBox = new UICheckBox(Inter.getLocText("FR-Designer_Is_Paint_Page"));
        jpanel1.add(GUICoreUtils.createFlowPane(isShowAsImageBox, 0));
        isAutoScaleBox = new UICheckBox(Inter.getLocText("FR-Designer_IS_Auto_Scale"));
        jpanel1.add(GUICoreUtils.createFlowPane(isAutoScaleBox, 0));
        isTDHeavyBox = new UICheckBox(Inter.getLocText("FR-Designer_IS_TD_HEAVY_EXPORT"), false);
        jpanel1.add(GUICoreUtils.createFlowPane(isTDHeavyBox, 0));
        editToolBarButton.addActionListener(editBtnListener);
        isUseToolBarCheckBox.setSelected(true);
        isUseToolBarCheckBox.addActionListener(new ActionListener() {

            final PageToolBarPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                editToolBarButton.setEnabled(isUseToolBarCheckBox.isSelected());
            }

            
            {
                this$0 = PageToolBarPane.this;
                super();
            }
        }
);
        jpanel1.add(GUICoreUtils.createFlowPane(new Component[] {
            isUseToolBarCheckBox, editToolBarButton
        }, 0));
        jpanel1.add(GUICoreUtils.createFlowPane(showListenersLabel, 0));
        eventPane = new EventPane((new WebPage()).supportedEvents());
        JPanel jpanel2 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel2.add(eventPane, "Center");
        jpanel.add(jpanel2, "Center");
        ToolBarManager toolbarmanager = ToolBarManager.createDefaultToolBar();
        toolbarmanager.setToolBarLocation(Location.createTopEmbedLocation());
        toolBarManagers = (new ToolBarManager[] {
            toolbarmanager
        });
    }

    protected WidgetOption[] getToolBarInstance()
    {
        return ReportWebWidgetConstants.getPageToolBarInstance();
    }

    public void setEnabled(boolean flag)
    {
        super.setEnabled(flag);
        centerRadioButton.setEnabled(flag);
        eventPane.setEnabled(flag);
        isTDHeavyBox.setEnabled(flag);
        isAutoScaleBox.setEnabled(flag);
        isShowAsImageBox.setEnabled(flag);
        leftRadioButton.setEnabled(flag);
        isUseToolBarCheckBox.setEnabled(flag);
        editToolBarButton.setEnabled(flag && isUseToolBarCheckBox.isSelected());
        showLocationLabel.setEnabled(flag);
        showListenersLabel.setEnabled(flag);
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("WEB-Pagination_Setting");
    }

    public void populateBean(WebContent webcontent)
    {
        if(webcontent == null)
            webcontent = new WebPage();
        WebPage webpage = (WebPage)webcontent;
        if(webpage.isViewAtCenter())
            centerRadioButton.setSelected(true);
        else
            leftRadioButton.setSelected(true);
        isShowAsImageBox.setSelected(webpage.isShowAsImage());
        isAutoScaleBox.setSelected(webpage.isAutoScaleWhenEmbeddedInIframe());
        isTDHeavyBox.setSelected(webpage.isTDHeavy());
        if(webpage.isUseToolBar())
        {
            toolBarManagers = webpage.getToolBarManagers();
            isUseToolBarCheckBox.setSelected(true);
        } else
        {
            isUseToolBarCheckBox.setSelected(false);
            editToolBarButton.setEnabled(false);
        }
        if(webpage.getListenerSize() != 0)
        {
            ArrayList arraylist = new ArrayList();
            for(int i = 0; i < webpage.getListenerSize(); i++)
                arraylist.add(webpage.getListener(i));

            eventPane.populate(arraylist);
        }
    }

    public WebPage updateBean()
    {
        WebPage webpage = new WebPage();
        if(isUseToolBarCheckBox.isSelected())
            webpage.setToolBarManagers(toolBarManagers);
        else
            webpage.setToolBarManagers(new ToolBarManager[0]);
        for(int i = 0; i < eventPane.update().size(); i++)
        {
            Listener listener = (Listener)eventPane.update().get(i);
            webpage.addListener(listener);
        }

        webpage.setViewAtCenter(centerRadioButton.isSelected());
        webpage.setShowAsImage(isShowAsImageBox.isSelected());
        webpage.setAutoScaleWhenEmbeddedInIframe(isAutoScaleBox.isSelected());
        webpage.setTDHeavy(isTDHeavyBox.isSelected());
        return webpage;
    }

    public void editServerToolBarPane()
    {
        final PageToolBarPane serverPageToolBarPane = new PageToolBarPane();
        ReportWebAttr reportwebattr = (ReportWebAttr)ConfigManager.getProviderInstance().getGlobalAttribute(com/fr/web/attr/ReportWebAttr);
        if(reportwebattr != null)
            serverPageToolBarPane.populateBean(reportwebattr.getWebPage());
        BasicDialog basicdialog = serverPageToolBarPane.showWindow(SwingUtilities.getWindowAncestor(this));
        basicdialog.addDialogActionListener(new DialogActionAdapter() {

            final PageToolBarPane val$serverPageToolBarPane;
            final PageToolBarPane this$0;

            public void doOk()
            {
                ReportWebAttr reportwebattr1 = (ReportWebAttr)ConfigManager.getProviderInstance().getGlobalAttribute(com/fr/web/attr/ReportWebAttr);
                if(reportwebattr1 == null)
                {
                    reportwebattr1 = new ReportWebAttr();
                    ConfigManager.getProviderInstance().putGlobalAttribute(com/fr/web/attr/ReportWebAttr, reportwebattr1);
                }
                reportwebattr1.setWebPage(serverPageToolBarPane.updateBean());
            }

            
            {
                this$0 = PageToolBarPane.this;
                serverPageToolBarPane = pagetoolbarpane1;
                super();
            }
        }
);
        basicdialog.setVisible(true);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((WebContent)obj);
    }


}
