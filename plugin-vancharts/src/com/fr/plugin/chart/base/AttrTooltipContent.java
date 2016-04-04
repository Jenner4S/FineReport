package com.fr.plugin.chart.base;

import com.fr.base.BaseXMLUtils;
import com.fr.base.CoreDecimalFormat;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.base.ChartConstants;
import com.fr.general.ComparatorUtils;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.*;

import java.text.DecimalFormat;
import java.text.Format;

/**
 *  ���ݵ���ʾ�ͱ�ǩ����������
 */
public class AttrTooltipContent implements XMLable{

    private static final long serialVersionUID = 1834245036047518367L;
    private static final String PREFIX = "function(){ return ";
    private static final String SUFFIX = ";}";
    private static final String ADD = "+";
    private static final String CATEGORY = "this.category";
    private static final String SERIES = "this.seriesName";
    private static final String VALUE = "this.value";
    private static final String PERCENT = "this.percentage";

    public static final String XML_TAG = "AttrToolTipContent";

    private boolean isCommon = true;
    private boolean isCategoryName = false;
    private boolean isSeriesName = false;
    private boolean isValue = true;
    private boolean isPercentValue = false;

    private Format format = new CoreDecimalFormat(new DecimalFormat("#.##"), "#.##");
    private Format percentFormat = new CoreDecimalFormat(new DecimalFormat("#.##%"), "#.##%");

    private String customText;
    private boolean useHtml;

    /**
     * ���ñ�ǩ (ϵ��ֵ)�����ָ�ʽ
     */
    public void setFormat(Format format) {
        this.format = format;
    }

    /**
     * ���ر�ǩ(ϵ��ֵ)�����ָ�ʽ.
     */
    public Format getFormat() {
        return format;
    }

    /**
     * ���ðٷֱȵ����ָ�ʽ
     */
    public void setPercentFormat(Format percentFormat) {
        this.percentFormat = percentFormat;
    }

    /**
     * �������ָ�ʽ�İٷֱȸ�ʽ.
     */
    public Format getPercentFormat() {
        return percentFormat;
    }

    public void setCategoryName(boolean isCategoryName) {
        this.isCategoryName = isCategoryName;
    }

    public void setCommon(boolean isCommon) {
        this.isCommon = isCommon;
    }

    public void setCustomText(String customText) {
        this.customText = customText;
    }

    public void setPercentValue(boolean isPercentValue) {
        this.isPercentValue = isPercentValue;
    }

    public void setSeriesName(boolean isSeriesName) {
        this.isSeriesName = isSeriesName;
    }

    public void setUseHtml(boolean useHtml) {
        this.useHtml = useHtml;
    }

    public void setValue(boolean isValue) {
        this.isValue = isValue;
    }

    /**
     * ������
     * @return ������
     */
    public boolean isCategoryName() {
        return isCategoryName;
    }

    /**
     * ͨ��
     * @return ͨ��
     */
    public boolean isCommon() {
        return isCommon;
    }

    /**
     * ֵ��ռ�ٷֱ�
     * @return ֵ��ռ�ٷֱ�
     */
    public boolean isPercentValue() {
        return isPercentValue;
    }

    /**
     * ϵ����
     * @return ϵ����
     */
    public boolean isSeriesName() {
        return isSeriesName;
    }

    /**
     * ֵ
     * @return ֵ
     */
    public boolean isValue() {
        return isValue;
    }

    /**
     * ʹ��html
     * @return ʹ��html
     */
    public boolean isUseHtml() {
        return useHtml;
    }

    public String getCustomText() {
        return customText;
    }

    public void setAllSelectFalse() {
        setCategoryName(false);
        setSeriesName(false);
        setValue(false);
        setPercentValue(false);
    }

