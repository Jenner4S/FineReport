package com.fr.design.mainframe.form;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.fr.design.DesignState;
import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.core.ActionUtils;
import com.fr.design.actions.form.FormECBackgroundAction;
import com.fr.design.actions.form.FormECColumnsAction;
import com.fr.design.actions.form.FormECFrozenAction;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.mainframe.CellElementPropertyPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.cell.QuickEditorRegion;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.page.ReportSettingsProvider;
import com.fr.report.worksheet.FormElementCase;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;

/**
 */
public class FormElementCasePaneDelegate extends ElementCasePane<FormElementCase>{

	public FormElementCasePaneDelegate(FormElementCase sheet) {
        super(sheet);
        this.addSelectionChangeListener(new SelectionListener() {
            @Override
            public void selectionChanged(SelectionEvent e) {
                CellElementPropertyPane.getInstance().populate(FormElementCasePaneDelegate.this);
                QuickEditorRegion.getInstance().populate(getCurrentEditor());
            }
        });
        this.addTargetModifiedListener(new TargetModifiedListener() {
            @Override
            public void targetModified(TargetModifiedEvent e) {
                CellElementPropertyPane.getInstance().populate(FormElementCasePaneDelegate.this);
            }
        });
    }

    @Override
    protected boolean supportRepeatedHeaderFooter() {
        return true;
    }

    /**
     * ѡ��Ŀ��� ��ӦMenu
     *
     * @return ����MenuDef����.
     */
    public MenuDef[] menus4Target() {
        return new MenuDef[0];
    }

    /**
     * ����鹤���� ��ݼ��˵���ť ������ҳ����
     *
     * @return ���ع���������.
     */
    public ToolBarDef[] toolbars4Target() {
        return new ToolBarDef[]{
                createFontToolBar(),
                createAlignmentToolBar(),
                createStyleToolBar(),
                createCellToolBar(),
                createInsertToolBar(),
                createFrozenColumnsToolBar()};
    }

    public int getMenuState() {
        return DesignState.WORK_SHEET;
    }

    /**
     * ���ı����Ĺ��߰�ť
     * @return ���߰�ť
     */
    public JComponent[] toolBarButton4Form() {
        return new JComponent[]{
                formatBrush };
    }

    /**
     * �Ƿ����Ϊ�ɼ���Χ.
     *
     * @return ���Ǳ����ڿɼ���Χ.
     */
    public boolean mustInVisibleRange() {
        return false;
    }

    /**
     * ģ��˵�
     * @return   ģ��˵�
     */
    public MenuDef createTemplateShortCuts() {
        MenuDef menuDef = new MenuDef(KeySetUtils.TEMPLATE.getMenuKeySetName(), KeySetUtils.TEMPLATE.getMnemonic());
        menuDef.addShortCut(new FormECBackgroundAction(this));
        menuDef.addShortCut(new FormECFrozenAction(this));
        menuDef.addShortCut(new FormECColumnsAction(this));
        return menuDef;
    }

    protected ToolBarDef createInsertToolBar() {
        UpdateAction[] cellInsertActions = ActionUtils.createCellInsertAction(ElementCasePane.class, this);
        ShortCut[] shortCuts = new ShortCut[cellInsertActions.length];
        System.arraycopy(cellInsertActions, 0, shortCuts, 0, cellInsertActions.length);
        return ShortCut.asToolBarDef(shortCuts);
    }

    private ToolBarDef createFrozenColumnsToolBar() {
       return ShortCut.asToolBarDef(new ShortCut[]{
               new FormECFrozenAction(this),
               new FormECColumnsAction(this),});
    }



    public JPanel getEastUpPane() {
        QuickEditorRegion.getInstance().populate(getCurrentEditor());
        return QuickEditorRegion.getInstance();
    }


    public JPanel getEastDownPane() {
        CellElementPropertyPane.getInstance().populate(FormElementCasePaneDelegate.this);
        return CellElementPropertyPane.getInstance();
    }


    @Override
    public ReportSettingsProvider getReportSettings() {
        return this.getTarget().getReportSettings();
    }

}