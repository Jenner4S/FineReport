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
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.event.Listener;
import com.fr.general.Inter;
import com.fr.report.web.Location;
import com.fr.report.web.ToolBarManager;
import com.fr.report.web.WebContent;
import com.fr.report.web.WebView;
import com.fr.web.attr.ReportWebAttr;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.webattr:
//            AbstractEditToolBarPane, EventPane, ReportWebWidgetConstants

public class ViewToolBarPane extends AbstractEditToolBarPane
{

    private EventPane eventPane;
    private UICheckBox isUseToolBarCheckBox;
    private UIButton editToolBarButton;
    private UILabel showListenersLabel;
    private UICheckBox sortCheckBox;
    private UICheckBox conditonFilterBox;
    private UICheckBox listFilterBox;

    public ViewToolBarPane()
    {
        isUseToolBarCheckBox = new UICheckBox(Inter.getLocText("FR-Designer_Use_ToolBar"));
        editToolBarButton = new UIButton(Inter.getLocText("FR-Designer_Edit"));
        showListenersLabel = new UILabel((new StringBuilder()).append(Inter.getLocText("Form-Editing_Listeners")).append(":").toString());
        sortCheckBox = new UICheckBox(Inter.getLocText("FR-Engine-Sort_Sort"));
        conditonFilterBox = new UICheckBox(Inter.getLocText("FR-Engine-Selection_Filter"));
        listFilterBox = new UICheckBox(Inter.getLocText("FR-Engine-List_Filter"));
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        add(jpanel, "Center");
        JPanel jpanel1 = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
        jpanel.add(jpanel1, "North");
        editToolBarButton.addActionListener(editBtnListener);
        isUseToolBarCheckBox.setSelected(true);
        isUseToolBarCheckBox.addActionListener(new ActionListener() {

            final ViewToolBarPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                editToolBarButton.setEnabled(isUseToolBarCheckBox.isSelected());
            }

            
            {
                this$0 = ViewToolBarPane.this;
                super();
            }
        }
);
        sortCheckBox.setSelected(true);
        conditonFilterBox.setSelected(true);
        listFilterBox.setSelected(true);
        jpanel1.add(GUICoreUtils.createFlowPane(new Component[] {
            sortCheckBox, conditonFilterBox, listFilterBox
        }, 0, 6));
        jpanel1.add(GUICoreUtils.createFlowPane(new Component[] {
            isUseToolBarCheckBox, editToolBarButton
        }, 0));
        jpanel1.add(GUICoreUtils.createFlowPane(showListenersLabel, 0));
        eventPane = new EventPane((new WebView()).supportedEvents());
        JPanel jpanel2 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel2.add(eventPane, "Center");
        jpanel.add(jpanel2, "Center");
        ToolBarManager toolbarmanager = ToolBarManager.createDefaultViewToolBar();
        toolbarmanager.setToolBarLocation(Location.createTopEmbedLocation());
        toolBarManagers = (new ToolBarManager[] {
            toolbarmanager
        });
    }

    public void setEnabled(boolean flag)
    {
        super.setEnabled(flag);
        eventPane.setEnabled(flag);
        isUseToolBarCheckBox.setEnabled(flag);
        sortCheckBox.setEnabled(flag);
        conditonFilterBox.setEnabled(flag);
        listFilterBox.setEnabled(flag);
        editToolBarButton.setEnabled(flag && isUseToolBarCheckBox.isSelected());
        showListenersLabel.setEnabled(flag);
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("M-Data_Analysis_Settings");
    }

    public void populateBean(WebContent webcontent)
    {
        if(webcontent == null)
            webcontent = new WebView();
        if(webcontent.isUseToolBar())
        {
            toolBarManagers = webcontent.getToolBarManagers();
            isUseToolBarCheckBox.setSelected(true);
        } else
        {
            isUseToolBarCheckBox.setSelected(false);
            editToolBarButton.setEnabled(false);
        }
        WebView webview = (WebView)webcontent;
        listFilterBox.setSelected(webview.isListFuncCheck());
        conditonFilterBox.setSelected(webview.isConditionFuncCheck());
        sortCheckBox.setSelected(webview.isSortFuncCheck());
        if(webcontent.getListenerSize() != 0)
        {
            ArrayList arraylist = new ArrayList();
            for(int i = 0; i < webcontent.getListenerSize(); i++)
                arraylist.add(webcontent.getListener(i));

            eventPane.populate(arraylist);
        }
    }

    public WebView updateBean()
    {
        WebView webview = new WebView();
        if(isUseToolBarCheckBox.isSelected())
            webview.setToolBarManagers(toolBarManagers);
        else
            webview.setToolBarManagers(new ToolBarManager[0]);
        webview.setIsSortFuncCheck(sortCheckBox.isSelected());
        webview.setIsConditionFuncCheck(conditonFilterBox.isSelected());
        webview.setIsListFuncCheck(listFilterBox.isSelected());
        for(int i = 0; i < eventPane.update().size(); i++)
        {
            Listener listener = (Listener)eventPane.update().get(i);
            webview.addListener(listener);
        }

        return webview;
    }

    public void editServerToolBarPane()
    {
        final ViewToolBarPane serverPageToolBarPane = new ViewToolBarPane();
        ReportWebAttr reportwebattr = (ReportWebAttr)ConfigManager.getProviderInstance().getGlobalAttribute(com/fr/web/attr/ReportWebAttr);
        if(reportwebattr != null)
            serverPageToolBarPane.populateBean(reportwebattr.getWebView());
        BasicDialog basicdialog = serverPageToolBarPane.showWindow(SwingUtilities.getWindowAncestor(this));
        basicdialog.addDialogActionListener(new DialogActionAdapter() {

            final ViewToolBarPane val$serverPageToolBarPane;
            final ViewToolBarPane this$0;

            public void doOk()
            {
                ReportWebAttr reportwebattr1 = (ReportWebAttr)ConfigManager.getProviderInstance().getGlobalAttribute(com/fr/web/attr/ReportWebAttr);
                if(reportwebattr1 == null)
                {
                    reportwebattr1 = new ReportWebAttr();
                    ConfigManager.getProviderInstance().putGlobalAttribute(com/fr/web/attr/ReportWebAttr, reportwebattr1);
                }
                reportwebattr1.setWebView(serverPageToolBarPane.updateBean());
            }

            
            {
                this$0 = ViewToolBarPane.this;
                serverPageToolBarPane = viewtoolbarpane1;
                super();
            }
        }
);
        basicdialog.setVisible(true);
    }

    protected WidgetOption[] getToolBarInstance()
    {
        return ReportWebWidgetConstants.getViewToolBarInstance();
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
