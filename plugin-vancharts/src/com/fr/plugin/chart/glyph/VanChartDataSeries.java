package com.fr.plugin.chart.glyph;

import com.fr.base.background.ColorBackground;
import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrBackground;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartglyph.DataSeries;
import com.fr.chart.chartglyph.FoldLine;
import com.fr.data.condition.CommonCondition;
import com.fr.data.core.Compare;
import com.fr.general.ComparatorUtils;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.AttrBand;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.base.*;
import com.fr.stable.ArrayUtils;
import com.fr.stable.web.Repository;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitisky on 15/8/20.
 */
public class VanChartDataSeries extends DataSeries {

    private static final long serialVersionUID = 2578466024949051678L;

    //下面这几个都是条件属性设置的，有可能为nil，用于传到前台
    //因为下面这下条件属性本来就是可能没有的，所以都放在dataSeries里了，并没有再根据不同图表写子类。
    private VanChartAttrLine attrLine;//折线线型相关设置
    private AttrBackground color;//配色，折线图配色只取到系列，数据点的配色没有任何意义。前台的图例也要用到。
    private AttrAlpha alpha;//透明度，前台图例要用。
    private VanChartAttrMarker marker;//系列这边也要传，图例要用到
    private AttrAreaSeriesFillColorBackground fillColorBackground;//填充色，面积图填充颜色只取到系列，数据点的填充色没有任何意义。
    private AttrSeriesStackAndAxis attrSeriesStackAndAxis;//堆积和坐标轴（多坐标轴情况）
    private VanChartAttrTrendLine attrTrendLine;//趋势线
    private List<AttrBand> bands = new ArrayList<AttrBand>();

    private List<VanChartBandGlyph> bandGlyphs = new ArrayList<VanChartBandGlyph>();

    //只有取bands得时候置为true，忽略条件属性中关于系列的值的判断，以获取到配色
    private boolean evalValue = false;

    private FoldLine areaTopLine = null;//面积图最上面的一根线

    public void setAreaTopLine(FoldLine areaTopLine) {
        this.areaTopLine = areaTopLine;
    }

    public void setAttrTrendLine(VanChartAttrTrendLine attrTrendLine) {
        this.attrTrendLine = attrTrendLine;
    }

    public void setAttrSeriesStackAndAxis(AttrSeriesStackAndAxis attrSeriesStackAndAxis) {
        this.attrSeriesStackAndAxis = attrSeriesStackAndAxis;
    }

    public AttrSeriesStackAndAxis getAttrSeriesStackAndAxis(){
        return this.attrSeriesStackAndAxis;
    }

    public void setAttrLine(VanChartAttrLine attrLine) {
        this.attrLine = attrLine;
    }

    public void setColor(AttrBackground color) {
        this.color = color;
    }

    public void setAlpha(AttrAlpha alpha) {
        this.alpha = alpha;
    }

    public void setMarker(VanChartAttrMarker marker) {
        this.marker = marker;
    }

    public AttrBackground getColor() {
        return this.color;
    }

    public void setFillColorBackground(AttrAreaSeriesFillColorBackground fillColorBackground) {
        this.fillColorBackground = fillColorBackground;
    }

    public AttrAreaSeriesFillColorBackground getFillColorBackground() {
        return this.fillColorBackground;
    }

    public VanChartAttrLine getAttrLine() {
        return this.attrLine;
    }

    public VanChartAttrMarker getMarker() {
        return this.marker;
    }

    public VanChartAttrTrendLine getAttrTrendLine() {
        return this.attrTrendLine;
    }

    public AttrAlpha getAlpha() {
        return alpha;
    }

    /**
     * 向该系列增加bands属性
     * @param bands bands属性
     */
    public void addAttrBands(AttrBand bands){
        this.bands.add(bands);
    }


    /**
     * 向该系列增加bands图形
     * @param bandGlyph bands图形
     */
    public void addBandGlyph(VanChartBandGlyph bandGlyph){
        this.bandGlyphs.add(bandGlyph);
    }

    public List<AttrBand> getBands() {
        return this.bands;
    }

    public void setEvalValue(boolean evalValue) {
        this.evalValue = evalValue;
    }

    /**
     * 给band赋默认值
     * @param min 最小值
     * @param max 最大值
     */
    public void initBandsDefaultMinMaxValue(double min, double max){
        if(bands == null || bands.size() < 1){
            return;
        }
        for(AttrBand band : bands){
            //相应的值不一定会改变，只有条件属性没有设置才会替换
            band.setMaxEval(min);//不要写反了
            band.setMinEval(max);
        }
    }

    public VanChartDataSeries(int seriesIndex){
        super(seriesIndex);
    }

