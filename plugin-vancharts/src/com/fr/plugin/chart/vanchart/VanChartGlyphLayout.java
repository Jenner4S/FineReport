package com.fr.plugin.chart.vanchart;

import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.chartglyph.ChartGlyphLayout;
import com.fr.chart.chartglyph.LegendGlyph;
import com.fr.chart.chartglyph.PlotGlyph;
import com.fr.plugin.chart.glyph.VanChartDataSheetGlyph;
import com.fr.plugin.chart.glyph.VanChartDataSheetPlotGlyphInterface;
import com.fr.plugin.chart.glyph.VanChartPlotGlyph;
import com.fr.plugin.chart.glyph.VanChartPlotGlyphInterface;

import java.awt.geom.Rectangle2D;

/**
 * Created by Mitisky on 15/10/21.
 */
public class VanChartGlyphLayout {
    private static final double WIDTH_GAP = 8;
    private static final double HEIGHT_GAP = 8;
    private static final double GAP_BETWEEN_AXIS = 10;
    /**
     * ͼ��ChartGlyph���������� �߽�
     * ����ͼ��Ĳ���˳�򣺱��⡢ͼ������
     * @param chartGlyph ���ڲ��ֵĶ���
     * @param resolution �ֱ��ʲ���
     */
    public static void doLayout(VanChartGlyph chartGlyph, int resolution) {
        if(chartGlyph == null || chartGlyph.getBounds() == null)  {
            return;
        }

        //ԭʼͼ��߽硣���������������⡢ͼ��������������ռ�ȵ�
        Rectangle2D chartOriginalBounds = chartGlyph.getBounds();
        //������ϸ��ݱ��⡢ͼ����������Ȳü�
        Rectangle2D leftBounds = chartGlyph.getBounds();

        ChartGlyphLayout.preGap4ChartBorder(chartGlyph, leftBounds);
        ChartGlyphLayout.preGap4ChartInPadding(leftBounds);

        //���ֱ��Ⲣ�ü�ͼ����
        ChartGlyphLayout.titleGlyphDoLayout(leftBounds, chartGlyph.getTitleGlyph(), resolution);

        //����ͼ�����ü�ͼ����
        legendDoLayoutWithLeftBounds(chartOriginalBounds, leftBounds, chartGlyph, resolution);

        //���ֻ�ͼ�������������ᡢ���ݱ��ϵ��ͼ�Σ�
        plotDoLayout(chartOriginalBounds, leftBounds, chartGlyph, resolution);
    }

    /**
     * �������ݱ�ͬʱ������ͼ����������ı߽���Ϣ
     * @param chartOriginalBounds ͼ�����ĳ�ʼ�߽�
     * @param plotOriginalBounds ��ͼ����������������ݱ�֮ǰ�ĳ�ʼ�߽�
     * @param chartGlyph ͼ���ͼ����
     * @param resolution ����
     */
    private static void dataSheetDoLayoutWithLeftBounds(Rectangle2D chartOriginalBounds, Rectangle2D plotOriginalBounds,
                                                        VanChartGlyph chartGlyph, VanChartDataSheetPlotGlyphInterface plotGlyph, int resolution){

        VanChartDataSheetGlyph dataSheetGlyph = (VanChartDataSheetGlyph)chartGlyph.getDataSheetGlyph();
        if(dataSheetGlyph == null){
            return;
        }

        //���ݱ��ֵ�һ�����������ݱ���Ҫ�����Բ���������
        //�����ʱ��ֻ�Ǹ������ݱ����õ����ԣ���߽���ص�ͼ���������ᡢ���ݵ�Ȳ���������
        dataSheetGlyph.initDataSheet(chartGlyph.getLegendGlyph(), plotGlyph);

        //���ݱ��ֵڶ���������ϵ�����Ŀ�ȣ����ݱ��е�ͼ���Ŀ�ȣ�
        dataSheetGlyph.calculateSeriesWidth(chartOriginalBounds, resolution);

        //�������ݱ����ϵ�����Ŀ�ȵ�����ͼ���Լ������ᣨx��unitLength�ȣ�
        double leftWidth = dataSheetGlyph.getLeftWidth();
        double leftGap = plotGlyph.getBounds().getX() - plotOriginalBounds.getX();
        if(leftWidth > leftGap){
            double gap = leftWidth - leftGap;
            plotGlyph.setBounds(new Rectangle2D.Double(
                    plotGlyph.getBounds().getX() + gap,
                    plotGlyph.getBounds().getY(),
                    plotGlyph.getBounds().getWidth() - gap,
                    plotGlyph.getBounds().getHeight()));
            plotGlyph.adjustAxisGlyphWithLeftGap(gap);
        }

        //���ݱ��ֵ�����������x����������յ�unitLength��������ǩ�ĸ߶ȡ�ϵ�еĸ߶ȵȡ�
        dataSheetGlyph.calculateOtherWidthAndHeight(resolution);

        //�������ݱ�����߶ȵ�����ͼ���Լ������ᣨy��unitLength�ȣ�
        double bottomHeight = dataSheetGlyph.getBottomHeight();
        plotGlyph.setBounds(new Rectangle2D.Double(
                plotGlyph.getBounds().getX(),
                plotGlyph.getBounds().getY(),
                plotGlyph.getBounds().getWidth(),
                plotGlyph.getBounds().getHeight() - bottomHeight));
        plotGlyph.adjustAxisGlyphWithBottomGap(bottomHeight);

        //���������ͼ������λ��
        double y = plotOriginalBounds.getMaxY() - bottomHeight;
        if(plotGlyph.getBottomXAxisCount() == 1){
            y -= GAP_BETWEEN_AXIS;
        }

        //���ݱ��ֵ��Ĳ���ȷ���߽磨�����ͼ����λ�ã�
        dataSheetGlyph.setBounds(new Rectangle2D.Double(plotGlyph.getBounds().getX() - leftWidth,
                 y, dataSheetGlyph.getWholeWidth(), bottomHeight));
    }

