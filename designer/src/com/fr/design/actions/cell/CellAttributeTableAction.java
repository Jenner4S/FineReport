package com.fr.design.actions.cell;

import java.awt.event.ActionEvent;

import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.CellElementPropertyPane;

/**
 * 所有的CellAttributeTableAction都是指向单元格属性表的,点了就自动跳转到单元格属性表
 * 
 * @author zhou
 * @since 2012-5-23下午4:19:48
 */
public abstract class CellAttributeTableAction extends UpdateAction {

	protected abstract String getID();

	@Override
	public void actionPerformed(ActionEvent e) {
		CellElementPropertyPane.getInstance().GoToPane(getID());
	}

}