// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.base.ConfigManager;
import com.fr.base.ConfigManagerProvider;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.event.Listener;
import com.fr.general.Inter;
import com.fr.report.web.ToolBarManager;
import com.fr.report.web.WebContent;
import com.fr.stable.StringUtils;
import com.fr.web.attr.ReportWebAttr;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.webattr:
//            ToolBarDragPane, EventPane

public abstract class WebSettingPane extends BasicBeanPane
{

    private static final String CHOOSEITEM[] = {
        Inter.getLocText("FR-Designer_I_Want_To_Set_Single"), Inter.getLocText("FR-Designer_Using_Server_Report_View_Settings")
    };
    private EventPane eventPane;
    private ToolBarDragPane dragToolBarPane;
    private UIComboBox choseComboBox;
    private static final int SINGLE_SET = 0;
    private static final int SERVER_SET = 1;
    private static final int ZERO = 0;
    private static final long LONGZERO = 0L;
    ItemListener itemListener;

    public WebSettingPane()
    {
        itemListener = new ItemListener() {

            final WebSettingPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                if(itemevent.getStateChange() == 2)
                    if(choseComboBox.getSelectedIndex() == 0)
                    {
                        checkEnabled(true);
                        setDefault();
                    } else
                    {
                        populateServerSettings();
                        checkEnabled(false);
                    }
            }

            
            {
                this$0 = WebSettingPane.this;
                super();
            }
        }
;
        JPanel jpanel = new JPanel(new FlowLayout(0, 0, 6));
        choseComboBox = new UIComboBox(CHOOSEITEM);
        choseComboBox.addItemListener(itemListener);
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Designer_Blow_set")).append(":").toString()));
        jpanel.add(choseComboBox);
        dragToolBarPane = new ToolBarDragPane();
        dragToolBarPane.setDefaultToolBar(getDefaultToolBarManager(), getToolBarInstance());
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel1.add(new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Designer_Event_Set")).append(':').toString()), "North");
        eventPane = new EventPane(getEventNames());
        jpanel1.add(eventPane, "Center");
        double d = -1D;
        double d1 = -2D;
        double ad[] = {
            d
        };
        JPanel jpanel2 = createOtherSetPane();
        JPanel jpanel3;
        if(jpanel2 != null)
        {
            Component acomponent[][] = {
                {
                    jpanel
                }, {
                    jpanel2
                }, {
                    dragToolBarPane
                }, {
                    jpanel1
                }
            };
            double ad1[] = {
                d1, d1, d1, d
            };
            jpanel3 = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        } else
        {
            Component acomponent1[][] = {
                {
                    jpanel
                }, {
                    dragToolBarPane
                }, {
                    jpanel1
                }
            };
            double ad2[] = {
                d1, d1, d
            };
            jpanel3 = TableLayoutHelper.createTableLayoutPane(acomponent1, ad2, ad);
        }
        setLayout(new BorderLayout());
        JPanel jpanel4 = FRGUIPaneFactory.createBorderLayout_L_Pane();
        jpanel4.add(jpanel3, "Center");
        add(jpanel4, "Center");
    }

    protected void checkEnabled(boolean flag)
    {
        dragToolBarPane.setAllEnabled(flag);
        eventPane.setEnabled(flag);
    }

    protected void setDefault()
    {
        dragToolBarPane.setCheckBoxSelected(true);
        dragToolBarPane.populateBean(new ToolBarManager[0]);
        eventPane.populate(new ArrayList());
    }

    protected abstract JPanel createOtherSetPane();

    protected abstract String[] getEventNames();

