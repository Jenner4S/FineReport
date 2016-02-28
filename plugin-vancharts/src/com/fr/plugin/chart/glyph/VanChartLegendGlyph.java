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

    //画的时候再初始每个图例元素图形
    public VanChartLegendGlyph(){
    }

    /**
     * 设置是否自定义悬浮位置
     * @param floating 设置是否自定义悬浮位置
     */
    public void setFloating(boolean floating) {
        this.floating = floating;
    }

    /**
     * 返回是否自定义悬浮位置
     * @return 返回是否自定义悬浮位置
     */
    public boolean isFloating() {
        return floating;
    }

    /**
     * 设置悬浮位置 距离左上角横向位置
     * @param x 横向位置
     */
    public void setFloatPercentX(double x) {
        this.floatPercentX = x;
    }

    /**
     * 返回悬浮位置 距离左上角横向位置
     * @return 横向位置
     */
    public double getFloatPercentX() {
        return floatPercentX;
    }

    /**
     * 设置悬浮位置 距离左上角纵向位置
     * @param y 纵向位置
     */
    public void setFloatPercentY(double y) {
        this.floatPercentY = y;
    }

    /**
     * 返回悬浮位置 距离左上角纵向位置
     * @return 纵向位置
     */
    public double getFloatPercentY() {
        return floatPercentY;
    }

    /**
     * 设置是否限制区域大小
     * @param limitSize 是否限制区域大小
     */
    public void setLimitSize(boolean limitSize) {
        this.limitSize = limitSize;
    }

    /**
     * 返回是否限制区域大小
     * @return 返回是否限制区域大小
     */
    public boolean isLimitSize() {
        return limitSize;
    }

    /**
     * 设置区域最大占比
     * @param maxHeight 区域最大占比
     */
    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
    }

    /**
     * 返回区域最大占比
     * @return 返回区域最大占比
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
     * 转为json数据
     *  @param repo 请求
     * @return 返回json
     * @throws com.fr.json.JSONException 抛错
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
