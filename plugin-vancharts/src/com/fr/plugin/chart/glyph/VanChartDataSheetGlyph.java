package com.fr.plugin.chart.glyph;

import com.fr.base.GraphHelper;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.base.GlyphUtils;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartglyph.*;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.gauge.glyph.GaugeGlyphHelper;
import com.fr.plugin.chart.glyph.axis.VanChartBaseAxisGlyph;
import com.fr.stable.Constants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.web.Repository;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.text.Format;

/**
 * Created by Mitisky on 15/10/19.
 */
public class VanChartDataSheetGlyph extends DataSheetGlyph {
    private static final long serialVersionUID = -7424733367816035489L;
    private static final double MAX_WIDTH = 4;//系列标签，如果文字过长，当格子的长度超过整个图表区的1/4时，折行显示，总的折行数不限制

    //配置的font、format、border都用父类dataSheetGlyph的。
    //画图需要的属性都用自己的。因为关于这些父类接口开的不全，统一起见，都重新自己定义一下。

    boolean hasAxisReversed = false;
    private FRFont legendFont;
    private TextAttr cateLabelTextAttr;
    private VanChartDataSheetPlotGlyphInterface plotGlyph;
    private LegendItem[] legendItems;//系列名称和标记点。这里只是使用，不改变其中的属性

    private double seriesWidth;//系列名称的宽度
    private double cateWidth;//分类名称和数据点值的宽度
    private double cateHeight;//分类名称的高度
    private double seriesHeight;//系列名称和数据点值的高度

    public VanChartDataSheetGlyph(FRFont font, Format format, GeneralInfo generalInfo) {
        setFont(font);
        setFormat(format);
        setGeneralInfo(generalInfo);
    }

    //布局第一步：将画数据表需要的属性补充完整。
    //构造的时候只是给了数据表配置的属性，这边将相关的图例、坐标轴、数据点等补充完整。
    public void initDataSheet(LegendGlyph legendGlyph, VanChartDataSheetPlotGlyphInterface plotGlyph){
        VanChartBaseAxisGlyph xAxisGlyph = plotGlyph.getDefaultXAxisGlyph();
        this.hasAxisReversed = xAxisGlyph.hasAxisReversed();
        this.cateLabelTextAttr = xAxisGlyph.getTextAttr();
        //数据表分类名称不旋转
        this.cateLabelTextAttr.setRotation(0);
        if(legendGlyph != null && legendGlyph.isVisible()){
            this.legendFont = legendGlyph.getFont();
            this.legendItems = plotGlyph.createLegendItems();
        } else {
            this.legendFont = getFont();
        }
        this.plotGlyph = plotGlyph;
    }

    //数据表布局第二步：计算系列名的宽度（图例的宽度）
    public void calculateSeriesWidth(Rectangle2D chartOriginalBounds, int resolution) {
        this.seriesHeight = Double.MIN_VALUE;
        this.seriesWidth = Double.MIN_VALUE;
        double limit = chartOriginalBounds.getWidth() / MAX_WIDTH;
        boolean hasLegend = hasLegend();
        for (int i = 0, size = this.plotGlyph.getSeriesSize(); i < size; i++) {
            Dimension2D dim;
            if (hasLegend) {
                dim = this.legendItems[i].prefferedSize(this.legendFont, resolution);
            } else {
                dim = GlyphUtils.calculateTextDimensionWithNoRotation(this.plotGlyph.getSeries(i).getSeriesName(), new TextAttr(this.legendFont), resolution);
            }
            this.seriesWidth = Math.max(this.seriesWidth, dim.getWidth());
            this.seriesWidth = Math.min(this.seriesWidth, limit);
            this.seriesHeight = Math.max(this.seriesHeight, Math.ceil(dim.getWidth()/this.seriesWidth) * dim.getHeight());
        }
        this.seriesWidth += (PADDING_GAP * 2);
    }

