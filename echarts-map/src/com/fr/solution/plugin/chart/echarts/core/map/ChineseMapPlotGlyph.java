package com.fr.solution.plugin.chart.echarts.core.map;

import java.io.IOException;

import com.fr.chart.chartglyph.DataPoint;
import com.fr.chart.chartglyph.DataSeries;
import com.fr.chart.chartglyph.PlotGlyph;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.solution.plugin.chart.echarts.GetLatAndLngByBaidu;
import com.fr.solution.plugin.chart.echarts.base.ChartMapType;
import com.fr.solution.plugin.chart.echarts.base.MapChartNameRef;
import com.fr.stable.StringUtils;
import com.fr.stable.web.Repository;

/**
 * Created by richie on 16/1/29.
 */
public class ChineseMapPlotGlyph extends PlotGlyph {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7667117676086456095L;
	private ChartMapType type=ChartMapType.CHINESE_MAP;
	private MapChartNameRef name_ref;
    @Override
    public void layoutAxisGlyph(int resolution) {

    }

    @Override
    public void layoutDataSeriesGlyph(int resolution) {
        /*for (int categoryIndex = 0; categoryIndex < this.getCategoryCount(); categoryIndex++) {
        	DataSeries dataSeries = this.getSeries(resolution);
        }*/
    	for (int categoryIndex = 0; categoryIndex < this.getCategoryCount(); categoryIndex++) {
            deal4EachCategory(categoryIndex, resolution);
        }

    }

    private void deal4EachCategory(int categoryIndex, int resolution) {
//        initLabelData(categoryIndex);
        dealWithDataSeries(categoryIndex, resolution);
    }
    
    
    
    
    @Override
    public void addSeriesJSON(JSONObject js, Repository repo) throws com.fr.json.JSONException {
//    	System.out.println("PLOT::"+getType().getType());
    	if(type.getType()=="guide"){
				guideMap(js, repo);
    		return;
    	}
    		
    	 chineseOrworldMap(js, repo);
    }

