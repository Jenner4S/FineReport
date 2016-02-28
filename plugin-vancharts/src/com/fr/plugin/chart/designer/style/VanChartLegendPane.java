package com.fr.plugin.chart.designer.style;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.frpane.UIBubbleFloatPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.style.*;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.VanChartLegend;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.background.VanChartBackgroundWithOutImagePane;
import com.fr.plugin.chart.designer.component.VanChartFloatPositionPane;
import com.fr.plugin.chart.designer.component.border.VanChartBorderWithRadiusPane;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.stable.Constants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 属性表, 图表样式-图例 界面.
 */
public class VanChartLegendPane extends BasicScrollPane<VanChart> {
    private static final long serialVersionUID = 7553135492053931171L;

    private static final int WIDTH = 165;
    private static final int HEIGHT = 100;
    private static final int GAP = 20;

    private UICheckBox isLegendVisible;
    private JPanel legendPane;

    private UIButtonGroup<Integer> location;
    private ChartTextAttrPane textAttrPane;
    private VanChartBorderWithRadiusPane borderPane;
    private VanChartBackgroundWithOutImagePane backgroundPane;

    private UIToggleButton customFloatPositionButton;
    private VanChartFloatPositionPane customFloatPositionPane;
    private UIButtonGroup<Integer> limitSize;
    private UISpinner maxProportion;
    protected VanChartStylePane parent;


    public VanChartLegendPane(VanChartStylePane parent) {
        super();
        this.parent = parent;
    }

    private class ContentPane extends JPanel {
        private static final long serialVersionUID = 1614283200308877353L;

        public ContentPane() {
            initComponents();
        }

