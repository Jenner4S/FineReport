/**
 * 
 */
package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;

/**
 * @author jim
 * @date 2014-8-5
 */
public class FRScaleLayoutAdapter extends AbstractLayoutAdapter {

	/**
	 * ���캯��
	 * 
	 * @param container
	 *            ��������
	 */
	public FRScaleLayoutAdapter(XLayoutContainer container) {
		super(container);
	}

	/**
	 * �ܷ��Ӧλ�÷��õ�ǰ���
	 * 
	 * @param creator
	 *            ���
	 * @param x
	 *            ��ӵ�λ��x����λ���������container��
	 * @param y
	 *            ��ӵ�λ��y����λ���������container��
	 * @return �Ƿ���Է���
	 */
	@Override
	public boolean accept(XCreator creator, int x, int y) {
		return false;
	}

	/**
	 * 
	 * @see com.fr.design.designer.beans.adapters.layout.AbstractLayoutAdapter#addComp(com.fr.design.designer.creator.XCreator,
	 *      int, int)
	 */
	@Override
	protected void addComp(XCreator creator, int x, int y) {
		return;
	}

	@Override
	public GroupModel getLayoutProperties() {
		return null;
	}

}
