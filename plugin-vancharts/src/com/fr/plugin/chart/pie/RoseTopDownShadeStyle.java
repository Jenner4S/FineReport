package com.fr.plugin.chart.pie;

import com.fr.chart.chartglyph.TopDownShadeChart;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by Mitisky on 15/9/7.
 */
public class RoseTopDownShadeStyle extends TopDownShadeChart {
    private double innerRadius;
    private double outerRadius;
    private Point2D centerPoint;
    private Shape shape;
    private static final double NUM = 0.8;

    public RoseTopDownShadeStyle(double innerRadius, double outerRadius, Point2D centerPoint, Color baseColor, Shape shape, boolean avoid){
        this.baseColor = baseColor;
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
        this.centerPoint = centerPoint;
        this.shape = shape;
        this.avoidOriginDraw = avoid;
    }

    /**
     * 渐变结束的颜色
     * @return 渐变结束的颜色
     */
    protected Color getEndColor(){
        baseColor = baseColor == null ? new Color(255, 255, 255, 0) : baseColor;
        return new Color( Math.min(255,(int)(baseColor.getRed()/NUM)), Math.min(255,(int)(baseColor.getGreen()/NUM)), Math.min(255,(int)(baseColor.getBlue()/NUM)), baseColor.getAlpha());
    }

    /**
     * 画出选中的样式
     * @param g 画图对象
     */
    public void paintStyle(Graphics g){
        if(outerRadius <= 0 || innerRadius == outerRadius){
            return;
        }
        Graphics2D g2d = (Graphics2D)g;

        Color endColor = this.getEndColor();//浅色
        Color startColor = this.getStartColor(); //深色

        Paint oldPaint = g2d.getPaint();
        Composite oldComposite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha >= 0 ? alpha : 1));

        float[] dist = {(float)(innerRadius/outerRadius), 1.0f};
        Color[] colors = {endColor, startColor};
        RadialGradientPaint paint = new RadialGradientPaint((float)centerPoint.getX(), (float)centerPoint.getY(), (float)outerRadius, dist, colors);
        g2d.setPaint(paint);
        g2d.fill(shape);
        g2d.setComposite(oldComposite);
        g2d.setPaint(oldPaint);

        Stroke oldStroke = g2d.getStroke();
        Stroke stroke = new BasicStroke((int)this.borderWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
        g2d.setStroke(stroke);
        g2d.setPaint(borderColor);
        g2d.draw(shape);

        g2d.setStroke(oldStroke);
    }

}
