package com.fr.design.chart.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartdata.MapSingleLayerTableDefinition;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.general.Inter;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class MapSinglePane4Chart extends FurtherBasicBeanPane<TopDefinitionProvider> {

   	private MapTableDataSinglePane4Chart tableSinglePane;

   	public MapSinglePane4Chart() {
   		initCom();
   	}

   	private void initCom() {
   		this.setLayout(new BorderLayout());

   		this.add(tableSinglePane = new MapTableDataSinglePane4Chart(), BorderLayout.CENTER);
   	}

    /**
     * �ж�׼������
     * @param ob  ���ݼ�
     * @return �ǲ��Ƕ�������
     */
   	public boolean accept(Object ob) {
   		return ob instanceof TopDefinition;
   	}

   	/**
   	 * ����
   	 */
   	public void reset() {

   	}

    /**
     *�������
     * @return �������
     */
   	public String title4PopupWindow() {
   		return Inter.getLocText(new String[]{"SingleLayer", "Chart-Map"});
   	}

   	/**
   	 * ���ص����ͼʱ�� ������Դ����
   	 */
   	public void populateBean(TopDefinitionProvider ob) {
        if(ob instanceof MapSingleLayerTableDefinition) {
   			tableSinglePane.populateBean((MapSingleLayerTableDefinition)ob);
   		}
   	}

   	/**
   	 * �������� �������ݽ���
   	 */
   	public TopDefinitionProvider updateBean() {
   		return tableSinglePane.updateBean();
   	}

	/**
     * �������ݼ��ı�
     * @param tableDataWrapper ���ݼ�
     */
	public void fireTableDataChanged(TableDataWrapper tableDataWrapper) {
		tableSinglePane.setTableDataWrapper(tableDataWrapper);
		tableSinglePane.refreshAreaNameBox();
	}
}
