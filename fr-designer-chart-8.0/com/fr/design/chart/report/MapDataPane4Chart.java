// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.MapMoreLayerTableDefinition;
import com.fr.chart.chartdata.MapSingleLayerTableDefinition;
import com.fr.chart.chartdata.TableDataDefinition;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.mainframe.AbstractChartDataPane4Chart;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDesignDataLoadPane;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.chart.report:
//            MapMoreCubeLayerPane4Chart, MapSinglePane4Chart

public class MapDataPane4Chart extends AbstractChartDataPane4Chart
{

    private UIComboBoxPane dataContentPane;
    private MapMoreCubeLayerPane4Chart morePane;
    private MapSinglePane4Chart singlePane;
    private ChartCollection currentCollection;
    private ItemListener itemListener;

    public MapDataPane4Chart(AttributeChangeListener attributechangelistener, ChartDataPane chartdatapane)
    {
        super(attributechangelistener, chartdatapane);
        morePane = new MapMoreCubeLayerPane4Chart();
        singlePane = new MapSinglePane4Chart();
        itemListener = new ItemListener() {

            final MapDataPane4Chart this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                fireTableDataChange();
            }

            
            {
                this$0 = MapDataPane4Chart.this;
                super();
            }
        }
;
        dataContentPane = new UIComboBoxPane() {

            final MapDataPane4Chart this$0;

            protected void initLayout()
            {
                setLayout(new BorderLayout(0, 6));
                JPanel jpanel = new JPanel(new FlowLayout(0));
                jpanel.add(new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("FR-Chart-Map_ShowWay")).append(":").toString()));
                jpanel.add(jcb);
                add(jpanel, "North");
                add(cardPane, "Center");
            }

            protected java.util.List initPaneList()
            {
                ArrayList arraylist = new ArrayList();
                arraylist.add(singlePane);
                arraylist.add(morePane);
                return arraylist;
            }

            protected void comboBoxItemStateChanged()
            {
                if(currentCollection == null)
                {
                    return;
                } else
                {
                    fireTableDataChange();
                    morePane.init4PopuMapTree(currentCollection);
                    return;
                }
            }

            protected String title4PopupWindow()
            {
                return Inter.getLocText(new String[] {
                    "Chart-Map", "Data"
                });
            }

            
            {
                this$0 = MapDataPane4Chart.this;
                super();
            }
        }
;
    }

    protected JPanel getDataContentPane()
    {
        return dataContentPane;
    }

    public void populate(ChartCollection chartcollection)
    {
        currentCollection = chartcollection;
        morePane.init4PopuMapTree(chartcollection);
        if(chartcollection != null && chartcollection.getSelectedChart() != null)
        {
            Chart chart = chartcollection.getSelectedChart();
            TopDefinitionProvider topdefinitionprovider = chart.getFilterDefinition();
            if(topdefinitionprovider instanceof TableDataDefinition)
            {
                com.fr.base.TableData tabledata = ((TableDataDefinition)topdefinitionprovider).getTableData();
                if(tabledata != null)
                {
                    populateChoosePane(tabledata);
                    fireTableDataChange();
                }
            }
            if(topdefinitionprovider instanceof MapSingleLayerTableDefinition)
                singlePane.populateBean(topdefinitionprovider);
            else
            if(topdefinitionprovider instanceof MapMoreLayerTableDefinition)
                morePane.populateBean(chartcollection);
        }
        remove(leftContentPane);
        initContentPane();
        validate();
        dataSource.addItemListener(dsListener);
        initAllListeners();
        initSelfListener(dataContentPane);
        addAttributeChangeListener(attributeChangeListener);
    }

    public void update(ChartCollection chartcollection)
    {
        if(dataContentPane.getSelectedIndex() == 0)
            chartcollection.getSelectedChart().setFilterDefinition(singlePane.updateBean());
        else
            morePane.updateBean(chartcollection);
        currentCollection = chartcollection;
    }

    public void fireTableDataChange()
    {
        if(dataContentPane == null)
            return;
        if(dataContentPane.getSelectedIndex() == 0)
            singlePane.fireTableDataChanged(choosePane.getTableDataWrapper());
        else
            morePane.fireTableDataChanged(choosePane.getTableDataWrapper());
    }



}
