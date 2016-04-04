// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.analysisline;

import com.fr.chart.base.AttrColor;
import com.fr.chart.base.AttrTrendLine;
import com.fr.chart.base.ConditionTrendLine;
import com.fr.chart.base.LineStyleInfo;
import com.fr.chart.base.TrendLine;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartAlertValue;
import com.fr.chart.chartattr.NumberAxis;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.ValueAxis;
import com.fr.chart.chartattr.XYPlot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.design.chart.axis.ChartAlertValueInTopBottomPane;
import com.fr.design.chart.axis.ChartAlertValuePane;
import com.fr.design.chart.series.SeriesCondition.ConditionTrendLinePane;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.frpane.UICorrelationComboBoxPane;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.Inter;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class ChartAnalysisLinePane extends BasicScrollPane
{

    private static final long serialVersionUID = 0x5056fd1621f1c268L;
    private Chart chart;
    private ChartStylePane parent;
    private UICorrelationComboBoxPane trendLinePane;
    private UICorrelationComboBoxPane xAlertPane;
    private UICorrelationComboBoxPane yAlertPane;
    private JPanel contentPane;

    public ChartAnalysisLinePane(ChartStylePane chartstylepane)
    {
        parent = chartstylepane;
    }

    protected JPanel createContentPane()
    {
        if(chart == null)
            return new JPanel();
        JPanel jpanel = null;
        JPanel jpanel1 = null;
        JPanel jpanel2 = null;
        Plot plot = chart.getPlot();
        if(plot.isSupportTrendLine())
        {
            createTrendLinePane();
            double d = -2D;
            double d2 = -1D;
            double ad[] = {
                d
            };
            double ad1[] = {
                d2
            };
            jpanel = TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
                "Chart_TrendLine"
            }, new Component[][] {
                new Component[] {
                    trendLinePane
                }
            }, ad, ad1);
        }
        if(plot.getAlertLinePaneTitle().length == 2)
        {
            String as[] = plot.getAlertLinePaneTitle();
            jpanel1 = createAlertLinePane(new String[] {
                as[0], "ChartF-Alert-Line"
            }, true);
            jpanel2 = createAlertLinePane(new String[] {
                as[1], "ChartF-Alert-Line"
            }, false);
        } else
        if(plot.getyAxis() instanceof ValueAxis)
            jpanel1 = createAlertLinePane(new String[] {
                "ChartF-Alert-Line"
            }, true);
        Component acomponent[][] = (Component[][])null;
        if(jpanel != null)
        {
            if(jpanel1 != null)
                acomponent = (new Component[][] {
                    new Component[] {
                        jpanel
                    }, new Component[] {
                        new JSeparator()
                    }, new Component[] {
                        jpanel1
                    }, new Component[] {
                        jpanel2
                    }
                });
            else
                acomponent = (new Component[][] {
                    new Component[] {
                        jpanel1
                    }, new Component[] {
                        jpanel2
                    }
                });
        } else
        {
            acomponent = (new Component[][] {
                new Component[] {
                    jpanel1
                }, new Component[] {
                    jpanel2
                }
            });
        }
        double d1 = -2D;
        double d3 = -1D;
        double ad2[] = {
            d1, d1, d1, d1
        };
        double ad3[] = {
            d3
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad2, ad3);
    }

    private JPanel createAlertLinePane(String as[], boolean flag)
    {
        ArrayList arraylist = new ArrayList();
        arraylist.add(new UIMenuNameableCreator(Inter.getLocText("ChartF-Alert-Line"), new ChartAlertValue(), com/fr/design/chart/axis/ChartAlertValuePane));
        if(flag)
            xAlertPane = new UICorrelationComboBoxPane(arraylist);
        else
            yAlertPane = new UICorrelationComboBoxPane(arraylist);
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d
        };
        double ad1[] = {
            d1
        };
        return TableLayoutHelper.createTableLayoutPane4Chart(as, new Component[][] {
            new Component[] {
                flag ? xAlertPane : yAlertPane
            }
        }, ad, ad1);
    }

    public void populateBean(Chart chart1)
    {
        chart = chart1;
        Plot plot = chart.getPlot();
        if(plot == null)
            return;
        if(contentPane == null)
        {
            remove(leftcontentPane);
            layoutContentPane();
            parent.initAllListeners();
        }
        if(trendLinePane != null && plot.isSupportTrendLine())
        {
            ConditionCollection conditioncollection = plot.getConditionCollection();
            AttrTrendLine attrtrendline = (AttrTrendLine)conditioncollection.getDefaultAttr().getExisted(com/fr/chart/base/AttrTrendLine);
            popTrendLine(attrtrendline);
        }
        if(plot.getAlertLinePaneTitle().length != 0)
        {
            if(plot instanceof XYPlot)
            {
                popuAlert((NumberAxis)plot.getxAxis(), xAlertPane);
                popuAlert((NumberAxis)plot.getyAxis(), yAlertPane);
            } else
            {
                popuAlert((NumberAxis)plot.getyAxis(), xAlertPane);
                popuAlert((NumberAxis)plot.getSecondAxis(), yAlertPane);
            }
        } else
        if(xAlertPane != null)
            popuAlert((NumberAxis)plot.getyAxis(), xAlertPane);
    }

    private void popuAlert(NumberAxis numberaxis, UICorrelationComboBoxPane uicorrelationcomboboxpane)
    {
        boolean flag = numberaxis.getPosition() == 2 || numberaxis.getPosition() == 4;
        ArrayList arraylist = new ArrayList();
        if(flag)
            arraylist.add(new UIMenuNameableCreator(Inter.getLocText("ChartF-Alert-Line"), new ChartAlertValue(), com/fr/design/chart/axis/ChartAlertValuePane));
        else
            arraylist.add(new UIMenuNameableCreator(Inter.getLocText("ChartF-Alert-Line"), new ChartAlertValue(), com/fr/design/chart/axis/ChartAlertValueInTopBottomPane));
        uicorrelationcomboboxpane.refreshMenuAndAddMenuAction(arraylist);
        ArrayList arraylist1 = new ArrayList();
        ChartAlertValue achartalertvalue[] = numberaxis.getAlertValues();
        for(int i = 0; i < achartalertvalue.length; i++)
        {
            ChartAlertValue chartalertvalue = achartalertvalue[i];
            if(flag)
                arraylist1.add(new UIMenuNameableCreator(chartalertvalue.getAlertPaneSelectName(), chartalertvalue, com/fr/design/chart/axis/ChartAlertValuePane));
            else
                arraylist1.add(new UIMenuNameableCreator(chartalertvalue.getAlertPaneSelectName(), chartalertvalue, com/fr/design/chart/axis/ChartAlertValueInTopBottomPane));
        }

        uicorrelationcomboboxpane.populateBean(arraylist1);
        uicorrelationcomboboxpane.doLayout();
    }

    public void updateBean(Chart chart1)
    {
        if(chart1 == null || chart1.getPlot() == null)
            return;
        Plot plot = chart1.getPlot();
        if(plot.isSupportTrendLine() && trendLinePane != null)
        {
            ConditionCollection conditioncollection = plot.getConditionCollection();
            AttrTrendLine attrtrendline = (AttrTrendLine)conditioncollection.getDefaultAttr().getExisted(com/fr/chart/base/AttrTrendLine);
            if(attrtrendline == null)
            {
                attrtrendline = new AttrTrendLine();
                conditioncollection.getDefaultAttr().addDataSeriesCondition(attrtrendline);
            }
            upTrendLine(attrtrendline);
        }
        if(plot.getAlertLinePaneTitle().length != 0)
        {
            if(plot instanceof XYPlot)
            {
                updateAlert((NumberAxis)plot.getxAxis(), xAlertPane);
                updateAlert((NumberAxis)plot.getyAxis(), yAlertPane);
            } else
            {
                updateAlert((NumberAxis)plot.getyAxis(), xAlertPane);
                updateAlert((NumberAxis)plot.getSecondAxis(), yAlertPane);
            }
        } else
        if(xAlertPane != null)
            updateAlert((NumberAxis)plot.getyAxis(), xAlertPane);
    }

    private void updateAlert(NumberAxis numberaxis, UICorrelationComboBoxPane uicorrelationcomboboxpane)
    {
        if(uicorrelationcomboboxpane != null)
        {
            ArrayList arraylist = new ArrayList();
            java.util.List list = uicorrelationcomboboxpane.updateBean();
            for(int i = 0; i < list.size(); i++)
            {
                UIMenuNameableCreator uimenunameablecreator = (UIMenuNameableCreator)list.get(i);
                ChartAlertValue chartalertvalue = (ChartAlertValue)uimenunameablecreator.getObj();
                chartalertvalue.setAlertPaneSelectName(uimenunameablecreator.getName());
                arraylist.add(chartalertvalue);
            }

            numberaxis.setAlertValues((ChartAlertValue[])arraylist.toArray(new ChartAlertValue[arraylist.size()]));
        }
    }

    private void upTrendLine(AttrTrendLine attrtrendline)
    {
        attrtrendline.clear();
        java.util.List list = trendLinePane.updateBean();
        for(int i = 0; i < list.size(); i++)
        {
            UIMenuNameableCreator uimenunameablecreator = (UIMenuNameableCreator)list.get(i);
            ConditionTrendLine conditiontrendline = (ConditionTrendLine)uimenunameablecreator.getObj();
            conditiontrendline.setPaneName(uimenunameablecreator.getName());
            attrtrendline.add(conditiontrendline);
        }

    }

    private void popTrendLine(AttrTrendLine attrtrendline)
    {
        if(attrtrendline != null && attrtrendline.size() > 0)
        {
            ArrayList arraylist = new ArrayList();
            for(int i = 0; i < attrtrendline.size(); i++)
            {
                ConditionTrendLine conditiontrendline = attrtrendline.get(i);
                arraylist.add(new UIMenuNameableCreator(conditiontrendline.getPaneName(), conditiontrendline, com/fr/design/chart/series/SeriesCondition/ConditionTrendLinePane));
            }

            if(!arraylist.isEmpty())
                trendLinePane.populateBean(arraylist);
        }
    }

    protected void createTrendLinePane()
    {
        ArrayList arraylist = new ArrayList();
        ConditionTrendLine conditiontrendline = new ConditionTrendLine();
        TrendLine trendline = new TrendLine();
        LineStyleInfo linestyleinfo = new LineStyleInfo();
        linestyleinfo.setAttrLineColor(new AttrColor(Color.gray));
        trendline.setLineStyleInfo(linestyleinfo);
        conditiontrendline.setLine(trendline);
        arraylist.add(new UIMenuNameableCreator(Inter.getLocText("Chart_TrendLine"), conditiontrendline, com/fr/design/chart/series/SeriesCondition/ConditionTrendLinePane));
        trendLinePane = new UICorrelationComboBoxPane(arraylist);
    }

    protected String title4PopupWindow()
    {
        return PaneTitleConstants.CHART_STYLE_LINE_TITLE;
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
