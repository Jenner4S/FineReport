// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.cell;

import com.fr.base.BaseUtils;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.dialog.BasicPane;
import com.fr.design.fun.CellAttributeProvider;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.gui.itabpane.TitleChangeListener;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.cell.settingpane.AbstractCellAttrPane;
import com.fr.design.mainframe.cell.settingpane.CellExpandAttrPane;
import com.fr.design.mainframe.cell.settingpane.CellOtherSetPane;
import com.fr.design.mainframe.cell.settingpane.CellPresentPane;
import com.fr.design.mainframe.cell.settingpane.CellStylePane;
import com.fr.design.utils.DesignUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JPanel;

public class CellElementEditPane extends BasicPane
{

    private static int TIME_GAP = 80;
    private java.util.List paneList;
    private TemplateCellElement cellelement;
    private ElementCasePane ePane;
    private UIHeadGroup tabsHeaderIconPane;
    private boolean isEditing;
    private int PaneListIndex;
    private CardLayout card;
    private JPanel center;
    private TitleChangeListener titleChangeListener;
    private CellAttributeProvider cellAttributeProvider;
    AttributeChangeListener listener;

    public CellElementEditPane()
    {
        titleChangeListener = null;
        cellAttributeProvider = null;
        listener = new AttributeChangeListener() {

            final CellElementEditPane this$0;

            public void attributeChange()
            {
                boolean flag = ComparatorUtils.equals(((AbstractCellAttrPane)paneList.get(tabsHeaderIconPane.getSelectedIndex())).getGlobalName(), Inter.getLocText("FR-Designer_LeftParent")) || ComparatorUtils.equals(((AbstractCellAttrPane)paneList.get(tabsHeaderIconPane.getSelectedIndex())).getGlobalName(), Inter.getLocText("ExpandD-Up_Father_Cell"));
                boolean flag1 = ComparatorUtils.equals(((AbstractCellAttrPane)paneList.get(tabsHeaderIconPane.getSelectedIndex())).getGlobalName(), Inter.getLocText("ExpandD-Expand_Direction"));
                if(flag1 || flag)
                    ePane.setSupportDefaultParentCalculate(true);
                if(ePane.getSelection() instanceof CellSelection)
                {
                    isEditing = true;
                    if(ePane.isSelectedOneCell())
                    {
                        ((AbstractCellAttrPane)paneList.get(tabsHeaderIconPane.getSelectedIndex())).updateBean();
                        ePane.fireTargetModified();
                    } else
                    {
                        ((AbstractCellAttrPane)paneList.get(tabsHeaderIconPane.getSelectedIndex())).updateBeans();
                        ePane.fireTargetModified();
                    }
                } else
                {
                    DesignUtils.errorMessage((new StringBuilder()).append(Inter.getLocText(new String[] {
                        "Not_use_a_cell_attribute_table_editing", "M_Insert-Float"
                    })).append("!").toString());
                }
                ePane.setSupportDefaultParentCalculate(false);
            }

            
            {
                this$0 = CellElementEditPane.this;
                super();
            }
        }
;
        setLayout(new BorderLayout());
        initPaneList();
        Icon aicon[] = new Icon[paneList.size()];
        card = new CardLayout();
        center = new JPanel(card);
        for(int i = 0; i < paneList.size(); i++)
        {
            AbstractCellAttrPane abstractcellattrpane = (AbstractCellAttrPane)paneList.get(i);
            aicon[i] = BaseUtils.readIcon(abstractcellattrpane.getIconPath());
            center.add(abstractcellattrpane, abstractcellattrpane.title4PopupWindow());
        }

        tabsHeaderIconPane = new UIHeadGroup(aicon) {

            final CellElementEditPane this$0;

            public void tabChanged(int j)
            {
                card.show(center, ((AbstractCellAttrPane)paneList.get(j)).title4PopupWindow());
                ((AbstractCellAttrPane)paneList.get(j)).populateBean(cellelement, ePane);
                ((AbstractCellAttrPane)paneList.get(j)).addAttributeChangeListener(listener);
                if(titleChangeListener != null)
                    titleChangeListener.fireTitleChange(getSelectedTabName());
            }

            
            {
                this$0 = CellElementEditPane.this;
                super(aicon);
            }
        }
;
        tabsHeaderIconPane.setNeedLeftRightOutLine(false);
        add(tabsHeaderIconPane, "North");
        add(center, "Center");
    }

    public transient void setSelectedIndex(String as[])
    {
        String s = as[0];
        int i = 0;
        do
        {
            if(i >= paneList.size())
                break;
            if(ComparatorUtils.equals(s, ((AbstractCellAttrPane)paneList.get(i)).title4PopupWindow()))
            {
                tabsHeaderIconPane.setSelectedIndex(i);
                if(as.length == 2)
                    ((AbstractCellAttrPane)paneList.get(i)).setSelectedByIds(1, as);
                break;
            }
            i++;
        } while(true);
    }

    public void populate(ElementCasePane elementcasepane)
    {
        if(isEditing)
        {
            isEditing = false;
            return;
        }
        if(elementcasepane == null)
            return;
        ePane = elementcasepane;
        Selection selection = ePane.getSelection();
        TemplateElementCase templateelementcase = ePane.getEditingElementCase();
        if(templateelementcase != null && (selection instanceof CellSelection))
        {
            CellSelection cellselection = (CellSelection)selection;
            Object obj = templateelementcase.getCellElement(cellselection.getColumn(), cellselection.getRow());
            if(obj == null)
            {
                obj = new DefaultTemplateCellElement(cellselection.getColumn(), cellselection.getRow());
                if(cellselection.isSelectedOneCell(elementcasepane) && cellselection.getColumn() + cellselection.getRow() == 0)
                    templateelementcase.addCellElement((TemplateCellElement)obj);
            }
            cellelement = (TemplateCellElement)obj;
            ((AbstractCellAttrPane)paneList.get(tabsHeaderIconPane.getSelectedIndex())).populateBean(cellelement, ePane);
            ((AbstractCellAttrPane)paneList.get(tabsHeaderIconPane.getSelectedIndex())).addAttributeChangeListener(listener);
        }
    }

    public String getSelectedTabName()
    {
        return ((AbstractCellAttrPane)paneList.get(tabsHeaderIconPane.getSelectedIndex())).title4PopupWindow();
    }

    public void addTitleChangeListner(TitleChangeListener titlechangelistener)
    {
        titleChangeListener = titlechangelistener;
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("CellElement-Property_Table");
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(240, 340);
    }

    private void initPaneList()
    {
        paneList = new ArrayList();
        paneList.add(new CellExpandAttrPane());
        paneList.add(new CellStylePane());
        paneList.add(new CellPresentPane());
        paneList.add(new CellOtherSetPane());
        cellAttributeProvider = ExtraDesignClassManager.getInstance().getCelllAttributeProvider();
        if(cellAttributeProvider != null)
            paneList.add((AbstractCellAttrPane)cellAttributeProvider.createCellAttributePane());
    }









}
