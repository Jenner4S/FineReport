// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.TableData;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.JSONTableData;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.data.impl.ExcelTableData;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDesignDataLoadPane;
import com.fr.design.mainframe.chart.gui.data.DataContentsPane;
import com.fr.design.mainframe.chart.gui.data.EmbbeddDataPane;
import com.fr.design.mainframe.chart.gui.data.ExcelDataPane;
import com.fr.design.mainframe.chart.gui.data.JSONDataPane;
import com.fr.general.Inter;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;

public class AbstractChartDataPane4Chart extends DataContentsPane
    implements UIObserver
{

    private static final int DATA_SOURCE_GAP = 18;
    private static final int WIDTH = 262;
    protected ChartDataPane parentPane;
    protected ChartDesignDataLoadPane choosePane;
    protected JComponent choose;
    protected UIObserverListener observerListener;
    protected UIComboBox dataSource;
    protected ItemListener dsListener;
    protected AttributeChangeListener attributeChangeListener;

    public AbstractChartDataPane4Chart(AttributeChangeListener attributechangelistener, ChartDataPane chartdatapane)
    {
        choose = new UILabel();
        dataSource = new UIComboBox(new String[] {
            (new StringBuilder()).append("JSON").append(Inter.getLocText("Chart-DS_TableData")).toString(), (new StringBuilder()).append(Inter.getLocText("Chart-Use_Local")).append("EXCEL").toString(), Inter.getLocText("Chart-DS_Embedded_TableData")
        });
        dsListener = new ItemListener() {

            final AbstractChartDataPane4Chart this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                int i = dataSource.getSelectedIndex();
                if(i == 0)
                    initJSON();
                else
                if(i == 1)
                    initExcel();
                else
                    initEmbbed();
                remove(AbstractChartDataPane4Chart.this.Object);
                clearTableDataSetting();
                initContentPane();
                validate();
            }

            
            {
                this$0 = AbstractChartDataPane4Chart.this;
                super();
            }
        }
;
        parentPane = chartdatapane;
        attributeChangeListener = attributechangelistener;
        initJSON();
        initAll();
    }

    protected void populateChoosePane(TableData tabledata)
    {
        dataSource.removeItemListener(dsListener);
        if(tabledata instanceof JSONTableData)
        {
            initJSON();
            dataSource.setSelectedIndex(0);
        } else
        if(tabledata instanceof ExcelTableData)
        {
            initExcel();
            dataSource.setSelectedIndex(1);
        } else
        if(tabledata instanceof EmbeddedTableData)
        {
            initEmbbed();
            dataSource.setSelectedIndex(2);
        }
        choosePane.populateChartTableData(tabledata);
        dataSource.addItemListener(dsListener);
    }

    protected void initJSON()
    {
        choosePane = new JSONDataPane(this);
        UILabel uilabel = new UILabel("URL:");
        uilabel.setHorizontalAlignment(4);
        choose = uilabel;
    }

    protected void initExcel()
    {
        choose = new UIButton(Inter.getLocText("Chart-Select_Path"));
        choosePane = new ExcelDataPane(this, choose);
    }

    protected void initEmbbed()
    {
        choosePane = new EmbbeddDataPane(this);
        choose = null;
    }

    public void setSupportCellData(boolean flag)
    {
    }

    protected JPanel createContentPane()
    {
        double d = -2D;
        double ad[] = {
            262D
        };
        double ad1[] = {
            d, d
        };
        Component acomponent[][] = {
            {
                new UILabel(Inter.getLocText("Chart-Data_Import"))
            }, {
                createDataImportPane()
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    private JPanel createDataImportPane()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d, d
        };
        Component acomponent[][] = {
            {
                createDataSourcePane()
            }, {
                new JSeparator()
            }, {
                createDataSetPane()
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    protected JPanel createDataSetPane()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d
        };
        Component acomponent[][] = {
            {
                new UILabel(Inter.getLocText("Chart-Data_Configuration"))
            }, {
                getDataContentPane()
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    protected JPanel getDataContentPane()
    {
        return new JPanel();
    }

    private JPanel createDataSourcePane()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            18D, d1
        };
        double ad1[] = {
            d
        };
        Component acomponent[][] = {
            {
                null, createChooseBoxPane()
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    private JPanel createChooseBoxPane()
    {
        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText("Chart-Data_Resource")).append(":").toString());
        uilabel.setHorizontalAlignment(4);
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d
        };
        Component acomponent[][] = {
            {
                uilabel, dataSource
            }, {
                choose, choosePane
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    public void registerChangeListener(UIObserverListener uiobserverlistener)
    {
        observerListener = uiobserverlistener;
    }

    public boolean shouldResponseChangeListener()
    {
        return true;
    }

    protected void initSelfListener(Container container)
    {
        for(int i = 0; i < container.getComponentCount(); i++)
        {
            Component component = container.getComponent(i);
            if(component instanceof Container)
                initListener((Container)component);
            if(component instanceof UIObserver)
                ((UIObserver)component).registerChangeListener(observerListener);
        }

    }

    public void populate(ChartCollection chartcollection)
    {
    }

    public void update(ChartCollection chartcollection)
    {
    }

    public void clearTableDataSetting()
    {
    }

    public void fireTableDataChange()
    {
    }


}
