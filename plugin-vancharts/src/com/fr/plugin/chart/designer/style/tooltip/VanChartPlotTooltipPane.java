package com.fr.plugin.chart.designer.style.tooltip;

import com.fr.chart.chartattr.Plot;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.background.VanChartBackgroundWithOutImagePane;
import com.fr.plugin.chart.designer.component.border.VanChartBorderWithRadiusPane;
import com.fr.plugin.chart.designer.component.VanChartTooltipContentPane;
import com.fr.plugin.chart.designer.component.tooltip.TooltipContentPaneWithOutSeries;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.gauge.GaugeStyle;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VanChartPlotTooltipPane extends BasicPane {
    private static final long serialVersionUID = 6087381131907589370L;

    protected UICheckBox isTooltipShow;

    protected VanChartTooltipContentPane tooltipContentPane;

    protected UIButtonGroup<Integer> style;
    protected ChartTextAttrPane textFontPane;

    protected VanChartBorderWithRadiusPane borderPane;

    protected VanChartBackgroundWithOutImagePane backgroundPane;

    protected UIToggleButton showAllSeries;
    protected UIButtonGroup followMouse;

    protected VanChartStylePane parent;

    protected JPanel tooltipPane;

    public VanChartPlotTooltipPane(Plot plot, VanChartStylePane parent) {
        this.parent = parent;
        addComponents(plot);
    }

    protected  void addComponents(Plot plot) {
        isTooltipShow = new UICheckBox(Inter.getLocText("Plugin-ChartF_UseTooltip"));
        tooltipPane = createTooltipPane(plot);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{isTooltipShow},
                new Component[]{new JSeparator()},
                new Component[]{tooltipPane}
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);

        isTooltipShow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkBoxUse();
            }
        });
    }

    protected JPanel createTooltipPane(Plot plot) {
        borderPane = new VanChartBorderWithRadiusPane();
        backgroundPane = new VanChartBackgroundWithOutImagePane();
        initTooltipContentPane(plot);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p,p,p,p,p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{tooltipContentPane,null},
                new Component[]{new JSeparator(),null},
                new Component[]{createLabelStylePane(),null},
                new Component[]{new JSeparator(),null},
                new Component[]{borderPane,null},
                new Component[]{backgroundPane,null},
                new Component[]{createDisplayStrategy(plot),null},
        };

        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
    }

    private void initTooltipContentPane(Plot plot){
        if(plot.isMeterPlot()){
            GaugeStyle gaugeStyle = ((VanChartGaugePlot)plot).getGaugeStyle();
            switch (gaugeStyle){
                case POINTER:
                    tooltipContentPane = new VanChartTooltipContentPane(parent, VanChartPlotTooltipPane.this);
                    break;
                case POINTER_SEMI:
                    tooltipContentPane = new VanChartTooltipContentPane(parent, VanChartPlotTooltipPane.this);
                    break;
                default:
                    tooltipContentPane = new TooltipContentPaneWithOutSeries(parent, VanChartPlotTooltipPane.this);
                    break;
            }
        } else {
            tooltipContentPane = new VanChartTooltipContentPane(parent, VanChartPlotTooltipPane.this);
        }
    }

    protected JPanel createLabelStylePane() {
        style = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_Automatic"),Inter.getLocText("Plugin-ChartF_Custom")});
        textFontPane = new ChartTextAttrPane();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p,p};
        Component[][] components = new Component[][]{
                new Component[]{style,null},
                new Component[]{textFontPane,null},
        };

        initStyleListener();

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("FR-Designer-Widget_Style"), panel);
    }


    private void initStyleListener() {
        style.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkStyleUse();
            }
        });
    }

    protected JPanel createDisplayStrategy(Plot plot) {
        showAllSeries = new UIToggleButton(Inter.getLocText("Plugin-ChartF_ShowAllSeries"));
        followMouse = new UIButtonGroup(new String[]{Inter.getLocText("Plugin-ChartF_FollowMouse"),
                Inter.getLocText("Plugin-ChartF_NotFollowMouse")});
        JPanel panel = new JPanel(new BorderLayout(0,4));
        if(plot.isSupportTooltipSeriesType()){
            panel.add(showAllSeries, BorderLayout.NORTH);
        }
        panel.add(followMouse,BorderLayout.SOUTH);
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_DisplayStrategy"), panel);
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }

    private void checkAllUse() {
        checkBoxUse();
        checkStyleUse();
    }
    /**
     * ºÏ≤Èbox π”√.
     */
    private void checkBoxUse() {
        tooltipPane.setVisible(isTooltipShow.isSelected());
    }

    private void checkStyleUse() {
        textFontPane.setEnabled(style.getSelectedIndex() == 1);
    }

    public void populate(AttrTooltip attr) {
        if(attr == null) {
            attr = new AttrTooltip();
        }

        isTooltipShow.setSelected(attr.isEnable());
        tooltipContentPane.populate(attr.getContent());
        style.setSelectedIndex(attr.isCustom() ? 1 : 0);
        textFontPane.populate(attr.getTextAttr());
        borderPane.populate(attr.getGeneralInfo());
        backgroundPane.populate(attr.getGeneralInfo());
        showAllSeries.setSelected(attr.isShowMutiSeries());
        followMouse.setSelectedIndex(attr.isFollowMouse() ? 0 : 1);

        checkAllUse();
    }

    public AttrTooltip update() {
        AttrTooltip attrTooltip = new AttrTooltip();

        attrTooltip.setEnable(isTooltipShow.isSelected());
        attrTooltip.setContent(tooltipContentPane.update());
        attrTooltip.setCustom(style.getSelectedIndex() == 1);
        if(textFontPane != null){
            attrTooltip.setTextAttr(textFontPane.update());
        }
        borderPane.update(attrTooltip.getGeneralInfo());
        backgroundPane.update(attrTooltip.getGeneralInfo());
        attrTooltip.setShowMutiSeries(showAllSeries.isSelected());
        attrTooltip.setFollowMouse(followMouse.getSelectedIndex() == 0);

        return attrTooltip;
    }


}