    public void populateBean(ReportWebAttr reportwebattr)
    {
        if(reportwebattr == null || getWebContent(reportwebattr) == null)
        {
            choseComboBox.removeItemListener(itemListener);
            choseComboBox.setSelectedIndex(1);
            choseComboBox.addItemListener(itemListener);
            checkEnabled(false);
            populateServerSettings();
            return;
        }
        choseComboBox.removeItemListener(itemListener);
        choseComboBox.setSelectedIndex(0);
        choseComboBox.addItemListener(itemListener);
        checkEnabled(true);
        WebContent webcontent = getWebContent(reportwebattr);
        if(webcontent.getListenerSize() != 0)
        {
            ArrayList arraylist = new ArrayList();
            for(int i = 0; i < webcontent.getListenerSize(); i++)
                arraylist.add(webcontent.getListener(i));

            eventPane.populate(arraylist);
        }
        dragToolBarPane.setCheckBoxSelected(webcontent.isUseToolBar());
        dragToolBarPane.populateBean(webcontent.getToolBarManagers());
        populateSubWebSettingrBean(webcontent);
    }

    public void update(ReportWebAttr reportwebattr)
    {
        if(choseComboBox.getSelectedIndex() == 1)
        {
            setWebContent(reportwebattr, null);
            reportwebattr = is_Null_ReportWebAttr(reportwebattr) ? null : reportwebattr;
        } else
        {
            reportwebattr = TemplateupdateBean(reportwebattr);
        }
    }

    public ReportWebAttr updateBean()
    {
        return null;
    }

    private ReportWebAttr TemplateupdateBean(ReportWebAttr reportwebattr)
    {
        WebContent webcontent = updateSubWebSettingBean();
        ToolBarManager atoolbarmanager[] = dragToolBarPane.updateBean();
        webcontent.setToolBarManagers(atoolbarmanager);
        for(int i = 0; i < eventPane.update().size(); i++)
        {
            Listener listener = (Listener)eventPane.update().get(i);
            webcontent.addListener(listener);
        }

        setWebContent(reportwebattr, webcontent);
        return reportwebattr;
    }

    protected abstract WebContent getWebContent(ReportWebAttr reportwebattr);

    protected abstract void populateSubWebSettingrBean(WebContent webcontent);

    protected abstract WebContent updateSubWebSettingBean();

    protected abstract void setWebContent(ReportWebAttr reportwebattr, WebContent webcontent);

    protected abstract WidgetOption[] getToolBarInstance();

    protected abstract ToolBarManager getDefaultToolBarManager();

    protected String title4PopupWindow()
    {
        return "WebSetting";
    }

    private void populateServerSettings()
    {
        ConfigManagerProvider configmanagerprovider = ConfigManager.getProviderInstance();
        ReportWebAttr reportwebattr = (ReportWebAttr)configmanagerprovider.getGlobalAttribute(com/fr/web/attr/ReportWebAttr);
        WebContent webcontent = getWebContent(reportwebattr);
        if(webcontent == null)
            return;
        ToolBarManager atoolbarmanager[] = webcontent.getToolBarManagers();
        if(webcontent.getListenerSize() != 0)
        {
            ArrayList arraylist = new ArrayList();
            for(int i = 0; i < webcontent.getListenerSize(); i++)
                arraylist.add(webcontent.getListener(i));

            eventPane.populate(arraylist);
        }
        dragToolBarPane.setCheckBoxSelected(webcontent.isUseToolBar());
        dragToolBarPane.populateBean(atoolbarmanager);
        populateSubWebSettingrBean(webcontent);
    }

    public static boolean is_Null_ReportWebAttr(ReportWebAttr reportwebattr)
    {
        if(reportwebattr == null)
            return true;
        else
            return reportwebattr.getBackground() == null && reportwebattr.getCacheValidateTime() == 0L && reportwebattr.getCSSImportCount() == 0 && reportwebattr.getJSImportCount() == 0 && reportwebattr.getPrinter() == null && reportwebattr.getWebPage() == null && reportwebattr.getWebView() == null && reportwebattr.getWebWrite() == null && StringUtils.isEmpty(reportwebattr.getTitle());
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((ReportWebAttr)obj);
    }



}
