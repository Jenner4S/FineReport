package com.fr.solution.plugin.chart.echarts.ui.map;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.solution.plugin.chart.echarts.base.MapChartNameRef;
import com.fr.solution.plugin.chart.echarts.base.NewChart;

public class MapNameRefPane extends BasicScrollPane<NewChart> {

    
    /**
	 * 
	 */
	private static final long serialVersionUID = -376590043407445959L;
	private UICorrelationPane correlationPane;
    public MapNameRefPane(MapEChartsStylePane mapEChartsStylePane) {
    	super();
		// TODO Auto-generated constructor stub
	}

	public MapNameRefPane() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unused")
	@Override
    protected JPanel createContentPane() {
    	 double p = TableLayout.PREFERRED;
    	 correlationPane = new UICorrelationPane("地图字段", "区域名");
         double f = TableLayout.FILL;
         double[] columnSize2 = {p, f};
         double[] columnSize1 = {p};
         double[] rowSize = {p,p,p};
         Component[][] components = new Component[][]{
        		 new Component[]{new JSeparator(),null},
                 new Component[]{null,correlationPane},
                 new Component[]{new JSeparator(),null}
         };

         return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize2);
    }
	
	/*public static void main(String... args) {
        JFrame jf = new JFrame("test");
        final String[] columnNames = {Inter.getLocText("Actual_Value"), Inter.getLocText("Display_Value")};
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel content = (JPanel) jf.getContentPane();
        content.setLayout(new BorderLayout());
        UICorrelationPane pane = new UICorrelationPane(columnNames);
        content.add(new MapNameRefPane().createContentPane(), BorderLayout.CENTER);
	}*/
    /*private Component createRangeContentPane(double[] ds, double[] columnSize) {
		// TODO Auto-generated method stub	tinyFormulaPane = new TinyFormulaPane();
    	UILabel label1 = new UILabel("\u6700\u5927\u503C");
    	UILabel label2 = new UILabel("\u6700\u5C0F\u503C");
        max = new UITextField();
        min = new UITextField();
        Component[][] components = new Component[][]{
                new Component[]{label1,max},
                new Component[]{label2,min},
        };

        
	}*/
	@Override
    public void populateBean(NewChart ob) {
		MapChartNameRef name_ref = (MapChartNameRef)ob.getName_ref();
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
    		correlationPane.populateBean(listobj);
    	}
    }

    @Override
    protected String title4PopupWindow() {
        return "区域名映射表";
    }
    @Override
    public void updateBean(NewChart chart) {
		MapChartNameRef name_ref = (MapChartNameRef)chart.getName_ref();
		List<Object[]> listobj = correlationPane.updateBean();
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
	@Override
	public NewChart updateBean() {
		// TODO Auto-generated method stub
		return super.updateBean();
		
	}
}
