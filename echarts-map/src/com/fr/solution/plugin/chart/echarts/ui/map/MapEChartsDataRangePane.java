package com.fr.solution.plugin.chart.echarts.ui.map;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.solution.plugin.chart.echarts.base.MapChartDataRange;
import com.fr.solution.plugin.chart.echarts.base.NewChart;

public class MapEChartsDataRangePane extends BasicScrollPane<NewChart> {

    
    /**
	 * 
	 */
	private static final long serialVersionUID = -376590043407445959L;
	private UITextField max;
	private UITextField min;
	public MapEChartsDataRangePane(MapEChartsStylePane parent) {
		super();
    }
    @SuppressWarnings("unused")
	@Override
    protected JPanel createContentPane() {
    	 double p = TableLayout.PREFERRED;
         double f = TableLayout.FILL;
         double[] columnSize2 = {p, f};
         double[] columnSize1 = {p};
         double[] rowSize = {p,p,p,p};
         Component[][] components = new Component[][]{
        		 new Component[]{new JSeparator(),null},
                 new Component[]{null,createRangeContentPane(new double[]{p,p},columnSize2)},
                 new Component[]{new JSeparator(),null}
         };

         return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize2);
    }

    private Component createRangeContentPane(double[] ds, double[] columnSize) {
		// TODO Auto-generated method stub	tinyFormulaPane = new TinyFormulaPane();
    	UILabel label1 = new UILabel("\u6700\u5927\u503C");
    	UILabel label2 = new UILabel("\u6700\u5C0F\u503C");
        max = new UITextField();
        min = new UITextField();
        Component[][] components = new Component[][]{
                new Component[]{label1,max},
                new Component[]{label2,min},
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components,ds,columnSize);
        return panel;//TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(/*Inter.getLocText("Plugin-ChartF_Content")*/"±ÍÃ‚ƒ⁄»›", panel);
 
	}
	@Override
    public void populateBean(NewChart ob) {
		MapChartDataRange dataRange = ob.getDataRange();
		if(dataRange==null){
			return;
		}
		max.setText(dataRange.getMax());
		min.setText(dataRange.getMin());

    }

    @Override
    protected String title4PopupWindow() {
        return "\u6570\u636E\u8303\u56F4";
    }
    @Override
    public void updateBean(NewChart chart) {
    	System.out.println("max.getText()"+max.getText());
    	MapChartDataRange dataRange = chart.getDataRange();
		if(dataRange==null){
			return;
		}
		dataRange.setMax(max.getText());
		dataRange.setMin(min.getText());
		
    	
    }
	@Override
	public NewChart updateBean() {
		// TODO Auto-generated method stub
		return super.updateBean();
		
	}

}
