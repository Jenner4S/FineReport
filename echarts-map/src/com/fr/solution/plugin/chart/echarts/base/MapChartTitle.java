package com.fr.solution.plugin.chart.echarts.base;

import com.fr.chart.chartattr.Title;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

public class MapChartTitle extends Title {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3788680495531705318L;
	public static final String XML_TAG = "MapChartTitle";
    private String title;
    private String  subTitle;
    private boolean isShow;
//    private String location_x;
    @Override
    public NewTitleGlyph createGlyph() {
    	super.createGlyph();
    	NewTitleGlyph titleGlyph = new NewTitleGlyph(getTitle(),getSubTitle());
    	titleGlyph.setPosition(getPosition());
    	titleGlyph.setShow(isShow());
//    	titleGlyph.setShow();
        return titleGlyph;
    }
    public MapChartTitle(Object textObject) {
        super(textObject);
    }
    public MapChartTitle() {
        super();
    }
    @Override
    public void readXML(XMLableReader reader) {
        super.readXML(reader);
        if (reader.isChildNode()) {
            String name = reader.getTagName();
            if ("Attr4MapChart".equals(name)) {
                setTitle(reader.getAttrAsString("Title", "\u9ED8\u8BA4\u4E3B\u6807\u9898"));//getAttrAsBoolean("useHtml", false));
                setSubTitle(reader.getAttrAsString("SubTitle", "\u9ED8\u8BA4\u526F\u6807\u9898"));
                setShow(reader.getAttrAsBoolean("IsShow", true));
            }
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);
        super.writeXML(writer);
        writer.startTAG("Attr4MapChart")
                .attr("Title", getTitle())
                .attr("SubTitle", getSubTitle())
                .attr("IsShow", isShow())
                .end();

        writer.end();
    }


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}/*
	public String getLocation_x() {
		return location_x;
	}
	public void setLocation_x(String location_x) {
		this.location_x = location_x;
	}*/
	public boolean isShow() {
		return isShow;
	}
	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}
}

