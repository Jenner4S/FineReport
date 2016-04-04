// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.MapPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.chart.ChartDesignEditPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.form.ui.ChartBook;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import java.util.Iterator;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe:
//            AbstractMapPlotPane4ToolBar, ChartDesigner, MapArrayPane, DesignerContext

public class MapPlotPane4ToolBar extends AbstractMapPlotPane4ToolBar
{

    private static final int WORLD_MAP = 0;
    private static final int STATE_MAP = 1;
    private static final int PROVINCE_MAP = 2;
    private static final int CUSTOM_MAP = 3;
    private static final int BUTTON_WIDTH = 44;
    private static final String TYPE_NAMES[] = {
        Inter.getLocText("FR-Chart-World_Map"), Inter.getLocText("FR-Chart-State_Map"), Inter.getLocText("FR-Chart-Province_Map"), Inter.getLocText("FR-Chart-Custom_Map")
    };
    private String lastEditingName;
    private UIButton mapEditButton;
    protected UIComboBox detailMaps;
    private java.awt.event.ItemListener detailListener;
    private java.awt.event.ActionListener mapEditListener;

    public MapPlotPane4ToolBar(ChartDesigner chartdesigner)
    {
        super(chartdesigner);
        lastEditingName = "";
        mapEditButton = new UIButton(Inter.getLocText("FR-Chart-Data_Edit")) {

            final MapPlotPane4ToolBar this$0;

            public java.awt.Dimension getPreferredSize()
            {
                return new java.awt.Dimension(44, 22);
            }

            
            {
                this$0 = MapPlotPane4ToolBar.this;
                super(s);
            }
        }
;
        detailMaps = new UIComboBox() {

            final MapPlotPane4ToolBar this$0;

            public java.awt.Dimension getPreferredSize()
            {
                return new java.awt.Dimension(230, 22);
            }

            
            {
                this$0 = MapPlotPane4ToolBar.this;
                super();
            }
        }
;
        detailListener = new java.awt.event.ItemListener() {

            final MapPlotPane4ToolBar this$0;

            public void itemStateChanged(java.awt.event.ItemEvent itemevent)
            {
                fireMapChange();
            }

            
            {
                this$0 = MapPlotPane4ToolBar.this;
                super();
            }
        }
;
        mapEditListener = new java.awt.event.ActionListener() {

            final MapPlotPane4ToolBar this$0;

            public void actionPerformed(java.awt.event.ActionEvent actionevent)
            {
                String s = "";
                if(detailMaps.getSelectedItem() != null)
                    s = detailMaps.getSelectedItem().toString();
                final MapArrayPane mapArrayPane = new MapArrayPane(mapTypeComboBox.getSelectedItem().toString(), s, chartDesigner) {

                    final _cls4 this$1;

                    public void updateBeans()
                    {
                        super.updateBeans();
                        if(reCalculateDetailsMaps(mapTypeComboBox.getSelectedItem().toString(), lastEditingName) || ComparatorUtils.equals("", lastEditingName))
                        {
                            detailMaps.setSelectedItem(lastEditingName);
                            ChartCollection chartcollection = (ChartCollection)((ChartBook)chartDesigner.getTarget()).getChartCollection();
                            Chart chart = chartcollection.getSelectedChart();
                            if(chart.getPlot().isMapPlot())
                            {
                                MapPlot mapplot = (MapPlot)chart.getPlot();
                                mapplot.setMapName(lastEditingName);
                            }
                        }
                    }

                    protected void update4Edited(String s1)
                    {
                        lastEditingName = s1;
                    }

                    
                    {
                        this$1 = _cls4.this;
                        super(s, s1, chartdesigner);
                    }
                }
;
                BasicDialog basicdialog = mapArrayPane.showWindow4ChartMapArray(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

                    final MapArrayPane val$mapArrayPane;
                    final _cls4 this$1;

                    public void doOk()
                    {
                        mapArrayPane.updateBeans();
                    }

                    
                    {
                        this$1 = _cls4.this;
                        mapArrayPane = maparraypane;
                        super();
                    }
                }
);
                basicdialog.setModal(true);
                mapArrayPane.setToolBarPane(MapPlotPane4ToolBar.this);
                mapArrayPane.populate(MapSvgXMLHelper.getInstance().getAllMapObjects4Cate(mapTypeComboBox.getSelectedItem().toString()));
                if(detailMaps.getSelectedItem() != null)
                    mapArrayPane.setSelectedName(detailMaps.getSelectedItem().toString());
                basicdialog.setVisible(true);
            }

            
            {
                this$0 = MapPlotPane4ToolBar.this;
                super();
            }
        }
;
        add(detailMaps);
        detailMaps.addItemListener(detailListener);
        mapEditButton.addActionListener(mapEditListener);
        add(mapEditButton);
    }

    public void populateMapPane(String s)
    {
        super.populateMapPane(s);
        populateDetilMaps(mapTypeComboBox.getSelectedItem().toString());
        detailMaps.removeItemListener(detailListener);
        detailMaps.setSelectedItem(s);
        detailMaps.addItemListener(detailListener);
    }

    public void fireMapChange()
    {
        MapPlot mapplot = new MapPlot();
        String s = "";
        if(detailMaps.getSelectedItem() != null)
            s = detailMaps.getSelectedItem().toString();
        mapplot.setMapName(s);
        ChartCollection chartcollection = (ChartCollection)((ChartBook)chartDesigner.getTarget()).getChartCollection();
        Chart chart = chartcollection.getSelectedChart();
        chart.setPlot(mapplot);
        ChartDesignEditPane.getInstance().populate(chartcollection);
        chartDesigner.fireTargetModified();
    }

    protected void calculateDetailMaps(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            populateDetilMaps(Inter.getLocText("FR-Chart-World_Map"));
            break;

        case 1: // '\001'
            populateDetilMaps(Inter.getLocText("FR-Chart-State_Map"));
            break;

        case 2: // '\002'
            populateDetilMaps(Inter.getLocText("FR-Chart-Province_Map"));
            break;

        case 3: // '\003'
            populateDetilMaps(Inter.getLocText("FR-Chart-Custom_Map"));
            break;

        default:
            populateDetilMaps(Inter.getLocText("FR-Chart-State_Map"));
            break;
        }
        fireMapChange();
    }

    private boolean reCalculateDetailsMaps(String s, String s1)
    {
        detailMaps.removeItemListener(detailListener);
        detailMaps.removeAllItems();
        List list = MapSvgXMLHelper.getInstance().getNamesListWithCateName(s);
        boolean flag = false;
        Iterator iterator = list.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            Object obj = iterator.next();
            detailMaps.addItem(obj);
            if(ComparatorUtils.equals(s1, obj))
                flag = true;
        } while(true);
        detailMaps.addItemListener(detailListener);
        return flag;
    }

    protected void populateDetilMaps(String s)
    {
        detailMaps.removeItemListener(detailListener);
        detailMaps.removeAllItems();
        List list = MapSvgXMLHelper.getInstance().getNamesListWithCateName(s);
        Object obj;
        for(Iterator iterator = list.iterator(); iterator.hasNext(); detailMaps.addItem(obj))
            obj = iterator.next();

        detailMaps.addItemListener(detailListener);
        if(detailMaps.getSelectedItem() != null)
            lastEditingName = detailMaps.getSelectedItem().toString();
    }

    protected Plot getSelectedClonedPlot()
    {
        MapPlot mapplot = new MapPlot();
        populateDetilMaps(Inter.getLocText("FR-Chart-State_Map"));
        if(detailMaps.getSelectedItem() != null && !StringUtils.isEmpty(detailMaps.getSelectedItem().toString()))
            mapplot.setMapName(detailMaps.getSelectedItem().toString());
        return mapplot;
    }

    public String[] getMapTypes()
    {
        return TYPE_NAMES;
    }




}
