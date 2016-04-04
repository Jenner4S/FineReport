// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.form.ui.Table;
import java.awt.Dimension;
import java.beans.IntrospectionException;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

// Referenced classes of package com.fr.design.designer.creator:
//            XWidgetCreator, CRPropertyDescriptor

public class XTableEditor extends XWidgetCreator
{

    public XTableEditor(Table table, Dimension dimension)
    {
        super(table, dimension);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return new CRPropertyDescriptor[0];
    }

    protected JComponent initEditor()
    {
        if(editor == null)
        {
            DefaultTableModel defaulttablemodel = new DefaultTableModel(4, 2);
            editor = new JTable(defaulttablemodel);
        }
        return editor;
    }

    public Dimension initEditorSize()
    {
        return MIDDLE_PREFERRED_SIZE;
    }

    protected String getIconName()
    {
        return "table_16.png";
    }
}
