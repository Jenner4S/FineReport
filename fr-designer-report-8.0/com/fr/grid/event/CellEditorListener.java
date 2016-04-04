// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid.event;

import java.util.EventListener;

// Referenced classes of package com.fr.grid.event:
//            CellEditorEvent

public interface CellEditorListener
    extends EventListener
{

    public abstract void editingStopped(CellEditorEvent celleditorevent);

    public abstract void editingCanceled(CellEditorEvent celleditorevent);
}
