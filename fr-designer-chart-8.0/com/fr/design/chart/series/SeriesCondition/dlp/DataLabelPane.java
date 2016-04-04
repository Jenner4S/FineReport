// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.dlp;

import com.fr.base.Utils;
import com.fr.chart.base.AttrContents;
import com.fr.chart.base.ChartConstants;
import com.fr.design.chart.series.SeriesCondition.DataLabelStylePane;
import com.fr.design.chart.series.SeriesCondition.TooltipContentsPane;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

public class DataLabelPane extends TooltipContentsPane
{

    private static final int SPACE = 4;
    protected UICheckBox showSeriesNameCB;
    protected UICheckBox showCategoryNameCB;
    protected UIComboBox delimiterBox;
    private DataLabelStylePane stylePane;
    private UIRadioButton bottomButton;
    private UIRadioButton leftButton;
    private UIRadioButton rigtButton;
    private UIRadioButton topButton;
    protected UIRadioButton centerButton;

    public DataLabelPane()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        contentPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        add(contentPane, "Center");
        contentPane.add(createJPanel4Label());
        contentPane.add(createJPanel4Delimiter());
        JPanel jpanel = createJPanel4FontStyle();
        if(jpanel != null)
            contentPane.add(jpanel);
        JPanel jpanel1 = createJPanel4Position();
        if(jpanel1 != null)
            contentPane.add(jpanel1);
    }

    private JPanel createJPanel4Label()
    {
        return createTableLayoutPane(new Component[][] {
            createComponents4ShowSeriesName(), createComponents4ShowCategoryName(), createComponents4Value(), createComponents4PercentValue(), createComponents4ShowGuidLine()
        });
    }

    protected Component[] createComponents4ShowSeriesName()
    {
        if(showSeriesNameCB == null)
            showSeriesNameCB = new UICheckBox(Inter.getLocText(new String[] {
                "ChartF-Series", "WF-Name"
            }));
        return (new Component[] {
            new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
                "Label", "Include"
            })).append(":").toString()), showSeriesNameCB
        });
    }

    protected Component[] createComponents4ShowCategoryName()
    {
        if(showCategoryNameCB == null)
            showCategoryNameCB = new UICheckBox(Inter.getLocText(new String[] {
                "StyleFormat-Category", "WF-Name"
            }));
        return (new Component[] {
            null, showCategoryNameCB
        });
    }

    protected Component[] createComponents4ShowGuidLine()
    {
        return new Component[0];
    }

    private JPanel createJPanel4Delimiter()
    {
        if(delimiterBox == null)
            delimiterBox = new UIComboBox(ChartConstants.DELIMITERS);
        delimiterBox.setPreferredSize(new Dimension(70, 20));
        JPanel jpanel = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane();
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText("Form-Delimiter")).append(":").toString()));
        jpanel.add(delimiterBox);
        return jpanel;
    }

    private JPanel createJPanel4FontStyle()
    {
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel jpanel1 = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane();
        jpanel.add(jpanel1, "West");
        jpanel1.add(new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
            "Label", "FRFont"
        })).append(":").toString()));
        jpanel.add(stylePane = new DataLabelStylePane(), "Center");
        return jpanel;
    }

    protected JPanel createJPanel4Position()
    {
        bottomButton = new UIRadioButton(Inter.getLocText("StyleAlignment-Bottom"));
        leftButton = new UIRadioButton(Inter.getLocText("StyleAlignment-Left"));
        rigtButton = new UIRadioButton(Inter.getLocText("StyleAlignment-Right"));
        topButton = new UIRadioButton(Inter.getLocText("StyleAlignment-Top"));
        centerButton = new UIRadioButton(Inter.getLocText("Center"));
        ButtonGroup buttongroup = new ButtonGroup();
        buttongroup.add(bottomButton);
        buttongroup.add(leftButton);
        buttongroup.add(rigtButton);
        buttongroup.add(topButton);
        buttongroup.add(centerButton);
        topButton.setSelected(true);
        JPanel jpanel = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
            "Label", "Layout"
        })).append(":").toString()));
        jpanel.add(bottomButton);
        jpanel.add(leftButton);
        jpanel.add(rigtButton);
        jpanel.add(topButton);
        jpanel.add(centerButton);
        return jpanel;
    }

    public void populate(AttrContents attrcontents)
    {
        super.populate(attrcontents);
        String s = attrcontents.getSeriesLabel();
        if(s != null)
        {
            int i = 0;
            do
            {
                if(i >= ChartConstants.DELIMITERS.length)
                    break;
                String s1 = ChartConstants.DELIMITERS[i];
                if(delimiterBox != null && s.contains(s1))
                {
                    delimiterBox.setSelectedItem(s1);
                    break;
                }
                i++;
            } while(true);
            if(delimiterBox != null && s.contains("${BR}"))
                delimiterBox.setSelectedItem(ChartConstants.DELIMITERS[3]);
            if(showCategoryNameCB != null)
                showCategoryNameCB.setSelected(s.contains("${CATEGORY}"));
            if(showSeriesNameCB != null)
                showSeriesNameCB.setSelected(s.contains("${SERIES}"));
        } else
        {
            if(showCategoryNameCB != null)
                showCategoryNameCB.setSelected(false);
            if(showSeriesNameCB != null)
                showSeriesNameCB.setSelected(false);
        }
        int j = attrcontents.getPosition();
        if(bottomButton != null && j == 3)
            bottomButton.setSelected(true);
        else
        if(topButton != null && j == 1)
            topButton.setSelected(true);
        else
        if(rigtButton != null && j == 4)
            rigtButton.setSelected(true);
        else
        if(leftButton != null && j == 2)
            leftButton.setSelected(true);
        else
        if(centerButton != null && j == 0)
            centerButton.setSelected(true);
        if(stylePane != null)
            stylePane.populate(attrcontents);
    }

    public void update(AttrContents attrcontents)
    {
        String s = "";
        String s1 = Utils.objectToString(delimiterBox.getSelectedItem());
        if(s1.contains(ChartConstants.DELIMITERS[3]))
            s1 = "${BR}";
        else
        if(s1.contains(ChartConstants.DELIMITERS[4]))
            s1 = " ";
        if(showCategoryNameCB != null && showCategoryNameCB.isSelected())
            s = (new StringBuilder()).append(s).append("${CATEGORY}").append(s1).toString();
        if(showSeriesNameCB != null && showSeriesNameCB.isSelected())
            s = (new StringBuilder()).append(s).append("${SERIES}").append(s1).toString();
        if(showValueCB != null && showValueCB.isSelected())
            s = (new StringBuilder()).append(s).append("${VALUE}").append(s1).toString();
        if(showPercent != null && showPercent.isSelected())
            s = (new StringBuilder()).append(s).append("${PERCENT}").append(s1).toString();
        if(s.contains(s1))
            s = s.substring(0, s.lastIndexOf(s1));
        if(topButton != null && topButton.isSelected())
            attrcontents.setPosition(1);
        else
        if(leftButton != null && leftButton.isSelected())
            attrcontents.setPosition(2);
        else
        if(bottomButton != null && bottomButton.isSelected())
            attrcontents.setPosition(3);
        else
        if(rigtButton != null && rigtButton.isSelected())
            attrcontents.setPosition(4);
        else
        if(centerButton != null && centerButton.isSelected())
            attrcontents.setPosition(0);
        if(stylePane != null)
            stylePane.update(attrcontents);
        attrcontents.setSeriesLabel(s);
        attrcontents.setFormat(format);
        attrcontents.setPercentFormat(percentFormat);
    }

    public void checkGuidBox()
    {
    }
}