    private void guideMap(JSONObject js, Repository repo)
			throws JSONException {
		int cateCount = getCategoryCount();
         int seriesCount = getSeriesSize();
         if (cateCount > 0){
        	 JSONObject series = new JSONObject();
        	 series.put("name", "")
        	 .put("type", "map")
        	 .put("mapType", "china")
        	 .put("data", new JSONArray())
        	 ;
        	 JSONObject markline = new JSONObject();
        	 markline.put("smooth", "true")
        	 .put("effect", createMarklineEffect())
        	 .put("itemStyle", createMarklineStyle());
        	 JSONObject markpoint = new JSONObject();
        	 
        	 markpoint.put("effect", JSONObject.create().put("show", "true").put("shadowBlur", 0));
        	 JSONObject geodata = JSONObject.create();
        	 JSONArray linedata = new JSONArray();
        	 JSONArray pointdata = new JSONArray();
             for(int cateIndex = 0; cateIndex < cateCount; cateIndex++){
                 String cateName = StringUtils.EMPTY;
                 for(int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++){
                     DataSeries dataSeries = getSeries(seriesIndex);
                     DataPoint dataPoint = dataSeries.getDataPoint(cateIndex);
                     cateName = dataPoint.getCategoryName();
                      String label = dataPoint.getSeriesName();
                     double value = dataPoint.getValue();
                     System.out.println("\u7CFB\u5217\u540D"+label+"\u503C"+value+ " \u5206\u7C7B\u540D" + cateName);
                     System.out.println(dataPoint.toJSONObject(repo).toString());
                     if(value>0){
                    	 if(name_ref!=null)
                    	 {
                    		 if(name_ref.getListName()!=null)
                    		 {
                    			 System.out.println("得到的值：："+name_ref.getListName().toString());
                            	 if(name_ref.getListName().has(label)){
                            		 label = name_ref.getListName().getString(label);
                            	 }
                    		 }
                    	 }
                    	 if(!geodata.has(label)){
                    		 geodata.put(label, GetLatAndLngByBaidu.getCoordinate(label));
                    	 }
                    	 if(!geodata.has(cateName)){
                    		 geodata.put(cateName, GetLatAndLngByBaidu.getCoordinate(cateName));
                    	 }
                    	 linedata.put(JSONArray.create().put(JSONObject.create().put("name", cateName)).put(JSONObject.
                    			 create().put("name",label).put("value", value)));
                    	 pointdata.put(JSONObject.create().put("name", label).put("value", value));
                     }
                 }
             }
             markline.put("data", linedata);
             String str = "{'\u4E0A\u6D77':[121.4648,31.2891],'\u4E1C\u839E':[113.8953,22.901],'\u4E1C\u8425':[118.7073,37.5513],'\u4E2D\u5C71':[113.4229,22.478],'\u4E34\u6C7E':[111.4783,36.1615],'\u4E34\u6C82':[118.3118,35.2936],'\u4E39\u4E1C':[124.541,40.4242],'\u4E3D\u6C34':[119.5642,28.1854],'\u4E4C\u9C81\u6728\u9F50':[87.9236,43.5883],'\u4F5B\u5C71':[112.8955,23.1097],'\u4FDD\u5B9A':[115.0488,39.0948],'\u5170\u5DDE':[103.5901,36.3043],'\u5305\u5934':[110.3467,41.4899],'\u5317\u4EAC':[116.4551,40.2539],'\u5317\u6D77':[109.314,21.6211],'\u5357\u4EAC':[118.8062,31.9208],'\u5357\u5B81':[108.479,23.1152],'\u5357\u660C':[116.0046,28.6633],'\u5357\u901A':[121.1023,32.1625],'\u53A6\u95E8':[118.1689,24.6478],'\u53F0\u5DDE':[121.1353,28.6688],'\u5408\u80A5':[117.29,32.0581],'\u547C\u548C\u6D69\u7279':[111.4124,40.4901],'\u54B8\u9633':[108.4131,34.8706],'\u54C8\u5C14\u6EE8':[127.9688,45.368],'\u5510\u5C71':[118.4766,39.6826],'\u5609\u5174':[120.9155,30.6354],'\u5927\u540C':[113.7854,39.8035],'\u5927\u8FDE':[122.2229,39.4409],'\u5929\u6D25':[117.4219,39.4189],'\u592A\u539F':[112.3352,37.9413],'\u5A01\u6D77':[121.9482,37.1393],'\u5B81\u6CE2':[121.5967,29.6466],'\u5B9D\u9E21':[107.1826,34.3433],'\u5BBF\u8FC1':[118.5535,33.7775],'\u5E38\u5DDE':[119.4543,31.5582],'\u5E7F\u5DDE':[113.5107,23.2196],'\u5ECA\u574A':[116.521,39.0509],'\u5EF6\u5B89':[109.1052,36.4252],'\u5F20\u5BB6\u53E3':[115.1477,40.8527],'\u5F90\u5DDE':[117.5208,34.3268],'\u5FB7\u5DDE':[116.6858,37.2107],'\u60E0\u5DDE':[114.6204,23.1647],'\u6210\u90FD':[103.9526,30.7617],'\u626C\u5DDE':[119.4653,32.8162],'\u627F\u5FB7':[117.5757,41.4075],'\u62C9\u8428':[91.1865,30.1465],'\u65E0\u9521':[120.3442,31.5527],'\u65E5\u7167':[119.2786,35.5023],'\u6606\u660E':[102.9199,25.4663],'\u676D\u5DDE':[119.5313,29.8773],'\u67A3\u5E84':[117.323,34.8926],'\u67F3\u5DDE':[109.3799,24.9774],'\u682A\u6D32':[113.5327,27.0319],'\u6B66\u6C49':[114.3896,30.6628],'\u6C55\u5934':[117.1692,23.3405],'\u6C5F\u95E8':[112.6318,22.1484],'\u6C88\u9633':[123.1238,42.1216],'\u6CA7\u5DDE':[116.8286,38.2104],'\u6CB3\u6E90':[114.917,23.9722],'\u6CC9\u5DDE':[118.3228,25.1147],'\u6CF0\u5B89':[117.0264,36.0516],'\u6CF0\u5DDE':[120.0586,32.5525],'\u6D4E\u5357':[117.1582,36.8701],'\u6D4E\u5B81':[116.8286,35.3375],'\u6D77\u53E3':[110.3893,19.8516],'\u6DC4\u535A':[118.0371,36.6064],'\u6DEE\u5B89':[118.927,33.4039],'\u6DF1\u5733':[114.5435,22.5439],'\u6E05\u8FDC':[112.9175,24.3292],'\u6E29\u5DDE':[120.498,27.8119],'\u6E2D\u5357':[109.7864,35.0299],'\u6E56\u5DDE':[119.8608,30.7782],'\u6E58\u6F6D':[112.5439,27.7075],'\u6EE8\u5DDE':[117.8174,37.4963],'\u6F4D\u574A':[119.0918,36.524],'\u70DF\u53F0':[120.7397,37.5128],'\u7389\u6EAA':[101.9312,23.8898],'\u73E0\u6D77':[113.7305,22.1155],'\u76D0\u57CE':[120.2234,33.5577],'\u76D8\u9526':[121.9482,41.0449],'\u77F3\u5BB6\u5E84':[114.4995,38.1006],'\u798F\u5DDE':[119.4543,25.9222],'\u79E6\u7687\u5C9B':[119.2126,40.0232],'\u7ECD\u5174':[120.564,29.7565],'\u804A\u57CE':[115.9167,36.4032],'\u8087\u5E86':[112.1265,23.5822],'\u821F\u5C71':[122.2559,30.2234],'\u82CF\u5DDE':[120.6519,31.3989],'\u83B1\u829C':[117.6526,36.2714],'\u83CF\u6CFD':[115.6201,35.2057],'\u8425\u53E3':[122.4316,40.4297],'\u846B\u82A6\u5C9B':[120.1575,40.578],'\u8861\u6C34':[115.8838,37.7161],'\u8862\u5DDE':[118.6853,28.8666],'\u897F\u5B81':[101.4038,36.8207],'\u897F\u5B89':[109.1162,34.2004],'\u8D35\u9633':[106.6992,26.7682],'\u8FDE\u4E91\u6E2F':[119.1248,34.552],'\u90A2\u53F0':[114.8071,37.2821],'\u90AF\u90F8':[114.4775,36.535],'\u90D1\u5DDE':[113.4668,34.6234],'\u9102\u5C14\u591A\u65AF':[108.9734,39.2487],'\u91CD\u5E86':[107.7539,30.1904],'\u91D1\u534E':[120.0037,29.1028],'\u94DC\u5DDD':[109.0393,35.1947],'\u94F6\u5DDD':[106.3586,38.1775],'\u9547\u6C5F':[119.4763,31.9702],'\u957F\u6625':[125.8154,44.2584],'\u957F\u6C99':[113.0823,28.2568],'\u957F\u6CBB':[112.8625,36.4746],'\u9633\u6CC9':[113.4778,38.0951],'\u9752\u5C9B':[120.4651,36.3373],'\u97F6\u5173':[113.7964,24.7028]}";
             JSONObject jso = new JSONObject(str);
             series.put("markLine", markline);
             markpoint.put("data", pointdata);
             series.put("markPoint", markpoint)
             		 .put("geoCoord",geodata.join(jso));;
             		 
             js.put("series", JSONArray.create().put(series));
             js.put("toolbox", createToolbox());
         }
	}
    
