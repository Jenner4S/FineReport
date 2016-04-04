// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.editor;

import com.fr.design.gui.date.UIDatePicker;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.Grid;
import com.fr.report.cell.TemplateCellElement;
import java.awt.Component;
import java.util.Date;

// Referenced classes of package com.fr.design.cell.editor:
//            AbstractCellEditor

public class DateCellEditor extends AbstractCellEditor
{

    private UIDatePicker comboBox;

    public DateCellEditor(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        comboBox = new UIDatePicker();
        comboBox.setFocusTraversalKeysEnabled(false);
    }

    public Object getCellEditorValue()
        throws Exception
    {
        return comboBox.getSelectedDate();
    }

    public Component getCellEditorComponent(Grid grid, TemplateCellElement templatecellelement, int i)
    {
        Object obj = null;
        if(templatecellelement != null)
            obj = templatecellelement.getValue();
        if(obj == null || !(obj instanceof Date))
            obj = new Date();
        comboBox.setSelectedItem(obj);
        return comboBox;
    }
}