    /**
     * 画系列背景、形状等.
     *
     * @param g          图形对象
     * @param resolution 分辨率
     */
    public void draw(Graphics g, int resolution) {
        Rectangle2D seriesBounds = g.getClipBounds();
        List<Rectangle2D> seriesBoundsList = getDataSeriesRecs(seriesBounds);
        if(seriesBoundsList.isEmpty()){
          drawImplAndAreaTopLine(g, resolution);
        } else {
            for(Rectangle2D bounds : seriesBoundsList){
                Shape oldClip = g.getClip();
                g.clipRect((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());
                drawImplAndAreaTopLine(g, resolution);
                g.setClip(oldClip);
            }
        }

        for(VanChartBandGlyph glyph : bandGlyphs){
            glyph.draw(g, resolution);
        }

        for (int i = 0, len = getDataPointCount(); i < len; i++) {
            getDataPoint(i).draw(g, resolution);
        }
    }

    private void drawImplAndAreaTopLine(Graphics g, int resolution){
        if (drawImpl != null) {
            drawImpl.draw(g, resolution);
        }

        if (areaTopLine != null){
            areaTopLine.draw(g, resolution);
        }
    }

    //面积图这边有可能设置某块儿区域.所以系列就要在剩下的地方画，避免重复画导致重叠处band颜色不准确
    private List<Rectangle2D> getDataSeriesRecs(Rectangle2D wholeBounds) {
        List<Rectangle2D> seriesList = new ArrayList<Rectangle2D>();
        List<VanChartBandGlyph> bandsList = dealBandsBoundsList();//互不相干的区域。
        if(bandsList.isEmpty()){
            return seriesList;
        } else {
            double[] result = sortY(bandsList);
            double width = wholeBounds.getWidth();
            double x = wholeBounds.getX();
            double minY = wholeBounds.getMinY();
            double maxY = wholeBounds.getMaxY();
            int len = result.length;
            seriesList.add(new Rectangle2D.Double(x, minY, width, result[0] - minY));
            seriesList.add(new Rectangle2D.Double(x, result[len -1], width, maxY - result[len - 1]));
            for(int i = 1; i < len - 1; i+=2){
                double y1 = result[i];
                double y2 = result[i + 1];
                seriesList.add(new Rectangle2D.Double(x, y1, width, y2 - y1));
            }
        }
        return seriesList;
    }

    //先把bands之间包含重叠的部分处理成互不相干的区域。只面积图处理。
    private List<VanChartBandGlyph> dealBandsBoundsList() {
        List<VanChartBandGlyph> areaBandGlyphList = new ArrayList<VanChartBandGlyph>();
        for(VanChartBandGlyph glyph : bandGlyphs){
            if(glyph.getAreaGlyph() == null){
                continue;
            }
            Rectangle2D bandBounds = glyph.getClipBounds();
            double minY1 = bandBounds.getMinY(),maxY1 = bandBounds.getMaxY();//后面的或者说当前的
            List<VanChartBandGlyph> removeList = new ArrayList<VanChartBandGlyph>();
            for(VanChartBandGlyph oldGlyph : areaBandGlyphList){//前面的处理过的所有互不相干的带有面积区域的bandGlyph
                //原则是后面的盖住前面的。特殊：当后面的完全包含在前面里时，舍弃后面的。
                Rectangle2D rect = oldGlyph.getClipBounds();
                double oldMinY2 = rect.getMinY(),oldMaxY2 = rect.getMaxY();
                if(minY1 < oldMinY2){
                    if(maxY1 <= oldMinY2){
                        continue;//互不相干
                    } else if(maxY1 < oldMaxY2){ //重叠。更改前面的。
                        Rectangle2D currentRect = new Rectangle2D.Double(rect.getX(), maxY1, rect.getWidth(), oldMaxY2 - maxY1);
                        oldGlyph.setClipBounds(currentRect);
                    } else{//包含。去掉前面的。
                        oldGlyph.setAreaGlyph(null);
                        removeList.add(oldGlyph);
                    }
                } else if(minY1 == oldMinY2){
                    if(maxY1 < oldMaxY2){//前面的包含后面的。更改前面的。
                        Rectangle2D currentRect = new Rectangle2D.Double(rect.getX(), maxY1, rect.getWidth(), oldMaxY2 - maxY1);
                        oldGlyph.setClipBounds(currentRect);
                    } else if(maxY1 >= oldMaxY2){
                        //相等或包含。去掉前面的。
                        oldGlyph.setAreaGlyph(null);
                        removeList.add(oldGlyph);
                    }
                } else if(minY1 < oldMaxY2){
                    if(maxY1 < oldMaxY2){//包含。完全在rect里面。说明不在其他的里面。removeBandsList为空，所以直接break就行。
                        glyph.setAreaGlyph(null);
                        removeList.add(glyph);//保留前面的，不加后面的。
                        break;
                    } else {//重叠。更改前面的。
                        Rectangle2D currentRect = new Rectangle2D.Double(rect.getX(), oldMinY2, rect.getWidth(), minY1);
                        oldGlyph.setClipBounds(currentRect);
                    }
                }
            }
            areaBandGlyphList.add(glyph);
            for(VanChartBandGlyph temp : removeList){
                areaBandGlyphList.remove(temp);
            }

        }
        return areaBandGlyphList;
    }

    //排序
    protected double[] sortY(List<VanChartBandGlyph> bandsList) {
        int len = bandsList.size();//因为一个里面有两个y值
        double[] result = new double[len * 2];
        for(int i = 0; i < len; i++){
            Rectangle2D bounds = bandsList.get(i).getClipBounds();
            result[2 * i] = bounds.getMinY();
            result[2 * i + 1] = bounds.getMaxY();
        }

        int low = 0;
        int high = len * 2 - 1;
        double temp;
        int j;
        while (low < high) {
            for (j = low; j < high; ++j) {
                if(result[j] > result[j+1]){
                    temp = result[j];
                    result[j] = result[j+1];
                    result[j+1] = temp;
                }
            }
            --high;
            for (j = high; j > low; --j) {
                if(result[j] < result[j-1]){
                    temp = result[j];
                    result[j] = result[j-1];
                    result[j-1] = temp;
                }
            }
            ++low;
        }
        return result;
    }

    /**
     * 返回图表中比较类型的结果, 为null时, 确定为false
     * @param cc  条件属性.
     *            @return 结果.
     */
    public Object toResult(CommonCondition cc) {
        String columnName = cc.getColumnName();
        boolean valueCondition = ComparatorUtils.equals(columnName, ChartConstants.VALUE)
                || ArrayUtils.contains(ChartConstants.PROJECT_ID_KEYS, columnName);
        if(evalValue && valueCondition){
            //本来系列这边条件属性中就没有值这一说。但是折线图可以设置某系列在某个值区间的颜色配色
            //所以这边当系列的条件属性涉及到系列的值，大于时返回双精度的最大值，以确保大于该条件的任何筛选值
            Compare comparator = cc.getCompare();
            if(comparator.getOp() == Compare.LESS_THAN || comparator.getOp() == Compare.LESS_THAN_OR_EQUAL){
                return Double.MIN_VALUE;
            } else if(comparator.getOp() == Compare.GREATER_THAN || comparator.getOp() == Compare.GREATER_THAN_OR_EQUAL){
                return Double.MAX_VALUE;
            }
        }
        return super.toResult(cc);
    }

    /**
     * 转为json数据
     *  @param repo 请求
     * @return 返回json
     * @throws com.fr.json.JSONException 抛错
     */
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        JSONObject js = new JSONObject();
        int count = getDataPointCount();
        if (count > 0) {
            JSONArray pointList = new JSONArray();
            for (int i = 0; i < count; i++) {
                pointList.put(this.getDataPoint(i).toJSONObject(repo));
            }
            js.put("data", pointList);
        }
        js.put("name", getSeriesName());
        if(attrSeriesStackAndAxis != null){
            js.put("xAxis", attrSeriesStackAndAxis.getXAxisIndex());
            js.put("yAxis", attrSeriesStackAndAxis.getYAxisIndex());
            if(attrSeriesStackAndAxis.isStacked()){
                js.put("stack", attrSeriesStackAndAxis.getStackID());
                js.put("stackByPercent", attrSeriesStackAndAxis.isPercentStacked());
            }
        }
        if(attrTrendLine != null){
            js.put("trendLine", attrTrendLine.toJSONObject(repo));
        }
        if(attrLine != null) {
            attrLine.addToJSONObject(js);
        }
        Color realColor = (((VanChartDataPoint)getDataPoint(0)).getDefaultColor());
        if(color != null){
            ColorBackground background = (ColorBackground)color.getSeriesBackground();
            realColor = background.getColor();
        }
        if(bands != null && !bands.isEmpty()){
            JSONArray bandsList = new JSONArray();
            for(AttrBand attrBand : this.bands){
                bandsList.put(attrBand.toJSONObject(repo, realColor));
            }
            js.put("bands",bandsList);
        }
        if(color != null) {//给某个系列设置配色，要传到前台，画图例的时候用到。折线图配色也只到系列有意义。
            float alpha = this.alpha == null ? 1 : this.alpha.getAlpha();
            js.put("color", VanChartAttrHelper.getRGBAColorWithColorAndAlpha(realColor, alpha));
        } else if (alpha != null && count > 0 && ((VanChartDataPoint)getDataPoint(0)).getDefaultColor() != null ){
            js.put("color", VanChartAttrHelper.getRGBAColorWithColorAndAlpha(realColor, alpha.getAlpha()));
        }
        if(marker != null){//系列这个层次也要传标记点，前台图例要用
            js.put("marker", marker.toJSONObject(repo));
        }
        if(fillColorBackground != null) {//面积图的填充色
            fillColorBackground.addToJSONObject(js, realColor);
        }
        return js;
    }

}
