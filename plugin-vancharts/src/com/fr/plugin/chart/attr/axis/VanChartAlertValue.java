package com.fr.plugin.chart.attr.axis;

import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.chart.chartattr.ChartAlertValue;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralUtils;
import com.fr.general.data.MOD_COLUMN_ROW;
import com.fr.general.xml.GeneralXMLTools;
import com.fr.script.Calculator;
import com.fr.stable.FCloneable;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLConstants;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.util.List;

/**
 * 警戒线
 */
public class VanChartAlertValue extends ChartAlertValue implements FCloneable {
    private static final long serialVersionUID = 5933572077220344590L;

    //以下这下属性不需要存取。设置警戒线的时候需要传进去坐标轴相关信息用来populate。
    private String axisName;
    private String[] axisNamesArray;
    private Object alertContentFormula;

    public void setAlertContentFormula(Object alertContentFormula) {
        this.alertContentFormula = alertContentFormula;
    }

    public Object getAlertContentFormula() {
        return alertContentFormula;
    }

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

    /**
     * 比较和Object是否相等
     * @param ob 用 于比较的Object
     * @return 一个boolean值
     */
    public boolean equals(Object ob) {
        return ob instanceof VanChartAlertValue && super.equals(ob)
                && ComparatorUtils.equals(((VanChartAlertValue) ob).getAxisName(), this.getAxisName())
                && ComparatorUtils.equals(((VanChartAlertValue) ob).getAxisNamesArray(), this.getAxisNamesArray())
                && ComparatorUtils.equals(((VanChartAlertValue) ob).getAlertContentFormula(), this.getAlertContentFormula());
    }

    /**
     * 克隆
     * @return 返回克隆的新对象
     */
    public Object clone() throws CloneNotSupportedException {
        VanChartAlertValue alertValue = (VanChartAlertValue)super.clone();

        if (this.getAxisName() != null) {
            alertValue.setAxisName(this.getAxisName());
        }

        if (this.getAxisNamesArray() != null) {
            alertValue.setAxisNamesArray(this.getAxisNamesArray().clone());
        }

        if (this.getAlertContentFormula() != null) {
            alertValue.setAlertContentFormula(this.getAlertContentFormula());
        }
        return alertValue;
    }

    /**
     * 读取XML属性
     * @param reader XML读取器
     */
    public void readXML(XMLableReader reader) {
        super.readXML(reader);
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();
            if (XMLConstants.OBJECT_TAG.equals(tagName)) {
                this.alertContentFormula = GeneralXMLTools.readObject(reader);
            } else if (tagName.equals("VanChartAlertValueAttr")) {
                if(!ComparatorUtils.equals(reader.getAttrAsString("contentFormula", ""), StringUtils.EMPTY)){
                    String contentString = reader.getAttrAsString("contentFormula", "");
                    if (StableUtils.maybeFormula(contentString)) {
                        this.alertContentFormula = new Formula(contentString);
                    } else {
                        this.alertContentFormula = contentString;
                    }
                }
            }
        }
    }
    /**
     * 输出XML属性
     * @param writer XML属性输出
     */
    public void writeXML(XMLPrintWriter writer) {
        super.writeXML(writer);

        if(alertContentFormula != null){
            GeneralXMLTools.writeObject(writer, this.alertContentFormula);
        }
    }

    /**
     * SE时处理公式.
     * @param calculator SE中的公式处理器
     */
    public void dealFormula(Calculator calculator) {
        super.dealFormula(calculator);
        if (this.alertContentFormula != null) {
            Utils.dealFormulaValue(alertContentFormula, calculator);
        }
    }

    /**
     * 预先计算聚合图表 表间公式顺序.
     * @param list 记录表间顺序列表
     * @param calculator 计算器
     */
    public void buidExecuteSequenceList(List list, Calculator calculator) {
        super.buidExecuteSequenceList(list, calculator);
        if (this.alertContentFormula != null) {
            GeneralUtils.dealBuidExecuteSequence(alertContentFormula, list, calculator);
        }
    }

    /**
     * 插入删除行列时 公式联动
     * @param mod 公式联动时的处理
     */
    public void modFormulaString(MOD_COLUMN_ROW mod) {
        super.modFormulaString(mod);
        if (alertContentFormula != null && alertContentFormula instanceof Formula) {
            ((Formula)alertContentFormula).modColumnRow(mod);
        }
    }

}
