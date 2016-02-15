package com.fr.design.mainframe.chart.gui;

import com.fr.base.FRContext;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ͼ�� ���Ա�, ����ѡ�� ����.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version ����ʱ�䣺2012-12-26 ����10:56:51
 */
public class ChartTypePane extends AbstractChartAttrPane{
	private ComboBoxPane chartTypePane;
	private ChartTypeButtonPane buttonPane;
    private ChartEditPane editPane;
    private ChartCollection editingCollection;
	
	@Override
	protected JPanel createContentPane() {
		JPanel content = new JPanel(new BorderLayout());
		
		buttonPane = new ChartTypeButtonPane();
		content.add(buttonPane, BorderLayout.NORTH);
		
		chartTypePane = new ComboBoxPane();
		chartTypePane.setBorder(BorderFactory.createEmptyBorder(0,0, 0, 10));
		content.add(chartTypePane, BorderLayout.CENTER);
		
		buttonPane.setEditingChartPane(chartTypePane);
		
		return content;
	}

	/**
	 * ������Ϊ��ťʱ��ͼƬλ��. design_base
	 */
	public String getIconPath() {
		return "com/fr/design/images/chart/ChartType.png";
	}

	/**
	 * �������
     * @return �������
	 */
	public String title4PopupWindow() {
		return PaneTitleConstants.CHART_TYPE_TITLE;
	}
	
	class ComboBoxPane extends UIComboBoxPane<Chart>{
		@Override
		protected List<FurtherBasicBeanPane<? extends Chart>> initPaneList() {
			List<FurtherBasicBeanPane<? extends Chart>> paneList = new ArrayList<FurtherBasicBeanPane<? extends Chart>>();
			ChartTypeInterfaceManager.getInstance().addPlotTypePaneList(paneList);
			return paneList;
		}

		@Override
		protected String title4PopupWindow() {
			return null;
		}
		
		public void updateBean(Chart chart) {
            int lastSelectIndex = editPane.getSelectedChartIndex(chart);

            try{
                Chart newDefaultChart = (Chart)((AbstractChartTypePane)cards.get(jcb.getSelectedIndex())).getDefaultChart().clone();
                if(!chart.accept(newDefaultChart.getClass())){
                    //vanChart �� chart ֮���л�
                    editingCollection.removeNameObject(editingCollection.getSelectedIndex());
                    editingCollection.addChart(newDefaultChart);
                    chart = newDefaultChart;
                }
            }catch (CloneNotSupportedException e){
                FRContext.getLogger().error(e.getMessage(), e);
            }

			//��һ�����滻plot
            ((AbstractChartTypePane) cards.get(jcb.getSelectedIndex())).updateBean(chart);

			Plot plot = chart.getPlot();

			if(plot != null){
				String plotID = plot.getPlotID();

				//plot�ı�Ļ�ͼ�����;���ı���

				chart.setWrapperName(ChartTypeManager.getInstance().getWrapperName(plotID));

				chart.setChartImagePath(ChartTypeManager.getInstance().getChartImagePath(plotID));

				boolean isUseDefault = ChartTypeInterfaceManager.getInstance().isUseDefaultPane(plotID);

				if(editPane.isDefaultPane() != isUseDefault || (!isUseDefault && lastSelectIndex != jcb.getSelectedIndex())){
					editPane.reLayout(chart);
				}
			}
		}
	}

	/**
	 * ���½������� ����չʾ
	 */
	public void populate(ChartCollection collection) {
		Chart chart = collection.getSelectedChart();
		chartTypePane.populateBean(chart);
		
		buttonPane.populateBean(collection);
	}

	/**
	 * �����������
	 */
	public void update(ChartCollection collection) {
        editingCollection = collection;
		buttonPane.update(collection);// �ڲ�����ʱ �Ѿ���������.
		Chart chart = collection.getSelectedChart();

		chartTypePane.updateBean(chart);
	}

    /**
     * ����ͼ������ͽ���
     * @return ���ͽ���
     */
    public FurtherBasicBeanPane[] getPaneList(){
        return chartTypePane.getCards().toArray(new FurtherBasicBeanPane[0]);
    }

    /**
     * ��ǰѡ�е�ͼ���index
     * @return ��ǰѡ�е�ͼ���index
     */
    public int getSelectedIndex(){
        return chartTypePane.getSelectedIndex();
    }

    /**
     * ����ѡ�е�ͼ���index
     * @return ѡ�е�ͼ������
     */
    public int getSelectedChartIndex(){
        return chartTypePane.getSelectedIndex();
    }

    /**
     * �����±༭�����
     * @param currentEditPane �����±༭�����
     */
    public void registerChartEditPane(ChartEditPane currentEditPane) {
        this.editPane = currentEditPane;
    }
}
