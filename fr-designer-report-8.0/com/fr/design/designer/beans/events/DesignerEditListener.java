// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.events;

import java.util.EventListener;

// Referenced classes of package com.fr.design.designer.beans.events:
//            DesignerEvent

public interface DesignerEditListener
    extends EventListener
{

    public abstract void fireCreatorModified(DesignerEvent designerevent);
}
