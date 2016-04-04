// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data;

import com.fr.base.*;
import com.fr.data.impl.ExcelTableData;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.datapane.preview.PreviewTablePane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.AbstractChartDataPane4Chart;
import com.fr.design.mainframe.DesignerContext;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data:
//            ChartDesignDataLoadPane

public class ExcelDataPane extends ChartDesignDataLoadPane
{

    private UITextField path;
    private UIButton reviewButton;
    private ExcelTableData tableData;
    private MouseListener listener;

    public ExcelDataPane(AbstractChartDataPane4Chart abstractchartdatapane4chart, JComponent jcomponent)
    {
        super(abstractchartdatapane4chart);
        path = new UITextField();
        listener = new MouseAdapter() {

            final ExcelDataPane this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                FILEChooserPane filechooserpane = new FILEChooserPane(true, true);
                if(filechooserpane.showOpenDialog(DesignerContext.getDesignerFrame(), ".xlsx") == 0)
                {
                    FILE file = filechooserpane.getSelectedFILE();
                    if(file != null && file.exists())
                    {
                        path.setText(file.getPath());
                    } else
                    {
                        JOptionPane.showConfirmDialog(ExcelDataPane.this, Inter.getLocText("FR-Template-Path_chooseRightPath"), Inter.getLocText("FR-App-All_Warning"), 2, 2);
                        path.setText("");
                    }
                    tableData.setFilePath(path.getText().toString());
                    tableData.setFromEnv(file.isEnvFile());
                    tableData.setNeedColumnName(true);
                    fireChange();
                }
            }

            
            {
                this$0 = ExcelDataPane.this;
                super();
            }
        }
;
        initReviewButton();
        tableData = new ExcelTableData();
        tableData.setFilePath(path.getText().toString());
        tableData.setNeedColumnName(true);
        path.setEditable(false);
        jcomponent.addMouseListener(listener);
        setLayout(new BorderLayout(0, 0));
        JPanel jpanel = new JPanel(new BorderLayout(4, 0));
        jpanel.add(path, "Center");
        jpanel.add(reviewButton, "East");
        add(jpanel, "Center");
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

            final ExcelDataPane this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                try
                {
                    PreviewTablePane.previewTableData(getTableData());
                }
                catch(Exception exception)
                {
                    FRContext.getLogger().error(exception.getMessage(), exception);
                }
            }

            
            {
                this$0 = ExcelDataPane.this;
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
        if(tabledata instanceof ExcelTableData)
        {
            path.setText(((ExcelTableData)tabledata).getFilePath());
            tableData = (ExcelTableData)tabledata;
            tableData.setNeedColumnName(true);
        }
        fireChange();
    }


}
