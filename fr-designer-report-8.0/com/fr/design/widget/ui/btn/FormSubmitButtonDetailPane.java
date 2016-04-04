// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui.btn;

import com.fr.design.widget.btn.ButtonWithHotkeysDetailPane;
import com.fr.form.parameter.FormSubmitButton;
import com.fr.form.ui.Button;
import java.awt.Component;

public class FormSubmitButtonDetailPane extends ButtonWithHotkeysDetailPane
{

    public FormSubmitButtonDetailPane()
    {
    }

    protected Component createCenterPane()
    {
        return null;
    }

    public FormSubmitButton update()
    {
        FormSubmitButton formsubmitbutton = (FormSubmitButton)super.update();
        return formsubmitbutton;
    }

    public FormSubmitButton createButton()
    {
        FormSubmitButton formsubmitbutton = new FormSubmitButton();
        formsubmitbutton.setCustomStyle(true);
        return formsubmitbutton;
    }

    public Class classType()
    {
        return com/fr/form/parameter/FormSubmitButton;
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
