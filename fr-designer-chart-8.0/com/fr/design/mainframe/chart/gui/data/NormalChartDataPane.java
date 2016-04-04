// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data:
//            DataContentsPane, ReportDataPane, TableDataPane

public class NormalChartDataPane extends DataContentsPane
{

    private UIComboBoxPane dataPane;
    private TableDataPane tableDataPane;
    private ReportDataPane reportDataPane;
    private AttributeChangeListener listener;
    private ChartDataPane parent;

    public NormalChartDataPane(AttributeChangeListener attributechangelistener, ChartDataPane chartdatapane)
    {
        listener = attributechangelistener;
        parent = chartdatapane;
        initAll();
    }

    public NormalChartDataPane(AttributeChangeListener attributechangelistener, ChartDataPane chartdatapane, boolean flag)
    {
        listener = attributechangelistener;
        parent = chartdatapane;
        initAll();
        dataPane.justSupportOneSelect(true);
    }

    protected JPanel createContentPane()
    {
        return new BasicScrollPane() {

            final NormalChartDataPane this$0;

            protected JPanel createContentPane()
            {
                JPanel jpanel = new JPanel(new BorderLayout());
                dataPane = new UIComboBoxPane() {

                    final _cls1 this$1;

                    protected void initLayout()
                    {
                        setLayout(new BorderLayout(4, 6));
                        JPanel jpanel1 = new JPanel(new BorderLayout(4, 0));
                        jpanel1.add(jcb, "Center");
                        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText("Chart-Data_Resource")).append(":").toString(), 4);
                        uilabel.setPreferredSize(new Dimension(75, 20));
                        jpanel1.add(GUICoreUtils.createBorderLayoutPane(new Component[] {
                            jcb, null, null, uilabel, null
                        }));
                        add(jpanel1, "North");
                        add(cardPane, "Center");
                    }

                    protected String title4PopupWindow()
                    {
                        return null;
                    }

                    protected java.util.List initPaneList()
                    {
                        tableDataPane = new TableDataPane(parent);
                        reportDataPane = new ReportDataPane(parent);
                        ArrayList arraylist = new ArrayList();
                        arraylist.add(tableDataPane);
                        arraylist.add(reportDataPane);
                        return arraylist;
                    }

                    
                    {
                        this$1 = _cls1.this;
                        super();
                    }
                }
;
                jpanel.add(dataPane, "Center");
                dataPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
                return jpanel;
            }

            protected String title4PopupWindow()
            {
                return "";
            }

            public void populateBean(ChartCollection chartcollection)
            {
            }

            public volatile void populateBean(Object obj)
            {
                populateBean((ChartCollection)obj);
            }

            
            {
                this$0 = NormalChartDataPane.this;
                super();
            }
        }
;
    }

    public void populate(ChartCollection chartcollection)
    {
        reportDataPane.refreshContentPane(chartcollection);
        tableDataPane.refreshContentPane(chartcollection);
        dataPane.populateBean(chartcollection);
        initAllListeners();
        addAttributeChangeListener(listener);
        reportDataPane.checkBoxUse();
        tableDataPane.checkBoxUse();
    }

    public void update(ChartCollection chartcollection)
    {
        if(dataPane.getSelectedIndex() == 0)
            tableDataPane.updateBean(chartcollection);
        else
            reportDataPane.updateBean(chartcollection);
    }

    public void setSupportCellData(boolean flag)
    {
        dataPane.justSupportOneSelect(flag);
    }







}
