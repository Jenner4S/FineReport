// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.axis;

import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.RadarAxis;
import com.fr.design.chart.axis.MinMaxValuePane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.axis:
//            ChartValuePane

public class ChartRadarPane extends ChartValuePane
{

    private UIButtonGroup allMaxMin;

    public ChartRadarPane()
    {
    }

    protected JPanel initMinMaxValue()
    {
        JPanel jpanel = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        JPanel jpanel1 = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        jpanel.add(jpanel1);
        String as[] = {
            Inter.getLocText("Chart_Axis_AutoCount"), Inter.getLocText("Chart_Axis_UnitCount")
        };
        Boolean aboolean[] = {
            Boolean.FALSE, Boolean.TRUE
        };
        allMaxMin = new UIButtonGroup(as, aboolean);
        jpanel1.add(allMaxMin);
        allMaxMin.setSelectedItem(Boolean.valueOf(true));
        allMaxMin.addActionListener(new ActionListener() {

            final ChartRadarPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                checkMinMax();
            }

            
            {
                this$0 = ChartRadarPane.this;
                super();
            }
        }
);
        minValuePane = new MinMaxValuePane();
        jpanel.add(minValuePane);
        return jpanel;
    }

    private void checkMinMax()
    {
        if(minValuePane != null && allMaxMin != null)
            minValuePane.setPaneEditable(((Boolean)allMaxMin.getSelectedItem()).booleanValue());
    }

    protected JPanel addAlertPane()
    {
        return null;
    }

    protected JPanel getAxisTitlePane()
    {
        return null;
    }

    public void populateBean(Axis axis)
    {
        if(axis instanceof RadarAxis)
        {
            RadarAxis radaraxis = (RadarAxis)axis;
            allMaxMin.setSelectedItem(Boolean.valueOf(radaraxis.isAllMaxMin()));
        }
        super.populateBean(axis);
        checkMinMax();
    }

    public void updateBean(Axis axis)
    {
        super.updateBean(axis);
        if(axis instanceof RadarAxis)
        {
            RadarAxis radaraxis = (RadarAxis)axis;
            radaraxis.setAllMaxMin(((Boolean)allMaxMin.getSelectedItem()).booleanValue());
        }
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
