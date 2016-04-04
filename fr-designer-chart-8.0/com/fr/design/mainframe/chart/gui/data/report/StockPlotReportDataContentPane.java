// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.report;

import com.fr.base.ScreenResolution;
import com.fr.base.Utils;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.base.GlyphUtils;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.StockLabel;
import com.fr.chart.chartdata.StockReportDefinition;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.UIObserverListener;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.itable.UITable;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.gui.itable.UITableUI;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.TableUI;
import javax.swing.table.TableModel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data.report:
//            AbstractReportDataContentPane

public class StockPlotReportDataContentPane extends AbstractReportDataContentPane
{
    private class StockTableEditor extends UITableEditor
    {

        private TinyFormulaPane formulaComponent;
        private UITextField labelComponent;
        private int currentEditColumn;
        final StockPlotReportDataContentPane this$0;

        public Object getCellEditorValue()
        {
            if(currentEditColumn == 0)
                return labelComponent.getText();
            else
                return formulaComponent.getUITextField().getText();
        }

        public java.awt.Component getTableCellEditorComponent(JTable jtable, Object obj, boolean flag, int i, int j)
        {
            if(j == jtable.getModel().getColumnCount())
                return null;
            seriesPane.stopCellEditing();
            currentEditColumn = j;
            if(currentEditColumn == 0)
            {
                labelComponent.setText(getShowText(i, Utils.objectToString(obj)));
                return labelComponent;
            } else
            {
                formulaComponent.getUITextField().setText(Utils.objectToString(obj));
                return formulaComponent;
            }
        }

        private String getShowText(int i, String s)
        {
            if(i == 0)
                return StringUtils.cutStringEndWith(s, (new StringBuilder()).append("(").append(StockPlotReportDataContentPane.VOLUME).append(")").toString());
            if(i == 1)
                return StringUtils.cutStringEndWith(s, (new StringBuilder()).append("(").append(StockPlotReportDataContentPane.OPEN).append(")").toString());
            if(i == 2)
                return StringUtils.cutStringEndWith(s, (new StringBuilder()).append("(").append(StockPlotReportDataContentPane.HIGHT).append(")").toString());
            if(i == 3)
                return StringUtils.cutStringEndWith(s, (new StringBuilder()).append("(").append(StockPlotReportDataContentPane.LOW).append(")").toString());
            if(i == 4)
                return StringUtils.cutStringEndWith(s, (new StringBuilder()).append("(").append(StockPlotReportDataContentPane.CLOSE).append(")").toString());
            else
                return s;
        }

