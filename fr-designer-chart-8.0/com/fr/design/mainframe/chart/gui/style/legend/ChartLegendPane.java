// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.legend;

import com.fr.base.BaseUtils;
import com.fr.chart.chartattr.*;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.style.*;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ChartLegendPane extends BasicScrollPane
{
    private class ContentPane extends JPanel
    {

        final ChartLegendPane this$0;

        private void initComponents()
        {
            isLegendVisible = new UICheckBox(Inter.getLocText("Chart_Legend_Is_Visible"));
            textAttrPane = new ChartTextAttrPane();
            borderPane = new ChartBorderPane();
            String as[] = {
                Inter.getLocText("StyleAlignment-Top"), Inter.getLocText("StyleAlignment-Bottom"), Inter.getLocText("StyleAlignment-Left"), Inter.getLocText("StyleAlignment-Right"), Inter.getLocText("Right_Top")
            };
            Integer ainteger[] = {
                Integer.valueOf(1), Integer.valueOf(3), Integer.valueOf(2), Integer.valueOf(4), Integer.valueOf(8)
            };
            Icon aicon[] = {
                BaseUtils.readIcon("/com/fr/design/images/chart/ChartLegend/layout_top.png"), BaseUtils.readIcon("/com/fr/design/images/chart/ChartLegend/layout_bottom.png"), BaseUtils.readIcon("/com/fr/design/images/chart/ChartLegend/layout_left.png"), BaseUtils.readIcon("/com/fr/design/images/chart/ChartLegend/layout_right.png"), BaseUtils.readIcon("/com/fr/design/images/chart/ChartLegend/layout_top_right.png")
            };
            location = new UIButtonGroup(aicon, ainteger);
            location.setAllToolTips(as);
            backgroundPane = new ChartBackgroundNoImagePane();
            double d = -2D;
            double d1 = -1D;
            double ad[] = {
                46D, d1
            };
            double ad1[] = {
                d, d, d, d, d, d
            };
            Component acomponent[][] = {
                {
                    null, textAttrPane
                }, {
                    new JSeparator(), null
                }, {
                    new BoldFontTextLabel(Inter.getLocText("Layout")), location
                }, {
                    new JSeparator(), null
                }, {
                    borderPane, null
                }, {
                    backgroundPane, null
                }
            };
            legendPane = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
            double ad2[] = {
                d1
            };
            double ad3[] = {
                d, d
            };
            JPanel jpanel = TableLayoutHelper.createTableLayoutPane(new Component[][] {
                new Component[] {
                    isLegendVisible
                }, new Component[] {
                    legendPane
                }
            }, ad3, ad2);
            setLayout(new BorderLayout());
            add(jpanel, "Center");
            isLegendVisible.addActionListener(new ActionListener() {

                final ContentPane this$1;

                public void actionPerformed(ActionEvent actionevent)
                {
                    checkBoxUse();
                }

                
                {
                    this$1 = ContentPane.this;
                    super();
                }
            }
);
        }

        public ContentPane()
        {
            this$0 = ChartLegendPane.this;
            super();
            initComponents();
        }
    }


    private UICheckBox isLegendVisible;
    private ChartTextAttrPane textAttrPane;
    private ChartBorderPane borderPane;
    private UIButtonGroup location;
    private ChartBackgroundNoImagePane backgroundPane;
    private JPanel legendPane;
    private static ChartLegendPane CONTEXT;

    public ChartLegendPane()
    {
    }

    public static final ChartLegendPane getInstance()
    {
        if(CONTEXT == null)
            CONTEXT = new ChartLegendPane();
        return CONTEXT;
    }

    private void checkBoxUse()
    {
        isLegendVisible.setEnabled(true);
        legendPane.setVisible(isLegendVisible.isSelected());
    }

    public String title4PopupWindow()
    {
        return PaneTitleConstants.CHART_STYLE_LEGNED_TITLE;
    }

    protected JPanel createContentPane()
    {
        return new ContentPane();
    }

    public void updateBean(Chart chart)
    {
        if(chart == null)
            return;
        Plot plot = chart.getPlot();
        if(plot == null)
            return;
        Legend legend = plot.getLegend();
        if(legend == null)
            legend = new Legend();
        legend.setLegendVisible(isLegendVisible.isSelected());
        legend.setFRFont(textAttrPane.updateFRFont());
        borderPane.update(legend);
        legend.setPosition(((Integer)location.getSelectedItem()).intValue());
        backgroundPane.update(legend);
    }

    public void populateBean(Chart chart)
    {
        if(chart != null && chart.getPlot() != null)
        {
            Legend legend = chart.getPlot().getLegend();
            if(legend != null)
            {
                isLegendVisible.setSelected(legend.isLegendVisible());
                textAttrPane.populate(legend.getFRFont());
                borderPane.populate(legend);
                location.setSelectedItem(Integer.valueOf(legend.getPosition()));
                backgroundPane.populate(legend);
            }
        }
        checkBoxUse();
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
