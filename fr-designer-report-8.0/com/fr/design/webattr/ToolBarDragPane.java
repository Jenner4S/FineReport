// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.web.Location;
import com.fr.report.web.ToolBarManager;
import com.fr.stable.ArrayUtils;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

// Referenced classes of package com.fr.design.webattr:
//            WidgetToolBarPane, ToolBarPane, SettingToolBar, ToolBarButton

public class ToolBarDragPane extends WidgetToolBarPane
{
    private class TableModel extends DefaultTableModel
    {

        final ToolBarDragPane this$0;

        public boolean isCellEditable(int i, int j)
        {
            return false;
        }

        public TableModel(int i, int j)
        {
            this$0 = ToolBarDragPane.this;
            super(i, j);
        }
    }


    private static final int COLUMN = 4;
    private int row;
    private DefaultTableModel toolbarButtonTableModel;
    private JTable layoutTable;
    private UICheckBox isUseToolBarCheckBox;
    private boolean isEnabled;
    DefaultTableCellRenderer tableRenderer;

    public ToolBarDragPane()
    {
        row = 6;
        isUseToolBarCheckBox = new UICheckBox((new StringBuilder()).append(Inter.getLocText("FR-Designer_Use_ToolBar")).append(":").toString());
        tableRenderer = new DefaultTableCellRenderer() {

            final ToolBarDragPane this$0;

            public Component getTableCellRendererComponent(JTable jtable, Object obj, boolean flag, boolean flag1, int i, int j)
            {
                super.getTableCellRendererComponent(jtable, obj, flag, flag1, i, j);
                if(obj instanceof WidgetOption)
                {
                    WidgetOption widgetoption = (WidgetOption)obj;
                    setText(widgetoption.optionName());
                    javax.swing.Icon icon = widgetoption.optionIcon();
                    if(icon != null)
                        setIcon(icon);
                }
                if(obj == null)
                {
                    setText("");
                    setIcon(null);
                }
                return this;
            }

            
            {
                this$0 = ToolBarDragPane.this;
                super();
            }
        }
;
        WidgetOption awidgetoption[] = ExtraDesignClassManager.getInstance().getWebWidgetOptions();
        if(awidgetoption != null)
        {
            double d = awidgetoption.length;
            row += Math.ceil(d / 4D);
        }
        toolbarButtonTableModel = new TableModel(row, 4);
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        UIButton uibutton = new UIButton(Inter.getLocText("FR-Designer_Restore_Default"));
        uibutton.addActionListener(new ActionListener() {

            final ToolBarDragPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                northToolBar.removeButtonList();
                northToolBar.removeAll();
                southToolBar.removeButtonList();
                southToolBar.removeAll();
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
                this$0 = ToolBarDragPane.this;
                super();
            }
        }
);
        jpanel.add(isUseToolBarCheckBox, "West");
        JPanel jpanel1 = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
        jpanel1.add(uibutton);
        jpanel.add(jpanel1, "Center");
        add(jpanel, "North");
        northToolBar = new ToolBarPane();
        northToolBar.setPreferredSize(new Dimension(1, 26));
        northToolBar.setBackground(Color.lightGray);
        UIButton uibutton1 = new UIButton(BaseUtils.readIcon("com/fr/design/images/arrow/arrow_up.png"));
        uibutton1.setBorder(null);
        uibutton1.setOpaque(false);
        uibutton1.setContentAreaFilled(false);
        uibutton1.setFocusPainted(false);
        uibutton1.setRequestFocusEnabled(false);
        uibutton1.addActionListener(new ActionListener() {

            final ToolBarDragPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(isSelectedtable())
                {
                    WidgetOption widgetoption = (WidgetOption)layoutTable.getValueAt(layoutTable.getSelectedRow(), layoutTable.getSelectedColumn());
                    com.fr.form.ui.Widget widget = widgetoption.createWidget();
                    ToolBarButton toolbarbutton = new ToolBarButton(widgetoption.optionIcon(), widget);
                    toolbarbutton.setNameOption(widgetoption);
                    northToolBar.add(toolbarbutton);
                    northToolBar.validate();
                    northToolBar.repaint();
                } else
                {
                    JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("FR-Designer_ChooseOneButton"));
                }
            }

            
            {
                this$0 = ToolBarDragPane.this;
                super();
            }
        }
);
        UIButton uibutton2 = new UIButton(BaseUtils.readIcon("com/fr/design/images/arrow/arrow_down.png"));
        uibutton2.setBorder(null);
        uibutton2.setMargin(null);
        uibutton2.setOpaque(false);
        uibutton2.setContentAreaFilled(false);
        uibutton2.setFocusPainted(false);
        uibutton2.setRequestFocusEnabled(false);
        uibutton2.addActionListener(new ActionListener() {

            final ToolBarDragPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(isSelectedtable())
                {
                    WidgetOption widgetoption = (WidgetOption)layoutTable.getValueAt(layoutTable.getSelectedRow(), layoutTable.getSelectedColumn());
                    com.fr.form.ui.Widget widget = widgetoption.createWidget();
                    ToolBarButton toolbarbutton = new ToolBarButton(widgetoption.optionIcon(), widget);
                    toolbarbutton.setNameOption(widgetoption);
                    southToolBar.add(toolbarbutton);
                    southToolBar.validate();
                    southToolBar.repaint();
                } else
                {
                    JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("FR-Designer_ChooseOneButton"));
                }
            }

            
            {
                this$0 = ToolBarDragPane.this;
                super();
            }
        }
);
        layoutTable = new JTable(toolbarButtonTableModel);
        layoutTable.setDefaultRenderer(java/lang/Object, tableRenderer);
        layoutTable.setSelectionMode(0);
        layoutTable.setColumnSelectionAllowed(false);
        layoutTable.setRowSelectionAllowed(false);
        layoutTable.setBackground(Color.white);
        layoutTable.addMouseListener(new MouseAdapter() {

            final ToolBarDragPane this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                if(mouseevent.getClickCount() > 1 && !SwingUtilities.isRightMouseButton(mouseevent) && isEnabled)
                {
                    WidgetOption widgetoption = (WidgetOption)layoutTable.getValueAt(layoutTable.getSelectedRow(), layoutTable.getSelectedColumn());
                    com.fr.form.ui.Widget widget = widgetoption.createWidget();
                    ToolBarButton toolbarbutton = new ToolBarButton(widgetoption.optionIcon(), widget);
                    toolbarbutton.setNameOption(widgetoption);
                    northToolBar.add(toolbarbutton);
                    northToolBar.validate();
                    northToolBar.repaint();
                }
            }

            
            {
                this$0 = ToolBarDragPane.this;
                super();
            }
        }
);
        JPanel jpanel2 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel2.setBackground(Color.white);
        jpanel2.add(uibutton1, "North");
        JPanel jpanel3 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel3.setBackground(Color.white);
        jpanel3.add(new UILabel(" "), "North");
        jpanel3.add(layoutTable, "Center");
        jpanel2.add(jpanel3, "Center");
        jpanel2.add(uibutton2, "South");
        southToolBar = new ToolBarPane();
        southToolBar.setPreferredSize(new Dimension(1, 26));
        southToolBar.setBackground(Color.lightGray);
        JPanel jpanel4 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel jpanel5 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        SettingToolBar settingtoolbar = new SettingToolBar(Inter.getLocText("FR-Designer_ToolBar_Top"), northToolBar);
        jpanel5.add(settingtoolbar, "East");
        jpanel5.add(northToolBar, "Center");
        jpanel5.setBackground(Color.lightGray);
        JPanel jpanel6 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        SettingToolBar settingtoolbar1 = new SettingToolBar(Inter.getLocText("FR-Designer_ToolBar_Bottom"), southToolBar);
        jpanel6.add(settingtoolbar1, "East");
        jpanel6.add(southToolBar, "Center");
        jpanel6.setBackground(Color.lightGray);
        jpanel4.add(jpanel5, "North");
        jpanel4.add(jpanel2, "Center");
        jpanel4.add(jpanel6, "South");
        add(new JScrollPane(jpanel4), "Center");
        isUseToolBarCheckBox.setSelected(true);
    }

    private boolean isSelectedtable()
    {
        for(int i = 0; i < layoutTable.getColumnCount(); i++)
            if(layoutTable.isColumnSelected(i))
                return true;

        return false;
    }

    public void setAllEnabled(boolean flag)
    {
        GUICoreUtils.setEnabled(this, flag);
        isEnabled = flag;
    }

    public boolean isUseToolbar()
    {
        return isUseToolBarCheckBox.isSelected();
    }

    public void setDefaultToolBar(ToolBarManager toolbarmanager, WidgetOption awidgetoption[])
    {
        super.setDefaultToolBar(toolbarmanager);
        if(awidgetoption != null)
        {
            for(int i = 0; i < awidgetoption.length; i++)
                toolbarButtonTableModel.setValueAt(awidgetoption[i], i % row, i / row);

        }
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "ReportServerP-Toolbar", "Set"
        });
    }

    public void setCheckBoxSelected(boolean flag)
    {
        isUseToolBarCheckBox.setSelected(flag);
    }

    public void populateBean(ToolBarManager atoolbarmanager[])
    {
        if(ArrayUtils.isEmpty(atoolbarmanager))
        {
            defaultToolBar.setToolBarLocation(Location.createTopEmbedLocation());
            atoolbarmanager = (new ToolBarManager[] {
                defaultToolBar
            });
        }
        super.populateBean(atoolbarmanager);
    }

    public ToolBarManager[] updateBean()
    {
        if(!isUseToolbar())
            return new ToolBarManager[0];
        else
            return super.updateBean();
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((ToolBarManager[])obj);
    }



}
