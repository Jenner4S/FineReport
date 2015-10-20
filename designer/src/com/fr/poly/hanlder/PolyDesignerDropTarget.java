/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.hanlder;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;

import javax.swing.JScrollBar;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.ScreenResolution;
import com.fr.base.chart.BaseChart;
import com.fr.design.mainframe.DesignerContext;
import com.fr.grid.Grid;
import com.fr.poly.PolyDesigner;
import com.fr.poly.PolyUtils;
import com.fr.poly.creator.BlockCreator;
import com.fr.poly.model.AddingData;
import com.fr.stable.unit.UnitRectangle;

/**
 * @author richer
 * @since 6.5.4 ������2011-4-1
 */
public class PolyDesignerDropTarget extends DropTargetAdapter {
	
    private static final double SCROLL_POINT = 100;
    private static final int SCROLL_DISTANCE = 15;
	
	private PolyDesigner designer;
	private AddingData addingData;
	private int resolution = ScreenResolution.getScreenResolution();
	//��ֹ�ص���ʾ����
	private BlockForbiddenWindow forbiddenWindow = new BlockForbiddenWindow();

	public PolyDesignerDropTarget(PolyDesigner designer) {
		this.designer = designer;
		new DropTarget(designer, this);
	}

	/**
	 * ��ק�����¼�
	 * 
	 * @param dtde ����¼�
	 * 
	 */
	public void dragEnter(DropTargetDragEvent dtde) {

		if (BaseUtils.isAuthorityEditing()) {
			return;
		}
		// richer:������һ����ק�������ظ�����
		if (designer.getAddingData() != null) {
			return;
		}
		try {
			Transferable tr = dtde.getTransferable();
			DataFlavor[] flavors = tr.getTransferDataFlavors();
			for (int i = 0; i < flavors.length; i++) {
				if (!tr.isDataFlavorSupported(flavors[i])) {
					continue;
				}
				Object obj = tr.getTransferData(flavors[i]);
				BlockCreator creator = null;
				if (obj instanceof Class) {
					Class clazz = (Class) obj;
					creator = PolyUtils.createCreator(clazz);
				} else if (obj instanceof BaseChart) {
					creator = PolyUtils.createCreator((BaseChart) obj);
				}
				if (creator == null) {
					return;
				}

				addingData = new AddingData(creator);
				designer.setAddingData(addingData);
			}
		} catch (Exception e) {
			FRContext.getLogger().error(e.getMessage(), e);
		}
		Point loc = dtde.getLocation();
		addingData.moveTo(loc);
		designer.repaint();
	}

	/**
	 * ��ק�����¼�
	 * 
	 * @param dtde ����¼�
	 * 
	 */
	public void dragOver(DropTargetDragEvent dtde) {
		if (addingData != null) {
			Point loc = dtde.getLocation();
			addingData.moveTo(loc);
			//����Ƿ���������ص�
			setForbiddenWindowVisibility(loc);
			//����Ƿ񵽴����
			scrollWhileDropCorner(dtde);
			
			designer.repaint();
		}
	}
	
	private void scrollWhileDropCorner(final DropTargetDragEvent dtde){
		Thread tt = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Point location = dtde.getLocation();
				//��block�����½�С�ڵ�ǰ����-SCROLL_POINTʱ, ��ʼ����
				if(location.x > designer.getWidth() - SCROLL_POINT){
					JScrollBar horizonBar = designer.getHorizontalScrollBar();
					horizonBar.setValue(horizonBar.getValue() + SCROLL_DISTANCE);
				}
				
				if(location.y> designer.getHeight() - SCROLL_POINT){
					JScrollBar verticalBar = designer.getVerticalScrollBar();
					verticalBar.setValue(verticalBar.getValue() + SCROLL_DISTANCE);
				}
				
			}
		});
		tt.start();
	}
	
	//�����Ƿ���ʾ ��ֹ����ص� ����
	private void setForbiddenWindowVisibility(Point loc){
		BlockCreator creator = addingData.getCreator();
		Rectangle pixRec = getCreatorPixRectangle(creator, loc);
		UnitRectangle rec = new UnitRectangle(pixRec, resolution);
		
		if(designer.intersectsAllBlock(rec, creator.getValue().getBlockName())){
	        int x = (int) (designer.getAreaLocationX() + pixRec.getCenterX() - designer.getHorizontalValue());
	        int y = (int) (designer.getAreaLocationY() + pixRec.getCenterY() - designer.getVerticalValue());
	        forbiddenWindow.showWindow(x, y);
		}else{
			forbiddenWindow.hideWindow();
		}
	}

	/**
	 * �����¼�
	 * 
	 * @param dtde ����¼�
	 * 
	 */
	public void drop(DropTargetDropEvent dtde) {
		if (addingData != null) {
			designer.stopAddingState();
			BlockCreator creator = addingData.getCreator();
			Point location = dtde.getLocation();
			Rectangle pixRec = getCreatorPixRectangle(creator, location);
			if(!intersectLocation(pixRec, creator)){
				return;
			}
			
			designer.addBlockCreator(creator);
			designer.stopEditing();
			designer.setSelection(creator);
			//������������ѡ��֮��Ҫ�Բ˵��͹��߽���target����������
			DesignerContext.getDesignerFrame().resetToolkitByPlus(DesignerContext.getDesignerFrame().getSelectedJTemplate());
			focusOnSelection();
			designer.fireTargetModified();
			// richer:��ק��ɺ���Ҫ��addingData����
			addingData = null;
		}
	}
	
	//�۽�ѡ�п�
	private void focusOnSelection(){
		if (designer.getSelection().getEditingElementCasePane() == null) {
			return;
		}
		Grid grid = designer.getSelection().getEditingElementCasePane().getGrid();
		if (!grid.hasFocus() && grid.isRequestFocusEnabled()) {
			grid.requestFocus();
		}
	}
	
	//����¼����creatorλ���Ƿ����ϵ��ص�, �ص�����false
	private boolean intersectLocation(Rectangle pixRec, BlockCreator creator){
		if (pixRec.getX() < 0 || pixRec.getY() < 0) {
			forbiddenWindow.hideWindow();
			designer.repaint();
			return false;
		}
		UnitRectangle rec = new UnitRectangle(pixRec, resolution);
		if(designer.intersectsAllBlock(rec, creator.getValue().getBlockName())){
			//ֹͣ�ƶ���Ҫ���ص���ֹ�ص��ı�־
			forbiddenWindow.hideWindow();
			designer.repaint();
			return false;
		}
		
		creator.getValue().setBounds(rec);
		
		return true;
	}
	
	private Rectangle getCreatorPixRectangle(BlockCreator creator, Point location){
		int width = creator.getWidth();
		int height = creator.getHeight();
		int resx = location.x - width / 2 + designer.getHorizontalValue();
		int resy = location.y - height / 2 + designer.getVerticalValue();
		return new Rectangle(resx, resy, width, height);
	}

	/**
	 * ��ק�Ƴ�ȥ�¼�
	 * 
	 * @param dte ��ק�¼�
	 * 
	 */
	public void dragExit(DropTargetEvent dte) {
		if (addingData != null) {
			addingData.reset();
			designer.repaint();
		}
		
		forbiddenWindow.hideWindow();
	}
	
}
