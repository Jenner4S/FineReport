// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.design.gui.frpane.RegPane;
import com.fr.form.ui.Password;
import com.fr.form.ui.TextEditor;

// Referenced classes of package com.fr.design.widget.ui:
//            TextFieldEditorDefinePane

public class PasswordDefinePane extends TextFieldEditorDefinePane
{

    private static final long serialVersionUID = 0x41c070af09527da2L;

    public PasswordDefinePane()
    {
    }

    protected TextEditor newTextEditorInstance()
    {
        return new Password();
    }

    protected RegPane createRegPane()
    {
        return new RegPane(RegPane.PASSWORD_REG_TYPE);
    }
}
