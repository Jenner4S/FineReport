package com.fr.plugin.chart.designer.other;

import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.web.ChartHyperPoplink;
import com.fr.chart.web.ChartHyperRelateCellLink;
import com.fr.chart.web.ChartHyperRelateFloatLink;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.javascript.ChartEmailPane;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.frpane.UICorrelationComboBoxPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.js.*;
import com.fr.plugin.chart.attr.VanChartTools;
import com.fr.plugin.chart.attr.VanChartZoom;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.base.VanChartConstants;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.stable.StableUtils;
import com.fr.stable.bridge.StableFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class VanChartInteractivePane extends BasicScrollPane<Chart> {

    private static final long serialVersionUID = 8135452818502145597L;

    protected UICheckBox isSort;
    protected UICheckBox exportImages;
    protected UICheckBox fullScreenDisplay;
    protected UIToggleButton collapse;

    protected UIToggleButton isChartAnimation;

    protected UIButtonGroup monitorRefresh;
    protected UISpinner autoRefreshTime;

    private UIButtonGroup zoomWidget;
    private UIButtonGroup zoomResize;
    private TinyFormulaPane from;
    private TinyFormulaPane to;
    private UIButtonGroup<String> zoomType;

    protected UICorrelationComboBoxPane superLink;

    protected Chart chart;
    protected JPanel interactivePane;

    /**
     * 界面标题.
     * @return 返回标题.
     */
    public String title4PopupWindow() {
        return Inter.getLocText("Chart-Interactive_Tab");
    }

    @Override
    protected JPanel createContentPane() {
        if(chart == null) {
            return new JPanel();
        }
        interactivePane = getInteractivePane();
        return interactivePane;

    }

    protected JPanel getInteractivePane(){
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p,p,p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{createToolBarPane(rowSize, columnSize),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createAnimationPane(),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createAutoRefreshPane(null),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createHyperlinkPane(),null}
        };

        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
    }

    private void reLayoutContentPane(VanChartPlot plot){
        interactivePane.removeAll();
        interactivePane = getInteractivePaneWithZoomPane(plot);
        reloaPane(interactivePane);
    }

    private JPanel getInteractivePaneWithZoomPane(VanChartPlot plot){
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p,p,p,p,p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{createToolBarPane(new double[]{p,p,p,p,p}, columnSize),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createAnimationPane(),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createZoomPane(new double[]{p,p,p}, columnSize, plot),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createAutoRefreshPane(plot),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createHyperlinkPane(),null}
        };

        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
    }

    private JPanel createZoomPane(double[] row, double[] col, VanChartPlot plot){
        zoomWidget = new UIButtonGroup(new String[]{Inter.getLocText("Plugin-ChartF_Open"), Inter.getLocText("Plugin-ChartF_Close")});
        zoomResize = new UIButtonGroup(new String[]{Inter.getLocText("Plugin-ChartF_Open"), Inter.getLocText("Plugin-ChartF_Close")});
        from = new TinyFormulaPane();
        to = new TinyFormulaPane();
        String[] nameArray = new String[]{Inter.getLocText("ChartF-X_Axis"), Inter.getLocText("ChartF-Y_Axis"),Inter.getLocText("Plugin-ChartF_XYAxis"),Inter.getLocText("Chart-Use_None")};
        String[] valueArray = new String[]{VanChartConstants.ZOOM_TYPE_X, VanChartConstants.ZOOM_TYPE_Y, VanChartConstants.ZOOM_TYPE_XY, VanChartConstants.ZOOM_TYPE_NONE};
        zoomType = new UIButtonGroup(nameArray, valueArray);

        JPanel zoomWidgetPane = TableLayout4VanChartHelper.createTableLayoutPaneWithSmallTitle(Inter.getLocText("Plugin-ChartF_ZoomWidget"),zoomWidget);

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Change")), zoomResize},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_From")),from},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_To")),to},
        };
        JPanel temp = TableLayoutHelper.createTableLayoutPane(components, row, col);
        JPanel changeEnablePane = TableLayout4VanChartHelper.createTableLayoutPaneWithSmallTitle(Inter.getLocText("Plugin-ChartF_WidgetBoundary"),temp);

        JPanel zoomTypePane = TableLayout4VanChartHelper.createTableLayoutPaneWithSmallTitle(Inter.getLocText("Plugin-ChartF_ZoomType"),zoomType);

        JPanel panel = new JPanel(new BorderLayout(0, 4));
        if(plot.isSupportZoomCategoryAxis()){//支持缩放控件
            panel.add(zoomWidgetPane, BorderLayout.NORTH);
            panel.add(changeEnablePane, BorderLayout.CENTER);
        }
        panel.add(zoomTypePane, BorderLayout.SOUTH);

        zoomWidget.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkZoomPane();
            }
        });
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Chart-Use_Zoom"), panel);
    }

    protected JPanel createToolBarPane(double[] row, double[] col){
        isSort = new UICheckBox(Inter.getLocText("Plugin-ChartF_Sort"));
        exportImages = new UICheckBox(Inter.getLocText("Plugin-ChartF_ExportImage"));
        fullScreenDisplay = new UICheckBox(Inter.getLocText("Plugin-ChartF_FullScreenDisplay"));
        collapse = new UIToggleButton(Inter.getLocText("Plugin-ChartF_Collapse"));

        Component[][] components = new Component[][]{
                new Component[]{isSort,null},
                new Component[]{exportImages,null},
                new Component[]{fullScreenDisplay,null},
                new Component[]{collapse,null},
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, row, col);
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_ToolBar"), panel);
    }

    protected JPanel createAnimationPane(){
        isChartAnimation = new UIToggleButton(Inter.getLocText("Plugin-ChartF_OpenAnimation"));
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_Animation"), isChartAnimation);
    }

    protected JPanel createAutoRefreshPane(VanChartPlot plot){
        monitorRefresh = new UIButtonGroup(new String[]{Inter.getLocText("Plugin-ChartF_CommonModel"), Inter.getLocText("Plugin-ChartF_MonitorModel")});
        autoRefreshTime = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
        JPanel autoRefreshPane = GUICoreUtils.createFlowPane(new Component[]{
                new UILabel(Inter.getLocText("Chart-Time_Interval")),
                autoRefreshTime,
                new UILabel(Inter.getLocText("Chart-Time_Seconds"))
        }, FlowLayout.LEFT);

        JPanel panel = new JPanel(new BorderLayout(0, 4));
//        if(plot != null && plot.isSupportMonitorRefresh()){
//            panel.add(monitorRefresh, BorderLayout.NORTH);
//        }
        panel.add(autoRefreshPane, BorderLayout.CENTER);

        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText(new String[]{"Chart-Use_Auto", "Chart-Use_Refresh"}), panel);
    }

    protected JPanel createHyperlinkPane() {
        superLink = new UICorrelationComboBoxPane();
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("M_Insert-Hyperlink"), superLink);
    }

    private void checkZoomPane() {
        boolean zoomWidgetEnabled = zoomWidget.getSelectedIndex() == 0;
        zoomResize.setEnabled(zoomWidgetEnabled);
        GUICoreUtils.setEnabled(from, zoomWidgetEnabled);
        GUICoreUtils.setEnabled(to, zoomWidgetEnabled);
        zoomType.setEnabled(!zoomWidgetEnabled);
    }

    @Override
    public void populateBean(Chart chart) {
        if (chart == null || chart.getPlot() == null) {
            return;
        }
        this.chart = chart;

        if(interactivePane == null){
            this.remove(leftcontentPane);
            layoutContentPane();
        }

        VanChartPlot plot = (VanChartPlot)chart.getPlot();

        if(plot.isSupportZoomDirection()){//支持缩放方向=方向+控件
            reLayoutContentPane(plot);
            populateChartZoom((VanChart)chart);
            checkZoomPane();
        }

        populateChartTools((VanChart)chart);
        populateChartAnimate(chart, plot);
        populateAutoRefresh(chart);
        populateHyperlink(plot);
    }

    private void populateChartTools(VanChart chart) {
        VanChartTools vanChartTools = chart.getVanChartTools();
        isSort.setSelected(vanChartTools.isSort());
        exportImages.setSelected(vanChartTools.isExport());
        fullScreenDisplay.setSelected(vanChartTools.isFullScreen());
        collapse.setSelected(vanChartTools.isHidden());
    }

    private void populateChartZoom(VanChart chart) {
        VanChartZoom zoom = chart.getVanChartZoom();
        if(zoom == null){
            zoom = new VanChartZoom();
        }
        zoomWidget.setSelectedIndex(zoom.isZoomVisible() ? 0 : 1);
        zoomResize.setSelectedIndex(zoom.isZoomResize() ? 0 : 1);
        if (zoom.getFrom() instanceof Formula) {
            from.populateBean(((Formula) zoom.getFrom()).getContent());
        } else {
            from.populateBean(Utils.objectToString(zoom.getFrom()));
        }
        if (zoom.getTo() instanceof Formula) {
            to.populateBean(((Formula) zoom.getTo()).getContent());
        } else {
            to.populateBean(Utils.objectToString(zoom.getTo()));
        }
        zoomType.setSelectedItem(zoom.getZoomType());
    }

    private void populateChartAnimate(Chart chart, Plot plot) {
        if(plot.isSupportAnimate()) {
            isChartAnimation.setSelected(chart.isJSDraw());
        }
    }

    protected void populateAutoRefresh(Chart chart) {
        VanChartPlot plot = (VanChartPlot)chart.getPlot();
        if(plot.isSupportAutoRefresh()) {
            autoRefreshTime.setValue(plot.getAutoRefreshPerSecond());
        }
        if(plot.isSupportMonitorRefresh()){
            monitorRefresh.setSelectedIndex(plot.isMonitorRefresh() ? 1 : 0);
        }
    }

    private void populateHyperlink(Plot plot) {
        HashMap paneMap = HyperlinkMapFactory.getHyperlinkMap(plot);

        java.util.List<UIMenuNameableCreator> list = refreshList(paneMap);
        superLink.refreshMenuAndAddMenuAction(list);

        java.util.List<UIMenuNameableCreator> hyperList = new ArrayList<UIMenuNameableCreator>();
        NameJavaScriptGroup nameGroup = plot.getHotHyperLink();
        for(int i = 0; nameGroup != null &&  i < nameGroup.size(); i++) {
            NameJavaScript javaScript = nameGroup.getNameHyperlink(i);
            if(javaScript != null && javaScript.getJavaScript() != null) {
                JavaScript script = javaScript.getJavaScript();
                hyperList.add(new UIMenuNameableCreator(javaScript.getName(), script, getUseMap(paneMap, script.getClass())));
            }
        }

        superLink.populateBean(hyperList);
        superLink.doLayout();
    }

    @Override
    public void updateBean(Chart chart) {
        if (chart == null || chart.getPlot() == null) {
            return;
        }

        VanChartPlot plot = (VanChartPlot)chart.getPlot();

        if(plot.isSupportZoomDirection()){
            updateChartZoom((VanChart)chart);
        }
        updateChartTools((VanChart)chart);
        updateChartAnimate(chart, plot);
        updateAutoRefresh(plot);
        updateHyperlink(plot);
    }

    private void updateChartTools(VanChart chart) {
        VanChartTools vanChartTools = new VanChartTools();
        vanChartTools.setExport(exportImages.isSelected());
        vanChartTools.setFullScreen(fullScreenDisplay.isSelected());
        vanChartTools.setSort(isSort.isSelected());
        vanChartTools.setHidden(collapse.isSelected());
        chart.setVanChartTools(vanChartTools);
    }

    private void updateChartZoom(VanChart chart) {
        VanChartZoom zoom = chart.getVanChartZoom();
        if(zoom == null){
            zoom = new VanChartZoom();
            chart.setVanChartZoom(zoom);
        }
        zoom.setZoomVisible(zoomWidget.getSelectedIndex() == 0);
        zoom.setZoomResize(zoomResize.getSelectedIndex() == 0);
        String fromString = from.updateBean();
        Object fromObject;
        if (StableUtils.maybeFormula(fromString)) {
            fromObject = new Formula(fromString);
        } else {
            fromObject = fromString;
        }
        zoom.setFrom(fromObject);
        String toString = to.updateBean();
        Object toObject;
        if (StableUtils.maybeFormula(toString)) {
            toObject = new Formula(toString);
        } else {
            toObject = toString;
        }
        zoom.setTo(toObject);
        zoom.setZoomType(zoomType.getSelectedItem());
    }

    private void updateChartAnimate(Chart chart, Plot plot) {
        if(plot.isSupportAnimate()) {
            chart.setJSDraw(isChartAnimation.isSelected());
        }
    }


    private void updateAutoRefresh(VanChartPlot plot) {
        if(plot.isSupportAutoRefresh()) {
            plot.setAutoRefreshPerSecond((int) autoRefreshTime.getValue());
        }
        if(plot.isSupportMonitorRefresh()){
            plot.setMonitorRefresh(monitorRefresh.getSelectedIndex() == 1);
        }
    }

    private void updateHyperlink(Plot plot) {
        NameJavaScriptGroup nameGroup = new NameJavaScriptGroup();
        nameGroup.clear();

        superLink.resetItemName();
        java.util.List list = superLink.updateBean();
        for(int i = 0; i < list.size(); i++) {
            UIMenuNameableCreator menu = (UIMenuNameableCreator)list.get(i);
            NameJavaScript nameJava = new NameJavaScript(menu.getName(), (JavaScript)menu.getObj());
            nameGroup.addNameHyperlink(nameJava);
        }
        plot.setHotHyperLink(nameGroup);
    }


    protected Class<? extends BasicBeanPane> getUseMap(HashMap map, Object key) {
        if(map.get(key) != null){
            return (Class<? extends BasicBeanPane>)map.get(key);
        }
        //引擎在这边放了个provider,当前表单对象
        for(Object tempKey : map.keySet()){
            if(((Class)tempKey).isAssignableFrom((Class)key)){
                return (Class<? extends BasicBeanPane>)map.get(tempKey);
            }
        }
        return null;
    }

    protected java.util.List<UIMenuNameableCreator> refreshList(HashMap map) {
        java.util.List<UIMenuNameableCreator> list = new ArrayList<UIMenuNameableCreator>();

        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Reportlet"),
                new ReportletHyperlink(), getUseMap(map, ReportletHyperlink.class)));
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Mail"), new EmailJavaScript(), ChartEmailPane.class));
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Web"),
                new WebHyperlink(), getUseMap(map, WebHyperlink.class)));
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Dynamic_Parameters"),
                new ParameterJavaScript(), getUseMap(map, ParameterJavaScript.class)));
        list.add(new UIMenuNameableCreator("JavaScript", new JavaScriptImpl(), getUseMap(map, JavaScriptImpl.class)));

        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Float_Chart"),
                new ChartHyperPoplink(), getUseMap(map, ChartHyperPoplink.class)));
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Cell"),
                new ChartHyperRelateCellLink(), getUseMap(map, ChartHyperRelateCellLink.class)));
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Float"),
                new ChartHyperRelateFloatLink(), getUseMap(map, ChartHyperRelateFloatLink.class)));

        FormHyperlinkProvider hyperlink = StableFactory.getMarkedInstanceObjectFromClass(FormHyperlinkProvider.XML_TAG, FormHyperlinkProvider.class);
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Form"),
                hyperlink, getUseMap(map, FormHyperlinkProvider.class)));

        return list;
    }


    @Override
    public Chart updateBean() {
        return null;
    }

    /**
     * 组件是否需要响应添加的观察者事件
     *
     * @return 如果需要响应观察者事件则返回true，否则返回false
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }
}

