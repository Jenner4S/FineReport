package com.fr.design.chart.report;

import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.itableeditorpane.UIArrayTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.gui.xcombox.ComboBoxUseEditor;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.TextEditor;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.List;

/**
 * ��ͼ, �²���ȡ����
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2012-10-19 ����03:19:53
 */
public class MapCubeSetDataPane extends BasicBeanPane<List>{
	
	private UITableEditorPane tableEditorPane;// ��ͼ����  +  ��ͼ�����б�
	
	private String[] initNames = new String[]{""};
	
	public MapCubeSetDataPane() {
		initCom();
	}

	private void initCom() {
		this.setLayout(new BorderLayout(0, 0));
		
		UIArrayTableModel model = new UIArrayTableModel(new String[]{Inter.getLocText("FR-Chart-Area_Name"), Inter.getLocText("FR-Chart-Drill_Map")}, new int[] {}) {
			public boolean isCellEditable(int row, int col) {
				return col != 0;
			}
		};
		model.setDefaultEditor(Object.class, new DefaultComboBoxEditor());
		model.setDefaultRenderer(Object.class, new DefaultComboBoxRenderer());
		
		tableEditorPane = new UITableEditorPane<Object[]>(model);
		this.add(tableEditorPane);
		
		model.addRow(new Object[]{"", ""});
	}
	
	/**
	 * ˢ�������б�
	 */
	public void freshComboxNames() {
		initNames = MapSvgXMLHelper.getInstance().mapAllNames();
	}
	
	// ��Ҫ�õ���ͼ ������������. �Լ���Ӧ�õ�����.
	@Override
	public void populateBean(List ob) {
		tableEditorPane.populate(ob.toArray());
	}

	@Override
	public List updateBean() {
		return tableEditorPane.update();
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("FR-Chart-Drill_Setting");
	}
	
	/**
	 * refresh TODO ֻ����Ҫ��ʱ����initNames
	* @author kunsnat E-mail:kunsnat@gmail.com
	* @version ����ʱ�䣺2012-11-20 ����05:12:20
	 */
	
	private class DefaultComboBoxEditor extends AbstractCellEditor implements TableCellEditor {
		private static final long serialVersionUID = -3239789564820528730L;
		private ValueEditorPane cellEditor;
		
		public DefaultComboBoxEditor() {
			cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ComboBoxUseEditor(initNames)});
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			if(column == 0) {
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
		private static final long serialVersionUID = -695450455731718014L;
		private ValueEditorPane cellEditor;
		
		public DefaultComboBoxRenderer() {
			cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ComboBoxUseEditor(initNames)});
		}
		
    	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    		if(column == 0) {
    			cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new TextEditor()});
			} else {
				cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ComboBoxUseEditor(initNames)});
			}
    		cellEditor.populate(value == null ? "" : value);
			return cellEditor;
    	}
    }
}