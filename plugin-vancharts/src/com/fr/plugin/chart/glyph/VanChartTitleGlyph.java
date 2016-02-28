package com.fr.plugin.chart.glyph;

import com.fr.base.DoubleDimension2D;
import com.fr.base.background.ColorBackground;
import com.fr.base.background.GradientBackground;
import com.fr.chart.base.*;
import com.fr.chart.chartglyph.TitleGlyph;
import com.fr.general.Background;
import com.fr.general.ComparatorUtils;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.stable.Constants;
import com.fr.stable.web.Repository;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Mitisky on 15/8/17.
 */
public class VanChartTitleGlyph extends TitleGlyph{
    private static final long serialVersionUID = 7433581862270748780L;

    private boolean useHtml;

    private boolean floating;
    private double floatPercentX;
    private double floatPercentY;

    private boolean limitSize;
    private double maxHeight;

    public VanChartTitleGlyph() {
        super();
    }

    public VanChartTitleGlyph(String text4Glyph, TextAttr textAttr) {
        super(text4Glyph, textAttr);
    }

    /**
     * 设置是否使用html解析文本
     * @param useHtml 是否使用html解析文本
     */
    public void setUseHtml(boolean useHtml) {
        this.useHtml = useHtml;
    }

    /**
     * 返回是否使用html解析文本
     * @return 是否使用html解析文本
     */
    public boolean isUseHtml() {
        return this.useHtml;
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

    /**
     *  布局标题并刷新图表的边界
     * @param chartBounds 图表边界
     * @param width_gap 宽度间隔
     * @param height_gap 高度间隔
     * @param resolution 分辨率
     */
    public void layoutTitleAndRefreshChartBounds(Rectangle2D chartBounds, double width_gap, double height_gap, int resolution) {
        Dimension2D preferredDimension = preferredDimension(resolution,chartBounds.getWidth(),chartBounds.getHeight());
        double titleX = 0;
        double titleY = 0;
        if(isFloating()){
            titleX = getFloatPercentX() * chartBounds.getWidth()/VanChartAttrHelper.PERCENT;
            titleY = getFloatPercentY() * chartBounds.getHeight()/VanChartAttrHelper.PERCENT;
        } else {
            if(getPosition() == Constants.LEFT) {
                titleX = chartBounds.getX();
            } else if(getPosition() == Constants.RIGHT) {
                titleX = chartBounds.getX() + chartBounds.getWidth() - preferredDimension.getWidth();
            } else {
                titleX = chartBounds.getX() + (chartBounds.getWidth() - preferredDimension.getWidth())/2;
            }
        }

        Rectangle2D titleBounds = new Rectangle2D.Double(titleX, titleY,
                preferredDimension.getWidth(),preferredDimension.getHeight());

        setBounds(ChartBaseUtils.rectangle2RoundRectangle(titleBounds, getRoundRadius()));

        if(!isFloating()){
            //自定义悬浮位置 不更新图表边界
            chartBounds.setRect(
                    chartBounds.getX(),
                    chartBounds.getY() + preferredDimension.getHeight() + width_gap,
                    chartBounds.getWidth(),
                    chartBounds.getHeight() - preferredDimension.getHeight() - height_gap
            );
        }
    }

    /**
     * 预计算标题 占用的Dim, 需要填充 判断Gap 和 MaxHeight
     * @param resolution 分辨率
     * @param chartWidth 宽度
     * @param chartHeight 高度
     * @return 大小
     */
    public Dimension2D preferredDimension(int resolution, double chartWidth, double chartHeight) {
        String titleText = dealStringAutoWrap(text, textAttr, resolution, chartWidth - ChartConstants.TITLE_GAP * 2);
        Dimension2D textDim = GlyphUtils.calculateTextDimensionWithNoRotation((titleText), this.textAttr, resolution);
        if(isFloating()) {
            //自定义悬浮位置。标题区域大小为文本所占区域大小
            return new DoubleDimension2D(Math.min(textDim.getWidth() + ChartConstants.TITLE_GAP * 2, chartWidth),
                    Math.min(textDim.getHeight() + ChartConstants.TITLE_GAP * 2, chartHeight));
        } else if(isLimitSize()){

            //限制区域大小。标题区域大小横向为chart横向，纵向为限制占比大小
            return new DoubleDimension2D(chartWidth, chartHeight * getMaxHeight()/VanChartAttrHelper.PERCENT);
        } else {
            //不限制区域大小。标题区域大小横向为chart横向，总想为文本纵向
            return new DoubleDimension2D(chartWidth, textDim.getHeight() + ChartConstants.TITLE_GAP * 2);
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

        Background background = getBackground();
        if(background != null && ComparatorUtils.equals(background.getBackgroundType(),"ColorBackground")){
            js.put("backgroundColor", VanChartAttrHelper.getRGBAColorWithColorAndAlpha(((ColorBackground)background).getColor(), getAlpha()));
        } else if(background != null && ComparatorUtils.equals(background.getBackgroundType(), "GradientBackground")){
            js.put("backgroundColor", VanChartAttrHelper.getGradientBackgroundJSON((GradientBackground) background, getAlpha()));
        }

        js.put("borderRadius", getRoundRadius());

        if(isFloating()){
            js.put("floating", isFloating());
            js.put("x", getFloatPercentX() + VanChartAttrHelper.STRING_PERCENT);
            js.put("y", getFloatPercentY() + VanChartAttrHelper.STRING_PERCENT);
        } else {
            js.put("align", ChartUtils.getPositionString(getPosition()));
            if(isLimitSize()){
                js.put("maxHeight", getMaxHeight() + VanChartAttrHelper.STRING_PERCENT);
            }
        }

        js.put("text", getText());
        js.put("style", VanChartAttrHelper.getCSSFontJSONWithFont(getTextAttr().getFRFont()));
        js.put("useHtml", isUseHtml());


        return js;
    }
}
