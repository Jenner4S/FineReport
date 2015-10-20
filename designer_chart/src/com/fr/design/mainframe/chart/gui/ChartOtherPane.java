package com.fr.design.mainframe.chart.gui;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.other.ChartConditionAttrPane;
import com.fr.design.mainframe.chart.gui.other.ChartInteractivePane;
import com.fr.design.mainframe.chart.gui.style.legend.AutoSelectedPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.MultiTabPane;
import com.fr.general.ComparatorUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChartOtherPane extends AbstractChartAttrPane {
	protected TabPane otherPane;

	protected boolean hasCondition = false;

	@Override
	protected JPanel createContentPane() {
		JPanel content = new JPanel(new BorderLayout());
		otherPane = new TabPane();
		content.add(otherPane, BorderLayout.CENTER);
		return content;
	}

	@Override
	public void populate(ChartCollection collection) {
		Plot plot = collection.getSelectedChart().getPlot();

        hasCondition = plot.isSupportDataSeriesCondition();

        this.removeAll();
        initAll();
        this.validate();// kunsnat: ���¼������. ��Ȼ�¼� �������Ȳ���Ӧ.

		otherPane.populateBean(collection);
	}
	
	/**
	 * ע�� �л���ť�� �ı��¼�, �ͳ�������.
     * @param currentChartEditPane  ��ǰͼ��༭����.
	 */
	public void registerChartEditPane(ChartEditPane currentChartEditPane) {
		otherPane.registerChartEditPane(currentChartEditPane);
	}

	@Override
	public void update(ChartCollection collection) {
		otherPane.updateBean(collection);
	}

	@Override
	public String getIconPath() {
		return "com/fr/design/images/chart/InterAttr.png";
	}

	/**
	 * �������
	 * @return ���ر���.
	 */
	public String title4PopupWindow() {
		return PaneTitleConstants.CHART_OTHER_TITLE;
	}
	
	private boolean isHaveCondition() {
		return hasCondition;
	}
	
	/**
	 * ����ѡ�еĽ���id
	 */
	public void setSelectedByIds(int level, String... id) {
		otherPane.setSelectedByIds(level, id);
    }

	protected class TabPane extends MultiTabPane<ChartCollection> {
		protected ChartInteractivePane interactivePane;
		protected ChartConditionAttrPane conditionAttrPane;

		@Override
		protected List<BasicPane> initPaneList() {
			List<BasicPane> paneList = new ArrayList<BasicPane>();
			interactivePane = new ChartInteractivePane(ChartOtherPane.this);

            paneList.add(interactivePane);

			if (ChartOtherPane.this.isHaveCondition()) {
				conditionAttrPane = new ChartConditionAttrPane();
				paneList.add(conditionAttrPane);
			}
			return paneList;
		}

		@Override
		protected void initLayout() {
			JPanel tabPanel = new JPanel(new BorderLayout());
			tabPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 10, getBackground()));
			tabPanel.add(tabPane, BorderLayout.CENTER);
			this.setLayout(new BorderLayout(0, 4));
			this.add(tabPanel, BorderLayout.NORTH);
			this.add(centerPane, BorderLayout.CENTER);
		}

		@Override
		public void populateBean(ChartCollection ob) {
            interactivePane.populateBean(ob.getSelectedChart());
			if (ChartOtherPane.this.isHaveCondition()) {
				conditionAttrPane.populateBean(ob.getSelectedChart());
			}
		}

		@Override
		public void updateBean(ChartCollection ob) {
			if(ob.getSelectedChart() == null) {
				return;
			}
			Chart chart = ob.getSelectedChart();
            interactivePane.updateBean(chart);
			if (ChartOtherPane.this.isHaveCondition()) {
				conditionAttrPane.updateBean(chart);
			}
		}
		
		/**
		 * ����ѡ�еĽ���id
		 */
		public void setSelectedByIds(int level, String... id) {
	        for (int i = 0; i < paneList.size(); i++) {
	            if (ComparatorUtils.equals(id[level], NameArray[i])) {
	            	tabPane.setSelectedIndex(i);
	            	tabPane.tabChanged(i);
	            	if (id.length >= level + 2) {
	            		((AutoSelectedPane)paneList.get(i)).setSelectedIndex(id[level + 1]);
	            	}
	                break;
	            }
	        }
	    }
		
		/**
		 * ע�� �л���ť���л��¼�.
		 * @param currentChartEditPane ��ǰ�༭��ͼ��༭����.
		 */
		public void registerChartEditPane(ChartEditPane currentChartEditPane) {
//			chartSwitchPane.registerChartEditPane(currentChartEditPane);
		}

		@Override
		public boolean accept(Object ob) {
			return false;
		}

		@Override
		public String title4PopupWindow() {
			return StringUtils.EMPTY;
		}

		@Override
		public void reset() {

		}

		@Override
		public ChartCollection updateBean() {
			return null;
		}
	}
}