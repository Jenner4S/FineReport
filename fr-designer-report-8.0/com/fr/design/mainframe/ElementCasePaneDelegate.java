// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.actions.cell.*;
import com.fr.design.actions.columnrow.InsertColumnAction;
import com.fr.design.actions.columnrow.InsertRowAction;
import com.fr.design.actions.core.ActionUtils;
import com.fr.design.actions.edit.HyperlinkAction;
import com.fr.design.actions.edit.merge.MergeCellAction;
import com.fr.design.actions.edit.merge.UnmergeCellAction;
import com.fr.design.actions.utils.DeprecatedActionManager;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.cell.QuickEditorRegion;
import com.fr.design.menu.*;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;
import com.fr.general.Inter;
import com.fr.page.ReportSettingsProvider;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ArrayUtils;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe:
//            ElementCasePane, CellElementPropertyPane, AuthorityPropertyPane, JTemplate, 
//            EastRegionContainerPane

public class ElementCasePaneDelegate extends ElementCasePane
{

    public ElementCasePaneDelegate(WorkSheet worksheet)
    {
        super(worksheet);
        addSelectionChangeListener(new SelectionListener() {

            final ElementCasePaneDelegate this$0;

            public void selectionChanged(SelectionEvent selectionevent)
            {
                if(BaseUtils.isAuthorityEditing())
                {
                    AuthorityPropertyPane authoritypropertypane = new AuthorityPropertyPane(ElementCasePaneDelegate.this);
                    authoritypropertypane.populate();
                    EastRegionContainerPane.getInstance().replaceUpPane(authoritypropertypane);
                    EastRegionContainerPane.getInstance().replaceDownPane(RolesAlreadyEditedPane.getInstance());
                    return;
                }
                CellElementPropertyPane.getInstance().populate(ElementCasePaneDelegate.this);
                QuickEditorRegion.getInstance().populate(getCurrentEditor());
                JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if(jtemplate != null && !jtemplate.isUpMode())
                {
                    EastRegionContainerPane.getInstance().replaceDownPane(CellElementPropertyPane.getInstance());
                    EastRegionContainerPane.getInstance().replaceUpPane(QuickEditorRegion.getInstance());
                }
            }

            
            {
                this$0 = ElementCasePaneDelegate.this;
                super();
            }
        }
);
        addTargetModifiedListener(new TargetModifiedListener() {

            final ElementCasePaneDelegate this$0;

            public void targetModified(TargetModifiedEvent targetmodifiedevent)
            {
                CellElementPropertyPane.getInstance().populate(ElementCasePaneDelegate.this);
            }

            
            {
                this$0 = ElementCasePaneDelegate.this;
                super();
            }
        }
);
    }

    protected boolean supportRepeatedHeaderFooter()
    {
        return true;
    }

    public MenuDef[] menus4Target()
    {
        if(BaseUtils.isAuthorityEditing())
            return super.menus4Target();
        else
            return (MenuDef[])(MenuDef[])ArrayUtils.addAll(super.menus4Target(), new MenuDef[] {
                createInsertMenuDef(), createCellMenuDef()
            });
    }

    public int getMenuState()
    {
        return 0;
    }

    public boolean mustInVisibleRange()
    {
        return false;
    }

    private MenuDef createInsertMenuDef()
    {
        MenuDef menudef = new MenuDef(Inter.getLocText("M-Insert"), 'I');
        menudef.setAnchor("insert");
        menudef.addShortCut(new ShortCut[] {
            DeprecatedActionManager.getCellMenu(this)
        });
        addInsertFloatMenuDef(menudef);
        menudef.addShortCut(new ShortCut[] {
            SeparatorDef.DEFAULT
        });
        menudef.addShortCut(new ShortCut[] {
            new InsertRowAction(this)
        });
        menudef.addShortCut(new ShortCut[] {
            new InsertColumnAction(this)
        });
        return menudef;
    }

    public JPanel getEastUpPane()
    {
        QuickEditorRegion.getInstance().populate(getCurrentEditor());
        return QuickEditorRegion.getInstance();
    }

    public JPanel getEastDownPane()
    {
        CellElementPropertyPane.getInstance().populate(this);
        return CellElementPropertyPane.getInstance();
    }

    private void addInsertFloatMenuDef(MenuDef menudef)
    {
        MenuDef menudef1 = new MenuDef(KeySetUtils.INSERT_FLOAT.getMenuName());
        menudef1.setIconPath("/com/fr/design/images/m_insert/float.png");
        menudef.addShortCut(new ShortCut[] {
            menudef1
        });
        com.fr.design.actions.UpdateAction aupdateaction[] = ActionUtils.createFloatInsertAction(com/fr/design/mainframe/ElementCasePane, this);
        for(int i = 0; i < aupdateaction.length; i++)
            menudef1.addShortCut(new ShortCut[] {
                aupdateaction[i]
            });

    }

    private MenuDef createCellMenuDef()
    {
        MenuDef menudef = new MenuDef(KeySetUtils.CELL.getMenuKeySetName(), KeySetUtils.CELL.getMnemonic());
        menudef.addShortCut(new ShortCut[] {
            new CellExpandAttrAction()
        });
        menudef.addShortCut(new ShortCut[] {
            new CellWidgetAttrAction(this)
        });
        menudef.addShortCut(new ShortCut[] {
            new GlobalStyleMenuDef(this)
        });
        menudef.addShortCut(new ShortCut[] {
            new ConditionAttributesAction(this)
        });
        menudef.addShortCut(new ShortCut[] {
            DeprecatedActionManager.getPresentMenu(this)
        });
        menudef.addShortCut(new ShortCut[] {
            new HyperlinkAction(this)
        });
        menudef.addShortCut(new ShortCut[] {
            SeparatorDef.DEFAULT
        });
        menudef.addShortCut(new ShortCut[] {
            new MergeCellAction(this)
        });
        menudef.addShortCut(new ShortCut[] {
            new UnmergeCellAction(this)
        });
        menudef.addShortCut(new ShortCut[] {
            SeparatorDef.DEFAULT
        });
        menudef.addShortCut(new ShortCut[] {
            new CellAttributeAction()
        });
        return menudef;
    }

    public ReportSettingsProvider getReportSettings()
    {
        return ((WorkSheet)getTarget()).getReportSettings();
    }
}
