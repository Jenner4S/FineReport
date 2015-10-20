/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.cell.bar;

/**
 * @author richer
 * @since 6.5.3
 * �������������������ĵ�ǰֵ����ȡ���Сֵ�����ֵ�İ�����
 */
public abstract class BarHelper {

    public abstract double getVisibleSize();

    public abstract int getExtent(int currentValue);
    
    public abstract void resetBeginValue(int newValue);

    public abstract void resetExtentValue(int extentValue);
}
