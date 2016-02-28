package com.fr.plugin.chart.radar;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrLineStyle;
import com.fr.chart.chartglyph.*;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.area.VanChartAreaPlotGlyph;
import com.fr.plugin.chart.glyph.VanChartDataSeries;
import com.fr.plugin.chart.glyph.axis.VanChartBaseAxisGlyph;
import com.fr.plugin.chart.glyph.axis.VanChartRadarAxisGlyph;
import com.fr.plugin.chart.glyph.marker.VanChartMarkerGlyph;
import com.fr.stable.web.Repository;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

/**
 * Created by Mitisky on 15/12/28.
 */
public class VanChartRadarPlotGlyph extends VanChartAreaPlotGlyph {

    private static final long serialVersionUID = 3690467196318699246L;

    private VanChartRadarAxisGlyph radarAxisGlyph;
    private RadarType radarType;
    private boolean isColumnType;

    public void setRadarAxisGlyph(VanChartRadarAxisGlyph radarAxisGlyph) {
        this.radarAxisGlyph = radarAxisGlyph;
    }

    public void setRadarType(RadarType radarType) {
        this.radarType = radarType;
    }

    public void setColumnType(boolean isColumnType) {
        this.isColumnType = isColumnType;
    }

    /**
     *  �Ի�ͼ���е������Ჿ�ֽ��в���,����ȥͼ��߽������������
     *  �״�ͼ�������᲼�ֲ��ǳ���������ͼ�β��֣����ó�����
     * @param chartOriginalBounds ԭʼͼ��߽�
     * @param resolution �ֱ���
     */
    public void layoutAxisGlyph(Rectangle2D chartOriginalBounds, int resolution){
        initYAxisGlyphMinMaxValue(0, radarAxisGlyph.getDataSeriesValueAxisGlyph());
        radarAxisGlyph.doLayout(new Rectangle2D.Double(0, 0, getBounds().getWidth(), getBounds().getHeight()), resolution);
    }

    //�ѻ������״�ͼ��ͼ��������ͼһ������ͨ�״�ͼͼ��������ͼһ����
    protected LineMarkerIcon getLegendMarkerIcon(VanChartDataSeries dataSeries, Color[] colors) {
        if(isColumnType){
            LineMarkerIcon itemIcon = new LineMarkerIcon();
            dealMarkerIconCondition(itemIcon, dataSeries, colors);
            return itemIcon;
        } else {
            return super.getLegendMarkerIcon(dataSeries, colors);
        }
    }

    //Ĭ�ϵ�series��bands���ԣ���������������Сֵ��Ϊ�˲���bands�������ֵ��Сֵֻ������һ��������
    protected void initDataSeriesBandsDefaultMinMaxValue(double min, double max, java.util.List<Number> seriesList4TheAxis){
    }

    protected void initDataSeriesGlyph(VanChartDataSeries dataSeries, GeneralPath linePaths, GeneralPath areaPaths, Color seriesColor,
                                       VanChartBaseAxisGlyph yAxisGlyph
            , int[] cateIndexArray, Point2D[] currentPositivePoints, Point2D[] lastPositivePoints, Point2D[] lastNegativePoints) {
        dealLinePath(areaPaths, linePaths, dataSeries, seriesColor, yAxisGlyph);
    }

    protected void dealBands(GeneralPath areaPaths, GeneralPath linePaths, VanChartDataSeries dataSeries, Color seriesColor,
                             VanChartBaseAxisGlyph yAxisGlyph, AttrLineStyle attrLineStyle) {
    }

    //���ͼû�й⻬���ߣ���Ϊ��ֵ�Ͽ�ʱ���ߺ������ɫ��·����һ�������Էֿ���
    protected void dealLine4AllSeries(VanChartDataSeries dataSeries, VanChartBaseAxisGlyph xAxisGlyph, VanChartBaseAxisGlyph yAxisGlyph,
                                      Map<String, Number> prePositiveSumValueInSameCateValue, Map<String, Number> preNegativeSumValueInSameCateValue,
                                      Point2D[] lastPositivePoints, Point2D[] lastNegativePoints,
                                      Color seriesColor, GeneralPath linePaths, GeneralPath areaPaths, int[] cateIndexArray, int resolution) {
        if(isColumnType){
            dealStackColumn4AllSeries(dataSeries, prePositiveSumValueInSameCateValue, preNegativeSumValueInSameCateValue, cateIndexArray, resolution);
        } else {
            double centerValue = yAxisGlyph.getMinValue();
            dealNormalLine4AllSeries(dataSeries, seriesColor, linePaths, areaPaths, cateIndexArray, centerValue, resolution);
        }
    }

