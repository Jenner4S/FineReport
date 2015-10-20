package com.fr.design.chart;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.controlpane.JControlPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.stable.Nameable;

import java.awt.*;
import java.util.HashMap;

/**
 * ����ͼ������Pane 
 * @author kunsnat: ChartComponent�Ƴ�.
 */
public class ChartControlPane extends JControlPane {
	private static final long serialVersionUID = 7336270815128413184L;

	public ChartControlPane() {
		super();
		// �����趨��С. ��ΪJControlPaneĬ�ϵ�(450,450) ���ʺ�ͼ����� @ChartSize
//		this.setPreferredSize(new Dimension(770, 520));
	}
	
	@Override
	public NameableCreator[] createNameableCreators() {
		return new NameableCreator[] {
				new NameObjectCreator(Inter.getLocText("Chart"), Chart.class, ChartTypeUpdatePane.class)
		};
	}
	
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("Chart-Manage_Chart_Type");
	}
	
	public void populate(ChartCollection cc) {
		if(cc == null)return;
		
		NameObject[] nameObjects = new NameObject[cc.getChartCount()];
		for (int i = 0; i < nameObjects.length; i++) {
			nameObjects[i] = new NameObject(cc.getChartName(i), cc.getChart(i));
		}
				
		populate(nameObjects);
		// kunsnat: ѡ�е�ǰͼ��ѡ�е�name
		String chartSelectedName = cc.getChartName(cc.getSelectedIndex() < cc.getChartCount() ? cc.getSelectedIndex() : 0);
		setSelectedName(chartSelectedName);
	}
	
	public void update(ChartCollection cc) {
		HashMap namesChart = new HashMap();// �ݴ��ж��Ƿ��б�Ҫ����
		for(int i = 0; i < cc.getChartCount(); i++) {
			try {
				namesChart.put(cc.getChartName(i), cc.getChart(i).clone());
			} catch (CloneNotSupportedException e) {

			}
		}
		
		Nameable[] nameables = update();
		if (nameables.length == 0 || cc == null) {
			return;
		}
		
		cc.removeAllNameObject(); 
		String select = getSelectedName();
		for (int i = 0; i < nameables.length; i++) {
			if (nameables[i] instanceof NameObject && ((NameObject)nameables[i]).getObject() instanceof Chart) {
				NameObject no = (NameObject)nameables[i];
				
				String name = no.getName();
				Chart chart = (Chart)no.getObject();
				if(namesChart.containsKey(name)) {
					Chart tmpChart = (Chart)namesChart.get(name);
					if(chart.getPlot() != null && tmpChart.getPlot() != null
							&& chart.getPlot().match4GUI(tmpChart.getPlot())) {
						chart = tmpChart;// ����֮ǰ�����༭��Chart
					}
				}
				cc.addNamedChart(name, chart);
				if(no.getName().equals(select)) {
					cc.setSelectedIndex(i);
				}
			}
		}	
	}
	
	/*
	 * alex:�̳�UpdatePane��ChartTypePane
	 */
	public static class ChartTypeUpdatePane extends BasicBeanPane<Chart> {
		private static final long serialVersionUID = -7058348930816218415L;
		private Chart editing;
		
		private ChartTypePane typePane;
		
		public ChartTypeUpdatePane() {
			this.setLayout(FRGUIPaneFactory.createBorderLayout());
			
			typePane = new ChartTypePane();
			this.add(typePane, BorderLayout.CENTER);
		}
		
		@Override
		protected String title4PopupWindow() {
			return "Chart Type";
		}
		
		@Override
		public void populateBean(Chart ob) {
			editing = ob;
			typePane.populate(ob);
		}
		
		@Override
		public Chart updateBean() {
			typePane.update(editing);
			
			return editing;
		}
	}
}
