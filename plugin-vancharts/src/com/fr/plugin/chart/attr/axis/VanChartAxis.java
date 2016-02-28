package com.fr.plugin.chart.attr.axis;

import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.base.chart.chartdata.ChartData;
import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.Title;
import com.fr.chart.chartglyph.AxisGlyph;
import com.fr.chart.chartglyph.ChartAlertValueGlyph;
import com.fr.general.ComparatorUtils;
import com.fr.general.data.MOD_COLUMN_ROW;
import com.fr.general.xml.GeneralXMLTools;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.base.VanChartConstants;
import com.fr.plugin.chart.glyph.VanChartAlertValueGlyph;
import com.fr.plugin.chart.glyph.axis.VanChartBaseAxisGlyph;
import com.fr.plugin.chart.glyph.axis.VanChartCategoryAxisGlyph;
import com.fr.script.Calculator;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLableReader;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ������������ԣ�ǡ�ú��ı�������һ��
 */
public class VanChartAxis extends Axis{
    private static final long serialVersionUID = -257446768551206668L;
    public static final String XML_TAG = "VanChartAxis";
    private String axisName;//���name��ʵ�����壬���ڽ���������tab

    private boolean titleUseHtml = false;

    private boolean autoLabelGap = true;

    private boolean limitSize = false;
    private double maxHeight = 15;

    private boolean commonValueFormat = true;
    private String customValueFormatText = "function(){ return this; }";
    private boolean customValueFormatUseHtml = false;

    private List<VanChartAlertValue> alertValues = new ArrayList<VanChartAlertValue>();

    private Color defaultIntervalBackgroundColor = null;
    private List<VanChartCustomIntervalBackground> customIntervalBackgroundArray = new ArrayList<VanChartCustomIntervalBackground>();

    private AxisTickLineType mainTickLine = AxisTickLineType.TICK_LINE_OUTSIDE;
    private AxisTickLineType secTickLine = AxisTickLineType.TICK_LINE_NONE;

    public void setMainTickLine(AxisTickLineType mainTickLine) {
        this.mainTickLine = mainTickLine;
    }

    public void setSecTickLine(AxisTickLineType secTickLine) {
        this.secTickLine = secTickLine;
    }

    public AxisTickLineType getSecTickLine() {
        return secTickLine;
    }

    public AxisTickLineType getMainTickLine() {
        return mainTickLine;
    }

    /**
     * ���� �̶�����
     */
    public void setTickMarkType(int tickMarkType) {
        this.mainTickLine = AxisTickLineType.parse(tickMarkType);
    }

    /**
     * ���ô�Ҫ�̶�����
     */
    public void setSecTickMarkType(int secTickMarkType) {
        this.secTickLine = AxisTickLineType.parse(secTickMarkType);
    }

    public void setAxisName(String axisName) {
        this.axisName = axisName;
    }

    public String getAxisName() {
        return this.axisName;
    }

    public void setTitleUseHtml(boolean titleUseHtml) {
        this.titleUseHtml = titleUseHtml;
    }

    /**
     * ����usehtml
     * @return ����usehtml
     */
    public boolean isTitleUseHtml() {
        return titleUseHtml;
    }

    public void setAutoLabelGap(boolean autoLabelGap) {
        this.autoLabelGap = autoLabelGap;
    }

    /**
     * �Զ����
     * @return �Զ����
     */
    public boolean isAutoLabelGap() {
        return autoLabelGap;
    }

    public void setCommonValueFormat(boolean commonValueFormat) {
        this.commonValueFormat = commonValueFormat;
    }

    /**
     * ͨ�ø�ʽ
     * @return ͨ�ø�ʽ
     */
    public boolean isCommonValueFormat() {
        return commonValueFormat;
    }

    public void setCustomValueFormatText(String customValueFormatText) {
        this.customValueFormatText = customValueFormatText;
    }

    public String getCustomValueFormatText() {
        return customValueFormatText;
    }

    public void setCustomValueFormatUseHtml(boolean customValueFormatUseHtml) {
        this.customValueFormatUseHtml = customValueFormatUseHtml;
    }

    /**
     * ʹ��html
     * @return ʹ��html
     */
    public boolean isCustomValueFormatUseHtml() {
        return customValueFormatUseHtml;
    }

    public void setAlertValues(List<VanChartAlertValue> alertValues) {
        this.alertValues = alertValues;
    }

    public List<VanChartAlertValue> getAlertValues() {
        return alertValues;
    }

    public void setCustomIntervalBackgroundArray(List<VanChartCustomIntervalBackground> customIntervalBackgroundArray) {
        this.customIntervalBackgroundArray = customIntervalBackgroundArray;
    }

    public void setDefaultIntervalBackgroundColor(Color defaultIntervalBackgroundColor) {
        this.defaultIntervalBackgroundColor = defaultIntervalBackgroundColor;
    }

