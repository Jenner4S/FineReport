package com.fr.plugin.chart.base;

import com.fr.chart.base.TextAttr;
import com.fr.general.ComparatorUtils;
import com.fr.stable.Constants;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLable;
import com.fr.stable.xml.XMLableReader;

import java.awt.*;

/**
 * Created by Mitisky on 15/12/7.
 */
public class AttrLabelDetail implements XMLable{

    private static final long serialVersionUID = -3240037946477132101L;
    private AttrTooltipContent content = new AttrTooltipContent();

    private int position = Constants.INSIDE;
    private boolean isShowGuidLine = false;

    private boolean isCustom = false;
    private TextAttr textAttr = new TextAttr();

    private Color backgroundColor;

    public void setContent(AttrTooltipContent content) {
        this.content = content;
    }

    public void setCustom(boolean isCustom) {
        this.isCustom = isCustom;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setShowGuidLine(boolean isShowGuidLine) {
        this.isShowGuidLine = isShowGuidLine;
    }

    public void setTextAttr(TextAttr textAttr) {
        this.textAttr = textAttr;
    }

    public TextAttr getTextAttr() {
        return textAttr;
    }

    public int getPosition() {
        return position;
    }

    /**
     * 牵引线
     * @return 牵引线
     */
    public boolean isShowGuidLine() {
        return isShowGuidLine;
    }

    /**
     * 自定义
     * @return 自定义
     */
    public boolean isCustom() {
        return isCustom;
    }

    public AttrTooltipContent getContent() {
        return content;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void readXML(XMLableReader reader) {
        String tagName = reader.getTagName();
        if(tagName.equals("Attr")) {
            this.isShowGuidLine = reader.getAttrAsBoolean("showLine", false);
            this.position = reader.getAttrAsInt("position", Constants.TOP);
            this.isCustom = reader.getAttrAsBoolean("isCustom", true);
            this.backgroundColor = reader.getAttrAsColor("backgroundColor", null);
        } else if (TextAttr.XML_TAG.equals(tagName)) {
            this.setTextAttr((TextAttr)reader.readXMLObject(new TextAttr()));
        } else if (AttrTooltipContent.XML_TAG.equals(tagName)){
            this.setContent((AttrTooltipContent)reader.readXMLObject(new AttrTooltipContent()));
        }

    }

    public void writeXML(XMLPrintWriter writer) {

        writer.startTAG("Attr")
                .attr("showLine", isShowGuidLine)
                .attr("position", position)
                .attr("isCustom", isCustom)
        ;
        if(backgroundColor != null){
            writer.attr("backgroundColor", backgroundColor.getRGB());
        }
        writer.end();

        if (this.textAttr != null) {
            this.textAttr.writeXML(writer);
        }

        this.content.writeXML(writer);
    }

    public boolean equals(Object ob) {
        return ob instanceof AttrLabelDetail
                && ComparatorUtils.equals(((AttrLabelDetail) ob).getContent(), this.getContent())
                && ComparatorUtils.equals(((AttrLabelDetail) ob).getPosition(), this.getPosition())
                && ComparatorUtils.equals(((AttrLabelDetail) ob).isCustom(), this.isCustom())
                && ComparatorUtils.equals(((AttrLabelDetail) ob).getTextAttr(), this.getTextAttr())
                && ComparatorUtils.equals(((AttrLabelDetail) ob).isShowGuidLine(), this.isShowGuidLine())
                && ComparatorUtils.equals(((AttrLabelDetail) ob).getBackgroundColor(), this.getBackgroundColor())
                ;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
