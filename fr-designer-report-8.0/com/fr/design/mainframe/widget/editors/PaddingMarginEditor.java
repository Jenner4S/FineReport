// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import com.fr.design.mainframe.widget.accessibles.AccessiblePropertyEditor;
import com.fr.design.mainframe.widget.accessibles.AccessilePaddingMarginEditor;

public class PaddingMarginEditor extends AccessiblePropertyEditor
{

    public PaddingMarginEditor()
    {
        super(new AccessilePaddingMarginEditor());
    }
}
