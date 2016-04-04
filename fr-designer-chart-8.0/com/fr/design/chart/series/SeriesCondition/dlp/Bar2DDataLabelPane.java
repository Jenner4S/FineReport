// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.dlp;

import com.fr.chart.base.AttrContents;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.chart.series.SeriesCondition.dlp:
//            DataLabelPane

public class Bar2DDataLabelPane extends DataLabelPane
{

    private static final long serialVersionUID = 0xec40ab4beebace13L;
    private UIRadioButton insideButton;
    private UIRadioButton outSideButton;

    public Bar2DDataLabelPane()
    {
    }

    protected JPanel createJPanel4Position()
    {
        centerButton = new UIRadioButton(Inter.getLocText("FR-Designer-StyleAlignment_Center"));
        insideButton = new UIRadioButton(Inter.getLocText("FR-Chart_DataLabelInside"));
        outSideButton = new UIRadioButton(Inter.getLocText("FR-Chart_DataLabelOutSide"));
        ButtonGroup buttongroup = new ButtonGroup();
        buttongroup.add(insideButton);
        buttongroup.add(outSideButton);
        buttongroup.add(centerButton);
        outSideButton.setSelected(true);
        JPanel jpanel = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
            "Label", "Layout"
        })).append(":").toString()));
        jpanel.add(outSideButton);
        jpanel.add(insideButton);
        jpanel.add(centerButton);
        return jpanel;
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
        else
        if(centerButton != null)
            centerButton.setSelected(true);
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
