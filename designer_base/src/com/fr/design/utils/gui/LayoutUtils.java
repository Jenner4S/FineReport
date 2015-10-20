/*
 * Copyright(c) 2001-2011, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.utils.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;

/**
 * Created by IntelliJ IDEA.
 * User   : Richer
 * Version: 6.5.5
 * Date   : 11-7-19
 * Time   : ����10:17
 */
public class LayoutUtils {

    /**
     * ��Ҫ���ڲ��ֹ������ı�ʱ����������ߴ类��ק �����仯ʱ���²��֡�
     */
	
    private LayoutUtils() {

    }

	/**
     * ���Ե�ǰ�������²��֣��������丸����
     */
    public static void layoutContainer(Container container) {
        _layoutContainer(container);
    }

	/**
	 * ���������ϲ��������ݹ���������ƽ���ĸ��¡�
	 */
	public static void layoutRootContainer(Component comp) {
		Container parentContainer = ((comp instanceof Container) ? (Container) comp : comp.getParent());
		if (parentContainer != null) {
			_layoutContainer(getTopContainer(parentContainer));
		}
	}
    
    /**
     * ��container��ʼ���ݵ���һ��û�в��ֹ��������������
     */
    public static Container getTopContainer(Container container) {
        Container parent = container.getParent();

        if (parent == null) {
            return container;
        }

        LayoutManager layout = parent.getLayout();

        if (layout == null) {
            return container;
        }

        return getTopContainer(parent);
    }

    /**
     * �ݹ鷽ʽ����������container��ʼ������
     */
    private static void _layoutContainer(Container container) {
        LayoutManager layout = container.getLayout();
        if (layout != null) {
            container.doLayout();
        }

        int count = container.getComponentCount();

        for (int i = 0; i < count; i++) {
            Component child = container.getComponent(i);
            if (child instanceof Container) {
                _layoutContainer((Container) child);
            }
        }
    }
}
