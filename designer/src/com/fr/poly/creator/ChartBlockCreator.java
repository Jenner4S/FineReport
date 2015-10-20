/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.creator;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import com.fr.design.DesignState;
import com.fr.design.designer.TargetComponent;
import com.fr.design.gui.chart.MiddleChartComponent;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.selection.QuickEditor;
import com.fr.quickeditor.ChartQuickEditor;
import com.fr.report.poly.PolyChartBlock;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.UNIT;
import com.fr.stable.unit.UnitRectangle;

/**
 * @author richer
 * @since 6.5.4 ������2011-5-10
 */
public class ChartBlockCreator extends BlockCreator<PolyChartBlock> {
	private MiddleChartComponent cpm;
	private ChartBlockEditor editor;
	
	//ͼ��Ĭ�Ͽ��330*240
	private static final UNIT DEFAULT_WIDTH = FU.getInstance(12573000);
	private static final UNIT DEFAULT_HEIGHT = FU.getInstance(9144000);

	public ChartBlockCreator() {

	}

	public ChartBlockCreator(PolyChartBlock block) {
		super(block);
	}

	/**
	 * ��ʼ��
	 * @return ��ʼ���Ŀؼ�.
	 */
	public JComponent initMonitor() {
		cpm = DesignModuleFactory.getChartComponent(getValue().getChartCollection());
		cpm.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		return cpm;
	}
	
	public UnitRectangle getDefaultBlockBounds() {
		return new UnitRectangle(UNIT.ZERO, UNIT.ZERO, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	@Override
	public BlockEditor getEditor() {
		if (editor == null) {
			editor = new ChartBlockEditor(designer, this);
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
		if (editor == null) {
			editor = new ChartBlockEditor(designer, this);
		}
		editor.checkChartButtonsEnable();
	}

	@Override
	public PolyChartBlock getValue() {
		return block;
	}

	@Override
	public void setValue(PolyChartBlock block) {
		this.block = block;
		cpm.populate(this.block.getChartCollection());
	}



	/**
	 * ��ȡ��ǰ��������
	 * 
	 * @return ��������
	 * 
	 * @date 2015-2-5-����11:29:07
	 * 
	 */
	public ToolBarDef[] toolbars4Target() {
		return new ToolBarDef[0];
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
		return new JComponent[0];
	}

	/**
	 * Ŀ����б�
	 * @return �����б�.
	 */
	public MenuDef[] menus4Target() {
		return new MenuDef[0];
	}

    public  int getMenuState(){
        return DesignState.POLY_SHEET;
    }

	/**
	 * ģ���Menu
	 * @return ģ���menu
	 */
	public ShortCut[] shortcut4TemplateMenu() {
		return new ShortCut[0];
	}

	@Override
	public PolyElementCasePane getEditingElementCasePane() {
		return null;
	}

	@Override
	public QuickEditor getQuickEditor(TargetComponent tc) {
		ChartQuickEditor quitEditor = ChartQuickEditor.getInstance();
		quitEditor.populate(tc);
		return quitEditor;
	}
}
