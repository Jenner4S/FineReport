// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart;


public class FactoryObject
{

    private Class axisPaneClass;
    private Class dataLabelPaneClass;
    public static FactoryObject EMPTY = new FactoryObject();

    public FactoryObject()
    {
    }

    public Class getAxisPaneClass()
    {
        return axisPaneClass;
    }

    public FactoryObject setAxisPaneCls(Class class1)
    {
        axisPaneClass = class1;
        return this;
    }

    public Class getDataLabelPaneClass()
    {
        return dataLabelPaneClass;
    }

    public FactoryObject setDataLabelPaneClass(Class class1)
    {
        dataLabelPaneClass = class1;
        return this;
    }

}
