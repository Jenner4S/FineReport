package com.fr.design.cell.smartaction;

import java.awt.BorderLayout;
import java.awt.Window;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionListener;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.selection.SelectionListener;

/*
 * SmartJTablePane������Grid����ѡ��Ԫ��ʱ�༭JTable
 */
public abstract class SmartJTablePane extends BasicPane {
	
	public static final int OK = 0;
	public static final int CANCEL = 1;
	
	protected ElementCasePane actionReportPane;
	protected AbstractTableModel model;
	
	protected SelectionListener gridSelectionChangeL;
	protected SmartJTablePaneAction action;
	
	protected JTable table;
	protected JScrollPane scrollPane;

	protected boolean old_editable = true;

	protected int editingRowIndex = 0;

	public SmartJTablePane(AbstractTableModel model, 
			ElementCasePane actionReportPane) {
		this.model = model;
		this.actionReportPane = actionReportPane;
		old_editable = actionReportPane.isEditable();

		initComponents();
	}

	public void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());

		// BasicPane��north������
		this.add(new UILabel(Inter.getLocText("RWA-Click_Cell_To_Edit_Value")), BorderLayout.NORTH);

		// BasicPane��center��JTable
		table = new JTable(model);
		this.add(scrollPane = new JScrollPane(table), BorderLayout.CENTER);

		// ����л�ѡ����ʱ,editingRowIndexҲҪ���ű�
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				int selected = table.getSelectedRow();
				// alex:��SmartJTablePaneʧȥ����ʱ,����Ҳ�ᴥ�����,selectedΪ-1
				if (selected >= 0) {
					setEditingRowIndex(selected);
					table.repaint();
				}
			}
		});

		setCellRenderer();
		actionReportPane.addSelectionChangeListener(gridSelectionChangeL);
	}
	
	public void changeGridSelectionChangeListener(SelectionListener g){
		actionReportPane.removeSelectionChangeListener(gridSelectionChangeL);
		gridSelectionChangeL = g;
		actionReportPane.addSelectionChangeListener(gridSelectionChangeL);
	}
	
	public void changeSmartJTablePaneAction(SmartJTablePaneAction a){
		this.action = a;
	}
	
	public abstract void setCellRenderer();
	
	
	
	/*
	 * �������ڱ༭��RowIndex,��Scroll
	 */
	protected void setEditingRowIndex(int idx) {
		editingRowIndex = idx;

		table.scrollRectToVisible(table.getCellRect(editingRowIndex, 2, true));
	}

	@Override
	public BasicDialog showWindow(Window window) {
		BasicDialog dlg = super.showSmallWindow(window,new DialogActionListener() {
			public void doOk() {
				action.doDialogExit(SmartJTablePane.OK);
			}

			public void doCancel() {
				action.doDialogExit(SmartJTablePane.CANCEL);
			}
		});
		

		return dlg;
	}
	
}
