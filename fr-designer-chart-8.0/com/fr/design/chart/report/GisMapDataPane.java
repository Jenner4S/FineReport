// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.GisMapReportDefinition;
import com.fr.chart.chartdata.GisMapTableDefinition;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.data.DataContentsPane;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.chart.report:
//            GisMapTableDataContentPane, GisMapReportDataContentPane

public class GisMapDataPane extends DataContentsPane
{

    private static final long serialVersionUID = 0xfd5af2356db635daL;
    private UIComboBoxPane dataFromPane;
    private GisMapReportDataContentPane reportPane;
    private GisMapTableDataContentPane tablePane;
    private AttributeChangeListener listener;

    public GisMapDataPane(AttributeChangeListener attributechangelistener)
    {
        listener = attributechangelistener;
    }

    public boolean accept(Object obj)
    {
        return obj instanceof TopDefinition;
    }

    public void reset()
    {
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("ChartF-Gis");
    }

    public void setSupportCellData(boolean flag)
    {
        dataFromPane.justSupportOneSelect(flag);
    }

    public void populate(ChartCollection chartcollection)
    {
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        if(topdefinitionprovider instanceof GisMapTableDefinition)
        {
            dataFromPane.setSelectedIndex(0);
            tablePane.populateBean((GisMapTableDefinition)topdefinitionprovider);
        } else
        if(topdefinitionprovider instanceof GisMapReportDefinition)
        {
            dataFromPane.setSelectedIndex(1);
            reportPane.populateBean((GisMapReportDefinition)topdefinitionprovider);
        }
        initAllListeners();
        addAttributeChangeListener(listener);
    }

    public void update(ChartCollection chartcollection)
    {
        if(dataFromPane.getSelectedIndex() == 0)
            chartcollection.getSelectedChart().setFilterDefinition(tablePane.updateBean());
        else
        if(dataFromPane.getSelectedIndex() == 1)
            chartcollection.getSelectedChart().setFilterDefinition(reportPane.updateBean());
    }

    protected JPanel createContentPane()
    {
        return new BasicScrollPane() {

            final GisMapDataPane this$0;

            protected JPanel createContentPane()
            {
                JPanel jpanel = new JPanel();
                jpanel.setLayout(new BorderLayout());
                jpanel.add(dataFromPane = new UIComboBoxPane() {

                    final _cls1 this$1;

                    protected void initLayout()
                    {
                        setLayout(new BorderLayout(4, 6));
                        double d = -2D;
                        double d1 = -1D;
                        double ad[] = {
                            d, d1
                        };
                        double ad1[] = {
                            d
                        };
                        Component acomponent[][] = {
                            {
                                new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("ChartF-Data-Resource")).append(":").toString()), jcb
                            }
                        };
                        JPanel jpanel1 = TableLayoutHelper.createGapTableLayoutPane(acomponent, ad1, ad, 0.0D, 0.0D);
                        add(jpanel1, "North");
                        add(cardPane, "Center");
                    }

                    protected java.util.List initPaneList()
                    {
                        ArrayList arraylist = new ArrayList();
                        arraylist.add(tablePane = new GisMapTableDataContentPane());
                        arraylist.add(reportPane = new GisMapReportDataContentPane());
                        return arraylist;
                    }

                    protected String title4PopupWindow()
                    {
                        return Inter.getLocText("Data_Setting");
                    }

                    
                    {
                        this$1 = _cls1.this;
                        super();
                    }
                }
, "Center");
                dataFromPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
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
                this$0 = GisMapDataPane.this;
                super();
            }
        }
;
    }




}
