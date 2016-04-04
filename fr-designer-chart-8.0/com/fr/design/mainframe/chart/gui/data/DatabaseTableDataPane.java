// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data;

import com.fr.base.*;
import com.fr.data.impl.NameTableData;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.TableDataComboBox;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.FRLogger;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class DatabaseTableDataPane extends BasicPane
{

    private static final long serialVersionUID = 0x49c648920886e412L;
    private TableDataComboBox tableNameCombox;
    private UIButton reviewButton;

    public DatabaseTableDataPane(UILabel uilabel)
    {
        initTableCombox();
        initReviewButton();
        setLayout(new BorderLayout(0, 0));
        if(uilabel != null)
            add(uilabel, "West");
        JPanel jpanel = new JPanel(new BorderLayout(4, 0));
        jpanel.add(tableNameCombox, "Center");
        jpanel.add(reviewButton, "East");
        add(jpanel, "Center");
    }

    public TableDataWrapper getTableDataWrapper()
    {
        return tableNameCombox.getSelectedItem();
    }

    public void populateBean(TableData tabledata)
    {
        if(tabledata == null)
            tableNameCombox.setSelectedItem(tabledata);
        else
            tableNameCombox.setSelectedTableDataByName(((NameTableData)tabledata).getName());
    }

    protected String title4PopupWindow()
    {
        return null;
    }

    protected void userEvent()
    {
    }

    private void initTableCombox()
    {
        tableNameCombox = new TableDataComboBox(DesignTableDataManager.getEditingTableDataSource());
        tableNameCombox.addItemListener(new ItemListener() {

            final DatabaseTableDataPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                if(itemevent.getStateChange() == 2)
                    userEvent();
            }

            
            {
                this$0 = DatabaseTableDataPane.this;
                super();
            }
        }
);
    }

    private void initReviewButton()
    {
        reviewButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/data/search.png"));
        reviewButton.setBorder(new LineBorder(UIConstants.LINE_COLOR));
        reviewButton.addMouseListener(new MouseAdapter() {

            final DatabaseTableDataPane this$0;

            public void mouseReleased(MouseEvent mouseevent)
            {
                TableDataWrapper tabledatawrapper = tableNameCombox.getSelectedItem();
                if(tabledatawrapper != null)
                    try
                    {
                        tabledatawrapper.previewData();
                    }
                    catch(Exception exception)
                    {
                        FRContext.getLogger().error(exception.getMessage(), exception);
                    }
                super.mouseReleased(mouseevent);
            }

            
            {
                this$0 = DatabaseTableDataPane.this;
                super();
            }
        }
);
    }

}
