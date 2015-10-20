package com.fr.design.mainframe.chart;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.web.ChartHyperPoplink;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperPopAttrPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartOtherPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.ChartTypePane;

import java.util.ArrayList;


/**
 * ͼ�� ��������  tab �л�
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-8-6 ����02:18:23
 */
public class ChartHyperEditPane  extends ChartEditPane {
	
	private ChartComponent useChartComponent;
	private ChartHyperPopAttrPane attrPane;
	
	public ChartHyperEditPane(int paraType) {
		paneList = new ArrayList<AbstractChartAttrPane>();
		
		paneList.add(attrPane = new ChartHyperPopAttrPane(paraType));
		paneList.add(new ChartTypePane());

        dataPane4SupportCell =  new ChartDataPane(listener);
        dataPane4SupportCell.setSupportCellData(false);
		paneList.add(dataPane4SupportCell);
		paneList.add(new ChartStylePane(listener));
		paneList.add(new ChartOtherPane());

		createTabsPane();
	}


    protected void addTypeAndDataPane() {
        paneList.add(attrPane);
        paneList.add(typePane);
        paneList.add(dataPane4SupportCell);

    }


    protected void setSelectedTab() {
        tabsHeaderIconPane.setSelectedIndex(1);
        card.show(center, getSelectedTabName());
        for (int i = 0; i < paneList.size(); i++) {
            paneList.get(i).registerChartEditPane(getCurrentChartEditPane());
        }
    }

	protected ChartEditPane getCurrentChartEditPane() {
		return this;
	}
	
	/**
	 * ����  ��Ӧ��ChartComponent
	 * @param chartComponent ��Ӧ��ChartComponent
	 */
	public void useChartComponent(ChartComponent chartComponent) {
		this.useChartComponent = chartComponent;
	}
	
	/**
	 * ��Ӧ���������е�demo�仯.
	 */
	public void fire() {
		if(useChartComponent != null) {
			useChartComponent.populate(this.collection);
			useChartComponent.reset();
		}
	}

    /**
     * ȡ
     * @param hyperlink ����
     */
	public void populateHyperLink(ChartHyperPoplink  hyperlink) {
		attrPane.populateBean(hyperlink);
		populate((ChartCollection)hyperlink.getChartCollection());
	}

    /**
     * ��
     * @param hyperlink ����
     */
	public void updateHyperLink(ChartHyperPoplink hyperlink) {
		attrPane.updateBean(hyperlink);
	}
	
}
