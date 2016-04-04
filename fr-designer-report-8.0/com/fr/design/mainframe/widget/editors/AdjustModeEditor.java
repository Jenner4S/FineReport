// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import com.fr.design.Exception.ValidationException;
import com.fr.design.gui.icombobox.DictionaryComboBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.general.Inter;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Referenced classes of package com.fr.design.mainframe.widget.editors:
//            AbstractPropertyEditor

public class AdjustModeEditor extends AbstractPropertyEditor
{

    public static final String AjustRowTypes[] = {
        Inter.getLocText("No"), Inter.getLocText("Utils-Row_Height"), Inter.getLocText("Utils-Column_Width"), Inter.getLocText("Default")
    };
    private UIComboBox combobox;

    public AdjustModeEditor()
    {
        combobox = new DictionaryComboBox(new Integer[] {
            Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)
        }, AjustRowTypes);
        combobox.addActionListener(new ActionListener() {

            final AdjustModeEditor this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                firePropertyChanged();
            }

            
            {
                this$0 = AdjustModeEditor.this;
                super();
            }
        }
);
    }

    public void validateValue()
        throws ValidationException
    {
    }

    public void setValue(Object obj)
    {
        combobox.setSelectedItem((Integer)obj);
    }

    public Object getValue()
    {
        return combobox.getSelectedItem();
    }

    public Component getCustomEditor()
    {
        return combobox;
    }

}
