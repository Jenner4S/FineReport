package com.fr.plugin.chart.line;

import com.fr.base.background.ColorBackground;
import com.fr.chart.base.AttrColor;
import com.fr.chart.base.AttrLineStyle;
import com.fr.chart.base.ChartUtils;
import com.fr.chart.base.LineStyleInfo;
import com.fr.chart.chartglyph.*;
import com.fr.general.Background;
import com.fr.general.ComparatorUtils;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.AttrBand;
import com.fr.plugin.chart.attr.LineStyle;
import com.fr.plugin.chart.base.VanChartAttrLine;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.plugin.chart.glyph.VanChartBandGlyph;
import com.fr.plugin.chart.glyph.VanChartDataSeries;
import com.fr.plugin.chart.glyph.VanChartLineMarkerIcon;
import com.fr.plugin.chart.glyph.VanChartRectanglePlotGlyph;
import com.fr.plugin.chart.glyph.axis.VanChartBaseAxisGlyph;
import com.fr.plugin.chart.glyph.marker.CustomImageMarker;
import com.fr.stable.Constants;
import com.fr.stable.web.Repository;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mitisky on 15/11/10.
 */
public class VanChartLinePlotGlyph extends VanChartRectanglePlotGlyph {

    private static final long serialVersionUID = 3936503431906562228L;
    //�����ڻ��ߵ�ʱ��
    protected boolean isStartPoint = true;

    //���������Ǵ���condition����ģ�ר���ó����Ǳ������ط�ʹ�ã�ѭ���������
    private VanChartAttrLine defaultAttrLine;
    private VanChartAttrMarker defaultAttrMarker;

    private VanChartAttrLine getDefaultAttrLine() {
        if(defaultAttrLine == null){
            ConditionAttr conditionAttr = getConditionCollection().getDefaultAttr();
            defaultAttrLine = (VanChartAttrLine)conditionAttr.getExisted(VanChartAttrLine.class);
        }
        return defaultAttrLine;
    }

    private VanChartAttrMarker getDefaultAttrMarker() {
        if(defaultAttrMarker == null){
            ConditionAttr conditionAttr = getConditionCollection().getDefaultAttr();
            defaultAttrMarker = (VanChartAttrMarker)conditionAttr.getExisted(VanChartAttrMarker.class);
        }
        return defaultAttrMarker;
    }

    /**
     * ĳ��ϵ���Ƿ��ǹ⻬����
     * @param dataSeries ��ϵ��
     * @return �Ƿ�⻬
     */
    protected boolean isCurve(VanChartDataSeries dataSeries){
        VanChartAttrLine attrLine = dataSeries.getAttrLine();
        if(attrLine == null) {
            attrLine = getDefaultAttrLine();
        }
        return attrLine != null && ComparatorUtils.equals(attrLine.getLineStyle(), LineStyle.CURVE);
    }

    protected boolean isStep(VanChartDataSeries dataSeries){
        VanChartAttrLine attrLine = dataSeries.getAttrLine();
        if(attrLine == null) {
            attrLine = getDefaultAttrLine();
        }
        return attrLine != null && ComparatorUtils.equals(attrLine.getLineStyle(), LineStyle.STEP);
    }

    protected boolean isNullValueBreak(VanChartDataSeries dataSeries){
        VanChartAttrLine attrLine = dataSeries.getAttrLine();
        if(attrLine == null) {
            attrLine = getDefaultAttrLine();
        }        return attrLine == null || attrLine.isNullValueBreak();
    }

    protected int getAttrLineWidth(VanChartDataSeries dataSeries){
        VanChartAttrLine attrLine = dataSeries.getAttrLine();
        if(attrLine == null) {
            attrLine = getDefaultAttrLine();
        }
        if(attrLine == null){
            attrLine = new VanChartAttrLine();
        }
        return attrLine.getLineWidth();
    }

    protected Color getAttrLineColor(VanChartDataSeries dataSeries){
        Color[] colors = createColors4Series();
        return getAttrLineColor(dataSeries, colors);
    }

    private VanChartAttrMarker getAttrMarker(VanChartLineDataPoint dataPoint) {
        VanChartAttrMarker attrMarker = dataPoint.getAttrMarker();
        if(attrMarker == null){
            attrMarker = getDefaultAttrMarker();
        }
        return attrMarker;
    }

