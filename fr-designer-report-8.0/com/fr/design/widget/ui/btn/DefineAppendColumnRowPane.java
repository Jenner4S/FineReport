// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui.btn;

import com.fr.design.dialog.BasicPane;
import com.fr.design.editor.editor.ColumnRowEditor;
import com.fr.design.editor.editor.IntegerEditor;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.report.web.button.write.AppendRowButton;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class DefineAppendColumnRowPane extends BasicPane
{

    private ColumnRowEditor crEditor;
    private IntegerEditor jNumberEditor;
    private UILabel rowCountLable;

    public DefineAppendColumnRowPane()
    {
        initComponents();
    }

    private void initComponents()
    {
        double d = -2D;
        double ad[] = {
            d, d
        };
        double ad1[] = {
            d, d, d
        };
        crEditor = new ColumnRowEditor();
        jNumberEditor = new IntegerEditor();
        rowCountLable = new UILabel((new StringBuilder()).append(Inter.getLocText("Edit-Row_Count")).append(":").toString());
        JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        jpanel.add(new UILabel(Inter.getLocText("Append_Delete_Row_Message")));
        Component acomponent[][] = {
            {
                new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
                    "Specify", "Cell"
                })).append(":").toString()), crEditor, jpanel
            }, {
                rowCountLable, jNumberEditor
            }
        };
        JPanel jpanel1 = TableLayoutHelper.createTableLayoutPane(acomponent, ad, ad1);
        jpanel1.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        setLayout(FRGUIPaneFactory.createBorderLayout());
        add(jpanel1);
    }

    protected String title4PopupWindow()
    {
        return "Button";
    }

    public void populate(AppendRowButton appendrowbutton)
    {
        crEditor.setValue(appendrowbutton.getFixCell());
        jNumberEditor.setValue(new Integer(appendrowbutton.getCount()));
    }

    public void update(AppendRowButton appendrowbutton)
    {
        appendrowbutton.setFixCell(crEditor.getValue());
        appendrowbutton.setCount(jNumberEditor.getValue().intValue());
    }
}
