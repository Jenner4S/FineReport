// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.ConfigManager;
import com.fr.base.ConfigManagerProvider;
import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.toolbar.AuthorityEditToolBarComponent;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.webattr.ReportWebWidgetConstants;
import com.fr.design.webattr.ToolBarButton;
import com.fr.design.webattr.ToolBarPane;
import com.fr.form.ui.Button;
import com.fr.form.ui.ToolBar;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.main.TemplateWorkBook;
import com.fr.privilege.finegrain.WidgetPrivilegeControl;
import com.fr.report.web.Location;
import com.fr.report.web.ToolBarManager;
import com.fr.report.web.WebContent;
import com.fr.stable.ArrayUtils;
import com.fr.web.attr.ReportWebAttr;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe:
//            AuthorityEditToolBarPane, JWorkBook, JTemplate, EastRegionContainerPane

public class AuthorityToolBarPane extends BasicBeanPane
    implements AuthorityEditToolBarComponent
{

    private static final int SMALL_GAP = 13;
    private static final int GAP = 25;
    private static final int PRE_GAP = 9;
    private static final int COMBOX_WIDTH = 144;
    private static final String CHOOSEITEM[] = {
        Inter.getLocText("M-Page_Preview"), Inter.getLocText(new String[] {
            "Face_Write", "PageSetup-Page"
        }), Inter.getLocText("M-Data_Analysis")
    };
    private UIComboBox choseComboBox;
    private ToolBarPane toolBarPane;
    private AuthorityEditToolBarPane authorityEditToolBarPane;
    private int selectedIndex;
    private UILabel title;
    private MouseListener mouseListener;
    private ItemListener itemListener;

    private int pressButtonIndex(MouseEvent mouseevent, java.util.List list)
    {
        if(!(mouseevent.getSource() instanceof ToolBarButton))
            return -1;
        ToolBarButton toolbarbutton = (ToolBarButton)mouseevent.getSource();
        for(int i = 0; i < list.size(); i++)
            if(ComparatorUtils.equals(toolbarbutton, list.get(i)))
                return i;

        return -1;
    }

    public void removeSelection()
    {
        ToolBarButton toolbarbutton;
        for(Iterator iterator = toolBarPane.getToolBarButtons().iterator(); iterator.hasNext(); toolbarbutton.setSelected(false))
            toolbarbutton = (ToolBarButton)iterator.next();

    }

    public AuthorityToolBarPane()
    {
        authorityEditToolBarPane = null;
        selectedIndex = -1;
        title = null;
        mouseListener = new MouseAdapter() {

            final AuthorityToolBarPane this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                if(!toolBarPane.isEnabled())
                    return;
                java.util.List list = toolBarPane.getToolBarButtons();
                int i = selectedIndex;
                selectedIndex = pressButtonIndex(mouseevent, list);
                if(mouseevent.isShiftDown())
                {
                    if(i == -1)
                    {
                        removeSelection();
                        ((ToolBarButton)mouseevent.getSource()).setSelected(true);
                    } else
                    {
                        int j = i < selectedIndex ? selectedIndex : i;
                        int k = i > selectedIndex ? selectedIndex : i;
                        for(int l = k; l <= j; l++)
                            ((ToolBarButton)list.get(l)).setSelected(true);

                    }
                } else
                if(!mouseevent.isControlDown())
                {
                    removeSelection();
                    if(selectedIndex != -1)
                        ((ToolBarButton)mouseevent.getSource()).setSelected(true);
                }
                authorityEditToolBarPane.populate();
                EastRegionContainerPane.getInstance().replaceUpPane(authorityEditToolBarPane);
            }

            
            {
                this$0 = AuthorityToolBarPane.this;
                super();
            }
        }
;
        itemListener = new ItemListener() {

            final AuthorityToolBarPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                if(itemevent.getStateChange() == 2)
                {
                    selectedIndex = -1;
                    populateToolBarPane();
                    authorityEditToolBarPane = new AuthorityEditToolBarPane(toolBarPane.getToolBarButtons());
                    authorityEditToolBarPane.setAuthorityToolBarPane(AuthorityToolBarPane.this);
                    EastRegionContainerPane.getInstance().replaceUpPane(authorityEditToolBarPane);
                    EastRegionContainerPane.getInstance().replaceDownPane(RolesAlreadyEditedPane.getInstance());
                }
            }

            
            {
                this$0 = AuthorityToolBarPane.this;
                super();
            }
        }
