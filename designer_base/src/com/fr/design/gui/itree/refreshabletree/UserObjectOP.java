package com.fr.design.gui.itree.refreshabletree;

import java.util.List;
import java.util.Map;

import com.fr.design.gui.itree.refreshabletree.loader.ChildrenNodesLoader;


/**
 * UserObjectRefreshJTree�Ĳ���
 * 
 * @editor zhou
 * @since 2012-3-28����9:49:31
 */

public interface UserObjectOP<T> extends ChildrenNodesLoader {

	/*
	 * ��ʼ������name, T��ֵ��
	 */
	public List<Map<String, T>> init();

	/*
	 * ButtonEnabled intercept
	 */
	public boolean interceptButtonEnabled();
	
	/*
	 * �Ƴ�������name��TableData
	 */
	public void removeAction(String name);

}
