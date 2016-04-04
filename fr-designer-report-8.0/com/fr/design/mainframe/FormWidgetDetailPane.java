// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.*;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.parameter.ParameterPropertyPane;
import com.fr.general.Inter;
import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.*;
import javax.swing.border.LineBorder;

// Referenced classes of package com.fr.design.mainframe:
//            FormDockView, MobileWidgetTable, MobileBodyWidgetTable, FormDesigner, 
//            FormSelection, DockingView

public class FormWidgetDetailPane extends FormDockView
{
    public class mobileWidgetDesignerAdapter
        implements DesignerEditListener
    {

        final FormWidgetDetailPane this$0;

        public void fireCreatorModified(DesignerEvent designerevent)
        {
            if(designerevent.getCreatorEventID() == 6 || designerevent.getCreatorEventID() == 5 || designerevent.getCreatorEventID() == 7 || designerevent.getCreatorEventID() == 1)
            {
                int i = downPanel.getVerticalScrollBar().getValue();
                if(hasSelectParaPane(getEditingFormDesigner()))
                {
                    cardLayout.show(centerPane, "para");
                    mobileWidgetTable.refresh();
                } else
                {
                    cardLayout.show(centerPane, "body");
                    mobileBodyWidgetTable.refresh();
                }
                downPanel.doLayout();
                downPanel.getVerticalScrollBar().setValue(i);
            }
        }

        public mobileWidgetDesignerAdapter()
        {
            this$0 = FormWidgetDetailPane.this;
            super();
        }
    }

    private static class HOLDER
    {

        private static FormWidgetDetailPane singleton = new FormWidgetDetailPane();




        private HOLDER()
        {
        }
    }


    public static final String PARA = "para";
    public static final String BODY = "body";
    private UITabbedPane tabbedPane;
    private ParameterPropertyPane parameterPropertyPane;
    private MobileWidgetTable mobileWidgetTable;
    private MobileBodyWidgetTable mobileBodyWidgetTable;
    private UIScrollPane downPanel;
    private JPanel centerPane;
    private CardLayout cardLayout;

    public static FormWidgetDetailPane getInstance()
    {
        if(HOLDER.singleton == null)
            HOLDER.singleton = new FormWidgetDetailPane();
        return HOLDER.singleton;
    }

    private FormWidgetDetailPane()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
    }

    public static FormWidgetDetailPane getInstance(FormDesigner formdesigner)
    {
        HOLDER.singleton.setEditingFormDesigner(formdesigner);
        HOLDER.singleton.refreshDockingView();
        return HOLDER.singleton;
    }

    public String getViewTitle()
    {
        return Inter.getLocText("FR-Widget_Tree_And_Table");
    }

    public Icon getViewIcon()
    {
        return BaseUtils.readIcon("/com/fr/design/images/m_report/attributes.png");
    }

    public void refreshDockingView()
    {
        FormDesigner formdesigner = getEditingFormDesigner();
        removeAll();
        if(formdesigner == null)
        {
            clearDockingView();
            return;
        }
        parameterPropertyPane = ParameterPropertyPane.getInstance(formdesigner);
        parameterPropertyPane.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.setBorder(null);
        mobileWidgetTable = new MobileWidgetTable(formdesigner);
        mobileBodyWidgetTable = new MobileBodyWidgetTable(formdesigner);
        formdesigner.addDesignerEditListener(new mobileWidgetDesignerAdapter());
        centerPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        cardLayout = (CardLayout)centerPane.getLayout();
        centerPane.add(mobileWidgetTable, "para");
        centerPane.add(mobileBodyWidgetTable, "body");
        if(hasSelectParaPane(formdesigner))
            cardLayout.show(centerPane, "para");
        else
            cardLayout.show(centerPane, "body");
        downPanel = new UIScrollPane(centerPane);
        downPanel.setBorder(new LineBorder(Color.gray));
        jpanel.add(downPanel, "Center");
        UILabel uilabel = new UILabel(Inter.getLocText("FR-Widget_Mobile_Table"), 0);
        uilabel.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        jpanel.add(uilabel, "North");
        tabbedPane = new UITabbedPane();
        tabbedPane.setOpaque(true);
        tabbedPane.setBorder(null);
        tabbedPane.setTabPlacement(3);
        tabbedPane.addTab(Inter.getLocText("FR-Widget_Mobile_Tree"), parameterPropertyPane);
        tabbedPane.addTab(Inter.getLocText("FR-Widget_Mobile_Terminal"), jpanel);
        add(tabbedPane, "Center");
    }

    public void setSelectedIndex(int i)
    {
        tabbedPane.setSelectedIndex(i);
    }

    public boolean hasSelectParaPane(FormDesigner formdesigner)
    {
        Object obj = formdesigner.getSelectionModel().getSelection().getSelectedCreator();
        if(obj == null)
            obj = formdesigner.getRootComponent();
        XLayoutContainer xlayoutcontainer = XCreatorUtils.getHotspotContainer(((XCreator) (obj)));
        return ((XCreator) (obj)).acceptType(new Class[] {
            com/fr/design/designer/creator/XWParameterLayout
        }) || xlayoutcontainer.acceptType(new Class[] {
            com/fr/design/designer/creator/XWParameterLayout
        });
    }

    public void clearDockingView()
    {
        parameterPropertyPane = null;
        mobileWidgetTable = null;
        JScrollPane jscrollpane = new JScrollPane();
        jscrollpane.setBorder(null);
        add(jscrollpane, "Center");
    }

    public DockingView.Location preferredLocation()
    {
        return DockingView.Location.WEST_BELOW;
    }






}