	private JSONObject createMarklineEffect() throws JSONException {
		// TODO Auto-generated method stub
		return JSONObject.create().put("show", "true")
		.put("scaleSize", "1")
		.put("period", "30")
		.put("color", "#fff")
		.put("shadowBlur", "10");
	}

	private JSONObject createMarklineStyle() throws JSONException {
		JSONObject itemStyle = JSONObject.create();
		JSONObject normal =new JSONObject()/*.put("color", "#fff")*/
        .put("borderWidth", "1")
        .put("lineStyle", JSONObject.create().put("type", "solid").put("shadowBlur", "10"));//"rgba(30,144,255,0.5)");
        itemStyle.put("normal", normal);
        return itemStyle;
	}

	private void chineseOrworldMap(JSONObject js, Repository repo)
			throws JSONException {
		int cateCount = getCategoryCount();
         int seriesCount = getSeriesSize();
         JSONArray series = new JSONArray();
         JSONArray cate =  new JSONArray();
          JSONArray legenddata= new JSONArray();
          JSONObject geodata = new JSONObject();
         if (cateCount > 0){
             for(int cateIndex = 0; cateIndex < cateCount; cateIndex++){
                 JSONObject oneseries = new JSONObject();
                 JSONArray markdata = new JSONArray();
                 String cateName = StringUtils.EMPTY;
                 
                 for(int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++){
                     DataSeries dataSeries = getSeries(seriesIndex);
                     DataPoint dataPoint = dataSeries.getDataPoint(cateIndex);
                     cateName = dataPoint.getCategoryName();
                      String label = dataPoint.getSeriesName();
                     double value = dataPoint.getValue();
                     System.out.println("\u7CFB\u5217\u540D"+label+"\u503C"+value+ " \u5206\u7C7B\u540D" + cateName);
                     System.out.println(dataPoint.toJSONObject(repo).toString());
                     if(value>0){
                    	 if(name_ref!=null)
                    	 {
                    		 if(name_ref.getListName()!=null)
                    		 {
                    			 System.out.println("得到的值：："+name_ref.getListName().toString());
                            	 if(name_ref.getListName().has(label)){
                            		 label = name_ref.getListName().getString(label);
                            	 }
                    		 }
                    	 }
                    	 if(!geodata.has(label)&&getType().getType()=="china"){
								geodata.put(label, GetLatAndLngByBaidu.getCoordinate(label));
                    	 }
//                    	 if(label.contains("省"))
                    	 markdata.put(JSONObject.create().put("name", label).put("value", value));
                     }
                 }
                 legenddata.put(cateName);
                 oneseries.put("name", cateName)
                 .put("type", "map")
                 .put("mapType", getType().getType())
                 .put("hoverable", false)
                 .put("roam", true);
                 oneseries.put("data",markdata).put("geoCoord", geodata);;
                 JSONObject markPoint = JSONObject.create().put("symbolSize", 5).put("itemStyle",new  JSONObject("{normal:{borderColor:'#87cefa',borderWidth:1,label:{show:false}},emphasis:{borderColor:'#1e90ff',borderWidth:5,label:{show:false}}}"));
                 markPoint.put("data",markdata);
                 oneseries.put("markPoint", markPoint);
                 series.put(oneseries);
                 cate.put(cateName);
             }
             JSONObject legend = new JSONObject();
             legend.put("orient", "vertical").put("data",legenddata).put("x", "left");
             js.put("series", series);
             js.put("legend", legend);
             js.put("toolbox", createToolbox());
         }
	}
//	private JSONObject createGuideItemStyle(Repository repo) throws JSONException {
//        JSONObject itemStyle = JSONObject.create();
//        JSONObject normal = new JSONObject().put("borderColor", "rgba(100,149,237,1)")
//        .put("borderWidth", "0.5").put("areaStyle", new JSONObject().put("color", "#1b1b1b"));
////        itemStyle.put();
//        itemStyle.put("normal", normal);
////        itemStyle.put("emphasis", JSONObject.create().put("label", JSONObject.create().put("show", true)));
//        return itemStyle;
//    }
	private JSONObject createToolbox() throws JSONException{
		return new JSONObject("{show:true,orient:'vertical',x:'right',y:'center',feature:{mark:{show:true},dataView:{show:true,readOnly:false},restore:{show:true},saveAsImage:{show:true}}}");
	}
    private void dealWithDataSeries(int categoryIndex, int resolution) {
        int seriesSize = this.getSeriesSize();
        for (int seriesIndex = 0; seriesIndex < seriesSize; seriesIndex++) {
            DataSeries dataSeries = this.getSeries(seriesIndex);
            DataPoint dataPoint = dataSeries.getDataPoint(categoryIndex);
            ;
            System.out.println("名字"+dataPoint.getCategoryName()/*+dataPoint.toJSONObject(arg0)*/);
            if (dataPoint.isValueIsNull()) {
                continue;
            }
        }
    }

    @Override
    public String getPlotGlyphType() {
        return "ChineseMapPlotGlyph";
    }

    @Override
    public String getChartType() {
        return "ChineseMap";
    }

	public ChartMapType getType() {
		return type;
	}

	public void setType(ChartMapType type) {
		this.type = type;
	}

	public MapChartNameRef getName_ref() {
		return name_ref;
	}

	public void setName_ref(MapChartNameRef name_ref) {
		this.name_ref = name_ref;
	}
}
