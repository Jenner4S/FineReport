package com.fr.plugin.chart.column;

import com.fr.chart.chartglyph.TopDownShadeChart;

import java.awt.*;
import java.awt.geom.RectangularShape;

/**
 * Created by Mitisky on 15/10/30.
 */
public class ColumnTopDownShadeStyle extends TopDownShadeChart {

    protected RectangularShape rect;
    private boolean isHorizontal;
    private boolean axisReversed;
    private static final double NUM = 0.9;

    public ColumnTopDownShadeStyle(Color baseColor, RectangularShape rect, boolean isHorizontal){
        this(baseColor, rect, isHorizontal, true, false);
    }

    public ColumnTopDownShadeStyle(Color baseColor, RectangularShape rect, boolean isHorizontal, boolean avoid, boolean axisReversed) {
        this.baseColor = baseColor;
        this.rect = rect;
        this.isHorizontal = isHorizontal;
        this.avoidOriginDraw = avoid;
        this.axisReversed = axisReversed;

    }

    /**
     * 画出选中的样式
     * @param g 画图对象
     */
    public void paintStyle(Graphics g){
        Graphics2D g2d = (Graphics2D)g;

        drawColumn(g2d);

        drawBorder(g2d);
    }

    /**
     * 渐变结束的颜色
     * @return 渐变结束的颜色
     */
    protected Color getEndColor(){
        return this.baseColor;
    }

    /**
     * 渐变开始的颜色
     * @return 渐变开始的颜色
     */
    protected Color getStartColor(){
        baseColor = baseColor == null ? new Color(255, 255, 255, 0) : baseColor;
        return new Color( Math.min(255,(int)(baseColor.getRed()/NUM)), Math.min(255,(int)(baseColor.getGreen()/NUM)), Math.min(255,(int)(baseColor.getBlue()/NUM)), baseColor.getAlpha());
    }

    private GradientPaint getGradientPaint(){
        Color startColor = this.getStartColor();
        Color endColor = this.getEndColor();
        if(this.isHorizontal){
            if(this.axisReversed){
                return new GradientPaint((float)rect.getX(), (float)rect.getY(), startColor, (float)(rect.getX()+rect.getWidth()), (float)rect.getY(), endColor);
            }else{
                return new GradientPaint((float)(rect.getX()+rect.getWidth()), (float)rect.getY(), startColor, (float)rect.getX(), (float)rect.getY(), endColor);
            }
        } else {
            if(this.axisReversed){
                return new GradientPaint((float)rect.getX(), (float)(rect.getY()+rect.getHeight()), startColor,
                        (float)rect.getX(), (float)rect.getY(), endColor);
            }else{
                return new GradientPaint((float)rect.getX(), (float)rect.getY(), startColor,
                        (float)rect.getX(), (float)(rect.getY()+rect.getHeight()), endColor);
            }
        }
    }

    protected void drawColumn(Graphics2D g2d){
        Paint oldPaint = g2d.getPaint();
        Composite oldComposite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha >= 0 ? alpha : 1));
        g2d.setPaint(getGradientPaint());
        g2d.fill(rect);
        g2d.setComposite(oldComposite);
        g2d.setPaint(oldPaint);
    }

    /**
     * 柱形图的描边
     * @param g2d 画图对象
     */
    protected void drawBorder(Graphics2D g2d){
        if(borderColor == null || this.borderWidth == 0){
            return;
        }
        Stroke oldStroke = g2d.getStroke();
        Stroke stroke = new BasicStroke((int)this.borderWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
        g2d.setStroke(stroke);
        g2d.setPaint(borderColor);
        g2d.draw(rect);

        g2d.setStroke(oldStroke);
    }
}
