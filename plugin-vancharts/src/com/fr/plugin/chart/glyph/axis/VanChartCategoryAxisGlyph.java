package com.fr.plugin.chart.glyph.axis;

import com.fr.base.GraphHelper;
import com.fr.base.Utils;
import com.fr.chart.base.ChartConstants;
import com.fr.general.ComparatorUtils;
import com.fr.general.DateUtils;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;

import java.awt.*;
import java.awt.geom.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mitisky on 15/10/21.
 */
public class VanChartCategoryAxisGlyph extends VanChartBaseAxisGlyph{
    private static final long serialVersionUID = -7226271395500582645L;
    protected ArrayList categoryLabelList = new ArrayList();//������ı�ǩ�Ǵ�chartData�����ɵ�
    public static final double HALF = 0.5;

    /**
     * ��ӷ������ǩ,
     *
     * @param label ��ǩ.
     */
    public void addCategoryLabel(Object label) {
        this.categoryLabelList.add(label);
    }

    public int getCategoryCount() {
        return this.categoryLabelList.size();
    }

    /**
     * ��ʼ������������ֵ����Сֵ
     *
     * @param autoMin ��Сֵ
     * @param autoMax ���ֵ
     */
    public void initMinMaxValue(double autoMin, double autoMax){
        this.maxValue = autoMax;
        this.minValue = autoMin;
        this.mainUnit = new BigDecimal("1");
        this.setSecUnit(0.2);
    }

    protected double getMaxTickValue() {
        double tickValue = getMaxValue();
        if (!isDrawBetweenTick()) {// 1����ǩ��ʱ��, max = 1
            if (this.categoryLabelList != null && this.categoryLabelList.size() > 1) {
                tickValue = tickValue - this.getMainUnit();
            }
        }

        return tickValue;
    }

    /**
     * ����������zero���Ӧ��ֵ�Ƕ��٣�
     */
    public double getCrossValue(){
        return getMinValue();
    }

    /**
     * ��ȡ�������ڼ�ͷ����Ҳ������β����ֵ
     */
    public double getArrowValue(){
        return getMaxValue();
    }

    public String getJSAxisType(){
        return "category";
    }

    public String getAxisName(){
        return "CategoryAxis";
    }

    /**
     * ���������������λ�� ����ռ�õĿռ��� , Ϊ�˸����ݱ�Ԥ��λ��
     */
    public double getPreLeftWidth4DataSheet(int resolution) {
        calculateTitleDimesion(resolution);
        double titlePreWidth = 0;
        if (titleDim.getWidth() > 0) {
            titlePreWidth = titleDim.getWidth() + ChartConstants.AXIS_LINE_LABEL_GAP;
        }
        double labelWidth = getMaxLabelWidthAndInitStartEndLabelDim(resolution);
        return (this.position == Constants.LEFT) ? (labelWidth + titlePreWidth) : 0;
    }

    /**
     * �õ��������ǩ�� ռ��λ�����ı�ǩ�ĳߴ�
     *
     * @param resolution �ֱ���
     */
    protected double getMaxLabelWidthAndInitStartEndLabelDim(int resolution){
        if (!this.isShowAxisLabel) {
            return 0;
        }
        double labelWidth = 0;
        Object[] ob = categoryLabelList.toArray();
        for (int i = 0; i < ob.length; i++) {
            Dimension2D dim = getAxisLabelDim(Utils.objectToString(ob[i]), resolution);
            if (i == 0) {
                startLabelDim = dim;
            } else {
                endLabelDim = dim;
            }

            //���� �߶� ���� ���
            double tmpWidth = getAxisLabelWidth(Utils.objectToString(ob[i]), resolution);
            labelWidth = (tmpWidth < labelWidth ? labelWidth : tmpWidth);
        }
        return labelWidth;
    }

    /**
     * ���������᳤�ȵ� ����.
     */
    protected void calculateAxisLengthUnit() {
        double tickNumber = this.getMaxValue() - this.getMinValue();
        if (tickNumber <= 0) {
            unitLength = 1;
            return;
        }
        // ���ж��Ƿ��ڿ̶��м� ���ǿ̶���
        if (this.isDrawBetweenTick()) {
            unitLength = axisLength / tickNumber;
        } else {
            unitLength = axisLength / Math.max(1, tickNumber - 1);
        }


    }

    /**
     * �������������λ�ã�category��time��value�����drawBetween �򷵻��м�λ��
     * @param ob �ö���
     * @return λ��
     */
    public Point2D getAlertValuePoint(Object ob) {
        double value = getObjectValue(ob);
        if(isDrawBetweenTick()){
            value += HALF;
        }
        return getPointInBounds(value);
    }

    public double getObjectValue(Object ob) {
        for(int index = 0, len = categoryLabelList.size(); index < len; index++){
            if(ComparatorUtils.equals(categoryLabelList.get(index), ob)){
                return index;
            }
        }
        return getCrossValue();
    }

