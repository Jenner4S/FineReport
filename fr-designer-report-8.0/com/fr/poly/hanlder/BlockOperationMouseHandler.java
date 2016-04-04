// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly.hanlder;

import com.fr.report.poly.PolyWorkSheet;
import javax.swing.event.MouseInputAdapter;

public abstract class BlockOperationMouseHandler extends MouseInputAdapter
{

    public BlockOperationMouseHandler()
    {
    }

    protected abstract PolyWorkSheet getTarget();
}
