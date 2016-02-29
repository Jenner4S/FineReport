package com.fr.solution.plugin.chart.echarts.base;

import com.fr.base.FRContext;
import com.fr.base.chart.BaseChartGlyph;
import com.fr.base.chart.chartdata.ChartData;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartdata.OneValueCDDefinition;
import com.fr.chart.chartglyph.PlotGlyph;
import com.fr.general.ComparatorUtils;
import com.fr.solution.plugin.chart.echarts.core.map.ChineseMapPlot;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

/**
 * Created by richie on 16/1/29.
 */
public class NewChart extends Chart {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8143079160652221111L;
	private MapChartNameRef name_ref = new MapChartNameRef();
	private ChartMapType maptype = ChartMapType.CHINESE_MAP;
    private MapChartDataRange dataRange = new MapChartDataRange();
    public NewChart() {
        setWrapperName("EChartsFactory");
        
    }
//    @Override
//    public void setTitle(Title arg0) {
//    	// TODO Auto-generated method stub
//    	super.setTitle(arg0);
//    }
    public NewChart(NewPlot plot) {
        super(plot);
        setTitle(new MapChartTitle());
//        setDataRange(new MapChartDataRange());
        setWrapperName("EChartsFactory");
    }

    @Override
    public BaseChartGlyph createGlyph(ChartData chartData) {
        NewGlyph glyph = new NewGlyph();
        glyph.setGeneralInfo(this);
        ChineseMapPlot newPlot = (ChineseMapPlot) getPlot();
        PlotGlyph plotGlyph = null;
        try {
            if(newPlot != null){
//            	newPlot.setType(getMaptype());
            	newPlot.setName_ref(getName_ref());
                plotGlyph =newPlot.createPlotGlyph(chartData);
                glyph.setPlotGlyph(plotGlyph);
            }
        } catch (Exception e) {
            // 抛错信息
        	glyph.setPlotGlyph(null);
            String message = "Error happens at Chart Plot." + "\nerror message is " + e.getMessage();
            FRContext.getLogger().error(message, e);
        }
        if(getDataRange()!=null){
        	glyph.setDataRange(getDataRange());
        }
//        MapChartTitle q = (MapChartTitle)getTitle();
        glyph.setTitleGlyph((NewTitleGlyph)getTitle().createGlyph());
        glyph.setWrapperName(getWrapperName());
        glyph.setChartImagePath(getImagePath());
        glyph.setRequiredJS(getRequiredJS());
        glyph.setJSDraw(isJSDraw());

        System.out.println("NEWCHART::"+ ((ChineseMapPlot)getPlot()).getType().getType());
        
        
        return glyph;
    }

    /**
     * 判断图表类型是否是obClass
     * @param obClass 传入对象
     * @return 是否是obClass对象
     */


    @Override
    protected void readChartXML(XMLableReader reader) {
    	super.readChartXML(reader);
        if (reader.isChildNode()) {
            String tmpNodeName = reader.getTagName();

            if (tmpNodeName.equals(MapChartTitle.XML_TAG)) {
                setTitle(new MapChartTitle());
                reader.readXMLObject(getTitle());
            } else if(tmpNodeName.equals(MapChartDataRange.XML_TAG)) {
                setDataRange((MapChartDataRange)reader.readXMLObject(new MapChartDataRange()));
            }if(tmpNodeName.equals(MapChartNameRef.XML_TAG)){
            	setName_ref((MapChartNameRef)reader.readXMLObject(new MapChartNameRef()));
            	
            }
        }
    }

    /*public static TopDefinition readDefinition(XMLableReader reader) {
        TopDefinition filterDefinition;
        String tmpNodeName = reader.getTagName();
        if (EChartsOneValueCDDefinition.XML_TAG.equals(tmpNodeName)) {
            filterDefinition = new EChartsOneValueCDDefinition();
        } else if (EChartsMoreNameCDDefinition.XML_TAG.equals(tmpNodeName)) {
            filterDefinition = new EChartsMoreNameCDDefinition();
        } else if (EChartsNormalReportDataDefinition.XML_TAG.equals(tmpNodeName)) {
            filterDefinition = new EChartsNormalReportDataDefinition();
        }else {
            return ChartXMLCompatibleUtils.readDefinition(reader);
        }

        reader.readXMLObject(filterDefinition);
        return filterDefinition;
    }*/



    @Override
    public void writeXML(XMLPrintWriter writer) {
        super.writeXML(writer);
        if(this.dataRange != null){
            this.dataRange.writeXML(writer);
        }if(this.getName_ref()!=null){
        	this.name_ref.writeXML(writer);
        }
        
//       writer.startTAG("Defin");
//       getFilterDefinition().writeXML(writer);
//      writer.end();
        
    }
    public boolean accept(Class<? extends Chart> obClass){
        return ComparatorUtils.equals(NewChart.class, obClass);
    }
	public MapChartDataRange getDataRange() {
		return dataRange;
	}
	public void setDataRange(MapChartDataRange dataRange) {
		this.dataRange = dataRange;
	}
	public ChartMapType getMaptype() {
		return maptype;
	}
	public void setMaptype(ChartMapType maptype) {
		this.maptype = maptype;
	}
	public MapChartNameRef getName_ref() {
		return name_ref;
	}
	public void setName_ref(MapChartNameRef name_ref) {
		this.name_ref = name_ref;
	}
	
	
	
	
}
