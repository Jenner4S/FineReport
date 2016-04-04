// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans;

import java.awt.Graphics;
import java.awt.Rectangle;

public interface Painter
{

    public abstract void setRenderingBounds(Rectangle rectangle);

    public abstract void paint(Graphics g, int i, int j);
}
