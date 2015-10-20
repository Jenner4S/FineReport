package com.fr.design.mainframe.chart.gui.data;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.TableData;
import com.fr.design.constants.UIConstants;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.TableDataComboBox;
import com.fr.data.impl.NameTableData;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.dialog.BasicPane;

public class DatabaseTableDataPane extends BasicPane{
	private static final long serialVersionUID = 5316016202202932242L;
	private TableDataComboBox tableNameCombox;
	private UIButton reviewButton;

	public DatabaseTableDataPane(UILabel label) {
		initTableCombox();
		initReviewButton();
		this.setLayout(new BorderLayout(0,0));
		if(label != null) {
			this.add(label, BorderLayout.WEST);
		}

        JPanel pane = new JPanel(new BorderLayout(LayoutConstants.HGAP_LARGE,0));
        pane.add(tableNameCombox,BorderLayout.CENTER);

        pane.add(reviewButton,BorderLayout.EAST);
        this.add(pane,BorderLayout.CENTER);
	}

	/**
	 * ����ѡ�е�����Դ.
	 */
	public TableDataWrapper getTableDataWrapper() {
		return tableNameCombox.getSelectedItem();  
	}

	public void populateBean(TableData nameTableData) {
		if(nameTableData == null) {
			tableNameCombox.setSelectedItem(nameTableData);
		} else {
			tableNameCombox.setSelectedTableDataByName(((NameTableData)nameTableData).getName());
		}
	}
	
	@Override
	protected String title4PopupWindow() {
		return null;
	}
	
	/**
	 * ���ݼ��б�ѡ�к���¼�
	 */
	protected void userEvent() {

	}

	private void initTableCombox() {
		tableNameCombox = new TableDataComboBox(DesignTableDataManager.getEditingTableDataSource());
		tableNameCombox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.DESELECTED){
					userEvent();
				}
			}
		});
	}
	
	private void initReviewButton() {
		reviewButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/data/search.png"));
		reviewButton.setBorder(new LineBorder(UIConstants.LINE_COLOR));
		reviewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				TableDataWrapper tableDataWrappe = tableNameCombox.getSelectedItem();
				if ( tableDataWrappe != null) {
					try {
						tableDataWrappe.previewData();
					} catch (Exception e1) {
						FRContext.getLogger().error(e1.getMessage(), e1);
					}
				}
				super.mouseReleased(e);
			}
		});
	}
}
