// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import com.fr.design.gui.itextfield.UITextField;
import java.awt.Color;

// Referenced classes of package com.fr.design.mainframe.widget.editors:
//            ITextComponent

public class TextField extends UITextField
    implements ITextComponent
{

    public TextField()
    {
        setBorder(null);
        setOpaque(false);
    }

    public void setValue(Object obj)
    {
    }

    public void setEditable(boolean flag)
    {
        super.setEditable(flag);
        setBackground(Color.white);
    }
}
