// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.ChartPreStyleManagerProvider;
import com.fr.base.ChartPreStyleServerManager;
import com.fr.base.background.ColorBackground;
import com.fr.chart.base.*;
import com.fr.chart.chartattr.*;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.chart.ChartDesignEditPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.gui.type.ColumnPlotPane4ToolBar;
import com.fr.design.mainframe.chart.gui.type.PlotPane4ToolBar;
import com.fr.form.ui.ChartBook;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

// Referenced classes of package com.fr.design.mainframe:
//            ChartDesigner, PlotToolBarFactory, AbstractMapPlotPane4ToolBar

public class ChartToolBarPane extends JPanel
{

    public static final int TOTAL_HEIGHT = 42;
    private static final int COM_HEIGHT = 22;
    private static final int GAP = 7;
    private static final int COM_GAP = 14;
    private static final int COMBOX_WIDTH = 230;
    private static final String CHOOSEITEM[] = {
        Inter.getLocText("FR-Chart-Type_Column"), Inter.getLocText("FR-Chart-Type_Line"), Inter.getLocText("FR-Chart-Type_Bar"), Inter.getLocText("FR-Chart-Type_Pie"), Inter.getLocText("FR-Chart-Type_Area"), Inter.getLocText("FR-Chart-Type_XYScatter"), Inter.getLocText("FR-Chart-Chart_BubbleChart"), Inter.getLocText("FR-Chart-Type_Radar"), Inter.getLocText("FR-Chart-Type_Stock"), Inter.getLocText("FR-Chart-Type_Meter"), 
        Inter.getLocText("FR-Chart-Type_Range"), Inter.getLocText("FR-Chart-Type_Comb"), Inter.getLocText("FR-Chart-Type_Gantt"), Inter.getLocText("FR-Chart-Type_Donut"), Inter.getLocText("FR-Chart-Map_Map"), (new StringBuilder()).append("gis").append(Inter.getLocText("FR-Chart-Map_Map")).toString()
    };
    private UIComboBox chooseComboBox;
    private JPanel stylePane;
    private JPanel plotTypeComboBoxPane;
    private UIButton topDownShade;
    private UIButton transparent;
    private UIButton plane3D;
    private UIButton gradient;
    private ItemListener itemListener;
    private PlotPane4ToolBar subChartTypesPane;
    private AbstractMapPlotPane4ToolBar mapTypePane;
    private JPanel centerPane;
    private ChartDesigner chartDesigner;
    private int lastStyleIndex;
    private MouseAdapter styleListener;

    private void setStyle(Chart chart, MouseEvent mouseevent, Plot plot)
    {
        if(mouseevent.getSource() == topDownShade)
        {
            topDownShade.setSelected(true);
            chart.setPlot(plot);
            chart.getPlot().setPlotStyle(4);
            resetChart(chart);
            createCondition4Shade(chart);
            setPlotFillStyle(chart);
        } else
        if(mouseevent.getSource() == transparent)
        {
            transparent.setSelected(true);
            chart.setPlot(plot);
            chart.getPlot().setPlotStyle(5);
            resetChart(chart);
            createCondition4Transparent(chart);
            setPlotFillStyle(chart);
        } else
        if(mouseevent.getSource() == plane3D)
        {
            plane3D.setSelected(true);
            chart.setPlot(plot);
            chart.getPlot().setPlotStyle(1);
            resetChart(chart);
            createCondition4Plane3D(chart);
            setPlotFillStyle(chart);
        } else
        if(mouseevent.getSource() == gradient)
        {
            gradient.setSelected(true);
            chart.setPlot(plot);
            chart.getPlot().setPlotStyle(2);
            resetChart(chart);
            createCondition4HighLight(chart);
            setPlotFillStyle(chart);
        }
        chart.setStyleGlobal(true);
        ChartEditPane charteditpane = ChartDesignEditPane.getInstance();
        charteditpane.styleChange(true);
        ChartDesignEditPane.getInstance().populate((ChartCollection)((ChartBook)chartDesigner.getTarget()).getChartCollection());
        charteditpane.styleChange(false);
    }

