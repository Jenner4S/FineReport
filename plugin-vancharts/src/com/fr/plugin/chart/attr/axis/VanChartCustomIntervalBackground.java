package com.fr.plugin.chart.attr.axis;

import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralUtils;
import com.fr.general.data.MOD_COLUMN_ROW;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.script.Calculator;
import com.fr.stable.FCloneable;
import com.fr.stable.web.Repository;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLable;
import com.fr.stable.xml.XMLableReader;

import java.awt.*;

/**
 * 自定义间隔背景
 */
public class VanChartCustomIntervalBackground implements XMLable, FCloneable {
    public static final String XML_TAG = "CustomIntervalBackground";
    private static final long serialVersionUID = 4679350281934613179L;

    private String customIntervalBackgroundSelectName;
    private Formula fromFormula;
    private Formula toFormula;
    private Color backgroundColor;
    private double alpha = 0.3;

    //以下这下属性不需要存取。设置警戒线的时候需要传进去坐标轴相关信息用来populate。
    private String axisName;
    private String[] axisNamesArray;

    public void setAxisName(String axisName) {
        this.axisName = axisName;
    }

    public String getAxisName() {
        return axisName;
    }

    public void setAxisNamesArray(String[] axisNamesArray) {
        this.axisNamesArray = axisNamesArray;
    }

    public String[] getAxisNamesArray() {
        return axisNamesArray;
    }

    public void setCustomIntervalBackgroundSelectName(String customIntervalBackgroundSelectName) {
        this.customIntervalBackgroundSelectName = customIntervalBackgroundSelectName;
    }

    public String getCustomIntervalBackgroundSelectName() {
        return customIntervalBackgroundSelectName;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setFromFormula(Formula fromFormula) {
        this.fromFormula = fromFormula;
    }

    public void setToFormula(Formula toFormula) {
        this.toFormula = toFormula;
    }

    public Formula getToFormula() {
        return toFormula;
    }

    public Formula getFromFormula() {
        return fromFormula;
    }

    public double getAlpha() {
        return alpha;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * 比较和Object是否相等
     * @param ob 用 于比较的Object
     * @return 一个boolean值
     */
    public boolean equals(Object ob) {
        return ob instanceof VanChartCustomIntervalBackground
                && ComparatorUtils.equals(((VanChartCustomIntervalBackground) ob).getCustomIntervalBackgroundSelectName(), getCustomIntervalBackgroundSelectName())
                && ComparatorUtils.equals(((VanChartCustomIntervalBackground) ob).getFromFormula(), getFromFormula())
                && ComparatorUtils.equals(((VanChartCustomIntervalBackground) ob).getToFormula(), getToFormula())
                && ComparatorUtils.equals(((VanChartCustomIntervalBackground) ob).getBackgroundColor(), getBackgroundColor())
                && ComparatorUtils.equals(((VanChartCustomIntervalBackground) ob).getAlpha(), getAlpha())
                ;
    }
    /**
     * 读取XML属性
     * @param reader XML读取器
     */
    public void readXML(XMLableReader reader) {
        if (reader.isChildNode() && reader.getTagName().equals("attr")) {
            customIntervalBackgroundSelectName = reader.getAttrAsString("selectName", "");
            fromFormula = new Formula(reader.getAttrAsString("fromFormula", ""));
            toFormula = new Formula(reader.getAttrAsString("toFormula", ""));
            alpha = reader.getAttrAsDouble("alpha",0);
            backgroundColor = reader.getAttrAsColor("color", null);
        }
    }
    /**
     * 输出XML属性
     * @param writer XML属性输出
     */
    public void writeXML(XMLPrintWriter writer) {

        writer.startTAG("attr")
                .attr("selectName", customIntervalBackgroundSelectName)
                .attr("fromFormula", fromFormula.toString())
                .attr("toFormula", toFormula.toString())
                .attr("alpha", alpha);
        if(backgroundColor != null){
            writer.attr("color", backgroundColor.getRGB());
        }
        writer.end();

    }

    /**
     * SE时处理公式.
     * @param calculator SE中的公式处理器
     */
    public void dealFormula(Calculator calculator) {
        if (this.fromFormula != null) {
            Utils.dealFormulaValue(fromFormula, calculator);
        }
        if (this.toFormula != null) {
            Utils.dealFormulaValue(toFormula, calculator);
        }
    }

    /**
     * 预先计算聚合图表 表间公式顺序.
     * @param list 记录表间顺序列表
     * @param calculator 计算器
     */
    public void buidExecuteSequenceList(java.util.List list, Calculator calculator) {
        if (this.fromFormula != null) {
            GeneralUtils.dealBuidExecuteSequence(fromFormula, list, calculator);
        }
        if(this.toFormula != null) {
            GeneralUtils.dealBuidExecuteSequence(toFormula, list, calculator);
        }
    }

    /**
     * 插入删除行列时 公式联动
     * @param mod 公式联动时的处理
     */
    public void modFormulaString(MOD_COLUMN_ROW mod) {
        if (this.fromFormula != null) {
            this.fromFormula.modColumnRow(mod);
        }
        if (this.toFormula != null) {
            this.toFormula.modColumnRow(mod);
        }

    }

    /**
     * 克隆
     * @return 返回克隆的新对象
     */
    public Object clone() throws CloneNotSupportedException {
        VanChartCustomIntervalBackground background = (VanChartCustomIntervalBackground)super.clone();
        if (this.getAxisName() != null) {
            background.setAxisName(this.getAxisName());
        }
        if (this.getAxisNamesArray() != null) {
            background.setAxisNamesArray(this.getAxisNamesArray().clone());
        }
        background.setAlpha(this.getAlpha());
        background.setBackgroundColor(this.getBackgroundColor());
        if(this.getFromFormula() != null){
            background.setFromFormula((Formula)this.getFromFormula().clone());
        }
        if(this.getToFormula() != null){
            background.setToFormula((Formula)this.getToFormula().clone());
        }
        return background;
    }

    /**
     * 转为json数据
     *  @param repo 请求
     * @return 返回json
     * @throws com.fr.json.JSONException 抛错
     */
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        JSONObject js = new JSONObject();

        js.put("color", VanChartAttrHelper.getRGBAColorWithColorAndAlpha(getBackgroundColor(), (float)getAlpha()));
        js.put("from", getFromFormula().getResult());
        js.put("to", getToFormula().getResult());

        return js;
    }
}