    /**
     * ֻ���ֻ�ͼ��
     * @param chartOriginalBounds ͼ��ԭʼ�߽�
     * @param chartBounds �ü��������ǻ�ͼ���ĳ�ʼ�߽�
     * @param chartGlyph ͼ���ͼ����
     * @param resolution �ֱ���
     */
    private static void plotDoLayout(Rectangle2D chartOriginalBounds, Rectangle2D chartBounds, VanChartGlyph chartGlyph, int resolution) {
        PlotGlyph plotGlyph = chartGlyph.getPlotGlyph();
        if(plotGlyph == null) {
            return;
        }
        //�Ȱ�û�вü�������������ݱ�Ļ�ͼ���߽縳ֵ����ͼ��
        //֮�󲼾����ݱ���������ٽ��вü�
        plotGlyph.setBounds(chartBounds);

        //���������Ტ�ü�
        if(plotGlyph instanceof VanChartPlotGlyphInterface){
            VanChartPlotGlyphInterface plotGlyphInterface = (VanChartPlotGlyphInterface)plotGlyph;
            plotGlyphInterface.layoutAxisGlyph(chartOriginalBounds, resolution);
        }

        //�������ݱ�
        if(plotGlyph instanceof VanChartDataSheetPlotGlyphInterface){
            dataSheetDoLayoutWithLeftBounds(chartOriginalBounds, chartBounds, chartGlyph, (VanChartDataSheetPlotGlyphInterface)plotGlyph, resolution);
        }

        //��ͼ���ı߽�ȷ����֮�� ��󲼾�ϵ��
        plotGlyph.layoutDataSeriesGlyph(resolution);

        if(plotGlyph.getRoundRadius() > 0){
            plotGlyph.setBounds(ChartBaseUtils.rectangle2RoundRectangle(plotGlyph.getBounds(), plotGlyph.getRoundRadius()));
        } else {
            plotGlyph.setBounds(ChartBaseUtils.rectangle2RoundRectangle(plotGlyph.getBounds(), plotGlyph.isRoundBorder()));
        }
    }

    /**
     * ����ͼ��
     * @param chartOriginalBounds ͼ��ԭʼ�߽�
     * @param chartBounds ͼ��ʣ��߽�
     * @param chartGlyph ͼ��
     * @param resolution �ֱ���
     */
    private static void legendDoLayoutWithLeftBounds(Rectangle2D chartOriginalBounds, Rectangle2D chartBounds, VanChartGlyph chartGlyph, int resolution) {
        LegendGlyph legendGlyph = chartGlyph.getLegendGlyph();
        if(legendGlyph == null || !legendGlyph.isVisible()) {
            return;
        }

        //����֮ǰ�ȸ���ϵ������ͼ��Ԫ��
        VanChartPlotGlyph plotGlyph = (VanChartPlotGlyph)chartGlyph.getPlotGlyph();
        legendGlyph.setItems(plotGlyph.createLegendItems());

        legendGlyph.layoutLegendAndRefreshChartBounds(chartOriginalBounds, chartBounds, WIDTH_GAP, HEIGHT_GAP, resolution);
    }

}
