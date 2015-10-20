/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.creator;

import com.fr.base.BaseUtils;
import com.fr.design.menu.KeySetUtils;
import com.fr.general.Inter;
import com.fr.page.ReportSettingsProvider;
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
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.AuthorityPropertyPane;
import com.fr.design.mainframe.CellElementPropertyPane;
import com.fr.design.mainframe.EastRegionContainerPane;
import com.fr.design.DesignState;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.SeparatorDef;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.poly.PolyDesigner;
import com.fr.report.poly.PolyECBlock;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;
import com.fr.stable.ArrayUtils;
import com.fr.design.mainframe.NoSupportAuthorityEdit;

import java.awt.*;

/**
 * @author richer
 * @since 6.5.4 ������2011-4-2 �ۺϱ�������༭��
 */
public class ECBlockPane extends PolyElementCasePane {
    private PolyDesigner designer;
    private BlockEditor be;

    public ECBlockPane(final PolyDesigner designer, PolyECBlock block, BlockEditor be) {
        super(block);
        this.designer = designer;
        this.be = be;
        this.setTarget(block);
        this.addSelectionChangeListener(new SelectionListener() {

            @Override
            public void selectionChanged(SelectionEvent e) {
                if (BaseUtils.isAuthorityEditing()) {
                    if (designer.getSelection().getEditingElementCasePane() == null) {
                        EastRegionContainerPane.getInstance().replaceUpPane(new NoSupportAuthorityEdit());
                        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().setAuthorityMode(false);
                        return;
                    }
                    AuthorityPropertyPane authorityPropertyPane = new AuthorityPropertyPane(designer);
                    authorityPropertyPane.populate();
                    EastRegionContainerPane.getInstance().replaceUpPane(authorityPropertyPane);
                    return;
                }

                ECBlockPane.this.be.resetSelectionAndChooseState();
            }
        });
        this.addTargetModifiedListener(new TargetModifiedListener() {

            @Override
            public void targetModified(TargetModifiedEvent e) {
                // kunsnat: û���ҵ��������,bug 35286 ��ͼ��ۺϴ�������populate, ���½����ֻص���һ��. ������.
//				ECBlockPane.this.be.resetSelectionAndChooseState();
            	// bug65880
            	// �ۺϱ���Ԫ��������չ��ʱ��û�д�������ͨ�����д���
            	CellElementPropertyPane.getInstance().populate(ECBlockPane.this);
            }
        });
    }

    @Override
    public void setTarget(PolyECBlock block) {
        super.setTarget(block);

        be.creator.setValue(block);
        // this.be.initSize();
        // ComponentUtils.layoutContainer(be);
    }

    public Dimension getCornerSize() {
        int h = getGridColumn().getPreferredSize().height;
        int w = getGridRow().getPreferredSize().width;
        return new Dimension(w, h);
    }

    /**
     * Ŀ���Menu
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


    // ����˵�
    private MenuDef createInsertMenuDef() {
        MenuDef menuDef = new MenuDef(Inter.getLocText("M-Insert"), 'I');
        // ��Ԫ��˵�
        menuDef.addShortCut(DeprecatedActionManager.getCellMenu(this));

        addInsertFloatMenuDef(menuDef);
        menuDef.addShortCut(SeparatorDef.DEFAULT);
        menuDef.addShortCut(new InsertRowAction(this));
        menuDef.addShortCut(new InsertColumnAction(this));
        return menuDef;
    }

    private void addInsertFloatMenuDef(MenuDef menuDef) {
        // ����Ԫ�ز˵�
        MenuDef subMenuDef = new MenuDef(KeySetUtils.INSERT_FLOAT.getMenuKeySetName());
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
        return designer.getTemplateReport().getReportSettings();
    }

}
