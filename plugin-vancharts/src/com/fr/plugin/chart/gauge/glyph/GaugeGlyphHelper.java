package com.fr.plugin.chart.gauge.glyph;

import com.fr.base.DoubleDimension2D;
import com.fr.base.GraphHelper;
import com.fr.chart.base.AttrContents;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.base.GlyphUtils;
import com.fr.chart.base.TextAttr;
import com.fr.general.FRFont;
import com.fr.stable.CoreGraphHelper;
import com.fr.stable.StringUtils;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.text.Format;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mitisky on 15/12/18.
 */
public class GaugeGlyphHelper {
    private static final double HEIGHT_GAP = 0.5;
    public static final double WIDTH_GAP = 10;

    /**
     * �����ı�������ռ�ĸ߶ȺͿ��(����ת�����ŵ�)
     * ���ı������������ܣ�����ʱ��Ϊ�о�+�и�
     * @param text �ı��ַ�
     * @param frFont �ı���ʽ.
     * @param resolution �ֱ���
     * @return ����ռ�ô�С.
     */
    public static Dimension2D calculateTextDimension(String text, FRFont frFont, int resolution) {
        if (StringUtils.isEmpty(text)) {
            return new Dimension(0, 0);
        }

        if (frFont == null) {
            frFont = FRFont.getInstance();
        }
        Font font = GlyphUtils.getFont4StringInSystem(frFont.applyResolutionNP(resolution), text);

        double width = 0, height;

        BufferedImage bufferedImage = CoreGraphHelper.createBufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = bufferedImage.createGraphics();
        FontRenderContext frc = g2d.getFontRenderContext();

        String normalText = GlyphUtils.changeAllReline2Normal(text);
        String[] relines = normalText.split(AttrContents.RelineSeparation);

        for(String string : relines) {
            width = Math.max(width,  GraphHelper.stringWidth(string, font, frc));
        }

        double oneLineHeight = calculateOneLineHeight(frFont);
        height = oneLineHeight * relines.length + oneLineHeight * HEIGHT_GAP * (relines.length - 1);

        return new DoubleDimension2D(width,height);
    }

    //һ���ı��и�
    public static double calculateOneLineHeight(FRFont frFont) {
        FontMetrics fm = GraphHelper.getFontMetrics(frFont);
        return fm.getHeight();
    }

    //����ÿ�ж����У��Ҵ�����һ��verdana��������Ӣ�ĵ����⡣
    public static void drawMultiLineText(Graphics g, String text, TextAttr textAttr, Rectangle2D labelBounds, int resolution) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(labelBounds.getX(), labelBounds.getY());

        Paint oldPaint = g2d.getPaint();
        Composite oldComposite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        String relineNormalResult = GlyphUtils.changeAllReline2Normal(text);
        String[] al = relineNormalResult.split(AttrContents.RelineSeparation);

        for(int relineIndex = 0; relineIndex < al.length; relineIndex++) {
            Dimension2D dim = GlyphUtils.calculateTextDimensionWithNoRotation(al[relineIndex], textAttr, resolution);
            double x = Math.max((labelBounds.getWidth() - dim.getWidth() - 2) / 2, 0);
            Rectangle2D bound = new Rectangle2D.Double(x - 1, dim.getHeight() * relineIndex, dim.getWidth() + 2, dim.getHeight());
            drawStrings(g2d, al[relineIndex], textAttr, bound, resolution);
        }

        g2d.setPaint(oldPaint);
        g2d.setComposite(oldComposite);

