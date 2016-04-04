// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.events;

import com.fr.design.designer.creator.XComponent;
import com.fr.general.ComparatorUtils;
import java.util.ArrayList;

// Referenced classes of package com.fr.design.designer.beans.events:
//            DesignerEditListener, DesignerEvent

public class CreatorEventListenerTable
{

    protected ArrayList listeners;

    public CreatorEventListenerTable()
    {
        listeners = new ArrayList();
    }

    public void addListener(DesignerEditListener designereditlistener)
    {
        if(designereditlistener == null)
            return;
        for(int i = 0; i < listeners.size(); i++)
            if(ComparatorUtils.equals(designereditlistener, listeners.get(i)))
            {
                listeners.set(i, designereditlistener);
                return;
            }

        listeners.add(designereditlistener);
    }

    private void fireCreatorModified(DesignerEvent designerevent)
    {
        for(int i = 0; i < listeners.size(); i++)
        {
            DesignerEditListener designereditlistener = (DesignerEditListener)listeners.get(i);
            designereditlistener.fireCreatorModified(designerevent);
        }

    }

    public void fireCreatorModified(XComponent xcomponent, int i)
    {
        DesignerEvent designerevent = new DesignerEvent(i, xcomponent);
        fireCreatorModified(designerevent);
    }

    public void fireCreatorModified(int i)
    {
        fireCreatorModified(null, i);
    }
}
