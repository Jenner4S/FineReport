package com.fr.design.chart.report;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.Utils;
import com.fr.chart.chartdata.MapSingleLayerTableDefinition;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.data.impl.NameTableData;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.chart.gui.data.DatabaseTableDataPane;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

/**
 * �����ͼʱ: ���ݼ� �������
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2012-10-22 ����11:14:59
 */
public class MapTableDataSinglePane extends FurtherBasicBeanPane<MapSingleLayerTableDefinition> implements UIObserver {

	private DatabaseTableDataPane fromTableData;
	
	private ArrayList<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
	private String[] initNames = {""};
	
	private UIComboBox areaNameBox;
	private UICorrelationPane titleValuePane;

	public MapTableDataSinglePane() {
		this.setLayout(new BorderLayout());

		JPanel northPane = new JPanel();
		this.add(northPane, BorderLayout.NORTH);

		northPane.setLayout(new FlowLayout(FlowLayout.LEFT));

		UILabel label = new UILabel(Inter.getLocText("Select_Data_Set") + ":", SwingConstants.RIGHT);

		northPane.add(fromTableData = new DatabaseTableDataPane(label) {
			@Override
			protected void userEvent() {
				refreshAreaNameBox();
			}
		});
		fromTableData.setPreferredSize(new Dimension(180, 20));
		northPane.add(fromTableData);
		
		UILabel nameLabel = new UILabel("    " + Inter.getLocText("Area_Name") + ":", SwingConstants.RIGHT);
		areaNameBox = new UIComboBox();
		areaNameBox.setPreferredSize(new Dimension(80, 20));
		
		JPanel areaNamePane = new JPanel();
		areaNamePane.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		
		areaNamePane.add(nameLabel);
		areaNamePane.add(areaNameBox);
		
		JPanel pane = new JPanel();
		this.add(pane, BorderLayout.CENTER);
		pane.setLayout(new BorderLayout());
		
		pane.add(areaNamePane, BorderLayout.NORTH);
		
		String[] titles = {Inter.getLocText(new String[]{"Filed", "Title"}), Inter.getLocText("Area_Value")};
		titleValuePane = new UICorrelationPane(titles){
			public UITableEditor createUITableEditor() {
				return new InnerTableEditor();
			}
		};
		
		pane.add(titleValuePane, BorderLayout.CENTER);
	}

	private void refreshAreaNameBox() {// ˢ�����������б�
		TableDataWrapper tableDataWrappe = fromTableData.getTableDataWrapper();
		if (tableDataWrappe == null) {
			return;
		}
		List<String> columnNameList = tableDataWrappe.calculateColumnNameList();
		initNames = columnNameList.toArray(new String[columnNameList.size()]);
		
		Object oldSelected = areaNameBox.getSelectedItem();
		areaNameBox.removeAllItems();
		for(int i = 0, size = initNames.length; i < size; i++) {
			areaNameBox.addItem(initNames[i]);
		}
		areaNameBox.getModel().setSelectedItem(oldSelected);
		stopEditing();
	}

	/**
	 * �������
	 */
	public boolean accept(Object ob) {
		return false;
	}

	/**
	 * ��������
	 */
	public void reset() {

	}

	/**
	 * ���浯������
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("DS-TableData");
	}

	private void stopEditing() {
	}

	@Override
	public void populateBean(MapSingleLayerTableDefinition ob) {
		stopEditing();
		if (ob instanceof MapSingleLayerTableDefinition) {
			MapSingleLayerTableDefinition mapDefinition = (MapSingleLayerTableDefinition) ob;

			fromTableData.populateBean(((NameTableData) mapDefinition.getTableData()));
			areaNameBox.setSelectedItem(mapDefinition.getAreaName());
			
			List paneList = new ArrayList();
			int titleValueSize = mapDefinition.getTitleValueSize();
			for(int i = 0; i < titleValueSize; i++) {
				SeriesDefinition definition = mapDefinition.getTitleValueWithIndex(i);
				if(definition != null && definition.getSeriesName() != null && definition.getValue() != null) {
					paneList.add(new Object[]{definition.getSeriesName(), definition.getValue()});
				}
			}
			
			if(!paneList.isEmpty()) {
				titleValuePane.populateBean(paneList);
			}
		}
	}

	@Override
	public MapSingleLayerTableDefinition updateBean() {// ��һ��������update
		stopEditing();

		MapSingleLayerTableDefinition definition = new MapSingleLayerTableDefinition();

		TableDataWrapper tableDataWrappe = fromTableData.getTableDataWrapper();
		if (tableDataWrappe == null || areaNameBox.getSelectedItem() == null) {
			return null;
		}
		
		definition.setTableData(new NameTableData(tableDataWrappe.getTableDataName()));
		definition.setAreaName(Utils.objectToString(areaNameBox.getSelectedItem()));
		
		List paneList = titleValuePane.updateBean();
		for(int i = 0, size = paneList.size(); i < size; i++) {
			Object[] values = (Object[])paneList.get(i);
			if(values.length == 2) {
				SeriesDefinition seriesDefinition = new SeriesDefinition();
				seriesDefinition.setSeriesName(values[0]);
				seriesDefinition.setValue(values[1]);
				definition.addTitleValue(seriesDefinition);
			}
		}

		return definition;
	}

	/**
	 * ������Ǽ�һ���۲��߼����¼�
	 *
	 * @param listener �۲��߼����¼�
	 */
	public void registerChangeListener(final UIObserverListener listener) {
		changeListeners.add(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				listener.doChange();
			}
		});
	}

	/**
	 * ����Ƿ���Ҫ��Ӧ��ӵĹ۲����¼�
	 *
	 * @return �����Ҫ��Ӧ�۲����¼��򷵻�true�����򷵻�false
	 */
	public boolean shouldResponseChangeListener() {
		return true;
	}

	private class InnerTableEditor extends UITableEditor {
		private JComponent editorComponent;

		/**
		 * ���ص�ǰ�༭����ֵ
		 */
		public Object getCellEditorValue() {
			if(editorComponent instanceof UITextField) {
				UITextField textField = (UITextField)editorComponent;
				return textField.getText();
			} else if(editorComponent instanceof UIComboBox) {
				UIComboBox boxPane = (UIComboBox)editorComponent;
				return boxPane.getSelectedItem();
			}
			return super.getCellEditorValue();
		}

		/**
		 * ���ص�ǰ�༭��..
		 */
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			if (column == table.getModel().getColumnCount()) {
				return null;
			}
			if(column == 0 ) {
				UITextField text = new UITextField();
				if(value != null) {
					text.setText(Utils.objectToString(value));
				}
				
				text.registerChangeListener(new UIObserverListener() {
					@Override
					public void doChange() {
						titleValuePane.fireTargetChanged();
					}
				});
				
				this.editorComponent = text;
			} else {
				UIComboBox box = new UIComboBox(initNames);
				box.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						titleValuePane.fireTargetChanged();
						titleValuePane.stopCellEditing();
					}
				});
				
				if (value != null && StringUtils.isNotEmpty(value.toString())) {
					box.setSelectedItem(value);
				} else {
					box.setSelectedItem(value);
				}
				
				this.editorComponent = box;
			}
			return this.editorComponent;
		}
	}
}
