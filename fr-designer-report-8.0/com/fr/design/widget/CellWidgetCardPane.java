// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.design.widget.ui.BasicWidgetPropertySettingPane;
import com.fr.form.event.Listener;
import com.fr.form.ui.NoneWidget;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

// Referenced classes of package com.fr.design.widget:
//            WidgetEventPane, WidgetDefinePaneFactory, DataModify, Operator

public class CellWidgetCardPane extends BasicPane
{

    private DataModify currentEditorDefinePane;
    private JTabbedPane tabbedPane;
    private BasicWidgetPropertySettingPane widgetPropertyPane;
    private JPanel attriPane;
    private JPanel cardPane;
    private CardLayout card;
    private JPanel presPane;
    private JPanel cardPaneForPresent;
    private CardLayout cardForPresent;
    private JPanel cardPaneForTreeSetting;
    private JPanel formPane;
    private WidgetEventPane eventTabPane;

    public CellWidgetCardPane(ElementCasePane elementcasepane)
    {
        initComponents(elementcasepane);
    }

    private void initComponents(ElementCasePane elementcasepane)
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        tabbedPane = new UITabbedPane();
        add(tabbedPane, "Center");
        attriPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        formPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        eventTabPane = new WidgetEventPane(elementcasepane);
        formPane.add(eventTabPane, "Center");
        tabbedPane.add(Inter.getLocText("Attribute"), attriPane);
        tabbedPane.add(Inter.getLocText("Form-Editing_Listeners"), formPane);
        presPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        cardPaneForPresent = FRGUIPaneFactory.createCardLayout_S_Pane();
        presPane.add(cardPaneForPresent, "Center");
        cardForPresent = new CardLayout();
        cardPaneForPresent.setLayout(cardForPresent);
        cardPaneForTreeSetting = FRGUIPaneFactory.createBorderLayout_L_Pane();
        widgetPropertyPane = new BasicWidgetPropertySettingPane();
        attriPane.add(widgetPropertyPane, "North");
        cardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        attriPane.add(cardPane, "Center");
        card = (CardLayout)cardPane.getLayout();
        setPreferredSize(new Dimension(600, 450));
    }

    protected String title4PopupWindow()
    {
        return "Widget";
    }

    public void populate(Widget widget)
    {
        currentEditorDefinePane = null;
        if(widget instanceof NoneWidget)
            tabbedPane.setEnabled(false);
        else
            tabbedPane.setEnabled(true);
        attriPane.remove(widgetPropertyPane);
        widgetPropertyPane = new BasicWidgetPropertySettingPane();
        attriPane.add(widgetPropertyPane, "North");
        WidgetDefinePaneFactory.RN rn = WidgetDefinePaneFactory.createWidgetDefinePane(widget, new Operator() {

            final CellWidgetCardPane this$0;

            public void did(DataCreatorUI datacreatorui, String s)
            {
                if(datacreatorui == null)
                {
                    addPresPane(false);
                    addTreeSettingPane(false);
                }
                if(datacreatorui instanceof DictionaryPane)
                    showDictPane(datacreatorui, s);
                else
                if(datacreatorui instanceof TreeSettingPane)
                    showTreePane(datacreatorui);
            }

            
            {
                this$0 = CellWidgetCardPane.this;
                super();
            }
        }
);
        DataModify datamodify = rn.getDefinePane();
        cardPane.add(datamodify.toSwingComponent(), rn.getCardName());
        card.show(cardPane, rn.getCardName());
        currentEditorDefinePane = datamodify;
        eventTabPane.populate(widget);
        widgetPropertyPane.populate(widget);
        tabbedPane.setSelectedIndex(0);
    }

    private void showDictPane(DataCreatorUI datacreatorui, String s)
    {
        cardPaneForPresent.removeAll();
        cardPaneForPresent.add(datacreatorui.toSwingComponent(), s);
        cardForPresent.show(cardPaneForPresent, s);
        addPresPane(true);
    }

    private void showTreePane(DataCreatorUI datacreatorui)
    {
        cardPaneForTreeSetting.removeAll();
        cardPaneForTreeSetting.add(datacreatorui.toSwingComponent());
        addTreeSettingPane(true);
    }

    public Widget update()
    {
        if(currentEditorDefinePane == null)
            return null;
        Widget widget = (Widget)currentEditorDefinePane.updateBean();
        if(widget == null)
            return null;
        widgetPropertyPane.update(widget);
        Listener alistener[] = eventTabPane != null ? eventTabPane.updateListeners() : new Listener[0];
        widget.clearListeners();
        Listener alistener1[] = alistener;
        int i = alistener1.length;
        for(int j = 0; j < i; j++)
        {
            Listener listener = alistener1[j];
            widget.addListener(listener);
        }

        return widget;
    }

    public void checkValid()
        throws Exception
    {
        currentEditorDefinePane.checkValid();
    }

    private void addPresPane(boolean flag)
    {
        if(flag)
        {
            tabbedPane.add(presPane, 1);
            tabbedPane.setTitleAt(1, Inter.getLocText("DS-Dictionary"));
        } else
        {
            tabbedPane.remove(presPane);
        }
    }

    private void addTreeSettingPane(boolean flag)
    {
        if(flag)
        {
            tabbedPane.add(cardPaneForTreeSetting, 1);
            tabbedPane.setTitleAt(1, Inter.getLocText("Create_Tree"));
        } else
        {
            tabbedPane.remove(cardPaneForTreeSetting);
        }
    }




}
