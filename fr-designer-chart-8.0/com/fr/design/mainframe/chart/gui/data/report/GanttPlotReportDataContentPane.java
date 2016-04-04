// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.GanttReportDefinition;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.itable.UITable;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.general.Inter;
import java.util.*;
import javax.swing.event.ChangeEvent;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data.report:
//            AbstractReportDataContentPane

public class GanttPlotReportDataContentPane extends AbstractReportDataContentPane
{

    private static final String STEP = Inter.getLocText("ChartF-Step");
    private static final String START = Inter.getLocText("ChartF-PlansToBegin");
    private static final String END = Inter.getLocText("ChartF-EndOfTheProject");
    private static final String RESTART = Inter.getLocText("ChartF-TheActualStart");
    private static final String REEND = Inter.getLocText("ChartF-TheActualEnd");
    private static final String PERCENT = Inter.getLocText("StyleFormat-Percent");
    private static final String PRO = Inter.getLocText("Chart_Project");

    public GanttPlotReportDataContentPane(ChartDataPane chartdatapane)
    {
        initEveryPane();
        ArrayList arraylist = new ArrayList();
        arraylist.add(((Object) (new Object[] {
            STEP, ""
        })));
        arraylist.add(((Object) (new Object[] {
            START, ""
        })));
        arraylist.add(((Object) (new Object[] {
            END, ""
        })));
        arraylist.add(((Object) (new Object[] {
            RESTART, ""
        })));
        arraylist.add(((Object) (new Object[] {
            REEND, ""
        })));
        arraylist.add(((Object) (new Object[] {
            PERCENT, ""
        })));
        arraylist.add(((Object) (new Object[] {
            PRO, ""
        })));
        seriesPane.populateBean(arraylist);
        seriesPane.noAddUse();
    }

    protected void initSeriesPane()
    {
        seriesPane = new UICorrelationPane(columnNames()) {

            final GanttPlotReportDataContentPane this$0;

            public UITableEditor createUITableEditor()
            {
                return new AbstractReportDataContentPane.InnerTableEditor(GanttPlotReportDataContentPane.this);
            }

            protected UITable initUITable()
            {
                return new UITable(columnCount) {

                    final _cls1 this$1;

                    public UITableEditor createTableEditor()
                    {
                        return createUITableEditor();
                    }

                    public void tableCellEditingStopped(ChangeEvent changeevent)
                    {
                        stopPaneEditing(changeevent);
                    }

                    public boolean isCellEditable(int i, int j)
                    {
                        return j != 0;
                    }

                    
                    {
                        this$1 = _cls1.this;
                        super(i);
                    }
                }
;
            }

            transient 
            {
                this$0 = GanttPlotReportDataContentPane.this;
                super(as);
            }
        }
;
    }

    public void populateBean(ChartCollection chartcollection)
    {
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        if(topdefinitionprovider instanceof GanttReportDefinition)
        {
            GanttReportDefinition ganttreportdefinition = (GanttReportDefinition)topdefinitionprovider;
            ArrayList arraylist = new ArrayList();
            arraylist.add(((Object) (new Object[] {
                STEP, ganttreportdefinition.getStep()
            })));
            arraylist.add(((Object) (new Object[] {
                START, ganttreportdefinition.getPlanStart()
            })));
            arraylist.add(((Object) (new Object[] {
                END, ganttreportdefinition.getPlanEnd()
            })));
            arraylist.add(((Object) (new Object[] {
                RESTART, ganttreportdefinition.getRealStart()
            })));
            arraylist.add(((Object) (new Object[] {
                REEND, ganttreportdefinition.getRealEnd()
            })));
            arraylist.add(((Object) (new Object[] {
                PERCENT, ganttreportdefinition.getProgress()
            })));
            arraylist.add(((Object) (new Object[] {
                PRO, ganttreportdefinition.getItem()
            })));
            seriesPane.populateBean(arraylist);
        }
    }

    public void updateBean(ChartCollection chartcollection)
    {
        GanttReportDefinition ganttreportdefinition = new GanttReportDefinition();
        List list = seriesPane.updateBean();
        HashMap hashmap = createNameValue(list);
        ganttreportdefinition.setStep(canBeFormula(hashmap.get(STEP)));
        ganttreportdefinition.setPlanStart(canBeFormula(hashmap.get(START)));
        ganttreportdefinition.setPlanEnd(canBeFormula(hashmap.get(END)));
        ganttreportdefinition.setRealStart(canBeFormula(hashmap.get(RESTART)));
        ganttreportdefinition.setRealEnd(canBeFormula(hashmap.get(REEND)));
        ganttreportdefinition.setProgress(canBeFormula(hashmap.get(PERCENT)));
        ganttreportdefinition.setItem(canBeFormula(hashmap.get(PRO)));
        chartcollection.getSelectedChart().setFilterDefinition(ganttreportdefinition);
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
