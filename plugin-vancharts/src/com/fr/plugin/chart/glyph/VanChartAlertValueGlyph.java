package com.fr.plugin.chart.glyph;

import com.fr.chart.base.ChartUtils;
import com.fr.chart.base.GlyphUtils;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartglyph.ChartAlertValueGlyph;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.glyph.axis.VanChartBaseAxisGlyph;
import com.fr.plugin.chart.glyph.axis.VanChartCategoryAxisGlyph;
import com.fr.stable.Constants;
import com.fr.stable.StableUtils;
import com.fr.stable.web.Repository;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Mitisky on 15/10/27.
 */
public class VanChartAlertValueGlyph extends ChartAlertValueGlyph {
    private static final long serialVersionUID = 202681693668095575L;
    private VanChartBaseAxisGlyph vanChartBaseAxisGlyph;

    public void setAxisGlyph(VanChartBaseAxisGlyph axisGlyph) {
        this.vanChartBaseAxisGlyph = axisGlyph;
    }

    public VanChartBaseAxisGlyph getAxisGlyph() {
        return vanChartBaseAxisGlyph;
    }

    /**
     * 处理警戒线
     */
    public void dealWithAlertLine(){
        double value = getAlertValue();
        if (value <= this.vanChartBaseAxisGlyph.getMaxValue() && value >= this.vanChartBaseAxisGlyph.getMinValue()) {
            lines = this.vanChartBaseAxisGlyph.getGridLine(value);
        }
    }

    public double getAlertValue() {
        Object ob = vanChartBaseAxisGlyph.getObjectFromFormula(this.getAlertValueFormula());
        double value = vanChartBaseAxisGlyph.getObjectValue(ob);
        if(vanChartBaseAxisGlyph.isDrawBetweenTick()){
            value += VanChartCategoryAxisGlyph.HALF;
        }
        return value;
    }


    //画警戒线的标签
    public void drawAlertValueLabel(Graphics g, int resolution){
        TextAttr textAttr = new TextAttr();
        textAttr.setFRFont(getAlertFont());
        String alertContent = getAlertContent();
        Dimension2D dim = GlyphUtils.calculateTextDimensionWithNoRotation(alertContent, textAttr, resolution);

        for(Line2D line2D : lines){
            double X = 0, Y = 0;
            switch (getAlertPosition()){
                case Constants.TOP:
                    X = line2D.getX1() - dim.getWidth();
                    Y = Math.min(line2D.getY1(), line2D.getY2());
                    break;
                case Constants.BOTTOM:
                    X = line2D.getX1() - dim.getWidth();
                    Y = Math.max(line2D.getY1(), line2D.getY2()) - dim.getHeight();
                    break;
                case Constants.LEFT:
                    X = Math.min(line2D.getX1(), line2D.getX2());
                    Y = line2D.getY1() - dim.getHeight();
                    break;
                case Constants.RIGHT:
                    X = Math.max(line2D.getX1(), line2D.getX2()) - dim.getWidth();
                    Y = line2D.getY1() - dim.getHeight();
                    break;
            }
            labelBounds = new Rectangle2D.Double(X, Y, dim.getWidth(), dim.getHeight());
        }

        GlyphUtils.drawStrings(g, alertContent, textAttr, labelBounds, resolution);
    }

    /**
     * 转为json数据
     *  @param repo 请求
     * @return 返回json
     * @throws com.fr.json.JSONException 抛错
     */
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        JSONObject js = new JSONObject();

        js.put("color", StableUtils.javaColorToCSSColor(getLineColor().getSeriesColor()));
        VanChartAttrHelper.addDashLineStyleJSON(js, getLineStyle().getLineStyle());

        if (getAlertValueFormula() != null && getAlertValueFormula().getResult() != null) {
            js.put("value", getAlertValueFormula().getResult());
        }

        JSONObject labelJS= new JSONObject();
        labelJS.put("align", ChartUtils.getPositionString(getAlertPosition()));
        labelJS.put("text", getAlertContent());

        labelJS.put("style", VanChartAttrHelper.getCSSFontJSONWithFont(getAlertFont()));

        js.put("label", labelJS);

        return js;
    }


}
