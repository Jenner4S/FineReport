// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import com.fr.design.designer.properties.items.Item;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.general.Inter;

// Referenced classes of package com.fr.design.mainframe.widget.editors:
//            ComboEditor

public class ButtonTypeEditor extends ComboEditor
{

    public ButtonTypeEditor()
    {
        super(new Item[] {
            new Item(Inter.getLocText("Default"), Boolean.valueOf(false)), new Item(Inter.getLocText("Custom"), Boolean.valueOf(true))
        });
    }

    public void setValue(Object obj)
    {
        Item item = new Item("", obj);
        comboBox.setSelectedItem(item);
    }

    public boolean refreshInTime()
    {
        return true;
    }

    public Object getValue()
    {
        Item item = (Item)comboBox.getSelectedItem();
        return item.getValue();
    }
}
