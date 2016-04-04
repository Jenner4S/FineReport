// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import com.fr.design.mainframe.widget.accessibles.AccessibleColorEditor;
import com.fr.design.mainframe.widget.accessibles.AccessiblePropertyEditor;
import java.awt.Color;

public class ColorEditor extends AccessiblePropertyEditor
{

    public ColorEditor()
    {
        super(new AccessibleColorEditor());
    }

    public void setDefaultValue(Object obj)
    {
        ((AccessibleColorEditor)editor).setDefaultColor((Color)obj);
    }
}
