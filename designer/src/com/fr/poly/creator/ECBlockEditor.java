/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.creator;

import java.awt.Dimension;

import com.fr.base.BaseUtils;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.CellElementPropertyPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.EastRegionContainerPane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.cell.QuickEditorRegion;
import com.fr.grid.GridUtils;
import com.fr.poly.PolyConstants;
import com.fr.poly.PolyDesigner;
import com.fr.poly.PolyDesigner.SelectionType;
import com.fr.poly.hanlder.ColumnOperationMouseHandler;
import com.fr.poly.hanlder.RowOperationMouseHandler;
import com.fr.report.poly.PolyECBlock;
import com.fr.stable.unit.UNITConstants;
import com.fr.stable.unit.UnitRectangle;

/**
 * @author richer
 * @since 6.5.4 创建于2011-5-5 单元格类型的聚合块编辑器
 */
public class ECBlockEditor extends BlockEditor<ECBlockPane, PolyECBlock> {

	private static final int HEIGHT_MORE = 5;

	public ECBlockEditor(PolyDesigner designer, ECBlockCreator creator) {
		super(designer, creator);
	}

	@Override
	protected void initDataChangeListener() {
		editComponent.addTargetModifiedListener(new TargetModifiedListener() {

			@Override
			public void targetModified(TargetModifiedEvent e) {
				designer.fireTargetModified();
			}
		});
	}

	/**
	 * 获取当前编辑的组件
	 * 
	 * @return 聚合报表组件
	 * 
	 *
	 * @date 2014-11-24-下午3:49:12
	 * 
	 */
	public ECBlockPane createEffective() {
		PolyECBlock pcb = creator.getValue();
		if (editComponent == null) {
			editComponent = new ECBlockPane(designer, pcb, this);
		}
		if (DesignerContext.getFormatState() == DesignerContext.FORMAT_STATE_NULL) {
			editComponent.getGrid().setCursor(UIConstants.CELL_DEFAULT_CURSOR);
		}
		return editComponent;
	}

	@Override
	protected void initSize() {
		Dimension cornerSize = getCornerSize();
		PolyECBlock block = getValue();
		UnitRectangle ub = block.getBounds();
		int x = ub.x.toPixI(resolution) - cornerSize.width - designer.getHorizontalValue();
		int y = ub.y.toPixI(resolution) - cornerSize.height - designer.getVerticalValue();
		int w = ub.width.toPixI(resolution) + cornerSize.width + PolyConstants.OPERATION_SIZE
				+ UNITConstants.DELTA.toPixI(resolution);
		int h = ub.height.toPixI(resolution) + cornerSize.height + PolyConstants.OPERATION_SIZE
				+ UNITConstants.DELTA.toPixI(resolution);
		setBounds(x, y, w, h);
		editComponent.getGrid().setVerticalExtent(
				GridUtils.getExtentValue(0, block.getRowHeightList_DEC(), editComponent.getGrid().getHeight(),
						resolution));
		editComponent.getGrid().setHorizontalExtent(
				GridUtils.getExtentValue(0, block.getColumnWidthList_DEC(), editComponent.getGrid().getWidth(),
						resolution));
	}

	public void setBounds(int x, int y, int width, int height) {
		int selfheight = height + HEIGHT_MORE;
		super.setBounds(x, y, width, selfheight);
	}

	@Override
	protected Dimension getAddHeigthPreferredSize() {
		Dimension cornerSize = getCornerSize();
		cornerSize.height = PolyConstants.OPERATION_SIZE;
		return cornerSize;
	}

	@Override
	protected Dimension getAddWidthPreferredSize() {
		Dimension cornerSize = getCornerSize();
		cornerSize.width = PolyConstants.OPERATION_SIZE;
		return cornerSize;
	}

	@Override
	protected RowOperationMouseHandler createRowOperationMouseHandler() {
		return new RowOperationMouseHandler.ECBlockRowOperationMouseHandler(designer, this);
	}

	@Override
	protected ColumnOperationMouseHandler createColumnOperationMouseHandler() {
		return new ColumnOperationMouseHandler.ECBlockColumnOperationMouseHandler(designer, this);
	}

	@Override
	public Dimension getCornerSize() {
		return editComponent.getCornerSize();
	}

	/**
	 * 重置当前的选择状态, 用于更新右侧属性表
	 * 
	 *
	 * @date 2014-11-24-下午3:48:19
	 * 
	 */
	public void resetSelectionAndChooseState() {
		designer.setChooseType(SelectionType.INNER);
		if (BaseUtils.isAuthorityEditing()) {
			JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
			if (jTemplate.isJWorkBook()) {
				//清参数面板
				jTemplate.removeParameterPaneSelection();
			}
			designer.noAuthorityEdit();
			return;
		}
		QuickEditorRegion.getInstance().populate(editComponent.getCurrentEditor());
		CellElementPropertyPane.getInstance().populate(editComponent);
		EastRegionContainerPane.getInstance().replaceDownPane(CellElementPropertyPane.getInstance());
	}
}