        g2d.translate(-labelBounds.getX(), -labelBounds.getY());
    }

    public static void drawStrings(Graphics g, String text, TextAttr textAttr, Rectangle2D bounds, int resolution) {
        double gap = isNumeric(text) ? 0 : WIDTH_GAP;
        Rectangle2D temp = new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth() + gap, bounds.getHeight());
        GlyphUtils.drawStrings(g, text, textAttr, temp, resolution);
    }

    private static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * ���ڵõ���������ֵ.���ݸ�ʽ���string
     */
    public static String value2String(double value, Format format) {
        String string;
        if (format != null) {
            string = format.format(new Double(value));
        } else {
            string = ChartBaseUtils.generalFormat(value);
        }
        return string;
    }

    public static Color getColor(Color color) {
        return color == null ? new Color(255,255,255,0) : color;
    }

    //��һ��Բ��ΪcenterPoint�� �뾶Ϊradius�� ������ɫΪbackgroundColor��Բ
    protected static void drawCircle(Graphics2D g2d, Point2D centerPoint, double radius, Color backgroundColor) {
        drawArc(g2d, centerPoint, radius, 0, 360, backgroundColor);
    }

    //��һ��Բ��ΪcenterPoint�� �뾶Ϊradius�� ������ɫΪbackgroundColor����ʼ�Ƕ�ΪstartAngle����Χ��extent������
    protected static void drawArc(Graphics2D g2d, Point2D centerPoint, double radius, double startAngle, double extent, Color backgroundColor) {
        drawArc(g2d, centerPoint, radius, startAngle, extent, backgroundColor, Arc2D.PIE);
    }

    //��һ��Բ��ΪcenterPoint�� �뾶Ϊradius�� ������ɫΪbackgroundColor����ʼ�Ƕ�ΪstartAngle����Χ��extent�ģ��պϻ������ӵ����Ρ�
    protected static void drawArc(Graphics2D g2d, Point2D centerPoint, double radius, double startAngle, double extent, Color backgroundColor, int arcStyle) {
        if(radius <= 0){
            return;
        }
        g2d.setColor(getColor(backgroundColor));

        Arc2D arc2D = new Arc2D.Double(
                centerPoint.getX() - radius,
                centerPoint.getY() - radius,
                2 * radius, 2 * radius, startAngle, extent, arcStyle
        );
        g2d.fill(arc2D);
    }


    //��һ��Բ��ΪcenterPoint�� �뾶Ϊradius�����ΪborderWidth����ɫΪborderColor�����(Բ��)
    protected static void drawCircleStroke(Graphics2D g2d, Point2D centerPoint, double innerRadius, double outerRadius, Color borderColor) {
        if(innerRadius <= 0 || outerRadius <= 0){
            return;
        }
        g2d.setColor(borderColor);
        GeneralPath arcPath = getArcPath(centerPoint, innerRadius, outerRadius);
        g2d.fill(arcPath);
    }

    //��һ��Բ��ΪcenterPoint�� �뾶Ϊradius�����ΪborderWidth����ɫΪborderColor,��ʼ�Ƕ�ΪstartAngle����Χ��extent��Բ��
    protected static void drawArcStroke(Graphics2D g2d, Point2D centerPoint, double innerRadius, double outerRadius, double startAngle, double extent, Color borderColor) {
        if(innerRadius <= 0 || outerRadius <= 0){
            return;
        }
        g2d.setColor(borderColor);
        GeneralPath arcPath = getArcPath(centerPoint, innerRadius, outerRadius, startAngle, extent);
        g2d.fill(arcPath);
    }

    //����ɫ���
    protected static void drawCircleShadeStroke(Graphics2D g2d, Point2D centerPoint, double innerRadius, double outerRadius, Color startColor, Color endColor) {
        if(innerRadius <= 0 || outerRadius <= 0){
            return;
        }
        Paint oldPaint = g2d.getPaint();
        GeneralPath arcPath = getArcPath(centerPoint, innerRadius, outerRadius);

        float[] dist = {(float)(innerRadius/outerRadius), 1.0f};
        Color[] colors = {endColor, startColor};
        RadialGradientPaint paint = new RadialGradientPaint((float)centerPoint.getX(), (float)centerPoint.getY(), (float)outerRadius, dist, colors);
        g2d.setPaint(paint);
        g2d.fill(arcPath);

        g2d.setPaint(oldPaint);
    }

    //�õ�һ��Բ����path
    public static GeneralPath getArcPath(Point2D centerPoint, double innerRadius, double outerRadius) {
        return getArcPath(centerPoint, innerRadius, outerRadius, 0, 360);
    }

    //�õ�һ�����ε�Բ����path
    public static GeneralPath getArcPath(Point2D centerPoint, double innerRadius, double outerRadius, double startAngle, double extent) {
        GeneralPath arcPath = new GeneralPath();
        if(innerRadius <= 0 || outerRadius <= 0){
            return arcPath;
        }

        Arc2D outerArc = new Arc2D.Double(
                centerPoint.getX() - outerRadius,
                centerPoint.getY() - outerRadius,
                2 * outerRadius, 2 * outerRadius, startAngle, extent, Arc2D.OPEN
        );

        Arc2D innerArc = new Arc2D.Double(
                centerPoint.getX() - innerRadius,
                centerPoint.getY() - innerRadius,
                2 * innerRadius, 2 * innerRadius, startAngle + extent, -extent, Arc2D.OPEN
        );

        arcPath.moveTo((float)outerArc.getStartPoint().getX(), (float)outerArc.getStartPoint().getY());
        arcPath.append(outerArc, true);
        arcPath.lineTo((float)innerArc.getStartPoint().getX(), (float)innerArc.getStartPoint().getY());
        arcPath.append(innerArc, true);
        arcPath.closePath();

        return arcPath;
    }

    //�õ�һ�����ε�path
    public static GeneralPath getDiamondPath(double x, double y, double size){
        double px[] = {x - size, x, x + size, x};
        double py[] = {y, y - size, y, y + size};

        GeneralPath diamondPaths = new GeneralPath(GeneralPath.WIND_NON_ZERO, px.length);

        diamondPaths.moveTo((float) px[px.length - 1], (float) py[px.length - 1]);
        for (int i = 0; i < px.length; i++) {
            diamondPaths.lineTo((float) px[i], (float) py[i]);
        }
        return diamondPaths;
    }

    //�õ�һ�������ε�path
    public static GeneralPath  getTrianglePath(double x, double y,double size){
        double px[] = {x - size, x + size, x};
        double py[] = {y + size, y + size, y - size};

        GeneralPath tranglePaths = new GeneralPath(GeneralPath.WIND_NON_ZERO, px.length);

        tranglePaths.moveTo((float) px[px.length - 1], (float) py[px.length - 1]);
        for (int i = 0; i < px.length; i++) {
            tranglePaths.lineTo((float) px[i], (float) py[i]);
        }

        return tranglePaths;
    }
}
