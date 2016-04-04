// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import java.util.ArrayList;

// Referenced classes of package com.fr.design.designer.creator:
//            XCreator

public interface XCreatorTools
{

    public abstract void notShowInComponentTree(ArrayList arraylist);

    public abstract void resetCreatorName(String s);

    public abstract XCreator getEditingChildCreator();

    public abstract XCreator getPropertyDescriptorCreator();

    public abstract void updateChildBound(int i);

    public abstract boolean isComponentTreeLeaf();

    public abstract boolean isDedicateContainer();
}
