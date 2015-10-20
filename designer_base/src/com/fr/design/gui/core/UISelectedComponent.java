package com.fr.design.gui.core;

import javax.swing.event.ChangeListener;

/**
 * ����ӿ�˵��һ����������ǿ���ѡ���
 * 
 * @author zhou
 * @since 2012-5-17����4:38:17
 */
public interface UISelectedComponent {
	/**
	 * isSelected
	 * 
	 * @return
	 */
	public boolean isSelected();

	/**
	 * setSelected
	 * 
	 * @param isSelected
	 */
	public void setSelected(boolean isSelected);
	
	/**
	 * the selected changed listener
	 * @param l
	 */
	void addChangeListener(ChangeListener l);

}
