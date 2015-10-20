/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.form.layout;

import java.awt.CardLayout;
import java.awt.Container;

import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.form.ui.container.WLayout;
import com.fr.general.ComparatorUtils;

/**
 * @author richer
 * @since 6.5.3
 */
public class FRCardLayout extends CardLayout implements FRLayoutManager{

    public FRCardLayout() {
        super();
    }

    public FRCardLayout(int hgap, int vgap) {
        super(hgap, vgap);
    }

    /**
     * �Ƿ�����ô�С
     * 
     * @return �Ƿ�����ô�С
     * 
     */
    @Override
    public boolean isResizable() {
        return false;
    }
    
    /**
     * չ�ֵ�ǰcard
     * 
     * @param parent ������
     * @param name ��ǰcard
     * 
     */
    public void show(Container parent, String name) {
	synchronized (parent.getTreeLock()) {
            int ncomponents = parent.getComponentCount();
            for (int i = 0; i < ncomponents; i++) {
            	XLayoutContainer container = (XLayoutContainer) parent.getComponent(i);
            	WLayout layout = container.toData();
                if (ComparatorUtils.equals(layout.getWidgetName(), name)) {
                    container.setVisible(true);
                    continue;
                }
                container.setVisible(false);
            }
		}
    }
}
