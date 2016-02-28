package com.fr.plugin.chart.glyph;

import com.fr.base.background.ColorBackground;
import com.fr.base.background.GradientBackground;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.base.ChartUtils;
import com.fr.chart.chartglyph.LegendGlyph;
import com.fr.general.Background;
import com.fr.general.ComparatorUtils;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.stable.Constants;
import com.fr.stable.StableUtils;
import com.fr.stable.web.Repository;

import java.awt.geom.Rectangle2D;

/**
 * Created by Mitisky on 15/8/18.
 */
public class VanChartLegendGlyph extends LegendGlyph {

    private static final long serialVersionUID = -4016910054344665747L;

    private boolean floating;
    private double floatPercentX;
    private double floatPercentY;

    private boolean limitSize;
    private double maxHeight;

    //����ʱ���ٳ�ʼÿ��ͼ��Ԫ��ͼ��
    public VanChartLegendGlyph(){
    }

    /**
     * �����Ƿ��Զ�������λ��
     * @param floating �����Ƿ��Զ�������λ��
     */
    public void setFloating(boolean floating) {
        this.floating = floating;
    }

    /**
     * �����Ƿ��Զ�������λ��
     * @return �����Ƿ��Զ�������λ��
     */
    public boolean isFloating() {
        return floating;
    }

    /**
     * ��������λ�� �������ϽǺ���λ��
     * @param x ����λ��
     */
    public void setFloatPercentX(double x) {
        this.floatPercentX = x;
    }

    /**
     * ��������λ�� �������ϽǺ���λ��
     * @return ����λ��
     */
    public double getFloatPercentX() {
        return floatPercentX;
    }

    /**
     * ��������λ�� �������Ͻ�����λ��
     * @param y ����λ��
     */
    public void setFloatPercentY(double y) {
        this.floatPercentY = y;
    }

    /**
     * ��������λ�� �������Ͻ�����λ��
     * @return ����λ��
     */
    public double getFloatPercentY() {
        return floatPercentY;
    }

    /**
     * �����Ƿ����������С
     * @param limitSize �Ƿ����������С
     */
    public void setLimitSize(boolean limitSize) {
        this.limitSize = limitSize;
    }

    /**
     * �����Ƿ����������С
     * @return �����Ƿ����������С
     */
    public boolean isLimitSize() {
        return limitSize;
    }

    /**
     * �����������ռ��
     * @param maxHeight �������ռ��
     */
    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
    }

    /**
     * �����������ռ��
     * @return �����������ռ��
     */
    public double getMaxHeight() {
        return maxHeight;
    }

    protected void setLegendBounds(Rectangle2D legendBounds){
        this.setBounds(ChartBaseUtils.rectangle2RoundRectangle(legendBounds, this.getRoundRadius()));
    }

    protected void refreshChartBoundsWithLegendBounds(Rectangle2D chartBounds, Rectangle2D legendBounds, double widthGap, double heightGap) {
        if(isFloating()){
            return;
        }
        super.refreshChartBoundsWithLegendBounds(chartBounds, legendBounds, widthGap, heightGap);
    }


    private boolean limitHeight() {
        return getPosition() == Constants.TOP || getPosition() == Constants.BOTTOM;
    }

    private boolean limitWidth() {
        return getPosition() == Constants.RIGHT || getPosition() == Constants.RIGHT_TOP || getPosition() == Constants.LEFT;
    }

    protected double getLegendX(Rectangle2D chartOriginalBounds, Rectangle2D chartBounds, int resolution) {
        if(isFloating()){
            return chartOriginalBounds.getX() + chartOriginalBounds.getWidth() * this.getFloatPercentX()/VanChartAttrHelper.PERCENT;
        }
        return super.getLegendX(chartOriginalBounds, chartBounds, resolution);
    }

    protected double getLegendY(Rectangle2D chartOriginalBounds, Rectangle2D chartBounds, int resolution) {
        if(isFloating()){
            return chartOriginalBounds.getY() + chartOriginalBounds.getHeight() * this.getFloatPercentY()/VanChartAttrHelper.PERCENT;
        }
        return super.getLegendY(chartOriginalBounds, chartBounds,resolution);
    }

    protected double getLegendWidth(Rectangle2D chartBounds, int resolution) {
        if(isFloating()){
            return super.getLegendWidth(chartBounds, resolution);
        } else if(isLimitSize() && limitWidth()){
            return chartBounds.getWidth() * getMaxHeight()/VanChartAttrHelper.PERCENT;
        } else {
            return super.getLegendWidth(chartBounds, resolution);
        }
    }

    protected double getLegendHeight(Rectangle2D chartBounds, int resolution) {
        if(isFloating()){
            return super.getLegendHeight(chartBounds, resolution);
        } else if(isLimitSize() && limitHeight()){
            return chartBounds.getHeight() * getMaxHeight()/VanChartAttrHelper.PERCENT;
        } else {
            return super.getLegendHeight(chartBounds, resolution);
        }
    }

    /**
     * תΪjson����
     *  @param repo ����
     * @return ����json
     * @throws com.fr.json.JSONException �״�
     */
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        JSONObject js = new JSONObject();

        js.put("enabled",isVisible());

        if(!isVisible()) {
            return js;
        }

        if(getBorderColor() != null){
            js.put("borderColor", StableUtils.javaColorToCSSColor(getBorderColor()));
        } else {
            js.put("borderColor", VanChartAttrHelper.TRANSPARENT_COLOR);
        }
        js.put("borderWidth", VanChartAttrHelper.getAxisLineStyle(getBorderStyle()));
        js.put("borderRadius", getRoundRadius());


        Background background = getBackground();
        if(background != null && ComparatorUtils.equals(background.getBackgroundType(), "ColorBackground")){
            js.put("backgroundColor", VanChartAttrHelper.getRGBAColorWithColorAndAlpha(((ColorBackground) background).getColor(), getAlpha()));
        } else if(background != null && ComparatorUtils.equals(background.getBackgroundType(), "GradientBackground")){
            js.put("backgroundColor", VanChartAttrHelper.getGradientBackgroundJSON((GradientBackground) background, getAlpha()));
        }
        js.put("shadow", isShadow());

        if(isFloating()){
            js.put("floating", isFloating());
            js.put("x", getFloatPercentX() + VanChartAttrHelper.STRING_PERCENT);
            js.put("y", getFloatPercentY() + VanChartAttrHelper.STRING_PERCENT);
        } else {
            js.put("position", ChartUtils.getPositionString(getPosition()));
            if(isLimitSize()){
                if(getPosition() == Constants.TOP || getPosition() == Constants.BOTTOM){
                    js.put("maxHeight", getMaxHeight() + VanChartAttrHelper.STRING_PERCENT);
                } else {
                    js.put("maxWidth", getMaxHeight() + VanChartAttrHelper.STRING_PERCENT);
                }
            }
        }

        js.put("style", VanChartAttrHelper.getCSSFontJSONWithFont(getFont()));

        return js;
    }
}
