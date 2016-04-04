// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui.btn;

import com.fr.base.Icon;
import com.fr.base.IconManager;
import com.fr.design.widget.btn.ButtonWithHotkeysDetailPane;
import com.fr.form.ui.Button;
import com.fr.general.Inter;
import com.fr.report.web.button.write.AppendRowButton;
import java.awt.Component;

// Referenced classes of package com.fr.design.widget.ui.btn:
//            DefineAppendColumnRowPane

public class AppendRowButtonDefinePane extends ButtonWithHotkeysDetailPane
{

    private DefineAppendColumnRowPane defineColumnRowPane;

    public AppendRowButtonDefinePane()
    {
    }

    protected Component createCenterPane()
    {
        return defineColumnRowPane = new DefineAppendColumnRowPane();
    }

    public AppendRowButton createButton()
    {
        AppendRowButton appendrowbutton = new AppendRowButton();
        appendrowbutton.setText(Inter.getLocText("Utils-Insert_Row"));
        appendrowbutton.setIconName(IconManager.ADD.getName());
        return appendrowbutton;
    }

    public void populate(Button button)
    {
        super.populate(button);
        if(button instanceof AppendRowButton)
            defineColumnRowPane.populate((AppendRowButton)button);
    }

    public AppendRowButton update()
    {
        AppendRowButton appendrowbutton = (AppendRowButton)super.update();
        defineColumnRowPane.update(appendrowbutton);
        return appendrowbutton;
    }

    public Class classType()
    {
        return com/fr/report/web/button/write/AppendRowButton;
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
