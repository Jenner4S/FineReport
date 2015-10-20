package com.fr.design.data.tabledata.tabledatapane;

import java.awt.Component;
import java.awt.event.ActionEvent;

import com.fr.design.gui.itableeditorpane.UITableEditAction;
import com.fr.design.gui.itableeditorpane.UITableModelAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;

//august������˵�����ֻ��һ�� �Ͳ�����jtable�����������ܱ�֮ǰ��ArrayTableModel��
public abstract class OneListTableModel<T> extends UITableModelAdapter<T> {

	protected Component component = null; ////ָ��ȷ�϶Ի���ĸ�����,bug40340
	public OneListTableModel(String columnName) {
		this(new String[] { columnName });
	}
	
	public OneListTableModel(String columnName, Component component) {
		this(new String[] { columnName });
		this.component = component;
	}

	// ����ľͲ�Ҫ�����������
	protected OneListTableModel(String[] columnNames) {
		super(columnNames);
	}
	@Override
	public int getColumnCount() {
		return 1;
	}
	@Override
	public UITableEditAction[] createAction() {
		return new UITableEditAction[] { getAddAction(),new DeleteAction(), new MoveUpAction(), new MoveDownAction() };
	}

	public abstract UITableEditAction getAddAction();

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		T os = this.getList().get(rowIndex);
		return os;
	}
	@SuppressWarnings("unchecked")
	public void setValueAt(Object value, int row, int column) {
		this.getList().set(row, (T) value);
		fireTableCellUpdated(row, column);
	}
	@Override
	public boolean isCellEditable(int row, int col) {
		return true;
	}
	
	public class AddJsAction extends AddTableRowAction{
		public AddJsAction() {
			super();
		}

		@SuppressWarnings("unchecked")
		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			FILEChooserPane fileChooser = FILEChooserPane.getInstance(false, false, true, new ChooseFileFilter("js", "javascript" + Inter.getLocText("File")));
			if(fileChooser.showOpenDialog(DesignerContext.getDesignerFrame()) == FILEChooserPane.OK_OPTION) {
				final FILE file = fileChooser.getSelectedFILE();
				if(file == null) {
					return ;
				}
				String fileName = file.getName();
				String fileType = fileName.substring(fileName.lastIndexOf(CoreConstants.DOT) + 1);
				if(!"js".equalsIgnoreCase(fileType)) {
					return ;
				}
				String temp = file.getPath().substring(1);
				addRow((T) temp);
				fireTableDataChanged();
			}
		}
	}
	public class AddCssAction extends AddTableRowAction{
		public AddCssAction() {
			super();
		}

		@SuppressWarnings("unchecked")
		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			FILEChooserPane fileChooser = FILEChooserPane.getInstance(false, false, true, new ChooseFileFilter("css", "css" + Inter.getLocText("File")));
			if(fileChooser.showOpenDialog(DesignerContext.getDesignerFrame()) == FILEChooserPane.OK_OPTION) {
				final FILE file = fileChooser.getSelectedFILE();
				if(file == null) {
					return ;
				}
				String fileName = file.getName();
				String fileType = fileName.substring(fileName.lastIndexOf(CoreConstants.DOT) + 1);
				if(!"css".equalsIgnoreCase(fileType)) {
					return ;
				}
				String temp = file.getPath().substring(1);
				addRow((T) temp);
				fireTableDataChanged();
			}
		}
	}
	
}
