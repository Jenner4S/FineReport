package com.fr.plugin.chart.designer.style.label;

import com.fr.chart.chartattr.Plot;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.plot.VanChartLabelPositionPlot;
import com.fr.plugin.chart.base.AttrLabelDetail;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.VanChartTooltipContentPane;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.stable.Constants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Mitisky on 15/12/7.
 */
public class VanChartPlotLabelDetailPane extends BasicPane {
    private static final long serialVersionUID = -22438250307946275L;

    protected VanChartTooltipContentPane dataLabelContentPane;

    protected UIButtonGroup<Integer> position;
    protected UIToggleButton tractionLine;

    protected UIButtonGroup<Integer> style;
    protected ChartTextAttrPane textFontPane;

    protected ColorSelectBox backgroundColor;

    protected VanChartStylePane parent;

    public VanChartPlotLabelDetailPane(Plot plot, VanChartStylePane parent) {
        this.parent = parent;
        this.setLayout(new BorderLayout());
        initToolTipContentPane(plot);
        JPanel contentPane = createLabelPane(plot);
        this.add(contentPane,BorderLayout.CENTER);
    }

    protected void initToolTipContentPane(Plot plot) {
        dataLabelContentPane = new VanChartTooltipContentPane(parent, VanChartPlotLabelDetailPane.this);
    }

    private JPanel createLabelPane(Plot plot) {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = getLabelPaneRowSize(plot, p);

        Component[][] components = getLabelPaneComponents(plot, p, columnSize);
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    protected Component[][] getLabelPaneComponents(Plot plot, double p, double[] columnSize) {
        if(hasLabelPosition(plot)){
            return new Component[][]{
                    new Component[]{dataLabelContentPane,null},
                    new Component[]{new JSeparator(),null},
                    new Component[]{createLabelPositionPane(new double[]{p,p}, columnSize, plot),null},
                    new Component[]{new JSeparator(),null},
                    new Component[]{createLabelStylePane(new double[]{p,p}, columnSize, plot),null},
            };
        } else {
            return  new Component[][]{
                    new Component[]{dataLabelContentPane,null},
                    new Component[]{new JSeparator(),null},
                    new Component[]{createLabelStylePane(new double[]{p,p}, columnSize, plot),null},
            };
        }
    }

    protected double[] getLabelPaneRowSize(Plot plot, double p) {
        return hasLabelPosition(plot) ? new double[]{p,p,p,p,p} : new double[]{p,p,p};
    }

    protected boolean hasLabelPosition(Plot plot) {
        return plot instanceof VanChartLabelPositionPlot;
    }

    protected JPanel createTableLayoutPaneWithTitle(String title, Component component) {
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(title, component);
    }

    protected JPanel createLabelPositionPane(double[] row, double[] col, Plot plot) {
        if(plot instanceof VanChartLabelPositionPlot){
            position = new UIButtonGroup<Integer>(((VanChartLabelPositionPlot) plot).getLabelLocationNameArray(), ((VanChartLabelPositionPlot) plot).getLabelLocationValueArray());
            if(plot.isSupportLeadLine()){
                tractionLine = new UIToggleButton(Inter.getLocText("ChartF-Show_GuidLine"));

                Component[][] components = new Component[][]{
                        new Component[]{position,null},
                        new Component[]{tractionLine,null},
                };

                initPositionListener();

                JPanel panel = TableLayoutHelper.createTableLayoutPane(components,row,col);
                return createTableLayoutPaneWithTitle(Inter.getLocText("Chart-Layout_Position"), panel);
            } else {
                return createTableLayoutPaneWithTitle(Inter.getLocText("Chart-Layout_Position"), position);
            }
        }
        return new JPanel();
    }

    protected void initPositionListener() {
        position.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkPosition();
            }
        });
    }

    protected JPanel createLabelStylePane(double[] row, double[] col, Plot plot) {
        style = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_Automatic"),
                Inter.getLocText("Plugin-ChartF_Custom")});
        textFontPane = new ChartTextAttrPane();

        initStyleListener();

        JPanel panel = TableLayoutHelper.createTableLayoutPane(getLabelStyleComponents(plot),row,col);
        return createTableLayoutPaneWithTitle(Inter.getLocText("FR-Designer-Widget_Style"), panel);
    }

    protected Component[][] getLabelStyleComponents(Plot plot) {
        return new Component[][]{
                new Component[]{style,null},
                new Component[]{textFontPane,null},
        };
    }

    private void initStyleListener() {
        style.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkStyleUse();
            }
        });
    }

    protected JPanel createBackgroundColorPane() {
        backgroundColor = new ColorSelectBox(100);
        return createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_Background"), backgroundColor);
    }

    protected String title4PopupWindow() {
        return null;
    }

    private void checkAllUse() {
        checkStyleUse();
        if(tractionLine == null){
            return;
        }
        checkPositionEnabled();
    }

    private void checkStyleUse() {
        textFontPane.setEnabled(style.getSelectedIndex() == 1);
    }

    private void checkPosition() {
        tractionLine.setSelected(position.getSelectedItem() == Constants.OUTSIDE);
        checkPositionEnabled();
    }
    private void checkPositionEnabled() {
        tractionLine.setEnabled(position.getSelectedItem() == Constants.OUTSIDE);
    }

    public void populate(AttrLabelDetail detail) {
        dataLabelContentPane.populate(detail.getContent());
        if(position != null){
            position.setSelectedItem(detail.getPosition());
        }
        if(tractionLine != null){
            tractionLine.setSelected(detail.isShowGuidLine());
        }
        style.setSelectedIndex(detail.isCustom() ? 1 : 0);
        textFontPane.populate(detail.getTextAttr());

        if(backgroundColor != null){
            backgroundColor.setSelectObject(detail.getBackgroundColor());
        }

        checkAllUse();
    }

    public void update(AttrLabelDetail detail) {
        detail.setContent(dataLabelContentPane.update());
        if(position != null && position.getSelectedItem() != null){
            detail.setPosition(position.getSelectedItem());
        } else if(position != null){
            position.setSelectedItem(detail.getPosition());
        }
        if(tractionLine != null){
            detail.setShowGuidLine(tractionLine.isSelected() && detail.getPosition() == Constants.OUTSIDE);
        }
        detail.setCustom(style.getSelectedIndex() == 1);
        if(textFontPane != null){
            detail.setTextAttr(textFontPane.update());
        }
        if(backgroundColor != null){
            detail.setBackgroundColor(backgroundColor.getSelectObject());
        }
    }

}
