// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.form.layout;

import java.awt.BorderLayout;

// Referenced classes of package com.fr.design.form.layout:
//            FRLayoutManager

public class FRBorderLayout extends BorderLayout
    implements FRLayoutManager
{

    public FRBorderLayout()
    {
        this(0, 0);
    }

    public FRBorderLayout(int i, int j)
    {
        super(i, j);
    }

    public boolean isResizable()
    {
        return false;
    }
}
