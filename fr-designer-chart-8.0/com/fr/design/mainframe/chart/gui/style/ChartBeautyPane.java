// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;

public class ChartBeautyPane extends BasicBeanPane
{

    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private UIComboBox styleBox;

    public ChartBeautyPane()
    {
        String as[] = {
            Inter.getLocText("Common"), Inter.getLocText("Plane3D"), Inter.getLocText(new String[] {
                "Gradient", "HighLight"
            }), Inter.getLocText("TopDownShade"), Inter.getLocText("Transparent")
        };
        styleBox = new UIComboBox(as);
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d
        };
        Component acomponent[][] = {
            {
                styleBox
            }
        };
        javax.swing.JPanel jpanel = TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
            "InterfaceStyle"
        }, acomponent, ad1, ad);
        setLayout(new BorderLayout());
        add(jpanel, "Center");
    }

    public void populateBean(Integer integer)
    {
        byte byte0 = 0;
        switch(integer.intValue())
        {
        case 0: // '\0'
            byte0 = 0;
            break;

        case 1: // '\001'
            byte0 = 1;
            break;

        case 2: // '\002'
            byte0 = 2;
            break;

        case 3: // '\003'
            byte0 = 0;
            break;

        case 4: // '\004'
            byte0 = 3;
            break;

        case 5: // '\005'
            byte0 = 4;
            break;

        default:
            byte0 = 0;
            break;
        }
        if(byte0 != styleBox.getSelectedIndex())
            styleBox.setSelectedIndex(byte0);
    }

    public Integer updateBean()
    {
        int i = styleBox.getSelectedIndex();
        byte byte0 = 0;
        switch(i)
        {
        case 0: // '\0'
            byte0 = 0;
            break;

        case 1: // '\001'
            byte0 = 1;
            break;

        case 2: // '\002'
            byte0 = 2;
            break;

        case 3: // '\003'
            byte0 = 4;
            break;

        case 4: // '\004'
            byte0 = 5;
            break;

        default:
            byte0 = 0;
            break;
        }
        return Integer.valueOf(byte0);
    }

    protected String title4PopupWindow()
    {
        return "";
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Integer)obj);
    }
}
