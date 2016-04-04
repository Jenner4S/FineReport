// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.base.ChartPreStyleManagerProvider;
import com.fr.base.ChartPreStyleServerManager;
import com.fr.base.FRContext;
import com.fr.base.background.ColorBackground;
import com.fr.chart.base.AttrContents;
import com.fr.chart.base.AttrFillStyle;
import com.fr.chart.base.ChartPreStyle;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.CategoryPlot;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Legend;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.Title;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.charttypes.BarIndependentChart;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.chart.series.PlotStyle.ChartSelectDemoPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.js.NameJavaScriptGroup;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.JSeparator;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            ChartImagePane

public abstract class AbstractChartTypePane extends FurtherBasicBeanPane
{

    private static final int ONE_LINE_NUM = 4;
    protected static final int STYLE_SHADE = 0;
    protected static final int STYLE_TRANSPARENT = 1;
    protected static final int STYLE_PLANE3D = 2;
    protected static final int STYLE_HIGHLIGHT = 3;
    protected static final int BAIDU = 0;
    protected static final int GOOGLE = 1;
    protected java.util.List typeDemo;
    protected java.util.List styleList;
    protected JPanel stylePane;
    protected int lastStyleIndex;
    protected int lastTypeIndex;
    protected boolean typeChanged;

    protected abstract String[] getTypeIconPath();

    protected abstract String[] getTypeTipName();

    protected abstract String[] getTypeLayoutPath();

    protected abstract String[] getTypeLayoutTipName();

    protected String[] getNormalLayoutTipName()
    {
        return (new String[] {
            Inter.getLocText("FR-Chart-Style_TopDownShade"), Inter.getLocText("FR-Chart-Style_Transparent"), Inter.getLocText("FR-Chart-Style_Plane3D"), Inter.getLocText("FR-Chart-Style_GradientHighlight")
        });
    }

    public AbstractChartTypePane()
    {
        lastStyleIndex = -1;
        lastTypeIndex = -1;
        typeChanged = false;
        double d = 4D;
        double d1 = -2D;
        double d2 = -1D;
        typeDemo = createTypeDemoList();
        styleList = createStyleList();
        checkDemosBackground();
        JPanel jpanel = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(4);
        for(int i = 0; i < typeDemo.size(); i++)
        {
            ChartImagePane chartimagepane = (ChartImagePane)typeDemo.get(i);
            jpanel.add(chartimagepane);
            chartimagepane.setDemoGroup((ChartSelectDemoPane[])typeDemo.toArray(new ChartSelectDemoPane[typeDemo.size()]));
        }

        JPanel jpanel1 = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(4);
        if(styleList != null && !styleList.isEmpty())
        {
            for(int j = 0; j < styleList.size(); j++)
            {
                ChartImagePane chartimagepane1 = (ChartImagePane)styleList.get(j);
                jpanel1.add(chartimagepane1);
                chartimagepane1.setDemoGroup((ChartSelectDemoPane[])styleList.toArray(new ChartSelectDemoPane[styleList.size()]));
            }

        }
        double ad[] = {
            d1, d, d2
        };
        double ad1[] = {
            d1, d1, d1, d1, d1, d1, d1
        };
        if(styleList != null && !styleList.isEmpty())
        {
            Component acomponent[][] = {
                {
                    new JSeparator()
                }, {
                    new BoldFontTextLabel(Inter.getLocText("FR-Chart_Layout"))
                }, {
                    jpanel1
                }
            };
            stylePane = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
            stylePane.setVisible(false);
        }
        JPanel jpanel2 = TableLayoutHelper.createTableLayoutPane(getPaneComponents(jpanel), ad1, ad);
        setLayout(new BorderLayout());
        add(jpanel2, "Center");
    }

    protected java.util.List createTypeDemoList()
    {
        return createImagePaneList(getTypeIconPath(), getTypeTipName());
    }

    protected java.util.List createStyleList()
    {
        return createImagePaneList(getTypeLayoutPath(), getTypeLayoutTipName());
    }

    private java.util.List createImagePaneList(String as[], String as1[])
    {
        ArrayList arraylist = new ArrayList();
        int i = as.length;
        int j = as1.length;
        int k = 0;
        for(int l = Math.min(i, j); k < l; k++)
        {
            boolean flag = k == l - 1 || (k + 1) % 4 == 0;
            ChartImagePane chartimagepane = new ChartImagePane(as[k], as1[k], flag);
            chartimagepane.isPressing = k == 0;
            arraylist.add(chartimagepane);
        }

        return arraylist;
    }

