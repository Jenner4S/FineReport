// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly.model;

import com.fr.poly.creator.BlockCreator;
import java.awt.Point;
import java.awt.event.MouseEvent;

public class AddingData
{

    private BlockCreator creator;
    private int current_x;
    private int current_y;

    public AddingData(BlockCreator blockcreator)
    {
        creator = blockcreator;
        current_x = -creator.getWidth();
        current_y = -creator.getHeight();
    }

    public void reset()
    {
        current_x = -creator.getWidth();
        current_y = -creator.getHeight();
    }

    public int getCurrentX()
    {
        return current_x;
    }

    public int getCurrentY()
    {
        return current_y;
    }

    public void moveTo(MouseEvent mouseevent)
    {
        current_x = mouseevent.getX() - creator.getWidth() / 2;
        current_y = mouseevent.getY() - creator.getHeight() / 2;
    }

    public void moveTo(int i, int j)
    {
        current_x = i - creator.getWidth() / 2;
        current_y = j - creator.getHeight() / 2;
    }

    public void moveTo(Point point)
    {
        current_x = point.x - creator.getWidth() / 2;
        current_y = point.y - creator.getHeight() / 2;
    }

    public BlockCreator getCreator()
    {
        return creator;
    }
}
