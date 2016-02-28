package com.fr.plugin.chart.attr;


import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.chart.chartattr.Title;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.plugin.chart.glyph.VanChartTitleGlyph;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.awt.*;

/**
 * Created by Mitisky on 15/8/17.
 */
public class VanChartTitle extends Title {

    private static final long serialVersionUID = -618243870663433046L;
    public static final String XML_TAG = "Title4VanChart";

    private boolean useHtml = false;

    private boolean floating = false;
    private double floatPercentX;
    private double floatPercentY;

    private boolean limitSize = false;
    private double maxHeight = 15;

    public VanChartTitle(){
        FRFont frFont = FRFont.getInstance("Microsoft YaHei", Font.PLAIN, 16, new Color(51, 51, 51));
        getTextAttr().setFRFont(frFont);
        setTextObject(Inter.getLocText("FR-Chart-Title_NewTitle"));
    }

    /**
     * �����ı�����ΪtextObject�ı������
     * @param textObject �����ı�����
     */
    public VanChartTitle(Object textObject) {
        super(textObject);
    }

    /**
     * �����Ƿ�ʹ��html�����ı�
     * @param useHtml �Ƿ�ʹ��html�����ı�
     */
    public void setUseHtml(boolean useHtml) {
        this.useHtml = useHtml;
    }

    /**
     * �����Ƿ�ʹ��html�����ı�
     * @return �Ƿ�ʹ��html�����ı�
     */
    public boolean isUseHtml() {
        return this.useHtml;
    }

    /**
     * �����Ƿ��Զ�������λ��
     * @param floating �����Ƿ��Զ�������λ��
     */
    public void setFloating(boolean floating) {
        this.floating = floating;
    }

    /**
     * �����Ƿ��Զ�������λ��
     * @return �����Ƿ��Զ�������λ��
     */
    public boolean isFloating() {
        return floating;
    }

    /**
     * ��������λ�� �������ϽǺ���λ��
     * @param x ����λ��
     */
    public void setFloatPercentX(double x) {
        this.floatPercentX = x;
    }

    /**
     * ��������λ�� �������ϽǺ���λ��
     * @return ����λ��
     */
    public double getFloatPercentX() {
        return floatPercentX;
    }

    /**
     * ��������λ�� �������Ͻ�����λ��
     * @param y ����λ��
     */
    public void setFloatPercentY(double y) {
        this.floatPercentY = y;
    }

    /**
     * ��������λ�� �������Ͻ�����λ��
     * @return ����λ��
     */
    public double getFloatPercentY() {
        return floatPercentY;
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

    /**
     * ����������תΪ����ͼ��
     * @return ����ͼ��
     */
    public VanChartTitleGlyph createGlyph() {
        String text4Glyph = Utils.objectToString(getTextObject());
        if(getTextObject() instanceof Formula) {
            Formula formula = (Formula)getTextObject();
            if(formula.getResult() != null) {
                text4Glyph = Utils.objectToString(formula.getResult());
            }
        }

        VanChartTitleGlyph vanChartTitleGlyph = new VanChartTitleGlyph(text4Glyph.trim(), getTextAttr());
        vanChartTitleGlyph.setGeneralInfo(this);
        vanChartTitleGlyph.setPosition(getPosition());
        vanChartTitleGlyph.setVisible(isTitleVisble());

        vanChartTitleGlyph.setUseHtml(isUseHtml());
        vanChartTitleGlyph.setFloating(isFloating());
        vanChartTitleGlyph.setFloatPercentX(getFloatPercentX());
        vanChartTitleGlyph.setFloatPercentY(getFloatPercentY());
        vanChartTitleGlyph.setMaxHeight(getMaxHeight());
        vanChartTitleGlyph.setLimitSize(isLimitSize());

        return vanChartTitleGlyph;
    }

    /**
     * ��ȡXML����
     * @param reader XML��ȡ��
     */
    public void readXML(XMLableReader reader) {
        super.readXML(reader);

        if (reader.isChildNode()) {
            String name = reader.getTagName();
            if ("Attr4VanChart".equals(name)) {
                setUseHtml(reader.getAttrAsBoolean("useHtml", false));
                setFloating(reader.getAttrAsBoolean("floating", false));
                setFloatPercentX(reader.getAttrAsInt("x", 0));
                setFloatPercentY(reader.getAttrAsInt("y", 0));
                setLimitSize(reader.getAttrAsBoolean("limitSize", false));
                setMaxHeight(reader.getAttrAsFloat("maxHeight", 0));
            }
        }
    }

    /**
     *  дXML
     * @param writer XML�������
     */
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);

        super.writeXML(writer);

        writer.startTAG("Attr4VanChart")
                .attr("useHtml", isUseHtml())
                .attr("floating", isFloating())
                .attr("x", getFloatPercentX())
                .attr("y", getFloatPercentY())
                .attr("limitSize", isLimitSize())
                .attr("maxHeight", getMaxHeight())
                .end();

        writer.end();
    }

    /**
     * �ȽϺ�Object�Ƿ����
     * @param ob �� �ڱȽϵ�Object
     * @return һ��booleanֵ
     */
    public boolean equals(Object ob) {
        return ob instanceof VanChartTitle && super.equals(ob)
                && ((VanChartTitle)ob).isUseHtml() == isUseHtml()
                && ((VanChartTitle)ob).isFloating() == isFloating()
                && ((VanChartTitle)ob).getFloatPercentX() == getFloatPercentX()
                && ((VanChartTitle)ob).getFloatPercentY() == getFloatPercentY()
                && ((VanChartTitle)ob).isLimitSize() == isLimitSize()
                && ((VanChartTitle)ob).getMaxHeight() == getMaxHeight();
    }

    public Object clone() throws CloneNotSupportedException {
        VanChartTitle vanChartTitle = (VanChartTitle)super.clone();

        vanChartTitle.setUseHtml(this.isUseHtml());
        vanChartTitle.setFloating(this.isFloating());
        vanChartTitle.setFloatPercentX(this.getFloatPercentX());
        vanChartTitle.setFloatPercentY(this.getFloatPercentY());
        vanChartTitle.setLimitSize(this.isLimitSize());
        vanChartTitle.setMaxHeight(this.getMaxHeight());

        return vanChartTitle;
    }
}
