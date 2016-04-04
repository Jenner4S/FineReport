// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.report.web.Location;
import com.fr.report.web.ToolBarManager;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import javax.swing.*;

// Referenced classes of package com.fr.design.webattr:
//            WidgetToolBarPane, ToolBarPane, SettingToolBar, NameOptionSelection, 
//            ToolBarButton

public class DragToolBarPane extends WidgetToolBarPane
{
    private class FromTransferHandler extends TransferHandler
    {

        private int index;
        final DragToolBarPane this$0;

        public int getSourceActions(JComponent jcomponent)
        {
            return 3;
        }

        public Transferable createTransferable(JComponent jcomponent)
        {
            index = toolbarButtonList.getSelectedIndex();
            if(index < 0 || index >= toolbarButtonListModel.getSize())
                return null;
            else
                return new NameOptionSelection((WidgetOption)toolbarButtonList.getSelectedValue());
        }

        public void exportDone(JComponent jcomponent, Transferable transferable, int i)
        {
            if(i != 2)
            {
                return;
            } else
            {
                toolbarButtonListModel.removeElementAt(index);
                return;
            }
        }

        private FromTransferHandler()
        {
            this$0 = DragToolBarPane.this;
            super();
            index = 0;
        }

    }


    private DefaultListModel toolbarButtonListModel;
    private JList toolbarButtonList;
    ListCellRenderer optionRenderer;

    public DragToolBarPane()
    {
        toolbarButtonListModel = new DefaultListModel();
        optionRenderer = new DefaultListCellRenderer() {

            final DragToolBarPane this$0;

            public Component getListCellRendererComponent(JList jlist, Object obj, int i, boolean flag, boolean flag1)
            {
                super.getListCellRendererComponent(jlist, obj, i, flag, flag1);
                if(obj instanceof WidgetOption)
                {
                    WidgetOption widgetoption = (WidgetOption)obj;
                    setText(widgetoption.optionName());
                    javax.swing.Icon icon = widgetoption.optionIcon();
                    if(icon != null)
                        setIcon(icon);
                }
                return this;
            }

            
            {
                this$0 = DragToolBarPane.this;
                super();
            }
        }
;
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        toolbarButtonList = new JList(toolbarButtonListModel);
        jpanel.add(new JScrollPane(new JPanel()));
        toolbarButtonList.addMouseListener(new MouseAdapter() {

            final DragToolBarPane this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                if(mouseevent.getClickCount() > 1 && !SwingUtilities.isRightMouseButton(mouseevent))
                {
                    if(toolbarButtonList.getSelectedValue() == null)
                        return;
                    WidgetOption widgetoption = (WidgetOption)toolbarButtonList.getSelectedValue();
                    com.fr.form.ui.Widget widget = widgetoption.createWidget();
                    ToolBarButton toolbarbutton = new ToolBarButton(widgetoption.optionIcon(), widget);
                    toolbarbutton.setNameOption(widgetoption);
                    northToolBar.add(toolbarbutton);
                    northToolBar.validate();
                    northToolBar.repaint();
                }
            }

            
            {
                this$0 = DragToolBarPane.this;
                super();
            }
        }
);
        toolbarButtonList.setCellRenderer(optionRenderer);
        toolbarButtonList.setDropMode(DropMode.ON_OR_INSERT);
        toolbarButtonList.setDragEnabled(true);
        toolbarButtonList.setTransferHandler(new FromTransferHandler());
        northToolBar = new ToolBarPane();
        northToolBar.setPreferredSize(new Dimension(1, 26));
        northToolBar.setBackground(Color.lightGray);
        southToolBar = new ToolBarPane();
        southToolBar.setPreferredSize(new Dimension(1, 26));
        southToolBar.setBackground(Color.lightGray);
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        SettingToolBar settingtoolbar = new SettingToolBar(Inter.getLocText("ToolBar_Top"), northToolBar);
        jpanel1.add(settingtoolbar, "East");
        jpanel1.add(northToolBar, "Center");
        jpanel1.setBackground(Color.lightGray);
        JPanel jpanel2 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        SettingToolBar settingtoolbar1 = new SettingToolBar(Inter.getLocText("ToolBar_Bottom"), southToolBar);
        jpanel2.add(settingtoolbar1, "East");
        jpanel2.add(southToolBar, "Center");
        jpanel2.setBackground(Color.lightGray);
        JPanel jpanel3 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel3.add(jpanel1, "North");
        jpanel3.add(toolbarButtonList, "Center");
        jpanel3.add(jpanel2, "South");
        add(new JScrollPane(jpanel3), "Center");
        JPanel jpanel4 = FRGUIPaneFactory.createCenterFlowInnerContainer_S_Pane();
        UIButton uibutton = new UIButton(Inter.getLocText("Use_Default_ToolBar"));
        uibutton.addActionListener(new ActionListener() {

            final DragToolBarPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                southToolBar.removeAll();
                southToolBar.removeButtonList();
                southToolBar.repaint();
                northToolBar.removeButtonList();
                if(defaultToolBar == null)
                {
                    return;
                } else
                {
                    ToolBarManager toolbarmanager = defaultToolBar;
                    toolbarmanager.setToolBarLocation(Location.createTopEmbedLocation());
                    ToolBarManager atoolbarmanager[] = {
                        toolbarmanager
                    };
                    populateBean(atoolbarmanager);
                    repaint();
                    return;
                }
            }

            
            {
                this$0 = DragToolBarPane.this;
                super();
            }
        }
);
        jpanel4.add(uibutton);
        UIButton uibutton1 = new UIButton(Inter.getLocText("Remove_All_Button"));
        uibutton1.addActionListener(new ActionListener() {

            final DragToolBarPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                northToolBar.removeAll();
                northToolBar.removeButtonList();
                southToolBar.removeAll();
                southToolBar.removeButtonList();
                southToolBar.repaint();
                northToolBar.repaint();
            }

            
            {
                this$0 = DragToolBarPane.this;
                super();
            }
        }
);
        add(jpanel4, "South");
    }

    public void setDefaultToolBar(ToolBarManager toolbarmanager, WidgetOption awidgetoption[])
    {
        super.setDefaultToolBar(toolbarmanager);
        if(awidgetoption != null)
        {
            for(int i = 0; i < awidgetoption.length; i++)
                toolbarButtonListModel.addElement(awidgetoption[i]);

        }
        toolbarButtonList.validate();
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "ReportServerP-Toolbar", "Set"
        });
    }


}
