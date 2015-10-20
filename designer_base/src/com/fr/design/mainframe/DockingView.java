/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe;

import javax.swing.Icon;

import com.fr.design.gui.frpane.UITitlePanel;
import com.fr.design.dialog.BasicPane;

/**
 * @author richer
 * @since 6.5.5
 *        ������
 *        ���е�����Docking��View�������嶼�̳��Դ���
 *        ��������������κ�ʱ��򿪸�Docking��ʱ���ܺ���Ӧ����ƽ����������
 */
// TODO ALEX_SEP ���ĸ�λ�ÿ��Է�Docking,�ܲ��ܰѸ�Docking��Preferred��λ�����Է���Docking������?
public abstract class DockingView extends BasicPane {
    /**
     * Generally speaking, invoke this method when need refresh the content of the docking.
     * @param agrs
     */
    // TODO ALEX_SEP ����������봫�κβ���
    public abstract void refreshDockingView();

    public abstract String getViewTitle();

    public abstract Icon getViewIcon();
    
    public abstract Location preferredLocation();
    
    public UITitlePanel createTitlePanel(){
    	return new UITitlePanel(this,getViewTitle());
    }
    
    @Override
    protected String title4PopupWindow() {
    	return getViewTitle();
    }
    
    public static enum Location {
    	WEST_ABOVE, WEST_BELOW
    }
}
