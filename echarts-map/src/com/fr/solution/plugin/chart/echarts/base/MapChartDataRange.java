package com.fr.solution.plugin.chart.echarts.base;

import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.stable.web.Repository;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLable;
import com.fr.stable.xml.XMLableReader;

public class MapChartDataRange implements XMLable {
	

    /**
	 * 
	 */
	private static final long serialVersionUID = 7747795991409026124L;
	public static final String XML_TAG = "MapDataRange";
	private String max;
	private String min;
	
	public Object clone() throws CloneNotSupportedException {
//        VanChartZoom newZoom = (VanChartZoom)super.clone();
		/*
        newZoom.setFrom(getFrom());
        newZoom.setTo(getTo());
        newZoom.setZoomResize(isZoomResize());
        newZoom.setZoomType(getZoomType());
        newZoom.setZoomVisible(isZoomVisible());*/
        return null;
    }
	@Override
	public void readXML(XMLableReader reader) {
		// TODO Auto-generated method stub
		if (reader.isChildNode()) {
            String name = reader.getTagName();
            if("DataRange".equals(name)){
                setMax(reader.getAttrAsString("max", "1000"));
                setMin(reader.getAttrAsString("min", "1000"));
            }
        }
	}

	@Override
	public void writeXML(XMLPrintWriter writer) {
		// TODO Auto-generated method stub
		 writer.startTAG(XML_TAG);
	        writer.startTAG("DataRange")
	                .attr("max", getMax())
	                .attr("min", getMin())
	                .end();
	        writer.end();
	}

	
	/**
     * ����json����
     * @return js����
     * @throws com.fr.json.JSONException �״�
     */
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        JSONObject js = new JSONObject();
        js
        .put("min", getMin())
        .put("max", getMax())
        .put("calculable", "true")
        .put("color", new JSONArray().put("red").put("blue"))
        .put("textStyle", new JSONObject().put("color", "#000"))
//        .put("y", "bottom")
        .put("text", JSONArray.create().put("\u9AD8").put("\u4F4E"));
        
        return js;
    }
	public String getMax() {
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}
	public String getMin() {
		return min;
	}
	public void setMin(String min) {
		this.min = min;
	}
}
