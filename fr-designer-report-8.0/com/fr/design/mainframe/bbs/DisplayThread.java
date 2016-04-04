// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.bbs;

import org.eclipse.swt.widgets.Display;

public class DisplayThread extends Thread
{

    private Display display;
    Object sem;

    public DisplayThread()
    {
        sem = new Object();
    }

    public void run()
    {
        synchronized(sem)
        {
            display = Display.getDefault();
            sem.notifyAll();
        }
        swtEventLoop();
    }

    private void swtEventLoop()
    {
        do
            try
            {
                while(display.readAndDispatch()) ;
                display.sleep();
            }
            catch(Exception exception) { }
        while(true);
    }

    public Display getDisplay()
    {
        Object obj = sem;
        JVM INSTR monitorenter ;
        while(display == null) 
            sem.wait();
        return display;
        Exception exception1;
        exception1;
        throw exception1;
        Exception exception;
        exception;
        return null;
    }
}
