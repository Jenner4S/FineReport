package com.fr.solution.plugin.chart.echarts.base;

import com.fr.chart.chartglyph.ChartGlyph;
import com.fr.chart.chartglyph.PlotGlyph;
import com.fr.chart.chartglyph.TitleGlyph;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.stable.web.Repository;

/**
 * Created by richie on 16/1/29.
 */
public class NewGlyph extends ChartGlyph {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8736597051837437709L;

	private NewTitleGlyph titleGlyph;

//    private NewLegendGlyph legendGlyph;
    
    private MapChartDataRange dataRange;
//    private NewSeriesGlyph seriseGlph;
    
//    private 
    public void setTitleGlyph(NewTitleGlyph titleGlyph) {
        this.titleGlyph = titleGlyph;
    }

//    @Override
//    public NewLegendGlyph getLegendGlyph() {
//        return legendGlyph;
//    }
//
//    public void setLegendGlyph(NewLegendGlyph legendGlyph) {
//        this.legendGlyph = legendGlyph;
//    }

    @Override
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        JSONObject jo = new JSONObject();
        
        if (titleGlyph != null) {
            jo.put("title", getTitleGlyph().toJSONObject(repo));
        }
        jo.put("tooltip", createTooltip(repo));
        /*if (legendGlyph != null) {
            jo.put("legend", legendGlyph.toJSONObject(repo));
        }else{
        	
        	jo.put("legend", createLegend(repo));
        }*/
        if(dataRange==null){
        	 jo.put("dataRange", createDataRange(repo));
        }else{
        	 jo.put("dataRange", getDataRange().toJSONObject(repo));
        }
       
        PlotGlyph plotGlyph = getPlotGlyph();
        if(plotGlyph == null){
            return jo;
        }
        plotGlyph.addSeriesJSON(jo, repo);
        System.out.println("NEWGLYPH:::"+jo.toString());
        return jo;
    }


    private JSONObject createTooltip(Repository repo) throws JSONException {
        return JSONObject.create()
                .put("trigger", "item");
    }
/*
	dataRange: {
        min : 0,
        max : 100,
        calculable : true,
        color: ['#ff3333', 'orange', 'yellow','lime','aqua'],
        textStyle:{
            color:'#fff'
        }
    },
	*/
	
	
/*
    private JSONObject createDataRange(Repository repo) throws JSONException {
        return JSONObject.create()
                .put("min", 0)
                .put("max", 250)
                .put("x", "left")
                .put("y", "bottom")
//                .put("calculable","true")
//                .put("color","['orange','yellow']")
                .put("text", JSONArray.create().put("ธ฿").put("ตอ"))
                .put("textStyle", "{color:'#fff' }");
        
    }*/
    private JSONObject createDataRange(Repository repo) throws JSONException {
        return JSONObject.create()
                .put("min", 0)
                .put("max", 2500)
                .put("x", "left")
                .put("y", "bottom")
                .put("text", JSONArray.create().put("ธ฿").put("ตอ"));
    }

    public TitleGlyph getTitleGlyph() {
		return titleGlyph;
	}

	public MapChartDataRange getDataRange() {
		return dataRange;
	}

	public void setDataRange(MapChartDataRange dataRange) {
		this.dataRange = dataRange;
	}
}
