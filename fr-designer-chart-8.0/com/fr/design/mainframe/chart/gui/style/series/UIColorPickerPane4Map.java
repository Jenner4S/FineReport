// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.base.ChartConstants;
import com.fr.design.style.color.*;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.*;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            UIColorPickerPane

public class UIColorPickerPane4Map extends UIColorPickerPane
{
    private class ColorSelectBox4Map extends ColorSelectBox
    {

        final UIColorPickerPane4Map this$0;

        protected ColorSelectPane getColorSelectPane()
        {
            return new ColorSelectPane4Map();
        }

        public ColorSelectBox4Map(int i)
        {
            this$0 = UIColorPickerPane4Map.this;
            super(i);
        }
    }

    private class ColorSelectPane4Map extends ColorSelectPane
    {

        final UIColorPickerPane4Map this$0;

        public void initCenterPaneChildren(JPanel jpanel)
        {
            JPanel jpanel1 = new JPanel();
            jpanel.add(jpanel1);
            jpanel1.setLayout(new GridLayout(5, 8, 5, 5));
            for(int i = 0; i < ChartConstants.MAP_COLOR_ARRAY.length; i++)
                jpanel1.add(new ColorCell(ChartConstants.MAP_COLOR_ARRAY[i], this));

            jpanel.add(Box.createVerticalStrut(5));
            jpanel.add(new JSeparator());
        }

        protected Color[] getColorArray()
        {
            return ChartConstants.MAP_COLOR_ARRAY;
        }

        private ColorSelectPane4Map()
        {
            this$0 = UIColorPickerPane4Map.this;
            super();
        }

    }


    public UIColorPickerPane4Map()
    {
    }

    protected ColorSelectBox getColorSelectBox()
    {
        return new ColorSelectBox4Map(100);
    }
}