    //数据表布局第三步：根据坐标轴的unitLength计算分类标签的高度、系列的高度等。
    public void calculateOtherWidthAndHeight(int resolution) {
        VanChartBaseAxisGlyph xAxisGlyph = this.plotGlyph.getDefaultXAxisGlyph();
        this.cateWidth = xAxisGlyph.getAxisLength()/getCategoryCount();
        calculateCateHeight(resolution);
        calculateSeriesHeight(resolution);
    }

    //计算分类的高度
    private void calculateCateHeight(int resolution) {
        DataSeries dataSeries = this.plotGlyph.getSeries(0);
        if(dataSeries != null){
            for(int i = 0, len = dataSeries.getDataPointCount(); i < len; i++){
                String cateString = dataSeries.getDataPoint(i).getCategoryName();
                Dimension2D dim = GlyphUtils.calculateTextDimensionWithNoRotation(cateString, this.cateLabelTextAttr, resolution);
                this.cateHeight = Math.max(this.cateHeight, Math.ceil(dim.getWidth()/this.cateWidth) * dim.getHeight());
            }
        }
        this.cateHeight += PADDING_GAP;
    }

    //计算系列的宽度
    private void calculateSeriesHeight(int resolution) {
        for (int seriesIndex = 0, seriesSize = this.plotGlyph.getSeriesSize(); seriesIndex < seriesSize; seriesIndex++) {
            DataSeries dataSeries = this.plotGlyph.getSeries(seriesIndex);
            for(int cateIndex = 0, cateSize = dataSeries.getDataPointCount(); cateIndex < cateSize; cateIndex++ ){
                double value = dataSeries.getDataPoint(cateIndex).getValue();
                String valueString = VanChartAttrHelper.getValueStringWithFormat(value, this.format);
                Dimension2D dim = GlyphUtils.calculateTextDimensionWithNoRotation(valueString, new TextAttr(getFont()), resolution);
                this.seriesHeight = Math.max(this.seriesHeight, Math.ceil(dim.getWidth()/this.cateWidth) * dim.getHeight());
            }
        }
        this.seriesHeight += (PADDING_GAP * 2);
    }

    //数据表左边占用宽度。因为会影响到绘图区的布局
    public double getLeftWidth() {
        return this.seriesWidth;
    }

    //数据表下边占用宽度。因为会影响到绘图区的布局
    public double getBottomHeight() {
        return this.cateHeight + this.seriesHeight * this.plotGlyph.getSeriesSize();
    }

    //数据表的总宽度
    public double getWholeWidth() {
        if(this.plotGlyph.getSeriesSize() < 1){
            return 0;
        }
        DataSeries dataSeries = this.plotGlyph.getSeries(0);
        return this.seriesWidth + this.cateWidth * dataSeries.getDataPointCount();
    }

    /**
     * 数据表在设计器上的画法
     * @param resolution 分辨率
     * @param g 图形对象
     */
    public void draw(Graphics g, int resolution) {
        if (this.plotGlyph.getSeriesSize() < 1) {
            return;
        }
        //画的过程就不用考虑没有系列的情况了。
        drawLine(g);
        drawCateLabel(g, resolution);
        drawDataPointValue(g, resolution);
        drawSeriesLabel(g, resolution);
    }

    //画所有的线
    private void drawLine(Graphics g) {
        int borderStyle = getBorderStyle();
        if(borderStyle == Constants.LINE_NONE){
            return;
        }
        Color oldColor = g.getColor();
        g.setColor(getBorderColor());

        Rectangle2D maxBounds = this.getBounds();
        // 画纵线，第一个分类左侧到最后一个分类右侧
        for (double x = maxBounds.getX() + seriesWidth; x <= maxBounds.getMaxX() + 1; x += cateWidth) {
            this.newLine(g, x, maxBounds.getY(), x, maxBounds.getMaxY(), borderStyle);
        }

        // 画横线, 第一个系列的上侧开始, 最后一个系列的下侧终止.
        for (double y = maxBounds.getY() + cateHeight; y <= maxBounds.getMaxY() + 1; y += seriesHeight) {
            this.newLine(g, maxBounds.getX(), y, maxBounds.getMaxX(), y, borderStyle);
        }

        //画最上面一根线
        this.newLine(g, maxBounds.getX() + seriesWidth, maxBounds.getY(), maxBounds.getMaxX(), maxBounds.getY(), borderStyle);
        //画最左边一根线
        this.newLine(g, maxBounds.getX(), maxBounds.getY() + cateHeight, maxBounds.getX(), maxBounds.getMaxY(), borderStyle);

        g.setColor(oldColor);
    }

