package com.fr.plugin.chart.glyph;

import com.fr.base.background.ColorBackground;
import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrBackground;
import com.fr.chart.base.AttrBorder;
import com.fr.chart.chartglyph.*;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.web.Repository;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by Mitisky on 15/11/17.
 */
public abstract class VanChartPlotGlyph extends PlotGlyph implements VanChartPlotGlyphInterface{
    private static final long serialVersionUID = -2857867893828263672L;
    private boolean monitorRefresh = false;

    //���������Ǵ���condition����ģ�ר���ó����Ǳ������ط�ʹ�ã�ѭ���������
    private AttrLabel defaultAttrLabel;
    private AttrTooltip defaultAttrTooltip;
    private AttrAlpha defaultAttrAlpha;
    private AttrBorder defaultAttrBorder;

    public void setMonitorRefresh(boolean monitorRefresh) {
        this.monitorRefresh = monitorRefresh;
    }

    /**
     * �Ƿ��Ǽ��ˢ��
     * @return �Ƿ��Ǽ��ˢ��
     */
    public boolean isMonitorRefresh() {
        return this.monitorRefresh;
    }

    protected AttrLabel getDefaultAttrLabel() {
        if(defaultAttrLabel == null){
            ConditionAttr attrList = getConditionCollection().getDefaultAttr();
            defaultAttrLabel = (AttrLabel)attrList.getExisted(AttrLabel.class);
        }
        return defaultAttrLabel;
    }

    protected AttrTooltip getDefaultAttrTooltip() {
        if(defaultAttrTooltip == null){
            ConditionAttr attrList = getConditionCollection().getDefaultAttr();
            defaultAttrTooltip = (AttrTooltip)attrList.getExisted(AttrTooltip.class);
        }
        return defaultAttrTooltip;
    }

    protected AttrAlpha getDefaultAttrAlpha() {
        if(defaultAttrAlpha == null) {
            ConditionAttr conditionAttr = getConditionCollection().getDefaultAttr();
            defaultAttrAlpha = (AttrAlpha)conditionAttr.getExisted(AttrAlpha.class);
        }
        return defaultAttrAlpha;
    }

    protected AttrBorder getDefaultAttrBorder() {
        if(defaultAttrBorder == null){
            ConditionAttr conditionAttr = getConditionCollection().getDefaultAttr();
            defaultAttrBorder = (AttrBorder)conditionAttr.getExisted(AttrBorder.class);
        }
        return defaultAttrBorder;
    }

    protected AttrAlpha getAttrAlpha(VanChartDataPoint dataPoint){
        AttrAlpha attrAlpha = dataPoint.getAlpha();
        if(attrAlpha == null){
            attrAlpha = getDefaultAttrAlpha();
        }
        if(attrAlpha == null){
            attrAlpha = new AttrAlpha(1.0f);
        }
        return attrAlpha;
    }

    protected AttrAlpha getAttrAlpha(VanChartDataSeries dataSeries){
        AttrAlpha attrAlpha = dataSeries.getAlpha();
        if(attrAlpha == null){
            attrAlpha = getDefaultAttrAlpha();
        }
        if(attrAlpha == null){
            attrAlpha = new AttrAlpha(1.0f);
        }
        return attrAlpha;
    }

    protected AttrBorder getAttrBorder(VanChartDataPoint dataPoint) {
        AttrBorder attrBorder = dataPoint.getBorder();
        if(attrBorder == null){
            attrBorder = getDefaultAttrBorder();
        }
        if(attrBorder == null){
            attrBorder = new AttrBorder();//����Ϊnull
        }
        return attrBorder;
    }

    protected AttrLabel getAttrLabel(VanChartDataPoint dataPoint) {
        AttrLabel attrLabel = dataPoint.getLabel();
        if(attrLabel == null){
            attrLabel = getDefaultAttrLabel();
        }
        return attrLabel;
    }

    protected AttrBackground getAttrBackground(VanChartDataPoint dataPoint) {
        AttrBackground color = dataPoint.getColor();
        if(color == null){
            color = (AttrBackground)ConditionAttrFactory.createConditionAttr(AttrBackground.class, dataPoint.getSeriesIndex(), createColors4Series());
        }
        return color;
    }

    //ͼ��������ͼ�����ͼ���õ�.��ȡĳ��ϵ�е���ɫ��
    protected Color getAttrLineColor(VanChartDataSeries dataSeries, Color[] colors){
        AttrBackground attrBackground = dataSeries.getColor();
        if(attrBackground != null && attrBackground.getSeriesBackground() instanceof ColorBackground){
            return  ((ColorBackground)attrBackground.getSeriesBackground()).getColor();
        } else {
            return colors[dataSeries.getSeriesIndex()];
        }
    }

