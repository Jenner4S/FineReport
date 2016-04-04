// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.gui.core.WidgetOption;
import java.util.ArrayList;

public abstract class FormDesignerModeForSpecial
{

    private Object target;

    public FormDesignerModeForSpecial(Object obj)
    {
        target = obj;
    }

    public Object getTarget()
    {
        return target;
    }

    public abstract WidgetOption[] getPredefinedWidgetOptions();

    public abstract ArrayList createRootDesignerPropertyGroup();

    public abstract boolean isFormParameterEditor();

    public abstract int getMinDesignWidth();

    public abstract int getMinDesignHeight();
}