    private VanChartAttrMarker getAttrMarker(VanChartDataSeries dataSeries) {
        VanChartAttrMarker attrMarker = dataSeries.getMarker();
        if(attrMarker == null){
            attrMarker = getDefaultAttrMarker();
        }
        return attrMarker;
    }

    //Ĭ�ϵ�series��bands���ԣ���������������Сֵ��Ϊ�˲���bands�������ֵ��Сֵֻ������һ��������
    protected void initDataSeriesBandsDefaultMinMaxValue(double min, double max, List<Number> seriesList4TheAxis){
        for(Number seriesIndex : seriesList4TheAxis){
            VanChartDataSeries dataSeries = (VanChartDataSeries)getSeries(seriesIndex.intValue());
            dataSeries.initBandsDefaultMinMaxValue(min, max);
        }
    }

    protected LineMarkerIcon getLegendMarkerIcon(VanChartDataSeries dataSeries, Color[] colors) {
        VanChartLineMarkerIcon itemIcon = new VanChartLineMarkerIcon();

        dealMarkerStyle(itemIcon, dataSeries, colors);

        return itemIcon;
    }

    private void dealMarkerStyle(VanChartLineMarkerIcon itemIcon, VanChartDataSeries dataSeries, Color[] colors) {
        VanChartAttrMarker attrMarker = getAttrMarker(dataSeries);
        Marker marker = MarkerFactory.createMarker(attrMarker.getMarkerType().getType());
        marker.setPlotBackground(Marker.SCATTER_PLOT_BACKROUNG);
        ColorBackground colorBackground = ColorBackground.getInstance(getAttrLineColor(dataSeries, colors));
        if(attrMarker.isCommon()){//ͼ���еı�ǵ���ɫ����һ�������Ǻ�ϵ��ɫһ��
            marker.setBackground(colorBackground);
        }
        itemIcon.setLineStyle(Constants.LINE_MEDIUM);//ͼ��������Ĭ�ϣ�������ϵ�еĿ�ȸı���ı�
        itemIcon.setBackground(colorBackground);
        itemIcon.setMarker(marker);
    }

    /**
     * ����ͼ�ζ���
     * @param resolution �ֱ���
     */
    public void layoutDataSeriesGlyph(int resolution) {
        super.layoutDataSeriesGlyph(resolution);
        Map<String, List<List<Number>>> axis2SeriesMap = buildAxisMap(false);
        for(String axisName : axis2SeriesMap.keySet()){
            buildSingleAxisLines(axis2SeriesMap.get(axisName), resolution);
        }
    }

    //һ���������ϵ�����ϵ��
    protected void buildSingleAxisLines(List<List<Number>> axisMap, int resolution){
        for(List<Number> allSeriesIndexInBar : axisMap){
            Map<String, Number> prePositiveSumValueInSameCateValue = new HashMap<String, Number>();//����ͬcateValue�µ��Ѿ������������ӵ���ֵ��
            Map<String, Number> preNegativeSumValueInSameCateValue = new HashMap<String, Number>();//����ͬcateValue�µ��Ѿ����ĸ������ӵ���ֵ��
            Point2D[] lastPositivePoints = null;
            Point2D[] lastNegativePoints = null;
            VanChartBaseAxisGlyph XAxisGlyph = null, YAxisGlyph = null;
            for(Number seriesIndex : allSeriesIndexInBar){//�ѻ���һ���ϵ��
                //��������ԭ���ǣ�����Ϊֵ�����ʱ����
                VanChartDataSeries dataSeries = (VanChartDataSeries) getSeries(seriesIndex.intValue());
                if(XAxisGlyph == null){
                    XAxisGlyph = getDataSeriesCateAxisGlyph(dataSeries);
                }
                if(YAxisGlyph == null){
                    YAxisGlyph = getDataSeriesValueAxisGlyph(dataSeries);
                }
                if(lastPositivePoints == null){
                    lastPositivePoints = new Point2D[dataSeries.getDataPointCount()];
                }
                if(lastNegativePoints == null){
                    lastNegativePoints = new Point2D[dataSeries.getDataPointCount()];
                }
                //��ͬϵ�п��ܷ��಻ͬ��
                int[] cateIndexArray = sortCateValue(XAxisGlyph, dataSeries);
                buildSingleLine(dataSeries, XAxisGlyph, YAxisGlyph, lastPositivePoints, lastNegativePoints,
                        prePositiveSumValueInSameCateValue, preNegativeSumValueInSameCateValue, cateIndexArray, resolution);
            }
        }
    }