    protected Component[][] getPaneComponents(JPanel jpanel)
    {
        return (new Component[][] {
            new Component[] {
                jpanel
            }, new Component[] {
                stylePane
            }
        });
    }

    protected Plot getSelectedClonedPlot()
    {
        return null;
    }

    protected void checkTypeChange()
    {
        if(styleList != null && !styleList.isEmpty())
        {
            int i = 0;
            do
            {
                if(i >= typeDemo.size())
                    break;
                if(((ChartImagePane)typeDemo.get(i)).isPressing && i != lastTypeIndex)
                {
                    typeChanged = true;
                    lastTypeIndex = i;
                    break;
                }
                typeChanged = false;
                i++;
            } while(true);
        }
    }

    public void populateBean(Chart chart)
    {
        for(Iterator iterator = typeDemo.iterator(); iterator.hasNext();)
        {
            ChartImagePane chartimagepane = (ChartImagePane)iterator.next();
            chartimagepane.isPressing = false;
        }

        for(Iterator iterator1 = styleList.iterator(); iterator1.hasNext();)
        {
            ChartImagePane chartimagepane1 = (ChartImagePane)iterator1.next();
            chartimagepane1.isPressing = false;
        }

        if(styleList != null && !styleList.isEmpty())
        {
            int i = chart.getPlot().getPlotStyle();
            String s = chart.getPlot().getPlotFillStyle().getFillStyleName();
            switch(i)
            {
            case 4: // '\004'
                if(ComparatorUtils.equals(Inter.getLocText("FR-Chart-Style_Retro"), s))
                {
                    ((ChartImagePane)styleList.get(0)).isPressing = true;
                    lastStyleIndex = 0;
                }
                break;

            case 5: // '\005'
                if(ComparatorUtils.equals(Inter.getLocText("FR-Chart-Style_Fresh"), s))
                {
                    ((ChartImagePane)styleList.get(1)).isPressing = true;
                    lastStyleIndex = 1;
                }
                break;

            case 1: // '\001'
                if(ComparatorUtils.equals(Inter.getLocText("FR-Chart-Style_Bright"), s))
                {
                    ((ChartImagePane)styleList.get(2)).isPressing = true;
                    lastStyleIndex = 2;
                }
                break;

            case 2: // '\002'
                if(ComparatorUtils.equals(Inter.getLocText("FR-Chart-Style_Bright"), s))
                {
                    ((ChartImagePane)styleList.get(3)).isPressing = true;
                    lastStyleIndex = 3;
                }
                break;

            case 3: // '\003'
            default:
                lastStyleIndex = -1;
                break;
            }
            stylePane.setVisible(!chart.getPlot().isSupport3D());
            repaint();
        }
    }

    protected void checkDemosBackground()
    {
        if(styleList != null && !styleList.isEmpty())
        {
            for(int i = 0; i < styleList.size(); i++)
            {
                ((ChartImagePane)styleList.get(i)).checkBackground();
                ((ChartImagePane)styleList.get(i)).repaint();
            }

        }
        for(int j = 0; j < typeDemo.size(); j++)
        {
            ((ChartImagePane)typeDemo.get(j)).checkBackground();
            ((ChartImagePane)typeDemo.get(j)).repaint();
        }

    }

