// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.base.ConfigManager;
import com.fr.base.ConfigManagerProvider;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIColorButton;
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
import com.fr.report.web.WebWrite;
import com.fr.web.attr.ReportWebAttr;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.webattr:
//            AbstractEditToolBarPane, EventPane, ReportWebWidgetConstants, DragToolBarPane

public class WriteToolBarPane extends AbstractEditToolBarPane
{

    private EventPane eventPane;
    private UICheckBox colorBox;
    private UIColorButton colorButton;
    private DragToolBarPane dragToolbarPane;
    private UIRadioButton topRadioButton;
    private UIRadioButton bottomRadioButton;
    private UILabel sheetShowLocationLabel;
    private UIRadioButton centerRadioButton;
    private UIRadioButton leftRadioButton;
    private UILabel rptShowLocationLabel;
    private UICheckBox isUseToolBarCheckBox;
    private UIButton editToolBarButton;
    private UILabel showListenersLabel;
    private UICheckBox unloadCheck;
    private UICheckBox showWidgets;
    private UICheckBox isAutoStash;
    private ActionListener editBtnListener;
    private ActionListener colorListener;

    public WriteToolBarPane()
    {
        topRadioButton = new UIRadioButton(Inter.getLocText("FR-Designer_Top"));
        bottomRadioButton = new UIRadioButton(Inter.getLocText("FR-Designer_Bottom"));
        sheetShowLocationLabel = new UILabel((new StringBuilder()).append("sheet").append(Inter.getLocText(new String[] {
            "Label", "Page_Number", "Display position"
        })).append(":").toString());
        centerRadioButton = new UIRadioButton(Inter.getLocText(new String[] {
            "Center", "Display"
        }));
        leftRadioButton = new UIRadioButton(Inter.getLocText(new String[] {
            "Left", "Display"
        }));
        rptShowLocationLabel = new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Designer_Report_Show_Location")).append(":").toString(), 2);
        isUseToolBarCheckBox = new UICheckBox(Inter.getLocText("FR-Designer_Use_ToolBar"));
        editToolBarButton = new UIButton(Inter.getLocText("FR-Designer_Edit"));
        showListenersLabel = new UILabel((new StringBuilder()).append(Inter.getLocText("Form-Editing_Listeners")).append(":").toString());
        editBtnListener = new ActionListener() {

            final WriteToolBarPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                final DragToolBarPane dragToolbarPane = new DragToolBarPane();
                dragToolbarPane.setDefaultToolBar(ToolBarManager.createDefaultWriteToolBar(), ReportWebWidgetConstants.getWriteToolBarInstance());
                dragToolbarPane.populateBean(toolBarManagers);
                BasicDialog basicdialog = dragToolbarPane.showWindow(SwingUtilities.getWindowAncestor(WriteToolBarPane.this));
                basicdialog.addDialogActionListener(new DialogActionAdapter() {

                    final DragToolBarPane val$dragToolbarPane;
                    final _cls2 this$1;

                    public void doOk()
                    {
                        toolBarManagers = dragToolbarPane.updateBean();
                    }

                    
                    {
                        this$1 = _cls2.this;
                        dragToolbarPane = dragtoolbarpane;
                        super();
                    }
                }
);
                basicdialog.setVisible(true);
            }

            
            {
                this$0 = WriteToolBarPane.this;
                super();
            }
        }
;
        colorListener = new ActionListener() {

            final WriteToolBarPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                colorButton.setEnabled(colorBox.isSelected());
            }

            
            {
                this$0 = WriteToolBarPane.this;
                super();
            }
        }
