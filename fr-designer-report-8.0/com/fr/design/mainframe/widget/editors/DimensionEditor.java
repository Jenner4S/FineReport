// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import com.fr.design.mainframe.widget.accessibles.AccessibleDimensionEditor;
import com.fr.design.mainframe.widget.accessibles.AccessiblePropertyEditor;

public class DimensionEditor extends AccessiblePropertyEditor
{

    public DimensionEditor()
    {
        super(new AccessibleDimensionEditor());
    }
}
