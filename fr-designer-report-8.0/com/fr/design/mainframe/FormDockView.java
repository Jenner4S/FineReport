// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;


// Referenced classes of package com.fr.design.mainframe:
//            DockingView, FormDesigner

public abstract class FormDockView extends DockingView
{

    private FormDesigner editor;

    public FormDockView()
    {
    }

    public void setEditingFormDesigner(FormDesigner formdesigner)
    {
        editor = formdesigner;
    }

    public FormDesigner getEditingFormDesigner()
    {
        return editor;
    }
}
