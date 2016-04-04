// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans;


public class Incremental
{

    public int top;
    public int left;
    public int bottom;
    public int right;

    public Incremental()
    {
        this(0, 0, 0, 0);
    }

    public Incremental(int i, int j, int k, int l)
    {
        top = 0;
        left = 0;
        bottom = 0;
        right = 0;
        top = i;
        left = j;
        bottom = k;
        right = l;
    }
}
