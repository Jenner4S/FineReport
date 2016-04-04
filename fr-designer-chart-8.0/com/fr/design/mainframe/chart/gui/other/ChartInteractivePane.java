// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.other;

import com.fr.base.CoreDecimalFormat;
import com.fr.base.Style;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.base.AttrContents;
import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.BubblePlot;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.GanttPlot;
import com.fr.chart.chartattr.GisMapPlot;
import com.fr.chart.chartattr.MapPlot;
import com.fr.chart.chartattr.MeterPlot;
import com.fr.chart.chartattr.PiePlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.StockPlot;
import com.fr.chart.chartattr.XYScatterPlot;
import com.fr.chart.chartdata.GisMapReportDefinition;
import com.fr.chart.chartdata.GisMapTableDefinition;
import com.fr.chart.web.ChartHyperPoplink;
import com.fr.chart.web.ChartHyperRelateCellLink;
import com.fr.chart.web.ChartHyperRelateFloatLink;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.javascript.ChartEmailPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperPoplinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperRelateCellLinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperRelateFloatLinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.FormHyperlinkPane;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.UIBubbleFloatPane;
import com.fr.design.gui.frpane.UICorrelationComboBoxPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.style.FormatPane;
import com.fr.design.hyperlink.ReportletHyperlinkPane;
import com.fr.design.hyperlink.WebHyperlinkPane;
import com.fr.design.javascript.JavaScriptImplPane;
import com.fr.design.javascript.ParameterJavaScriptPane;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartOtherPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.js.EmailJavaScript;
import com.fr.js.FormHyperlinkProvider;
import com.fr.js.JavaScript;
import com.fr.js.JavaScriptImpl;
import com.fr.js.NameJavaScript;
import com.fr.js.NameJavaScriptGroup;
import com.fr.js.ParameterJavaScript;
import com.fr.js.ReportletHyperlink;
import com.fr.js.WebHyperlink;
import com.fr.stable.bridge.StableFactory;
import com.fr.third.org.hsqldb.lib.HashMap;
import com.fr.third.org.hsqldb.lib.Iterator;
import com.fr.third.org.hsqldb.lib.Set;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JSeparator;

// Referenced classes of package com.fr.design.mainframe.chart.gui.other:
//            TimeSwitchPane