;
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        add(jpanel, "Center");
        JPanel jpanel1 = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(2);
        jpanel.add(jpanel1, "North");
        ButtonGroup buttongroup = new ButtonGroup();
        bottomRadioButton.setSelected(true);
        buttongroup.add(topRadioButton);
        buttongroup.add(bottomRadioButton);
        jpanel1.add(GUICoreUtils.createFlowPane(new Component[] {
            sheetShowLocationLabel, topRadioButton, bottomRadioButton
        }, 0));
        ButtonGroup buttongroup1 = new ButtonGroup();
        leftRadioButton.setSelected(true);
        buttongroup1.add(leftRadioButton);
        buttongroup1.add(centerRadioButton);
        jpanel1.add(GUICoreUtils.createFlowPane(new Component[] {
            rptShowLocationLabel, centerRadioButton, leftRadioButton
        }, 0));
        colorBox = new UICheckBox((new StringBuilder()).append(Inter.getLocText(new String[] {
            "Face_Write", "Current", "Edit", "Row", "Background", "Set"
        })).append(":").toString());
        colorBox.setSelected(true);
        colorBox.addActionListener(colorListener);
        colorButton = new UIColorButton(BaseUtils.readIcon("/com/fr/design/images/gui/color/background.png"));
        jpanel1.add(GUICoreUtils.createFlowPane(new Component[] {
            colorBox, colorButton
        }, 0));
        unloadCheck = new UICheckBox(Inter.getLocText(new String[] {
            "Event-unloadcheck", "Tooltips"
        }));
        unloadCheck.setSelected(true);
        showWidgets = new UICheckBox(Inter.getLocText("FR-Designer_Event_ShowWidgets"));
        showWidgets.setSelected(false);
        isAutoStash = new UICheckBox(Inter.getLocText("FR-Designer-Write_Auto_Stash"));
        isAutoStash.setSelected(false);
        jpanel1.add(GUICoreUtils.createFlowPane(new Component[] {
            unloadCheck, showWidgets, isAutoStash
        }, 0));
        editToolBarButton.addActionListener(editBtnListener);
        isUseToolBarCheckBox.setSelected(true);
        isUseToolBarCheckBox.addActionListener(new ActionListener() {

            final WriteToolBarPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                editToolBarButton.setEnabled(isUseToolBarCheckBox.isSelected());
            }

            
            {
                this$0 = WriteToolBarPane.this;
                super();
            }
        }
);
        jpanel1.add(GUICoreUtils.createFlowPane(new Component[] {
            isUseToolBarCheckBox, editToolBarButton
        }, 0));
        jpanel1.add(new UILabel());
        jpanel1.add(GUICoreUtils.createFlowPane(showListenersLabel, 0));
        eventPane = new EventPane((new WebWrite()).supportedEvents());
        JPanel jpanel2 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel2.add(eventPane, "Center");
        jpanel.add(jpanel2, "Center");
        ToolBarManager toolbarmanager = ToolBarManager.createDefaultWriteToolBar();
        toolbarmanager.setToolBarLocation(Location.createTopEmbedLocation());
        toolBarManagers = (new ToolBarManager[] {
            toolbarmanager
        });
    }

    protected WidgetOption[] getToolBarInstance()
    {
        return ReportWebWidgetConstants.getWriteToolBarInstance();
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("WEB-Write_Setting");
    }

    public void populateBean(WebContent webcontent)
    {
        if(webcontent == null)
            webcontent = new WebWrite();
        WebWrite webwrite = (WebWrite)webcontent;
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
        if(webwrite.isUseToolBar())
        {
            toolBarManagers = webwrite.getToolBarManagers();
            isUseToolBarCheckBox.setSelected(true);
            editToolBarButton.setEnabled(true);
        } else
        {
            isUseToolBarCheckBox.setSelected(false);
            editToolBarButton.setEnabled(false);
        }
        if(webwrite.getListenerSize() != 0)
        {
            ArrayList arraylist = new ArrayList();
            for(int i = 0; i < webwrite.getListenerSize(); i++)
                arraylist.add(webwrite.getListener(i));

            eventPane.populate(arraylist);
        }
    }

    public WebWrite updateBean()
    {
        WebWrite webwrite = new WebWrite();
        if(isUseToolBarCheckBox.isSelected())
            webwrite.setToolBarManagers(toolBarManagers);
        else
            webwrite.setToolBarManagers(new ToolBarManager[0]);
        if(colorBox.isSelected())
            webwrite.setSelectedColor(colorButton.getColor());
        else
            webwrite.setSelectedColor(null);
        webwrite.setUnloadCheck(unloadCheck.isSelected());
        webwrite.setShowWidgets(showWidgets.isSelected());
        webwrite.setViewAtLeft(leftRadioButton.isSelected());
        webwrite.setAutoStash(isAutoStash.isSelected());
        if(topRadioButton.isSelected())
            webwrite.setSheetPosition(1);
        else
        if(bottomRadioButton.isSelected())
            webwrite.setSheetPosition(3);
        for(int i = 0; i < eventPane.update().size(); i++)
            webwrite.addListener((Listener)eventPane.update().get(i));

        return webwrite;
    }

    public void setEnabled(boolean flag)
    {
        super.setEnabled(flag);
        eventPane.setEnabled(flag);
        topRadioButton.setEnabled(flag);
        bottomRadioButton.setEnabled(flag);
        centerRadioButton.setEnabled(flag);
        leftRadioButton.setEnabled(flag);
        isUseToolBarCheckBox.setEnabled(flag);
        editToolBarButton.setEnabled(flag && isUseToolBarCheckBox.isSelected());
        colorBox.setEnabled(flag);
        colorButton.setEnabled(flag && colorBox.isSelected());
        showListenersLabel.setEnabled(flag);
        unloadCheck.setEnabled(flag);
        showWidgets.setEnabled(flag);
        isAutoStash.setEnabled(flag);
    }

    public void editServerToolBarPane()
    {
        final WriteToolBarPane serverPageToolBarPane = new WriteToolBarPane();
        ReportWebAttr reportwebattr = (ReportWebAttr)ConfigManager.getProviderInstance().getGlobalAttribute(com/fr/web/attr/ReportWebAttr);
        if(reportwebattr != null)
            serverPageToolBarPane.populateBean(reportwebattr.getWebWrite());
        BasicDialog basicdialog = serverPageToolBarPane.showWindow(SwingUtilities.getWindowAncestor(this));
        basicdialog.addDialogActionListener(new DialogActionAdapter() {

            final WriteToolBarPane val$serverPageToolBarPane;
            final WriteToolBarPane this$0;

            public void doOk()
            {
                ReportWebAttr reportwebattr1 = (ReportWebAttr)ConfigManager.getProviderInstance().getGlobalAttribute(com/fr/web/attr/ReportWebAttr);
                if(reportwebattr1 == null)
                {
                    reportwebattr1 = new ReportWebAttr();
                    ConfigManager.getProviderInstance().putGlobalAttribute(com/fr/web/attr/ReportWebAttr, reportwebattr1);
                }
                reportwebattr1.setWebWrite(serverPageToolBarPane.updateBean());
            }

            
            {
                this$0 = WriteToolBarPane.this;
                serverPageToolBarPane = writetoolbarpane1;
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
