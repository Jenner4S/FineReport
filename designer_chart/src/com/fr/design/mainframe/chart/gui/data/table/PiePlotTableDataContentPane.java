package com.fr.design.mainframe.chart.gui.data.table;

import java.awt.BorderLayout;
import java.util.List;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.PiePlot;
import com.fr.design.mainframe.chart.gui.ChartDataPane;

public class PiePlotTableDataContentPane extends AbstractTableDataContentPane{
	private SeriesTypeUseComboxPane typeChoosePane;
	
	public PiePlotTableDataContentPane(ChartDataPane parent) {
		typeChoosePane = new SeriesTypeUseComboxPane(parent, new PiePlot());
		this.setLayout(new BorderLayout());
		this.add(typeChoosePane, BorderLayout.CENTER);
	}

    /**
     * �ж�ʱ��ʹ��typeChoosePane
     * @param hasUse �Ƿ�ʹ��
     */
	public void checkBoxUse(boolean hasUse) {
		typeChoosePane.checkUseBox(hasUse);
	}
	
	protected void refreshBoxListWithSelectTableData(List list) {
		typeChoosePane.refreshBoxListWithSelectTableData(list);
	}

    /**
     * ������е�box����
     */
    public void clearAllBoxList(){
        typeChoosePane.clearAllBoxList();
    }
	
	@Override
	public void updateBean(ChartCollection collection) {
		typeChoosePane.updateBean(collection);
	}

	@Override
	public void populateBean(ChartCollection collection) {
		typeChoosePane.populateBean(collection,this.isNeedSummaryCaculateMethod());
	}

    /**
     * ���²����������
     */
    public void redoLayoutPane(){
        typeChoosePane.relayoutPane(this.isNeedSummaryCaculateMethod());
    }
}
