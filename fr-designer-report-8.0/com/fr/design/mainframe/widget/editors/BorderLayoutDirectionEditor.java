// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.items.FRBorderConstraintsItems;
import com.fr.design.designer.properties.items.Item;
import com.fr.design.gui.icombobox.ComboCheckBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import java.awt.Component;
import javax.swing.ComboBoxModel;
import javax.swing.JList;

// Referenced classes of package com.fr.design.mainframe.widget.editors:
//            AbstractPropertyEditor

public class BorderLayoutDirectionEditor extends AbstractPropertyEditor
{

    public static final Item ITEMS[] = {
        new Item(Inter.getLocText("BorderLayout-North"), "North"), new Item(Inter.getLocText("BorderLayout-South"), "South"), new Item(Inter.getLocText("BorderLayout-West"), "West"), new Item(Inter.getLocText("BorderLayout-East"), "East")
    };
    private ComboCheckBox comboBox;

    public BorderLayoutDirectionEditor()
    {
        comboBox = new ComboCheckBox(ITEMS);
        comboBox.setRenderer(new UIComboBoxRenderer() {

            final BorderLayoutDirectionEditor this$0;

            public Component getListCellRendererComponent(JList jlist, Object obj, int i, boolean flag, boolean flag1)
            {
                if(obj != null)
                {
                    if(obj instanceof Object[])
                    {
                        Object aobj[] = (Object[])(Object[])obj;
                        String as[] = new String[aobj.length];
                        int j = 0;
                        for(int k = aobj.length; j < k; j++)
                            as[j] = ((Item)aobj[j]).getName();

                        setText(StringUtils.join("¡¢", as));
                    }
                } else
                {
                    setText(Inter.getLocText("None"));
                }
                return this;
            }

            
            {
                this$0 = BorderLayoutDirectionEditor.this;
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
        return comboBox.getSelectedItem();
    }

    public void setValue(Object obj)
    {
        Item aitem[];
        if(obj instanceof String[])
            aitem = FRBorderConstraintsItems.createItems((String[])(String[])obj);
        else
            aitem = new Item[0];
        comboBox.getModel().setSelectedItem(aitem);
    }

}
