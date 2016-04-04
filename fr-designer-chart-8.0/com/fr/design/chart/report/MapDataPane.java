// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.MapMoreLayerReportDefinition;
import com.fr.chart.chartdata.MapMoreLayerTableDefinition;
import com.fr.chart.chartdata.MapSingleLayerReportDefinition;
import com.fr.chart.chartdata.MapSingleLayerTableDefinition;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.mainframe.chart.gui.data.DataContentsPane;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.chart.report:
//            MapMoreCubeLayerPane, MapSinglePane

public class MapDataPane extends DataContentsPane
{

    private UIComboBoxPane mainPane;
    private MapMoreCubeLayerPane morePane;
    private MapSinglePane singlePane;
    private AttributeChangeListener listener;

    public MapDataPane(AttributeChangeListener attributechangelistener)
    {
        listener = attributechangelistener;
    }

    public void populate(ChartCollection chartcollection)
    {
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        morePane.init4PopuMapTree(chartcollection);
        if(topdefinitionprovider instanceof MapSingleLayerTableDefinition)
        {
            mainPane.setSelectedIndex(0);
            singlePane.populateBean(topdefinitionprovider);
        } else
        if(topdefinitionprovider instanceof MapMoreLayerTableDefinition)
        {
            mainPane.setSelectedIndex(1);
            morePane.populateBean(chartcollection);
        } else
        if(topdefinitionprovider instanceof MapMoreLayerReportDefinition)
        {
            mainPane.setSelectedIndex(1);
            morePane.populateBean(chartcollection);
        } else
        if(topdefinitionprovider instanceof MapSingleLayerReportDefinition)
        {
            mainPane.setSelectedIndex(0);
            singlePane.populateBean(topdefinitionprovider);
        }
        initAllListeners();
        addAttributeChangeListener(listener);
    }

    public void update(ChartCollection chartcollection)
    {
        if(mainPane.getSelectedIndex() == 0)
            chartcollection.getSelectedChart().setFilterDefinition(singlePane.updateBean());
        else
            morePane.updateBean(chartcollection);
    }

    protected JPanel createContentPane()
    {
        BasicScrollPane basicscrollpane = new BasicScrollPane() {

            final MapDataPane this$0;

            protected JPanel createContentPane()
            {
                JPanel jpanel = new JPanel();
                jpanel.setLayout(new BorderLayout());
                mainPane = new UIComboBoxPane() {

                    final _cls1 this$1;

                    protected void initLayout()
                    {
                        setLayout(new BorderLayout(0, 6));
                        JPanel jpanel1 = new JPanel(new FlowLayout(0));
                        jpanel1.add(new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("Map Show Type")).append(":").toString()));
                        jpanel1.add(jcb);
                        add(jpanel1, "North");
                        add(cardPane, "Center");
                    }

                    protected java.util.List initPaneList()
                    {
                        ArrayList arraylist = new ArrayList();
                        arraylist.add(singlePane = new MapSinglePane());
                        arraylist.add(morePane = new MapMoreCubeLayerPane());
                        return arraylist;
                    }

                    protected String title4PopupWindow()
                    {
                        return Inter.getLocText(new String[] {
                            "Chart-Map", "Data"
                        });
                    }

                    
                    {
                        this$1 = _cls1.this;
                        super();
                    }
                }
;
                jpanel.add(mainPane, "Center");
                return jpanel;
            }

            public void populateBean(Chart chart)
            {
            }

            protected String title4PopupWindow()
            {
                return null;
            }

            public volatile void populateBean(Object obj)
            {
                populateBean((Chart)obj);
            }

            
            {
                this$0 = MapDataPane.this;
                super();
            }
        }
;
        return basicscrollpane;
    }

    public String getIconPath()
    {
        return "com/fr/design/images/chart/ChartData.png";
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "Chart-Map", "Data"
        });
    }

    public void setSupportCellData(boolean flag)
    {
        morePane.setSurpportCellData(flag);
        singlePane.setSurpportCellData(flag);
    }




}
