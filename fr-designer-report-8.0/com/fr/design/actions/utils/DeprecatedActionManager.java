// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.utils;

import com.fr.base.BaseUtils;
import com.fr.base.present.DictPresent;
import com.fr.base.present.FormulaPresent;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.cell.NewPresentAction;
import com.fr.design.actions.columnrow.*;
import com.fr.design.actions.core.ActionUtils;
import com.fr.design.actions.edit.clear.*;
import com.fr.design.actions.edit.order.*;
import com.fr.design.constants.UIConstants;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.fun.PresentKindProvider;
import com.fr.design.gui.imenu.UIMenu;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.*;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.cellattr.BarcodePresent;
import com.fr.report.cell.cellattr.CurrencyLinePresent;
import javax.swing.JMenu;

public class DeprecatedActionManager
{

    public DeprecatedActionManager()
    {
    }

    public static UIMenu getClearMenu(ElementCasePane elementcasepane)
    {
        UIMenu uimenu = new UIMenu(Inter.getLocText("M_Edit-Clear"));
        uimenu.setIcon(UIConstants.BLACK_ICON);
        uimenu.setMnemonic('a');
        Object obj = new ClearAllAction(elementcasepane);
        uimenu.add(((ClearAction) (obj)).createMenuItem());
        obj = new ClearFormatsAction(elementcasepane);
        uimenu.add(((ClearAction) (obj)).createMenuItem());
        obj = new ClearContentsAction(elementcasepane);
        uimenu.add(((ClearAction) (obj)).createMenuItem());
        JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if(jtemplate.isJWorkBook())
        {
            ClearWidgetAction clearwidgetaction = new ClearWidgetAction(elementcasepane);
            uimenu.add(clearwidgetaction.createMenuItem());
        }
        return uimenu;
    }

    public static JMenu getOrderMenu(ElementCasePane elementcasepane)
    {
        JMenu jmenu = new JMenu(Inter.getLocText("M_Edit-Order"));
        jmenu.setIcon(UIConstants.BLACK_ICON);
        jmenu.setMnemonic('O');
        jmenu.setBackground(UIConstants.NORMAL_BACKGROUND);
        jmenu.add((new BringFloatElementToFrontAction(elementcasepane)).createMenuItem());
        jmenu.add((new SendFloatElementToBackAction(elementcasepane)).createMenuItem());
        jmenu.add((new BringFloatElementForwardAction(elementcasepane)).createMenuItem());
        jmenu.add((new SendFloatElementBackwardAction(elementcasepane)).createMenuItem());
        return jmenu;
    }

    public static MenuDef getCellMenu(ElementCasePane elementcasepane)
    {
        MenuDef menudef = new MenuDef(KeySetUtils.CELL_ELEMENT.getMenuName());
        menudef.setIconPath("/com/fr/design/images/m_insert/cell.png");
        com.fr.design.actions.UpdateAction aupdateaction[] = ActionUtils.createCellInsertAction(com/fr/design/mainframe/ElementCasePane, elementcasepane);
        for(int i = 0; i < aupdateaction.length; i++)
            menudef.addShortCut(new ShortCut[] {
                aupdateaction[i]
            });

        elementcasepane.addSelectionChangeListener(new SelectionListener(elementcasepane, menudef) {

            final ElementCasePane val$ePane;
            final MenuDef val$subMenuDef;

            public void selectionChanged(SelectionEvent selectionevent)
            {
                Selection selection = ePane.getSelection();
                if(selection instanceof CellSelection)
                    subMenuDef.setEnabled(true);
                else
                    subMenuDef.setEnabled(false);
            }

            
            {
                ePane = elementcasepane;
                subMenuDef = menudef;
                super();
            }
        }
);
        return menudef;
    }