    public void readXML(XMLableReader reader) {
        if(reader.isChildNode()){
            String tagName = reader.getTagName();
            if(tagName.equals("Attr")) {
                isCommon = reader.getAttrAsBoolean("isCommon", true);
                isCategoryName = reader.getAttrAsBoolean("isCategoryName", false);
                isSeriesName = reader.getAttrAsBoolean("isSeriesName", false);
                isValue = reader.getAttrAsBoolean("isValue", true);
                isPercentValue = reader.getAttrAsBoolean("isPercentValue", false);
                customText = reader.getAttrAsString("customText", StringUtils.EMPTY);
                useHtml = reader.getAttrAsBoolean("useHtml", false);
            } else if (XMLConstants.FORMAT_TAG.equals(tagName)) {
                this.setFormat(BaseXMLUtils.readFormat(reader));
            } else if ("PercentFormat".equals(tagName)){
                reader.readXMLObject(new XMLReadable() {
                    public void readXML(XMLableReader reader) {
                        if (reader.isChildNode()) {
                            if (XMLConstants.FORMAT_TAG.equals(reader.getTagName())){
                                AttrTooltipContent.this.setPercentFormat(BaseXMLUtils.readFormat(reader));
                            }

                        }
                    }
                });
            }
        }

    }

    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);
        writer.startTAG("Attr")
                .attr("isCommon", isCommon)
                .attr("isCategoryName", isCategoryName)
                .attr("isSeriesName", isSeriesName)
                .attr("isValue", isValue)
                .attr("isPercentValue", isPercentValue)
                .attr("customText", customText)
                .attr("useHtml", useHtml)
                .end();

        if (this.format != null) {
            BaseXMLUtils.writeFormat(writer, this.format);
        }
        if (this.percentFormat != null) {
            writer.startTAG("PercentFormat");
            BaseXMLUtils.writeFormat(writer, this.percentFormat);
            writer.end();
        }
        writer.end();


    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean equals(Object ob) {
        return ob instanceof AttrTooltipContent
                && ComparatorUtils.equals(((AttrTooltipContent) ob).isCommon(), this.isCommon())
                && ComparatorUtils.equals(((AttrTooltipContent) ob).isCategoryName(), this.isCategoryName())
                && ComparatorUtils.equals(((AttrTooltipContent) ob).isSeriesName(), this.isSeriesName())
                && ComparatorUtils.equals(((AttrTooltipContent) ob).isValue(), this.isValue())
                && ComparatorUtils.equals(((AttrTooltipContent) ob).isPercentValue(), this.isPercentValue())
                && ComparatorUtils.equals(((AttrTooltipContent) ob).getCustomText(), this.getCustomText())
                && ComparatorUtils.equals(((AttrTooltipContent) ob).isUseHtml(), this.isUseHtml())
                && ComparatorUtils.equals(((AttrTooltipContent) ob).getFormat(), this.getFormat())
                && ComparatorUtils.equals(((AttrTooltipContent) ob).getPercentFormat(), this.getPercentFormat())
                ;
    }

    /**
     * �Ƿ��������
     * @return �����Ƿ��й�ѡ��������һ������
     */
    public boolean hasLabelContent() {
        return isCategoryName || isSeriesName || isValue || isPercentValue;
    }


    /**
     * תΪjson����
     * @return ����json
     * @throws com.fr.json.JSONException �״�
     */
    public void toJSONObject(JSONObject js) throws JSONException {
        if(isCommon){
            JSONObject commonJs = new JSONObject();
            commonJs.put("identifier", getCommonLabelContent());
            commonJs.put("valueFormat", ChartBaseUtils.format2JS(this.format));
            commonJs.put("percentFormat", ChartBaseUtils.format2JS(this.percentFormat));

            js.put("formatter", commonJs);
        } else if(!customText.isEmpty()){
            js.put("formatter", customText);
            js.put("useHtml", useHtml);
        }
    }

    public String getCommonLabelContent() {
        String contents = StringUtils.EMPTY;
        if (isCategoryName()) {
            contents += ChartConstants.CATEGORY_PARA;
        }
        if (isSeriesName()) {
            contents += ChartConstants.SERIES_PARA;
        }
        if (isValue()) {
            contents += ChartConstants.VALUE_PARA;
        }
        if (isPercentValue()) {
            contents += ChartConstants.PERCENT_PARA;
        }
        return contents;
    }

    public String getFormatterTextFromCommon() {
        String formatterString = StringUtils.EMPTY;
        boolean b = isCategoryName || isSeriesName || isValue;
        if(b || isPercentValue){
            formatterString += PREFIX;
            if(isCategoryName){
                formatterString += CATEGORY;
            }
            if(isSeriesName){
                formatterString += isCategoryName ? ADD : StringUtils.EMPTY;
                formatterString += SERIES;
            }
            if(isValue){
                formatterString += (isCategoryName || isSeriesName) ? ADD : StringUtils.EMPTY;
                formatterString += VALUE;
            }
            if(isPercentValue){
                formatterString += b ? ADD :StringUtils.EMPTY;
                formatterString += PERCENT;
            }
            formatterString += SUFFIX;

        }
        return formatterString;
    }
}
