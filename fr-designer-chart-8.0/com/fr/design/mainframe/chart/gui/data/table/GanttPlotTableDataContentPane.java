// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.GanttPlot;
import com.fr.chart.chartdata.GanttTableDefinition;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data.table:
//            AbstractTableDataContentPane

public class GanttPlotTableDataContentPane extends AbstractTableDataContentPane
{

    private UIComboBox step;
    private UIComboBox planStart;
    private UIComboBox planEnd;
    private UIComboBox finalStart;
    private UIComboBox finalEnd;
    private UIComboBox percent;
    private UIComboBox project;
    ItemListener check;

    public GanttPlotTableDataContentPane(ChartDataPane chartdatapane)
    {
        check = new ItemListener() {

            final GanttPlotTableDataContentPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                checkBoxUse();
            }

            
            {
                this$0 = GanttPlotTableDataContentPane.this;
                super();
            }
        }
;
        step = new UIComboBox();
        planStart = new UIComboBox();
        planEnd = new UIComboBox();
        finalStart = new UIComboBox();
        finalEnd = new UIComboBox();
        percent = new UIComboBox();
        project = new UIComboBox();
        step.setPreferredSize(new Dimension(100, 20));
        planStart.setPreferredSize(new Dimension(100, 20));
        planEnd.setPreferredSize(new Dimension(100, 20));
        finalStart.setPreferredSize(new Dimension(100, 20));
        finalEnd.setPreferredSize(new Dimension(100, 20));
        percent.setPreferredSize(new Dimension(100, 20));
        project.setPreferredSize(new Dimension(100, 20));
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d, d, d, d, d, d, d
        };
        Component acomponent[][] = {
            {
                new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("Chart-Step_Name")).append(":").toString(), 4), step
            }, {
                new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("Chart-Plan_Start")).append(":").toString(), 4), planStart
            }, {
                new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("Chart-Plan_End")).append(":").toString(), 4), planEnd
            }, {
                new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("Chart-Actual_Start")).append(":").toString(), 4), finalStart
            }, {
                new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("Chart-Actual_End")).append(":").toString(), 4), finalEnd
            }, {
                new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("Chart-Use_Percent")).append(":").toString(), 4), percent
            }, {
                new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("Chart-Use_Items")).append(":").toString(), 4), project
            }
        };
        javax.swing.JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        setLayout(new BorderLayout());
        add(jpanel, "Center");
        finalStart.addItem(GanttPlot.NONE);
        finalEnd.addItem(GanttPlot.NONE);
        percent.addItem(GanttPlot.NONE);
        project.addItem(GanttPlot.NONE);
        finalStart.setSelectedItem(GanttPlot.NONE);
        finalEnd.setSelectedItem(GanttPlot.NONE);
        percent.setSelectedItem(GanttPlot.NONE);
        project.setSelectedItem(GanttPlot.NONE);
        step.addItemListener(check);
        planStart.addItemListener(check);
        planEnd.addItemListener(check);
        step.addItemListener(tooltipListener);
        planStart.addItemListener(tooltipListener);
        planEnd.addItemListener(tooltipListener);
        finalStart.addItemListener(tooltipListener);
        finalEnd.addItemListener(tooltipListener);
        percent.addItemListener(tooltipListener);
        project.addItemListener(tooltipListener);
    }

    private void checkBoxUse()
    {
        boolean flag = step.getSelectedItem() != null;
        planStart.setEnabled(flag);
        planEnd.setEnabled(flag);
        finalStart.setEnabled(flag);
        finalEnd.setEnabled(flag);
        percent.setEnabled(flag);
        project.setEnabled(flag);
        if(planStart.isEnabled() && planEnd.isEnabled())
            project.setEnabled(planStart.getSelectedItem() != null && planEnd.getSelectedItem() != null);
    }

    public void updateBean(ChartCollection chartcollection)
    {
        GanttTableDefinition gantttabledefinition = new GanttTableDefinition();
        Object obj = step.getSelectedItem();
        Object obj1 = planStart.getSelectedItem();
        Object obj2 = planEnd.getSelectedItem();
        Object obj3 = finalStart.getSelectedItem();
        Object obj4 = finalEnd.getSelectedItem();
        Object obj5 = percent.getSelectedItem();
        Object obj6 = project.getSelectedItem();
        if(obj != null)
            gantttabledefinition.setStepString(obj.toString());
        if(obj1 != null)
            gantttabledefinition.setPlanStart(obj1.toString());
        if(obj2 != null)
            gantttabledefinition.setPlanEnd(obj2.toString());
        if(obj3 != null)
            gantttabledefinition.setRealStart(obj3.toString());
        if(obj4 != null)
            gantttabledefinition.setRealEnd(obj4.toString());
        if(obj5 != null)
            gantttabledefinition.setProgress(obj5.toString());
        if(obj6 != null)
            gantttabledefinition.setItem(obj6.toString());
        chartcollection.getSelectedChart().setFilterDefinition(gantttabledefinition);
    }

    public void populateBean(ChartCollection chartcollection)
    {
        super.populateBean(chartcollection);
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        if(topdefinitionprovider instanceof GanttTableDefinition)
        {
            GanttTableDefinition gantttabledefinition = (GanttTableDefinition)topdefinitionprovider;
            combineCustomEditValue(step, gantttabledefinition.getStepString());
            combineCustomEditValue(planStart, gantttabledefinition.getPlanStart());
            combineCustomEditValue(planEnd, gantttabledefinition.getPlanEnd());
            combineCustomEditValue(finalStart, gantttabledefinition.getRealStart());
            combineCustomEditValue(finalEnd, gantttabledefinition.getRealEnd());
            combineCustomEditValue(percent, gantttabledefinition.getProgress());
            combineCustomEditValue(project, gantttabledefinition.getItem());
        }
        checkBoxUse();
    }

    protected void refreshBoxListWithSelectTableData(java.util.List list)
    {
        refreshBoxItems(step, list);
        refreshBoxItems(planStart, list);
        refreshBoxItems(planEnd, list);
        refreshBoxItems(finalStart, list);
        refreshBoxItems(finalEnd, list);
        refreshBoxItems(percent, list);
        refreshBoxItems(project, list);
        finalStart.addItem(GanttPlot.NONE);
        finalEnd.addItem(GanttPlot.NONE);
        percent.addItem(GanttPlot.NONE);
        project.addItem(GanttPlot.NONE);
    }

    public void clearAllBoxList()
    {
        clearBoxItems(step);
        clearBoxItems(planStart);
        clearBoxItems(planEnd);
        clearBoxItems(finalStart);
        clearBoxItems(finalEnd);
        clearBoxItems(percent);
        clearBoxItems(project);
        finalStart.addItem(GanttPlot.NONE);
        finalEnd.addItem(GanttPlot.NONE);
        percent.addItem(GanttPlot.NONE);
        project.addItem(GanttPlot.NONE);
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
