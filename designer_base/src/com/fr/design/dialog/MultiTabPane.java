package com.fr.design.dialog;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;

import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.ibutton.UITabGroup;

/**
 * 
 * ��׼�Ķ��Tab�л�����.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version ����ʱ�䣺2013-4-22 ����10:31:03
 */
public abstract class MultiTabPane<T> extends FurtherBasicBeanPane<T>{
	private static final long serialVersionUID = 2298609199400393886L;
	protected UITabGroup tabPane;
	protected String[] NameArray;
	protected JPanel centerPane;
	protected CardLayout cardLayout;
	protected List<BasicPane> paneList;

	protected abstract List<BasicPane> initPaneList();
	public abstract void populateBean(T ob);
	public abstract void updateBean(T ob);
	
	public int getSelectedIndex() {
		return tabPane.getSelectedIndex();
	}

	public MultiTabPane() {
		cardLayout = new CardLayout();
		paneList = initPaneList();
		relayoutWhenListChange();
	}
	
	/**
	 * ��List�еĽ���仯ʱ, ���²���
	 */
	public void relayoutWhenListChange() {
		centerPane = new JPanel(cardLayout) {
			@Override
			public Dimension getPreferredSize() {
				if (tabPane.getSelectedIndex() == -1) {
					return super.getPreferredSize();
				} else {
					return paneList.get(tabPane.getSelectedIndex()).getPreferredSize();
				}
			}
		};
		NameArray = new String[paneList.size()];
		for (int i = 0; i < paneList.size(); i++) {
			BasicPane pane = paneList.get(i);
			NameArray[i] = pane.title4PopupWindow(); 
			centerPane.add(pane, NameArray[i]);
		}

		tabPane = new UITabGroup(NameArray) {
			@Override
			public void tabChanged(int index) {
				dealWithTabChanged(index);
			}
		};
		tabPane.setSelectedIndex(0);
		tabPane.tabChanged(0);
		initLayout();
	}

	protected void dealWithTabChanged(int index) {
		cardLayout.show(centerPane, NameArray[index]);
		tabChanged();
	}
	
	protected void tabChanged() {
		
	}
	
	protected void initLayout() {
		this.setLayout(new BorderLayout(0, 4));
		this.add(tabPane, BorderLayout.NORTH);
		this.add(centerPane, BorderLayout.CENTER);
	}
}
