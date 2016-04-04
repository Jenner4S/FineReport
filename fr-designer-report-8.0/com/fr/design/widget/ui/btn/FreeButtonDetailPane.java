// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui.btn;

import com.fr.design.widget.btn.ButtonWithHotkeysDetailPane;
import com.fr.form.ui.Button;
import com.fr.form.ui.FreeButton;
import java.awt.Component;

// Referenced classes of package com.fr.design.widget.ui.btn:
//            ButtonSytleDefinedPane

public class FreeButtonDetailPane extends ButtonWithHotkeysDetailPane
{

    private ButtonSytleDefinedPane stylePane;

    public FreeButtonDetailPane()
    {
    }

    protected Component createCenterPane()
    {
        return stylePane = new ButtonSytleDefinedPane();
    }

    public FreeButton createButton()
    {
        return new FreeButton();
    }

    public void populate(Button button)
    {
        super.populate(button);
        if(button instanceof FreeButton)
            stylePane.populate((FreeButton)button);
    }

    public FreeButton update()
    {
        FreeButton freebutton = (FreeButton)super.update();
        return stylePane.update(freebutton);
    }

    public Class classType()
    {
        return com/fr/form/ui/FreeButton;
    }

    public volatile Button update()
    {
        return update();
    }

    public volatile Button createButton()
    {
        return createButton();
    }
}
