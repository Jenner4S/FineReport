package com.fr.design.chart.report;

import com.fr.chart.chartdata.MapMoreLayerReportDefinition;
import com.fr.chart.chartdata.MapSingleLayerReportDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.general.Inter;

import java.awt.*;

/**
 * �����ȡ  ��Ԫ����������ý���
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2012-10-23 ����10:07:41
 */
public class MapReportCubeDataPane extends FurtherBasicBeanPane<MapMoreLayerReportDefinition> {

    private MapMoreReportIndexPane reportPane;
	
	public MapReportCubeDataPane() {
		this.setLayout(new BorderLayout(0, 0));

        reportPane = new MapMoreReportIndexPane();
		this.add(reportPane, BorderLayout.CENTER);
	}

	/**
	 * �ܹ�չʾ������ж�.
     * @param ob  ��������
     *            @return  �����Ƿ����.
	 */
	public boolean accept(Object ob) {
		return ob instanceof MapMoreLayerReportDefinition;
	}

	/**
	 * ��������
	 */
	public void reset() {

	}

	/**
	 * ���浯������
     * @return  ���ر���.
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("Cell");
	}

	@Override
	public void populateBean(MapMoreLayerReportDefinition ob) {// ����Populate �����㼶�ĸ���,  ����Ϊrow�ĸ���

		if (ob != null) {

            MapSingleLayerReportDefinition[] values = ob.getNameValues();

            if(values != null && values.length > 0) {
                reportPane.populateBean(values[0]);
            }
		}
	}

	@Override
	public MapMoreLayerReportDefinition updateBean() {
		MapMoreLayerReportDefinition reportDefinition = new MapMoreLayerReportDefinition();

        reportDefinition.clearNameValues();
        reportDefinition.addNameValue(reportPane.updateBean());

		return reportDefinition;
	}

}
