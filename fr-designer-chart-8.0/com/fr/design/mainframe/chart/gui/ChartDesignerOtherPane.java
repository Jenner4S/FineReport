// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui;

import com.fr.design.mainframe.chart.gui.other.ChartDesignerConditionAttrPane;
import com.fr.design.mainframe.chart.gui.other.ChartDesignerInteractivePane;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui:
//            ChartOtherPane

public class ChartDesignerOtherPane extends ChartOtherPane
{
    private class ChartTabPane extends ChartOtherPane.TabPane
    {

        final ChartDesignerOtherPane this$0;

        protected java.util.List initPaneList()
        {
            ArrayList arraylist = new ArrayList();
            interactivePane = new ChartDesignerInteractivePane(ChartDesignerOtherPane.this);
            arraylist.add(interactivePane);
            if(isHaveCondition())
            {
                conditionAttrPane = new ChartDesignerConditionAttrPane();
                arraylist.add(conditionAttrPane);
            }
            return arraylist;
        }

        private ChartTabPane()
        {
            this$0 = ChartDesignerOtherPane.this;
            super(ChartDesignerOtherPane.this);
        }

    }


    public ChartDesignerOtherPane()
    {
    }

    protected JPanel createContentPane()
    {
        JPanel jpanel = new JPanel(new BorderLayout());
        otherPane = new ChartTabPane();
        jpanel.add(otherPane, "Center");
        return jpanel;
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("Advanced");
    }

    private boolean isHaveCondition()
    {
        return hasCondition;
    }

}
