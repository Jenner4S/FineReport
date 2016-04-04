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
     * �����Ƿ�ʹ��html�����ı�
     * @param useHtml �Ƿ�ʹ��html�����ı�
     */
    public void setUseHtml(boolean useHtml) {
        this.useHtml = useHtml;
    }

    /**
     * �����Ƿ�ʹ��html�����ı�
     * @return �Ƿ�ʹ��html�����ı�
     */
    public boolean isUseHtml() {
        return this.useHtml;
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

    /**
     *  ���ֱ��Ⲣˢ��ͼ��ı߽�
     * @param chartBounds ͼ��߽�
     * @param width_gap ��ȼ��
     * @param height_gap �߶ȼ��
     * @param resolution �ֱ���
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
            //�Զ�������λ�� ������ͼ��߽�
            chartBounds.setRect(
                    chartBounds.getX(),
                    chartBounds.getY() + preferredDimension.getHeight() + width_gap,
                    chartBounds.getWidth(),
                    chartBounds.getHeight() - preferredDimension.getHeight() - height_gap
            );
        }
    }

    /**
     * Ԥ������� ռ�õ�Dim, ��Ҫ��� �ж�Gap �� MaxHeight
     * @param resolution �ֱ���
     * @param chartWidth ���
     * @param chartHeight �߶�
     * @return ��С
     */
    public Dimension2D preferredDimension(int resolution, double chartWidth, double chartHeight) {
        String titleText = dealStringAutoWrap(text, textAttr, resolution, chartWidth - ChartConstants.TITLE_GAP * 2);
        Dimension2D textDim = GlyphUtils.calculateTextDimensionWithNoRotation((titleText), this.textAttr, resolution);
        if(isFloating()) {
            //�Զ�������λ�á����������СΪ�ı���ռ�����С
            return new DoubleDimension2D(Math.min(textDim.getWidth() + ChartConstants.TITLE_GAP * 2, chartWidth),
                    Math.min(textDim.getHeight() + ChartConstants.TITLE_GAP * 2, chartHeight));
        } else if(isLimitSize()){

            //���������С�����������С����Ϊchart��������Ϊ����ռ�ȴ�С
            return new DoubleDimension2D(chartWidth, chartHeight * getMaxHeight()/VanChartAttrHelper.PERCENT);
        } else {
            //�����������С�����������С����Ϊchart��������Ϊ�ı�����
            return new DoubleDimension2D(chartWidth, textDim.getHeight() + ChartConstants.TITLE_GAP * 2);
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
