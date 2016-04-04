// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.XCreator;

// Referenced classes of package com.fr.design.designer.beans:
//            HoverPainter, ConstraintsGroupModel

public interface LayoutAdapter
{

    public abstract boolean accept(XCreator xcreator, int i, int j);

    public abstract void fix(XCreator xcreator);

    public abstract boolean addBean(XCreator xcreator, int i, int j);

    public abstract HoverPainter getPainter();

    public abstract void showComponent(XCreator xcreator);

    public abstract void addNextComponent(XCreator xcreator);

    public abstract void addBefore(XCreator xcreator, XCreator xcreator1);

    public abstract void addAfter(XCreator xcreator, XCreator xcreator1);

    public abstract boolean canAcceptMoreComponent();

    public abstract ConstraintsGroupModel getLayoutConstraints(XCreator xcreator);

    public abstract GroupModel getLayoutProperties();

    public abstract void removeBean(XCreator xcreator, int i, int j);
}
