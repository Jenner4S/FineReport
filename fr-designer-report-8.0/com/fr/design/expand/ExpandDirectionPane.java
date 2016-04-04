// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.expand;

import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.CellExpandAttr;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

public class ExpandDirectionPane extends JPanel
{

    private UIRadioButton t2bRadioButton;
    private UIRadioButton l2rRadioButton;
    private UIRadioButton noneRadioButton;
    private String InsertText;

    public ExpandDirectionPane()
    {
        InsertText = " ";
        initComponents();
    }

    public void initComponents()
    {
        setLayout(new GridLayout(1, 3));
        JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
        add(jpanel);
        t2bRadioButton = new UIRadioButton(Inter.getLocText("Utils-Top_to_Bottom"));
        l2rRadioButton = new UIRadioButton(Inter.getLocText("Utils-Left_to_Right"));
        noneRadioButton = new UIRadioButton(Inter.getLocText("ExpandD-Not_Expand"));
        ButtonGroup buttongroup = new ButtonGroup();
        buttongroup.add(t2bRadioButton);
        buttongroup.add(l2rRadioButton);
        buttongroup.add(noneRadioButton);
        jpanel.add(GUICoreUtils.createFlowPane(new Component[] {
            new UILabel(InsertText), t2bRadioButton
        }, 0));
        jpanel.add(GUICoreUtils.createFlowPane(new Component[] {
            new UILabel(InsertText), l2rRadioButton
        }, 0));
        jpanel.add(GUICoreUtils.createFlowPane(new Component[] {
            new UILabel(InsertText), noneRadioButton
        }, 0));
    }

    public void populate(CellExpandAttr cellexpandattr)
    {
        if(cellexpandattr == null)
            cellexpandattr = new CellExpandAttr();
        switch(cellexpandattr.getDirection())
        {
        case 0: // '\0'
            t2bRadioButton.setSelected(true);
            break;

        case 1: // '\001'
            l2rRadioButton.setSelected(true);
            break;

        default:
            noneRadioButton.setSelected(true);
            break;
        }
    }

    public void update(CellExpandAttr cellexpandattr)
    {
        if(cellexpandattr == null)
            cellexpandattr = new CellExpandAttr();
        if(t2bRadioButton.isSelected())
            cellexpandattr.setDirection((byte)0);
        else
        if(l2rRadioButton.isSelected())
            cellexpandattr.setDirection((byte)1);
        else
            cellexpandattr.setDirection((byte)2);
    }

    public void setNoneRadioButtonSelected(boolean flag)
    {
        if(flag)
        {
            noneRadioButton.setSelected(true);
            l2rRadioButton.setEnabled(false);
            t2bRadioButton.setEnabled(false);
            noneRadioButton.setEnabled(false);
        } else
        {
            t2bRadioButton.setEnabled(true);
            l2rRadioButton.setEnabled(true);
            noneRadioButton.setEnabled(true);
            if(noneRadioButton.isSelected())
                t2bRadioButton.setSelected(true);
        }
    }
}
