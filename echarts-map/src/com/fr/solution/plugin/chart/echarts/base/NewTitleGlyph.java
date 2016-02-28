package com.fr.solution.plugin.chart.echarts.base;

import com.fr.chart.chartglyph.TitleGlyph;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.stable.web.Repository;

/**
 * Created by richie on 16/2/2.
 */
public class NewTitleGlyph extends TitleGlyph {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8824387638646712048L;
	private String subTitle;
	private String title;
	private boolean isShow;
	public NewTitleGlyph() {
		// TODO Auto-generated constructor stub
	}
	
    public NewTitleGlyph(String title, String subTitle) {
    	setTitle(title);
    	setSubTitle(subTitle);
    	System.out.println("TitleGLYPH£º£º£º"+getTitle()+getSubTitle());
    }

    @Override
    public JSONObject toJSONObject(Repository repo) throws JSONException {
    	String x ="";
    	switch(getPosition()){
    	case 0:x="left";break;
    	case 2 :x="right";break;
    	default :x="center";
    	}
    	if(isShow()){
    		return JSONObject.create()
                    .put("text", getTitle())
                    .put("subtext", getSubTitle())
                    .put("x", x);
    	}
    	
    	 return JSONObject.create()
    			 
                /* .put("text", getTitle())
                 .put("subtext", getSubTitle())
                 .put("x", ChartUtils.getPositionString(getPosition()))*/
                 /*.put("x", getPosition())*/;
//        return createTitle(repo);
    }

    public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	

}
