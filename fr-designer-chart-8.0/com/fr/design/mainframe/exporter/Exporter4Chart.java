// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.exporter;

import com.fr.design.mainframe.JChart;
import java.io.OutputStream;

public interface Exporter4Chart
{

    public abstract void export(OutputStream outputstream, JChart jchart)
        throws Exception;
}
