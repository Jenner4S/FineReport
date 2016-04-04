// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.base.Utils;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.StockPlot;
import com.fr.chart.chartdata.StockLabel;
import com.fr.chart.chartdata.StockTableDefinition;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.UIEditLabel;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data.table:
//            AbstractTableDataContentPane

public class StockPlotTableDataContentPane extends AbstractTableDataContentPane
{

    private static final String TIMEAXIS = Inter.getLocText("Chart-Horizontal_TimeAxis");
    private static final int LABEL_WIDTH = 105;
    private UIComboBox axisBox;
    private UIComboBox volumeBox;
    private UIComboBox openBox;
    private UIComboBox highBox;
    private UIComboBox lowBox;
    private UIComboBox closeBox;
    private UIEditLabel volumeLabel;
    private UIEditLabel openLabel;
    private UIEditLabel highLabel;
    private UIEditLabel lowLabel;
    private UIEditLabel closeLabel;

    public StockPlotTableDataContentPane(ChartDataPane chartdatapane)
    {
        volumeLabel = new UIEditLabel(StockLabel.VOLUMEN, 4) {

            final StockPlotTableDataContentPane this$0;

            protected void doAfterMousePress()
            {
                clearBackGround();
            }

            
            {
                this$0 = StockPlotTableDataContentPane.this;
                super(s, i);
            }
        }
;
        openLabel = new UIEditLabel(StockLabel.OPEN, 4) {

            final StockPlotTableDataContentPane this$0;

            protected void doAfterMousePress()
            {
                clearBackGround();
            }

            
            {
                this$0 = StockPlotTableDataContentPane.this;
                super(s, i);
            }
        }
;
        highLabel = new UIEditLabel(StockLabel.HIGHT, 4) {

            final StockPlotTableDataContentPane this$0;

            protected void doAfterMousePress()
            {
                clearBackGround();
            }

            
            {
                this$0 = StockPlotTableDataContentPane.this;
                super(s, i);
            }
        }
;
        lowLabel = new UIEditLabel(StockLabel.LOW, 4) {

            final StockPlotTableDataContentPane this$0;

            protected void doAfterMousePress()
            {
                clearBackGround();
            }

            
            {
                this$0 = StockPlotTableDataContentPane.this;
                super(s, i);
            }
        }
;
        closeLabel = new UIEditLabel(StockLabel.CLOSE, 4) {

            final StockPlotTableDataContentPane this$0;

            protected void doAfterMousePress()
            {
                clearBackGround();
            }

            
            {
                this$0 = StockPlotTableDataContentPane.this;
                super(s, i);
            }
        }
;
        setLayout(new BorderLayout());
        axisBox = new UIComboBox();
        volumeBox = new UIComboBox();
        openBox = new UIComboBox();
        highBox = new UIComboBox();
        lowBox = new UIComboBox();
        closeBox = new UIComboBox();
        axisBox.setPreferredSize(new Dimension(90, 20));
        volumeBox.setPreferredSize(new Dimension(90, 20));
        openBox.setPreferredSize(new Dimension(90, 20));
        highBox.setPreferredSize(new Dimension(90, 20));
        lowBox.setPreferredSize(new Dimension(90, 20));
        closeBox.setPreferredSize(new Dimension(90, 20));
        double d = -2D;
        double ad[] = {
            105D, d
        };
        double ad1[] = {
            d, d, d, d, d, d, d, d, d
        };
        Component acomponent[][] = {
            {
                new BoldFontTextLabel((new StringBuilder()).append(TIMEAXIS).append(":").toString(), 4), axisBox
            }, {
                volumeLabel, volumeBox
            }, {
                openLabel, openBox
            }, {
                highLabel, highBox
            }, {
                lowLabel, lowBox
            }, {
                closeLabel, closeBox
            }
        };
        javax.swing.JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        add(jpanel, "Center");
        volumeBox.addItem(StockPlot.NONE);
        openBox.addItem(StockPlot.NONE);
        axisBox.addItemListener(tooltipListener);
        volumeBox.addItemListener(tooltipListener);
        openBox.addItemListener(tooltipListener);
        highBox.addItemListener(tooltipListener);
        lowBox.addItemListener(tooltipListener);
        closeBox.addItemListener(tooltipListener);
    }

