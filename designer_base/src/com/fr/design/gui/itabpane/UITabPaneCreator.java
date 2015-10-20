package com.fr.design.gui.itabpane;

import javax.swing.JPanel;

public class UITabPaneCreator {
	/**
	 * ����Component�����и�name��Ա��������ò�Ҫ���Ǹ�
	 */
	private String tabName;
	
	private String iconPath;
	
	// private String tooltips;
	
	private JPanel pane;

	public UITabPaneCreator(String tabName, String iconPath, JPanel pane) {
		this.tabName = tabName;
		this.setIconPath(iconPath);
		this.pane = pane;
	}

	public void setTabName(String name) {
		this.tabName = name;
	}

	public String getTabName() {
		return this.tabName;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public JPanel getPane() {
		return pane;
	}


}
