package com.fr.design.mainframe.chart.gui.other;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.CustomPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.chartglyph.CustomAttr;
import com.fr.design.chart.series.SeriesCondition.impl.DataSeriesConditionPaneFactory;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.UICorrelationComboBoxPane;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.general.Inter;

public class ChartConditionAttrPane extends BasicScrollPane<Chart> {
	private static final long serialVersionUID = 5725969986029470291L;
	private UICorrelationComboBoxPane conditionPane;
	
	public ChartConditionAttrPane() {
		super();
	}
	
	@Override
	protected JPanel createContentPane() {
		if (conditionPane == null) {
			conditionPane = new UICorrelationComboBoxPane();
		}
		
		return conditionPane;
	}

	@Override
	public String title4PopupWindow() {
		return Inter.getLocText("Condition_Display");
	}
	
	@Override
	public void populateBean(Chart chart) {
		Plot plot = chart.getPlot();
//		if(plot instanceof CustomPlot) {// 保留原因: 已经按照UI做好, 只是在下个迭代任务再继续进行.
//			List<UIMenuNameableCreator> list = new ArrayList<UIMenuNameableCreator>();
//			
//			Class<? extends ConditionAttributesPane> bar2D = DataSeriesConditionPaneFactory.findConfitionPane4DataSeries(new Bar2DPlot());
//			Class<? extends ConditionAttributesPane> barStack = DataSeriesConditionPaneFactory.findConfitionPane4DataSeries(new Bar2DPlot(true));
//			Class<? extends ConditionAttributesPane> line = DataSeriesConditionPaneFactory.findConfitionPane4DataSeries(new LinePlot());
//			Class<? extends ConditionAttributesPane> area = DataSeriesConditionPaneFactory.findConfitionPane4DataSeries(new AreaPlot(true));
//
//			list.add(new UIMenuNameableCreator(Inter.getLocText("ChartF-Column"), new CustomAttr(), bar2D));
//			list.add(new UIMenuNameableCreator(Inter.getLocText(new String[]{"ChartF-Stacked", "ChartF-Column"}), new CustomAttr(), barStack));
//			list.add(new UIMenuNameableCreator(Inter.getLocText("I-LineStyle_Line"), new CustomAttr(), line));
//			list.add(new UIMenuNameableCreator(Inter.getLocText(new String[]{"ChartF-Stacked", "I-AreaStyle_Normal"}), new CustomAttr(), area));
//			
//			conditionPane.refreshMenuAndAddMenuAction(list);
//			
//			CustomPlot custom = (CustomPlot)plot;
//			int size = custom.getConditionCollection().getConditionAttrSize();
//			List valueList = new ArrayList();
//			for(int i = 0; i < size; i++) {
//				CustomAttr customAttr = (CustomAttr)custom.getConditionCollection().getConditionAttr(i);
//				int type = customAttr.getRenderer();
//				if(type == ChartConstants.BAR_RENDERER) {
//					valueList.add(new UIMenuNameableCreator(customAttr.getName(), customAttr, bar2D));
//				} else if(type == ChartConstants.BAR_STACK) {
//					valueList.add(new UIMenuNameableCreator(customAttr.getName(), customAttr, barStack));
//				} else if(type == ChartConstants.LINE_RENDERER) {
//					valueList.add(new UIMenuNameableCreator(customAttr.getName(), customAttr, line));
//				} else if(type == ChartConstants.AREA_STACK) {
//					valueList.add(new UIMenuNameableCreator(customAttr.getName(), customAttr, area));
//				}
//			}
//			
//			conditionPane.populateBean(valueList);
//			conditionPane.doLayout();
//			
//		} else {
//		}
		Class<? extends ConditionAttributesPane> showPane = DataSeriesConditionPaneFactory.findConfitionPane4DataSeries(chart.getPlot());
		List<UIMenuNameableCreator> list = new ArrayList<UIMenuNameableCreator>();
		
		if(plot instanceof CustomPlot) {
			list.add(new UIMenuNameableCreator(Inter.getLocText("Condition_Attributes"), new CustomAttr(), showPane));
		} else {
			list.add(new UIMenuNameableCreator(Inter.getLocText("Condition_Attributes"), new ConditionAttr(), showPane));
		}
		
		conditionPane.refreshMenuAndAddMenuAction(list);
		
		ConditionCollection collection = chart.getPlot().getConditionCollection();
		List<UIMenuNameableCreator> valueList = new ArrayList<UIMenuNameableCreator>();
		
		for(int i = 0; i < collection.getConditionAttrSize(); i++) {
			valueList.add(new UIMenuNameableCreator(collection.getConditionAttr(i).getName(), collection.getConditionAttr(i), showPane));
		}
		
		conditionPane.populateBean(valueList);
		conditionPane.doLayout();
	}

	@Override
	public void updateBean(Chart chart) {
		List<UIMenuNameableCreator> list = conditionPane.updateBean();
		
		ConditionCollection cc = chart.getPlot().getConditionCollection();
		
		cc.clearConditionAttr();
		for(int i = 0; i < list.size(); i++) {
			UIMenuNameableCreator nameMenu = list.get(i);
			ConditionAttr ca = (ConditionAttr)nameMenu.getObj();
			ca.setName(nameMenu.getName());
			cc.addConditionAttr(ca);
		}
	}
}
