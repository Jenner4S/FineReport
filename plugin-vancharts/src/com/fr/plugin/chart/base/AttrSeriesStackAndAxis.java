package com.fr.plugin.chart.base;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.general.ComparatorUtils;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.stable.web.Repository;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

/**
 * 系列的堆积和坐标轴。
 * 先放到条件属性中，然后赋给具体的DataSeries。
 */
public class AttrSeriesStackAndAxis extends DataSeriesCondition{

    public static final String XML_TAG = "AttrSeriesStackAndAxis";
    private static final long serialVersionUID = -3769917681690919402L;

    private int XAxisIndex = 0;
    private int YAxisIndex = 0;
    private boolean stacked = true;
    private boolean percentStacked = false;
    private String stackID = "stackID";

    //以下这下属性不需要存取。设置警戒线的时候需要传进去坐标轴相关信息用来populate。
    private String[] XAxisNamesArray;
    private String[] YAxisNameArray;

    public void setXAxisNamesArray(String[] XAxisNamesArray) {
        this.XAxisNamesArray = XAxisNamesArray;
    }

    public void setYAxisNameArray(String[] YAxisNameArray) {
        this.YAxisNameArray = YAxisNameArray;
    }

    public String[] getYAxisNameArray() {
        return YAxisNameArray;
    }

    public String[] getXAxisNamesArray() {
        return XAxisNamesArray;
    }

    public void setPercentStacked(boolean percentStacked) {
        this.percentStacked = percentStacked;
    }

    public void setStacked(boolean stacked) {
        this.stacked = stacked;
    }

    public void setXAxisIndex(int XAxisIndex) {
        this.XAxisIndex = XAxisIndex;
    }

    public void setYAxisIndex(int YAxisIndex) {
        this.YAxisIndex = YAxisIndex;
    }

    public int getYAxisIndex() {
        return YAxisIndex;
    }

    public int getXAxisIndex() {
        return XAxisIndex;
    }

    /**
     * 堆积
     * @return 堆积
     */
    public boolean isStacked() {
        return stacked;
    }

    /**
     * 百分比堆积
     * @return 百分比堆积
     */
    public boolean isPercentStacked() {
        return percentStacked;
    }

    public void setStackID(String stackID) {
        this.stackID = stackID;
    }

    public String getStackID() {
        return stackID;
    }

    public void readXML(XMLableReader reader) {
        if(reader.isChildNode()) {
            String tagName = reader.getTagName();
            if(tagName.equals("Attr")) {
                setXAxisIndex(reader.getAttrAsInt("xAxisIndex",0));
                setYAxisIndex(reader.getAttrAsInt("yAxisIndex",0));
                setStacked(reader.getAttrAsBoolean("stacked",true));
                setPercentStacked(reader.getAttrAsBoolean("percentStacked",false));
                setStackID(reader.getAttrAsString("stackID",""));
            }
        }
    }

    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);

        writer.startTAG("Attr")
                .attr("xAxisIndex", getXAxisIndex())
                .attr("yAxisIndex", getYAxisIndex())
                .attr("stacked", isStacked())
                .attr("percentStacked", isPercentStacked())
                .attr("stackID", getStackID())
                .end();

        writer.end();
    }

    public Object clone() throws CloneNotSupportedException {
        AttrSeriesStackAndAxis seriesStackAndAxis = (AttrSeriesStackAndAxis)super.clone();
        seriesStackAndAxis.setXAxisNamesArray(getXAxisNamesArray());
        seriesStackAndAxis.setYAxisNameArray(getYAxisNameArray());
        return seriesStackAndAxis;
    }
    /**
     * 转为json数据
     *  @param repo 请求
     * @return 返回json
     * @throws com.fr.json.JSONException 抛错
     */
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        return new JSONObject();
    }

    public boolean equals(Object ob) {
        return ob instanceof AttrSeriesStackAndAxis
                && ComparatorUtils.equals(((AttrSeriesStackAndAxis) ob).getXAxisIndex(), this.getXAxisIndex())
                && ComparatorUtils.equals(((AttrSeriesStackAndAxis) ob).getYAxisIndex(), this.getYAxisIndex())
                && ComparatorUtils.equals(((AttrSeriesStackAndAxis) ob).isStacked(), this.isStacked())
                && ComparatorUtils.equals(((AttrSeriesStackAndAxis) ob).isPercentStacked(), this.isPercentStacked())
                && ComparatorUtils.equals(((AttrSeriesStackAndAxis) ob).getStackID(), this.getStackID())
                ;
    }

    public String getConditionType() {
        return XML_TAG;
    }
}
