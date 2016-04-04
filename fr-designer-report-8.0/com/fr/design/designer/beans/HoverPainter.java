// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans;

import com.fr.design.designer.creator.XCreator;
import java.awt.Point;

// Referenced classes of package com.fr.design.designer.beans:
//            Painter

public interface HoverPainter
    extends Painter
{

    public abstract void setHotspot(Point point);

    public abstract void setCreator(XCreator xcreator);
}
