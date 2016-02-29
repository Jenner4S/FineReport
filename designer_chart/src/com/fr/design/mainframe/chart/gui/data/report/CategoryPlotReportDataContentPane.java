package com.fr.design.mainframe.chart.gui.data.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.NormalReportDataDefinition;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryPlotReportDataContentPane extends AbstractReportDataContentPane {
	protected static final int PRE_WIDTH = 210;

    protected TinyFormulaPane categoryName;
    protected ChartDataFilterPane filterPane;
    
    public CategoryPlotReportDataContentPane(){
    	
    }

    public CategoryPlotReportDataContentPane(ChartDataPane parent) {
        initEveryPane();
        categoryName = initCategoryBox(Inter.getLocText("FR-Chart-Category_Name") + ":");

        this.add(categoryName, "0,0,2,0");
        this.add(new BoldFontTextLabel(Inter.getLocText("FR-Chart-Data_Filter")), "0,4,2,4");
        this.add(filterPane = new ChartDataFilterPane(new Bar2DPlot(), parent), "0,6,2,4");
    }
    
	protected TinyFormulaPane initCategoryBox(final String leftLabel) {
		TinyFormulaPane categoryName = new TinyFormulaPane() {
            @Override
            protected void initLayout() {
                this.setLayout(new BorderLayout(4, 0));
                
                if(StringUtils.isNotEmpty(leftLabel)) {
                	UILabel label1 = new UILabel(Inter.getLocText("FR-Chart-Category_Name") + ":", SwingConstants.RIGHT);
                	label1.setPreferredSize(new Dimension(75, 20));
                	this.add(label1, BorderLayout.WEST);
                }
                
                formulaTextField.setPreferredSize(new Dimension(100, 20));
                this.add(formulaTextField, BorderLayout.CENTER);
                this.add(formulaTextFieldButton, BorderLayout.EAST);
            }

            public void okEvent() {
                checkBoxUse();
            }
        };

        categoryName.getUITextField().getDocument().addDocumentListener(new DocumentListener() {
            public void removeUpdate(DocumentEvent e) {
                checkBoxUse();
            }

            public void insertUpdate(DocumentEvent e) {
                checkBoxUse();
            }

            public void changedUpdate(DocumentEvent e) {
                checkBoxUse();
            }
        });
        
        return categoryName;
	}

    @Override
    protected String[] columnNames() {
        return new String[]{
                Inter.getLocText("FR-Chart-Series_Name"),
                Inter.getLocText("Chart-Series_Value")
        };
    }

    public void populateBean(ChartCollection collection) {
        checkBoxUse();

        TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
        if (definition instanceof NormalReportDataDefinition) {
            NormalReportDataDefinition reportDefinition = (NormalReportDataDefinition) definition;
            if (reportDefinition.getCategoryName() != null) {
                categoryName.getUITextField().setText(reportDefinition.getCategoryName().toString());

                List list = getEntryList(reportDefinition);
                if (!list.isEmpty()) {
                    populateList(list);
                }
            }
            
            seriesPane.doLayout();
        }

        filterPane.populateBean(collection);
    }

    private List getEntryList(NormalReportDataDefinition seriesList) {
        List list = new ArrayList();
        for (int i = 0; i < seriesList.size(); i++) {
            SeriesDefinition seriesEntry = (SeriesDefinition) seriesList.get(i);
            Object[] nameAndValue = new Object[2];
            nameAndValue[0] = seriesEntry.getSeriesName();
            nameAndValue[1] = seriesEntry.getValue();
            if (nameAndValue[0] != null && nameAndValue[1] != null) {
                list.add(nameAndValue);
            }
        }
        return list;
    }

    public void updateBean(ChartCollection collection) {
        collection.getSelectedChart().setFilterDefinition(new NormalReportDataDefinition());

        TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
        if (definition instanceof NormalReportDataDefinition) {
            NormalReportDataDefinition reportDefinition = (NormalReportDataDefinition) definition;
            
            reportDefinition.setCategoryName(canBeFormula(categoryName.getUITextField().getText()));

            List list = updateList();
            for (int i = 0; i < list.size(); i++) {
                Object[] value = (Object[]) list.get(i);
                SeriesDefinition sd = new SeriesDefinition();

                sd.setSeriesName(canBeFormula(value[0]));
                sd.setValue(canBeFormula(value[1]));
                reportDefinition.add(sd);
            }
        }
        filterPane.updateBean(collection);
    }
}