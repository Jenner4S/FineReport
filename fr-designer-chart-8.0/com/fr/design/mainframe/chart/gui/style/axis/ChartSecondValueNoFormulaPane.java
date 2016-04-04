// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.axis;

import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.ValueAxis;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.general.Inter;
import java.awt.FlowLayout;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.axis:
//            ChartValueNoFormulaPane

public class ChartSecondValueNoFormulaPane extends ChartValueNoFormulaPane
{

    private UICheckBox isAlignZeroValue;

    public ChartSecondValueNoFormulaPane()
    {
    }

    protected JPanel aliagnZero4Second()
    {
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new FlowLayout(2));
        jpanel.add(isAlignZeroValue = new UICheckBox(Inter.getLocText("Chart_AxisAlignZeroValueLine"), false));
        return jpanel;
    }

    public void populateBean(Axis axis)
    {
        if(axis instanceof ValueAxis)
        {
            super.populateBean(axis);
            isAlignZeroValue.setSelected(((ValueAxis)axis).isAlignZeroValue());
        }
    }

    public void updateBean(Axis axis)
    {
        if(axis instanceof ValueAxis)
        {
            ValueAxis valueaxis = (ValueAxis)axis;
            super.updateBean(valueaxis);
            valueaxis.setAlignZeroValue(isAlignZeroValue.isSelected());
        }
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "Second", "Chart_F_Radar_Axis"
        });
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((Axis)obj);
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Axis)obj);
    }
}
