// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.cell;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.selection.QuickEditor;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class QuickEditorRegion extends JPanel
{

    private static QuickEditorRegion singleton = new QuickEditorRegion();
    private static JPanel EMPTY;

    public static QuickEditorRegion getInstance()
    {
        return singleton;
    }

    public static JPanel getEmptyEditor()
    {
        if(EMPTY == null)
        {
            EMPTY = new JPanel(new BorderLayout());
            UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
                "DataFunction-None", "HJS-Message", "Form-Widget_Property_Table"
            })).append("!").toString());
            uilabel.setBorder(BorderFactory.createEmptyBorder(0, 70, 0, 0));
            EMPTY.add(uilabel, "Center");
        }
        return EMPTY;
    }

    private QuickEditorRegion()
    {
        setLayout(new BorderLayout());
    }

    public void populate(QuickEditor quickeditor)
    {
        removeAll();
        if(quickeditor.getComponentCount() == 0)
            add(getEmptyEditor(), "Center");
        else
            add(quickeditor, "Center");
        validate();
        repaint();
    }

}
