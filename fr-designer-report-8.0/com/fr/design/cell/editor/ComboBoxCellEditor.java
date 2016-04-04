// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.editor;

import com.fr.base.Utils;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.Grid;
import com.fr.report.cell.TemplateCellElement;
import java.awt.Component;

// Referenced classes of package com.fr.design.cell.editor:
//            AbstractCellEditor

public class ComboBoxCellEditor extends AbstractCellEditor
{

    private UIComboBox comboBox;

    public ComboBoxCellEditor(ElementCasePane elementcasepane, Object aobj[])
    {
        super(elementcasepane);
        comboBox = new UIComboBox(aobj);
        comboBox.setFocusTraversalKeysEnabled(false);
    }

    public Object getCellEditorValue()
        throws Exception
    {
        return comboBox.getSelectedItem();
    }

    public Component getCellEditorComponent(Grid grid, TemplateCellElement templatecellelement, int i)
    {
        Object obj = null;
        if(templatecellelement != null)
            obj = templatecellelement.getValue();
        if(obj == null)
            obj = "";
        comboBox.setSelectedItem(Utils.objectToString(obj));
        return comboBox;
    }
}
