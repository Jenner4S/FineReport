// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data;

import com.fr.base.*;
import com.fr.chart.chartdata.JSONTableData;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.AbstractChartDataPane4Chart;
import com.fr.general.FRLogger;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data:
//            ChartDesignDataLoadPane

public class JSONDataPane extends ChartDesignDataLoadPane
{

    private UITextField url;
    private UIButton reviewButton;
    private JSONTableData tableData;

    public JSONDataPane(AbstractChartDataPane4Chart abstractchartdatapane4chart)
    {
        super(abstractchartdatapane4chart);
        url = new UITextField();
        initReviewButton();
        url.addKeyListener(new KeyAdapter() {

            final JSONDataPane this$0;

            public void keyTyped(KeyEvent keyevent)
            {
                if(keyevent.getKeyChar() == '\n')
                {
                    tableData.setFilePath(url.getText());
                    fireChange();
                }
            }

            
            {
                this$0 = JSONDataPane.this;
                super();
            }
        }
);
        setLayout(new BorderLayout(0, 0));
        JPanel jpanel = new JPanel(new BorderLayout(4, 0));
        jpanel.add(url, "Center");
        jpanel.add(reviewButton, "East");
        add(jpanel, "Center");
        tableData = new JSONTableData(url.getText());
    }

    protected String title4PopupWindow()
    {
        return null;
    }

    private void initReviewButton()
    {
        reviewButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/data/search.png"));
        reviewButton.setBorder(new LineBorder(UIConstants.LINE_COLOR));
        reviewButton.addMouseListener(new MouseAdapter() {

            final JSONDataPane this$0;

            public void mouseReleased(MouseEvent mouseevent)
            {
                tableData.setFilePath(url.getText());
                fireChange();
                TableDataWrapper tabledatawrapper = getTableDataWrapper();
                if(tabledatawrapper != null)
                    try
                    {
                        tabledatawrapper.previewData();
                    }
                    catch(Exception exception)
                    {
                        FRContext.getLogger().error(exception.getMessage(), exception);
                    }
            }

            
            {
                this$0 = JSONDataPane.this;
                super();
            }
        }
);
    }

    public TableData getTableData()
    {
        return tableData;
    }

    protected String getNamePrefix()
    {
        return null;
    }

    public void populateChartTableData(TableData tabledata)
    {
        if(tabledata instanceof JSONTableData)
        {
            url.setText(((JSONTableData)tabledata).getFilePath());
            tableData = (JSONTableData)tabledata;
        }
    }


}
