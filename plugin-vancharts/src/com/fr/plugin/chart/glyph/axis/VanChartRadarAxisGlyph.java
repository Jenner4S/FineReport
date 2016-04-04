package com.fr.plugin.chart.glyph.axis;

import com.fr.base.GraphHelper;
import com.fr.base.chart.chartdata.ChartData;
import com.fr.chart.base.GlyphUtils;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartglyph.ChartAlertValueGlyph;
import com.fr.chart.chartglyph.SpecialGlyph;
import com.fr.chart.chartglyph.TitleGlyph;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.axis.VanChartValueAxis;
import com.fr.plugin.chart.gauge.glyph.GaugeGlyphHelper;
import com.fr.plugin.chart.glyph.VanChartAlertValueGlyph;
import com.fr.plugin.chart.radar.RadarType;
import com.fr.stable.Constants;

import java.awt.*;
import java.awt.geom.*;

/**
 * Created by Mitisky on 16/1/4.
 */
public class VanChartRadarAxisGlyph extends SpecialGlyph{
    private static final long serialVersionUID = 6463436295139097829L;
    private static final int POLYGON = 3;//至少三个分类可以组成一个多边形，封闭区域。
    private static final double WHOLE_ANGLE = 360;
    private static final double START_ANGLE = 90;
    private static final double PIE = 180;
    private static final double ANGLE1 = 90;//根据angle1、2将整个区域分成两个区域CD+两个点AB。http://note.youdao.com/group/2941765/pdf/81248173
    private static final double ANGLE2 = -90;
    private static final int CATE_AXIS_LABEL_GAP = 10;//分类轴标签
    private static final int LABEL_GAP = 8;//数据点的标签
    private static final double MIN_R = 1.0/3;//雷达图优先显示标签，雷达图半径与绘图区（不包括图例）最短边作比较，两者之比不得小于1/3
    private static final double ARC_ANGLE = 2.0/5;//堆积柱形雷达图，扇形的一半占eachAngle比例

    private Rectangle2D bounds;
    private double radius;
    private Point2D centerPoint;
    private double eachAngle;

    private Shape outerPath;//x轴轴线，y轴标签超出此范围则不画

    private VanChartRadarXAxisGlyph xAxisGlyph;
    private VanChartRadarYAxisGlyph yAxisGlyph;
    private RadarType radarType;

    public VanChartRadarAxisGlyph( ChartData chartData, VanChartAxis xAxis, VanChartValueAxis yAxis, RadarType radarType){
        this.xAxisGlyph = createRadarXAxisGlyph(chartData, xAxis);
        this.yAxisGlyph = createRadarYAxisGlyph(yAxis);
        this.radarType = radarType;
    }

    public VanChartBaseAxisGlyph getDataSeriesValueAxisGlyph(){
        return this.yAxisGlyph;
    }

    public VanChartBaseAxisGlyph getDataSeriesCateAxisGlyph(){
        return this.xAxisGlyph;
    }

    //根据x轴坐标轴标签调整半径,确定eachAngle、radius。
    public void doLayout(Rectangle2D bounds, int resolution) {
        this.bounds = bounds;
        this.centerPoint = new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());//雷达图圆心始终为绘图区中心
        this.radius = Math.min(bounds.getWidth(), bounds.getHeight())/2 - CATE_AXIS_LABEL_GAP;//初始半径，布局是根据标签再缩小

        xAxisGlyph.adjustRadius(resolution);

