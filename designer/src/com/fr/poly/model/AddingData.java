/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.model;

import java.awt.Point;
import java.awt.event.MouseEvent;

import com.fr.poly.creator.BlockCreator;

/**
 * @author richer
 * @since 6.5.4 ������2011-4-1
 */
public class AddingData {

	private BlockCreator creator;
	// ��¼��ǰ����λ����Ϣ
	private int current_x;
	private int current_y;

	public AddingData(BlockCreator creator) {
		this.creator = creator;
		current_x = -this.creator.getWidth();
		current_y = -this.creator.getHeight();
	}
	
	// ���ص�ǰ�����ͼ��
	public void reset() {
		current_x = -this.creator.getWidth();
		current_y = -this.creator.getHeight();
	}
	
	public int getCurrentX() {
		return current_x;
	}

	public int getCurrentY() {
		return current_y;
	}

	// �ƶ����ͼ�굽����¼�������λ��
	public void moveTo(MouseEvent e) {
		current_x = e.getX() - (this.creator.getWidth() / 2);
		current_y = e.getY() - (this.creator.getHeight() / 2);
	}

	public void moveTo(int x, int y) {
		current_x = x - (this.creator.getWidth() / 2);
		current_y = y - (this.creator.getHeight() / 2);
	}
	
	public void moveTo(Point p) {
		current_x = p.x - (this.creator.getWidth() / 2);
		current_y = p.y - (this.creator.getHeight() / 2);
	}

	public BlockCreator getCreator() {
		return creator;
	}

}
