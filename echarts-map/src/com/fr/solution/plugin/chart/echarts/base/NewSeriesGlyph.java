package com.fr.solution.plugin.chart.echarts.base;

import java.util.List;

import com.fr.chart.chartglyph.ChartGlyph;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.stable.web.Repository;

/**
 * Created by richie on 16/2/2.
 */
public class NewSeriesGlyph extends ChartGlyph {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3941616723732030648L;
	public String name;
	public String type;
	public String mapType;
	public String itemStyle;
	public String data;
	public List<NewSeriesGlyph> list ;
	public void addNewSeriesGlyph(NewSeriesGlyph gly ){
		list.add(gly);
	}
	public NewSeriesGlyph() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public JSONObject toJSONObject(Repository arg0) throws JSONException {
		// TODO Auto-generated method stub
		JSONArray series = JSONArray.create();
		for(int i=0;i<list.size();i++)
		{
			 series.put(list.get(i));
		}
		JSONObject jo = series.toJSONObject(series);
		return jo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMapType() {
		return mapType;
	}
	public void setMapType(String mapType) {
		this.mapType = mapType;
	}
	public String getItemStyle() {
		return itemStyle;
	}
	public void setItemStyle(String itemStyle) {
		this.itemStyle = itemStyle;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	
	

}
