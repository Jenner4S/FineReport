package com.fr.design.designer.beans;

import java.awt.Point;

import com.fr.design.designer.creator.XCreator;

/**
 * ��Ⱦ����Ŀ����Ϊ������߲��ֹ������ṩ�������Ⱦ��ڡ�
 * @since 6.5.3
 */
public interface HoverPainter extends Painter {
	/**
	 *  ��ǰ�����ȵ㣬��������ڵ�
	 * @param p ����λ��
	 */
	void setHotspot(Point p);

	/**
	 *  ��ǰҪ���õ����
	 * @param creator ���
	 */
	void setCreator(XCreator creator);
}
