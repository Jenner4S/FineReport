// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import com.fr.design.Exception.ValidationException;
import com.fr.design.gui.icheckbox.UICheckBox;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Referenced classes of package com.fr.design.mainframe.widget.editors:
//            AbstractPropertyEditor

public class BooleanEditor extends AbstractPropertyEditor
{

    private UICheckBox checkBox;

    public BooleanEditor()
    {
        checkBox = new UICheckBox();
        checkBox.addActionListener(new ActionListener() {

            final BooleanEditor this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                firePropertyChanged();
            }

            
            {
                this$0 = BooleanEditor.this;
                super();
            }
        }
);
    }

    public void setValue(Object obj)
    {
        Boolean boolean1 = (Boolean)obj;
        checkBox.setSelected(boolean1 != null ? boolean1.booleanValue() : false);
    }

    public Object getValue()
    {
        return checkBox.isSelected() ? Boolean.TRUE : Boolean.FALSE;
    }

    public Component getCustomEditor()
    {
        return checkBox;
    }

    public void validateValue()
        throws ValidationException
    {
    }
}
