package com.fr.solution.plugin.chart.echarts.base;

import com.fr.chart.chartglyph.LegendGlyph;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.stable.web.Repository;

/**
 * Created by richie on 16/2/2.
 */
public class NewLegendGlyph extends LegendGlyph {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2987787576292955289L;
	public String orient;
	public String x;
	public String data;
	
	public NewLegendGlyph() {
		// TODO Auto-generated constructor stub
	}
    @Override
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        return createLegend(repo);
    }
    private JSONObject createLegend(Repository repo) throws JSONException {
        return JSONObject.create()
                .put("orient", getOrient())
                .put("x", getX())
                .put("data", getData());
    }
	public String getOrient() {
		return orient;
	}
	public void setOrient(String orient) {
		this.orient = orient;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
