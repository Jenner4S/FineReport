package com.fr.plugin.chart.attr;

import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralUtils;
import com.fr.general.data.MOD_COLUMN_ROW;
import com.fr.general.xml.GeneralXMLTools;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.base.VanChartConstants;
import com.fr.script.Calculator;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLable;
import com.fr.stable.xml.XMLableReader;

/**
 * 缩放相关
 */
public class VanChartZoom implements XMLable {

    public static final String XML_TAG = "VanChartZoom";
    private static final long serialVersionUID = 1891400392215463542L;

    private boolean zoomVisible = false;
    private boolean zoomResize = true;
    private Object from = "";
    private Object to = "";
    private String zoomType = VanChartConstants.ZOOM_TYPE_XY;

    public void setFrom(Object from) {
        this.from = from;
    }

    public void setTo(Object to) {
        this.to = to;
    }

    public void setZoomResize(boolean zoomResize) {
        this.zoomResize = zoomResize;
    }

    public void setZoomType(String zoomType) {
        this.zoomType = zoomType;
    }

    public void setZoomVisible(boolean zoomVisible) {
        this.zoomVisible = zoomVisible;
    }

    public String getZoomType() {
        return zoomType;
    }

    public Object getTo() {
        return to;
    }

    public Object getFrom() {
        return from;
    }

    /**
     * 是否开启缩放
     * @return 开启缩放
     */
    public boolean isZoomVisible() {
        return zoomVisible;
    }

    /**
     * 缩放是否可调节
     * @return 缩放是否可调节
     */
    public boolean isZoomResize() {
        return zoomResize;
    }

    /**
     *  写XML
     * @param writer XML属性输出
     */
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);
        writer.startTAG("zoomAttr")
                .attr("zoomVisible", zoomVisible)
                .attr("zoomResize", zoomResize)
                .attr("zoomType", zoomType)
                .end();

        if(from != null){
            GeneralXMLTools.writeObject(writer, from, "from");
        }

        if(to != null){
            GeneralXMLTools.writeObject(writer, to, "to");
        }
        writer.end();
    }

    /**
     * 读取XML属性
     * @param reader XML读取器
     */
    public void readXML(XMLableReader reader) {
        if (reader.isChildNode()) {
            String name = reader.getTagName();
            if("zoomAttr".equals(name)){
                zoomVisible = reader.getAttrAsBoolean("zoomVisible", false);
                zoomResize = reader.getAttrAsBoolean("zoomResize", true);
                zoomType = reader.getAttrAsString("zoomType", "");
            }else if ("from".equals(name)) {
                from = GeneralXMLTools.readObject(reader);
            } else if("to".equals(name)){
                to = GeneralXMLTools.readObject(reader);
            }
        }
    }

    /**
     * SE 中处理公式结果
     * @param calculator SE中的公式处理器
     */
    public void dealFormula(Calculator calculator) {
        Utils.dealFormulaValue(from, calculator);
        Utils.dealFormulaValue(to, calculator);
    }

    /**
     * 预先计算聚合图表 表间公式顺序.
     * @param list 记录表间顺序列表
     * @param calculator 计算器
     */
    public void buidExecuteSequenceList(java.util.List list, Calculator calculator) {
        GeneralUtils.dealBuidExecuteSequence(from, list, calculator);
        GeneralUtils.dealBuidExecuteSequence(to, list, calculator);
    }

    /**
     * 插入删除行列时 图表中的公式 联动
     * @param mod 执行公式联动的对象
     */
    public void modFormulaString(MOD_COLUMN_ROW mod) {
        mod.mod_object(from);
        mod.mod_object(to);
    }

    /**
     * 比较和Object是否相等
     * @param ob 用 于比较的Object
     * @return 一个boolean值
     */
    public boolean equals(Object ob) {
        return ob instanceof VanChartZoom
                && ComparatorUtils.equals(((VanChartZoom) ob).getFrom(), this.getFrom())
                && ComparatorUtils.equals(((VanChartZoom) ob).getTo(), this.getTo())
                && ComparatorUtils.equals(((VanChartZoom) ob).getZoomType(), this.getZoomType())
                && ComparatorUtils.equals(((VanChartZoom) ob).isZoomResize(), this.isZoomResize())
                && ComparatorUtils.equals(((VanChartZoom) ob).isZoomVisible(), this.isZoomVisible())
                ;
    }

    public Object clone() throws CloneNotSupportedException {
        VanChartZoom newZoom = (VanChartZoom)super.clone();
        newZoom.setFrom(getFrom());
        newZoom.setTo(getTo());
        newZoom.setZoomResize(isZoomResize());
        newZoom.setZoomType(getZoomType());
        newZoom.setZoomVisible(isZoomVisible());
        return newZoom;
    }

    /**
     * 返回json对象
     * @return js对象
     * @throws com.fr.json.JSONException 抛错
     */
    public JSONObject toJSONObject() throws JSONException {
        JSONObject js = new JSONObject();
        if(!zoomVisible && !ComparatorUtils.equals(zoomType, VanChartConstants.ZOOM_TYPE_NONE)){
            js.put("zoomType", zoomType);
        }
        JSONObject tools = new JSONObject();
        tools.put("visible", zoomVisible);
        String fromString = Utils.objectToString(from);
        if(from instanceof Formula) {
            Formula formula = (Formula)from;
            if(formula.getResult() != null) {
                fromString = Utils.objectToString(formula.getResult());
            }
        }
        tools.put("from", fromString);
        String toString = Utils.objectToString(to);
        if(to instanceof Formula) {
            Formula formula = (Formula)to;
            if(formula.getResult() != null) {
                toString = Utils.objectToString(formula.getResult());
            }
        }
        tools.put("to", toString);
        tools.put("resize", zoomResize);
        js.put("zoomTool", tools);
        return js;
    }
}