    protected void buildSingleLine(VanChartDataSeries dataSeries, VanChartBaseAxisGlyph xAxisGlyph, VanChartBaseAxisGlyph yAxisGlyph,
                                   Point2D[] lastPositivePoints, Point2D[] lastNegativePoints,
                                Map<String, Number> prePositiveSumValueInSameCateValue,
                                Map<String, Number> preNegativeSumValueInSameCateValue,int[] cateIndexArray, int resolution) {
        Color seriesColor = getAttrLineColor(dataSeries);//ϵ��ɫ������������ʾ������ϵ���Ǳߵ���ɫ
        GeneralPath linePaths = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        GeneralPath curvePaths = new GeneralPath(GeneralPath.WIND_NON_ZERO);

        //���clone֮�������һ�������ԭ���ǣ�
        //dealLine4AllSeries��ı�lastPositivePoints��ֵ��initDataSeriesGlyph�������Ҫ�øı�֮ǰ��ֵ
        Point2D[] lastPositivePointsClone = lastPositivePoints.clone();
        Point2D[] lastNegativePointsClone = lastNegativePoints.clone();

        dealLine4AllSeries(dataSeries, xAxisGlyph, yAxisGlyph, prePositiveSumValueInSameCateValue, preNegativeSumValueInSameCateValue,
                lastPositivePoints, lastNegativePoints, seriesColor, linePaths, curvePaths, cateIndexArray, resolution);

        initDataSeriesGlyph(dataSeries, linePaths, curvePaths, seriesColor, yAxisGlyph, cateIndexArray, lastPositivePoints, lastPositivePointsClone, lastNegativePointsClone);
    }

    protected void initDataSeriesGlyph(VanChartDataSeries dataSeries, GeneralPath linePaths, GeneralPath curvePaths, Color seriesColor, VanChartBaseAxisGlyph yAxisGlyph
                                       , int[] cateIndexArray, Point2D[] currentPositivePoints, Point2D[] lastPositivePoints, Point2D[] lastNegativePoints) {
        if (isCurve(dataSeries)) {
            ChartUtils.curveTo(linePaths, curvePaths);
        }
        dealLinePath(linePaths, dataSeries, seriesColor, yAxisGlyph);
    }

    //�������Ÿ���
    protected int[] sortCateValue(VanChartBaseAxisGlyph axisGlyph, VanChartDataSeries dataSeries) {
        if(getSeriesSize() < 1){
            return new int[0];
        }
        int cateLen = dataSeries.getDataPointCount();
        int[] cateIndexArray = new int[cateLen];//��ź����±֮꣬����������
        double[] cateValueArray = new double[cateLen];//��ź�����ֵ������ʱ����仯
        for(int cateIndex = 0; cateIndex < cateLen; cateIndex++){
            DataPoint dataPoint = dataSeries.getDataPoint(cateIndex);
            double cateValue = getCateValue(axisGlyph, dataPoint, false);
            cateIndexArray[cateIndex] = cateIndex;
            cateValueArray[cateIndex] = cateValue;
        }
        int low = 0;
        int high = cateLen - 1;
        int temp;
        int j;
        while (low < high) {
            for (j = low; j < high; ++j) {
                if(cateValueArray[cateIndexArray[j]] > cateValueArray[cateIndexArray[j+1]]){
                    temp = cateIndexArray[j];
                    cateIndexArray[j] = cateIndexArray[j+1];
                    cateIndexArray[j+1] = temp;
                }
            }
            --high;
            for (j = high; j > low; --j) {
                if(cateValueArray[cateIndexArray[j]] < cateValueArray[cateIndexArray[j-1]]){
                    temp = cateIndexArray[j];
                    cateIndexArray[j] = cateIndexArray[j-1];
                    cateIndexArray[j-1] = temp;
                }
            }
            ++low;
        }
        return cateIndexArray;
    }

