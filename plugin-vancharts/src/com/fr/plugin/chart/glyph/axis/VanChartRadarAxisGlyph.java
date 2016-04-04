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
    private static final int POLYGON = 3;//������������������һ������Σ��������
    private static final double WHOLE_ANGLE = 360;
    private static final double START_ANGLE = 90;
    private static final double PIE = 180;
    private static final double ANGLE1 = 90;//����angle1��2����������ֳ���������CD+������AB��http://note.youdao.com/group/2941765/pdf/81248173
    private static final double ANGLE2 = -90;
    private static final int CATE_AXIS_LABEL_GAP = 10;//�������ǩ
    private static final int LABEL_GAP = 8;//���ݵ�ı�ǩ
    private static final double MIN_R = 1.0/3;//�״�ͼ������ʾ��ǩ���״�ͼ�뾶���ͼ����������ͼ������̱����Ƚϣ�����֮�Ȳ���С��1/3
    private static final double ARC_ANGLE = 2.0/5;//�ѻ������״�ͼ�����ε�һ��ռeachAngle����

    private Rectangle2D bounds;
    private double radius;
    private Point2D centerPoint;
    private double eachAngle;

    private Shape outerPath;//x�����ߣ�y���ǩ�����˷�Χ�򲻻�

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

    //����x���������ǩ�����뾶,ȷ��eachAngle��radius��
    public void doLayout(Rectangle2D bounds, int resolution) {
        this.bounds = bounds;
        this.centerPoint = new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());//�״�ͼԲ��ʼ��Ϊ��ͼ������
        this.radius = Math.min(bounds.getWidth(), bounds.getHeight())/2 - CATE_AXIS_LABEL_GAP;//��ʼ�뾶�������Ǹ��ݱ�ǩ����С

        xAxisGlyph.adjustRadius(resolution);

        radius = Math.max(radius, Math.min(bounds.getWidth(), bounds.getHeight()) * MIN_R);
        yAxisGlyph.setAxisLength(radius);
        xAxisGlyph.setBounds(bounds);
        yAxisGlyph.setBounds(bounds);
    }


    public void draw(java.awt.Graphics graphics, int resolution){
        Graphics2D g2d = (Graphics2D) graphics;
        //xy���˳���ܷ��ˡ���Ϊ��y���ǩ��ʱ����ж��Ƿ񳬳�x������ߡ�
        xAxisGlyph.drawInfo(g2d, resolution);//x���ǩ������
        yAxisGlyph.drawInfo(g2d, resolution);//y��������ߡ���ǩ������
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
     * ���ݷ�����ź�ֵȷ����
     * @param cateIndex �������
     * @param value ֵ
     * @return ����
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

    //��ȡֵ��ĳ��ֵ��Ӧ��Բ�λ����ε��ߣ�����x������ߺ�y��������ߣ�
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


    //��ȡx������
    private Shape getXAxisLine() {
        return getCircleOrPolygonGridLine(yAxisGlyph.getMaxValue());
    }

    //y������������
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

        //���ݷ�����Ż�ȡ�Ƕ�
        private double getAngleWithCateIndex(int cateIndex) {
            return START_ANGLE - (cateIndex - this.getCrossValue()) * VanChartRadarAxisGlyph.this.eachAngle;//����˳ʱ��
        }

        //����x���������ǩ�����뾶
        private void adjustRadius(int resolution) {
            int count = getCategoryCount();
            VanChartRadarAxisGlyph.this.eachAngle = WHOLE_ANGLE/count;
            boolean isToNumber = this.isToNumber();
            int labelCrement = (this.getLabelNumber() > 0) ? this.getLabelNumber() : this.getTickSamplingTime();
            for(int cateIndex = 0; cateIndex < count; cateIndex += labelCrement){
                String label = this.getLabelString(cateIndex, isToNumber);//����format
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

                if(gap > 0){//������Χ
                    radius -= gap;
                }
            }
        }

        /**
         * @param cateIndex       ��ǩ��λ��
         * @param offset      ƫ��������ǩ���ڿ̶�֮��ʱΪ0.5�����ڿ̶�����ʱΪ0
         * @param labelString ��ǩ�ı�
         * @param resolution  ��Ļ�ֱ���
         * @return ��ǩ·�����ں���bottom��top��ʱ��, ����bounds������margin. ��ֹ̫���ܼ�����.
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
         * ��������������Ԫ��
         *
         * @param g          ����
         * @param resolution �ֱ���
         */
        public void draw(Graphics g, int resolution) {
        }

        private void drawInfo(Graphics g, int resolution) {
            // ��x�������������
            drawAxisLine(g, resolution);
            // ��x��̶ȱ�ǩ
            drawTickLabel(g, resolution, 1, isToNumber(), 0);
        }

        /**
         * ���յ��������ǩ����
         */
        protected void drawLabel(Graphics g, double value, double offset, String labelString, int resolution) {
            if (!this.isShowAxisLabel) {
                return;
            }
            Graphics graphics = g.create();

            //todo:��������ת������
            textAttr.setRotation(-textAttr.getRotation());
            TitleGlyph textGlyph = new TitleGlyph(labelString, textAttr);
            Rectangle2D labelBounds = getLabelBounds(value, offset, labelString, resolution);
            //��Ϊ�Ҷ��룬���������ͼ�ζ��������ֱ�ӻ�
            textGlyph.setPosition(Constants.RIGHT);
            textGlyph.setBounds(getNiceLabelBounds(labelBounds));
            textGlyph.draw(graphics, resolution);
            textAttr.setRotation(-textAttr.getRotation());
        }

        //��ʾ���£�������ʾ��
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
         * ��x�������������
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

        //����ֵ��ȡ��Բ�ĵľ��룬���뾶
        private double getRadiusWithValue(double value) {
            double wholeValue;

            if (this.isLog() && this.getCrossValue() > 0) {
                // ָ��������
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

        //����ֵ��ȡ��Բ�ĵľ��룬���뾶.���ֵΪ������İ뾶��
        private double getRadiusWithValueInSideAxis(double value) {
            double r = getRadiusWithValue(value);
            return Math.min(r, radius);
        }

        /**
         * @param value       ��ǩ��λ��
         * @param offset      ƫ��������ǩ���ڿ̶�֮��ʱΪ0.5�����ڿ̶�����ʱΪ0
         * @param labelString ��ǩ�ı�
         * @param resolution  ��Ļ�ֱ���
         * @return ��ǩ·�����ں���bottom��top��ʱ��, ����bounds������margin. ��ֹ̫���ܼ�����.
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
         * ��������������Ԫ��
         *
         * @param g          ����
         * @param resolution �ֱ���
         */
        public void draw(Graphics g, int resolution) {
        }

        private void drawInfo(Graphics g, int resolution) {
            Graphics2D g2d = (Graphics2D)g;
            // �������
            drawIntervalBackground(g);
            // y���������
            drawAxisGrid(g);
            // y�������
            drawAxisLine(g, resolution);
            // ���̶ȱ�ǩ
            drawTickLable(g, resolution);
            // ��������
            drawAlertValueGlyph(g2d, resolution);
        }

        protected void drawMainGridLineWithValue(Graphics2D g2d, double value) {
            if(value == getMaxValue()){
                //���ֵ��x�������
                return;
            }
            g2d.draw(getCircleOrPolygonGridLine(value));
        }

        /**
         * ��y�������������
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
