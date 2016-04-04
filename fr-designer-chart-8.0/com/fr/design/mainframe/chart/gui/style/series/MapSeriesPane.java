// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.base.Utils;
import com.fr.chart.base.ChartEnumDefinitions;
import com.fr.chart.chartattr.*;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.chart.chartglyph.MapHotAreaColor;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartFillStylePane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JSeparator;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            AbstractPlotSeriesPane, MapColorPickerPaneWithFormula, CombinedBubbleSeriesPane, CombinedPie2DSeriesPane, 
//            CombinedBar2DSeriesPane, UIColorPickerPane4Map

public class MapSeriesPane extends AbstractPlotSeriesPane
{

    private UICheckBox isHeatMap;
    private UIComboBox areaTitles;
    private UIColorPickerPane4Map colorPickPane;
    private AbstractPlotSeriesPane combinePane;

    public MapSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        super(chartstylepane, plot, false);
    }

    protected UIColorPickerPane4Map createColorPickerPane()
    {
        return new MapColorPickerPaneWithFormula();
    }

    protected ChartFillStylePane getFillStylePane()
    {
        return null;
    }

    protected JPanel getContentInPlotType()
    {
        isHeatMap = new UICheckBox(Inter.getLocText("FR-Chart-Heat_Map"));
        isHeatMap.addActionListener(new ActionListener() {

            final MapSeriesPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                checkHeatMapAreaTitles();
            }

            
            {
                this$0 = MapSeriesPane.this;
                super();
            }
        }
);
        areaTitles = new UIComboBox();
        areaTitles.setEnabled(false);
        colorPickPane = createColorPickerPane();
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d, d
        };
        Component acomponent[][] = {
            {
                isHeatMap, null
            }, {
                new UILabel(Inter.getLocText("Chart-Data_Configuration")), areaTitles
            }, {
                new UILabel(Inter.getLocText("ChartF_ValueRange_MatchColor")), null
            }, {
                colorPickPane, null
            }
        };
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        combinePane = getCombinePane();
        if(combinePane != null)
        {
            double ad2[] = {
                d1
            };
            double ad3[] = {
                d, d
            };
            Component acomponent1[][] = {
                {
                    new JSeparator()
                }, {
                    combinePane
                }
            };
            JPanel jpanel1 = TableLayoutHelper.createTableLayoutPane(acomponent1, ad3, ad2);
            JPanel jpanel2 = new JPanel(new BorderLayout());
            jpanel2.add(jpanel, "North");
            jpanel2.add(jpanel1, "Center");
            return jpanel2;
        } else
        {
            return jpanel;
        }
    }

    private AbstractPlotSeriesPane getCombinePane()
    {
        Object obj = null;
        if(chart != null && chart.getPlot() != null)
        {
            MapPlot mapplot = (MapPlot)chart.getPlot();
            static class _cls2
            {

                static final int $SwitchMap$com$fr$chart$base$ChartEnumDefinitions$MapType[];

                static 
                {
                    $SwitchMap$com$fr$chart$base$ChartEnumDefinitions$MapType = new int[com.fr.chart.base.ChartEnumDefinitions.MapType.values().length];
                    try
                    {
                        $SwitchMap$com$fr$chart$base$ChartEnumDefinitions$MapType[com.fr.chart.base.ChartEnumDefinitions.MapType.Map_Bubble.ordinal()] = 1;
                    }
                    catch(NoSuchFieldError nosuchfielderror) { }
                    try
                    {
                        $SwitchMap$com$fr$chart$base$ChartEnumDefinitions$MapType[com.fr.chart.base.ChartEnumDefinitions.MapType.Map_Pie.ordinal()] = 2;
                    }
                    catch(NoSuchFieldError nosuchfielderror1) { }
                    try
                    {
                        $SwitchMap$com$fr$chart$base$ChartEnumDefinitions$MapType[com.fr.chart.base.ChartEnumDefinitions.MapType.Map_Column.ordinal()] = 3;
                    }
                    catch(NoSuchFieldError nosuchfielderror2) { }
                }
            }

            if(mapplot.getMapType() != com.fr.chart.base.ChartEnumDefinitions.MapType.Map_Normal)
                switch(_cls2..SwitchMap.com.fr.chart.base.ChartEnumDefinitions.MapType[mapplot.getMapType().ordinal()])
                {
                case 1: // '\001'
                    obj = new CombinedBubbleSeriesPane(parentPane, mapplot.getCurrentCombinedPlot());
                    break;

                case 2: // '\002'
                    obj = new CombinedPie2DSeriesPane(parentPane, mapplot.getCurrentCombinedPlot());
                    break;

                case 3: // '\003'
                    obj = new CombinedBar2DSeriesPane(parentPane, mapplot.getCurrentCombinedPlot());
                    break;

                default:
                    obj = null;
                    break;
                }
        }
        return ((AbstractPlotSeriesPane) (obj));
    }

    private void checkHeatMapAreaTitles()
    {
        if(chart != null && chart.getFilterDefinition() != null)
        {
            MapPlot mapplot = (MapPlot)chart.getPlot();
            String s = Utils.objectToString(areaTitles.getSelectedItem());
            areaTitles.removeAllItems();
            TopDefinition topdefinition = (TopDefinition)chart.getFilterDefinition();
            ArrayList arraylist = topdefinition.getSeriesDefinitionList();
            int i = 0;
            int j = 0;
            for(int k = arraylist.size(); j < k; j++)
            {
                SeriesDefinition seriesdefinition = (SeriesDefinition)arraylist.get(j);
                String s1 = Utils.objectToString(seriesdefinition.getSeriesName());
                areaTitles.addItem(s1);
                if(ComparatorUtils.equals(s1, s))
                    i = j;
            }

            mapplot.setHeatIndex(i);
            if(i < arraylist.size())
                areaTitles.setSelectedIndex(i);
        }
        areaTitles.setEnabled(isHeatMap.isSelected());
    }

    public void populateBean(Plot plot)
    {
        super.populateBean(plot);
        if(plot instanceof MapPlot)
        {
            MapPlot mapplot = (MapPlot)plot;
            reLayoutWithMapPlot();
            if(combinePane != null)
                combinePane.populateBean(mapplot.getCurrentCombinedPlot());
            colorPickPane.populateBean(mapplot.getMapAreaColor());
            isHeatMap.setSelected(mapplot.isHeatMap());
            populateAreaTitles();
        }
    }

    private void populateAreaTitles()
    {
        if(chart != null && chart.getFilterDefinition() != null)
        {
            MapPlot mapplot = (MapPlot)chart.getPlot();
            areaTitles.removeAllItems();
            TopDefinition topdefinition = (TopDefinition)chart.getFilterDefinition();
            ArrayList arraylist = topdefinition.getSeriesDefinitionList();
            int i = 0;
            for(int j = arraylist.size(); i < j; i++)
            {
                SeriesDefinition seriesdefinition = (SeriesDefinition)arraylist.get(i);
                String s = Utils.objectToString(seriesdefinition.getSeriesName());
                areaTitles.addItem(s);
            }

            if(mapplot.getHeatIndex() < arraylist.size())
                areaTitles.setSelectedIndex(mapplot.getHeatIndex());
        }
        areaTitles.setEnabled(isHeatMap.isSelected());
    }

    public void updateBean(Plot plot)
    {
        super.updateBean(plot);
        if(plot instanceof MapPlot)
        {
            MapPlot mapplot = (MapPlot)plot;
            if(combinePane != null)
                combinePane.updateBean(mapplot.getCurrentCombinedPlot());
            MapHotAreaColor maphotareacolor = mapplot.getMapAreaColor();
            colorPickPane.updateBean(maphotareacolor);
            mapplot.setHeatMap(isHeatMap.isSelected());
            mapplot.setHeatIndex(mapplot.isHeatMap() ? areaTitles.getSelectedIndex() : 0);
        }
    }

    private void reLayoutWithMapPlot()
    {
        removeAll();
        fillStylePane = getFillStylePane();
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d, d
        };
        Component acomponent[][] = new Component[3][1];
        if(fillStylePane != null)
        {
            acomponent[0] = (new Component[] {
                fillStylePane
            });
            acomponent[1] = (new Component[] {
                new JSeparator()
            });
        }
        JPanel jpanel = getContentInPlotType();
        if(jpanel != null)
            acomponent[2] = (new Component[] {
                jpanel
            });
        JPanel jpanel1 = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        setLayout(new BorderLayout());
        add(jpanel1, "Center");
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((Plot)obj);
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Plot)obj);
    }

}
