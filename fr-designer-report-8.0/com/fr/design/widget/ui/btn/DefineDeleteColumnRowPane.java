// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui.btn;

import com.fr.design.dialog.BasicPane;
import com.fr.design.editor.editor.ColumnRowEditor;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.report.web.button.write.DeleteRowButton;
import javax.swing.BorderFactory;

public class DefineDeleteColumnRowPane extends BasicPane
{

    private static final int BORDER_LEFT = -10;
    private ColumnRowEditor crEditor;

    public DefineDeleteColumnRowPane()
    {
        initComponents();
    }

    private void initComponents()
    {
        crEditor = new ColumnRowEditor();
        setLayout(FRGUIPaneFactory.createL_FlowLayout());
        setBorder(BorderFactory.createEmptyBorder(0, -10, 0, 0));
        add(new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
            "Specify", "Cell"
        })).append(":").toString()));
        add(crEditor);
        add(new UILabel(Inter.getLocText("Append_Delete_Row_Message")));
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("Button");
    }

    public void populate(DeleteRowButton deleterowbutton)
    {
        crEditor.setValue(deleterowbutton.getFixCell());
    }

    public void update(DeleteRowButton deleterowbutton)
    {
        deleterowbutton.setFixCell(crEditor.getValue());
    }
}
