package com.fr.design.chart.report;

import com.fr.chart.chartdata.MapMoreLayerTableDefinition;
import com.fr.chart.chartdata.MapSingleLayerTableDefinition;
import com.fr.data.impl.NameTableData;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.gui.data.DatabaseTableDataPane;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * ��ͼ �����ȡ ���ݼ��������
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2012-10-23 ����10:55:39
 */
public class MapTableCubeDataPane extends FurtherBasicBeanPane<MapMoreLayerTableDefinition> {

	private DatabaseTableDataPane dataFromBox;

    private MapMoreTableIndexPane  tablePane;
	
	public MapTableCubeDataPane() {
		this.setLayout(new BorderLayout());

		JPanel pane = new JPanel();
		this.add(pane, BorderLayout.NORTH);

		pane.setLayout(new FlowLayout(FlowLayout.LEFT));

		UILabel label = new UILabel(Inter.getLocText("Select_Data_Set") + ":", SwingConstants.RIGHT);

		dataFromBox = new DatabaseTableDataPane(label) {
			protected void userEvent() {
				refreshAreaNameBox();
			}
		};
		dataFromBox.setPreferredSize(new Dimension(180, 20));
		pane.add(dataFromBox);

        tablePane = new MapMoreTableIndexPane();
		this.add(tablePane, BorderLayout.CENTER);
	}

	private void refreshAreaNameBox() {// ˢ�����������б�
		TableDataWrapper tableDataWrappe = dataFromBox.getTableDataWrapper();
		if (tableDataWrappe == null) {
			return;
		}

        List<String> columnNameList = tableDataWrappe.calculateColumnNameList();
		tablePane.initAreaComBox(columnNameList.toArray(new String[columnNameList.size()]));
	}

	/**
	 * �������
     * @param ob  ����
     *            @return  ���ؽ���.
	 */
	public boolean accept(Object ob) {
		return true;
	}

	/**
	 * ����
	 */
	public void reset() {
	}

	/**
	 * ���浯������
     * @return  ���ر���.
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("DS-TableData");
	}

	@Override
	public void populateBean(MapMoreLayerTableDefinition tableDefinition) {// editingLayerCout

		if (tableDefinition != null) {
			dataFromBox.populateBean(tableDefinition.getTableData());

            MapSingleLayerTableDefinition[] values = tableDefinition.getNameValues();
            if(values != null && values.length > 0) {
                tablePane.populateBean(values[0]);
            }
		}
	}

	@Override
	public MapMoreLayerTableDefinition updateBean() {
		MapMoreLayerTableDefinition tableDefinition = new MapMoreLayerTableDefinition();

		TableDataWrapper tableDataWrappe = dataFromBox.getTableDataWrapper();
		if (tableDataWrappe != null) {
			tableDefinition.setTableData(new NameTableData(tableDataWrappe.getTableDataName()));

            tableDefinition.clearNameValues();
            tableDefinition.addNameValue(tablePane.updateBean());
		}

		return tableDefinition;
	}
}
