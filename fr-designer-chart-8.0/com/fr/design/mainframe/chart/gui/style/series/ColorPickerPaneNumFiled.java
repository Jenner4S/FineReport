// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.design.gui.itextfield.UINumberField;
import java.util.Timer;
import java.util.TimerTask;

public class ColorPickerPaneNumFiled extends UINumberField
{

    private Timer timer;

    public ColorPickerPaneNumFiled()
    {
    }

    protected void attributeChange()
    {
        if(timer != null)
            timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {

            final ColorPickerPaneNumFiled this$0;

            public void run()
            {
                runChange();
            }

            
            {
                this$0 = ColorPickerPaneNumFiled.this;
                super();
            }
        }
, 500L);
    }

    protected void runChange()
    {
        super.attributeChange();
        timer.cancel();
    }
}
