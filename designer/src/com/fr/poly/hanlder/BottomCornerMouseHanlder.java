/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.hanlder;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import com.fr.base.BaseUtils;
import com.fr.base.ScreenResolution;
import com.fr.design.beans.location.Absorptionline;
import com.fr.design.beans.location.MoveUtils;
import com.fr.design.beans.location.MoveUtils.RectangleDesigner;
import com.fr.design.beans.location.MoveUtils.RectangleIterator;
import com.fr.general.ComparatorUtils;
import com.fr.poly.PolyDesigner;
import com.fr.poly.PolyDesigner.SelectionType;
import com.fr.poly.creator.BlockEditor;
import com.fr.report.poly.TemplateBlock;
import com.fr.stable.ArrayUtils;
import com.fr.stable.unit.UnitRectangle;

/**
 * @author richer
 * @since 6.5.4 ������2011-4-11
 */
public class BottomCornerMouseHanlder extends MouseInputAdapter {
	private PolyDesigner designer;
	private BlockEditor editor;
	private Point pressed;
	private UnitRectangle oldBounds;
	private int resolution = ScreenResolution.getScreenResolution();

	public BottomCornerMouseHanlder(PolyDesigner designer, BlockEditor editor) {
		this.designer = designer;
		this.editor = editor;
	}

	/**
	 * ��갴���¼�
	 * 
	 * @param e ����¼�
	 * 
	 * @date 2015-2-12-����2:32:04
	 * 
	 */
	public void mousePressed(MouseEvent e) {
		if (BaseUtils.isAuthorityEditing()) {
			designer.noAuthorityEdit();
		}
		pressed = e.getPoint();
		oldBounds = editor.getValue().getBounds();
		designer.setChooseType(SelectionType.BLOCK);
	}

	/**
	 * ����ͷ��¼�
	 * 
	 * @param e ����¼�
	 * 
	 * @date 2015-2-12-����2:32:04
	 * 
	 */
	public void mouseReleased(MouseEvent e) {
		if (BaseUtils.isAuthorityEditing()) {
			designer.noAuthorityEdit();
		}
		editor.setDragging(false);

		if(designer.intersectsAllBlock(editor.getValue())){
			//����ص�
			editor.getValue().setBounds(oldBounds);
			return;
		}
		
		if (!ComparatorUtils.equals(editor.getValue().getBounds(), oldBounds)) {
			designer.fireTargetModified();
		}
		designer.repaint();
	}

	/**
	 * �����ק�¼�
	 * 
	 * @param e ����¼�
	 * 
	 * @date 2015-2-12-����2:32:04
	 * 
	 */
	public void mouseDragged(MouseEvent e) {
		if (BaseUtils.isAuthorityEditing()) {
			designer.noAuthorityEdit();
			return;
		}
		editor.setDragging(true);
		Point dragStart = e.getPoint();
		dragStart.x -= pressed.x;
		dragStart.y -= pressed.y;
		TemplateBlock block = editor.getValue();

		Rectangle bounds = block.getBounds().toRectangle(resolution);
		Point resultPoint = MoveUtils.sorption(bounds.x + dragStart.x < 0 ? 0 : bounds.x + dragStart.x, bounds.y
				+ dragStart.y < 0 ? 0 : bounds.y + dragStart.y, bounds.width, bounds.height, rectDesigner);
		block.setBounds(new UnitRectangle(new Rectangle(resultPoint.x, resultPoint.y, bounds.width, bounds.height),
				resolution));
		designer.repaint();
	}
	
	private RectangleDesigner rectDesigner = new RectangleDesigner() {
		@Override
		public void setXAbsorptionline(Absorptionline line) {
			editor.setXAbsorptionline(line);
		}

		@Override
		public void setYAbsorptionline(Absorptionline line) {
			editor.setYAbsorptionline(line);
		}

		@Override
		public RectangleIterator createRectangleIterator() {
			return getRectangleIt();
		}
		
		/**
		 * ��ȡ��ǰѡ�п�Ĵ�ֱ������
		 * 
		 * @return ��Ĵ�ֱ������
		 * 
		 */
		public int[] getVerticalLine(){
			return editor.getValue().getVerticalLine();
		}
		
		/**
		 * ��ȡ��ǰѡ�п��ˮƽ������
		 * 
		 * @return ���ˮƽ������
		 * 
		 */
		public int[] getHorizontalLine(){
			return editor.getValue().getHorizontalLine();
		}

	};
	
	private RectangleIterator getRectangleIt(){
		return new RectangleIterator() {
			private int i;
	
			@Override
			public boolean hasNext() {
				//�Ƿ���
				boolean isOverFlow = i >= designer.getTarget().getBlockCount();
				if(isOverFlow){
					return false;
				}
				
				//�Ƿ�Ϊ��ǰ�Ѿ�ѡ�еĿ�
				boolean isSelf = designer.getTarget().getBlock(i) == editor.getValue();
				if(!isSelf){
					return true;
				}
				
				//���������, ���ж�һ��һ����
				boolean isNextOverFlow = ++i < designer.getTarget().getBlockCount();
				return isNextOverFlow;
			}
			
			public int[] getHorizontalLine(){
				//��Ϊȡnext��ʱ���Ѿ�i++��, ������߻�ȡ���ֵҪ-1
				TemplateBlock block = (TemplateBlock) designer.getTarget().getBlock(i - 1);
				if(block == null){
					return ArrayUtils.EMPTY_INT_ARRAY;
				}
				
				return block.getHorizontalLine();
			}
			
			public int[] getVerticalLine(){
				TemplateBlock block = (TemplateBlock) designer.getTarget().getBlock(i - 1);
				if(block == null){
					return ArrayUtils.EMPTY_INT_ARRAY;
				}
				
				return block.getVerticalLine();
			}
			
			@Override
			public Rectangle nextRectangle() {
				UnitRectangle UnitBounds = designer.getTarget().getBlock(i++).getBounds();
				return UnitBounds.toRectangle(resolution);
			}
		};
	}
}
