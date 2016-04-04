// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.mainframe.BaseJForm;
import com.fr.design.mainframe.FormDesigner;
import java.awt.Rectangle;
import javax.swing.JComponent;

public interface XComponent
{

    public abstract Rectangle getBounds();

    public abstract void setBounds(Rectangle rectangle);

    public abstract JComponent createToolPane(BaseJForm basejform, FormDesigner formdesigner);
}