    private void dealLinePath(GeneralPath linePaths, VanChartDataSeries dataSeries, Color seriesColor, VanChartBaseAxisGlyph yAxisGlyph) {
        FoldLine foldLine = new FoldLine(linePaths);
        dataSeries.setDrawImpl(foldLine);

        LineStyleInfo info = foldLine.getLineStyleInfo();

        AttrLineStyle attrLineStyle = new AttrLineStyle(getAttrLineWidth(dataSeries));
        info.changeStyleAttrLineStyle(attrLineStyle);
        info.changeStyleAttrColor(new AttrColor(seriesColor));

        List<AttrBand> bansList = dataSeries.getBands();
        for(AttrBand band : bansList){
            double min_y = yAxisGlyph.getPointInBounds(band.getMinEval()).getY();
            double max_y = yAxisGlyph.getPointInBounds(band.getMaxEval()).getY();
            Rectangle2D clipBounds = new Rectangle2D.Double(0, Math.min(min_y, max_y), getBounds().getWidth(), Math.abs(min_y - max_y));
            VanChartBandGlyph bandGlyph = new VanChartBandGlyph(linePaths, clipBounds);
            LineStyleInfo bandInfo = bandGlyph.getLineStyleInfo();
            bandInfo.changeStyleAttrLineStyle(attrLineStyle);
            bandInfo.changeStyleAttrColor(new AttrColor(band.getColor()));
            dataSeries.addBandGlyph(bandGlyph);
        }
    }

    protected void dealLine4AllSeries(VanChartDataSeries dataSeries, VanChartBaseAxisGlyph xAxisGlyph, VanChartBaseAxisGlyph yAxisGlyph,
                                    Map<String, Number> prePositiveSumValueInSameCateValue, Map<String, Number> preNegativeSumValueInSameCateValue,
                                    Point2D[] lastPositivePoints, Point2D[] lastNegativePoints,
                                    Color seriesColor, GeneralPath linePaths, GeneralPath curvePaths, int[] cateIndexArray, int resolution) {
        VanChartBaseAxisGlyph valueAxisGlyph = getDataSeriesValueAxisGlyph(dataSeries);
        boolean isPercentStack = valueAxisGlyph.isPercentage();
        int lineDataCount = calculateLineDataCount(dataSeries);
        double[] xVal = new double[lineDataCount],  yVal = new double[lineDataCount];
        lineDataCount = 0;
        isStartPoint = true; // �Ƿ�����ʼ��, �ж�ֵ�Ƿ�Ϊ��, �Ƿ��ǵ�һ������
        boolean isCurve = isCurve(dataSeries), isStep = isStep(dataSeries), isNullValueBreak = isNullValueBreak(dataSeries);
        double defaultY = yAxisGlyph.getPoint2D(0).getY();
        for (int cateIndex : cateIndexArray) {
            VanChartLineDataPoint dataPoint = (VanChartLineDataPoint)dataSeries.getDataPoint(cateIndex);
            double cateValue = getCateValue(xAxisGlyph, dataPoint, true);
            float point_x = (float) xAxisGlyph.getPoint2D(cateValue).getX();
            if(lastNegativePoints[cateIndex] == null){
                lastNegativePoints[cateIndex] = new Point2D.Double(point_x, defaultY);
            }
            if(lastPositivePoints[cateIndex] == null){
                lastPositivePoints[cateIndex] = new Point2D.Double(point_x, defaultY);
            }
            if (dataPoint.isValueIsNull()) {
                if (isNullValueBreak) {
                    isStartPoint = true;  //��ֵ�Ͽ�
                }
                continue;
            }
            double value = isPercentStack ? dataPoint.getPercentValue() : dataPoint.getValue();
            if(this.isDataPointXNotInPlotBounds(point_x)) {
                dataPoint.setDataLabel(null);
                continue;
            }
            double sumValue = getSumValue(cateValue, value, prePositiveSumValueInSameCateValue, preNegativeSumValueInSameCateValue);
            float point_y = (float)yAxisGlyph.getBounds().getY() + (float) yAxisGlyph.getPoint2D(sumValue).getY();
            xVal[lineDataCount] = point_x;// ��¼�������
            yVal[lineDataCount] = point_y;
            if(value < 0){
                lastNegativePoints[cateIndex] = new Point2D.Double(point_x, point_y);
            } else {
                lastPositivePoints[cateIndex] = new Point2D.Double(point_x, point_y);
            }
            float last_point_y = (float)(isStartPoint ? 0 : yVal[lineDataCount - 1]);//��ֱ����Ҫ�õ�
            lineDataCount++;
            dealLinePoint4EveryDataPoint(linePaths, curvePaths, point_x, point_y, isCurve, isStep, last_point_y);
            initMarkerGlyph(dataPoint, point_x, point_y, seriesColor);
            dealDataPointLabel(dataPoint, resolution);
        }
        trendLineFitting(xVal, yVal, dataSeries);// ��ϼ���
    }

