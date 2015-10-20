package com.fr.design.gui.core;

import javax.swing.event.ChangeListener;

/**
 * ����ӿ�˵��һ�����������������tabbedpane����ʽ
 * 
 * @author zhou
 * @since 2012-5-17����4:46:00
 */
public interface UITabComponent {
	/**
	 * ��ȡ��ǰѡ�е�tab
	 * 
	 * @return
	 */
	public int getSelectedIndex();

	/**
	 * ����ѡ�е�tab
	 * 
	 * @param newSelectedIndex
	 */
	public void setSelectedIndex(int newSelectedIndex);
	
	/**
	 * Adds a <code>ChangeListener</code> to the listener list.
	 */
	public void addChangeListener(ChangeListener l);
	
	/**
	 * removes a <code>ChangeListener</code> from the listener list.
	 */
	public void removeChangeListener(ChangeListener l);
}