        public StockTableEditor()
        {
            this$0 = StockPlotReportDataContentPane.this;
            super();
            currentEditColumn = 1;
            labelComponent = new UITextField();
            formulaComponent = new TinyFormulaPane() {

                final StockPlotReportDataContentPane val$this$0;
                final StockTableEditor this$1;

                public void okEvent()
                {
                    seriesPane.stopCellEditing();
                    seriesPane.fireTargetChanged();
                }


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
;
            formulaComponent.setBackground(UIConstants.FLESH_BLUE);
            formulaComponent.getUITextField().registerChangeListener(new UIObserverListener() {

                final StockPlotReportDataContentPane val$this$0;
                final StockTableEditor this$1;

                public void doChange()
                {
                    seriesPane.fireTargetChanged();
                }


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
        }
    }


    private static final String AXIS = Inter.getLocText("Chart_HorialTimeAxis");
    private static final String VOLUME = Inter.getLocText("ChartF_Stock_Volume");
    private static final String OPEN = Inter.getLocText("ChartF_Stock_Open");
    private static final String HIGHT = Inter.getLocText("ChartF_Stock_High");
    private static final String LOW = Inter.getLocText("ChartF_Stock_Low");
    private static final String CLOSE = Inter.getLocText("ChartF_Stock_Close");
    private static final int PRE_WIDTH = 210;
    private static final int VOLUMN_INDEX = 0;
    private static final int OPEN_INDEX = 1;
    private static final int HIGH_INDEX = 2;
    private static final int LOW_INDEX = 3;
    private static final int CLOSE_INDEX = 4;
    private TinyFormulaPane axisTime;

    public StockPlotReportDataContentPane(ChartDataPane chartdatapane)
    {
        initEveryPane();
        axisTime = new TinyFormulaPane() {

            final StockPlotReportDataContentPane this$0;

            protected void initLayout()
            {
                setLayout(new java.awt.BorderLayout(4, 0));
                add(new BoldFontTextLabel(StockPlotReportDataContentPane.AXIS), "West");
                add(formulaTextField, "Center");
                add(formulaTextFieldButton, "East");
            }

            public java.awt.Dimension getPreferredSize()
            {
                java.awt.Dimension dimension = new java.awt.Dimension();
                dimension.width = 210;
                dimension.height = super.getPreferredSize().height;
                return dimension;
            }

            
            {
                this$0 = StockPlotReportDataContentPane.this;
                super();
            }
        }
;
        add(axisTime, "0,0,2,0");
        ArrayList arraylist = new ArrayList();
        arraylist.add(((Object) (new Object[] {
            VOLUME, ""
        })));
        arraylist.add(((Object) (new Object[] {
            OPEN, ""
        })));
        arraylist.add(((Object) (new Object[] {
            HIGHT, ""
        })));
        arraylist.add(((Object) (new Object[] {
            LOW, ""
        })));
        arraylist.add(((Object) (new Object[] {
            CLOSE, ""
        })));
        seriesPane.populateBean(arraylist);
        seriesPane.noAddUse();
    }

    protected void initSeriesPane()
    {
        seriesPane = new UICorrelationPane(columnNames()) {

            final StockPlotReportDataContentPane this$0;

            public UITableEditor createUITableEditor()
            {
                return new StockTableEditor();
            }

            protected boolean isDeletable()
            {
                return false;
            }

            protected UITable initUITable()
            {
                return new UITable(columnCount) {

                    final _cls2 this$1;

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
                        return j <= 1;
                    }

                    public TableUI getUI()
                    {
                        return new UITableUI() {

                            final _cls1 this$2;

                            protected boolean isDeletable()
                            {
                                return false;
                            }

                        
                        {
                            this$2 = _cls1.this;
                            super();
                        }
                        }
;
                    }

                    public void dealWithRollOver(int i)
                    {
                        String s = (String)getModel().getValueAt(i, 0);
                        double d = GlyphUtils.calculateTextDimensionWithNoRotation(s, new TextAttr(FRFont.getInstance()), ScreenResolution.getScreenResolution()).getWidth();
                        double d1 = getCellRect(i, 0, false).getWidth();
                        if(d <= d1)
                            setToolTipText(null);
                        else
                            setToolTipText(s);
                    }

                    
                    {
                        this$1 = _cls2.this;
                        super(i);
                    }
                }
;
            }

            transient 
            {
                this$0 = StockPlotReportDataContentPane.this;
                super(as);
            }
        }
;
    }

    public void populateBean(ChartCollection chartcollection)
    {
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        if(topdefinitionprovider instanceof StockReportDefinition)
        {
            StockReportDefinition stockreportdefinition = (StockReportDefinition)topdefinitionprovider;
            if(stockreportdefinition.getCategoryName() != null)
                axisTime.getUITextField().setText(stockreportdefinition.getCategoryName().toString());
            populateSeriesPane(stockreportdefinition);
        }
    }

    private void populateSeriesPane(StockReportDefinition stockreportdefinition)
    {
        StockLabel stocklabel = stockreportdefinition.getStockLabel();
        ArrayList arraylist = new ArrayList();
        String s = ComparatorUtils.equals(stocklabel.getVolumeLabel(), VOLUME) ? stocklabel.getVolumeLabel() : StringUtils.perfectEnd(stocklabel.getVolumeLabel(), (new StringBuilder()).append("(").append(VOLUME).append(")").toString());
        String s1 = ComparatorUtils.equals(stocklabel.getOpenLabel(), OPEN) ? stocklabel.getOpenLabel() : StringUtils.perfectEnd(stocklabel.getOpenLabel(), (new StringBuilder()).append("(").append(OPEN).append(")").toString());
        String s2 = ComparatorUtils.equals(stocklabel.getHighLabel(), HIGHT) ? stocklabel.getHighLabel() : StringUtils.perfectEnd(stocklabel.getHighLabel(), (new StringBuilder()).append("(").append(HIGHT).append(")").toString());
        String s3 = ComparatorUtils.equals(stocklabel.getLowLabel(), LOW) ? stocklabel.getLowLabel() : StringUtils.perfectEnd(stocklabel.getLowLabel(), (new StringBuilder()).append("(").append(LOW).append(")").toString());
        String s4 = ComparatorUtils.equals(stocklabel.getCloseLabel(), CLOSE) ? stocklabel.getCloseLabel() : StringUtils.perfectEnd(stocklabel.getCloseLabel(), (new StringBuilder()).append("(").append(CLOSE).append(")").toString());
        arraylist.add(((Object) (new Object[] {
            s, stockreportdefinition.getStockVolumn()
        })));
        arraylist.add(((Object) (new Object[] {
            s1, stockreportdefinition.getStockOpen()
        })));
        arraylist.add(((Object) (new Object[] {
            s2, stockreportdefinition.getStockHigh()
        })));
        arraylist.add(((Object) (new Object[] {
            s3, stockreportdefinition.getStockLow()
        })));
        arraylist.add(((Object) (new Object[] {
            s4, stockreportdefinition.getStockClose()
        })));
        seriesPane.populateBean(arraylist);
    }

    public void updateBean(ChartCollection chartcollection)
    {
        chartcollection.getSelectedChart().setFilterDefinition(new StockReportDefinition());
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        if(topdefinitionprovider instanceof StockReportDefinition)
        {
            StockReportDefinition stockreportdefinition = (StockReportDefinition)topdefinitionprovider;
            stockreportdefinition.setCategoryName(axisTime.getUITextField().getText());
            List list = seriesPane.updateBean();
            StockLabel stocklabel = new StockLabel(((Object[])list.get(0))[0].toString(), ((Object[])list.get(1))[0].toString(), ((Object[])list.get(2))[0].toString(), ((Object[])list.get(3))[0].toString(), ((Object[])list.get(4))[0].toString());
            stockreportdefinition.setStockLabel(stocklabel);
            stockreportdefinition.setStockVolumn(canBeFormula(((Object[])list.get(0))[1]));
            stockreportdefinition.setStockOpen(canBeFormula(((Object[])list.get(1))[1]));
            stockreportdefinition.setStockHigh(canBeFormula(((Object[])list.get(2))[1]));
            stockreportdefinition.setStockLow(canBeFormula(((Object[])list.get(3))[1]));
            stockreportdefinition.setStockClose(canBeFormula(((Object[])list.get(4))[1]));
            populateSeriesPane(stockreportdefinition);
        }
    }

    protected HashMap createNameValue(List list)
    {
        HashMap hashmap = new HashMap();
        for(int i = 0; i < list.size(); i++)
        {
            Object aobj[] = (Object[])list.get(i);
            hashmap.put(aobj[0], aobj[1]);
        }

        return hashmap;
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