    public static MenuDef getPresentMenu(ElementCasePane elementcasepane)
    {
        MenuDef menudef = new MenuDef(KeySetUtils.PRESENT.getMenuKeySetName());
        menudef.setIconPath("com/fr/design/images/data/source/dataDictionary.png");
        menudef.setMnemonic(KeySetUtils.PRESENT.getMnemonic());
        NewPresentAction newpresentaction = new NewPresentAction(elementcasepane, Inter.getLocText("M_Format-Data_Map"), com/fr/base/present/DictPresent.getName());
        newpresentaction.setMnemonic('D');
        NewPresentAction newpresentaction1 = new NewPresentAction(elementcasepane, Inter.getLocText("Present-Formula_Present"), com/fr/base/present/FormulaPresent.getName());
        newpresentaction1.setMnemonic('F');
        NewPresentAction newpresentaction2 = new NewPresentAction(elementcasepane, Inter.getLocText("M_Insert-Barcode"), com/fr/report/cell/cellattr/BarcodePresent.getName());
        newpresentaction2.setMnemonic('B');
        NewPresentAction newpresentaction3 = new NewPresentAction(elementcasepane, Inter.getLocText("Currency_Line"), com/fr/report/cell/cellattr/CurrencyLinePresent.getName());
        newpresentaction3.setMnemonic('L');
        NewPresentAction newpresentaction4 = new NewPresentAction(elementcasepane, Inter.getLocText("Present-No_Present"), "NOPRESENT");
        newpresentaction4.setMnemonic('N');
        menudef.addShortCut(new ShortCut[] {
            newpresentaction
        });
        menudef.addShortCut(new ShortCut[] {
            newpresentaction1
        });
        menudef.addShortCut(new ShortCut[] {
            newpresentaction2
        });
        menudef.addShortCut(new ShortCut[] {
            newpresentaction3
        });
        PresentKindProvider apresentkindprovider[] = ExtraDesignClassManager.getInstance().getPresentKindProviders();
        PresentKindProvider apresentkindprovider1[] = apresentkindprovider;
        int i = apresentkindprovider1.length;
        for(int j = 0; j < i; j++)
        {
            PresentKindProvider presentkindprovider = apresentkindprovider1[j];
            NewPresentAction newpresentaction5 = new NewPresentAction(elementcasepane, presentkindprovider.title(), presentkindprovider.kindOfPresent().getName());
            newpresentaction5.setMnemonic(presentkindprovider.mnemonic());
            menudef.addShortCut(new ShortCut[] {
                newpresentaction5
            });
        }

        menudef.addShortCut(new ShortCut[] {
            newpresentaction4
        });
        elementcasepane.addSelectionChangeListener(new SelectionListener(elementcasepane, menudef) {

            final ElementCasePane val$ePane;
            final MenuDef val$presentMenu;

            public void selectionChanged(SelectionEvent selectionevent)
            {
                Selection selection = ePane.getSelection();
                if(selection instanceof CellSelection)
                    presentMenu.setEnabled(true);
                else
                    presentMenu.setEnabled(false);
            }

            
            {
                ePane = elementcasepane;
                presentMenu = menudef;
                super();
            }
        }
);
        return menudef;
    }

    public static UIMenu getDeleteMenu(ElementCasePane elementcasepane)
    {
        UIMenu uimenu = new UIMenu(Inter.getLocText("M_Edit-Delete"));
        uimenu.setIcon(BaseUtils.readIcon("/com/fr/design/images/control/remove.png"));
        uimenu.setMnemonic('d');
        uimenu.add((new DeleteRowAction(elementcasepane)).createMenuItem());
        uimenu.add((new DeleteColumnAction(elementcasepane)).createMenuItem());
        return uimenu;
    }

    public static UIMenu getInsertMenu(ElementCasePane elementcasepane)
    {
        UIMenu uimenu = new UIMenu((new StringBuilder()).append(Inter.getLocText("Insert")).append("(I)").toString());
        uimenu.setIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/add.png"));
        uimenu.setMnemonic('i');
        uimenu.add((new InsertRowAction(elementcasepane)).createMenuItem());
        uimenu.add((new InsertColumnAction(elementcasepane)).createMenuItem());
        return uimenu;
    }
}
