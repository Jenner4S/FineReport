package com.fr.design.fun;

import javax.swing.*;

/**
 * �����˵����������չ�����ڿ��Ƹò˵��Ƿ���web����ʾ
 * @author focus
 * @date Jul 2, 2015
 * @since 8.0
 */
public interface ExportToolBarProvider {
	
	public static final String XML_TAG = "ExportToolBarProvider";
	
	
	/**
	 *
	 * ������� ����web���Ƿ���ʾ�ò˵���checkbox�����
	 * 
	 * @param pane ���
	 * @return �����
	 */
	public JPanel updateCenterPane(JPanel pane);
	
	/**
	 * ����xml������web�ΰ�ť��ʾ״̬���¶�Ӧ��checkbox
	 * 
	 */
	public void populate();
	
	/**
	 * ����checkbox����web�β˵��Ƿ���ʾ
	 * 
	 * @return
	 */
	public void update();
	
}
