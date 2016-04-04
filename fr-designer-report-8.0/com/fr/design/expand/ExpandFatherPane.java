// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.expand;

import com.fr.base.BaseUtils;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.gui.columnrow.ColumnRowPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;
import com.fr.general.Inter;
import com.fr.grid.Grid;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.stable.ColumnRow;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;

public abstract class ExpandFatherPane extends JPanel
    implements GlobalNameObserver
{

    private UIComboBox comboBox;
    private ColumnRowPane customParentColumnRowPane;
    private ElementCasePane ePane;
    private SelectionListener gridSelectionChangeListener;
    private CellSelection oldSelection;
    private String expandFatherName;
    private GlobalNameListener globalNameListener;
    private boolean isAlreadyAddListener;

    public ExpandFatherPane()
    {
        expandFatherName = "";
        globalNameListener = null;
        isAlreadyAddListener = false;
        setLayout(new BorderLayout(0, 4));
        comboBox = new UIComboBox(new String[] {
            Inter.getLocText("None"), Inter.getLocText("Default"), Inter.getLocText("Custom")
        });
        final CardLayout cardLayout = new CardLayout();
        final JPanel customPane = new JPanel(cardLayout);
        customParentColumnRowPane = new ColumnRowPane() {

            final ExpandFatherPane this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(super.getPreferredSize().width, 20);
            }

            public void setGlobalName()
            {
                if(shouldResponseNameListener())
                    globalNameListener.setGlobalName(expandFatherName);
            }

            
            {
                this$0 = ExpandFatherPane.this;
                super();
            }
        }
;
        UIButton uibutton = new UIButton(BaseUtils.readIcon("com/fr/design/images/buttonicon/select.png"));
        uibutton.setPreferredSize(new Dimension(24, 20));
        JPanel jpanel = new JPanel(new BorderLayout(1, 0));
        jpanel.add(customParentColumnRowPane, "Center");
        jpanel.add(uibutton, "East");
        customPane.add(jpanel, "content");
        customPane.add(new JPanel(), "none");
        add(comboBox, "North");
        add(customPane, "Center");
        comboBox.addItemListener(new ItemListener() {

            final CardLayout val$cardLayout;
            final JPanel val$customPane;
            final ExpandFatherPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                cardLayout.show(customPane, comboBox.getSelectedIndex() != 2 ? "none" : "content");
                if(globalNameListener != null && shouldResponseNameListener())
                    globalNameListener.setGlobalName(expandFatherName);
            }

            
            {
                this$0 = ExpandFatherPane.this;
                cardLayout = cardlayout;
                customPane = jpanel;
                super();
            }
        }
);
        uibutton.addActionListener(new ActionListener() {

            final ExpandFatherPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(ePane == null || isAlreadyAddListener)
                {
                    return;
                } else
                {
                    oldSelection = (CellSelection)ePane.getSelection();
                    ePane.getGrid().setNotShowingTableSelectPane(false);
                    ePane.setEditable(false);
                    ePane.repaint(10L);
                    gridSelectionChangeListener = new SelectionListener() {

                        final _cls3 this$1;

                        public void selectionChanged(SelectionEvent selectionevent)
                        {
                            Selection selection = ePane.getSelection();
                            if(selection instanceof CellSelection)
                            {
                                CellSelection cellselection = (CellSelection)selection;
                                ColumnRow columnrow = ColumnRow.valueOf(cellselection.getColumn(), cellselection.getRow());
                                ePane.setOldSelecton(oldSelection);
                                customParentColumnRowPane.setColumnRow(columnrow);
                            }
                            ePane.removeSelectionChangeListener(gridSelectionChangeListener);
                            isAlreadyAddListener = false;
                            ePane.getGrid().setNotShowingTableSelectPane(true);
                            ePane.setEditable(true);
                            ePane.repaint();
                        }

                    
                    {
                        this$1 = _cls3.this;
                        super();
                    }
                    }
;
                    ePane.addSelectionChangeListener(gridSelectionChangeListener);
                    isAlreadyAddListener = true;
                    return;
                }
            }

            
            {
                this$0 = ExpandFatherPane.this;
                super();
            }
        }
);
        comboBox.setSelectedIndex(1);
    }

    public void registerNameListener(GlobalNameListener globalnamelistener)
    {
        globalNameListener = globalnamelistener;
    }

    public boolean shouldResponseNameListener()
    {
        return true;
    }

    protected abstract ColumnRow getColumnRow(CellExpandAttr cellexpandattr);

    protected abstract boolean isParentDefault(CellExpandAttr cellexpandattr);

    public void populate(CellExpandAttr cellexpandattr)
    {
        ColumnRow columnrow = getColumnRow(cellexpandattr);
        if(isParentDefault(cellexpandattr))
        {
            comboBox.setSelectedIndex(1);
            customParentColumnRowPane.populate(ColumnRow.valueOf(0, 0));
        } else
        if(ColumnRow.validate(columnrow))
        {
            comboBox.setSelectedIndex(2);
            customParentColumnRowPane.populate(columnrow);
        } else
        {
            comboBox.setSelectedIndex(0);
            customParentColumnRowPane.populate(ColumnRow.valueOf(0, 0));
        }
    }

    public void setGlobalName(String s)
    {
        expandFatherName = s;
        comboBox.setGlobalName(s);
    }

    protected abstract void setValue(CellExpandAttr cellexpandattr, boolean flag, ColumnRow columnrow);

    public void update(CellExpandAttr cellexpandattr)
    {
        if(cellexpandattr == null)
            cellexpandattr = new CellExpandAttr();
        int i = comboBox.getSelectedIndex();
        switch(i)
        {
        case 1: // '\001'
            setValue(cellexpandattr, true, null);
            break;

        case 2: // '\002'
            setValue(cellexpandattr, false, customParentColumnRowPane.update());
            break;

        default:
            setValue(cellexpandattr, false, null);
            break;
        }
    }

    public void setElementCasePane(ElementCasePane elementcasepane)
    {
        ePane = elementcasepane;
    }











}