        radius = Math.max(radius, Math.min(bounds.getWidth(), bounds.getHeight()) * MIN_R);
        yAxisGlyph.setAxisLength(radius);
        xAxisGlyph.setBounds(bounds);
        yAxisGlyph.setBounds(bounds);
    }


    public void draw(java.awt.Graphics graphics, int resolution){
        Graphics2D g2d = (Graphics2D) graphics;
        //xy轴的顺序不能反了。因为画y轴标签的时候会判断是否超出x轴的轴线。
        xAxisGlyph.drawInfo(g2d, resolution);//x轴标签、轴线
        yAxisGlyph.drawInfo(g2d, resolution);//y轴的网格线、标签、轴线
    }

    public Shape getShape(){
        return new Arc2D.Double(centerPoint.getX() - radius, centerPoint.getY() - radius, 2 * radius, 2 * radius, 0, 360, Arc2D.PIE);
    }

    private VanChartRadarXAxisGlyph createRadarXAxisGlyph(ChartData chartData, VanChartAxis axis){
        VanChartRadarXAxisGlyph axisGlyph = new VanChartRadarXAxisGlyph();
        axis.initAxisGlyphWithChartData(chartData, axisGlyph);
        return axisGlyph;
    }

    private VanChartRadarYAxisGlyph createRadarYAxisGlyph(VanChartValueAxis axis) {
        VanChartRadarYAxisGlyph axisGlyph = new VanChartRadarYAxisGlyph();
        axis.initAxisGlyphWithChartData(axisGlyph);
        return axisGlyph;
    }

    /**
     * 根据分类序号和值确定点
     * @param cateIndex 分类序号
     * @param value 值
     * @return 坐标
     */
    public Point2D getPoint2DWithCateIndexAndValue(int cateIndex, double value){
        double angle = xAxisGlyph.getAngleWithCateIndex(cateIndex);
        double r = yAxisGlyph.getRadiusWithValue(value);
        return getPoint2DWithAngleAndRadius(angle, r);
    }

    public Shape getArcShapeBetweenTwoValues(double startValue, double endValue, int cateIndex) {
        double angle = xAxisGlyph.getAngleWithCateIndex(cateIndex);
        double startAngle = angle - eachAngle * ARC_ANGLE;
        double extent = eachAngle * ARC_ANGLE * 2;
        return yAxisGlyph.getCircleBackgroundShape(startValue, endValue, startAngle, extent);
    }

    public Rectangle2D getNormalLabelBounds(Dimension2D dim, int cateIndex, double value) {
        double r = yAxisGlyph.getRadiusWithValue(value) + LABEL_GAP;
        return xAxisGlyph.getLabelBoundsOut(dim, cateIndex, r);
    }

    public Rectangle2D getColumnTypeLabelBounds(Dimension2D dim, int cateIndex, double value) {
        double r = yAxisGlyph.getRadiusWithValue(value) - LABEL_GAP;
        r = r > 0 ? r : 0;
        return xAxisGlyph.getLabelBoundsIn(dim, cateIndex, r);
    }

    private Point2D getPoint2DWithCateIndexAndRadius(int cateIndex, double r){
        double angle = xAxisGlyph.getAngleWithCateIndex(cateIndex);
        return getPoint2DWithAngleAndRadius(angle, r);
    }

    private Point2D getPoint2DWithAngleAndRadius(double angle, double r){
        double x = centerPoint.getX() + r * Math.cos(Math.PI * (angle / PIE));
        double y = centerPoint.getY() - r * Math.sin(Math.PI * (angle / PIE));
        return new Point2D.Double(x, y);
    }

    //获取值轴某个值对应的圆形或多边形的线（包括x轴的轴线和y轴的网格线）
    private Shape getCircleOrPolygonGridLine(double value) {
        double r = yAxisGlyph.getRadiusWithValue(value);
        switch (radarType){
            case CIRCLE:
                return new Arc2D.Double(centerPoint.getX() - r, centerPoint.getY() - r, 2 * r, 2 * r, 0, 360, Arc2D.OPEN);
            default:
                GeneralPath path = new GeneralPath();
                completePolygonLine(path, value);
                return path;
        }
    }

    private void completePolygonLine(GeneralPath generalPath, double value) {
        Point2D p0 = getPoint2DWithCateIndexAndValue(0, value);
        int categoryCount = xAxisGlyph.getCategoryCount();
        generalPath.moveTo(p0.getX(), p0.getY());
        for (int cateIndex = 1; cateIndex < categoryCount; cateIndex++) {
            Point2D temp = getPoint2DWithCateIndexAndValue(cateIndex, value);
            generalPath.lineTo(temp.getX(), temp.getY());
        }
        generalPath.lineTo(p0.getX(), p0.getY());
    }

    private void completePolygonLineReversed(GeneralPath generalPath, double value) {
        Point2D p0 = getPoint2DWithCateIndexAndValue(0, value);
        int categoryCount = xAxisGlyph.getCategoryCount();
        generalPath.moveTo(p0.getX(), p0.getY());
        for (int cateIndex = categoryCount - 1; cateIndex > 0; cateIndex--) {
            Point2D temp = getPoint2DWithCateIndexAndValue(cateIndex, value);
            generalPath.lineTo(temp.getX(), temp.getY());
        }
        generalPath.lineTo(p0.getX(), p0.getY());
    }


    //获取x轴轴线
    private Shape getXAxisLine() {
        return getCircleOrPolygonGridLine(yAxisGlyph.getMaxValue());
    }

    //y的坐标轴轴线
    private Line2D[] getYAxisLine() {
        int count = xAxisGlyph.getCategoryCount();
        Line2D[] lines = new Line2D[count];
        for(int cateIndex = 0; cateIndex < count; cateIndex++){
            Point2D p = getPoint2DWithCateIndexAndRadius(cateIndex, radius);
            lines[cateIndex] = new Line2D.Double(centerPoint, p);
        }
        return lines;
    }


    private class VanChartRadarXAxisGlyph extends VanChartCategoryAxisGlyph {
        private static final long serialVersionUID = -3624844525338565417L;

        //根据分类序号获取角度
        private double getAngleWithCateIndex(int cateIndex) {
            return START_ANGLE - (cateIndex - this.getCrossValue()) * VanChartRadarAxisGlyph.this.eachAngle;//分类顺时针
        }

        //根据x轴坐标轴标签调整半径
        private void adjustRadius(int resolution) {
            int count = getCategoryCount();
            VanChartRadarAxisGlyph.this.eachAngle = WHOLE_ANGLE/count;
            boolean isToNumber = this.isToNumber();
            int labelCrement = (this.getLabelNumber() > 0) ? this.getLabelNumber() : this.getTickSamplingTime();
            for(int cateIndex = 0; cateIndex < count; cateIndex += labelCrement){
                String label = this.getLabelString(cateIndex, isToNumber);//考虑format
                Rectangle2D labelRect = this.getLabelBounds(cateIndex, 0, label, resolution);
                double angle = this.getAngleWithCateIndex(cateIndex);
                double gap;
                if(angle == ANGLE1){//B
                    gap = VanChartRadarAxisGlyph.this.bounds.getY() - labelRect.getY();
                } else if(angle < ANGLE1 && angle > ANGLE2){//C
                    gap = labelRect.getMaxX() - VanChartRadarAxisGlyph.this.bounds.getMaxX();
                } else if(angle == ANGLE2){//A
                    gap = labelRect.getMaxY() - VanChartRadarAxisGlyph.this.bounds.getMaxY();
                } else {//D
                    gap = VanChartRadarAxisGlyph.this.bounds.getX() - labelRect.getX();
                }

                if(gap > 0){//超出范围
                    radius -= gap;
                }
            }
        }

        /**
         * @param cateIndex       标签的位置
         * @param offset      偏移量，标签画在刻度之间时为0.5，画在刻度下面时为0
         * @param labelString 标签文本
         * @param resolution  屏幕分辨率
         * @return 标签路径，在横向bottom或top的时候, 最后的bounds加入了margin. 防止太过密集相邻.
         */
        protected Rectangle2D getLabelBounds(double cateIndex, double offset, String labelString, int resolution) {
            Dimension2D dim = GlyphUtils.calculateTextDimensionWithRotation(labelString, this.getTextAttr(), resolution);
            return getLabelBoundsOut(dim, (int)cateIndex, VanChartRadarAxisGlyph.this.radius + CATE_AXIS_LABEL_GAP);
        }

        private Rectangle2D getLabelBoundsOut(Dimension2D dim, int cateIndex, double r) {
            double angle = getAngleWithCateIndex(cateIndex);
            Point2D p = getPoint2DWithCateIndexAndRadius(cateIndex, r);
            double x = p.getX(), y = p.getY();
            if(angle == ANGLE1){//B
                x -= dim.getWidth()/2;
                y -= dim.getHeight();
            } else if(angle < ANGLE1 && angle > ANGLE2){//C
                y -= dim.getHeight()/2;
            } else if(angle == ANGLE2){//A
                x -= dim.getWidth()/2;
            } else {//D
                x -= dim.getWidth();
                y -= dim.getHeight()/2;
            }
            return new Rectangle2D.Double(x, y, dim.getWidth(), dim.getHeight());
        }

        private Rectangle2D getLabelBoundsIn(Dimension2D dim, int cateIndex, double r) {
            double angle = getAngleWithCateIndex(cateIndex);
            Point2D p = getPoint2DWithCateIndexAndRadius(cateIndex, r);
            double x = p.getX(), y = p.getY();
            if(angle == ANGLE1){//B
                x -= dim.getWidth()/2;
            } else if(angle < ANGLE1 && angle > ANGLE2){//C
                x -= dim.getWidth();
                y -= dim.getHeight()/2;
            } else if(angle == ANGLE2){//A
                x -= dim.getWidth()/2;
                y -= dim.getHeight();
            } else {//D
                y -= dim.getHeight()/2;
            }
            return new Rectangle2D.Double(x, y, dim.getWidth(), dim.getHeight());
        }

        /**
         * 画出坐标轴的相关元素
         *
         * @param g          画布
         * @param resolution 分辨率
         */
        public void draw(Graphics g, int resolution) {
        }

        private void drawInfo(Graphics g, int resolution) {
            // 画x轴坐标轴的轴线
            drawAxisLine(g, resolution);
            // 画x轴刻度标签
            drawTickLabel(g, resolution, 1, isToNumber(), 0);
        }

        /**
         * 最终的坐标轴标签画法
         */
        protected void drawLabel(Graphics g, double value, double offset, String labelString, int resolution) {
            if (!this.isShowAxisLabel) {
                return;
            }
            Graphics graphics = g.create();

            //todo:多行且旋转有问题
            textAttr.setRotation(-textAttr.getRotation());
            TitleGlyph textGlyph = new TitleGlyph(labelString, textAttr);
            Rectangle2D labelBounds = getLabelBounds(value, offset, labelString, resolution);
            //因为右对齐，所以用这个图形对象而不是直接画
            textGlyph.setPosition(Constants.RIGHT);
            textGlyph.setBounds(getNiceLabelBounds(labelBounds));
            textGlyph.draw(graphics, resolution);
            textAttr.setRotation(-textAttr.getRotation());
        }

        //显示不下，换行显示。
        private Rectangle2D getNiceLabelBounds(Rectangle2D oldBounds){
            double x = oldBounds.getX(), width = oldBounds.getWidth();
            if(oldBounds.getMinX() < 0){
                x = 0;
                width = width + oldBounds.getMinX();
            }
            if(oldBounds.getMaxX() > bounds.getWidth()){
                width = bounds.getWidth() - x;
            }
            double h_gap = TitleGlyph.GAP;
            double v_gap = TitleGlyph.GAP;
            if(textAttr.getRotation() != 0){
                h_gap += TitleGlyph.GAP;
            }
            return new Rectangle2D.Double(x - h_gap, oldBounds.getY() - v_gap, width + 2 * h_gap, VanChartRadarAxisGlyph.this.bounds.getHeight());
        }

        /**
         * 画x轴坐标轴的轴线
         */
        protected void drawAxisLine(Graphics g, int resolution) {
            Graphics2D g2d = (Graphics2D) g;
            Paint oldPaint = g2d.getPaint();
            Stroke oldStroke = g2d.getStroke();

            int lineStyle = this.getLineStyle();
            Color lineColor = this.getLineColor();
            outerPath = getXAxisLine();
            if (lineColor != null && lineStyle != Constants.LINE_NONE) {
                g2d.setStroke(GraphHelper.getStroke(lineStyle));
                g2d.setPaint(lineColor);
                g2d.draw(outerPath);
            }

            g2d.setStroke(oldStroke);
            g2d.setPaint(oldPaint);
        }
    }

    private class VanChartRadarYAxisGlyph extends VanChartValueAxisGlyph {

        private static final long serialVersionUID = -8745777455132242370L;
        private static final double GAP = 2;

        public double getDefaultTickCount() {
            return ValueAxisHelper.RADAR_TICK_COUNT;
        }

        //根据值获取和圆心的距离，即半径
        private double getRadiusWithValue(double value) {
            double wholeValue;

            if (this.isLog() && this.getCrossValue() > 0) {
                // 指数型坐标
                if (value <= LOG_MIN) {
                    value = this.getCrossValue();
                }
                value = (Math.log(value) - Math.log(this.getCrossValue())) / Math.log(this.getMainUnit());
                wholeValue =  (Math.log(this.getArrowValue()) - Math.log(this.getCrossValue())) / Math.log(this.getMainUnit());
            } else {
                //default
                if (value < this.getCrossValue()) {
                    value = this.getCrossValue();
                }
                value = value - this.getCrossValue();
                wholeValue = this.getArrowValue() - this.getCrossValue();
            }

            return value / wholeValue * VanChartRadarAxisGlyph.this.radius;
        }

        //根据值获取和圆心的距离，即半径.最大值为坐标轴的半径。
        private double getRadiusWithValueInSideAxis(double value) {
            double r = getRadiusWithValue(value);
            return Math.min(r, radius);
        }

        /**
         * @param value       标签的位置
         * @param offset      偏移量，标签画在刻度之间时为0.5，画在刻度下面时为0
         * @param labelString 标签文本
         * @param resolution  屏幕分辨率
         * @return 标签路径，在横向bottom或top的时候, 最后的bounds加入了margin. 防止太过密集相邻.
         */
        protected Rectangle2D getLabelBounds(double value, double offset, String labelString, int resolution) {
            Point2D p = getPoint2DWithAngleAndRadius(START_ANGLE, getRadiusWithValue(value));
            Dimension2D dim = GlyphUtils.calculateTextDimensionWithRotation(labelString, this.getTextAttr(), resolution);
            Rectangle2D bounds = new Rectangle2D.Double(p.getX() - GAP - dim.getWidth(), p.getY() - dim.getHeight(), dim.getWidth(), dim.getHeight());
            if(outerPath.contains(bounds) || xAxisGlyph.getCategoryCount() < POLYGON){
                return bounds;
            }
            return new Rectangle2D.Double(0,0,0,0);
        }

        /**
         * 画出坐标轴的相关元素
         *
         * @param g          画布
         * @param resolution 分辨率
         */
        public void draw(Graphics g, int resolution) {
        }

        private void drawInfo(Graphics g, int resolution) {
            Graphics2D g2d = (Graphics2D)g;
            // 间隔背景
            drawIntervalBackground(g);
            // y轴的网格线
            drawAxisGrid(g);
            // y轴的轴线
            drawAxisLine(g, resolution);
            // 画刻度标签
            drawTickLable(g, resolution);
            // 画警戒线
            drawAlertValueGlyph(g2d, resolution);
        }

        protected void drawMainGridLineWithValue(Graphics2D g2d, double value) {
            if(value == getMaxValue()){
                //最大值是x轴的轴线
                return;
            }
            g2d.draw(getCircleOrPolygonGridLine(value));
        }

        /**
         * 画y轴坐标轴的轴线
         */
        protected void drawAxisLine(Graphics g, int resolution) {
            Graphics2D g2d = (Graphics2D) g;
            Paint oldPaint = g2d.getPaint();
            Stroke oldStroke = g2d.getStroke();

            int lineStyle = this.getLineStyle();
            Color lineColor = this.getLineColor();
            if (lineColor != null && lineStyle != Constants.LINE_NONE) {
                g2d.setStroke(GraphHelper.getStroke(lineStyle));
                g2d.setPaint(lineColor);
                Line2D[] line2Ds = getYAxisLine();
                for(Line2D line : line2Ds){
                    g2d.draw(line);
                }
            }

            g2d.setStroke(oldStroke);
            g2d.setPaint(oldPaint);
        }

        protected Shape getCustomBackgroundShape(double startValue, double endValue, boolean isCustom) {
            switch (radarType){
                case CIRCLE:
                    return getCircleBackgroundShape(startValue, endValue, 0, 360);
                default:
                    GeneralPath result = new GeneralPath();
                    completePolygonLine(result, startValue);
                    completePolygonLineReversed(result, endValue);
                    return result;
            }
        }

        private Shape getCircleBackgroundShape(double startValue, double endValue, double startAngle, double extent) {
            double r1 = getRadiusWithValueInSideAxis(startValue);
            double r2 = getRadiusWithValueInSideAxis(endValue);
            if(r1 == 0 || r2 == 0){
                double r = r1 + r2;
                return new Arc2D.Double(
                        centerPoint.getX() - r,
                        centerPoint.getY() - r,
                        2 * r, 2 * r, startAngle, extent, Arc2D.PIE
                );
            }
            return GaugeGlyphHelper.getArcPath(centerPoint, r1, r2, startAngle, extent);
        }

        public void drawAlertValueGlyph(Graphics2D g2d, int resolution) {
            Paint oldPaint = g2d.getPaint();
            Stroke oldStroke = g2d.getStroke();
            Composite oldComposite = g2d.getComposite();
            for(ChartAlertValueGlyph glyph : getAlertValues()) {
                VanChartAlertValueGlyph alertValueGlyph = (VanChartAlertValueGlyph)glyph;
                Color alertColor = alertValueGlyph.getLineColor().getSeriesColor();
                if (alertColor == null) {
                    continue;
                }
                double alertValue = alertValueGlyph.getAlertValue();
                double r = getRadiusWithValue(alertValue);
                if(r > VanChartRadarAxisGlyph.this.radius){
                    continue;
                }
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alertValueGlyph.getAlertLineAlpha()));
                g2d.setPaint(alertColor);
                g2d.setStroke(GraphHelper.getStroke(alertValueGlyph.getLineStyle().getLineStyle()));

                Shape line = getCircleOrPolygonGridLine(alertValue);
                g2d.draw(line);

                TextAttr textAttr = new TextAttr();
                textAttr.setFRFont(alertValueGlyph.getAlertFont());
                String alertContent = alertValueGlyph.getAlertContent();
                Point2D p = getPoint2DWithAngleAndRadius(START_ANGLE, r);
                Dimension2D dim = GlyphUtils.calculateTextDimensionWithRotation(alertContent, textAttr, resolution);
                Rectangle2D bounds;
                if(alertValueGlyph.getAlertPosition() == Constants.LEFT){
                    bounds = new Rectangle2D.Double(p.getX() - GAP - dim.getWidth(), p.getY() - dim.getHeight(), dim.getWidth(), dim.getHeight());
                } else {
                    bounds = new Rectangle2D.Double(p.getX() + GAP, p.getY() - dim.getHeight(), dim.getWidth(), dim.getHeight());
                }
                GlyphUtils.drawStrings(g2d, alertContent, textAttr, bounds, resolution);
            }
            g2d.setComposite(oldComposite);
            g2d.setStroke(oldStroke);
            g2d.setPaint(oldPaint);
        }

    }

}
