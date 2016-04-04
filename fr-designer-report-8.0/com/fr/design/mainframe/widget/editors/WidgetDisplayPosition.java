// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import com.fr.design.designer.properties.items.*;
import com.fr.design.gui.icombobox.UIComboBox;

// Referenced classes of package com.fr.design.mainframe.widget.editors:
//            ComboEditor

public class WidgetDisplayPosition extends ComboEditor
{

    public WidgetDisplayPosition()
    {
        this(((ItemProvider) (new WidgetDisplayPositionItems())));
    }

    public WidgetDisplayPosition(ItemProvider itemprovider)
    {
        this(itemprovider.getItems());
    }

    public WidgetDisplayPosition(Item aitem[])
    {
        super(aitem);
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
