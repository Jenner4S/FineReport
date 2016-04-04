// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.write.submit;

import com.fr.design.mainframe.ElementCasePane;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

// Referenced classes of package com.fr.design.write.submit:
//            SmartInsertDBManipulationPane

public class SmartInsertDBManipulationInWidgetEventPane extends SmartInsertDBManipulationPane
{

    public SmartInsertDBManipulationInWidgetEventPane(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
    }

    protected void setBorderAndLayout(JPanel jpanel)
    {
        jpanel.setLayout(new FlowLayout(0));
    }

    protected void addComponent(JPanel jpanel, JScrollPane jscrollpane)
    {
    }

    protected Dimension createConditionPanePreferredSize()
    {
        return new Dimension(454, 30);
    }

    protected Dimension createControlBtnPanePreferredSize()
    {
        return new Dimension(92, 20);
    }

    protected String setControlBtnPanePosition()
    {
        return "West";
    }
}