public class ChartInteractivePane extends BasicScrollPane
    implements UIObserver
{

    private static final long serialVersionUID = 0x30423dcb97db5f18L;
    private static HashMap normalMap = new HashMap();
    private static HashMap gisMap = new HashMap();
    private static HashMap mapMap = new HashMap();
    private static HashMap pieMap = new HashMap();
    private static HashMap xyMap = new HashMap();
    private static HashMap bubbleMap = new HashMap();
    private static HashMap stockMap = new HashMap();
    private static HashMap ganttMap = new HashMap();
    private static HashMap meterMap = new HashMap();
    private static final int TIME_SWITCH_GAP = 40;
    private UICheckBox isChartAnimation;
    private UICheckBox isSeriesDragEnable;
    private UICheckBox isAxisZoom;
    private UICheckBox isDatapointValue;
    private UIButton dataPointValueFormat;
    private UICheckBox isDatapointPercent;
    private UIButton dataPointPercentFormat;
    private UILabel tooltipStyleLabel;
    private UIComboBox tooltipStyle;
    private UILabel tooltipShowTypeLabel;
    private UIComboBox tooltipShowType;
    private UICheckBox isAddressTittle;
    private UICheckBox isAddress;
    private UICheckBox isAddressName;
    private UICheckBox isAxisShowToolTip;
    protected UICheckBox isAutoRefresh;
    protected UISpinner autoRefreshTime;
    private UICorrelationComboBoxPane superLink;
    private FormatPane valueFormatPane;
    private FormatPane percentFormatPane;
    private Format valueFormat;
    private Format percentFormat;
    private JPanel tooltipPane;
    private JPanel axisShowPane;
    private JPanel autoRefreshPane;
    private JPanel superlinkPane;
    private ChartOtherPane parent;
    private UICheckBox timeSwitch;
    private JPanel timeSwitchContainer;
    private TimeSwitchPane timeSwitchPane;
    private static final int SIZEX = 258;
    private static final int SIZEY = 209;
    private static final int DET = 20;

    public ChartInteractivePane(ChartOtherPane chartotherpane)
    {
        parent = chartotherpane;
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("Chart-Interactive_Tab");
    }

    protected JPanel createContentPane()
    {
        isChartAnimation = new UICheckBox(Inter.getLocText("Chart-Animation_JSShow"));
        isSeriesDragEnable = new UICheckBox(Inter.getLocText("Chart-Series_Drag"));
        isDatapointValue = new UICheckBox(Inter.getLocText("Chart-Use_Value"));
        dataPointValueFormat = new UIButton(Inter.getLocText("Chart-Use_Format"));
        isDatapointPercent = new UICheckBox(Inter.getLocText("Chart-Value_Percent"));
        dataPointPercentFormat = new UIButton(Inter.getLocText("Chart-Use_Format"));
        tooltipStyle = new UIComboBox(new String[] {
            Inter.getLocText("Chart-White_Black"), Inter.getLocText("Chart-Black_White")
        });
        tooltipStyleLabel = new UILabel(Inter.getLocText("Chart-Style_Name"));
        tooltipShowType = new UIComboBox(new String[] {
            Inter.getLocText("Chart-Series_SingleData"), Inter.getLocText("Chart-Series_AllData")
        });
        tooltipShowTypeLabel = new UILabel(Inter.getLocText("Chart-Use_Show"));
        isAddressTittle = new UICheckBox(Inter.getLocText("Chart-Area_Title"));
        isAddress = new UICheckBox(Inter.getLocText("Chart-Gis_Address"));
        isAddressName = new UICheckBox(Inter.getLocText("Chart-Address_Name"));
        isAxisShowToolTip = new UICheckBox(Inter.getLocText("Chart-Interactive_AxisTooltip"));
        isAxisZoom = new UICheckBox(Inter.getLocText("Chart-Use_Zoom"));
        isAutoRefresh = new UICheckBox(Inter.getLocText(new String[] {
            "Chart-Use_Auto", "Chart-Use_Refresh"
        }));
        autoRefreshTime = new UISpinner(1.0D, 2147483647D, 1.0D);
        superLink = new UICorrelationComboBoxPane();
        isAutoRefresh.addActionListener(new ActionListener() {

            final ChartInteractivePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                checkAutoRefresh();
            }

            
            {
                this$0 = ChartInteractivePane.this;
                super();
            }
        }
);
        timeSwitch = new UICheckBox(Inter.getLocText("FR-Chart-Interactive_timeSwitch"));
        timeSwitchPane = new TimeSwitchPane();
        initFormatListener();
        return initPaneWithListener();
    }

    private void initFormatListener()
    {
        initValueFormatListener();
        initPercentFormatListener();
        isAxisZoom.addItemListener(new ItemListener() {

            final ChartInteractivePane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                timeSwitch.setEnabled(isAxisZoom.isSelected());
                if(!isAxisZoom.isSelected())
                    timeSwitch.setSelected(false);
            }

            
            {
                this$0 = ChartInteractivePane.this;
                super();
            }
        }
);
        timeSwitch.addItemListener(new ItemListener() {

            final ChartInteractivePane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                relayoutTimeSwitchPane();
            }

            
            {
                this$0 = ChartInteractivePane.this;
                super();
            }
        }
);
    }

    private void initValueFormatListener()
    {
        dataPointValueFormat.addMouseListener(new MouseAdapter() {

            final ChartInteractivePane this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                if(valueFormatPane == null)
                    valueFormatPane = new FormatPane();
                Point point = dataPointValueFormat.getLocationOnScreen();
                Point point1 = new Point(point.x - 20, point.y + dataPointValueFormat.getHeight());
                UIBubbleFloatPane uibubblefloatpane = new UIBubbleFloatPane(4, point1, valueFormatPane, 258, 209) {

                    final _cls4 this$1;

                    public void updateContentPane()
                    {
                        valueFormat = valueFormatPane.update();
                        parent.attributeChanged();
                    }

                    
                    {
                        this$1 = _cls4.this;
                        super(i, point, basicbeanpane, j, k);
                    }
                }
;
                uibubblefloatpane.show(ChartInteractivePane.this, Style.getInstance(valueFormat));
            }

            
            {
                this$0 = ChartInteractivePane.this;
                super();
            }
        }
);
    }

    private void initPercentFormatListener()
    {
        dataPointPercentFormat.addMouseListener(new MouseAdapter() {

            final ChartInteractivePane this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                if(percentFormatPane == null)
                    percentFormatPane = new FormatPane();
                CoreDecimalFormat coredecimalformat = new CoreDecimalFormat(new DecimalFormat(), "#.##%");
                percentFormatPane.populateBean(coredecimalformat);
                Point point = dataPointPercentFormat.getLocationOnScreen();
                Point point1 = new Point(point.x - 20, point.y + dataPointPercentFormat.getHeight());
                UIBubbleFloatPane uibubblefloatpane = new UIBubbleFloatPane(4, point1, percentFormatPane, 258, 209) {

                    final _cls5 this$1;

                    public void updateContentPane()
                    {
                        percentFormat = percentFormatPane.update();
                        parent.attributeChanged();
                    }

                    
                    {
                        this$1 = _cls5.this;
                        super(i, point, basicbeanpane, j, k);
                    }
                }
;
                uibubblefloatpane.show(ChartInteractivePane.this, Style.getInstance(percentFormat));
                super.mouseReleased(mouseevent);
                percentFormatPane.justUsePercentFormat();
            }

            
            {
                this$0 = ChartInteractivePane.this;
                super();
            }
        }
);
    }

    private void checkAutoRefresh()
    {
        GUICoreUtils.setEnabled(autoRefreshTime, isAutoRefresh.isSelected());
    }

    private JPanel initPaneWithListener()
    {
        initDataPointToolTipPane();
        initAxisShowPane();
        initAutoRefreshPane();
        initSuperlinkPane();
        initTimeSwitchPane();
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d, d, d, d, d, d
        };
        Component acomponent[][] = {
            {
                isChartAnimation
            }, {
                isSeriesDragEnable
            }, {
                tooltipPane
            }, {
                axisShowPane
            }, {
                isAxisZoom
            }, {
                autoRefreshPane
            }, {
                superlinkPane
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    private void initTimeSwitchPane()
    {
        timeSwitchContainer = new JPanel(new BorderLayout());
        timeSwitchContainer.add(timeSwitch, "Center");
    }

    private void initDataPointToolTipPane()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d, d, d, d
        };
        Component acomponent[][] = {
            {
                isDatapointValue, dataPointValueFormat
            }, {
                isDatapointPercent, dataPointPercentFormat
            }, {
                isAddress, null
            }, {
                isAddressName, null
            }, {
                isAddressTittle, null
            }, {
                tooltipShowTypeLabel, tooltipShowType
            }, {
                tooltipStyleLabel, tooltipStyle
            }
        };
        tooltipPane = TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
            "ChartData-Tooltip"
        }, acomponent, ad1, ad);
    }

    private void relayoutDataPointToolTipPane(Plot plot)
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d
        };
        if(plot.isShowAllDataPointLabel())
            isDatapointPercent.setText(Inter.getLocText("Chart-Value_Conversion"));
        if(plot.isSupportAddress4Gis())
        {
            UIButton uibutton = new UIButton();
            uibutton.setVisible(false);
            ad1 = (new double[] {
                d, d, d, d, d
            });
            Component acomponent2[][] = {
                {
                    isAddress, null
                }, {
                    isAddressName, null
                }, {
                    isAddressTittle, uibutton
                }, {
                    isDatapointValue, dataPointValueFormat
                }
            };
            tooltipPane = TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
                "ChartData-Tooltip"
            }, acomponent2, ad1, ad);
        } else
        if(plot.isSupportValuePercent())
        {
            Component acomponent[][];
            if(plot.isSupportTooltipSeriesType())
            {
                ad1 = (new double[] {
                    d, d, d, d
                });
                acomponent = (new Component[][] {
                    getTooltipShowTypeComponent(), new Component[] {
                        isDatapointValue, dataPointValueFormat
                    }, new Component[] {
                        isDatapointPercent, dataPointPercentFormat
                    }, getTooltipStyleComponent()
                });
            } else
            {
                ad1 = (new double[] {
                    d, d, d
                });
                acomponent = (new Component[][] {
                    new Component[] {
                        isDatapointValue, dataPointValueFormat
                    }, new Component[] {
                        isDatapointPercent, dataPointPercentFormat
                    }, getTooltipStyleComponent()
                });
            }
            tooltipPane = TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
                "ChartData-Tooltip"
            }, acomponent, ad1, ad);
        } else
        {
            Component acomponent1[][] = {
                {
                    isDatapointValue, dataPointValueFormat
                }, getTooltipStyleComponent()
            };
            tooltipPane = TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
                "ChartData-Tooltip"
            }, acomponent1, ad1, ad);
        }
    }

    private Component[] getTooltipShowTypeComponent()
    {
        double d = -2D;
        double d1 = -1D;
        Component acomponent[][] = {
            {
                tooltipShowTypeLabel, tooltipShowType
            }
        };
        double ad[] = {
            d1, d
        };
        double ad1[] = {
            d
        };
        return (new Component[] {
            TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad), null
        });
    }

    private Component[] getTooltipStyleComponent()
    {
        double d = -2D;
        double d1 = -1D;
        Component acomponent[][] = {
            {
                tooltipStyleLabel, tooltipStyle
            }
        };
        double ad[] = {
            d1, d
        };
        double ad1[] = {
            d
        };
        return (new Component[] {
            TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad), null
        });
    }

    private void initAxisShowPane()
    {
        double d = -2D;
        double ad[] = {
            d
        };
        double ad1[] = {
            d
        };
        Component acomponent[][] = {
            {
                isAxisShowToolTip
            }
        };
        axisShowPane = TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
            "ChartF-Axis", "Chart-Interactive"
        }, acomponent, ad1, ad);
    }

    private void initAutoRefreshPane()
    {
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
                isAutoRefresh, null
            }, {
                GUICoreUtils.createFlowPane(new Component[] {
                    new UILabel(Inter.getLocText("Chart-Time_Interval")), autoRefreshTime, new UILabel(Inter.getLocText("Chart-Time_Seconds"))
                }, 1)
            }, {
                new UILabel((new StringBuilder()).append("<html><font size='2' face='Microsoft Yahei' color='red'>").append(Inter.getLocText("FR-Chart-AutoRefresh_NotSupportIMGAndReportData")).append("</font></html>").toString()), null
            }
        };
        autoRefreshPane = TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
            "Data-Check"
        }, acomponent, ad1, ad);
    }

    private void initSuperlinkPane()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d
        };
        Component acomponent[][] = {
            {
                superLink, null
            }
        };
        superlinkPane = TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
            "Hyperlink"
        }, acomponent, ad1, ad);
    }

    private void relayoutWithPlot(Plot plot)
    {
        removeAll();
        double d = -2D;
        double ad[] = {
            -1D
        };
        double ad1[] = {
            d, d, d
        };
        Component acomponent[][] = {
            getChartAnimatePane(plot, ad1, ad), getChartScalePane(plot, ad1, ad), getDataTooltipPane(plot, ad1, ad), getAxisTipPane(plot, ad1, ad), getAutoRefreshPane(plot, ad1, ad), getHotHyperlinPane(plot)
        };
        double ad2[] = {
            d, d, d, d, d, d
        };
        reloaPane(TableLayoutHelper.createTableLayoutPane(acomponent, ad2, ad));
    }

    private Component[] getChartAnimatePane(Plot plot, double ad[], double ad1[])
    {
        if(plot.isSupportAnimate() && plot.isSupportSeriesDrag())
            return (new Component[] {
                TableLayoutHelper.createTableLayoutPane(new Component[][] {
                    new Component[] {
                        isChartAnimation
                    }, new Component[] {
                        isSeriesDragEnable
                    }, new Component[] {
                        new JSeparator()
                    }
                }, ad, ad1)
            });
        if(plot.isSupportAnimate() && !plot.isSupportSeriesDrag())
            return (new Component[] {
                TableLayoutHelper.createTableLayoutPane(new Component[][] {
                    new Component[] {
                        isChartAnimation
                    }, new Component[] {
                        new JSeparator()
                    }
                }, ad, ad1)
            });
        else
            return (new Component[] {
                null
            });
    }

    private void relayoutTimeSwitchPane()
    {
        timeSwitchContainer.removeAll();
        timeSwitchContainer.add(timeSwitch, "Center");
        if(timeSwitch.isSelected())
        {
            double d = -2D;
            double d1 = -1D;
            double ad[] = {
                40D, d1
            };
            double ad1[] = {
                d
            };
            Component acomponent[][] = {
                {
                    null, timeSwitchPane
                }
            };
            JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
            timeSwitchContainer.add(jpanel, "South");
        }
        timeSwitchContainer.revalidate();
    }

    private Component[] getChartScalePane(Plot plot, double ad[], double ad1[])
    {
        boolean flag = plot.getxAxis() != null && plot.getxAxis().isDate();
        if(plot.isSupportZoomCategoryAxis() && !flag)
            return (new Component[] {
                TableLayoutHelper.createTableLayoutPane(new Component[][] {
                    new Component[] {
                        isAxisZoom
                    }, new Component[] {
                        new JSeparator()
                    }
                }, ad, ad1)
            });
        if(plot.isSupportZoomCategoryAxis() && flag)
            return (new Component[] {
                TableLayoutHelper.createTableLayoutPane(new Component[][] {
                    new Component[] {
                        isAxisZoom
                    }, new Component[] {
                        timeSwitchContainer
                    }, new Component[] {
                        new JSeparator()
                    }
                }, ad, ad1)
            });
        else
            return (new Component[] {
                null
            });
    }

    private Component[] getDataTooltipPane(Plot plot, double ad[], double ad1[])
    {
        relayoutDataPointToolTipPane(plot);
        if(plot.isSupportTooltipInInteractivePane())
            return (new Component[] {
                TableLayoutHelper.createTableLayoutPane(new Component[][] {
                    new Component[] {
                        tooltipPane
                    }, new Component[] {
                        new JSeparator()
                    }
                }, ad, ad1)
            });
        else
            return (new Component[] {
                null
            });
    }

    private Component[] getAxisTipPane(Plot plot, double ad[], double ad1[])
    {
        if(plot.isSupportAxisTip())
            return (new Component[] {
                TableLayoutHelper.createTableLayoutPane(new Component[][] {
                    new Component[] {
                        axisShowPane
                    }, new Component[] {
                        new JSeparator()
                    }
                }, ad, ad1)
            });
        else
            return (new Component[] {
                null
            });
    }

    private Component[] getAutoRefreshPane(Plot plot, double ad[], double ad1[])
    {
        if(plot.isSupportAutoRefresh())
            return (new Component[] {
                TableLayoutHelper.createTableLayoutPane(new Component[][] {
                    new Component[] {
                        autoRefreshPane
                    }, new Component[] {
                        new JSeparator()
                    }
                }, ad, ad1)
            });
        else
            return (new Component[] {
                null
            });
    }

    private Component[] getHotHyperlinPane(Plot plot)
    {
        return (new Component[] {
            superlinkPane
        });
    }

    public void populateBean(Chart chart)
    {
        if(chart == null || chart.getPlot() == null)
        {
            return;
        } else
        {
            Plot plot = chart.getPlot();
            relayoutWithGis(chart, plot);
            relayoutWithPlot(plot);
            populateChartAnimate(chart, plot);
            populateChartScale(plot);
            populateDataTooltip(plot);
            populateAxisTip(plot);
            populateAutoRefresh(chart);
            populateHyperlink(plot);
            checkAutoRefresh();
            return;
        }
    }

    private void relayoutWithGis(Chart chart, Plot plot)
    {
        if(plot.isSupportAddress4Gis())
        {
            TopDefinitionProvider topdefinitionprovider = chart.getFilterDefinition();
            boolean flag = true;
            if(topdefinitionprovider instanceof GisMapTableDefinition)
                flag = ((GisMapTableDefinition)topdefinitionprovider).isAddress();
            else
            if(topdefinitionprovider instanceof GisMapReportDefinition)
                flag = ((GisMapReportDefinition)topdefinitionprovider).isAddress();
            if(flag)
                isAddress.setText(Inter.getLocText("Chart-Use_Address"));
            else
                isAddress.setText(Inter.getLocText("Chart-Use_LatLng"));
        }
    }

    private void populateChartAnimate(Chart chart, Plot plot)
    {
        if(plot.isSupportAnimate())
            isChartAnimation.setSelected(chart.isJSDraw());
        if(plot.isSupportSeriesDrag())
            isSeriesDragEnable.setSelected(plot.isSeriesDragEnable());
    }

    private void populateChartScale(Plot plot)
    {
        if(plot.isSupportZoomCategoryAxis())
            isAxisZoom.setSelected(plot.getxAxis() != null && plot.getxAxis().isZoom());
        timeSwitch.setSelected(false);
        timeSwitch.setEnabled(false);
        if(!plot.isSupportZoomCategoryAxis() || !isAxisZoom.isSelected())
            return;
        if(plot.getxAxis() == null && !plot.getxAxis().isDate())
            return;
        timeSwitch.setEnabled(true);
        ArrayList arraylist = plot.getxAxis().getTimeSwitchMap();
        timeSwitch.setSelected(arraylist != null && !arraylist.isEmpty());
        if(timeSwitch.isSelected())
            timeSwitchPane.populate(plot);
    }

    private void populateDataTooltip(Plot plot)
    {
        if(plot.isSupportTooltipInInteractivePane())
        {
            AttrContents attrcontents = plot.getHotTooltipStyle();
            if(attrcontents == null)
                return;
            String s = attrcontents.getSeriesLabel();
            if(s == null)
                return;
            valueFormat = attrcontents.getFormat();
            isDatapointValue.setSelected(s.contains("${VALUE}"));
            if(attrcontents.isWhiteBackground())
                tooltipStyle.setSelectedIndex(0);
            else
                tooltipStyle.setSelectedIndex(1);
            if(plot.isSupportValuePercent())
            {
                percentFormat = attrcontents.getPercentFormat();
                isDatapointPercent.setSelected(s.contains("${PERCENT}"));
            }
            if(plot.isSupportAddress4Gis())
            {
                isAddressTittle.setSelected(s.contains("${AREA_TITTLE}"));
                isAddress.setSelected(s.contains("${ADDRESS}"));
                isAddressName.setSelected(s.contains("${ADDRESS_NAME}"));
            }
            if(plot.isSupportTooltipSeriesType())
                if(attrcontents.isShowMutiSeries())
                    tooltipShowType.setSelectedIndex(1);
                else
                    tooltipShowType.setSelectedIndex(0);
        }
    }

    private void populateAxisTip(Plot plot)
    {
        if(plot.isSupportAxisTip())
            isAxisShowToolTip.setSelected(plot.isInteractiveAxisTooltip());
    }

    protected void populateAutoRefresh(Chart chart)
    {
        Plot plot = chart.getPlot();
        if(plot.isSupportAutoRefresh())
            if(plot.getAutoRefreshPerSecond() < 1)
            {
                isAutoRefresh.setSelected(false);
                autoRefreshTime.setValue(2D);
            } else
            {
                isAutoRefresh.setSelected(true);
                autoRefreshTime.setValue(plot.getAutoRefreshPerSecond());
            }
    }

    private void populateHyperlink(Plot plot)
    {
        HashMap hashmap = renewMapWithPlot(plot);
        java.util.List list = refreshList(hashmap);
        superLink.refreshMenuAndAddMenuAction(list);
        ArrayList arraylist = new ArrayList();
        NameJavaScriptGroup namejavascriptgroup = plot.getHotHyperLink();
        for(int i = 0; namejavascriptgroup != null && i < namejavascriptgroup.size(); i++)
        {
            NameJavaScript namejavascript = namejavascriptgroup.getNameHyperlink(i);
            if(namejavascript != null && namejavascript.getJavaScript() != null)
            {
                JavaScript javascript = namejavascript.getJavaScript();
                arraylist.add(new UIMenuNameableCreator(namejavascript.getName(), javascript, getUseMap(hashmap, javascript.getClass())));
            }
        }

        superLink.populateBean(arraylist);
        superLink.doLayout();
    }

    public void updateBean(Chart chart)
    {
        if(chart == null || chart.getPlot() == null)
        {
            return;
        } else
        {
            Plot plot = chart.getPlot();
            updateChartAnimate(chart, plot);
            updateChartScale(plot);
            updateDataTooltip(plot);
            updateAxisTip(plot);
            updateAutoRefresh(plot);
            updateHyperlink(plot);
            return;
        }
    }

    private void updateChartAnimate(Chart chart, Plot plot)
    {
        if(plot.isSupportAnimate())
            chart.setJSDraw(isChartAnimation.isSelected());
        if(plot.isSupportSeriesDrag())
            plot.setSeriesDragEnable(isSeriesDragEnable.isSelected());
    }

    private void updateChartScale(Plot plot)
    {
        if(plot.isSupportZoomCategoryAxis() && plot.getxAxis() != null)
            plot.getxAxis().setZoom(isAxisZoom.isSelected());
        if(plot.getxAxis() == null)
            return;
        boolean flag = plot.getxAxis() != null && plot.getxAxis().isDate();
        boolean flag1 = !flag || !timeSwitch.isSelected();
        if(flag1 && plot.getxAxis().getTimeSwitchMap() != null)
        {
            plot.getxAxis().getTimeSwitchMap().clear();
            return;
        }
        if(plot.getxAxis().isDate() && timeSwitch.isSelected())
            timeSwitchPane.update(plot);
    }

    private void updateDataTooltip(Plot plot)
    {
        if(plot.isSupportTooltipInInteractivePane())
        {
            AttrContents attrcontents = plot.getHotTooltipStyle();
            if(attrcontents == null)
                attrcontents = new AttrContents();
            String s = plot.isSupportAddress4Gis() ? getGisTooltipContent() : getTooltipContent(plot);
            attrcontents.setSeriesLabel(s);
            if(tooltipStyle != null)
            {
                boolean flag = tooltipStyle.getSelectedIndex() == 0;
                attrcontents.setWhiteBackground(flag);
            }
            if(tooltipShowType != null)
            {
                boolean flag1 = plot.isSupportTooltipSeriesType() && tooltipShowType.getSelectedIndex() == 1;
                attrcontents.setShowMutiSeries(flag1);
            }
            attrcontents.setFormat(valueFormat);
            if(plot.isSupportValuePercent() && percentFormat != null)
                attrcontents.setPercentFormat(percentFormat);
        }
    }

    private String getTooltipContent(Plot plot)
    {
        String s = "";
        s = (new StringBuilder()).append(s).append("${SERIES}${BR}${CATEGORY}").toString();
        if(isDatapointValue.isSelected() && !isDatapointPercent.isSelected())
            s = (new StringBuilder()).append(s).append("${BR}${VALUE}").toString();
        else
        if(!isDatapointValue.isSelected() && isDatapointPercent.isSelected())
            s = (new StringBuilder()).append(s).append("${BR}${PERCENT}").toString();
        else
        if(isDatapointValue.isSelected() && isDatapointPercent.isSelected())
            s = (new StringBuilder()).append(s).append("${BR}${VALUE}${BR}${PERCENT}").toString();
        else
            s = null;
        return s;
    }

    private String getGisTooltipContent()
    {
        String s = "";
        s = (new StringBuilder()).append(s).append("${SERIES}${BR}${CATEGORY}").toString();
        boolean flag = true;
        if(isDatapointValue.isSelected())
        {
            s = (new StringBuilder()).append(s).append("${BR}${VALUE}").toString();
            flag = false;
        }
        if(isAddressTittle.isSelected())
        {
            s = (new StringBuilder()).append(s).append("${BR}${AREA_TITTLE}").toString();
            flag = false;
        }
        if(isAddress.isSelected())
        {
            s = (new StringBuilder()).append(s).append("${BR}${ADDRESS}").toString();
            flag = false;
        }
        if(isAddressName.isSelected())
        {
            s = (new StringBuilder()).append(s).append("${BR}${ADDRESS_NAME}").toString();
            flag = false;
        }
        if(flag)
            s = null;
        return s;
    }

    private void updateAxisTip(Plot plot)
    {
        if(plot.isSupportAxisTip())
            plot.setInteractiveAxisTooltip(isAxisShowToolTip.isSelected());
    }

    private void updateAutoRefresh(Plot plot)
    {
        if(plot.isSupportAutoRefresh())
            if(isAutoRefresh.isSelected() && autoRefreshTime.getValue() >= 2D)
                plot.setAutoRefreshPerSecond((int)autoRefreshTime.getValue());
            else
                plot.setAutoRefreshPerSecond(-1);
    }

    private void updateHyperlink(Plot plot)
    {
        NameJavaScriptGroup namejavascriptgroup = new NameJavaScriptGroup();
        namejavascriptgroup.clear();
        superLink.resetItemName();
        java.util.List list = superLink.updateBean();
        for(int i = 0; i < list.size(); i++)
        {
            UIMenuNameableCreator uimenunameablecreator = (UIMenuNameableCreator)list.get(i);
            NameJavaScript namejavascript = new NameJavaScript(uimenunameablecreator.getName(), (JavaScript)uimenunameablecreator.getObj());
            namejavascriptgroup.addNameHyperlink(namejavascript);
        }

        plot.setHotHyperLink(namejavascriptgroup);
    }

    protected Class getUseMap(HashMap hashmap, Object obj)
    {
        if(hashmap.get(obj) != null)
            return (Class)hashmap.get(obj);
        for(Iterator iterator = hashmap.keySet().iterator(); iterator.hasNext();)
        {
            Class class1 = (Class)iterator.next();
            if(class1.isAssignableFrom((Class)obj))
                return (Class)hashmap.get(class1);
        }

        return null;
    }

    protected java.util.List refreshList(HashMap hashmap)
    {
        ArrayList arraylist = new ArrayList();
        arraylist.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Reportlet"), new ReportletHyperlink(), getUseMap(hashmap, com/fr/js/ReportletHyperlink)));
        arraylist.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Mail"), new EmailJavaScript(), com/fr/design/chart/javascript/ChartEmailPane));
        arraylist.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Web"), new WebHyperlink(), getUseMap(hashmap, com/fr/js/WebHyperlink)));
        arraylist.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Dynamic_Parameters"), new ParameterJavaScript(), getUseMap(hashmap, com/fr/js/ParameterJavaScript)));
        arraylist.add(new UIMenuNameableCreator("JavaScript", new JavaScriptImpl(), getUseMap(hashmap, com/fr/js/JavaScriptImpl)));
        arraylist.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Float_Chart"), new ChartHyperPoplink(), getUseMap(hashmap, com/fr/chart/web/ChartHyperPoplink)));
        arraylist.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Cell"), new ChartHyperRelateCellLink(), getUseMap(hashmap, com/fr/chart/web/ChartHyperRelateCellLink)));
        arraylist.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Float"), new ChartHyperRelateFloatLink(), getUseMap(hashmap, com/fr/chart/web/ChartHyperRelateFloatLink)));
        FormHyperlinkProvider formhyperlinkprovider = (FormHyperlinkProvider)StableFactory.getMarkedInstanceObjectFromClass("FormHyperlink", com/fr/js/FormHyperlinkProvider);
        arraylist.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Form"), formhyperlinkprovider, getUseMap(hashmap, com/fr/js/FormHyperlinkProvider)));
        return arraylist;
    }

    private HashMap renewMapWithPlot(Plot plot)
    {
        if(plot instanceof PiePlot)
            return getPiePlotHyperMap();
        if(plot instanceof MapPlot)
            return getMapPlotHyperMap();
        if(plot instanceof GisMapPlot)
            return getGisPlotHyperMap();
        if(plot instanceof XYScatterPlot)
            return getXYHyperMap();
        if(plot instanceof BubblePlot)
            return getBubbleHyperMap();
        if(plot instanceof StockPlot)
            return getStockHyperMap();
        if(plot instanceof GanttPlot)
            return getGanttHyperMap();
        if(plot instanceof MeterPlot)
            return getMeterHyperMap();
        else
            return getNormalPlotHyperMap();
    }

    private HashMap getXYHyperMap()
    {
        if(xyMap.isEmpty())
        {
            xyMap.put(com/fr/js/ReportletHyperlink, com/fr/design/hyperlink/ReportletHyperlinkPane$CHART_XY);
            xyMap.put(com/fr/js/EmailJavaScript, com/fr/design/chart/javascript/ChartEmailPane);
            xyMap.put(com/fr/js/WebHyperlink, com/fr/design/hyperlink/WebHyperlinkPane$CHART_XY);
            xyMap.put(com/fr/js/ParameterJavaScript, com/fr/design/javascript/ParameterJavaScriptPane$CHART_XY);
            xyMap.put(com/fr/js/JavaScriptImpl, com/fr/design/javascript/JavaScriptImplPane$CHART_XY);
            xyMap.put(com/fr/chart/web/ChartHyperPoplink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperPoplinkPane$CHART_XY);
            xyMap.put(com/fr/chart/web/ChartHyperRelateCellLink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperRelateCellLinkPane$CHART_XY);
            xyMap.put(com/fr/chart/web/ChartHyperRelateFloatLink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperRelateFloatLinkPane$CHART_XY);
            xyMap.put(com/fr/js/FormHyperlinkProvider, com/fr/design/chart/series/SeriesCondition/impl/FormHyperlinkPane$CHART_XY);
        }
        return xyMap;
    }

    private HashMap getBubbleHyperMap()
    {
        if(bubbleMap.isEmpty())
        {
            bubbleMap.put(com/fr/js/ReportletHyperlink, com/fr/design/hyperlink/ReportletHyperlinkPane$CHART_BUBBLE);
            bubbleMap.put(com/fr/js/EmailJavaScript, com/fr/design/chart/javascript/ChartEmailPane);
            bubbleMap.put(com/fr/js/WebHyperlink, com/fr/design/hyperlink/WebHyperlinkPane$CHART_BUBBLE);
            bubbleMap.put(com/fr/js/ParameterJavaScript, com/fr/design/javascript/ParameterJavaScriptPane$CHART_BUBBLE);
            bubbleMap.put(com/fr/js/JavaScriptImpl, com/fr/design/javascript/JavaScriptImplPane$CHART_BUBBLE);
            bubbleMap.put(com/fr/chart/web/ChartHyperPoplink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperPoplinkPane$CHART_BUBBLE);
            bubbleMap.put(com/fr/chart/web/ChartHyperRelateCellLink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperRelateCellLinkPane$CHART_BUBBLE);
            bubbleMap.put(com/fr/chart/web/ChartHyperRelateFloatLink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperRelateFloatLinkPane$CHART_BUBBLE);
            bubbleMap.put(com/fr/js/FormHyperlinkProvider, com/fr/design/chart/series/SeriesCondition/impl/FormHyperlinkPane$CHART_BUBBLE);
        }
        return bubbleMap;
    }

    private HashMap getStockHyperMap()
    {
        if(stockMap.isEmpty())
        {
            stockMap.put(com/fr/js/ReportletHyperlink, com/fr/design/hyperlink/ReportletHyperlinkPane$CHART_STOCK);
            stockMap.put(com/fr/js/EmailJavaScript, com/fr/design/chart/javascript/ChartEmailPane);
            stockMap.put(com/fr/js/WebHyperlink, com/fr/design/hyperlink/WebHyperlinkPane$CHART_STOCK);
            stockMap.put(com/fr/js/ParameterJavaScript, com/fr/design/javascript/ParameterJavaScriptPane$CHART_STOCK);
            stockMap.put(com/fr/js/JavaScriptImpl, com/fr/design/javascript/JavaScriptImplPane$CHART_STOCK);
            stockMap.put(com/fr/chart/web/ChartHyperPoplink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperPoplinkPane$CHART_STOCK);
            stockMap.put(com/fr/chart/web/ChartHyperRelateCellLink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperRelateCellLinkPane$CHART_STOCK);
            stockMap.put(com/fr/chart/web/ChartHyperRelateFloatLink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperRelateFloatLinkPane$CHART_STOCK);
            stockMap.put(com/fr/js/FormHyperlinkProvider, com/fr/design/chart/series/SeriesCondition/impl/FormHyperlinkPane$CHART_STOCK);
        }
        return stockMap;
    }

    private HashMap getGanttHyperMap()
    {
        if(ganttMap.isEmpty())
        {
            ganttMap.put(com/fr/js/ReportletHyperlink, com/fr/design/hyperlink/ReportletHyperlinkPane$CHART_GANTT);
            ganttMap.put(com/fr/js/EmailJavaScript, com/fr/design/chart/javascript/ChartEmailPane);
            ganttMap.put(com/fr/js/WebHyperlink, com/fr/design/hyperlink/WebHyperlinkPane$CHART_GANTT);
            ganttMap.put(com/fr/js/ParameterJavaScript, com/fr/design/javascript/ParameterJavaScriptPane$CHART_GANTT);
            ganttMap.put(com/fr/js/JavaScriptImpl, com/fr/design/javascript/JavaScriptImplPane$CHART_GANTT);
            ganttMap.put(com/fr/chart/web/ChartHyperPoplink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperPoplinkPane$CHART_GANTT);
            ganttMap.put(com/fr/chart/web/ChartHyperRelateCellLink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperRelateCellLinkPane$CHART_GANTT);
            ganttMap.put(com/fr/chart/web/ChartHyperRelateFloatLink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperRelateFloatLinkPane$CHART_GANTT);
            ganttMap.put(com/fr/js/FormHyperlinkProvider, com/fr/design/chart/series/SeriesCondition/impl/FormHyperlinkPane$CHART_GANTT);
        }
        return ganttMap;
    }

    private HashMap getMeterHyperMap()
    {
        if(meterMap.isEmpty())
        {
            meterMap.put(com/fr/js/ReportletHyperlink, com/fr/design/hyperlink/ReportletHyperlinkPane$CHART_METER);
            meterMap.put(com/fr/js/EmailJavaScript, com/fr/design/chart/javascript/ChartEmailPane);
            meterMap.put(com/fr/js/WebHyperlink, com/fr/design/hyperlink/WebHyperlinkPane$CHART_METER);
            meterMap.put(com/fr/js/ParameterJavaScript, com/fr/design/javascript/ParameterJavaScriptPane$CHART_METER);
            meterMap.put(com/fr/js/JavaScriptImpl, com/fr/design/javascript/JavaScriptImplPane$CHART_METER);
            meterMap.put(com/fr/chart/web/ChartHyperPoplink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperPoplinkPane$CHART_METER);
            meterMap.put(com/fr/chart/web/ChartHyperRelateCellLink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperRelateCellLinkPane$CHART_METER);
            meterMap.put(com/fr/chart/web/ChartHyperRelateFloatLink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperRelateFloatLinkPane$CHART_METER);
            meterMap.put(com/fr/js/FormHyperlinkProvider, com/fr/design/chart/series/SeriesCondition/impl/FormHyperlinkPane$CHART_METER);
        }
        return meterMap;
    }

    private HashMap getMapPlotHyperMap()
    {
        if(mapMap.isEmpty())
        {
            mapMap.put(com/fr/js/ReportletHyperlink, com/fr/design/hyperlink/ReportletHyperlinkPane$CHART_MAP);
            mapMap.put(com/fr/js/EmailJavaScript, com/fr/design/chart/javascript/ChartEmailPane);
            mapMap.put(com/fr/js/WebHyperlink, com/fr/design/hyperlink/WebHyperlinkPane$CHART_MAP);
            mapMap.put(com/fr/js/ParameterJavaScript, com/fr/design/javascript/ParameterJavaScriptPane$CHART_MAP);
            mapMap.put(com/fr/js/JavaScriptImpl, com/fr/design/javascript/JavaScriptImplPane$CHART_MAP);
            mapMap.put(com/fr/chart/web/ChartHyperPoplink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperPoplinkPane$CHART_MAP);
            mapMap.put(com/fr/chart/web/ChartHyperRelateCellLink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperRelateCellLinkPane$CHART_MAP);
            mapMap.put(com/fr/chart/web/ChartHyperRelateFloatLink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperRelateFloatLinkPane$CHART_MAP);
            mapMap.put(com/fr/js/FormHyperlinkProvider, com/fr/design/chart/series/SeriesCondition/impl/FormHyperlinkPane$CHART_MAP);
        }
        return mapMap;
    }

    private HashMap getPiePlotHyperMap()
    {
        if(pieMap.isEmpty())
        {
            pieMap.put(com/fr/js/ReportletHyperlink, com/fr/design/hyperlink/ReportletHyperlinkPane$CHART_PIE);
            pieMap.put(com/fr/js/EmailJavaScript, com/fr/design/chart/javascript/ChartEmailPane);
            pieMap.put(com/fr/js/WebHyperlink, com/fr/design/hyperlink/WebHyperlinkPane$CHART_PIE);
            pieMap.put(com/fr/js/ParameterJavaScript, com/fr/design/javascript/ParameterJavaScriptPane$CHART_PIE);
            pieMap.put(com/fr/js/JavaScriptImpl, com/fr/design/javascript/JavaScriptImplPane$CHART_PIE);
            pieMap.put(com/fr/chart/web/ChartHyperPoplink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperPoplinkPane$CHART_PIE);
            pieMap.put(com/fr/chart/web/ChartHyperRelateCellLink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperRelateCellLinkPane$CHART_PIE);
            pieMap.put(com/fr/chart/web/ChartHyperRelateFloatLink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperRelateFloatLinkPane$CHART_PIE);
            pieMap.put(com/fr/js/FormHyperlinkProvider, com/fr/design/chart/series/SeriesCondition/impl/FormHyperlinkPane$CHART_PIE);
        }
        return pieMap;
    }

    private HashMap getGisPlotHyperMap()
    {
        if(gisMap.isEmpty())
        {
            gisMap.put(com/fr/js/ReportletHyperlink, com/fr/design/hyperlink/ReportletHyperlinkPane$CHART_GIS);
            gisMap.put(com/fr/js/EmailJavaScript, com/fr/design/chart/javascript/ChartEmailPane);
            gisMap.put(com/fr/js/WebHyperlink, com/fr/design/hyperlink/WebHyperlinkPane$CHART_GIS);
            gisMap.put(com/fr/js/ParameterJavaScript, com/fr/design/javascript/ParameterJavaScriptPane$CHART_GIS);
            gisMap.put(com/fr/js/JavaScriptImpl, com/fr/design/javascript/JavaScriptImplPane$CHART_GIS);
            gisMap.put(com/fr/chart/web/ChartHyperPoplink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperPoplinkPane$CHART_GIS);
            gisMap.put(com/fr/chart/web/ChartHyperRelateCellLink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperRelateCellLinkPane$CHART_GIS);
            gisMap.put(com/fr/chart/web/ChartHyperRelateFloatLink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperRelateFloatLinkPane$CHART_GIS);
            gisMap.put(com/fr/js/FormHyperlinkProvider, com/fr/design/chart/series/SeriesCondition/impl/FormHyperlinkPane$CHART_GIS);
        }
        return gisMap;
    }

    private HashMap getNormalPlotHyperMap()
    {
        if(normalMap.isEmpty())
        {
            FormHyperlinkProvider formhyperlinkprovider = (FormHyperlinkProvider)StableFactory.getMarkedInstanceObjectFromClass("FormHyperlink", com/fr/js/FormHyperlinkProvider);
            normalMap.put(com/fr/js/ReportletHyperlink, com/fr/design/hyperlink/ReportletHyperlinkPane$CHART);
            normalMap.put(com/fr/js/EmailJavaScript, com/fr/design/chart/javascript/ChartEmailPane);
            normalMap.put(com/fr/js/WebHyperlink, com/fr/design/hyperlink/WebHyperlinkPane$CHART);
            normalMap.put(com/fr/js/ParameterJavaScript, com/fr/design/javascript/ParameterJavaScriptPane$CHART);
            normalMap.put(com/fr/js/JavaScriptImpl, com/fr/design/javascript/JavaScriptImplPane$CHART);
            normalMap.put(com/fr/chart/web/ChartHyperPoplink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperPoplinkPane);
            normalMap.put(com/fr/chart/web/ChartHyperRelateCellLink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperRelateCellLinkPane);
            normalMap.put(com/fr/chart/web/ChartHyperRelateFloatLink, com/fr/design/chart/series/SeriesCondition/impl/ChartHyperRelateFloatLinkPane);
            normalMap.put(com/fr/js/FormHyperlinkProvider, com/fr/design/chart/series/SeriesCondition/impl/FormHyperlinkPane$CHART);
            if(formhyperlinkprovider != null)
                normalMap.put(formhyperlinkprovider.getClass(), com/fr/design/chart/series/SeriesCondition/impl/FormHyperlinkPane$CHART);
        }
        return normalMap;
    }

    public Chart updateBean()
    {
        return null;
    }

    public void registerChangeListener(UIObserverListener uiobserverlistener)
    {
        timeSwitch.registerChangeListener(uiobserverlistener);
        timeSwitchPane.registerChangeListener(uiobserverlistener);
    }

    public boolean shouldResponseChangeListener()
    {
        return true;
    }

    public volatile Object updateBean()
    {
        return updateBean();
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
