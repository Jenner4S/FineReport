// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.mainframe.widget.renderer.EncoderCellRenderer;

// Referenced classes of package com.fr.design.designer.properties:
//            HorizontalAlignmentWrapper

public class HorizontalAlignmentRenderer extends EncoderCellRenderer
{

    public HorizontalAlignmentRenderer()
    {
        super(new HorizontalAlignmentWrapper());
    }
}