    private void newLine(Graphics g, double x, double y, double x1, double y1, int borderStyle) {
        Line2D line = new Line2D.Double(x, y, x1, y1);
        GraphHelper.draw(g, line, borderStyle);
    }

    /**
     * 系列的数量
     */
    public int getSeriesSize() {
        return this.plotGlyph.getSeriesSize();
    }

    /**
     * 返回指定位置上的系列
     *
     * @param index 要返回的系列的索引
     */
    protected DataSeries getSeries(int index) {
        return this.plotGlyph.getSeries(index);
    }

    /**
     * 返回系列的数量
     */
    public int getCategoryCount() {
        return getSeries(0) == null ? 0 : getSeries(0).getDataPointCount();
    }

    private double getXWithCateIndex(int cateIndex, int cateCount) {
        return getBounds().getX() + seriesWidth + cateWidth * (hasAxisReversed ?  (cateCount - 1 - cateIndex): cateIndex);
    }

    //居中处理
    private Rectangle2D adjustStringBounds(double tempX, double y, double width, double height, String string, TextAttr textAttr, int resolution) {
        Dimension2D dim = GlyphUtils.calculateTextDimensionWithNoRotation(string, textAttr, resolution);
        if(dim.getWidth() < width){//一行的画居中，多行就不管啦
            tempX += (width - dim.getWidth())/2;
            width = dim.getWidth();
        }
        return new Rectangle2D.Double(tempX, y, width, height);
    }

    //画分类名称
    private void drawCateLabel(Graphics g, int resolution) {
        DataSeries dataSeries = getSeries(0);
        double y = getBounds().getY();
        for(int cateIndex = 0, cateCount = getCategoryCount(); cateIndex < cateCount; cateIndex++){
            String cateString = dataSeries.getDataPoint(cateIndex).getCategoryName();
            double tempX = getXWithCateIndex(cateIndex, cateCount);
            Rectangle2D labelBounds = adjustStringBounds(tempX, y, cateWidth, cateHeight, cateString, cateLabelTextAttr, resolution);
            GlyphUtils.drawStrings(g, cateString, cateLabelTextAttr, labelBounds, resolution);
        }
    }

    //画各个标记点的值
    private void drawDataPointValue(Graphics g, int resolution) {
        TextAttr textAttr = new TextAttr(getFont());
        double tempY = getBounds().getY() + cateHeight;
        for (int seriesIndex = 0, seriesSize = this.plotGlyph.getSeriesSize(); seriesIndex < seriesSize; seriesIndex++) {
            DataSeries dataSeries = this.plotGlyph.getSeries(seriesIndex);
            for(int cateIndex = 0, cateSize = dataSeries.getDataPointCount(); cateIndex < cateSize; cateIndex++ ){
                DataPoint dataPoint = dataSeries.getDataPoint(cateIndex);
                double value = dataPoint.getValue();
                String valueString = VanChartAttrHelper.getValueStringWithFormat(value, this.format);
                if(dataPoint.isValueIsNull()){
                    valueString = "-";
                }
                double tempX = getXWithCateIndex(cateIndex, cateSize);
                Rectangle2D valueBounds = adjustStringBounds(tempX, tempY, cateWidth, seriesHeight, valueString, textAttr, resolution);
                GlyphUtils.drawStrings(g, valueString, textAttr, valueBounds, resolution);
            }
            tempY += seriesHeight;
        }
    }

    private boolean hasLegend() {
        return legendItems != null && legendItems.length == getSeriesSize();
    }

