// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.events;

import com.fr.design.designer.creator.XComponent;

public class DesignerEvent
{

    public static final int CREATOR_ADDED = 1;
    public static final int CREATOR_DELETED = 2;
    public static final int CREATOR_CUTED = 3;
    public static final int CREATOR_PASTED = 4;
    public static final int CREATOR_EDITED = 5;
    public static final int CREATOR_RESIZED = 6;
    public static final int CREATOR_SELECTED = 7;
    public static final int CREATOR_RENAMED = 8;
    private int eventID;
    private XComponent affectedXCreator;

    DesignerEvent(int i, XComponent xcomponent)
    {
        eventID = i;
        affectedXCreator = xcomponent;
    }

    public int getCreatorEventID()
    {
        return eventID;
    }

    public XComponent getAffectedCreator()
    {
        return affectedXCreator;
    }
}
