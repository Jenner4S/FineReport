// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.Exception.ValidationException;
import com.fr.design.mainframe.widget.editors.AbstractPropertyEditor;
import java.awt.Component;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.mainframe.widget.accessibles:
//            AccessibleEditor

public class AccessiblePropertyEditor extends AbstractPropertyEditor
{

    protected AccessibleEditor editor;

    public AccessiblePropertyEditor(AccessibleEditor accessibleeditor)
    {
        editor = accessibleeditor;
        accessibleeditor.addChangeListener(new ChangeListener() {

            final AccessiblePropertyEditor this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                firePropertyChanged();
            }

            
            {
                this$0 = AccessiblePropertyEditor.this;
                super();
            }
        }
);
    }

    public void setValue(Object obj)
    {
        editor.setValue(obj);
    }

    public Object getValue()
    {
        return editor.getValue();
    }

    public Component getCustomEditor()
    {
        return editor.getEditor();
    }

    public void validateValue()
        throws ValidationException
    {
        editor.validateValue();
    }

}
