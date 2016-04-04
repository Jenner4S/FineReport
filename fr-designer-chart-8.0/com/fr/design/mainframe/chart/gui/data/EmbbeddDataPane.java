// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data;

import com.fr.base.*;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.tabledata.tabledatapane.EmbeddedTableDataPane;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.dialog.*;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.AbstractChartDataPane4Chart;
import com.fr.general.FRLogger;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data:
//            ChartDesignDataLoadPane

public class EmbbeddDataPane extends ChartDesignDataLoadPane
{

    private UIButton edit;
    private UIButton reviewButton;
    private EmbeddedTableData tableData;

    public EmbbeddDataPane(AbstractChartDataPane4Chart abstractchartdatapane4chart)
    {
        super(abstractchartdatapane4chart);
        tableData = new EmbeddedTableData();
        initEditButton();
        initReviewButton();
        setLayout(new FlowLayout(0, 0, 3));
        add(edit);
        add(reviewButton);
    }

    private void initEditButton()
    {
        edit = new UIButton(BaseUtils.readIcon("com/fr/design/images/control/edit.png"));
        edit.setBorder(new LineBorder(UIConstants.LINE_COLOR));
        edit.addMouseListener(new MouseAdapter() {

            final EmbbeddDataPane this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                EmbeddedTableDataPane embeddedtabledatapane = new EmbeddedTableDataPane();
                embeddedtabledatapane.populateBean(tableData);
                dgEdit(embeddedtabledatapane, getNamePrefix());
            }

            
            {
                this$0 = EmbbeddDataPane.this;
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

            final EmbbeddDataPane this$0;

            public void mouseReleased(MouseEvent mouseevent)
            {
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
                this$0 = EmbbeddDataPane.this;
                super();
            }
        }
);
    }

    protected String title4PopupWindow()
    {
        return null;
    }

    public TableData getTableData()
    {
        return tableData;
    }

    public void populateChartTableData(TableData tabledata)
    {
        if(tabledata instanceof EmbeddedTableData)
            tableData = (EmbeddedTableData)tabledata;
    }

    protected String getNamePrefix()
    {
        return "Embedded";
    }

    private void dgEdit(final EmbeddedTableDataPane uPanel, String s)
    {
        com.fr.design.dialog.BasicPane.NamePane namepane = uPanel.asNamePane();
        namepane.setObjectName(s);
        BasicDialog basicdialog = namepane.showLargeWindow(SwingUtilities.getWindowAncestor(this), new DialogActionAdapter() {

            final EmbeddedTableDataPane val$uPanel;
            final EmbbeddDataPane this$0;

            public void doOk()
            {
                tableData = uPanel.updateBean();
                fireChange();
            }

            
            {
                this$0 = EmbbeddDataPane.this;
                uPanel = embeddedtabledatapane;
                super();
            }
        }
);
        basicdialog.setVisible(true);
    }



}
