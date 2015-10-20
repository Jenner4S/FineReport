package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.fun.MenuHandler;
import com.fr.design.menu.KeySetUtils;
import com.fr.general.Inter;
import com.fr.page.ReportSettingsProvider;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.DesignState;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.cell.*;
import com.fr.design.actions.core.ActionUtils;
import com.fr.design.actions.edit.HyperlinkAction;
import com.fr.design.actions.edit.merge.MergeCellAction;
import com.fr.design.actions.edit.merge.UnmergeCellAction;
import com.fr.design.actions.columnrow.InsertColumnAction;
import com.fr.design.actions.columnrow.InsertRowAction;
import com.fr.design.actions.utils.DeprecatedActionManager;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.mainframe.cell.QuickEditorRegion;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.SeparatorDef;
import com.fr.report.worksheet.WorkSheet;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;
import com.fr.stable.ArrayUtils;

import javax.swing.*;

/**
 * ��˵��: �����������Ϸ���"����" "��Ԫ��"�����б�Menuģ��.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-6-25 ����09:56:57
 */
public class ElementCasePaneDelegate extends ElementCasePane<WorkSheet> {

    public ElementCasePaneDelegate(WorkSheet sheet) {
        super(sheet);
        this.addSelectionChangeListener(new SelectionListener() {

            @Override
            public void selectionChanged(SelectionEvent e) {
                //�ڱ༭Ȩ�ޣ�����Ҫ����Ȩ�ޱ༭���
                if (BaseUtils.isAuthorityEditing()) {
                    AuthorityPropertyPane authorityPropertyPane = new AuthorityPropertyPane(ElementCasePaneDelegate.this);
                    authorityPropertyPane.populate();
                    EastRegionContainerPane.getInstance().replaceUpPane(authorityPropertyPane);
                    EastRegionContainerPane.getInstance().replaceDownPane(RolesAlreadyEditedPane.getInstance());
                    return;
                }

                CellElementPropertyPane.getInstance().populate(ElementCasePaneDelegate.this);
                QuickEditorRegion.getInstance().populate(getCurrentEditor());
                JTemplate editingTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if (editingTemplate != null && !editingTemplate.isUpMode()) {
                    EastRegionContainerPane.getInstance().replaceDownPane(CellElementPropertyPane.getInstance());
                    EastRegionContainerPane.getInstance().replaceUpPane(QuickEditorRegion.getInstance());
                }
            }
        });
        this.addTargetModifiedListener(new TargetModifiedListener() {
            @Override
            public void targetModified(TargetModifiedEvent e) {
                CellElementPropertyPane.getInstance().populate(ElementCasePaneDelegate.this);
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
        if (BaseUtils.isAuthorityEditing()) {
            return super.menus4Target();
        }
        return (MenuDef[]) ArrayUtils.addAll(super.menus4Target(), new MenuDef[]{createInsertMenuDef(), createCellMenuDef()});
    }

    public int getMenuState() {
        return DesignState.WORK_SHEET;
    }

    /**
     * �Ƿ����Ϊ�ɼ���Χ.
     *
     * @return ���Ǳ����ڿɼ���Χ.
     */
    public boolean mustInVisibleRange() {
        return false;
    }

    // ����˵�
    private MenuDef createInsertMenuDef() {
        MenuDef menuDef = new MenuDef(Inter.getLocText("M-Insert"), 'I');
        menuDef.setAnchor(MenuHandler.INSERT);
        // ��Ԫ��˵�
        menuDef.addShortCut(DeprecatedActionManager.getCellMenu(this));

        addInsertFloatMenuDef(menuDef);
        menuDef.addShortCut(SeparatorDef.DEFAULT);
        menuDef.addShortCut(new InsertRowAction(this));
        menuDef.addShortCut(new InsertColumnAction(this));
        return menuDef;
    }


    public JPanel getEastUpPane() {
        QuickEditorRegion.getInstance().populate(getCurrentEditor());
        return QuickEditorRegion.getInstance();
    }


    public JPanel getEastDownPane() {
        CellElementPropertyPane.getInstance().populate(ElementCasePaneDelegate.this);
        return CellElementPropertyPane.getInstance();
    }


    private void addInsertFloatMenuDef(MenuDef menuDef) {
        // ����Ԫ�ز˵�
        MenuDef subMenuDef = new MenuDef(KeySetUtils.INSERT_FLOAT.getMenuName());
        subMenuDef.setIconPath("/com/fr/design/images/m_insert/float.png");
        menuDef.addShortCut(subMenuDef);
        UpdateAction[] actions = ActionUtils.createFloatInsertAction(ElementCasePane.class, this);
        for (int i = 0; i < actions.length; i++) {
            subMenuDef.addShortCut(actions[i]);
        }
    }

    // ��ʽ�˵�
    private MenuDef createCellMenuDef() {
        MenuDef menuDef = new MenuDef(KeySetUtils.CELL.getMenuKeySetName(), KeySetUtils.CELL.getMnemonic());

        menuDef.addShortCut(new CellExpandAttrAction());
        menuDef.addShortCut(new CellWidgetAttrAction(this));
        menuDef.addShortCut(new GlobalStyleMenuDef(this));
        menuDef.addShortCut(new ConditionAttributesAction(this));

        // ��Ԫ����̬
        menuDef.addShortCut(DeprecatedActionManager.getPresentMenu(this));

        menuDef.addShortCut(new HyperlinkAction(this));
        menuDef.addShortCut(SeparatorDef.DEFAULT);
        menuDef.addShortCut(new MergeCellAction(this));
        menuDef.addShortCut(new UnmergeCellAction(this));
        menuDef.addShortCut(SeparatorDef.DEFAULT);
        menuDef.addShortCut(new CellAttributeAction());
        return menuDef;
    }

    @Override
    public ReportSettingsProvider getReportSettings() {
        return this.getTarget().getReportSettings();
    }

}