    protected void changeInfoWithCondition(GeneralInfo info, VanChartDataPoint dataPoint) {
        AttrBorder attrBorder = getAttrBorder(dataPoint);
        info.changeStyleAttrBorder(attrBorder);

        AttrAlpha attrAlpha = getAttrAlpha(dataPoint);
        info.changeStyleAttrAlpha(attrAlpha);

        AttrBackground attrBackground = getAttrBackground(dataPoint);
        info.changeStyleAttrBackground(attrBackground);
    }

    /**
     * �Ի�ͼ���е������Ჿ�ֽ��в���
     *
     * @param resolution �ֱ���
     */
    public void layoutAxisGlyph(int resolution){
    }

    /**
     *  �Ի�ͼ���е������Ჿ�ֽ��в���,����ȥͼ��߽������������
     * @param chartOriginalBounds ԭʼͼ��߽�
     * @param resolution �ֱ���
     */
    public void layoutAxisGlyph(Rectangle2D chartOriginalBounds, int resolution){
        layoutAxisGlyph(resolution);
    }

    //��ȡ���������е������Сֵ
    protected double[] getMinMaxValue() {
        double[] minMax = new double[]{Double.MAX_VALUE, -Double.MAX_VALUE};
        for(int categoryIndex = 0, categoryCount = getCategoryCount(); categoryIndex < categoryCount; categoryIndex++){
            for(int seriesIndex = 0, seriesSize = getSeriesSize(); seriesIndex < seriesSize; seriesIndex++){
                DataPoint dataPoint = getSeries(seriesIndex).getDataPoint(categoryIndex);
                minMax[0] = Math.min(minMax[0], dataPoint.getValue());
                minMax[1] = Math.max(minMax[1], dataPoint.getValue());
            }
        }
        return minMax;
    }

    /**
     * ����ͼ�ζ���
     * @param resolution �ֱ���
     */
    public void layoutDataSeriesGlyph(int resolution) {
        createDataPointLabelAfterInstallAxisGlyph();
    }

    /**
     * ��ʼ���������ٴ�����ǩ����ΪҪ��ٷֱ�
     */
    public void createDataPointLabelAfterInstallAxisGlyph() {
        calculateDataPointPercentValue();
        createDataLabel4EverySeries();
    }

    protected void createDataLabel4EverySeries() {
        for (int seriesIndex = 0, len = getSeriesSize(); seriesIndex < len; seriesIndex++) {
            DataSeries dataSeries = getSeries(seriesIndex);

            for (int categoryIndex = 0; categoryIndex < dataSeries.getDataPointCount(); categoryIndex++) {
                DataPoint dataPoint = dataSeries.getDataPoint(categoryIndex);

                if (dataPoint.isValueIsNull()) {
                    continue;
                }

                createDataLabel4DataPointWithCondition(dataPoint, getConditionCollection());
            }
        }
    }

    protected void createDataLabel4DataPointWithCondition(DataPoint dataPoint, ConditionCollection conditionCollection) {
        VanChartDataPoint vanChartDataPoint = (VanChartDataPoint)dataPoint;

        AttrLabel attrLabel = (AttrLabel)conditionCollection.getDataSeriesCondition(AttrLabel.class, vanChartDataPoint);

        if(attrLabel == null){
            return;
        }

        String labelText = VanChartAttrHelper.getLabelText(attrLabel.getContent(), vanChartDataPoint);

        TextGlyph textGlyph = new TextGlyph();
        textGlyph.setVisible(true);

        getConditionCollection().changeStyleConditionWithInfo(textGlyph, dataPoint, new Color[0]);

        if (StringUtils.isNotEmpty(labelText)) {
            textGlyph.setText(labelText);
            textGlyph.setVisible(true);
            textGlyph.setTextAttr(attrLabel.getTextAttr());
            dataPoint.setDataLabel(textGlyph);
        }
    }

    public LegendItem[] createLegendItems() {
        int seriesSize = getSeriesSize();
        LegendItem[] items = new LegendItem[seriesSize];

        Color[] colors = createColors4Series();
        for (int seriesIndex = 0, itemIndex = 0; seriesIndex < seriesSize; seriesIndex++, itemIndex++) {
            VanChartDataSeries dataSeries = (VanChartDataSeries)getSeries(seriesIndex);
            items[itemIndex] = new LegendItem(dataSeries.getSeriesName());

            items[itemIndex].setLineMarkerIcon(getLegendMarkerIcon(dataSeries, colors));
        }

        return items;
    }