    public ChartToolBarPane(ChartDesigner chartdesigner)
    {
        chooseComboBox = new UIComboBox(CHOOSEITEM) {

            final ChartToolBarPane this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(230, 22);
            }

            
            {
                this$0 = ChartToolBarPane.this;
                super(aobj);
            }
        }
;
        topDownShade = new UIButton(Inter.getLocText("FR-Chart-Style_TopDownShade"));
        transparent = new UIButton(Inter.getLocText("FR-Chart-Style_Transparent"));
        plane3D = new UIButton(Inter.getLocText("FR-Chart-Style_Plane3D"));
        gradient = new UIButton(Inter.getLocText("FR-Chart-Style_GradientHighlight"));
        itemListener = new ItemListener() {

            final ChartToolBarPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                if(itemevent.getStateChange() == 2)
                {
                    remove(centerPane);
                    remove(stylePane);
                    if(chooseComboBox.getSelectedIndex() < ChartTypeValueCollection.MAP.toInt())
                    {
                        calSubChartTypesPane(chooseComboBox.getSelectedIndex());
                        add(subChartTypesPane, "Center");
                        centerPane = subChartTypesPane;
                        add(stylePane, "East");
                    } else
                    {
                        calMapSubChartTypesPane(chooseComboBox.getSelectedIndex());
                        add(mapTypePane, "Center");
                        centerPane = mapTypePane;
                    }
                    ChartCollection chartcollection = (ChartCollection)((ChartBook)chartDesigner.getTarget()).getChartCollection();
                    Chart chart = chartcollection.getSelectedChart();
                    validate();
                    fireTypeChange();
                    if(chooseComboBox.getSelectedIndex() == ChartTypeValueCollection.MAP.toInt())
                        mapTypePane.populateMapPane(((MapPlot)chart.getPlot()).getMapName());
                    else
                    if(chooseComboBox.getSelectedIndex() == ChartTypeValueCollection.GIS.toInt())
                        mapTypePane.populateMapPane(chart.getChartName());
                }
            }

            
            {
                this$0 = ChartToolBarPane.this;
                super();
            }
        }
;
        lastStyleIndex = -1;
        styleListener = new MouseAdapter() {

            final ChartToolBarPane this$0;

            public void mousePressed(MouseEvent mouseevent)
            {
                ChartCollection chartcollection = (ChartCollection)((ChartBook)chartDesigner.getTarget()).getChartCollection();
                Chart chart = chartcollection.getSelectedChart();
                int i = chart.getPlot().getPlotType().toInt();
                if(i >= ChartTypeValueCollection.MAP.toInt())
                    return;
                Plot plot = subChartTypesPane.setSelectedClonedPlotWithCondition(chart.getPlot());
                chartDesigner.fireTargetModified();
                UIButton uibutton = (UIButton)mouseevent.getSource();
                if(uibutton.isSelected())
                {
                    uibutton.setSelected(false);
                    chart.setPlot(plot);
                    resetChart(chart);
                    lastStyleIndex = -1;
                    ChartDesignEditPane.getInstance().populateSelectedTabPane();
                    return;
                } else
                {
                    clearStyleChoose();
                    setStyle(chart, mouseevent, plot);
                    lastStyleIndex = chart.getPlot().getPlotStyle();
                    return;
                }
            }

            
            {
                this$0 = ChartToolBarPane.this;
                super();
            }
        }
