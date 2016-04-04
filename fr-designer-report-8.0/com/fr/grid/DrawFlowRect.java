// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid;

import com.fr.base.FRContext;
import com.fr.general.FRLogger;
import java.awt.Color;
import java.awt.Graphics;

// Referenced classes of package com.fr.grid:
//            Grid

public class DrawFlowRect
    implements Runnable
{

    private int templen;
    private int lenReal;
    private boolean isReal;
    private Grid grid;
    private boolean running;

    public DrawFlowRect()
    {
        templen = 0;
        lenReal = 0;
        isReal = false;
        running = true;
        (new Thread(this, "drawFlowLine")).start();
    }

    public void setGrid(Grid grid1)
    {
        grid = grid1;
    }

    public void run()
    {
        while(running) 
        {
            templen = templen + 1;
            if(templen >= 4)
            {
                templen = 0;
                isReal = !isReal;
            }
            if(grid != null && !grid.IsNotShowingTableSelectPane())
                grid.repaint();
            try
            {
                Thread.sleep(25L);
            }
            catch(InterruptedException interruptedexception)
            {
                FRContext.getLogger().error(interruptedexception.getMessage(), interruptedexception);
            }
        }
    }

    public void drawFlowRect(Graphics g, int i, int j, int k, int l)
    {
        Color color = g.getColor();
        lenReal = templen;
        boolean flag = isReal;
        int i1 = lenReal;
        drawTopLine(g, i, j, k, flag, i1);
        drawRightLine(g, i, j, k, l, flag, i1);
        drawLeftLine(g, i, j, l, flag, i1);
        drawBottomLine(g, i, j, k, l, flag, i1);
        isReal = flag;
        g.setColor(color);
    }

    private void drawTopLine(Graphics g, int i, int j, int k, boolean flag, int l)
    {
        g.setColor(Color.black);
        g.drawLine(i, j, k, j);
        g.drawLine(i - 1, j - 1, k, j - 1);
        g.setColor(Color.white);
        drawFlowLine(g, i, j, k, j);
        isReal = flag;
        lenReal = l;
        drawFlowLine(g, i - 1, j - 1, k, j - 1);
    }

    private void drawRightLine(Graphics g, int i, int j, int k, int l, boolean flag, int i1)
    {
        g.setColor(Color.black);
        g.drawLine(k, j, k, l);
        g.drawLine(k - 1, j - 1, k - 1, l);
        g.setColor(Color.white);
        isReal = flag;
        lenReal = i1;
        drawFlowLine(g, k, j, k, l);
        isReal = flag;
        lenReal = i1;
        drawFlowLine(g, k - 1, j - 1, k - 1, l);
    }

    private void drawLeftLine(Graphics g, int i, int j, int k, boolean flag, int l)
    {
        g.setColor(Color.black);
        g.drawLine(i, k, i, j);
        g.drawLine(i + 1, k - 1, i + 1, j);
        g.setColor(Color.white);
        isReal = flag;
        lenReal = l;
        drawFlowLine(g, i, k, i, j);
        isReal = flag;
        lenReal = l;
        drawFlowLine(g, i + 1, k + 1, i + 1, j);
    }

    private void drawBottomLine(Graphics g, int i, int j, int k, int l, boolean flag, int i1)
    {
        g.setColor(Color.black);
        g.drawLine(k, l, i, l);
        g.drawLine(k + 1, l + 1, i, l + 1);
        g.setColor(Color.white);
        isReal = flag;
        lenReal = i1;
        drawFlowLine(g, k, l, i, l);
        isReal = flag;
        lenReal = i1;
        drawFlowLine(g, k + 1, l + 1, i, l + 1);
    }

    private void drawFlowLine(Graphics g, int i, int j, int k, int l)
    {
        int i1 = i;
        int j1 = j;
        byte byte0 = 4;
        byte byte1 = 5;
        int k1 = 0;
        int l1 = 0;
        byte byte2 = 0;
        byte byte3 = 0;
        int i2 = 1;
        boolean flag = true;
        if(k - i != 0)
            byte2 = ((byte)(i >= k ? -1 : 1));
        else
            byte3 = ((byte)(j >= l ? -1 : 1));
        if(isReal)
        {
            k1 = lenReal * byte2 + i;
            l1 = lenReal * byte3 + j;
            g.drawLine(i1, j1, k1, l1);
            i1 = (byte1 + lenReal) * byte2 + i;
            j1 = (byte1 + lenReal) * byte3 + j;
        } else
        {
            i1 = lenReal * byte2 + i;
            j1 = lenReal * byte3 + j;
        }
        int j2 = i1;
        int k2 = j1;
        drawTheRestPart(g, i1, j1, i, j, k, l, byte1, byte0, j2, k2, byte2, byte3, k1, l1, flag, i2);
    }

    private void drawTheRestPart(Graphics g, int i, int j, int k, int l, int i1, int j1, 
            int k1, int l1, int i2, int j2, int k2, int l2, int i3, 
            int j3, boolean flag, int k3)
    {
        do
        {
            i3 = (k3 * (l1 + k1) - k1) * k2 + i2;
            j3 = (k3 * (l1 + k1) - k1) * l2 + j2;
            if(Math.abs(i3 - k) > Math.abs(i1 - k))
            {
                lenReal = Math.abs(i3 - i1);
                isReal = true;
                i3 = i1;
                flag = false;
            }
            if(Math.abs(j3 - l) > Math.abs(j1 - l))
            {
                lenReal = Math.abs(j3 - j1);
                isReal = true;
                j3 = j1;
                flag = false;
            }
            g.drawLine(i, j, i3, j3);
            i = k3 * (l1 + k1) * k2 + i2;
            j = k3 * (l1 + k1) * l2 + j2;
            if(i > Math.max(k, i1) || j > Math.max(l, j1))
            {
                if(flag)
                {
                    isReal = false;
                    if(i > Math.max(k, i1))
                        lenReal = i - Math.max(k, i1);
                    else
                        lenReal = j - Math.max(l, j1);
                }
                break;
            }
            k3++;
        } while(flag);
    }

    public void exit()
    {
        running = false;
    }
}