    private void setPlotFillStyle(Chart chart)
    {
        ChartPreStyleManagerProvider chartprestylemanagerprovider = ChartPreStyleServerManager.getProviderInstance();
        Plot plot = chart.getPlot();
        Object obj = null;
        String s = "";
        if(((ChartImagePane)styleList.get(0)).isPressing)
        {
            s = Inter.getLocText("FR-Chart-Style_Retro");
            obj = chartprestylemanagerprovider.getPreStyle(s);
        } else
        if(((ChartImagePane)styleList.get(1)).isPressing)
        {
            s = Inter.getLocText("FR-Chart-Style_Fresh");
            obj = chartprestylemanagerprovider.getPreStyle(s);
        } else
        if(((ChartImagePane)styleList.get(2)).isPressing)
        {
            s = Inter.getLocText("FR-Chart-Style_Bright");
            obj = chartprestylemanagerprovider.getPreStyle(s);
        } else
        if(((ChartImagePane)styleList.get(3)).isPressing)
        {
            s = Inter.getLocText("FR-Chart-Style_Bright");
            obj = chartprestylemanagerprovider.getPreStyle(s);
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

    public void updateBean(Chart chart)
    {
        checkTypeChange();
        Plot plot = setSelectedClonedPlotWithCondition(chart.getPlot());
        if(styleList != null && !styleList.isEmpty())
        {
            if(((ChartImagePane)styleList.get(0)).isPressing && lastStyleIndex != 0)
            {
                lastStyleIndex = 0;
                chart.setPlot(plot);
                chart.getPlot().setPlotStyle(4);
                resetChart(chart);
                createCondition4Shade(chart);
                setPlotFillStyle(chart);
            } else
            if(((ChartImagePane)styleList.get(1)).isPressing && lastStyleIndex != 1)
            {
                lastStyleIndex = 1;
                chart.setPlot(plot);
                chart.getPlot().setPlotStyle(5);
                resetChart(chart);
                createCondition4Transparent(chart);
                setPlotFillStyle(chart);
            } else
            if(((ChartImagePane)styleList.get(2)).isPressing && lastStyleIndex != 2)
            {
                lastStyleIndex = 2;
                chart.setPlot(plot);
                chart.getPlot().setPlotStyle(1);
                resetChart(chart);
                createCondition4Plane3D(chart);
                setPlotFillStyle(chart);
            } else
            if(((ChartImagePane)styleList.get(3)).isPressing && lastStyleIndex != 3)
            {
                lastStyleIndex = 3;
                chart.setPlot(plot);
                chart.getPlot().setPlotStyle(2);
                resetChart(chart);
                createCondition4HighLight(chart);
                setPlotFillStyle(chart);
            } else
            if(lastStyleIndex >= 0 && lastStyleIndex <= 3 && (((ChartImagePane)styleList.get(lastStyleIndex)).isDoubleClicked || typeChanged))
            {
                chart.setPlot(plot);
                resetChart(chart);
                ((ChartImagePane)styleList.get(lastStyleIndex)).isPressing = false;
                checkDemosBackground();
                lastStyleIndex = -1;
            }
            stylePane.setVisible(!chart.getPlot().isSupport3D());
            repaint();
        }
    }

    private Plot setSelectedClonedPlotWithCondition(Plot plot)
    {
        Plot plot1 = getSelectedClonedPlot();
        if(plot != null && ComparatorUtils.equals(plot1.getClass(), plot.getClass()))
        {
            if(plot.getHotHyperLink() != null)
            {
                NameJavaScriptGroup namejavascriptgroup = plot.getHotHyperLink();
                try
                {
                    plot1.setHotHyperLink((NameJavaScriptGroup)namejavascriptgroup.clone());
                }
                catch(CloneNotSupportedException clonenotsupportedexception)
                {
                    FRContext.getLogger().error("Error in Hyperlink, Please Check it.", clonenotsupportedexception);
                }
            }
            plot1.setConditionCollection(plot.getConditionCollection());
            plot1.setSeriesDragEnable(plot.isSeriesDragEnable());
            if(plot1.isSupportZoomCategoryAxis() && plot1.getxAxis() != null)
                plot1.getxAxis().setZoom(plot.getxAxis().isZoom());
            if(plot1.isSupportTooltipInInteractivePane())
                plot1.setHotTooltipStyle(plot.getHotTooltipStyle());
            if(plot1.isSupportAutoRefresh())
                plot1.setAutoRefreshPerSecond(plot.getAutoRefreshPerSecond());
            if(plot1.isSupportAxisTip())
                plot1.setInteractiveAxisTooltip(plot.isInteractiveAxisTooltip());
        }
        return plot1;
    }

    public Chart updateBean()
    {
        return null;
    }

    protected void resetChart(Chart chart)
    {
        chart.setTitle(new Title(chart.getTitle().getTextObject()));
        chart.setBorderStyle(0);
        chart.setBorderColor(new Color(150, 150, 150));
        chart.setBackground(null);
    }

    protected void changePlotWithClone(Chart chart, Plot plot)
    {
        try
        {
            chart.switchPlot((Plot)plot.clone());
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            FRLogger.getLogger().error("Error in change plot");
        }
    }

    public void reset()
    {
    }

    protected abstract String getPlotTypeID();

    public boolean accept(Object obj)
    {
        if(obj instanceof Chart)
        {
            Chart chart = (Chart)obj;
            Plot plot = chart.getPlot();
            if(plot != null && ComparatorUtils.equals(plot.getPlotID(), getPlotTypeID()))
                return true;
        }
        return false;
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

    protected boolean needsResetChart(Chart chart)
    {
        return chart != null && chart.getPlot() != null && chart.getPlot().getPlotStyle() != 0;
    }

    public Chart getDefaultChart()
    {
        return BarIndependentChart.barChartTypes[0];
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((Chart)obj);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Chart)obj);
    }
}
