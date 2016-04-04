// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.dlp;

import com.fr.chart.base.AttrContents;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

// Referenced classes of package com.fr.design.chart.series.SeriesCondition.dlp:
//            DataLabelPane

public class PieDataLabelPane extends DataLabelPane
{

    private JRadioButton insideButton;
    private JRadioButton outSideButton;
    private JCheckBox showGuidLine;

    public PieDataLabelPane()
    {
    }

    protected Component[] createComponents4ShowGuidLine()
    {
        if(showGuidLine == null)
            showGuidLine = new JCheckBox(Inter.getLocText("ChartF-Show_GuidLine"));
        return (new Component[] {
            null, showGuidLine
        });
    }

    private ActionListener getPositionListener()
    {
        return new ActionListener() {

            final PieDataLabelPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                checkGuidBox();
            }

            
            {
                this$0 = PieDataLabelPane.this;
                super();
            }
        }
;
    }

    public void checkGuidBox()
    {
        if(insideButton != null && insideButton.isSelected())
        {
            showGuidLine.setSelected(false);
            showGuidLine.setEnabled(false);
        }
        if(outSideButton != null && outSideButton.isSelected())
        {
            showGuidLine.setEnabled(true);
            showGuidLine.setSelected(true);
        }
    }

    protected JPanel createJPanel4Position()
    {
        insideButton = new JRadioButton(Inter.getLocText("Chart_In_Pie"));
        outSideButton = new JRadioButton(Inter.getLocText("Chart_Out_Pie"));
        outSideButton.addActionListener(getPositionListener());
        insideButton.addActionListener(getPositionListener());
        showPercent.addActionListener(getPieLeadActionListener());
        showSeriesNameCB.addActionListener(getPieLeadActionListener());
        showCategoryNameCB.addActionListener(getPieLeadActionListener());
        showValueCB.addActionListener(getPieLeadActionListener());
        ButtonGroup buttongroup = new ButtonGroup();
        buttongroup.add(insideButton);
        buttongroup.add(outSideButton);
        insideButton.setSelected(true);
        JPanel jpanel = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
            "Label", "Layout"
        })).append(":").toString()));
        jpanel.add(insideButton);
        jpanel.add(outSideButton);
        return jpanel;
    }

    private ActionListener getPieLeadActionListener()
    {
        return new ActionListener() {

            final PieDataLabelPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                checkShowLineBox();
            }

            
            {
                this$0 = PieDataLabelPane.this;
                super();
            }
        }
;
    }

    private void checkShowLineBox()
    {
        if(showGuidLine != null)
            showGuidLine.setEnabled(showCategoryNameCB.isSelected() || showSeriesNameCB.isSelected() || showValueCB.isSelected() || showPercent.isSelected());
    }

    public void populate(AttrContents attrcontents)
    {
        super.populate(attrcontents);
        if(showGuidLine != null)
        {
            showGuidLine.setSelected(attrcontents.isShowGuidLine());
            showGuidLine.setEnabled(showSeriesNameCB.isSelected() || showCategoryNameCB.isSelected() || showValueCB.isSelected() || showPercent.isSelected());
        }
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
        if(showGuidLine != null && showGuidLine.isEnabled())
            attrcontents.setShowGuidLine(showGuidLine.isSelected());
        if(insideButton != null && insideButton.isSelected())
            attrcontents.setPosition(5);
        else
        if(outSideButton != null && outSideButton.isSelected())
            attrcontents.setPosition(6);
    }

}