    public List<VanChartCustomIntervalBackground> getCustomIntervalBackgroundArray() {
        return customIntervalBackgroundArray;
    }

    public Color getDefaultIntervalBackgroundColor() {
        return defaultIntervalBackgroundColor;
    }

    /**
     * �����Ƿ����������С
     * @param limitSize �Ƿ����������С
     */
    public void setLimitSize(boolean limitSize) {
        this.limitSize = limitSize;
    }

    /**
     * �����Ƿ����������С
     * @return �����Ƿ����������С
     */
    public boolean isLimitSize() {
        return limitSize;
    }

    /**
     * �����������ռ��
     * @param maxHeight �������ռ��
     */
    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
    }

    /**
     * �����������ռ��
     * @return �����������ռ��
     */
    public double getMaxHeight() {
        return maxHeight;
    }

    public AxisType getAxisType() {
        return AxisType.AXIS_CATEGORY;
    }

    public VanChartAxis() {
        this(StringUtils.BLANK, VanChartConstants.AXIS_BOTTOM);
    }

    public VanChartAxis(String axisName, int position) {
        this.axisName = axisName;
        this.position = position;
        setMainGridStyle(Constants.LINE_THIN);
        getTextAttr().setFRFont(VanChartAttrHelper.DEFAULT_AXIS_TITLE_LABLE_FONT);
        if(getTitle() == null){
            setTitle(new Title(StringUtils.EMPTY));
        }
        getTitle().getTextAttr().setFRFont(VanChartAttrHelper.DEFAULT_AXIS_TITLE_LABLE_FONT);
        if(!VanChartAttrHelper.isXAxis(axisName)) {
            getTitle().getTextAttr().setRotation(-90);
        }
    }

    //��axisGlyph��������
    public void initAxisGlyphWithChartData(ChartData chartData, VanChartCategoryAxisGlyph axisGlyph){
        initAxisGlyph(axisGlyph);

        for (int i = 0, len = chartData.getCategoryLabelCount(); i < len; i++) {
            axisGlyph.addCategoryLabel(chartData.getCategoryPresentLabel(i) == null ? "" : chartData.getCategoryPresentLabel(i));
        }

        axisGlyph.initMinMaxValue(0, chartData.getCategoryLabelCount());
    }

    /**
     *����ChartData ���ɶ�Ӧ��AxisGlyph
     * @param chartData ����
     * @return �������ͼ��
     */
    public VanChartBaseAxisGlyph createAxisGlyph(ChartData chartData) {
        VanChartCategoryAxisGlyph axisGlyph = new VanChartCategoryAxisGlyph();

        initAxisGlyphWithChartData(chartData, axisGlyph);

        return axisGlyph;
    }

    /**
     *��ʼ����Ӧ������������
     * @param axisGlyph �������ͼ��
     */
    public void initAxisGlyph(AxisGlyph axisGlyph) {
        super.initAxisGlyph(axisGlyph);
        VanChartBaseAxisGlyph vanChartAxisGlyph = (VanChartBaseAxisGlyph)axisGlyph;
        vanChartAxisGlyph.setVanAxisType(getAxisType());
        vanChartAxisGlyph.setTitleUseHtml(isTitleUseHtml());
        vanChartAxisGlyph.setISXAxis(VanChartAttrHelper.isXAxis(getAxisName()));
        vanChartAxisGlyph.setVanAxisName(getAxisName());
        vanChartAxisGlyph.setLimitSize(isLimitSize());
        vanChartAxisGlyph.setMaxHeight(getMaxHeight());
        vanChartAxisGlyph.setAutoLabelGap(isAutoLabelGap());
        vanChartAxisGlyph.setDefaultIntervalBackgroundColor(getDefaultIntervalBackgroundColor());
        installAlertValues(vanChartAxisGlyph);
        vanChartAxisGlyph.setCustomIntervalBackgroundArray(getCustomIntervalBackgroundArray());

        vanChartAxisGlyph.setCommonValueFormat(isCommonValueFormat());
        vanChartAxisGlyph.setCustomValueFormatText(getCustomValueFormatText());
        vanChartAxisGlyph.setCustomValueFormatUseHtml(isCustomValueFormatUseHtml());

        vanChartAxisGlyph.setMainTickLine(getMainTickLine());
        vanChartAxisGlyph.setSecTickLine(getSecTickLine());
    }

    private void installAlertValues(VanChartBaseAxisGlyph axisGlyph) {
        if(alertValues != null){
            ArrayList<ChartAlertValueGlyph> alertValueList = new ArrayList<ChartAlertValueGlyph>();
            for(int index = 0; index < alertValues.size(); index++){
                VanChartAlertValue alertValue = alertValues.get(index);
                if(alertValue.getAlertValueFormula() == null){
                    continue;
                }
                VanChartAlertValueGlyph alertValueGlyph = new VanChartAlertValueGlyph();
                String text4Glyph = Utils.objectToString(alertValue.getAlertContentFormula());

                if(alertValue.getAlertContentFormula() instanceof Formula) {
                    Formula formula = (Formula)alertValue.getAlertContentFormula();
                    if(formula.getResult() != null) {
                        text4Glyph = Utils.objectToString(formula.getResult());
                    }
                }
                alertValueGlyph.setAlertContent(text4Glyph);
                alertValueGlyph.setAlertFont(alertValue.getAlertFont());
                alertValueGlyph.setAlertLineAlpha(alertValue.getAlertLineAlpha());
                alertValueGlyph.setAlertPaneSelectName(alertValue.getAlertPaneSelectName());
                alertValueGlyph.setAlertPosition(alertValue.getAlertPosition());
                alertValueGlyph.setAlertValueFormula(alertValue.getAlertValueFormula());
                alertValueGlyph.setLineColor(alertValue.getLineColor());
                alertValueGlyph.setLineStyle(alertValue.getLineStyle());
                alertValueGlyph.setIndex(index);
                alertValueGlyph.setAxisGlyph(axisGlyph);

                alertValueList.add(alertValueGlyph);
            }
            axisGlyph.setAlertValues(alertValueList);
        }
    }

    public boolean equals(Object ob) {
        return super.equals(ob) && ob instanceof VanChartAxis
                && ComparatorUtils.equals(((VanChartAxis) ob).getMainTickLine(), this.getMainTickLine())
                && ComparatorUtils.equals(((VanChartAxis) ob).getSecTickLine(), this.getSecTickLine())
                && ComparatorUtils.equals(((VanChartAxis) ob).getAxisName(), this.getAxisName())
                && ComparatorUtils.equals(((VanChartAxis) ob).isTitleUseHtml(), this.isTitleUseHtml())
                && ComparatorUtils.equals(((VanChartAxis) ob).isAutoLabelGap(), this.isAutoLabelGap())
                && ComparatorUtils.equals(((VanChartAxis) ob).isLimitSize(), this.isLimitSize())
                && ComparatorUtils.equals(((VanChartAxis) ob).getMaxHeight(), this.getMaxHeight())
                && ComparatorUtils.equals(((VanChartAxis) ob).isCommonValueFormat(), this.isCommonValueFormat())
                && ComparatorUtils.equals(((VanChartAxis) ob).getCustomValueFormatText(), this.getCustomValueFormatText())
                && ComparatorUtils.equals(((VanChartAxis) ob).isCustomValueFormatUseHtml(), this.isCustomValueFormatUseHtml())
                && ComparatorUtils.equals(((VanChartAxis) ob).getDefaultIntervalBackgroundColor(), this.getDefaultIntervalBackgroundColor())
                && ComparatorUtils.equals(((VanChartAxis) ob).getAlertValues(), this.getAlertValues())
                && ComparatorUtils.equals(((VanChartAxis) ob).getCustomIntervalBackgroundArray(), this.getCustomIntervalBackgroundArray())
                ;
    }

    public Object clone() throws CloneNotSupportedException {
        VanChartAxis axis = (VanChartAxis) super.clone();
        axis.setMainTickLine(this.getMainTickLine());
        axis.setSecTickLine(this.getSecTickLine());
        axis.setAxisName(this.getAxisName());
        axis.setTitleUseHtml(this.isTitleUseHtml());
        axis.setAutoLabelGap(this.isAutoLabelGap());
        axis.setLimitSize(this.isLimitSize());
        axis.setMaxHeight(this.getMaxHeight());
        axis.setCommonValueFormat(this.isCommonValueFormat());
        axis.setCustomValueFormatText(this.getCustomValueFormatText());
        axis.setCustomValueFormatUseHtml(this.isCustomValueFormatUseHtml());
        axis.setDefaultIntervalBackgroundColor(this.getDefaultIntervalBackgroundColor());
        axis.alertValues = new ArrayList<VanChartAlertValue>();
        for(VanChartAlertValue alertValue : this.alertValues){
            axis.alertValues.add((VanChartAlertValue)alertValue.clone());
        }
        axis.customIntervalBackgroundArray = new ArrayList<VanChartCustomIntervalBackground>();
        for(VanChartCustomIntervalBackground background : this.customIntervalBackgroundArray){
            axis.customIntervalBackgroundArray.add((VanChartCustomIntervalBackground)background.clone());
        }

        return axis;
    }

    public void readXML(XMLableReader reader) {
        super.readXML(reader);
        if (reader.isChildNode()) {
            String name = reader.getTagName();
            if (name.equals("VanChartAxisAttr")) {
                int mainTick = reader.getAttrAsInt("mainTickLine", -1);
                int secTick = reader.getAttrAsInt("secTickLine", -1);
                if(mainTick != -1){
                    mainTickLine = AxisTickLineType.parse(mainTick);
                }
                if(secTick != -1){
                    secTickLine = AxisTickLineType.parse(secTick);
                }
                axisName = reader.getAttrAsString("axisName", "");
                titleUseHtml = reader.getAttrAsBoolean("titleUseHtml",false);
                autoLabelGap = reader.getAttrAsBoolean("autoLabelGap",true);
                limitSize = reader.getAttrAsBoolean("limitSize",false);
                maxHeight = reader.getAttrAsInt("maxHeight",0);
                commonValueFormat = reader.getAttrAsBoolean("commonValueFormat",true);
                customValueFormatText = reader.getAttrAsString("customValueFormatText","");
                customValueFormatUseHtml = reader.getAttrAsBoolean("customValueUseHtml",false);
                defaultIntervalBackgroundColor = reader.getAttrAsColor("defaultIntervalBackgroundColor",null);
            } else if(name.equals("alertList")){
                reader.readXMLObject(new XMLReadable() {
                    @Override
                    public void readXML(XMLableReader reader) {
                        if (reader.isChildNode()) {
                            if (reader.getTagName().equals("VanChartAlert")) {
                                VanChartAlertValue alertValue = (VanChartAlertValue)GeneralXMLTools.readXMLable(reader);
                                alertValues.add(alertValue);
                            }
                        }
                    }
                });
            } else if(name.equals("customBackgroundList")){
                reader.readXMLObject(new XMLReadable() {
                    @Override
                    public void readXML(XMLableReader reader) {
                        if (reader.isChildNode()) {
                            if (reader.getTagName().equals("VanChartCustomBackground")) {
                                VanChartCustomIntervalBackground background = (VanChartCustomIntervalBackground)GeneralXMLTools.readXMLable(reader);
                                customIntervalBackgroundArray.add(background);
                            }
                        }
                    }
                });

            }
        }
    }

    public void writeXML(XMLPrintWriter writer) {
        super.writeXML(writer);

        writer.startTAG("VanChartAxisAttr")
                .attr("mainTickLine", getMainTickLine().getType())
                .attr("secTickLine", getSecTickLine().getType())
                .attr("axisName", axisName)
                .attr("titleUseHtml", titleUseHtml)
                .attr("autoLabelGap", autoLabelGap)
                .attr("limitSize", limitSize)
                .attr("maxHeight", maxHeight)
                .attr("commonValueFormat", commonValueFormat)
                .attr("customValueFormatText", customValueFormatText)
                .attr("customValueUseHtml", customValueFormatUseHtml);

        if(defaultIntervalBackgroundColor != null){
            writer.attr("defaultIntervalBackgroundColor", defaultIntervalBackgroundColor.getRGB());
        }
        writer.end();

        if(alertValues != null){
            writer.startTAG("alertList");
            for(VanChartAlertValue alert : alertValues){
                GeneralXMLTools.writeXMLable(writer, alert, "VanChartAlert");
            }
            writer.end();
        }

        if(customIntervalBackgroundArray != null){
            writer.startTAG("customBackgroundList");
            for(VanChartCustomIntervalBackground background : customIntervalBackgroundArray){
                GeneralXMLTools.writeXMLable(writer, background, "VanChartCustomBackground");
            }
            writer.end();
        }
    }

    /**
     * SE�д���ʽ
     * @param calculator  ��ʽ������.
     */
    public void dealFormula(Calculator calculator) {
        super.dealFormula(calculator);

        for(VanChartAlertValue alertValue : alertValues){
            alertValue.dealFormula(calculator);
        }

        for(VanChartCustomIntervalBackground background : customIntervalBackgroundArray){
            background.dealFormula(calculator);
        }
    }

    /**
     * Ԥ�ȼ���ۺ�ͼ�� ��乫ʽ˳��.
     * @param calculator  ��ʽ������
     *                    @param list  ����б�
     */
    public void buidExecuteSequenceList(List list, Calculator calculator) {
        super.buidExecuteSequenceList(list, calculator);

        for(VanChartAlertValue alertValue : alertValues){
            alertValue.buidExecuteSequenceList(list, calculator);
        }

        for(VanChartCustomIntervalBackground background : customIntervalBackgroundArray){
            background.buidExecuteSequenceList(list, calculator);
        }
    }

    /**
     * ����ɾ������ʱ ��ʽ����
     * @param mod  ���б䶯
     */
    public void modFormulaString(MOD_COLUMN_ROW mod) {
        super.modFormulaString(mod);

        for(VanChartAlertValue alertValue : alertValues){
            alertValue.modFormulaString(mod);
        }

        for (VanChartCustomIntervalBackground background : customIntervalBackgroundArray){
            background.modFormulaString(mod);
        }
    }
}
