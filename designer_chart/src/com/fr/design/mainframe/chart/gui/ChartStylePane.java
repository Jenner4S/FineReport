
package com.fr.design.mainframe.chart.gui;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.style.analysisline.ChartAnalysisLinePane;
import com.fr.design.mainframe.chart.gui.style.area.ChartAreaPane;
import com.fr.design.mainframe.chart.gui.style.axis.ChartAxisNoFormulaPane;
import com.fr.design.mainframe.chart.gui.style.axis.ChartAxisPane;
import com.fr.design.mainframe.chart.gui.style.datalabel.ChartDataLabelPane;
import com.fr.design.mainframe.chart.gui.style.datalabel.ChartLabelFontPane;
import com.fr.design.mainframe.chart.gui.style.datasheet.ChartDatasheetPane;
import com.fr.design.mainframe.chart.gui.style.legend.AutoSelectedPane;
import com.fr.design.mainframe.chart.gui.style.legend.ChartLegendPane;
import com.fr.design.mainframe.chart.gui.style.series.ChartSeriesPane;
import com.fr.design.mainframe.chart.gui.style.title.ChartTitlePane;
import com.fr.design.mainframe.chart.gui.style.title.ChartTitlePaneNoFormula;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.MultiTabPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChartStylePane extends AbstractChartAttrPane implements UIObserver{
	private TabPane stylePane;
	private Chart chart;
	private AttributeChangeListener listener;
    private boolean isNeedFormula = true;//�ж�����������ǲ���Ҫ�й�ʽ
	private ChartTitlePane chartTitlePane;
    private ChartTitlePaneNoFormula chartTitlePaneNoFormula;
    private BasicPane chartLegendPane;
    private BasicPane chartSeriesPane;
    private BasicPane chartDataLabelPane;
    private BasicPane chartLabelFontPane;
    private BasicPane chartAxisPane;
    private BasicPane chartAreaPane;
    private ChartDatasheetPane chartDatasheetPane;
    private BasicPane chartAnalysisLinePane;
	private UIObserverListener uiObserverListener;


	public ChartStylePane() {
		super();
		initListPanes();
	}
	
	public ChartStylePane(AttributeChangeListener listener) {
		super();
		this.listener = listener;
		initListPanes();
	}

    public ChartStylePane(AttributeChangeListener listener, boolean isNeedFormula){
        super();
        this.listener = listener;
        this.isNeedFormula = isNeedFormula;
		initListPanes();
    }

	private void initListPanes(){
		chartTitlePane = new ChartTitlePane();
		chartTitlePaneNoFormula = new ChartTitlePaneNoFormula();
		chartDatasheetPane = new ChartDatasheetPane();
		chartLegendPane = new BasicPane() {
			@Override
			protected String title4PopupWindow() {
				return PaneTitleConstants.CHART_STYLE_LEGNED_TITLE;
			}
		};
		chartSeriesPane = new BasicPane() {
			@Override
			protected String title4PopupWindow() {
				return PaneTitleConstants.CHART_STYLE_SERIES_TITLE;
			}
		};
		chartDataLabelPane = new BasicPane() {
			@Override
			protected String title4PopupWindow() {
				return PaneTitleConstants.CHART_STYLE_LABEL_TITLE;
			}
		} ;
		chartLabelFontPane = new BasicPane() {
			@Override
			protected String title4PopupWindow() {
				return Inter.getLocText(Inter.getLocText("Label"));
			}
		} ;
		chartAxisPane = new BasicPane() {
			@Override
			protected String title4PopupWindow() {
				return PaneTitleConstants.CHART_STYLE_AXIS_TITLE;
			}
		} ;
		chartAreaPane = new BasicPane() {
			@Override
			protected String title4PopupWindow() {
				return PaneTitleConstants.CHART_STYLE_AREA_TITLE;
			}
		} ;
		chartAnalysisLinePane = new BasicPane() {
			@Override
			protected String title4PopupWindow() {
				return PaneTitleConstants.CHART_STYLE_LINE_TITLE;
			}
		} ;
	}

	protected void initSelfListener(Container parentComponent) {
     for (int i = 0; i < parentComponent.getComponentCount(); i++) {
         Component tmpComp = parentComponent.getComponent(i);
         if (tmpComp instanceof Container) {
             initListener((Container) tmpComp);
         }
         if (tmpComp instanceof UIObserver) {
             ((UIObserver) tmpComp).registerChangeListener(uiObserverListener);
         }
     }
 }

	@Override
	protected JPanel createContentPane() {
		JPanel content = new JPanel(new BorderLayout());
		if (chart == null) {
			return content;
		}
		stylePane = new TabPane();
		content.add(stylePane, BorderLayout.CENTER);
		return content;
	}

	@Override
	public void populate(ChartCollection collection) {
		this.chart = collection.getSelectedChart();
		this.remove(leftContentPane);
		initContentPane();
		this.removeAttributeChangeListener();
		stylePane.populateBean(chart);
		this.addAttributeChangeListener(listener);
		this.initAllListeners();
	}

	protected ChartSeriesPane createChartSeriesPane(){
		return new ChartSeriesPane(ChartStylePane.this);
	}


	@Override
	public void update(ChartCollection collection) {
		stylePane.updateBean(collection.getSelectedChart());
	}

	/**
	 * ���ض�Ӧ��ͼƬ·��.
	 */
	public String getIconPath() {
		return "com/fr/design/images/chart/ChartStyle.png";
	}

	/**
	 * ���ؽ���ı�������
     * @return  ��������.
	 */
	public String title4PopupWindow() {
		return PaneTitleConstants.CHART_STYLE_TITLE;
	}
	
	/**
	 * ����ѡ�еĽ���id
	 */
	public void setSelectedByIds(int level, String... id) {
        stylePane.setSelectedByIds(level, id);
    }

	/**
	 * ע���¼�������
	 * @param listener �۲��߼����¼�
	 */
	public void registerChangeListener(UIObserverListener listener) {
		 this.uiObserverListener = listener;
	}

	/**
	 * �Ƿ���Ӧ�¼�
	 * @return ��
	 */
	public boolean shouldResponseChangeListener() {
		return true;
	}

	class TabPane extends MultiTabPane<Chart> {

		protected void dealWithTabChanged(int index) {
			dealWithChoosedPane(index);
			cardLayout.show(centerPane, NameArray[index]);
			tabChanged();
		}

		private void dealWithChoosedPane(int index){
			//��һ�����Ǳ���
			if(index == 0){
				return;
			}

			BasicPane choosedPane = paneList.get(index);
			centerPane.remove(index);
			paneList.remove(index);
			if(ComparatorUtils.equals(choosedPane,chartLegendPane)){
				chartLegendPane = new ChartLegendPane();
				choosedPane = chartLegendPane;
			}else if(ComparatorUtils.equals(choosedPane,chartSeriesPane)){
				chartSeriesPane = createChartSeriesPane();
				choosedPane = chartSeriesPane;
			}else if(ComparatorUtils.equals(choosedPane,chartDataLabelPane)){
				chartDataLabelPane = new ChartDataLabelPane(ChartStylePane.this);
				choosedPane = chartDataLabelPane;
			}else if(ComparatorUtils.equals(choosedPane,chartLabelFontPane)) {
				chartLabelFontPane = new ChartLabelFontPane();
				choosedPane = chartLabelFontPane;
			}else if(ComparatorUtils.equals(choosedPane,chartAxisPane)){
				chartAxisPane = isNeedFormula ? new ChartAxisPane(chart.getPlot(),ChartStylePane.this)
						: new ChartAxisNoFormulaPane(chart.getPlot(),ChartStylePane.this);
				choosedPane = chartAxisPane;
			}else if(ComparatorUtils.equals(choosedPane,chartAreaPane)){
				chartAreaPane = new ChartAreaPane(chart.getPlot(),ChartStylePane.this);
				choosedPane = chartAreaPane;
			}else if(ComparatorUtils.equals(choosedPane,chartDatasheetPane)){
				chartAxisPane = isNeedFormula ? new ChartAxisPane(chart.getPlot(),ChartStylePane.this)
						: new ChartAxisNoFormulaPane(chart.getPlot(),ChartStylePane.this);
				chartDatasheetPane.useWithAxis((ChartAxisPane)chartAxisPane);
				choosedPane = chartDatasheetPane;
			}else if(ComparatorUtils.equals(choosedPane,chartAnalysisLinePane)){
				chartAnalysisLinePane = new ChartAnalysisLinePane(ChartStylePane.this);
				choosedPane = chartAnalysisLinePane;
			}
			initSelfListener(choosedPane);
			centerPane.add(choosedPane,choosedPane.getTitle(),index);
			paneList.add(index,choosedPane);
		}

		@Override
		protected void tabChanged() {
			ChartStylePane.this.removeAttributeChangeListener();
			((BasicBeanPane<Chart>) paneList.get(tabPane.getSelectedIndex())).populateBean(chart);
			ChartStylePane.this.addAttributeChangeListener(listener);
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
		protected List<BasicPane> initPaneList() {
			List<BasicPane> paneList = new ArrayList<BasicPane>();
			Plot plot = chart.getPlot();

            if(isNeedFormula){
                paneList.add(chartTitlePane);
            }else{
                paneList.add(chartTitlePaneNoFormula);
            }

			if(!plot.isSupportLegend()){
				plot.setLegend(null);
			}

			if(plot.getLegend() != null) {
				paneList.add(chartLegendPane);
			}
			
			if(plot.isSupportDataSeriesAttr()) {// ϵ������
				paneList.add(chartSeriesPane);
			}

            if(plot.isSupportDataLabelAttr()) {// ���ݱ�ǩ
				paneList.add(chartDataLabelPane);
            }

            if(plot.isMeterPlot()){
                //��ǩ����
                paneList.add(chartLabelFontPane);
            }
			
			if(plot.isHaveAxis()) {// Ȼ��������������.
				paneList.add(chartAxisPane);
				
				paneList.add(chartAreaPane);
				
				if(plot.isSupportDataSheet()) {
					paneList.add(chartDatasheetPane);
				}
			} else {
				paneList.add(chartAreaPane);
			}
			
			//���ط������ͽ���
			if(plot.needAnalysisLinePane()){
				paneList.add(chartAnalysisLinePane);
			}
			
			return paneList;
		}

		@Override
		public void populateBean(Chart chart) {
			if (chart == null || stylePane.getSelectedIndex() == -1) {
				return;
			}
			((BasicBeanPane<Chart>) paneList.get(stylePane.getSelectedIndex())).populateBean(chart);

		}

		@Override
		public Chart updateBean() {
			if (chart == null) {
				return null;
			}
			((BasicBeanPane<Chart>) paneList.get(stylePane.getSelectedIndex())).updateBean(chart);
			return chart;
		}
		
		@Override
		public void updateBean(Chart ob) {
			((BasicBeanPane<Chart>) paneList.get(stylePane.getSelectedIndex())).updateBean(ob);
		}

		/**
		 * ���ܽ��������
		 */
		@Override
		public boolean accept(Object ob) {
			return true;
		}

		/**
		 * �������
		 */
		@Override
		public String title4PopupWindow() {
			return "";
		}

		/**
		 * ����
		 */
		public void reset() {

		}
		
		/**
		 * ����ѡ�еĽ���id
		 */
		public void setSelectedByIds(int level, String... id) {
            tabPane.setSelectedIndex(-1);
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
	}
}

