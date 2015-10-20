package com.fr.design.fun;

import javax.swing.JPanel;

/**
 * 导出菜单设计器端拓展，用于控制该菜单是否在web端显示
 * @author focus
 * @date Jul 2, 2015
 * @since 8.0
 */
public interface ExportToolBarProvider {
	
	public static final String XML_TAG = "ExportToolBarProvider";
	
	
	/**
	 *
	 * 用于添加 控制web端是否显示该菜单的checkbox的面板
	 * 
	 * @param pane 面板
	 * @return 该面板
	 */
	public JPanel updateCenterPane(JPanel pane);
	
	/**
	 * 根据xml里面存的web段按钮显示状态更新对应的checkbox
	 * 
	 */
	public void populate();
	
	/**
	 * 根据checkbox控制web段菜单是否显示
	 * 
	 * @return
	 */
	public void update();
	
}