;
        setLayout(new FlowLayout(0, 0, 3));
        setBorder(BorderFactory.createEmptyBorder(0, 9, 0, 0));
        title = new UILabel(Inter.getLocText(new String[] {
            "ReportServerP-Toolbar", "Choose_Role"
        }));
        title.setHorizontalAlignment(0);
        add(title, 0);
        choseComboBox = new UIComboBox(CHOOSEITEM) {

            final AuthorityToolBarPane this$0;

            public Dimension getPreferredSize()
            {
                Dimension dimension = super.getPreferredSize();
                dimension.width = 144;
                return dimension;
            }

            
            {
                this$0 = AuthorityToolBarPane.this;
                super(aobj);
            }
        }
;
        choseComboBox.addItemListener(itemListener);
        choseComboBox.setSelectedIndex(0);
        add(createGapPanel(13));
        add(choseComboBox);
        toolBarPane = new ToolBarPane();
        toolBarPane.setBorder(null);
        toolBarPane.removeDefaultMouseListener();
        add(createGapPanel(25));
        add(toolBarPane);
        populateDefaultToolBarWidgets();
        populateBean(getReportWebAttr());
        toolBarPane.addAuthorityListener(mouseListener);
        authorityEditToolBarPane = new AuthorityEditToolBarPane(toolBarPane.getToolBarButtons());
        authorityEditToolBarPane.setAuthorityToolBarPane(this);
        checkToolBarPaneEnable();
    }

    private JPanel createGapPanel(final int gap)
    {
        return new JPanel() {

            final int val$gap;
            final AuthorityToolBarPane this$0;

            public Dimension getPreferredSize()
            {
                Dimension dimension = super.getPreferredSize();
                dimension.width = gap;
                return dimension;
            }

            
            {
                this$0 = AuthorityToolBarPane.this;
                gap = i;
                super();
            }
        }
;
    }

    private void populateToolBarPane()
    {
        toolBarPane.removeAll();
        populateDefaultToolBarWidgets();
        populateBean(getReportWebAttr());
        toolBarPane.addAuthorityListener(mouseListener);
        toolBarPane.repaint();
        authorityEditToolBarPane = new AuthorityEditToolBarPane(toolBarPane.getToolBarButtons());
        checkToolBarPaneEnable();
    }

    private void checkToolBarPaneEnable()
    {
        java.util.List list = toolBarPane.getToolBarButtons();
        boolean flag = ComparatorUtils.equals(title.getText(), Inter.getLocText(new String[] {
            "Server", "ReportServerP-Toolbar", "Choose_Role"
        })) && !FRContext.getCurrentEnv().isRoot();
        ToolBarButton toolbarbutton;
        for(Iterator iterator = list.iterator(); iterator.hasNext(); toolbarbutton.setEnabled(!flag))
            toolbarbutton = (ToolBarButton)iterator.next();

        toolBarPane.setEnabled(!flag);
    }

    public void populateAuthority()
    {
        toolBarPane.repaint();
    }

    private ReportWebAttr getReportWebAttr()
    {
        JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if(!jtemplate.isJWorkBook())
        {
            return null;
        } else
        {
            JWorkBook jworkbook = (JWorkBook)jtemplate;
            TemplateWorkBook templateworkbook = (TemplateWorkBook)jworkbook.getTarget();
            return templateworkbook.getReportWebAttr();
        }
    }

    public void setAuthorityWebAttr(Widget widget, boolean flag, String s)
    {
        JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if(!jtemplate.isJWorkBook())
            return;
        JWorkBook jworkbook = (JWorkBook)jtemplate;
        TemplateWorkBook templateworkbook = (TemplateWorkBook)jworkbook.getTarget();
        ReportWebAttr reportwebattr = templateworkbook.getReportWebAttr();
        ConfigManagerProvider configmanagerprovider = ConfigManager.getProviderInstance();
        ReportWebAttr reportwebattr1 = (ReportWebAttr)configmanagerprovider.getGlobalAttribute(com/fr/web/attr/ReportWebAttr);
        if(choseComboBox.getSelectedIndex() == 0)
        {
            if(reportwebattr == null || reportwebattr.getWebPage() == null)
                dealWithWebContent(reportwebattr1.getWebPage(), widget, flag, s);
        } else
        if(choseComboBox.getSelectedIndex() == 1)
        {
            if(reportwebattr == null || reportwebattr.getWebPage() == null)
                dealWithWebContent(reportwebattr1.getWebWrite(), widget, flag, s);
        } else
        if(reportwebattr == null || reportwebattr.getWebPage() == null)
            dealWithWebContent(reportwebattr1.getWebView(), widget, flag, s);
        try
        {
            FRContext.getCurrentEnv().writeResource(configmanagerprovider);
        }
        catch(Exception exception)
        {
            FRLogger.getLogger().error(exception.getMessage());
        }
    }

    private void dealWithWebContent(WebContent webcontent, Widget widget, boolean flag, String s)
    {
        ToolBarManager atoolbarmanager[] = webcontent.getToolBarManagers();
        if(atoolbarmanager == null)
            return;
        for(int i = 0; i < atoolbarmanager.length; i++)
        {
            ToolBar toolbar = atoolbarmanager[i].getToolBar();
            for(int j = 0; j < toolbar.getWidgetSize(); j++)
            {
                if(!(widget instanceof Button) || !(toolbar.getWidget(j) instanceof Button) || !ComparatorUtils.equals(((Button)widget).getIconName(), ((Button)toolbar.getWidget(j)).getIconName()))
                    continue;
                if(!flag)
                    toolbar.getWidget(j).getWidgetPrivilegeControl().addInvisibleRole(s);
                else
                    toolbar.getWidget(j).getWidgetPrivilegeControl().removeInvisibleRole(s);
            }

        }

        webcontent.setToolBarManagers(atoolbarmanager);
    }

    public void populateBean(ReportWebAttr reportwebattr)
    {
        remove(title);
        if(reportwebattr == null || getWebContent(reportwebattr) == null)
        {
            title = new UILabel(Inter.getLocText(new String[] {
                "Server", "ReportServerP-Toolbar", "Choose_Role"
            }));
            populateServerSettings();
            add(title, 0);
            return;
        } else
        {
            WebContent webcontent = getWebContent(reportwebattr);
            title = new UILabel(Inter.getLocText(new String[] {
                "the_template", "ReportServerP-Toolbar", "Choose_Role"
            }));
            add(title, 0);
            populate(webcontent.getToolBarManagers());
            return;
        }
    }

    public ReportWebAttr updateBean()
    {
        return null;
    }

    public void populate(ToolBarManager atoolbarmanager[])
    {
        if(ArrayUtils.isEmpty(atoolbarmanager))
            return;
        if(atoolbarmanager.length == 0)
            return;
        for(int i = 0; i < atoolbarmanager.length; i++)
            toolBarPane.populateBean(atoolbarmanager[i].getToolBar());

    }

    public Dimension getPreferredSize()
    {
        Dimension dimension = super.getPreferredSize();
        dimension.height = 26;
        return dimension;
    }

    public void populateBean(ToolBarManager atoolbarmanager[])
    {
        if(ArrayUtils.isEmpty(atoolbarmanager))
            return;
        for(int i = 0; i < atoolbarmanager.length; i++)
        {
            Location location = atoolbarmanager[i].getToolBarLocation();
            if(location instanceof com.fr.report.web.Location.Embed)
                toolBarPane.populateBean(atoolbarmanager[i].getToolBar());
        }

    }

    private void populateServerSettings()
    {
        ConfigManagerProvider configmanagerprovider = ConfigManager.getProviderInstance();
        ReportWebAttr reportwebattr = (ReportWebAttr)configmanagerprovider.getGlobalAttribute(com/fr/web/attr/ReportWebAttr);
        if(getWebContent(reportwebattr) != null)
            populate(getWebContent(reportwebattr).getToolBarManagers());
    }

    protected String title4PopupWindow()
    {
        return null;
    }

    private WebContent getWebContent(ReportWebAttr reportwebattr)
    {
        if(choseComboBox.getSelectedIndex() == 0)
            return reportwebattr != null ? reportwebattr.getWebPage() : null;
        if(choseComboBox.getSelectedIndex() == 1)
            return reportwebattr != null ? reportwebattr.getWebWrite() : null;
        else
            return reportwebattr != null ? reportwebattr.getWebView() : null;
    }

    private void populateDefaultToolBarWidgets()
    {
        if(choseComboBox.getSelectedIndex() == 0)
            ReportWebWidgetConstants.getPageToolBarInstance();
        else
        if(choseComboBox.getSelectedIndex() == 1)
            ReportWebWidgetConstants.getWriteToolBarInstance();
        else
            ReportWebWidgetConstants.getViewToolBarInstance();
    }

    private ToolBarManager getDefaultToolBarManager()
    {
        if(choseComboBox.getSelectedIndex() == 0)
            return ToolBarManager.createDefaultToolBar();
        if(choseComboBox.getSelectedIndex() == 1)
            return ToolBarManager.createDefaultWriteToolBar();
        else
            return ToolBarManager.createDefaultViewToolBar();
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
