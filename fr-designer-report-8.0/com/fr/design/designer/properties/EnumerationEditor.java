// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.designer.properties.items.Item;
import com.fr.design.designer.properties.items.ItemProvider;
import com.fr.design.gui.icombobox.UIComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.*;

// Referenced classes of package com.fr.design.designer.properties:
//            DelegateEditor

public class EnumerationEditor extends DelegateEditor
{

    public EnumerationEditor(ItemProvider itemprovider)
    {
        this(itemprovider.getItems());
    }

    public EnumerationEditor(Item aitem[])
    {
        DefaultComboBoxModel defaultcomboboxmodel = new DefaultComboBoxModel();
        Item aitem1[] = aitem;
        int i = aitem1.length;
        for(int j = 0; j < i; j++)
        {
            Item item = aitem1[j];
            defaultcomboboxmodel.addElement(item);
        }

        init(new UIComboBox(defaultcomboboxmodel));
    }

    public EnumerationEditor(Iterable iterable)
    {
        DefaultComboBoxModel defaultcomboboxmodel = new DefaultComboBoxModel();
        Item item;
        for(Iterator iterator = iterable.iterator(); iterator.hasNext(); defaultcomboboxmodel.addElement(item))
            item = (Item)iterator.next();

        init(new UIComboBox(defaultcomboboxmodel));
    }

    public EnumerationEditor(Enumeration enumeration)
    {
        DefaultComboBoxModel defaultcomboboxmodel = new DefaultComboBoxModel();
        for(; enumeration.hasMoreElements(); defaultcomboboxmodel.addElement(enumeration.nextElement()));
        init(new UIComboBox(defaultcomboboxmodel));
    }

    public EnumerationEditor(UIComboBox uicombobox)
    {
        init(uicombobox);
    }

    private void init(final UIComboBox comboBox)
    {
        editorComponent = comboBox;
        comboBox.putClientProperty("UIComboBox.isTableCellEditor", Boolean.TRUE);
        _flddelegate = new DelegateEditor.EditorDelegate() {

            final UIComboBox val$comboBox;
            final EnumerationEditor this$0;

            public void setValue(Object obj)
            {
                Item item = new Item(null, obj);
                comboBox.setSelectedItem(item);
            }

            public Object getCellEditorValue()
            {
                Item item = (Item)comboBox.getSelectedItem();
                return item.getValue();
            }

            public boolean shouldSelectCell(EventObject eventobject)
            {
                if(eventobject instanceof MouseEvent)
                {
                    MouseEvent mouseevent = (MouseEvent)eventobject;
                    return mouseevent.getID() != 506;
                } else
                {
                    return true;
                }
            }

            public boolean stopCellEditing()
            {
                if(comboBox.isEditable())
                    comboBox.actionPerformed(new ActionEvent(EnumerationEditor.this, 0, ""));
                return super.stopCellEditing();
            }

            
            {
                this$0 = EnumerationEditor.this;
                comboBox = uicombobox;
                super(EnumerationEditor.this);
            }
        }
;
        ((JComponent)comboBox.getEditor().getEditorComponent()).setBorder(null);
        editorComponent.setBorder(null);
        comboBox.addActionListener(_flddelegate);
    }
}
