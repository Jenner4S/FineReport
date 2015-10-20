package com.fr.design.gui.core;

/**
 * ��render����
 * 
 * @author zhou
 * @since 2012-5-28����4:27:24
 */
public interface UIFurtherTabComponent<T> extends UITabComponent {

	/**
	 * set the SelectedItem by the element
	 * 
	 * @param ob
	 */
	public void setSelectedItem(T element);

	/**
	 * get the SelectedItem
	 * 
	 * @return
	 */
	public T getSelectedItem();
}
