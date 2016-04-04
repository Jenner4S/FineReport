// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.start;

import com.fr.design.mainframe.actions.NewFormAction;
import com.fr.design.menu.ShortCut;
import com.fr.design.module.FormDesignerModule;

// Referenced classes of package com.fr.start:
//            BaseDesigner

public class Designer4Form extends BaseDesigner
{

    public static void main(String args[])
    {
        new Designer4Form(args);
    }

    public Designer4Form(String as[])
    {
        super(as);
    }

    protected String module2Start()
    {
        return com/fr/design/module/FormDesignerModule.getName();
    }

    public ShortCut[] createNewFileShortCuts()
    {
        return (new ShortCut[] {
            new NewFormAction()
        });
    }
}
