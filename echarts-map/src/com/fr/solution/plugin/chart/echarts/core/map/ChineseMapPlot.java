package com.fr.solution.plugin.chart.echarts.core.map;

import com.fr.base.TableData;
import com.fr.base.chart.chartdata.ChartData;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartdata.NormalChartData;
import com.fr.chart.chartglyph.PlotGlyph;
import com.fr.file.DatasourceManager;
import com.fr.file.DatasourceManagerProvider;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.general.data.DataModel;
import com.fr.general.data.TableDataException;
import com.fr.script.Calculator;
import com.fr.solution.plugin.chart.echarts.base.ChartMapType;
import com.fr.solution.plugin.chart.echarts.base.MapChartNameRef;
import com.fr.solution.plugin.chart.echarts.base.NewLegendGlyph;
import com.fr.solution.plugin.chart.echarts.base.NewPlot;
import com.fr.solution.plugin.chart.echarts.base.NewSeriesGlyph;
import com.fr.solution.plugin.chart.echarts.base.NewTitleGlyph;
import com.fr.stable.StringUtils;
import com.fr.stable.fun.FunctionProcessor;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

/**
 * Created by richie on 16/1/29.
 */
public class ChineseMapPlot extends NewPlot {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2081961841252733958L;
	private ChartMapType type=ChartMapType.CHINESE_MAP;
	private MapChartNameRef name_ref=new MapChartNameRef();
    @Override
    public PlotGlyph createPlotGlyph(ChartData chartData) {
    	
    	ChineseMapPlotGlyph glyph = new ChineseMapPlotGlyph();
        glyph.setType(getType());
        glyph.setName_ref(name_ref);
        install4PlotGlyph(glyph, chartData);
        return glyph;
    }
    
    @Override
    public String getPlotID() {
        return "EChartsChinaMapPlot";
    }
   
    @Override
    public boolean matchPlotType(Plot newPlot) {
        return newPlot instanceof ChineseMapPlot;
    }

    @Override
    public ChartData defaultChartData() {
        return new NormalChartData();
    }

    @Override
    public FunctionProcessor getFunctionToRecord() {
        return ChineseMapFunctionProcessor.MAP;
    }

    public boolean accept(Class<? extends Plot> obClass) {
        return ComparatorUtils.equals(ChineseMapPlot.class, obClass);
    }

    @Override
    public String getPlotName() {
        return Inter.getLocText("Plugin-ECharts_Chinese_Map");
    }

	@Override
	public NewTitleGlyph createChartTitleGlyph(ChartData chartData) {
		// TODO Auto-generated method stub
		
		
		return null;
	}

	@Override
	public NewLegendGlyph createChartLegendGlyph(ChartData chartData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NewSeriesGlyph createChartSeriesGlyph(ChartData chartData) {
		// TODO Auto-generated method stub
		return null;
	}

	public ChartMapType getType() {
		return type;
	}

	public void setType(ChartMapType type) {
		this.type = type;
	}
	@Override
	 protected void readPlotXML(XMLableReader reader){
	        super.readPlotXML(reader);
	        if (reader.isChildNode()) {
	            String tagName = reader.getTagName();
	            if(tagName.equals("MapChart")) {
	                setType(ChartMapType.parse(reader.getAttrAsString("Type", StringUtils.EMPTY)));
	            }
	        }
	        
	 }
	 public void writeXML(XMLPrintWriter writer) {
	        super.writeXML(writer);

	        writer.startTAG("MapChart")
	                .attr("Type", getType().getType());

	        writer.end();
	 }

	public MapChartNameRef getName_ref() {
		return name_ref;
	}

	public void setName_ref(MapChartNameRef name_ref) {
		this.name_ref = name_ref;
	}


}
