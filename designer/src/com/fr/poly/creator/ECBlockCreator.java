/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.creator;

import javax.swing.JComponent;

import com.fr.design.DesignState;
import com.fr.design.designer.TargetComponent;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.selection.QuickEditor;
import com.fr.poly.JPolyBlockPane;
import com.fr.poly.PolyDesigner;
import com.fr.report.poly.PolyCoreUtils;
import com.fr.report.poly.PolyECBlock;
import com.fr.stable.unit.UnitRectangle;

/**
 * @author richer
 * @since 6.5.4 ������2011-4-1
 */
public class ECBlockCreator extends BlockCreator<PolyECBlock> {
	private ECBlockEditor editor;

	public ECBlockCreator() {

	}

	public ECBlockCreator(PolyECBlock block) {
		super(block);
	}

	@Override
	protected JComponent initMonitor() {
		return new JPolyBlockPane(block);
	}

	@Override
	public PolyECBlock getValue() {
		return block;
	}

	@Override
	public PolyDesigner getDesigner() {
		return designer;
	}

	@Override
	public void setDesigner(PolyDesigner designer) {
		this.designer = designer;
	}
	
	public UnitRectangle getDefaultBlockBounds() {
		return PolyCoreUtils.getDefaultBlockBounds();
	}

	@Override
	public BlockEditor getEditor() {
		if (editor == null) {
			editor = new ECBlockEditor(designer, this);
		}
		return editor;
	}

	/**
	 * ��ⰴť״̬
	 * 
	 * @date 2015-2-5-����11:33:46
	 * 
	 */
	public void checkButtonEnable() {
		return;
	}


	@Override
	public void setValue(PolyECBlock block) {
		block.setWorksheet(designer.getTarget());
		
		cal(block);
		repaint();
	}

	/**
	 * ��ǰ����Ĺ���������
	 * 
	 * @return ����������
	 * 
	 * @date 2015-2-5-����11:32:10
	 * 
	 */
	public ToolBarDef[] toolbars4Target() {
		return editor.createEffective().toolbars4Target();
	}

	/**
	 * ��Form�Ĺ�������
	 * 
	 * @return �������
	 * 
	 * @date 2015-2-5-����11:31:46
	 * 
	 */
	public JComponent[] toolBarButton4Form() {
		return editor.createEffective().toolBarButton4Form();
	}

	/**
	 * ��ȡ��ǰ�˵�����
	 * 
	 * @return �˵�����
	 * 
	 * @date 2015-2-5-����11:29:07
	 * 
	 */
	public MenuDef[] menus4Target() {
		return editor.createEffective().menus4Target();
	}

    public  int getMenuState(){
        return DesignState.WORK_SHEET;
    }

    /**
	 * ��ȡ�˵���Ŀ�ݷ�ʽ����
	 * 
	 * @return �˵���Ŀ�ݷ�ʽ����
	 * 
	 * @date 2015-2-5-����11:27:08
	 * 
	 */
	public ShortCut[] shortcut4TemplateMenu() {
		return editor.createEffective().shortcut4TemplateMenu();
	}

	@Override
	public PolyElementCasePane getEditingElementCasePane() {
		return editor.createEffective();
	}

	@Override
	public QuickEditor getQuickEditor(TargetComponent tc) {
		return editor.createEffective().getCurrentEditor();
	}

}
