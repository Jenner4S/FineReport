// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.design.gui.frpane.RegPane;
import com.fr.form.ui.TextArea;
import com.fr.form.ui.TextEditor;

// Referenced classes of package com.fr.design.widget.ui:
//            TextFieldEditorDefinePane

public class TextAreaDefinePane extends TextFieldEditorDefinePane
{

    public TextAreaDefinePane()
    {
    }

    protected TextEditor newTextEditorInstance()
    {
        return new TextArea();
    }

    protected RegPane createRegPane()
    {
        return new RegPane(RegPane.TEXTAREA_REG_TYPE);
    }
}
