package com.fr.design.beans;

import com.fr.design.dialog.BasicPane;

/**
 * Update Pane
 */
public abstract class BasicBeanPane<T> extends BasicPane {

	/**
	 * Populate.
	 */
	public abstract void populateBean(T ob);

	/**
	 * Update.
	 */
	public abstract T updateBean();

	public void updateBean(T ob) {

	}

	/**
	 * ����Ȩ�޹��������
	 */
	public void populateAuthority() {

	}

	/**
	 * ����������ͼ��������ĵ�ͼ���
	 * @param mapType ��ͼ����
	 */
	public void dealWidthMap(String mapType){

	}

}
