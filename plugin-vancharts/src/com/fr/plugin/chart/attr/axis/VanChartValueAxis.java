package com.fr.plugin.chart.attr.axis;

import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.base.chart.chartdata.ChartData;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.chartglyph.AxisGlyph;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralUtils;
import com.fr.general.data.MOD_COLUMN_ROW;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.glyph.axis.VanChartValueAxisGlyph;
import com.fr.script.Calculator;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.util.List;

/**
 * ��ֵ������
 */
public class VanChartValueAxis extends VanChartAxis {

    public static final String XML_TAG = "VanChartValueAxis";

    private static final long serialVersionUID = -5830910929691301496L;
    private boolean isLog = false;
    private Formula logBase = new Formula("10");

    /**
     * �Ƿ�ٷֱ�.
     */
    private boolean isPercentage = false;

    /**
     * ���ö����ĵ�
     */
    public void setLogBase(Formula logBase) {
        this.logBase = logBase;
    }

    /**
     * ���ض����ĵ�
     */
    public Formula getLogBase() {
        return logBase;
    }

    /**
     * �����Ƿ��ö���������
     */
    public void setLog(boolean isLog) {
        this.isLog = isLog;
    }

    /**
     * ���� �Ƿ�Ϊ��������������
     * @return  �Ƿ�Ϊ����.
     */
    public boolean isLog() {
        return isLog;
    }

    public AxisType getAxisType() {
        return AxisType.AXIS_VALUE;
    }

    /**
     * �����Ƿ�Ϊ�ٷֱ�������
     */
    public void setPercentage(boolean isPercentage) {
        this.isPercentage = isPercentage;
    }

    /**
     * �����Ƿ�Ϊ�ٷֱ�������
     * @return  �����Ƿ�Ϊ�ٷֱ�������
     */
    public boolean isPercentage() {
        return isPercentage;
    }

    public VanChartValueAxis(){
        super();
    }

    public VanChartValueAxis(String axisName, int position){
        super(axisName, position);
    }

    public void initAxisGlyphWithChartData(VanChartValueAxisGlyph axisGlyph){
        initAxisGlyph(axisGlyph);
        if(isPercentage() && getFormat() == null){
            axisGlyph.setFormat(VanChartAttrHelper.PERCENT_FORMAT);
        }
    }

    /**
     *����ChartData ���ɶ�Ӧ��AxisGlyph
     * @param chartData ����
     * @return �������ͼ��
     */
    public VanChartValueAxisGlyph createAxisGlyph(ChartData chartData) {
        VanChartValueAxisGlyph axisGlyph = new VanChartValueAxisGlyph();

        initAxisGlyphWithChartData(axisGlyph);

        return axisGlyph;
    }

    /**
     *��ʼ����Ӧ������������
     * @param axisGlyph �������ͼ��
     */
    public void initAxisGlyph(AxisGlyph axisGlyph) {
        super.initAxisGlyph(axisGlyph);
        VanChartValueAxisGlyph vanChartAxisGlyph = (VanChartValueAxisGlyph)axisGlyph;
        vanChartAxisGlyph.setPercentage(isPercentage());
        vanChartAxisGlyph.setLog(isLog());
        Number number = ChartBaseUtils.formula2Number(this.getLogBase());
        if (number != null) {
            vanChartAxisGlyph.setLogBase(number.doubleValue());
        } else {
            vanChartAxisGlyph.setLogBase(10);
        }
    }

    public boolean equals(Object ob) {
        return super.equals(ob) && ob instanceof VanChartValueAxis
                && ComparatorUtils.equals(((VanChartValueAxis) ob).isPercentage(), this.isPercentage())
                && ComparatorUtils.equals(((VanChartValueAxis) ob).isLog(), this.isLog())
                && ComparatorUtils.equals(((VanChartValueAxis) ob).getLogBase(), this.getLogBase());
    }

    public Object clone() throws CloneNotSupportedException {
        VanChartValueAxis newAxis = (VanChartValueAxis)super.clone();

        newAxis.setLog(this.isLog());
        if(this.logBase != null){
            newAxis.setLogBase((Formula)this.getLogBase().clone());
        }
        newAxis.setPercentage(this.isPercentage());

        return newAxis;
    }

    public void readXML(XMLableReader reader) {
        super.readXML(reader);
        if (reader.isChildNode()) {
            String name = reader.getTagName();
            if (name.equals("VanChartValueAxisAttr")) {
                isLog = reader.getAttrAsBoolean("isLog",false);
                logBase = new Formula(reader.getAttrAsString("baseLog", "10"));
            }
        }
    }

    public void writeXML(XMLPrintWriter writer) {
        super.writeXML(writer);

        writer.startTAG("VanChartValueAxisAttr")
                .attr("isLog", isLog)
                .attr("baseLog", logBase.toString())
                .end();
    }

    /**
     * SE�д���ʽ
     * @param calculator  ��ʽ������.
     */
    public void dealFormula(Calculator calculator) {
        super.dealFormula(calculator);

        if(this.logBase != null) {
            Utils.dealFormulaValue(this.logBase, calculator);
        }
    }

    /**
     * Ԥ�ȼ���ۺ�ͼ�� ��乫ʽ˳��.
     * @param calculator  ��ʽ������
     *                    @param list  ����б�
     */
    public void buidExecuteSequenceList(List list, Calculator calculator) {
        super.buidExecuteSequenceList(list, calculator);

        if(this.logBase != null) {
            GeneralUtils.dealBuidExecuteSequence(this.logBase, list, calculator);
        }
    }

    /**
     * ����ɾ������ʱ ��ʽ����
     * @param mod  ���б䶯
     */
    public void modFormulaString(MOD_COLUMN_ROW mod) {
        super.modFormulaString(mod);

        if(this.logBase != null) {
            this.logBase.modColumnRow(mod);
        }

    }

}
