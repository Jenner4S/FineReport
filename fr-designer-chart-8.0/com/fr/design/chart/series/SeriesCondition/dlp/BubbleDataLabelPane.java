// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.dlp;

import com.fr.chart.base.AttrContents;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import java.awt.Component;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.chart.series.SeriesCondition.dlp:
//            DataLabelPane

public class BubbleDataLabelPane extends DataLabelPane
{

    private static final long serialVersionUID = 0x54f5f850ae4a1435L;
    private UIButton insideButton;
    private UIButton outSideButton;

    public BubbleDataLabelPane()
    {
    }

    protected JPanel createJPanel4Position()
    {
        insideButton = new UIButton(Inter.getLocText("Chart_Bubble_Inside"));
        outSideButton = new UIButton(Inter.getLocText("Chart_Bubble_Outside"));
        ButtonGroup buttongroup = new ButtonGroup();
        buttongroup.add(insideButton);
        buttongroup.add(outSideButton);
        outSideButton.setSelected(true);
        JPanel jpanel = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
            "Label", "Layout"
        })).append(":").toString()));
        jpanel.add(outSideButton);
        jpanel.add(insideButton);
        return jpanel;
    }

    protected Component[] createComponents4ShowCategoryName()
    {
        return new Component[0];
    }

    protected Component[] createComponents4PercentValue()
    {
        return new Component[0];
    }

    public void populate(AttrContents attrcontents)
    {
        super.populate(attrcontents);
        int i = attrcontents.getPosition();
        if(insideButton != null && i == 5)
            insideButton.setSelected(true);
        else
        if(outSideButton != null && i == 6)
            outSideButton.setSelected(true);
    }

    public void update(AttrContents attrcontents)
    {
        super.update(attrcontents);
        if(insideButton != null && insideButton.isSelected())
            attrcontents.setPosition(5);
        else
        if(outSideButton != null && outSideButton.isSelected())
            attrcontents.setPosition(6);
    }
}
