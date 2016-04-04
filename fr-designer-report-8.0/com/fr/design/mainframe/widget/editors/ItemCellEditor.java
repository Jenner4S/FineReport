// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import com.fr.design.designer.properties.items.*;
import com.fr.design.gui.icombobox.UIComboBox;
import java.util.Vector;

// Referenced classes of package com.fr.design.mainframe.widget.editors:
//            ComboEditor

public class ItemCellEditor extends ComboEditor
{

    public ItemCellEditor()
    {
        this(((ItemProvider) (new LabelHorizontalAlignmentItems())));
    }

    public ItemCellEditor(ItemProvider itemprovider)
    {
        this(itemprovider.getItems());
    }

    public ItemCellEditor(Item aitem[])
    {
        super(aitem);
    }

    public ItemCellEditor(Vector vector)
    {
        super(vector);
    }

    public void setValue(Object obj)
    {
        Item item = new Item("", obj);
        comboBox.setSelectedItem(item);
    }

    public Object getValue()
    {
        Item item = (Item)comboBox.getSelectedItem();
        return item.getValue();
    }
}
