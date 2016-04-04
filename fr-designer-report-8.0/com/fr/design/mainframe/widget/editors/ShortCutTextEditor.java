// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import com.fr.base.Utils;
import com.fr.design.Exception.ValidationException;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.widget.btn.ButtonConstants;
import com.fr.form.ui.Button;
import com.fr.stable.StableUtils;
import java.awt.Component;

// Referenced classes of package com.fr.design.mainframe.widget.editors:
//            AbstractPropertyEditor

public class ShortCutTextEditor extends AbstractPropertyEditor
{

    private Button widget;
    private UITextField text;

    public ShortCutTextEditor(Object obj)
    {
        widget = (Button)obj;
        text = new UITextField();
        text.setToolTipText(StableUtils.join(ButtonConstants.HOTKEYS, ","));
        text.setText(widget.getHotkeys());
    }

    public void validateValue()
        throws ValidationException
    {
    }

    public void setValue(Object obj)
    {
        text.setText(Utils.objectToString(obj));
    }

    public Object getValue()
    {
        return text.getText();
    }

    public Component getCustomEditor()
    {
        return text;
    }
}
