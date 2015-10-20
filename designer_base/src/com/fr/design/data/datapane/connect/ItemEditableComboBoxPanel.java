package com.fr.design.data.datapane.connect;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.FRGUIPaneFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class ItemEditableComboBoxPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static final Object EMPTY = new Object() {
		public String toString() {
			return "";
		}
	};
	
	protected UIComboBox itemComboBox;
	protected UIButton editButton;
    protected UIButton refreshButton;
	
	public ItemEditableComboBoxPanel() {
		super();
		
		initComponents();
	}
	
	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createM_BorderLayout());
		
		itemComboBox = new UIComboBox();
		itemComboBox.setEnabled(true);
		this.add(itemComboBox, BorderLayout.CENTER);
		
		editButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/control/control-center2.png"));
        refreshButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/control/refresh.png"));
        JPanel jPanel = FRGUIPaneFactory.createNColumnGridInnerContainer_Pane(2, 4 ,4);
        jPanel.add(editButton);
        jPanel.add(refreshButton);
		this.add(jPanel, BorderLayout.EAST);
        Dimension buttonSize = new Dimension(26, 20);
		editButton.setPreferredSize(buttonSize);
		editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                editItems();
            }
        });
        refreshButton.setPreferredSize(buttonSize);
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshItems();
            }
        });
	}
	
	/**
	 * ��itemComboBox���ActionListener
	 */
	public void addComboBoxActionListener(ActionListener l) {
		itemComboBox.addActionListener(l);
	}
	
	/*
	 * ˢ��itemComboBox������
	 */
	protected void refreshItems() {
		// ��¼ԭ��ѡ�е�Item,���¼��غ���Ҫ�ٴ�ѡ��
		Object lastSelectedItem = itemComboBox.getSelectedItem();
		
		DefaultComboBoxModel model = ((DefaultComboBoxModel) itemComboBox.getModel());
		model.removeAllElements();
		
		// �ȼ�EMPTY,�ټ�items
		model.addElement(EMPTY);
		
		java.util.Iterator<String> itemIt = items();
		while(itemIt.hasNext()) {
			model.addElement(itemIt.next());
		}
		
		// �ٴ�ѡ��֮ǰѡ�е�Item
		int idx = model.getIndexOf(lastSelectedItem);
		if(idx < 0) {
			idx = 0;
		}
		itemComboBox.setSelectedIndex(idx);
	}
	
	/*
	 * �õ����е�itemComboBox��ѡ�е�Item
	 */
	public String getSelectedItem() {
		Object selected = itemComboBox.getSelectedItem();
		
		return selected instanceof String ? (String)selected : null;
	}
	
	/*
	 * ѡ��name��
	 */
	public void setSelectedItem(String name) {
		DefaultComboBoxModel model = ((DefaultComboBoxModel) itemComboBox.getModel());
		model.setSelectedItem(name);
	}
	
	/*
	 * ˢ��ComboBox.items
	 */
	protected abstract java.util.Iterator<String> items();
	
	/*
	 * �����Ի���༭Items
	 */
	protected abstract void editItems();
}