    //画系列名称
    private void drawSeriesLabel(Graphics g, int resolution) {
        Rectangle2D bounds = this.getBounds();

        TextAttr textAttr = new TextAttr(legendFont);
        int seriesCount = getSeriesSize();
        double tempY = getBounds().getY() + cateHeight;
        boolean hasLegend = hasLegend();
        for (int i = 0; i < seriesCount; i++) {
            String seriesName = getSeries(i).getSeriesName();
            if (hasLegend) {
                double oneLineHeight = GaugeGlyphHelper.calculateOneLineHeight(legendFont);
                int iconSize = legendFont.getShowSize(resolution);
                String firstLineString = LegendItem.getTextWhenNotEnoughWidth(seriesName, legendFont.applyResolutionNP(resolution), seriesWidth);
                int index = firstLineString.lastIndexOf("...");
                int length = firstLineString.length();
                if(index == length - 3){//多行
                    firstLineString = firstLineString.substring(0, length - 3);
                    if(ComparatorUtils.equals(firstLineString, seriesName)){
                        drawOneLineLegend(i, g, tempY, oneLineHeight, resolution);
                        tempY += seriesHeight;
                        continue;
                    }
                    legendItems[i].setLabel(StringUtils.EMPTY);
                    legendItems[i].setBounds(new Rectangle2D.Double(bounds.getX() + PADDING_GAP, tempY, seriesWidth, oneLineHeight));
                    legendItems[i].draw(g, legendFont, resolution);
                    legendItems[i].setLabel(seriesName);
                    Rectangle2D firstLineStringBounds = new Rectangle2D.Double(bounds.getX() + PADDING_GAP + iconSize, tempY, seriesWidth - iconSize, oneLineHeight);
                    GlyphUtils.drawStrings(g, firstLineString, textAttr, firstLineStringBounds, resolution);
                    String otherLineString = seriesName.replace(firstLineString, StringUtils.EMPTY);
                    Rectangle2D otherLineStringBounds = new Rectangle2D.Double(bounds.getX() + PADDING_GAP, tempY + oneLineHeight, seriesWidth, seriesHeight - oneLineHeight);
                    GlyphUtils.drawStrings(g, otherLineString, textAttr, otherLineStringBounds, resolution);
                } else {//一行显示的下
                   drawOneLineLegend(i, g, tempY, oneLineHeight, resolution);
                }
            } else {
                Rectangle2D seriesBounds = adjustStringBounds(bounds.getX(), tempY, seriesWidth, seriesHeight, seriesName, textAttr, resolution);
                GlyphUtils.drawStrings(g, seriesName, textAttr, seriesBounds, resolution);
            }
            tempY += seriesHeight;
        }
    }

    private void drawOneLineLegend(int i, Graphics g, double tempY, double oneLineHeight, int resolution) {
        legendItems[i].setBounds(new Rectangle2D.Double(bounds.getX() + PADDING_GAP, tempY + (seriesHeight - oneLineHeight)/2, seriesWidth, oneLineHeight + PADDING_GAP));
        boolean isCut = legendItems[i].isDrawInCut();
        legendItems[i].setDrawInCut(true);
        legendItems[i].draw(g, legendFont, resolution);
        legendItems[i].setDrawInCut(isCut);
    }

    /**
     * 转为json数据
     *  @param repo 请求
     * @return 返回json
     * @throws com.fr.json.JSONException 抛错
     */
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        JSONObject js = new JSONObject();

        js.put("enabled", true);//有此Glyph，说明数据表被勾上了

        if(getBorderColor() != null){
            js.put("borderColor", StableUtils.javaColorToCSSColor(getBorderColor()));
        } else {
            js.put("borderColor", VanChartAttrHelper.TRANSPARENT_COLOR);
        }
        js.put("borderWidth", VanChartAttrHelper.getAxisLineStyle(getBorderStyle()));

        js.put("style", VanChartAttrHelper.getCSSFontJSONWithFont(getFont()));

        JSONObject formatJS = new JSONObject();
        formatJS.put("format", ChartBaseUtils.format2JS(getFormat()));
        js.put("formatter", formatJS);

        return js;
    }

}