    private void dealNormalLine4AllSeries(VanChartDataSeries dataSeries, Color seriesColor, GeneralPath linePaths,
                                          GeneralPath areaPaths, int[] cateIndexArray, double centerValue, int resolution) {
        isStartPoint = true; // �Ƿ�����ʼ��, �ж�ֵ�Ƿ�Ϊ��, �Ƿ��ǵ�һ������
        boolean isNullValueBreak = isNullValueBreak(dataSeries);
        Point2D firstPoint = null;//��һ�����ǿ�ֵ�����ݵ�
        for (int cateIndex : cateIndexArray) {
            VanChartRadarDataPoint dataPoint = (VanChartRadarDataPoint)dataSeries.getDataPoint(cateIndex);
            double value = dataPoint.getValue();
            if(dataPoint.isValueIsNull()){
                value = centerValue;
            }
            Point2D p = radarAxisGlyph.getPoint2DWithCateIndexAndValue(cateIndex, value);
            dealAreaPoint4EveryPoint(areaPaths, p, firstPoint);

            if (dataPoint.isValueIsNull()) {
                if (isNullValueBreak) {
                    isStartPoint = true;  //��ֵ�Ͽ�
                }
                continue;
            } else if(firstPoint == null){
                firstPoint = p;
            }

            dealLinePoint4EveryDataPoint(linePaths, new GeneralPath(), (float)p.getX(), (float)p.getY(), false, false, 0);
            VanChartMarkerGlyph markerGlyph = new VanChartMarkerGlyph(){
                protected boolean isNeedBorderBG() {
                    return false;
                }
            };
            initMarkerGlyph(markerGlyph, dataPoint, (float)p.getX(), (float)p.getY(), seriesColor);
            dealDataPointLabel(dataPoint, value, resolution);
        }
        if(firstPoint != null){
            dealAreaPoint4EveryPoint(areaPaths, firstPoint, firstPoint);
            dealLinePoint4EveryDataPoint(linePaths, new GeneralPath(), (float)firstPoint.getX(), (float)firstPoint.getY(), false, false, 0);
        }
    }

    private void dealAreaPoint4EveryPoint(GeneralPath areaPaths, Point2D p, Point2D firstPoint) {
        if(firstPoint == null){
            areaPaths.moveTo(p.getX(), p.getY());
        } else {
            areaPaths.lineTo(p.getX(), p.getY());
        }
    }

    private void dealStackColumn4AllSeries(VanChartDataSeries dataSeries, Map<String, Number> prePositiveSumValueInSameCateValue,
                                           Map<String, Number> preNegativeSumValueInSameCateValue, int[] cateIndexArray, int resolution) {
        for (int cateIndex : cateIndexArray) {
            VanChartRadarDataPoint dataPoint = (VanChartRadarDataPoint)dataSeries.getDataPoint(cateIndex);
            double value = dataPoint.getValue();
            double lastSumValue = getLastSumValue(cateIndex, value, prePositiveSumValueInSameCateValue, preNegativeSumValueInSameCateValue);
            double sumValue = getSumValue(cateIndex, value, prePositiveSumValueInSameCateValue, preNegativeSumValueInSameCateValue);

            Shape shape = radarAxisGlyph.getArcShapeBetweenTwoValues(lastSumValue, sumValue, cateIndex);
            ShapeGlyph shapeGlyph = new ShapeGlyph(shape);

            changeInfoWithCondition(shapeGlyph.getGeneralInfo(), dataPoint);

            dataPoint.setDrawImpl(shapeGlyph);

            dealDataPointLabel(dataPoint, sumValue, resolution);
        }
    }

    /**
     * �������ݵ�ı�ǩ�ı߿�
     *
     * @param dataPoint  ���ݵ�
     * @param resolution �ֱ���
     */
    private void dealDataPointLabel(DataPoint dataPoint, double realValue, int resolution) {
        TextGlyph labelGlyph = dataPoint.getDataLabel();
        if(labelGlyph == null) {
            return;
        }

        if (notDealDataPointLabel(dataPoint, labelGlyph)) {
            labelGlyph.setBounds(null);
        } else {
            Dimension2D labelPreDim = labelGlyph.preferredDimension(resolution);

            Rectangle2D bounds = isColumnType ? radarAxisGlyph.getColumnTypeLabelBounds(labelPreDim, dataPoint.getCategoryIndex(), realValue)
                    : radarAxisGlyph.getNormalLabelBounds(labelPreDim, dataPoint.getCategoryIndex(), realValue);

            labelGlyph.setBounds(bounds);
        }
    }

    private double getLastSumValue(double cateValue, double value, Map<String, Number> prePositiveSumValueInSameCateValue,
                                   Map<String, Number> preNegativeSumValueInSameCateValue) {
        if(value > 0){
            Number sum = prePositiveSumValueInSameCateValue.get(String.valueOf(cateValue));
            return sum == null ? 0 : sum.doubleValue();
        } else {
            Number sum = preNegativeSumValueInSameCateValue.get(String.valueOf(cateValue));
            return sum == null ? 0 : sum.doubleValue();
        }
    }

    /**
     * ��������ϼ���
     */
    protected void trendLineFitting(double[] xVal, double[] yVal, DataSeries dataSeries) {
    }

    //�״�ͼ��ϵ�е�ʱ���ܳ���������x���Χ��Բ�λ���������
    public void drawShape4Series(Graphics g, int resolution) {
        Shape oldShape = g.getClip();
        g.setClip(radarAxisGlyph.getShape());
        super.drawShape4Series(g, resolution);
        g.setClip(oldShape);
    }

    protected void drawAxis(Graphics g, int resolution) {
        radarAxisGlyph.draw(g, resolution);
    }

    public  String getChartType(){
        return "radar";
    }

    /**
     * ��ȡ plotOptions��JSON����
     * @param repo ����
     * @param isJsDraw ��̬չʾ
     * @return ����json
     * @throws com.fr.json.JSONException �׳�����
     */
    public JSONObject getPlotOptionsJSON(Repository repo, boolean isJsDraw) throws JSONException {
        JSONObject js = super.getPlotOptionsJSON(repo, isJsDraw);
        js.put("type", radarType.getType());
        js.put("columnType", isColumnType);

        AttrAlpha attrAlpha = getDefaultAttrAlpha();
        if(attrAlpha != null){
            js.put("fillColorOpacity", attrAlpha.getAlpha());
        }
        return js;
    }

}
