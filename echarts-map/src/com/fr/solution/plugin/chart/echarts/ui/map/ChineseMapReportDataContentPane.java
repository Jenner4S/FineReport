package com.fr.solution.plugin.chart.echarts.ui.map;
import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.report.core.A.n;
import com.fr.solution.plugin.chart.echarts.base.MapChartNameRef;
import com.fr.solution.plugin.chart.echarts.base.NewChart;
import com.fr.solution.plugin.chart.echarts.core.map.ChineseMapPlot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Created by richie on 16/1/29.
 */
public class ChineseMapReportDataContentPane extends AbstractReportDataContentPane implements UIObserver {

    /**
	 * 
	 */
	@Override
		public void checkBoxUse() {
			// TODO Auto-generated method stub
//			checkBoxUse();
		}
//	private UICorrelationPane correlationPane;
//	protected static final int PRE_WIDTH = 210;
	public ChineseMapReportDataContentPane(ChartDataPane parent) {
		initEveryPane();
    	/*setLayout(new BorderLayout());
//        UILabel label = new UILabel("测试布局");
//        setBorder(BorderFactory.createLineBorder(Color.RED));
//        add(label, BorderLayout.CENTER);
        correlationPane = new UICorrelationPane("区域名", "地图字段");
        JPanel categoryPane = new JPanel(new BorderLayout(4,0));
        categoryPane.setBorder(BorderFactory.createMatteBorder(0, 0, 6, 1, getBackground()));*/
//        categoryPane.add(seriesPane);

//        seriesPane.doLayout();
//        this.add(seriesPane);
//		initEveryPane();
		
//		UILabel label1 = new UILabel(Inter.getLocText("FR-Chart-Category_Name") + ":", SwingConstants.RIGHT);
//    	label1.setPreferredSize(new Dimension(75, 20));
////    	this.add(label1, BorderLayout.WEST);
//		 this.add(label1, "0,0,2,0");
//	        this.add(new BoldFontTextLabel(Inter.getLocText("FR-Chart-Data_Filter")), "0,4,2,4");
//	        this.add(filterPane = new ChartDataFilterPane(new Bar2DPlot(), parent), "0,6,2,4");
    }


	 @Override
	    protected String[] columnNames() {
	        return new String[]{
	                "区域名",
	                "地图字段"
	        };
	    }

    @Override
    public void populateBean(ChartCollection ob) {
    	MapChartNameRef name_ref =  ((NewChart) ob.getSelectedChart()).getName_ref();
    	List<Object[]> listobj = new ArrayList<Object[]>();
    	System.out.println("GGGGGGGG:::"+name_ref.getListName());
    	if(null!=name_ref){
    		JSONObject name_lis = name_ref.getListName();
    		 Iterator iterator = name_lis.keys();
    		for(;iterator.hasNext();){
    			String key = (String) iterator.next();
//    			if(key!=null)
    			Object[] objs=null;
    			try {
    				objs=new String[]{key,name_lis.getString(key)};
    				} 
    			catch (JSONException e){// TODO Auto-generated catch block
				e.printStackTrace();
				}
    			listobj.add(objs);
    		}
    		seriesPane.populateBean(listobj);
    		seriesPane.doLayout();
    	}
    }
    
    @Override
    public void registerChangeListener(UIObserverListener listener) {
    	
    }

    @Override
    public boolean shouldResponseChangeListener() {
        return false;
    }
	@Override
	public void updateBean(ChartCollection collection) {
		// TODO Auto-generated method stub
//		populateBean( collection) ;
		MapChartNameRef name_ref =  ((NewChart) collection.getSelectedChart()).getName_ref();
		List<Object[]> listobj = seriesPane.updateBean();
		Iterator<Object[]> iterator = listobj.iterator();
		JSONObject json = new JSONObject();
		for(;iterator.hasNext();){
			Object[] objs  = iterator.next();
			if(objs[0]!=null&&objs[1]!=null)
			try {
				
				json.put((String )objs[0], (String )objs[1]);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		name_ref.setListName(json);
		System.out.println("updatabean：：：："+json);
		return;
	}
}