;
        chartDesigner = chartdesigner;
        subChartTypesPane = new ColumnPlotPane4ToolBar(chartdesigner);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(7, 14, 7, 0));
        plotTypeComboBoxPane = new JPanel();
        plotTypeComboBoxPane.setBorder(new EmptyBorder(2, 0, 2, 0));
        plotTypeComboBoxPane.setLayout(new BorderLayout());
        plotTypeComboBoxPane.add(chooseComboBox, "Center");
        chooseComboBox.addItemListener(itemListener);
        chooseComboBox.setSelectedIndex(0);
        add(plotTypeComboBoxPane, "West");
        initStylePane();
        add(stylePane, "East");
        add(subChartTypesPane, "Center");
        centerPane = subChartTypesPane;
        topDownShade.addMouseListener(styleListener);
        transparent.addMouseListener(styleListener);
        plane3D.addMouseListener(styleListener);
        gradient.addMouseListener(styleListener);
    }

    private void initStylePane()
    {
        stylePane = new JPanel() {

            final ChartToolBarPane this$0;

            public Dimension getPreferredSize()
            {
                Dimension dimension = super.getPreferredSize();
                return new Dimension(dimension.width, 22);
            }

            
            {
                this$0 = ChartToolBarPane.this;
                super();
            }
        }
;
        stylePane.setLayout(new FlowLayout(0, 14, 0));
        stylePane.setBorder(new EmptyBorder(3, 0, 3, 0));
        stylePane.add(topDownShade);
        stylePane.add(transparent);
        stylePane.add(plane3D);
        stylePane.add(gradient);
    }

    public void clearStyleChoose()
    {
        topDownShade.setSelected(false);
        transparent.setSelected(false);
        plane3D.setSelected(false);
        gradient.setSelected(false);
    }

    private void calMapSubChartTypesPane(int i)
    {
        ChartTypeValueCollection charttypevaluecollection = ChartTypeValueCollection.parse(i);
        mapTypePane = PlotToolBarFactory.createToolBar4MapPlot(charttypevaluecollection, chartDesigner);
    }

    private void calSubChartTypesPane(int i)
    {
        ChartTypeValueCollection charttypevaluecollection = ChartTypeValueCollection.parse(i);
        subChartTypesPane = PlotToolBarFactory.createToolBar4NormalPlot(charttypevaluecollection, chartDesigner);
    }

    private void fireTypeChange()
    {
        if(chooseComboBox.getSelectedIndex() < ChartTypeValueCollection.MAP.toInt())
            subChartTypesPane.fireChange();
        else
            mapTypePane.fireChange();
    }

    private void resetChart(Chart chart)
    {
        chart.setTitle(new Title(chart.getTitle().getTextObject()));
        chart.setBorderStyle(0);
        chart.setBorderColor(new Color(150, 150, 150));
        chart.setBackground(null);
        setPlotFillStyle(chart);
    }

    private void createCondition4HighLight(Chart chart)
    {
        if(chart != null)
        {
            Title title = new Title(chart.getTitle().getTextObject());
            chart.setTitle(title);
            title.setTitleVisible(true);
            TextAttr textattr = title.getTextAttr();
            if(textattr == null)
            {
                textattr = new TextAttr();
                title.setTextAttr(textattr);
            }
            title.setPosition(2);
            textattr.setFRFont(FRFont.getInstance("Microsoft YaHei", 1, 16F, new Color(51, 51, 51)));
            Legend legend = new Legend();
            legend.setFRFont(FRFont.getInstance("SimSun", 0, 9F, new Color(138, 140, 139)));
            legend.setPosition(8);
            chart.getPlot().setLegend(legend);
            if(chart.getPlot() instanceof CategoryPlot)
            {
                CategoryPlot categoryplot = (CategoryPlot)chart.getPlot();
                Axis axis = categoryplot.getxAxis();
                axis.setAxisStyle(5);
                axis.setAxisColor(new Color(204, 220, 228));
                axis.setTickMarkType(1);
                axis.setSecTickMarkType(0);
                axis.setShowAxisLabel(true);
                axis.getTextAttr().setFRFont(FRFont.getInstance("Microsoft YaHei", 0, 10F, new Color(138, 140, 139)));
                Axis axis1 = categoryplot.getyAxis();
                axis1.setAxisStyle(2);
                axis1.setAxisColor(null);
                axis1.setTickMarkType(1);
                axis1.setSecTickMarkType(0);
                axis1.setShowAxisLabel(true);
                axis1.getTextAttr().setFRFont(FRFont.getInstance("SimSun", 0, 10F, new Color(138, 140, 139)));
                categoryplot.setBorderStyle(1);
                categoryplot.setBorderColor(new Color(204, 220, 228));
                categoryplot.setBackground(ColorBackground.getInstance(new Color(248, 247, 245)));
                categoryplot.getyAxis().setMainGridStyle(1);
                categoryplot.getyAxis().setMainGridColor(new Color(192, 192, 192));
            }
        }
    }

    private void createCondition4Plane3D(Chart chart)
    {
        if(chart != null)
        {
            Title title = new Title(chart.getTitle().getTextObject());
            chart.setTitle(title);
            title.setTitleVisible(true);
            TextAttr textattr = title.getTextAttr();
            if(textattr == null)
            {
                textattr = new TextAttr();
                title.setTextAttr(textattr);
            }
            title.setPosition(0);
            textattr.setFRFont(FRFont.getInstance("Microsoft YaHei", 0, 16F, new Color(51, 51, 51)));
            Legend legend = new Legend();
            legend.setFRFont(FRFont.getInstance("SimSun", 0, 9F, new Color(128, 128, 128)));
            legend.setPosition(1);
            chart.getPlot().setLegend(legend);
            if(chart.getPlot() instanceof CategoryPlot)
            {
                CategoryPlot categoryplot = (CategoryPlot)chart.getPlot();
                Axis axis = categoryplot.getxAxis();
                axis.setAxisStyle(5);
                axis.setAxisColor(new Color(57, 57, 57));
                axis.setTickMarkType(0);
                axis.setSecTickMarkType(0);
                axis.setShowAxisLabel(true);
                axis.getTextAttr().setFRFont(FRFont.getInstance("Microsoft YaHei", 0, 10F, new Color(57, 57, 57)));
                Axis axis1 = categoryplot.getyAxis();
                axis1.setAxisStyle(0);
                axis1.setTickMarkType(0);
                axis1.setSecTickMarkType(0);
                axis1.setShowAxisLabel(false);
                categoryplot.getyAxis().setMainGridStyle(1);
                categoryplot.getyAxis().setMainGridColor(new Color(192, 192, 192));
                chart.setBorderStyle(0);
                ConditionAttr conditionattr = categoryplot.getConditionCollection().getDefaultAttr();
                DataSeriesCondition dataseriescondition = conditionattr.getExisted(com/fr/chart/base/AttrContents);
                if(dataseriescondition != null)
                    conditionattr.remove(dataseriescondition);
                AttrContents attrcontents = new AttrContents();
                attrcontents.setPosition(6);
                attrcontents.setSeriesLabel("${VALUE}");
                attrcontents.setTextAttr(new TextAttr(FRFont.getInstance("SimSun", 0, 9F, new Color(51, 51, 51))));
                conditionattr.addDataSeriesCondition(attrcontents);
            }
        }
    }

    private void createCondition4Transparent(Chart chart)
    {
        if(chart != null)
        {
            Title title = new Title(chart.getTitle().getTextObject());
            chart.setTitle(title);
            title.setTitleVisible(true);
            TextAttr textattr = title.getTextAttr();
            if(textattr == null)
            {
                textattr = new TextAttr();
                title.setTextAttr(textattr);
            }
            title.setPosition(2);
            textattr.setFRFont(FRFont.getInstance("Microsoft YaHei", 1, 16F, new Color(192, 192, 192)));
            Legend legend = new Legend();
            legend.setFRFont(FRFont.getInstance("SimSun", 0, 9F, new Color(138, 140, 139)));
            legend.setPosition(8);
            chart.getPlot().setLegend(legend);
            Plot plot = chart.getPlot();
            chart.setBackground(ColorBackground.getInstance(new Color(51, 51, 51)));
            if(plot instanceof CategoryPlot)
            {
                plot.setBorderStyle(1);
                plot.setBorderColor(new Color(65, 65, 65));
                Axis axis = plot.getxAxis();
                axis.setAxisStyle(5);
                axis.setAxisColor(new Color(192, 192, 192));
                axis.setTickMarkType(0);
                axis.setSecTickMarkType(0);
                axis.setShowAxisLabel(true);
                axis.getTextAttr().setFRFont(FRFont.getInstance("Microsoft YaHei", 0, 10F, new Color(150, 150, 150)));
                Axis axis1 = plot.getyAxis();
                axis1.setShowAxisLabel(true);
                axis1.setAxisStyle(0);
                axis1.getTextAttr().setFRFont(FRFont.getInstance("SimSun", 0, 10F, new Color(150, 150, 150)));
                axis1.setMainGridStyle(1);
                axis1.setMainGridColor(new Color(63, 62, 62));
            }
        }
    }

    private void createCondition4Shade(Chart chart)
    {
        if(chart != null)
        {
            Title title = new Title(chart.getTitle().getTextObject());
            chart.setTitle(title);
            title.setTitleVisible(true);
            TextAttr textattr = title.getTextAttr();
            if(textattr == null)
            {
                textattr = new TextAttr();
                title.setTextAttr(textattr);
            }
            title.setPosition(0);
            textattr.setFRFont(FRFont.getInstance("Microsoft YaHei", 1, 16F, new Color(0, 51, 102)));
            Legend legend = new Legend();
            legend.setFRFont(FRFont.getInstance("SimSun", 0, 9F, new Color(128, 128, 128)));
            legend.setPosition(3);
            chart.getPlot().setLegend(legend);
            if(chart.getPlot() instanceof CategoryPlot)
            {
                CategoryPlot categoryplot = (CategoryPlot)chart.getPlot();
                Axis axis = categoryplot.getxAxis();
                axis.setAxisStyle(5);
                axis.setAxisColor(new Color(73, 100, 117));
                axis.setTickMarkType(0);
                axis.setSecTickMarkType(0);
                axis.setShowAxisLabel(true);
                axis.getTextAttr().setFRFont(FRFont.getInstance("Microsoft YaHei", 0, 10F, new Color(128, 128, 128)));
                Axis axis1 = categoryplot.getyAxis();
                axis1.setShowAxisLabel(true);
                axis1.getTextAttr().setFRFont(FRFont.getInstance("SimSun", 0, 10F, new Color(128, 128, 128)));
                axis1.setAxisStyle(0);
                categoryplot.getyAxis().setMainGridStyle(1);
                categoryplot.getyAxis().setMainGridColor(new Color(192, 192, 192));
                categoryplot.setHorizontalIntervalBackgroundColor(new Color(243, 243, 243));
            }
        }
    }

    private void setPlotFillStyle(Chart chart)
    {
        ChartPreStyleManagerProvider chartprestylemanagerprovider = ChartPreStyleServerManager.getProviderInstance();
        Plot plot = chart.getPlot();
        Object obj = null;
        String s = "";
        if(topDownShade.isSelected())
        {
            s = Inter.getLocText("FR-Chart-Style_Retro");
            obj = chartprestylemanagerprovider.getPreStyle(s);
        } else
        if(transparent.isSelected())
        {
            s = Inter.getLocText("FR-Chart-Style_Fresh");
            obj = chartprestylemanagerprovider.getPreStyle(s);
        } else
        if(plane3D.isSelected())
        {
            s = Inter.getLocText("FR-Chart-Style_Bright");
            obj = chartprestylemanagerprovider.getPreStyle(s);
        } else
        if(gradient.isSelected())
        {
            s = Inter.getLocText("FR-Chart-Style_Bright");
            obj = chartprestylemanagerprovider.getPreStyle(s);
        } else
        {
            obj = null;
        }
        if(obj == null)
        {
            plot.getPlotFillStyle().setColorStyle(0);
        } else
        {
            AttrFillStyle attrfillstyle = ((ChartPreStyle)obj).getAttrFillStyle();
            attrfillstyle.setFillStyleName(s);
            plot.setPlotFillStyle(attrfillstyle);
        }
    }

    public void populate()
    {
        ChartCollection chartcollection = (ChartCollection)((ChartBook)chartDesigner.getTarget()).getChartCollection();
        Chart chart = chartcollection.getSelectedChart();
        chooseComboBox.removeItemListener(itemListener);
        chooseComboBox.setSelectedIndex(chart.getPlot().getPlotType().toInt());
        int i = chart.getPlot().getPlotType().toInt();
        removeAll();
        populateStyle();
        add(plotTypeComboBoxPane, "West");
        initStylePane();
        if(i < ChartTypeValueCollection.MAP.toInt())
        {
            calSubChartTypesPane(i);
            subChartTypesPane.setSelectedIndex(chart.getPlot().getDetailType());
            add(subChartTypesPane, "Center");
            add(subChartTypesPane, "Center");
            centerPane = subChartTypesPane;
            add(stylePane, "East");
        } else
        if(i == ChartTypeValueCollection.MAP.toInt())
        {
            calMapSubChartTypesPane(i);
            mapTypePane.populateMapPane(((MapPlot)chart.getPlot()).getMapName());
            add(mapTypePane, "Center");
            centerPane = mapTypePane;
        } else
        {
            calMapSubChartTypesPane(i);
            mapTypePane.populateMapPane(chart.getPlot().getPlotName());
            add(mapTypePane, "Center");
            centerPane = mapTypePane;
        }
        validate();
        chooseComboBox.addItemListener(itemListener);
    }

    private void populateStyle()
    {
        clearStyleChoose();
        ChartCollection chartcollection = (ChartCollection)((ChartBook)chartDesigner.getTarget()).getChartCollection();
        Chart chart = chartcollection.getSelectedChart();
        int i = chart.getPlot().getPlotStyle();
        switch(i)
        {
        case 4: // '\004'
            topDownShade.setSelected(chart.isStyleGlobal());
            break;

        case 5: // '\005'
            transparent.setSelected(chart.isStyleGlobal());
            break;

        case 1: // '\001'
            plane3D.setSelected(chart.isStyleGlobal());
            break;

        case 2: // '\002'
            gradient.setSelected(chart.isStyleGlobal());
            break;

        case 3: // '\003'
        default:
            clearStyleChoose();
            break;
        }
        lastStyleIndex = i;
    }














}
