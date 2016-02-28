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
 * ������
 */
public class VanChartAlertValue extends ChartAlertValue implements FCloneable {
    private static final long serialVersionUID = 5933572077220344590L;

    //�����������Բ���Ҫ��ȡ�����þ����ߵ�ʱ����Ҫ����ȥ�����������Ϣ����populate��
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
     * �ȽϺ�Object�Ƿ����
     * @param ob �� �ڱȽϵ�Object
     * @return һ��booleanֵ
     */
    public boolean equals(Object ob) {
        return ob instanceof VanChartAlertValue && super.equals(ob)
                && ComparatorUtils.equals(((VanChartAlertValue) ob).getAxisName(), this.getAxisName())
                && ComparatorUtils.equals(((VanChartAlertValue) ob).getAxisNamesArray(), this.getAxisNamesArray())
                && ComparatorUtils.equals(((VanChartAlertValue) ob).getAlertContentFormula(), this.getAlertContentFormula());
    }

    /**
     * ��¡
     * @return ���ؿ�¡���¶���
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
     * ��ȡXML����
     * @param reader XML��ȡ��
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
     * ���XML����
     * @param writer XML�������
     */
    public void writeXML(XMLPrintWriter writer) {
        super.writeXML(writer);

        if(alertContentFormula != null){
            GeneralXMLTools.writeObject(writer, this.alertContentFormula);
        }
    }

    /**
     * SEʱ����ʽ.
     * @param calculator SE�еĹ�ʽ������
     */
    public void dealFormula(Calculator calculator) {
        super.dealFormula(calculator);
        if (this.alertContentFormula != null) {
            Utils.dealFormulaValue(alertContentFormula, calculator);
        }
    }

    /**
     * Ԥ�ȼ���ۺ�ͼ�� ��乫ʽ˳��.
     * @param list ��¼���˳���б�
     * @param calculator ������
     */
    public void buidExecuteSequenceList(List list, Calculator calculator) {
        super.buidExecuteSequenceList(list, calculator);
        if (this.alertContentFormula != null) {
            GeneralUtils.dealBuidExecuteSequence(alertContentFormula, list, calculator);
        }
    }

    /**
     * ����ɾ������ʱ ��ʽ����
     * @param mod ��ʽ����ʱ�Ĵ���
     */
    public void modFormulaString(MOD_COLUMN_ROW mod) {
        super.modFormulaString(mod);
        if (alertContentFormula != null && alertContentFormula instanceof Formula) {
            ((Formula)alertContentFormula).modColumnRow(mod);
        }
    }

}
