// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.comp;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;
import java.awt.*;

public class BorderAttriPane extends BasicPane
{

    private LineComboBox lineCombo;
    private ColorSelectBox colorSelectBox;

    public BorderAttriPane()
    {
        this(Inter.getLocText("Line-Style"), Inter.getLocText("Color"));
    }

    public BorderAttriPane(String s, String s1)
    {
        setLayout(new FlowLayout(0, 2, 0));
        add(new UILabel((new StringBuilder()).append(s).append(":").toString()));
        add(lineCombo = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART));
        lineCombo.setPreferredSize(new Dimension(60, 18));
        add(new UILabel((new StringBuilder()).append(s1).append(":").toString()));
        add(colorSelectBox = new ColorSelectBox(80));
        colorSelectBox.setPreferredSize(new Dimension(60, 18));
        colorSelectBox.setSelectObject(null);
    }

    public void setLineColor(Color color)
    {
        colorSelectBox.setSelectObject(color);
    }

    public Color getLineColor()
    {
        return colorSelectBox.getSelectObject();
    }

    public void setLineStyle(int i)
    {
        if(i != 0 && i != 5 && i != 1 && i != 2)
            i = 1;
        lineCombo.setSelectedLineStyle(i);
    }

    public int getLineStyle()
    {
        return lineCombo.getSelectedLineStyle();
    }

    protected String title4PopupWindow()
    {
        return "Border";
    }
}
