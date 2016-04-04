// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.form;

import com.fr.design.actions.core.ActionUtils;
import com.fr.design.actions.form.*;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.mainframe.CellElementPropertyPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.cell.QuickEditorRegion;
import com.fr.design.menu.*;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;
import com.fr.page.ReportSettingsProvider;
import com.fr.report.worksheet.FormElementCase;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class FormElementCasePaneDelegate extends ElementCasePane
{

    public FormElementCasePaneDelegate(FormElementCase formelementcase)
    {
        super(formelementcase);
        addSelectionChangeListener(new SelectionListener() {

            final FormElementCasePaneDelegate this$0;

            public void selectionChanged(SelectionEvent selectionevent)
            {
                CellElementPropertyPane.getInstance().populate(FormElementCasePaneDelegate.this);
                QuickEditorRegion.getInstance().populate(getCurrentEditor());
            }

            
            {
                this$0 = FormElementCasePaneDelegate.this;
                super();
            }
        }
);
        addTargetModifiedListener(new TargetModifiedListener() {

            final FormElementCasePaneDelegate this$0;

            public void targetModified(TargetModifiedEvent targetmodifiedevent)
            {
                CellElementPropertyPane.getInstance().populate(FormElementCasePaneDelegate.this);
            }

            
            {
                this$0 = FormElementCasePaneDelegate.this;
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
        return new MenuDef[0];
    }

    public ToolBarDef[] toolbars4Target()
    {
        return (new ToolBarDef[] {
            createFontToolBar(), createAlignmentToolBar(), createStyleToolBar(), createCellToolBar(), createInsertToolBar(), createFrozenColumnsToolBar()
        });
    }

    public int getMenuState()
    {
        return 0;
    }

    public JComponent[] toolBarButton4Form()
    {
        return (new JComponent[] {
            formatBrush
        });
    }

    public boolean mustInVisibleRange()
    {
        return false;
    }

    public MenuDef createTemplateShortCuts()
    {
        MenuDef menudef = new MenuDef(KeySetUtils.TEMPLATE.getMenuKeySetName(), KeySetUtils.TEMPLATE.getMnemonic());
        menudef.addShortCut(new ShortCut[] {
            new FormECBackgroundAction(this)
        });
        menudef.addShortCut(new ShortCut[] {
            new FormECFrozenAction(this)
        });
        menudef.addShortCut(new ShortCut[] {
            new FormECColumnsAction(this)
        });
        return menudef;
    }

    protected ToolBarDef createInsertToolBar()
    {
        com.fr.design.actions.UpdateAction aupdateaction[] = ActionUtils.createCellInsertAction(com/fr/design/mainframe/ElementCasePane, this);
        ShortCut ashortcut[] = new ShortCut[aupdateaction.length];
        System.arraycopy(aupdateaction, 0, ashortcut, 0, aupdateaction.length);
        return ShortCut.asToolBarDef(ashortcut);
    }

    private ToolBarDef createFrozenColumnsToolBar()
    {
        return ShortCut.asToolBarDef(new ShortCut[] {
            new FormECFrozenAction(this), new FormECColumnsAction(this)
        });
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

    public ReportSettingsProvider getReportSettings()
    {
        return ((FormElementCase)getTarget()).getReportSettings();
    }
}