        private void initComponents(){
            isLegendVisible = new UICheckBox(Inter.getLocText("Chart-Legend_Is_Visible"));
            legendPane = createLegendPane();

            double p = TableLayout.PREFERRED;
            double f = TableLayout.FILL;
            double[] col = {f};
            double[] row = {p, p, p};
            Component[][] components = new Component[][]{
                    new Component[]{isLegendVisible},
                    new Component[]{new JSeparator()},
                    new Component[]{legendPane},
            };

            JPanel panel = TableLayoutHelper.createTableLayoutPane(components,row,col);
            this.setLayout(new BorderLayout());
            this.add(panel,BorderLayout.CENTER);

            isLegendVisible.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    checkBoxUse();
                }
            });
        }
    }

    private JPanel createLegendPane(){
        borderPane = new VanChartBorderWithRadiusPane();
        backgroundPane = new VanChartBackgroundWithOutImagePane();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = { p,p,p,p,p,p,p};

        Component[][] components = new Component[][]{
                new Component[]{createTitlePositionPane(new double[]{p,p},columnSize),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createTitleStylePane(),null} ,
                new Component[]{new JSeparator(),null},
                new Component[]{borderPane,null},
                new Component[]{backgroundPane,null},
                new Component[]{createDisplayStrategy(),null}
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    private JPanel createTitlePositionPane(double[] row, double[] col){
        String[] textArray = {Inter.getLocText("Chart-StyleAlignment_Top"), Inter.getLocText("Chart-StyleAlignment_Bottom"),
                Inter.getLocText("Chart-StyleAlignment_Left"), Inter.getLocText("Chart-StyleAlignment_Right"), Inter.getLocText("Chart-Right_Top")};
        Integer[] valueArray = {Constants.TOP, Constants.BOTTOM, Constants.LEFT, Constants.RIGHT, Constants.RIGHT_TOP};
        Icon[] iconArray = {BaseUtils.readIcon("/com/fr/design/images/chart/ChartLegend/layout_top.png"),
                BaseUtils.readIcon("/com/fr/design/images/chart/ChartLegend/layout_bottom.png"),
                BaseUtils.readIcon("/com/fr/design/images/chart/ChartLegend/layout_left.png"),
                BaseUtils.readIcon("/com/fr/design/images/chart/ChartLegend/layout_right.png"),
                BaseUtils.readIcon("/com/fr/design/images/chart/ChartLegend/layout_top_right.png")
        };

        location = new UIButtonGroup<Integer>(iconArray, valueArray);
        location.setAllToolTips(textArray);

        customFloatPositionButton = new UIToggleButton(Inter.getLocText("Plugin-ChartF_CustomFloatPosition"));
        customFloatPositionButton.setEventBannded(true);

        Component[][] components = new Component[][]{
                new Component[]{location,null},
                new Component[]{customFloatPositionButton,null}
        };

        customFloatPositionPane =  new VanChartFloatPositionPane();

        initPositionListener();

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components,row,col);
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Chart-Layout_Position"), panel);
    }

    private void initPositionListener(){

        location.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                customFloatPositionButton.setSelected(false);
                checkDisplayStrategyUse();
            }
        });

        customFloatPositionButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!customFloatPositionButton.isSelected()){
                    customFloatPositionButton.setSelected(true);
                    checkDisplayStrategyUse();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                location.setSelectedIndex(-1);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                if(customFloatPositionPane == null) {
                    customFloatPositionPane =  new VanChartFloatPositionPane();
                }
                Point comPoint = customFloatPositionButton.getLocationOnScreen();
                Point arrowPoint = new Point(comPoint.x + customFloatPositionButton.getWidth()/2 - GAP, comPoint.y + customFloatPositionButton.getHeight());
                UIBubbleFloatPane<Style> pane = new UIBubbleFloatPane(Constants.TOP, arrowPoint, customFloatPositionPane, WIDTH, HEIGHT) {
                    @Override
                    public void updateContentPane() {
                        parent.attributeChanged();
                    }
                };
                pane.show(VanChartLegendPane.this, null);
            }
        });
    }

    private JPanel createTitleStylePane(){
        textAttrPane = new ChartTextAttrPane();
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("FR-Designer-Widget_Style"), textAttrPane);
    }

    private JPanel createDisplayStrategy(){
        maxProportion = new UISpinner(0,100,1,30);
        limitSize = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_LimitAreaSize"),Inter.getLocText("Plugin-ChartF_NotLimitAreaSize")});
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f,p};
        double[] rowSize = {p,p};
        Component[][] components = new Component[][]{
                new Component[]{limitSize,null},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_LegendMaxProportion")+":"),maxProportion},
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);

        limitSize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkMaxProPortionUse();
            }
        });

        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_DisplayStrategy"), panel);
    }

    private void checkAllUse() {
        checkBoxUse();
        checkDisplayStrategyUse();
        this.repaint();
    }

    //检查显示策略界面是否可用
    private void checkDisplayStrategyUse() {
        limitSize.setEnabled(!customFloatPositionButton.isSelected());
        checkMaxProPortionUse();
    }

    //检查最大显示占比是否可用
    private void checkMaxProPortionUse() {
        maxProportion.setEnabled(limitSize.getSelectedIndex() == 0 && limitSize.isEnabled());
    }

    private void checkBoxUse() {
        isLegendVisible.setEnabled(true);
        legendPane.setVisible(isLegendVisible.isSelected());
    }

    /**
     * 标题
     * @return 标题
     */
    public String title4PopupWindow() {
        return PaneTitleConstants.CHART_STYLE_LEGNED_TITLE;
    }

    @Override
    protected JPanel createContentPane() {
        return new ContentPane();
    }

    @Override
    public void updateBean(VanChart chart) {
        if(chart == null) {
            return;
        }
        Plot plot = chart.getPlot();
        if(plot == null) {
            return;
        }
        VanChartLegend legend = (VanChartLegend)plot.getLegend();
        if(legend == null) {
            legend = new VanChartLegend();
        }
        legend.setLegendVisible(isLegendVisible.isSelected());
        legend.setFRFont(textAttrPane.updateFRFont());
        borderPane.update(legend);
        backgroundPane.update(legend);

        if(!customFloatPositionButton.isSelected()){
            legend.setPosition(location.getSelectedItem());
        } else {
            legend.setPosition(-1);
        }
        legend.setFloating(customFloatPositionButton.isSelected());
        legend.setLimitSize(limitSize.getSelectedIndex() == 0);
        legend.setMaxHeight(maxProportion.getValue());
        legend.setFloatPercentX(customFloatPositionPane.getFloatPosition_x());
        legend.setFloatPercentY(customFloatPositionPane.getFloatPosition_y());
    }

    @Override
    public void populateBean(VanChart chart) {
        if(chart != null && chart.getPlot() != null) {
            VanChartLegend legend = (VanChartLegend)chart.getPlot().getLegend();
            if (legend != null) {
                isLegendVisible.setSelected(legend.isLegendVisible());
                textAttrPane.populate(legend.getFRFont());
                borderPane.populate(legend);
                backgroundPane.populate(legend);
                if(!legend.isFloating()){
                    location.setSelectedItem(legend.getPosition());
                }
                customFloatPositionButton.setSelected(legend.isFloating());
                customFloatPositionPane.setFloatPosition_x(legend.getFloatPercentX());
                customFloatPositionPane.setFloatPosition_y(legend.getFloatPercentY());
                limitSize.setSelectedIndex(legend.isLimitSize() ? 0 : 1);
                maxProportion.setValue(legend.getMaxHeight());
            }
        }

        checkAllUse();
    }
}

