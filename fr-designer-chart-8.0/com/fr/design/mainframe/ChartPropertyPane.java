// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.gui.ilable.UILabel;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.mainframe:
//            MiddleChartPropertyPane, BaseWidgetPropertyPane

public class ChartPropertyPane extends MiddleChartPropertyPane
{

    private static ChartPropertyPane singleton;

    public ChartPropertyPane()
    {
    }

    public static synchronized ChartPropertyPane getInstance()
    {
        if(singleton == null)
            singleton = new ChartPropertyPane();
        singleton.setSureProperty();
        return singleton;
    }

    protected void createNameLabel()
    {
        nameLabel = new UILabel() {

            final ChartPropertyPane this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(super.getPreferredSize().width, 18);
            }

            
            {
                this$0 = ChartPropertyPane.this;
                super();
            }
        }
;
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
        nameLabel.setHorizontalAlignment(0);
    }

    protected void createMainPane()
    {
        add(chartEditPane, "Center");
    }

    protected JComponent createNorthComponent()
    {
        return nameLabel;
    }

    public void setWidgetPropertyPane(BaseWidgetPropertyPane basewidgetpropertypane)
    {
    }
}
