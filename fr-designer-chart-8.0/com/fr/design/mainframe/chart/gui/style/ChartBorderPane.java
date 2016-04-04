// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style;

import com.fr.chart.chartglyph.GeneralInfo;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JSeparator;

public class ChartBorderPane extends BasicPane
{

    private static final long serialVersionUID = 0x942b4f49f7c46208L;
    private LineComboBox currentLineCombo;
    private ColorSelectBox currentLineColorPane;
    private UICheckBox isRoundBorder;

    public ChartBorderPane()
    {
        initComponents();
    }

    private void initComponents()
    {
        currentLineCombo = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
        currentLineColorPane = new ColorSelectBox(100);
        isRoundBorder = new UICheckBox(Inter.getLocText("Border-Style-Radius"));
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d
        };
        Component acomponent[][] = {
            {
                new UILabel((new StringBuilder()).append(Inter.getLocText("Line-Style")).append(":").toString()), currentLineCombo
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("Color")).append(":").toString()), currentLineColorPane
            }, {
                null, isRoundBorder
            }
        };
        javax.swing.JPanel jpanel = TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
            "Border"
        }, acomponent, ad1, ad);
        setLayout(new BorderLayout());
        add(jpanel, "Center");
        add(new JSeparator(), "South");
    }

    public String title4PopupWindow()
    {
        return null;
    }

    public void populate(GeneralInfo generalinfo)
    {
        if(generalinfo == null)
        {
            return;
        } else
        {
            currentLineCombo.setSelectedLineStyle(generalinfo.getBorderStyle());
            currentLineColorPane.setSelectObject(generalinfo.getBorderColor());
            isRoundBorder.setSelected(generalinfo.isRoundBorder());
            return;
        }
    }

    public void update(GeneralInfo generalinfo)
    {
        if(generalinfo == null)
            generalinfo = new GeneralInfo();
        generalinfo.setBorderStyle(currentLineCombo.getSelectedLineStyle());
        generalinfo.setBorderColor(currentLineColorPane.getSelectObject());
        generalinfo.setRoundBorder(isRoundBorder.isSelected());
    }
}
