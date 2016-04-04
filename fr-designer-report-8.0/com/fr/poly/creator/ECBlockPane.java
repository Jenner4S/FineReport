// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly.creator;

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
import com.fr.design.mainframe.*;
import com.fr.design.menu.*;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;
import com.fr.general.Inter;
import com.fr.grid.GridColumn;
import com.fr.grid.GridRow;
import com.fr.page.ReportSettingsProvider;
import com.fr.poly.PolyDesigner;
import com.fr.report.poly.PolyECBlock;
import com.fr.report.poly.PolyWorkSheet;
import com.fr.stable.ArrayUtils;
import java.awt.Dimension;

// Referenced classes of package com.fr.poly.creator:
//            PolyElementCasePane, BlockEditor, BlockCreator

public class ECBlockPane extends PolyElementCasePane
{

    private PolyDesigner designer;
    private BlockEditor be;

    public ECBlockPane(final PolyDesigner designer, PolyECBlock polyecblock, BlockEditor blockeditor)
    {
        super(polyecblock);
        this.designer = designer;
        be = blockeditor;
        setTarget(polyecblock);
        addSelectionChangeListener(new SelectionListener() {

            final PolyDesigner val$designer;
            final ECBlockPane this$0;

            public void selectionChanged(SelectionEvent selectionevent)
            {
                if(BaseUtils.isAuthorityEditing())
                {
                    if(designer.getSelection().getEditingElementCasePane() == null)
                    {
                        EastRegionContainerPane.getInstance().replaceUpPane(new NoSupportAuthorityEdit());
                        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().setAuthorityMode(false);
                        return;
                    } else
                    {
                        AuthorityPropertyPane authoritypropertypane = new AuthorityPropertyPane(designer);
                        authoritypropertypane.populate();
                        EastRegionContainerPane.getInstance().replaceUpPane(authoritypropertypane);
                        return;
                    }
                } else
                {
                    be.resetSelectionAndChooseState();
                    return;
                }
            }

            
            {
                this$0 = ECBlockPane.this;
                designer = polydesigner;
                super();
            }
        }
);
        addTargetModifiedListener(new TargetModifiedListener() {

            final ECBlockPane this$0;

            public void targetModified(TargetModifiedEvent targetmodifiedevent)
            {
                CellElementPropertyPane.getInstance().populate(ECBlockPane.this);
            }

            
            {
                this$0 = ECBlockPane.this;
                super();
            }
        }
);
    }

    public void setTarget(PolyECBlock polyecblock)
    {
        super.setTarget(polyecblock);
        be.creator.setValue(polyecblock);
    }

    public Dimension getCornerSize()
    {
        int i = getGridColumn().getPreferredSize().height;
        int j = getGridRow().getPreferredSize().width;
        return new Dimension(j, i);
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

    private MenuDef createInsertMenuDef()
    {
        MenuDef menudef = new MenuDef(Inter.getLocText("M-Insert"), 'I');
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

    private void addInsertFloatMenuDef(MenuDef menudef)
    {
        MenuDef menudef1 = new MenuDef(KeySetUtils.INSERT_FLOAT.getMenuKeySetName());
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
        return ((PolyWorkSheet)designer.getTemplateReport()).getReportSettings();
    }

    public volatile void setTarget(Object obj)
    {
        setTarget((PolyECBlock)obj);
    }

}
