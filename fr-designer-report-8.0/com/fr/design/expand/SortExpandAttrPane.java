// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.expand;

import com.fr.base.BaseUtils;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.stable.StringUtils;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SortExpandAttrPane extends JPanel
{

    private UIButtonGroup sort_type_pane;
    private TinyFormulaPane tinyFormulaPane;
    private CardLayout cardLayout;
    private JPanel centerPane;

    public SortExpandAttrPane()
    {
        setLayout(new BorderLayout(0, 4));
        Icon aicon[] = {
            BaseUtils.readIcon("/com/fr/design/images/expand/none16x16.png"), BaseUtils.readIcon("/com/fr/design/images/expand/asc.png"), BaseUtils.readIcon("/com/fr/design/images/expand/des.png")
        };
        String as[] = {
            Inter.getLocText("Sort-Original"), Inter.getLocText("Sort-Ascending"), Inter.getLocText("Sort-Descending")
        };
        sort_type_pane = new UIButtonGroup(aicon);
        sort_type_pane.setAllToolTips(as);
        sort_type_pane.setGlobalName(Inter.getLocText("ExpandD-Sort_After_Expand"));
        add(sort_type_pane, "North");
        cardLayout = new CardLayout();
        centerPane = new JPanel(cardLayout);
        tinyFormulaPane = new TinyFormulaPane();
        centerPane.add(new JPanel(), "none");
        centerPane.add(tinyFormulaPane, "content");
        add(centerPane, "Center");
        sort_type_pane.addChangeListener(new ChangeListener() {

            final SortExpandAttrPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                cardLayout.show(centerPane, sort_type_pane.getSelectedIndex() != 0 ? "content" : "none");
            }

            
            {
                this$0 = SortExpandAttrPane.this;
                super();
            }
        }
);
    }

    public void populate(CellExpandAttr cellexpandattr)
    {
        if(cellexpandattr == null)
            cellexpandattr = new CellExpandAttr();
        int i = cellexpandattr.getOrder();
        sort_type_pane.setSelectedIndex(i);
        String s = cellexpandattr.getSortFormula();
        tinyFormulaPane.populateBean(s);
        cardLayout.show(centerPane, sort_type_pane.getSelectedIndex() != 0 ? "content" : "none");
    }

    public void update(CellExpandAttr cellexpandattr)
    {
        if(cellexpandattr == null)
            cellexpandattr = new CellExpandAttr();
        String s = null;
        cellexpandattr.setOrder(sort_type_pane.getSelectedIndex());
        if(cellexpandattr.getOrder() != 0)
        {
            String s1 = tinyFormulaPane.updateBean();
            if(StringUtils.isNotEmpty(s1))
                cellexpandattr.setSortFormula(s1);
            else
                cellexpandattr.setSortFormula(s);
        } else
        {
            cellexpandattr.setSortFormula(s);
        }
    }



}
