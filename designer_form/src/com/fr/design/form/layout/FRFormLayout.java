/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.form.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

/**
 * @author richer
 * @since 6.5.3
 * �����֣��������ɵ��������λ�õ�������������֮������ཻ�����
 */
public class FRFormLayout implements FRLayoutManager, java.io.Serializable {
    public FRFormLayout() {
        
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return parent.getPreferredSize();
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return parent.getMinimumSize();
    }

    @Override
    public void layoutContainer(Container parent) {
        
    }
}
