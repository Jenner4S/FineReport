package com.fr.design.chart.report;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.MapPlot;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.MultiTabPane;
import com.fr.general.Inter;

import java.util.ArrayList;
import java.util.List;

/**
 * ��ͼ�����ȡ����, ����һ�����Tab�л�����
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2012-10-22 ����10:23:37
 */
public class MapMoreCubeLayerPane extends MultiTabPane<ChartCollection>{
	private static final long serialVersionUID = -174286187746442527L;
	
	private MapCubeLayerPane layerPane;
	private MapCubeDataPane dataPane;
	
	@Override
	protected List<BasicPane> initPaneList() {
		List<BasicPane> paneList = new ArrayList<BasicPane>();

		paneList.add(layerPane = new MapCubeLayerPane());
		paneList.add(dataPane = new MapCubeDataPane());
		
		return paneList;
	}
	
	public ChartCollection updateBean() {
		return null;// do nothing
	}
	
	public void populateBean(ChartCollection collection) {
		Chart selectChart = collection.getSelectedChart();
		if(selectChart != null && selectChart.getPlot() instanceof MapPlot) {
			MapPlot map = (MapPlot)selectChart.getPlot();
            layerPane.setSvg(map.isSvgMap());
			layerPane.populateBean(map.getMapName());
		}

		// ȷ�ϲ㼶��ϵ 
		dataPane.populateBean(collection.getSelectedChart().getFilterDefinition());
	}

	public void updateBean(ChartCollection collection) {
		
		collection.getSelectedChart().setFilterDefinition(dataPane.update());
		
		Chart selectChart = collection.getSelectedChart();
		if(selectChart != null && selectChart.getPlot() instanceof MapPlot) {
			MapPlot map = (MapPlot)selectChart.getPlot();
			layerPane.updateBean(map.getMapName());// ȷ�����µ�ͼ��������Ӧ�Ĳ㼶��ϵ
		}
	}
	
	/**
	 * ˢ�²㼶�� �� ������populate ���ݵĲ���
     * @param collection  ͼ���ռ���.
	 */
	public void init4PopuMapTree(ChartCollection collection) {
		Chart selectChart = collection.getSelectedChart();
		if(selectChart != null && selectChart.getPlot() instanceof MapPlot) {
			MapPlot map = (MapPlot)selectChart.getPlot();
			if(layerPane != null) {
                layerPane.setSvg(map.isSvgMap());
				layerPane.initRootTree(map.getMapName());
			}
		}
	}

    /**
     * �ж��Ƿ�ϸ�
     * @param ob  �����ж�
     * @return Ĭ�Ϻϸ�.
     */
	public boolean accept(Object ob) {
		return true;
	}

    /**
     * �������
     * @return ���ر���
     */
	public String title4PopupWindow() {
		return Inter.getLocText("FR-Chart-Muiti_In");
	}

    /**
     * ����
     */
	public void reset() {

	}
	
	/**
	 * �����Ƿ�֧�ֵ�Ԫ������.
	 */
	public void setSurpportCellData(boolean surpportCellData) {
		dataPane.justSupportOneSelect(surpportCellData);
	}
}
