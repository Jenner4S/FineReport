package com.fr.plugin.chart.vanchart;

import com.fr.base.background.ColorBackground;
import com.fr.base.background.GradientBackground;
import com.fr.base.background.ImageBackground;
import com.fr.chart.chartglyph.ChartGlyph;
import com.fr.chart.chartglyph.PlotGlyph;
import com.fr.general.Background;
import com.fr.general.ComparatorUtils;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.attr.VanChartTools;
import com.fr.plugin.chart.attr.VanChartZoom;
import com.fr.plugin.chart.glyph.VanChartAxisPlotGlyphInterface;
import com.fr.stable.StableUtils;
import com.fr.stable.web.Repository;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Map;

/**
 * Created by Mitisky on 15/8/14.
 */
public class VanChartGlyph extends ChartGlyph {

    private static final long serialVersionUID = 2594623742395993399L;

    private VanChartTools vanChartTools;

    private VanChartZoom vanChartZoom;

    public void setVanChartTools(VanChartTools vanChartTools) {
        this.vanChartTools = vanChartTools;
    }

    public void setVanChartZoom(VanChartZoom vanChartZoom) {
        this.vanChartZoom = vanChartZoom;
    }

    protected void makeSureBounds(double borderSize, int width, int height) {
        this.setBounds(new RoundRectangle2D.Double(0, 0, width - borderSize, height - borderSize, getRoundRadius(), getRoundRadius()));
    }

    protected void chartDoLayout(int resolution) {
        VanChartGlyphLayout.doLayout(this, resolution);
    }

    protected void addRefresh2JSON(Map<String, Object> chartAttrMap) {
        PlotGlyph plotGlyph = getPlotGlyph();
        if(plotGlyph != null) {
            chartAttrMap.put("autoRefreshTime", plotGlyph.getAutoRefreshPerSecond());
//            if(((VanChartPlotGlyphInterface)plotGlyph).isMonitorRefresh()){
//                chartAttrMap.put("animation", "monitor");
//            }
        }
    }

    /**
     * �������������Ҫ��JSON����
     * @param repo ����.
     * @return ����json����
     * @throws JSONException  �׳�����
     */
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        //��֮ǰ������
        if (getPlotGlyph()!= null && getPlotGlyph().isNeedDealHotHyperlink()) {
            getPlotGlyph().dealPlotHotAttr(repo);
        }

        JSONObject js = new JSONObject();

        if(getTitleGlyph() != null && getTitleGlyph().isVisible()){
            js.put("title", getTitleGlyph().toJSONObject(repo));
        }
        if(getLegendGlyph() != null){
            js.put("legend", getLegendGlyph().toJSONObject(repo));
        }
        if(getDataSheetGlyph() != null){
            js.put("dataSheet", getDataSheetGlyph().toJSONObject(repo));
        }

        addChartBackgroundAndBorder(js, repo);
        js.put("tools", vanChartTools.toJSONObject());

        if(vanChartZoom != null){
            js.put("zoom", vanChartZoom.toJSONObject());
        }

        PlotGlyph plotGlyph = getPlotGlyph();

        if(plotGlyph == null){
            return js;
        }

        js.put("chartType", plotGlyph.getChartType());

        js.put("style", VanChartAttrHelper.getPlotStyle(plotGlyph.getPlotStyle()));

        addColorsJSON(plotGlyph, js);

        addPlotBackgroundAndBorder(plotGlyph, js, repo);

        plotGlyph.addSeriesJSON(js, repo);

        js.put("plotOptions", plotGlyph.getPlotOptionsJSON(repo, isJSDraw()));

        if(plotGlyph instanceof VanChartAxisPlotGlyphInterface){
            ((VanChartAxisPlotGlyphInterface) plotGlyph).addXAxisJSON(js, repo);
            ((VanChartAxisPlotGlyphInterface) plotGlyph).addYAxisJSON(js, repo);
        }

        return js;
    }

    /**
     * ͼ��������д��json
     * @param js  json����
     * @param repo ����
     * @throws JSONException �״�
     */
    public void addChartBackgroundAndBorder(JSONObject js, Repository repo) throws JSONException {
        Background background = getBackground();
        if(background != null && ComparatorUtils.equals(background.getBackgroundType(), "ColorBackground")){
            js.put("backgroundColor", VanChartAttrHelper.getRGBAColorWithColorAndAlpha(((ColorBackground) background).getColor(), getAlpha()));
        } else if(background != null && ComparatorUtils.equals(background.getBackgroundType(), "GradientBackground")) {
            js.put("backgroundColor", VanChartAttrHelper.getGradientBackgroundJSON((GradientBackground)background, getAlpha()));
        } else if(background != null && ComparatorUtils.equals(background.getBackgroundType(), "ImageBackground")){
            js.put("backgroundImage", VanChartAttrHelper.getImageBackground((ImageBackground) background, repo));
        }
        js.put("shadow", isShadow());

        if(getBorderColor() != null){
            js.put("borderColor", StableUtils.javaColorToCSSColor(getBorderColor()));
        } else {
            js.put("borderColor", VanChartAttrHelper.TRANSPARENT_COLOR);
        }
        js.put("borderWidth", VanChartAttrHelper.getAxisLineStyle(getBorderStyle()));
        js.put("borderRadius", getRoundRadius());
    }

    /**
     * ��ɫд��json
     * @param plotGlyph  ��ͼ����
     * @param js  json����
     * @throws JSONException �״�
     */
    public void addColorsJSON(PlotGlyph plotGlyph, JSONObject js) throws JSONException{
        //Ĭ����ɫ�Ͳ�����
        //������Ĭ����ɫҲ�ô�����ΪĬ�ϵĿ������Զ����Ԥ�����һ����ɫ
        Color[] colors = plotGlyph.createColors4Series();
        JSONArray array = new JSONArray();
        for(Color color : colors) {
            if(color != null) {
                array.put(StableUtils.javaColorToCSSColor(color));
            }
        }
        js.put("colors", array);
    }

    /**
     * ��ͼ������д��json
     * @param plotGlyph  ��ͼ����
     * @param js  json����
     * @param repo ����
     * @throws JSONException �״�
     */
    public void addPlotBackgroundAndBorder(PlotGlyph plotGlyph, JSONObject js, Repository repo) throws JSONException {
        Background background = plotGlyph.getBackground();
        if(background != null && ComparatorUtils.equals(background.getBackgroundType(), "ColorBackground")){
            js.put("plotBackgroundColor", VanChartAttrHelper.getRGBAColorWithColorAndAlpha(((ColorBackground) background).getColor(), plotGlyph.getAlpha()));
        } else if(background != null && ComparatorUtils.equals(background.getBackgroundType(), "GradientBackground")) {
            js.put("plotBackgroundColor", VanChartAttrHelper.getGradientBackgroundJSON((GradientBackground) background, plotGlyph.getAlpha()));
        } else if(background != null && ComparatorUtils.equals(background.getBackgroundType(), "ImageBackground")) {
            js.put("plotBackgroundImage", VanChartAttrHelper.getImageBackground((ImageBackground) background, repo));
        }
        js.put("plotShadow", plotGlyph.isShadow());

        if(plotGlyph.getBorderColor() != null){
            js.put("plotBorderColor", StableUtils.javaColorToCSSColor(plotGlyph.getBorderColor()));
        } else {
            js.put("plotBorderColor", VanChartAttrHelper.TRANSPARENT_COLOR);
        }
        js.put("plotBorderWidth", VanChartAttrHelper.getAxisLineStyle(plotGlyph.getBorderStyle()));
        js.put("plotBorderRadius", plotGlyph.getRoundRadius());
    }

}
