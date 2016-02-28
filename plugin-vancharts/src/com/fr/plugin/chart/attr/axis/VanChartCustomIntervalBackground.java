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
 * �Զ���������
 */
public class VanChartCustomIntervalBackground implements XMLable, FCloneable {
    public static final String XML_TAG = "CustomIntervalBackground";
    private static final long serialVersionUID = 4679350281934613179L;

    private String customIntervalBackgroundSelectName;
    private Formula fromFormula;
    private Formula toFormula;
    private Color backgroundColor;
    private double alpha = 0.3;

    //�����������Բ���Ҫ��ȡ�����þ����ߵ�ʱ����Ҫ����ȥ�����������Ϣ����populate��
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
     * �ȽϺ�Object�Ƿ����
     * @param ob �� �ڱȽϵ�Object
     * @return һ��booleanֵ
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
     * ��ȡXML����
     * @param reader XML��ȡ��
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
     * ���XML����
     * @param writer XML�������
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
     * SEʱ����ʽ.
     * @param calculator SE�еĹ�ʽ������
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
     * Ԥ�ȼ���ۺ�ͼ�� ��乫ʽ˳��.
     * @param list ��¼���˳���б�
     * @param calculator ������
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
     * ����ɾ������ʱ ��ʽ����
     * @param mod ��ʽ����ʱ�Ĵ���
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
     * ��¡
     * @return ���ؿ�¡���¶���
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
     * תΪjson����
     *  @param repo ����
     * @return ����json
     * @throws com.fr.json.JSONException �״�
     */
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        JSONObject js = new JSONObject();

        js.put("color", VanChartAttrHelper.getRGBAColorWithColorAndAlpha(getBackgroundColor(), (float)getAlpha()));
        js.put("from", getFromFormula().getResult());
        js.put("to", getToFormula().getResult());

        return js;
    }
}
