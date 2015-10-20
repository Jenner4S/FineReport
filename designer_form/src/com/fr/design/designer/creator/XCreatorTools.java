/**
 * 
 */
package com.fr.design.designer.creator;

import java.awt.Component;
import java.util.ArrayList;

/**
 * @author jim
 * @date 2014-11-7
 * 
 */
public interface XCreatorTools {

	/**
	 * �ؼ�������ʾ�����
	 * @param path �ؼ���list
	 */
	void notShowInComponentTree(ArrayList<Component> path);
	
	/**
	 * �������������
	 * @param name ����
	 */
	void resetCreatorName(String name);
	
	/**
	 * ���ر༭���������scaleΪ���ڲ����
	 * @return ���
	 */
	XCreator getEditingChildCreator();
	
	/**
	 * ���ض�Ӧ���Ա�������scale��title�����������
	 * @return ���
	 */
	XCreator getPropertyDescriptorCreator();
	
	/**
	 * �����������Bound; û�в�����
	 *  @param minHeight ��С�߶�
	 */
	void updateChildBound(int minHeight);
	
	/**
	 * �Ƿ���Ϊ�ؼ�����Ҷ�ӽڵ�
	 * @return ���򷵻�true
	 */
	boolean isComponentTreeLeaf();
	
	/**
	 * �Ƿ�Ϊsclae��titleר������
	 * @return ���򷵻�true
	 */
	boolean isDedicateContainer();
	
}
