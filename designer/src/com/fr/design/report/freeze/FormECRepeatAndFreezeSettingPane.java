package com.fr.design.report.freeze;

import javax.swing.JPanel;

import com.fr.general.Inter;

/**
 * �����ظ��������Լ��������õ�Pane
 * 
 * Ŀǰֻ֧�����ö�����, ��֧�ֺ���Ķ��������
 */
public class FormECRepeatAndFreezeSettingPane extends RepeatAndFreezeSettingPane {
	
	/**
	 * ��ȡ��ҳ����ı���(���в���Ҫд��ҳ����)
	 * 
	 * @return ��ҳ����ı���
	 * 
	 *
	 * @date 2014-11-14-����1:32:08
	 * 
	 */
	protected String getPageFrozenTitle(){
		return Inter.getLocText("FR-Engine_Frozen") + ":";
	}
	
	protected void initWriteListener(){
		
	}
	
	protected void addWriteFrozen(JPanel freezePanel) {
		
	}
	
}
