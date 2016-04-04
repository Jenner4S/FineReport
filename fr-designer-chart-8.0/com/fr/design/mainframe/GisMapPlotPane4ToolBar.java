// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.GisMapIndependentChart;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.ChartBook;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

// Referenced classes of package com.fr.design.mainframe:
//            AbstractMapPlotPane4ToolBar, ChartDesigner

public class GisMapPlotPane4ToolBar extends AbstractMapPlotPane4ToolBar
{

    private static final int BAIDU = 0;
    private static final int GOOGLE = 1;
    private static final String TYPE_NAMES[] = {
        Inter.getLocText("FR-Chart-Map_Baidu"), Inter.getLocText("FR-Chart-Map_Google")
    };
    private UITextField keyField;
    private DocumentListener keyListener;

    private void fireKeyChange()
    {
        ChartCollection chartcollection = (ChartCollection)((ChartBook)chartDesigner.getTarget()).getChartCollection();
        Chart chart = chartcollection.getSelectedChart();
        GisMapPlot gismapplot = (GisMapPlot)chart.getPlot();
        String s = keyField.getText().trim();
        if(gismapplot.isGisType() && s != gismapplot.getBaiduKey())
            gismapplot.setBaiduKey(s);
        else
        if(!gismapplot.isGisType() && s != gismapplot.getGoogleKey())
            gismapplot.setGoogleKey(s);
        chartDesigner.fireTargetModified();
    }

    public GisMapPlotPane4ToolBar(ChartDesigner chartdesigner)
    {
        super(chartdesigner);
        keyField = new UITextField() {

            final GisMapPlotPane4ToolBar this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(230, 22);
            }

            
            {
                this$0 = GisMapPlotPane4ToolBar.this;
                super();
            }
        }
;
        keyListener = new DocumentListener() {

            final GisMapPlotPane4ToolBar this$0;

            public void insertUpdate(DocumentEvent documentevent)
            {
                fireKeyChange();
            }

            public void removeUpdate(DocumentEvent documentevent)
            {
                fireKeyChange();
            }

            public void changedUpdate(DocumentEvent documentevent)
            {
                fireKeyChange();
            }

            
            {
                this$0 = GisMapPlotPane4ToolBar.this;
                super();
            }
        }
;
        add(getKeyPane());
        keyField.getDocument().addDocumentListener(keyListener);
    }

    private JPanel getKeyPane()
    {
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
                new UILabel("key"), keyField
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    protected void calculateDetailMaps(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            populateDetilMaps(Inter.getLocText("FR-Chart-Map_Baidu"));
            break;

        case 1: // '\001'
            populateDetilMaps(Inter.getLocText("FR-Chart-Map_Google"));
            break;

        default:
            populateDetilMaps(Inter.getLocText("FR-Chart-Map_Baidu"));
            break;
        }
        fireMapChange();
    }

    public void populateMapPane(String s)
    {
        super.populateMapPane(s);
        populateDetilMaps(mapTypeComboBox.getSelectedItem().toString());
    }

    protected void populateDetilMaps(String s)
    {
        mapTypeComboBox.removeItemListener(mapTypeListener);
        ChartCollection chartcollection = (ChartCollection)((ChartBook)chartDesigner.getTarget()).getChartCollection();
        Chart chart = chartcollection.getSelectedChart();
        GisMapPlot gismapplot = (GisMapPlot)chart.getPlot();
        keyField.getDocument().removeDocumentListener(keyListener);
        if(gismapplot.isGisType())
        {
            keyField.setText(gismapplot.getBaiduKey());
            mapTypeComboBox.setSelectedIndex(0);
        } else
        {
            keyField.setText(gismapplot.getGoogleKey());
            mapTypeComboBox.setSelectedIndex(1);
        }
        keyField.getDocument().addDocumentListener(keyListener);
        mapTypeComboBox.addItemListener(mapTypeListener);
    }

    private void fireMapChange()
    {
        ChartCollection chartcollection = (ChartCollection)((ChartBook)chartDesigner.getTarget()).getChartCollection();
        Chart chart = chartcollection.getSelectedChart();
        if(chart.getPlot().getPlotStyle() != 0)
            resetChart(chart);
        Chart achart[] = GisMapIndependentChart.gisChartTypes;
        GisMapPlot gismapplot;
        if(achart.length > 0)
            try
            {
                gismapplot = (GisMapPlot)achart[0].getPlot().clone();
            }
            catch(Exception exception)
            {
                gismapplot = new GisMapPlot();
            }
        else
            gismapplot = new GisMapPlot();
        try
        {
            chart.switchPlot((Plot)gismapplot.clone());
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            FRLogger.getLogger().error("Error In GisChart");
            chart.switchPlot(new GisMapPlot());
        }
        gismapplot = (GisMapPlot)chart.getPlot();
        boolean flag = gismapplot.isGisType();
        gismapplot.setGisType(mapTypeComboBox.getSelectedIndex() == 1);
        if(flag != gismapplot.isGisType())
        {
            if(gismapplot.isGisType())
                keyField.setText(gismapplot.getBaiduKey());
            else
                keyField.setText(gismapplot.getGoogleKey());
        } else
        {
            String s = keyField.getText().trim();
            if(gismapplot.isGisType() && s != gismapplot.getBaiduKey())
                gismapplot.setBaiduKey(s);
            else
            if(!gismapplot.isGisType() && s != gismapplot.getGoogleKey())
                gismapplot.setGoogleKey(s);
        }
        chartDesigner.fireTargetModified();
    }

    protected Plot getSelectedClonedPlot()
    {
        Chart achart[] = GisMapIndependentChart.gisChartTypes;
        GisMapPlot gismapplot;
        if(achart.length > 0)
            try
            {
                gismapplot = (GisMapPlot)achart[0].getPlot().clone();
            }
            catch(Exception exception)
            {
                gismapplot = new GisMapPlot();
            }
        else
            gismapplot = new GisMapPlot();
        Plot plot = null;
        try
        {
            plot = (Plot)gismapplot.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            FRLogger.getLogger().error("Error In GisMapChart");
        }
        return plot;
    }

    public String[] getMapTypes()
    {
        return TYPE_NAMES;
    }


}
