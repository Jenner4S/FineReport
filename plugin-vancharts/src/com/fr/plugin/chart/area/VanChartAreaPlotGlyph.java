package com.fr.plugin.chart.area;

import com.fr.base.background.ColorBackground;
import com.fr.chart.base.AttrColor;
import com.fr.chart.base.AttrLineStyle;
import com.fr.chart.base.ChartUtils;
import com.fr.chart.base.LineStyleInfo;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.DataPoint;
import com.fr.chart.chartglyph.FoldLine;
import com.fr.chart.chartglyph.ShapeGlyph;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.AttrBand;
import com.fr.plugin.chart.base.AttrAreaSeriesFillColorBackground;
import com.fr.plugin.chart.glyph.VanChartBandGlyph;
import com.fr.plugin.chart.glyph.VanChartDataSeries;
import com.fr.plugin.chart.glyph.axis.VanChartBaseAxisGlyph;
import com.fr.plugin.chart.line.VanChartLinePlotGlyph;
import com.fr.stable.web.Repository;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Created by Mitisky on 15/11/19.
 */
public class VanChartAreaPlotGlyph extends VanChartLinePlotGlyph {

    private static final long serialVersionUID = 7221168915757808571L;
    //以下属性是存在condition里面的，专门拿出来是避免多个地方使用，循环遍历多次
    private AttrAreaSeriesFillColorBackground defaultAttrAreaSeriesFillColorBackground;

    private AttrAreaSeriesFillColorBackground getDefaultAttrAreaSeriesFillColorBackground() {
        if(defaultAttrAreaSeriesFillColorBackground == null){
            ConditionAttr conditionAttr = getConditionCollection().getDefaultAttr();
            defaultAttrAreaSeriesFillColorBackground = (AttrAreaSeriesFillColorBackground)conditionAttr.getExisted(AttrAreaSeriesFillColorBackground.class);
        }
        return defaultAttrAreaSeriesFillColorBackground;
    }
    public String getChartType(){
        return "area";
    }

    private AttrAreaSeriesFillColorBackground getAreaFillColorAndAlpha(VanChartDataSeries dataSeries){
        AttrAreaSeriesFillColorBackground fillColorBackground = dataSeries.getFillColorBackground();
        if(fillColorBackground == null){
            fillColorBackground = getDefaultAttrAreaSeriesFillColorBackground();
        }
        return fillColorBackground;
    }

    protected void initXAxisLabelDrawPosition(VanChartBaseAxisGlyph axisGlyph) {
    }

    protected void initDataSeriesGlyph(VanChartDataSeries dataSeries, GeneralPath linePaths, GeneralPath curvePaths, Color seriesColor,  VanChartBaseAxisGlyph yAxisGlyph,
                                       int[] cateIndexArray, Point2D[] currentPositivePoints, Point2D[] lastPositivePoints, Point2D[] lastNegativePoints) {
        boolean isCurve = isCurve(dataSeries);
        if(isCurve){
            ChartUtils.curveTo(linePaths, curvePaths);
            curvePaths.reset();
        }
        GeneralPath areaPaths = (GeneralPath)linePaths.clone();
        dealAreaPath(isCurve, dataSeries, cateIndexArray, curvePaths, areaPaths, yAxisGlyph,
                currentPositivePoints, lastPositivePoints, lastNegativePoints);
        dealLinePath(areaPaths, linePaths, dataSeries, seriesColor, yAxisGlyph);
    }

    private void dealAreaPath(boolean isCurve, VanChartDataSeries dataSeries, int[] cateIndexArray, GeneralPath curvePaths,
                              GeneralPath areaPaths, VanChartBaseAxisGlyph yAxisGlyph,
                              Point2D[] currentPositivePoints, Point2D[] lastPositivePoints, Point2D[] lastNegativePoints){
        double y = yAxisGlyph.getPoint2D(0).getY();
        boolean isStep = isStep(dataSeries);//应该取上一个系列的形态，这边说是和前台一样，先这样
        boolean isNullValueBreak = isNullValueBreak(dataSeries);
        double last_point_x = 0;
        boolean start = false;
        Point2D lastCurrentNotNullPoint = null;
        int lastPointIndex = cateIndexArray.length - 1;
        for(int i = lastPointIndex; i >= 0; i--){
            int cateIndex = cateIndexArray[i];
            DataPoint dataPoint = dataSeries.getDataPoint(cateIndex);
            Point2D point2D = dataPoint.getValue() < 0 ? lastNegativePoints[cateIndex] : lastPositivePoints[cateIndex];
            if (point2D == null){//堆积的第一个系列
                point2D = new Point2D.Double(currentPositivePoints[cateIndex].getX(), y);
            }
            if(this.isDataPointXNotInPlotBounds((float)point2D.getX())) {
                continue;
            }

            if(dataPoint.isValueIsNull() && !start && isNullValueBreak){
                start = true;
                completeAreaPath(isCurve, lastCurrentNotNullPoint, areaPaths, curvePaths);
            } else if (start){
                start = false;
                Point2D currentP = currentPositivePoints[cateIndex];
                areaPaths.moveTo(currentP.getX(), currentP.getY());
                areaPaths.lineTo(point2D.getX(), point2D.getY());
                if(isCurve){
                    curvePaths.moveTo(point2D.getX(), point2D.getY());
                }
            } else {
                dealAreaPoint4EveryDataPoint(isCurve, isStep, i, lastPointIndex, point2D, areaPaths, curvePaths, last_point_x);
                last_point_x = point2D.getX();
            }

            if(cateIndex == 0){
                completeAreaPath(isCurve, currentPositivePoints[cateIndex], areaPaths, curvePaths);
            }
            if(!dataPoint.isValueIsNull()){
                lastCurrentNotNullPoint = currentPositivePoints[cateIndex];
            }
        }
    }

