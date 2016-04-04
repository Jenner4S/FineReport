// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.*;
import com.fr.chart.chartdata.MeterReportDefinition;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JSeparator;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data.report:
//            AbstractReportDataContentPane

public class MeterPlotReportDataContentPane extends AbstractReportDataContentPane
{

    private static final String CATENAME = Inter.getLocText(new String[] {
        "ChartF-Meter", "StyleFormat-Category", "WF-Name"
    });
    private static final String NVALUE = Inter.getLocText("Needle Value");
    private TinyFormulaPane singCatePane;
    private TinyFormulaPane singValuePane;
    private ChartDataFilterPane filterPane;

    public MeterPlotReportDataContentPane(ChartDataPane chartdatapane)
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d
        };
        Component acomponent[][] = {
            {
                new UILabel(CATENAME), singCatePane = new TinyFormulaPane()
            }, {
                new UILabel(NVALUE), singValuePane = new TinyFormulaPane()
            }, {
                null, null
            }
        };
        javax.swing.JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        setLayout(new BorderLayout());
        add(jpanel, "North");
        double ad2[] = {
            d
        };
        acomponent = (new Component[][] {
            new Component[] {
                new JSeparator()
            }, new Component[] {
                new BoldFontTextLabel(Inter.getLocText("Data_Filter"))
            }, new Component[] {
                filterPane = new ChartDataFilterPane(new MeterPlot(), chartdatapane)
            }
        });
        javax.swing.JPanel jpanel1 = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad2);
        add(jpanel1, "Center");
    }

    public void populateBean(ChartCollection chartcollection)
    {
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        if(topdefinitionprovider instanceof MeterReportDefinition)
        {
            MeterReportDefinition meterreportdefinition = (MeterReportDefinition)topdefinitionprovider;
            if(meterreportdefinition.getName() != null)
                singCatePane.getUITextField().setText(meterreportdefinition.getName().toString());
            if(meterreportdefinition.getValue() != null)
                singValuePane.getUITextField().setText(meterreportdefinition.getValue().toString());
        }
        filterPane.populateBean(chartcollection);
    }

    public void updateBean(ChartCollection chartcollection)
    {
        if(chartcollection != null)
        {
            MeterReportDefinition meterreportdefinition = new MeterReportDefinition();
            meterreportdefinition.setName(canBeFormula(singCatePane.getUITextField().getText()));
            meterreportdefinition.setValue(canBeFormula(singValuePane.getUITextField().getText()));
            chartcollection.getSelectedChart().setFilterDefinition(meterreportdefinition);
            filterPane.updateBean(chartcollection);
        }
    }

    protected String[] columnNames()
    {
        return (new String[] {
            "", ""
        });
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((ChartCollection)obj);
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((ChartCollection)obj);
    }

}
