// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.design.dialog.MultiTabPane;
import com.fr.design.gui.ibutton.UITabGroup;
import com.fr.design.mainframe.chart.gui.style.legend.AutoSelectedPane;
import com.fr.general.ComparatorUtils;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public abstract class ChartTabPane extends MultiTabPane
{

    private static final long serialVersionUID = 0x77cff28ad3e36a28L;

    public ChartTabPane()
    {
    }

    protected void initLayout()
    {
        JPanel jpanel = new JPanel(new BorderLayout());
        jpanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 10, getBackground()));
        jpanel.add(tabPane, "Center");
        setLayout(new BorderLayout(0, 4));
        add(jpanel, "North");
        add(centerPane, "Center");
    }

    public boolean accept(Object obj)
    {
        return false;
    }

    public String title4PopupWindow()
    {
        return "";
    }

    public void reset()
    {
    }

    public transient void setSelectedByIds(int i, String as[])
    {
        tabPane.setSelectedIndex(-1);
        int j = 0;
        do
        {
            if(j >= paneList.size())
                break;
            if(ComparatorUtils.equals(as[i], NameArray[j]))
            {
                tabPane.setSelectedIndex(j);
                tabPane.tabChanged(j);
                if(as.length >= i + 2)
                    ((AutoSelectedPane)paneList.get(j)).setSelectedIndex(as[i + 1]);
                break;
            }
            j++;
        } while(true);
    }
}
