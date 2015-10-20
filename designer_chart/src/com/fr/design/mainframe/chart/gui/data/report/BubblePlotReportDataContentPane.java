package com.fr.design.mainframe.chart.gui.data.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.BubblePlot;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.BubbleReportDefinition;
import com.fr.chart.chartdata.BubbleSeriesValue;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.general.Inter;

import java.util.ArrayList;
import java.util.List;

/**
 * ����ͼ ���Ա� ��Ԫ������Դ����.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2012-12-19 ����03:47:56
 */
public class BubblePlotReportDataContentPane extends AbstractReportDataContentPane {
	private static final int BUBBLE = 4;
	private ChartDataFilterPane filterPane;
	
	public BubblePlotReportDataContentPane(ChartDataPane parent) {
		initEveryPane();
		
		this.add(new BoldFontTextLabel(Inter.getLocText("Data_Filter")), "0,4,2,4");
		this.add(filterPane = new ChartDataFilterPane(new BubblePlot(), parent), "0,6,2,4");
	}
	
	@Override
	protected String[] columnNames() {
		return  new String[]{
			Inter.getLocText("Bubble-Series_Name"),
			Inter.getLocText("ChartF-X_Axis"),
			Inter.getLocText("ChartF-Y_Axis"),
			Inter.getLocText("FRFont-Size")
		};
	}
	
	public void checkBoxUse() {
	}

	/**
	 * ��������ͼ ��Ԫ���������
	 */
	public void populateBean(ChartCollection collection) {
		if(collection != null) {
			TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
			if(definition instanceof BubbleReportDefinition) {
				seriesPane.populateBean(populateSeriesEntryList((BubbleReportDefinition)definition));
			}
		}
		filterPane.populateBean(collection);
	}
	
	private List populateSeriesEntryList(BubbleReportDefinition seriesList) {
		List array = new ArrayList();
		for(int i = 0; i < seriesList.size(); i++) {
			BubbleSeriesValue seriesEntry = (BubbleSeriesValue)seriesList.get(i);
			Object[] nameAndValue = new Object[BUBBLE];
			nameAndValue[0] = seriesEntry.getBubbleSereisName();
			nameAndValue[1] = seriesEntry.getBubbleSeriesX();
			nameAndValue[2] = seriesEntry.getBubbleSeriesY();
			nameAndValue[3] = seriesEntry.getBubbleSeriesSize();
			array.add(nameAndValue);
		}
		return array;
    }
	
	/**
	 * ��������ͼ ��Ԫ��������ݵ�collection
	 */
	public void updateBean(ChartCollection collection) {
		if(collection != null) {
			BubbleReportDefinition bubbleReport = new BubbleReportDefinition();
			collection.getSelectedChart().setFilterDefinition(bubbleReport);
			
			updateSeriesEntryList(bubbleReport, seriesPane.updateBean());
			filterPane.updateBean(collection);
		}
	}
	
	private void updateSeriesEntryList(BubbleReportDefinition list, List<Object[]> valueList) {
		for (int i = 0; i < valueList.size(); i++) {
			Object[] rowValueList = valueList.get(i);

			BubbleSeriesValue seriesEntry = new BubbleSeriesValue();
			
			seriesEntry.setBubbleSeriesName(canBeFormula(rowValueList[0]));
			seriesEntry.setBubbleSeriesX(canBeFormula(rowValueList[1]));
			seriesEntry.setBubbleSeriesY(canBeFormula(rowValueList[2]));
			seriesEntry.setBubbleSeriesSize(canBeFormula(rowValueList[3]));
			list.add(seriesEntry);
		}
    }
}