    private void dealAreaPoint4EveryDataPoint(boolean isCurve, boolean isStep, int i, int lastPointIndex, Point2D point2D,
                                              GeneralPath areaPaths, GeneralPath curvePaths, double last_point_x) {
        if (isCurve) {
            if (i == lastPointIndex) {
                areaPaths.lineTo(point2D.getX(), point2D.getY());
                curvePaths.moveTo(point2D.getX(), point2D.getY());
            } else {
                curvePaths.lineTo(point2D.getX(), point2D.getY());
            }
        } else if (isStep) {
            if(i != lastPointIndex){
                areaPaths.lineTo(last_point_x, point2D.getY());
            }
            areaPaths.lineTo(point2D.getX(), point2D.getY());
        } else {
            areaPaths.lineTo(point2D.getX(), point2D.getY());
        }
    }

    private void completeAreaPath(boolean isCurve, Point2D point2D, GeneralPath areaPaths, GeneralPath curvePaths){
        if(point2D !=null){
            if(isCurve){
                ChartUtils.curveTo(areaPaths, curvePaths);
                curvePaths.reset();
            }
            areaPaths.lineTo(point2D.getX(), point2D.getY());
        }
    }

    protected void dealLinePath(GeneralPath areaPaths, GeneralPath linePaths, VanChartDataSeries dataSeries, Color seriesColor, VanChartBaseAxisGlyph yAxisGlyph) {
        FoldLine foldLine = new FoldLine(linePaths);
        dataSeries.setAreaTopLine(foldLine);
        ShapeGlyph shapeGlyph = new ShapeGlyph(areaPaths);
        dataSeries.setDrawImpl(shapeGlyph);

        LineStyleInfo info = foldLine.getLineStyleInfo();

        AttrLineStyle attrLineStyle = new AttrLineStyle(getAttrLineWidth(dataSeries));
        info.changeStyleAttrLineStyle(attrLineStyle);
        info.changeStyleAttrColor(new AttrColor(seriesColor));
        AttrAreaSeriesFillColorBackground areaFillColorAndAlpha = getAreaFillColorAndAlpha(dataSeries);
        ColorBackground areaColorBackground;
        if(areaFillColorAndAlpha == null){
            areaColorBackground = ColorBackground.getInstance(seriesColor);
        } else{
            areaColorBackground = areaFillColorAndAlpha.getColorBackground();
            if(areaColorBackground == null){
                areaColorBackground = ColorBackground.getInstance(seriesColor);
            }
            shapeGlyph.setAlpha((float)areaFillColorAndAlpha.getAlpha());
        }
        shapeGlyph.setBackground(areaColorBackground);

        dealBands(areaPaths, linePaths, dataSeries, seriesColor, yAxisGlyph, attrLineStyle);
    }

    protected void dealBands(GeneralPath areaPaths, GeneralPath linePaths, VanChartDataSeries dataSeries, Color seriesColor, VanChartBaseAxisGlyph yAxisGlyph, AttrLineStyle attrLineStyle) {
        List<AttrBand> bansList = dataSeries.getBands();
        for(AttrBand band : bansList){
            double min_y = yAxisGlyph.getPointInBounds(band.getMinEval()).getY();
            double max_y = yAxisGlyph.getPointInBounds(band.getMaxEval()).getY();
            Rectangle2D clipBounds = new Rectangle2D.Double(0, Math.min(min_y, max_y), getBounds().getWidth(), Math.abs(min_y - max_y));
            VanChartBandGlyph bandGlyph = new VanChartBandGlyph(linePaths, clipBounds);

            LineStyleInfo bandInfo = bandGlyph.getLineStyleInfo();
            bandInfo.changeStyleAttrLineStyle(attrLineStyle);
            Color lineColor = band.getColor();
            if(lineColor == null){
                lineColor = seriesColor;
            }
            bandInfo.changeStyleAttrColor(new AttrColor(lineColor));

            AttrAreaSeriesFillColorBackground fillColorBackground = band.getFillColorBackground();
            if(fillColorBackground != null){
                ShapeGlyph areaGlyph = new ShapeGlyph(areaPaths);
                areaGlyph.setAlpha((float)fillColorBackground.getAlpha());
                ColorBackground colorBackground = fillColorBackground.getColorBackground();
                if(colorBackground == null){
                    colorBackground = ColorBackground.getInstance(seriesColor);
                }
                areaGlyph.setBackground(colorBackground);
                bandGlyph.setAreaGlyph(areaGlyph);
            }

            dataSeries.addBandGlyph(bandGlyph);
        }
    }


    /**
     * 获取 plotOptions的JSON对象
     * @param repo 请求
     * @param isJsDraw 动态展示
     * @return 返回json
     * @throws com.fr.json.JSONException 抛出问题
     */
    public JSONObject getPlotOptionsJSON(Repository repo, boolean isJsDraw) throws JSONException {
        JSONObject js = super.getPlotOptionsJSON(repo, isJsDraw);
        AttrAreaSeriesFillColorBackground seriesFillColorBackground = getDefaultAttrAreaSeriesFillColorBackground();
          if(seriesFillColorBackground != null){
            seriesFillColorBackground.addToJSONObject(js, null);
        }

        return js;
    }

}
