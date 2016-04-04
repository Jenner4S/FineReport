// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import com.fr.design.Exception.ValidationException;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.Editor;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.mainframe.widget.editors:
//            AbstractPropertyEditor

public class DateRangeEditor extends AbstractPropertyEditor
{

    private ValueEditorPane dv;

    public DateRangeEditor()
    {
        dv = ValueEditorPaneFactory.createDateValueEditorPane(null, null);
        dv.addPropertyChangeListener("value", new PropertyChangeListener() {

            final DateRangeEditor this$0;

            public void propertyChange(PropertyChangeEvent propertychangeevent)
            {
                firePropertyChanged();
            }

            
            {
                this$0 = DateRangeEditor.this;
                super();
            }
        }
);
        Editor aeditor[] = dv.getCards();
        int i = aeditor.length;
        for(int j = 0; j < i; j++)
        {
            Editor editor = aeditor[j];
            editor.addChangeListener(new ChangeListener() {

                final DateRangeEditor this$0;

                public void stateChanged(ChangeEvent changeevent)
                {
                    firePropertyChanged();
                }

            
            {
                this$0 = DateRangeEditor.this;
                super();
            }
            }
);
        }

    }

    public void validateValue()
        throws ValidationException
    {
    }

    public Component getCustomEditor()
    {
        return dv;
    }

    public Object getValue()
    {
        return dv.update();
    }

    public void setValue(Object obj)
    {
        dv.populate(obj);
    }
}
