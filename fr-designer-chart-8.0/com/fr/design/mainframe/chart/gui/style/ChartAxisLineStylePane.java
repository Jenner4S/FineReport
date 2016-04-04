// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style;

import com.fr.base.BaseUtils;
import com.fr.chart.chartattr.Axis;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Icon;

public class ChartAxisLineStylePane extends BasicPane
{

    private static final long serialVersionUID = 0xd9d3fcbee02356bcL;
    private LineComboBox axisLineStyle;
    private ColorSelectBox axisLineColor;
    private UIButtonGroup mainTickPosition;
    private UIButtonGroup secondTickPosition;

    public ChartAxisLineStylePane()
    {
        initComponents();
    }

    private void initComponents()
    {
        axisLineStyle = new LineComboBox(CoreConstants.LINE_STYLE_ARRAY_4_AXIS);
        axisLineColor = new ColorSelectBox(100);
        String as[] = {
            Inter.getLocText("External"), Inter.getLocText("Inside"), Inter.getLocText("ChartF-Cross"), Inter.getLocText("None")
        };
        Integer ainteger[] = {
            Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(3), Integer.valueOf(0)
        };
        Icon aicon[] = {
            BaseUtils.readIcon("/com/fr/design/images/chart/ChartAxisLineStyle/external.png"), BaseUtils.readIcon("/com/fr/design/images/chart/ChartAxisLineStyle/inside.png"), BaseUtils.readIcon("/com/fr/design/images/chart/ChartAxisLineStyle/cross.png"), BaseUtils.readIcon("/com/fr/design/images/expand/none16x16.png")
        };
        mainTickPosition = new UIButtonGroup(aicon, ainteger);
        mainTickPosition.setAllToolTips(as);
        secondTickPosition = new UIButtonGroup(aicon, ainteger);
        secondTickPosition.setAllToolTips(as);
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d, d, d
        };
        Component acomponent[][] = {
            {
                new UILabel(Inter.getLocText("Type")), axisLineStyle
            }, {
                new UILabel(Inter.getLocText("Color")), axisLineColor
            }, {
                new UILabel(Inter.getLocText("MainGraduationLine")), null
            }, {
                null, mainTickPosition
            }, {
                new UILabel(Inter.getLocText("SecondGraduationLine")), null
            }, {
                null, secondTickPosition
            }
        };
        javax.swing.JPanel jpanel = TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
            "Axis", "Style"
        }, acomponent, ad1, ad);
        setLayout(new BorderLayout());
        add(jpanel, "Center");
    }

    protected String title4PopupWindow()
    {
        return null;
    }

    public void populate(Axis axis)
    {
        axisLineStyle.setSelectedLineStyle(axis.getAxisStyle());
        axisLineColor.setSelectObject(axis.getAxisColor());
        mainTickPosition.setSelectedItem(Integer.valueOf(axis.getTickMarkType()));
        secondTickPosition.setSelectedItem(Integer.valueOf(axis.getSecTickMarkType()));
    }

    public void update(Axis axis)
    {
        axis.setAxisStyle(axisLineStyle.getSelectedLineStyle());
        axis.setAxisColor(axisLineColor.getSelectObject());
        if(mainTickPosition.getSelectedItem() != null)
            axis.setTickMarkType(((Integer)mainTickPosition.getSelectedItem()).intValue());
        if(secondTickPosition.getSelectedItem() != null)
            axis.setSecTickMarkType(((Integer)secondTickPosition.getSelectedItem()).intValue());
    }
}
