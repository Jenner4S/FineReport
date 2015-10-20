package com.fr.design.chart.series.PlotSeries;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import com.fr.chart.base.MapSvgAttr;
import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.base.Utils;
import com.fr.design.data.DesignTableDataManager;
import com.fr.data.TableDataSource;
import com.fr.data.core.DataCoreUtils;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.icombobox.FilterComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itableeditorpane.UIArrayTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.mainframe.chart.gui.data.DatabaseTableDataPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.gui.xcombox.ComboBoxUseEditor;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.TextEditor;
import com.fr.general.Inter;
import com.fr.third.org.apache.poi.hssf.record.formula.functions.T;

/**
 * ��ͼ, ����������.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2012-10-19 ����03:19:53
 */
public class MapDefiAreaNamePane extends BasicBeanPane<String> implements AbstrctMapAttrEditPane{

	private DatabaseTableDataPane tableDataBox;
	private FilterComboBox columnBox;

	// ˫��:  ���Label, �����б�(ȫ������UIComboBox, ֧���Զ���).
	private UITableEditorPane tableEditorPane;
	private UIArrayTableModel tableEditorModel;

	private String[] initNames = new String[]{};

	private String editName = "";
	private boolean isNeedDataSource = true;
	private MapSvgAttr currentSvg;

	public MapDefiAreaNamePane(boolean isNeedDataSource){
		this.isNeedDataSource = isNeedDataSource;
		initCom();
	}

	public MapDefiAreaNamePane() {
		initCom();
	}

	private void initCom() {
		this.setLayout(new BorderLayout(0, 0));

		JPanel northPane = new JPanel();
		if(this.isNeedDataSource){
			this.add(northPane, BorderLayout.NORTH);
		}

		northPane.setLayout(new FlowLayout(FlowLayout.LEFT));

		UILabel lable = new UILabel(Inter.getLocText("Chart-DS_TableData") + ":", SwingConstants.RIGHT);

		// ���ݼ�, �ֶ�, �����б�, ʹ�ø����ݽ����Զ�����
		tableDataBox = new DatabaseTableDataPane(lable) {
			protected void userEvent() {
				refreshAreaNameBox();
			}
		};

		tableDataBox.setPreferredSize(new Dimension(200, 20));
		northPane.add(tableDataBox);

		columnBox = new FilterComboBox<T>();
		columnBox.setPreferredSize(new Dimension(40, 20));
		columnBox.addItemListener(columnChange);

		northPane.add(new UILabel(Inter.getLocText("FR-Chart-Map_Field")+":"));
		northPane.add(columnBox);

		tableEditorModel = new UIArrayTableModel(new String[]{Inter.getLocText("FR-Chart-Map_Use_Field"), Inter.getLocText("FR-Chart-Area_Name")}, new int[]{}) {
			public boolean isCellEditable(int row, int col) {
				return col != 0;
			}
		};
		tableEditorModel.setDefaultEditor(Object.class, new DefaultComboBoxEditor());
		tableEditorModel.setDefaultRenderer(Object.class, new DefaultComboBoxRenderer());
		tableEditorPane = new UITableEditorPane<Object[]>(tableEditorModel);
		this.add(tableEditorPane);
	}

	ItemListener columnChange = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			if (columnBox.getSelectedItem() != null) {
				String columnName = Utils.objectToString(columnBox.getSelectedItem());

				TableDataWrapper tableDataWrappe = tableDataBox.getTableDataWrapper();
				TableDataSource source = DesignTableDataManager.getEditingTableDataSource();
				if (tableDataWrappe == null || source == null) {
					return;
				}

				initNames = DataCoreUtils.getColValuesInData(source, tableDataWrappe.getTableDataName(), columnName);

				if (tableEditorModel != null) {
					tableEditorModel.stopCellEditing();// ֻ������ˢ���б��combox
				}
			}
		}
	};

	private void refreshAreaNameBox() {// ˢ�����������б�
		TableDataWrapper tableDataWrappe = tableDataBox.getTableDataWrapper();
		if (tableDataWrappe == null) {
			return;
		}
		List<String> columnNameList = tableDataWrappe.calculateColumnNameList();

		columnBox.setItemList(columnNameList);
	}

	// ��Ӧ��ͼ������
	public void populateBean(String mapName) {
		if (MapSvgXMLHelper.getInstance().containsMapName(mapName)) {
			MapSvgAttr editingMapAttr = MapSvgXMLHelper.getInstance().getMapAttr(mapName);
			this.editName = mapName;
			this.populateMapAttr(editingMapAttr);
		}
	}

	@Override
	public String updateBean() {
		// �̶��洢 �� ������ ��Ӧֵ �б�  MapHelper
        updateMapAttr();
		MapSvgXMLHelper.getInstance().removeMapAttr(currentSvg.getName());
		MapSvgXMLHelper.getInstance().pushMapAttr(currentSvg.getName(),currentSvg);
        return "";
	}

	private void updateMapAttr(){
		if(currentSvg != null){
			tableEditorModel.stopCellEditing();
			List list = tableEditorPane.update();
			for(int i = 0, size = list.size(); i < size; i++) {
				Object[] tmp = (Object[]) list.get(i);
				String name = Utils.objectToString(tmp[0]);
				String nameTo = Utils.objectToString(tmp[1]);
				currentSvg.setNameTo(name, nameTo);
			}
		}
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText(new String[]{"Define", "Area_Name"});
	}

	/**
     * ���½���
     * @param  editingMapAttr ��ͼ����
    */
	public void populateMapAttr(MapSvgAttr editingMapAttr) {
		List popuValues = new ArrayList();
		if(editingMapAttr == null) {
			currentSvg = null;
			tableEditorPane.populate(popuValues.toArray());
			return;
		}
		currentSvg =editingMapAttr;
		List namesList = new ArrayList();
		Iterator shapeNames = editingMapAttr.shapeValuesIterator();
		while (shapeNames.hasNext()) {
			namesList.add(shapeNames.next());// �ȵõ����еĴ�������, Ȼ���ٴ����Ӧ��ϵ
		}
		for (int i = 0; i < namesList.size(); i++) {
			Object name = namesList.get(i);
			Object value = editingMapAttr.getNameToValue(Utils.objectToString(name));
			popuValues.add(new Object[]{name, value});
		}
		tableEditorPane.populate(popuValues.toArray());
	}

	/**
     * ����MapSvgAttr
     * @return  ��������
    */
	public MapSvgAttr updateCurrentAttr() {
		updateMapAttr();
		return currentSvg;
	}

	private class DefaultComboBoxEditor extends AbstractCellEditor implements TableCellEditor {
		private ValueEditorPane cellEditor;

		public DefaultComboBoxEditor() {
			cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ComboBoxUseEditor(initNames)});
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			if (column == 0) {
				cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new TextEditor()});
			} else {
				cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ComboBoxUseEditor(initNames)});
			}
			cellEditor.populate(value == null ? "" : value);
			return cellEditor;
		}

		public Object getCellEditorValue() {
			return cellEditor.update();
		}
	}

	private class DefaultComboBoxRenderer extends DefaultTableCellRenderer {
		private ValueEditorPane cellEditor;

		public DefaultComboBoxRenderer() {
			cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ComboBoxUseEditor(initNames)});
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (column == 0) {
				cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new TextEditor()});
			} else {
				cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ComboBoxUseEditor(initNames)});
			}
			cellEditor.populate(value == null ? "" : value);
			return cellEditor;
		}
	}
}