    protected double getSumValue(double cateValue, double value, Map<String, Number> prePositiveSumValueInSameCateValue,
                               Map<String, Number> preNegativeSumValueInSameCateValue){
        double sumValue;
        if(value > 0){
            Number sum = prePositiveSumValueInSameCateValue.get(String.valueOf(cateValue));
            sumValue = value + (sum == null ? 0 : sum.doubleValue());
            prePositiveSumValueInSameCateValue.put(String.valueOf(cateValue), sumValue);
        } else {
            Number sum = preNegativeSumValueInSameCateValue.get(String.valueOf(cateValue));
            sumValue = value + (sum == null ? 0 : sum.doubleValue());
            preNegativeSumValueInSameCateValue.put(String.valueOf(cateValue), sumValue);
        }
        return sumValue;
    }

    protected void dealLinePoint4EveryDataPoint(GeneralPath linePaths, GeneralPath curvePaths, float point_x, float point_y,
                                              boolean isSeriesCurve, boolean isSeriesStep, float last_point_y) {
        if (isSeriesCurve) {//�⻬����
            if (isStartPoint) {
                ChartUtils.curveTo(linePaths, curvePaths);
                curvePaths.reset();
                curvePaths.moveTo(point_x, point_y);
                linePaths.moveTo(point_x, point_y);
                isStartPoint = false;
            } else {
                curvePaths.lineTo(point_x, point_y);
            }
        } else if(isSeriesStep){//��ֱ����
            if (this.isStartPoint) {
                linePaths.moveTo(point_x, point_y);
                this.isStartPoint = false;
            } else {
                linePaths.lineTo(point_x, last_point_y);
                linePaths.lineTo(point_x, point_y);
            }
        } else {//��ͨ����
            if (isStartPoint) {
                linePaths.moveTo(point_x, point_y);
                isStartPoint = false;
            } else {
                linePaths.lineTo(point_x, point_y);
            }
        }
    }

    protected void initMarkerGlyph(VanChartLineDataPoint dataPoint, float point_x, float point_y, Color seriesColor) {
        initMarkerGlyph(new MarkerGlyph(), dataPoint, point_x, point_y, seriesColor);
    }

    protected void initMarkerGlyph(MarkerGlyph markerGlyph, VanChartLineDataPoint dataPoint, float point_x, float point_y, Color seriesColor) {
        VanChartAttrMarker attrMarker = getAttrMarker(dataPoint);
        Marker marker = attrMarker.getMarkerType().getMarker();
        if(attrMarker.isCommon()){
            if(attrMarker.getColorBackground() != null){//���Ҹ������ǵ����õ���ɫ
                marker.setBackground(attrMarker.getColorBackground());
            } else {//���Ҹ�ϵ�е���ɫ
                marker.setBackground(ColorBackground.getInstance(seriesColor));
            }
            double radius = attrMarker.getRadius();
            marker.setSize(radius);
            markerGlyph.setShape(new Rectangle2D.Double(point_x - radius, point_y - radius, radius * 2, radius * 2));
        } else if(attrMarker.getImageBackground() != null){
            double width = attrMarker.getWidth();
            double height = attrMarker.getHeight();
            marker = new CustomImageMarker(width, height);
            marker.setBackground(attrMarker.getImageBackground());
            markerGlyph.setShape(new Rectangle2D.Double(point_x - width/2, point_y - height/2, width, height));
        }
        marker.setPlotBackground(getMarkerBackground());
        markerGlyph.setMarker(marker);
        dataPoint.setDrawImpl(markerGlyph);
    }

    private Background getMarkerBackground() {
        if(this.getBackground() instanceof ColorBackground){//��ͼ������ɫ����
            return this.getBackground();
        } else if(this.getBackground() == null && getWholeChartBackground() instanceof ColorBackground){//��ͼ��û�б�����ͼ��������ɫ����
            return this.getWholeChartBackground();
        } else {
            return null;
        }
    }

    public String getChartType(){
        return "line";
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

        VanChartAttrLine attrLine = getDefaultAttrLine();
        VanChartAttrMarker attrMarker = getDefaultAttrMarker();
        if(attrLine != null){
            attrLine.addToJSONObject(js);
        }

        if(attrMarker != null){
            js.put("marker", attrMarker.toJSONObject(repo));
        }

        return js;
    }
}
