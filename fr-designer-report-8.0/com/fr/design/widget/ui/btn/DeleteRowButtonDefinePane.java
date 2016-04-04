// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui.btn;

import com.fr.base.Icon;
import com.fr.base.IconManager;
import com.fr.design.widget.btn.ButtonWithHotkeysDetailPane;
import com.fr.form.ui.Button;
import com.fr.general.Inter;
import com.fr.report.web.button.write.DeleteRowButton;
import java.awt.Component;

// Referenced classes of package com.fr.design.widget.ui.btn:
//            DefineDeleteColumnRowPane

public class DeleteRowButtonDefinePane extends ButtonWithHotkeysDetailPane
{

    private DefineDeleteColumnRowPane ddcp;

    public DeleteRowButtonDefinePane()
    {
    }

    protected Component createCenterPane()
    {
        return ddcp = new DefineDeleteColumnRowPane();
    }

    public DeleteRowButton createButton()
    {
        DeleteRowButton deleterowbutton = new DeleteRowButton();
        deleterowbutton.setText(Inter.getLocText("Utils-Delete_Row"));
        deleterowbutton.setIconName(IconManager.DELETE.getName());
        return deleterowbutton;
    }

    public Class classType()
    {
        return com/fr/report/web/button/write/DeleteRowButton;
    }

    public void populate(Button button)
    {
        super.populate(button);
        if(button instanceof DeleteRowButton)
            ddcp.populate((DeleteRowButton)button);
    }

    public DeleteRowButton update()
    {
        DeleteRowButton deleterowbutton = (DeleteRowButton)super.update();
        ddcp.update(deleterowbutton);
        return deleterowbutton;
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
