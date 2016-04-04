// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.GisMapPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.GisMapIndependentChart;
import com.fr.design.chart.series.PlotStyle.ChartSelectDemoPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            AbstractChartTypePane, ChartImagePane

public class GisMapPlotPane extends AbstractChartTypePane
{

    private static final long serialVersionUID = 0x2404149166d520f4L;
    private static final int GISMAP = 0;
    private UITextField keyInput;

    public GisMapPlotPane()
    {
        double d = -2D;
        double d1 = -1D;
        Component acomponent[][] = (Component[][])null;
        styleList = createStyleList();
        checkDemosBackground();
        JPanel jpanel = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(4);
        if(styleList != null && !styleList.isEmpty())
        {
            for(int i = 0; i < styleList.size(); i++)
            {
                ChartImagePane chartimagepane = (ChartImagePane)styleList.get(i);
                jpanel.add(chartimagepane);
                chartimagepane.setDemoGroup((ChartSelectDemoPane[])styleList.toArray(new ChartSelectDemoPane[styleList.size()]));
            }

        }
        keyInput = new UITextField();
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d, d, d
        };
        if(styleList != null && !styleList.isEmpty())
            acomponent = (new Component[][] {
                new Component[] {
                    jpanel
                }, new Component[] {
                    new UILabel(Inter.getLocText("FR-Chart-Waring_Please_Input_The_Key"))
                }, new Component[] {
                    keyInput
                }
            });
        JPanel jpanel1 = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        setLayout(new BorderLayout());
        add(jpanel1, "Center");
    }

    protected String[] getTypeIconPath()
    {
        return (new String[] {
            "/com/fr/design/images/chart/GisMapPlot/type/0.png"
        });
    }

    protected String[] getTypeTipName()
    {
        return (new String[] {
            (new StringBuilder()).append("gis").append(Inter.getLocText("FR-Chart-Map_Map")).toString()
        });
    }

    protected String getPlotTypeID()
    {
        return "FineReportGisChart";
    }

    protected String[] getTypeLayoutPath()
    {
        return (new String[] {
            "/com/fr/design/images/chart/GisMapPlot/layout/0.png", "/com/fr/design/images/chart/GisMapPlot/layout/1.png"
        });
    }

    protected String[] getTypeLayoutTipName()
    {
        return (new String[] {
            Inter.getLocText("FR-Chart-Type_BaiduMap"), Inter.getLocText("FR-Chart-Map_GoogleMap")
        });
    }

    public void updateBean(Chart chart)
    {
        if(needsResetChart(chart))
            resetChart(chart);
        Chart achart[] = GisMapIndependentChart.gisChartTypes;
        GisMapPlot gismapplot;
        if(achart.length > 0)
            try
            {
                gismapplot = (GisMapPlot)achart[0].getPlot().clone();
            }
            catch(Exception exception)
            {
                gismapplot = new GisMapPlot();
            }
        else
            gismapplot = new GisMapPlot();
        try
        {
            chart.switchPlot((Plot)gismapplot.clone());
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            FRLogger.getLogger().error("Error In LineChart");
            chart.switchPlot(new GisMapPlot());
        }
        gismapplot = (GisMapPlot)chart.getPlot();
        boolean flag = gismapplot.isGisType();
        if(((ChartImagePane)styleList.get(0)).isPressing)
            gismapplot.setGisType(true);
        else
            gismapplot.setGisType(false);
        if(flag != gismapplot.isGisType())
        {
            if(gismapplot.isGisType())
                keyInput.setText(gismapplot.getBaiduKey());
            else
                keyInput.setText(gismapplot.getGoogleKey());
        } else
        {
            String s = keyInput.getText().trim();
            if(gismapplot.isGisType() && s != gismapplot.getBaiduKey())
                gismapplot.setBaiduKey(s);
            else
            if(!gismapplot.isGisType() && s != gismapplot.getGoogleKey())
                gismapplot.setGoogleKey(s);
        }
    }

    public void populateBean(Chart chart)
    {
        ((ChartImagePane)typeDemo.get(0)).isPressing = true;
        GisMapPlot gismapplot = (GisMapPlot)chart.getPlot();
        if(gismapplot.isGisType())
        {
            ((ChartImagePane)styleList.get(0)).isPressing = true;
            ((ChartImagePane)styleList.get(1)).isPressing = false;
            keyInput.setText(gismapplot.getBaiduKey());
        } else
        {
            ((ChartImagePane)styleList.get(1)).isPressing = true;
            ((ChartImagePane)styleList.get(0)).isPressing = false;
            keyInput.setText(gismapplot.getGoogleKey());
        }
        ((ChartImagePane)styleList.get(1)).checkBackground();
        ((ChartImagePane)styleList.get(0)).checkBackground();
    }

    public String title4PopupWindow()
    {
        return (new StringBuilder()).append("gis").append(Inter.getLocText("FR-Chart-Map_Map")).toString();
    }

    public boolean isHaveAxis()
    {
        return false;
    }

    public Chart getDefaultChart()
    {
        return GisMapIndependentChart.gisChartTypes[0];
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((Chart)obj);
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Chart)obj);
    }
}
