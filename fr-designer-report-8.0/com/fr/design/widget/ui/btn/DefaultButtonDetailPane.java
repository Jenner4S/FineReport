// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui.btn;

import com.fr.design.widget.btn.ButtonWithHotkeysDetailPane;
import com.fr.form.ui.Button;
import com.fr.form.ui.FreeButton;
import java.awt.Component;

public class DefaultButtonDetailPane extends ButtonWithHotkeysDetailPane
{

    public DefaultButtonDetailPane()
    {
    }

    protected Component createCenterPane()
    {
        return null;
    }

    public FreeButton createButton()
    {
        return new FreeButton();
    }

    public Class classType()
    {
        return com/fr/form/ui/Button;
    }

    public volatile Button createButton()
    {
        return createButton();
    }
}
