// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import com.fr.design.mainframe.widget.accessibles.AccessiblePropertyEditor;
import com.fr.design.mainframe.widget.accessibles.AccessibleRegexEditor;

public class RegexEditor extends AccessiblePropertyEditor
{
    public static class RegexEditor4TextArea extends AccessiblePropertyEditor
    {

        public RegexEditor4TextArea()
        {
            super(new com.fr.design.mainframe.widget.accessibles.AccessibleRegexEditor.AccessibleRegexEditor4TextArea());
        }
    }


    public RegexEditor()
    {
        super(new AccessibleRegexEditor());
    }
}
