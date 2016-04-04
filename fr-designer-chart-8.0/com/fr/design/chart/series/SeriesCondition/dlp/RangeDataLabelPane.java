// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.dlp;

import com.fr.chart.base.AttrContents;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import javax.swing.*;

// Referenced classes of package com.fr.design.chart.series.SeriesCondition.dlp:
//            DataLabelPane

public class RangeDataLabelPane extends DataLabelPane
{

    private JRadioButton topButton;
    private JRadioButton centerButton;
    private JRadioButton bottomButton;

    public RangeDataLabelPane()
    {
    }

    protected JPanel createJPanel4Position()
    {
        topButton = new JRadioButton(Inter.getLocText("StyleAlignment-Top"));
        bottomButton = new JRadioButton(Inter.getLocText("StyleAlignment-Bottom"));
        centerButton = new JRadioButton(Inter.getLocText("Center"));
        ButtonGroup buttongroup = new ButtonGroup();
        buttongroup.add(topButton);
        buttongroup.add(bottomButton);
        buttongroup.add(centerButton);
        topButton.setSelected(true);
        JPanel jpanel = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
            "Label", "Layout"
        })).append(":").toString()));
        jpanel.add(topButton);
        jpanel.add(bottomButton);
        jpanel.add(centerButton);
        return jpanel;
    }

    public void populate(AttrContents attrcontents)
    {
        super.populate(attrcontents);
        int i = attrcontents.getPosition();
        if(centerButton != null && i == 0)
            centerButton.setSelected(true);
        else
        if(bottomButton != null && i == 3)
            bottomButton.setSelected(true);
        else
        if(topButton != null)
            topButton.setSelected(true);
    }

    public void update(AttrContents attrcontents)
    {
        super.update(attrcontents);
        if(centerButton != null && centerButton.isSelected())
            attrcontents.setPosition(0);
        else
        if(bottomButton != null && bottomButton.isSelected())
            attrcontents.setPosition(3);
        else
            attrcontents.setPosition(1);
    }
}