    /**
     * ���������������
     *
     * @param g Graphics
     */
    public void drawAxisGrid(Graphics g) {
        if (this.axisLength <= MIN_TICK_LENGTH) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;

        double maxTickValue = this.getMaxTickValue();
        int tickSamplingTime = this.getTickSamplingTime();

        if (this.getMainGridStyle() != Constants.LINE_NONE && this.getMainUnit() > 0
                && this.getMainGridColor() != null) {
            Stroke oldStoke = g2d.getStroke();
            Paint oldPaint = g2d.getPaint();

            g2d.setPaint(this.getMainGridColor());
            g2d.setStroke(GraphHelper.getStroke(this.getMainGridStyle()));

            for (double value = (getMainUnit() + getCrossValue()) * tickSamplingTime; value <= maxTickValue; value += getMainUnit() * tickSamplingTime) {
                Line2D[] lines = getGridLine(value);
                for (int i = 0; i < lines.length; i++) {
                    g2d.draw(lines[i]);
                }
            }

            g2d.setStroke(oldStoke);
            g2d.setPaint(oldPaint);
        }
    }

    /**
     * �����̶����. �̶��� �̶ȱ�ǩ ��
     */

    protected void drawTicks(Graphics g, int resolution) {
        if (this.axisLength <= MIN_TICK_LENGTH) {
            return;
        }
        BasicStroke mainStroke = new BasicStroke(TICK_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        int tickSamplingTime = getTickSamplingTime();
        boolean isToNumber = this.isToNumber();
        double offset = (this.isDrawBetweenTick() ? 0.5 : 0);
        int labelCrement = ((this.getLabelNumber() > 0) ? this.getLabelNumber() : getLabelSamplingTime(tickSamplingTime, isToNumber, DEFAULT_SAMPLING_TIME, resolution, offset) * tickSamplingTime);
        double maxTickValue = this.getMaxTickValue();

        //��Ҫ�̶�
        int tickLength = MAIN_TICK_LENGTH;
        for (double value = getCrossValue() + labelCrement; value <= maxTickValue; value += labelCrement) {
            Line2D line = getTickLine(value, tickLength, this.getMainTickLine());
            drawTickLine(g, line, mainStroke);
        }
        //��Ҫ�̶�
        if (tickSamplingTime <= 1) {
            for (double value = getCrossValue() + getSecUnit(); value <= maxTickValue; value += getSecUnit()) {
                Line2D line = getTickLine(value, SEC_TICK_LENGTH, this.getSecTickLine());
                drawTickLine(g, line, mainStroke);
            }
        }
       drawTickLabel(g, resolution, labelCrement, isToNumber, offset);
    }

    protected void drawTickLabel(Graphics g, int resolution, int labelCrement, boolean isToNumber, double offset) {
        //ƫ��������ǩ�ڿ̶�֮��ʱ��ƫ��Ϊ0.5����ǩ�ڿ̶��·�ʱ��ƫ��Ϊ0
        if (this.isLabelWrap && labelCrement == 1) { //��ʾ��ʽ������ʾ
            for (int value = 0, categoryLabelListSize = this.categoryLabelList.size(); value < categoryLabelListSize; value += labelCrement) {
                drawLabelWrapWhenNeed(g, value, offset, this.getLabelString(value, isToNumber), resolution);
            }
        } else {
            //����ǩ
            for (int value = 0, categoryLabelListSize = this.categoryLabelList.size(); value < categoryLabelListSize; value += labelCrement) {
                drawLabel(g, value, offset, this.getLabelString(value, isToNumber), resolution);
            }
        }
    }

    public boolean isToNumber() {
        // �ж��Ƿ���ת��Ϊnumber �����е�
        boolean isToNumber = true;
        for (int i = 0, categoryLabelListSize = this.categoryLabelList.size(); i < categoryLabelListSize; i++) {
            Object[] ob = this.categoryLabelList.toArray();
            // �ڲ��ַ��������� ���ֲ��ǵ�ʱ��   ��֤��ͬ���ĸ�ʽ ����ѭ����������
            Number number = Utils.objectToNumber(ob[i], true);
            if (number == null) {
                isToNumber = false;
            }
            if (!isToNumber) {
                break;
            }
        }
        return isToNumber;
    }

    public int getLabelSamplingTime(int tickSamplingTime, boolean isToNumber, int labelSamplingTime, int resolution, double offset) {
        int tmp = tickSamplingTime * labelSamplingTime;
        GeneralPath labelPath = new GeneralPath();
        for (int value = 0, categoryLabelListSize = this.categoryLabelList.size(); value < categoryLabelListSize; value += tmp) {
            String labelString = getLabelString(value, isToNumber);
            Rectangle2D labelBounds = this.getLabelBounds(value, offset, labelString, resolution);
            if (labelPath.intersects(labelBounds)) {
                labelSamplingTime++;
                return getLabelSamplingTime(tickSamplingTime, isToNumber, labelSamplingTime, resolution, offset);
            } else {
                labelPath.append(labelBounds, false);
            }
        }
        return labelSamplingTime;
    }

    public String getLabelString(int value, boolean isToNumber) {
        Object labelObject = this.categoryLabelList.get(value);
        String labelString = Utils.objectToString(labelObject);
        if (format != null && StringUtils.isNotEmpty(labelString)) {
            if (isToNumber) {
                labelString = format.format(Double.valueOf(labelString));
            } else {
                Date date = DateUtils.object2Date(labelObject, true);
                labelString = ((date == null) ? labelString : format.format(date));
            }
        }
        return labelString;
    }

}
