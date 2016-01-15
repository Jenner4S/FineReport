package com.fr.design.mainframe.chart.gui.type;

import com.fr.base.ChartPreStyleManagerProvider;
import com.fr.base.ChartPreStyleServerManager;
import com.fr.base.FRContext;
import com.fr.base.background.ColorBackground;
import com.fr.chart.base.*;
import com.fr.chart.chartattr.*;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.charttypes.BarIndependentChart;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.chart.series.PlotStyle.ChartSelectDemoPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.js.NameJavaScriptGroup;
import com.fr.stable.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractChartTypePane extends FurtherBasicBeanPane<Chart>{

    protected static final int STYLE_SHADE = 0;
    protected static final int STYLE_TRANSPARENT = 1;
    protected static final int STYLE_PLANE3D = 2;
    protected static final int STYLE_HIGHLIGHT =  3;

    protected static final int BAIDU = 0;
    protected static final int GOOGLE = 1;

    protected List<ChartImagePane> typeDemo;
    protected List<ChartImagePane> styleList;

    protected JPanel stylePane; //��ʽ���ֵ����
    protected abstract List<ChartImagePane> initDemoList();
    protected abstract List<ChartImagePane> initStyleList();
    protected abstract String[] getTypeIconPath();
    protected abstract String[] getTypeLayoutPath();
    protected int lastStyleIndex = -1;
    protected int lastTypeIndex = -1;
    protected boolean typeChanged = false;//ͼ�������Ƿ����仯

    /**
     * ���ַ��Ĳ���
     * @return ͼƬ��list
     */
    protected List<ChartImagePane> initNormalStyleList(){
        List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
        String shade = Inter.getLocText("FR-Chart-Style_TopDownShade");
        String transparent = Inter.getLocText("FR-Chart-Style_Transparent");
        String plane3D = Inter.getLocText("FR-Chart-Style_Plane3D");
        String highLight = Inter.getLocText("FR-Chart-Style_GradientHighlight");
        String[] layoutPaths = getTypeLayoutPath();

        ChartImagePane pane = new ChartImagePane(layoutPaths[0], shade);
        demoList.add(pane);

        demoList.add(new ChartImagePane(layoutPaths[1], transparent));
        demoList.add(new ChartImagePane(layoutPaths[2], plane3D));
        demoList.add(new ChartImagePane(layoutPaths[3], highLight));
        return demoList;
    }

    public AbstractChartTypePane() {
        double vs = 4;
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        Component[][] components;

        typeDemo = initDemoList();
        styleList = initStyleList();

        checkDemosBackground();

        JPanel typePane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(4);
        for(int i = 0; i < typeDemo.size(); i++) {
            ChartImagePane tmp = typeDemo.get(i);
            typePane.add(tmp);
            tmp.setDemoGroup(typeDemo.toArray(new ChartSelectDemoPane[typeDemo.size()]));
        }

        JPanel layoutPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(4);
        if(styleList != null && !styleList.isEmpty()) {
            for(int i = 0; i < styleList.size(); i ++) {
                ChartImagePane tmp = styleList.get(i);
                layoutPane.add(tmp);
                tmp.setDemoGroup(styleList.toArray(new ChartSelectDemoPane[styleList.size()]));
            }
        }

        double[] columnSize = { p, vs, f };
        double[] rowSize = { p,p,p,p,p,p,p};

        components = new Component[][]{
                new Component[]{typePane}
        };

        if(styleList != null && !styleList.isEmpty()) {
            Component[][] styleComp = new Component[][]{
                    new Component[]{new JSeparator()},
                    new Component[]{new BoldFontTextLabel(Inter.getLocText("FR-Chart_Layout"))},
                    new Component[]{layoutPane},
            };
            stylePane = TableLayoutHelper.createTableLayoutPane(styleComp,rowSize,columnSize);
            stylePane.setVisible(false);

            components = new Component[][]{
                    new Component[]{typePane},
                    new Component[]{stylePane}
            };
        }

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
    }

    //���า��
    protected Plot getSelectedClonedPlot(){
        return null;
    }

    protected void checkTypeChange(){
        if(styleList != null && !styleList.isEmpty()){
            for(int i = 0; i < typeDemo.size(); i++){
                if(typeDemo.get(i).isPressing && i != lastTypeIndex){
                    typeChanged = true;
                    lastTypeIndex = i;
                    break;
                }
                typeChanged = false;
            }
        }
    }
    /**
     * ���½�������
     */
    public void populateBean(Chart chart) {
        for(ChartImagePane imagePane : typeDemo) {
            imagePane.isPressing = false;
        }
        for(ChartImagePane imagePane : styleList) {
            imagePane.isPressing = false;
        }

        if(styleList != null && !styleList.isEmpty()){
            int plotStyle = chart.getPlot().getPlotStyle();
            String styleName = chart.getPlot().getPlotFillStyle().getFillStyleName();

            switch (plotStyle){
                case ChartConstants.STYLE_SHADE: if(ComparatorUtils.equals(Inter.getLocText("FR-Chart-Style_Retro"), styleName)){
                    styleList.get(STYLE_SHADE).isPressing = true;
                    lastStyleIndex = STYLE_SHADE;
                }
                    break;
                case ChartConstants.STYLE_TRANSPARENT:if(ComparatorUtils.equals(Inter.getLocText("FR-Chart-Style_Fresh"), styleName)){
                    styleList.get(STYLE_TRANSPARENT).isPressing = true;
                    lastStyleIndex = STYLE_TRANSPARENT;
                }
                    break;
                case ChartConstants.STYLE_3D: if(ComparatorUtils.equals(Inter.getLocText("FR-Chart-Style_Bright"), styleName)){
                    styleList.get(STYLE_PLANE3D).isPressing = true;
                    lastStyleIndex = STYLE_PLANE3D;
                }
                    break;
                case ChartConstants.STYLE_OUTER:if(ComparatorUtils.equals(Inter.getLocText("FR-Chart-Style_Bright"), styleName)){
                    styleList.get(STYLE_HIGHLIGHT).isPressing = true;
                    lastStyleIndex = STYLE_HIGHLIGHT;
                }
                    break;
                default:
                    lastStyleIndex = -1;
                    break;
            }
            stylePane.setVisible(!(chart.getPlot().isSupport3D()));
            this.repaint();
        }
    }

    protected void checkDemosBackground() {
        if(this.styleList != null && !styleList.isEmpty()){
            for(int i = 0; i < styleList.size(); i++) {
                styleList.get(i).checkBackground();
                styleList.get(i).repaint();
            }
        }

        for(int i = 0; i < typeDemo.size(); i++) {
            typeDemo.get(i).checkBackground();
            typeDemo.get(i).repaint();
        }
    }

    private void setPlotFillStyle(Chart chart){
        ChartPreStyleManagerProvider manager = ChartPreStyleServerManager.getProviderInstance();
        Plot plot = chart.getPlot();
        Object preStyle = null;
        String name = "";
        if(styleList.get(STYLE_SHADE).isPressing){
            name = Inter.getLocText("FR-Chart-Style_Retro");
            preStyle = manager.getPreStyle(name);
        }else if(styleList.get(STYLE_TRANSPARENT).isPressing){
            name = Inter.getLocText("FR-Chart-Style_Fresh");
            preStyle = manager.getPreStyle(name);
        }else if(styleList.get(STYLE_PLANE3D).isPressing){
            name = Inter.getLocText("FR-Chart-Style_Bright");
            preStyle = manager.getPreStyle(name);
        }else if(styleList.get(STYLE_HIGHLIGHT).isPressing){
            name = Inter.getLocText("FR-Chart-Style_Bright");
            preStyle = manager.getPreStyle(name);
        }
        if(preStyle == null){
            plot.getPlotFillStyle().setColorStyle(ChartConstants.COLOR_DEFAULT);
        }else{
            AttrFillStyle fillStyle = ((ChartPreStyle)preStyle).getAttrFillStyle();
            fillStyle.setFillStyleName(name);
            plot.setPlotFillStyle(fillStyle);
        }
    }

    /**
     * ������,��ѡ�еķ��������
     */
    public void updateBean(Chart chart) {
        checkTypeChange();//�ж�ͼ��������Ƿ����仯
        Plot newPlot = this.setSelectedClonedPlotWithCondition(chart.getPlot());
        if(styleList != null && !styleList.isEmpty()){
            if(styleList.get(STYLE_SHADE).isPressing && lastStyleIndex != STYLE_SHADE){
                lastStyleIndex = STYLE_SHADE;
                chart.setPlot(newPlot);
                chart.getPlot().setPlotStyle(ChartConstants.STYLE_SHADE);
                resetChart(chart);
                createCondition4Shade(chart);
                setPlotFillStyle(chart);
            }else if(styleList.get(STYLE_TRANSPARENT).isPressing && lastStyleIndex != STYLE_TRANSPARENT){
                lastStyleIndex = STYLE_TRANSPARENT;
                chart.setPlot(newPlot);
                chart.getPlot().setPlotStyle(ChartConstants.STYLE_TRANSPARENT);
                resetChart(chart);
                createCondition4Transparent(chart);
                setPlotFillStyle(chart);
            }else if(styleList.get(STYLE_PLANE3D).isPressing && lastStyleIndex != STYLE_PLANE3D){
                lastStyleIndex = STYLE_PLANE3D;
                chart.setPlot(newPlot);
                chart.getPlot().setPlotStyle(ChartConstants.STYLE_3D);
                resetChart(chart);
                createCondition4Plane3D(chart);
                setPlotFillStyle(chart);
            }else if(styleList.get(STYLE_HIGHLIGHT).isPressing && lastStyleIndex != STYLE_HIGHLIGHT){
                lastStyleIndex = STYLE_HIGHLIGHT;
                chart.setPlot(newPlot);
                chart.getPlot().setPlotStyle(ChartConstants.STYLE_OUTER);
                resetChart(chart);
                createCondition4HighLight(chart);
                setPlotFillStyle(chart);
            }else if(lastStyleIndex >= STYLE_SHADE && lastStyleIndex <= STYLE_HIGHLIGHT){
                if(styleList.get(lastStyleIndex).isDoubleClicked || typeChanged){
                    chart.setPlot(newPlot);
                    resetChart(chart);
                    styleList.get(lastStyleIndex).isPressing = false;
                    checkDemosBackground();
                    lastStyleIndex = -1;
                }
            }

            stylePane.setVisible(!(chart.getPlot().isSupport3D()));
            this.repaint();
        }
    }

    private Plot setSelectedClonedPlotWithCondition(Plot oldPlot){
        Plot newPlot = getSelectedClonedPlot();
        if(oldPlot != null && ComparatorUtils.equals(newPlot.getClass(), oldPlot.getClass())){
            if(oldPlot.getHotHyperLink() != null){
                NameJavaScriptGroup hotHyper = oldPlot.getHotHyperLink();
                try {
                    newPlot.setHotHyperLink((NameJavaScriptGroup)hotHyper.clone());
                } catch (CloneNotSupportedException e) {
                    FRContext.getLogger().error("Error in Hyperlink, Please Check it.", e);
                }
            }
            newPlot.setConditionCollection(oldPlot.getConditionCollection());
            newPlot.setSeriesDragEnable(oldPlot.isSeriesDragEnable());
            if(newPlot.isSupportZoomCategoryAxis() && newPlot.getxAxis() != null) {
                newPlot.getxAxis().setZoom(oldPlot.getxAxis().isZoom());
            }
            if(newPlot.isSupportTooltipInInteractivePane()) {
                newPlot.setHotTooltipStyle(oldPlot.getHotTooltipStyle());
            }

            if(newPlot.isSupportAutoRefresh()) {
                newPlot.setAutoRefreshPerSecond(oldPlot.getAutoRefreshPerSecond());
            }

            if (newPlot.isSupportAxisTip()) {
                newPlot.setInteractiveAxisTooltip(oldPlot.isInteractiveAxisTooltip());
            }
        }
        return newPlot;
    }

    /**
     * ���������µ�Chart����
     */
    public Chart updateBean() {
        return null;
    }

    //ͼ�����������
    protected void resetChart(Chart chart){
        chart.setTitle(new Title(chart.getTitle().getTextObject()));
        chart.setBorderStyle(Constants.LINE_NONE);
        chart.setBorderColor(new Color(150, 150, 150));
        chart.setBackground(null);
    }

    protected void changePlotWithClone(Chart chart, Plot plot) {
        try {
            chart.switchPlot((Plot)plot.clone());
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error in change plot");
        }
    }

    /**
     * ����
     */
    public void reset() {
    }

    public boolean accept(Object ob) {
        return ob instanceof Chart;
    }

        //�߹⽥���Ĭ����������
    private void createCondition4HighLight(Chart chart){
        if(chart != null){
            //����
            Title title = new Title(chart.getTitle().getTextObject());
            chart.setTitle(title);
            title.setTitleVisble(true);
            TextAttr textAttr = title.getTextAttr();
            if (textAttr == null) {
                textAttr = new TextAttr();
                title.setTextAttr(textAttr);
            }
            title.setPosition(Constants.LEFT);
            textAttr.setFRFont(FRFont.getInstance("Microsoft YaHei", Font.BOLD, 16f, new Color(51, 51, 51)));

            //ͼ��
            Legend legend = new Legend();
            legend.setFRFont(FRFont.getInstance("SimSun", Font.PLAIN, 9f, new Color(138,140,139)));
            legend.setPosition(Constants.RIGHT_TOP);
            chart.getPlot().setLegend(legend);

            //������,����ֻ������ͼ������ͼ�����ͼ
            if(chart.getPlot() instanceof CategoryPlot){
                CategoryPlot plot = (CategoryPlot)chart.getPlot();

                //����������
                Axis cateAxis = plot.getxAxis();
                cateAxis.setAxisStyle(Constants.LINE_THICK);
                cateAxis.setAxisColor(new Color(204, 220, 228));
                cateAxis.setTickMarkType(Constants.TICK_MARK_INSIDE);
                cateAxis.setSecTickMarkType(Constants.TICK_MARK_NONE);
                cateAxis.setShowAxisLabel(true);
                cateAxis.getTextAttr().setFRFont(FRFont.getInstance("Microsoft YaHei", Font.PLAIN, 10f, new Color(138,140,139)));

                //ֵ��
                Axis valueAxis = plot.getyAxis();
                valueAxis.setAxisStyle(Constants.NONE);
                valueAxis.setAxisColor(null);
                valueAxis.setTickMarkType(Constants.TICK_MARK_INSIDE);
                valueAxis.setSecTickMarkType(Constants.TICK_MARK_NONE);
                valueAxis.setShowAxisLabel(true);
                valueAxis.getTextAttr().setFRFont(FRFont.getInstance("SimSun", Font.PLAIN, 10f, new Color(138,140,139)));

                //��ͼ��
                plot.setBorderStyle(Constants.LINE_THIN);
                plot.setBorderColor(new Color(204, 220, 228));
                plot.setBackground(ColorBackground.getInstance(new Color(248, 247, 245)));
                plot.getyAxis().setMainGridStyle(Constants.LINE_THIN);
                plot.getyAxis().setMainGridColor(new Color(192,192,192));
            }

        }
    }

    //ƽ��3D��Ĭ����������
    private void createCondition4Plane3D(Chart chart){
        if(chart != null){
            //����
            Title title = new Title(chart.getTitle().getTextObject());
            chart.setTitle(title);
            title.setTitleVisble(true);
            TextAttr textAttr = title.getTextAttr();
            if (textAttr == null) {
                textAttr = new TextAttr();
                title.setTextAttr(textAttr);
            }
            title.setPosition(Constants.CENTER);
            textAttr.setFRFont(FRFont.getInstance("Microsoft YaHei", Font.PLAIN, 16f, new Color(51,51,51)));

            //ͼ��
            Legend legend = new Legend();
            legend.setFRFont(FRFont.getInstance("SimSun", Font.PLAIN, 9f, new Color(128,128,128)));
            legend.setPosition(Constants.TOP);
            chart.getPlot().setLegend(legend);

            //������,����ֻ������ͼ������ͼ�����ͼ
            if(chart.getPlot() instanceof CategoryPlot){
                CategoryPlot plot = (CategoryPlot)chart.getPlot();
                //����������
                Axis cateAxis = plot.getxAxis();
                cateAxis.setAxisStyle(Constants.LINE_THICK);
                cateAxis.setAxisColor(new Color(57, 57, 57));
                cateAxis.setTickMarkType(Constants.TICK_MARK_NONE);
                cateAxis.setSecTickMarkType(Constants.TICK_MARK_NONE);
                cateAxis.setShowAxisLabel(true);
                cateAxis.getTextAttr().setFRFont(FRFont.getInstance("Microsoft YaHei", Font.PLAIN, 10f, new Color(57,57,57)));

                //ֵ������
                Axis  valueAxis = plot.getyAxis();
                valueAxis.setAxisStyle(Constants.LINE_NONE);
                valueAxis.setTickMarkType(Constants.TICK_MARK_NONE);
                valueAxis.setSecTickMarkType(Constants.TICK_MARK_NONE);
                valueAxis.setShowAxisLabel(false);

                //��ͼ��
                plot.getyAxis().setMainGridStyle(Constants.LINE_THIN);
                plot.getyAxis().setMainGridColor(new Color(192,192,192));
                chart.setBorderStyle(Constants.LINE_NONE);

                //���ݱ�ǩ
                ConditionAttr attrList = plot.getConditionCollection().getDefaultAttr();
                DataSeriesCondition attr = attrList.getExisted(AttrContents.class);
                if(attr != null) {
                    attrList.remove(attr);
                }
                AttrContents attrContents = new AttrContents();
                attrContents.setPosition(Constants.OUTSIDE);
                attrContents.setSeriesLabel(ChartConstants.VALUE_PARA);
                attrContents.setTextAttr(new TextAttr(FRFont.getInstance("SimSun", Font.PLAIN, 9f, new Color(51,51,51))));
                attrList.addDataSeriesCondition(attrContents);
            }
        }
    }

    //͸������Ĭ����������
    private void createCondition4Transparent(Chart chart){
        if(chart != null){
            //����
            Title title = new Title(chart.getTitle().getTextObject());
            chart.setTitle(title);
            title.setTitleVisble(true);
            TextAttr textAttr = title.getTextAttr();
            if (textAttr == null) {
                textAttr = new TextAttr();
                title.setTextAttr(textAttr);
            }
            title.setPosition(Constants.LEFT);
            textAttr.setFRFont(FRFont.getInstance("Microsoft YaHei", Font.BOLD, 16f, new Color(192,192,192)));

            //ͼ��
            Legend legend = new Legend();
            legend.setFRFont(FRFont.getInstance("SimSun", Font.PLAIN, 9f, new Color(138,140,139)));
            legend.setPosition(Constants.RIGHT_TOP);
            chart.getPlot().setLegend(legend);

            Plot plot = chart.getPlot();
            //��ͼ��
            chart.setBackground(ColorBackground.getInstance(new Color(51,51,51)));

            //������,����ֻ������ͼ������ͼ�����ͼ
            if(plot instanceof CategoryPlot){
                //�߿�
                plot.setBorderStyle(Constants.LINE_THIN);
                plot.setBorderColor(new Color(65,65,65));

                //����������
                Axis cateAxis = plot.getxAxis();
                cateAxis.setAxisStyle(Constants.LINE_THICK);
                cateAxis.setAxisColor(new Color(192,192,192));
                cateAxis.setTickMarkType(Constants.TICK_MARK_NONE);
                cateAxis.setSecTickMarkType(Constants.TICK_MARK_NONE);
                cateAxis.setShowAxisLabel(true);
                cateAxis.getTextAttr().setFRFont(FRFont.getInstance("Microsoft YaHei", Font.PLAIN, 10f, new Color(150,150,150)));

                //ֵ��
                Axis valueAxis = plot.getyAxis();
                valueAxis.setShowAxisLabel(true);
                valueAxis.setAxisStyle(Constants.LINE_NONE);
                valueAxis.getTextAttr().setFRFont(FRFont.getInstance("SimSun", Font.PLAIN, 10f, new Color(150,150,150)));
                valueAxis.setMainGridStyle(Constants.LINE_THIN);
                valueAxis.setMainGridColor(new Color(63, 62, 62));
            }
        }
    }

    //�����Ĭ����������
    private void createCondition4Shade(Chart chart){
        if(chart != null){
            //����
            Title title = new Title(chart.getTitle().getTextObject());
            chart.setTitle(title);
            title.setTitleVisble(true);
            TextAttr textAttr = title.getTextAttr();
            if (textAttr == null) {
                textAttr = new TextAttr();
                title.setTextAttr(textAttr);
            }
            title.setPosition(Constants.CENTER);
            textAttr.setFRFont(FRFont.getInstance("Microsoft YaHei", Font.BOLD, 16f, new Color(0,51,102)));

            //ͼ��
            Legend legend = new Legend();
            legend.setFRFont(FRFont.getInstance("SimSun", Font.PLAIN, 9f, new Color(128,128,128)));
            legend.setPosition(Constants.BOTTOM);
            chart.getPlot().setLegend(legend);

            //������,����ֻ������ͼ������ͼ�����ͼ
            if(chart.getPlot() instanceof CategoryPlot){
                CategoryPlot plot = (CategoryPlot)chart.getPlot();

                //����������
                Axis cateAxis = plot.getxAxis();
                cateAxis.setAxisStyle(Constants.LINE_THICK);
                cateAxis.setAxisColor(new Color(73, 100, 117));
                cateAxis.setTickMarkType(Constants.TICK_MARK_NONE);
                cateAxis.setSecTickMarkType(Constants.TICK_MARK_NONE);
                cateAxis.setShowAxisLabel(true);
                cateAxis.getTextAttr().setFRFont(FRFont.getInstance("Microsoft YaHei", Font.PLAIN, 10f, new Color(128,128,128)));

                //ֵ��
                Axis valueAxis = plot.getyAxis();
                valueAxis.setShowAxisLabel(true);
                valueAxis.getTextAttr().setFRFont(FRFont.getInstance("SimSun", Font.PLAIN, 10f, new Color(128,128,128)));
                valueAxis.setAxisStyle(Constants.LINE_NONE);

                //��ͼ��
                plot.getyAxis().setMainGridStyle(Constants.LINE_THIN);
                plot.getyAxis().setMainGridColor(new Color(192,192,192));
                plot.setHorizontalIntervalBackgroundColor(new Color(243,243,243));
            }
        }
    }

    protected boolean needsResetChart(Chart chart){
        return chart != null
                &&chart.getPlot() != null
                && chart.getPlot().getPlotStyle() != ChartConstants.STYLE_NONE;
    }

    public Chart getDefaultChart() {
        return BarIndependentChart.barChartTypes[0];
    }
}
