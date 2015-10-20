/**
 * 
 */
package com.fr.design.form.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;

import com.fr.form.ui.container.WFitLayout;
import com.fr.form.ui.container.WLayout;

/**
 * @author jim
 * @date 2014-9-23
 */
public class FRScaleLayout implements FRLayoutManager{

	/**
	 * �������
	 * @param name ����
	 * @param comp ���
	 */
	@Override
	public void addLayoutComponent(String name, Component comp) {
		
	}

	/**
	 * �Ƴ����
	 * @param comp ���
	 */
	@Override
	public void removeLayoutComponent(Component comp) {
		
	}

	/**
	 * ���Ŵ�С
	 * @param parent ������
	 * @return Ĭ�ϴ�С
	 */
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return new Dimension(WLayout.MIN_WIDTH, WFitLayout.MIN_HEIGHT);
	}

	/**
	 * ��С��С
	 * @param parent ������
	 * @return Ĭ�ϳ�ʼ��С
	 */
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension(WLayout.MIN_WIDTH, WFitLayout.MIN_HEIGHT);
	}

	/**
	 * ����ˢ��
	 * @param target ����
	 */
	@Override
	public void layoutContainer(Container parent) {
		synchronized (parent.getTreeLock()) {
			if (parent.getComponentCount() < 1) {
				return;
			}
			// ���ǲ������ڱ߾࣬�����ı��򶼿ؼ������õĵ�ǰlayoutˢ�����ӿؼ�
			Component comp = parent.getComponent(0);
			if (comp != null) {
				Rectangle rec = parent.getBounds();
				comp.setBounds(0, 0, rec.width, comp.getHeight());
			}
		}
	}

	/**
	 * �Ƿ����ô�С
	 * @return ��
	 */
	@Override
	public boolean isResizable() {
		return true;
	}

}
