// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.gui.ilable.UILabel;
import com.fr.form.ui.Widget;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.designer.creator:
//            XWidgetCreator

public class NullCreator extends XWidgetCreator
{

    public NullCreator(Widget widget, Dimension dimension)
    {
        super(widget, dimension);
    }

    protected String getIconName()
    {
        return "none_widget.png";
    }

    protected JComponent initEditor()
    {
        UILabel uilabel = new UILabel("UNEXPECTED WIDGET");
        uilabel.setForeground(Color.red);
        uilabel.setVerticalAlignment(0);
        uilabel.setHorizontalAlignment(0);
        setBorder(DEFALUTBORDER);
        return editor = uilabel;
    }
}
