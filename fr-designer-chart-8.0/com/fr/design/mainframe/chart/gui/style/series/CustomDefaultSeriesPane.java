// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.base.AttrAreaSeries;
import com.fr.chart.base.AttrAxisPosition;
import com.fr.chart.base.AttrBarSeries;
import com.fr.chart.base.AttrLineSeries;
import com.fr.chart.base.ChartAxisPosition;
import com.fr.chart.base.ChartCustomRendererType;
import com.fr.chart.chartglyph.CustomAttr;
import com.fr.chart.chartglyph.Marker;
import com.fr.chart.chartglyph.MarkerFactory;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.xcombox.MarkerComboBox;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class CustomDefaultSeriesPane extends BasicPane
{
    private class CustomAreaDefaultSeriesPane extends FurtherBasicBeanPane
    {

        private UICheckBox isCurve;
        protected MarkerComboBox markerPane;
        final CustomDefaultSeriesPane this$0;

        public CustomAttr updateBean()
        {
            return new CustomAttr();
        }

        public void updateBean(CustomAttr customattr)
        {
            customattr.removeAll();
            AttrAreaSeries attrareaseries = new AttrAreaSeries();
            customattr.addDataSeriesCondition(attrareaseries);
            attrareaseries.setCurve(isCurve.isSelected());
            attrareaseries.setMarkerType(markerPane.getSelectedMarkder().getMarkerType());
            attrareaseries.setAxisPosition(ChartAxisPosition.AXIS_LEFT);
        }

        public void populateBean(CustomAttr customattr)
        {
            AttrAreaSeries attrareaseries = new AttrAreaSeries();
            attrareaseries.setAxisPosition(ChartAxisPosition.AXIS_LEFT);
            if(customattr.getExisted(com/fr/chart/base/AttrAreaSeries) != null)
                attrareaseries = (AttrAreaSeries)customattr.getExisted(com/fr/chart/base/AttrAreaSeries);
            isCurve.setSelected(attrareaseries.isCurve());
            markerPane.setSelectedMarker(MarkerFactory.createMarker(attrareaseries.getMarkerType()));
            customattr.removeAll();
            customattr.addDataSeriesCondition(attrareaseries);
        }

        public String title4PopupWindow()
        {
            return CustomDefaultSeriesPane.AREA_STACK;
        }

        public void reset()
        {
        }

        public boolean accept(Object obj)
        {
            return obj instanceof AttrAreaSeries;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((CustomAttr)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((CustomAttr)obj);
        }

        public CustomAreaDefaultSeriesPane()
        {
            this$0 = CustomDefaultSeriesPane.this;
            super();
            setLayout(new BorderLayout());
            isCurve = new UICheckBox(Inter.getLocText("FR-Chart-Curve_Line"));
            markerPane = new MarkerComboBox(MarkerFactory.getMarkerArray());
            double d = -2D;
            double d1 = -1D;
            double ad[] = {
                d, d1
            };
            double ad1[] = {
                d, d
            };
            Component acomponent[][] = {
                {
                    new UILabel(Inter.getLocText("FR-Chart-Line_Style")), isCurve
                }, {
                    new BoldFontTextLabel(Inter.getLocText("FR-Chart-Marker_Type")), markerPane
                }
            };
            add(TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad), "North");
        }
    }

    private class CustomLineDefaultSeriesPane extends FurtherBasicBeanPane
    {

        protected UICheckBox isCurve;
        protected UIButtonGroup isNullValueBreak;
        protected LineComboBox lineStyle;
        protected MarkerComboBox markerPane;
        final CustomDefaultSeriesPane this$0;

        public CustomAttr updateBean()
        {
            return new CustomAttr();
        }

        public void updateBean(CustomAttr customattr)
        {
            customattr.removeAll();
            AttrLineSeries attrlineseries = new AttrLineSeries();
            customattr.addDataSeriesCondition(attrlineseries);
            attrlineseries.setCurve(isCurve.isSelected());
            attrlineseries.setNullValueBreak(isNullValueBreak.getSelectedIndex() == 0);
            attrlineseries.setLineStyle(lineStyle.getSelectedLineStyle());
            attrlineseries.setMarkerType(markerPane.getSelectedMarkder().getMarkerType());
            attrlineseries.setAxisPosition(ChartAxisPosition.AXIS_LEFT);
        }

        public void populateBean(CustomAttr customattr)
        {
            AttrLineSeries attrlineseries = new AttrLineSeries();
            attrlineseries.setAxisPosition(ChartAxisPosition.AXIS_LEFT);
            if(customattr.getExisted(com/fr/chart/base/AttrLineSeries) != null)
                attrlineseries = (AttrLineSeries)customattr.getExisted(com/fr/chart/base/AttrLineSeries);
            isCurve.setSelected(attrlineseries.isCurve());
            isNullValueBreak.setSelectedIndex(attrlineseries.isNullValueBreak() ? 0 : 1);
            lineStyle.setSelectedLineStyle(attrlineseries.getLineStyle());
            markerPane.setSelectedMarker(MarkerFactory.createMarker(attrlineseries.getMarkerType()));
            customattr.removeAll();
            customattr.addDataSeriesCondition(attrlineseries);
        }

        public String title4PopupWindow()
        {
            return CustomDefaultSeriesPane.LINE;
        }

        public void reset()
        {
        }

        public boolean accept(Object obj)
        {
            return obj instanceof AttrLineSeries;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((CustomAttr)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((CustomAttr)obj);
        }

        public CustomLineDefaultSeriesPane()
        {
            this$0 = CustomDefaultSeriesPane.this;
            super();
            setLayout(new BorderLayout());
            isCurve = new UICheckBox(Inter.getLocText("Chart_Curve"));
            lineStyle = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
            markerPane = new MarkerComboBox(MarkerFactory.getMarkerArray());
            String as[] = {
                Inter.getLocText("Chart_Null_Value_Break"), Inter.getLocText("Chart_Null_Value_Continue")
            };
            Boolean aboolean[] = {
                Boolean.valueOf(true), Boolean.valueOf(false)
            };
            isNullValueBreak = new UIButtonGroup(as, aboolean);
            double d = -2D;
            double d1 = -1D;
            double ad[] = {
                d, d1
            };
            double ad1[] = {
                d, d, d, d
            };
            Component acomponent[][] = {
                {
                    new BoldFontTextLabel(Inter.getLocText("Chart_Line_Style")), isCurve
                }, {
                    new BoldFontTextLabel(Inter.getLocText("Line-Style")), lineStyle
                }, {
                    new BoldFontTextLabel(Inter.getLocText("FR-Chart-Marker_Type")), markerPane
                }, {
                    new BoldFontTextLabel(Inter.getLocText("Null_Value_Show")), isNullValueBreak
                }
            };
            add(TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad), "North");
        }
    }

    private class CustomBar3DDefaultSeriesPane extends FurtherBasicBeanPane
    {

        private String title;
        final CustomDefaultSeriesPane this$0;

        public CustomAttr updateBean()
        {
            return new CustomAttr();
        }

        public void updateBean(CustomAttr customattr)
        {
            customattr.removeAll();
            customattr.addDataSeriesCondition(new AttrAxisPosition(ChartAxisPosition.AXIS_LEFT));
        }

        public void populateBean(CustomAttr customattr)
        {
            customattr.removeAll();
            customattr.addDataSeriesCondition(new AttrAxisPosition(ChartAxisPosition.AXIS_LEFT));
        }

        public String title4PopupWindow()
        {
            return title;
        }

        public void reset()
        {
        }

        public boolean accept(Object obj)
        {
            return obj instanceof AttrAxisPosition;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((CustomAttr)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((CustomAttr)obj);
        }

        public CustomBar3DDefaultSeriesPane(String s)
        {
            this$0 = CustomDefaultSeriesPane.this;
            super();
            title = "";
            title = s;
        }
    }

    private class CustomBarDefaultSeriesPane extends FurtherBasicBeanPane
    {

        private static final double HUNDRED = 100D;
        private static final double MAX_TIME = 5D;
        private String title;
        private UINumberDragPane seriesGap;
        private UINumberDragPane categoryGap;
        final CustomDefaultSeriesPane this$0;

        public CustomAttr updateBean()
        {
            return new CustomAttr();
        }

        public void updateBean(CustomAttr customattr)
        {
            customattr.removeAll();
            AttrBarSeries attrbarseries = new AttrBarSeries();
            attrbarseries.setSeriesOverlapPercent(seriesGap.updateBean().doubleValue() / 100D);
            attrbarseries.setCategoryIntervalPercent(categoryGap.updateBean().doubleValue() / 100D);
            attrbarseries.setAxisPosition(ChartAxisPosition.AXIS_LEFT);
            customattr.addDataSeriesCondition(attrbarseries);
        }

        public void populateBean(CustomAttr customattr)
        {
            AttrBarSeries attrbarseries = new AttrBarSeries();
            attrbarseries.setAxisPosition(ChartAxisPosition.AXIS_LEFT);
            if(customattr.getExisted(com/fr/chart/base/AttrBarSeries) != null)
                attrbarseries = (AttrBarSeries)customattr.getExisted(com/fr/chart/base/AttrBarSeries);
            seriesGap.populateBean(Double.valueOf(attrbarseries.getSeriesOverlapPercent() * 100D));
            categoryGap.populateBean(Double.valueOf(attrbarseries.getCategoryIntervalPercent() * 100D));
            customattr.removeAll();
            customattr.addDataSeriesCondition(attrbarseries);
        }

        public String title4PopupWindow()
        {
            return title;
        }

        public void reset()
        {
        }

        public boolean accept(Object obj)
        {
            return obj instanceof AttrBarSeries;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((CustomAttr)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((CustomAttr)obj);
        }

        public CustomBarDefaultSeriesPane(String s)
        {
            this$0 = CustomDefaultSeriesPane.this;
            super();
            title = "";
            title = s;
            setLayout(new BorderLayout());
            seriesGap = new UINumberDragPane(-100D, 100D);
            categoryGap = new UINumberDragPane(0.0D, 500D);
            double d = -2D;
            double d1 = -1D;
            double ad[] = {
                d, d1
            };
            double ad1[] = {
                d, d
            };
            Component acomponent[][] = {
                {
                    new UILabel(Inter.getLocText("FR-Chart-Series_Gap")), seriesGap
                }, {
                    new UILabel(Inter.getLocText("FR-Chart-Category_Gap")), categoryGap
                }
            };
            add(TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad), "North");
        }
    }


    private static final String BAR = Inter.getLocText("ChartF-Column");
    private static final String BAR_STACK = Inter.getLocText("I-BarStyle_NormalStack");
    private static final String BAR3D = Inter.getLocText("FR-Chart-Bar3D_Chart");
    private static final String BAR3D_STACK = Inter.getLocText("FR-Chart-Bar3DStack_Chart");
    private static final String LINE = Inter.getLocText("I-LineStyle_Line");
    private static final String AREA_STACK = Inter.getLocText("I-AreaStyle_Stack");
    private UIComboBoxPane boxPane;
    CustomBarDefaultSeriesPane barSeriesPane;
    CustomBarDefaultSeriesPane barStackSeriesPane;
    CustomBar3DDefaultSeriesPane bar3DSeriesPane;
    CustomBar3DDefaultSeriesPane bar3DStackSeriesPane;
    CustomLineDefaultSeriesPane lineSeriesPane;
    CustomAreaDefaultSeriesPane areaSeriesPane;
    private static final HashMap COM_MAP = new HashMap() {

            
            {
                put(ChartCustomRendererType.BAR_RENDERER, Integer.valueOf(0));
                put(ChartCustomRendererType.BAR_STACK, Integer.valueOf(1));
                put(ChartCustomRendererType.BAR3D, Integer.valueOf(2));
                put(ChartCustomRendererType.BAR3D_STACK, Integer.valueOf(3));
                put(ChartCustomRendererType.LINE_RENDERER, Integer.valueOf(4));
                put(ChartCustomRendererType.AREA_STACK, Integer.valueOf(5));
            }
    }
;

    public CustomDefaultSeriesPane()
    {
        barSeriesPane = new CustomBarDefaultSeriesPane(BAR);
        barStackSeriesPane = new CustomBarDefaultSeriesPane(BAR_STACK);
        bar3DSeriesPane = new CustomBar3DDefaultSeriesPane(BAR3D);
        bar3DStackSeriesPane = new CustomBar3DDefaultSeriesPane(BAR3D_STACK);
        lineSeriesPane = new CustomLineDefaultSeriesPane();
        areaSeriesPane = new CustomAreaDefaultSeriesPane();
        setLayout(new BorderLayout());
        boxPane = new UIComboBoxPane() {

            final CustomDefaultSeriesPane this$0;

            protected java.util.List initPaneList()
            {
                ArrayList arraylist = new ArrayList();
                arraylist.add(barSeriesPane);
                arraylist.add(barStackSeriesPane);
                arraylist.add(bar3DSeriesPane);
                arraylist.add(bar3DStackSeriesPane);
                arraylist.add(lineSeriesPane);
                arraylist.add(areaSeriesPane);
                return arraylist;
            }

            protected String title4PopupWindow()
            {
                return "";
            }

            
            {
                this$0 = CustomDefaultSeriesPane.this;
                super();
            }
        }
;
        add(boxPane, "North");
    }

    public void populateBean(CustomAttr customattr)
    {
        boxPane.setSelectedIndex(((Integer)COM_MAP.get(customattr.getRenderer())).intValue());
        FurtherBasicBeanPane furtherbasicbeanpane = (FurtherBasicBeanPane)boxPane.getCards().get(((Integer)COM_MAP.get(customattr.getRenderer())).intValue());
        furtherbasicbeanpane.populateBean(customattr);
    }

    public void updateBean(CustomAttr customattr)
    {
        int i = boxPane.getSelectedIndex();
        FurtherBasicBeanPane furtherbasicbeanpane = (FurtherBasicBeanPane)boxPane.getCards().get(i);
        if(((Integer)COM_MAP.get(customattr.getRenderer())).intValue() == boxPane.getSelectedIndex())
        {
            furtherbasicbeanpane.updateBean(customattr);
        } else
        {
            customattr.removeAll();
            customattr.setRenderer(indexToRender(boxPane.getSelectedIndex()));
            furtherbasicbeanpane.populateBean(customattr);
        }
        customattr.setUseRenderer(customattr.getRenderer());
    }

    private ChartCustomRendererType indexToRender(int i)
    {
        for(Iterator iterator = COM_MAP.keySet().iterator(); iterator.hasNext();)
        {
            ChartCustomRendererType chartcustomrenderertype = (ChartCustomRendererType)iterator.next();
            int j = ((Integer)COM_MAP.get(chartcustomrenderertype)).intValue();
            if(j == i)
                return chartcustomrenderertype;
        }

        return ChartCustomRendererType.BAR_RENDERER;
    }

    protected String title4PopupWindow()
    {
        return "";
    }



}