    protected LineMarkerIcon getLegendMarkerIcon(VanChartDataSeries dataSeries, Color[] colors) {
        LineMarkerIcon itemIcon = new LineMarkerIcon();

        dealMarkerIconCondition(itemIcon, dataSeries, colors);

        return itemIcon;
    }

    protected LineMarkerIcon dealMarkerIconCondition(LineMarkerIcon itemIcon, VanChartDataSeries dataSeries, Color[] colors) {
        itemIcon.setBackground(ColorBackground.getInstance(getAttrLineColor(dataSeries, colors)));
        itemIcon.setAlpha(getAttrAlpha(dataSeries).getAlpha());
        return itemIcon;
    }

    public void draw(Graphics g, int resolution) {
        if(getBounds().getWidth() < 0 || getBounds().getHeight() < 0){
            return;
        }
        drawInfo(g, resolution);
        super.draw(g, resolution);
    }

    /**
     * ������������������������ڵ���ʽ
     *
     * @param g ͼ�ζ���
     */
    protected void drawInfo(Graphics g, int resolution) {
        drawInfo(g);
    }

    /**
     * ϵ��д��json(ϵ�кͷ������һ��)
     * @param js  json����
     * @param repo ����
     * @throws com.fr.json.JSONException �״�
     */
    protected void addSeriesJSONWithCate2Series(JSONObject js, Repository repo) throws JSONException {
        int cateCount = getCategoryCount();
        int seriesCount = getSeriesSize();
        JSONArray cateList = new JSONArray();
        if (cateCount > 0){
            for(int cateIndex = 0; cateIndex < cateCount; cateIndex++){
                JSONObject cateJS = new JSONObject();

                //���pointList�����һ��������������ݵ�
                JSONArray pointList = new JSONArray();
                String cateName = StringUtils.EMPTY;
                for(int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++){
                    DataSeries dataSeries = getSeries(seriesIndex);
                    DataPoint dataPoint = dataSeries.getDataPoint(cateIndex);
                    cateName = dataPoint.getCategoryName();
                    pointList.put(dataPoint.toJSONObject(repo));
                }

                cateJS.put("name", cateName);
                cateJS.put("data", pointList);

                cateList.put(cateJS);
            }
            js.put("series", cateList);
        }
    }

    /**
     * ��ȡ plotOptions��JSON����
     * @param repo ����
     * @param isJsDraw ��̬չʾ
     * @return ����json
     * @throws com.fr.json.JSONException �׳�����
     */
    public JSONObject getPlotOptionsJSON(Repository repo, boolean isJsDraw) throws JSONException {
        JSONObject js = new JSONObject();

        js.put("animation", isJsDraw);

        AttrLabel attrLabel = getDefaultAttrLabel();
        if(attrLabel != null && attrLabel.isEnable()){
           addLabelJSON(js, attrLabel, repo);
        } else {
            addNullLabelJSON(js);
        }
        AttrTooltip attrTooltip = getDefaultAttrTooltip();
        if(attrTooltip != null && attrTooltip.isEnable()){
            js.put("tooltip", attrTooltip.toJSONObject(repo));
        }
        AttrBorder attrBorder =  getDefaultAttrBorder();
        if(attrBorder != null){
            js.put("borderWidth", VanChartAttrHelper.getAxisLineStyle(attrBorder.getBorderStyle()));
            if(attrBorder.getBorderColor() != null){
                js.put("borderColor", StableUtils.javaColorToCSSColor(attrBorder.getBorderColor()));
            } else {
                js.put("borderColor", VanChartAttrHelper.TRANSPARENT_COLOR);
            }
            js.put("borderRadius", attrBorder.getRoundRadius());
        }

        return js;
    }

    protected void addLabelJSON(JSONObject js, AttrLabel attrLabel, Repository repo) throws JSONException {
        js.put("dataLabels", attrLabel.toJSONObject(repo));
    }

    protected void addNullLabelJSON(JSONObject js) throws JSONException{
    }

    //������������֮ǰͼ���л��õģ��µ�ͼ��û�õ���
    public String getPlotGlyphType() {
        return StringUtils.EMPTY;
    }
    public  String getSmallIconType(){
        return StringUtils.EMPTY;
    }
}
