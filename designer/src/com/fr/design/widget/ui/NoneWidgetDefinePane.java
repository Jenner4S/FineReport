package com.fr.design.widget.ui;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.form.ui.NoneWidget;
import com.fr.design.widget.DicPaneAndTreePaneCreator;

/**
 * 
 * @author Administrator
 * 用于处理没有控件的情况
 */ 
public class NoneWidgetDefinePane extends BasicBeanPane<NoneWidget> implements DicPaneAndTreePaneCreator {
	
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

    public DictionaryPane getDictionaryPane() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public TreeSettingPane getTreeSettingPane() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
