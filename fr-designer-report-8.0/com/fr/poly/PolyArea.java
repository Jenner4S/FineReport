// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly;

import javax.swing.JComponent;

// Referenced classes of package com.fr.poly:
//            PolyDesignUI, PolyDesigner

public class PolyArea extends JComponent
{

    private PolyDesigner polyDesigner;

    public PolyArea(PolyDesigner polydesigner)
    {
        polyDesigner = polydesigner;
        setUI(new PolyDesignUI());
    }

    public PolyDesigner getPolyDesigner()
    {
        return polyDesigner;
    }
}
