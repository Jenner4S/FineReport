package com.fr.design.widget.ui;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.form.ui.NoneWidget;
import com.fr.design.widget.DataModify;

/**
 * 
 * @author Administrator
 * ���ڴ���û�пؼ������
 */ 
public class NoneWidgetDefinePane extends AbstractDataModify<NoneWidget> {
	
	@Override
	protected String title4PopupWindow() {
		return "none";
	}
	
	@Override
	public void populateBean(NoneWidget w) {
	}

	@Override
	public NoneWidget updateBean() {
		return new NoneWidget();
	}
}
