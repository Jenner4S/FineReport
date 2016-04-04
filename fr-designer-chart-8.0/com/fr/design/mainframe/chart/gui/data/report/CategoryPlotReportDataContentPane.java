// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.NormalReportDataDefinition;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data.report:
//            AbstractReportDataContentPane

public class CategoryPlotReportDataContentPane extends AbstractReportDataContentPane
{

    protected static final int PRE_WIDTH = 210;
    protected TinyFormulaPane categoryName;
    protected ChartDataFilterPane filterPane;

    public CategoryPlotReportDataContentPane()
    {
    }

    public CategoryPlotReportDataContentPane(ChartDataPane chartdatapane)
    {
        initEveryPane();
        categoryName = initCategoryBox((new StringBuilder()).append(Inter.getLocText("FR-Chart-Category_Name")).append(":").toString());
        add(categoryName, "0,0,2,0");
        add(new BoldFontTextLabel(Inter.getLocText("FR-Chart-Data_Filter")), "0,4,2,4");
        add(filterPane = new ChartDataFilterPane(new Bar2DPlot(), chartdatapane), "0,6,2,4");
    }

    protected TinyFormulaPane initCategoryBox(final String leftLabel)
    {
        TinyFormulaPane tinyformulapane = new TinyFormulaPane() {

            final String val$leftLabel;
            final CategoryPlotReportDataContentPane this$0;

            protected void initLayout()
            {
                setLayout(new java.awt.BorderLayout(4, 0));
                if(StringUtils.isNotEmpty(leftLabel))
                {
                    UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Chart-Category_Name")).append(":").toString(), 4);
                    uilabel.setPreferredSize(new java.awt.Dimension(75, 20));
                    add(uilabel, "West");
                }
                formulaTextField.setPreferredSize(new java.awt.Dimension(100, 20));
                add(formulaTextField, "Center");
                add(formulaTextFieldButton, "East");
            }

            public void okEvent()
            {
                checkBoxUse();
            }

            
            {
                this$0 = CategoryPlotReportDataContentPane.this;
                leftLabel = s;
                super();
            }
        }
;
        tinyformulapane.getUITextField().getDocument().addDocumentListener(new DocumentListener() {

            final CategoryPlotReportDataContentPane this$0;

            public void removeUpdate(DocumentEvent documentevent)
            {
                checkBoxUse();
            }

            public void insertUpdate(DocumentEvent documentevent)
            {
                checkBoxUse();
            }

            public void changedUpdate(DocumentEvent documentevent)
            {
                checkBoxUse();
            }

            
            {
                this$0 = CategoryPlotReportDataContentPane.this;
                super();
            }
        }
);
        return tinyformulapane;
    }

    protected String[] columnNames()
    {
        return (new String[] {
            Inter.getLocText("FR-Chart-Series_Name"), Inter.getLocText("Chart-Series_Value")
        });
    }

    public void populateBean(ChartCollection chartcollection)
    {
        checkBoxUse();
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        if(topdefinitionprovider instanceof NormalReportDataDefinition)
        {
            NormalReportDataDefinition normalreportdatadefinition = (NormalReportDataDefinition)topdefinitionprovider;
            if(normalreportdatadefinition.getCategoryName() != null)
            {
                categoryName.getUITextField().setText(normalreportdatadefinition.getCategoryName().toString());
                List list = getEntryList(normalreportdatadefinition);
                if(!list.isEmpty())
                    populateList(list);
            }
            seriesPane.doLayout();
        }
        filterPane.populateBean(chartcollection);
    }

    private List getEntryList(NormalReportDataDefinition normalreportdatadefinition)
    {
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < normalreportdatadefinition.size(); i++)
        {
            SeriesDefinition seriesdefinition = (SeriesDefinition)normalreportdatadefinition.get(i);
            Object aobj[] = new Object[2];
            aobj[0] = seriesdefinition.getSeriesName();
            aobj[1] = seriesdefinition.getValue();
            if(aobj[0] != null && aobj[1] != null)
                arraylist.add(((Object) (aobj)));
        }

        return arraylist;
    }

    public void updateBean(ChartCollection chartcollection)
    {
        chartcollection.getSelectedChart().setFilterDefinition(new NormalReportDataDefinition());
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        if(topdefinitionprovider instanceof NormalReportDataDefinition)
        {
            NormalReportDataDefinition normalreportdatadefinition = (NormalReportDataDefinition)topdefinitionprovider;
            normalreportdatadefinition.setCategoryName(canBeFormula(categoryName.getUITextField().getText()));
            List list = updateList();
            for(int i = 0; i < list.size(); i++)
            {
                Object aobj[] = (Object[])(Object[])list.get(i);
                SeriesDefinition seriesdefinition = new SeriesDefinition();
                seriesdefinition.setSeriesName(canBeFormula(aobj[0]));
                seriesdefinition.setValue(canBeFormula(aobj[1]));
                normalreportdatadefinition.add(seriesdefinition);
            }

        }
        filterPane.updateBean(chartcollection);
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
