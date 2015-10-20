/**
 * 
 */
package com.fr.design.form.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import com.fr.form.ui.container.WLayout;
import com.fr.form.ui.container.WTitleLayout;
import com.fr.general.ComparatorUtils;

/**
 * ���ֹ���������ҪΪһЩ��Ҫ�ӱ��������ã��籨��顢ͼ���
 * 
 * @author jim
 * @date 2014-9-25
 */
public class FRTitleLayout implements FRLayoutManager, LayoutManager{
	
	public static final String TITLE = "Title";
	
	public static final String BODY = "Body";
	
	// ����ؼ���Ĭ��Ϊ�ı���
	private Component title;
	// ����ؼ����б�����顢ͼ���
	private Component body;
	private int gap;
	
	/**
	 * ���캯��
	 */
	public FRTitleLayout() {
		this(0);
	}
	
	/**
	 * ����������϶gap�Ĳ���
	 * @param gap  ��϶ֵ
	 */
	public FRTitleLayout(int gap) {
		this.gap = gap;
	}
	
	 /**
	  * ����
     * Returns the  gap between components.
     */
    public int getGap() {
    	return gap;
    }

    /**
     * ����
     * Sets the gap between components.
     * @param the gap between components
     */
    public void setGap(int gap) {
    	this.gap = gap;
    }
	
	 /**
	 * �������
	 * @param name ����
	 * @param comp ���
	 */
  	public void addLayoutComponent(String name, Component comp) {
	      synchronized (comp.getTreeLock()) {
				if (ComparatorUtils.equals(name, null)) {
				    name = BODY;
				}
				if (ComparatorUtils.equals(name, BODY)) {
					body = comp;
				} else if (ComparatorUtils.equals(name, TITLE)) {
				    title = comp;
				} 
	      }
	 }

	/**
	 * �Ƴ����
	 * @param comp ���
	 */
	@Override
	public void removeLayoutComponent(Component comp) {
		 synchronized (comp.getTreeLock()) {
			 if (comp == title) {
				 title = null;
			 } 
		 }
	}

	/**
	 * ���Ŵ�С
	 * @param parent ������
	 * @return Ĭ�ϴ�С
	 */
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return new Dimension(WLayout.MIN_WIDTH, WLayout.MIN_HEIGHT*2);
	}

	/**
	 * ��С��С
	 * @param parent ������
	 * @return Ĭ�ϳ�ʼ��С
	 */
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		// �б���ʱ����С�߶�Ϊ��������߶�
		return new Dimension(WLayout.MIN_WIDTH, title==null? WLayout.MIN_HEIGHT : WLayout.MIN_HEIGHT+WTitleLayout.TITLE_HEIGHT);
	}

	/**
	 * ����ˢ��
	 * @param target ����
	 */
	@Override
	public void layoutContainer(Container target) {
		synchronized (target.getTreeLock()) {
			int width = target.getWidth();
			int height = target.getHeight();
			int titleH = title==null ? 0 : WTitleLayout.TITLE_HEIGHT;
			for (int i=0; i< target.getComponentCount(); i++) {
				Component comp = target.getComponent(i);
				if (comp == title) {
					comp.setBounds(0, 0, width, WTitleLayout.TITLE_HEIGHT);
				} else if (comp == body) {
					int y = titleH+gap;
					comp.setBounds(0, y, width, height-y);
				}
			}
		}
	}
	
	 public Object getConstraints(Component comp) {
        if (comp == null){
            return null;
        }
		if (comp == title) {
		    return TITLE;
		} else if (comp == body) {
		    return BODY;
		} 
		return null;
	 }

	/**
	 * �Ƿ����ô�С
	 * @return ��
	 */
	@Override
	public boolean isResizable() {
		return false;
	}

}
