/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.creator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JComponent;

import com.fr.base.ScreenResolution;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.selection.SelectableElement;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.general.ComparatorUtils;
import com.fr.poly.PolyConstants;
import com.fr.poly.PolyDesigner;
import com.fr.report.poly.TemplateBlock;
import com.fr.stable.core.PropertyChangeAdapter;
import com.fr.stable.unit.UNITConstants;
import com.fr.stable.unit.UnitRectangle;

/**
 * @author richer
 * @since 6.5.4 ������2011-5-5 �ۺϿ�༭��
 */
public abstract class BlockCreator<T extends TemplateBlock> extends JComponent implements java.io.Serializable, SelectableElement {
	protected PolyDesigner designer;
	protected T block;

	protected int resolution = ScreenResolution.getScreenResolution();

	public BlockCreator() {

	}

	public BlockCreator(T block) {
		cal(block);
	}

	protected void cal(T block) {
		if (this.block != block) {
			this.block = block;
			block.addPropertyListener(new PropertyChangeAdapter() {

				@Override
				public void propertyChange() {
					calculateMonitorSize();
				}

				@Override
				public boolean equals(Object o) {
					return ComparatorUtils.equals(o.getClass().getName(), this.getClass().getName());
				}
			});
			this.removeAll();
			this.setLayout(FRGUIPaneFactory.createBorderLayout());
			this.add(initMonitor(), BorderLayout.CENTER);
		}
		this.calculateMonitorSize();
	}

	protected void calculateMonitorSize() {
		UnitRectangle bounds = block.getBounds();
		if (bounds == null) {
			bounds = this.getDefaultBlockBounds();
			block.setBounds(bounds);
		}
		this.setBounds(bounds.x.toPixI(resolution), bounds.y.toPixI(resolution), bounds.width.toPixI(resolution) + UNITConstants.DELTA.toPixI(resolution),
				bounds.height.toPixI(resolution) + UNITConstants.DELTA.toPixI(resolution));
		LayoutUtils.layoutContainer(this);
	}

	//Ĭ�ϴ�С, �����Ĭ��3��6��, ͼ���Ĭ��330*240
	public abstract UnitRectangle getDefaultBlockBounds();
	
	// ��ͬ��Block�в�ͬ����ʾ��
	protected abstract JComponent initMonitor();

	public abstract PolyElementCasePane getEditingElementCasePane();

	public abstract BlockEditor getEditor();


	/**
	 * ��ⰴť״̬
	 * 
	 * @date 2015-2-5-����11:33:46
	 * 
	 */
	public abstract void checkButtonEnable();

	public PolyDesigner getDesigner() {
		return designer;
	}

	public void setDesigner(PolyDesigner designer) {
		this.designer = designer;
	}

	public Rectangle getEditorBounds() {
		Rectangle bounds = this.getBounds();
		Dimension d = getEditor().getCornerSize();
		bounds.x -= d.width + designer.getHorizontalValue();
		bounds.y -= d.height + designer.getVerticalValue();
		bounds.width += d.width + PolyConstants.OPERATION_SIZE;
		bounds.height += d.height + PolyConstants.OPERATION_SIZE;
		return bounds;
	}

	public T getValue() {
		return block;
	}

	public abstract void setValue(T block);

	// /////////////////////////////////////ToolBarMenuDock//////////////


	/**
	 * ��ȡ��ǰ��������
	 * 
	 * @return ��������
	 * 
	 * @date 2015-2-5-����11:29:07
	 * 
	 */
	public abstract ToolBarDef[] toolbars4Target();

	/**
	 * ��Form�Ĺ�������
	 * 
	 * @return �������
	 * 
	 * @date 2015-2-5-����11:31:46
	 * 
	 */
	public abstract JComponent[] toolBarButton4Form();
	
	/**
	 * ��ȡ��ǰ�˵�����
	 * 
	 * @return �˵�����
	 * 
	 * @date 2015-2-5-����11:29:07
	 * 
	 */
	public abstract MenuDef[] menus4Target();

    public  abstract int  getMenuState();


    /**
	 * ��ȡ�˵���Ŀ�ݷ�ʽ����
	 * 
	 * @return �˵���Ŀ�ݷ�ʽ����
	 * 
	 * @date 2015-2-5-����11:27:08
	 * 
	 */
	public abstract ShortCut[] shortcut4TemplateMenu();

}