    private void clearBackGround()
    {
        volumeLabel.resetNomalrBackground();
        openLabel.resetNomalrBackground();
        highLabel.resetNomalrBackground();
        lowLabel.resetNomalrBackground();
        closeLabel.resetNomalrBackground();
    }

    protected void refreshBoxListWithSelectTableData(java.util.List list)
    {
        refreshBoxItems(axisBox, list);
        refreshBoxItems(volumeBox, list);
        refreshBoxItems(openBox, list);
        refreshBoxItems(highBox, list);
        refreshBoxItems(lowBox, list);
        refreshBoxItems(closeBox, list);
        volumeBox.addItem(StockPlot.NONE);
        openBox.addItem(StockPlot.NONE);
    }

    public void clearAllBoxList()
    {
        clearBoxItems(axisBox);
        clearBoxItems(volumeBox);
        clearBoxItems(openBox);
        clearBoxItems(highBox);
        clearBoxItems(lowBox);
        clearBoxItems(closeBox);
        volumeBox.addItem(StockPlot.NONE);
        openBox.addItem(StockPlot.NONE);
    }

    public void populateBean(ChartCollection chartcollection)
    {
        if(chartcollection == null)
            return;
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        if(topdefinitionprovider instanceof StockTableDefinition)
        {
            StockTableDefinition stocktabledefinition = (StockTableDefinition)topdefinitionprovider;
            StockLabel stocklabel = stocktabledefinition.getStockLabel();
            volumeLabel.setText(stocklabel.getVolumeLabel());
            openLabel.setText(stocklabel.getOpenLabel());
            highLabel.setText(stocklabel.getHighLabel());
            lowLabel.setText(stocklabel.getLowLabel());
            closeLabel.setText(stocklabel.getLowLabel());
            combineCustomEditValue(axisBox, stocktabledefinition.getCateTime());
            combineCustomEditValue(volumeBox, stocktabledefinition.getVolumnString());
            combineCustomEditValue(openBox, stocktabledefinition.getOpenString());
            combineCustomEditValue(highBox, stocktabledefinition.getHighString());
            combineCustomEditValue(lowBox, stocktabledefinition.getLowString());
            combineCustomEditValue(closeBox, stocktabledefinition.getCloseString());
        }
    }

    public void updateBean(ChartCollection chartcollection)
    {
        if(chartcollection != null)
        {
            StockTableDefinition stocktabledefinition = new StockTableDefinition();
            chartcollection.getSelectedChart().setFilterDefinition(stocktabledefinition);
            stocktabledefinition.setStockLabels(new StockLabel(volumeLabel.getText(), openLabel.getText(), highLabel.getText(), lowLabel.getText(), closeLabel.getText()));
            stocktabledefinition.setCateTime(Utils.objectToString(axisBox.getSelectedItem()));
            stocktabledefinition.setVolumnString(Utils.objectToString(volumeBox.getSelectedItem()));
            stocktabledefinition.setOpenString(Utils.objectToString(openBox.getSelectedItem()));
            stocktabledefinition.setHighString(Utils.objectToString(highBox.getSelectedItem()));
            stocktabledefinition.setLowString(Utils.objectToString(lowBox.getSelectedItem()));
            stocktabledefinition.setCloseString(Utils.objectToString(closeBox.getSelectedItem()));
            StockLabel stocklabel = stocktabledefinition.getStockLabel();
            volumeLabel.setText(stocklabel.getVolumeLabel());
            openLabel.setText(stocklabel.getOpenLabel());
            highLabel.setText(stocklabel.getHighLabel());
            lowLabel.setText(stocklabel.getLowLabel());
            closeLabel.setText(stocklabel.getCloseLabel());
        }
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
