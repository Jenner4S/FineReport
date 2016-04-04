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

    //�����⼸�����������������õģ��п���Ϊnil�����ڴ���ǰ̨
    //��Ϊ���������������Ա������ǿ���û�еģ����Զ�����dataSeries���ˣ���û���ٸ��ݲ�ͬͼ��д���ࡣ
    private VanChartAttrLine attrLine;//���������������
    private AttrBackground color;//��ɫ������ͼ��ɫֻȡ��ϵ�У����ݵ����ɫû���κ����塣ǰ̨��ͼ��ҲҪ�õ���
    private AttrAlpha alpha;//͸���ȣ�ǰ̨ͼ��Ҫ�á�
    private VanChartAttrMarker marker;//ϵ�����ҲҪ����ͼ��Ҫ�õ�
    private AttrAreaSeriesFillColorBackground fillColorBackground;//���ɫ�����ͼ�����ɫֻȡ��ϵ�У����ݵ�����ɫû���κ����塣
    private AttrSeriesStackAndAxis attrSeriesStackAndAxis;//�ѻ��������ᣨ�������������
    private VanChartAttrTrendLine attrTrendLine;//������
    private List<AttrBand> bands = new ArrayList<AttrBand>();

    private List<VanChartBandGlyph> bandGlyphs = new ArrayList<VanChartBandGlyph>();

    //ֻ��ȡbands��ʱ����Ϊtrue���������������й���ϵ�е�ֵ���жϣ��Ի�ȡ����ɫ
    private boolean evalValue = false;

    private FoldLine areaTopLine = null;//���ͼ�������һ����

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
     * ���ϵ������bands����
     * @param bands bands����
     */
    public void addAttrBands(AttrBand bands){
        this.bands.add(bands);
    }


    /**
     * ���ϵ������bandsͼ��
     * @param bandGlyph bandsͼ��
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
     * ��band��Ĭ��ֵ
     * @param min ��Сֵ
     * @param max ���ֵ
     */
    public void initBandsDefaultMinMaxValue(double min, double max){
        if(bands == null || bands.size() < 1){
            return;
        }
        for(AttrBand band : bands){
            //��Ӧ��ֵ��һ����ı䣬ֻ����������û�����òŻ��滻
            band.setMaxEval(min);//��Ҫд����
            band.setMinEval(max);
        }
    }

    public VanChartDataSeries(int seriesIndex){
        super(seriesIndex);
    }

    /**
     * ��ϵ�б�������״��.
     *
     * @param g          ͼ�ζ���
     * @param resolution �ֱ���
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

    //���ͼ����п�������ĳ�������.����ϵ�о�Ҫ��ʣ�µĵط����������ظ��������ص���band��ɫ��׼ȷ
    private List<Rectangle2D> getDataSeriesRecs(Rectangle2D wholeBounds) {
        List<Rectangle2D> seriesList = new ArrayList<Rectangle2D>();
        List<VanChartBandGlyph> bandsList = dealBandsBoundsList();//������ɵ�����
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

    //�Ȱ�bands֮������ص��Ĳ��ִ���ɻ�����ɵ�����ֻ���ͼ����
    private List<VanChartBandGlyph> dealBandsBoundsList() {
        List<VanChartBandGlyph> areaBandGlyphList = new ArrayList<VanChartBandGlyph>();
        for(VanChartBandGlyph glyph : bandGlyphs){
            if(glyph.getAreaGlyph() == null){
                continue;
            }
            Rectangle2D bandBounds = glyph.getClipBounds();
            double minY1 = bandBounds.getMinY(),maxY1 = bandBounds.getMaxY();//����Ļ���˵��ǰ��
            List<VanChartBandGlyph> removeList = new ArrayList<VanChartBandGlyph>();
            for(VanChartBandGlyph oldGlyph : areaBandGlyphList){//ǰ��Ĵ���������л�����ɵĴ�����������bandGlyph
                //ԭ���Ǻ���ĸ�סǰ��ġ����⣺���������ȫ������ǰ����ʱ����������ġ�
                Rectangle2D rect = oldGlyph.getClipBounds();
                double oldMinY2 = rect.getMinY(),oldMaxY2 = rect.getMaxY();
                if(minY1 < oldMinY2){
                    if(maxY1 <= oldMinY2){
                        continue;//�������
                    } else if(maxY1 < oldMaxY2){ //�ص�������ǰ��ġ�
                        Rectangle2D currentRect = new Rectangle2D.Double(rect.getX(), maxY1, rect.getWidth(), oldMaxY2 - maxY1);
                        oldGlyph.setClipBounds(currentRect);
                    } else{//������ȥ��ǰ��ġ�
                        oldGlyph.setAreaGlyph(null);
                        removeList.add(oldGlyph);
                    }
                } else if(minY1 == oldMinY2){
                    if(maxY1 < oldMaxY2){//ǰ��İ�������ġ�����ǰ��ġ�
                        Rectangle2D currentRect = new Rectangle2D.Double(rect.getX(), maxY1, rect.getWidth(), oldMaxY2 - maxY1);
                        oldGlyph.setClipBounds(currentRect);
                    } else if(maxY1 >= oldMaxY2){
                        //��Ȼ������ȥ��ǰ��ġ�
                        oldGlyph.setAreaGlyph(null);
                        removeList.add(oldGlyph);
                    }
                } else if(minY1 < oldMaxY2){
                    if(maxY1 < oldMaxY2){//��������ȫ��rect���档˵���������������档removeBandsListΪ�գ�����ֱ��break���С�
                        glyph.setAreaGlyph(null);
                        removeList.add(glyph);//����ǰ��ģ����Ӻ���ġ�
                        break;
                    } else {//�ص�������ǰ��ġ�
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

    //����
    protected double[] sortY(List<VanChartBandGlyph> bandsList) {
        int len = bandsList.size();//��Ϊһ������������yֵ
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
     * ����ͼ���бȽ����͵Ľ��, Ϊnullʱ, ȷ��Ϊfalse
     * @param cc  ��������.
     *            @return ���.
     */
    public Object toResult(CommonCondition cc) {
        String columnName = cc.getColumnName();
        boolean valueCondition = ComparatorUtils.equals(columnName, ChartConstants.VALUE)
                || ArrayUtils.contains(ChartConstants.PROJECT_ID_KEYS, columnName);
        if(evalValue && valueCondition){
            //����ϵ��������������о�û��ֵ��һ˵����������ͼ��������ĳϵ����ĳ��ֵ�������ɫ��ɫ
            //������ߵ�ϵ�е����������漰��ϵ�е�ֵ������ʱ����˫���ȵ����ֵ����ȷ�����ڸ��������κ�ɸѡֵ
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
     * תΪjson����
     *  @param repo ����
     * @return ����json
     * @throws com.fr.json.JSONException �״�
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
        if(color != null) {//��ĳ��ϵ��������ɫ��Ҫ����ǰ̨����ͼ����ʱ���õ�������ͼ��ɫҲֻ��ϵ�������塣
            float alpha = this.alpha == null ? 1 : this.alpha.getAlpha();
            js.put("color", VanChartAttrHelper.getRGBAColorWithColorAndAlpha(realColor, alpha));
        } else if (alpha != null && count > 0 && ((VanChartDataPoint)getDataPoint(0)).getDefaultColor() != null ){
            js.put("color", VanChartAttrHelper.getRGBAColorWithColorAndAlpha(realColor, alpha.getAlpha()));
        }
        if(marker != null){//ϵ��������ҲҪ����ǵ㣬ǰ̨ͼ��Ҫ��
            js.put("marker", marker.toJSONObject(repo));
        }
        if(fillColorBackground != null) {//���ͼ�����ɫ
            fillColorBackground.addToJSONObject(js, realColor);
        }
        return js;
    }

}
