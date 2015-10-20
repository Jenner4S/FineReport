package com.fr.design.gui.ilist;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JList;
import javax.swing.ListModel;

import com.fr.general.ComparatorUtils;

/**
 * ֧�ּ���������ٲ������JList
 * 
 * @editor zhou
 * @since 2012-3-28����3:10:58
 */
public class QuickList extends JList {
	private ListModel listModel;

	public QuickList(ListModel model) {
		super(model);
		this.listModel = model;
		initList();
	}

	private void initList() {
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				String key = e.getKeyChar() + "";
				for (int i = 0; i < listModel.getSize(); i++) {
					String first = listModel.getElementAt(i).toString();
					if (ComparatorUtils.equals(first, key) && !ComparatorUtils.equals(i, QuickList.this.getSelectedIndex())) {
						QuickList.this.setSelectedIndex(i);
						break;
					}
				}
			}
		});
	}

}
