// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import com.fr.design.Exception.ValidationException;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.form.ui.WidgetManager;
import com.fr.form.ui.WidgetManagerProvider;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;

// Referenced classes of package com.fr.design.mainframe.widget.editors:
//            AbstractPropertyEditor

public class NameWidgetComboboxEditor extends AbstractPropertyEditor
{

    private Object value;
    private UIComboBox comboBox;

    public NameWidgetComboboxEditor()
    {
        Vector vector = new Vector();
        WidgetManagerProvider widgetmanagerprovider = WidgetManager.getProviderInstance();
        String s;
        for(Iterator iterator = widgetmanagerprovider.getWidgetConfigNameIterator(); iterator.hasNext(); vector.add(s))
            s = (String)iterator.next();

        comboBox = new UIComboBox(new DefaultComboBoxModel(vector));
        comboBox.addItemListener(new ItemListener() {

            final NameWidgetComboboxEditor this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                if(itemevent.getStateChange() == 1)
                    firePropertyChanged();
            }

            
            {
                this$0 = NameWidgetComboboxEditor.this;
                super();
            }
        }
);
    }

    public void validateValue()
        throws ValidationException
    {
    }

    public Component getCustomEditor()
    {
        return comboBox;
    }

    public Object getValue()
    {
        Object obj = comboBox.getSelectedItem();
        return obj != null ? obj : value;
    }

    public void setValue(Object obj)
    {
        comboBox.setSelectedItem(obj);
        value = obj;
    }
}
